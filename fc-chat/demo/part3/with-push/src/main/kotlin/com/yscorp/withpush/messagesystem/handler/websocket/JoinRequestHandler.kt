package com.yscorp.withpush.messagesystem.handler.websocket

import com.yscorp.withpush.messagesystem.constant.IdKey
import com.yscorp.withpush.messagesystem.constant.MessageType
import com.yscorp.withpush.messagesystem.constant.ResultType
import com.yscorp.withpush.messagesystem.dto.domain.UserId
import com.yscorp.withpush.messagesystem.dto.websocket.inbound.JoinRequest
import com.yscorp.withpush.messagesystem.dto.websocket.outbound.ErrorResponse
import com.yscorp.withpush.messagesystem.dto.websocket.outbound.JoinResponse
import com.yscorp.withpush.messagesystem.service.ChannelService
import com.yscorp.withpush.messagesystem.service.ClientNotificationService
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketSession

@Component
@Suppress("unused")
class JoinRequestHandler(
    private val channelService: ChannelService,
    private val clientNotificationService: ClientNotificationService
) : BaseRequestHandler<JoinRequest> {

    /**
     * 초대코드를 기반으로 채널에 참여 요청을 처리합니다.
     * 성공 시 JoinResponse, 실패 시 ErrorResponse 를 클라이언트에게 전송합니다.
     */
    override fun handleRequest(senderSession: WebSocketSession, request: JoinRequest) {
        val senderUserId = senderSession.attributes[IdKey.USER_ID.value] as? UserId ?: return

        val (channel, result) = try {
            channelService.join(request.inviteCode, senderUserId)
        } catch (ex: Exception) {
            clientNotificationService.sendMessage(
                senderSession,
                senderUserId,
                ErrorResponse(MessageType.JOIN_REQUEST, ResultType.FAILED.message)
            )
            return
        }

        if (channel != null) {
            clientNotificationService.sendMessage(
                senderSession,
                senderUserId,
                JoinResponse(channel.channelId, channel.title)
            )
        } else {
            clientNotificationService.sendMessage(
                senderSession,
                senderUserId,
                ErrorResponse(MessageType.JOIN_REQUEST, result.message)
            )
        }
    }
}
