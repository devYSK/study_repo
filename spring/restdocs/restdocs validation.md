# REST Docs에 DTO Bean Validation 담기 

Bean Validation 사용시 Validation 정보를 담기 위한 커스텀 템플릿과 `ConstraintDescriptions`을 이용해서 Bean Validation 정보를 가져오는 방법에 대해 알아보자.

## 1. 커스텀 템플릿 만들기

스니펫 템플릿을 커스텀한다.

* test/resources/org/springframework/restdocs/templates/request-fields.snippet 파일

```adoc
// request-fields.snippet
===== Request Fields
|===
|필드명|타입|설명|필수값|Constraints

{{#fields}}
|{{#tableCellContent}}`+{{path}}+`{{/tableCellContent}}
|{{#tableCellContent}}`+{{type}}+`{{/tableCellContent}}
|{{#tableCellContent}}{{description}}{{/tableCellContent}}
|{{#tableCellContent}}{{#optional}}true{{/optional}}{{^optional}}false{{/optional}}{{/tableCellContent}}

|{{#tableCellContent}}{{#constraints}}{{.}} +{{/constraints}}{{/tableCellContent}}

{{/fields}}

|===
```

* 필드명, 타입, 설명, 필수여부, constrains

* constraints의 `{{.}}`은 필드가 존재할때만 출력을 해주는 문법이다. `+`는 강제 개행인데, 한 줄씩 띄어준다. 

  쉼표같이 선호하는 표현 방식이 있으면 바꿔서 쓰면 된다.

* 아래처럼 attributes에 constraint가 없을 경우 `MustacheException$Context 예외가 발생할 수 있다. 그러므로 위에처럼 작성하자. 

```
|{{#tableCellContent}}{{constraints}}{{/tableCellContent}} // 이렇게 하면 에러 발생가능
```



테스트코드에 다음과 같은 형식으로 작성하면 되지만, 하나하나 입력하기에는 너무 번거롭다.

```java
requestFields(
    fieldWithPath("title").type(JsonFieldType.STRING)
      .description("제목")
      .attributes(key("constraints").value("not null"))
);
```



컬럼 항목이 많아지다보니 표가 복잡해지면, `[%autowidth.stretch]`를 표에 붙이면 된다.

여유가 있으면 스니펫을 새로 만들면 된다.

## 2. Validation 정보 불러오기

### ConstraintDescriptions

`spring-restdocs-core`에 `ConstraintDescriptions`가 들어있다. 특정 클래스에 제약사항이 있는지 확인해준다.

```java
package org.springframework.restdocs.constraints;

public class ConstraintDescriptions {

	private final Class<?> clazz;

	private final ConstraintResolver constraintResolver;

	private final ConstraintDescriptionResolver descriptionResolver;
  
  ..
    
  public List<String> descriptionsForProperty(String property) {
		List<Constraint> constraints = this.constraintResolver.resolveForProperty(property, this.clazz);
		List<String> descriptions = new ArrayList<>();
		for (Constraint constraint : constraints) {
			descriptions.add(this.descriptionResolver.resolveDescription(constraint));
		}
		Collections.sort(descriptions);
		return descriptions;
	}
}
```



사용법은 간단하다.

```java
ConstraintDescriptions constraintDescriptions = 
  new ConstraintDescriptions(DTO클래스.class);
List<String> nameDescription = constraintDescriptions.descriptionsForProperty("필드이름");
```

* DTO클래스.class로 ConstraintDesciptions를 만들고
* constraintDescriptions.descriptionsForProperty(필드명) 으로 제약조건들의 메시지를 List로 가지고 올 수 있다.
* 아직 한글화는 되지 않은것 같은데, Locail 설정을 찾거나, ConstraintDescriptions 클래스가 생성자로 `constraintResolver` 를 받고 있으므로 ResourceBundleConstraintDescriptionResolver 구현체 처럼 우리가 **직접 구현체를 상속해서 집어넣어주면 될 것 같다.**



다음처럼 메서드로 뺄 수도 있다.

```java
private static String getConstraintMessage(Class<?> constraintClassType, String propertyName) {		
  ConstraintDescriptions constraintDescriptions = new ConstraintDescriptions(constraintClassType);
       
  List<String> nameDescription = constraintDescriptions.descriptionsForProperty(propertyName);     
  
  return String.join("\n", nameDescription);
}
  
private static Attribute constrainsAttribute(Class<?> constraintClassType, String propertyName) {
  return key("constraints").value(getConstraintMessage(constraintClassType, propertyName));
}
```



`ConstraintDescriptionResolver`는 `ConstraintResolver`가 찾아온 `Constraint`을 문자열로 변환해준다. 

기본적으로 Bean Validator 2.0과 Hibernate Validator 스펙에 맞게 지원한다. 커스텀 제약사항이 있다면 해당 프로퍼티를 객체 생성시에 넣어주면 된다.

> 참고 - [기본 지원 constraints](https://docs.spring.io/spring-restdocs/docs/2.0.5.RELEASE/reference/html5/#documenting-your-api-constraints-describing)



만약 다음과 같은 에러가 발생하면

> Unable to create a Configuration,` because no Jakarta Bean Validation provider could be found`. Add a provider like Hibernate Validator (RI) to your classpath.
> `javax.validation.NoProviderFoundException`: Unable to create a Configuration, because no Jakarta Bean Validation provider could be found. Add a provider like Hibernate Validator (RI) to your classpath.



```groovy
implementation 'org.springframework.boot:spring-boot-starter-validation'
```

을 추가한다. 



> Bean Validation을 사용하는 DTO라면 어디에든 넣어줄 수 있기 때문에 request parameter에도 똑같이 적용할 수 있다.

## 3. DTO와 테스트코드 작성

```java
// dto
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PostCreateRequest {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    @NotNull(message = "null은 안됩니다.") 
    @Length(min = 1, max = 100, message = "1~100자만 가능 ")
    private String content;

    @NotNull(message = "userId는 필수 값입니다.")
    private Long userId;

}
```



테스트 코드

```java
class PostControllerTest {

    @DisplayName("Post 생성 성공 - post /api/v1/posts - Post 생성에 성공한다.")
    @Test
    void createPostSuccess() throws Exception {
        
        ...  생략
      
        this.mockMvc.perform(post("/api/v1/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postCreateRequest)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.postId").exists())
            .andDo(document("posts-create",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("title").type(JsonFieldType.STRING).description("게시글 제목")
                        .attributes(key("constraints").value(
                            getConstraintMessage(PostCreateRequest.class, "title")
                        )),
                    fieldWithPath("content").type(JsonFieldType.STRING).description("게시글 내용")
                        .attributes(key("constraints").value(
                            getConstraintMessage(PostCreateRequest.class, "content")
                        )),
                    fieldWithPath("userId").type(JsonFieldType.NUMBER).description("게시글 작성 유저 Id")
                        .attributes(constrains(PostCreateRequest.class, "userId"))
                ),
                responseFields(
                    fieldWithPath("postId").type(JsonFieldType.NUMBER).description("게시글 Id")
                )
            ))
            .andDo(print());
    }

    private static String getConstraintMessage(Class<?> constraintClassType, String propertyName) {
 				ConstraintDescriptions constraintDescriptions = 
          new ConstraintDescriptions(constraintClassType);
        List<String> nameDescription = constraintDescriptions.descriptionsForProperty(propertyName);
        return String.join("\n", nameDescription);
    }
  
    private static Attribute constrainsAttribute(Class<?> constraintClassType, String propertyName) {
        return key("constraints").value(getConstraintMessage(constraintClassType, propertyName));
    }
}
```

* getConstraint 란 메소드를 만들어 공통으로 사용할 수 있게 하였다.
* constrainsAttribute() 메소드 처럼 조금 편하게 구현할 수도 있다.







## 내부 동작

`org.springframework.restdocs.constraints` 패키지 안은 아래와 같이 구성된다.

<img src="https://blog.kakaocdn.net/dn/Pwynw/btrXnHjcxwr/hxWWmsHWZ1nqkuKGMSgEGK/img.png" width = 950 height = 500>

`ConstraintDescriptions.descriptionsForProperty() 메소드`에 프로퍼티(필드) 명을 매개변수로 넣으면 `ConstraintResolver`는 해당 프로퍼티에 맞는 `Constraint`를 가져온다.

 기본 구현인 `ValidatorConstraintResolver`는 내부적으로 Bean Validation의 `Validator` 객체를 사용한다.

Bean Validation을 이용해서 해당 클래스의 필드에 붙은 `@NotNull`과 같은 constraint를 가져온다. Java Bean 규격을 따르기 때문에 필드명과 json field의 이름이 일치하지 않는 경우 주의해야 한다.



> 만약 json field가 user_id일 경우 카멜케이스로 변경해서 넣어줘야 한다. 
>
> 내부에 있는 `PropertyDescriptor`가 [Java Bean 컨벤션](https://en.wikipedia.org/wiki/JavaBeans#JavaBean_conventions)에 따라 프로퍼티를 찾기 때문이다.



`ConstraintDescriptionResolver`는 `ConstraintResolver`가 찾아온 `Constraint`을 문자열로 변환해준다. 

기본적으로 Bean Validator 2.0과 Hibernate Validator 스펙에 맞게 지원한다. 

커스텀 제약사항(custom validation)이 있다면 해당 프로퍼티를 객체 생성시에 넣어주면 된다.

> 참고 - [기본 지원 constraints](https://docs.spring.io/spring-restdocs/docs/2.0.5.RELEASE/reference/html5/#documenting-your-api-constraints-describing)





### 참조

* https://velog.io/@dae-hwa/REST-Docs%EC%97%90-DTO%EC%9D%98-Validation-%EC%A0%95%EB%B3%B4-%EB%8B%B4%EA%B8%B0
* https://docs.spring.io/spring-restdocs/docs/2.0.5.RELEASE/reference/html5/#documenting-your-api-constraints
* https://github.com/spring-projects/spring-restdocs/blob/main/samples/rest-notes-spring-hateoas/src/test/java/com/example/notes/ApiDocumentation.java
* https://github.com/spring-projects/spring-restdocs/blob/main/spring-restdocs-core/src/test/java/org/springframework/restdocs/constraints/ResourceBundleConstraintDescriptionResolverTests.java
