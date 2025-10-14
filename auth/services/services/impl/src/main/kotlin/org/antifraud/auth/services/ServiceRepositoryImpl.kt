package org.antifraud.auth.services

import org.antifraud.auth.services.config.ServicesConfig
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class ServiceRepositoryImpl(private val db: Database) : ServiceRepository {

    override suspend fun getAll(): List<Service> = transaction(db) {
        ServiceDAO.all().map(ServiceDAO::toService)
    }

    override suspend fun get(serviceId: String): Service? = transaction(db) {
        ServiceDAO.find { ServiceStore.serviceId eq serviceId }.firstOrNull()?.toService()
    }

    override suspend fun register(newService: Service): Service = transaction(db) {
        if (ServiceDAO.count(ServiceStore.serviceId eq newService.serviceId) > 0) {
            println()
            ServiceDAO.all().map { it.toService() }.forEach(::println)
            throw DuplicateServiceIdException(newService.serviceId)
        }

        ServiceDAO.new {
            name = newService.name
            serviceId = newService.serviceId
            role = newService.role
        }.toService()
    }

    override suspend fun update(serviceId: String, update: ServiceUpdate): Service =
        transaction(db) {
            val service = ServiceDAO.find { ServiceStore.serviceId eq serviceId }.singleOrNull()
                ?: throw NoSuchServiceException(serviceId)

            service.apply {
                name = update.name ?: name
                role = update.role ?: role
            }

            service.toService()
        }

    override suspend fun delete(serviceId: String): Service = transaction(db) {
        val service = ServiceDAO.find { ServiceStore.serviceId eq serviceId }.singleOrNull()
            ?: throw NoSuchServiceException(serviceId)

        val snapshot = service.toService()

        service.delete()

        return@transaction snapshot
    }

    override suspend fun getSecretKeys(serviceId: String): List<SecretKey> = transaction(db) {
        if (ServiceDAO.count(ServiceStore.serviceId eq serviceId) < 1)
            throw NoSuchServiceException(serviceId)

        SecretKeyDAO.find { SecretKeyStore.serviceId eq serviceId }
            .map(SecretKeyDAO::toSecretKey)
    }

    override suspend fun generateSecretKey(serviceId: String): SecretKey = transaction(db) {
        if (ServiceDAO.count(ServiceStore.serviceId eq serviceId) < 1)
            throw NoSuchServiceException(serviceId)

        val activeKeys = SecretKeyDAO.count(
            (SecretKeyStore.serviceId eq serviceId) and
                    (SecretKeyStore.status eq KeyStatus.Active)
        )
        if (activeKeys >= ServicesConfig.maxActiveKeys)
            throw MaxActiveKeysExceededException(ServicesConfig.maxActiveKeys)

        var key: String
        var attempts = 0
        do {
            key = "${ServicesConfig.secretKeyPrefix}-" +
                    randomAlphanumeric(ServicesConfig.secretKeyLength)
            val exists = SecretKeyDAO.count(
                (SecretKeyStore.serviceId eq serviceId) and (SecretKeyStore.value eq key)
            ) > 0
            attempts++

            if (attempts >= 100) throw RuntimeException(
                "Failed to generate a unique secret key for service $serviceId"
            )
        } while (exists)

        SecretKeyDAO.new {
            this.serviceId = serviceId
            this.value = key
            this.status = SecretKeyStatus.Active
        }.toSecretKey()
    }

    @OptIn(ExperimentalTime::class)
    override suspend fun revokeSecretKey(serviceId: String, keyId: Long) = transaction(db) {
        if (ServiceDAO.count(ServiceStore.serviceId eq serviceId) < 1)
            throw NoSuchServiceException(serviceId)

        val key =
            SecretKeyDAO.find {
                (SecretKeyStore.serviceId eq serviceId) and (SecretKeyStore.id eq keyId)
            }.singleOrNull()
            ?: throw NoSuchSecretKeyException(serviceId, keyId)

        val currentTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        key.status = SecretKeyStatus.Revoked(currentTime)

        key.toSecretKey()
    }

    override fun close() { /** No Resources to close **/ }

}