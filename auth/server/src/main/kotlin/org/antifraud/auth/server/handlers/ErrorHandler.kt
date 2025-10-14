package org.antifraud.auth.server.handlers

import io.ktor.http.HttpStatusCode
import io.ktor.serialization.JsonConvertException
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.statuspages.StatusPagesConfig
import org.antifraud.auth.AuthException
import org.antifraud.auth.keys.NoSuchJWTKeyException
import org.antifraud.auth.server.respondError
import org.antifraud.auth.server.respondMessage
import org.antifraud.auth.services.DuplicateServiceIdException
import org.antifraud.auth.services.MaxActiveKeysExceededException
import org.antifraud.auth.services.NoSuchSecretKeyException
import org.antifraud.auth.services.NoSuchServiceException
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.MissingFieldException
import kotlinx.serialization.SerializationException

val ErrorHandler: StatusPagesConfig.() -> Unit = {

    exception<AuthException> { call, cause ->
        call.respondMessage(HttpStatusCode.Unauthorized, cause.message)
        cause.printStackTrace()
    }

    exception<NoSuchServiceException> { call, cause ->
        call.respondMessage(HttpStatusCode.NotFound, cause.message)
    }

    exception<NoSuchSecretKeyException> { call, cause ->
        call.respondMessage(HttpStatusCode.NotFound, cause.message)
    }

    exception<DuplicateServiceIdException> { call, cause ->
        call.respondMessage(HttpStatusCode.Conflict, cause.message)
    }

    exception<MaxActiveKeysExceededException> { call, cause ->
        call.respondMessage(HttpStatusCode.Conflict, cause.message)
    }

    exception<NoSuchJWTKeyException> { call, cause ->
        call.respondMessage(HttpStatusCode.NotFound, cause.message)
    }

    exception<IllegalArgumentException> { call, cause ->
        call.respondMessage(HttpStatusCode.BadRequest, cause.message)
    }

    exception<SerializationException> { call, cause ->
        cause.printStackTrace()
        call.respondError()
    }

    @OptIn(ExperimentalSerializationApi::class)
    exception<BadRequestException> { call, cause ->
        val causes = generateSequence<Throwable>(cause) { it.cause }
        val rootCause = causes.last()

        val status = HttpStatusCode.BadRequest

        if (causes.any { it is JsonConvertException }) {
            val conversionErrorMessage = causes.first { it is JsonConvertException }.message
            val match = Regex("'(.*)' at path \\$\\.(.+)").find(conversionErrorMessage ?: "")

            if (match != null) {
                val (value, key) = match.destructured
                return@exception call.respondMessage(status, "Invalid '$key' value: '$value'")
            }
        }

        when (rootCause) {
            is MissingFieldException -> {
                val missingFields = rootCause.missingFields.joinToString(", ")
                call.respondMessage(status, "Missing required field(s): $missingFields")
            }
            else -> {
                println(cause)
                cause.printStackTrace()

                call.respondMessage(status, status.description)
            }
        }
    }

    exception<Throwable> { call, cause ->
        println(cause)
        cause.printStackTrace()

        call.respondError()
    }

    status(*HttpStatusCode.allStatusCodes.toTypedArray()) { status ->
        call.respondMessage(status, status.description)
    }

}