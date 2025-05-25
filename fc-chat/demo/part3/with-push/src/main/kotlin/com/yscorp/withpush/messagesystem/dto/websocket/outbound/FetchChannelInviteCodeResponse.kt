package com.yscorp.withpush.messagesystem.dto.websocket.outbound

import net.prostars.messagesystem.constant.MessageType

class FetchChannelInviteCodeResponse(channelId: ChannelId, inviteCode: InviteCode) :
    BaseMessage(MessageType.FETCH_CHANNEL_INVITECODE_RESPONSE) {
    private val channelId: ChannelId = channelId
    private val inviteCode: InviteCode = inviteCode

    fun getChannelId(): ChannelId {
        return channelId
    }

    fun getInviteCode(): InviteCode {
        return inviteCode
    }
}
