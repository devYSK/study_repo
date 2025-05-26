package com.yscorp.withpush.messagesystem.entity

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "channel")
class ChannelEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "channel_id")
    val channelId: Long? = null,

    @Column(name = "title", nullable = false)
    var title: String,

    @Column(name = "invite_code", nullable = false)
    var inviteCode: String? = null,

    @Column(name = "head_count", nullable = false)
    var headCount: Int = 0,

    ) : BaseEntity() {

    constructor(title: String, headCount: Int) : this(
        title = title,
        inviteCode = UUID.randomUUID().toString().replace("-", ""),
        headCount = headCount
    ) {
    }

    override fun equals(o: Any?): Boolean {
        if (o == null || javaClass != o.javaClass) return false
        val that = o as ChannelEntity
        return channelId == that.channelId
    }

    override fun hashCode(): Int {
        return Objects.hashCode(channelId)
    }

    override fun toString(): String {
        return "ChannelEntity{channelId=%d, title='%s', inviteCode='%s', headCount=%d}"
            .formatted(channelId, title, inviteCode, headCount)
    }
}
