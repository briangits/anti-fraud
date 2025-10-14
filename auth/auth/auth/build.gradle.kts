plugins {
    alias(libs.plugins.kotlin.jvm)
}

group = "org.antifraud.auth"
version = "0.0.1"

dependencies {
    api(project(":auth:auth:api"))
    api(project(":auth:auth:lib"))
}
