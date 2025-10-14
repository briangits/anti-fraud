package org.antifraud.auth

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import io.ktor.http.path
import io.ktor.http.takeFrom
import io.ktor.serialization.kotlinx.json.json
import org.antifraud.auth.client.config.Endpoints
import org.antifraud.auth.client.config.RemoteAuthService
import kotlin.time.ExperimentalTime

class AuthServiceImpl() : AuthService {

    val client get() = HttpClient {
        install(ContentNegotiation) { json() }

        HttpResponseValidator {
            validateResponse { response ->
                when (response.status) {
                    HttpStatusCode.Unauthorized -> throw AuthException("Incorrect credentials")
                }
            }
        }

        defaultRequest {
            url {
                takeFrom(RemoteAuthService.host)
                path(RemoteAuthService.endponint)
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    override suspend fun authenticate(serviceId: String, secretKey: String): AuthToken =
        client.use {
            it.post(Endpoints.authToken) {
                setBody(
                    AuthCredentials(serviceId, secretKey)
                )
            }.body()
        }


}