package com.yscorp.withpush.messagesystem.dto.websocket.outbound

import net.prostars.messagesystem.constant.MessageType

class DisconnectResponse(val username: String, status: UserConnectionStatus) :
    BaseMessage(MessageType.DISCONNECT_RESPONSE) {
    private val status: UserConnectionStatus = status

    fun getStatus(): UserConnectionStatus {
        return status
    }
}
