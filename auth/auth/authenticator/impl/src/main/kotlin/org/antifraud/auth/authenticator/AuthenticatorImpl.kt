package org.antifraud.auth.authenticator

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.antifraud.auth.AuthException
import org.antifraud.auth.AuthedService
import org.antifraud.auth.JWTPublicKey
import org.antifraud.auth.authenticator.config.JWTKeys
import org.antifraud.auth.services.ServiceRole

class AuthenticatorImpl : Authenticator {

    override fun getValidationKeys(): List<JWTPublicKey> = KeyStore.getAll()

    override fun getValidationKey(keyId: String): JWTPublicKey = KeyStore.get(keyId)

    override fun authenticate(token: String): AuthedService {
        val keyId = JWT.decode(token).keyId

        val key = this.getValidationKey(keyId)
        val publicKey = key.toRSAPublicKey()

        val algorithm = Algorithm.RSA256(publicKey, null)
        val verifier = JWT.require(algorithm).apply {
            withIssuer(JWTKeys.issuer)
        }.build()

        return runCatching {
            val token = verifier.verify(token)

            val serviceId = token.claims["serviceId"]?.asString()
            val role = token.claims["role"]?.asString()
                ?.runCatching { ServiceRole.valueOf(this) }
                ?.getOrNull()

            if (serviceId != null && role != null) AuthedService(serviceId, role)
            else null
        }.getOrNull() ?: throw AuthException("Invalid or expired token")
    }

}