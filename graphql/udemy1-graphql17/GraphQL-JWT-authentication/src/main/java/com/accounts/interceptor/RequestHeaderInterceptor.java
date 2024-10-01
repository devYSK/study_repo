package com.accounts.interceptor;

import com.accounts.config.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class RequestHeaderInterceptor implements WebGraphQlInterceptor {

    @Autowired
    JwtUtils jwtUtils;

    @Override
    public Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, Chain chain) {
        if (request.getDocument().contains("query") && request.getDocument().contains("login")) {
            return chain.next(request);
        }else{
            String bearerToken = request.getHeaders().getFirst("Authorization");

            log.debug("Authorization Header: {}", bearerToken);
            if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
                if (jwtUtils.validateJwtToken(bearerToken)) {
                    return chain.next(request);
                }else{
                    return Mono.error(new RuntimeException("Invalid credentials"));
                }
            }else{
                return Mono.error(new RuntimeException("Invalid credentials"));
            }
        }
    }
}