package org.antifraud.auth.services

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class SecretKey(
    val id: Long,
    val value: String,
    val createdAt: LocalDateTime,
    val status: SecretKeyStatus,
)