package org.antifraud.auth.server

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.install
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.statuspages.StatusPages
import kotlinx.serialization.json.Json
import org.antifraud.auth.server.config.ServerConfig
import org.antifraud.auth.server.handlers.ErrorHandler

fun main() {
    val server = embeddedServer(CIO, host = ServerConfig.host, port = ServerConfig.port) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }

        install(StatusPages, ErrorHandler)
        // TODO
        // install(ServiceAuthentication)

        configureRouting()
    }

    server.start(wait = true)
}
