package com.yscorp.withpush.messagesystem.service

import com.yscorp.withpush.messagesystem.dto.domain.InviteCode
import com.yscorp.withpush.messagesystem.dto.domain.User
import com.yscorp.withpush.messagesystem.dto.domain.UserId
import com.yscorp.withpush.messagesystem.entity.UserEntity
import com.yscorp.withpush.messagesystem.repository.UserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class UserService(
    private val sessionService: SessionService,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    fun getUsername(userId: UserId): String? {
        return userRepository.findByUserId(userId.id)?.username
    }

    fun getUserId(username: String): UserId? {
        return userRepository.findByUsername(username)?.let { UserId(it.userId) }
    }

    fun getUserIds(usernames: List<String>): List<UserId> {
        return userRepository.findByUsernameIn(usernames)
            .map { UserId(it.userId) }
    }

    fun getUser(inviteCode: InviteCode): User? {
        return userRepository.findByInviteCode(inviteCode.code)?.let {
            User(UserId(it.userId), it.username)
        }
    }

    fun getInviteCode(userId: UserId): InviteCode? {
        return userRepository.findInviteCodeByUserId(userId.id)?.inviteCode?.let(::InviteCode)
    }

    fun getConnectionCount(userId: UserId): Int? {
        return userRepository.findCountByUserId(userId.id)?.connectionCount
    }

    @Transactional
    fun addUser(username: String, password: String): UserId {
        val encodedPassword = passwordEncoder.encode(password)
        val userEntity = userRepository.save(UserEntity(username, encodedPassword))

        log.info("User registered. UserId: {}, Username: {}", userEntity.userId, userEntity.username)
        return UserId(userEntity.userId)
    }

    @Transactional
    fun removeUser() {
        val username = sessionService.username
        val userEntity = userRepository.findByUsername(username) ?: throw NoSuchElementException()
        userRepository.deleteById(userEntity.userId)

        log.info("User unregistered. UserId: {}, Username: {}", userEntity.userId, userEntity.username)
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(UserService::class.java)
    }
}
