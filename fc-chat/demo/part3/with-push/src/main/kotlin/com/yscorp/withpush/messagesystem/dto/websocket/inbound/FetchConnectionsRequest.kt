package com.yscorp.withpush.messagesystem.dto.websocket.inbound

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import net.prostars.messagesystem.constant.MessageType

class FetchConnectionsRequest @JsonCreator constructor(@JsonProperty("status") status: UserConnectionStatus) :
    BaseRequest(MessageType.FETCH_CONNECTIONS_REQUEST) {
    private val status: UserConnectionStatus = status

    fun getStatus(): UserConnectionStatus {
        return status
    }
}
