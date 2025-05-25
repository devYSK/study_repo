package com.yscorp.withpush.messagesystem.dto.websocket.inbound

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import net.prostars.messagesystem.constant.MessageType

class InviteRequest @JsonCreator constructor(@JsonProperty("userInviteCode") userInviteCode: InviteCode) :
    BaseRequest(MessageType.INVITE_REQUEST) {
    private val userInviteCode: InviteCode = userInviteCode

    fun getUserInviteCode(): InviteCode {
        return userInviteCode
    }
}
