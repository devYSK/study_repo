package com.yscorp.withpush.messagesystem.dto.websocket.outbound

import com.yscorp.withpush.messagesystem.constant.MessageType
import com.yscorp.withpush.messagesystem.dto.domain.ChannelId


class CreateResponse(val channelId: ChannelId, val title: String) : BaseMessage(MessageType.CREATE_RESPONSE) {

    fun getChannelId(): ChannelId {
        return channelId
    }
}
