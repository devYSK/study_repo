package com.yscorp.withpush.messagesystem.entity

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "message_user")
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    val userId: Long = 0,

    @Column(name = "username", nullable = false)
    var username: String,

    @Column(name = "password", nullable = false)
    var password: String,

    @Column(name = "invite_code", nullable = false)
    var inviteCode: String,

    @Column(name = "connection_count", nullable = false)
    var connectionCount: Int = 0
) : BaseEntity() {

    constructor(username: String, password: String) : this() {
        this.username = username
        this.password = password
        this.inviteCode = UUID.randomUUID().toString().replace("-", "")
    }

    constructor() : this(0, "", "", ""){
    }

    override fun equals(o: Any?): Boolean {
        if (o == null || javaClass != o.javaClass) return false
        val that = o as UserEntity
        return username == that.username
    }

    override fun hashCode(): Int {
        return Objects.hashCode(username)
    }

    override fun toString(): String {
        return "MessageUserEntity{userId=%d, username='%s', createAt=%s, updatedAt=%s}"
            .formatted(userId, username, createAt, updatedAt)
    }
}
