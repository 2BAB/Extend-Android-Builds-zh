import me.xx2bab.extendagp.buildsrc.getAppFeatureSwitchesFromDotKt
import me.xx2bab.extendagp.buildsrc.getAppFeatureSwitchesFromDotKt2

plugins {
    id("com.android.application")
    kotlin("android")

    id("dummy-script-plugin") // Or use `dummy-script-plugin`
    id("dummy-binary-plugin")
    id("basis-ext-plugin")
    id("eco-coordinator-plugin")

//    id("dummy-script-plugin2")
//    id("dummy-binary-plugin2")

}

apply(from = "../standalone-scripts/app-build-features-export2.gradle.kts")

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
    implementation(project(":library1"))
    implementation(project(":library2"))
    implementation(deps.kotlin.std)
    implementation("me.2bab.extendagp:composite-library:0.1.0")
}

afterEvaluate {
    // Test Extension Function from BuildSrc
    println("Test Extension Function from .kt file:")
    println(getAppFeatureSwitchesFromDotKt(android))
    println("Test Extension Function from .kt file2:")
    println(getAppFeatureSwitchesFromDotKt2())

    // Test project extra with Function export from Standalone Script Plugin
    println("Test project extra with Function export from Script Plugin:")
    val funcFromScript = extra["getAppFeatureSwitchesFromScriptPlugin"] as () -> Map<String, Boolean?>
    println(funcFromScript())
}

