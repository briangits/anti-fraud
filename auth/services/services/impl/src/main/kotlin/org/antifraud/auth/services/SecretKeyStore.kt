package org.antifraud.auth.services

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import org.jetbrains.exposed.v1.dao.LongEntity
import org.jetbrains.exposed.v1.dao.LongEntityClass
import org.jetbrains.exposed.v1.datetime.CurrentDateTime
import org.jetbrains.exposed.v1.datetime.datetime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

internal enum class KeyStatus { Active, Revoked }

@OptIn(ExperimentalTime::class)
internal object SecretKeyStore : LongIdTable("ServiceSecretKeys") {
    val serviceId =
        reference("serviceId", ServiceStore.serviceId, onDelete = ReferenceOption.CASCADE)
    val value = varchar("key", 255)
    val createdAt = datetime("createdAt").defaultExpression(CurrentDateTime)
    val status = enumerationByName<KeyStatus>("status", 32)
    val revokedAt = datetime("revokedAt").nullable()
}

class SecretKeyDAO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<SecretKeyDAO>(SecretKeyStore)

    var serviceId by SecretKeyStore.serviceId
    var value by SecretKeyStore.value
    var createdAt by SecretKeyStore.createdAt
    private var _status by SecretKeyStore.status
    var revokedAt by SecretKeyStore.revokedAt

    @OptIn(ExperimentalTime::class)
    var status
        get() = when (_status) {
            KeyStatus.Active -> SecretKeyStatus.Active
            KeyStatus.Revoked -> {
                val revokedAt = revokedAt
                    ?: Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                SecretKeyStatus.Revoked(revokedAt)
            }
        }
        set(value) {
            _status = when (value) {
                is SecretKeyStatus.Active -> KeyStatus.Active
                is SecretKeyStatus.Revoked -> {
                    revokedAt = Clock.System.now().toLocalDateTime(TimeZone.UTC)
                    KeyStatus.Revoked
                }
            }
        }
}

fun SecretKeyDAO.toSecretKey() = SecretKey(id = id.value, value, createdAt, status)
