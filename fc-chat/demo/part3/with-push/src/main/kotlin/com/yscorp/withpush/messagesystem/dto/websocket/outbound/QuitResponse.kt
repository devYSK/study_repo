package com.yscorp.withpush.messagesystem.dto.websocket.outbound

import com.yscorp.withpush.messagesystem.constant.MessageType
import com.yscorp.withpush.messagesystem.dto.domain.ChannelId

class QuitResponse(val channelId: ChannelId) : BaseMessage(MessageType.QUIT_RESPONSE) {
}
