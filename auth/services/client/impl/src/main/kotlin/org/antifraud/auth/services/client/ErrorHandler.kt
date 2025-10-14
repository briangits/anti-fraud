package org.antifraud.auth.services.client

import io.ktor.client.plugins.HttpCallValidatorConfig
import io.ktor.http.HttpStatusCode
import org.antifraud.auth.AuthException

val ErrorHandler: HttpCallValidatorConfig.() -> Unit = {
    validateResponse { response ->
        val status = response.status

        when (status) {
            HttpStatusCode.Unauthorized -> throw AuthException(status.description)
            HttpStatusCode.NotFound -> throw NoSuchElementException(status.description)
            HttpStatusCode.Conflict -> throw IllegalStateException(status.description)
        }
    }
}