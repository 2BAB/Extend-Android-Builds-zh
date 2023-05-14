package me.xx2bab.extendagp.buildsrc

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class NoInputTask : DefaultTask() {

    @get:OutputFile
    var final: File? = null

    @TaskAction
    fun writeFile() {
        final?.writeText("Dummy Text")
    }

}