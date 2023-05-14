package me.xx2bab.extendagp.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project

class LifecycleForProjectPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        println("The dummy-binary-plugin2 from ./build-logic is applied.")
    }

}