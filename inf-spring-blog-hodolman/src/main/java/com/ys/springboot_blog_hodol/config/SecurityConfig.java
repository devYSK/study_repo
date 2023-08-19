package com.ys.springboot_blog_hodol.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig {


	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
			.authorizeHttpRequests()
			.requestMatchers("/login")
			.permitAll()
			.anyRequest()
			.authenticated()
			.and()
			.formLogin()
			.usernameParameter("username")
			.passwordParameter("password")
			.loginPage("/auth/login")
			.loginProcessingUrl("/auth/login")
			.defaultSuccessUrl("/")
			.and()
			.userDetailsService(userDetailsService())
			.csrf(AbstractHttpConfigurer::disable)
			.build();
	}

	@Bean
	UserDetailsService userDetailsService() {
		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
		final UserDetails user = User.withUsername("ys")
									 .password("1234")
									 .roles("ADMIN")
									 .build();

		manager.createUser(user);

		return manager;
	}
}
