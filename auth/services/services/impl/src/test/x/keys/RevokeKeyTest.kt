package keys

import DI
import org.antifraud.auth.services.NoSuchSecretKeyException
import org.antifraud.auth.services.NoSuchServiceException
import org.antifraud.auth.services.SecretKeyStatus
import org.antifraud.auth.services.Service
import org.antifraud.auth.services.ServiceRepository
import org.antifraud.auth.services.ServiceRepositoryImpl
import org.antifraud.auth.services.ServiceRole
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.kodein.di.instance
import kotlin.test.assertTrue

class RevokeKeyTest {

    private val serviceRepository: ServiceRepository = ServiceRepositoryImpl(DI.instance())

    private val service = runBlocking {
        val testService = Service(
            name = "Revoke Keys Test Service",
            serviceId = "revoke-keys-test-service",
            role = ServiceRole.Client
        )

        return@runBlocking serviceRepository.get(testService.serviceId)
            ?: serviceRepository.register(testService)
    }

    @Test
    fun `revoking a key should update the key's status to revoked`() = runTest {
        val key = serviceRepository.generateSecretKey(service.serviceId)

        serviceRepository.revokeSecretKey(service.serviceId, key.id)

        assertTrue {
            serviceRepository.getSecretKeys(service.serviceId).any {
                it.id == key.id && it.status is SecretKeyStatus.Revoked
            }
        }
    }

    @Test
    fun `revoking a key for a non-existing service should throw a NoSuchServiceException`() =
        runTest {
            assertThrows<NoSuchServiceException> {
                serviceRepository.revokeSecretKey("x", 0)
            }
        }

    @Test
    fun `revoking a key for a non-existing key should throw a NoSuchSecretKeysException`() =
        runTest {
            assertThrows<NoSuchSecretKeyException> {
                serviceRepository.revokeSecretKey(service.serviceId, -1)
            }
        }

}