package com.yscorp.dgsweb.security

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException


class ProblemzHttpConfigurer : AbstractHttpConfigurer<ProblemzHttpConfigurer?, HttpSecurity?>() {
    @Throws(Exception::class)
    override fun configure(http: HttpSecurity?) {
        val authenticationManager = http?.getSharedObject(
            AuthenticationManager::class.java
        )!!

        http?.addFilterBefore(
            ProblemzSecurityFilter(authenticationManager),
            UsernamePasswordAuthenticationFilter::class.java
        )
    }

    companion object {
        fun newInstance(): ProblemzHttpConfigurer {
            return ProblemzHttpConfigurer()
        }
    }
}


class ProblemzSecurityFilter(private val authenticationManager: AuthenticationManager) : OncePerRequestFilter() {
    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest, response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        var authToken = request.getHeader("authToken")

        if (authToken.isNotBlank()) {
            authToken = ""
        }

        val authentication = UsernamePasswordAuthenticationToken(null, authToken)

        val authenticated = authenticationManager.authenticate(authentication)
        SecurityContextHolder.getContext().authentication = authenticated

        filterChain.doFilter(request, response)
    }
}
