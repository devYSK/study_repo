# RestDocs Custom

원하는 스니펫이 없을 경우 아예 스니펫을 새로 만들 수도 있다. [Generating Custom Templated Snippets with Spring REST Docs](https://medium.com/@rfrankel_8960/generating-custom-templated-snippets-with-spring-rest-docs-d136534a6f29)에 잘 정리돼있다.

* RestDocs 공식문서 - https://docs.spring.io/spring-restdocs/docs/2.0.5.RELEASE/reference/html5/



## 예제 - 원하는데로 커스텀



http-request.adoc, http-response.adoc, request-body.adoc, request.fields.adoc, response-body.adoc, response-fields.adoc 등 마음대로 스니펫을 커스텀할 수 있다.



default 형식은  **spring-restdocs-core /snippet/templates** 밑에 정의되어 있으며, 우리가 기본적으로 사용하는 default snippet들이 들어있으므로 보고 형식을 따올 수 있다. 



* File 중에 default- 필요한 부분.snippent을 검색(커맨드 + 쉬프트 + f)을 하여 참고하면 대략적인 형식을 알 수 있다 .
  * ex) request-parameter 커스텀시 `default-request-parameters.snippent`  검색
  * ex) response-fields를 커스텀시  `default-response-fields.snippent` 검색

<img src="https://blog.kakaocdn.net/dn/bxrE53/btrXv4KsHKo/d3g4SGspO0lEyY6XwRubek/img.png" width = 500 height = 800> 



## 1. src/test/resources/org/springframework/restdocs/templates 경로에 원하는 형식을 추가



이 예제에서는 response-fields.snippet을 커스텀 한다.



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

<img src="https://blog.kakaocdn.net/dn/cjENL1/btrXonR1L5o/Vct7z2INPpfUg4rarhaWk0/img.png" width = 800 height = 700>

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
   static RestDocumentationResultHandler getMethodDocument(
     String identifier, List<ParameterDescriptor> queryParameters,
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

<img src="https://blog.kakaocdn.net/dn/bxUuhY/btrXonxJBPA/JrHtJ4inYiibUJ7Ca0V3E0/img.png" width = 600 height =200>



# 기본 템플릿들

1. 

```
===== Request Fields 
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









