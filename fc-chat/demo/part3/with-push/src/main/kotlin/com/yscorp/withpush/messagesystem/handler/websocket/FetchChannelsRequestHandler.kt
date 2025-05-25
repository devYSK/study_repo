package com.yscorp.withpush.messagesystem.handler.websocket

import net.prostars.messagesystem.constant.IdKey
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class FetchChannelsRequestHandler(
    channelService: ChannelService,
    clientNotificationService: ClientNotificationService
) :
    BaseRequestHandler<FetchChannelsRequest?> {
    private val channelService: ChannelService = channelService
    private val clientNotificationService: ClientNotificationService = clientNotificationService

    override fun handleRequest(senderSession: WebSocketSession, request: FetchChannelsRequest?) {
        val senderUserId: UserId = senderSession.getAttributes().get(IdKey.USER_ID.getValue()) as UserId

        clientNotificationService.sendMessage(
            senderSession,
            senderUserId,
            FetchChannelsResponse(channelService.getChannels(senderUserId))
        )
    }
}
