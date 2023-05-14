package me.xx2bab.waytobuild.customplugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class SamplePlugin: Plugin<Project> {

    override fun apply(target: Project) {
        for (i in 0..10) {
            println(i)
        }
    }

}