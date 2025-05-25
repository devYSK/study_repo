package com.yscorp.withpush.messagesystem.handler.websocket

import net.prostars.messagesystem.constant.IdKey
import org.springframework.data.util.Pair
import org.springframework.stereotype.Component
import java.util.*
import java.util.function.Consumer

@Component
@Suppress("unused")
class AcceptRequestHandler(
    userConnectionService: UserConnectionService,
    clientNotificationService: ClientNotificationService
) : BaseRequestHandler<AcceptRequest> {
    private val userConnectionService: UserConnectionService = userConnectionService
    private val clientNotificationService: ClientNotificationService = clientNotificationService

    override fun handleRequest(senderSession: WebSocketSession, request: AcceptRequest) {
        val acceptorUserId: UserId = senderSession.getAttributes().get(IdKey.USER_ID.getValue()) as UserId
        val result: Pair<Optional<UserId>, String> =
            userConnectionService.accept(acceptorUserId, request.getUsername())
        result
            .first
            .ifPresentOrElse(
                Consumer<UserId> { inviterUserId: UserId? ->
                    clientNotificationService.sendMessage(
                        senderSession, acceptorUserId, AcceptResponse(request.getUsername())
                    )
                    val acceptorUsername = result.second
                    clientNotificationService.sendMessage(
                        inviterUserId, AcceptNotification(acceptorUsername)
                    )
                },
                Runnable {
                    val errorMessage = result.second
                    clientNotificationService.sendMessage(
                        senderSession,
                        acceptorUserId,
                        ErrorResponse(MessageType.ACCEPT_REQUEST, errorMessage)
                    )
                })
    }
}
