plugins {
    id("build-env") apply false
    `kotlin-dsl`
    `java-gradle-plugin`
}

gradlePlugin {
    plugins.register("slack") {
        id = "me.2bab.buildinaction.slack-nested-blocks"
        implementationClass = "me.xx2bab.buildinaction.slackblocks.SlackNotificationPlugin"
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
    testImplementation(deps.junit)
}