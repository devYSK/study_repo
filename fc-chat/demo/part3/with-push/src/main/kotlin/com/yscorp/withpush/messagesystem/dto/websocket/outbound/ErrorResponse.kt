package com.yscorp.withpush.messagesystem.dto.websocket.outbound

import com.yscorp.withpush.messagesystem.constant.MessageType


class ErrorResponse(val messageType: String, val message: String) : BaseMessage(MessageType.ERROR)
