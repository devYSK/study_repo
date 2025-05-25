package com.yscorp.withpush.messagesystem.service

import net.prostars.messagesystem.constant.MessageType
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*

@Service
class ClientNotificationService(
    webSocketSessionManager: WebSocketSessionManager,
    private val pushService: PushService,
    jsonUtil: JsonUtil
) {
    private val webSocketSessionManager: WebSocketSessionManager = webSocketSessionManager
    private val jsonUtil: JsonUtil = jsonUtil

    init {
        pushService.registerPushMessageType(MessageType.INVITE_RESPONSE)
        pushService.registerPushMessageType(MessageType.ASK_INVITE)
        pushService.registerPushMessageType(MessageType.ACCEPT_RESPONSE)
        pushService.registerPushMessageType(MessageType.NOTIFY_ACCEPT)
        pushService.registerPushMessageType(MessageType.NOTIFY_JOIN)
        pushService.registerPushMessageType(MessageType.DISCONNECT_RESPONSE)
        pushService.registerPushMessageType(MessageType.REJECT_RESPONSE)
        pushService.registerPushMessageType(MessageType.CREATE_RESPONSE)
        pushService.registerPushMessageType(MessageType.NOTIFY_JOIN)
        pushService.registerPushMessageType(MessageType.QUIT_RESPONSE)
    }

    fun sendMessage(session: WebSocketSession?, userId: UserId, message: BaseMessage) {
        sendPayload(session, userId, message)
    }

    fun sendMessage(userId: UserId, message: BaseMessage) {
        sendPayload(webSocketSessionManager.getSession(userId), userId, message)
    }

    private fun sendPayload(session: WebSocketSession?, userId: UserId, message: BaseMessage) {
        val json: Optional<String> = jsonUtil.toJson(message)
        if (json.isEmpty) {
            log.error("Send message failed. MessageType: {}", message.getType())
            return
        }
        val payload = json.get()
        try {
            if (session != null) {
                webSocketSessionManager.sendMessage(session, payload)
            } else {
                pushService.pushMessage(userId, message.getType(), payload)
            }
        } catch (ex: Exception) {
            pushService.pushMessage(userId, message.getType(), payload)
        }
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(ClientNotificationService::class.java)
    }
}
