package com.yscorp.withpush.messagesystem.auth

import com.fasterxml.jackson.databind.ObjectMapper
import com.yscorp.withpush.messagesystem.dto.restapi.LoginRequest
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.context.HttpSessionSecurityContextRepository
import org.springframework.security.web.util.matcher.RequestMatcher
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.*

class RestApiLoginAuthFilter(
    requiresAuthenticationRequestMatcher: RequestMatcher,
    authenticationManager: AuthenticationManager
) : AbstractAuthenticationProcessingFilter(requiresAuthenticationRequestMatcher, authenticationManager) {
    private val objectMapper = ObjectMapper()

    @Throws(AuthenticationException::class, IOException::class)
    override fun attemptAuthentication(
        request: HttpServletRequest, response: HttpServletResponse
    ): Authentication? {
        if (!request.contentType.startsWith(MediaType.APPLICATION_JSON_VALUE)) {
            throw AuthenticationServiceException(
                "Unsupported Content-Type: " + request.contentType
            )
        }

        val loginRequest: LoginRequest =
            objectMapper.readValue(request.inputStream, LoginRequest::class.java)
        val authenticationToken =
            UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password)

        return authenticationManager?.authenticate(authenticationToken)
    }

    @Throws(IOException::class)
    override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
        authResult: Authentication
    ) {
        val securityContext: SecurityContext = SecurityContextHolder.getContext()
        (authResult.principal as MessageUserDetails).erasePassword()
        securityContext.authentication = authResult

        val contextRepository =
            HttpSessionSecurityContextRepository()

        contextRepository.saveContext(securityContext, request, response)

        val sessionId = request.session.id
        val encodedSessionId =
            Base64.getEncoder().encodeToString(sessionId.toByteArray(StandardCharsets.UTF_8))

        response.status = HttpServletResponse.SC_OK
        response.contentType = MediaType.TEXT_PLAIN_VALUE
        response.characterEncoding = "UTF-8"
        response.writer.write(encodedSessionId)
        response.writer.flush()
    }

    @Throws(IOException::class)
    override fun unsuccessfulAuthentication(
        request: HttpServletRequest, response: HttpServletResponse, failed: AuthenticationException
    ) {
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.contentType = MediaType.TEXT_PLAIN_VALUE
        response.characterEncoding = "UTF-8"
        response.writer.write("Not authenticated.")
        response.writer.flush()
    }
}
