package com.ys.domain.user

import org.springframework.stereotype.Repository

@Repository
class UserReader(
    private val userRepository: UserRepository
) {

    fun read(id: Long) : User {
        return userRepository.read(id) ?: throw NoSuchElementException()
    }

}