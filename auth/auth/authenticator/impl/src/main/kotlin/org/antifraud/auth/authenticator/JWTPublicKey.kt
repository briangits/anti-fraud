package org.antifraud.auth.authenticator

import org.antifraud.auth.JWTPublicKey
import java.security.KeyFactory
import java.security.interfaces.RSAPublicKey
import java.security.spec.X509EncodedKeySpec
import java.util.Base64

fun JWTPublicKey.toRSAPublicKey(): RSAPublicKey {
    val keyBytes = Base64.getDecoder().decode(this.publicKey)

    val factory = KeyFactory.getInstance("RSA")
    val publicKey =
        factory.generatePublic(X509EncodedKeySpec(keyBytes)) as RSAPublicKey

    return publicKey
}