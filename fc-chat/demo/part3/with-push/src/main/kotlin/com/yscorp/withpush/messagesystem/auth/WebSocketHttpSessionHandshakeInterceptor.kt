package com.yscorp.withpush.messagesystem.auth

import com.yscorp.withpush.messagesystem.constant.IdKey
import com.yscorp.withpush.messagesystem.dto.domain.UserId
import jakarta.servlet.http.HttpSession
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.http.server.ServletServerHttpRequest
import org.springframework.lang.NonNull
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor

@Component
class WebSocketHttpSessionHandshakeInterceptor : HttpSessionHandshakeInterceptor() {
    override fun beforeHandshake(
        @NonNull request: ServerHttpRequest,
        @NonNull response: ServerHttpResponse,
        @NonNull wsHandler: WebSocketHandler,
        @NonNull attributes: MutableMap<String, Any>
    ): Boolean {
        if (request is ServletServerHttpRequest) {
            val authentication: Authentication? = SecurityContextHolder.getContext().getAuthentication()
            if (authentication == null) {
                log.warn("WebSocket handshake failed. authentication is null.")
                response.setStatusCode(HttpStatus.UNAUTHORIZED)
                return false
            }
            val httpSession: HttpSession? = request.servletRequest.getSession(false)
            if (httpSession == null) {
                log.info("WebSocket handshake failed. httpSession is null")
                response.setStatusCode(HttpStatus.UNAUTHORIZED)
                return false
            }
            val messageUserDetails = authentication.principal as MessageUserDetails
            attributes[IdKey.HTTP_SESSION_ID.value] = httpSession.id
            attributes[IdKey.USER_ID.value] = UserId(messageUserDetails.userId)
            return true
        } else {
            log.info("WebSocket handshake failed. request is {}", request.javaClass)
            response.setStatusCode(HttpStatus.BAD_REQUEST)
            return false
        }
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(WebSocketHttpSessionHandshakeInterceptor::class.java)
    }
}
