package com.yscorp.withpush.messagesystem.repository

import com.yscorp.withpush.messagesystem.dto.projection.UserIdProjection
import com.yscorp.withpush.messagesystem.entity.UserChannelEntity
import com.yscorp.withpush.messagesystem.entity.UserChannelId
import net.prostars.messagesystem.dto.projection.ChannelProjection
import org.springframework.data.jpa.repository.JpaRepository

@org.springframework.stereotype.Repository
interface UserChannelRepository : JpaRepository<UserChannelEntity, UserChannelId> {
    fun existsByUserIdAndChannelId(
        UserId: Long,
        ChannelId: Long
    ): Boolean

    fun findUserIdsByChannelId(channelId: Long): List<UserIdProjection>

    @org.springframework.data.jpa.repository.Query(
        ("SELECT c.channelId AS channelId, c.title AS title, c.headCount AS headCount "
            + "FROM UserChannelEntity uc "
            + "INNER JOIN ChannelEntity c ON uc.channelId = c.channelId "
            + "WHERE uc.userId = :userId")
    )
    fun findChannelsByUserId(@org.springframework.data.repository.query.Param("userId") userId: Long): List<ChannelProjection>

    fun deleteByUserIdAndChannelId(
        userId: Long,
        channelId: Long
    )
}
