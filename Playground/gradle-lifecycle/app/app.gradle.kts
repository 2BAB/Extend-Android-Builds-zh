import me.xx2bab.gradle.lifecycle.build.Lifecycle

plugins {
    id("com.android.application")
    id("lifecycle-project")
}

android {
    compileSdk = 31
    defaultConfig {
        applicationId = "me.xx2bab.gradle.lifecycle"
        minSdk = 28
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

dependencies {
    implementation(project(":lib"))
}

abstract class Listener : org.gradle.tooling.events.OperationCompletionListener {
    override fun onFinish(event: org.gradle.tooling.events.FinishEvent?) {
        println("event: $event")
    }
}
interface RegistryProvider {
    @get:Inject
    val registry: BuildEventsListenerRegistry
}
objects.newInstance<RegistryProvider>().registry
    .onTaskCompletion(provider { objects.newInstance<Listener>() })