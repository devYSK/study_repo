package com.ys.apigateway.filter;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Set;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

	private final SecretKey signingKey;

	public AuthorizationHeaderFilter(Environment env) {
		super(Config.class);

		byte[] secretKeyBytes = Base64.getEncoder()
			.encode(env.getProperty("token.secret")
				.getBytes());

		this.signingKey = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS512.getJcaName());
	}

	@Override
	public GatewayFilter apply(Config config) {
		return (exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest();

			if (!request.getHeaders()
				.containsKey(HttpHeaders.AUTHORIZATION)) {
				return onError(exchange, "No authorization header", HttpStatus.UNAUTHORIZED);
			}

			logRequestHeaders(request);

			String jwt = extractJwtFromAuthorizationHeader(request);
			System.out.println("jwt! : " + jwt);
			final boolean isValid = isJwtValid(jwt);

			System.out.println("isValid? " + isValid);
			if (!isValid) {
				return onError(exchange, "JWT token is not valid", HttpStatus.UNAUTHORIZED);
			}

			return chain.filter(exchange);
		};
	}

	private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
		log.error(err);

		byte[] bytes = err.getBytes(StandardCharsets.UTF_8);
		DataBuffer buffer = exchange.getResponse()
			.bufferFactory()
			.wrap(bytes);

		exchange.getResponse()
			.setStatusCode(httpStatus);
		return exchange.getResponse()
			.writeWith(Mono.just(buffer));
	}

	private boolean isJwtValid(String jwt) {
		try {


			String subject = Jwts.parserBuilder()
				.setSigningKey(signingKey)
				.build()
				.parseClaimsJws(jwt)
				.getBody()
				.getSubject();

			System.out.println("subject : " + subject);
			System.out.println(subject != null && !subject.isEmpty());

			return subject != null && !subject.isEmpty();
		} catch (Exception ex) {
			return false;
		}
	}

	private void logRequestHeaders(ServerHttpRequest request) {
		request.getHeaders()
			.forEach((key, value) -> log.info("{}={}", key, value));
	}

	private String extractJwtFromAuthorizationHeader(ServerHttpRequest request) {
		String authorizationHeader = request.getHeaders()
			.getFirst(HttpHeaders.AUTHORIZATION);

		return authorizationHeader.replace("Bearer", "")
			.trim();
	}

	public static class Config {
		// Put configuration properties here
	}

}
