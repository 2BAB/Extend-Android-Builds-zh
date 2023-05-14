includeBuild("../../build-env")
// Gradle 7.5 or above
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")

pluginManagement {
    plugins {
        `kotlin-dsl`
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
