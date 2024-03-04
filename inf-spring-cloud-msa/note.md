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

