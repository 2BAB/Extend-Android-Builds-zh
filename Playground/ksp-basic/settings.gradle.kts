enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
pluginManagement {
    val versions = file("deps.versions.toml").readText()
    val regexPlaceHolder = "%s\\s\\=\\s\\\"([A-Za-z0-9\\.\\-]+)\\\""
    val getVersion = { s: String -> regexPlaceHolder.format(s).toRegex().find(versions)!!.groupValues[1] }

    plugins {
        kotlin("android") version getVersion("kotlinVer") apply false
        kotlin("jvm") version getVersion("kotlinVer") apply false
        kotlin("plugin.serialization") version getVersion("kotlinVer") apply false
        id("com.android.application") version getVersion("agpVer") apply false
        id("com.android.library") version getVersion("agpVer") apply false
        id("com.google.devtools.ksp") version getVersion("kspVer") apply false
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

include(":app", "sample-processor")
