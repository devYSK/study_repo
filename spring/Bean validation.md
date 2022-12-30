# Java Bean Validation + Validation Tip

* [추천 레퍼런스 페이지](https://medium.com/@gaemi/java-%EC%99%80-spring-%EC%9D%98-validation-b5191a113f5c)

## Bean Validation이란?

특정 구현체가 아닌 Bean Validation 2.0(JSR-380)이라는 기술 표준으로 여러 검증 애노테이션과 여러 인터페이스의 모음이다. 

> [Spring Boot 3.0.0 M1 릴리스 노트](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.0.0-M1-Release-Notes) 에서 
>
> Spring Boot 2.X는 Jakarta EE를 지원하지 않지만 Spring Boot 3.X에서는 지원이 제공된다.
>
> - Spring Boot 2.X의 경우 `javax`.
> - Spring Boot 3.X의 경우 `jakarta`. 를 사용해야 한다. 



Bean Validation을 구현한 기술중 일반적으로 사용하는 구현체는 하이버네이트 Validator이다. 

(이름이 하이버네이트지만 ORM과는 관련없다. )

즉, Bean Validation 를 활용하면 `어노테이션 기반으로` 우리가 구현했던 각종 구현로직들을 간단하게 적용할 수 있다. 



>  이전에 사용했던 검증방식은 직접 Validator 인터페이스를 구현한 뒤 InitBinder로 구현한 검증기를 등록해서 사용하거나 또는 Assert, If문 등을 통한 검증 메소드를 만들어 사용하였다. 



* https://beanvalidation.org/ 



<img src="https://blog.kakaocdn.net/dn/mVulJ/btrUNUEO8dT/WSbr6qTKsn3KKG2FHuCKc0/img.png" width = 800 height = 400>

* https://docs.jboss.org/hibernate/validator/8.0/reference/en-US/html_single/#preface

 유효성 검사(Validation)은 어디서 해야할까? 데이터 검증이 여러 계층에 걸쳐서 이루어 진다면, 동일한 내용에 대한 검증 로직이 중복되거나 계층간 검증 로직의 불일치로 오류가 생길 것이다. 이를 해결하기 위해서 데이터 검증을 위한 로직을 도메인 모델 자체에 묶어서 표현하는 방식이 있다.



일반적인 애플리케이션에서 데이터 유효성 검사 로직은 다음과 같은 문제를 가지고 있다.

1. 애플리케이션 전체에 분산되어 있다.
2. 코드 중복이 심하다.
3. 비즈니스 로직에 섞여있어 검사 로직 추척이 어렵고 애플리케이션이 복잡해진다.

이러한 문제 때문에 데이터 유효성 검사 로직에 기능을 추가, 수정하기 어렵고, 오류가 발생할 가능성도 크다.

Java에서는 2009년부터 Bean Validation이라는 데이터 유효성 검사 프레임워크를 제공하고 있다. Bean Validation은 위에서 말한 문제들을 해결하기 위해 다양한 제약(Contraint)을 도메인 모델(Domain Model)에 어노테이션(Annotation)로 정의할 수 있게한다. 이 제약을 유효성 검사가 필요한 객체에 직접 정의하는 방법으로 기존 유효성 검사 로직의 문제를 해결한다.

### Jakarta Validation API 구현체 - Hibernate Validator

#### **구현체는 다음 패키지에 정의되어 있다**

> [org.hibernate.validator.internal.constraintvalidators.bv](https://github.com/hibernate/hibernate-validator/blob/main/engine/src/main/java/org/hibernate/validator/internal/constraintvalidators/bv/NotNullValidator.java)


  
<br>

> `javax.validation` 으로 시작하면 특정 구현에 관계없이 제공되는 표준 인터페이스이고,
> `org.hibernate.validator` 로 시작하면 하이버네이트 validator 구현체를 사용할 때만 제공되는 검증 기능이라고 한다. 
>
> 실무에서 보통 hibernate validator를 사용하고, spring에서도 hibernamte validator를 사용하므로 편하게 사용하면 된다고 한다.



## 세팅

스프링 프레임워크에서 제공하는 유효성 라이브러리를 사용하기위해서는 `gradle`의 dependecy에 추가해야만 한다. 

```groovy
dependencies{
	implementation 'org.springframework.boot:spring-boot-starter-validation'
}
```

* 이 의존성을 추가하지 않으면 동작하지 않는다.



## Spring Boot AutoConfigruation With Validator

Spring Boot `ValidationAutoConfiguration` 클래스를 통해서  

 `LocalValidatorFactoryBean`와 `MethodValidationPostProcessor`를 자동으로 설정한다.

`LocalValidatorFactoryBean`는 Spring에서 Validator를 사용하기 위해서 필요하고  
 `MethodValidationPostProcessor`는 메서드 파라미터 또는 리턴 값을 검증하기 위해서 사용된다.



AutoConfiguration으로 별다른 설정 없이 Spring Boot에서 바로 Validation을 사용할 수 있다.



# 사용 방법

annotation만 붙는다고 동작하지 않고 추가적으로 @Valid와 @Validated 어노테이션 을 붙여줘야 Validation이 동작한다.

* JPA에서는 INSERT 전에 검사하는 구문이 있다.

@Valid와 @Validated 어노테이션을 이용하여 검증할 수 있다.



> @Valid와 @Validated 어노테이션은 동작하는 위치나 기능이 조금 다르다. 밑에 정리하였다.



## Collection, Container 요소 validation

컨테이너(Container)의 요소(Element)에 대해서도 유효성 검사가 필요할 때가 있다. Bean Validation 2.0부터 컨테이너의 요소에 대해 유효성 검사가 가능해졌다. 아래와 같이 제약을 정의할 수 있다.

```java
public class DeleteContacts {
    @Min(1)
    private Collection<@Length(max = 64) @NotBlank String> uids;
}
```



## @Valid 어노테이션

Controller에서  들어오는 입력값에 대해 `@Valid` 어노테이션을 이용하여 검증할 수 있다.

```java
@PostMapping
public ResponseEntity<?> save(@Valid @RequestBody SaveRequest) {
  ...
}
```

* 주의 : DTO 클래스에 validation 어노테이션을 적용했다 하더라도 메서드에 @Valid가 없다면 동작하지 않는다 



> **컨트롤러에서 검증 실패 시, `MethodArgumentNotValidException` 이 발생한다.**



### 동작 원리

Controller 로 요청이 들어오면, Dispatcher Servlet을 걸쳐서 Controller가 호출이 된다.

이 과정에서 Dispatcher Servlet 에서 @Valid 를 찾아서 검증을 진행하게 됩니다.

Dispatcher Servlet 동작 중, 컨트롤러 메소드를 호출하는 과정에서 메소드의 값을 처리해주는 `ArgumentResolver`가 동작하는데, 이때 `@Valid` 역시 `ArgumentResolver`에 의해 처리된다. 이러한 이유로, `@Valid` 어노테이션은 컨트롤러에서만 동작한다.



1. Dispatcher Servlet 에서 요청에 맞는 컨트롤러 탐색

2. RequestResponseBodyMethodProcessor 클래스의 resolverArgument 메소드 호출

3. AbstractMessageConverterMethodArgumentResolver 클래스의 validateIfApplicable 메소드 호출

4. ValidationAnnotationUtils 클래스의 determineValidationHints 메소드 호출

   * 어노테이션이 java.validation.Valid 인지, Validated인지, Valid 로 시작하는지 확인 후 Object Array 리턴

5. DataBinder class의 validate 호출 

   * getValidator() for문 돌려 null이 아니면 SpringValidatorAdapter.validate() 호출

   *  SpringValidatorAdapter 를 통해  hibernate.validator의 ValidatorImpl 클래스 호출
   * ValidatorImpl 에서 검증 후 결과 리턴

6. 검증 결과 binder에 Errors 에 값이 있을 시 MethodArgumentNotValidException 발생



> \- @Valid 은 Dispatcher Servlet 에서 사용하고 있다.



## **@Validated** 어노테이션

- Controller 말고 다른 곳에서도 검증을 하고 싶으면?
- 객체 내에 있는 컴포넌트 객체들도 검증하고 싶으면?
- 검증 메소드 별로 상황에 따라서 DTO를 계속 만들지 않고, 그룹을 사용하고 싶으면?

`@Validated`은 스프링에서 제공하는 인터페이스로 **다른 계층에서 유효성을 검증할 때 사용하고**, 유효성 검증 그룹의 지정할 때 사용한다.

@Validated 어노테이션은 AOP 방식으로 동작하며, **MethodValidationInterceptor** 클래스에서 가로채서 검증 한다.

* `@Validated기능은 AOP에 의해 동작되기 때문에스프링 Bean이여만 사용할 수 있다`

> 검증 실패 시 @Valid와 다르게 **ConstraintViolationException**이 발생한다



```java
@Override
@Nullable
public Object invoke(MethodInvocation invocation) throws Throwable {
		// Avoid Validator invocation on FactoryBean.getObjectType/isSingleton
		if (isFactoryBeanMetadataMethod(invocation.getMethod())) {
			return invocation.proceed();
		}

		Class<?>[] groups = determineValidationGroups(invocation);

		// Standard Bean Validation 1.1 API
		ExecutableValidator execVal = this.validator.forExecutables();
		Method methodToValidate = invocation.getMethod();
		Set<ConstraintViolation<Object>> result;

		Object target = invocation.getThis();
		Assert.state(target != null, "Target must not be null");

		try {
			result = execVal.validateParameters(target, methodToValidate, invocation.getArguments(), groups);
		}
		catch (IllegalArgumentException ex) {
			// Probably a generic type mismatch between interface and impl as reported in SPR-12237 / HV-1011
			// Let's try to find the bridged method on the implementation class...
			methodToValidate = BridgeMethodResolver.findBridgedMethod(
					ClassUtils.getMostSpecificMethod(invocation.getMethod(), target.getClass()));
			result = execVal.validateParameters(target, methodToValidate, invocation.getArguments(), groups);
		}
		if (!result.isEmpty()) {
			throw new ConstraintViolationException(result);
		}

		Object returnValue = invocation.proceed();

		result = execVal.validateReturnValue(target, methodToValidate, returnValue, groups);
		if (!result.isEmpty()) {
			throw new ConstraintViolationException(result);
		}

		return returnValue;
}
```



> 특정 ArgumnetResolver에 의해 유효성 검사가 진행되었던 @Valid와 달리, @Validated는 AOP 기반으로 메소드 요청을 인터셉터하여 처리된다. 
>
> @Validated를 클래스 레벨에 선언하면 해당 클래스에 유효성 검증을 위한 AOP의 어드바이스 또는 인터셉터(MethodValidationInterceptor)가 등록된다. 
>
> 그리고 해당 클래스의 메소드들이 호출될 때 AOP의 포인트 컷으로써 요청을 가로채서 유효성 검증을 진행한다.
>
> 이러한 이유로 @Validated를 사용하면 컨트롤러, 서비스, 레포지토리 등 계층에 무관하게 스프링 빈이라면 유효성 검증을 진행할 수 있다. 대신 클래스에는 유효성 검증 AOP가 적용되도록 @Validated를, 검증을 진행할 메소드에는 @Valid를 선언해주어야 한다.
>
> `이러한 이유로 @Valid에 의한 예외는 MethodArgumentNotValidException이며, @Validated에 의한 예외는 ConstraintViolationException이다. `



### @Validated 어노테이션 사용방법

```java
@Validated // 어노테이션 적용 시, 해당 클래스 내 @Valid 를 인식
@RequiredArgsConstructor
@Service
public class HelloService {

    private final HelloRepository helloRepository;

	  // @Valid가 선언된 경우, 해당 메소드는 Spring AOP Proxy 객체가 대신 수행
    // 실행 전에MethodValidationInterceptor.invoke() 로 검증한다
    // 검증에 실패하면 ConstraintViolationException 예외가 발생한다
    public HelloInsertRspDto save(@Valid SaveRequest request) {
        return new HelloInsertRspDto(helloRepository.save(request.toEntity());
    }

}
```



### 유효성 검증 그룹(Groups)의 지정 - @Validated

동일한 클래스에 대해 제약조건이 요청에 따라 달라질 수 있다. @Validated어노테이션이 이를 지원한다. 

 예를 들어 Create와 Update의 요청이 1개의 클래스로 처리될 때, 각기 다른 상황에 같은 클래스를 사용하므로 다른 제약 조건이 적용되어야 할 때가 있다. 이때는 검증될 제약 조건이 2가지로 나누어져야 하는데, Spring은 이를 위해 제약 조건이 적용될 검증 그룹을 지정할 수 있는 기능 역시 @Validated를 통해 제공하고 있다.

검증 그룹을 지정하기 위해서는 (내용이 없는) 마커 인터페이스를 간단히 정의해야 한다. 


<br>

CRUD 예제를 위해 `OnCreate`와 `OnUpdate`라는 2개의 marker interface를 정의한다.

* onCreate :  “Create” 상황에서는 id가 null일 수 있다
* onUpdate : “Update” 상황에서는 id가 있는 리소스이므로 not null 이여야 한다. 

```java
interface OnCreateGroup {}

interface OnUpdateGroup {}
```

그 다음 marker interface를 다음과 같은 constraint 어노테이션과 함께 사용할 수 있다.

```java
class PostRequest {

  @Null(groups = OnCreateGroup.class)
  @NotNull(groups = OnUpdateGroup.class)
  private Long id;
  
  @NotNull
  private String title;
  // ...
}
```

Spring은 `@Validated` 어노테이션을 이용해 validation group을 사용할 수 있도록 지원한다.

```java
@Service
@Validated
public class PostService {

    @Validated(OnCreateGroup.class)
    void validateForCreate(@Valid PostRequest request) { // create 시 Id는 null 이여야 한다
        // do something
    }

    @Validated(OnUpdateGroup.class)
    void validateForUpdate(@Valid PostRequest request) { // update 시 Id는 null이면 안된다. 
        // do something
    }
}
```

만약 위와 같이 OnCreateGroup를 @Validated의 파라미터로 넣어주었다면 OnCreateGroup에 해당하는 제약 조건만 검증이 된다. 만약 @Validated에 특정 `마커(group)`를 지정해주지 않았거나, groups가 지정되어 있는데 @Valid를 이용하면 다음과 같이 처리된다.

- @Validated에 특정 클래스를 지정하지 않는 경우: groups가 없는 속성들만 처리 
- @Valid or @Validated에 특정 클래스를 지정한 경우: 지정된 클래스를 groups로 가진 제약사항만 처리

 

@Validated의 그룹 지정 기능은 코드가 복잡해진다는 단점이 있다. 



## **[ @Valid와 @Validated 차이 ]**

@Validated의 기능으로 유효성 검증 그룹의 지정도 있지만 거의 사용되지 않으므로 유효성 검증 진행을 기준으로 차이가 있다.

-  @Valid
  - JSR-303 자바 표준 스펙
  - 특정 ArgumentResolver를 통해 진행되어 `컨트롤러 메소드의 유효성 검증`만 가능하다.
  - Java Bean 객체만 검증할 수 있기 때문에 Dto가 아닌 @RequestParam, @PathVariable은 숫자나 String값이기 때문에 처리하지 못한다. 
  - 유효성 검증에 실패할 경우 `MethodArgumentNotValidException`이 발생한다.
- @Validated
  - 자바 표준 스펙이 아닌 스프링 프레임워크가 제공하는 기능
  - `AOP를 기반`으로 스프링 빈의 유효성 검증을 위해 사용되며` 클래스에는 @Validated를, 메소드에는 @Valid`를 붙여주어야 한다.
  - 유효성 검증에 실패할 경우 `ConstraintViolationException`이 발생한다.



Spring은 `MethodArgumentNotValidException` 예외에 대해서 HTTP status code 400(Bad Request)를 기본적으로 자동으로 변환하지만,

`ConstraintViolationException` 예외는 기본적으로 exception을 handling 하지 않기 때문에 HTTP status code 500(Internal Server Error)로 처리한다.

* ConstraintViolationException는 예외처리 핸들러에 등록해서 처리해야 한다. 



## 어노테이션 방식을 사용하지 않고 Service나 다른 계층의 Bean에서 validation 하는 방법 



```java
@Service
public class UserAccountService {

    @Autowired
    private Validator validator;
    
    @Autowired
    private UserAccountDao dao;
    
    public String addUserAccount(UserAccount useraccount) {
        
        Set<ConstraintViolation<UserAccount>> violations = validator.validate(useraccount);

        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<UserAccount> constraintViolation : violations) {
                sb.append(constraintViolation.getMessage());
            }
            throw new ConstraintViolationException("Error occurred: " + sb.toString(), violations);
        }

        dao.addUserAccount(useraccount);       
        return "Account for " + useraccount.getName() + " Added!";
    }
}
```

 Spring은 자동으로 Validator 인스턴스를 제공하며 이를 UserAccountService 에 주입할 수 있다. 
반환값은 *ConstraintViolation*  Set 이다.

유효성 검사를 위반하지 않은 경우(객체가 유효한 경우) Set 은 비어 있다. (isEmpty) 

그렇지 않으면 *ConstraintViolationException* 이 발생한다



### 또는 이렇게도 사용가능 하다 

```java
public class ValidateUtil {
	@Autowired
	private Validator validator;

  //예외 종류는 바꿔도 되지만 RuntimeException 계열로 바꾸거면 상관없지만
	//checked 예외로 바꾼다면 rolLbackFor = " 에와 코드를 추가 할 것

  public void validate (Object entity){
		Set<ConstraintViolation<Object>> violations = validator.validate (entity);

    if (!violations.isEmpty()) {

      StringBuilder sb = new StringBuilder();

      for (ConstraintViolation‹Object> constraintViolation : violations) {
        sb.append (constraintViolation.getMessage )) ;
      }

      throw new ConstraintViolationException("Error occUrred: " + sb, violations);
    }
}
```

```java
public void save(UserAccount userAccount) {
  ValidateUtil.validate(userAccount);
}
```





## Path Variables & Request Parameters Validation

path에 포함된 variables과 query parameters에 대한 유효성 검사는 조금 다르게 동작한다.

Path Variable과 Request Parameter의 경우는 int와 같은 primitive type이거나 Integer 혹은 String과 같은 객체이기 때문에 복잡한 유효성 검사를 하지 않는다.

```java
@Validated
@RestController
public class ValidateParametersController {

    @GetMapping("/validatePathVariable/{id}")
    ResponseEntity<String> validatePathVariable(@PathVariable("id") @Min(5) int id) {
        return ResponseEntity.ok("valid");
    }

    @GetMapping("/validateRequestParameter")
    ResponseEntity<String> validateRequestParameter(@RequestParam("param") @Min(5) int param) {
        return ResponseEntity.ok("valid");
    }
}
```

**`@Validated` 어노테이션을 클래스 레벨의 Controller에 추가해 Spring이 메서드 매개 변수에 대한 제한 조건 annotation을 평가하게 해야한다.**

request body 유효성 검사와 달리 실패할 경우 `MethodArgumentNotValidException` 예외가 아닌 `ConstraintViolationException` 예외가 발생한다. 



# 참고 - 스프링 부트는 자동으로 글로벌 Validator로 등록한다.

LocalValidatorFactoryBean이 글로벌 Validator로 등록되며 위에서 사용해봤던 @NotNull과 같은 애노테이션 검증을 수행한다. 또한 검증 오류 발생시 FieldError, ObjectError를 생성해 BindingResult에 담아준다. 



## 주의 - 임의로 글로벌 Validator를 등록하는것을 주의하자.

 임의로 글로벌 Validator를 등록해주면 스프링 부트는 Bean Validator를 자동으로 글로벌 Validator로 등록하지않는다. 

 그러면  검증 애노테이션들이 동작하지 않는다. 그러니 이 부분을 주의하도록 하자. 



## 제약 조건에 대한 유효성 검증

### 1. **ValidatorFactory**에서 Validator를 가져와 `validate()`를 사용해 빈의 유효성을 검증

```java
ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
Validator validator = factory.getValidator();

Set<ConstraintViolation<Participants>> constraintViolations = validator.validate(participants);
```

제약조건을 위반한 내용은 Constraintiolation 인터페이스에 저장되고, 검증에 실패했다고 바로 Exception이 발생하진 않는다.

  

## JPA Hibernate에서 INSERT,UPDATE 전에 검증 처리 

JpaRepository 기본 Save 시
EntityIdentityInsertAction.preInsert() 메소드를 통해 
실제 DB Insert 전 검증과정을 거친다. 

이 때, 검증 작업이 진행된다.



* UPDATE는 `EntityUpdateAction 클래스`에서 검증메소드를 호출한다 



1. EntityIdentityInsertAction.execute() 호출
2. execute() 내부에서 preInsert()  호출
3. preInsert()내에서 EventListener들을 가지고 와서 onPreInsert(event) 호출 
4. BeanValidationEventListener.onPreInsert() 호출
   * 이 메소드 내에서 validate가 진행되며 이 때 javax.validation.Validator 가 사용된다. 



> BeanValidationEventListener 이클래스에서 insert, update, delete 전에 검증을 진행한다. 



# Spring MVC Custom Validation

사용 가능한 [constraint 어노테이션](https://docs.jboss.org/hibernate/beanvalidation/spec/2.0/api/javax/validation/constraints/package-summary.html)이 제공하는 제약 조건 외에 필요한 경우, 직접 만들어서 사용할 수 있다.

임의의 제약(Constraint)과 검증자(Validator)를 구현하여 사용하면 된다. 



CustomValidation을 위한 Annotation을 생성한다.

```java
@Documented
@Constraint(validatedBy = ContactNumberValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ContactNumberConstraint {
    String message() default "Invalid phone number";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
```

*@Constraint* 주석을 사용하여 필드의 유효성을 검사할 클래스를 정의. 

*message()* 는 사용자 인터페이스에 표시되는 오류 메시지 .

마지막으로 추가 코드는 대부분 Spring 표준을 준수하기 위한 상용구 코드이다.



## Validator 만들기

```java
public class ContactNumberValidator implements 
  ConstraintValidator<ContactNumberConstraint, String> {

    @Override
    public void initialize(ContactNumberConstraint contactNumber) {
    }

    @Override
    public boolean isValid(String contactField,
      ConstraintValidatorContext cxt) {
        return contactField != null && contactField.matches("[0-9]+")
          && (contactField.length() > 8) && (contactField.length() < 14);
    }

}
```

유효성 검사 클래스는 *ConstraintValidator* 인터페이스를 구현하고 isValid 메서드도 구현해야 한다.

 isValid 메서드에서 유효성 검사 규칙을 정의하면 된다 .

ConstraintValidator의 구현은 다음 제한 사항을 준수해야 한다.

- 객체는 매개변수화되지 않은 유형으로 확인되어야 합니다.
- 개체의 일반 매개변수는 제한되지 않은 와일드카드 유형이어야 합니다.



이 어노테이션을 사용하고자 하는  FIELD에 정의하면 된다



## 커스텀 클래스 수준 유효성 검사 Custom Class Validation

클래스에 적용할 수 있는 *FieldsValueMatch* 라는 새 annotaion을 추가한다 . 

주석에는 비교할 필드의 이름을 나타내는 두 개의 매개변수( field 및 fieldMatch) 가 있다.

### Custom Annotation 만들기

```java
@Constraint(validatedBy = FieldsValueMatchValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldsValueMatch {

    String message() default "Fields values don't match!";

    String field();

    String fieldMatch();

    @Target({ ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        FieldsValueMatch[] value();
    }
}
```

사용자 지정 주석에는 클래스에서 여러 FieldsValueMatch 주석을 정의하기 위한 *List* 하위 인터페이스 도 포함되어 있음을 볼 수 있다.



### Custom Validator 만들기

```java
public class FieldsValueMatchValidator 
  implements ConstraintValidator<FieldsValueMatch, Object> {

    private String field;
    private String fieldMatch;

    public void initialize(FieldsValueMatch constraintAnnotation) {
        this.field = constraintAnnotation.field();
        this.fieldMatch = constraintAnnotation.fieldMatch();
    }

    public boolean isValid(Object value, 
      ConstraintValidatorContext context) {

        Object fieldValue = new BeanWrapperImpl(value)
          .getPropertyValue(field);
        Object fieldMatchValue = new BeanWrapperImpl(value)
          .getPropertyValue(fieldMatch);
        
        if (fieldValue != null) {
            return fieldValue.equals(fieldMatchValue);
        } else {
            return fieldMatchValue == null;
        }
    }
}
```



### 어노테이션 적용 

```java
@FieldsValueMatch.List({ 
    @FieldsValueMatch(
      field = "password", 
      fieldMatch = "verifyPassword", 
      message = "Passwords do not match!"
    ), 
    @FieldsValueMatch(
      field = "email", 
      fieldMatch = "verifyEmail", 
      message = "Email addresses do not match!"
    )
})
public class NewUserForm {
    private String email;
    private String verifyEmail;
    private String password;
    private String verifyPassword;

    // standard constructor, getters, setters
}
```

사용자 등록에 필요한 데이터를 위한 *NewUserForm* 모델 클래스이다 . 

두 개의 값을 다시 입력하기 위한 두 개의 *verifyEmail* 및 *verifyPassword* 속성 과 함께 두 개의 *이메일* 및 비밀번호 속성이 있다.

일치하는 해당 필드에 대해 확인할 두 개의 필드가 있으므로 *NewUserForm* 클래스 에 두 개의 *@FieldsValueMatch* 어노테이션을 추가한다. 하나는 이메일 값용 이고 다른 하나는 비밀번호 이다 .



## EnumValidation 예시

```java
public enum MyColor {
    RED,
    GREEN,
    BLUE;
}

public class DTO {
  //...
      @EnumValid(enumClass = MyColor.class, message = "GREEN은 안됩니다.")
          MyColor favoriteColor;
  //...
}
```



### 커스텀 validation annotation

```java
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {EnumValidator.class}) 
public @interface EnumValid {
  // message는 어노테이션에서 지정해주지 않으면 default로 나가거나, properties로 설정해줄 수도 있다.
    String message() default "Invalid value. This is not permitted.";

  // 유효성 검사가 어떤 상황에서 실행되는지 정의할 수 있는 매개변수 그룹
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class<? extends java.lang.Enum<?>> enumClass();
}
```

### 커스텀 Validator

```java
public class EnumValidator implements ConstraintValidator<EnumValid, Enum<?>> {
    private EnumValid enumValid;

  // 어노테이션에서 설정한 값을 가져오려면 초기화해주어야 한다. 만약 isValid함수에서 그냥 해결할 수 있으면 안해도됨
    @Override
    public void initialize(EnumValid constraintAnnotation) {
        enumValid = constraintAnnotation;
    }

  // isValid를 오버라이드해서 작성하면 된다.
    @Override
    public boolean isValid(Enum<?> value, ConstraintValidatorContext context) {
        boolean result = true;
        Object[] enumValues = enumValid.enumClass().getEnumConstants();

        if (String.valueOf(value).equals(MyColor.GREEN.toString())) return false;

        return result;
    }
}
```

### 예외 처리 Hanlder (ControllerAdvice)

```java
@RestControllerAdvice
public class ExceptionHandler {
  @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        ErrorCode errorCode = ErrorCode.INVALID_INPUT_VALUE;
        ErrorResponse er = getErrorResponse(e, errorCode);
        log.error("handleValidationException[{}]", er);
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(er);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException e) {
        ErrorCode errorCode = ErrorCode.INVALID_INPUT_VALUE;
        ErrorResponse er = getErrorResponse(e, errorCode);
        log.error("ConstraintViolationException[{}]", er);
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(er);
    }

    public static ErrorResponse getErrorResponse(BindException e, ErrorCode code) {

        List<ErrorResponse.ValidationError> validationErrorList = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(ErrorResponse.ValidationError::of)
                .collect(Collectors.toList());

        return ErrorResponse.builder()
                .code(code.getCode())
                .message(code.getMessage())
                .errors(validationErrorList)
                .status(code.getStatus())
                .build();
    }
}
```



### 예외 공통 응답 Response Dto

```java
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {
    private int status;
    private String code;
    private String message;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<ValidationError> errors;

    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class ValidationError {
        private final String field;
        private final String value;
        private final String message;

        public static ValidationError of(FieldError fieldError) {
            return ValidationError.builder()
                    .field(fieldError.getField())
                    .value(String.valueOf(fieldError.getRejectedValue()))
                    .message(fieldError.getDefaultMessage())
                    .build();
        }

        public static ValidationError of(ConstraintViolation violation) {
            return ValidationError.builder()
                    .field(String.valueOf(violation.getPropertyPath()))
                    .value(String.valueOf(violation.getInvalidValue()))
                    .message(violation.getMessageTemplate())
                    .build();
        }
    }

    @Builder
    public ErrorResponse(int status, String code, String message, List<ValidationError> errors) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.errors = errors;
    }
}
```

# 결론

어노테이션의 의도는 숨어있기 때문에 내부적으로 어떤 동작을 하게 되는지 명확하지 않다면 로직 플로우를 이해하기 어렵다.

하물며 ‘커스텀’ 어노테이션은 그 부담을 가중시킬 수 있다.

어노테이션 추가가 당장의 작업 속도를 끌어올릴 순 있지만, 장기적 관점에서 시의적절한 것인지를 공감할 수 있어야 한다.

코드가 간결해진다는 장점 하나만 보고 커스텀 어노테이션을 남용하지 않게 주의해야 한다.

반복적으로 사용하지도 않고, 특정 요청 외에는 사용할 일이 없어 보이는 유효성 검사라면 단순 메서드로 만들어 처리하는 것이 더 좋을 것이다.  


### 

커스텀 어노테이션을 잘 이용하면 불필요한 반복코드가 줄어들고, **비즈니스 로직에 더 집중**할 수 있다는 장점이 있다.

다만, 커스텀 어노테이션은 의도와 목적을 명확히 하여 구성원간 공감대를 이룬 후 추가하는 것이 좋다.





# Bean Validation Annotation 종류

Validation 어노테이션은 2가지 패키지에 나눠서 존재한다. 

1. jakarta.validation 라이브러리의 javax.validation 패키지
2. org.hibernate.validator 라이브러리의 org.hibernate.validator.constratins 패키지 



## javax.validation 패키지에 존재하는 어노테이션

<img src="https://blog.kakaocdn.net/dn/bH3KeC/btrUL7SrL5c/3t7Ukw0sSCC7caOKwYRLP0/img.png" width= 450 height =600 style = "float: left">





|           애노테이션           |                             설명                             | 지원 타입                                                    |
| :----------------------------: | :----------------------------------------------------------: | ------------------------------------------------------------ |
|            `@Null`             |            null 이어야 한다. <br />모든 타입 허용            |                                                              |
|           `@NotNull`           |      null을 허용하지 않는다. <br />" ", ""은 허용한다.       |                                                              |
|          `@NotEmpty`           | null과 ""를 허용하지 않는다. " "는 허용한다. <br />collection의 경우 null이 아니고 크기가 0이 아닌지 검사한다. |                                                              |
|          `@NotBlank`           |              null과 "", " "를 허용하지 않는다.               |                                                              |
|           `@Email `            | 이메일 주소가 유효한지 검사한다.  <br />null은 유효하다고 판단한다. | CharSequence                                                 |
|      `@Size(min=, max=)`       | 길이 제약조건 검사. <br /> min 이상, max 이하의 길이를 가져야 하며 null은 허용하므로 @NotNull을 같이 사용해야 한다. | CharSequence, Collection, Map, 배열                          |
|             `@Min`             | 길이가 아닌 숫자의 크기를 검사한다.<br /> `최소값 검증`. <br />null은 허용하므로 @NotNull을 같이 사용해야 한다. | BigDecimal, BigInteger, byte, <br />short, int, long <br />및 관련 Wrapper 타입 |
|             `@Max`             | 길이가 아닌 숫자의 `크기`를 검사한다.<br /> `최댓값 검증`. <br />null은 허용하므로 @NotNull을 같이 사용해야 한다. | BigDecimal, BigInteger, byte, <br />short, int, long <br />및 관련 Wrapper 타입 |
| `@AssertTrue` , `@AssertFalse` | 값이 true인지 또는 false인지 검사한다. <br />null은 유효하다고 판단한다. | boolean Boolean                                              |
|  `@DecimalMax`, `@DecimalMin`  | 지정한 값보다 작거나 같은지 또는 크거나 같은지 검사한다.<br />inclusive가 false면 value로 지정한 값은 포함하지 않는다. <br />null은 유효하다고 판단한다. | BigDecimal, BigInteger, <br />CharSequence, byte, short, int, long <br />및 관련 Wrapper 타입 |
|           `@Digits`            | 자릿수가 지정한 크기를 넘지 않는지 검사한다. <br /> null은 유효하다고 판단한다. | BigDecimal, BigInteger, CharSequencebyte,<br /> short, int, long <br />및 관련 Wrapper 타입 |
|           `@Pattern`           | 값이 정규표현식에 일치하는지 검사한다. <br />null은 유효하다고 판단한다. | CharSequence                                                 |
|          `@Positive`           | 양수인지 검사. OrZero가 붙은 것은 0 또는 양수인지 검사한다.<br />null은 유효하다고 판단한다. | BigDecimal BigInteger <br />byte, short, int, long<br /> 및 관련 Wrapper 타입 |
|       `@PositiveOrZero `       | 0 또는 양수인지 검사한다. <br />null은 유효하다고 판단한다.  | BigDecimal, BigInteger, <br /> byte, short, int, long, float, double, <br />및 관련 Wrapper 타입 |
|          `@Negative`           |     음수인지 검사한다. <br />null은 유효하다고 판단한다.     | BigDecimal BigInteger byte, short, int, long <br />및 관련 Wrapper 타입 |
|       `@NegativeOrZero `       | 0 또는 음수인지 검사한다. <br />null은 유효하다고 판단한다.  | BigDecimal BigInteger byte, short, int, long <br />및 관련 Wrapper 타입 |
|           `@Future`            | 해당 시간이 미래 시간인지 검사한다. <br />null은 유효하다고 판단한다. | 시간 관련 타입                                               |
|       `@FutureOrPresent`       | 현재 또는 미래 시간인지 검사한다. <br /> null은 유효하다고 판단한다. | 시간 관련 타입 <br />Date, Calendar, Instant, <br />LocalDate, LocalDateTime, LocalTime, <br />MonthDay, OffsetDateTime, OffsetTime, <br />Year, YearMonth, ZonedDateTime, HijrahDate,<br /> JapaneseDate, MinguoDate, ThaiBuddhistDate |
|            `@Past`             | 해당 시간이 과거 시간인지 검사한다. <br /> null은 유효하다고 판단한다. | 시간 관련 타입                                               |
|        `@PastOrPresent`        | 현재 또는 과거 시간인지 검사한다.<br /> null은 유효하다고 판단한다. | 시간 관련 타입                                               |



## org.hibernate.validator 라이브러리의 org.hibernate.validator.constratins 패키지 



| 애노테이션                                       | 설명                                                         |
| :----------------------------------------------- | :----------------------------------------------------------- |
| `@URL(protocol=, host=, port=, regexp=, flags=)` | 주석이 달린 `문자열이 URL인지 확인` <br />매개변수 protocol, host, port는 URL의 해당 부분과 매칭.<br/>추가 정규식은 regexp와 flags를 사용하여 지정할 수 있다.<br/>기본적으로 이 제약 조건에 대한 유효성 검증은 java.net.URL 생성자를 사용하여 문자열의 유효성을 검증.<br/>이는 일치하는 프로토콜 처리기를 사용할 수 있어야 함을 의미. |
| `@UniqueElements`                                | 주석이 달린 Collection의 모든 객체가 unique한지 검사         |
| `@Range(min=, max=)`                             | 주석이 달린 요소의 `범위`가 min, max 내에 있는지 검사 <br />숫자 값 또는 숫자 값의 문자열 표현에 적용 |
| `@Length(min=, max=)`                            | 문자열의 길이가 min과 max 사이인지 검사 <br/>min의 default 값은 0, max의 default 값은 2147483647 |
| `@ISBN(type=) `                                  | 주석이 달린 CharSequence가 유효한 ISBN인지 검사.<br/>숫자의 길이와 숫자가 모두 확인.<br/>`null은 유효한 것으로 간주`.<br/>유효성 검사 중에 ISBN 문자가 아닌 것들은 무시된다.<br/>모든 숫자와 X가 유효한 ISBN 문자로 간주된다<br/>이는 숫자를 구분하는 대시가 있는 ISBN을 확인할 때 유용하다. (ex. 978-161-729-045-9)<br/>type은 ISBN의 길이를 정의.<br/>유효한 길이는 10과 13으로 각각 ISBN_10과 ISBN_13으로 표사.<br/>type의 default 값은 ISBN_13. |

* @Length는 Hibernate Validation 어노테이션이며 @Size와 같은 의미이다.
* @Size가 JPA와 Hinernate로부터 독립적인 bean을 만들어준다. 결과적으로 @Size는 @Length보다 더 가볍다.



### @NotNull vs @NonNull vs @Column(nullable=false)

`@NotNull`과 `@NonNull` 둘다 어노테이션 방식이지만 @NonNull은 validator의 validation이 아니라
 `lombok에서의 entity에 대한 제약조건 검증 어노테이션`이다. 

값이 null이 들어가면 NullPointException이 일어나며 커스텀 예외로 처리할 수 없다. 

값의 null을 처리하고 핸들링 하고 싶다면 `@NotNull`을 사용하고 혹시나 엔티티에 null이 들어가는 것 자체를 막기 위해서는 `@NonNull`을 사용하면 될 것 같다.

엔티티 필드에 붙은 `@NotNull`은 @Valid 어노테이션과 별개로 JPA가 읽어 동작하기 때문에 ConstraintViolaitionExcpeiton이 발생한다.

@Column(nullable=false)는 db에 들어갈 때 null이면 hibernate쪽에서 오류를 발생시키는것.

엔티티 -> column nullable (쿼리실행시 예외처리) / 
@NotNull(JPA엔티티 필드값이 null로 채워질 때 예외처리) / 
엔티티가 아닌 값은 @Valid(jakarta validation)의 NotNull 또는 lombok의 NonNull 

 

## Validation Tip - by Plan B



사용자의 입력값에는 validation rule이 상세하게 정의되는 것이 좋다



### validation을 하는 이유



#### 악의적 사용자가 서비스를 해하는 것을 방지

* 게시글의 내용을 매우 길게(GB 단위) 설정
* 매우 많은양의 고해상도 이미지를 업로드
  

#### 사용자의 input이 오류(500 error)를 일으키는 것을 방지

* 닉네임 란에 emoji를 넣어서, DB에 설정된 인코딩이 처리를 못하는 경우
* 생년월일 란에 2099년 99월 99일을 넣어서, 비즈니스 로직에서 에러가 생기는 경우
* 생년월일 각각을 int로 저장하는 것, 혹은 2099-99-99 문자열로 insert는 성공하겠지만
* 이 내용을 가지고 Date 객체를 만들거나 할 때 Exception이 발생할 수 있음
  

#### 디지털 서비스에 익숙하지 않은 사용자가 실수하는 것을 방지

* 이름 란에 음절 외의 문자 가 들어가는 것을 방어하지 않는다면
* 조민규 를 조민ㄴ규 로 오타를 내고도 지나칠 수 있음



#### 데이터 일관성 확보

* 전화번호 란에 validation이 없다면
* 01012345678 을 입력하는 사람도 있을 것이고
* 12345678 을 입력하는 사람도 있을 것이고
* 010-1234-5678 을 입력하는 사람도 있을 것이고
* +821012345678 을 입력하는 사람도 있을 것



#### validation은 프론트엔드만의 일이 아니다



### 1. 비즈니스의 RULE에 따라 DB column type을 근거있게 정하자.

SNS 게시글 최대 길이를 400글자로 정했다면 - varchar(400)

validation rule이 없어서 TEXT 타입을 쓰는 것보다 훨씬 절약할 수 있게 됨



### 2. rule을 유추할 수 있고 그것이 명확하더라도 기획에 없다면 언급하여 정하자 

* API는 validation 걸고 있는데 프론트는 안 해서 사용자가 알 수 없는 에러를 겪을 수 있으므로 미리 정해놓자.
* max를 지정하는 것이 가장 중요하다 - 제한 없는 데이터는 어떤 타입이던 위험함
* 모든 것을 방어할 수는 없다 - 최대한 막고 서로에게 책임을 묻지는 말자.
* Duplicate가 발생하는 케이스를 고려하자 - 



### 3. 문자열 Validation

최소/최대 길이 를 정의하자 (e.g. SNS 서비스에서 상태 메시지 1~150글자)

* RFC, ISO 등의 표준에 맞추자
  * 표준을 기준으로 구현된 라이브러리가 많기 때문
  * regex를 직접 정의하는 것보다 더 빠르게 정답에 도달할 수 있음(개발하기 편함)
  * 참고 : spring boot validation의 @Email 어노테이션
* 표준이 없다면 regex로 정의하자
  * 차량번호( \d{2,3}[가-힇]\d{4} )
  * regex가 요구사항에 맞게 동작하는지 검색이나 테스트를 꼭 해봐야 한다. 
* enum을 줘서 값을 입력하는 게 아니라, 선택하게 만들자
* 사용 가능한 문자 범위
  * UTF-8로 제한 하는 식으로 encoding 개념에서 생각하는 것 어떠냐고 제안할 수 있음



### 4. 숫자 Validation

* 최소, 최대값 을 정해놓자 (e.g. 주문수량 1~99)
* 실수의 경우 소수점 몇 번째 자리까지 인정할 것인지 제한을 초과한 부분은 반올림할 것인지, 버릴 것인지 정해놓자. 



### 5. 멀티미디어 (이미지, 동영상) 등에는 용량 및 개수 제한을 걸어두자

* 그렇지 않으면 8K 사진을 100개씩 올린다거나 할수도 있다

* 사례
  * 카카오톡은 한 번에 최대 30개의 이미지를 보낼 수 있음
    페이스북은 이미지의 해상도를 클라이언트에서 낮춰 업로드함



## 참조

* https://w97ww.tistory.com/102

* https://www.baeldung.com/spring-service-layer-validation

* https://tecoble.techcourse.co.kr/post/2021-06-21-custom-annotation/

* https://kdhyo98.tistory.com/m/80
* https://meetup.nhncloud.com/posts/223

* https://www.baeldung.com/spring-boot-bean-validation

* https://velog.io/@idean3885/Dto-Entity-Validation-%EC%B2%98%EB%A6%AC

* https://kapentaz.github.io/spring/Spring-Boo-Bean-Validation-%EC%A0%9C%EB%8C%80%EB%A1%9C-%EC%95%8C%EA%B3%A0-%EC%93%B0%EC%9E%90/#