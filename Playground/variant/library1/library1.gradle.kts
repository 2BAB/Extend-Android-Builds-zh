import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

plugins {
    kotlin("jvm")
    `maven-publish`
}

group = "me.2bab.extendagp"
version = "1.0.1"

val SourceSet.kotlin: SourceDirectorySet
    get() = project.extensions
        .getByType<KotlinJvmProjectExtension>()
        .sourceSets
        .getByName(name)
        .kotlin

val okhttp = sourceSets.create("okhttp") {
    java.srcDirs("src/okhttp/java", "src/main/java")
    kotlin.srcDirs("src/okhttp/kotlin", "src/main/kotlin")

    // Separate capability from core library completely if you want
//    java.srcDir("src/okhttp/java")
//    kotlin.srcDir("src/okhttp/kotlin")
}

java {
    registerFeature("okhttp") {
        usingSourceSet(okhttp)
    }
}

configurations.named("okhttpImplementation")
    .get()
    .extendsFrom(configurations.implementation.get())

dependencies {
    implementation(project(":library1-api"))
    implementation(deps.kotlin.std)
    "okhttpImplementation"(deps.okHttp)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}

afterEvaluate {
    println(
        "okhttpImplementation - all deps size: " + configurations.named("okhttpImplementation")
            .get().dependencies.size
    )
    configurations.named("okhttpImplementation").get().allDependencies.forEach {
        println("okhttpImplementation - dep from all: $it")
    }
    configurations.named("okhttpImplementation").get().dependencies.forEach {
        println("okhttpImplementation - dep from it self: $it")
    }
}