package com.yscorp.withpush.messagesystem.dto.websocket.outbound

import com.yscorp.withpush.messagesystem.constant.MessageType
import com.yscorp.withpush.messagesystem.dto.domain.ChannelId

class MessageNotification(val channelId: ChannelId, val username: String, val content: String) :
    BaseMessage(MessageType.NOTIFY_MESSAGE) {
}
