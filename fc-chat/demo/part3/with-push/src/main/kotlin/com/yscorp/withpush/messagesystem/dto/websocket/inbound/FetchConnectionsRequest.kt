package com.yscorp.withpush.messagesystem.dto.websocket.inbound

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.yscorp.withpush.messagesystem.constant.MessageType
import com.yscorp.withpush.messagesystem.constant.UserConnectionStatus

class FetchConnectionsRequest @JsonCreator constructor(@JsonProperty("status") val status: UserConnectionStatus) :
    BaseRequest(MessageType.FETCH_CONNECTIONS_REQUEST) {
}
