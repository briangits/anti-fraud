package org.antifraud.auth.services

class MaxActiveKeysExceededException(
    val limit: Int
) : Exception("Maximum number of active keys exceeded, limit: $limit active keys")
