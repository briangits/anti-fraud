package org.antifraud.auth.services

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
sealed class SecretKeyStatus {

    @Serializable
    data object Active : SecretKeyStatus()

    @Serializable
    data class Revoked(val revokedAt: LocalDateTime) : SecretKeyStatus()

}