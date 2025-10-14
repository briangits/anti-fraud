package org.antifraud.auth.services

import org.jetbrains.exposed.v1.jdbc.Database

fun ServiceRepository(db: Database): ServiceRepository = ServiceRepositoryImpl(db)
