# Restdocs Enum 공통코드 문서화 방법

문서 작성 시 사용되는 타입에 enum이 없기 때문에 enum인 경우 따로  보기좋게 만들 수 있다. 
예를 들어 Hobby와 Role의 Enum 클래스를 문서로 만들 수 있다.



먼저 interface를 정의한다.

```java
public interface DocsEnumType {

  String getType();
	
  String getDescription();

}
```

enum은 interface를 상속받아 메서드를 구현할 수 있는데, interface를 구현하게 되면, 다른 메소드나 생성자에서 인터페이스로 enum을 참조 할 수 있다.

만약 문서화가 필요한 Enum이라면 DocsEnumType 인터페이스를 상속받아야 하고, 

DocsEnumType을 상속받은 모든 enum은 getType()과 getDescription()을 구현해야 하고,  DocsEnumType으로 공통적으로 받을 수 있게 된다.



**Enum - Hobby, Role** 

```java
@Getter
@RequiredArgsConstructor
public enum Hobby implements DocsEnumType {
	BASE_BALL("야구 필드"),
	BASKET_BALL("농구 필드"),
	FOOT_BALL("축구 필드");

	private final String description;

	@Override
	public String getType() {
		return this.name();
	}

	@Override
	public String getDescription() {
		return this.description;
	}
}

@Getter
@RequiredArgsConstructor
public enum Role implements DocsEnumType {

	ROLE_USER("일반 권한"),
	ROLE_ADMIN("어드민 권한"),
	ROLE_ANONYMOUS("익명 사용자 권한");

	private final String description;

	@Override
	public String getType() {
		return this.name();
	}

	@Override
	public String getDescription() {
		return this.description;
	}
}
```



## 방법

모든 작업은 테스트 패키지 내에서만 실행한다. 테스트 패키지에 작성하면 테스트 실행시에만 동작하므로 실제 운영에서는 동작하지 않는다. 문서만 만들어 지게 된다)



### 1. Test 패키지에 문서화하고자 하는 Enum 값을 반환하는 컨트롤러를 만든다 

* 주의사항 - 테스트 패키지에 클래스를 만들어야 한다. 테스트 패키지가 아니면 운영환경에서도 동작한다. 

```java
// 주의사항 - 테스트 패키지에 클래스를 만들어야 한다. 테스트 패키지가 아니면 운영환경에서도 동작한다.  
@RestController
public class EnumController {

	@GetMapping("/enums")
	public ResponseEntity<?> enums() {

		return ResponseEntity.ok(
			EnumDocsResponse
				.builder()
				.hobby(to(Hobby.values()))
				.role(to(Role.values()))
				.build());
	}
  
}

//
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public static class EnumDocsResponse {

		private Map<String, String> hobby;
		private Map<String, String> role;

}
```

EnumDocsResponse를 이용한 응답값으로 enum 문서를 만든다. 
문서화하고자 하는 모든 enum값을 명시해준다.

* Map<String, String> 형식으로 enum 이름은 필드명.
* 이외에 Sex, Authority 등 추가적인 Enum을 문서화 할 경우 그냥 필드로 추가해주면 된다.
  * private Map<String, String> Sex;
  * private Map<String, String> Authority;



### 2. src/test/resources/org/springframework/restdocs/templates 밑에 커스텀 스니펫 파일을 만든다.

```
// in enum-response.fields.snippet 파일 
{{enumTypeName}}
|===
|코드|코드명

{{#fields}}
|{{#tableCellContent}}`+{{path}}+`{{/tableCellContent}}
|{{#tableCellContent}}{{description}}{{/tableCellContent}}

{{/fields}}
|===
```

enumTypeName으로 문서 제목을 꾸밀것이며, 이것은 테스트 코드에서 지정할 수 있다. (attributes() 메소드로)

path와 description은 DocsEnumType 인터페이스를 구현한 Enum에서 얻어 사용할 수 있다.

* public String getType() {}
* public String getDescription() {}



### 3. 테스트 코드를 작성한다. 테스트코드용 클래스도 역시 테스트 패키지에 존재한다. 

```java
public class EnumControllerTest {

	protected final MockMvc mockMvc;

	protected final ObjectMapper objectMapper;

	@Test
	void enums() throws Exception {

		// request
		ResultActions result = this.mockMvc.perform(get("/enums") // enums라는 테스트에 생성한 Controller에 요청
			.contentType(MediaType.APPLICATION_JSON));

		MvcResult mvcResult = result.andReturn();

		EnumDocsResponse enumDocsResponse = parseResult(mvcResult);

		// 문서화
		result.andExpect(status().isOk())
			.andDo(print())
			.andDo(document("enum-response",
				customResponseFields("enum-response", beneathPath("hobby").withSubsectionId("hobby"),
					attributes(key("enumTypeName").value("hobby")),
					enumConvertFieldDescriptor((enumDocsResponse.getHobby()))
				),
				customResponseFields("enum-response", beneathPath("role").withSubsectionId("role"),
					attributes(key("enumTypeName").value("role")),
					enumConvertFieldDescriptor((enumDocsResponse.getRole()))
				)
			));
	}

	// Map으로 넘어온 enumValue를 fieldWithPath로 변경하여 리턴
	private static FieldDescriptor[] enumConvertFieldDescriptor(Map<String, String> enumValues) {
		return enumValues.entrySet().stream()
			.map(x -> fieldWithPath(x.getKey()).description(x.getValue()))
			.toArray(FieldDescriptor[]::new);
	}

  // 응답값 Body를 파싱하여 다시 자바 객체로 직렬화
	private EnumDocsResponse parseResult(MvcResult result) throws IOException {
		return objectMapper.readValue(
			result.getResponse().getContentAsByteArray(),
			new TypeReference<>() {
			});
	}

	// 커스텀 템플릿 사용을 위한 함수
	public static CustomResponseFieldsSnippet customResponseFields
	(String type, PayloadSubsectionExtractor<?> subsectionExtractor,
		Map<String, Object> attributes, FieldDescriptor... descriptors) {
		
    return new CustomResponseFieldsSnippet(type, subsectionExtractor, 
                                           Arrays.asList(descriptors), attributes, true);
	}


}
```

코드의 대략적인 플로우는 다음과 같다 

1. 테스트 패키지에 생성한 Controller에 요청을 한다. 요청을 하면 응답값으로 Map<String,String>형태로 우리가 지정한 enum의 value 값이 리턴된다. 
2. parseResult() 메서드를 통해 응답값을 ObjectMapper로 다시 직렬화 한다. 우리는 여기서 EnumDocsResponse 객체를 얻을 수 있다.
3. result.andDo(document("문서화된 폴더 이름")) 을 지정한다
4. customResponseFields 메소드로 커스텀한 enum의 문서화를 진행한다 .
   * customResponseFields() : CustomResponseFieldsSnippet 클래스를 반환 - 문서를 만든다
   * "enum-response" : 사용할 snippet 파일의 이름을 지정한다. 우리는 enum-response-fields.snippet을 사용하므로 "enum-response" 만 넘긴다. (커스텀 스니펫 파일의 prefix) 
   * beneathPath("hobby").withSubsectionId("hobby") : 주어진 경로 아래에 있는 JSON 페이로드의 하위 섹션을 추출한다.
     * response에 hobby를 명시해주면 이에 따라 데이터를 추출한다.
   * attributes(key("enumTypeName").value("role")) : enumTypeName이라고 적혀있는 곳에 role을 매핑한다 - key,value
   * enumConvertFieldDescriptor(): key, value 형식으로 FieldDescriptor를 만들어 문서를 만들어준다. 



**CustomResponseFieldsSnippet 클래스**

```java
public static class CustomResponseFieldsSnippet extends AbstractFieldsSnippet {

		public CustomResponseFieldsSnippet(String type, PayloadSubsectionExtractor<?> subsectionExtractor,
			List<FieldDescriptor> descriptors, Map<String, Object> attributes,
			boolean ignoreUndocumentedFields) {
			super(type, descriptors, attributes, ignoreUndocumentedFields,
				subsectionExtractor);
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

* 이 클래스는 default 템플릿이 아닌 custom 템플릿을 사용하기 위한 클래스
* 생성자의 인자 중 type을 보고 template에서 맞는 템플릿을 선택해서 동작
  * 방금 만든 enum-response-fields.snippet을 사용하기 위해서는 type의 값으로 "enum-response"를 주면된다



CustomResponseFieldsSnippet 클래스의 생성자 인자.

-  **type** 
  - 사용할 .snippet 파일명을 지정한다.
  -  Fields, Body 등 postfix가 다 다르며 현재 우리는 enum-response-fields.snippet을 사용하므로 "enum-response" 만 넘긴다. (커스텀 스니펫 파일의 prefix) 

- **subsectionExtractor** 
  - 만약 ApiResponseDTO로 Wrapping을 해서 응답을 보낸다면, 반환값으로 ApiResponseDto는 data필드를 가지고 있고 이 데이터 필드 안에 문서화하고자 하는 enum값들을 담아서 보낸다.
  - hobby값을 예로 들면, data.hobby에 값이 들어있다. (ApiResponseDTO의 필드명이 data ). 
  - 따라서 beneathPath에는 data.hobby, withSubsectionId에는 hobby를 명시해주면 이에 따라 데이터를 추출한다.
  - 만약 wrapping 하지 않는다면 그냥 필드명인 hobby나 role을 입력해주면 된다. 
  - 또한 생성되는 adoc 파일 이름에도 추가로 붙는다 -  enum-response-fields-hobby.adoc 이 된다. 
- **attributes**
  - snippet 파일에 매핑하여 입력할 key, value 형식의 데이터이다.  
- **descriptors**
  - 현재 응답값을 key, value 형식으로 돌려주고 있다. 
  - key, value 형식으로 FieldDescriptor를 만들어 문서를 만들어준다. 





## Api Response 커스텀시

만약 Response 를 커스텀하여 다음과 같이 사용한다면

```java
@Getter
@NoArgsConstructor
@Builder
public class ApiResponseDto<T> {

    private T data;

    private ApiResponseDto(T data){
        this.data = data;
    }

}
```



API도 다음과 같이 진행한다

```java
class CommonDocControllerTest extends RestDocsTestSupport {

    @Test
    public void enums() throws Exception {
        // 요청
        ResultActions result = this.mockMvc.perform(
                get("/enums")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // 결과값
        MvcResult mvcResult = result.andReturn();

        // Result 파싱
        EnumDocsResponse enumDocsResponse = parseResult(mvcResult);

        // 문서화 진행
        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        customResponseFields("enum-response", beneathPath("data.hobby")
                                             .withSubsectionId("hobby"), // (1)
                                attributes(key("enumTypeName").value("hobby")),
                                enumConvertFieldDescriptor((enumDocsResponse.getHobby()))
                        ),
             
                        customResponseFields("enum-response", beneathPath("data.role")
                                             .withSubsectionId("role"), 
                                attributes(key("enumTypeName").value("role")),
                                enumConvertFieldDescriptor((enumDocsResponse.getRole()))
                        )
                ));
    }
  
  	private EnumDocsResponse parseResult(MvcResult result) throws IOException {
      ApiResponseDto<EnumDocsResponse> apiResponseDto = objectMapper
        .readValue(result.getResponse().getContentAsByteArray(),
                   new TypeReference<ApiResponseDto<EnumDocsResponse>>() {});
		
    return apiResponseDto.getData();
	}

}
```





# 최종 코드

Controller

````java
@RestController
public class EnumController {

	@GetMapping("/enums")
	public ResponseEntity<?> enums() {

		return ResponseEntity.ok(
			EnumDocsResponse
				.builder()
				.hobby(to(Hobby.values()))
				.role(to(Role.values()))
				.build());
	}

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class EnumDocsResponse {

		private Map<String, String> hobby;
		private Map<String, String> role;

	}

	private Map<String, String> to(DocsEnumType[] enumTypes) {
		return Arrays.stream(enumTypes)
			.collect(Collectors.toMap(DocsEnumType::getType, DocsEnumType::getDescription));
	}

	public interface DocsEnumType {
		String getType();

		String getDescription();
	}

	@Getter
	@RequiredArgsConstructor
	public enum Hobby implements DocsEnumType {
		BASE_BALL("야구 필드"),
		BASKET_BALL("농구 필드"),
		FOOT_BALL("축구 필드");

		private final String description;

		@Override
		public String getType() {
			return this.name();
		}

		@Override
		public String getDescription() {
			return this.description;
		}
	}

	@Getter
	@RequiredArgsConstructor
	public enum Role implements DocsEnumType {

		ROLE_USER("일반 권한"),
		ROLE_ADMIN("어드민 권한"),
		ROLE_ANONYMOUS("익명 사용자 권한");

		private final String description;

		@Override
		public String getType() {
			return this.name();
		}

		@Override
		public String getDescription() {
			return this.description;
		}
	}

}
````



테스트 코드

```java
@WebMvcTest
@AutoConfigureRestDocs
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
public class EnumControllerTest {

	protected final MockMvc mockMvc;

	protected final ObjectMapper objectMapper;

	@Test
	void enums() throws Exception {

		// request
		ResultActions result = this.mockMvc.perform(get("/enums")
			.contentType(MediaType.APPLICATION_JSON));

		MvcResult mvcResult = result.andReturn();

		EnumDocsResponse enumDocsResponse = parseResult(mvcResult);

		// 문서화
		result.andExpect(status().isOk())
			.andDo(print())
			.andDo(MockMvcRestDocumentation.document("enum-response",
				customResponseFields("enum-response", beneathPath("hobby").withSubsectionId("hobby"),
					attributes(key("enumTypeName").value("hobby")),
					enumConvertFieldDescriptor((enumDocsResponse.getHobby()))
				),
				customResponseFields("enum-response", beneathPath("role").withSubsectionId("role"),
					attributes(key("enumTypeName").value("role")),
					enumConvertFieldDescriptor((enumDocsResponse.getRole()))
				)
			));
	}

	// Map으로 넘어온 enumValue를 fieldWithPath로 변경하여 리턴
	private static FieldDescriptor[] enumConvertFieldDescriptor(Map<String, String> enumValues) {
		return enumValues.entrySet().stream()
			.map(x -> fieldWithPath(x.getKey()).description(x.getValue()))
			.toArray(FieldDescriptor[]::new);
	}

   // 응답값 Body를 파싱하여 다시 자바 객체로 직렬화
	private EnumDocsResponse parseResult(MvcResult result) throws IOException {
		return objectMapper.readValue(
			result.getResponse().getContentAsByteArray(),
			new TypeReference<>() {
			});
	}

	// 커스텀 템플릿 사용을 위한 함수
	public static CustomResponseFieldsSnippet customResponseFields
	(String type, PayloadSubsectionExtractor<?> subsectionExtractor,
		Map<String, Object> attributes, FieldDescriptor... descriptors) {
		return new CustomResponseFieldsSnippet(type, subsectionExtractor, Arrays.asList(descriptors), attributes
			, true);
	}

	public static class CustomResponseFieldsSnippet extends AbstractFieldsSnippet {

		public CustomResponseFieldsSnippet(String type, PayloadSubsectionExtractor<?> subsectionExtractor,
			List<FieldDescriptor> descriptors, Map<String, Object> attributes,
			boolean ignoreUndocumentedFields) {
			super(type, descriptors, attributes, ignoreUndocumentedFields,
				subsectionExtractor);
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

}

```



커스텀 snippet 파일

```
{{enumTypeName}}
|===
|코드|코드명

{{#fields}}
|{{#tableCellContent}}`+{{path}}+`{{/tableCellContent}}
|{{#tableCellContent}}{{description}}{{/tableCellContent}}

{{/fields}}
|===
```



<img src="https://blog.kakaocdn.net/dn/bySMnB/btrXv2MDjdH/J3tgGghdOX3xT2yOQviamk/img.png" width = 950 height = 350>



### 참조

* https://velog.io/@backtony/Spring-REST-Docs-%EC%A0%81%EC%9A%A9-%EB%B0%8F-%EC%B5%9C%EC%A0%81%ED%99%94-%ED%95%98%EA%B8%B0#enum-%EC%BD%94%EB%93%9C-%EB%AC%B8%EC%84%9C%ED%99%94