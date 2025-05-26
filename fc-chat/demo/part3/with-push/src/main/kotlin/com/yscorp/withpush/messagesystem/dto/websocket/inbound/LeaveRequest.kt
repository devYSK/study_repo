package com.yscorp.withpush.messagesystem.dto.websocket.inbound

import com.yscorp.withpush.messagesystem.constant.MessageType


class LeaveRequest @com.fasterxml.jackson.annotation.JsonCreator constructor() : BaseRequest(MessageType.LEAVE_REQUEST)
