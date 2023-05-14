plugins {
    id("com.android.application")
    kotlin("android")
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

    setOf("main", "test", "androidTest").forEach {
        sourceSets[it].java.srcDir("src/$it/kotlin")
    }
}

dependencies {
    implementation(deps.kotlin.std)
    implementation("me.2bab.extendagp:library1-api:1.0.1")

    // Local components
    implementation(project(":library1")) {
        capabilities {
            // Using kebab-case to bind artifact "library1" with registering feature "okhttp"
            requireCapability("me.2bab.extendagp:library1-okhttp")
        }
    }

    // Remote components
//    implementation("me.2bab.extendagp:library1:1.0.1") {
//        capabilities {
//            // Using kebab-case to bind artifact "library1" with registering feature "okhttp"
//            requireCapability("me.2bab.extendagp:library1-okhttp")
//        }
//    }

    // Separate capability from core library completely if you want
//    implementation("me.2bab.extendagp:library1-okhttp:1.0.1")

}