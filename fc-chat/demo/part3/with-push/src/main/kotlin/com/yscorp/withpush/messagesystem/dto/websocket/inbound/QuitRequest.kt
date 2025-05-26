package com.yscorp.withpush.messagesystem.dto.websocket.inbound

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.yscorp.withpush.messagesystem.constant.MessageType
import com.yscorp.withpush.messagesystem.dto.domain.ChannelId


class QuitRequest @JsonCreator constructor(@JsonProperty("channelId") val channelId: ChannelId) :
    BaseRequest(MessageType.QUIT_REQUEST) {
}
