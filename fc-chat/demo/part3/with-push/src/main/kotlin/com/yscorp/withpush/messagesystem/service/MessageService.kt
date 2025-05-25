package com.yscorp.withpush.messagesystem.service

import net.prostars.messagesystem.constant.MessageType
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Service
class MessageService(
    private val channelService: ChannelService,
    private val pushService: PushService,
    messageRepository: MessageRepository,
    webSocketSessionManager: WebSocketSessionManager,
    jsonUtil: JsonUtil
) {
    private val messageRepository: MessageRepository = messageRepository
    private val webSocketSessionManager: WebSocketSessionManager = webSocketSessionManager
    private val jsonUtil: JsonUtil = jsonUtil
    private val senderThreadPool: ExecutorService = Executors.newFixedThreadPool(
        THREAD_POOL_SIZE
    )

    init {
        pushService.registerPushMessageType(MessageType.NOTIFY_MESSAGE)
    }

    fun sendMessage(
        senderUserId: UserId, content: String?, channelId: ChannelId, message: BaseMessage
    ) {
        val json: Optional<String> = jsonUtil.toJson(message)
        if (json.isEmpty) {
            log.error("Send message failed. MessageType: {}", message.getType())
            return
        }
        val payload = json.get()

        try {
            messageRepository.save(MessageEntity(senderUserId.id(), content))
        } catch (ex: Exception) {
            log.error("Send message failed. cause: {}", ex.message)
            return
        }

        val allParticipantIds: List<UserId?>? = channelService.getParticipantIds(channelId)
        val onlineParticipantIds: List<UserId?>? = channelService.getOnlineParticipantIds(
            channelId,
            allParticipantIds!!
        )

        for (idx in allParticipantIds.indices) {
            val participantId: UserId? = allParticipantIds[idx]
            if (senderUserId.equals(participantId)) {
                continue
            }
            if (onlineParticipantIds!![idx] != null) {
                CompletableFuture.runAsync(
                    {
                        try {
                            val participantSession: WebSocketSession =
                                webSocketSessionManager.getSession(participantId)
                            if (participantSession != null) {
                                webSocketSessionManager.sendMessage(participantSession, payload)
                            } else {
                                pushService.pushMessage(participantId, MessageType.NOTIFY_MESSAGE, payload)
                            }
                        } catch (ex: Exception) {
                            pushService.pushMessage(participantId, MessageType.NOTIFY_MESSAGE, payload)
                        }
                    },
                    senderThreadPool
                )
            } else {
                pushService.pushMessage(participantId, MessageType.NOTIFY_MESSAGE, payload)
            }
        }
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(MessageService::class.java)
        private const val THREAD_POOL_SIZE = 10
    }
}
