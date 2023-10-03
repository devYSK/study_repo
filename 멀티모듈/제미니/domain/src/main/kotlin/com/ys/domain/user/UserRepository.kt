package com.ys.domain.user

interface UserRepository {

    fun read(id: Long): User?

    fun add(name: String): Long

}