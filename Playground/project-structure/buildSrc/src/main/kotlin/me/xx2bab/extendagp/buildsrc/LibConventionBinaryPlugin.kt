package me.xx2bab.extendagp.buildsrc

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class LibConventionBinaryPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        println("The lib-convention-binary-plugin from ./buildSrc is applied.")

        val android = target.extensions.getByType<LibraryExtension>()
        android.lint.abortOnError = false
    }
}
