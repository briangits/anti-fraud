package org.antifraud.auth.authenticator

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import io.ktor.http.path
import io.ktor.http.takeFrom
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import org.antifraud.auth.JWTPublicKey
import org.antifraud.auth.authenticator.config.AuthService
import org.antifraud.auth.authenticator.config.Endpoints
import org.antifraud.auth.keys.NoSuchJWTKeyException
import java.util.concurrent.ConcurrentHashMap

object KeyStore {

    private val keys = ConcurrentHashMap<String, JWTPublicKey>()

    private val client: HttpClient = HttpClient {
        install(ContentNegotiation) { json() }

        defaultRequest {
            url {
                takeFrom(AuthService.host)
                path(AuthService.endponint)
            }
        }
    }

    fun getAll(): List<JWTPublicKey> = runBlocking {
        val keys: List<JWTPublicKey> = client.get(Endpoints.validationKeys).body()

        keys.forEach { this@KeyStore.keys[it.keyId] = it }

        return@runBlocking keys
    }

    private fun getKey(keyId: String): JWTPublicKey = runBlocking {
        val response = client.get("${Endpoints.validationKeys}/$keyId")

        if (response.status == HttpStatusCode.NotFound) throw NoSuchJWTKeyException(keyId)

        if (!response.status.isSuccess())
            throw RuntimeException("Failed to get JWT Public Key, error: ${response.bodyAsText()}")

        val key: JWTPublicKey = response.body()
        keys[keyId] = key

        return@runBlocking key
    }

    operator fun get(keyId: String): JWTPublicKey = keys[keyId] ?: this.getKey(keyId)

}