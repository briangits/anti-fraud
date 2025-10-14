plugins {
    alias(libs.plugins.kotlin.jvm)
}

group = "org.antifraud.auth.keys"
version = "0.0.1"

dependencies {
    implementation(project(":keys:api"))
    implementation(project(":keys:keys:impl"))

    implementation(libs.exposed.jdbc)
}
