includeBuild("../../build-env")
include("gradle-instrumented-kit")
project(":gradle-instrumented-kit").projectDir = file("../gradle-instrumented-kit")

// Gradle 7.5 or above
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")

pluginManagement {
    val versions = file("../../deps.versions.toml").readText()
    val regexPlaceHolder = "%s\\s\\=\\s\\\"([A-Za-z0-9\\.\\-]+)\\\""
    val getVersion = { s: String -> regexPlaceHolder.format(s).toRegex().find(versions)!!.groupValues[1] }

    plugins {
        `kotlin-dsl`
        kotlin("jvm") version getVersion("kotlinVer")
    }
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }
    versionCatalogs {
        create("deps") {
            from(files("../../deps.versions.toml"))
        }
    }
}
