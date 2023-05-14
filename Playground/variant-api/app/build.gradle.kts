plugins {
    id("com.android.application")
    kotlin("android")
    // Enable either v1 or v2 (pleas do not enable at the same time)
    id("variant-v1-basis")
    id("variant-v1-advanced")
//    id("variant-v2-basis")
//    id("variant-v2-advanced")
//    id("variant-v2-polyfill")
    id("task-cache-testing")
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
        }
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro"
            )
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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(deps.kotlin.std)
}

// DO NOT use below approach
//if (gradle.startParameter.taskNames.toString()
//        .contains("release", ignoreCase = true)) {
//    tasks.create("runOnReleaseVariantOnly") {
//        doFirst {
//            println("Task runOnReleaseVariantOnly: running...")
//        }
//    }
//}

//androidComponents {
//    onVariants { variant ->
//        if (variant.name.contains("release", true)) {
//            variant.androidResources.aaptAdditionalParameters.add("-v")
//        } else {
//            variant.androidResources.aaptAdditionalParameters.add("-p")
//        }
//    }
//}