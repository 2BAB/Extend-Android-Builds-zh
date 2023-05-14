import com.android.build.api.artifact.ScopedArtifact
import com.android.build.api.variant.*
import ins.JarOutputStreamService
import ins.TransformedClass
import me.xx2bab.bytecode.intro.ReplaceClassVisitor
import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFile
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.register
import org.gradle.workers.WorkAction
import org.gradle.workers.WorkParameters
import org.gradle.workers.WorkQueue
import org.gradle.workers.WorkerExecutor
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import javax.inject.Inject

private fun InputStream.transformBytecode(): ByteArray {
    val reader = ClassReader(this)
    val writer = ClassWriter(reader, ClassWriter.COMPUTE_FRAMES)
    val visitor = ReplaceClassVisitor(writer)
    reader.accept(visitor, ClassReader.EXPAND_FRAMES)
    return writer.toByteArray()
}

private fun String.isInstrumentable(): Boolean {
    return this.startsWith("me.xx2bab.bytecode.intro").not()
}

class VariantAPIPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val androidExtension =
            project.extensions.getByType(ApplicationAndroidComponentsExtension::class.java)
        androidExtension.onVariants { variant ->
            val variantCapitalizedName = variant.name.capitalize()
            val runInParallel = true // Change to switch between parallel and sequential execution
            if (runInParallel) {
                // Run in parallel (Do not enable 2 tasks at the same time)
                val jarOutputStreamService = project.gradle
                    .sharedServices
                    .registerIfAbsent(
                        "JarOutputStreamServiceFor${variantCapitalizedName}",
                        JarOutputStreamService::class.java
                    ) {}
                val modifyClassesInParallelTaskProvider =
                    project.tasks.register<ModifyClassesInParallelTask>(
                        "modify${variantCapitalizedName}ClassesInParallel"
                    ) {
                        internalJarOutputStreamServiceProp.set(jarOutputStreamService)
                    }
                variant.artifacts
                    .forScope(ScopedArtifacts.Scope.ALL)
                    .use(modifyClassesInParallelTaskProvider)
                    .toTransform(
                        ScopedArtifact.CLASSES,
                        ModifyClassesInParallelTask::allJars,
                        ModifyClassesInParallelTask::allDirectories,
                        ModifyClassesInParallelTask::output
                    )
            } else {
                // Run in sequence (Do not enable 2 tasks at the same time)
                val modifyClassesTaskProvider = project.tasks.register<ModifyClassesTask>(
                    "modify${variantCapitalizedName}Classes"
                )
                variant.artifacts
                    .forScope(ScopedArtifacts.Scope.ALL)
                    .use(modifyClassesTaskProvider)
                    .toTransform(
                        ScopedArtifact.CLASSES,
                        ModifyClassesTask::allJars,
                        ModifyClassesTask::allDirectories,
                        ModifyClassesTask::output
                    )

                val modifyClassesTaskProvider2 = project.tasks.register<ModifyClassesTask>(
                    "modify${variantCapitalizedName}Classes2"
                )
                variant.artifacts
                    .forScope(ScopedArtifacts.Scope.ALL)
                    .use(modifyClassesTaskProvider2)
                    .toTransform(
                        ScopedArtifact.CLASSES,
                        ModifyClassesTask::allJars,
                        ModifyClassesTask::allDirectories,
                        ModifyClassesTask::output
                    )
            }
        }
    }
}


@CacheableTask
internal abstract class ModifyClassesTask : DefaultTask() {

//    @get:Internal
//    lateinit var objectFactory: ObjectFactory

    @get:InputFiles
    @get:PathSensitive(PathSensitivity.ABSOLUTE)
    abstract val allJars: ListProperty<RegularFile>

//    @InputFiles
//    @PathSensitive(PathSensitivity.ABSOLUTE)
//    @Incremental
//    fun getAllJarsInFileCollection(): FileCollection {
//        val fc = objectFactory.fileCollection()
//        fc.from(allJars)
//        return fc
//    }

    @get:InputFiles
    @get:PathSensitive(PathSensitivity.ABSOLUTE)
    abstract val allDirectories: ListProperty<Directory>

//    @InputFiles
//    @PathSensitive(PathSensitivity.ABSOLUTE)
//    @Incremental
//    fun getAllDirectoriesInFileCollection(): FileCollection {
//        val fc = objectFactory.fileCollection()
//        fc.from(allDirectories)
//        return fc
//    }

    @get:OutputFile
    abstract val output: RegularFileProperty


    @TaskAction
    fun taskAction() {
//        inputChanges: InputChanges
//        println("ModifyClassesTask -> isIncremental ${inputChanges.isIncremental}")
        println("ModifyClassesTask -> allJars ${
            allJars.get().joinToString(";\n") { it.asFile.absolutePath }
        }.")
        println("ModifyClassesTask -> allDirs ${
            allDirectories.get().joinToString(";\n") { it.asFile.absolutePath }
        }.")
        println("ModifyClassesTask -> out ${output.asFile.get().absolutePath}.")

        val start = System.currentTimeMillis()

        // The allJars and output are the same file, which may cause the reading/writing conflict,
        // and breaks the Gradle cache mechanism, so we need to create a temp file to store the
        // transformed bytecode during processing, and then move it to the output file after all.
        // https://issuetracker.google.com/issues/276431893
        // This is just a workaround, and we need AGP to provide a better solution.
        val expectOutput = output.get().asFile
        val tmpOutput = File(expectOutput.parentFile, expectOutput.name + "-tmp")
                .also { it.createNewFile() }

        val jarOutput = JarOutputStream(
            BufferedOutputStream(
                FileOutputStream(tmpOutput)
            )
        )
        allJars.get().forEach { inputFile ->
            // logger.lifecycle("handling jar: " + inputFile.asFile.absolutePath)
            val inputJarFile = JarFile(inputFile.asFile)
            val entries = inputJarFile.entries()
                .asSequence()
                .filter { !it.isDirectory && !it.name.startsWith("META-INF") }
            entries.forEach { inputJarEntry ->
                // logger.lifecycle("Adding from jar ${inputJarEntry.name}")
                jarOutput.putNextEntry(JarEntry(inputJarEntry.name))
                inputJarFile.getInputStream(inputJarEntry).use {
                    if (inputJarFile.name.endsWith(".class")
                        && inputJarFile.name.isInstrumentable()
                    ) {
                        jarOutput.write(it.transformBytecode())
                    } else { // .kotlin_module or other files
                        it.copyTo(jarOutput)
                    }
                }
                jarOutput.closeEntry()
            }
            inputJarFile.close()
        }
        allDirectories.get().forEach { dir ->
            // logger.lifecycle("handling dir: " + directory.asFile.absolutePath)
            val filteredFiles = dir.asFile
                .walk()
                .filter { it.isFile }
            filteredFiles.forEach { inputFile ->
                val relativePath = dir.asFile
                    .toURI()
                    .relativize(inputFile.toURI())
                    .path
                    .replace(File.separatorChar, '/')
                // logger.lifecycle("Adding from directory $relativePath")
                jarOutput.putNextEntry(JarEntry(relativePath))
                inputFile.inputStream().use {
                    if (inputFile.extension == "class"
                        && inputFile.name.isInstrumentable()
                    ) {
                        jarOutput.write(it.transformBytecode())
                    } else {
                        it.copyTo(jarOutput)
                    }
                }
                jarOutput.closeEntry()
            }
        }
        jarOutput.close()

        if (expectOutput.exists()) {
            expectOutput.delete()
        }
        tmpOutput.copyTo(expectOutput, overwrite = true)

        val end = System.currentTimeMillis()
        logger.lifecycle("ModifyClassesTask cost: ${end - start} ms")
    }

}


@CacheableTask
internal abstract class ModifyClassesInParallelTask @Inject constructor(
    private val workerExecutor: WorkerExecutor
) : DefaultTask() {

    @get:InputFiles
    @get:PathSensitive(PathSensitivity.ABSOLUTE)
    abstract val allJars: ListProperty<RegularFile>

    @get:InputFiles
    @get:PathSensitive(PathSensitivity.ABSOLUTE)
    abstract val allDirectories: ListProperty<Directory>

    @get:OutputFile
    abstract val output: RegularFileProperty

    @get:Internal
    abstract val internalJarOutputStreamServiceProp: Property<JarOutputStreamService>

    @TaskAction
    fun taskAction() {
        val start = System.currentTimeMillis()

        logger.lifecycle("ModifyClassesTask output: ${output.get().asFile.absolutePath}")
        val workQueue: WorkQueue = workerExecutor.noIsolation()

        var jobCount = 0
        allJars.get().forEach { jarFile ->
            jobCount += JarFile(jarFile.asFile).entries().asSequence()
                .filter { !it.isDirectory && !it.name.startsWith("META-INF") }
                .toList()
                .size
            workQueue.submit(JarTransformWork::class.java) {
                jarOutputStreamServiceProp.set(internalJarOutputStreamServiceProp)
                inputJarProp.set(jarFile)
            }
        }
        allDirectories.get().forEach { classDirectory ->
            jobCount += classDirectory.asFile.walk().filter { it.isFile }.toList().size
            workQueue.submit(DirTransformWork::class.java) {
                jarOutputStreamServiceProp.set(internalJarOutputStreamServiceProp)
                inputDirProp.set(classDirectory)
            }
        }
        workQueue.submit(JarOutputWork::class.java) {
            jarOutputStreamServiceProp.set(internalJarOutputStreamServiceProp)
            outputFileProp.set(output)
            jobCountProp.set(jobCount)
        }

        workQueue.await()

        val end = System.currentTimeMillis()
        logger.lifecycle("ModifyClassesTask cost: ${end - start} ms")
    }

}

private interface JarTransformWorkParameters : WorkParameters {
    val jarOutputStreamServiceProp: Property<JarOutputStreamService>
    val inputJarProp: RegularFileProperty
}

private abstract class JarTransformWork : WorkAction<JarTransformWorkParameters> {

    override fun execute() {
        // Parameters preparation
        val jarOutputStreamService = parameters.jarOutputStreamServiceProp.get()
        val inputJar = parameters.inputJarProp.get().asFile

        // Main logic
        // logger.lifecycle("handling jar: " + inputJar.absolutePath)
        val inputJarFile = JarFile(inputJar)
        val entries = inputJarFile.entries()
            .asSequence()
            .filter { !it.isDirectory && !it.name.startsWith("META-INF") }
        entries.forEach { inputJarEntry ->
            // logger.lifecycle("Adding from jar ${inputJarEntry.name}")
            inputJarFile.getInputStream(inputJarEntry).use {
                if (inputJarFile.name.endsWith(".class")
                    && inputJarFile.name.isInstrumentable()
                ) {
                    val bytecode = it.transformBytecode()
                    jarOutputStreamService.offer(TransformedClass(inputJarEntry.name, bytecode))
                } else { // .kotlin_module or other files
                    val bytecode = it.readAllBytes()
                    jarOutputStreamService.offer(TransformedClass(inputJarEntry.name, bytecode))
                }
            }
        }
        inputJarFile.close()
    }

}

private interface DirTransformWorkParameters : WorkParameters {
    val jarOutputStreamServiceProp: Property<JarOutputStreamService>
    val inputDirProp: DirectoryProperty
}

private abstract class DirTransformWork : WorkAction<DirTransformWorkParameters> {

    override fun execute() {
        // Parameters preparation
        val jarOutputStreamService = parameters.jarOutputStreamServiceProp.get()
        val inputDir = parameters.inputDirProp.get().asFile

        // Main logic
        // logger.lifecycle("handling dir: " + inputDir.absolutePath)
        val filteredFiles = inputDir.walk()
            .filter { it.isFile }
        filteredFiles.forEach { inputFile ->
            val relativePath = inputDir.toURI()
                .relativize(inputFile.toURI())
                .path
                .replace(File.separatorChar, '/')
            // logger.lifecycle("Adding from directory $relativePath")
            inputFile.inputStream().use {
                if (inputFile.extension == "class" && inputFile.name.isInstrumentable()) {
                    val bytecode = it.transformBytecode()
                    jarOutputStreamService.offer(TransformedClass(relativePath, bytecode))
                } else {
                    val bytecode = it.readAllBytes()
                    jarOutputStreamService.offer(TransformedClass(relativePath, bytecode))
                }
            }
        }
    }

}


private interface JarOutputWorkParameters : WorkParameters {
    val jarOutputStreamServiceProp: Property<JarOutputStreamService>
    val outputFileProp: RegularFileProperty
    val jobCountProp: Property<Int>
}

private abstract class JarOutputWork : WorkAction<JarOutputWorkParameters> {

    override fun execute() {
        val jarOutputStreamService = parameters.jarOutputStreamServiceProp.get()
        val outputFile = parameters.outputFileProp.get().asFile
        val jobCount = parameters.jobCountProp.get()
        jarOutputStreamService.startWithJobCountLimit(outputFile, jobCount)
    }

}