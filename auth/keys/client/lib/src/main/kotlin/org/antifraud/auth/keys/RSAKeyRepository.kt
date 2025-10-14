package org.antifraud.auth.keys

import org.jetbrains.exposed.v1.jdbc.Database

fun RSAKeyRepository(db: Database): JWTKeyRepository = JWTKeyRepositoryImpl(db)
