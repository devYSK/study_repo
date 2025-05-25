package com.yscorp.withpush.messagesystem.handler.websocket

import net.prostars.messagesystem.constant.IdKey
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class FetchUserInvitecodeRequestHandler
    (userService: UserService, clientNotificationService: ClientNotificationService) :
    BaseRequestHandler<FetchUserInvitecodeRequest?> {
    private val userService: UserService = userService
    private val clientNotificationService: ClientNotificationService = clientNotificationService

    override fun handleRequest(senderSession: WebSocketSession, request: FetchUserInvitecodeRequest?) {
        val senderUserId: UserId = senderSession.getAttributes().get(IdKey.USER_ID.getValue()) as UserId
        userService
            .getInviteCode(senderUserId)
            .ifPresentOrElse(
                { inviteCode ->
                    clientNotificationService.sendMessage(
                        senderSession, senderUserId, FetchUserInvitecodeResponse(inviteCode)
                    )
                },
                {
                    clientNotificationService.sendMessage(
                        senderSession,
                        senderUserId,
                        ErrorResponse(
                            MessageType.FETCH_USER_INVITECODE_REQUEST,
                            "Fetch user invite code failed."
                        )
                    )
                })
    }
}
