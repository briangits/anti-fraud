package org.antifraud.auth

import kotlinx.serialization.Serializable

@Serializable
data class JWTPublicKey(
    val keyId: String,
    val publicKey: String
)
