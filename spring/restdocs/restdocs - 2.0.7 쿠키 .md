# Spring 2.7 버전대 RestDocs Cookie 문서화 방법(RestDocs 2.0.7)

* SpringBoot 2.7.8
* Spring Rest Docs 2.0.7



프로젝트를 진행하던 중, 인증과정에서 RefreshToken을 Cookie로 사용할 일이 있어 RestDocs로 Cookie를 문서화 하여야 했습니다.

이와 관련해서 RestDocs 공식문서, github 이슈, StackOverFlow글을 관련하여 찾아보았습니다.

* https://github.com/spring-projects/spring-restdocs/pull/592
* https://docs.spring.io/spring-restdocs/docs/2.0.7.RELEASE/reference/html5/
* https://stackoverflow.com/questions/39472873/how-can-i-document-the-cookie-using-spring-restdoc





**결론적으로, RestDocs 2.0.7 에서는 Request, Response Cookie를 문서화 하는 API를 지원하지 않습니다.**

찾아보니, **Spring Boot 3.0.0** , **RestDocs 3.0.0 부터 지원**하는것을 확인하였습니다.

* https://docs.spring.io/spring-restdocs/docs/current/api/
* [org.springframework.restdocs.cookies](https://docs.spring.io/spring-restdocs/docs/current/api/org/springframework/restdocs/cookies/package-summary.html) 패키지

현재로써는, Cookie 때문에 프로젝트 버전을 3.0.0로 올리기에 **수정해야 하는 점이 많아 비효율적이라 판단**했고, 변경할 수 없었습니다.

그래서 RestDocs를 분석하고 **Custom해서 Cookie를 문서화 하는 방법을 구현**했습니다.



## 코드 분석

일반적으로 웹사이트에서 사용하는 Cookie는 사용자의 웹 브라우저에 담깁니다.

쿠키는 Http프로토콜의 일부이며 Http 요청 및 응답 Header를 통해 전달됩니다.  



그래서 RestDocs에서 문서화 할 때, requestHeader() 메소드나 responseHeader() 메소드를 이용하면 될 것 같으나 그렇지 않습니다.

이유는 다음과 같습니다.

```java
public class RequestHeadersSnippet extends AbstractHeadersSnippet {

	protected RequestHeadersSnippet(List<HeaderDescriptor> descriptors) {
		this(descriptors, null);
	}

	protected RequestHeadersSnippet(List<HeaderDescriptor> descriptors, 
                                  Map<String, Object> attributes) {
		super("request", descriptors, attributes);
	}

	@Override
	protected Set<String> extractActualHeaders(Operation operation) {
		return operation.getRequest().getHeaders().keySet();
  }
}
```

RestDocs에서 Header를 문서화 하려면 AbstractHeadersSnippet 클래스를 상속받은 `RequestHeadersSnippet 클래스`를 사용해야 합니다.

`extractActualHeaders(`) 메소드에서 헤더의 이름을 추출합니다. 

* 여기서 실제로 헤더의 `key값`을 꺼내서 반환하는것을 볼 수 있습니다.

하지만 Spring에서 Cookie는 Request의 `headers` 로 꺼내지 않고 `cookies()`라는 메소드로 꺼낼 수 있습니다. 

* 위 코드를 보면, `operation.getRequest().getHeaders().keySet(); `로 꺼내므로 사용할 수 없습니다. 

<img src="https://blog.kakaocdn.net/dn/cgdUIK/btr4WG4LrwO/OvwyHEanScCxA5dY98xmV1/img.png" width = 500 height = 350>

<img src="https://blog.kakaocdn.net/dn/bYY0h2/btr40RStjmt/T5veLwR0VW1pKsvcYr2B6k/img.png" width = 500 height = 300>

* RequestHeadersSnippet은 `getRequest().getHeaders()`를 사용하여 헤더 값을 꺼낸다.
* 하지만 실제로 쿠키는 `getRequest().getCookies()` 메소드에 들어있다. 

문제는 여기서 발견할 수 있었고 여기서 해결 방법을 찾아냈습니다.  



## 해결 방법 및 문서화 방법

Cookie를 문서화할 때, **RequestHeadersSnippet 를 사용하지 말고**,

**AbstractHeadersSnippet를 상속받은 클래스**를 만들고  **extractActualHeaders() 메소드에서 쿠키값을 반환하도록** 합니다



### 1. 헤더 스니펫을 만들기위해 AbstractHeadersSnippet 을 상속받은 클래스를 만듭니다.

```java
public class RequestCookiesSnippet extends AbstractHeadersSnippet {

	protected RequestCookiesSnippet(List<HeaderDescriptor> descriptors,
		Map<String, Object> attributes) {
		super("cookie-request", descriptors, attributes);
	}

	@Override
	protected Set<String> extractActualHeaders(Operation operation) {
		return operation.getRequest().getCookies().stream().map(
			RequestCookie::getName
		).collect(Collectors.toSet());
	}
  
  public static RequestCookiesSnippet customRequestHeaderCookies(
		HeaderDescriptor... descriptors) {
		return new RequestCookiesSnippet(Arrays.asList(descriptors), null);
	}

	public static HeaderDescriptor cookieWithName(String cookieName) {
		return headerWithName(cookieName);
	}
}
```

* `super("cookie-request", descriptors, attributes);` : 쿠키를 문서화할 custom snippet 파일을 지정합니다.
  * [restdocs custom snippet을 이용한 문서 작성 방법](https://0soo.tistory.com/201)
* 핵심은 extractActualHeaders 메소드 입니다. 

* RequestHeadersSnippet 클래스는 headers().keySet()을 반환하지만, RequestCookiesSnippet는 getCookies()를 반환하도록 합니다.
* `customRequestHeaderCookies()` static method는 `RequestCookiesSnippet클래스`를 편하게 사용하기 위해 선언하였습니다.
* `cookieWithName(cookieName) `메소드로 쿠키를 추출하도록 하였습니다.



### 2. 문서화될 custom snippet 파일을 작성합니다.

```
|===
|Name|Description

{{#headers}}
|{{#tableCellContent}}`+{{name}}+`{{/tableCellContent}}
|{{#tableCellContent}}{{description}}{{/tableCellContent}}

{{/headers}}
|===
```

* 파일 이름은 위 `RequestCookiesSnippet`에서 쓰이는 것과 같이 `cookie-request-headers.snippet` 으로 지정하고 src/test/resources/org/springframework/restdocs/templates 경로에 저장합니다.

### 3. mockMvc 테스트에서 사용 

```java
mockMvc.perform(post("/api/auth/token")
				.characterEncoding(StandardCharsets.UTF_8)
				.cookie(refreshTokenCookie)
			).andExpect(status().isOk())
			.andDo(this.restDocs.document(
					customRequestHeaderCookies(
						cookieWithName("RefreshToken").description("리프레시 토큰 쿠키 이름")
					),
					responseHeaders(
						headerWithName("Authorization").description("Refresh 된 access Token")
					),
					responseFields(
						fieldWithPath("accessToken").description("refresh된 accessToken. Type:  Bearer")
					)
				)
);
```

* 이제 `customRequestHeaderCookies()` static 메소드와 `cookieWithName()` static method를 이용해서 쿠키도 문서화가 가능합니다.

<img src="https://blog.kakaocdn.net/dn/bQjhKN/btr41IAES8p/FusvGuBAyCbPRZ6XKkFSw0/img.png" width = 700 height = 550>





