package com.yscorp.withpush.messagesystem.dto.websocket.inbound

import com.yscorp.withpush.messagesystem.constant.MessageType

class FetchUserInvitecodeRequest @com.fasterxml.jackson.annotation.JsonCreator constructor() :
    BaseRequest(MessageType.FETCH_USER_INVITECODE_REQUEST)
