import java.util.Properties

plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.buildConfig) apply false
    alias(libs.plugins.ktor) apply false
    alias(libs.plugins.kotlinx.serialization) apply false
}

fun loadEnv(path: String = ".env"): Map<String, String> {
    val envMap = mutableMapOf<String, String>()

    val envFile = project.rootDir.resolve(path)

    if (envFile.exists()) {
        val properties = Properties()
        properties.load(envFile.inputStream())

        properties.forEach { (key, _value) ->
            val value = "$_value".removeSurrounding("\"").removeSurrounding("'")
            envMap["$key"] = value
        }
    }

    return envMap.toMap()
}

allprojects {
    if (project != rootProject) return@allprojects

    val env = loadEnv().toMutableMap()

    // Load and override Production variables with Test variables
    //  when running tests
    if (env["ENVIRONMENT"] == "Test") env.putAll(loadEnv(".env.test"))

    ext["env"] = env.toMap()
}

subprojects.forEach { subproject ->
    subproject.tasks.withType<Test> {
        useJUnitPlatform()
    }

    subproject.tasks.withType<Jar> {
        archiveBaseName = project.path.replace(':', '-').removePrefix("-")
    }
}
