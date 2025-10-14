plugins {
    alias(libs.plugins.kotlin.jvm)
}

group = "org.antifraud.auth.services"
version = "0.0.1"

dependencies {
    implementation(project(":services:api"))
    implementation(project(":services:services:impl"))

    implementation(libs.exposed.jdbc)
}
