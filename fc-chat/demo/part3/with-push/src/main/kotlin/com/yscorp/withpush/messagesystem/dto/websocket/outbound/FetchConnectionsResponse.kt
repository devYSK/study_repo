package com.yscorp.withpush.messagesystem.dto.websocket.outbound

import com.yscorp.withpush.messagesystem.constant.MessageType
import com.yscorp.withpush.messagesystem.dto.domain.Connection

class FetchConnectionsResponse(val connections: List<Connection>) : BaseMessage(MessageType.FETCH_CONNECTIONS_RESPONSE) {
}
