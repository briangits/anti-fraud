package org.antifraud.auth

import org.antifraud.auth.services.ServiceRole

data class AuthedService(
    val serviceId: String,
    val role: ServiceRole
)
