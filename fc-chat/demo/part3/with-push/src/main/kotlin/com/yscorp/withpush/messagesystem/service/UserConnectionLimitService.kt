package com.yscorp.withpush.messagesystem.service

import net.prostars.messagesystem.constant.UserConnectionStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.function.Function

@Service
class UserConnectionLimitService(userRepository: UserRepository, userConnectionRepository: UserConnectionRepository) {
    private val userRepository: UserRepository = userRepository
    private val userConnectionRepository: UserConnectionRepository = userConnectionRepository
    var limitConnections: Int = 1000

    @Transactional
    fun accept(acceptorUserId: UserId, inviterUserId: UserId) {
        val firstUserId = java.lang.Long.min(acceptorUserId.id(), inviterUserId.id())
        val secondUserId = java.lang.Long.max(acceptorUserId.id(), inviterUserId.id())
        val firstUserEntity: UserEntity =
            userRepository
                .findForUpdateByUserId(firstUserId)
                .orElseThrow { EntityNotFoundException("Invalid userId: $firstUserId") }
        val secondUserEntity: UserEntity =
            userRepository
                .findForUpdateByUserId(secondUserId)
                .orElseThrow { EntityNotFoundException("Invalid userId: $secondUserId") }
        val userConnectionEntity: UserConnectionEntity =
            userConnectionRepository
                .findByPartnerAUserIdAndPartnerBUserIdAndStatus(
                    firstUserId, secondUserId, UserConnectionStatus.PENDING
                )
                .orElseThrow { EntityNotFoundException("Invalid status.") }

        val getErrorMessage =
            Function { userId: Long ->
                if (userId == acceptorUserId.id())
                    "Connection limit reached."
                else
                    "Connection limit reached by the other user."
            }

        val firstConnectionCount: Int = firstUserEntity.getConnectionCount()
        check(firstConnectionCount < limitConnections) { getErrorMessage.apply(firstUserId) }
        val secondConnectionCount: Int = secondUserEntity.getConnectionCount()
        check(secondConnectionCount < limitConnections) { getErrorMessage.apply(secondUserId) }

        firstUserEntity.setConnectionCount(firstConnectionCount + 1)
        secondUserEntity.setConnectionCount(secondConnectionCount + 1)
        userConnectionEntity.setStatus(UserConnectionStatus.ACCEPTED)
    }

    @Transactional
    fun disconnect(senderUserId: UserId, partnerUserId: UserId) {
        val firstUserId = java.lang.Long.min(senderUserId.id(), partnerUserId.id())
        val secondUserId = java.lang.Long.max(senderUserId.id(), partnerUserId.id())
        val firstUserEntity: UserEntity =
            userRepository
                .findForUpdateByUserId(firstUserId)
                .orElseThrow { EntityNotFoundException("Invalid userId: $firstUserId") }
        val secondUserEntity: UserEntity =
            userRepository
                .findForUpdateByUserId(secondUserId)
                .orElseThrow { EntityNotFoundException("Invalid userId: $secondUserId") }
        val userConnectionEntity: UserConnectionEntity =
            userConnectionRepository
                .findByPartnerAUserIdAndPartnerBUserIdAndStatus(
                    firstUserId, secondUserId, UserConnectionStatus.ACCEPTED
                )
                .orElseThrow { EntityNotFoundException("Invalid status.") }

        val firstConnectionCount: Int = firstUserEntity.getConnectionCount()
        check(firstConnectionCount > 0) { "Count is already zero. userId: $firstUserId" }
        val secondConnectionCount: Int = secondUserEntity.getConnectionCount()
        check(secondConnectionCount > 0) { "Count is already zero. userId: $senderUserId" }

        firstUserEntity.setConnectionCount(firstConnectionCount - 1)
        secondUserEntity.setConnectionCount(secondConnectionCount - 1)
        userConnectionEntity.setStatus(UserConnectionStatus.DISCONNECTED)
    }
}
