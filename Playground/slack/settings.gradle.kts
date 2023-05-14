enableFeaturePreview("VERSION_CATALOGS")
pluginManagement {
    val versions = file("deps.versions.toml").readText()
    val regexPlaceHolder = "%s\\s\\=\\s\\\"([A-Za-z0-9\\.\\-]+)\\\""
    val getVersion = { s: String -> regexPlaceHolder.format(s).toRegex().find(versions)!!.groupValues[1] }

    // Do not use below approach in settings.gradle.kts, though Gradle is fine with it, and it's short.
    // However, the IDE does not support it well on some cases.
    //    plugins {
    //        id("com.android.application") version "4.2.2"
    //        kotlin("android") version "1.5.31"
    //    }
    // Use resolutionStrategy{} instead.
    resolutionStrategy {
        eachPlugin {
            when (requested.id.namespace) {
                "com.android" ->
                    useModule("com.android.tools.build:gradle:${getVersion("agpVer")}")
                "org.jetbrains.kotlin" ->
                    useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:${getVersion("kotlinVer")}")
            }
        }
    }
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }
    versionCatalogs {
        create("deps") {
            from(files("./deps.versions.toml"))
        }
    }
}

include(":app")
includeBuild("plugin-config")
includeBuild("build-env")
rootProject.name = "play-slack"
rootProject.children.forEach {
    it.buildFileName = "${it.name}.gradle.kts"
}

// Once Android Studio or IDEA got issues fixed, we can remove corresponding script below,
// Gradle can run without these fixes.
//apply(from = "gradle/ide-fix-scripts/action-transform-fix-for-both-ide-settings.gradle.kts")
//apply(from = "gradle/ide-fix-scripts/nested-composite-builds-fix-for-as.gradle.kts")
