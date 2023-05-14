plugins {
    kotlin("jvm")
}

dependencies {
    implementation("com.didiglobal.booster:booster-aapt2:4.15.0")
    testImplementation(deps.junit)
    testImplementation(deps.hamcrest)
}

tasks.withType<Test> {
    testLogging {
        this.showStandardStreams = true
    }
}