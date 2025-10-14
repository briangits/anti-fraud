package org.antifraud.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.antifraud.auth.config.AuthConfig
import org.antifraud.auth.keys.JWTKey
import org.antifraud.auth.keys.JWTKeyRepository
import org.antifraud.auth.keys.JWTKeyStatus
import org.antifraud.auth.services.SecretKeyStatus
import org.antifraud.auth.services.Service
import org.antifraud.auth.services.ServiceRepository
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.time.toJavaInstant

class AuthServiceImpl(
    private val serviceRepository: ServiceRepository,
    private val keysRepository: JWTKeyRepository,
) : AuthService {

    @OptIn(ExperimentalTime::class)
    private suspend infix fun Service.generateTokenExpiringAt(expiry: Instant): String {
        val signingKey = keysRepository.getSigningKey()

        val token = JWT.create().apply {
            withKeyId(signingKey.keyId)

            withIssuer(AuthConfig.jwtIssuer)

            withClaim("serviceId", this@generateTokenExpiringAt.serviceId)
            withClaim("role", this@generateTokenExpiringAt.role.name)

            withExpiresAt(expiry.toJavaInstant())
        }

        val (publicKey, privateKey) = signingKey.toKeyPair()
        val algorithm = Algorithm.RSA256(publicKey, privateKey)

        return token.sign(algorithm)
    }

    @OptIn(ExperimentalTime::class)
    override suspend fun authenticate(serviceId: String, secretKey: String): AuthToken {
        val service = serviceRepository.get(serviceId)

        if (service == null) throw AuthException("Incorrect credentials")

        val activeSecretKeys = serviceRepository.getSecretKeys(serviceId)
            .filter { it.status == SecretKeyStatus.Active }
            .map { it.value }

        if (secretKey !in activeSecretKeys) throw AuthException("Incorrect credentials")

        val expiry = Clock.System.now() + AuthConfig.jwtValidity.seconds

        val token = service generateTokenExpiringAt expiry

        return AuthToken(token, expiry.toEpochMilliseconds())
    }

    override suspend fun getValidationKeys(): List<JWTPublicKey> =
        keysRepository.getAll()
            .filter { it.status == JWTKeyStatus.Active || it.status == JWTKeyStatus.InActive }
            .map(JWTKey::toJWTPublicKey)

}