package services

import DI
import org.antifraud.auth.services.NoSuchServiceException
import org.antifraud.auth.services.Service
import org.antifraud.auth.services.ServiceRepository
import org.antifraud.auth.services.ServiceRole
import org.antifraud.auth.services.ServiceUpdate
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import org.kodein.di.instance
import kotlin.test.assertEquals

@Execution(ExecutionMode.SAME_THREAD)
class UpdateServiceTest {

    private val serviceRepository: ServiceRepository = DI.instance()

    private val service: Service = runBlocking {
        val testService = Service(
            name = "Update Test Service",
            serviceId = "update-test-service",
            role = ServiceRole.Client
        )

        if (serviceRepository.get(testService.serviceId) != null)
            serviceRepository.delete(testService.serviceId)

        return@runBlocking serviceRepository.register(testService)
    }

    @Test
    fun `updating a service should return the updated service`() = runTest {
        val update = ServiceUpdate(
            name = "Updated Test Service",
            role = ServiceRole.Administration
        )

        val updatedService = serviceRepository.update(service.serviceId, update)

        assertEquals(update.name, updatedService.name)
        assertEquals(update.role, updatedService.role)
    }

    @Test
    fun `updating a non-existing service should throw a NoSuchServiceException`() = runTest {
        assertThrows<NoSuchServiceException> {
            serviceRepository.update("x", ServiceUpdate())
        }
    }

}