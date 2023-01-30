### HTTP Headers

요청 및 응답 헤더에 대한 세팅을 할 수 있다. 각각 `request-headers.adoc` 과 `response-headers.adoc` 파일이 생성된다. [Docs Reference](https://docs.spring.io/spring-restdocs/docs/current/reference/html5/#documenting-your-api-http-headers)

```java
this.mockMvc
	.perform(get("/people").header("Authorization", "Basic dXNlcjpzZWNyZXQ=")) 
	.andExpect(status().isOk())
	.andDo(document("headers",
			requestHeaders( 
					headerWithName("Authorization").description(
							"Basic auth credentials")), 
			responseHeaders( 
					headerWithName("X-RateLimit-Limit").description(
							"The total number of requests permitted per period"),
					headerWithName("X-RateLimit-Remaining").description(
							"Remaining requests permitted in current period"),
					headerWithName("X-RateLimit-Reset").description(
							"Time at which the rate limit period will reset"))));
```

