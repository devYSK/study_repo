package com.yscorp.withpush.messagesystem.handler.websocket

import com.yscorp.withpush.messagesystem.constant.IdKey
import com.yscorp.withpush.messagesystem.constant.MessageType
import com.yscorp.withpush.messagesystem.constant.ResultType
import com.yscorp.withpush.messagesystem.dto.domain.Channel
import com.yscorp.withpush.messagesystem.dto.domain.UserId
import com.yscorp.withpush.messagesystem.dto.websocket.inbound.CreateRequest
import com.yscorp.withpush.messagesystem.dto.websocket.outbound.CreateResponse
import com.yscorp.withpush.messagesystem.dto.websocket.outbound.ErrorResponse
import com.yscorp.withpush.messagesystem.dto.websocket.outbound.JoinNotification
import com.yscorp.withpush.messagesystem.service.ChannelService
import com.yscorp.withpush.messagesystem.service.ClientNotificationService
import com.yscorp.withpush.messagesystem.service.UserService
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketSession
import java.util.concurrent.CompletableFuture
import java.util.function.Consumer

@Component
@Suppress("unused")
class CreateRequestHandler(
    private val channelService: ChannelService,
    private val userService: UserService,
    private val clientNotificationService: ClientNotificationService
) : BaseRequestHandler<CreateRequest> {

    override fun handleRequest(senderSession: WebSocketSession, request: CreateRequest) {
        val senderUserId: UserId = senderSession.attributes[IdKey.USER_ID.value] as UserId

        val participantIds: List<UserId> = userService.getUserIds(request.participantUsernames)
        if (participantIds.isEmpty()) {
            clientNotificationService.sendMessage(
                senderSession,
                senderUserId,
                ErrorResponse(MessageType.CREATE_REQUEST, ResultType.NOT_FOUND.message)
            )
            return
        }

        val result: Pair<Channel?, ResultType>

        try {
            result = channelService.create(senderUserId, participantIds, request.title)
        } catch (ex: Exception) {
            clientNotificationService.sendMessage(
                senderSession,
                senderUserId,
                ErrorResponse(MessageType.CREATE_REQUEST, ResultType.FAILED.message)
            )
            return
        }

        if (result.first == null) {
            clientNotificationService.sendMessage(
                senderSession,
                senderUserId,
                ErrorResponse(MessageType.CREATE_REQUEST, result.second.message)
            )
            return
        }

        val channel: Channel = result.first!!

        clientNotificationService.sendMessage(
            senderSession, senderUserId, CreateResponse(channel.channelId, channel.title)
        )

        participantIds.forEach(
            Consumer { participantId: UserId? ->
                CompletableFuture.runAsync {
                    clientNotificationService.sendMessage(
                        participantId,
                        JoinNotification(channel.channelId, channel.title)
                    )
                }
            })
    }
}
