package org.antifraud.auth.keys
data class NoSuchJWTKeyException(
    val keyId: String
) : NoSuchElementException("No such key exists, keyId: $keyId")