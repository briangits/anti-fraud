package org.antifraud.auth.authenticator

import org.antifraud.auth.AuthedService
import org.antifraud.auth.JWTPublicKey

interface Authenticator {

    fun getValidationKeys(): List<JWTPublicKey>

    fun getValidationKey(keyId: String): JWTPublicKey

    fun authenticate(token: String): AuthedService

}