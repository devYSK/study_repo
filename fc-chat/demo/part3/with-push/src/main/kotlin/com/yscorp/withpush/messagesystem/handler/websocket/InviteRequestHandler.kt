package com.yscorp.withpush.messagesystem.handler.websocket

import com.yscorp.withpush.messagesystem.constant.IdKey
import com.yscorp.withpush.messagesystem.constant.MessageType
import com.yscorp.withpush.messagesystem.constant.UserConnectionStatus
import com.yscorp.withpush.messagesystem.dto.domain.UserId
import com.yscorp.withpush.messagesystem.dto.websocket.inbound.InviteRequest
import com.yscorp.withpush.messagesystem.dto.websocket.outbound.ErrorResponse
import com.yscorp.withpush.messagesystem.dto.websocket.outbound.InviteNotification
import com.yscorp.withpush.messagesystem.dto.websocket.outbound.InviteResponse
import com.yscorp.withpush.messagesystem.service.ClientNotificationService
import com.yscorp.withpush.messagesystem.service.UserConnectionService
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketSession

@Component
@Suppress("unused")
class InviteRequestHandler(
    private val userConnectionService: UserConnectionService,
    private val clientNotificationService: ClientNotificationService
) : BaseRequestHandler<InviteRequest> {

    /**
     * 초대 코드 기반으로 상대방을 친구 요청하며,
     * 성공 시 양쪽 유저에게 초대 알림 또는 응답 메시지를 전송합니다.
     */
    override fun handleRequest(senderSession: WebSocketSession, request: InviteRequest) {
        val inviterUserId = senderSession.attributes[IdKey.USER_ID.value] as? UserId ?: return

        val (partnerUserId, inviterUsername) =
            userConnectionService.invite(inviterUserId, request.userInviteCode)

        if (partnerUserId != null) {
            clientNotificationService.sendMessage(
                senderSession,
                inviterUserId,
                InviteResponse(request.userInviteCode, UserConnectionStatus.PENDING)
            )

            clientNotificationService.sendMessage(
                partnerUserId,
                InviteNotification(inviterUsername)
            )
        } else {
            clientNotificationService.sendMessage(
                senderSession,
                inviterUserId,
                ErrorResponse(MessageType.INVITE_REQUEST, inviterUsername)
            )
        }
    }
}
