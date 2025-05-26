package com.yscorp.withpush.messagesystem.dto.websocket.outbound

import com.yscorp.withpush.messagesystem.constant.MessageType
import com.yscorp.withpush.messagesystem.constant.UserConnectionStatus


class DisconnectResponse(val username: String, val status: UserConnectionStatus) :
    BaseMessage(MessageType.DISCONNECT_RESPONSE) {
}
