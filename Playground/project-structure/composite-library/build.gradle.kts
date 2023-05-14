plugins {
    kotlin("jvm")
}

// To make it available as direct dependency
group = "me.2bab.extendagp"
version = "0.1.0"

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
}