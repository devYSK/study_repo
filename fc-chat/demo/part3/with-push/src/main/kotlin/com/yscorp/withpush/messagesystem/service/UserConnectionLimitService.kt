package com.yscorp.withpush.messagesystem.service

import com.yscorp.withpush.messagesystem.constant.UserConnectionStatus
import com.yscorp.withpush.messagesystem.dto.domain.UserId
import com.yscorp.withpush.messagesystem.repository.UserConnectionRepository
import com.yscorp.withpush.messagesystem.repository.UserRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserConnectionLimitService(
    private val userRepository: UserRepository,
    private val userConnectionRepository: UserConnectionRepository
) {

    var limitConnections: Int = 1000

    /**
     * 친구 요청 수락 처리.
     * 두 유저의 연결 수를 증가시키고, 상태를 ACCEPTED로 변경.
     * 제한을 초과하면 예외 발생.
     */
    @Transactional
    fun accept(acceptorUserId: UserId, inviterUserId: UserId) {
        val firstUserId = minOf(acceptorUserId.id, inviterUserId.id)
        val secondUserId = maxOf(acceptorUserId.id, inviterUserId.id)

        val firstUser = userRepository.findForUpdateByUserId(firstUserId)
            ?: throw EntityNotFoundException("Invalid userId: $firstUserId")
        val secondUser = userRepository.findForUpdateByUserId(secondUserId)
            ?: throw EntityNotFoundException("Invalid userId: $secondUserId")
        val connection = userConnectionRepository
            .findByPartnerAUserIdAndPartnerBUserIdAndStatus(
                firstUserId, secondUserId, UserConnectionStatus.PENDING
            ) ?: throw EntityNotFoundException("Invalid status.")

        fun errorMessage(userId: Long): String =
            if (userId == acceptorUserId.id) "Connection limit reached."
            else "Connection limit reached by the other user."

        check(firstUser.connectionCount < limitConnections) { errorMessage(firstUserId) }
        check(secondUser.connectionCount < limitConnections) { errorMessage(secondUserId) }

        firstUser.connectionCount += 1
        secondUser.connectionCount += 1
        connection.status = UserConnectionStatus.ACCEPTED
    }

    /**
     * 친구 연결 해제 처리.
     * 두 유저의 연결 수를 감소시키고, 상태를 DISCONNECTED로 변경.
     * 연결 수가 이미 0이면 예외 발생.
     */
    @Transactional
    fun disconnect(senderUserId: UserId, partnerUserId: UserId) {
        val firstUserId = minOf(senderUserId.id, partnerUserId.id)
        val secondUserId = maxOf(senderUserId.id, partnerUserId.id)

        val firstUser = userRepository.findForUpdateByUserId(firstUserId)
            ?: throw EntityNotFoundException("Invalid userId: $firstUserId")
        val secondUser = userRepository.findForUpdateByUserId(secondUserId)
            ?: throw EntityNotFoundException("Invalid userId: $secondUserId")
        val connection = userConnectionRepository
            .findByPartnerAUserIdAndPartnerBUserIdAndStatus(
                firstUserId, secondUserId, UserConnectionStatus.ACCEPTED
            ) ?: throw EntityNotFoundException("Invalid status.")

        check(firstUser.connectionCount > 0) { "Count is already zero. userId: $firstUserId" }
        check(secondUser.connectionCount > 0) { "Count is already zero. userId: $secondUserId" }

        firstUser.connectionCount -= 1
        secondUser.connectionCount -= 1
        connection.status = UserConnectionStatus.DISCONNECTED
    }
}
