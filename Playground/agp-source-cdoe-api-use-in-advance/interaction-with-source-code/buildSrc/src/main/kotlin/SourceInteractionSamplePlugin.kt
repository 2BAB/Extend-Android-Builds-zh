import com.android.build.api.variant.*
import com.android.build.gradle.api.ApplicationVariant
import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.tasks.*
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.register
import java.io.File


const val SOURCE_TYPE_RESTFUL_API = "restfulApi"

class SourceInteractionSamplePlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val androidComponent =
            project.extensions.getByType(ApplicationAndroidComponentsExtension::class.java)

        //================== 1.Java/Kotlin SourceSet ==================
        androidComponent.finalizeDsl { appExt ->
            appExt.sourceSets.named("staging").configure {
                java.srcDirs("src/shared1/java")
                kotlin.srcDirs("src/shared1/kotlin")
            }
            appExt.sourceSets.named("preproduction").configure {
                java.srcDirs("src/shared1/java")
                kotlin.srcDirs("src/shared1/kotlin")
                java.srcDirs("src/shared2/java")
                kotlin.srcDirs("src/shared2/kotlin")
            }
            appExt.sourceSets.named("production").configure {
                java.srcDirs("src/shared2/java")
                kotlin.srcDirs("src/shared2/kotlin")
            }
        }

        //================== 2.registerSourceType ==================
        androidComponent.registerSourceType(SOURCE_TYPE_RESTFUL_API)

        //================== 3.Code Generation ==================
        androidComponent.onVariants { variant ->
            val variantCapitalizedName = variant.name.capitalize()
            val restfulApis = variant.sources.getByName(SOURCE_TYPE_RESTFUL_API)
            val sourceGenTaskProvider = project.tasks
                .register<RestfulAPISourceGenTask>(
                    "${variant.name}AddRESTfulSources"
                ) {
                    jsonSourceDirsProp.set(restfulApis.all)
                }
            variant.sources.java?.let { java ->
                java.addGeneratedSourceDirectory(
                    sourceGenTaskProvider,
                    RestfulAPISourceGenTask::outputDirectoryProp
                )
            }
        }
    }

    @CacheableTask
    abstract class RestfulAPISourceGenTask : DefaultTask() {

        @get:InputFiles
        @get:PathSensitive(PathSensitivity.RELATIVE)
        abstract val jsonSourceDirsProp: ListProperty<Directory>

        @get:OutputDirectory
        abstract val outputDirectoryProp: DirectoryProperty

        @TaskAction
        fun generate() {
            val jsonSourceDirs = jsonSourceDirsProp.get()
            val outputDirectory = outputDirectoryProp.get().asFile
            logger.lifecycle("jsonSourceDirs: " +
                    "${jsonSourceDirs.joinToString(",") { it.asFile.absolutePath }}")

            // Some code to generate Java/Kotlin source code from json files, you can:
            // 1. leverage the Gradle Worker API to parallelize the code generation.
            // 2. leverage the JavaPoet/KotlinPoet library to build Java/Kotlin source code if it's complex.
            // Here we just create some dummy files.

            val usersListJava = File(outputDirectory, "UsersList.java")
            usersListJava.writeText("""
                package com.example;
                public class UsersList {
                    public final String relativeUrl = "/users";
                    public final String method = "GET";
                    // ...
                }
            """.trimIndent())
            // Only if the staging variant is selected will the following file be generated.
            val debugLogUploadJava = File(outputDirectory, "DebugLogUpload.java")
            debugLogUploadJava.writeText("""
                package com.example;
                public class DebugLogUpload {
                    public final String relativeUrl = "/debugLog";
                    public final String method = "POST";
                    // ...
                }
            """.trimIndent())
        }

    }

}