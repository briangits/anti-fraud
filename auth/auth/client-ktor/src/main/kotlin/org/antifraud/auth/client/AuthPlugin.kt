package org.antifraud.auth.client

import io.ktor.client.plugins.api.Send
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.request.bearerAuth
import io.ktor.http.HttpStatusCode
import org.antifraud.auth.AuthService
import kotlin.concurrent.atomics.AtomicReference
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import org.antifraud.auth.client.AuthConfig as ClientAuthConfig

@OptIn(ExperimentalAtomicApi::class)
val ServiceAuth = createClientPlugin("Auth", ::ClientAuthConfig) {

    val tokenStorage = AtomicReference<String?>(null)

    val authService = AuthService()

    suspend fun refreshToken(): String {
        val configs = pluginConfig.credentials

        val token = authService.authenticate(configs.serviceId, configs.secretKey)

        tokenStorage.store(token.value)

        return token.value
    }

    on(Send) { request ->
        var token = tokenStorage.load() ?: refreshToken()
        request.bearerAuth(token)

        val call = proceed(request)

        if (call.response.status == HttpStatusCode.Unauthorized) {
            token = refreshToken()
            request.bearerAuth(token)

            return@on proceed(request)
        }

        return@on call
    }

}