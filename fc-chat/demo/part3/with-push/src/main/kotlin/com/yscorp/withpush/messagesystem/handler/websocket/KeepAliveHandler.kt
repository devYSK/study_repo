package com.yscorp.withpush.messagesystem.handler.websocket

import com.yscorp.withpush.messagesystem.constant.IdKey
import com.yscorp.withpush.messagesystem.dto.domain.UserId
import com.yscorp.withpush.messagesystem.dto.websocket.inbound.KeepAlive
import com.yscorp.withpush.messagesystem.service.SessionService
import net.prostars.messagesystem.constant.IdKey
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketSession

@Component
@Suppress("unused")
class KeepAliveHandler(
    private val sessionService: SessionService
) : BaseRequestHandler<KeepAlive> {

    /**
     * 클라이언트의 연결 유지를 위한 KeepAlive 요청을 처리합니다.
     * 해당 세션의 TTL(Time To Live)을 연장합니다.
     */
    override fun handleRequest(senderSession: WebSocketSession, request: KeepAlive) {
        val senderUserId = senderSession.attributes[IdKey.USER_ID.value] as? UserId ?: return
        val httpSessionId = senderSession.attributes[IdKey.HTTP_SESSION_ID.value] as? String ?: return

        sessionService.refreshTTL(senderUserId, httpSessionId)
    }
}
