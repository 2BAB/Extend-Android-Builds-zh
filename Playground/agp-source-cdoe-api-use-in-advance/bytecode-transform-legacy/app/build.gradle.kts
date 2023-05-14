plugins {
    id("com.android.application")
    kotlin("android")
    id("transform-api-plugin")
}

android {
    compileSdk = 33
    namespace = "me.xx2bab.extendagp.transform.newapi"
    defaultConfig {
        minSdk = 28
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            applicationIdSuffix = ".debug"
        }
    }

    kotlinOptions {
        jvmTarget = "17"
    }
    testOptions {
        unitTests {
            this.isIncludeAndroidResources = true
        }
    }
}

dependencies {
    implementation(deps.kotlin.std)
    implementation(project(":lib"))

    // Test more dependency transformations
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    testImplementation("org.hamcrest:hamcrest-library:2.2")
    testImplementation("org.robolectric:robolectric:4.9.2")
    testImplementation("junit:junit:4.13.2")
}