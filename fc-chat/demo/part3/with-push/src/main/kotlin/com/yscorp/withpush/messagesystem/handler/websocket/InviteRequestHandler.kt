package com.yscorp.withpush.messagesystem.handler.websocket

import net.prostars.messagesystem.constant.IdKey
import org.springframework.data.util.Pair
import org.springframework.stereotype.Component
import java.util.*
import java.util.function.Consumer

@Component
@Suppress("unused")
class InviteRequestHandler(
    userConnectionService: UserConnectionService,
    clientNotificationService: ClientNotificationService
) : BaseRequestHandler<InviteRequest> {
    private val userConnectionService: UserConnectionService = userConnectionService
    private val clientNotificationService: ClientNotificationService = clientNotificationService

    override fun handleRequest(senderSession: WebSocketSession, request: InviteRequest) {
        val inviterUserId: UserId = senderSession.getAttributes().get(IdKey.USER_ID.getValue()) as UserId
        val result: Pair<Optional<UserId>, String> =
            userConnectionService.invite(inviterUserId, request.getUserInviteCode())
        result
            .first
            .ifPresentOrElse(
                Consumer<UserId> { partnerUserId: UserId? ->
                    val inviterUsername = result.second
                    clientNotificationService.sendMessage(
                        senderSession,
                        inviterUserId,
                        InviteResponse(request.getUserInviteCode(), UserConnectionStatus.PENDING)
                    )
                    clientNotificationService.sendMessage(
                        partnerUserId, InviteNotification(inviterUsername)
                    )
                },
                Runnable {
                    val errorMessage = result.second
                    clientNotificationService.sendMessage(
                        senderSession,
                        inviterUserId,
                        ErrorResponse(MessageType.INVITE_REQUEST, errorMessage)
                    )
                })
    }
}
