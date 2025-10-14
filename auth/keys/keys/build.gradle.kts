plugins {
    alias(libs.plugins.kotlin.jvm)
}

group = "org.antifraud.auth.keys"

dependencies {
    api(project(":keys:api"))
    api(project(":keys:keys:lib"))
}
