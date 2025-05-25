package com.yscorp.withpush.messagesystem.dto.websocket.outbound

import net.prostars.messagesystem.constant.MessageType

class JoinNotification(channelId: ChannelId, val title: String) : BaseMessage(MessageType.NOTIFY_JOIN) {
    private val channelId: ChannelId = channelId

    fun getChannelId(): ChannelId {
        return channelId
    }
}
