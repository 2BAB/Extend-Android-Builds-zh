plugins {
    kotlin("jvm")
}

configurations.all {
    exclude("org.jetbrains.kotlin", "kotlin-stdlib-jre7")
    exclude("org.jetbrains.kotlin", "kotlin-stdlib-jre8")
}

dependencies {
    implementation(gradleApi())
    implementation("com.android.tools.build:gradle:${rootProject.extra["agpVersion"]}")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}