package com.yscorp.withpush.messagesystem.handler.websocket

import net.prostars.messagesystem.constant.IdKey
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class QuitRequestHandler(channelService: ChannelService, clientNotificationService: ClientNotificationService) :
    BaseRequestHandler<QuitRequest> {
    private val channelService: ChannelService = channelService
    private val clientNotificationService: ClientNotificationService = clientNotificationService

    override fun handleRequest(senderSession: WebSocketSession, request: QuitRequest) {
        val senderUserId: UserId = senderSession.getAttributes().get(IdKey.USER_ID.getValue()) as UserId

        val result: ResultType
        try {
            result = channelService.quit(request.getChannelId(), senderUserId)
        } catch (ex: Exception) {
            clientNotificationService.sendMessage(
                senderSession,
                senderUserId,
                ErrorResponse(MessageType.QUIT_REQUEST, ResultType.FAILED.getMessage())
            )
            return
        }

        if (result === ResultType.SUCCESS) {
            clientNotificationService.sendMessage(
                senderSession, senderUserId, QuitResponse(request.getChannelId())
            )
        } else {
            clientNotificationService.sendMessage(
                senderSession,
                senderUserId,
                ErrorResponse(MessageType.QUIT_REQUEST, result.getMessage())
            )
        }
    }
}
