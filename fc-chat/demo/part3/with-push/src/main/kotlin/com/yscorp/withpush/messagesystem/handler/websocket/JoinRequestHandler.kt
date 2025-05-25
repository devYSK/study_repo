package com.yscorp.withpush.messagesystem.handler.websocket

import net.prostars.messagesystem.constant.IdKey
import org.springframework.data.util.Pair
import org.springframework.stereotype.Component
import java.util.*
import java.util.function.Consumer

@Component
@Suppress("unused")
class JoinRequestHandler(channelService: ChannelService, clientNotificationService: ClientNotificationService) :
    BaseRequestHandler<JoinRequest> {
    private val channelService: ChannelService = channelService
    private val clientNotificationService: ClientNotificationService = clientNotificationService

    override fun handleRequest(senderSession: WebSocketSession, request: JoinRequest) {
        val senderUserId: UserId = senderSession.getAttributes().get(IdKey.USER_ID.getValue()) as UserId

        val result: Pair<Optional<Channel>, ResultType>
        try {
            result = channelService.join(request.getInviteCode(), senderUserId)
        } catch (ex: Exception) {
            clientNotificationService.sendMessage(
                senderSession,
                senderUserId,
                ErrorResponse(MessageType.JOIN_REQUEST, ResultType.FAILED.getMessage())
            )
            return
        }

        result
            .getFirst()
            .ifPresentOrElse(
                Consumer<Channel> { channel: Channel ->
                    clientNotificationService.sendMessage(
                        senderSession,
                        senderUserId,
                        JoinResponse(channel.channelId(), channel.title())
                    )
                },
                Runnable {
                    clientNotificationService.sendMessage(
                        senderSession,
                        senderUserId,
                        ErrorResponse(MessageType.JOIN_REQUEST, result.getSecond().getMessage())
                    )
                })
    }
}
