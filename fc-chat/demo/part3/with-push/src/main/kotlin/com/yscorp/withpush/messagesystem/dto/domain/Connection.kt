package com.yscorp.withpush.messagesystem.dto.domain

import com.yscorp.withpush.messagesystem.constant.UserConnectionStatus

class Connection(val username: String, status: UserConnectionStatus) {
    val status: UserConnectionStatus = status
}
