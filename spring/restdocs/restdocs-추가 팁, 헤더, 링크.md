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



# 스프링 기반 REST API 개발

## 3. HATEOAS와 Self-Decribtive Message 적용

## 포스팅 참조 정보

### GitHub

공부한 내용은 **GitHub**에 공부용 **Organizations**에 정리 하고 있습니다

### 해당 포스팅에 대한 내용의 GitHub 주소

실습 내용이나 자세한 소스코드는 **GitHub**에 있습니다
포스팅 내용은 간략하게 추린 핵심 내용만 포스팅되어 있습니다

https://github.com/freespringlecture/spring-rest-api-study/tree/chap03-05_rest_docs_snippets_create

### 해당 포스팅 참고 인프런 강의

https://www.inflearn.com/course/spring_rest-api/dashboard

### 실습 환경

- **Java Version: Java 11**
- **SpringBoot Version: 2.1.2.RELEASE**

# 5. 스프링 REST Docs: 링크, (Req, Res) 필드와 헤더 문서화

## 요청 필드 문서화

- `requestFields()` + `fieldWithPath()`
- `responseFields()` + `fieldWithPath()`
- `requestHeaders()` + `headerWithName()`
- `responseHedaers()` + `headerWithName()`
- `links()` + `linkWithRel()`

## 테스트 할 것

**API** 문서 만들기

- 요청 본문 문서화: **request-body**

- 응답 본문 문서화: **response-body**

- 링크 문서화:

   

  links

  - **self**
  - **query-events**
  - **update-event**
  - **profile** 링크 추가

- 요청 헤더 문서화: **requestHeaders**

- 요청 필드 문서화: **requestFields**

- 응답 헤더 문서화: **responseHeaders**

- 응답 필드 문서화: **responseFields**

## Relaxed 접두어

**Relaxed** 접두어를 사용하지 않고 전부 다 문서화 하는 것을 권장

**API**가 변경되었을 때 변경을 테스트가 감지해서 적절하게 문서도 바뀐 **API**코드에 맞춰서 업데이트를 할 수 있으므로

- 장점: 문서 일부분만 테스트 할 수 있다
- 단점: 정확한 문서를 생성하지 못한다

## 링크, 요청(헤더, 필드), 응답(헤더, 본문) 문서화 적용

```java
...

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

...

public class EventControllerTests {

    ...

    @Test
        @TestDescription("정상적으로 이벤트를 생성하는 테스트")
        public void createEvent() throws Exception {
            mockMvc.perform(post("/api/events/")
                    .contentType(MediaType.APPLICATION_JSON_UTF8) //요청타입
                    .accept(MediaTypes.HAL_JSON) //받고싶은 타입
                    .content(objectMapper.writeValueAsString(event))) //event를 json을 String으로 맵핑

                    ...

                    .andDo(document("create-event", //문서 이름
                        links(  //링크 문서화
                                linkWithRel("self").description("link to self"),
                                linkWithRel("query-events").description("link to query events"),
                                linkWithRel("update-event").description("link to update an existing event")
                        ),
                        requestHeaders( //요청 헤더 문서화
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        requestFields( //요청 필드 문서화
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("description").description("date time of begin of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("date time of begin of new event"),
                                fieldWithPath("closeEnrollmentDateTime").description("date time of close of new event"),
                                fieldWithPath("beginEventDateTime").description("date time of begin of new event"),
                                fieldWithPath("endEventDateTime").description("date time of end of new event"),
                                fieldWithPath("location").description("location of new event"),
                                fieldWithPath("basePrice").description("base price of new event"),
                                fieldWithPath("maxPrice").description("max price of new event"),
                                fieldWithPath("limitOfEnrollment").description("limit of enrollment")
                        ),
                        responseHeaders( //응답 헤더 문서화
                                headerWithName(HttpHeaders.LOCATION).description("Location header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                        ),
                        responseFields( //응답 본문 문서화
                                fieldWithPath("id").description("identifier of new event"),
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("description").description("date time of begin of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("date time of begin of new event"),
                                fieldWithPath("closeEnrollmentDateTime").description("date time of close of new event"),
                                fieldWithPath("beginEventDateTime").description("date time of begin of new event"),
                                fieldWithPath("endEventDateTime").description("date time of end of new event"),
                                fieldWithPath("location").description("location of new event"),
                                fieldWithPath("basePrice").description("base price of new event"),
                                fieldWithPath("maxPrice").description("max price of new event"),
                                fieldWithPath("limitOfEnrollment").description("limit of enrollment"),
                                fieldWithPath("free").description("it tells if this event is free or not"),
                                fieldWithPath("offline").description("it tells if this event is offline meeting or not"),
                                fieldWithPath("eventStatus").description("event status"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.query-events.href").description("link to query events list"),
                                fieldWithPath("_links.update-event.href").description("link to update an existing event")
                        )
                ))

                ...
```





# 스프링 기반 REST API 개발

## 3. HATEOAS와 Self-Decribtive Message 적용

## 포스팅 참조 정보

### GitHub

공부한 내용은 **GitHub**에 공부용 **Organizations**에 정리 하고 있습니다

### 해당 포스팅에 대한 내용의 GitHub 주소

실습 내용이나 자세한 소스코드는 **GitHub**에 있습니다
포스팅 내용은 간략하게 추린 핵심 내용만 포스팅되어 있습니다

https://github.com/freespringlecture/spring-rest-api-study/tree/chap03-06_rest-docs-build

### 해당 포스팅 참고 인프런 강의

https://www.inflearn.com/course/spring_rest-api/dashboard

### 실습 환경

- **Java Version: Java 11**
- **SpringBoot Version: 2.1.2.RELEASE**

# 6. 스프링 REST Docs: 문서 빌드

## 스프링 REST Docs

https://docs.spring.io/spring-restdocs/docs/current/reference/html5/

### pom.xml에 메이븐 플러그인 설정

```xml
<dependency> 
    <groupId>org.springframework.restdocs</groupId>
    <artifactId>spring-restdocs-mockmvc</artifactId>
    <version>{project-version}</version>
    <scope>test</scope>
</dependency>

<build>
    <plugins>
        <plugin> 
            <groupId>org.asciidoctor</groupId>
            <artifactId>asciidoctor-maven-plugin</artifactId>
            <version>1.5.8</version>
            <executions>
                <execution>
                    <id>generate-docs</id>
                    <phase>prepare-package</phase> 
                    <goals>
                        <goal>process-asciidoc</goal>
                    </goals>
                    <configuration>
                        <backend>html</backend>
                        <doctype>book</doctype>
                    </configuration>
                </execution>
            </executions>
            <dependencies>
                <dependency> 
                    <groupId>org.springframework.restdocs</groupId>
                    <artifactId>spring-restdocs-asciidoctor</artifactId>
                    <version>{project-version}</version>
                </dependency>
            </dependencies>
        </plugin>
    <plugin> 
      <groupId>org.asciidoctor</groupId>
      <artifactId>asciidoctor-maven-plugin</artifactId>
      <!-- … -->
    </plugin>
    <plugin> 
      <artifactId>maven-resources-plugin</artifactId>
      <version>2.7</version>
      <executions>
        <execution>
          <id>copy-resources</id>
          <phase>prepare-package</phase>
          <goals>
            <goal>copy-resources</goal>
          </goals>
          <configuration> 
            <outputDirectory>
              ${project.build.outputDirectory}/static/docs
            </outputDirectory>
            <resources>
              <resource>
                <directory>
                  ${project.build.directory}/generated-docs
                </directory>
              </resource>
            </resources>
          </configuration>
        </execution>
      </executions>
    </plugin>
    </plugins>
</build>
```

### 템플릿 파일 추가

https://github.com/freelife1191
https://github.com/freespringlecture/spring-rest-api-study/blob/chap03-06_rest-docs-build/src/main/asciidoc/index.adoc

```
src/main/asciidoc/index.adoc
= REST API Guide
프리라이프;
:doctype: book
:icons: font
:source-highlighter: highlightjs
:toc: left
:toclevels: 4
:sectlinks:
:operation-curl-request-title: Example request
:operation-http-response-title: Example response

[[overview]]
= 개요

[[overview-http-verbs]]
== HTTP 동사

본 REST API에서 사용하는 HTTP 동사(verbs)는 가능한한 표준 HTTP와 REST 규약을 따릅니다.

|===
| 동사 | 용례

| `GET`
| 리소스를 가져올 때 사용

| `POST`
| 새 리소스를 만들 때 사용

| `PUT`
| 기존 리소스를 수정할 때 사용

| `PATCH`
| 기존 리소스의 일부를 수정할 때 사용

| `DELETE`
| 기존 리소스를 삭제할 떄 사용
|===

[[overview-http-status-codes]]
== HTTP 상태 코드

본 REST API에서 사용하는 HTTP 상태 코드는 가능한한 표준 HTTP와 REST 규약을 따릅니다.

|===
| 상태 코드 | 용례

| `200 OK`
| 요청을 성공적으로 처리함

| `201 Created`
| 새 리소스를 성공적으로 생성함. 응답의 `Location` 헤더에 해당 리소스의 URI가 담겨있다.

| `204 No Content`
| 기존 리소스를 성공적으로 수정함.

| `400 Bad Request`
| 잘못된 요청을 보낸 경우. 응답 본문에 더 오류에 대한 정보가 담겨있다.

| `404 Not Found`
| 요청한 리소스가 없음.
|===

[[overview-errors]]
== 오류

에러 응답이 발생했을 때 (상태 코드 >= 400), 본문에 해당 문제를 기술한 JSON 객체가 담겨있다. 에러 객체는 다음의 구조를 따른다.

include::{snippets}/errors/response-fields.adoc[]

예를 들어, 잘못된 요청으로 이벤트를 만들려고 했을 때 다음과 같은 `400 Bad Request` 응답을 받는다.

include::{snippets}/errors/http-response.adoc[]

[[overview-hypermedia]]
== 하이퍼미디어

본 REST API는 하이퍼미디어와 사용하며 응답에 담겨있는 리소스는 다른 리소스에 대한 링크를 가지고 있다.
응답은 http://stateless.co/hal_specification.html[Hypertext Application from resource to resource. Language (HAL)] 형식을 따른다.
링크는 `_links`라는 키로 제공한다. 본 API의 사용자(클라이언트)는 URI를 직접 생성하지 않아야 하며, 리소스에서 제공하는 링크를 사용해야 한다.

[[resources]]
= 리소스

[[resources-index]]
== 인덱스

인덱스는 서비스 진입점을 제공한다.


[[resources-index-access]]
=== 인덱스 조회

`GET` 요청을 사용하여 인덱스에 접근할 수 있다.

operation::index[snippets='response-body,http-response,links']

[[resources-events]]
== 이벤트

이벤트 리소스는 이벤트를 만들거나 조회할 때 사용한다.

[[resources-events-list]]
=== 이벤트 목록 조회

`GET` 요청을 사용하여 서비스의 모든 이벤트를 조회할 수 있다.

operation::get-events[snippets='response-fields,curl-request,http-response,links']

[[resources-events-create]]
=== 이벤트 생성

`POST` 요청을 사용해서 새 이벤트를 만들 수 있다.

operation::create-event[snippets='request-fields,curl-request,http-request,request-headers,http-response,response-headers,response-fields,links']

[[resources-events-get]]
=== 이벤트 조회

`Get` 요청을 사용해서 기존 이벤트 하나를 조회할 수 있다.

operation::get-event[snippets='request-fields,curl-request,http-response,links']

[[resources-events-update]]
=== 이벤트 수정

`PUT` 요청을 사용해서 기존 이벤트를 수정할 수 있다.

operation::update-event[snippets='request-fields,curl-request,http-response,links']
```

## 문서 생성하기

`mvn package` 해주면 `target/generated-docs`, `target/classes/static/docs` 에 `index.html` 이 생성됨

- ```
  mvn package
  ```

  - **test**
  - **prepare-package** :: **process-asciidoc**
  - **prepare-package** :: **copy-resources**

- 문서확인

  - 아래의 경로에서 확인 가능
    - `/docs/index.html`

## 생성 과정 설명

### index.html 생성

- **asciidoctor-maven-plugin**이 패키징할때 **prepare-package**에 **process-asciidoc**을 처리하라고 함
- **package**라는 `maven goal`을 실행할때 **asciidoctor-maven-plugin**이 제공하는
- **process-asciidoc** 이라는 기능이 실행이 된거고 이 기능은 기본적으로 `src/main/asciidoc` 안에 들어있는
- 모든 **asciidoc** 문서를 **html**로 만들어줌

### `target/classes/static/docs` 경로에 카피

- **maven-resources-plugin**의 기능 중에 **copy-resources**라는 기능을 **prepare-package**에 끼워넣음
- 순서가 중요함 **asciidoctor-maven-plugin** 다음에 **maven-resources-plugin**를 처리해야함
- **copy**는 `resources/resource/directory` 의 디렉토리의 모든 파일을 **outputDirectory**로 카피해줌

### 스프링 부트 정적 리소스 지원 기능

**build**된 디렉토리 기준으로 **static** 디렉토리 안에 있으면 서버에서 리소스 접근이 가능

### profile 추가

```java
...

@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class EventController {

  ...

  @PostMapping
  public ResponseEntity createEvent(@RequestBody @Valid EventDto eventDto, Errors errors) {

    ...

    EventResource eventResource = new EventResource(event); //이벤트를 이벤트리소스로 변환

    ...

    eventResource.add(new Link("/docs/index.html").withRel("profile")); //profile

    ...
```

### profile 링크 문서화에 추가

```java
...

public class EventControllerTests {

    ...

    @Test
        @TestDescription("정상적으로 이벤트를 생성하는 테스트")
        public void createEvent() throws Exception {
            mockMvc.perform(post("/api/events/")
                    .contentType(MediaType.APPLICATION_JSON_UTF8) //요청타입
                    .accept(MediaTypes.HAL_JSON) //받고싶은 타입
                    .content(objectMapper.writeValueAsString(event))) //event를 json을 String으로 맵핑
                    ...
                    .andDo(document("create-event",
                            links(  //링크 문서화

                            ...

                                    linkWithRel("profile").description("link to update an existing event")

                            ...

                            responseFields( //응답 본문 문서화

                            ...

                                    fieldWithPath("_links.profile.href").description("link to profile")

                            ...
```

## 테스트 할 것

API 문서 만들기

- 요청 본문 문서화
- 응답 본문 문서화
- 링크문서화
  - **self**
  - **query-events**
  - **update-event**
  - **profile** 링크 추가
- 요청 헤더 문서화
- 요청 필드 문서화
- 응답 헤더 문서화
- 응답 필드 문서화