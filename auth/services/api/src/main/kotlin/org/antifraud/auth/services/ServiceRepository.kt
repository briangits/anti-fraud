package org.antifraud.auth.services

interface ServiceRepository : AutoCloseable {

    suspend fun getAll(): List<Service>

    suspend fun get(serviceId: String): Service?

    suspend fun register(newService: Service): Service

    suspend fun update(serviceId: String, update: ServiceUpdate): Service

    suspend fun delete(serviceId: String): Service

    suspend fun getSecretKeys(serviceId: String): List<SecretKey>

    suspend fun generateSecretKey(serviceId: String): SecretKey

    suspend fun revokeSecretKey(serviceId: String, keyId: Long): SecretKey

}