package com.yscorp.withpush.messagesystem.dto.websocket.inbound

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import net.prostars.messagesystem.constant.MessageType

class JoinRequest @JsonCreator constructor(@JsonProperty("inviteCode") inviteCode: InviteCode) :
    BaseRequest(MessageType.JOIN_REQUEST) {
    private val inviteCode: InviteCode = inviteCode

    fun getInviteCode(): InviteCode {
        return inviteCode
    }
}
