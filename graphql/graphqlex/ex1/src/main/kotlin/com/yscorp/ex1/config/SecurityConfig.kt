package com.yscorp.ex1.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer.AuthorizationManagerRequestMatcherRegistry
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer
import org.springframework.security.web.SecurityFilterChain


@Configuration
@EnableWebSecurity
class SecurityConfig {
    @Autowired
    private val unauthorizedHandler: AuthEntryPointJwt? = null

    @Bean
    @Throws(Exception::class)
    fun defaultSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf { csrf: CsrfConfigurer<HttpSecurity> -> csrf.disable() }

        http.authorizeHttpRequests { authorizeRequests ->
            authorizeRequests
                .requestMatchers("/graphql").permitAll()
                .anyRequest().authenticated()
        }

        http.exceptionHandling { exception: ExceptionHandlingConfigurer<HttpSecurity?> ->
            exception.authenticationEntryPoint(
                unauthorizedHandler
            )
        }

        // Enable basic HTTP authentication
        http.httpBasic(Customizer.withDefaults())

        return http.build()
    }
}
