package com.yscorp.withpush.messagesystem.handler.websocket

import com.yscorp.withpush.messagesystem.constant.IdKey
import com.yscorp.withpush.messagesystem.constant.MessageType
import com.yscorp.withpush.messagesystem.constant.ResultType
import com.yscorp.withpush.messagesystem.dto.domain.UserId
import com.yscorp.withpush.messagesystem.dto.websocket.inbound.EnterRequest
import com.yscorp.withpush.messagesystem.dto.websocket.outbound.EnterResponse
import com.yscorp.withpush.messagesystem.dto.websocket.outbound.ErrorResponse
import com.yscorp.withpush.messagesystem.service.ChannelService
import com.yscorp.withpush.messagesystem.service.ClientNotificationService
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketSession
import java.util.*

@Component
@Suppress("unused")
class EnterRequestHandler(
    private val channelService: ChannelService,
    private val clientNotificationService: ClientNotificationService
) : BaseRequestHandler<EnterRequest> {

    override fun handleRequest(senderSession: WebSocketSession, request: EnterRequest) {
        val senderUserId: UserId = senderSession.getAttributes().get(IdKey.USER_ID.value) as UserId

        val result: Pair<String?, ResultType> = channelService.enter(request.getChannelId(), senderUserId)

        val (title, errorType) = result

        if (title != null) {
            clientNotificationService.sendMessage(
                senderSession, senderUserId, EnterResponse(request.getChannelId(), title)
            )
        } else {
            clientNotificationService.sendMessage(
                senderSession,
                senderUserId,
                ErrorResponse(MessageType.ENTER_REQUEST, errorType.message)
            )
        }
    }
}
