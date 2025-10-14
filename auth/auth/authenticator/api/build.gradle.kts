plugins {
    alias(libs.plugins.kotlin.jvm)
}

group = "org.antifraud.auth"
version = "0.0.1"

dependencies {
    api(project(":auth:common"))

    api(project(":keys:common"))

    api(libs.auth0.java.jwt)
}