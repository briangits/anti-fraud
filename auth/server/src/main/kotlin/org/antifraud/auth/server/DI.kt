package org.antifraud.auth.server

import com.mysql.cj.jdbc.Driver
import org.antifraud.auth.AuthService
import org.antifraud.auth.keys.RSAKeyRepository
import org.antifraud.auth.server.config.DBConfig
import org.antifraud.auth.services.ServiceRepository
import org.jetbrains.exposed.v1.jdbc.Database
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.bindSingletonOf

val DI = DI.direct {
    bindSingleton {
        Database.connect(
            url = "jdbc:mysql://${DBConfig.host}:${DBConfig.port}/${DBConfig.name}",
            driver = Driver::class.qualifiedName ?: "com.mysql.cj.jdbc.Driver",
            user = DBConfig.username,
            password = DBConfig.password
        )
    }

    bindSingletonOf(::ServiceRepository)

    bindSingletonOf(::RSAKeyRepository)

    bindSingletonOf(::AuthService)
}