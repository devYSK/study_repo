package com.yscorp.dgsweb.security

import com.yscorp.dgsweb.domain.UserRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer.AuthorizationManagerRequestMatcherRegistry
import org.springframework.security.web.SecurityFilterChain


@Configuration
@EnableMethodSecurity(securedEnabled = true)
class SecurityConfig(
    private val userzRepository: UserRepository
) {

    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        val authProvider: ProblemzAuthenticationProvider = ProblemzAuthenticationProvider(userzRepository)
//
//        http.authenticationProvider(authProvider)
//        http.csrf().disable().authorizeHttpRequests(
//            Customizer { auth: AuthorizationManagerRequestMatcherRegistry ->
//                auth.anyRequest().authenticated()
//            }
//        )

        return http.build()
    }
}
