package me.xx2bab.extendagp.buildsrc

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectories
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.OutputFiles
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.getByType
import java.io.File
import java.util.ServiceLoader.Provider

abstract class TaskEssentialsTask : DefaultTask() {

    /*Inputs*/
    @get:Input
    var inputString: String = ""

    @get:InputFile
    var inputFile: File? = null

    @get:InputFiles
    var inputFiles: List<File>? = null

    @get:InputFiles
    abstract val inputFileCollection: ConfigurableFileCollection

    // This is not supported by Gradle
    //    @get:InputFiles
    //    var inputFileMap: Map<String, File>? = null

    @get:InputDirectory
    var inputDir: File? = null


    /*Outputs*/
    @get:OutputFile
    var outputFile: File? = null

    @get:OutputFiles
    var outputFiles: List<File>? = null

    @get:OutputFiles
    abstract val outputFileCollection: ConfigurableFileCollection

    @get:OutputFiles
    var outputFileMap: Map<String, File>? = null

    @get:OutputDirectory
    var outputDir: File? = null

    @get:OutputDirectories
    abstract val outputDirCollection: ConfigurableFileCollection

    @get:OutputDirectories
    var outputDirMap: Map<String, File>? = null

    @TaskAction
    fun apply() {
        inputFile!!.copyTo(outputFile!!)

        inputFiles!!.forEachIndexed { i, file ->
            file.copyTo(outputFiles!![i])
        }

        val outputCollection = outputFileCollection.files.toList().sorted()
        inputFileCollection.files.toList().sorted().forEachIndexed { i, file ->
            file.copyTo(outputCollection[i])

            // Because the `inputFileMap` is not supported by Gradle as inputs,
            // we simply put the outputFileMap copy-logic here to validate the result.
            file.copyTo(outputFileMap!![i.toString()]!!)
        }

        inputDir!!.copyRecursively(outputDir!!)

        val outputDirCollection = outputDirCollection.files.toList()
        outputDirCollection.forEach {
            inputDir!!.copyRecursively(it)
        }

        for (i in 0 until outputDirMap!!.size) {
            inputDir!!.copyRecursively(outputDirMap!![i.toString()]!!)
        }
    }
}