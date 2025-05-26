package com.yscorp.withpush.messagesystem.handler.websocket

import com.yscorp.withpush.messagesystem.constant.IdKey
import com.yscorp.withpush.messagesystem.constant.MessageType
import com.yscorp.withpush.messagesystem.constant.UserConnectionStatus
import com.yscorp.withpush.messagesystem.dto.domain.UserId
import com.yscorp.withpush.messagesystem.dto.websocket.inbound.RejectRequest
import com.yscorp.withpush.messagesystem.dto.websocket.outbound.ErrorResponse
import com.yscorp.withpush.messagesystem.dto.websocket.outbound.RejectResponse
import com.yscorp.withpush.messagesystem.service.ClientNotificationService
import com.yscorp.withpush.messagesystem.service.UserConnectionService
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketSession

@Component
@Suppress("unused")
class RejectRequestHandler(
    private val userConnectionService: UserConnectionService,
    private val clientNotificationService: ClientNotificationService
) : BaseRequestHandler<RejectRequest> {

    /**
     * 친구 초대 거절 요청을 처리합니다.
     * 성공 시 RejectResponse, 실패 시 ErrorResponse를 클라이언트에 전송합니다.
     */
    override fun handleRequest(senderSession: WebSocketSession, request: RejectRequest) {
        val senderUserId = senderSession.attributes[IdKey.USER_ID.value] as? UserId ?: return

        val (success, message) = userConnectionService.reject(senderUserId, request.username)

        if (success) {
            clientNotificationService.sendMessage(
                senderSession,
                senderUserId,
                RejectResponse(request.username, UserConnectionStatus.REJECTED)
            )
        } else {
            clientNotificationService.sendMessage(
                senderSession,
                senderUserId,
                ErrorResponse(MessageType.REJECT_REQUEST, message)
            )
        }
    }
}
