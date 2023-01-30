# Restdocs pretty print - 이쁘게 출력하기

기본적으로 요청과 응답의 body가 텍스트로 쭉 나열된다. 크기가 작으면 상관 없지만, json 객체가 커진다면 제대로 확인하기 힘들어진다.

pretty print 기능은 말 그대로 예쁘게 포메팅해준다.

스니펫을 모아 `DocumentFilter`를 만들때 넣어주면 된다.

```java
RestDocumentationFilter restDocumentationFilter = document(
        "simple-read",
    	preprocessRequest(prettyPrint()),
        preprocessResponse(prettyPrint()),
        simplePathParameterSnippet(),
        simpleRequestParameterSnippet(),
        simpleResponseFieldsSnippet()
);
```

기본 설정으로 사용하려면 초기화할때 넣어준다.

```java
@BeforeEach
void setUpRestDocs(RestDocumentationContextProvider restDocumentation) {
    Filter documentationConfiguration = documentationConfiguration(restDocumentation)
            .operationPreprocessors()
            .withRequestDefaults(prettyPrint())
            .withResponseDefaults(prettyPrint());

    this.spec = new RequestSpecBuilder()
            .addFilter(documentationConfiguration)
            .build();
}
```



# 불필요한 헤더 제거 - Restdocs header 제거 

API문서에 불필요한 헤더가 포함되어 보인다. 이를 없애줄 수도 있다.

```java
@BeforeEach
void setUpRestDocs(RestDocumentationContextProvider restDocumentation) {
    Filter documentationConfiguration = documentationConfiguration(restDocumentation)
            .operationPreprocessors()
            .withRequestDefaults(prettyPrint())
            .withResponseDefaults(
                    removeHeaders(
                            "Transfer-Encoding",
                            "Date",
                            "Keep-Alive",
                            "Connection"
                    ),
                    prettyPrint()
            );

    this.spec = new RequestSpecBuilder()
            .addFilter(documentationConfiguration)
            .build();
}
```



# 매개변수화된 출력 디렉토리

`DocumentFilter`를 만들면서 어떤 디렉토리에 저장할지 정해진다.

```java
RestDocumentationFilter restDocumentationFilter = document(
        "simple-read", // 디렉토리명
        simplePathParameterSnippet(),
        simpleRequestParameterSnippet(),
        simpleResponseFieldsSnippet()
);
```

일일이 이름을 정해줘야 한다. 개발자의 숙명이지만 항상 어렵다.

이런 고통을 덜어주는 기능이 있다. 미리 정해진 변수를 적어주면 클래스명과 메소드명 그리고 스텝 순서를 불러올 수 있다.

| Parameter     | Description                         |
| ------------- | ----------------------------------- |
| {methodName}  | 메소드명                            |
| {method-name} | 메소드명을 kebab-case로 포메팅 한다 |
| {meth         | 메소드명을 snake_case로 포메팅 한다 |
| {ClassName}   | 클래스명                            |
| {class-name}  | 클래스명을 kebab-case로 포메팅 한다 |
| {class_name}  | 클래스명을 snake_case로 포메팅 한다 |
| {step}        | 현재 테스트의 실행 순서를 불러온다  |

테스트를 나눠서 애매한 경우도 처리 가능하다. 테스트 메소드를 기준으로 이름을 만들어주기 때문이다.

```java
RestDocumentationFilter restDocumentationFilter = document(
        "{class-name}/{method-name}",
        preprocessResponse(prettyPrint()),
        simplePathParameterSnippet(),
        simpleRequestParameterSnippet(),
        simpleResponseFieldsSnippet()
);

@Test
void test1() {
    RequestSpecification given = RestAssured.given(this.spec)
                .baseUri(BASE_URL)
                .port(port)
                .pathParam("id", 1)
                .queryParam("name", "name")
                .filter(restDocumentationFilter);
}

@Test
void test2() {
    RequestSpecification given = RestAssured.given(this.spec)
                .baseUri(BASE_URL)
                .port(port)
                .pathParam("id", 1)
                .queryParam("name", "name")
                .filter(restDocumentationFilter);
}
```

위의 경우는 `클래스명/test1`과 `클래스명/test2`가 만들어진다.

MockMvc와 REST Assured에서만 사용 가능하고, WebTestClient는 사용 불가능하다.



# 배열 표현 - Array

배열은 `a.b[].c`와 같이 표현할 수 있다.

> 이외에도 여러가지 표현식이 있는데, [JSON Field Paths](https://docs.spring.io/spring-restdocs/docs/current/reference/html5/#documenting-your-api-request-response-payloads-fields-json-field-paths)를 참고하자.

일반적인 경우에는 문제가 없겠지만, 배열을 동적으로 생성하거나 해야 하는 경우에 표현이 애매할 수 있다. 이럴 때 `a.*[].c`와 같이 와일드 카드로 처리해줄 수 있다.





### 참조

* https://velog.io/@dae-hwa/Spring-REST-Docs-%EC%82%B4%ED%8E%B4%EB%B3%BC%EB%A7%8C%ED%95%9C-%EA%B8%B0%EB%8A%A5%EB%93%A4