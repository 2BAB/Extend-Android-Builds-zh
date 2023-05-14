plugins {
    id("build-env") apply false
    `kotlin-dsl`
    `java-gradle-plugin`
}

gradlePlugin {
    plugins.register("slack") {
        id = "me.2bab.buildinaction.slack-task-orchestra"
        implementationClass = "me.xx2bab.buildinaction.slackorchestra.SlackNotificationPlugin"
    }
    plugins.register("task-orchestra-test") {
        id = "me.2bab.buildinaction.task-orchestra-test"
        implementationClass = "me.xx2bab.buildinaction.slackorchestra.TaskOrchestraTestPlugin"
    }
}

val myCheck by tasks.registering {
    doLast {}
}

val myCheck2 by tasks.creating {
    doLast {}
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