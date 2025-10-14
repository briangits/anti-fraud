package org.antifraud.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthToken(
    @SerialName("token") val value: String,
    val expiresAt: Long
)
