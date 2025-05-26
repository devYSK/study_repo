package com.yscorp.withpush.messagesystem.config

import com.yscorp.withpush.messagesystem.auth.WebSocketHttpSessionHandshakeInterceptor
import com.yscorp.withpush.messagesystem.handler.WebSocketHandler
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry

@Configuration
@EnableWebSocket
@Suppress("unused")
class WebSocketHandlerConfig(
    webSocketHandler: WebSocketHandler,
    webSocketHttpSessionHandshakeInterceptor: WebSocketHttpSessionHandshakeInterceptor
) : WebSocketConfigurer {

    private val webSocketHandler: WebSocketHandler = webSocketHandler
    private val webSocketHttpSessionHandshakeInterceptor: WebSocketHttpSessionHandshakeInterceptor =
        webSocketHttpSessionHandshakeInterceptor

    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry
            .addHandler(webSocketHandler, "/ws/v1/message")
            .addInterceptors(webSocketHttpSessionHandshakeInterceptor)
    }

}
