plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.buildConfig)
}

group = "org.antifraud.auth.authenticator"
version = "0.0.1"

val env: Map<String, String> by rootProject.ext

buildConfig {
    packageName = "$group.config"

    forClass(packageName = packageName.get(), "AuthService") {
        buildConfigField("host", env["SERVICE_HOST"])
        buildConfigField("endponint", env["SERVICE_ENDPOINT"])
    }

    forClass(packageName = packageName.get(), "JWTKeys") {
        buildConfigField("issuer", env["JWT_ISSUER"])
    }

    forClass(packageName = packageName.get(), "Endpoints") {
        buildConfigField("validationKeys", env["JWT_VALIDATION_KEYS_ENDPOINT"])
    }
}

dependencies {
    implementation(project(":auth:common"))
    implementation(project(":auth:authenticator:api"))

   implementation(libs.auth0.java.jwt)

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.contentNegotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
}
