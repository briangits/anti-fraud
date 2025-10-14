package org.antifraud.auth.server.handlers

import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingContext
import org.antifraud.auth.server.DI
import org.antifraud.auth.services.ServiceRepository
import org.kodein.di.instance

val GetServiceSecretKeysHandler: suspend RoutingContext.() -> Unit = handler@{
    val serviceId = call.parameters["serviceId"]
    require(!serviceId.isNullOrBlank()) { "Invalid Service ID" }

    val keys = DI.instance<ServiceRepository>()
        .getSecretKeys(serviceId)

    call.respond(keys)
}

val GenerateSecretKeyHandler: suspend RoutingContext.() -> Unit = handler@{
    val serviceId = call.parameters["serviceId"]
    require(!serviceId.isNullOrBlank()) { "Invalid Service ID" }

    val newKey = DI.instance<ServiceRepository>()
        .generateSecretKey(serviceId)

    call.respond(newKey)
}

val RevokeSecretKeyHandler: suspend RoutingContext.() -> Unit = handler@{
    val serviceId = call.parameters["serviceId"]
    require(!serviceId.isNullOrBlank()) { "Invalid Service ID" }

    val rawKeyId = call.parameters["keyId"]
    require(!rawKeyId.isNullOrBlank()) { "Invalid Secret Key ID" }

    val keyId = rawKeyId.toLongOrNull()
    require(keyId != null) { "Invalid Secret Key ID value: '$rawKeyId'" }

    val revokedKey = DI.instance<ServiceRepository>()
        .revokeSecretKey(serviceId, keyId)

    call.respond(revokedKey)
}