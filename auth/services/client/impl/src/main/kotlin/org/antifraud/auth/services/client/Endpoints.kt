package org.antifraud.auth.services.client

import org.antifraud.auth.services.client.config.RemoteRepository

internal fun RemoteRepository.  serviceEndpoint(serviceId: String): String =
    "${this.servicesEndpoint}/$serviceId"

internal fun RemoteRepository.serviceKeysEndpoint(serviceId: String): String =
        "${this.serviceEndpoint(serviceId)}/${this.secretKeysEndpoint}"

internal fun RemoteRepository.keyEndpoint(serviceId: String, keyId: Long): String =
    "${this.serviceKeysEndpoint(serviceId)}/$keyId"
