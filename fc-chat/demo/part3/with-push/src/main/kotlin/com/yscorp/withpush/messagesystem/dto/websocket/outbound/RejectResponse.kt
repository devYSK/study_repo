package com.yscorp.withpush.messagesystem.dto.websocket.outbound

import net.prostars.messagesystem.constant.MessageType

class RejectResponse(val username: String, status: UserConnectionStatus) : BaseMessage(MessageType.REJECT_RESPONSE) {
    private val status: UserConnectionStatus = status

    fun getStatus(): UserConnectionStatus {
        return status
    }
}
