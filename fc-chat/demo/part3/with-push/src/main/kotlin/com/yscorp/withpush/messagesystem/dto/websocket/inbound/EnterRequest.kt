package com.yscorp.withpush.messagesystem.dto.websocket.inbound

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.yscorp.withpush.messagesystem.constant.MessageType
import com.yscorp.withpush.messagesystem.dto.domain.ChannelId

class EnterRequest @JsonCreator constructor(@JsonProperty("channelId") channelId: ChannelId) :
    BaseRequest(MessageType.ENTER_REQUEST) {
    private val channelId: ChannelId = channelId

    fun getChannelId(): ChannelId {
        return channelId
    }
}
