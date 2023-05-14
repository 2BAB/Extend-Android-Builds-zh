package me.xx2bab.extendagp.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.initialization.Settings

class LifecycleForSettingsPlugin : Plugin<Settings> {

    override fun apply(target: Settings) {
        println("The dummy-binary-plugin3 from ./build-logic is applied.")
    }

}