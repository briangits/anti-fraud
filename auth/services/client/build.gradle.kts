plugins {
    alias(libs.plugins.kotlin.jvm)
}

group = "org.antifraud.auth.services.client"
version = "0.0.1"

dependencies {
    api(project(":services:api"))
    api(project(":services:client:lib"))
}
