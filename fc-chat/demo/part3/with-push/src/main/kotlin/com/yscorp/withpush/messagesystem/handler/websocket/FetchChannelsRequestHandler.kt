package com.yscorp.withpush.messagesystem.handler.websocket

import com.yscorp.withpush.messagesystem.constant.IdKey
import com.yscorp.withpush.messagesystem.dto.domain.UserId
import com.yscorp.withpush.messagesystem.dto.websocket.inbound.FetchChannelsRequest
import com.yscorp.withpush.messagesystem.dto.websocket.outbound.FetchChannelsResponse
import com.yscorp.withpush.messagesystem.service.ChannelService
import com.yscorp.withpush.messagesystem.service.ClientNotificationService
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketSession

@Component
@Suppress("unused")
class FetchChannelsRequestHandler(
    private val channelService: ChannelService,
    private val clientNotificationService: ClientNotificationService
) : BaseRequestHandler<FetchChannelsRequest> {

    override fun handleRequest(senderSession: WebSocketSession, request: FetchChannelsRequest) {
        val senderUserId = senderSession.attributes[IdKey.USER_ID.value] as UserId

        clientNotificationService.sendMessage(
            senderSession,
            senderUserId,
            FetchChannelsResponse(channelService.getChannels(senderUserId))
        )
    }

}
