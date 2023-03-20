# RestDocs - Custom Error Code Enum 문서화

* Enum으로 Custom ErrorCode 관리시 Enum 문서 자동화 방법

<img src="https://blog.kakaocdn.net/dn/bDxI3z/btr4YuiU9nJ/4A5LmzolnAIs2kGh1KgsKk/img.png" width = 800 height = 650>



> SpringBoot 버전 2.7.8
>
> restdocs 버전 2.0.7



## 개요

데브코스에서 프로젝트를 하다 고민이 생겼습니다.

프론트엔드와의 협업간에 API 문서로 Spring RestDocs를 사용하고 있었고, 

API 사용 시 에러 응답에 대해 httpStatus 코드만으로는 클라이언트에 에러에 대해 디테일하게 설명할 수가 없어서 클라이언트가 개발과정 중 생긴 오류들에 대해 정확하게 알 수 있게 우리는 ErrorCode를 Enum으로 정의해서 내려주기로 하였습니다.

```java
public enum ErrorCode {

  ...
  NOT_MATCHED_COMMENT_AUTHOR(HttpStatus.FORBIDDEN, "C2", "해당 요청자가 작성한 코멘트가 아닙니다."),

	ALREADY_EXISTS_NICKNAME(HttpStatus.BAD_REQUEST, "U1", "이미 존재하는 닉네임입니다."),
	RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "C1", "%s 리소스가 존재하지 않습니다."),

  UNAUTHORIZED_USER_NOT_FOUND(HttpStatus.UNAUTHORIZED, "A1", "유저가 존재하지 않습니다."),
	INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "A2", "잘못된 접근입니다. 유효한 토큰이 아닙니다."),
	EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "A4", "액세스 토큰이 만료되었습니다. 리프레시 하거나 다시 로그인 해야 합니다."),
  // ... 생략 
 
  private final HttpStatus status;

  private final String code;

	private final String message;   
}
```

* field에 보이는 code (C1, C2, C3) 등이 우리가 클라이언트와 약속한 커스텀 에러 코드 입니다.

만약 특정 API에 잘못된 요청이 오면 다음과 같이 응답을 정의하였습니다. 

```json
{
	"status": 400,
	"message": "tit1e의 파라미터가 잘못되었습니다. input value : null",
	"code": "C2",
	"timestamp": "2023-03-14 19:58:36",
	"errors": [
    {
			"fieldName": "title",
			"inputValue": "null",
			"reason": "title 입력되지 않았습니다.
    }
  ],
	"path": " /api/books
}
```

하지만 다음과 같은 **문제가** 있었습니다. 

* 서버에서 내려주는 코드는 클라이언트 개발자에게, 클라이언트 개발자는 에러 코드를 보고 사용자에게 좀 더 친숙한 메시지로 변환해서 보여주기로하였다.

* 예외 메시지가 제각각이다.
* errorcode등과 같은 enum은 계속 생겨날 수 있는데 그럴때마다 노션 등과 같은 문서로 공유하고 업데이트 하기 힘들다는 문제가 있다.



그래서 이 문제를 해결하기 위해, 이전에 분석해본 경험을 토대로 RestDocs 문서화의 작동방식과 원리를 이용하여, 

정의한 errorCode를 문서화를 자동화하여 보여주기로 하였습니다.



# RestDocs 문서화 진행 방식

mockMvc를 이용해서 문서화 할 때,  다음과 같이 문서화 할 수 있습니다. 

```java
mockMvc.perform(get("/api/users/me")
				.contentType(MediaType.APPLICATION_JSON)
				.header(ACCESS_TOKEN_HEADER_NAME, MOCK_ACCESS_TOKEN)
			)
			.andExpect(status().isOk())
			.andDo(document(
					...
					responseFields(
						fieldWithPath("userId").type(JsonFieldType.NUMBER).description("user Id"),
						fieldWithPath("name").type(JsonFieldType.STRING).optional().description("유저 이름. 실명"),
						fieldWithPath("nickname").type(JsonFieldType.STRING).optional().description("유저 닉네임"),
						...
					)

				)
);
```

* RestDocumentationResultHandler.document(String identifier, Snippet... snippets)

  * identfier : 문서화 할 snippet 파일 이름
  * Snippet : 문서화 할 문서 조각. Snippet 인터페이스를 구현하고 TemplatedSnippet 추상 클래스를 상속받은 여러 클래스들
  * RequestFieldSnippet.class : 요청 필드 문서화시 사용. 보통 static method인 **requestFields()** 메소드 사용하여 문서화.
  * ResponseFieldsSnippet.class : 응답 필드 문서화시 사용. 보통 static method인 **responseFields()** 메소드 사용하여 문서화. 

  * <img src="https://blog.kakaocdn.net/dn/pzwJz/btr4WFSgFA4/aKgHPQoNWIRrJ8IcJfWI4K/img.png" with = 400 height = 400>



여기서 다음과 같은 방법을 고려하고 이용하기로 하였습니다.

* 문서화를 위환 테스트 전용 ErrorCode 응답값 반환 : 테스트 시에만 사용되는 ErrorCodeController를 만들어서 ErrorCode Enum을 전부 특정  Json형식으로 반환한다.
* **Custom Snippet 파일**과, **Custom Sinppet 클래스** : ErrorCodeEnum은 HttpStatus, code, message 필드로 구성되어있으며 이런 Snippet은 지원하지 않는다. 때문에 Snippet 문서와 클래스가 따로 필요하다. 
  * [RestDocs Snippet 클래스들과 파일 이름 인식 방법. -  동작 원리](https://0soo.tistory.com/198)



# 문서화 방법

고려해야 할것은 다음 3가지 입니다.

1. Custom .Snippet file
2. 응답값 형식과, 형식에 맞는 Descriptor 구현
3. Custom .Snippet file과 Descriptor들을 지정할 Custom Response Snippet Class

## 1. 테스트 전용 ErrorCode Controller와 응답값 반환

프로덕션 코드에 영향을 받지 않게  테스트시에만 사용되도록 Test 패키지 밑에 Controller를 구현하고 응답값을 정의.

```java
@RestController
public class ErrorCodeController {

	@GetMapping(value = "/error-code", consumes = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, ErrorCodeResponse> findEnums() {

		Map<String, ErrorCodeResponse> map = new HashMap<>();

		for (ErrorCode errorCode : ErrorCode.values()) {
			map.put(errorCode.name(), new ErrorCodeMap(errorCode));
		}

		return map;
	}


	@Getter
	@NoArgsConstructor
	protected static class ErrorCodeResponse {

		private String code;

		private String message;

		private int status;

		public ErrorCodeResponse(ErrorCode errorCode) {
			this.code = errorCode.getCode();
			this.message = errorCode.getMessage();
			this.status = errorCode.getStatus().value();
		}
	}

}
```

*  String, ErrorCodeResponse 형식으로 정의하였다, 이렇게하게되면 응답값은 다음처럼 나오게 됩니다.

```json
{
  "NOT_MATCHED_COMMENT_AUTHOR": {
    "code": "C2",
    "message": "해당 요청자가 작성한 코멘트가 아닙니다.",
    "status": 403
  },
  "ALREADY_CONTAIN_BOOKSHELF_ITEM": {
    "code": "BS1",
    "message": "이미 책장에 포함된 아아템입니다.",
    "status": 400
  },
  "CANNOT_DELETE_MEMBER_EXIST": {
    "code": "BG6",
    "message": "멤버가 존재하는 모임은 삭제할 수 없습니다.",
    "status": 400
  },
  "MISMATCH_LOGOUT_AUTHENTICATION_TOKEN_NOT_FOUND": {
    "code": "A6",
    "message": "액세스 토큰이나, 리프레시 토큰이 존재하지 않아 잘못된 로그아웃 요청입니다.",
    "status": 401
  },
  "EMPTY_REFRESH_TOKEN": {
    "code": "A5",
    "message": "리프레시 토큰이 존재하지 않습니다.",
    "status": 401
  },
  ...
}
```

* 우리가 원하는것은, ErrorCode Enum 값이 아닌, code, message, status의 문서화를 원하므로 field로 출력되도록 합니다. 
  * EMPTY_REFRESH_TOKEN 등은 Enum 값.



## 2. Custom Snippet file 정의

* [0soo 블로그 - RestDocs Custom](https://0soo.tistory.com/201)

http-request.adoc, http-response.adoc, request-body.adoc, request.fields.adoc, response-body.adoc, response-fields.adoc 등 마음대로 스니펫을 커스텀할 수 있습니다.



우리가 문서화할 형식은 response-fields입니다. 그러므로 response-fields.snippet을 커스텀 해야 합니다.

* src/test/resources/org/springframework/restdocs/templates 경로에 원하는 형식을 추가

* snippet prefix는 errorcode로 하였습니다.

```
Type : 'code'

- 에러 코드.
- cmd + f 또는 ctrl + f로 검색하셔서 찾을 수 있습니다.
- Code : 약속된 코드명.

|===
| Code | 설명 및 메시지 | Http 상태 코드 | status 설명

{{#fields}}
|{{#tableCellContent}}{{code}}{{/tableCellContent}}
|{{#tableCellContent}}{{message}}{{/tableCellContent}}
|{{#tableCellContent}}{{statusCode}}{{/tableCellContent}}
|{{#tableCellContent}}{{status}}{{/tableCellContent}}
{{/fields}}
|===
```

* key는 code, message, statusCode, status 가 됩니다. <- 잘 기억해 두셔야 안헷갈립니다.
* 커스텀 시에는 .attributes(key("키 값").value("value 값"), key("키 값").value("value 값"))으로 계속 문서에 추가할 수 있으므로 이것을 이용할 것입니다. 
* 문법을 잘 지켜야 오류가 발생하지 않습니다. 

<img src="https://blog.kakaocdn.net/dn/qlhWC/btr41wtG9po/KL1NssXmkr7vQ7SuaO2b0k/img.png" width = 800 height = 300>



Custom Response Snippet 파일 이름은 errorcode-response-fields.snippet 입니다.

* 반드시 response-fields.snippet 과 같은 네이밍을 지켜주셔야 내부적으로 snippet 파일을 읽고 문서화 시켜줍니다! 
  * AbstractFieldsSnippet 의 protected 생성자를 보면, 이 규칙으로 문서화를 진행합니다. 



## 3. Custom Snippet 파일을 입히기 위한 Snippet 클래스 작성

```java
public class CustomResponseFieldsSnippet extends AbstractFieldsSnippet {
	
  // 주목!
	public CustomResponseFieldsSnippet(String type, List<FieldDescriptor> descriptors,
		boolean ignoreUndocumentedFields) {
		super(type, descriptors, null, ignoreUndocumentedFields);
	}

	@Override
	protected MediaType getContentType(Operation operation) {
		return operation.getResponse().getHeaders().getContentType();
	}

	@Override
	protected byte[] getContent(Operation operation) throws IOException {
		return operation.getResponse().getContent();
	}
}
```

* `type` :  snippet file prefix 입니다.
* `List<fieldDescriptors>` : 우리가 커스텀할 response의 fields를 문서화 하기 위한 Descriptors 입니다. 
  * 앞서 말한  code, message, statusCode, status key 생성자인 descriptor로 받아서 문서화 하기 위함 입니다. 



 ## 4. ErrorCodeController Test 코드 작성

```java
@WebMvcTest(controllers = ErrorCodeController.class)
class ErrorCodeControllerTest extends ControllerSliceTest {

	private static final String ERROR_SNIPPET_FILE = "errorcode-response";

	@Test
	void errorCodes() throws Exception {
		ResultActions result = mockMvc.perform(get("/error-code")
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print());

		result.andDo(document(ERROR_SNIPPET_FILE,
			customResponseFields(ERROR_SNIPPET_FILE,
				fieldDescriptors()
			)
		));
	}

	private List<FieldDescriptor> fieldDescriptors() {
		List<FieldDescriptor> fieldDescriptors = new ArrayList<>();

		for (ErrorCode errorCode : ErrorCode.values()) {
			FieldDescriptor attributes =
				fieldWithPath(errorCode.name()).type(JsonFieldType.OBJECT)
					.attributes(
            key("code").value(errorCode.getCode()),
						key("message").value(errorCode.getMessage()),
            key("statusCode").value(String.valueOf(errorCode.getStatus().value())),
						key("status").value(errorCode.getStatus().getReasonPhrase()));
			fieldDescriptors.add(attributes);
		}

		return fieldDescriptors;
	}

	public static CustomResponseFieldsSnippet customResponseFields(
		String snippetFilePrefix,
		List<FieldDescriptor> fieldDescriptors) {
		return new CustomResponseFieldsSnippet(snippetFilePrefix, fieldDescriptors, true);
	}

}
```



코드를 세군대로 분리해서 설명하겠습니다.

### 1. customResponseFields static method

```java
public static CustomResponseFieldsSnippet customResponseFields(
		String snippetFilePrefix,
		FieldDescriptor... descriptors) {
		return new CustomResponseFieldsSnippet(snippetFilePrefix, Arrays.asList(descriptors), true);
}
```

* snippet 파일의 prefix 이름과 여러 description을 만들어 위에서 정의한 CustomResponseFieldsSnippet 클래스를 반환합니다.
  * 일반적으로 사용하는 responseFields()를 커스텀해서 사용하기 위함입니다.



### 2. fieldDescriptors()

```java
private List<FieldDescriptor> fieldDescriptors() {
		List<FieldDescriptor> fieldDescriptors = new ArrayList<>();

		for (ErrorCode errorCode : ErrorCode.values()) {
			FieldDescriptor attributes =
				fieldWithPath(errorCode.name()).type(JsonFieldType.OBJECT)
					.attributes(
            key("code").value(errorCode.getCode()),
						key("message").value(errorCode.getMessage()),
            key("statusCode").value(String.valueOf(errorCode.getStatus().value())),
						key("status").value(errorCode.getStatus().getReasonPhrase()));
			fieldDescriptors.add(attributes);
		}

		return fieldDescriptors;
}
```

* fieldWithPath static 메소드는 한 경로에 존재하는 값들을 매핑하기 위한 메소드 입니다. 

* attributes() 메소드는 .snippet 파일의 key에 존재하는 필드에 value의 값을 매핑할 수 있습니다. 

* ```json
  "NOT_MATCHED_COMMENT_AUTHOR": {
      "code": "C2",
      "message": "해당 요청자가 작성한 코멘트가 아닙니다.",
      "status": 403
  }
  ```

* 응답이 위와 같은 형태로 존재하므로, key() 메소드와 value() 메소드로 errorcode-response-fields.snippet 파일에 맞게 문서화 시켜줄 수 있습니다.

  * errorcode-response-fields.snippet 파일에는 4개의 key 값인 code, message, statusCode, status 가 존재합니다. 



### 3. 테스트코드

```java
class ErrorCodeControllerTest extends ControllerSliceTest {
	private static final String ERROR_SNIPPET_FILE = "errorcode-response";

	@Test
	void errorCodes() throws Exception {
		ResultActions result = mockMvc.perform(get("/error-code")
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print());

		result.andDo(document(ERROR_SNIPPET_FILE,
			customResponseFields(ERROR_SNIPPET_FILE,
				fieldDescriptors()
			)
		));
	}
}
```

* customResponseFields(ERROR_SNIPPET_FILE,fieldDescriptors())
  * ERROR_SNIPPET_FILE은 우리가 커스텀한 snippet 파일 입니다. 
  * 위에서 설명한 fieldDescriptors()를 이용하여 설명을 매핑할 수 있습니다.





이렇게 자동화를 하여서 , ErrorCode에 대한 Enum이 계속 생겨나도, 클라이언트 개발자들은 RestDocs로 만들어진 API 문서를 보고 응답 코드를 편하게 볼 수 있게 되었습니다. 

이제  노션 등과 같은 문서로 공유할떄 업데이트를 해야하는 귀찮음 등을 해결할 수 있었습니다. 

* 실제로 프로젝트를 같이 하신 프론트엔드, 백엔드 팀원들이 매우 편리해 하였고 협업과정과 작업속도에 좋은 효과를 보였습니다.



>  라이브러리를 분석하여 원하는 기능과 결과를 만들어낸것에 대해 너무나 만족스러웠고, 
>
> 저랑 같은 고민을 하는 다른 개발자들에게 도움이 되었으면 좋겠습니다. 



> 추가적으로 Link나 팝업창 이 필요하다면?
>
> *  [RestDocs에 날개를 달자 - 이호진님](https://techblog.woowahan.com/2678/)  
> * https://0soo.tistory.com/198

  



### Ref

* https://docs.asciidoctor.org/asciidoc/latest/docinfo/

*  [RestDocs에 날개를 달자 - 이호진님](https://techblog.woowahan.com/2678/)  

* [0soo 블로그 - RestDocs Custom](https://0soo.tistory.com/201)
* [0soo 블로그 - Enum 문서화](https://0soo.tistory.com/199)

