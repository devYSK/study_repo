package com.yscorp.withpush.messagesystem.dto.websocket.inbound

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.yscorp.withpush.messagesystem.constant.MessageType


class DisconnectRequest @JsonCreator constructor(
    @param:JsonProperty(
        "username"
    ) val username: String
) :
    BaseRequest(MessageType.DISCONNECT_REQUEST)
