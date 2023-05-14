package me.xx2bab.buildinaction.slacklazy

import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.*

abstract class ProviderTestPlugin : Plugin<Project> {
    override fun apply(project: Project) {

        val taskProviderA = project.tasks.register(
            "testProviderA",
            TaskA::class.java
        ) {
            initialParam = "Raw Param"
            metadata.set(project.layout.buildDirectory.file("outputs/logs/productA.txt"))
        }

        val taskProviderB = project.tasks.register(
            "testProviderB",
            TaskB::class.java
        ) {
            // You can use map{} as well to satisfy the same purpose,
            // because metadata has been marked as OutputFile of a task.
            // metadata.set(taskProviderA.map { it.metadata.get() })
            metadata.set(taskProviderA.flatMap { it.metadata })
        }

//        val taskProviderC =
        project.tasks.register(
            "testProviderC",
            TaskC::class.java
        ) {
            // If use map{} here it brings the dependencies of Task B,
            // while flatmap only brings dependencies from metadata itself which means Task A,
            // because A produces metadata provider.

            // Execute C with below will see A->B->C
            // metadata.set(taskProviderB.map { it.metadata.get() })

            // While execute C with below will see A->C
            metadata.set(taskProviderB.flatMap { it.metadata })
        }

//        val taskProviderD =
        project.tasks.register(
            "testProviderD",
            TaskD::class.java
        ) {
            // A->D
//            param.set(taskProviderA.map { "Inject ${it.initialParam} for TaskD!"})

            // Just D
            param.set(taskProviderA.flatMap { project.provider { "Inject ${it.initialParam} for TaskD!" } })
        }

//        val taskProviderE =
        project.tasks.register(
            "testProviderE",
            TaskE::class.java
        ) {
            param.set(taskProviderA.map { it.initialParam })
        }
    }

    abstract class TaskA : DefaultTask() {
        @get:Input
        var initialParam: String = ""

        @get:OutputFile
        abstract val metadata: RegularFileProperty

        @TaskAction
        fun write() {
            metadata.get().asFile.writeText("ProductA plus $initialParam")
            logger.lifecycle("TaskA writes something to the metadata file.")
        }
    }

    abstract class TaskB : DefaultTask() {
        @get:InputFile
        abstract val metadata: RegularFileProperty

        @TaskAction
        fun print() {
            logger.lifecycle("TaskB reads ${metadata.get().asFile.readText()}.")
        }
    }

    abstract class TaskC : DefaultTask() {
        @get:InputFile
        abstract val metadata: RegularFileProperty

        @TaskAction
        fun print() {
            logger.lifecycle("TaskC reads ${metadata.get().asFile.readText()}.")
        }
    }

    abstract class TaskD : DefaultTask() {
        @get:Input
        abstract val param: Property<String>

        @TaskAction
        fun print() {
            logger.lifecycle(param.get())
        }
    }

    abstract class TaskE : DefaultTask() {
        @get:Input
        abstract val param: Property<String>

        @Internal
        val selfIntro: Provider<String> = param.map { "This is TaskE which consumes $it from A." }

        @TaskAction
        fun print() {
            logger.lifecycle(selfIntro.get())
        }
    }
}