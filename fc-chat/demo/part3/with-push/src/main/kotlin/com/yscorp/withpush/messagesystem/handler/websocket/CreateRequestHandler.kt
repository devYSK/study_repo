package com.yscorp.withpush.messagesystem.handler.websocket

import net.prostars.messagesystem.constant.IdKey
import org.springframework.data.util.Pair
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.function.Consumer

@Component
@Suppress("unused")
class CreateRequestHandler(
    channelService: ChannelService,
    userService: UserService,
    clientNotificationService: ClientNotificationService
) : BaseRequestHandler<CreateRequest?> {
    private val channelService: ChannelService = channelService
    private val userService: UserService = userService
    private val clientNotificationService: ClientNotificationService = clientNotificationService

    override fun handleRequest(senderSession: WebSocketSession, request: CreateRequest) {
        val senderUserId: UserId = senderSession.getAttributes().get(IdKey.USER_ID.getValue()) as UserId

        val participantIds: List<UserId> = userService.getUserIds(request.getParticipantUsernames())
        if (participantIds.isEmpty()) {
            clientNotificationService.sendMessage(
                senderSession,
                senderUserId,
                ErrorResponse(MessageType.CREATE_REQUEST, ResultType.NOT_FOUND.getMessage())
            )
            return
        }

        val result: Pair<Optional<Channel>, ResultType>
        try {
            result = channelService.create(senderUserId, participantIds, request.getTitle())
        } catch (ex: Exception) {
            clientNotificationService.sendMessage(
                senderSession,
                senderUserId,
                ErrorResponse(MessageType.CREATE_REQUEST, ResultType.FAILED.getMessage())
            )
            return
        }

        if (result.getFirst().isEmpty()) {
            clientNotificationService.sendMessage(
                senderSession,
                senderUserId,
                ErrorResponse(MessageType.CREATE_REQUEST, result.getSecond().getMessage())
            )
            return
        }

        val channel: Channel = result.getFirst().get()
        clientNotificationService.sendMessage(
            senderSession, senderUserId, CreateResponse(channel.channelId(), channel.title())
        )
        participantIds.forEach(
            Consumer<UserId> { participantId: UserId? ->
                CompletableFuture.runAsync {
                    clientNotificationService.sendMessage(
                        participantId,
                        JoinNotification(channel.channelId(), channel.title())
                    )
                }
            })
    }
}
