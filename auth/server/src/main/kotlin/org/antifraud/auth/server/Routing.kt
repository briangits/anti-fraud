package org.antifraud.auth.server

import io.ktor.server.application.Application
import io.ktor.server.response.respondText
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import org.antifraud.auth.server.config.Endpoints
import org.antifraud.auth.server.handlers.AuthenticationHandler
import org.antifraud.auth.server.handlers.DeleteServiceHandler
import org.antifraud.auth.server.handlers.GenerateNewJWTKeyHandler
import org.antifraud.auth.server.handlers.GenerateSecretKeyHandler
import org.antifraud.auth.server.handlers.GetJWTKeysHandler
import org.antifraud.auth.server.handlers.GetJWTValidationKeyHandler
import org.antifraud.auth.server.handlers.GetJWTValidationKeysHandler
import org.antifraud.auth.server.handlers.GetServiceHandler
import org.antifraud.auth.server.handlers.GetServiceSecretKeysHandler
import org.antifraud.auth.server.handlers.GetServicesHandler
import org.antifraud.auth.server.handlers.RevokeJWTKeyHandler
import org.antifraud.auth.server.handlers.RevokeSecretKeyHandler
import org.antifraud.auth.server.handlers.ServiceRegistrationHandler
import org.antifraud.auth.server.handlers.UpdateServiceHandler

fun Application.configureRouting() = routing {
    get("/") { call.respondText("Hello, Kredo!") }

    post(Endpoints.authToken, AuthenticationHandler)

    route(Endpoints.jwtValidationKeys) {
        get(GetJWTValidationKeysHandler)

        get("/{keyId}", GetJWTValidationKeyHandler)
    }

    // TODO
    //authenticateService(ServiceRole.Administration) {
        route(Endpoints.jwtKeys) {
            get(GetJWTKeysHandler)
            post(GenerateNewJWTKeyHandler)

            route("/{keyId}") {
                get(GetJWTKeysHandler)
                delete(RevokeJWTKeyHandler)
            }
        }
    //}

    //authenticateService(ServiceRole.Administration) {
        route(Endpoints.services) {
            get(GetServicesHandler)
            post(ServiceRegistrationHandler)

            route("/{serviceId}") {
                get(GetServiceHandler)
                patch(UpdateServiceHandler)
                delete(DeleteServiceHandler)

                route(Endpoints.serviceKeys) {
                    get(GetServiceSecretKeysHandler)
                    post(GenerateSecretKeyHandler)
                    delete("/{keyId}", RevokeSecretKeyHandler)
                }
            }
        }
    //}
}
