package org.antifraud.auth.services

data class NoSuchServiceException(
    val serviceId: String
) : NoSuchElementException("No such service exists, serviceId: $serviceId")
