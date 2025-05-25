package com.yscorp.previewwebsocket

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PreviewWebsocketApplication

fun main(args: Array<String>) {
    runApplication<PreviewWebsocketApplication>(*args)
}
