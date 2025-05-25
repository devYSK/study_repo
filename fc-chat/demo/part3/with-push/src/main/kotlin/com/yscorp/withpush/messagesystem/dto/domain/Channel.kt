package com.yscorp.withpush.messagesystem.dto.domain

import java.util.*

@JvmRecord
data class Channel(val channelId: ChannelId, val title: String, val headCount: Int) {
    override fun equals(o: Any?): Boolean {
        if (o == null || javaClass != o.javaClass) return false
        val channel = o as Channel
        return channelId == channel.channelId
    }

    override fun hashCode(): Int {
        return Objects.hashCode(channelId)
    }
}
