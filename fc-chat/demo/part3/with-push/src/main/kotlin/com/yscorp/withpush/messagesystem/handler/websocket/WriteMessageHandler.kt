package com.yscorp.withpush.messagesystem.handler.websocket

import com.yscorp.withpush.messagesystem.constant.IdKey
import com.yscorp.withpush.messagesystem.dto.domain.UserId
import com.yscorp.withpush.messagesystem.dto.websocket.inbound.WriteMessage
import com.yscorp.withpush.messagesystem.dto.websocket.outbound.MessageNotification
import com.yscorp.withpush.messagesystem.service.MessageService
import com.yscorp.withpush.messagesystem.service.UserService
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketSession

@Component
@Suppress("unused")
class WriteMessageHandler(
    private val userService: UserService,
    private val messageService: MessageService
) : BaseRequestHandler<WriteMessage> {

    /**
     * 클라이언트로부터 메시지 전송 요청을 처리합니다.
     * 사용자 정보와 메시지를 받아 메시지 서비스를 통해 전송하고, 알림도 생성합니다.
     */
    override fun handleRequest(senderSession: WebSocketSession, request: WriteMessage) {
        val senderUserId = senderSession.attributes[IdKey.USER_ID.value] as? UserId ?: return
        val channelId = request.channelId
        val content = request.content
        val senderUsername = userService.getUsername(senderUserId) ?: "unknown"

        messageService.sendMessage(
            senderUserId,
            content,
            channelId,
            MessageNotification(channelId, senderUsername, content)
        )
    }
}
