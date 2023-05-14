package me.xx2bab.buildinaction.slacktest.base

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import java.io.File
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue
import kotlin.math.max
import kotlin.random.Random

object GradleRunnerExecutor {

    val versions = DepVersions()
    val envProps = EnvProperties()

    private fun initTestResources(): File {
        val sampleAppDir =
            File("build/test-samples/", "test-sample-${System.currentTimeMillis()}-${Random.nextInt(1024, 2048)}")
        File("src/fixtures/test-sample").copyRecursively(sampleAppDir)
        return sampleAppDir
    }

    fun execute(config: String, vararg tasks: String) = execute(config, false, *tasks)

    fun executeExpectingFailure(config: String, vararg tasks: String) = execute(config, true, *tasks)

    private fun execute(
        config: String,
        expectFailure: Boolean,
        vararg tasks: String,
    ): ExecuteResult {
        val sampleAppDir = initTestResources()

        // language=kotlin
        File(sampleAppDir, "settings.gradle.kts").appendText(
            """
            pluginManagement {
                plugins {
                    id("com.android.application") version "${versions.getVersion("agpVer")}" apply false
                    id ("me.2bab.buildinaction.slack-test") version "1.0" apply false
                }
                repositories {
                    gradlePluginPortal()
                    google() 
                    mavenCentral()
                }
            }
        """
        )

        // language=gradle
        File(sampleAppDir, "app/build.gradle.kts").appendText(
            """
            $config
        """
        )

        return executeGradle(sampleAppDir, expectFailure) {
            withArguments(*tasks)
        }
    }

    private fun executeGradle(
        sampleAppDir: File,
        expectFailure: Boolean,
        block: GradleRunner.() -> Unit,
    ): ExecuteResult = runWithTestDir { testDir ->
        val runner = GradleRunner.create()
            .withPluginClasspath()
            .withProjectDir(sampleAppDir)
            .withTestKitDir(testDir)
            .apply(block)

        if (!expectFailure) {
            runner.withArguments(runner.arguments.toMutableList() + "-s") // stacktrace
        }

        val result:BuildResult = if (expectFailure) runner.buildAndFail() else runner.build()

        if ("--debug" !in runner.arguments) {
            println(result.output)
        }

        ExecuteResult(
            sampleAppDir,
            result
        )
    }


    private val testDirPool: BlockingQueue<File>

    /**
     * @see https://github.com/Triple-T/gradle-play-publisher/blob/master/play/plugin/src/test/kotlin/com/github/triplet/gradle/play/helpers/IntegrationTestBase.kt
     */
    init {
        // Each test kit Gradle runner must start its own daemon which takes a significant
        // amount of time. Thus, we only want a small amount of concurrency. Furthermore,
        // having multiple test kits running in parallel without using different directories is
        // useless as they will all fight to acquire file locks, but initializing those
        // directories also takes some time.
        //
        // To balance all these tradeoffs, we generate a persistent, random set of test kit
        // directories and pool them for concurrent use by test threads.

        val random = Random(System.getProperty("user.name").hashCode())
        val tempDir = System.getProperty("java.io.tmpdir")

        val threads = max(1, Runtime.getRuntime().availableProcessors() / 4 - 1)
        val dirs = mutableListOf<File>()
        repeat(threads) {
            dirs.add(File("$tempDir/gppGradleTests${random.nextInt()}"))
        }

        testDirPool = ArrayBlockingQueue(threads, false, dirs)
    }

    private fun <T> runWithTestDir(block: (File) -> T): T {
        val dir = testDirPool.take()
        try {
            return block(dir)
        } finally {
            testDirPool.put(dir)
        }
    }


}

data class ExecuteResult(
    val projectDir: File,
    val buildResult: BuildResult
)

