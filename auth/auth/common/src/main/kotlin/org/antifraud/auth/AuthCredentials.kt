package org.antifraud.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthCredentials(
    @SerialName("client_id") val serviceId: String,
    @SerialName("client_secret") val secretKey: String
)
