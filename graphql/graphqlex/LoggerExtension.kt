package com.yscorp.webflux.common

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging

class LoggerExtension {
}

inline fun <reified T : Any> T.logger(): KLogger {
    val name = T::class.qualifiedName ?: T::class.simpleName ?: "Unknown"
    return KotlinLogging.logger(name)
}
