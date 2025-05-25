package com.yscorp.withpush.messagesystem.dto.websocket.inbound

import net.prostars.messagesystem.constant.MessageType

class LeaveRequest @com.fasterxml.jackson.annotation.JsonCreator constructor() : BaseRequest(MessageType.LEAVE_REQUEST)
