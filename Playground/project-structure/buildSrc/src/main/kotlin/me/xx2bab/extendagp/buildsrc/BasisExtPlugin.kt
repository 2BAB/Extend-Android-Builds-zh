package me.xx2bab.extendagp.buildsrc

import org.gradle.api.Plugin
import org.gradle.api.Project

class BasisExtPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        println("The CoreExtPlugin from ./buildSrc is applied.")

        target.afterEvaluate {
            target.configurations.first { cf ->
                cf.name == "implementation"
            }.let { cf ->
                println("${cf.name} = ${cf.dependencies.size}")
                cf.dependencies.map { println(it.toString()) }
            }
        }
    }
}