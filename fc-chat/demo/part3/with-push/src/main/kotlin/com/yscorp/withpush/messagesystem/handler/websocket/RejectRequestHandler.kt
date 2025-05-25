package com.yscorp.withpush.messagesystem.handler.websocket

import net.prostars.messagesystem.constant.IdKey
import org.springframework.data.util.Pair
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class RejectRequestHandler(
    userConnectionService: UserConnectionService,
    clientNotificationService: ClientNotificationService
) : BaseRequestHandler<RejectRequest?> {
    private val userConnectionService: UserConnectionService = userConnectionService
    private val clientNotificationService: ClientNotificationService = clientNotificationService

    override fun handleRequest(senderSession: WebSocketSession, request: RejectRequest) {
        val senderUserId: UserId = senderSession.getAttributes().get(IdKey.USER_ID.getValue()) as UserId
        val result: Pair<Boolean, String> =
            userConnectionService.reject(senderUserId, request.getUsername())
        if (result.first) {
            clientNotificationService.sendMessage(
                senderSession,
                senderUserId,
                RejectResponse(request.getUsername(), UserConnectionStatus.REJECTED)
            )
        } else {
            val errorMessage = result.second
            clientNotificationService.sendMessage(
                senderSession, senderUserId, ErrorResponse(MessageType.REJECT_REQUEST, errorMessage)
            )
        }
    }
}
