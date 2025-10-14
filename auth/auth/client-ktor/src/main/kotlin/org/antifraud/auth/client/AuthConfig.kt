package org.antifraud.auth.client

import org.antifraud.auth.AuthCredentials

class AuthConfig {

    internal lateinit var credentials: AuthCredentials

    fun credentials(serviceId: String, secretKey: String) {
        credentials = AuthCredentials(serviceId, secretKey)
    }

    fun credentials(provider: () -> AuthCredentials) {
        credentials = provider()
    }

}