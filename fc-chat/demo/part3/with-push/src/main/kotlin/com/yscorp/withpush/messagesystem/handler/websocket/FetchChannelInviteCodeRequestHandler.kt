package com.yscorp.withpush.messagesystem.handler.websocket

import net.prostars.messagesystem.constant.IdKey
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class FetchChannelInviteCodeRequestHandler
    (channelService: ChannelService, clientNotificationService: ClientNotificationService) :
    BaseRequestHandler<FetchChannelInviteCodeRequest> {
    private val channelService: ChannelService = channelService
    private val clientNotificationService: ClientNotificationService = clientNotificationService

    override fun handleRequest(senderSession: WebSocketSession, request: FetchChannelInviteCodeRequest) {
        val senderUserId: UserId = senderSession.getAttributes().get(IdKey.USER_ID.getValue()) as UserId

        if (!channelService.isJoined(request.getChannelId(), senderUserId)) {
            clientNotificationService.sendMessage(
                senderSession,
                senderUserId,
                ErrorResponse(
                    MessageType.FETCH_CHANNEL_INVITECODE_REQUEST, "Not joined the channel."
                )
            )
            return
        }

        channelService
            .getInviteCode(request.getChannelId())
            .ifPresentOrElse(
                { inviteCode ->
                    clientNotificationService.sendMessage(
                        senderSession,
                        senderUserId,
                        FetchChannelInviteCodeResponse(request.getChannelId(), inviteCode)
                    )
                },
                {
                    clientNotificationService.sendMessage(
                        senderSession,
                        senderUserId,
                        ErrorResponse(
                            MessageType.FETCH_CHANNEL_INVITECODE_REQUEST,
                            "Fetch channel invite code failed."
                        )
                    )
                })
    }
}
