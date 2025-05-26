package com.yscorp.withpush.messagesystem.dto.websocket.inbound

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.yscorp.withpush.messagesystem.constant.MessageType
import com.yscorp.withpush.messagesystem.dto.domain.ChannelId


class WriteMessage @JsonCreator constructor(
    @JsonProperty("channelId") val channelId: ChannelId,
    @JsonProperty(
        "content"
    ) val content: String
) :
    BaseRequest(MessageType.WRITE_MESSAGE) {

    }
