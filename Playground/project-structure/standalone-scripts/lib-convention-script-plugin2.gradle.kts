import com.android.build.gradle.LibraryExtension

buildscript {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.1.0")
        classpath("com.squareup.okhttp3:okhttp:4.9.0")
    }
}

apply(plugin = "com.android.library")

// `android {}` does not work here since Type-safe model accessor
// is not working for standalone script plugin.
configure<LibraryExtension> {
    lint {
        abortOnError = false
    }
}

println("The lib-convention-script-plugin2 from ./standalone-scripts is applied.")
