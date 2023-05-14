
enableFeaturePreview("VERSION_CATALOGS")
pluginManagement {
    val versions = file("deps.versions.toml").readText()
    val regexPlaceHolder = "%s\\s\\=\\s\\\"([A-Za-z0-9\\.\\-]+)\\\""
    val getVersion = { s: String -> regexPlaceHolder.format(s).toRegex().find(versions)!!.groupValues[1] }
    resolutionStrategy {
        eachPlugin {
            println("requested:" + requested.toString())
            println("id:" + requested.id.toString())
            println("id.id:" + requested.id.id)
            println("id.namespace:" + requested.id.namespace)
            println("id.name:" + requested.id.name)
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