package com.ys.springboot_blog_hodol.controller;

import java.time.Duration;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ys.springboot_blog_hodol.config.AppConfig;
import com.ys.springboot_blog_hodol.request.Login;
import com.ys.springboot_blog_hodol.request.Signup;
import com.ys.springboot_blog_hodol.response.SessionResponse;
import com.ys.springboot_blog_hodol.service.AuthService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;
	private final AppConfig appConfig;

	@PostMapping("/auth/login")
	public ResponseEntity<Object> login(@RequestBody Login login) {
		Long userId = authService.signin(login);

		SecretKey key = Keys.hmacShaKeyFor(appConfig.getJwtKey());

		String jws = Jwts.builder()
			.setSubject(String.valueOf(userId))
			.signWith(key)
			.setIssuedAt(new Date())
			.compact();

		ResponseCookie cookie = ResponseCookie.from("SESSION", jws)
			.domain("localhost")
			.maxAge(Duration.ofDays(30))
			.httpOnly(true)
			.secure(true)
			.path("/")
			.sameSite("Strict")
			.build();

		return ResponseEntity.ok()
			.header(HttpHeaders.SET_COOKIE, cookie.toString())
			.body(new SessionResponse(jws));
	}

	@PostMapping("/auth/signup")
	public void signup(@RequestBody Signup signup) {
		authService.signup(signup);
	}

}
