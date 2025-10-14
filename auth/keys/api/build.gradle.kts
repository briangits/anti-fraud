plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlinx.serialization)
}

group = "org.antifraud.auth.keys"
version = "0.0.1"

dependencies {
    api(project(":keys:common"))

    implementation(libs.kotlinx.serialization.json)
}
