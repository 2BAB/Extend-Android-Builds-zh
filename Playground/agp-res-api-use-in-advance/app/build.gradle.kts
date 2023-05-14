plugins {
    id("com.android.application")
    kotlin("android")
    id("resource-api-interaction-app")
    `maven-publish`
}

android {
    compileSdk = 31

    defaultConfig {
        applicationId = "me.xx2bab.extendagp.variantapi"
        minSdk = 28
        targetSdk = 31
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            applicationIdSuffix = ".debug"
            manifestPlaceholders["hostName"] = "bar"
        }
    }

    flavorDimensions += "server"
    productFlavors {
        create("staging") {
            dimension = "server"
            applicationIdSuffix = ".staging"
            versionNameSuffix = "-staging"
        }
        create("production") {
            dimension = "server"
            applicationIdSuffix = ".production"
            versionNameSuffix = "-production"
            versionCode = 2
        }
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    androidResources {
        // excludes += "..." does not exist in new DSL of AGP 7+
        ignoreAssetsPattern += "!app-key.txt"
    }

    packagingOptions {
        // exclude("/META-INF/**")
        // excludes.add("/META-INF/com/android/build/gradle/app-metadata.properties")
        resources {
            // excludes += "META-INF/com/android/build/gradle/app-metadata.properties"
            // excludes += "**/*.txt"
        }
    }
}

dependencies {
    implementation(deps.kotlin.std)
    implementation(project(":lib"))
}
