package com.yscorp.withpush.messagesystem.handler.websocket

import com.yscorp.withpush.messagesystem.constant.IdKey
import com.yscorp.withpush.messagesystem.constant.MessageType
import com.yscorp.withpush.messagesystem.constant.UserConnectionStatus
import com.yscorp.withpush.messagesystem.dto.domain.UserId
import com.yscorp.withpush.messagesystem.dto.websocket.inbound.DisconnectRequest
import com.yscorp.withpush.messagesystem.dto.websocket.outbound.DisconnectResponse
import com.yscorp.withpush.messagesystem.dto.websocket.outbound.ErrorResponse
import com.yscorp.withpush.messagesystem.service.ClientNotificationService
import com.yscorp.withpush.messagesystem.service.UserConnectionService
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketSession

@Component
@Suppress("unused")
class DisconnectRequestHandler(
    private val userConnectionService: UserConnectionService,
    private val clientNotificationService: ClientNotificationService
) : BaseRequestHandler<DisconnectRequest> {

    override fun handleRequest(senderSession: WebSocketSession, request: DisconnectRequest) {
        val senderUserId: UserId = senderSession.attributes[IdKey.USER_ID.value] as UserId
        val result: Pair<Boolean, String> =
            userConnectionService.disconnect(senderUserId, request.username)
        if (result.first) {
            clientNotificationService.sendMessage(
                senderSession,
                senderUserId,
                DisconnectResponse(request.username, UserConnectionStatus.DISCONNECTED)
            )
        } else {
            val errorMessage = result.second
            clientNotificationService.sendMessage(
                senderSession,
                senderUserId,
                ErrorResponse(MessageType.DISCONNECT_REQUEST, errorMessage)
            )
        }
    }
}
