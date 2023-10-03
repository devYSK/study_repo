package com.ys.domain.user

import org.springframework.stereotype.Service


@Service
class UserService(
    private val userWriter: UserWriter,
    private val userReader: UserReader
) {

    fun add(name: String): Long {
        return userWriter.add(name)
    }

    fun read(id: Long): User {
        return userReader.read(id)
    }
}