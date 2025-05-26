package com.yscorp.withpush.messagesystem.entity

import com.yscorp.withpush.messagesystem.constant.UserConnectionStatus
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "user_connection")
@IdClass(UserConnectionId::class)
class UserConnectionEntity(
    @Id
    @Column(name = "partner_a_user_id", nullable = false)
    var partnerAUserId: Long? = null,

    @Id
    @Column(name = "partner_b_user_id", nullable = false)
    var partnerBUserId: Long? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    var status: UserConnectionStatus? = null,

    @Column(name = "inviter_user_id", nullable = false)
    var inviterUserId: Long? = null,
) : BaseEntity() {

    override fun equals(o: Any?): Boolean {
        if (o == null || javaClass != o.javaClass) return false
        val that = o as UserConnectionEntity
        return partnerAUserId == that.partnerAUserId
            && partnerBUserId == that.partnerBUserId
    }

    override fun hashCode(): Int {
        return Objects.hash(partnerAUserId, partnerBUserId)
    }

    override fun toString(): String {
        return "UserConnectionEntity{partnerAUserId=%d, partnerBUserId=%d, status=%s, inviterUserId=%d}"
            .formatted(partnerAUserId, partnerBUserId, status, inviterUserId)
    }
}
