plugins {
    id("com.android.application")
    kotlin("android")

    id("task-essentials-plugin")
}

android {
    compileSdk = 31
    defaultConfig {
        applicationId = "me.xx2bab.extendagp.app"
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

    buildFeatures {
        viewBinding = true
    }

    setOf("main", "test", "androidTest").forEach {
        sourceSets[it].java.srcDir("src/$it/kotlin")
    }
}

dependencies {
    implementation(deps.kotlin.std)
}

