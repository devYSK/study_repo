package com.yscorp.withpush.messagesystem.dto.domain

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

@JvmRecord
data class InviteCode @JsonCreator constructor(@field:JsonValue val code: String?) {
    init {
        require(!(code == null || code.isEmpty())) { "Invalid InviteCode" }
    }
}
