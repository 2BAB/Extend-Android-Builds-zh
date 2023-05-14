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
    plugins.register("dummy-binary-plugin2") {
        id = "dummy-binary-plugin2"
        implementationClass = "me.xx2bab.extendagp.buildlogic.DummyBinaryPlugin2"
    }
    plugins.register("dummy-binary-plugin3") {
        id = "dummy-binary-plugin3"
        implementationClass = "me.xx2bab.extendagp.buildlogic.DummyBinaryPlugin3"
    }
}