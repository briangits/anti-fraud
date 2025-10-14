package org.antifraud.auth.server.handlers

import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingContext
import org.antifraud.auth.AuthCredentials
import org.antifraud.auth.AuthService
import org.antifraud.auth.keys.NoSuchJWTKeyException
import org.antifraud.auth.server.DI
import org.kodein.di.instance

val AuthenticationHandler: suspend RoutingContext.() -> Unit = handler@{
    val credentials = call.receive<AuthCredentials>()

    val serviceId = credentials.serviceId
    require(serviceId.isNotBlank()) { "serviceId cannot be blank" }

    val secretKey = credentials.secretKey
    require(secretKey.isNotBlank()) { "secretKey cannot be blank" }

    val token = DI.instance<AuthService>()
        .authenticate(serviceId, secretKey)

    call.respond(token)
}

val GetJWTValidationKeysHandler: suspend RoutingContext.() -> Unit = {
    val validationKeys = DI.instance<AuthService>()
        .getValidationKeys()

    call.respond(validationKeys)
}

val GetJWTValidationKeyHandler: suspend RoutingContext.() -> Unit = {
    val keyId = call.parameters["keyId"]
    require(!keyId.isNullOrBlank()) { "keyId cannot be blank" }

    val validationKey = DI.instance<AuthService>()
        .getValidationKeys()
        .firstOrNull { it.keyId == keyId }
        ?: throw NoSuchJWTKeyException(keyId)

    call.respond(validationKey)
}