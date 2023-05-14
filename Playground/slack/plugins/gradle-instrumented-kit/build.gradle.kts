plugins {
    kotlin("jvm")
}

dependencies {
    implementation(gradleApi())
    implementation(gradleTestKit())
    implementation(deps.kotlin.std)
    implementation(deps.hamcrest)
    implementation(deps.mockk)
    implementation(deps.gson)
}