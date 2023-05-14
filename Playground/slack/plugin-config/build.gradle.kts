plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

// To make it available as direct dependency
group = "me.2bab.buildinaction"
version = "SNAPSHOT"

dependencies {
    implementation(gradleApi())
    implementation(deps.kotlin.std)
    implementation(deps.android.gradle.plugin)
    implementation("me.2bab.buildinaction:slack:+")
    implementation("me.2bab.buildinaction:slack-lazy:+")
    implementation("me.2bab.buildinaction:slack-nested-blocks:+")
    implementation("me.2bab.buildinaction:slack-task-orchestra:+")
    implementation("me.2bab.buildinaction:slack-cache-rules-compliance:+")
    implementation("me.2bab.buildinaction:slack-test:+")
}

repositories {
    google()
    mavenCentral()
}
