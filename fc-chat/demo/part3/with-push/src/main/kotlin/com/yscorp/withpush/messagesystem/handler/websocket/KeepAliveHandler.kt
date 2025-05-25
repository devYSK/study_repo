package com.yscorp.withpush.messagesystem.handler.websocket

import net.prostars.messagesystem.constant.IdKey
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class KeepAliveHandler(sessionService: SessionService) : BaseRequestHandler<KeepAlive?> {
    private val sessionService: SessionService = sessionService

    override fun handleRequest(senderSession: WebSocketSession, request: KeepAlive?) {
        val senderUserId: UserId = senderSession.getAttributes().get(IdKey.USER_ID.getValue()) as UserId

        sessionService.refreshTTL(
            senderUserId, senderSession.getAttributes().get(IdKey.HTTP_SESSION_ID.getValue()) as String
        )
    }
}
