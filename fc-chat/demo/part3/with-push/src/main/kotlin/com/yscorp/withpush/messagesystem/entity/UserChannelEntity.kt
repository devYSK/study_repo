package com.yscorp.withpush.messagesystem.entity

import java.util.*

@Entity
@Table(name = "user_channel")
@IdClass(UserChannelId::class)
class UserChannelEntity : BaseEntity {
    @Id
    @Column(name = "user_id", nullable = false)
    var userId: Long? = null
        private set

    @Id
    @Column(name = "channel_id", nullable = false)
    var channelId: Long? = null
        private set

    @Column(name = "last_read_msg_seq", nullable = false)
    var lastReadMsgSeq: Long = 0

    constructor()

    constructor(userId: Long?, channelId: Long?, lastReadMsgSeq: Long) {
        this.userId = userId
        this.channelId = channelId
        this.lastReadMsgSeq = lastReadMsgSeq
    }

    override fun equals(o: Any?): Boolean {
        if (o == null || javaClass != o.javaClass) return false
        val that = o as UserChannelEntity
        return userId == that.userId && channelId == that.channelId
    }

    override fun hashCode(): Int {
        return Objects.hash(userId, channelId)
    }

    override fun toString(): String {
        return "UserChannelEntity{userId=%d, channelId=%d, lastReadMsgSeq=%d}"
            .formatted(userId, channelId, lastReadMsgSeq)
    }
}
