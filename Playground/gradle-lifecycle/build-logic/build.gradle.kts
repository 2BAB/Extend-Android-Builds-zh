plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

// To make it available as direct dependency
group = "me.2bab.extendagp"
version = "SNAPSHOT"

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation(gradleApi())
    implementation(kotlin("stdlib-jdk8"))
}

gradlePlugin {
    plugins.register("lifecycle-project") {
        id = "lifecycle-project"
        implementationClass = "me.xx2bab.extendagp.buildlogic.LifecycleForProjectPlugin"
    }
    plugins.register("lifecycle-settings") {
        id = "lifecycle-settings"
        implementationClass = "me.xx2bab.extendagp.buildlogic.LifecycleForSettingsPlugin"
    }
    plugins.register("lifecycle-listener-registry") {
        id = "lifecycle-listener-registry"
        implementationClass = "me.xx2bab.extendagp.buildlogic.BuildEventsListenerRegistryPlugin"
    }
}