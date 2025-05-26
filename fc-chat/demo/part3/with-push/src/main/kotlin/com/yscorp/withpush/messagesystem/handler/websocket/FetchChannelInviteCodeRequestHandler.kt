package com.yscorp.withpush.messagesystem.handler.websocket

import com.yscorp.withpush.messagesystem.constant.IdKey
import com.yscorp.withpush.messagesystem.constant.MessageType
import com.yscorp.withpush.messagesystem.dto.domain.UserId
import com.yscorp.withpush.messagesystem.dto.websocket.inbound.FetchChannelInviteCodeRequest
import com.yscorp.withpush.messagesystem.dto.websocket.outbound.ErrorResponse
import com.yscorp.withpush.messagesystem.service.ChannelService
import com.yscorp.withpush.messagesystem.service.ClientNotificationService
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketSession

@Component
@Suppress("unused")
class FetchChannelInviteCodeRequestHandler(
    private val channelService: ChannelService,
    private val clientNotificationService: ClientNotificationService
) :
    BaseRequestHandler<FetchChannelInviteCodeRequest> {

    override fun handleRequest(senderSession: WebSocketSession, request: FetchChannelInviteCodeRequest) {
        val senderUserId: UserId = senderSession.attributes[IdKey.USER_ID.value] as UserId

        if (!channelService.isJoined(request.channelId, senderUserId)) {
            clientNotificationService.sendMessage(
                senderSession,
                senderUserId,
                ErrorResponse(
                    MessageType.FETCH_CHANNEL_INVITECODE_REQUEST, "Not joined the channel."
                )
            )
            return
        }

        val inviteCode1 = channelService
            .getInviteCode(request.channelId)

        if (inviteCode1 == null) {
            clientNotificationService.sendMessage(
                senderSession,
                senderUserId,
                ErrorResponse(
                    MessageType.FETCH_CHANNEL_INVITECODE_REQUEST, "Channel invite code not found."
                )
            )
            return
        } else {
            clientNotificationService.sendMessage(
                senderSession,
                senderUserId,
                ErrorResponse(
                    MessageType.FETCH_CHANNEL_INVITECODE_REQUEST,
                    "Fetch channel invite code failed."
                )
            )
        }

    }
}
