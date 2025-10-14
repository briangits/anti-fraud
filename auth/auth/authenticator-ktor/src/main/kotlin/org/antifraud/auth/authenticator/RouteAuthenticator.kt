package org.antifraud.auth.authenticator

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.createRouteScopedPlugin
import io.ktor.server.auth.AuthenticationChecked
import io.ktor.server.auth.authenticate
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.util.AttributeKey
import org.antifraud.auth.AuthedService
import org.antifraud.auth.services.ServiceRole

private val ServiceKey = AttributeKey<AuthedService>("AuthenticatedService")

private val RouteAuthenticator = createRouteScopedPlugin(
    name = "RoleAuthenticator",
    createConfiguration = { mutableSetOf() }
) {
    on(AuthenticationChecked) { call ->
        val service = ServiceAuthenticator.authenticate(call.authToken)
        val allowedRoles = pluginConfig.toSet()

        if (allowedRoles.isNotEmpty() && service.role !in allowedRoles) {
            val status = HttpStatusCode.Forbidden
            return@on call.respond(status, status.description)
        }

        call.attributes.put(ServiceKey, service)
    }
}

val ApplicationCall.authedService: AuthedService get() =
    this.attributes.getOrNull(ServiceKey)
        ?: error("No service is authenticated")

fun Route.authenticateService(
    vararg roles: ServiceRole = emptyArray(),
    build: Route.() -> Unit
): Route {
    return authenticate("serviceAuthenticator") {
        install(RouteAuthenticator) { addAll(roles.toSet()) }
        build()
    }
}
