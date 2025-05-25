package com.yscorp.withpush.messagesystem.service

import net.prostars.messagesystem.dto.domain.InviteCode
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class UserService(
    private val sessionService: SessionService,
    userRepository: UserRepository,
    passwordEncoder: PasswordEncoder
) {
    private val userRepository: UserRepository = userRepository
    private val passwordEncoder: PasswordEncoder = passwordEncoder

    fun getUsername(userId: UserId): Optional<String> {
        return userRepository.findByUserId(userId.id()).map(UsernameProjection::getUsername)
    }

    fun getUserId(username: String?): Optional<UserId?> {
        return userRepository
            .findByUsername(username)
            .map { userEntity -> UserId(userEntity.getUserId()) }
    }

    fun getUserIds(usernames: List<String?>?): List<UserId> {
        return userRepository.findByUsernameIn(usernames).stream()
            .map { projection -> UserId(projection.getUserId()) }
            .toList()
    }

    fun getUser(inviteCode: InviteCode): Optional<User> {
        return userRepository
            .findByInviteCode(inviteCode.code())
            .map { entity -> User(UserId(entity.getUserId()), entity.getUsername()) }
    }

    fun getInviteCode(userId: UserId): Optional<InviteCode> {
        return userRepository
            .findInviteCodeByUserId(userId.id())
            .map { inviteCode -> InviteCode(inviteCode.getInviteCode()) }
    }

    fun getConnectionCount(userId: UserId): Optional<Int?> {
        return userRepository.findCountByUserId(userId.id()).map(CountProjection::getConnectionCount)
    }

    @Transactional
    fun addUser(username: String?, password: String?): UserId {
        val userEntity: UserEntity =
            userRepository.save(UserEntity(username, passwordEncoder.encode(password)))
        log.info(
            "User registered. UserId: {}, Username: {}",
            userEntity.getUserId(),
            userEntity.getUsername()
        )
        return UserId(userEntity.getUserId())
    }

    @Transactional
    fun removeUser() {
        val username = sessionService.username
        val userEntity: UserEntity = userRepository.findByUsername(username).orElseThrow()
        userRepository.deleteById(userEntity.getUserId())

        log.info(
            "User unregistered. UserId: {}, Username: {}",
            userEntity.getUserId(),
            userEntity.getUsername()
        )
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(UserService::class.java)
    }
}
