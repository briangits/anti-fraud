plugins {
    alias(libs.plugins.kotlin.jvm)
}

group = "org.antifraud.auth.authenticator"
version = "0.0.1"

dependencies {
    api(project(":auth:authenticator:api"))
    api(project(":auth:authenticator:impl"))
}
