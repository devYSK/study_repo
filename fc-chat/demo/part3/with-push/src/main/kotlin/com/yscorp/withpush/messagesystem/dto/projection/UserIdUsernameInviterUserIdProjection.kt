package com.yscorp.withpush.messagesystem.dto.projection

interface UserIdUsernameInviterUserIdProjection {
    val userId: Long

    val username: String

    val inviterUserId: Long
}
