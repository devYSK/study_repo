package com.yscorp.withpush.messagesystem.repository

import com.yscorp.withpush.messagesystem.constant.UserConnectionStatus
import com.yscorp.withpush.messagesystem.dto.projection.InviterUserIdProjection
import com.yscorp.withpush.messagesystem.dto.projection.UserConnectionStatusProjection
import com.yscorp.withpush.messagesystem.dto.projection.UserIdUsernameInviterUserIdProjection
import com.yscorp.withpush.messagesystem.entity.UserConnectionEntity
import com.yscorp.withpush.messagesystem.entity.UserConnectionId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UserConnectionRepository : JpaRepository<UserConnectionEntity, UserConnectionId> {

    fun findUserConnectionStatusByPartnerAUserIdAndPartnerBUserId(
        partnerAUserId: Long,
        partnerBUserId: Long
    ): UserConnectionStatusProjection?

    fun findByPartnerAUserIdAndPartnerBUserIdAndStatus(
        partnerAUserId: Long,
        partnerBUserId: Long,
        status: UserConnectionStatus
    ): UserConnectionEntity?

    fun findInviterUserIdByPartnerAUserIdAndPartnerBUserId(
        partnerAUserId: Long,
        partnerBUserId: Long
    ): InviterUserIdProjection?

    fun countByPartnerAUserIdAndPartnerBUserIdInAndStatus(
        partnerAUserId: Long,
        partnerBUserIds: Collection<Long>,
        status: UserConnectionStatus?
    ): Long

    fun countByPartnerBUserIdAndPartnerAUserIdInAndStatus(
        partnerBUserId: Long,
        partnerAUserIds: Collection<Long>,
        status: UserConnectionStatus?
    ): Long

    @Query(
        """
        SELECT u.partnerBUserId AS userId, userB.username AS username, u.inviterUserId AS inviterUserId
        FROM UserConnectionEntity u
        INNER JOIN UserEntity userB ON u.partnerBUserId = userB.userId
        WHERE u.partnerAUserId = :userId AND u.status = :status
        """
    )
    fun findByPartnerAUserIdAndStatus(
        @Param("userId") userId: Long,
        @Param("status") status: UserConnectionStatus
    ): List<UserIdUsernameInviterUserIdProjection>

    @Query(
        """
        SELECT u.partnerAUserId AS userId, userA.username AS username, u.inviterUserId AS inviterUserId
        FROM UserConnectionEntity u
        INNER JOIN UserEntity userA ON u.partnerAUserId = userA.userId
        WHERE u.partnerBUserId = :userId AND u.status = :status
        """
    )
    fun findByPartnerBUserIdAndStatus(
        @Param("userId") userId: Long,
        @Param("status") status: UserConnectionStatus
    ): List<UserIdUsernameInviterUserIdProjection>
}
