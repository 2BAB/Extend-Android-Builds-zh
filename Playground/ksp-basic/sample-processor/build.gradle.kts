plugins {
    kotlin("jvm")
}

dependencies {
    implementation(deps.kotlin.std)
    implementation(deps.ksp.api)
    implementation(deps.kotlinpoet.core)
    implementation(deps.kotlinpoet.ksp)
}
