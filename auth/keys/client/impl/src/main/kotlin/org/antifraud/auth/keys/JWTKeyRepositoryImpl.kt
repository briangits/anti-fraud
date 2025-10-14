package org.antifraud.auth.keys

import org.antifraud.auth.keys.config.JWTKeysConfig
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.neq
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.security.KeyPairGenerator
import java.util.Base64
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class JWTKeyRepositoryImpl(private val db: Database) : JWTKeyRepository {

    override suspend fun getAll(): List<JWTKey> =
        transaction(db) { KeyDAO.all().map(KeyDAO::toRSAKey) }

    override suspend fun get(keyId: String): JWTKey? =
        transaction(db) {
            KeyDAO.find { KeyStore.keyId eq keyId }.singleOrNull()?.toRSAKey()
        }

    override suspend fun getSigningKey(): JWTKey =
        transaction {
            KeyDAO.find { KeyStore.status eq JWTKeyStatus.Active }.singleOrNull()?.toRSAKey()
        } ?: this.generateKey()

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun generateKey(): JWTKey {
        val keyId = "${JWTKeysConfig.keyIdPrefix}-${Uuid.random()}"
        val generator = KeyPairGenerator.getInstance("RSA")
        generator.initialize(JWTKeysConfig.keyLength)

        val key = generator.generateKeyPair()
        val privateKey = key.private
        val publicKey = key.public

        fun ByteArray.toBase64(): String = Base64.getEncoder().encodeToString(this)

        val rsaKey = transaction(db) {
            KeyDAO.new {
                this.keyId = keyId
                this.privateKey = privateKey.encoded.toBase64()
                this.publicKey = publicKey.encoded.toBase64()
                this.status = JWTKeyStatus.Active
            }
        }

        transaction(db) {
            KeyDAO.find{ (KeyStore.keyId neq keyId) and (KeyStore.status eq JWTKeyStatus.Active) }
                .forEach { it.status = JWTKeyStatus.InActive }
        }

        return rsaKey.toRSAKey()
    }

    override suspend fun revokeKey(keyId: String): JWTKey = transaction(db) {
        val key = KeyDAO.find { KeyStore.keyId eq keyId }.singleOrNull()
            ?: throw NoSuchJWTKeyException(keyId)

        key.status = JWTKeyStatus.Revoked

        key.toRSAKey()
    }

}