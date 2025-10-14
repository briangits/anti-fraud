package org.antifraud.auth.server.handlers

import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingContext
import org.antifraud.auth.keys.JWTKeyRepository
import org.antifraud.auth.keys.NoSuchJWTKeyException
import org.antifraud.auth.server.DI
import org.kodein.di.instance

val GetJWTKeysHandler: suspend RoutingContext.() -> Unit = {
    val keys = DI.instance<JWTKeyRepository>()
        .getAll()

    call.respond(keys)
}

val GetJWTKeyHandler: suspend RoutingContext.() -> Unit = {
    val keyId = call.parameters["keyId"]
    require(!keyId.isNullOrBlank()) { "Invalid RSA Key ID" }

    val key = DI.instance<JWTKeyRepository>()
        .get(keyId) ?: throw NoSuchJWTKeyException(keyId)

    call.respond(key)
}

val GenerateNewJWTKeyHandler: suspend RoutingContext.() -> Unit = {
    val newKey = DI.instance<JWTKeyRepository>()
        .generateKey()

    call.respond(newKey)
}

val RevokeJWTKeyHandler: suspend RoutingContext.() -> Unit = {
    val keyId = call.parameters["keyId"]
    require(!keyId.isNullOrBlank()) { "Invalid RSA Key ID" }

    val revokedKey = DI.instance<JWTKeyRepository>()
        .revokeKey(keyId)

    call.respond(revokedKey)
}
