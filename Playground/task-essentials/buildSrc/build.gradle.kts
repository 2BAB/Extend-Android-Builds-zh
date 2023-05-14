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
    implementation("com.android.tools.build:gradle:7.2.2")
}

gradlePlugin {
    plugins.register("task-essentials-plugin") {
        id = "task-essentials-plugin"
        implementationClass = "me.xx2bab.extendagp.buildsrc.TaskEssentialsPlugin"
    }
}