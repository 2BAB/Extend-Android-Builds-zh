plugins {
    id("com.android.library")
    id("resource-api-interaction-lib")
}

android {
    compileSdk = 31
    defaultConfig {
        minSdk = 28
        targetSdk = 31
    }
    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
        }
        getByName("release") {}
    }
    flavorDimensions += "server"
    productFlavors {
        create("staging") {
            dimension = "server"
//            applicationIdSuffix = ".staging"
//            versionNameSuffix = "-staging"
        }
        create("production") {
            dimension = "server"
//            applicationIdSuffix = ".production"
//            versionNameSuffix = "-production"
//            versionCode = 2
        }
    }

    publishing {
        multipleVariants {
            allVariants()
            withSourcesJar()
            withJavadocJar()
        }
    }
}
