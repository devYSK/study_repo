package com.yscorp.dgsweb.domain

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.*


@Entity
@Table(name = "users_token")
class UserToken {
    @Id
    var userId: UUID? = null
    var authToken: String? = null

    @CreationTimestamp
    var creationTimestamp: LocalDateTime? = null

    var expiryTimestamp: LocalDateTime? = null
}

@Repository
interface UserTokenRepository : CrudRepository<UserToken, UUID>
