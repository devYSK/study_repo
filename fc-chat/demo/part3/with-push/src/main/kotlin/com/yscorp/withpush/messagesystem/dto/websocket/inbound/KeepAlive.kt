package com.yscorp.withpush.messagesystem.dto.websocket.inbound

import com.yscorp.withpush.messagesystem.constant.MessageType


class KeepAlive @com.fasterxml.jackson.annotation.JsonCreator constructor() : BaseRequest(MessageType.KEEP_ALIVE)
