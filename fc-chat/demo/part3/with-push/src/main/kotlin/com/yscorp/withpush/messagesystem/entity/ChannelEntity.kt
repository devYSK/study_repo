package com.yscorp.withpush.messagesystem.entity

import java.util.*

@Entity
@Table(name = "channel")
class ChannelEntity : BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "channel_id")
    val channelId: Long? = null

    @Column(name = "title", nullable = false)
    var title: String? = null
        private set

    @Column(name = "invite_code", nullable = false)
    var inviteCode: String? = null
        private set

    @Column(name = "head_count", nullable = false)
    var headCount: Int = 0

    constructor()

    constructor(title: String?, headCount: Int) {
        this.title = title
        this.headCount = headCount
        this.inviteCode = UUID.randomUUID().toString().replace("-", "")
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
