plugins {
    id("com.android.application")
    id("me.2bab.buildinaction.slack-test")
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
    }

}

dependencies {}

