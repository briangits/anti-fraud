package org.antifraud.auth.services.client

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.path
import io.ktor.http.takeFrom
import io.ktor.serialization.kotlinx.json.json
import org.antifraud.auth.client.ServiceAuth
import org.antifraud.auth.services.DuplicateServiceIdException
import org.antifraud.auth.services.MaxActiveKeysExceededException
import org.antifraud.auth.services.NoSuchSecretKeyException
import org.antifraud.auth.services.NoSuchServiceException
import org.antifraud.auth.services.SecretKey
import org.antifraud.auth.services.Service
import org.antifraud.auth.services.ServiceRepository
import org.antifraud.auth.services.ServiceUpdate
import org.antifraud.auth.services.client.config.RemoteRepository

class ServiceRepositoryImpl(
    val serviceId: String,
    val secretKey: String
) : ServiceRepository {

    private val client: HttpClient by lazy {
        HttpClient {
            install(ServiceAuth) { credentials(serviceId, secretKey) }
            install(ContentNegotiation) { json() }

            HttpResponseValidator(ErrorHandler)

            defaultRequest {
                url {
                    takeFrom(RemoteRepository.host)
                    path(RemoteRepository.endpoint)
                }

                contentType(ContentType.Application.Json)
            }
        }
    }

    override suspend fun getAll(): List<Service> =
        client.get(RemoteRepository.servicesEndpoint).body()

    override suspend fun get(serviceId: String): Service? =
        try {
            client.get(RemoteRepository.serviceEndpoint(serviceId)).body()
        } catch (e: NoSuchElementException) {
            null
        }

    override suspend fun register(newService: Service): Service =
        try {
            client.post(RemoteRepository.servicesEndpoint) {
                setBody(newService)
            }.body()
        } catch (e: IllegalStateException) {
            throw DuplicateServiceIdException(newService.serviceId)
        }

    override suspend fun update(serviceId: String, update: ServiceUpdate): Service =
       try {
           client.patch(RemoteRepository.serviceEndpoint(serviceId)) {
               setBody(update)
           }.body()
       } catch (e: NoSuchElementException) {
           throw NoSuchServiceException(serviceId)
       }

    override suspend fun delete(serviceId: String): Service =
        try {
            client.delete(RemoteRepository.serviceEndpoint(serviceId)).body()
        } catch (e: NoSuchElementException) {
            throw NoSuchServiceException(serviceId)
        }

    override suspend fun getSecretKeys(serviceId: String): List<SecretKey> =
        try {
            client.get(RemoteRepository.serviceKeysEndpoint(serviceId)).body()
        } catch (e: NoSuchElementException) {
            throw NoSuchServiceException(serviceId)
        }

    override suspend fun generateSecretKey(serviceId: String): SecretKey =
        try {
            client.post(RemoteRepository.serviceKeysEndpoint(serviceId)).body()
        } catch (e: Exception) {
            throw when (e) {
                is NoSuchElementException -> NoSuchServiceException(serviceId)
                is IllegalStateException -> MaxActiveKeysExceededException(12) // TODO
                else -> e
            }
        }

    override suspend fun revokeSecretKey(serviceId: String, keyId: Long): SecretKey =
        try {
            client.delete(RemoteRepository.keyEndpoint(serviceId, keyId)).body()
        } catch (e: NoSuchElementException) {
            val service = this.get(serviceId)

            if (service == null) throw NoSuchServiceException(serviceId)
            else throw NoSuchSecretKeyException(serviceId, keyId)
        }

    override fun close() = client.close()

}