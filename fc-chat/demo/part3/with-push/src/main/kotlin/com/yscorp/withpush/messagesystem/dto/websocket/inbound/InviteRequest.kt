package com.yscorp.withpush.messagesystem.dto.websocket.inbound

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.yscorp.withpush.messagesystem.constant.MessageType
import com.yscorp.withpush.messagesystem.dto.domain.InviteCode


class InviteRequest @JsonCreator constructor(@JsonProperty("userInviteCode") val userInviteCode: InviteCode) :
    BaseRequest(MessageType.INVITE_REQUEST) {
}
