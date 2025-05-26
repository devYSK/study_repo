package com.yscorp.withpush.messagesystem.entity

import java.io.Serializable
import java.util.*

class UserConnectionId(
    val partnerAUserId: Long,
    val partnerBUserId: Long,
) : Serializable {

    override fun equals(o: Any?): Boolean {
        if (o == null || javaClass != o.javaClass) return false
        val that = o as UserConnectionId
        return partnerAUserId == that.partnerAUserId
            && partnerBUserId == that.partnerBUserId
    }

    override fun hashCode(): Int {
        return Objects.hash(partnerAUserId, partnerBUserId)
    }

    override fun toString(): String {
        return "UserConnectionId{partnerAUserId=%d, partnerBUserId=%d}"
            .formatted(partnerAUserId, partnerBUserId)
    }
}
