package com.yscorp.ex1.config

import io.github.oshai.kotlinlogging.KotlinLogging
import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component

import javax.crypto.SecretKey
import java.nio.charset.StandardCharsets
import java.security.Key
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Date

@Component
class JwtUtils {

    val logger = KotlinLogging.logger {}

    @Value("\${app.jwt.secret}")
    private val jwtSecret: String? = null

    @Value("\${spring.app.jwtExpirationMs}")
    private val jwtExpirationMs = 0

    @Value("\${app.systemUser.login.user}")
    private val appUser: String? = null

    fun getJwtFromHeader(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization")
        logger.debug("Authorization Header: {}", bearerToken)
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7) // Remove Bearer prefix
        }
        return null
    }

    fun generateTokenFromUsername(userDetails: UserDetails): String {
        val username: String = userDetails.getUsername()
        return Jwts.builder()
            .subject(username)
            .issuedAt(Date())
            .expiration(Date(Date().time + jwtExpirationMs))
            .signWith(key())
            .compact()
    }

    fun getUserNameFromJwtToken(token: String?): String {
        return Jwts.parser()
            .verifyWith(key() as SecretKey)
            .build().parseSignedClaims(token)
            .getPayload().getSubject()
    }

    private fun key(): Key {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret))
    }

    fun generateJWTToken(): String {
        val key: Key = Keys.hmacShaKeyFor(jwtSecret!!.toByteArray(StandardCharsets.UTF_8))
        val algorithm: SignatureAlgorithm = SignatureAlgorithm.HS512

        val now = Instant.now()
        val expiryDate = Date.from(now.plus(10, ChronoUnit.MINUTES)) // Token valid for 1 hour

        val jwt: String = Jwts.builder()
            .setSubject(appUser)
            .claim(appUser, appUser)
            .setIssuedAt(Date.from(now))
            .setExpiration(expiryDate) // Set the expiration date
            .signWith(key, algorithm)
            .compact()

        return jwt
    }

    fun validateJwtToken(authToken: String?): Boolean {
        var authToken = authToken
        try {
            val key: Key = Keys.hmacShaKeyFor(jwtSecret!!.toByteArray(StandardCharsets.UTF_8))

            if (authToken != null && authToken.startsWith("Bearer ")) {
                authToken = authToken.substring(7)
            }

            val claims: Claims = Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(authToken)
                .getBody()

            return true
        } catch (e: MalformedJwtException) {
            logger.error("Invalid JWT token: {}", e.message)
        } catch (e: ExpiredJwtException) {
            logger.error("JWT token is expired: {}", e.message)
        } catch (e: UnsupportedJwtException) {
            logger.error("JWT token is unsupported: {}", e.message)
        } catch (e: IllegalArgumentException) {
            logger.error("JWT claims string is empty: {}", e.message)
        }
        return false
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(JwtUtils::class.java)
    }
}
