package com.yscorp.previewwebsocket.timer.config

import com.yscorp.previewwebsocket.timer.handler.TimerHandler
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry

@org.springframework.context.annotation.Configuration
@EnableWebSocket
class WebSocketConfig : WebSocketConfigurer {
    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(TimerHandler(), "/ws/timer")
    }
}
