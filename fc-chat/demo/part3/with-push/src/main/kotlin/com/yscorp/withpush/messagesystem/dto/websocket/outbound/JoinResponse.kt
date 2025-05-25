package com.yscorp.withpush.messagesystem.dto.websocket.outbound

import net.prostars.messagesystem.constant.MessageType

class JoinResponse(channelId: ChannelId, val title: String) : BaseMessage(MessageType.JOIN_RESPONSE) {
    private val channelId: ChannelId = channelId

    fun getChannelId(): ChannelId {
        return channelId
    }
}
