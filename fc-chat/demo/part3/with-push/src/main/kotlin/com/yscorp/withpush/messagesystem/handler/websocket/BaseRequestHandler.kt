package com.yscorp.withpush.messagesystem.handler.websocket

import net.prostars.messagesystem.dto.websocket.inbound.BaseRequest

interface BaseRequestHandler<T : BaseRequest?> {
    fun handleRequest(webSocketSession: WebSocketSession, request: T)
}
