package com.yscorp.withpush.messagesystem.dto.websocket.outbound

import com.yscorp.withpush.messagesystem.constant.MessageType

class AcceptNotification(val username: String) : BaseMessage(MessageType.NOTIFY_ACCEPT)
