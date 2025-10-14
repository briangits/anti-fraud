package org.antifraud.auth.services

import java.security.SecureRandom

fun randomAlphanumeric(length: Int): String {
    val chars = ('A'..'Z') + ('a'..'z') + ('0'..'9')

    val random = SecureRandom()
    val randomString = (1..length)
        .map { chars[random.nextInt(chars.size)] }
        .joinToString("")

    return randomString
}