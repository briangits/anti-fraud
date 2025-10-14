plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.buildConfig)
}

group = "org.antifraud.auth.services"
version = "0.0.1"

val env: Map<String, String> by rootProject.ext

buildConfig {
    packageName = "$group.config"
    className = "ServicesConfig"

    buildConfigField("secretKeyLength", env["SERVICE_SECRET_KEY_LENGTH"]?.toInt() ?: 12)
    buildConfigField("secretKeyPrefix", env["SERVICE_SECRET_KEY_PREFIX"])
    buildConfigField("maxActiveKeys", env["SERVICE_MAX_ACTIVE_SERVICE_KEYS"]?.toInt() ?: 12)
}


sourceSets.test {
    buildConfig {
        forClass("DBConfig") {
            buildConfigField("host", env["DB_HOST"])
            buildConfigField("port", env["DB_PORT"]?.toInt())
            buildConfigField("name", env["DB_NAME"])
            buildConfigField("username", env["DB_USERNAME"])
            buildConfigField("password", env["DB_PASSWORD"])
        }
    }
}

dependencies {
    implementation(project(":services:api"))

    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.kotlin.datetime)
    implementation(libs.mysql.connector)

    testImplementation(libs.kotlin.test.junit5)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.kosilibs.kodein)
}
