plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.buildConfig)
}

group = "org.antifraud.auth"
version = "0.0.1"

val env: Map<String, String> by rootProject.ext

buildConfig {
    packageName = "$group.config"
    className = "AuthConfig"

    buildConfigField("jwtSecretKey", env["JWT_SECRET_KEY"])
    buildConfigField("jwtIssuer", env["JWT_ISSUER"])
    // make tokens expire immediately in case of mis-config
    buildConfigField("jwtValidity", env["JWT_VALIDITY"]?.toInt() ?: 0)
}

dependencies {
    implementation(project(":auth:common"))
    implementation(project(":auth:auth:api"))

    implementation(project(":keys:api"))

    implementation(project(":services:services"))

    implementation(libs.kotlinx.datetime)

    implementation(libs.auth0.java.jwt)
}
