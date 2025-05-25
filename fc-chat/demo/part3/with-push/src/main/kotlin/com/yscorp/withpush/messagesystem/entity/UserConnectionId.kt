package com.yscorp.withpush.messagesystem.entity

import java.io.Serializable
import java.util.*

class UserConnectionId : Serializable {
    var partnerAUserId: Long? = null
        private set
    var partnerBUserId: Long? = null
        private set

    constructor()

    constructor(partnerAUserId: Long?, partnerBUserId: Long?) {
        this.partnerAUserId = partnerAUserId
        this.partnerBUserId = partnerBUserId
    }

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
