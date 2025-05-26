package com.yscorp.withpush.messagesystem.repository

import com.yscorp.withpush.messagesystem.dto.projection.CountProjection
import com.yscorp.withpush.messagesystem.dto.projection.InviteCodeProjection
import com.yscorp.withpush.messagesystem.dto.projection.UserIdProjection
import com.yscorp.withpush.messagesystem.dto.projection.UsernameProjection
import com.yscorp.withpush.messagesystem.entity.UserEntity
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<UserEntity, Long> {

    fun findByUsername(username: String): UserEntity?

    fun findByUsernameIn(usernames: Collection<String>): List<UserIdProjection>

    fun findByUserId(userId: Long): UsernameProjection?

    fun findByInviteCode(inviteCode: String): UserEntity?

    fun findInviteCodeByUserId(userId: Long): InviteCodeProjection?

    fun findCountByUserId(userId: Long): CountProjection?

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findForUpdateByUserId(userId: Long): UserEntity?
}
