package com.yscorp.withpush.messagesystem.service

import com.yscorp.withpush.messagesystem.constant.UserConnectionStatus
import com.yscorp.withpush.messagesystem.dto.domain.InviteCode
import com.yscorp.withpush.messagesystem.dto.domain.User
import com.yscorp.withpush.messagesystem.dto.domain.UserId
import com.yscorp.withpush.messagesystem.entity.UserConnectionEntity
import com.yscorp.withpush.messagesystem.repository.UserConnectionRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class UserConnectionService(
    private val userService: UserService,
    private val userConnectionLimitService: UserConnectionLimitService,
    private val userConnectionRepository: UserConnectionRepository
) {

    @Transactional(readOnly = true)
    fun getUsersByStatus(userId: UserId, status: UserConnectionStatus): List<User> {
        val usersA = userConnectionRepository.findByPartnerAUserIdAndStatus(userId.id, status)
        val usersB = userConnectionRepository.findByPartnerBUserIdAndStatus(userId.id, status)

        return (usersA + usersB)
            .filter { status != UserConnectionStatus.ACCEPTED || it.inviterUserId != userId.id }
            .map { User(UserId(it.userId), it.username) }
    }

    fun getStatus(inviterUserId: UserId, partnerUserId: UserId): UserConnectionStatus {
        val a = inviterUserId.id
        val b = partnerUserId.id

        return userConnectionRepository
            .findUserConnectionStatusByPartnerAUserIdAndPartnerBUserId(minOf(a, b), maxOf(a, b))
            ?.status
            ?.let(UserConnectionStatus::valueOf)
            ?: UserConnectionStatus.NONE
    }

    @Transactional(readOnly = true)
    fun countConnectionStatus(senderUserId: UserId, partnerUserIds: List<UserId>, status: UserConnectionStatus?): Long {
        val ids = partnerUserIds.map { it.id }
        return userConnectionRepository.countByPartnerAUserIdAndPartnerBUserIdInAndStatus(senderUserId.id, ids, status) +
            userConnectionRepository.countByPartnerBUserIdAndPartnerAUserIdInAndStatus(senderUserId.id, ids, status)
    }

    fun invite(inviterUserId: UserId, inviteCode: InviteCode): Pair<UserId?, String> {
        val partner = userService.getUser(inviteCode) ?: return null to "Invalid invite code."

        val partnerUserId = partner.userId
        val partnerUsername = partner.username

        if (partnerUserId == inviterUserId) return null to "Can't self invite."

        return when (val status = getStatus(inviterUserId, partnerUserId)) {
            UserConnectionStatus.NONE, UserConnectionStatus.DISCONNECTED -> {
                val isLimitReached = userService.getConnectionCount(inviterUserId)
                    ?.let { it >= userConnectionLimitService.limitConnections } ?: false

                if (isLimitReached) return null to "Connection limit reached."

                val inviterUsername = userService.getUsername(inviterUserId) ?: return null to "InviteRequest failed."

                try {
                    setStatus(inviterUserId, partnerUserId, UserConnectionStatus.PENDING)
                    partnerUserId to inviterUsername
                } catch (ex: Exception) {
                    log.error("Set pending failed. cause: {}", ex.message)
                    null to "InviteRequest failed."
                }
            }

            UserConnectionStatus.ACCEPTED -> partnerUserId to "Already connected with $partnerUsername"

            UserConnectionStatus.PENDING, UserConnectionStatus.REJECTED -> {
                log.info("{} invites {} but does not deliver the invitation request.", inviterUserId, partnerUsername)
                partnerUserId to "Already invited to $partnerUsername"
            }
        }
    }

    @Transactional
    fun accept(acceptorUserId: UserId, inviterUsername: String): Pair<UserId?, String> {
        val inviterUserId = userService.getUserId(inviterUsername) ?: return null to "Invalid username."

        if (acceptorUserId == inviterUserId) return null to "Can't self accept."

        val actualInviter = getInviterUserId(acceptorUserId, inviterUserId)
        if (actualInviter != inviterUserId) return null to "Invalid username."

        val status = getStatus(inviterUserId, acceptorUserId)
        if (status == UserConnectionStatus.ACCEPTED) return null to "Already connected."
        if (status != UserConnectionStatus.PENDING) return null to "Accept failed."

        val acceptorUsername = userService.getUsername(acceptorUserId) ?: return null to "Accept failed."

        return try {
            userConnectionLimitService.accept(acceptorUserId, inviterUserId)
            inviterUserId to acceptorUsername
        } catch (ex: IllegalStateException) {
            null to ex.message!!
        } catch (ex: Exception) {
            log.error("Accept failed. cause: {}", ex.message)
            null to "Accept failed."
        }
    }

    fun reject(senderUserId: UserId, inviterUsername: String): Pair<Boolean, String> {
        val inviterUserId = userService.getUserId(inviterUsername) ?: return false to "Reject failed."

        if (inviterUserId == senderUserId) return false to "Reject failed."

        val actualInviter = getInviterUserId(inviterUserId, senderUserId)
        if (actualInviter != inviterUserId) return false to "Reject failed."

        if (getStatus(inviterUserId, senderUserId) != UserConnectionStatus.PENDING) {
            return false to "Reject failed."
        }

        return try {
            setStatus(inviterUserId, senderUserId, UserConnectionStatus.REJECTED)
            true to inviterUsername
        } catch (ex: Exception) {
            log.error("Set rejected failed. cause: {}", ex.message)
            false to "Reject failed."
        }
    }

    @Transactional
    fun disconnect(senderUserId: UserId, partnerUsername: String): Pair<Boolean, String> {
        val partnerUserId = userService.getUserId(partnerUsername) ?: return false to "Disconnect failed."
        if (senderUserId == partnerUserId) return false to "Disconnect failed."

        return try {
            val status = getStatus(senderUserId, partnerUserId)
            if (status == UserConnectionStatus.ACCEPTED) {
                userConnectionLimitService.disconnect(senderUserId, partnerUserId)
                true to partnerUsername
            } else if (
                status == UserConnectionStatus.REJECTED &&
                getInviterUserId(senderUserId, partnerUserId) == partnerUserId
            ) {
                setStatus(senderUserId, partnerUserId, UserConnectionStatus.DISCONNECTED)
                true to partnerUsername
            } else {
                false to "Disconnect failed."
            }
        } catch (ex: Exception) {
            log.error("Disconnect failed. cause: {}", ex.message)
            false to "Disconnect failed."
        }
    }

    private fun getInviterUserId(partnerAUserId: UserId, partnerBUserId: UserId): UserId? {
        return userConnectionRepository
            .findInviterUserIdByPartnerAUserIdAndPartnerBUserId(
                minOf(partnerAUserId.id, partnerBUserId.id),
                maxOf(partnerAUserId.id, partnerBUserId.id)
            )
            ?.inviterUserId
            ?.let(::UserId)
    }

    @Transactional
    fun setStatus(inviterUserId: UserId, partnerUserId: UserId, status: UserConnectionStatus) {
        require(status != UserConnectionStatus.ACCEPTED) { "Can't set to accepted." }

        userConnectionRepository.save(
            UserConnectionEntity(
                minOf(inviterUserId.id, partnerUserId.id),
                maxOf(inviterUserId.id, partnerUserId.id),
                status,
                inviterUserId.id
            )
        )
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(UserConnectionService::class.java)
    }
}
