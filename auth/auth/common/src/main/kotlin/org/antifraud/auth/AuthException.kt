package org.antifraud.auth

data class AuthException(override val message: String) : Exception(message)