package com.yscorp.withpush.messagesystem.entity

import java.util.*

@Entity
@Table(name = "message")
class MessageEntity : BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_sequence")
    val messageSequence: Long? = null

    @Column(name = "user_id", nullable = false)
    var userId: Long? = null
        private set

    @Column(name = "content", nullable = false)
    var content: String? = null
        private set

    constructor()

    constructor(userId: Long?, content: String?) {
        this.userId = userId
        this.content = content
    }

    override fun equals(o: Any?): Boolean {
        if (o == null || javaClass != o.javaClass) return false
        val that = o as MessageEntity
        return messageSequence == that.messageSequence
    }

    override fun hashCode(): Int {
        return Objects.hashCode(messageSequence)
    }

    override fun toString(): String {
        return "MessageEntity{messageSequence=%d, userId=%d, content='%s', createAt=%s, updatedAt=%s}"
            .formatted(messageSequence, userId, content, createAt, updatedAt)
    }
}
