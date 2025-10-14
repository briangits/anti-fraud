package org.antifraud.auth

import org.antifraud.auth.keys.JWTKey
import java.security.KeyFactory
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.Base64

fun JWTKey.toKeyPair(): Pair<RSAPublicKey, RSAPrivateKey> {
    fun String.toBytes() = Base64.getDecoder().decode(this)

    val factory = KeyFactory.getInstance("RSA")
    val publicKey =
        factory.generatePublic(X509EncodedKeySpec(this.publicKey.toBytes())) as RSAPublicKey
    val privateKey =
        factory.generatePrivate(PKCS8EncodedKeySpec(this.privateKey.toBytes())) as RSAPrivateKey

    return Pair(publicKey, privateKey)
}

fun JWTKey.toJWTPublicKey() = JWTPublicKey(keyId, publicKey)
