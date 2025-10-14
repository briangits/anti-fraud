package org.antifraud.auth.services.client

import org.antifraud.auth.services.ServiceRepository

fun ServiceRepository(serviceId: String, secretKey: String): ServiceRepository =
    ServiceRepositoryImpl(serviceId, secretKey)
