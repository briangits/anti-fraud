plugins {
    alias(libs.plugins.kotlin.jvm)
}

group = "org.antifraud.auth.services"
version = "0.0.1"

dependencies {
    api(project(":services:common"))
}
