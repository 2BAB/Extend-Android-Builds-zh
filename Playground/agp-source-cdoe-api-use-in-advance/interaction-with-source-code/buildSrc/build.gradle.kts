plugins {
    `kotlin-dsl`
    id("java-gradle-plugin")
}
dependencies {
    implementation("com.android.tools.build:gradle:8.0.0-rc01")
}

repositories {
    mavenCentral()
    google()
}

gradlePlugin {
    plugins {
        create("source-interaction-sample") {
            id = "source-interaction-sample"
            implementationClass = "SourceInteractionSamplePlugin"
        }
    }
}