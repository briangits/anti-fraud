plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlinx.serialization)
}

group = "org.antifraud.auth"
version = "0.0.1"

dependencies {
    api(project(":services:common"))

    implementation(libs.kotlinx.serialization.json)
}
