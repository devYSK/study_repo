package com.yscorp.withpush.messagesystem.service

import net.prostars.messagesystem.dto.domain.UserId

@org.springframework.stereotype.Service
class PushService {
    private val pushMessageTypes = java.util.HashSet<String>()

    fun registerPushMessageType(messageType: String) {
        pushMessageTypes.add(messageType)
    }

    fun pushMessage(userId: UserId?, messageType: String, message: String?) {
        if (pushMessageTypes.contains(messageType)) {
            log.info("push message: {} to user: {}", message, userId)
        }
    }

    companion object {
        private val log: org.slf4j.Logger = org.slf4j.LoggerFactory.getLogger(PushService::class.java)
    }
}
