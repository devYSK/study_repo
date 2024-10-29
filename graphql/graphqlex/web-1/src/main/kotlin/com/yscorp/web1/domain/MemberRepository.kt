package com.yscorp.web1.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface MemberRepository : JpaRepository<Member, Long> {
    fun findByType(type: MemberType): List<Member>

    @Query(value = "SELECT m FROM Member m WHERE LOWER(m.firstName) like %:name%")
    fun fetchMembersByName(@Param("name") text: String): List<Member>
}
