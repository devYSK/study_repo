package com.yscorp.dgsweb.domain

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.net.URL
import java.time.LocalDateTime
import java.util.*


@Entity
@Table(name = "users")
class User {
    @Id
    var id: UUID? = null
    var username: String? = null
    var email: String? = null
    var hashedPassword: String? = null
    var avatar: URL? = null

    @CreatedDate
    var createdAt: LocalDateTime? = null

    var displayName: String? = null
    var isActive: Boolean = false

    var userRole: String = "USER"
}


@Repository
interface UserRepository : CrudRepository<User, UUID> {

    fun findByUsernameIgnoreCase(username: String): User

    @Query(
        nativeQuery = true, value = "select u.* "
            + "from users u inner join users_token ut "
            + "on u.id = ut.user_id "
            + "where ut.auth_token = ? "
            + " and ut.expiry_timestamp > current_timestamp"
    )
    fun findUserByToken(authToken: String): User?


    @Query("SELECT u FROM User u ORDER BY u.createdAt DESC")
    fun findTopUsers(limit: Int): List<User>

    @Query("SELECT u FROM User u WHERE u.id > :cursor ORDER BY u.createdAt DESC")
    fun findUsersAfterCursor(cursor: Long, limit: Int): List<User>
}
