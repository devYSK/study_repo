package com.yscorp.withpush.messagesystem.dto.domain

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

@JvmRecord
data class ChannelId @JsonCreator constructor(@field:JsonValue val id: Long) {
    init {
        require(!(id == null || id <= 0)) { "Invalid ChannelId" }
    }
}
