package com.yscorp.withpush.messagesystem.repository

import net.prostars.messagesystem.dto.projection.ChannelProjection

@org.springframework.stereotype.Repository
interface ChannelRepository : JpaRepository<ChannelEntity?, Long?> {
    fun findChannelTitleByChannelId(@org.springframework.lang.NonNull channelId: Long?): java.util.Optional<ChannelTitleProjection?>?

    fun findChannelInviteCodeByChannelId(@org.springframework.lang.NonNull channelId: Long?): java.util.Optional<InviteCodeProjection?>?

    fun findChannelByInviteCode(@org.springframework.lang.NonNull inviteCode: String?): java.util.Optional<ChannelProjection?>?

    @org.springframework.data.jpa.repository.Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findForUpdateByChannelId(@org.springframework.lang.NonNull channelId: Long?): java.util.Optional<ChannelEntity?>?
}
