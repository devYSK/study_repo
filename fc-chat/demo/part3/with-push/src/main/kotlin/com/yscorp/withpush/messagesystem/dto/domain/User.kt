package com.yscorp.withpush.messagesystem.dto.domain

import java.util.*

@JvmRecord
data class User(val userId: UserId, val username: String) {
    override fun equals(o: Any?): Boolean {
        if (o == null || javaClass != o.javaClass) return false
        val user = o as User
        return userId == user.userId
    }

    override fun hashCode(): Int {
        return Objects.hashCode(userId)
    }
}
