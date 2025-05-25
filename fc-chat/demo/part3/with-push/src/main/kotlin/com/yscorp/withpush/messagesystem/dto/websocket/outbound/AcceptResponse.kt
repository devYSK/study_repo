package com.yscorp.withpush.messagesystem.dto.websocket.outbound

import net.prostars.messagesystem.constant.MessageType

class AcceptResponse(val username: String) : BaseMessage(MessageType.ACCEPT_RESPONSE)
