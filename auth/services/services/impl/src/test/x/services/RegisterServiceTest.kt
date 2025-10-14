package services

import DI
import org.antifraud.auth.services.DuplicateServiceIdException
import org.antifraud.auth.services.Service
import org.antifraud.auth.services.ServiceRepository
import org.antifraud.auth.services.ServiceRepositoryImpl
import org.antifraud.auth.services.ServiceRole
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.kodein.di.instance
import kotlin.test.assertEquals

class RegisterServiceTest {

    private val serviceRepository: ServiceRepository = ServiceRepositoryImpl(DI.instance())

    @Test
    fun `registering a new service should return the registered service`() = runTest {
        val serviceToRegister =
            Service("Register Test Service", "register-test-service", ServiceRole.Client)

        if (serviceRepository.get(serviceToRegister.serviceId) != null)
            serviceRepository.delete(serviceToRegister.serviceId)

        val registeredService = serviceRepository.register(serviceToRegister)

        assertEquals(serviceToRegister, registeredService)
    }

    @Test
    fun `registering a service with an existing serviceId should throw a DuplicateServiceIdException`() =
        runTest {
            val service = Service(
                name = "Register Duplicate Test Service",
                serviceId = "register-test-service",
                role = ServiceRole.Client
            )

            if (serviceRepository.get(service.serviceId) == null)
                serviceRepository.register(service)

            assertThrows<DuplicateServiceIdException> {
                serviceRepository.register(service.copy())
            }
        }

}