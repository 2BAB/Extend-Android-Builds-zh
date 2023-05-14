package me.xx2bab.extendagp.buildsrc

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class DummyBinaryPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        println("The dummy-binary-plugin from ./buildSrc is applied.")

        val android = target.extensions.getByType<ApplicationExtension>()
        android.lint.abortOnError = false
    }
}