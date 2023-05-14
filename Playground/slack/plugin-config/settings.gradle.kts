includeBuild("../plugins/slack") {
    dependencySubstitution {
        substitute(module("me.2bab.buildinaction:slack"))
            .using(project(":"))
    }
}
includeBuild("../plugins/slack-lazy") {
    dependencySubstitution {
        substitute(module("me.2bab.buildinaction:slack-lazy"))
            .using(project(":"))
    }
}
includeBuild("../plugins/slack-nested-blocks") {
    dependencySubstitution {
        substitute(module("me.2bab.buildinaction:slack-nested-blocks"))
            .using(project(":"))
    }
}
includeBuild("../plugins/slack-task-orchestra") {
    dependencySubstitution {
        substitute(module("me.2bab.buildinaction:slack-task-orchestra"))
            .using(project(":"))
    }
}
includeBuild("../plugins/slack-cache-rules-compliance") {
    dependencySubstitution {
        substitute(module("me.2bab.buildinaction:slack-cache-rules-compliance"))
            .using(project(":"))
    }
}
includeBuild("../plugins/slack-test") {
    dependencySubstitution {
        substitute(module("me.2bab.buildinaction:slack-test"))
            .using(project(":"))
    }
}


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
            from(files("../deps.versions.toml"))
        }
    }
}
