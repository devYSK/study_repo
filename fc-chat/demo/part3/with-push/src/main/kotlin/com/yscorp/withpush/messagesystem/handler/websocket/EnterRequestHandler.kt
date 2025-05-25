package com.yscorp.withpush.messagesystem.handler.websocket

import net.prostars.messagesystem.constant.IdKey
import org.springframework.data.util.Pair
import org.springframework.stereotype.Component
import java.util.*

@Component
@Suppress("unused")
class EnterRequestHandler(channelService: ChannelService, clientNotificationService: ClientNotificationService) :
    BaseRequestHandler<EnterRequest> {
    private val channelService: ChannelService = channelService
    private val clientNotificationService: ClientNotificationService = clientNotificationService

    override fun handleRequest(senderSession: WebSocketSession, request: EnterRequest) {
        val senderUserId: UserId = senderSession.getAttributes().get(IdKey.USER_ID.getValue()) as UserId

        val result: Pair<Optional<String>, ResultType> =
            channelService.enter(request.getChannelId(), senderUserId)
        result
            .getFirst()
            .ifPresentOrElse(
                { title: String? ->
                    clientNotificationService.sendMessage(
                        senderSession, senderUserId, EnterResponse(request.getChannelId(), title)
                    )
                },
                {
                    clientNotificationService.sendMessage(
                        senderSession,
                        senderUserId,
                        ErrorResponse(MessageType.ENTER_REQUEST, result.getSecond().getMessage())
                    )
                })
    }
}
