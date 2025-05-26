package com.yscorp.withpush.messagesystem.handler.websocket

import com.yscorp.withpush.messagesystem.constant.IdKey
import com.yscorp.withpush.messagesystem.dto.domain.Connection
import com.yscorp.withpush.messagesystem.dto.domain.UserId
import com.yscorp.withpush.messagesystem.dto.websocket.inbound.FetchConnectionsRequest
import com.yscorp.withpush.messagesystem.dto.websocket.outbound.FetchConnectionsResponse
import com.yscorp.withpush.messagesystem.service.ClientNotificationService
import com.yscorp.withpush.messagesystem.service.UserConnectionService
import net.prostars.messagesystem.constant.IdKey
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketSession

@Component
@Suppress("unused")
class FetchConnectionsRequestHandler(
    private val userConnectionService: UserConnectionService,
    private val clientNotificationService: ClientNotificationService
) : BaseRequestHandler<FetchConnectionsRequest> {

    /**
     * 사용자의 특정 상태(ACCEPTED, PENDING 등)에 해당하는 유저 목록을 조회하여
     * Connection 객체로 변환한 후 응답 메시지로 전송합니다.
     */
    override fun handleRequest(senderSession: WebSocketSession, request: FetchConnectionsRequest) {
        val senderUserId = senderSession.attributes[IdKey.USER_ID.value] as? UserId ?: return

        val status = request.status
        val connections = userConnectionService.getUsersByStatus(senderUserId, status)
            .map { user -> Connection(user.username, status) }

        clientNotificationService.sendMessage(
            senderSession,
            senderUserId,
            FetchConnectionsResponse(connections)
        )
    }
}
