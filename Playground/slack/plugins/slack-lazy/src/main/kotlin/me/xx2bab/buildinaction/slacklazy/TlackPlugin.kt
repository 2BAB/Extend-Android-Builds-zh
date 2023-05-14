package me.xx2bab.buildinaction.slacklazy

import com.android.build.api.artifact.SingleArtifact
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.api.variant.BuiltArtifactsLoader
import com.android.build.gradle.AppPlugin
import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*
import org.gradle.kotlin.dsl.withType
import java.io.File
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

@Suppress("UnstableApiUsage")
abstract class TlackPlugin : Plugin<Project> {

    private val slackNotificationPluginApplied = AtomicBoolean(false)

    override fun apply(project: Project) {
        project.afterEvaluate {
            check(slackNotificationPluginApplied.get()) {
                "Tlack plugin should only be applied to a Slack Plugin enabled project " + "but ${project.displayName} doesn't have the 'me.2bab.buildinaction.slack-lazy' plugin applied."
            }
        }

        project.plugins.withType<AppPlugin> {
            slackNotificationPluginApplied.set(true)

            // Main logic starts from here.
            val androidExtension = project.extensions.getByType(ApplicationAndroidComponentsExtension::class.java)
            androidExtension.onVariants { appVariant ->
                // 1. Prepare task arguments.
                val artifactsLoader = appVariant.artifacts.getBuiltArtifactsLoader()
                val apkDir = appVariant.artifacts.get(SingleArtifact.APK)

                // 2. Task registry.
                val taskName = "collectArtifactsMetaData${appVariant.name.capitalize(Locale.ENGLISH)}"
                val metadataName = "artifacts-metadata-${appVariant.name}.csv"
                val tlackTaskProvider = project.tasks.register(
                    taskName, CollectArtifactsMetaDataTask::class.java
                ) {
                    builtArtifactsLoader.set(artifactsLoader)
                    apkFolder.set(apkDir)
                    metadata.set(project.layout.buildDirectory.file("outputs/logs/$metadataName"))
                }

                // 3. Wire Tlack's Output with Slack's Input
                val slackVariantAwareTaskName = "assembleAndNotify${appVariant.name.capitalize(Locale.ENGLISH)}"
                val slackTaskProvider =
                    project.tasks.named(slackVariantAwareTaskName) as TaskProvider<SlackNotificationTask>
                slackTaskProvider.configure {
                    message.set(tlackTaskProvider.map { it.metadata.get().asFile.readText() })
                }
            }
        }
    }

    abstract class CollectArtifactsMetaDataTask : DefaultTask() {

        @get:Internal
        abstract val builtArtifactsLoader: Property<BuiltArtifactsLoader>

        @get:InputFiles
        @get:PathSensitive(PathSensitivity.RELATIVE)
        abstract val apkFolder: DirectoryProperty

        @get:OutputFile
        abstract val metadata: RegularFileProperty

        @TaskAction
        fun collect() {
            val builtArtifacts =
                builtArtifactsLoader.get().load(apkFolder.get()) ?: throw RuntimeException("Cannot load APKs")
            val allArtifactPaths = builtArtifacts.elements
                .map { File(it.outputFile) }
                .joinToString("\r\n") { "${it.name}, ${prettifyFileSize(it.length())}" }
            metadata.get().asFile.writeText(allArtifactPaths)
        }

        private fun prettifyFileSize(fileSize: Long): String {
            val fileSizeInMB = fileSize * 1.0 / 1024 / 1024
            val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.FLOOR
            return df.format(fileSizeInMB) + " MB"
        }
    }

}