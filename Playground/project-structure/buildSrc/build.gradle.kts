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
    implementation("com.android.tools.build:gradle:7.1.0")
}

gradlePlugin {
    plugins.register("dummy-binary-plugin") {
        id = "dummy-binary-plugin"
        implementationClass = "me.xx2bab.extendagp.buildsrc.DummyBinaryPlugin"
    }
    plugins.register("lib-convention-binary-plugin") {
        id = "lib-convention-binary-plugin"
        implementationClass = "me.xx2bab.extendagp.buildsrc.LibConventionBinaryPlugin"
    }
    plugins.register("basis-ext-plugin") {
        id = "basis-ext-plugin"
        implementationClass = "me.xx2bab.extendagp.buildsrc.BasisExtPlugin"
    }
    plugins.register("eco-coordinator-plugin") {
        id = "eco-coordinator-plugin"
        implementationClass = "me.xx2bab.extendagp.buildsrc.EcoCoordinatorPlugin"
    }
}