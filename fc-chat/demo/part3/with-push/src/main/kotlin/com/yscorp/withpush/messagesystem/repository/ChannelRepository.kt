package com.yscorp.withpush.messagesystem.repository

import com.yscorp.withpush.messagesystem.dto.projection.ChannelProjection
import com.yscorp.withpush.messagesystem.dto.projection.ChannelTitleProjection
import com.yscorp.withpush.messagesystem.dto.projection.InviteCodeProjection
import com.yscorp.withpush.messagesystem.entity.ChannelEntity
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository

@org.springframework.stereotype.Repository
interface ChannelRepository : JpaRepository<ChannelEntity, Long> {
    fun findChannelTitleByChannelId(channelId: Long): ChannelTitleProjection?

    fun findChannelInviteCodeByChannelId(channelId: Long): InviteCodeProjection?

    fun findChannelByInviteCode(inviteCode: String): ChannelProjection?

    @org.springframework.data.jpa.repository.Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findForUpdateByChannelId(channelId: Long): ChannelEntity?

}
