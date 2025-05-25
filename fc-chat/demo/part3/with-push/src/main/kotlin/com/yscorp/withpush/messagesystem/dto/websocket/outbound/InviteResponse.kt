package com.yscorp.withpush.messagesystem.dto.websocket.outbound

import net.prostars.messagesystem.constant.MessageType

class InviteResponse(inviteCode: InviteCode, status: UserConnectionStatus) : BaseMessage(MessageType.INVITE_RESPONSE) {
    private val inviteCode: InviteCode = inviteCode
    private val status: UserConnectionStatus = status

    fun getInviteCode(): InviteCode {
        return inviteCode
    }

    fun getStatus(): UserConnectionStatus {
        return status
    }
}
