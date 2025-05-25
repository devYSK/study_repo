package com.yscorp.withpush.messagesystem.handler.websocket

import net.prostars.messagesystem.constant.IdKey
import org.springframework.data.util.Pair
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class DisconnectRequestHandler(
    userConnectionService: UserConnectionService,
    clientNotificationService: ClientNotificationService
) : BaseRequestHandler<DisconnectRequest> {
    private val userConnectionService: UserConnectionService = userConnectionService
    private val clientNotificationService: ClientNotificationService = clientNotificationService

    override fun handleRequest(senderSession: WebSocketSession, request: DisconnectRequest) {
        val senderUserId: UserId = senderSession.getAttributes().get(IdKey.USER_ID.getValue()) as UserId
        val result: Pair<Boolean, String> =
            userConnectionService.disconnect(senderUserId, request.getUsername())
        if (result.first) {
            clientNotificationService.sendMessage(
                senderSession,
                senderUserId,
                DisconnectResponse(request.getUsername(), UserConnectionStatus.DISCONNECTED)
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
