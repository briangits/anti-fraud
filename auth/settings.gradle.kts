dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

rootProject.name = "antifraud-auth"

include(":services:api")
include(":services:common")
include(":services:services", ":services:services:impl", ":services:services:lib")
include(":services:client", ":services:client:impl", ":services:client:lib")

include(":keys:common", ":keys:api")
include(":keys:keys", ":keys:keys:impl", ":keys:keys:lib")
include(":keys:client", ":keys:client:impl", ":keys:client:lib")

include(":auth:common")
include(":auth:auth", ":auth:auth:api", ":auth:auth:impl", ":auth:auth:lib")
include(
    ":auth:authenticator", ":auth:authenticator:api",
    ":auth:authenticator:impl", ":auth:authenticator:lib"
)
include(":auth:authenticator-ktor")
include(":auth:client-ktor")
include(":auth:client", ":auth:client:api", ":auth:client:impl", ":auth:client:lib")

include(":server")
