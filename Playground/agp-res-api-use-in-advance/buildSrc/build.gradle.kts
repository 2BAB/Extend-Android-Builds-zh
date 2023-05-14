plugins {
    `kotlin-dsl`
    id("java-gradle-plugin")
}
dependencies {
    implementation("com.android.tools.build:gradle:7.2.2")
    implementation("com.android.tools:sdk-common:30.2.2")
}

repositories {
    mavenCentral()
    google()
}

gradlePlugin {
    plugins {
        create("resource-api-interaction-app") {
            id = "resource-api-interaction-app"
            implementationClass = "ResourceAPIInteractionAppPlugin"
        }
        create("resource-api-interaction-lib") {
            id = "resource-api-interaction-lib"
            implementationClass = "ResourceAPIInteractionLibPlugin"
        }
    }
}