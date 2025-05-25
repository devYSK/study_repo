package com.yscorp.withpush.messagesystem.config

import net.prostars.messagesystem.auth.WebSocketHttpSessionHandshakeInterceptor
import org.springframework.context.annotation.Configuration

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
