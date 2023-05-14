plugins {
    `kotlin-dsl`
    id("java-gradle-plugin")
}
dependencies {
    implementation("com.android.tools.build:gradle:7.4.1")
    implementation("com.android.tools:common:30.4.1")
    implementation("org.ow2.asm:asm:9.4")
    implementation("org.ow2.asm:asm-commons:9.4")
    implementation("org.ow2.asm:asm-util:9.4")
}

repositories {
    mavenCentral()
    google()
}

gradlePlugin {
    plugins {
        create("transform-api-plugin") {
            id = "transform-api-plugin"
            implementationClass = "TransformAPIPlugin"
        }
    }
}
