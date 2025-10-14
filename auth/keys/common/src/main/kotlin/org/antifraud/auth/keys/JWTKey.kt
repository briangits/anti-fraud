package org.antifraud.auth.keys

import kotlinx.serialization.Serializable

@Serializable
data class JWTKey(
    val keyId: String,
    val privateKey: String,
    val publicKey: String,
    val status: JWTKeyStatus
)