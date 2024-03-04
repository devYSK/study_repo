# 인프런 Spring cloud MSA

[toc]

* https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%81%B4%EB%9D%BC%EC%9A%B0%EB%93%9C-%EB%A7%88%EC%9D%B4%ED%81%AC%EB%A1%9C%EC%84%9C%EB%B9%84%EC%8A%A4/news

![image-20240302004329338](./images//image-20240302004329338.png)

* https://github.com/joneconsulting/msa_with_spring_cloud
* https://github.com/joneconsulting/toy-msa/tree/springboot3.2



# MSA

Cloud Native Architecture의 특징

- ﻿﻿확장 가능한 아키텍쳐

  - ﻿﻿시스템의 수평적 확정에 유연

  - ﻿﻿확장된 서버로 시스템의 부하 분산, 가용성 보장

  - ﻿﻿시스템 또는, 서비스 애플리케이션 단위의 패키지(컨테이너 기반 패키지 . 모니터링

- ﻿﻿탄력적 아키텍처

  - ﻿﻿서비스 생성- 통합. 배포, 비즈니스 환경 변화에 대응 시간 단축

  - ﻿﻿분활 된 서비스 구조
  -  무상태 통신 프로토콜
  -  서비스의 추가와 삭제 자동으로 감지

  - ﻿﻿변경된 서비스 요청에 따라 사용자 요청 처리(동적 처리)

- ﻿﻿장애 격리 (Fault isolation)
  -  특정 서비스에 오류가 발생해도 다른 서비스에 영향 주지 않음

## 12 Factors

* https://12factor.net/

클라우드 네이티브 앱을 개발할 때 고려해야 할점 

## MSA 표준 구성요소 - https://landscape.cncf.io/



## 스프링 클라우드 구성 요소

중앙 집중식 구성 관리 (Centralized configuration management)

- Spring Cloud Config Server: 애플리케이션의 모든 환경 구성을 중앙에서 관리할 수 있게 해주는 서비스입니다. 이를 통해 개발자들은 애플리케이션의 설정을 한 곳에서 관리할 수 있으며, 변경 사항을 쉽게 적용할 수 있습니다.

위치 투명성 ( Location transparency)

- Naming Server (Eureka): 서비스 인스턴스의 위치를 자동으로 관리해주며, 클라이언트가 서비스 위치에 대해 알 필요 없이 서비스를 찾고 통신할 수 있게 해주는 서비스입니다. 이는 마이크로서비스 아키텍처에서 서비스 간의 통신을 간소화합니다.

부하 분산 (Load Balancing) 

- Ribbon (Client Side): 클라이언트 측에서 로드 밸런싱을 수행하는 라이브러리로, 서비스 호출 시 여러 인스턴스 중 하나를 선택해 부하를 분산시킵니다.
- Spring Cloud Gateway: 마이크로서비스 아키텍처에서 API 게이트웨이 역할을 하며, 로드 밸런싱, 보안, 모니터링 등의 기능을 제공합니다.

더 쉬운 REST 클라이언트 

- FeignClient: 마이크로서비스 간의 HTTP 기반 통신을 추상화하여, RESTful 서비스 클라이언트를 쉽게 만들 수 있게 해주는 도구입니다.

가시성 및 모니터링 (Visibility and monitoring)

- Zipkin Distributed Tracing: 분산된 시스템에서 요청의 흐름을 추적하고 모니터링하기 위한 도구로, 성능 문제를 진단하고 시스템의 가시성을 높이는 데 도움을 줍니다.
- Netflix API gateway: 마이크로서비스 아키텍처에서 클라이언트와 서비스 사이의 단일 진입점을 제공하며, 요청을 적절한 서비스로 라우팅합니다. 이는 종종 Spring Cloud Gateway와 비교되곤 합니다.

장애 허용 (Fault Tolerance)

- Hystrix: 서비스 간의 통신이 실패하거나 지연될 때, 애플리케이션의 복원력을 높여주는 라이브러리입니다. 히스트릭스는 서킷 브레이커 패턴을 구현하여, 장애가 발생할 경우 대체 로직을 제공하거나, 장애가 전파되는 것을 방지합니다.

# Service Discovery

서비스 디스커버리는 마이크로서비스 아키텍처에서 `서비스 인스턴스들의 위치(예: IP 주소와 포트 번호)를` 동적으로 찾는 과정

* 마이크로서비스 환경에서는 수많은 서비스들이 다양한 환경에서 실행되며, 각각의 서비스는 자동으로 확장, 축소되거나 업데이트될 수 있다
* 이러한 환경에서 서비스 디스커버리 메커니즘이 없다면, 각 서비스의 위치를 수동으로 관리하고 업데이트하는 것은 매우 어려운 일이 되는데, 서비스 디스커버리 시스템은 이러한 문제를 해결하며, 클라이언트가 서비스의 현재 위치를 자동으로 찾을 수 있게 해준다.

Spring Cloud Netflix Eureka는 서비스 디스커버리를 위한 Netflix OSS의 구성 요소 중 하나로, Spring Cloud에서 쉽게 사용할 수 있도록 통합된 Java 기반의 솔루션

Eureka는 서비스 레지스트리로 작동하며, 모든 서비스 인스턴스는 시작할 때 자신을 Eureka 서버에 등록하며 클라이언트는 필요한 서비스를 Eureka 서버에서 찾아 통신할 수 있다.

Eureka는 다음 두 가지 주요 구성 요소로 구성된다:

1. **Eureka Server**: 서비스 레지스트리 역할을 하며, 모든 서비스 인스턴스들의 상태 정보를 유지합니다. 서비스 인스턴스들은 자신의 상태(예: 실행 중, 중단됨)를 Eureka 서버에 주기적으로 보고하며, 이 정보를 바탕으로 클라이언트는 사용 가능한 서비스 인스턴스를 찾을 수 있습니다.
2. **Eureka Client**: 서비스 인스턴스에 포함되어 Eureka 서버에 자신을 등록하고, 다른 서비스 인스턴스의 위치 정보를 조회하는 역할을 합니다. Eureka 클라이언트는 서비스의 가용성을 높이기 위해, 서비스 인스턴스의 상태를 주기적으로 Eureka 서버에 갱신합니다.

Eureka는 자가 보호 모드(Self-preservation mode)라는 독특한 기능을 제공하여, 네트워크 문제로 인해 일시적으로 Eureka 서버에 서비스 인스턴스의 상태 정보가 갱신되지 않는 경우에도, 해당 인스턴스를 서비스 레지스트리에서 바로 제거하지 않습니다. 이는 네트워크 분할(Network Partition) 상황에서도 시스템의 안정성을 유지하도록 돕습니다.



## Spring cloud netflix eureka

* https://cloud.spring.io/spring-cloud-netflix/reference/html/
* https://github.com/spring-cloud/spring-cloud-netflix

```groovy
plugins {
    id 'org.springframework.boot' version '3.2.2'
    id 'io.spring.dependency-management' version '1.0.11.RELEASE'
    id 'java'
}

repositories {
    mavenCentral()
}

ext {
    set('springCloudVersion', '2023.0.0')
}

dependencies {
    implementation 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-server'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
}

dependencyManagement {
    imports {
        mavenBom "org.springframework.cloud:spring-cloud-dependencies:${springCloudVersion}"
    }
}
```

코드 1개 추가

```java
@SpringBootApplication
@EnableEurekaServer
public class ServiceDiscoveryEurekaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceDiscoveryEurekaApplication.class, args);
	}

}

/**
 * Annotation to activate Eureka Server related configuration.
 * {@link EurekaServerAutoConfiguration}
 *
 * @author Dave Syer
 * @author Biju Kunjummen
 *
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(EurekaServerMarkerConfiguration.class)
public @interface EnableEurekaServer { }


public class EurekaServerAutoConfiguration implements WebMvcConfigurer { ... }
```

yml 설정

```yaml
server:
  port: 8761 # Eureka 서버는 일반적으로 기본 포트인 8761에서 실행됩니다.

spring:
  application:
    name: discoveryservice # 이 이름은 Eureka 서버에 등록될 때 사용됩니다.

eureka:
  client:
    register-with-eureka: false # Eureka 클라이언트가 Eureka 서버에 자신을 등록하지 않도록 설정합니다. 이 설정은 Eureka 서버 자체에 대한 설정으로, 서버가 자기 자신을 등록하는 것을 방지합니다.
    fetch-registry: false # 이 Eureka 클라이언트가 Eureka 서버에서 서비스 등록 정보를 가져오지 않도록 설정합니다. 이 설정 역시 Eureka 서버 자체의 설정으로, 서버가 다른 서비스 정보를 필요로 하지 않기 때문에 사용됩니다.

```



http://localhost:8761/ 로 접속해서 볼 수 있다.



## 클라이언트에서 eureka에 등록하기.

```groovy
dependencies {
    implementation 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-client'
}
```

```java
@SpringBootApplication
@EnableDiscoveryClient
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

}
```

```yaml
server:
  port: 9001
  
spring:
  application:
    name: user-service
    
eureka:
  instance:
    instance-id: ${spring.application.name}:${spring.application.instance_id:${random.value}}
  client:
    register-with-eureka: true
    fetch-registry: true
    service-url:
      defaultZone: http://127.0.0.1:8761/eureka # eureka의 위치
```

등록완료시

```
INFO 14364 --- [user-service] [nfoReplicator-0] com.netflix.discovery.DiscoveryClient    : DiscoveryClient_USER-SERVICE/192.168.0.4:user-service:9001 - registration status: 204
```

![image-20240302152649571](./images//image-20240302152649571.png)



# API-gateway

API 게이트웨이는 마이크로서비스 아키텍처에서 클라이언트 요청을 적절한 마이크로서비스로 라우팅하는 중앙 집중식 진입점.

모든 인바운드 요청을 수신하고, 이를 처리하기 위해 여러 백엔드 서비스 중 하나로 전달하는 역

API 게이트웨이는 다음과 같은 기능을 수행합니다:

- 요청 라우팅: 클라이언트로부터 받은 요청을 적절한 마이크로서비스로 전달합니다.
- 인증 및 권한 부여: 클라이언트 요청을 처리하기 전에 사용자의 인증 및 권한을 검증합니다.
- 요청/응답 변환: 클라이언트와 마이크로서비스 사이에서 데이터 포맷을 변환합니다.
- 부하 분산: 인바운드 요청을 여러 인스턴스에 분산하여 부하를 균등하게 분배합니다.
- 속도 제한: 특정 시간 동안 클라이언트가 보낼 수 있는 요청의 수를 제한합니다.
- 모니터링 및 로깅: 시스템의 성능을 모니터링하고 로그를 기록합니다.
- 응답 캐싱
- IP 화이트/블랙 리스트

### Spring Cloud API Gateway란?

Spring Cloud API Gateway는 Spring Cloud 프로젝트의 일부로, 자바 개발자들이 쉽게 API 게이트웨이를 구축할 수 있도록 설계된 라이브러리입니다. 이것은 리액티브 프로그래밍 모델을 기반으로 하며, Spring WebFlux를 사용하여 비동기적이고 논블로킹 방식으로 요청을 처리합니다. Spring Cloud API Gateway는 다음과 같은 특징을 가집니다:

- 경로 기반 라우팅 및 필터링: URI 경로를 기반으로 요청을 적절한 마이크로서비스로 라우팅하고, 요청 및 응답을 필터링합니다.
- 통합된 서비스 디스커버리: Eureka, Consul 같은 서비스 디스커버리 메커니즘과 통합하여, 동적으로 서비스 인스턴스를 찾고 라우팅할 수 있습니다.
- 보안 기능: OAuth 2.0, JWT 등을 사용하여 인증 및 권한 부여를 지원합니다.
- 속도 제한 및 회로 차단: 넷플릭스 Hystrix와 같은 라이브러리를 통합하여 속도 제한 및 회로 차단 기능을 제공합니다.

```groovy
dependencies {
    // Spring WebFlux 스타터: 비동기적이고 논블로킹 방식의 웹 애플리케이션 개발을 위한 스타터 패키지
    implementation 'org.springframework.boot:spring-boot-starter-webflux'

    // Spring Cloud Gateway 스타터: API 게이트웨이 구축을 위한 스타터 패키지
    implementation 'org.springframework.cloud:spring-cloud-starter-gateway'

    // Eureka 클라이언트 스타터: 서비스 디스커버리를 위해 Eureka 서버와 통신하는 클라이언트 개발을 위한 스타터
    implementation 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-client'
}
```

yml과 java 설정 둘다 가능하다.

```yaml

server:
  port: 8000

eureka:
  client:
    register-with-eureka: true
    fetch-registry: true
    service-url:
      defaultZone: http://localhost:8761/eureka

spring:
  application:
    name: apigateway-service
  cloud:
    gateway:
      routes:
        - id: first-service
          uri: http://localhost:8081/
          predicates:
            - Path=/first-service/**
          filters:
          	- AddRequestHeader=first-request, first-request-header2
            - AddResponseHeader=first-response, first-response-header2
        - id: second-service
          uri: http://localhost:8082/
          predicates:
            - Path=/second-service/**
					filters:
					  - AddRequestHeader=second-request, second-request-header2
            - AddResponseHeader=second-response, second-response-header2
```

또는

```java
@Configuration
public class FilterConfig {
    Environment env;

   @Bean
   public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {

       return builder.routes()
           .route(r -> r.path("/first-service/**")
               .filters(f -> f.addRequestHeader("first-request", "first-request-header-by-java")
                       .addResponseHeader("first-response", "first-response-header-by-java")
               )
               .uri("http://localhost:8081"))
           .route(r -> r.path("/second-service/**")
               .filters(f -> f.addRequestHeader("second-request", "second-request-header-by-java")
                   .addResponseHeader("second-response", "second-response-header-by-java"))
               .uri("http://localhost:8082"))
           .build();
   }

}

```



## Gateway CustomFilter

* 추가 참고 : https://www.baeldung.com/spring-cloud-custom-gateway-filters

커스텀 필터 작성

```java
@Component
@Slf4j
public class CustomFilter extends AbstractGatewayFilterFactory<CustomFilter.Config> {

    public CustomFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        // Custom Pre Filter
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("Custom PRE filter: request id -> {}", request.getId());
            System.out.println("Yes");
            // Custom Post Filter
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                log.info("Custom POST filter: response code -> {}", response.getStatusCode());
            }));
        };
    }

    public static class Config {
        // Put the configuration properties
    }
}

```

yml에 등록 또는 filterConfig에 등록 (둘중 하나만 하면 됌)

```yaml
spring:
  application:
    name: apigateway-service
  cloud:
    gateway:
      routes:
        - id: first-service
          uri: http://localhost:8081/
          predicates:
            - Path=/first-service/**
          filters:
            - name: CustomFilter
        - id: second-service
          uri: http://localhost:8082/
          predicates:
            - Path=/second-service/**
          filters:
            - name: CustomFilter # here
```

또는

```java
@Bean
public RouteLocator customRouteLocator(RouteLocatorBuilder builder, CustomFilter customFilter) {
    return builder.routes()
            .route(r -> r.path("/somepath/**")
                        .filters(f -> f.filter(customFilter.apply(new CustomFilter.Config())))
                        .uri("http://example.org"))
            .build();
}
```

또는

```java
@Configuration
public class FilterConfig {
    Environment env;

   @Bean
   public RouteLocator gatewayRoutes(RouteLocatorBuilder builder, CustomFilter customFilter) {

       return builder.routes()
           .route(r -> r.path("/first-service/**")
               .filters(f -> f.addRequestHeader("first-request", "first-request-header-by-java")
                       .addResponseHeader("first-response", "first-response-header-by-java")
                       .filter(customFilter.apply(new CustomFilter.Config()))
               )
               .uri("http://localhost:8081"))

           .build();
   }

}
```

## GlobalFilter

Spring Cloud Gateway에서 `GlobalFilter`는 모든 라우트에 적용되는 필터입니다. 특정 조건이나 설정 없이 게이트웨이를 통과하는 모든 요청/응답에 대해 실행

`GlobalFilter` 인터페이스를 구현하여 사용자 정의 글로벌 필터를 만들 수 있으며, Spring의 의존성 주입(DI)을 통해 자동으로 게이트웨이 필터 체인에 등록

> 가장 처음에 등록되므로 가장 마지막에 밖으로 나감.

### GlobalFilter 사용 예

- **인증 및 권한 부여**: 모든 요청에 대해 사용자의 인증 정보를 검증하거나 권한을 확인합니다.
- **로깅 및 모니터링**: 요청과 응답에 대한 로깅을 수행하여 모니터링 및 디버깅에 활용합니다.
- **헤더 수정**: 요청 또는 응답 헤더를 추가, 삭제 또는 수정하여 후속 처리를 위한 정보를 전달합니다.
- **요청/응답 수정**: 요청의 바디를 수정하거나 응답의 바디를 가공합니다.
- **속도 제한**: 요청의 속도를 제한하여 API 사용률을 관리합니다.
- **CORS(Cross-Origin Resource Sharing) 설정**: 모든 요청에 대해 CORS 정책을 적용합니다.

```java
@Component
@Slf4j
public class GlobalFilter extends AbstractGatewayFilterFactory<GlobalFilter.Config> {

    public GlobalFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("Global Filter baseMessage: {}, {}", config.getBaseMessage(), request.getRemoteAddress());

            if (config.isPreLogger()) {
                log.info("Global Filter Start: request id -> {}", request.getId());
            }

            return chain.filter(exchange).then(Mono.fromRunnable(()->{
                if (config.isPostLogger()) {
                    log.info("Global Filter End: response code -> {}", response.getStatusCode());
                }
            }));
        });
    }

    @Data
    public static class Config { // yml에서 필요한 정보를 등록. 외부에서 값을 가져올수도 있다. 
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;
    }
    
}
```

등록은 다음과 같다.

```yaml
spring:
  application:
    name: apigateway-service
  cloud:
    gateway:
      default-filters:
        - name: GlobalFilter
          args:
            baseMessage: Spring Cloud Gateway Global Filter
            preLogger: true
            postLogger: true
      routes:
        - id: first-service
          uri: http://localhost:8081/
          predicates:
            - Path=/first-service/**
          filters:
            - CustomFilter # 같음

        - id: second-service
          uri: http://localhost:8082/
          predicates:
            - Path=/second-service/**
          filters:
            - name: CustomFilter # 같음
```

## LoggingFilter (Custom Filter)

```java
Component
@Slf4j
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> {
    public LoggingFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        GatewayFilter filter = new OrderedGatewayFilter((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("Logging Filter baseMessage: {}", config.getBaseMessage());
            if (config.isPreLogger()) {
                log.info("Logging PRE Filter: request id -> {}", request.getId());
            }
            return chain.filter(exchange).then(Mono.fromRunnable(()->{
                if (config.isPostLogger()) {
                    log.info("Logging POST Filter: response code -> {}", response.getStatusCode());
                }
            }));
        }, Ordered.HIGHEST_PRECEDENCE);

        return filter;
    }

    @Data
    public static class Config {
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;
    }
}
```

* 필터 순위를 높였다

```yaml
spring:
  application:
    name: apigateway-service
  cloud:
    gateway:
      default-filters:
        - name: GlobalFilter
          args:
            baseMessage: Spring Cloud Gateway Global Filter
            preLogger: true
            postLogger: true
      routes:
        - id: first-service
          uri: http://localhost:8081/
          predicates:
            - Path=/first-service/**
          filters:
            - CustomFilter # 같음

        - id: second-service
          uri: http://localhost:8082/
          predicates:
            - Path=/second-service/**
          filters:
            - name: CustomFilter # 같음
            - name: LoggingFilter
              args:
                baseMessage: Hi, LoggingFilter
                preLogger: true
                postLogger: true
```



## Gateway Authentication Filter(인증 필터)

필터 구현

```java

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

			if (!isJwtValid(jwt)) {
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

```

필터 등록

```yaml
spring:
  cloud:
    gateway:
      default-filters:
        - name: GlobalFilter
          args:
            baseMessage: Spring Cloud Gateway Global Filter
            preLogger: true
            postLogger: true
      routes:
        - id: user-service
          uri: lb://USER-SERVICE
          predicates:
            - Path=/user-service/login
            - Method=POST
          filters:
            - RemoveRequestHeader=Cookie
            - RewritePath=/user-service/(?<segment>.*), /$\{segment}

        - id: user-service
          uri: lb://USER-SERVICE
          predicates:
            - Path=/user-service/**
          filters:
            - RemoveRequestHeader=Cookie
            - RewritePath=/user-service/(?<segment>.*), /$\{segment}
            - AuthorizationHeaderFilter # -------------- 적용


token:
  secret: user_token # 토큰값. spring-cloud-config로도 관리 가능
```





## SpringCloudGateway - Eureka 연동 (Load Balancer)

gateway 설정 변경

```yaml
spring:
  application:
    name: apigateway-service
  cloud:
    gateway:
      default-filters:
        - name: GlobalFilter
          args:
            baseMessage: Spring Cloud Gateway Global Filter
            preLogger: true
            postLogger: true
      routes:
        - id: first-service
#          uri: http://localhost:8081/
          uri: lb://MY-FIRST-SERVICE # eureka name server에 등록한 이름
          predicates:
            - Path=/first-service/**
          filters:
            - CustomFilter # 같음

        - id: second-service
#          uri: http://localhost:8082/
          uri: lb://MY-SECOND-SERVICE #  eurekaname server에 등록한 이름
          predicates:
            - Path=/second-service/**
          filters:
            - name: CustomFilter # 같음
            - name: LoggingFilter
              args:
                baseMessage: Hi, LoggingFilter
                preLogger: true
                postLogger: true
```

eureka에 등록된 서비스 목록

![image-20240302182931140](./images//image-20240302182931140.png)

* lb랑 이름이 같다
  * 자매품 : **`ws://`와 `wss://`**: WebSocket 프로토콜과 WebSocket Secure 프로토콜을 위한 URI 스키마



# 이커머스 애플리케이션 아키텍처

![image-20240302184045677](./images//image-20240302184045677.png)

![image-20240302184137282](./images//image-20240302184137282.png)

![image-20240302184257437](./images//image-20240302184257437.png)



# Spring Cloud Config

- ﻿﻿분산 시스템에서 서버, 클라이언트 구성에 필요한 설정 정보(application.ym)를 외부 시스템에서 관리
- ﻿﻿하나의 중앙화 된 저장소에서 구성요소 관리 가능
- ﻿﻿각 서비스를 다시 빌드하지 않고, 바로 적응 가능 
- 애플리케이션 배포 파이프라인을 통해 DEV- UAT- PROD 환경에 맞는 구성 정보 사용

git, secureVault, File 에 저장 가능하다.

gradle

```groovy
dependencies {
    implementation 'org.springframework.cloud:spring-cloud-config-server'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    implementation 'org.springframework.boot:spring-boot-starter-actuator'
    implementation 'org.springframework.boot:spring-boot-starter-validation'
    // spring-cloud-starter-bus-amqp와 spring-cloud-starter-netflix-eureka-client는 주석 처리되어 제외됨
    implementation 'org.springframework.cloud:spring-cloud-starter-bootstrap'
}
```

```java
@SpringBootApplication
@EnableConfigServer
public class ConfigServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigServiceApplication.class, args);
    }

}
```



* 레퍼런스 : https://docs.spring.io/spring-cloud-config/docs/current/reference/html/

## Spring Cloud Config 설정 파일 우선 순위

설정 파일은 크게 다음의 위치에 존재할 수 있으며 다음의 순서대로 읽어진다. 나중에 읽어지는 것이 우선순위가 높다.

프로젝트의 application.yaml
설정 저장소의 application.yaml
프로젝트의 application-{profile}.yaml
설정 저장소의 {application name}/{application name}-{profile}

```
로컬의 appliation.yaml, application-local.yaml에 있고, 설정 저장소의 application.yaml, hello/hello-local.yaml에도 있다면 다음의 순서대로 읽어진다.

프로젝트의 application.yaml
설정 저장소의 application.yaml
프로젝트 application-local.yaml
설정 저장소의 hello/hello-local.yaml
```



## Spring Cloud Config 클론

스프링 클라우드 공식 문서에서는 이미 이와 관련된 내용을 Warning으로 다루고 있다. 아래의 내용을 보면 OS에 따라서 주기적으로 임시 디렉토리가 삭제될 수 있으니, basedir을 설정



설정 파일을 클론받는 위치를 직접 지정해서 임시 디렉토리에 받지  않는것이 좋다.

```yaml
spring:
  cloud:
    config:
      server:
        git:
          default-label: main
          uri: https://github.com/repo/spring-cloud-config
          search-paths: test-cloud-config-file/**
          basedir: ./repository
```



##  SpringCloudConfig 프로퍼티 암호화

> basic auth를 사용해서 막아도 된다. 

spring cloud config 서버의 `yml`파일의 맨 밑에 다음과 같이 추가한다.

```
encrypt:
  key: my-secret # 사용할 암호
```

정보를 암호화할 키를 넣는다. 사용할 암호를 임의로 지정해 적용하면 된다.

암호화 하고 싶은 정보를 `Body`에 담아 보내면 결과를 응답해준다.

해당응답 결과를  property에 넣는다.

```yaml
jwt:
  secret: "{cipher}aadfc7c7841c2ef32e1abcc4e6860755f22fb5be06cde48106ae95029413d794"
```

* 쌍따옴표 안에 `{cipher}`를 넣고 암호화된 문자를 넣는다.

따라서 클라이언트에서 해당 정보를 복호화해 사용해야 한다.

환경변수에 `ENCRYPT_KEY=my-secret` 값을 다음과 같이 적용한다. 암호화 관련 설정에서 사용한 비밀키를 넣으면 된다.

## yml 읽어오는법

![image-20240303013613670](./images//image-20240303013613670.png)

3가지가 있다.

* yml
* -name
* -name-profile

```
ecommerce-dev.yml 
ecommerce-uat.yml 
ecommerce-prod.yml
```

없으면 default를 가져간다.

bootstrap.yml을 다음과같이 원하는 프로파일로 수정한다. 

```yaml
spring:
  cloud:
    config:
      uri: http://127.0.0.1:8888
      name: user-service
  profiles:
    active: dev
```

환경변수를 사용하여 참조하고 주입 가능하다

```yaml
spring:
  cloud:
    config:
      uri: http://127.0.0.1:8888
      name: user-service
  profiles:
    active: ${SPRING_PROFILES_ACTIVE:default}
```





## 1. local git repository 사용

```shell
mkdir git-local-repo
cd /Users/ysk/temp/git-local-repo

git init
git add ecommerce.yml
git commit -m 'upload an application yaml file'
```

```yaml
# in ecommerce.yml

token:
  expiration_time: 86400000
  secret: ysk-microservice-practice-jwt-secret-key # ysk-microservice-practice-jwt-secret-key
  
gateway:
	ip: http://192.168.0.4
```

application.yml 에 설정

```yaml
spring:
  application:
    name: config-service
  cloud:
    config:
      server:
        git: #default
          uri: file:///Users/ysk/temp/git-local-repo
          default-label: master
```

접속시

```
http://localhost:8888/ecommerce/default

서버:포트번호/yml파일명/profile 

* profile : spring profile
```

## 2. remote git repository 사용

```yaml
spring:
  application:
    name: config-service
  cloud:
    config:
      server:
        git: #default
	        uri: https://github.com/devysk/spring-cloud-config
```



## 3. private remote git repository 사용

```yaml
spring:
  cloud:
    config:
      server:
        git:
          uri: <http git repo url>
          username: <github id>
          password: <github password>
```

> 보안을 위해 rsa 키 대신 ecdsa을 사용해야 한다

### 공개키 사용 1

1. config-server에서 키 생성

```
ssh-keygen -m PEM -t ecdsa -b 256
```

* 생성 시 파일 이름과 passphrase 관련 질문을 모두 넘기면 다음과 같이 `~/.ssh`에 `id_ecdsa.pub`으로 공개키가 생성된다.

2. public key를 deploy key에 등록하기

그 다음 설정 파일 깃 저장소의 deploy key에 공개키를 저장해주어야 한다. 공개키는 비대칭키를 생성한 서버에서 다음으로 조회가능하다.

```
cat /home1/irteam/.ssh/id_ecdsa.pub
```

* Settings > SSH and GPG keys 탭에서 추가

3. yml 수정

아래의 설정 파일을 복사한 다음에 개인에 맞게 uri, search-paths, hostKey, hostKeyAlgorithm, privateKey, passphrase 부분을 수정

```yaml
spring:
  cloud:
    config:
      server:
        git:
          default-label: master
          uri: git@github.com:MangKyu/spring-cloud-config-sample.git
          search-paths: test-cloud-config-file/**
          ignoreLocalSshSettings: true
          strictHostKeyChecking: false
          hostKey: hostKey // ssh-keyscan 명령어로 확인. hostKey를 base64 인코딩한 값
          hostKeyAlgorithm: ecdsa-sha2-nistp256 // ssh-keyscan 명령어로 확인. 비대칭키 생성 알고리즘
          privateKey: |
            -----BEGIN EC PRIVATE KEY-----
            
            ... 생략
            
            -----END EC PRIVATE KEY-----
          passphrase: passphrase // 비대칭키 생성 시 입력한 값
```

hostkey는 다음과 같이 설정 서버에서 ssh-keyscan으로 찾은 값을 base64 인코딩해주면 된다.

```
ssh-keyscan -t ssh-rsa github.com
```

개인 키는 다음의 명령어로 조회한 값을 입력해주면 된다. 

그런데 주의할 점은 위와 같이 설정 파일에 privateKey를 입력한다면 값에 "|"가 들어가야 하므로 빼먹지 않도록 주의해야 한다는 것이다. 그리고 실행 후 확인해보면 정상적으로 SSL 연결 설정 되었음을 확인할 수 있다.

```
cat /home1/irteam/.ssh/id_ecdsa
```





### 공개키 사용 2

먼저 SSH key 를 생성합니다.

```
$ ssh-keygen -m PEM -t rsa -b 4096 -C "github 이메일"
```

경로는 일반적으로 `~/.ssh` 밑에 생성됩니다. 해당 경로로 들어가보면 공개키와 개인키, 2개의 파일이 생성됩니다.

2. 공개키는 github 에서 사용하고 개인키는 config server 에서 사용해줍니다. -> Settings > SSH and GPG keys 탭에서 추가할 수 있습니다.

### config server에 개인키 추가하기

config server 어플리케이션의 application.yml 에 설정을 수정합니다.

```
spring:
  cloud:
    config:
      server:
        git:
          uri: <github ssh url>
          ignore-local-ssh-settings: true
          private-key: |
            -----BEGIN EC PRIVATE KEY-----
            your-key-here
            your-key-here
            your-key-here
            -----END EC PRIVATE KEY-----
          host-key:  AAAAE2VjZHNhLXNoYTItbm.....
          host-key-algorithm: ecdsa-sha2-nistp256
```

*  **파이프라인 기호( | )를 적어주지 않고 복사하면 에러가 날 수도 있기 때문에 반드시 적어준다. 또한,** -----BEGIN EC PRIVATE KEY----- 부터 -----END EC PRIVATE KEY-----까지 모두 복사해줘야 한다.

RSA 방식이 아닌 ECDSA 형식의 암호화 키를 생성하는법

```bash
$ ssh-keygen -M PEM -t ecdsa -b 256 -C "Github 계정" -f 키파일명
```

키 파일을 생성하는 것만 다르고 Github 계정 Setting에 들어가서 공개키를 업로드하는 부분은 동일하다.

하지만, application.yaml 파일에서 설정을 추가적으로 해줘야한다.

```yaml
private-key: |
             -----BEGIN EC PRIVATE KEY-----
             MHcCAQEEIIQf9VslUAT8vxL6sUBTaKuftWw7E6utoMcsdl4sl3/loAoGCCqGSM49
             ...
             -----END EC PRIVATE KEY-----
host-key: AAAAE2VjZHNhLXNoYTItbmlzdHAy.... # ssh-keyscan github.com의 hostmname
host-key-algorithm: ecdsa-sha2-nistp256    # 호스트 키 알고리즘
```

private-key 는 생성한 개인키를 복사해서 그대로 넣어준다.

그리고 , host-key 부분과 , host-key-algorithm 부분은 ssh-keyscan 명령어를 이용해 찾아서 넣어줄 수 있다.

```bash
$ ssh-keyscan -t ecdsa github.com
# github.com:22 SSH-2.0-babeld-4f04c79d
# host-key-algorith : ecdsa-sha2-nistp256 
# host-key AAAAE2VjZHNhLXNoYTItbmlzdHAyNTYAAAAIbmlzdHAy...
```

이렇게 레포지토리를 수정해준 후 동일하게 프로젝트를 빌드 후 엔드포인트를 호출하면 제대로 가져오는 것을 확인할 수 있다.

## 다른 Service에서 spring cloud config import하기

gradle

```groovy
dependencies {
   implementation 'org.springframework.cloud:spring-cloud-starter-config'
   implementation 'org.springframework.cloud:spring-cloud-starter-bootstrap'
}
```

application.yml 수정

```yaml
spring:
  application:
    name: user-service

  cloud:     # 추가
    config:
      name: ecommerce

# 아래처럼도 사용 가능
spring:
	import: optional:configserver:http://localhost:8888
```

* optional : optional prefix를 설정하지 않으면 config server에 연결할 수 없는 경우 애플리케이션이 종료됩니다.

main/resources에 bootstrap.yml 추가

```yaml
spring:
  cloud:
    config:
      uri: http://127.0.0.1:8888
      name: ecommerce # yml 확장자명을 제외한 파일명
#    profiles:
#      active: dev
			fail-fast: true # fail-fast를 true로 설정하면, Config 서버에 연결할 수 없는 경우 애플리케이션의 시작이 실패
	retry: # Config 서버에 연결 실패 시 재시도할 횟수, 초기 재시도 간격(밀리초), 재시도 간격 증가율을 설정
    max-attempts: 5
    initial-interval: 3000
    multiplier: 1.5
```



### 한 파일에서 여러 profile 관리 (default, dev, prod)

test-db.yml 파일

* 접근 : http://localhost:8888/test-db/profile명

```

test:
  db: good


---
spring:
  profile:
    active: dev

test:
  db: good-dev
  sexy: sexy-dev

---

spring:
  profile:
    active: prod

test:
  db: good-prod
  sexy: sexy-prod

```

접근방법

* http://localhost:8888/test-db/default
* http://localhost:8888/test-db/dev
* http://localhost:8888/test-db/prod



## 여러 구성 파일 사용

* 애플리케이션은 user-service

* profile은 default, local, dev, prod 
* 파일을 db, jwt, redis, aws로 분리

> Spring Cloud Config를 사용할 때, 하나의 파일 내에서 여러 프로필(dev, prod)을 정의하는 것은 직접적으로 지원되지 않는다.
> ---로 나누지말고 각 프로필에 대한 구성을 별도의 파일로 분리하는 것이 표준 접근 방식입니다. 이렇게 함으로써 구성 관리가 명확해지고, 각 환경에 대한 설정을 더 쉽게 관리하고 이해할 수 있습니다.

### `bootstrap.yml` 구성 예시

```yaml
spring:
  application:
    name: user-service
  cloud:
    config:
      uri: http://localhost:8888
      profile: ${SPRING_PROFILES_ACTIVE:prod}
      name: user-service,db,jwt,redis,aws
```

`application.yml 구성 명시 `

```yaml
spring:
  cloud:
    config:
      name: user-service,db,jwt,redis,aws
  profiles:
    active: ${SPRING_PROFILES_ACTIVE:prod}
```

> 반드시 application.yml에 가져올 여러 name(파일명)을 기술해야 한다.

* bootstrap.yml과 application.yml에 동시에 명시 안하면 busrefresh가 동작하지 않는다.

`name` 속성에 명시된 `user-service,db,jwt,redis,aws`에 기반하여, Config 서버는 다음과 같은 파일들을 찾으려고 한다.

- `user-service-dev.yml`, `user-service-prod.yml`
- `db-dev.yml` `db-prod.yml`
- `jwt-dev.yml` `jwt-prod.yml`
- `redis-dev.yml`, ...
- `aws-dev.yml`, ...

## 3. local 파일 시스템 사용

```yaml
spring:
  profiles:
    active: native
  application:
    name: config-service
  cloud:
    config:
      server:
        native:
          search-locations: file:///Users/ysk/temp/git-local-repo
```



## Configuration Change 방법 3가지

1. 서버 재기동
2. Actuator refresh
3. Spring Cloud bus 사용



### Actuator

gradle

```groovy
implementation 'org.springframework.boot:spring-boot-starter-actuator'
```

yml

```yaml
management:
  endpoints:
    web:
      exposure:
        include: refresh, health, beans
```

config 서버 파일 수정 후 /actuator/refresh 호출

```
POST http://localhost:9999/actuator/refresh
```



## Spring Cloud bus로 구성정보 변경

Spring Cloud Bus는 분산 시스템 내의 노드 간에 상태와 메시지를 경량 메시지 브로커를 통해 쉽게 방송할 수 있도록 해주는 도구입니다. 이를 통해 애플리케이션 인스턴스들 간에 상태를 동기화하거나, 설정 변경 같은 이벤트를 전파하는 것이 간편해집니다. Spring Cloud Bus는 주로 Spring Cloud Config 서버와 함께 사용되어, 구성 변경 사항을 자동으로 다른 서비스 인스턴스에 전파하는 데 사용됩니다.

![image-20240303140802872](./images//image-20240303140802872.png)

### 사용 예

Spring Cloud Bus를 사용하여 구성 변경을 자동으로 감지하고 적용하는 예제는 다음과 같습니다:

1. Spring Cloud Config 서버에 연결된 모든 클라이언트(애플리케이션 인스턴스)는 Spring Cloud Bus를 통해 연결됩니다.
2. 구성 변경이 발생하면, 이 변경 사항을 Config 서버가 감지합니다.
3. Config 서버는 Spring Cloud Bus를 통해 변경 사항을 연결된 모든 클라이언트에게 알립니다.
4. 각 클라이언트는 변경 사항을 자동으로 받아, 새로운 구성으로 업데이트됩니다.

```groovy
// 스프링 클라우드 스트림을 사용한 AMQP 지원
implementation 'org.springframework.cloud:spring-cloud-starter-bus-amqp'
implementation 'org.springframework.cloud:spring-cloud-starter-bootstrap'
```

docker-compose

```yaml
version: '3.8'
services:

  rabbitmq:
    image: rabbitmq:3-management-alpine
    container_name: rabbitmq
    ports:
      - "5672:5672" # RabbitMQ 서버
      - "15672:15672" # 관리 인터페이스
    environment:
      RABBITMQ_DEFAULT_USER: user # 기본 사용자 이름 변경
      RABBITMQ_DEFAULT_PASS: password # 기본 사용자 비밀번호 변경
    volumes:
      - "./data:/var/lib/rabbitmq" # 데이터 볼륨
      - "./config:/etc/rabbitmq" # 설정 파일을 위한 볼륨 (선택 사항)
      - "./logs:/var/log/rabbitmq" # 로그 파일을 위한 볼륨 (선택 사항)
```

localhost:15672 접속

bus를 사용하는 application.yml 수정

```yaml
spring:

  rabbitmq:
    host: 127.0.0.1
    port: 5672
    username: user
    password: password
    
# actuator도 수정 필요 -> busrefresh

management:
  endpoints:
    web:
      exposure:
        include: health, refresh, metrics, busrefresh #busrefresh
```

config서버들이 실행될때 config서버의 어떤 데이터를 읽는지 알수 있다. 

```
.c.c.ConfigServicePropertySourceLocator : Fetching config from server at : http://127.0.0.1:8888

2024-03-03T14:20:29.003+09:00  INFO 4940 --- [apigateway-service] [           main] c.c.c.ConfigServicePropertySourceLocator : Located environment: name=ecommerce, profiles=[prod], label=null, version=a4c22e02a8157b84a0450fc9092a8aad4a5fadb5, state=null

2024-03-03T14:20:29.004+09:00  INFO 4940 --- [apigateway-service] [           main] b.c.PropertySourceBootstrapConfiguration : Located property source: [BootstrapPropertySource {name='bootstrapProperties-configClient'}, BootstrapPropertySource {name='bootstrapProperties-file:///Users/ysk/temp/git-local-repo/ecommerce-prod.yml'}, BootstrapPropertySource {name='bootstrapProperties-file:///Users/ysk/temp/git-local-repo/ecommerce.yml'}]
```

* http://127.0.0.1:8888의 prod 프로파일이며 ecommerce-prod.yml



 ecommerce-prod.yml을 변경 하고 config 서버에서 확인해보자.

* http://localhost:8888/ecommerce/prod

```json
{
  "name": "ecommerce",
  "profiles": [
    "prod"
  ],
  "label": null,
  "version": "a4c22e02a8157b84a0450fc9092a8aad4a5fadb5",
  "state": null,
  "propertySources": [
    {
      "name": "file:///Users/ysk/temp/git-local-repo/ecommerce-prod.yml",
      "source": {
        "token.expiration_time": 86400000,
        "token.secret": "ysk-microservice-practice-jwt-secret-key-prod-changed",
        "gateway.ip": "http://192.168.0.4"
      }
    },
    {
      "name": "file:///Users/ysk/temp/git-local-repo/ecommerce.yml",
      "source": {
        "token.expiration_time": 86400000,
        "token.secret": "ysk-microservice-practice-jwt-secret-key-default",
        "gateway.ip": "http://192.168.0.4"
      }
    }
  ]
}
```

* changed로 변경됌

변경을 원하고자 하는 서버의 액츄에이터에 요청을 보낸다

```
POST http://localhost:9999/actuator/busrefresh # 9999는 user-service

api gateway를 사용하고있다면
POST http://api-gateway/actuator/busrefresh
```

* 시간이 좀 걸린다

연결된 서비스에도 변경사항이 전파된다.

> bootstrap.yml과 application.yml에 동시에 명시 안하면 busrefresh가 동작하지 않는다.
> 반드시 같이 명시해야 한다. 



## Spring Cloud config 암호화 처리



1. symmetric Encryption(대칭키 암호화, 공유 키)

대칭키 암호화에서는 암호화와 복호화에 같은 키가 사용. 

암호화와 복호화 과정이 빠르다는 장점이 있습니다. 그러나, 안전하게 키를 공유하는 것이 중요한 도전 과제입니다

2. Asymmetric Encryption (RSA, 비대칭키 암호화 (RSA 키 페어))

비대칭키 암호화에서는 두 개의 키가 사용됩니다: 하나는 공개 키(public key)이며 다른 하나는 개인 키(private key)입니다. 

공개 키는 데이터를 암호화하는 데 사용되며, 누구에게나 제공될 수 있습니다. 

반면, 개인 키는 데이터를 복호화하는 데 사용되며, 반드시 비밀로 유지되어야 합니다.

 이 방식은 키를 안전하게 공유할 필요가 없어 대칭키 방식의 주요 단점을 해결합니다. 그러나, 대칭키 암호화에 비해 계산 과정이 더 복잡하고 느릴 수 있다.



### JCE(Java Cryptography Extension)

JCE 는 Java 에서 암호화와 관련된 다양한 기능을 제공하는 확장 패키지

* JCE : Java 에서 제공하는 비대칭키를 생성하는 기능을 제공하는 암호 관련 라이브러리**
* **JKS : Java 에서 제공하는 JCE 에서 만든 비대칭키를 저장하고 관리하는 저장소**



### 대칭키 암호화

1. Config Server 의 bootstrap.yml 작성

```yml
// Config Server 의 bootstrap.yml 
encrypt:
  key: 0123456789abcdefghijklmnopqrstuvwxyz
```

* 해당 정보는 암호화와 복호화에 사용되는 대칭키

2. Config Server 을 재가동하고 암호화 앤드포인트를 호출

```http
POST http://[Config Server 주소]:[포트]/encrypt

test

response : bf1b2b5b0c9687d9a07157294af4147d3c9b016ef21b35344161aa776e851960
```

* body는 row data로 아무거나 입력. (암호화 할 데이터)

`Config Server` 가 bootstrap.yml 가지고 있는 대칭키로 암호화가 된다 

3. 복호화 방법

```
POST http://[Config Server 주소]:[포트]/decrypt

bf1b2b5b0c9687d9a07157294af4147d3c9b016ef21b35344161aa776e851960 // encrpyt한 데이터

response : test
```



4. 암호화 값 사용

config-server의 설정파일(yml에 다음과 같이 적어 사용한다

```yaml
# in config-server yml file

test: '{cipher}bf1b2b5b0c9687d9a07157294af4147d3c9b016ef21b35344161aa776e851960' 
```



### 비대칭키 암호화

비대칭키 방식은 암/복호화에 사용하는 키가 다른 방식

일반적으로 암호화할 때 Private Key 가 사용되며, 복호화할 때 Public Key 가 사용



### 1. JDK Keytool & JKS 을 통해 Key Store 파일(.jks) 생성

`Keytool` 의 명령어를 통해 `Spring Cloud Config Server` 에서 사용할 key 파일을 생성합니다.

```shell
$ keytool -genkeypair -alias ecommerceEncryptionKey -keyalg RSA \
-dname "CN=ysk, OU=API Development, O=devysk.co.kr, L=KR" -keypass \
"test12" -keystore ecommerceEncryptionKey.jks -storepass "test12"
```

- **-genkeypair : 키쌍(개인키 & 공개키) 을 생성하는 옵션**
- **-alias ecommerceEncrpytionKey : 생성되는 키 쌍에 대한 별칭을 지정 해당 별칭은 Key Store 에서 키 쌍 식별자로 사용(원하는 이름으로 생성하면 됩니다.)**
- **-keyalg RSA : RSA 알고리즘을 사용하여 키 쌍을 생성(RSA 알고리즘은 대표적인 비대칭키 방식의 알고리즘입니다.)**
- **-dname : "CN=ysk, OU=API Development, O=devysk.co.kr, L=KR" :
  인증서에 포함될 사용자의 이름, 조직, 지역 및 국가 정보를 지정**
- **-keypass "test12" : 개인키에 대한 암호를 지정**
- **-keystore ecommerceEncryptionKey.jks : 생성될 키 쌍을 저장할 키 Key Store 파일의 경로와 이름을 지정**
- **-storepass "test12" : Key Store 파일에 대한 암호를 지정**

> ecommerceEncryptionKey.jks 가 생성됌
>
> `Config Server` 가 가지는 키 파일이다.

아래 명령어로 정보 확인 가능

```
keytool -list -keystore ecommerceEncryptionKey.jks -v

password > test12
```

### 2. 공개키와 비밀키 생성

먼저 생성된 Key Store 파일을 이용해 공개키를 위한 인증서

```
$ keytool -export -alias ecommerceEncryptionKey -keystore ecommerceEncryptionKey.jks -rfc -file trust_ecommerceKey.cer
// $ keytool -export -alias [별칭] -keystore [위에서 만들어진 파일명].jks -rfc -file [인증서명].cer
```

`trust_ecommercetKey.cer` 인증서가 생성

다음으로 앞에서 만든 인증서를 통해 `인증된 Key Store 파일` 을 만들어보겠습니다.

```shell
$ keytool -import -alias trust_ecommerceKey -file trust_ecommerceKey.cer -keystore publicKey.jks
// $ keytool -import -alias [별칭] -file [인증서 파일].cer -keystore [새로 만들 파일].jks
```

* publicKey.jks 가 생성된다. 
* `publicKey.jks` 는 공개키만 가지는 Key Store 파일 주로 공개 키를 다른 서비스나 클라이언트에게 제공하는 용도로 사용



### 3. Config Server 에 적용 및 설정 파일 저장소 데이터 암호화

1. config server의 bootstrap.yml에 생성한 key store의 경로 설정.

```
encrypt:
  key-store:
    location: file:///Users/ysk/temp/ecommerceEncryptionKey.jks
    password: test12
    alias: ecommerceEncryptionKey
```

2. Config Server 을 재가동하고 암호화 앤드포인트를 호출

```http
POST http://[Config Server 주소]:[포트]/encrypt

root

response : AQClD~~~
```

* body는 row data로 아무거나 입력. (암호화 할 데이터)

`Config Server` 가 bootstrap.yml 가지고 있는 대칭키로 암호화가 된다 

3. 복호화 방법

```
POST http://[Config Server 주소]:[포트]/decrypt

AQClD~~~ // encrpyt한 데이터

response : test
```

4. config-server의 설정파일(yml에 다음과 같이 적어 사용한다

```yaml
# in config-server yml file

test: '{cipher}AQClD~~~' 
```



# Micro service간 통신

docker exec -it kafka1 kafka-topics.sh --create --topic example-catalog-topic --bootstrap-server localhost:9092



# Kafka

```yaml
version: '3.8'
services:

  rabbitmq:
    image: rabbitmq:3-management-alpine
    container_name: rabbitmq
    ports:
      - "5672:5672" # RabbitMQ 서버
      - "15672:15672" # 관리 인터페이스
    environment:
      RABBITMQ_DEFAULT_USER: user # 기본 사용자 이름 변경
      RABBITMQ_DEFAULT_PASS: password # 기본 사용자 비밀번호 변경
#    volumes:
#      - "./data:/var/lib/rabbitmq" # 데이터 볼륨
#      - "./config:/etc/rabbitmq" # 설정 파일을 위한 볼륨 (선택 사항)
#      - "./logs:/var/log/rabbitmq" # 로그 파일을 위한 볼륨 (선택 사항)

  zookeeper:
    image: confluentinc/cp-zookeeper:7.3.0
    container_name: zookeeper
    ports:
      - "2181:2181"
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
      ZOOKEEPER_TICK_TIME: 2000
    networks:
      - kafka-network

  broker:
    image: confluentinc/cp-kafka:7.3.0
    container_name: broker
    ports:
      - "29092:29092"
      - "9092:9092"
      - "9101:9101"
    depends_on:
      - zookeeper
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: 'zookeeper:2181'
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: "PLAINTEXT:PLAINTEXT,PLAINTEXT_INTERNAL:PLAINTEXT"
      KAFKA_ADVERTISED_LISTENERS: "PLAINTEXT://localhost:9092,PLAINTEXT_INTERNAL://broker:29092"
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
      KAFKA_TRANSACTION_STATE_LOG_MIN_ISR: 1
      KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR: 1
    networks:
      - kafka-network
  connect:
    image: confluentinc/cp-kafka-connect:7.3.0
    container_name: kafka-connect
    depends_on:
      - broker
      - zookeeper
    ports:
      - "8083:8083"
    environment:
      CONNECT_BOOTSTRAP_SERVERS: broker:29092
      CONNECT_REST_PORT: 8083
      CONNECT_GROUP_ID: "quickstart-avro"
      CONNECT_CONFIG_STORAGE_TOPIC: "quickstart-avro-config"
      CONNECT_OFFSET_STORAGE_TOPIC: "quickstart-avro-offsets"
      CONNECT_STATUS_STORAGE_TOPIC: "quickstart-avro-status"
      CONNECT_CONFIG_STORAGE_REPLICATION_FACTOR: 1
      CONNECT_OFFSET_STORAGE_REPLICATION_FACTOR: 1
      CONNECT_STATUS_STORAGE_REPLICATION_FACTOR: 1
      CONNECT_KEY_CONVERTER: "org.apache.kafka.connect.json.JsonConverter"
      CONNECT_VALUE_CONVERTER: "org.apache.kafka.connect.json.JsonConverter"
      CONNECT_INTERNAL_KEY_CONVERTER: "org.apache.kafka.connect.json.JsonConverter"
      CONNECT_INTERNAL_VALUE_CONVERTER: "org.apache.kafka.connect.json.JsonConverter"
      CONNECT_REST_ADVERTISED_HOST_NAME: "localhost"
      CONNECT_LOG4J_ROOT_LOGLEVEL: WARN
      CONNECT_PLUGIN_PATH: '/usr/share/java,/usr/share/confluent-hub-components,/etc/kafka-connect/jars'
    command:
      - bash
      - -c
      - |
        confluent-hub install --no-prompt confluentinc/kafka-connect-jdbc:latest &&
        echo "Starting Kafka Connect" &&
        /etc/confluent/docker/run
    networks:
      - kafka-network

networks:
  kafka-network:
    name: kafka-network
    driver: bridge
#  // Kafka Topic 생성
#  docker exec -it kafka kafka-topics.sh --create --topic topic-example1 --bootstrap-server localhost:9092
#
#
#토픽 리스트 확인: kafka-topics.sh --list --bootstrap-server localhost:9092
#토픽 상세 조회: kafka-topics.sh --describe --topic topic1 --bootstrap-server kafka:9092
#토픽 삭제: kafka-topics.sh --delete --bootstrap-server kafka:9092 --topic topic1
```



create connector-config.json

```json
{
  "name": "my-source-connect",
  "config": {
    "connector.class": "io.confluent.connect.jdbc.JdbcSourceConnector",
    "connection.url": "jdbc:mysql://host.docker.internal:3306/sc_msa",
    "connection.user": "root",
    "connection.password": "root",
    "mode": "incrementing",
    "incrementing.column.name": "id",
    "table.whitelist": "sc_msa.users",
    "topic.prefix": "my_topic_",
    "tasks.max": "1"
  }
}
```

post

```
 curl -X POST -H "Content-Type: application/json" --data @connector-config.json http://localhost:8083/connectors
```



```
GET http://localhost:8083/connectors/my-source-connect/status
```



Kafka Sink Connect 추가

```json
{
  "name": "my-sink-connect",
  "config": {
    "connector.class": "io.confluent.connect.jdbc.JdbcSinkConnector",
    "connection.url": "jdbc:mysql://host.docker.internal:3306/sc_msa",
    "connection.user": "root",
    "connection.password": "root",
    "auto.create" : "true",
    "auto.evolve" : "true",
    "delete.enabled" : "false",
    "tasks.max" : "1",
    "topics" : "my_topic_users"
  }
}
```

```
 curl -X POST -H "Content-Type: application/json" --data @sink-connector-config.json http://localhost:8083/connectors
```



# 장애처리 - CircuiteBreaker with Resillience4j



```groovy
implementation 'org.springframework.cloud:spring-cloud-starter-circuitbreaker-resilience4j'
```



config

```java

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
```

적용

```java

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	private final Environment env;
	private final OrderServiceClient orderServiceClient;
	private final CatalogServiceClient catalogServiceClient;

	private final CircuitBreakerFactory circuitBreakerFactory;

	@Override
	public UserDto getUserByUserId(String userId) {
		UserEntity userEntity = userRepository.findByUserId(userId);

		if (userEntity == null) {
			throw new UsernameNotFoundException("User not found");
		}

		UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);

		log.info("Before call orders microservice");

		CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitBreaker1");

		List<ResponseOrder> ordersList = circuitBreaker.run(() -> orderServiceClient.getOrders(userId),
			throwable -> new ArrayList<>()); // 예외발생시 

		List<ResponseCatalog> catalogList = catalogServiceClient.getCatalogs();

		userDto.setOrders(ordersList);
		userDto.setCatalogs(catalogList);

		log.info("After called orders microservice");

		return userDto;
	}
}
```



# MicroService 분산 추적 - Zipkin

## Zipkin

- ﻿﻿https://zipkin.io/

![image-20240304153329369](./images//image-20240304153329369.png)

Twitter에서 사용하는 분산 환경의 Timing 데이터 수집, 추적 시스템 (오픈소스)

Google Drapper에서 발전하였으며, 분산환경에서의 시스템 병목 현상 파악

Collector, Query Service, Databasem WebU로 구성

\- Span -  하나의 요청에 사용되는 작업의 단위.  64 bit unique ID

\- Trace - 트리 구조로 이뤄진 Span 셋 하나의 요청에 대한 같은 Trace ID 발급

## ﻿﻿Spring Cloud Sleuth

https://spring.io/projects/spring-cloud-sleuth

- ﻿﻿스프링 부트 애플리케이션을 Zipkin과 연동
- 요청 값에 따른 Trace Ip, Span ID 부여 Trace와 Span Ids를 로그에 추가 가능

- ﻿﻿servlet filter, rest template, scheduled actions, message channels, feign client

![image-20240304153945837](./images//image-20240304153945837.png)



## zipkin 설치

https://zipkin.io/pages/quickstart.html

```shell
docker run -d -p 9411:9411 openzipkin/zipkin
```

## Java

If you have Java 17 or higher installed, the quickest way to get started is to fetch the [latest release](https://search.maven.org/remote_content?g=io.zipkin&a=zipkin-server&v=LATEST&c=exec) as a self-contained executable jar:

```
curl -sSL https://zipkin.io/quickstart.sh | bash -s
java -jar zipkin.jar
```



### zipkin 사용을 위한 설정

애석하게도 Sleuth [공식 문서](https://docs.spring.io/spring-cloud-sleuth/docs/current-SNAPSHOT/reference/html/)를 보면 Spring Boot 3 버전부터 사용할 수 없다는 공지가 있습니다.

 기존 프로젝트는 [Micrometer Tracing](https://micrometer.io/docs/tracing)으로 이전한다고 하네요.

 [Spring Boot 공식 문서 13.8. Tracing](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#actuator.micrometer-tracing)에 다양한 분산 추적 라이브러리와 연동하는 방법이 설명

gradle

```groovy
dependencies {
  // Spring MVC를 사용한다고 가정
  // 만약 MVC, WebFlux를 모두 사용하지 않는다면 `io.zipkin.reporter2:zipkin-sender-urlconnection` 의존성이 필요합니다.
  implementation 'org.springframework.boot:spring-boot-starter-web'

  implementation 'org.springframework.boot:spring-boot-starter-actuator'
  implementation 'io.micrometer:micrometer-tracing-bridge-brave'
  implementation 'io.zipkin.reporter2:zipkin-reporter-brave'
}
```

yml

```yaml
management:
  tracing:
    sampling:
      probability: 1.0
    propagation:
      consume: B3
      produce: B3_MULTI
  zipkin:
    tracing:
      endpoint: "http://localhost:9411/api/v2/spans"
  endpoints:
    web:
      exposure:
        include: refresh, health, beans, busrefresh
        
# logging 설정도 추가
logging:
  level:
    com.example.userservice.client: DEBUG
  pattern:
    level: '%5p [${spring.application.name:},%X{traceId:-},%X{spanId:-}]'

```

* 빠뜨리면 안되는 설정인 `management.propagation`의 `consume`, `produce` 확인합니다. 
* MSA 환경에서 추적 문맥(Trace context)을 다른 서비스로 전파할 때 HTTP 헤더에 정보를 넣어서 전송합니다. 이 때 HTTP 헤더에서 어떤 이름을 사용할지 여러 선택지가 있습니다. 
* 서비스에서 외부 서비스로 요청할 때 추적 문맥 전파 방식은 `produce`, 외부 서비스에서 현재 서비스로 보내는 요청의 추적 문맥을 해석하는 방식은 `consume`으로 따로 설정
* 여기서는 3가지 중 하나를 선택할 수 있습니다.
  1. [W3C propagation](https://www.w3.org/TR/trace-context/) (기본값)
  2. [B3 single header](https://github.com/openzipkin/b3-propagation#single-header)
  3. [B3 multiple headers](https://github.com/openzipkin/b3-propagation#multiple-headers)



log 설정

```java
log.info("Before call orders microservice");

CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitBreaker1");
CircuitBreaker circuitBreaker2 = circuitBreakerFactory.create("circuitBreaker2");

List<ResponseOrder> ordersList = circuitBreaker.run(() -> orderServiceClient.getOrders(userId),
			throwable -> new ArrayList<>());

log.info("After called orders microservice");		
```

