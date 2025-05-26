package com.yscorp.withpush.messagesystem.service

import com.yscorp.withpush.messagesystem.constant.MessageType
import com.yscorp.withpush.messagesystem.dto.domain.ChannelId
import com.yscorp.withpush.messagesystem.dto.domain.UserId
import com.yscorp.withpush.messagesystem.dto.websocket.outbound.BaseMessage
import com.yscorp.withpush.messagesystem.entity.MessageEntity
import com.yscorp.withpush.messagesystem.json.JsonUtil
import com.yscorp.withpush.messagesystem.repository.MessageRepository
import com.yscorp.withpush.messagesystem.session.WebSocketSessionManager
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Service
class MessageService(
    private val channelService: ChannelService,
    private val pushService: PushService,
    private val messageRepository: MessageRepository,
    private val webSocketSessionManager: WebSocketSessionManager,
    private val jsonUtil: JsonUtil
) {
    private val senderThreadPool: ExecutorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE)

    init {
        // 클라이언트가 받을 수 있는 메시지 타입 등록
        pushService.registerPushMessageType(MessageType.NOTIFY_MESSAGE)
    }

    /**
     * 메시지를 저장하고, 모든 참여자(온라인/오프라인)에게 WebSocket 또는 푸시로 전송합니다.
     */
    fun sendMessage(senderUserId: UserId, content: String?, channelId: ChannelId, message: BaseMessage) {
        val payload = jsonUtil.toJson(message)

        if (payload == null) {
            log.error("Send message failed. MessageType: {}", message.type)
            return
        }

        try {
            messageRepository.save(MessageEntity(senderUserId.id, content))
        } catch (ex: Exception) {
            log.error("Send message failed. cause: {}", ex.message)
            return
        }

        val allParticipantIds = channelService.getParticipantIds(channelId)
        val onlineParticipantIds = channelService.getOnlineParticipantIds(channelId, allParticipantIds)

        allParticipantIds.forEachIndexed { idx, participantId ->
            if (participantId == senderUserId) return@forEachIndexed

            val isOnline = onlineParticipantIds.getOrNull(idx) != null

            if (isOnline) {
                CompletableFuture.runAsync({
                    try {
                        val session = webSocketSessionManager.getSession(participantId)
                        if (session != null) {
                            webSocketSessionManager.sendMessage(session, payload)
                        } else {
                            pushService.pushMessage(participantId, MessageType.NOTIFY_MESSAGE, payload)
                        }
                    } catch (ex: Exception) {
                        pushService.pushMessage(participantId, MessageType.NOTIFY_MESSAGE, payload)
                    }
                }, senderThreadPool)
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
