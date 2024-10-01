package com.accounts.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Collections;

@Configuration
public class GraphQLConfig {

    @Bean
    public WebGraphQlInterceptor securityInterceptor() {
        return (webInput, interceptorChain) -> {
            RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                webInput.configureExecutionInput((executionInput, builder) ->
                        builder.graphQLContext(Collections.singletonMap(
                                "authentication",
                                SecurityContextHolder.getContext().getAuthentication()
                        )).build()
                );
            }
            return interceptorChain.next(webInput);
        };
    }
}
