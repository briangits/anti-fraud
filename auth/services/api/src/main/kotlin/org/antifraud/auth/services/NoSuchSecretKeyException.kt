package org.antifraud.auth.services

data class NoSuchSecretKeyException(
    val serviceId: String,
    val keyId: Long
) : NoSuchElementException("No such key exists, serviceId: $serviceId, keyId: $keyId")