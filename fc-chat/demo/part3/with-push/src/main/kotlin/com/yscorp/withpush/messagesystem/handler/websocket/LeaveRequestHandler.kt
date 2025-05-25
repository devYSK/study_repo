package com.yscorp.withpush.messagesystem.handler.websocket

import net.prostars.messagesystem.constant.IdKey
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class LeaveRequestHandler(channelService: ChannelService, clientNotificationService: ClientNotificationService) :
    BaseRequestHandler<LeaveRequest?> {
    private val channelService: ChannelService = channelService
    private val clientNotificationService: ClientNotificationService = clientNotificationService

    override fun handleRequest(senderSession: WebSocketSession, request: LeaveRequest?) {
        val senderUserId: UserId = senderSession.getAttributes().get(IdKey.USER_ID.getValue()) as UserId

        if (channelService.leave(senderUserId)) {
            clientNotificationService.sendMessage(senderSession, senderUserId, LeaveResponse())
        } else {
            clientNotificationService.sendMessage(
                senderSession,
                senderUserId,
                ErrorResponse(MessageType.LEAVE_REQUEST, "Leave failed.")
            )
        }
    }
}
