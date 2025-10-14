package org.antifraud.auth.services

data class DuplicateServiceIdException(
    val serviceId: String
) : Exception("A service with ID '$serviceId' already exists")
