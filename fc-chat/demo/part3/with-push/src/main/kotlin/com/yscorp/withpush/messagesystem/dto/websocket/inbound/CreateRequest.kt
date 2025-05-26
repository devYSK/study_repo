package com.yscorp.withpush.messagesystem.dto.websocket.inbound

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.yscorp.withpush.messagesystem.constant.MessageType

class CreateRequest @JsonCreator constructor(
    @param:JsonProperty("title") val title: String,
    @param:JsonProperty("participantUsernames") val participantUsernames: List<String>
) : BaseRequest(MessageType.CREATE_REQUEST)
