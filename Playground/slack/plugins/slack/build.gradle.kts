plugins {
    id("build-env") apply false
    `java-gradle-plugin`
    `kotlin-dsl`
}

gradlePlugin {
    plugins.register("slack") {
        id = "me.2bab.buildinaction.slack"
        implementationClass = "me.xx2bab.buildinaction.slack.SlackNotificationPlugin"
    }
}

repositories {
    mavenCentral()
    google()
    gradlePluginPortal()
}

dependencies {
    // Basis
    implementation(gradleApi())
    implementation(deps.kotlin.std)
    implementation(deps.android.gradle.plugin)
    // Module specific
    implementation(deps.okHttp)
}