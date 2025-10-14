plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.buildConfig)
}

group = "org.antifraud.auth.services.client"
version = "0.0.1"

val env: Map<String, String> by rootProject.ext

buildConfig {
    packageName = "$group.config"
    className = "ServicesRepository"

    forClass(packageName = packageName.get(), "RemoteRepository") {
        buildConfigField("host", env["SERVICE_HOST"])
        buildConfigField("endpoint", env["SERVICE_ENDPOINT"])
        buildConfigField("servicesEndpoint", env["SERVICES_ENDPOINT"])
        buildConfigField("secretKeysEndpoint", env["SERVICE_KEYS_ENDPOINT"])
    }
}

sourceSets.test {
    buildConfig {
        packageName = "$group.config"
        className = "ServicesConfig"

        buildConfigField("secretKeyLength", env["SERVICE_SECRET_KEY_LENGTH"]?.toInt() ?: 12)
        buildConfigField("secretKeyPrefix", env["SERVICE_SECRET_KEY_PREFIX"])
        buildConfigField("maxActiveKeys", env["SERVICE_MAX_ACTIVE_SERVICE_KEYS"]?.toInt() ?: 12)

        forClass("AuthCredentials") {
            buildConfigField("serviceId", env["SERVICE_ID"])
            buildConfigField("secretKey", env["SERVICE_SECRET_KEY"])
        }
    }
}

dependencies {
    api(project(":services:api"))
    api(project(":auth:client-ktor"))

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.contentNegotiation)
    implementation(libs.ktor.serialization.kotlinx.json)

    testImplementation(libs.kotlin.test.junit5)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.kosilibs.kodein)
}
