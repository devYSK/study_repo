package com.yscorp.withpush.messagesystem.dto.websocket.outbound

import net.prostars.messagesystem.constant.MessageType

class EnterResponse(channelId: ChannelId, val title: String) : BaseMessage(MessageType.ENTER_RESPONSE) {
    private val channelId: ChannelId = channelId

    fun getChannelId(): ChannelId {
        return channelId
    }
}
