package com.yscorp.withpush.messagesystem.handler.websocket

import com.yscorp.withpush.messagesystem.constant.IdKey
import com.yscorp.withpush.messagesystem.constant.ResultType
import com.yscorp.withpush.messagesystem.dto.domain.UserId
import com.yscorp.withpush.messagesystem.dto.websocket.inbound.QuitRequest
import com.yscorp.withpush.messagesystem.service.ChannelService
import com.yscorp.withpush.messagesystem.service.ClientNotificationService
import net.prostars.messagesystem.constant.IdKey
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketSession

@Component
@Suppress("unused")
class QuitRequestHandler(
    private val channelService: ChannelService,
    private val clientNotificationService: ClientNotificationService
) : BaseRequestHandler<QuitRequest> {

    /**
     * 사용자의 채널 퇴장 요청을 처리합니다.
     * 성공 시 QuitResponse, 실패 시 ErrorResponse 전송.
     */
    override fun handleRequest(senderSession: WebSocketSession, request: QuitRequest) {
        val senderUserId = senderSession.attributes[IdKey.USER_ID.value] as? UserId ?: return

        val result = runCatching {
            channelService.quit(request.channelId, senderUserId)
        }.getOrElse {
            clientNotificationService.sendMessage(
                senderSession,
                senderUserId,
                ErrorResponse(MessageType.QUIT_REQUEST, ResultType.FAILED.message)
            )
            return
        }

        if (result == ResultType.SUCCESS) {
            clientNotificationService.sendMessage(
                senderSession,
                senderUserId,
                QuitResponse(request.channelId)
            )
        } else {
            clientNotificationService.sendMessage(
                senderSession,
                senderUserId,
                ErrorResponse(MessageType.QUIT_REQUEST, result.message)
            )
        }
    }
}
