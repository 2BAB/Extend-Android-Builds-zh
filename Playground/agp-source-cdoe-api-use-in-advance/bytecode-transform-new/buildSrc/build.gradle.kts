plugins {
    `kotlin-dsl`
    id("java-gradle-plugin")
}
dependencies {
    implementation("com.android.tools.build:gradle:8.0.0-rc01")
    implementation("org.ow2.asm:asm:9.4")
    implementation("org.ow2.asm:asm-commons:9.4")
    implementation("org.ow2.asm:asm-util:9.4")
    implementation("org.javassist:javassist:3.26.0-GA")
}

repositories {
    mavenCentral()
    google()
}

gradlePlugin {
    plugins {
        create("instrumentation-api-plugin") {
            id = "instrumentation-api-plugin"
            implementationClass = "InstrumentationAPIPlugin"
        }
        create("variant-api-plugin") {
            id = "variant-api-plugin"
            implementationClass = "VariantAPIPlugin"
        }
    }
}
