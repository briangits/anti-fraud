package org.antifraud.auth.keys

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import org.jetbrains.exposed.v1.dao.LongEntity
import org.jetbrains.exposed.v1.dao.LongEntityClass
import org.jetbrains.exposed.v1.datetime.datetime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

internal object KeyStore : LongIdTable("AuthJWKeys") {
    @OptIn(ExperimentalTime::class)
    val createdAt = datetime("createdAt")
        .clientDefault { Clock.System.now().toLocalDateTime(TimeZone.UTC) }
    val keyId = varchar("keyId", 255)
    val privateKey = text("privateKey")
    val publicKey = text("publicKey")
    val status = enumerationByName<JWTKeyStatus>("status", 10)
    val statusUpdateAt = datetime("statusUpdatedAt")
}


internal class KeyDAO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<KeyDAO>(KeyStore)

    val createdAt by KeyStore.createdAt
    var keyId by KeyStore.keyId
    var privateKey by KeyStore.privateKey
    var publicKey by KeyStore.publicKey
    private var _status by KeyStore.status
    var statusUpdateAt by KeyStore.statusUpdateAt

    @OptIn(ExperimentalTime::class)
    var status
        get() = _status
        set(value) {
            _status = value
            statusUpdateAt = Clock.System.now().toLocalDateTime(TimeZone.UTC)
        }
}

internal fun KeyDAO.toRSAKey() = JWTKey(keyId, privateKey, publicKey, status)
