package com.yscorp.withpush.messagesystem.dto.domain

import net.prostars.messagesystem.constant.UserConnectionStatus

class Connection(val username: String, status: UserConnectionStatus) {
    val status: UserConnectionStatus = status
}
