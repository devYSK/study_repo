package com.yscorp.withpush.messagesystem.dto.websocket.outbound

import net.prostars.messagesystem.constant.MessageType

class CreateResponse(channelId: ChannelId, val title: String) : BaseMessage(MessageType.CREATE_RESPONSE) {
    private val channelId: ChannelId = channelId

    fun getChannelId(): ChannelId {
        return channelId
    }
}
