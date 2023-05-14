plugins {
    id("com.android.library")
}

android {
    compileSdk = 31
    namespace = "me.xx2bab.extendagp.sourceinteraction.lib"
    defaultConfig {
        minSdk = 28
    }
    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
        }
        getByName("release") {}
    }
}
