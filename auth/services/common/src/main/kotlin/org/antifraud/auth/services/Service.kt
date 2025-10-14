package org.antifraud.auth.services

import kotlinx.serialization.Serializable

@Serializable
data class Service(
    val name: String,
    val serviceId: String,
    val role: ServiceRole
)
