package org.antifraud.auth

import org.antifraud.auth.keys.JWTKeyRepository
import org.antifraud.auth.services.ServiceRepository

fun AuthService(
    serviceRepository: ServiceRepository,
    keysRepository: JWTKeyRepository
): AuthService = AuthServiceImpl(serviceRepository, keysRepository)
