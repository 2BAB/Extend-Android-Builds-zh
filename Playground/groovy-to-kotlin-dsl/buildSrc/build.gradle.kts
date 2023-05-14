plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

// To make it available as direct dependency
group = "me.2bab.buildinaction"
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
    plugins.register("buildenv") {
        id = "build-env"
        implementationClass = "BuildEnvStubPlugin"

    }
}