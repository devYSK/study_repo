package com.yscorp.withpush.messagesystem.handler.websocket

import com.yscorp.withpush.messagesystem.constant.IdKey
import com.yscorp.withpush.messagesystem.constant.MessageType
import com.yscorp.withpush.messagesystem.dto.domain.UserId
import com.yscorp.withpush.messagesystem.dto.websocket.inbound.LeaveRequest
import com.yscorp.withpush.messagesystem.dto.websocket.outbound.ErrorResponse
import com.yscorp.withpush.messagesystem.dto.websocket.outbound.LeaveResponse
import com.yscorp.withpush.messagesystem.service.ChannelService
import com.yscorp.withpush.messagesystem.service.ClientNotificationService
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketSession

@Component
@Suppress("unused")
class LeaveRequestHandler(
    private val channelService: ChannelService,
    private val clientNotificationService: ClientNotificationService
) : BaseRequestHandler<LeaveRequest> {

    /**
     * 유저의 채널 퇴장 요청을 처리합니다.
     * 성공 시 LeaveResponse, 실패 시 ErrorResponse 전송.
     */
    override fun handleRequest(senderSession: WebSocketSession, request: LeaveRequest) {
        val senderUserId = senderSession.attributes[IdKey.USER_ID.value] as? UserId ?: return

        if (channelService.leave(senderUserId)) {
            clientNotificationService.sendMessage(
                senderSession,
                senderUserId,
                LeaveResponse()
            )
        } else {
            clientNotificationService.sendMessage(
                senderSession,
                senderUserId,
                ErrorResponse(MessageType.LEAVE_REQUEST, "Leave failed.")
            )
        }
    }
}