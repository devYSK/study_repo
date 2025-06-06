package com.example

import com.example.config.*
import com.example.config.configureDatabase
import com.example.controller.configureRouting
import com.example.shared.applicationEnv
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    this.log.info("current env: ${applicationEnv()}")

    configureDatabase()
    configureDependencyInjection()
    configureHttp()
    configureSession()
    configureSecurity()
    configureSerialization()
    configureRouting()
    configureErrorHandling()
    configureLogging()
}