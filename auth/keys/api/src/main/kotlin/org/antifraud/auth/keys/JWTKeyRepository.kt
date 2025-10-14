package org.antifraud.auth.keys

interface JWTKeyRepository {

    suspend fun getAll(): List<JWTKey>

    suspend operator fun get(keyId: String): JWTKey?

    suspend fun getSigningKey(): JWTKey

    suspend fun generateKey(): JWTKey

    suspend fun revokeKey(keyId: String): JWTKey

}