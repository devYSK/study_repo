package com.yscorp.withpush.messagesystem.dto.websocket.outbound

import com.yscorp.withpush.messagesystem.constant.MessageType
import com.yscorp.withpush.messagesystem.constant.UserConnectionStatus
import com.yscorp.withpush.messagesystem.dto.domain.InviteCode

class InviteResponse(val inviteCode: InviteCode,val  status: UserConnectionStatus) : BaseMessage(MessageType.INVITE_RESPONSE) {
}
