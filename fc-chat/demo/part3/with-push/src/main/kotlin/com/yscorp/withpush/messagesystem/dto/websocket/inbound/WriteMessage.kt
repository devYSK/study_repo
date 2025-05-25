package com.yscorp.withpush.messagesystem.dto.websocket.inbound

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import net.prostars.messagesystem.constant.MessageType

class WriteMessage @JsonCreator constructor(
    @JsonProperty("channelId") channelId: ChannelId,
    @JsonProperty(
        "content"
    ) val content: String
) :
    BaseRequest(MessageType.WRITE_MESSAGE) {
    private val channelId: ChannelId = channelId

    fun getChannelId(): ChannelId {
        return channelId
    }
}
