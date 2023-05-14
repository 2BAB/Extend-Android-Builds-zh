//import com.android.build.api.artifact.ScopedArtifact
//import com.android.build.api.variant.ScopedArtifacts.Scope
//import javassist.ClassPool

plugins {
    id("com.android.application")
    kotlin("android")
    id("variant-api-plugin")
    id("instrumentation-api-plugin")
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




//abstract class AddClassesTask: DefaultTask() {
//    @get:OutputFiles
//    abstract val output: DirectoryProperty
//
//    @TaskAction
//    fun taskAction() {
//        val pool = javassist.ClassPool(javassist.ClassPool.getDefault())
//        val interfaceClass = pool.makeInterface("com.android.api.tests.SomeInterface");
//        interfaceClass.writeFile(output.get().asFile.absolutePath)
//    }
//}
//
//androidComponents {
//    onVariants { variant ->
//        val taskProvider = project.tasks.register<AddClassesTask>("${variant.name}AddClasses")
//        variant.artifacts
//            .forScope(Scope.PROJECT)
//            .use(taskProvider)
//            .toAppend(
//                ScopedArtifact.CLASSES,
//                AddClassesTask::output
//            )
//    }
//}