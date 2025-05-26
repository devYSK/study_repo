package com.yscorp.withpush.messagesystem.dto.websocket.outbound

import com.yscorp.withpush.messagesystem.constant.MessageType


class InviteNotification(val username: String) : BaseMessage(MessageType.ASK_INVITE)
