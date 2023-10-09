package com.ys.sns.configuration;

import com.ys.sns.configuration.filter.JwtTokenFilter;
import com.ys.sns.exception.CustomAuthenticationEntryPoint;
import com.ys.sns.domain.user.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AuthenticationConfiguration extends WebSecurityConfigurerAdapter {

	private final UserService userService;

	@Value("${jwt.secret-key}")
	private String secretKey;

	public static final String[] PERMIT_ALL_URLS = {
		"/api/*/users/join",
		"/api/*/users/login",
		"/api/*/users/alarm/subscribe/*",
		"/api/notification/**",
		"/api/**"
	};

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring()
			.regexMatchers("^(?!/api/).*");

	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf()
			.disable()
			.authorizeRequests()
			.antMatchers(PERMIT_ALL_URLS).permitAll()
			.anyRequest()
			.permitAll()
			.and()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.exceptionHandling()
			.authenticationEntryPoint(new CustomAuthenticationEntryPoint())
			.and()
			.addFilterBefore(new JwtTokenFilter(userService, secretKey), UsernamePasswordAuthenticationFilter.class);
	}

}