package org.antifraud.auth.server

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond

suspend fun ApplicationCall.respondMessage(
    status: HttpStatusCode = HttpStatusCode.OK,
    message: String? = status.description
) = this.respond(status, mapOf("message" to message))

suspend fun ApplicationCall.respondError(
    message: String? = HttpStatusCode.InternalServerError.description
) = this.respondMessage(status = HttpStatusCode.InternalServerError, message = message)
