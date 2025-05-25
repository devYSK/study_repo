package com.yscorp.withpush.messagesystem.repository

import net.prostars.messagesystem.constant.UserConnectionStatus
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.lang.NonNull
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserConnectionRepository

    : JpaRepository<UserConnectionEntity?, UserConnectionId?> {
    fun findUserConnectionStatusByPartnerAUserIdAndPartnerBUserId(
        @NonNull partnerAUserId: Long?, @NonNull partnerBUserId: Long?
    ): Optional<UserConnectionStatusProjection?>?

    fun findByPartnerAUserIdAndPartnerBUserIdAndStatus(
        @NonNull partnerAUserId: Long?,
        @NonNull partnerBUserId: Long?,
        @NonNull status: UserConnectionStatus?
    ): Optional<UserConnectionEntity?>?

    fun findInviterUserIdByPartnerAUserIdAndPartnerBUserId(
        @NonNull partnerAUserId: Long?, @NonNull partnerBUserId: Long?
    ): Optional<InviterUserIdProjection?>?

    fun countByPartnerAUserIdAndPartnerBUserIdInAndStatus(
        @NonNull partnerAUserId: Long?,
        @NonNull partnerBUserIds: Collection<Long?>?,
        @NonNull status: UserConnectionStatus?
    ): Long

    fun countByPartnerBUserIdAndPartnerAUserIdInAndStatus(
        @NonNull partnerBUserId: Long?,
        @NonNull partnerAUserIds: Collection<Long?>?,
        @NonNull status: UserConnectionStatus?
    ): Long

    @Query(
        ("SELECT u.partnerBUserId AS userId, userB.username AS username, u.inviterUserId AS inviterUserId "
            + "FROM UserConnectionEntity u "
            + "INNER JOIN UserEntity userB ON u.partnerBUserId = userB.userId "
            + "WHERE u.partnerAUserId = :userId AND u.status = :status")
    )
    fun findByPartnerAUserIdAndStatus(
        @NonNull @Param("userId") userId: Long?, @NonNull @Param("status") status: UserConnectionStatus?
    ): List<UserIdUsernameInviterUserIdProjection?>?

    @Query(
        ("SELECT u.partnerAUserId AS userId, userA.username AS username, u.inviterUserId AS inviterUserId "
            + "FROM UserConnectionEntity u "
            + "INNER JOIN UserEntity userA ON u.partnerAUserId = userA.userId "
            + "WHERE u.partnerBUserId = :userId AND u.status = :status")
    )
    fun findByPartnerBUserIdAndStatus(
        @NonNull @Param("userId") userId: Long?, @NonNull @Param("status") status: UserConnectionStatus?
    ): List<UserIdUsernameInviterUserIdProjection?>?
}
