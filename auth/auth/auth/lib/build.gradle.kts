plugins {
    alias(libs.plugins.kotlin.jvm)
}

group = "org.antifraud.auth"
version = "0.0.1"

dependencies {
    api(project(":auth:auth:api"))
    implementation(project(":auth:auth:impl"))

    api(project(":services:api"))

    api(project(":keys:api"))
}
