package com.yscorp.dgsweb.security

import com.yscorp.dgsweb.domain.User
import com.yscorp.dgsweb.domain.UserRepository
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority


class ProblemzAuthenticationProvider(userzRepository: UserRepository) : AuthenticationProvider {
    private val userzRepository: UserRepository = userzRepository

    override fun authenticate(auth: Authentication): Authentication {
        val user = userzRepository.findUserByToken(auth.credentials.toString()) ?: User()

        return UsernamePasswordAuthenticationToken(
            user, auth.credentials.toString(),
            getAuthorities(user.userRole)
        )
    }

    override fun supports(authentication: Class<*>): Boolean {
        return authentication == UsernamePasswordAuthenticationToken::class.java
    }

    private fun getAuthorities(userRole: String): Collection<GrantedAuthority> {
        val authorities = ArrayList<GrantedAuthority>()

        if (userRole.isNotBlank()) {
            authorities.add(SimpleGrantedAuthority(userRole))
        }

        return authorities
    }
}
