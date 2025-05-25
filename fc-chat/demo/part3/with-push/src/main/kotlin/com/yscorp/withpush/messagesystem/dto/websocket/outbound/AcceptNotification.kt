package com.yscorp.withpush.messagesystem.dto.websocket.outbound

import net.prostars.messagesystem.constant.MessageType

class AcceptNotification(val username: String) : BaseMessage(MessageType.NOTIFY_ACCEPT)
