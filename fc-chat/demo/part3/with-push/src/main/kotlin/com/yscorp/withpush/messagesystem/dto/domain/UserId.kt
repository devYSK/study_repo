package com.yscorp.withpush.messagesystem.dto.domain

@JvmRecord
data class UserId(val id: Long) {
    init {
        require(id > 0) { "Invalid UserId" }
    }
}
