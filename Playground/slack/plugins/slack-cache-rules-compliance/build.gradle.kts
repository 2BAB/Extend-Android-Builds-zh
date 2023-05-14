plugins {
    id("build-env") apply false
    `kotlin-dsl`
    `java-gradle-plugin`
}

gradlePlugin {
    plugins.register("slack-cache") {
        id = "me.2bab.buildinaction.slack-cache-rules-compliance"
        implementationClass = "me.xx2bab.buildinaction.slackcache.SlackNotificationPlugin"
    }
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