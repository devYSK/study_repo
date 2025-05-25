package com.yscorp.withpush.messagesystem.repository

import net.prostars.messagesystem.dto.projection.ChannelProjection

@org.springframework.stereotype.Repository
interface UserChannelRepository : JpaRepository<UserChannelEntity?, UserChannelId?> {
    fun existsByUserIdAndChannelId(
        @org.springframework.lang.NonNull UserId: Long?,
        @org.springframework.lang.NonNull ChannelId: Long?
    ): Boolean

    fun findUserIdsByChannelId(@org.springframework.lang.NonNull channelId: Long?): List<UserIdProjection?>?

    @org.springframework.data.jpa.repository.Query(
        ("SELECT c.channelId AS channelId, c.title AS title, c.headCount AS headCount "
            + "FROM UserChannelEntity uc "
            + "INNER JOIN ChannelEntity c ON uc.channelId = c.channelId "
            + "WHERE uc.userId = :userId")
    )
    fun findChannelsByUserId(@org.springframework.lang.NonNull @org.springframework.data.repository.query.Param("userId") userId: Long?): List<ChannelProjection?>?

    fun deleteByUserIdAndChannelId(
        @org.springframework.lang.NonNull userId: Long?,
        @org.springframework.lang.NonNull channelId: Long?
    )
}
