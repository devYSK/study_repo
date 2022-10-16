package com.group.libraryapp.domain.user

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface UserRepository: JpaRepository<User, Long> {

    fun findByName(name: String): User?

    @Query(value = "select distinct u from User u left join fetch u.userLoanHistories ")
    fun findAllWithHistories(): List<User>

}
