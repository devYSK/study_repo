package com.yscorp.withpush.messagesystem.dto.websocket.outbound

import net.prostars.messagesystem.constant.MessageType

class InviteNotification(val username: String) : BaseMessage(MessageType.ASK_INVITE)
