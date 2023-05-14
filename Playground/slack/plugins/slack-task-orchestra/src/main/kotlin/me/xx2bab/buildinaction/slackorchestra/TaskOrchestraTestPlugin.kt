package me.xx2bab.buildinaction.slackorchestra

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.internal.tasks.factory.dependsOn
import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.withType
import java.net.URI


abstract class TaskOrchestraTestPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.plugins.withType<AppPlugin> {
            val privateCloudService = project.gradle.sharedServices.registerIfAbsent(
                "PrivateCloudService",
                PrivateCloudService::class.java
            ) {
                this.parameters.username.set("username")
                this.parameters.password.set("password")
            }

            val androidExtension = project.extensions.findByType(AppExtension::class.java)!!
            androidExtension.applicationVariants.configureEach {
                val variantName = this.name.capitalize()
                val taskProviderA = project.tasks.register(
                    "testTaskA$variantName",
                    TaskA::class.java
                )
                val taskProviderB = project.tasks.register(
                    "testTaskB$variantName",
                    TaskB::class.java
                )

                val taskProviderC = project.tasks.register(
                    "testTaskC$variantName",
                    TaskC::class.java
                ) {
                    this.mustRunAfter(taskProviderB)
                }
                // If you want to make C first, can test it out using below code
                // (remember to disable the above rule or it throws "Circular dependency between the following tasks")
                // taskProviderB.configure { this.mustRunAfter(taskProviderC) }

                val taskProviderD = project.tasks.register(
                    "testTaskD$variantName",
                    TaskD::class.java
                )

                val taskProviderE = project.tasks.register(
                    "testTaskE$variantName",
                    TaskE::class.java
                )


                val taskProviderGG = project.tasks.register(
                    "testTaskGG$variantName",
                    TaskE::class.java
                )
                val taskProviderGGG = project.tasks.register(
                    "testTaskGGG$variantName",
                    TaskE::class.java
                )
                val taskProviderGGGG = project.tasks.register(
                    "testTaskGGGG$variantName",
                    TaskE::class.java
                )





                val taskProviderF = project.tasks.register(
                    "testTaskF$variantName",
                    TaskF::class.java
                ) {
                    cloudService.set(privateCloudService)
                }

                val taskProviderG = project.tasks.register(
                    "testTaskG$variantName",
                    TaskG::class.java
                ) {
                    cloudService.set(privateCloudService)
                }

                val powerAssemble = project.tasks.register("powerAssemble$variantName")

                ///////////////////////////// In the middle

                // Add on Forward task
                taskProviderA.dependsOn(taskProviderB)

                // Add on Following task
                taskProviderD.dependsOn(taskProviderA)
                assembleProvider.dependsOn(taskProviderD)

                // Adjust parallel task order
                // Check this line above when registered the TaskC ->  this.mustRunAfter(taskProviderB)
                taskProviderA.dependsOn(taskProviderC)

                // Add to the head
                preBuildProvider.dependsOn(taskProviderE)

                // Add to the tail - single task + before assemble
//                assembleProvider.dependsOn(taskProviderF)
//                taskProviderF.dependsOn(packageApplicationProvide)

                // Add to the tail - single task + after assemble
//                taskProviderF.dependsOn(assembleProvider)

                // Add to the tail - multi tasks
                // PowerAssemble is a lifecycle task, which makes it easy for u to add
                // unlimited tasks between assemble and powerAssemble.
                // Expandable and maintainable.
                taskProviderF.dependsOn(assembleProvider)
                taskProviderG.dependsOn(taskProviderF)
                powerAssemble.dependsOn(taskProviderG)
                powerAssemble.dependsOn(taskProviderGG)
                powerAssemble.dependsOn(taskProviderGGG)
                powerAssemble.dependsOn(taskProviderGGGG)

                // finalizedBy(...)
                val taskProviderFinalizer = project.tasks.register(
                    "testTaskFinalizer$variantName",
                    TaskFinalizer::class.java
                )
                assembleProvider.configure { finalizedBy(taskProviderFinalizer) }
            }
        }
    }
}

abstract class TaskA : DefaultTask() {
    @TaskAction
    fun print() {
        Thread.sleep(2000)
        logger.lifecycle("Task A is running.")
    }
}

abstract class TaskB : DefaultTask() {
    @TaskAction
    fun print() {
        Thread.sleep(2000)
        logger.lifecycle("Task B is running.")
    }
}

abstract class TaskC : DefaultTask() {
    @TaskAction
    fun print() {
        Thread.sleep(2000)
        logger.lifecycle("Task C is running.")
    }
}

abstract class TaskD : DefaultTask() {
    @TaskAction
    fun print() {
        Thread.sleep(2000)
        logger.lifecycle("Task D is running.")
    }
}

abstract class TaskE : DefaultTask() {
    @TaskAction
    fun print() {
        Thread.sleep(2000)
        logger.lifecycle("Task E is running.")
    }
}

abstract class TaskF : DefaultTask() {
    @get:Internal
    abstract val cloudService: Property<PrivateCloudService>

    @TaskAction
    fun print() {
        Thread.sleep(2000)
        logger.lifecycle("Task F is running.")
        cloudService.get()
            .fetch(URI.create("/security/se_img.webp"))
        // ...
    }
}

abstract class TaskG : DefaultTask() {
    @get:Internal
    abstract val cloudService: Property<PrivateCloudService>

    @TaskAction
    fun print() {
        Thread.sleep(2000)
        logger.lifecycle("Task G is running.")
        val channels = cloudService.get()
            .fetch(URI.create("/config/notification_channels.csv"))
        logger.lifecycle("Task G fetched channels: $channels")
        // ...
    }
}

abstract class TaskFinalizer : DefaultTask() {
    @TaskAction
    fun print() {
        logger.lifecycle("Task Finalizer is running.")
    }
}

