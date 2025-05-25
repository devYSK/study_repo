package com.yscorp.withpush.messagesystem.dto.websocket.outbound

import net.prostars.messagesystem.constant.MessageType

class FetchChannelsResponse(channels: List<Channel>) : BaseMessage(MessageType.FETCH_CHANNELS_RESPONSE) {
    private val channels: List<Channel> = channels

    fun getChannels(): List<Channel> {
        return channels
    }
}
