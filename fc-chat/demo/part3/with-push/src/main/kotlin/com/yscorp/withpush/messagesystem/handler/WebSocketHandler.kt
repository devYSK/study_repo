package com.yscorp.withpush.messagesystem.handler

import net.prostars.messagesystem.constant.IdKey
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.lang.NonNull
import org.springframework.stereotype.Component

@Component
class WebSocketHandler(
    requestDispatcher: RequestDispatcher,
    webSocketSessionManager: WebSocketSessionManager,
    jsonUtil: JsonUtil
) : TextWebSocketHandler() {
    private val jsonUtil: JsonUtil = jsonUtil

    private val webSocketSessionManager: WebSocketSessionManager = webSocketSessionManager

    private val requestDispatcher: RequestDispatcher = requestDispatcher

    override fun afterConnectionEstablished(session: WebSocketSession) {
        log.info("ConnectionEstablished: {}", session.getId())
        val concurrentWebSocketSessionDecorator: ConcurrentWebSocketSessionDecorator =
            ConcurrentWebSocketSessionDecorator(session, 5000, 100 * 1024)
        val userId: UserId = session.getAttributes().get(IdKey.USER_ID.getValue()) as UserId
        webSocketSessionManager.putSession(userId, concurrentWebSocketSessionDecorator)
    }

    override fun handleTransportError(session: WebSocketSession, exception: Throwable) {
        log.error("TransportError: [{}] from {}", exception.message, session.getId())
        val userId: UserId = session.getAttributes().get(IdKey.USER_ID.getValue()) as UserId
        webSocketSessionManager.closeSession(userId)
    }

    override fun afterConnectionClosed(session: WebSocketSession, @NonNull status: CloseStatus) {
        log.info("ConnectionClosed: [{}] from {}", status, session.getId())
        val userId: UserId = session.getAttributes().get(IdKey.USER_ID.getValue()) as UserId
        webSocketSessionManager.closeSession(userId)
    }

    override fun handleTextMessage(senderSession: WebSocketSession, @NonNull message: TextMessage) {
        val payload: String = message.getPayload()
        log.info("Received TextMessage: [{}] from {}", payload, senderSession.getId())
        jsonUtil
            .fromJson(payload, BaseRequest::class.java)
            .ifPresent { msg -> requestDispatcher.dispatchRequest(senderSession, msg) }
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(WebSocketHandler::class.java)
    }
}
