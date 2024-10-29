package com.yscorp.ex1.interceptor

import com.yscorp.ex1.config.JwtUtils
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.graphql.server.WebGraphQlInterceptor
import org.springframework.graphql.server.WebGraphQlRequest
import org.springframework.graphql.server.WebGraphQlResponse
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class RequestHeaderInterceptor(
    var jwtUtils: JwtUtils,
) : WebGraphQlInterceptor {


    private val log = KotlinLogging.logger { }

    override fun intercept(request: WebGraphQlRequest, chain: WebGraphQlInterceptor.Chain): Mono<WebGraphQlResponse> {
        if (request.document.contains("query") && request.document.contains("login")) {
            return chain.next(request)
        } else {
            val bearerToken = request.headers.getFirst("Authorization")

            log.debug("Authorization Header: {}", bearerToken)

            return if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
                if (jwtUtils.validateJwtToken(bearerToken)) {
                    chain.next(request)
                } else {
                    Mono.error(RuntimeException("Invalid credentials"))
                }
            } else {
                Mono.error(RuntimeException("Invalid credentials"))
            }
        }
    }
}