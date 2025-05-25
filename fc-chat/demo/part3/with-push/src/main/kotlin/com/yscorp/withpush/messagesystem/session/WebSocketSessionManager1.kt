package com.yscorp.withpush.messagesystem.session

import com.yscorp.withpush.messagesystem.dto.domain.UserId
import com.yscorp.withpush.messagesystem.json.JsonUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import java.io.IOException
import java.util.concurrent.ConcurrentHashMap

@Component
class WebSocketSessionManager(jsonUtil: JsonUtil) {
    private val sessions: MutableMap<UserId, WebSocketSession> = ConcurrentHashMap<UserId, WebSocketSession>()
    private val jsonUtil: JsonUtil = jsonUtil

    fun getSessions(): List<WebSocketSession> {
        return sessions.values.stream().toList()
    }

    fun getSession(userId: UserId): WebSocketSession? {
        return sessions[userId]
    }

    fun putSession(userId: UserId, webSocketSession: WebSocketSession) {
        log.info("Store Session : {}", webSocketSession.getId())
        sessions[userId] = webSocketSession
    }

    fun closeSession(userId: UserId) {
        try {
            val webSocketSession: WebSocketSession? = sessions.remove(userId)
            if (webSocketSession != null) {
                log.info("Remove session : {}", userId)
                webSocketSession.close()
                log.info("Close session : {}", userId)
            }
        } catch (ex: Exception) {
            log.error("WebSocketSession close failed. userId: {}", userId)
        }
    }

    @Throws(IOException::class)
    fun sendMessage(session: WebSocketSession, message: String) {
        try {
            session.sendMessage(TextMessage(message))
            log.info("Send message: {} to {}", message, session.getId())
        } catch (ex: Exception) {
            log.error("Failed to send message to {} error: {}", session.getId(), ex.message)

            throw ex
        }
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(WebSocketSessionManager::class.java)
    }
}
