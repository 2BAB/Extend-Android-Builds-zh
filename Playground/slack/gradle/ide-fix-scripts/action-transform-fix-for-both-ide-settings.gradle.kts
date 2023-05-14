/**
 * This script is to give a workaround for both Android Studio and IDEA
 * which convert `org.gradle.api.Action<T>` in a wrong way.
 * Bascially the root cause is when using `pluginManagement` instead of legacy `buildscript{ dependencies{} }`.
 * Check the link below to see the reproduceable steps and screenshot.
 *
 * @link https://youtrack.jetbrains.com/issue/IDEA-279528
 */
//println("Applying fix script for action convertion on both IDEA and Android Studio - settings.gradle.kts.")
includeBuild("plugin-config"){
    dependencySubstitution {
        substitute(module("me.2bab.buildinaction:plugin-config"))
            .with(project(":"))
    }
}
includeBuild("build-env") {
    dependencySubstitution {
        substitute(module("me.2bab.buildinaction:build-env"))
            .with(project(":"))
    }
}