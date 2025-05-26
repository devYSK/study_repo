package com.yscorp.withpush.messagesystem.dto.websocket.outbound

import com.yscorp.withpush.messagesystem.constant.MessageType


class AcceptResponse(val username: String) : BaseMessage(MessageType.ACCEPT_RESPONSE)
