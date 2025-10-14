package org.antifraud.auth.services

import kotlinx.serialization.Serializable

@Serializable
data class ServiceUpdate(
    val name: String? = null,
    val role: ServiceRole? = null
)