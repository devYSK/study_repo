package com.yscorp.withpush.messagesystem.repository

import net.prostars.messagesystem.dto.projection.CountProjection

@org.springframework.stereotype.Repository
interface UserRepository : JpaRepository<UserEntity?, Long?> {
    fun findByUsername(@org.springframework.lang.NonNull username: String?): java.util.Optional<UserEntity?>?

    fun findByUsernameIn(@org.springframework.lang.NonNull usernames: Collection<String?>?): List<UserIdProjection?>?

    fun findByUserId(@org.springframework.lang.NonNull userId: Long?): java.util.Optional<UsernameProjection?>?

    fun findByInviteCode(@org.springframework.lang.NonNull inviteCode: String?): java.util.Optional<UserEntity?>?

    fun findInviteCodeByUserId(@org.springframework.lang.NonNull userId: Long?): java.util.Optional<InviteCodeProjection?>?

    fun findCountByUserId(@org.springframework.lang.NonNull userId: Long?): java.util.Optional<CountProjection?>?

    @org.springframework.data.jpa.repository.Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findForUpdateByUserId(@org.springframework.lang.NonNull userId: Long?): java.util.Optional<UserEntity?>?
}
