import com.android.build.api.variant.*
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.DefaultTask
import org.gradle.api.file.FileSystemLocation
import com.android.build.gradle.tasks.MergeResources
import com.android.build.gradle.internal.DependencyResourcesComputer
import com.android.build.gradle.internal.publishing.AndroidArtifacts
import com.android.build.api.variant.impl.ApplicationVariantImpl
import com.android.build.api.component.impl.ApkCreationConfigImpl
import org.gradle.api.provider.Provider
import org.gradle.api.provider.SetProperty
import org.gradle.api.tasks.*
import java.io.File

class VariantV2PolyfillPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val androidExtension =
            project.extensions.findByType(ApplicationAndroidComponentsExtension::class.java)!!

        androidExtension.onVariants { variant ->
            val variantCapitalizedName = variant.name.capitalize()
            val apkCreationConfigImpl = (variant as ApplicationVariantImpl).delegate


            // Example 1: to collect all input manifests
            val preUpdateTask = project.tasks.register(
                "preUpdate${variantCapitalizedName}Manifest",
                ManifestBeforeMergeTask::class.java
            ) {
                inputManifests.set(obtainInputManifests(apkCreationConfigImpl, project))
            }

            project.afterEvaluate {
                project.tasks.named("process${variantCapitalizedName}MainManifest")
                    .apply { configure { dependsOn(preUpdateTask) } }
            }
            project.rootProject.subprojects {
                if (this !== project) {
                    this.tasks.whenTaskAdded {
                        val newTask = this
                        if (newTask.name == "process${variantCapitalizedName}Manifest"
                            || newTask.name == "extractDeepLinks${variantCapitalizedName}"
                        ) {
                            preUpdateTask.configure {
                                this.dependsOn(newTask)
                            }
                        }
                    }
                }
            }


            // Example 2: to collect all input resources
            val preUpdateResourceTask = project.tasks.register(
                "preUpdate${variantCapitalizedName}Resources",
                ResourceBeforeMergeTask::class.java
            ) {
               //  val resource = obtainInputResources(apkCreationConfigImpl, project)
//                inputResources.set(resource)
            }

            project.afterEvaluate {
                val mergeTask = apkCreationConfigImpl.config.taskContainer.mergeResourcesTask.get()
                mergeTask.dependsOn(preUpdateResourceTask)
            }
        }
    }

    // ProcessApplicationManifest#configure(...)
    private fun obtainInputManifests(
        apkCreationConfigImpl: ApkCreationConfigImpl,
        project: Project
    ): Provider<Set<FileSystemLocation>> = apkCreationConfigImpl.config
        .variantDependencies
        .getArtifactCollection(
            AndroidArtifacts.ConsumedConfigType.RUNTIME_CLASSPATH,
            AndroidArtifacts.ArtifactScope.ALL,
            AndroidArtifacts.ArtifactType.MANIFEST
        )
        .artifactFiles
        .elements


    abstract class ManifestBeforeMergeTask : DefaultTask() {

        @get:InputFiles
        abstract val inputManifests: SetProperty<FileSystemLocation>

        @TaskAction
        fun beforeMerge() {
            inputManifests.get().forEach {
                println(
                    "ManifestBeforeMergeTask received input manifest: " +
                            it.asFile.absolutePath
                )
            }
        }
    }


    // MergeResources#getConfiguredResourceSets(...)
    // It may consume eagerly if you don't put it inside the tasks.register("..."){ ... } block.
//    private fun obtainInputResources(
//        apkCreationConfigImpl: ApkCreationConfigImpl,
//        project: Project
//    ): Provider<Set<FileSystemLocation>> {
//        val mergeTask = apkCreationConfigImpl.config
//            .taskContainer
//            .mergeResourcesTask
//            .get()
//        val resourcesComputer = getField(
//            MergeResources::class.java,
//            mergeTask,
//            "resourcesComputer"
//        ) as DependencyResourcesComputer
//        val resourceSets = resourcesComputer.compute(mergeTask.processResources, null)
//        val resourceFiles = resourceSets.mapNotNull { resourceSet ->
//            val getSourceFiles = resourceSet.javaClass.methods.find {
//                it.name == "getSourceFiles" && it.parameterCount == 0
//            }
//            @Suppress("UNCHECKED_CAST")
//            getSourceFiles?.invoke(resourceSet) as? Iterable<File>
//        }.flatten()
//        return project.files(resourceFiles).elements
//    }

    abstract class ResourceBeforeMergeTask : DefaultTask() {
        @get:InputFiles
        abstract val inputResources: SetProperty<FileSystemLocation>

        @TaskAction
        fun beforeMerge() {
            inputResources.get().forEach {
                println(
                    "ResourceBeforeMergeTask received input resource file:" +
                            it.asFile.absolutePath
                )
            }
        }
    }

    private fun <T> getField(clazz: Class<T>, instance: T, fieldName: String): Any {
        val field = clazz.declaredFields.filter { it.name == fieldName }[0]
        field.isAccessible = true
        return field.get(instance) as Any
    }

}