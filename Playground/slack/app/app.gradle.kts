plugins {
    id("com.android.application")
    kotlin("android")

    // Enable one plugin each time
//    id("pre-compiled-script")
    id("slack-config")
//    id("slack-lazy-config")
//    id("slack-nested-blocks-config")
//    id("slack-task-orchestra-config")
//    id("slack-cache-rules-compliance")
//    id("slack-test")
}

android {
    compileSdk = 31
    defaultConfig {
        applicationId = "me.xx2bab.buildinaction.app"
        minSdk = 21
        targetSdk = 31
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
        }
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    lint {
        abortOnError = false
    }

    setOf("main", "test", "androidTest").forEach {
        sourceSets[it].java.srcDir("src/$it/kotlin")
    }
}

dependencies {

}

class LocalTestPlugin :
    Plugin<Project> {

    override fun apply(target: Project) { // Can not use `project` as the param name
        target.logger.lifecycle("applied LocalTestPlugin")
        target.tasks.register("localTestTask")
    }
}

apply<LocalTestPlugin>()