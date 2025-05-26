package com.yscorp.withpush.messagesystem.dto.websocket.outbound

import com.yscorp.withpush.messagesystem.dto.domain.InviteCode
import com.yscorp.withpush.messagesystem.constant.MessageType

class FetchUserInvitecodeResponse(val inviteCode: InviteCode) : BaseMessage(MessageType.FETCH_USER_INVITECODE_RESPONSE) {
}
