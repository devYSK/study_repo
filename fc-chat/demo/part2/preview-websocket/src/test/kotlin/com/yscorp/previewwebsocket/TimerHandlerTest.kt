package com.yscorp.previewwebsocket

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue
import java.util.concurrent.TimeUnit

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TimerHandlerTest {

    @LocalServerPort
    var port: Int = 0

    private var session: WebSocketSession? = null

    @AfterEach
    fun tearDown() {
        session?.close()
    }

    @Test
    fun `timer 동작 테스트`() {
        val url = "ws://localhost:$port/ws/timer"
        val queue: BlockingQueue<String> = ArrayBlockingQueue(2)

        val client = StandardWebSocketClient()

        session = client.execute(object : TextWebSocketHandler() {
            override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
                queue.put(message.payload)
            }
        }, url).get()

        session!!.sendMessage(TextMessage("2"))

        val registerMsg = queue.poll(1, TimeUnit.SECONDS)
        assertTrue(registerMsg?.contains("등록 완료") == true, "등록 완료 메시지 없음")

        val completeMsg = queue.poll(5, TimeUnit.SECONDS)
        assertTrue(completeMsg?.contains("타이머 완료") == true, "타이머 완료 메시지 없음")
    }
}