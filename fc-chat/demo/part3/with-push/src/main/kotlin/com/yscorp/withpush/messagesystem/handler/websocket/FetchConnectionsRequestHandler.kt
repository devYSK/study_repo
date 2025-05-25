package com.yscorp.withpush.messagesystem.handler.websocket

import net.prostars.messagesystem.constant.IdKey
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class FetchConnectionsRequestHandler(
    userConnectionService: UserConnectionService,
    clientNotificationService: ClientNotificationService
) : BaseRequestHandler<FetchConnectionsRequest> {
    private val userConnectionService: UserConnectionService = userConnectionService
    private val clientNotificationService: ClientNotificationService = clientNotificationService

    override fun handleRequest(senderSession: WebSocketSession, request: FetchConnectionsRequest) {
        val senderUserId: UserId = senderSession.getAttributes().get(IdKey.USER_ID.getValue()) as UserId
        val connections: List<Connection> =
            userConnectionService.getUsersByStatus(senderUserId, request.getStatus()).stream()
                .map { user -> Connection(user.username(), request.getStatus()) }
                .toList()
        clientNotificationService.sendMessage(
            senderSession, senderUserId, FetchConnectionsResponse(connections)
        )
    }
}
