package com.yscorp.withpush.messagesystem.dto.websocket.outbound

import com.yscorp.withpush.messagesystem.constant.MessageType
import com.yscorp.withpush.messagesystem.dto.domain.ChannelId
import com.yscorp.withpush.messagesystem.dto.domain.InviteCode


class FetchChannelInviteCodeResponse(val channelId: ChannelId, val inviteCode: InviteCode) :
    BaseMessage(MessageType.FETCH_CHANNEL_INVITECODE_RESPONSE) {
}
