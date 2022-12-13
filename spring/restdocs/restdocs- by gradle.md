# RestDocs By Gradle 설정

## 환경

- Spring Boot 2.7.6
- Gradle 7.2
- Java 11
- junit5
- MocvMvc



## build.gradle



* 먼저 전체 설정

```groovy
plugins {
    id 'java'
    id 'org.springframework.boot' version '2.7.6'
    id 'io.spring.dependency-management' version '1.0.15.RELEASE'
    id "org.asciidoctor.jvm.convert" version "3.3.2"
}

configurations {
    compileOnly {
        extendsFrom annotationProcessor
    }
    asciidoctorExt
}

repositories {
    mavenCentral()
}

ext {
    set('snippetsDir', file("build/generated-snippets"))
}

dependencies {
    
    ...
    testImplementation 'org.springframework.restdocs:spring-restdocs-mockmvc'
    asciidoctorExt 'org.springframework.restdocs:spring-restdocs-asciidoctor'
}

tasks.named('test') {
    outputs.dir snippetsDir
    useJUnitPlatform()
}

tasks.named('asciidoctor') {
    inputs.dir snippetsDir
    dependsOn test
}

test {
    useJUnitPlatform()
    outputs.dir snippetsDir
}

asciidoctor {
    inputs.dir snippetsDir
    configurations 'asciidoctorExt'
    dependsOn test
}

asciidoctor.doFirst {
    delete file('src/main/resources/static/docs')
}

task copyDocument(type: Copy) {
    dependsOn asciidoctor
    from file("build/docs/asciidoc")
    into file("src/main/resources/static/docs")
}

build {
    dependsOn copyDocument
}

`
```



### gradle 명령어

* `task` : `gradle`을 통해 실행되는 단위 

- `ext` : 전역 변수 셋팅 
- `asciidoctor` : `testDocument`를 의존하여 테스트를 실행하고, `snippetsDir`에서 `snippets`을 참조하여 문서를 생성한다. 
- `bootJar` : `jar` 빌드 시 `asciidoctor`를 참조하여 문서를 생성하고, `snippetsDir`에 있는 `html5`파일을 `static/docs`로 복사한다. 복사하는 이유는 api 요청으로 문서 접근을 위함 
- `copyDocument` : `from` 디렉토리에 있는 API 문서 파일을 `into`로 복사한다. 복사하는 이유는 api 요청으로 문서 접근을 위함. 테스트 시에는 이걸 쓰고, 배포 시에는 `bootjar`를 씀. 
- `testDocument` : `restdocs` 전용 테스트 실행 task로 `**.documnetation.*`에 해당하는 테스트만 실행한다.
- `includeTestMatching` : 실행할 테스트 패턴 정의    
  - 우리는 `Documentation`이라는 RestDocs 전용 부모 클래스를 상속받아 사용하기 때문에    `**.documnetation.*`로 지정    
  - - [https://docs.gradle.org/current/javadoc/org/gradle/api/tasks/testing/TestFilter.html](https://docs.gradle.org/current/javadoc/org/gradle/api/tasks/testing/TestFilter.html) 

  



## Plugin 추가 - Asciidoctor 플러그인을 적용

```groovy
plugins {
    id 'java'
    id 'org.springframework.boot' version '2.7.6'
    id 'io.spring.dependency-management' version '1.0.15.RELEASE'
    id "org.asciidoctor.jvm.convert" version "3.3.2" <<<<<< 이부분
}
```

.

gradle 7 부터는 `org.asciidoctor.convert`가 아닌`asciidoctor.jvm.convert`를 사용해야한다.

이 플러그인은 adoc 파일 변환, build 디렉토리에 복사하기 위한 역할을 한다.

### `build` 하위에 docs 디렉토리가 생기지 않는 경우 - 버전 문제 

[공식 문서](https://spring.io/guides/gs/testing-restdocs/)나 많은 블로그 글을 따라 진행해봤을때, `build` 하위에 docs 디렉토리가 생기지 않는 경우, 버전 문제이다.

스프링 부트 2.7, gradle 7 이상 을 사용하는 경우에는 공식문서에 나와있는대로 진행하면 docs 디렉토리가 생기지 않는다.

gradle 7부터는 `id 'org.asciidoctor.convert' version '1.5.6'` 대신 `id "org.asciidoctor.jvm.convert" version "3.3.2"`를 사용해야한다.



## configurations - Asciidoctor를 확장하는 종속성에 대한 구성을 선언 `asciidoctorExt`

```groovy
configurations {
    compileOnly {
        extendsFrom annotationProcessor
    }
    asciidoctorExt <<< 이부분 
}
```



## 의존성 추가 

asciidoctorExt 이렇게 하면 파일 `snippets`에서 사용할 속성을 `.adoc`가리키 도록 자동으로 구성된다 build/generated-snippets

```groovy
dependencies {
    
    testImplementation 'org.springframework.restdocs:spring-restdocs-mockmvc'
    asciidoctorExt 'org.springframework.restdocs:spring-restdocs-asciidoctor'
}

```

1. asciidoctorExt  : 이렇게 하면 파일 `snippets`에서 사용할 속성을 `.adoc`가리키 도록 자동으로 구성된다 build/generated-snippets
2. spring-restdocs-mockmvc : . MockMvc 대신 또는 REST Assured를 사용하려면 각각에 `WebTestClient`대한 종속성을 추가한다 .`spring-restdocs-webtestclient`



## 스니펫 디렉토리 설정

ext는 전역변수를 설정해주는 것이다. gradle은 `build/generated-snippets`에 스니펫이 생성되므로, 스니펫 생성 디렉토리를 변수에 담아준다.

```groovy
ext {
    set('snippetsDir', file("build/generated-snippets"))
}
```

* 생성된 스니펫의 출력 위치를 정의하도록 속성을 구성한다.

```groovy
test {
    useJUnitPlatform()
    outputs.dir snippetsDir
}
```

* `test`스니펫 디렉터리를 출력으로 추가하도록 작업을 구성한다 .



## asciidoctor 추가

asciidoctor 설정. 작업을 구성한다

```groovy
asciidoctor {
    dependsOn test // 문서가 생성되기 전에 테스트가 실행되도록 한다.
    inputs.dir snippetsDir // 스니펫 디렉터리를 입력으로 구성한다.
}

asciidoctor.doFirst {
    delete file('src/main/resources/static/docs') // 기존에 존재하는 docs를 삭제해준다.
}
```



## bootJar 추가

```groovy
bootJar {
    dependsOn asciidoctor
    copy {
        from "${asciidoctor.outputDir}"
        into 'BOOT-INF/classes/static/docs'
    }
}
```

스니펫을 이용해 문서 작성 후, `build - docs - asciidoc` 하위에 생기는 html 파일을 `BOOT-INF/classes/static/docs`로 복사해준다.

> 이 때 반드시 src/docs/asciidoc 안에 *.adoc 파일이 존재해야 한다.



이 설정은 생성된 문서를 **jar파일에 패키징**하는 설정이다.

> You may want to package the generated documentation in your project’s jar file … - [Spring REST Docs의 Packaging the Documentation](https://docs.spring.io/spring-restdocs/docs/2.0.4.RELEASE/reference/html5/#getting-started-build-configuration-packaging-the-documentation)

```nginx
bootJar {
    dependsOn asciidoctor
    copy {
        from "${asciidoctor.outputDir}"
        into 'BOOT-INF/classes/static/docs'
    }
}
```

* 프로젝트 전체를 jar파일로 만들 때 restdocs가 해당 jar파일에 들어가게끔 한다.

* 실행 시에 asciidoctor.outputDir에 있는 html 파일을 BOOT-INF/classes/static/docs의 경로 아래로 복사해주는 역할을 한다.

  


만약 `build/asciidoc/html5/`에 `html`파일을 `src/main/resources/static/doc` 복사해주고 싶으시다면 아래 설정을 추가해 주면 된다

```gradle
task copyDocument(type: Copy) {
    dependsOn asciidoctor
 
    from file("build/asciidoc/html5/")
    into file("src/main/resources/static/docs")
}
 
build {
    dependsOn copyDocument
}
```

- gradle build 시 test -> asciidoctor -> bootjar 순으로 실행되며, build/docs/asciidoc에 html 문서가 생성되며, 이를 src/main/resources/static/docs 에 복사하여 저장한다.
- gradle 설정은 각자 다 다르기 때문에 [공식문서](https://docs.spring.io/spring-restdocs/docs/current/reference/html5/#getting-started-build-configuration) 를 참고하면 좋을 것 같다.



## copyDocument - 문서 복사

```groovy
task copyDocument(type: Copy) {
    dependsOn asciidoctor
    from file("build/docs/asciidoc")
    into file("src/main/resources/static/docs")
}
```

gradle build 시 test -> asciidoctor -> bootjar 순으로 실행되며

`build/docs/asciidoc` 파일을 `src/main/resources/static/docs`로 복사해준다.

> ./gradle build 또는 인텔리제이 오른쪽 Tasks/build 에서 build 하기 



# 반드시 src/docs/asciidoc 하위에 adoc 파일을 만들것!

생성된 파일을 묶어 html api문서로 만들기 위해서 gradle인 경우 `src/docs/asciidoc` 하위에 adoc 파일을 만들어줘야한다.(네이밍은 자유!)

* 만들어준 파일에 생성된 스니펫을 이용해서 문서를 작성하면 된다.



## 진행 순서 - 밑에 매커니즘과 같다

1. 테스트과 통과되면 각 테스트에 맞게 sinppets가 생성된다
2. 생성된 snippets를 이용하여 문서를 작성한다 - `src/docs/asciidoc/index.adoc` (adoc 파일 이름은 자유 )

3. gradle 빌드하면 2번 문서인 adoc 파일을 기반으로 html 문서가 생성된다
4. main/resources/static/docs로 3번 html 문서가 복사된다





## [Rest Docs API 문서 생성 매커니즘](https://0soo.tistory.com/124#Rest%--Docs%--API%--%EB%AC%B-%EC%--%-C%--%EC%--%-D%EC%--%B-%--%EB%A-%A-%EC%BB%A-%EB%-B%--%EC%A-%--)

우선, Rest Docs의 문서 생성 매커니즘을 다음과 같다.

1. MockMvc로 작성한 테스트 코드를 실행한다.
2. 테스트가 통과하면 `build/generated-snippets` 하위에 스니펫(문서조각)들이 생성된다.  
   

> *gradle은 build/generated-snippets에 스니펫이 생성된다.*

  


1. `build/generated-snippets` 하위에 생성된 스니펫들을 묶어서 HTML 문서를 만들기 위해서, `src/docs/asciidoc` 하위에 스니펫들을 묶은 adoc문서를 만든다.
2. 스니펫을 이용해서 `src/docs/asciidoc` 하위에 adoc 파일을 생성했다면, `./gradlew build` 명령어를 통해 빌드를 해준다.
3. 빌드가 완료되면 `resources - static - docs` 하위에 HTML 문서가 생성된다.
4. 어플리케이션을 실행 한 후, `http://localhost:8080/docs/{HTML 파일명}` 을 웹브라우저에 검색하면 생성한 REST API 문서를 확인 할 수 있다.



> **❗❗ API문서 url은 코드를 통해 변경 가능하다.**



### [❗유의할 점](https://0soo.tistory.com/124#%E-%-D%--%EC%-C%A-%EC%-D%--%ED%--%A-%--%EC%A-%--)

resources - static - docs 하위의 HTML 파일은 실제로는 build.gradle의 설정파일에 따라서 위와같이 build - docs - asciidoc 하위의 HTML 파일을 복사해온 파일이다.

AsciiDoc 플러그인을 설치하면 인텔리제이 상에서도 REST API 문서를 실시간으로 확인할수 있다.







## 빌드 후 최신 반영 하는법

 asciidoctor.doFirst를 사용하여 기존에 존재하는 html 문서를 지워준 후, copyDocument로 직접 복사하면 최신화가 된다.



### 커스텀 스니펫 적용

커스템 스니펫을 적용하려고 찾아보다가
`src/test/resources/org/springframework/restdocs/templates`
하위에 .snippet 파일을 만들면 적용된다는 글을 보고 해보았으나 실패했다.

좀 더 찾아본 결과 asciidoctor라는 디렉토리를 하나 더 만들어야했다.
`src/test/resources/org/springframework/restdocs/templates/asciidoctor`

이 하위에 스니펫 파일을 만드니까 정상 작동했다.



### pathVariable 

기본적으로 `MockMvcRequestBuilders.get()`을 많이 사용하였는데, pathParameters를 문서화 해주려면 `RestDocumentationRequestBuilders.get()`을 써줘야 한다.







# Rest-docs 살펴볼만한 기능들

* https://velog.io/@dae-hwa/Spring-REST-Docs-%EC%82%B4%ED%8E%B4%EB%B3%BC%EB%A7%8C%ED%95%9C-%EA%B8%B0%EB%8A%A5%EB%93%A4

* https://techblog.woowahan.com/2597/
  * 필수값 여부 : 입력 필드들이 필수값인지 아닌지 여부 표시
  * 입력 포맷 : 예를 들어 날짜 같은 경우 yyyy-MM-dd 로 할지 yyyyMMdd 로 할지 표시
  * 입력해야하는 코드 표기 : 성별 같은경우 MALE, FEMALE 을 받는데 이것을 표시
  * 공통 포맷 : code, message, data 포맷은 공통이기에 한번만 표시



## RestDocs .adoc Example

### .adoc Exmaple

```
= Spring REST Docs
:toc: left
:toclevels: 2
:sectlinks:

[[resources-post]]
== Member

[[resources-post-create]]
=== getAll

==== HTTP request

include::{snippets}/member-get-all/http-request.adoc[] // member-get-all은 테스트시 document로 만든 문서 이름

==== HTTP response

include::{snippets}/member-get-all/http-response.adoc[] // 이 문서 이름은 generated-snippets 밑에 디렉토리로 존재 
```



```
= Natural REST API Guide 
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

include::{snippets}/create-event/response-fields.adoc[]

예를 들어, 잘못된 요청으로 이벤트를 만들려고 했을 때 다음과 같은 `400 Bad Request` 응답을 받는다.

include::{snippets}/create-event/http-response.adoc[]

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

operation::create-event[snippets='request-fields,curl-request,http-request, request-headers, http-response, response-headers, response-fields, links']

[[resources-events-get]]
=== 이벤트 조회

`Get` 요청을 사용해서 기존 이벤트 하나를 조회할 수 있다.

operation::get-event[snippets='request-fields,curl-request,http-response,links']

[[resources-events-update]]
=== 이벤트 수정

`PUT` 요청을 사용해서 기존 이벤트를 수정할 수 있다.

operation::update-event[snippets='request-fields,curl-request,http-response,links']
```



```
=  API
~~~ -api-docs
:doctype: book
:icons: font
:source-highlighter: highlightjs
:toc: left
:toclevels: 4
:sectlinks:

ifndef::snippets[]
:snippets: ./build/generated-snippets
endif::[]

== ~~~ Home

=== ~~~ 생성


==== Request Sample
include::{snippets}/post/http-request.adoc[]

==== Request Field
include::{snippets}/post/request-fields.adoc[]

==== Response Sample
include::{snippets}/post/http-request.adoc[]

==== Response Fields
include::{snippets}/post/response-fields.adoc[]
```

1. =  는 섹션을 나누게 해주는 부분이다. 섹션은 총 5단계까지 표현이 가능하다. 섹션이 중요한 점은 Table of Contents의 소제목으로 쓰이기 때문이다.

2. include sms 우리가 테스트 코드를 통해 만든 여러 adoc파일을 주입해주는 부분이다. 



## Controller Test Code 작성



* @AutoConfigureRestDocs : restdocs를 위해 사용하는 bean이나 어노테이션들을 가져와 준다. 
  * https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/test/autoconfigure/restdocs/AutoConfigureRestDocs.html
  * target/generated-snippets dir 생성하고 테스트 코드를 통해 snippets를 추가해주는 애노테이션이다



* [JUnit 5 테스트 설정 링크](https://docs.spring.io/spring-restdocs/docs/current/reference/htmlsingle/#getting-started-documentation-snippets-setup-junit-5)

JUnit 5를 사용할 때 문서 조각 생성의 첫 번째 단계는 `RestDocumentationExtension`테스트 클래스에 적용한다. 

```java
@ExtendWith(RestDocumentationExtension.class)
public class JUnit5ExampleTests {
```

일반적인 Spring 애플리케이션을 테스트할 때 다음도 적용해야 한다. `SpringExtension`.

```java
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public class JUnit5ExampleTests {
```

는 `RestDocumentationExtension`프로젝트의 빌드 도구에 따라 출력 디렉터리로 자동 구성됩니다.

| 빌드 도구 | 출력 디렉토리               |
| :-------- | :-------------------------- |
| 메이븐    | `target/generated-snippets` |
| 그레이들  | `build/generated-snippets`  |



- @ExtendWith(RestDocumentationExtension.class 는 필수 !
- [공식문서](https://docs.spring.io/spring-restdocs/docs/current/reference/html5/#getting-started-documentation-snippets-setup)를 보면 MockMvc를 @Before method 통해 설정해주었지만, 해당 프로젝트에서 uriHost 명을 설정하기 위해 @AutoConfigureRestDocs 어노테이션을 사용하였기 때문에 따로 @Before 과정이 필요 없다.





### 테스트 코드 작성

테스트 코드의 초기 setting.

```java
@ExtendWith(RestDocumentationExtension.class) // When using JUnit5 
@SpringBootTest
public class MemberControllerTest {

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
            RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }
    
}
```

* 위 테스트가 복잡하다면 [@AutoConfigureMockMvc](https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/test/autoconfigure/restdocs/AutoConfigureRestDocs.html)와 [@AutoConfigureRestDocs](https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/test/autoconfigure/web/servlet/AutoConfigureMockMvc.html)를 이용하여 간단하게 setup 할 수 있다.



### In Spring Boot Test

```java
@AutoConfigureMockMvc // -> webAppContextSetup(webApplicationContext)
@AutoConfigureRestDocs // -> apply(documentationConfiguration(restDocumentation))
@SpringBootTest
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
}
```

이렇게 해주시면 위의 복잡한 설정을 대신 해줄 수 있다. 

* 자세히 알고 싶다면 [@AutoConfigureMockMvc](https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/test/autoconfigure/restdocs/AutoConfigureRestDocs.html)와 [@AutoConfigureRestDocs](https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/test/autoconfigure/web/servlet/AutoConfigureMockMvc.html)를 참고



### In WebMvcTest

```java
@MockBean(JpaMetamodelMappingContext.class) // JpaMetamodel을 사용하기 위하여 필요.
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(controllers = {MemberController.class})
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

}
```





## 테스트 코드 예제 1

```java
this.mvc.perform(post(TEST_URL)
        ...
        .andDo(document("/post",					// (1)
                preprocessRequest(prettyPrint()),   // (2)
                preprocessResponse(prettyPrint()),  // (3)
                requestFields( 						// (4)
                        fieldWithPath("todo").description("할 일")

                ),									
                responseFields(						// (5)
                        fieldWithPath("id").description("사용자 id"), //
                        fieldWithPath("todo").description("할 일")
                ))
        );;
```

1. 우리의 테스트 코드로 인해 생길 adoc의 경로를 설정해주는 부분이다. 우리가 앞써 gradle에서 설정한 snippetsDir의 하위 루트를 의미한다.

2. 우리가 문서에 나올 json은 html입장에서는 그냥 string이다. 따라서 우리가 브라우저에서 보는 예쁜 형식으로 Json을 보여주기 위해서는 다음과 같이 preprocess~ 를 넣어주어 해당 과정에서 생기는 json에 대한 처리를 prettyprint()로 명시해줘야한다.

3. 2번과 같이 응답(Response)를 예쁜 형식으로 보여주기위해 설정

4. request-fields.adoc을 만들어주는 역할을 한다. 우리가 만약 api의 요구조건에 request 부분에 json이 들어간다고 할 때 해당 json의 field가 어떤 역할을 하는지 decription을 통해 적어줄 수 있다. 그렇게 되면 다음과 같이 부분이 자동으로 만들어진다.

5. 마지막으로 response 영역에 대한 설명이다.

   * 특정 필드를 숨기고 싶다면 ignored()를 사용 

   * 만약 우리가 id를 response fields에서 숨기고 싶다면

   * ```java
     fieldWithPath("id").description("사용자 id").ignored()
     ```



## 테스트 코드 예제2



* [참조]() https://tecoble.techcourse.co.kr/post/2020-08-18-spring-rest-docs/)

### Post

```java
this.mockMvc.perform(post("/posts") // 1
            .content("{\"title\": \"title\", \n\"content\": \"content\"}") // 2
            .contentType(MediaType.APPLICATION_JSON)) // 3
            .andExpect(status().isCreated()) // 4
            .andDo(document("post-create", // 5
                    requestFields( // 6
                            fieldWithPath("title").description("Post 제목"), // 7
                            fieldWithPath("content").description("Post 내용").optional() // 8
                    )
            ));
```

1 - 요청 방식(get, post 등)은 post를 선택하고 `/posts`를 호출.

2 - create는 RequestBody를 받기 때문에 content 안에 보낼 데이터를 입력.

3 - create는 application/json 형식으로 요청을 받는다는 의미

4 - 정상적으로 동작 시 `isCreated`상태 코드로 응답한다는 의미

5 - 이 documentation의 이름을 “post-create”로 하겠다는 의미

6 - create는 requestFields를 받기 때문에 문서에 requestFields을 명시하겠다는 의미

7 - `fieldWithPath`는 key 값을, `description`는 `fieldWithPath`에 대한 설명을 작성.

8 - Test를 할 때 만약 `content`의 값이 없다면 테스트는 실패한다 . 따라서 `content`와 같이 `null`일 수 있다면 `optional()`을 붙여주면 된다.

  


### get

```java
@Test
void findAll() throws Exception {
    List<PostResponse> postResponses = Lists.newArrayList(
        new PostResponse(1L, "title1", "content1"),
        new PostResponse(2L, "title2", "content2")
    );

    when(postService.findAll()).thenReturn(postResponses);

    this.mockMvc.perform(get("/posts")
            .accept(MediaType.APPLICATION_JSON)) // 1
            .andExpect(status().isOk())
            .andDo(document("post-get-all",
                    responseFields( // 2
                            fieldWithPath("[].id").description("Post Id"), // 3
                            fieldWithPath("[].title").description("Post 제목"),
                            fieldWithPath("[].content").description("Post 내용")
                    )
            ));
}

@Test
void findById() throws Exception {
    final PostResponse postResponse = new PostResponse(1L, "title", "content");
    when(postService.findById(anyLong())).thenReturn(postResponse);

    this.mockMvc.perform(get("/post/{postId}", postResponse.getId()) // 4
            .accept(MediaType.APPLICATION_JSON))
        	.andExpect(status().isOk())
        	.andDo(document("post-get-one",
                    pathParameters( // 5
                            parameterWithName("postId").description("Post Id") // 6
                    ),
                    responseFields(
                            fieldWithPath("id").description("Post Id"),
                            fieldWithPath("title").description("Post 제목"),
                            fieldWithPath("content").description("Post 내용")
                    )
            ));
}
```

1. findAll는 application/json 형식으로 응답을 보내겠다는 의미.

2. findAll는 responseFields 보내기 때문에 responseFields를 명시

3. 설명은 create의 7번과 같고, List형식은 `[].id`처럼 앞에 `[]`를 해야 한다. - [참고](https://docs.spring.io/spring-restdocs/docs/2.0.4.RELEASE/reference/html5/#documenting-your-api-request-response-payloads-fields-reusing-field-descriptors)

4. PathVariable로 받는 값(ex. `postResponse.getId()`)은 위와 같이 넣을 수 있다.

5. findById는 PathVariable을 받기 때문에 PathVariable를 문서에 명시한다는 의미.

6. pathParameters는 parameterWithName를 사용하여 PathVariable의 Name(postId)을 명시할 수 있고 description은 설명을 적어주면 된다



### Update, Delete

```java
@Test
void update() throws Exception {
    this.mockMvc.perform(put("/post/{postId}", 1L)
            .content("{\"title\": \"turtle\", \n\"content\": \"context\"}")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("post-update",
                    pathParameters(
                            parameterWithName("postId").description("Post Id")
                    ),
                    requestFields(
                            fieldWithPath("title").description("Post 제목"),
                            fieldWithPath("content").description("Post 내용")
                    )
            ));
}

@Test
void remove() throws Exception {
    this.mockMvc.perform(delete("/post/{postId}", 1L))
            .andExpect(status().isNoContent())
            .andDo(document("post-delete",
                    pathParameters(
                            parameterWithName("postId").description("Post Id")
                    )
            ));
}
```





## generated-snippets - 만들어 지는 문서

build를 하면 build/benerated-snippets 디렉토리 아래에 우리가 지정한 위치에 문서가 생성된다.

<img src="https://blog.kakaocdn.net/dn/0Vm7c/btrTuByA51q/KkK0WASWjSVfN99ugM6zN1/img.png" width =600 height = 600> 



**1. curl-request.adoc, http-request.adoc** : 우리가 요청을 보내는 방식에 대한 문서화된 파일이다.

**2. http-response** : 우리가 받은 response를 문서화 시켜준 부분이다.

**3. path-parameter.adoc** : 리소스가 지원하는 path 파라미터를 설명하는 테이블이 존재한다.

**3. request-body, fields, response-body, fields** : 이들은 request의 body와 각 필드에 대한 설명(코드 내에서 만들어줬던 부분)이 만들어져있는 곳이다. 



`src/docs/asciidoc`와 같이 디렉토리를 만들고 `*.adoc`파일을 작성해야한다. ([Asciidoctor User Manual](https://asciidoctor.org/docs/user-manual/#introduction-to-asciidoctor) 참고)

> Asciidoctor는 일반 텍스트를 처리하고 필요에 맞게 스타일 및 레이아웃 된 HTML을 생성한다..





> ## REST Docs index.html 작성
>
> maven인 경우 `src/main/asciidoc`, gradle이라면 `src/docs/asciidoc` 안에 `index.adoc` 파일을 작성한다는데, 설정에 따라 달라진다.  



 HTML 이 생성되는 위치는 다음과 같다.

<img src="https://blog.kakaocdn.net/dn/cI0dd8/btrToa3GtgQ/cGPnrkpAbKaKdSithUQoC0/img.png" width =500 height = 800>

> * 만약 src/main/resources/static/docs에 html이 없다면,
> * `build 디렉토리에 밑에 static/docs에 복사되는것이다`.  
> * 그래도 없다면 maven이나 gradle 설정이 잘 되었는지 확인해보자. 





이제  `서버:포트번호/docs/index.html`에 접속하면 문서를 볼 수 있다.

* ex ) localhost:8080/docs/index.html











### 참조

https://velog.io/@max9106/Spring-Spring-rest-docs%EB%A5%BC-%EC%9D%B4%EC%9A%A9%ED%95%9C-%EB%AC%B8%EC%84%9C%ED%99%94