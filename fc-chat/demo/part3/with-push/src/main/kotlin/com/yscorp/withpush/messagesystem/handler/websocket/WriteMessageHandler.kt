package com.yscorp.withpush.messagesystem.handler.websocket

import net.prostars.messagesystem.constant.IdKey
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class WriteMessageHandler(userService: UserService, messageService: MessageService) :
    BaseRequestHandler<WriteMessage?> {
    private val userService: UserService = userService
    private val messageService: MessageService = messageService

    override fun handleRequest(senderSession: WebSocketSession, request: WriteMessage) {
        val senderUserId: UserId = senderSession.getAttributes().get(IdKey.USER_ID.getValue()) as UserId
        val channelId: ChannelId = request.getChannelId()
        val content: String = request.getContent()
        val senderUsername: String = userService.getUsername(senderUserId).orElse("unknown")
        messageService.sendMessage(
            senderUserId,
            content,
            channelId,
            MessageNotification(channelId, senderUsername, content)
        )
    }
}
