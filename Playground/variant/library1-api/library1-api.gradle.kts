plugins {
    kotlin("jvm")
    `maven-publish`
}

group = "me.2bab.extendagp"
version = "1.0.1"

dependencies {
    implementation(deps.kotlin.std)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}