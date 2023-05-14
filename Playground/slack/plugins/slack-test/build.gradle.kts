plugins {
    id("build-env") apply false
    id("org.gradle.kotlin.kotlin-dsl") version "3.1.0"
    kotlin("plugin.serialization") version "1.6.20"
    `java-gradle-plugin`
}

dependencies {
    // Basis
    implementation(gradleApi())
    implementation(deps.kotlin.std)
    implementation(deps.android.gradle.plugin)
    // Module specific
    implementation(deps.okHttp)
    implementation(deps.retrofit)
    implementation(deps.retrofit.logging)
    implementation(deps.gson.converter)
    implementation(deps.kt.csv)
    // Tests
    testImplementation(deps.hamcrest)
    testImplementation(deps.mockk)
    testImplementation(gradleTestKit())
}

val deleteOldInstrumentedTests by tasks.registering(Delete::class) {
    delete(layout.buildDirectory.dir("test-samples"))
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter()
        }

        val integrationTest by registering(JvmTestSuite::class) {
            dependencies {
                implementation(project)
                implementation(project(":gradle-instrumented-kit"))
            }
            targets {
                all {
                    testTask.configure {
                        shouldRunAfter(test)
                        dependsOn(deleteOldInstrumentedTests)
                    }
                }
            }
        }

        val functionalTest by registering(JvmTestSuite::class) {
            dependencies {
                implementation(project)
                implementation(project(":gradle-instrumented-kit"))
            }
            targets {
                all {
                    testTask.configure {
                        shouldRunAfter(test)
                        dependsOn(deleteOldInstrumentedTests)
                    }
                }
            }
        }
    }
}

configurations["integrationTestImplementation"]
    .extendsFrom(configurations["testImplementation"])
configurations["functionalTestImplementation"]
    .extendsFrom(configurations["testImplementation"])

tasks.check.configure {
    dependsOn(tasks.named("test"))
    dependsOn(tasks.named("integrationTest"))
    dependsOn(tasks.named("functionalTest"))
}

tasks.withType<Test> {
    testLogging {
        this.showStandardStreams = true
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

gradlePlugin {
    plugins.register("slack-test") {
        id = "me.2bab.buildinaction.slack-test"
        implementationClass = "me.xx2bab.buildinaction.slacktest.SlackNotificationPlugin"
    }
    testSourceSets.add(sourceSets["integrationTest"])
    testSourceSets.add(sourceSets["functionalTest"])
}

version = "1.0"