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



// 0.
// This block is not debuggable.
project.extensions
    .findByType(com.android.build.gradle.AppExtension::class.java)!!
    .buildTypes.forEach {
        project.logger.lifecycle("CustomScript ==> ")
        project.logger.lifecycle(it.name)
    }


// Wrap it by Plugin -> u expect to be able to debug it, but unfortunately current Kotlin DSL didn't support this type either
//class VariantTestPlugin1 : Plugin<Project> {
//
//    override fun apply(target: Project) {
//        project.logger.lifecycle("applied VariantTestPlugin1")
//    }
//
//}
//apply<VariantTestPlugin1>()
//
// Check the me.xx2bab.waytobuild.VariantTestPlugin that created inside buildSrc
// Check the applied plugin/script on the top



// The 1~5 are blocks we can add breakpoints into,
// the full experiment and results can be found from link below
// https://2bab.me/2021/02/14/android-build-script-debug-support

// 2
dependencies {
    implementation(project(":library1"))
    implementation(deps.kotlin.std)
}

// 3.1
val propertyInPreCompiledScript = 0
project.logger.lifecycle("[Pg][DebugTips]: 3.1 " +
        "propertyInPreCompiledScript=$propertyInPreCompiledScript")

// 3.2
project.afterEvaluate {
    val taskCount = tasks.count()
    project.logger.lifecycle("[Pg][DebugTips]: 3.2 " +
            "taskCount=$taskCount")
}

// 3.3
project.extensions
    .findByType(com.android.build.gradle.AppExtension::class.java)!!
    .buildTypes
    .first()
    .let {
        project.logger.lifecycle("[Pg][DebugTips]: 3.3 $it.name")
    }
