import com.android.build.api.artifact.SingleArtifact
import com.android.build.api.artifact.MultipleArtifact
import com.android.build.api.variant.*
import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*
import org.gradle.kotlin.dsl.register
import java.io.File

class VariantV2AdvancedPlugin : Plugin<Project> {

    class RST: VariantExtension

    override fun apply(project: Project) {
        val androidExtension =
            project.extensions.getByType(ApplicationAndroidComponentsExtension::class.java)
val abc = DslExtension.Builder(RST::class.simpleName!!).build()
        androidExtension.registerExtension(abc) { variantExtConfig ->
            variantExtConfig.variant
            RST()
        }
        androidExtension.onVariants { variant ->
            val mainOutput: VariantOutput = variant.outputs.single {
                it.outputType == VariantOutputConfiguration.OutputType.SINGLE
            }
            val variantCapitalizedName = variant.name.capitalize()

            // Example 1
            val renameApkTask: TaskProvider<RenameApkFile> = project.tasks.register<RenameApkFile>(
                "rename${variantCapitalizedName}Apk"
            ) {
                val apkFolderProvider = variant.artifacts.get(SingleArtifact.APK)
                this.outApk.set(File(apkFolderProvider.get().asFile, "custom-name-${variant.name}.apk"))
                this.apkFolder.set(apkFolderProvider)
                this.builtArtifactsLoader.set(variant.artifacts.getBuiltArtifactsLoader())
            }

            // Example 2
            val postUpdateTask = project.tasks.register<ManifestAfterMergeTask>(
                "postUpdate${variantCapitalizedName}Manifest")
            variant.artifacts
                .use(postUpdateTask)
                .wiredWithFiles(
                    ManifestAfterMergeTask::mergedManifest,
                    ManifestAfterMergeTask::updatedManifest
                )
                .toTransform(SingleArtifact.MERGED_MANIFEST)

            val getManifestTask = project.tasks.register<GetManifestTask>(
                "getManifest$variantCapitalizedName") {
                updatedManifest.set(variant.artifacts.get(SingleArtifact.MERGED_MANIFEST))
            }
            renameApkTask.configure { dependsOn(getManifestTask) }


            // Example 3
            val addAssetTask = project.tasks.register<AddAssetTask>(
                "AddAssetTask${variantCapitalizedName}") {
                additionalAsset.set(project.file("app_key.txt"))
            }
            variant.artifacts.use(addAssetTask)
                .wiredWith(AddAssetTask::outputDirectory)
                .toAppendTo(MultipleArtifact.ASSETS)

            // Example 4
            val notificationTaskProvider: TaskProvider<NotificationTask> = project.tasks.register<NotificationTask>(
                "notify${variantCapitalizedName}Build") {
                title.set("${project.name} apk is built successfully.")
                releaseNote.set(renameApkTask.map {
                    val size = it.outApk.get().asFile.length() / 1024.0 / 1024.0
                    "Apk - $size MB"
                })
            }


            val appKey = project.objects.fileProperty()
            appKey.set(project.file("app_key.txt"))
            val keyTask1 = project.tasks.register<TestTaskA>("keytask11${variantCapitalizedName}") {
                keyFileProvider.set(appKey)
            }
            val keyTask2 = project.tasks.register<TestTaskB>("keytask22${variantCapitalizedName}") {
                val a = keyTask1.map { println("task1 map")
                    appKey.get() }
                keyFile.set(a)
            }


        }
    }

    abstract class ManifestAfterMergeTask : DefaultTask() {

        @get:InputFile
        abstract val mergedManifest: RegularFileProperty

        @get:OutputFile
        abstract val updatedManifest: RegularFileProperty

        @TaskAction
        fun afterMerge() {
            val modifiedManifest = mergedManifest.get().asFile.readText()
                .replace("allowBackup=\"true\"", "allowBackup=\"false\"")
            updatedManifest.get().asFile.writeText(modifiedManifest)
        }

    }

    abstract class GetManifestTask : DefaultTask() {

        @get:InputFile
        abstract val updatedManifest: RegularFileProperty

        @TaskAction
        fun afterMerge() {
            println(updatedManifest.get().asFile.readText())
        }

    }

    abstract class RenameApkFile : DefaultTask() {

        @get:InputFiles
        abstract val apkFolder: DirectoryProperty

        @get:Internal
        abstract val builtArtifactsLoader: Property<BuiltArtifactsLoader>

        @get:OutputFile
        abstract val outApk: RegularFileProperty

        @TaskAction
        fun taskAction() {
            val builtArtifacts = builtArtifactsLoader.get().load(apkFolder.get())
                ?: throw RuntimeException("Cannot load APKs")
            File(builtArtifacts.elements.single().outputFile)
                .copyTo(outApk.get().asFile)
        }
    }

    abstract class AddAssetTask: DefaultTask() {

        @get:InputFile
        abstract val additionalAsset: RegularFileProperty

        @get:OutputFiles
        abstract val outputDirectory: DirectoryProperty

        @TaskAction
        fun addAsset() {
            val target = additionalAsset.get().asFile
            val assetDir = outputDirectory.get().asFile
            assetDir.mkdirs()
            target.copyTo(File(assetDir, target.name))
        }

    }

    abstract class NotificationTask : DefaultTask() {

        @get:Input
        abstract val title: Property<String>

        @get:Input
        abstract val releaseNote: Property<String>

        @TaskAction
        fun taskAction() {
            val message = "${title.get()}\n${releaseNote.get()}"
            val channel = "123456789"
            NotificationClient().send(message, channel)
        }

    }


    abstract class TestTaskA : DefaultTask() {

        @get:InputFile
        abstract val keyFileProvider: RegularFileProperty

        @TaskAction
        fun afterMerge() {
            println("TestTaskA:" + keyFileProvider.get().asFile.readText())
            keyFileProvider.get().asFile.writeText("1234")
        }

    }

    abstract class TestTaskB : DefaultTask() {

        @get:InputFile
        abstract val keyFile: RegularFileProperty

        @TaskAction
        fun afterMerge() {
            println("TestTaskA:" + keyFile.get().asFile.readText())
        }

    }




    class NotificationClient {
        fun send(message: String, channel: String) {
            println("NotificationTask: sending \"$message\" to channel $channel")
        }
    }
}