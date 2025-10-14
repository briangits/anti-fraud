package org.antifraud.auth.authenticator

import io.ktor.http.HttpHeaders
import io.ktor.server.application.ApplicationCall
import org.antifraud.auth.AuthException

internal val ApplicationCall.authToken: String
    get() = this.request.headers[HttpHeaders.Authorization]
        ?. removePrefix("Bearer")
        ?.trim()
        ?: throw AuthException("Unauthorized")
