package com.yscorp.previewwebsocket.timer.handler

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.lang.NonNull
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.io.IOException
import java.time.Instant
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class TimerHandler : TextWebSocketHandler() {

    private val scheduledExecutorService: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()

    override fun afterConnectionEstablished(session: WebSocketSession) {
        log.info("Connection Established: {}", session.id)
    }

    override fun handleTextMessage(session: WebSocketSession, @NonNull message: TextMessage) {
        log.info("Received TextMessage: [{}] from {}", message, session.id)

        try {
            val seconds = message.payload.toInt()
            val timestamp = Instant.now().toEpochMilli()

            scheduledExecutorService.schedule(
                { sendMessage(session, String.format("%d에 등록한 %d초 타이머 완료.", timestamp, seconds)) },
                seconds.toLong(),
                TimeUnit.SECONDS
            )

            val responseMessage = String.format("%d에 %d초 타이머 등록 완료.", timestamp, seconds)
            sendMessage(session, responseMessage)
        } catch (ignored: NumberFormatException) {
            sendMessage(session, "정수가 아님. 타이머 등록 실패.")
        }
    }

    private fun sendMessage(session: WebSocketSession, message: String) {
        try {
            session.sendMessage(TextMessage(message))
            log.info("send message: {} to {}", message, session.id)
        } catch (ex: IOException) {
            log.error("메시지 전송 실패. error: {}", ex.message)
        }
    }

    override fun handleTransportError(session: WebSocketSession, exception: Throwable) {
        log.error("TransportError: [{}] from {}", exception.message, session.id)
    }

    override fun afterConnectionClosed(session: WebSocketSession, @NonNull status: CloseStatus) {
        log.info("Connection Closed: [{}] from {}", status, session.id)
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(TimerHandler::class.java)
    }
}
