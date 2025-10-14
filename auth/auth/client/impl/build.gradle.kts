plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.buildConfig)
}

group = "org.antifraud.auth.client"
version = "0.0.1"

val env: Map<String, String> by rootProject.ext

buildConfig {
    packageName = "$group.config"
    className = "AuthConfig"

    buildConfigField("jwtSecretKey", env["JWT_SECRET_KEY"])
    buildConfigField("jwtIssuer", env["JWT_ISSUER"])
    // make tokens expire immediately in case of mis-config
    buildConfigField("jwtValidity", env["JWT_VALIDITY"]?.toInt() ?: 0)

    forClass(packageName = packageName.get(), "RemoteAuthService") {
        buildConfigField("host", env["SERVICE_HOST"])
        buildConfigField("endponint", env["SERVICE_ENDPOINT"])
    }

    forClass(packageName = packageName.get(), "Endpoints") {
        buildConfigField("authToken", env["AUTH_TOKEN_ENDPOINT"])
    }
}

dependencies {
    implementation(project(":auth:common"))
    implementation(project(":auth:client:api"))

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.contentNegotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
}
