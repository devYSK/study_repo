package com.yscorp.withpush.messagesystem.auth

import com.fasterxml.jackson.annotation.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
@JsonIgnoreProperties(ignoreUnknown = true)
class MessageUserDetails @JsonCreator constructor(
    @param:JsonProperty("userId") val userId: Long,
    @param:JsonProperty("username") private val username: String,
    @param:JsonProperty("password") private var password: String
) : UserDetails {
    fun erasePassword() {
        password = ""
    }

    override fun getUsername(): String {
        return username
    }

    override fun getPassword(): String {
        return password
    }

    @JsonIgnore
    override fun getAuthorities(): Collection<GrantedAuthority> {
        return listOf()
    }

    override fun equals(o: Any?): Boolean {
        if (o == null || javaClass != o.javaClass) return false
        val that = o as MessageUserDetails
        return username == that.username
    }

    override fun hashCode(): Int {
        return Objects.hashCode(username)
    }
}
