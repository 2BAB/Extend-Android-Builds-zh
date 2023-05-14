plugins {
    id("com.android.application")
    kotlin("android")
    id("source-interaction-sample")
}

android {
    compileSdk = 31
    namespace = "me.xx2bab.extendagp.sourceinteraction"
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
        }
        create("preproduction") {
            dimension = "server"
        }
        create("production") {
            dimension = "server"
        }
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

}

dependencies {
    implementation(deps.kotlin.std)

}