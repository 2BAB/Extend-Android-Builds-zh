package me.xx2bab.bytecode.intro

import com.tschuchort.compiletesting.KotlinCompilation.Result
import java.io.File

fun Result.getCompiledFileByName(fileName: String): File = compiledClassAndResourceFiles
    .find { it.name == fileName }!!

/**
 * For debug usage when tests throw errors.
 */
fun Result.printAll() {
    compiledClassAndResourceFiles.forEach {
        println("${it.name}")
    }
    outputDirectory.listFiles().forEach { println("dir: ${it.absolutePath}") }
}