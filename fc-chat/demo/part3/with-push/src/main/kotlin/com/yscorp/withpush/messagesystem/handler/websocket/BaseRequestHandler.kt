package com.yscorp.withpush.messagesystem.handler.websocket

import com.yscorp.withpush.messagesystem.dto.websocket.inbound.BaseRequest
import org.springframework.web.socket.WebSocketSession

interface BaseRequestHandler<T : BaseRequest> {
    fun handleRequest(webSocketSession: WebSocketSession, request: T)
}
