package com.yscorp.withpush.messagesystem.config

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import net.prostars.messagesystem.auth.RestApiLoginAuthFilter
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.config.Customizer
import org.springframework.security.core.Authentication
import java.io.IOException

@Configuration
@Suppress("unused")
class SecurityConfig {
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun authenticationManager(
        detailsService: UserDetailsService?, passwordEncoder: PasswordEncoder?
    ): AuthenticationManager {
        val daoAuthenticationProvider: DaoAuthenticationProvider = DaoAuthenticationProvider()
        daoAuthenticationProvider.setUserDetailsService(detailsService)
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder)
        return ProviderManager(daoAuthenticationProvider)
    }

    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(
        httpSecurity: HttpSecurity, authenticationManager: AuthenticationManager?
    ): SecurityFilterChain {
        val restApiLoginAuthFilter: RestApiLoginAuthFilter =
            RestApiLoginAuthFilter(
                AntPathRequestMatcher("/api/v1/auth/login", "POST"), authenticationManager
            )

        httpSecurity
            .csrf(Customizer<CsrfConfigurer<HttpSecurity?>> { obj: CsrfConfigurer<HttpSecurity?> -> obj.disable() })
            .httpBasic(Customizer<HttpBasicConfigurer<HttpSecurity?>> { obj: HttpBasicConfigurer<HttpSecurity?> -> obj.disable() })
            .formLogin(Customizer<FormLoginConfigurer<HttpSecurity?>> { obj: FormLoginConfigurer<HttpSecurity?> -> obj.disable() })
            .addFilterAt(restApiLoginAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
            .authorizeHttpRequests(
                Customizer<AuthorizationManagerRequestMatcherRegistry> { auth: AuthorizationManagerRequestMatcherRegistry ->
                    auth.requestMatchers("/api/v1/auth/register", "/api/v1/auth/login")
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                })
            .logout(
                Customizer<LogoutConfigurer<HttpSecurity?>> { logout: LogoutConfigurer<HttpSecurity?> ->
                    logout.logoutUrl("/api/v1/auth/logout").logoutSuccessHandler(
                        LogoutSuccessHandler { request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication? ->
                            this.logoutHandler(
                                request,
                                response,
                                authentication
                            )
                        })
                })
        return httpSecurity.build()
    }

    private fun logoutHandler(
        request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication?
    ) {
        response.contentType = MediaType.TEXT_PLAIN_VALUE
        response.characterEncoding = "UTF-8"
        val message: String

        if (authentication != null && authentication.isAuthenticated) {
            response.status = HttpStatus.OK.value()
            message = "Logout success."
        } else {
            response.status = HttpStatus.UNAUTHORIZED.value()
            message = "Logout failed."
        }

        try {
            response.writer.write(message)
        } catch (ex: IOException) {
            log.error("Response failed. cause: {}", ex.message)
        }
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(SecurityConfig::class.java)
    }
}
