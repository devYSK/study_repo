package com.yscorp.withpush.messagesystem.dto.websocket.outbound

import net.prostars.messagesystem.constant.MessageType

class ErrorResponse(val messageType: String, val message: String) : BaseMessage(MessageType.ERROR)
