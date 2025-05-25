package com.yscorp.withpush.messagesystem.dto.websocket.outbound

import net.prostars.messagesystem.constant.MessageType

class MessageNotification(channelId: ChannelId, val username: String, val content: String) :
    BaseMessage(MessageType.NOTIFY_MESSAGE) {
    private val channelId: ChannelId = channelId

    fun getChannelId(): ChannelId {
        return channelId
    }
}
