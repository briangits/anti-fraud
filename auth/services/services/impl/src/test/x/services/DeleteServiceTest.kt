package services

import DI
import org.antifraud.auth.services.NoSuchServiceException
import org.antifraud.auth.services.Service
import org.antifraud.auth.services.ServiceRepository
import org.antifraud.auth.services.ServiceRepositoryImpl
import org.antifraud.auth.services.ServiceRole
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.kodein.di.instance
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DeleteServiceTest {

    private val serviceRepository: ServiceRepository = ServiceRepositoryImpl(DI.instance())

    @Test
    fun `deleting a service removes it from registered services`() = runTest {
        val service = Service(
            name = "Delete Service Test Service",
            serviceId = "delete-service-test-service",
            role = ServiceRole.Client
        )

        if (serviceRepository.get(service.serviceId) == null)
            serviceRepository.register(service)

        val deletedService = serviceRepository.delete(service.serviceId)

        assertEquals(service.serviceId, deletedService.serviceId)

        assertTrue {
            serviceRepository.getAll().none {
                it.serviceId == service.serviceId
            }
        }
    }

    @Test
    fun `deleting a non-existing service should throw a NoSuchServiceException`() = runTest {
        assertThrows<NoSuchServiceException> {
            serviceRepository.delete("x")
        }
    }

}