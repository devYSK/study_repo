

RestDocs 공식문서 - https://docs.spring.io/spring-restdocs/docs/2.0.5.RELEASE/reference/html5/

# IntelliJ Restdocs Unexpected token -  .snippet 파일을 AsciiDoc로 인식하지 않을 때 해결 방법 



Restdocs 커스텀을 위하여 src/test/resources/org/springframework/restdocs/templates 경로에 snippet 파일을 추가하여 작성하면 다음처럼 인식이 되지 않아서 작성하기 힘든 경우가 생긴다.

> Unexpected token 

빨간줄에, 문법 형식도 맞지 않게 작성된다. 

<img src="https://blog.kakaocdn.net/dn/Gv7mL/btrXnKfszgD/KHQFx4awgJON9T2wl0e4YK/img.png" width = 600 height = 350>



Intellij 설정을 바꾸면 해결된다.

## 해결법

> Mac 기준
>
> Preferences -> Editor -> File Types -> Recognized File Types 
>
> 
>
> Recognized File Types 에서 마우스 조금 내리다보면 AsciiDoc files보인다.
>
> File name patterns에 *.snippet 추가

<img src="https://blog.kakaocdn.net/dn/cSAqzk/btrXo5pH7M9/p0a9Txdw4ZKzfqmLGpV0A0/img.png" width = 800 height=550>

writing AsciiDoc works best with soft-wrap enabled. Do you want to enable it by default? 라는 문구 발견

* AsciiDoc 작성은 소프트 랩이 활성화된 상태에서 가장 잘 작동합니다. 기본적으로 활성화하시겠습니까?

활성화 창 클릭 

<img src="https://blog.kakaocdn.net/dn/stDux/btrXn9e3qyZ/FKZmvPjEivXk2PWBr1MAK1/img.png" width = 600 height=350>



yes, take me to the Soft Wrap settings! 클릭 

<img src="https://blog.kakaocdn.net/dn/E7xJQ/btrXpcbiDK3/3igMBTspAjkdiiyku2kF2k/img.png" width = 700 height = 300>

soft-wrap these fiels -> *.snippet; 추가 

<img src="https://blog.kakaocdn.net/dn/nkBD3/btrXmSrPEzm/CKWN04Mck0j4mC88nMkWP0/img.png" width= 800 height = 550>



문제없이 asciidoc 작성을 할 수 있다. 







# RestDocs Custom

원하는 스니펫이 없을 경우 아예 스니펫을 새로 만들 수도 있다. [Generating Custom Templated Snippets with Spring REST Docs](https://medium.com/@rfrankel_8960/generating-custom-templated-snippets-with-spring-rest-docs-d136534a6f29)에 잘 정리돼있다.



## 예제 - 내 원하는데로 커스텀



http-request.adoc, http-response.adoc, request-body.adoc, request.fields.adoc, response-body.adoc, response-fields.adoc 등 내 마음대로 스니펫을 커스텀할 수 있다.



default 형식은 

spring-restdocs-core / snippet/templates 밑에 정의되어 있으며, 우리가 기본적으로 사용하는 default snippet들이 들어있으므로 보고 형식을 따올 수 있다. 



* File 중에 default- 필요한 부분.snippent을 검색(커맨드 + 쉬프트 + f)을 하여 참고하면 대략적인 형식을 알 수 있음.
  * ex) request-parameter 커스텀시 `default-request-parameters.snippent`  검색
  * ex) response-fields를 커스텀시  `default-response-fields.snippent` 검색

![image-20230128235824125](/Users/ysk/study/study_repo/spring/restdocs/images//image-20230128235824125.png) 



## 1. src/test/resources/org/springframework/restdocs/templates 경로에 원하는 형식을 추가



이 예제에서는 response-fields.snippet을 커스텀 해보기로 하였다.



커스텀 시에는 .attributes(key("키 값").value("value 값"), key("키 값").value("value 값"))

으로 계속 문서에 추가할 수 있으며, .snippet 파일에 해당 필드를 참조하여 매핑될 수 있도록 작성해야 한다. 

> `.snippet`파일이 템플릿이고 여기에 있는 템플릿을 오버라이딩 하는것.
>
> 오버라이딩하고 싶은 템플릿은 `src/test/resources/org/springframework/restdocs/templates/asciidoctor`
>
> 에 넣어주면 된다. 
>
> 예를 들어, `curl-request`를 새롭게 정의하고 싶으면 `src/test/resources/org/springframework/restdocs/templates/asciidoctor.curl-request.snippet`
>
> 을 만들어주면 된다.

**Controller Test Code - MockMvc** 

```java
this.mockMvc.perform(get("/api/v1/posts/{postId}", post.getId())
                .contentType(MediaType.APPLICATION_JSON))
            // 생략
  // 여기서부터 보면 된다. 
            .andDo(document("posts-findById",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("postId").description("Post Id")
                ),
                // 이 responseFields를 커스텀한다. 
                responseFields(
                    fieldWithPath("postId").type(JsonFieldType.NUMBER).description("게시글 Id")
                        .attributes(
                            key("custom attribute").value("콘스트레인츠~~"),
                            key("Byte").value("바이트~~"),
                            key("add").value("얘는 무조건 돌려줌")
                        )
                    ,
                    fieldWithPath("title").type(JsonFieldType.STRING).description("게시글 제목")
                        .attributes(key("custom attribute").value("콘스트레인츠~~"),
                            key("Byte").value("바이트~~"),
                            key("add").value("얘도 무조건 돌려줌"))
                    ,
                    fieldWithPath("content").type(JsonFieldType.STRING).description("게시글 내용")
                        .attributes(key("custom attribute").value("콘스트레인츠~~"),
                            key("Byte").value("바이트~~"),
                            key("add").value("얘도 무조건 돌려줌"))
                    ,
                    fieldWithPath("userId").type(JsonFieldType.NUMBER).description("유저 Id")
                        .attributes(key("custom attribute").value("콘스트레인츠~~"),
                            key("Byte").value("바이트~~"),
                            key("add").value("얘도 작성자니까 무조건 돌려줌")).optional()
                    ,
                    fieldWithPath("createdAt").type(JsonFieldType.STRING).description("게시글 생성시간")
                        .attributes(key("custom attribute").value("콘스트레인츠~~"),
                            key("Byte").value("바이트~~"),
                            key("add").value("언제 생성 되었을까"))
                    ,
                    fieldWithPath("createdBy").description("게시글 작성자")
                        .attributes(key("custom attribute").value("콘스트레인츠~~"),
                            key("Byte").value("바이트~~"),
                            key("add").value("누가 쓴건지 알고싶나?")
                        )
                    )
            ))
```



뭔가 엄청 복잡해보이지만 천천히 해석해보면 된다.

**Restdocs - 문서 파일명 **

![image-20230129000511400](/Users/ysk/study/study_repo/spring/restdocs/images//image-20230129000511400.png)

#### [Response Fields](#_response_fields)

| 필드명      | 타입     | 설명            | 필수값 | Byte     | 커스텀 attribute | 추가 설명                     | 필요한가?                        |
| :---------- | :------- | :-------------- | :----- | :------- | :--------------- | :---------------------------- | :------------------------------- |
| `postId`    | `Number` | 게시글 Id       | false  | 바이트~~ | 콘스트레인츠~~   | 얘는 무조건 돌려줌            | 이 속성 추가 안하면 이 글이 나옴 |
| `title`     | `String` | 게시글 제목     | false  | 바이트~~ | 콘스트레인츠~~   | 얘도 무조건 돌려줌            | 이 속성 추가 안하면 이 글이 나옴 |
| `content`   | `String` | 게시글 내용     | false  | 바이트~~ | 콘스트레인츠~~   | 얘도 무조건 돌려줌            | 이 속성 추가 안하면 이 글이 나옴 |
| `userId`    | `Number` | 유저 Id         | true   | 바이트~~ | 콘스트레인츠~~   | 얘도 작성자니까 무조건 돌려줌 | 이 속성 추가 안하면 이 글이 나옴 |
| `createdAt` | `String` | 게시글 생성시간 | false  | 바이트~~ | 콘스트레인츠~~   | 언제 생성 되었을까            | 이 속성 추가 안하면 이 글이 나옴 |
| `createdBy` | `Null`   | 게시글 작성자   | false  | 바이트~~ | 콘스트레인츠~~   | 누가 쓴건지 알고싶나?         | 이 속성 추가 안하면 이 글이 나옴 |



**custom snippet 파일**

```
==== Response Fields

|===
|필드명|타입|설명|필수값|Byte|커스텀 attribute|추가 설명|필요한가?

{{#fields}}

|{{#tableCellContent}}`+{{path}}+`{{/tableCellContent}}
|{{#tableCellContent}}`+{{type}}+`{{/tableCellContent}}
|{{#tableCellContent}}{{description}}{{/tableCellContent}}
|{{#tableCellContent}}{{#optional}}true{{/optional}}{{^optional}}false{{/optional}}{{/tableCellContent}}

|{{#tableCellContent}}{{Byte}}{{/tableCellContent}}
|{{#tableCellContent}}{{custom attribute}}{{/tableCellContent}}
|{{#tableCellContent}}{{add}}{{/tableCellContent}}
|{{#tableCellContent}}{{^need}}이 속성 추가 안하면 이 글이 나옴{{/need}}{{/tableCellContent}}

{{/fields}}

|===
```



먼저 상단의 |필드명|타입 으로 시작하는 것은 테이블의 제목명을 커스텀하는것이다. - html의 `th` 라고 보면 된다

* 들어갈 필드의 수와 다르면 순서가 깨지므로 잘 맞춰야 한다.



{{#fields}} 로 부터 필드 정의가 시작된다.

* 테이블 헤더 (th)와 수가 다르면 순서가 깨진다



* path : responseFields(**fieldWithPath("postId")**) 의 값이다. " "안에 들어간 값이다

* type : fieldWithPath("postId").**type(JsonFieldType.NUMBER)))** 메서드 의 type 값이다. JSON 필드 타입을 의미한다
* description : 기본 정의된 값인 **description("게시글 제목")** 메서드의 값이다.
* optional : 필수값 여부를 나타내는 필드이며 optional() 메서드의 값이다. 

> ```
> {{#...}} {{...}} {{/...}}`
> `{{^...}} "empty or null" {{/...}}
> ```

위는 `{{ }}` 안의 value 가 있다면 해당 값이 출력되고,
empty,null 인 경우에는 "empty or null"이 출력되는 예시다.

```null
{{#optional}}true{{/optional}}{{^optional}}false{{/optional}}
```

따라서 이 경우에는 `optional` 값이 있다면 해당 값을 출력하고, 없다면 "false" 이 출력되도록 했다.



이런식으로 응용도 가능하다

```null
<td>
{{#sms_agree}}{{sms_agree}}{{/sms_agree}}
{{^sms_agree}}N{{/sms_agree}}
</td>
// `sms_agree` 값이 있다면 해당 값을 출력 없다면 "N" 이 출력
```

* Byte :  .attributes(key("Byte").value("바이트") 의 값이다. Byte라는 key값으로 바이트 라는 value를 매핑한 값이다.
  * {{custom attribute}}, {{add}}도 마찬가지이다. 매핑 안하면 테스트시 오류가 난다. 
* need : .attributes(key("need").value("값") 이 attribute가 추가되어 있지 않다면 해당 내용이 출력된다. 

```
{{^need}}이 속성 추가 안하면 이 글이 나옴{{/need}}
```



## request fields 커스텀 예제



```java
 @Test
 public void getUsersTest() throws Exception{
     
     ...
     
     testActions
             .andDo(getMethodDocument("get-users",
                     List.of(queryParam("page","page 번호"),
                             queryParam("size","page 크기")),
                     List.of(responseField("data", JsonFieldType.ARRAY, "결과 데이터"),
                             responseField("data[].userId", JsonFieldType.NUMBER, "회원 식별자"),
                             responseField("data[].name",JsonFieldType.STRING, "회원 이름"),
                             responseField("data[].companyLocation", JsonFieldType.STRING, "회사 위치"),
                             responseField("data[].companyName", JsonFieldType.STRING, "회사 이름"),
                             responseField("data[].companyType", JsonFieldType.STRING, "회사 타입"),
                             responseField("pageInfo.page", JsonFieldType.NUMBER, "현재 페이지"),
                             responseField("pageInfo.size", JsonFieldType.NUMBER, "현재 페이지 크기"),
                             responseField("pageInfo.totalElements", JsonFieldType.NUMBER, "총 User 수"),
                             responseField("pageInfo.totalPages", JsonFieldType.NUMBER, "총 페이지 수"))));
 }
public interface Document {
   static RestDocumentationResultHandler getMethodDocument(String identifier,
                                                           List<ParameterDescriptor> queryParameters,
                                                           List<FieldDescriptor> responseFields){
       return document(identifier,
               ApiDocumentUtils.getRequestPreProcessor(),
               ApiDocumentUtils.getResponsePreProcessor(),
               requestParameters(queryParameters),
               responseFields(responseFields));
   }

   static ParameterDescriptor queryParam(String name, String description){
       return parameterWithName(name).description(description);
   }

   static FieldDescriptor responseField(String jsonPath, JsonFieldType type, String description){
       return fieldWithPath(jsonPath).type(type).description(description);
   }
}
```

- Custom 이전의 get-users > request-parameters.adoc

  | Parameter | Description |
  | --------- | ----------- |
  | page      | page 번호   |
  | size      | page 크기   |

- src/test/resources/org/springframework/restdocs/templates
  경로에 request-parameters.snippet 추가

  - 형식은 Asciidoc 를 따름
  - File 중에 default-request-parameters.snippent을 검색(Ctrl + N)을 하여 참고하면 대략적인 형식을 알 수 있음.

  ```adoc
   // request-parameters.snippet
    
   |===
   |Parameter|Description|필수값
  
   {{#parameters}}
   |{{#tableCellContent}}`+{{name}}+`{{/tableCellContent}}
   |{{#tableCellContent}}{{description}}{{/tableCellContent}}
   |{{#tableCellContent}}{{^optional}}true{{/optional}}{{/tableCellContent}}
  
   {{/parameters}}
   |===
  ```

- Custom 이후 get-users > request-parameters.adoc

  - 테스트를 다시 실행 후에 적용됨

![image-20230129001714643](/Users/ysk/study/study_repo/spring/restdocs/images//image-20230129001714643.png)



# pretty print - 이쁘게 출력하기

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





# Operation - 불필요한 include 제거하기

adoc파일에서 다른 adoc파일을 삽입하려면 `include` 문법을 사용해야 한다.

```adoc
// index.adoc
= Rest Docs Example

== Simple Service

=== curl request

include::{snippets}/simple-read/curl-request.adoc[]

=== Path parameters

include::{snippets}/simple-read/path-parameters.adoc[]

=== Request parameters

include::{snippets}/simple-read/request-parameters.adoc[]

=== Response Fields

include::{snippets}/simple-read/response-fields.adoc[]

=== HTTP request

include::{snippets}/simple-read/http-request.adoc[]

=== HTTP response

include::{snippets}/simple-read/http-response.adoc[]
```

많이 불편하다. 테스트 메소드 하나당 기본적으로 6개, 이것 저것 하다보면 10개에 가까운 adoc 파일이 생긴다. 테스트가 한 두개도 아니고 매 번 복붙하는데도 한계가 있다.

`operation`을 사용하면 include 여러개 하는 고통을 없애준다.

```adoc
= Rest Docs Example

== Simple Service

operation::simple-read[]
```

원하는 것만 골라서 가져올 수도 있다.

```adoc
snippets='curl-request,path-parameters,request-parameters,response-fields,http-request,http-response'
```

테이블만 불러오는게 아니라, 헤더도 단계에 맞게 자동으로 생성해준다.





# 배열 표현

배열은 `a.b[].c`와 같이 표현할 수 있다.

> 이외에도 여러가지 표현식이 있는데, [JSON Field Paths](https://docs.spring.io/spring-restdocs/docs/current/reference/html5/#documenting-your-api-request-response-payloads-fields-json-field-paths)를 참고하자.

일반적인 경우에는 문제가 없겠지만, 배열을 동적으로 생성하거나 해야 하는 경우에 표현이 애매할 수 있다. 이럴 때 `a.*[].c`와 같이 와일드 카드로 처리해줄 수 있다.



# 불필요한 헤더 제거

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



# 기본 템플릿들

1. 

```
===== Request Fields // (1)
|===
|필드명|타입|필수값|설명

{{#fields}}
|{{#tableCellContent}}`+{{path}}+`{{/tableCellContent}}
|{{#tableCellContent}}`+{{type}}+`{{/tableCellContent}}
|{{#tableCellContent}}{{^optional}}true{{/optional}}{{/tableCellContent}}  //(2)
|{{#tableCellContent}}{{description}}{{/tableCellContent}}

{{/fields}}

|===
```



2. 

```
|===
|Path|Type|Description|Optional|Custom attribute

{{#fields}}
|{{#tableCellContent}}`+{{path}}+`{{/tableCellContent}}
|{{#tableCellContent}}`+{{type}}+`{{/tableCellContent}}
|{{#tableCellContent}}{{description}}{{/tableCellContent}}
|{{#tableCellContent}}_{{optional}}_{{/tableCellContent}}
|{{#tableCellContent}}{{custom attribute}}{{/tableCellContent}}

{{/fields}}
|===
```





# RestDocs 작성 가이드 #2 응용편 - 보일러플레이트 코드 제거

> RestDocs 는 여러 컨트롤러에서 만들어지는 중복되는 필드 정의에 의해 생산성이 저하될 수 있다.

### AS-IS



![img](https://blog.kakaocdn.net/dn/bGLj4d/btre42iLU7T/wayEXIKECDKTJv0FSmnVmK/img.png)



### TO-BE



![img](https://blog.kakaocdn.net/dn/coWQpx/btreY2jPwlV/LSbeGf4PZgCNcWXBhMkK51/img.png)



AS-IS 에서 표시된 영역에서 반복이 발생되어 모듈로 만들어 재사용할 수 있도록 TO-BE 와 같이 구현할 것이다.

그래서 보일러플레이트 코드를 제거하고 한번 정의된 필드를 재사용할 수 있는 방법을 가이드하였다.



### 클래스 설명

- Descriptor : RestDocs 사용에 맞춰 필드를 정의하는 인터페이스이다.
- RestDocsDescriptor : 정의된 필드를 제어한다.
- AutoConfigureMvcRestDocs : 현 API 초기 설정을 정의한 RestDocs 설정한다.



### 필드 정의 예시

**[코드 1-1]** enum 에서 필드 정의

```java
@Getter
public enum ChangePasswordField implements Descriptor {
    currentPassword("현재 비밀번호", false),
    newPassword("새로운 비밀번호", false),
    newPasswordConfirm("새로운 비밀번호 확인", false);

    private final String description;
    private final boolean optional;

    ChangePasswordField(String description, boolean optional) {
        this.description = description;
        this.optional = optional;
    }
}
```

테스트 작성시 아래와 같이 선언해주어야 한다.

```java
private final RestDocsDescriptor changePasswordFieldHandler = new RestDocsDescriptor(ChangePasswordField.values());
```

위와 같이 하면 필드 정의는 끝났다.

### 테스트 작성

항목 정의 기능

- RestDocsDescriptor.of(Descriptor... field) : 정의할 필드를 입력한다. 입력하지 않으면 전체를 필드를 정의한다.

최종 완료 기능

- RestDocsDescriptor.exclude(Descriptor.... field) : payload 에 제외한 필드를 정의한다.
- RestDocsDescriptor.stream() : java stream 을 반환한다. Stream
- RestDocsDescriptor.collect(DescriptorCollectors::xxxDescriptor) : RestDocs 에서 사용할 수 있도록 리스트로 반환한다.
  - DescriptorCollectors::headerDescriptor
  - DescriptorCollectors::linkDescriptor
  - DescriptorCollectors::fieldDescriptor
  - DescriptorCollectors::requestPartDescriptor
  - DescriptorCollectors::parameterDescriptor

예시 코드

```java
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureMvcRestDocs
class AuthenticatedAccountRestControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    private final RestDocsDescriptor changePasswordFieldHandler = new RestDocsDescriptor(ChangePasswordField.values());

    private String pathPrefix;
    private String restdocsPath;

    @BeforeEach
    void init() {
        pathPrefix = "/v1/me";
        restdocsPath = "accounts/v1/me/{method-name}";
    }

    @Test
    void changePassword() throws Exception {

        AccountRequestDto.ChangePassword changePassword = AccountRequestDto.ChangePassword.builder()
            .currentPassword("1234")
            .newPassword("aaaa")
            .newPasswordConfirm("aaaa")
            .build();

        mvc.perform(patch(pathPrefix + "/password")
            .content(mapper.writeValueAsString(changePassword))
            .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())

            .andDo(document(restdocsPath,
                requestHeaders(
                    headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON)
                ),

                requestFields(
                    changePasswordFieldHandler.of(ChangePasswordField.currentPassword, ChangePasswordField.newPassword, ChangePasswordField.newPasswordConfirm)
                        .collect(DescriptorCollectors::fieldDescriptor)
                )
            ));
    }
}
```

## 그외 사용 예시 참고

- https://github.com/syakuis/spring-restdocs/blob/master/src/test/java/io/github/syakuis/restdocs/DescriptorCollectorTest.java
- https://github.com/syakuis/spring-restdocs/blob/master/src/test/java/io/github/syakuis/restdocs/RestDocsDescriptorTest.java

## 추가적인 이슈

필드가 추가되거나 변경될 경우 모든 페이지에 수정작업이 발생된다.

하여 필드를 정의하는 enum 구현에서 정적 메서드로 명시적인 필드를 제공하는 것이 효율적일 수 있다.

코드 1-1 을 참고하여 아래와 같이 작성할 수 있다.

```java
@Getter
public enum AccountField implements FieldSpec {
    ... skip ...

    public static String[] request() {
        return new String[]{
            AccountField.username,
            AccountField.password,
            AccountField.name,
            AccountField.disabled,
            AccountField.blocked
        };
    }

    public static String[] profile() {
        return new String[]{
            AccountField.uid,
            AccountField.username,
            AccountField.name,
            AccountField.registeredOn,
            AccountField.updatedOn
        };
    }
}
```



# 공통 코드 문서화

이제 마지막으로 HTTP Error Response, HTTP status codes, Host 환경 같은 내용을 문서화해보겠습니다.
에러 관련 문서화 내용이 필요하므로 테스트 패키지에서 에러를 발생시키는 컨트롤러를 만들어주고 테스트를 작성합니다.

**CommonDocController.java**

```java
@RestController
@RequestMapping("/test")
public class CommonDocController {

    @PostMapping("/error")
    public void errorSample(@RequestBody @Valid SampleRequest dto) {
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SampleRequest {

        @NotEmpty
        private String name;

        @Email
        private String email;
    }

    ....

}
```

기존에 있던 CommonDocController 위 코드를 추가합니다.



**CommonDocControllerTest.java**

```java
class CommonDocControllerTest extends RestDocsTestSupport {

    @Test
    public void errorSample() throws Exception {
        CommonDocController.SampleRequest sampleRequest = new CommonDocController.SampleRequest("name","hhh.naver");
        mockMvc.perform(
                post("/test/error")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleRequest))
        )
                .andExpect(status().isBadRequest())
                .andDo(
                        restDocs.document(
                                responseFields(
                                        fieldWithPath("message").description("에러 메시지"),
                                        fieldWithPath("code").description("Error Code"),
                                        fieldWithPath("errors").description("Error 값 배열 값"),
                                        fieldWithPath("errors[0].field").description("문제 있는 필드"),
                                        fieldWithPath("errors[0].value").description("문제가 있는 값"),
                                        fieldWithPath("errors[0].reason").description("문재가 있는 이유")
                                )
                        )
                )
        ;
    }

    ...
}
```

기존 CommonDocControllerTest에서 위 코드를 추가해줍니다. 잘못된 email 형식을 넣어 에러가 터지게 합니다.
다시 한번 빌드해줍니다.



이제 빌드로 생성된 조각을 이용해서 문서를 작성하겠습니다.
src/docs/asciidocs 위치에 overview.adoc 를 만들어줍니다.
**overview.adoc**

```js
[[overview]]
== Overview

[[overview-host]]
=== Host

|===
| 환경 | Host

| Beta
| `beta-backtony.github.io`

| Production
| `backtony.github.io`
|===

[[overview-http-status-codes]]
=== HTTP status codes

|===
| 상태 코드 | 설명

| `200 OK`
| 성공

| `400 Bad Request`
| 잘못된 요청

| `401 Unauthorized`
| 비인증 상태

| `403 Forbidden`
| 권한 거부

| `404 Not Found`
| 존재하지 않는 요청 리소스

| `500 Internal Server Error`
| 서버 에러
|===

[[overview-error-response]]
=== HTTP Error Response
operation::common-doc-controller-test/error-sample[snippets='http-response,response-fields']
```



**index.adoc**

```js
= REST Docs 문서 만들기 (글의 제목)
backtony.github.io(부제)
:doctype: book
:icons: font
:source-highlighter: highlightjs
:toc: left
:toclevels: 2
:sectlinks:
:docinfo: shared-head

include::overview.adoc[]

include::Member-API.adoc[]
```

기본 index.adoc에 overview.adoc을 추가해줍니다.

* 최 상단에 추가하여 최 상단에 뜨게된다.

##  링크로 문서화

------

현재 한 페이지에서 모든 API를 보여주고 있습니다. 왼쪽 Table of Content로 바로가기를 할 수는 있지만, 가독성이 너무 떨어지는 것 같은 느낌이 들었습니다.
각 API마다 링크를 걸어주고 새로운 페이지에서 확인할 수 있도록 하여 가독성을 높여보겠습니다.



### build.gradle

```null
sources{
        include("**/*.adoc","**/common/*.adoc")
    }
```

앞서 작성했던 build.gradle의 source 부분을 수정해서 모두 html로 문서화 시켜주도록 합니다.



### API별 문서화

![img](https://github.com/backtony/blog-code/blob/master/spring/img/test/3/3-20.PNG?raw=true)

각각의 API별로 .adoc 파일로 문서화를 진행합니다.



![img](https://github.com/backtony/blog-code/blob/master/spring/img/test/3/3-21.PNG?raw=true)

build를 진행하면 이제 build/docs 폴더 안에 adoc 파일마다 각각의 HTML 파일이 생성됩니다.



### index.adoc

![img](https://github.com/backtony/blog-code/blob/master/spring/img/test/3/3-22.PNG?raw=true)
index 파일에서는 이제 * link 를 사용하여 각 html 파일명을 적어줍니다. window blank는 새로운 창을 의미합니다.



### 최종 결과

![img](https://github.com/backtony/blog-code/blob/master/spring/img/test/3/3-23.PNG?raw=true)

이제 문서를 확인해보면 각각의 API별로 링크가 들어가 있고 클릭 시 새로운 창이 띄워지게 됩니다.
이렇게 최종적으로 문서를 완성했습니다.





# Spring REST Docs에 DTO의 Validation 정보 담기

Spring을 이용해서 API의 제약사항을 표현할때 Bean Validation을 많이 사용한다. 필드에 표현은 쉽게 되는데, 이걸 REST Docs에 표현하자니 description에 적어줘야 할지... 애매해진다.

Validation 정보를 담기 위한 커스텀 템플릿과 `ConstraintDescriptions`을 이용해서 Bean Validation 정보를 가져오는 방법에 대해 알아보자.

> REST Docs에 대한 사용법은 시리즈의 이전 포스팅을 참고해주세요!

## 커스텀 템플릿 만들기

일단은 스니펫 템플릿을 개조해본다.

```adoc
// request-fields.snippet
|===
|Path|Type|Description|Constraints

{{#fields}}
|{{#tableCellContent}}`+{{path}}+`{{/tableCellContent}}
|{{#tableCellContent}}`+{{type}}+`{{/tableCellContent}}
|{{#tableCellContent}}{{description}}{{/tableCellContent}}
|{{#tableCellContent}}{{constraints}}{{/tableCellContent}}

{{/fields}}
|===
requestFields(
    fieldWithPath("name")
      .type(JsonFieldType.STRING)
      .description("이름")
      .attributes(key("constraints").value("not null"))
);
```

![img](https://velog.velcdn.com/images%2Fdae-hwa%2Fpost%2Fede0658b-54a2-4fef-a73f-3f139763d520%2Fimage-20211209175140617.png)

하지만 이걸로는 안 된다. not null 이라고 직접 입력한 것 뿐이다.

![img](https://velog.velcdn.com/images%2Fdae-hwa%2Fpost%2F9edaa197-5f41-4c5a-a1fb-7a7a423be2f3%2Fimage-20211209125633404.png)

필드 개수가 얼마 안 되면 어떻게 해보겠지만, 이렇게 필드가 많아지면 너무 힘들어진다. 자동화 시켜보자.

## Validation 정보 불러오기

### ConstraintDescriptions

`spring-restdocs-core`에 `ConstraintDescriptions`가 들어있다. 특정 클래스에 제약사항이 있는지 확인해준다.

![img](https://velog.velcdn.com/images%2Fdae-hwa%2Fpost%2Fe134f0cb-a062-4762-8eab-843b873e6e19%2Fimage-20211209181841196.png)

```java
ConstraintDescriptions simpleRequestConstraints = new ConstraintDescriptions(SimpleRequest.class);
List<String> nameDescription = simpleRequestConstraints.descriptionsForProperty("name");
```

이렇게 하면 `SimpleRequest`의 필드에 붙은 제약사항을 불러온다. 이대로 넣어주기만 하면 된다.

```java
requestFields(
    fieldWithPath("name")
      .type(JsonFieldType.STRING)
      .description("이름")
      .attributes(key("constraint").value(nameDescription))
)
```

다만, attributes에 constraint가 없을 경우 `MustacheException$Context 예외가 발생할 수 있다. 스니펫 템플릿을 아래처럼 변경해주면 된다.

```adoc
// request-fields.snippet
|===
|Path|Type|Description|Constraints

{{#fields}}
|{{#tableCellContent}}`+{{path}}+`{{/tableCellContent}}
|{{#tableCellContent}}`+{{type}}+`{{/tableCellContent}}
|{{#tableCellContent}}{{description}}{{/tableCellContent}}
|{{#tableCellContent}}{{#constraints}}{{.}} +
{{/constraints}}{{/tableCellContent}}

{{/fields}}
|===
```

`{{.}}`은 필드가 존재할때만 출력을 해주는 문법이다. `+`는 강제 개행인데, 한 줄씩 띄어준다. 쉼표같이 선호하는 표현 방식이 있으면 바꿔서 쓰면 된다.

### 내부 동작

`org.springframework.restdocs.constraints` 패키지 안은 아래와 같이 구성된다.

![img](https://velog.velcdn.com/images%2Fdae-hwa%2Fpost%2F59f6461e-f2ac-457e-a824-f5f24e646890%2Fimage-20211210162316640.png)

`ConstraintDescriptions.descriptionsForProperty`에 프로퍼티 명을 매개변수로 넣으면 `ConstraintResolver`는 해당 프로퍼티에 맞는 `Constraint`를 가져온다. 기본 구현인 `ValidatorConstraintResolver`는 내부적으로 Bean Validation의 `Validator` 객체를 사용한다.

Bean Validation을 이용해서 해당 클래스의 필드에 붙은 `@NotNull`과 같은 constraint를 가져온다. Java Bean 규격을 따르기 때문에 필드명과 json field의 이름이 일치하지 않는 경우 주의해야 한다.

> 만약 json field가 user_id일 경우 카멜케이스로 변경해서 넣어줘야 한다. 내부에 있는 `PropertyDescriptor`가 [Java Bean 컨벤션](https://en.wikipedia.org/wiki/JavaBeans#JavaBean_conventions)에 따라 프로퍼티를 찾기 때문이다.

`ConstraintDescriptionResolver`는 `ConstraintResolver`가 찾아온 `Constraint`을 문자열로 변환해준다. 기본적으로 Bean Validator 2.0과 Hibernate Validator 스펙에 맞게 지원한다. 커스텀 제약사항이 있다면 해당 프로퍼티를 객체 생성시에 넣어주면 된다.

> 참고 - [기본 지원 constraints](https://docs.spring.io/spring-restdocs/docs/2.0.5.RELEASE/reference/html5/#documenting-your-api-constraints-describing)

## 마치며

공식문서에 깃허브 링크만 덜렁 보여주면서 따라해라고 돼있어 가이드를 작성해봤다. Attribute를 직접 넣어주는게 단점이지만, 반대로 Bean Validation을 사용하는 DTO라면 어디에든 넣어줄 수 있기 때문에 request parameter에도 똑같이 적용할 수 있다.

`ConstraintDescriptions`도 쓰기 쉽게 잘 만들어 놓아서 Java Bean을 사용해야 한다는 사실만 숙지하면 쉽게 사용할 수 있다. 반복될 가능성이 높기 때문에 래핑해서 쓰는 것도 좋아보인다.

그럼에도 고민 되는 부분은 컬럼 항목이 많아지다보니 표가 복잡해진다는 것인데, `[%autowidth.stretch]`를 표에 붙여 타협하고 있다. 여유가 있으면 스니펫을 새로 만들면 되지만 시간을 꽤 투자해야 할 것 같아 망설여진다.

혹시 좋은 아이디어가 있으면 공유 부탁드립니다 ㅎㅎ







### 테스트 클래스 설정

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(RestDocumentationExtension.class)
class SimpleAcceptanceTest {

    private static final String BASE_URL = "http://localhost";

    @LocalServerPort
    private int port;

    protected RequestSpecification spec;

    @BeforeEach
    void setUpRestDocs(RestDocumentationContextProvider restDocumentation) {
        this.spec = new RequestSpecBuilder()
                .addFilter(documentationConfiguration(restDocumentation))
                .build();
    }
}
```

## 스니펫

말 그대로 문서 조각이다. 이를 이용하여 문서에 사용될 객체의 구조를 정의하고 테스트 과정에서 확인한 뒤 adoc 파일을 만들어준다. 스니펫 객체가 실제 요청, 응답에 사용되는 객체와 다르면 테스트 통과가 되지 않는다. 즉, 엔드포인트에 사용되는 객체의 구조를 검증하는 동시에 테스트 문서를 만드는 것이다.

> [참고](https://docs.spring.io/spring-restdocs/docs/2.0.5.RELEASE/reference/html5/#getting-started-documentation-snippets)

### 스니펫 객체 만들기

아래와 같이 스니펫 객체를 만들 수 있다.

```java
private Snippet simplePathParameterSnippet() {
    return pathParameters(parameterWithName("id").description("아이디"));
}

private Snippet simpleRequestParameterSnippet() {
    return requestParameters(parameterWithName("name").description("이름"));
}

private Snippet simpleResponseFieldsSnippet() {
    return responseFields(
            fieldWithPath("id")
                    .type(JsonFieldType.NUMBER)
                    .description("아이디"),
            fieldWithPath("name")
                    .type(JsonFieldType.STRING)
                    .description("이름")
    );
}
```

### documentation 필터 만들기

이를 이용해 Restassured documentation 필터를 만들어 테스트에 넣어주면, 명시한 디렉토리에 adoc파일을 만들어준다.

```java
@Test
void read() {
    RestDocumentationFilter restDocumentationFilter = document(
            // identifier, 이를 이용해 adoc파일을 저장할 디렉토리를 생성한다 
            "{class_name}/{method_name}/",
            simplePathParameterSnippet(),
            simpleRequestParameterSnippet(),
            simpleResponseFieldsSnippet()
    );

    RequestSpecification given = RestAssured.given(this.spec)
        .baseUri(BASE_URL)
        .port(port)
        .pathParam("id", 1)
        .queryParam("name", "name")
        // 필터를 넣어준다.
        .filter(restDocumentationFilter);

    Response actual = given.when()
                           .get("/simple/{id}");

    actual.then()
          .statusCode(HttpStatus.OK.value())
          .log().all();
}
```

### asciidoctor 실행

`build.gradle`에 만들어둔 `asciidoctor` task를 실행시킨다.

![img](https://velog.velcdn.com/images%2Fdae-hwa%2Fpost%2F52c5a480-7ab1-494f-a7df-b63ca23214e6%2Fimage-20211102172731021.png)

`build.gradle`에 지정해준 `snippetsDir`의 하위 디렉토리에 adoc파일이 생성됐다.

![img](https://velog.velcdn.com/images%2Fdae-hwa%2Fpost%2Ff325c739-da9d-425c-a28a-41df996f38e6%2Fimage-20211102172516322.png)

> [참고 - 기본 생성 스니펫](https://docs.spring.io/spring-restdocs/docs/current/reference/html5/#documenting-your-api-default-snippets)









### 참조

* https://velog.io/@gwichanlee/Spring-Rest-Docs-Custom

* https://dotheright.tistory.com/339
* https://velog.io/@dae-hwa/Spring-REST-Docs-%EC%82%B4%ED%8E%B4%EB%B3%BC%EB%A7%8C%ED%95%9C-%EA%B8%B0%EB%8A%A5%EB%93%A4
* https://velog.io/@backtony/Spring-REST-Docs-%EC%A0%81%EC%9A%A9-%EB%B0%8F-%EC%B5%9C%EC%A0%81%ED%99%94-%ED%95%98%EA%B8%B0