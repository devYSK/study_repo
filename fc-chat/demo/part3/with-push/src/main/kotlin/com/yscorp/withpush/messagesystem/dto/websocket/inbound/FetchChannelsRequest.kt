package com.yscorp.withpush.messagesystem.dto.websocket.inbound

import com.yscorp.withpush.messagesystem.constant.MessageType

class FetchChannelsRequest @com.fasterxml.jackson.annotation.JsonCreator constructor() :
    BaseRequest(MessageType.FETCH_CHANNELS_REQUEST)
