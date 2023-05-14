import com.android.build.api.artifact.MultipleArtifact
import com.android.build.api.variant.*
import com.android.build.gradle.internal.crash.afterEvaluate
import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.*
import org.gradle.kotlin.dsl.register
import java.io.File

class ResourceAPIInteractionAppPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val appExtension = project.extensions.getByType(
            ApplicationAndroidComponentsExtension::class.java
        )

        //================== 1. Resource SourceSet ==================
        appExtension.finalizeDsl { appExt ->
            with(appExt) {
                sourceSets {
                    getByName("main") {
                        res {  // Android Resource
                            srcDir("keys/res") // Add one more
                            // srcDirs() // Add more than one
                            // setSrcDirs() // Override
                        }
                        resources { // Java Resource
                            // DO NOT take it as Android Res.
                        }
                    }
                }
            }
        }
        appExtension.onVariants { variant ->
            val variantCapitalizedName = variant.name.capitalize()
            afterEvaluate {
                project.tasks.named("pre${variantCapitalizedName}Build").configure {
                    doFirst {
                        // Download files and put into the /keys/res folder
                    }
                }
            }
        }


        //================== 2. Manifest PlaceHolder/Merge Report ==================
        appExtension.onVariants(
            appExtension.selector()
                .withBuildType("debug")
                .withFlavor(Pair("server", "production"))
        ) { variant ->
            variant.manifestPlaceholders.put("hostName", "pre-live.bar.com")

            val variantCapitalizedName = variant.name.capitalize()
            val flavorSuffix = if (variant.flavorName == null) "" else "-${variant.flavorName}"
            val buildTypeSuffix = if (variant.buildType == null) "" else "-${variant.buildType}"
            val mergeReport = project.layout.buildDirectory
                .file("outputs/logs/manifest-merger${flavorSuffix}${buildTypeSuffix}-report.txt")
            project.tasks.register<GetManifestMergeReportTask>(
                "get${variantCapitalizedName}ManifestMergeReport"
            ) {
                report.set(mergeReport)
            }
        }


        //================== 3. Resource Value PlaceHolder ==================
        appExtension.finalizeDsl { appExt ->
            with(appExt) {
                buildTypes {
                    getByName("debug") {
                        resValue("string", "app_token1", "123")
                    }
                }
            }
        }
        appExtension.onVariants(
            appExtension.selector()
                .withBuildType("debug")
                .withFlavor(Pair("server", "production"))
        ) { variant ->
            // val key = ResValueKeyImpl("string", "app_token")
            val key = variant.makeResValueKey(type = "string", name = "app_token")
            val value = ResValue(value = "1234", comment = "")
            variant.resValues.put(key, value)
        }


        //================== 4. APK Value PlaceHolder ==================
        appExtension.onVariants { variant ->
            val variantCapitalizedName = variant.name.capitalize()

            val genAssetTaskProvider = project.tasks.register<GenAssetTask>(
                "genAssetTask${variantCapitalizedName}"
            ) {
                additionalKeyFileProp.set(project.file("app_key.txt"))
            }

            val addAssetTaskProvider = project.tasks.register<AddAssetTask>(
                "addAssetTask${variantCapitalizedName}"
            ) {
                additionalKeyFileProp.set(
                    genAssetTaskProvider.flatMap { it.additionalKeyFileProp })
            }
            variant.artifacts
                .use(addAssetTaskProvider)
                .wiredWith(AddAssetTask::outputDirectoryProp)
                .toAppendTo(MultipleArtifact.ASSETS)
        }
    }



    abstract class GetManifestMergeReportTask : DefaultTask() {
        @get:InputFile
        abstract val report: RegularFileProperty

        @TaskAction
        fun afterMerge() {
            println(report.get().asFile.readText())
            // Upload it...
        }
    }

    abstract class GenAssetTask: DefaultTask() {

        @get:OutputFiles
        abstract val additionalKeyFileProp: RegularFileProperty

        @TaskAction
        fun gen() {
            additionalKeyFileProp.get().asFile
                .apply {
                    createNewFile()
                    writeText("app-key")
                }

        }
    }

    abstract class AddAssetTask : DefaultTask() {

        @get:InputFile
        abstract val additionalKeyFileProp: RegularFileProperty

        @get:OutputFiles
        abstract val outputDirectoryProp: DirectoryProperty

        @TaskAction
        fun addAsset() {
            val keyFile = additionalKeyFileProp.get().asFile
            val assetDir = outputDirectoryProp.get().asFile
            assetDir.mkdirs()
            keyFile.copyTo(File(assetDir, keyFile.name))
        }
    }


}
