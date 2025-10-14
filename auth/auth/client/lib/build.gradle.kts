plugins {
    alias(libs.plugins.kotlin.jvm)
}

group = "org.antifraud.auth.client"
version = "0.0.1"

dependencies {
    implementation(project(":auth:client:api"))
    implementation(project(":auth:client:impl"))
}
