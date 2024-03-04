package com.ys.userservice.config;

import java.time.Duration;

import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;

@Configuration
public class Resilience4JConfig {

    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> globalCustomConfiguration() {
        // 서킷 브레이커 구성을 정의합니다.
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
            .failureRateThreshold(4) // 실패율이 4%를 초과하면 서킷 브레이커 open
            .waitDurationInOpenState(Duration.ofMillis(1000)) // 서킷 브레이커 open 유지되는 시간을 1초로 설정
            .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED) // 슬라이딩 윈도우 유형을 호출 횟수 기반
            .slidingWindowSize(2) // 슬라이딩 윈도우 크기를 2로 설정하여 최근 2번의 호출을 기준으로 실패율을 계산
            .build();

        // 타임 리미터 구성을 정의합니다.
        TimeLimiterConfig timeLimiterConfig = TimeLimiterConfig.custom()
            .timeoutDuration(Duration.ofSeconds(4)) // 메서드 호출이 4초를 초과하면 타임아웃
            .build();

        // Resilience4J 서킷 브레이커 팩토리에 대한 글로벌 컨피그를 정의
        return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
            .timeLimiterConfig(timeLimiterConfig) // 위에서 정의한 타임 리미터 구성을 적용
            .circuitBreakerConfig(circuitBreakerConfig) // 위에서 정의한 서킷 브레이커 구성을 적용
            .build()
        );
    }

    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> specificCustomConfiguration1() {
        // 첫 번째 서킷 브레이커 구성을 정의합니다.
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
            .failureRateThreshold(6) // 실패율이 6%를 초과하면 서킷 브레이커가 열립니다.
            .waitDurationInOpenState(Duration.ofMillis(1000)) // 서킷 브레이커가 열린 상태로 유지되는 시간을 1초로 설정합니다.
            .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED) // 슬라이딩 윈도우 유형을 호출 횟수 기반으로 설정합니다.
            .slidingWindowSize(3) // 슬라이딩 윈도우 크기를 3으로 설정하여 최근 3번의 호출을 기준으로 실패율을 계산합니다.
            .build();

        TimeLimiterConfig timeLimiterConfig = TimeLimiterConfig.custom()
            .timeoutDuration(Duration.ofSeconds(4)) // 메서드 호출이 4초를 초과하면 타임아웃 예외가 발생합니다.
            .build();

        // 특정 서킷 브레이커(circuitBreaker1)에 대한 커스텀 구성을 정의합니다.
        return factory -> factory.configure(builder -> builder.circuitBreakerConfig(circuitBreakerConfig)
            .timeLimiterConfig(timeLimiterConfig).build(), "circuitBreaker1");
    }

    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> specificCustomConfiguration2() {
        // 두 번째 서킷 브레이커 구성을 정의합니다.
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
            .failureRateThreshold(8) // 실패율이 8%를 초과하면 서킷 브레이커가 열립니다.
            .waitDurationInOpenState(Duration.ofMillis(1000)) // 서킷 브레이커가 열린 상태로 유지되는 시간을 1초로 설정합니다.
            .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED) // 슬라이딩 윈도우 유형을 호출 횟수 기반으로 설정합니다.
            .slidingWindowSize(4) // 슬라이딩 윈도우 크기를 4로 설정하여 최근 4번의 호출을 기준으로 실패율을 계산합니다.
            .build();

        TimeLimiterConfig timeLimiterConfig = TimeLimiterConfig.custom()
            .timeoutDuration(Duration.ofSeconds(4)) // 메서드 호출이 4초를 초과하면 타임아웃 예외가 발생합니다.
            .build();

        // 특정 서킷 브레이커(circuitBreaker2)에 대한 커스텀 구성을 정의합니다.
        return factory -> factory.configure(builder -> builder.circuitBreakerConfig(circuitBreakerConfig)
            .timeLimiterConfig(timeLimiterConfig).build(), "circuitBreaker2");
    }

}
