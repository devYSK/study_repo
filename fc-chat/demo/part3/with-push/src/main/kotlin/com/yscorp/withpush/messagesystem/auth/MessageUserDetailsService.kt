package com.yscorp.withpush.messagesystem.auth

import com.yscorp.withpush.messagesystem.entity.UserEntity
import com.yscorp.withpush.messagesystem.repository.UserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
@Suppress("unused")
class MessageUserDetailsService(userRepository: UserRepository) : UserDetailsService {
    private val userRepository: UserRepository = userRepository

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val userEntity: UserEntity =
            userRepository
                .findByUsername(username)
                ?: throw UsernameNotFoundException("")

        return MessageUserDetails(
            userEntity.userId, userEntity.username, userEntity.password
        )
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(MessageUserDetailsService::class.java)
    }
}
