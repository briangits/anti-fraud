plugins {
    alias(libs.plugins.kotlin.jvm)
    `maven-publish`
}

group = "org.antifraud.auth.client"
version = "0.0.1"

val env: Map<String, String> by rootProject.ext

dependencies {
    api(project(":auth:authenticator:api"))
    api(project(":auth:authenticator:lib"))
}

publishing {
    repositories {
        maven {
            url = uri(env["GITHUB_PACKAGES_REPO_URL"]!!)
            credentials {
                username = env["GITHUB_USERNAME"]!!
                password = env["GITHUB_PACKAGES_TOKEN"]!!
            }
        }
    }

    publications {
        create<MavenPublication>("maven") {
            from(components["kotlin"])

            groupId = "$group"
            artifactId = "auth-authenticator"
            version = version

            pom {
                name = "Auth Client"
                description = "A client for internal microservice authentication"

                developers {
                    developer {
                        id = "briangits"
                        name = "Gits"
                        email = "itsbriangits@gmail.com"
                    }
                }
            }
        }
    }
}
