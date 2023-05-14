import me.xx2bab.gradle.lifecycle.build.Lifecycle

plugins {
    id("com.android.library")
    id("lifecycle-project")
}

android {
    compileSdk = 31
    defaultConfig {
        minSdk = 28
        targetSdk = 31
    }
}
