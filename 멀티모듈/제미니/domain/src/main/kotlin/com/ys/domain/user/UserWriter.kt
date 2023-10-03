package com.ys.domain.user

import org.springframework.stereotype.Repository

@Repository
class UserWriter(
    private val userRepository: UserRepository
) {

    fun add(name: String): Long {
        return userRepository.add(name)
    }
}