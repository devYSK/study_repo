package com.yscorp.withpush.messagesystem.handler.websocket

import com.yscorp.withpush.messagesystem.constant.IdKey
import com.yscorp.withpush.messagesystem.constant.MessageType
import com.yscorp.withpush.messagesystem.dto.domain.UserId
import com.yscorp.withpush.messagesystem.dto.websocket.inbound.FetchUserInvitecodeRequest
import com.yscorp.withpush.messagesystem.dto.websocket.outbound.ErrorResponse
import com.yscorp.withpush.messagesystem.dto.websocket.outbound.FetchUserInvitecodeResponse
import com.yscorp.withpush.messagesystem.service.ClientNotificationService
import com.yscorp.withpush.messagesystem.service.UserService
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketSession

@Component
@Suppress("unused")
class FetchUserInvitecodeRequestHandler(
    private val userService: UserService,
    private val clientNotificationService: ClientNotificationService
) : BaseRequestHandler<FetchUserInvitecodeRequest> {

    /**
     * 사용자의 초대코드를 조회하고 성공/실패에 따라 클라이언트에 메시지를 전송합니다.
     */
    override fun handleRequest(senderSession: WebSocketSession, request: FetchUserInvitecodeRequest) {
        val senderUserId = senderSession.attributes[IdKey.USER_ID.value] as? UserId ?: return

        val inviteCode = userService.getInviteCode(senderUserId)

        if (inviteCode != null) {
            clientNotificationService.sendMessage(
                senderSession,
                senderUserId,
                FetchUserInvitecodeResponse(inviteCode)
            )
        } else {
            clientNotificationService.sendMessage(
                senderSession,
                senderUserId,
                ErrorResponse(
                    MessageType.FETCH_USER_INVITECODE_REQUEST,
                    "Fetch user invite code failed."
                )
            )
        }
    }
}
