plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.buildConfig)
}

group = "org.antifraud.auth.keys"
version = "0.0.1"

val env: Map<String, String> by rootProject.ext

buildConfig {
    packageName = "$group.config"
    className = "JWTKeysConfig"

    buildConfigField("keyLength", env["RSA_KEY_LENGTH"]?.toInt() ?: 2048)
    buildConfigField("keyIdPrefix", env["RSA_KEY_ID_PREFIX"])
}

dependencies {
    implementation(project(":keys:api"))

    implementation(libs.nimbus.jose.jwt)

    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.kotlin.datetime)
    implementation(libs.mysql.connector)

}
