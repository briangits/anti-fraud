plugins {
    alias(libs.plugins.kotlin.jvm)
}

group = "org.antifraud.auth.services.client"
version = "0.0.1"

dependencies {
    implementation(project(":services:api"))
    implementation(project(":services:client:impl"))
}
