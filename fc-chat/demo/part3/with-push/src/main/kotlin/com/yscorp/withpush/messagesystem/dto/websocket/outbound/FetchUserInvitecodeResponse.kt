package com.yscorp.withpush.messagesystem.dto.websocket.outbound

import net.prostars.messagesystem.constant.MessageType

class FetchUserInvitecodeResponse(inviteCode: InviteCode) : BaseMessage(MessageType.FETCH_USER_INVITECODE_RESPONSE) {
    private val inviteCode: InviteCode = inviteCode

    fun getInviteCode(): InviteCode {
        return inviteCode
    }
}
