# Feign 클라이언트 사용

[toc]

Feign 이란 선언적으로 사용할 수 있는 Client이다.

- (= Feign is a declarative web service client. )
- ref : https://docs.spring.io/spring-cloud-openfeign/docs/current/reference/htm



Feign 클라이언트의 주요 기능

- Connection/Read Timeout
- Feign Interceptor
- Feign Logger
- Feign ErrorDecoder

1. **Connection/Read Timeout**:
   - **Connection Timeout**: 클라이언트가 서버에 연결을 시도할 때까지의 최대 시간을 나타냅니다. 이 시간 내에 연결이 이루어지지 않으면 연결 시간 초과 오류가 발생합니다.
   - **Read Timeout**: 클라이언트가 서버의 응답을 기다리는 최대 시간을 나타냅니다. 서버로부터 데이터를 읽는 동안 지정된 시간을 초과하면 읽기 시간 초과 오류가 발생합니다.
2. **Feign Interceptor**:
   - Feign Interceptor는 Feign 클라이언트의 요청을 보내기 전이나 응답을 받은 후에 추가적인 작업을 수행하기 위해 사용됩니다.
   - 예를 들어, 모든 요청에 공통 헤더를 추가하거나 로깅을 수행하는 등의 작업을 Interceptor에서 수행할 수 있습니다.
3. **Feign Logger**:
   - Feign은 로깅을 위한 내장 Logger를 제공합니다. 이를 사용하여 Feign 클라이언트의 요청과 응답에 대한 세부 정보를 로깅할 수 있습니다.
   - 로깅 레벨을 조정하여 원하는 만큼의 세부 정보를 로깅할 수 있습니다 (예: 기본적인 요청 정보, 헤더, 본문 등).
4. **Feign ErrorDecoder**:
   - Feign의 ErrorDecoder는 원격 서비스에서 오류 응답을 받았을 때 해당 응답을 해석하고 적절한 예외로 변환하는 역할을 합니다.
   - 이를 사용하면 서비스 간의 통신에서 발생하는 오류를 통일된 방식으로 처리하거나, 특정 오류 코드에 대한 사용자 정의 예외를 발생시킬 수 있습니다.



## 의존성 추가

```groovy
// Feign
implementation 'org.springframework.cloud:spring-cloud-starter-openfeign'
```

## yml 설정

```yaml
feign:
  url:
    prefix: http://localhost:8080/target_server # DemoFeignClient에서 사용할 url prefix 값
  client:
    config:
      default:
        connectTimeout: 1000
        readTimeout: 3000
        loggerLevel: NONE
      demo-client: # DemoFeignClient에서 사용할 Client 설정 값
        connectTimeout: 1000
        readTimeout: 10000
        loggerLevel: HEADERS # 여기서 설정한 값은 FeignCustomLogger -> Logger.Level logLevel 변수에 할당됨

logging:
  level:
    feign:
      logger:
        level: FULL

# [로거 레벨 옵션]
# 참조: feign.Logger.Level
# ```
# NONE, // 로깅 없음.
# BASIC, // 요청 메서드와 URL, 그리고 응답 상태 코드와 실행 시간만 로깅.
# HEADERS, // 기본 정보와 함께 요청 및 응답 헤더 로깅.
# FULL // 요청 및 응답의 헤더, 본문, 메타데이터 모두 로깅.
# ```
```



## FeignClient 설정

```java
@FeignClient(
	name = "demo-client", // application.yaml에 설정해 놓은 값을 참조
	url = "${feign.url.prefix}", // application.yaml에 설정해 놓은 값을 참조 (= http://localhost:8080/target_server)
	configuration = DemoFeignConfig.class)
public interface DemoFeignClient {

	@GetMapping("/get")// "${feign.url.prefix}/get"으로 요청
	ResponseEntity<BaseResponseInfo> callGet(
		@RequestHeader(CUSTOM_HEADER_NAME) String customHeader,
		@RequestParam("name") String name,
		@RequestParam("age") Long age
	);

	@PostMapping("/post") // "${feign.url.prefix}/post"로 요청
	ResponseEntity<BaseResponseInfo> callPost(@
		RequestHeader(CUSTOM_HEADER_NAME) String customHeader,
		@RequestBody BaseRequestInfo baseRequestInfo
	);

	@GetMapping("/errorDecoder")
	ResponseEntity<BaseResponseInfo> callErrorDecoder();

	@GetMapping("/get_member")
	ResponseEntity<Member> findByUsername(
		@SpringQueryMap Map<String, Object> param
	);

  @GetMapping("/member/{id}"}
  ResponseEntity<Member> findById(
		@PathVariable(id) Long id
  );
}
```



## Feign 커스텀 Log 설정

```java
import feign.Logger;

@Configuration
public class FeignConfig {

    @Bean
    public Logger feignLogger() {
        return new FeignCustomLogger();
    }
}

```

```java

@Slf4j
@RequiredArgsConstructor
public class FeignCustomLogger extends feign.Logger {
	private static final int DEFAULT_SLOW_API_TIME = 3_000;
	private static final String SLOW_API_NOTICE = "Slow API";

	@Override
	protected void log(String configKey, String format, Object... args) {
		// log를 어떤 형식으로 남길지 정해준다.
		System.out.printf(feign.Logger.methodTag(configKey) + format + "%n", args); // feign.methodTag
	}

	@Override
	protected void logRequest(String configKey, Logger.Level logLevel, Request request) {
		/**
		 * [값]
		 * configKey = DemoFeignClient#callGet(String,String,Long)
		 * logLevel = BASIC # "feign.client.config.demo-client.loggerLevel" 참고
		 *
		 * [동작 순서]
		 * `logRequest` 메소드 진입 -> 외부 요청 -> `logAndRebufferResponse` 메소드 진입
		 *
		 * [참고]
		 * request에 대한 정보는
		 * `logAndRebufferResponse` 메소드 파라미터인 response에도 있다.
		 * 그러므로 request에 대한 정보를 [logRequest, logAndRebufferResponse] 중 어디에서 남길지 정하면 된다.
		 * 만약 `logAndRebufferResponse`에서 남긴다면 `logRequest`는 삭제해버리자.
		 */
		System.out.println(request);
	}

	@Override
	protected Response logAndRebufferResponse(String configKey, Logger.Level logLevel,
		Response response, long elapsedTime) throws IOException {
		/**
		 * [참고]
		 * - `logAndRebufferResponse` 메소드내에선 Request, Response에 대한 정보를 log로 남길 수 있다.
		 * - 매소드내 코드는 "feign.Logger#logAndRebufferResponse(java.lang.String, feign.Logger.Level, feign.Response, long)"에서 가져왔다.
		 *
		 * [사용 예]
		 * 예상 요청 처리 시간보다 오래 걸렸다면 "Slow API"라는 log를 출력시킬 수 있다.
		 * ex) [DemoFeignClient#callGet] <--- HTTP/1.1 200 (115ms)
		 *     [DemoFeignClient#callGet] connection: keep-alive
		 *     [DemoFeignClient#callGet] content-type: application/json
		 *     [DemoFeignClient#callGet] date: Sun, 24 Jul 2022 01:26:05 GMT
		 *     [DemoFeignClient#callGet] keep-alive: timeout=60
		 *     [DemoFeignClient#callGet] transfer-encoding: chunked
		 *     [DemoFeignClient#callGet] {"name":"customName","age":1,"header":"CustomHeader"}
		 *     [DemoFeignClient#callGet] [Slow API] elapsedTime : 3001
		 *     [DemoFeignClient#callGet] <--- END HTTP (53-byte body)
		 */

		String protocolVersion = resolveProtocolVersion(response.protocolVersion());
        
		String reason = response.reason() != null
			&& logLevel.compareTo(Level.NONE) > 0 ? " " + response.reason() : "";
		int status = response.status();
		
        log(configKey, "<--- %s %s%s (%sms)", protocolVersion, status, reason, elapsedTime);
		
        if (logLevel.ordinal() >= Level.HEADERS.ordinal()) {

			for (String field : response.headers()
										.keySet()) {
				if (shouldLogResponseHeader(field)) {
					for (String value : valuesOrEmpty(response.headers(), field)) {
						log(configKey, "%s: %s", field, value);
					}
				}
			}

			int bodyLength = 0;
			if (response.body() != null && !(status == 204 || status == 205)) {
				// HTTP 204 No Content "...response MUST NOT include a message-body"
				// HTTP 205 Reset Content "...response MUST NOT include an entity"
				if (logLevel.ordinal() >= Level.FULL.ordinal()) {
					log(configKey, ""); // CRLF
				}
				byte[] bodyData = Util.toByteArray(response.body()
														   .asInputStream());
				bodyLength = bodyData.length;
				if (logLevel.ordinal() >= Level.HEADERS.ordinal() && bodyLength > 0) {
					log(configKey, "%s", decodeOrDefault(bodyData, UTF_8, "Binary data"));
				}
				if (elapsedTime > DEFAULT_SLOW_API_TIME) {
					log(configKey, "[%s] elapsedTime : %s", SLOW_API_NOTICE, elapsedTime);
				}
                
				log(configKey, "<--- END HTTP (%s-byte body)", bodyLength);
				
                return response.toBuilder()
							   .body(bodyData)
							   .build();
			} else {
				log(configKey, "<--- END HTTP (%s-byte body)", bodyLength);
			}
		}
		return response;
	}
}
```

## 인터셉터, 디코더 설정

```java
@Configuration
public class DemoFeignConfig {

    @Bean
    public DemoFeignInterceptor feignInterceptor() {
        return DemoFeignInterceptor.of();
    }

    @Bean
    public DemoFeignErrorDecoder DemoErrorDecoder() {
        return new DemoFeignErrorDecoder();
    }

}
```

```java
import feign.Request.HttpMethod;
import feign.RequestInterceptor;
import feign.RequestTemplate;

@RequiredArgsConstructor(staticName = "of")
public final class DemoFeignInterceptor implements RequestInterceptor {

	@Override
	public void apply(RequestTemplate template) { // 필요에 따라 template 필드 값을 활용하자!

		// get 요청일 경우
		if (template.method() == HttpMethod.GET.name()) {
			System.out.println("[GET] [DemoFeignInterceptor] queries : " + template.queries());
			// ex) [GET] [DemoFeignInterceptor] queries : {name=[CustomName], age=[1]}
			return;
		}

		// post 요청일 경우
		String encodedRequestBody = StringUtils.toEncodedString(template.body(), UTF_8);
		System.out.println("[POST] [DemoFeignInterceptor] requestBody : " + encodedRequestBody);
		// ex) [POST] [DemoFeignInterceptor] requestBody : {"name":"customName","age":1}

		// Do Something
		// ex) requestBody 값 수정 등등

		// 새로운 requestBody 값으로 설정
		template.body(encodedRequestBody);
	}
}

```

```java
public final class DemoFeignErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder errorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        final HttpStatus httpStatus = HttpStatus.resolve(response.status());

        /**
         * [참고]
         * 외부 컴포넌트와 통신 시
         * 정의해놓은 예외 코드 일 경우엔 적절하게 핸들링하여 처리한다.
         */
        if (httpStatus == HttpStatus.NOT_FOUND) {
            System.out.println("[DemoFeignErrorDecoder] Http Status = " + httpStatus);
            throw new RuntimeException(String.format("[RuntimeException] Http Status is %s", httpStatus));
        }

        return errorDecoder.decode(methodKey, response);
    }

}

```

