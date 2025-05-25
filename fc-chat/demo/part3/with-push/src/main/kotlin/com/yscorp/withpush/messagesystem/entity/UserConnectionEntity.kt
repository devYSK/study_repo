package com.yscorp.withpush.messagesystem.entity

import net.prostars.messagesystem.constant.UserConnectionStatus
import java.util.*

@Entity
@Table(name = "user_connection")
@IdClass(UserConnectionId::class)
class UserConnectionEntity : BaseEntity {
    @Id
    @Column(name = "partner_a_user_id", nullable = false)
    var partnerAUserId: Long? = null
        private set

    @Id
    @Column(name = "partner_b_user_id", nullable = false)
    var partnerBUserId: Long? = null
        private set

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private var status: UserConnectionStatus? = null

    @Column(name = "inviter_user_id", nullable = false)
    var inviterUserId: Long? = null
        private set

    constructor()

    constructor(partnerAUserId: Long?, partnerBUserId: Long?, status: UserConnectionStatus?, inviterUserId: Long?) {
        this.partnerAUserId = partnerAUserId
        this.partnerBUserId = partnerBUserId
        this.status = status
        this.inviterUserId = inviterUserId
    }

    fun getStatus(): UserConnectionStatus? {
        return status
    }

    fun setStatus(status: UserConnectionStatus?) {
        this.status = status
    }

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
