package com.yscorp.withpush.messagesystem.entity

import java.io.Serializable
import java.util.*

class UserChannelId(
    val userId: Long,
    val channelId: Long
) : Serializable {

    override fun equals(o: Any?): Boolean {
        if (o == null || javaClass != o.javaClass) return false
        val that = o as UserChannelId
        return userId == that.userId && channelId == that.channelId
    }

    override fun hashCode(): Int {
        return Objects.hash(userId, channelId)
    }

    override fun toString(): String {
        return "ChannelId{UserId=%d, ChannelId=%d}".formatted(userId, channelId)
    }
}
