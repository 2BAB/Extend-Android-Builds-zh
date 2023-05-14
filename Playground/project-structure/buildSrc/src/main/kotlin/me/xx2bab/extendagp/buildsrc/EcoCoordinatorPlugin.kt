package me.xx2bab.extendagp.buildsrc

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class EcoCoordinatorPlugin: Plugin<Project> {

    override fun apply(target: Project) {
        println("The EcoCoordinatorPlugin from ./buildSrc is applied.")

        val android = target.extensions.getByType<AppExtension>()
        android.applicationVariants.configureEach {
            val variant = this
            val checkResourceTask = target.tasks.register(
                "check${variant.name.capitalize()}ResourceTask") {
                doLast {
                    println(variant.mergeResourcesProvider.get()
                        .outputDir.asFile.get().absolutePath)
                }
                this.dependsOn(variant.mergeResourcesProvider)
            }
            variant.assembleProvider.configure { dependsOn(checkResourceTask) }
        }
    }
}