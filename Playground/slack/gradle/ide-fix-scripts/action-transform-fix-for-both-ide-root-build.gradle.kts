/**
 * This script is to give a workaround for both Android Studio and IDEA
 * which convert `org.gradle.api.Action<T>` in a wrong way.
 * Bascially the root cause is when using `pluginManagement` instead of legacy `buildscript{ dependencies{} }`.
 * Check the link below to see the reproduceable steps and screenshot.
 *
 * @link https://youtrack.jetbrains.com/issue/IDEA-279528
 */
//println("Applying fix script for action convertion on both IDEA and Android Studio - root build.gradle.kts.")
buildscript {
    repositories {
        google()
        mavenCentral()
        mavenLocal()
    }

    dependencies {
        classpath("me.2bab.buildinaction:plugin-config:+")
        classpath("me.2bab.buildinaction:build-env:+")
    }
}


