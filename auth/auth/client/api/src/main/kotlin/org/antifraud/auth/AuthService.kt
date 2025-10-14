package org.antifraud.auth

interface AuthService {

    suspend fun authenticate(serviceId: String, secretKey: String): AuthToken

}