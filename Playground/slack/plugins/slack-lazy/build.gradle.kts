plugins {
    id("build-env") apply false
    `kotlin-dsl`
    `java-gradle-plugin`
}

gradlePlugin {
    plugins.register("slack") {
        id = "me.2bab.buildinaction.slack-lazy"
        implementationClass = "me.xx2bab.buildinaction.slacklazy.SlackNotificationPlugin"
    }
    plugins.register("tlack") {
        id = "me.2bab.buildinaction.tlack-lazy"
        implementationClass = "me.xx2bab.buildinaction.slacklazy.TlackPlugin"
    }
    plugins.register("providerTest") {
        id = "me.2bab.buildinaction.provider-test"
        implementationClass = "me.xx2bab.buildinaction.slacklazy.ProviderTestPlugin"
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