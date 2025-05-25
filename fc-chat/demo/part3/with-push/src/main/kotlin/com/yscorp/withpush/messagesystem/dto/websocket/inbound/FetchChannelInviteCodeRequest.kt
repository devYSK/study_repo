package com.yscorp.withpush.messagesystem.dto.websocket.inbound

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import net.prostars.messagesystem.constant.MessageType

class FetchChannelInviteCodeRequest @JsonCreator constructor(@JsonProperty("channelId") channelId: ChannelId) :
    BaseRequest(MessageType.FETCH_CHANNEL_INVITECODE_REQUEST) {
    private val channelId: ChannelId = channelId

    fun getChannelId(): ChannelId {
        return channelId
    }
}
