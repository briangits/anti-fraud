package keys

import DI
import org.antifraud.auth.services.MaxActiveKeysExceededException
import org.antifraud.auth.services.Service
import org.antifraud.auth.services.ServiceRepository
import org.antifraud.auth.services.ServiceRepositoryImpl
import org.antifraud.auth.services.ServiceRole
import org.antifraud.auth.services.config.ServicesConfig
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import org.kodein.di.instance
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@Execution(ExecutionMode.SAME_THREAD)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class GenerateKeysTest {

    private val serviceRepository: ServiceRepository = ServiceRepositoryImpl(DI.instance())

    private val service: Service = runBlocking {
        val testService = Service(
            name = "Generate Keys Test Service",
            serviceId = "generate-keys-test-service",
            role = ServiceRole.Client
        )

        if (serviceRepository.get(testService.serviceId) != null)
            serviceRepository.delete(testService.serviceId)

        return@runBlocking serviceRepository.register(testService)
    }

    @Test
    @Order(1)
    fun `generating a key should return the generated key`() = runTest {
        val key = serviceRepository.generateSecretKey(service.serviceId)

        assertTrue { key.value.startsWith(ServicesConfig.secretKeyPrefix) }

        val rawKey = key.value.removePrefix("${ServicesConfig.secretKeyPrefix}-")
        assertEquals(ServicesConfig.secretKeyLength, rawKey.length)

        assertTrue {
            serviceRepository.getSecretKeys(service.serviceId).any {
                it.id == key.id && it.value == key.value
            }
        }
    }

    @Test
    @Order(2)
    fun `generating a key for a non-existing service should throw a ServiceNotFoundException`() =
        runTest {
            assertThrows<NoSuchElementException> {
                serviceRepository.generateSecretKey("x")
            }
        }

    @Test
    @Order(3)
    fun `exceeding max number of allowed active keys should throw a MaxActiveKeysExceededException`() =
        runTest {
            assertThrows<MaxActiveKeysExceededException> {
                repeat(ServicesConfig.maxActiveKeys + 1) {
                    serviceRepository.generateSecretKey(service.serviceId)
                }
            }
        }

}