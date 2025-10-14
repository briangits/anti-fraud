package org.antifraud.auth.authenticator

import io.ktor.server.application.createApplicationPlugin
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.bearer

val ServiceAuthentication = createApplicationPlugin("ServiceAuthenticator") {

    this.application.install(Authentication) {
        bearer("serviceAuthenticator") {
            realm = "KredoAuthService"

            authenticate { credentials ->
                ServiceAuthenticator.authenticate(credentials.token)
            }
        }
    }
}