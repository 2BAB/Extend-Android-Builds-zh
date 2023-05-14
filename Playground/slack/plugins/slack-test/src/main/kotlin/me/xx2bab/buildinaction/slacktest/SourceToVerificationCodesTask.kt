package me.xx2bab.buildinaction.slacktest

import me.xx2bab.buildinaction.slacktest.core.HashCalculator
import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileType
import org.gradle.api.tasks.*
import org.gradle.work.ChangeType
import org.gradle.work.Incremental
import org.gradle.work.InputChanges
import java.io.File

@CacheableTask
abstract class SourceToVerificationCodesTask : DefaultTask() {

    @get:Incremental
    @get:InputFiles
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val sources: ConfigurableFileCollection // ListProperty<Directory> is not supported for incremental detection

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @TaskAction
    fun compute(inputChanges: InputChanges) {
        val hashCalculator = HashCalculator()
        val outDir = outputDir.get().asFile

        if (inputChanges.isIncremental) {
            inputChanges.getFileChanges(sources).forEach { change ->
                if (change.fileType == FileType.DIRECTORY) return@forEach
                println("${change.changeType}: ${change.normalizedPath}")
                when (change.changeType) {
                    ChangeType.ADDED, ChangeType.MODIFIED -> {
                        processSourceFile(change.file, outDir, hashCalculator)
                    }

                    ChangeType.REMOVED -> {
                        removeTargetFile(change.file, outDir)
                    }
                }
            }
        } else {
            sources.forEach {
                it.walk().forEach { inputFile ->
                    processSourceFile(inputFile, outDir, hashCalculator)
                }
            }
        }
    }

    private fun processSourceFile(inputFile: File, outDir: File, hashCalculator: HashCalculator) {
        if (inputFile.isFile
            && (inputFile.extension == "java" || inputFile.extension == "kt")
        ) {
            val fullFileName = inputFile.fullFileName
            val sha256MD5 = File(outDir, fullFileName)
            sha256MD5.createNewFile()
            val content = inputFile.readText()
            sha256MD5.writeText(
                """
                |SHA256=${hashCalculator.sha256(content)}
                |MD5=${hashCalculator.md5(content)}
                """.trimMargin()
            )
        }
    }

    private fun removeTargetFile(inputFile: File, outDir: File) {
        if (inputFile.extension == "java" || inputFile.extension == "kt") {
            val fullFileName = inputFile.fullFileName
            val sha256MD5 = File(outDir, fullFileName)
            sha256MD5.delete()
        }
    }

    private val File.fullFileName: String
        get() {
            // For demonstrating only do not apply this approach in prod
            val keyword = "src/main/"
            val srcIndex = absolutePath.indexOf(keyword)
            val fileName = absolutePath.drop(srcIndex + keyword.length)
                .replace("/", "_")
            return fileName + "_SHA256MD5.properties"
        }

}