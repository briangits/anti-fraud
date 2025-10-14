plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.buildConfig)
}

group = "org.antifraud.auth.server"
version = "0.0.1"

application {
    mainClass = "$group.MainKt"
}

val env: Map<String, String> by rootProject.ext

buildConfig {
    packageName = "$group.config"
    className = "ServerConfig"

    buildConfigField("host", env["SERVER_HOST_NAME"])
    buildConfigField("port", env["SERVER_HOST_PORT"]?.toInt())

    forClass(packageName = packageName.get(), "Endpoints") {
        buildConfigField("authToken", env["AUTH_TOKEN_ENDPOINT"])

        buildConfigField("jwtValidationKeys", env["JWT_VALIDATION_KEYS_ENDPOINT"])
        buildConfigField("jwtKeys", env["JWT_KEYS_ENDPOINT"])

        buildConfigField("services", env["SERVICES_ENDPOINT"])
        buildConfigField("serviceKeys", env["SERVICE_KEYS_ENDPOINT"])

    }

    forClass(packageName = packageName.get(), "DBConfig") {
        buildConfigField("host", env["DB_HOST"])
        buildConfigField("port", env["DB_PORT"]?.toInt())
        buildConfigField("name", env["DB_NAME"])
        buildConfigField("username", env["DB_USERNAME"])
        buildConfigField("password", env["DB_PASSWORD"])
    }
}

dependencies {
    implementation(project(":auth:auth"))
    implementation(project(":auth:authenticator"))
    implementation(project(":auth:authenticator-ktor"))

    implementation(project(":services:services"))

    implementation(project(":keys:keys"))

    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.cio)
    implementation(libs.ktor.server.statusPages)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.auth.jwt)
    implementation(libs.ktor.server.contentNegotiation)
    implementation(libs.ktor.serialization.kotlinx.json)

    implementation(libs.exposed.jdbc)
    implementation(libs.mysql.connector)

    implementation(libs.kosilibs.kodein)
}

