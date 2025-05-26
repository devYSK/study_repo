package com.yscorp.withpush.messagesystem.dto.websocket.outbound

import com.yscorp.withpush.messagesystem.constant.MessageType
import com.yscorp.withpush.messagesystem.dto.domain.Channel


class FetchChannelsResponse(val channels: List<Channel>) : BaseMessage(MessageType.FETCH_CHANNELS_RESPONSE) {
}
