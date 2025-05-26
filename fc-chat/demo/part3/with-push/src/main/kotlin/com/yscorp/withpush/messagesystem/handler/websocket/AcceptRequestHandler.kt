package com.yscorp.withpush.messagesystem.handler.websocket

import com.yscorp.withpush.messagesystem.constant.IdKey
import com.yscorp.withpush.messagesystem.constant.MessageType
import com.yscorp.withpush.messagesystem.dto.domain.UserId
import com.yscorp.withpush.messagesystem.dto.websocket.inbound.AcceptRequest
import com.yscorp.withpush.messagesystem.dto.websocket.outbound.AcceptNotification
import com.yscorp.withpush.messagesystem.dto.websocket.outbound.AcceptResponse
import com.yscorp.withpush.messagesystem.dto.websocket.outbound.ErrorResponse
import com.yscorp.withpush.messagesystem.service.ClientNotificationService
import com.yscorp.withpush.messagesystem.service.UserConnectionService
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketSession

@Component
class AcceptRequestHandler(
    private val userConnectionService: UserConnectionService,
    private val clientNotificationService: ClientNotificationService
) : BaseRequestHandler<AcceptRequest> {

    override fun handleRequest(senderSession: WebSocketSession, request: AcceptRequest) {
        val acceptorUserId: UserId = senderSession.attributes[IdKey.USER_ID.value] as UserId
        val (userId, errorMessage) = userConnectionService.accept(acceptorUserId, request.username)

        if (userId == null) {
            val errorMessage = errorMessage

            clientNotificationService.sendMessage(
                senderSession,
                acceptorUserId,
                ErrorResponse(MessageType.ACCEPT_REQUEST, errorMessage)
            )
        } else {

            clientNotificationService.sendMessage(
                senderSession, acceptorUserId, AcceptResponse(request.username)
            )
            clientNotificationService.sendMessage(
                userId, AcceptNotification(errorMessage)
            )
        }
    }
}
