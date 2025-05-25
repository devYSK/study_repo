package com.yscorp.withpush.messagesystem.dto.websocket.outbound

import net.prostars.messagesystem.constant.MessageType

class FetchConnectionsResponse(connections: List<Connection>) : BaseMessage(MessageType.FETCH_CONNECTIONS_RESPONSE) {
    private val connections: List<Connection> = connections

    fun getConnections(): List<Connection> {
        return connections
    }
}
