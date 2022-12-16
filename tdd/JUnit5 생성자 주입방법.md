# JUnit5 생성자 주입 방법과 원리 



앞서 정리한 글 중에서, main 환경이랑 test 환경이랑 환경 자체가 달라서 생성자 주입이 안된다고 하였다.

그래서 대부분 @Autowired로 주입하거나, @BeforeEach로 테스트 시작 전에 직접 주입을 시켜주는 방식을 사용했다.



하지만 스프링 5.2.x 버전 부터 어노테이션을 통한 `생성자 주입`이 가능해졌다.

(또 어노테이션이야?)

그런데 @Autowired 처럼 필드 하나하나마다 어노테이션을 선언하는 것이 아닌 1번만 선언해서 생성자 주입이 가능하다.



바로 `@TestConstructor(autowireMode = TestConstructor.AutowiredMode.ALL)` 을 사용하면 된다

* spring.properties 파일을 통한 @Autowired 어노테이션 없이 주입도 가능하다. 



## @TestConstructor



JUnit 5  스프링 5.2.x 버전 부터 생성자를 통한 의존관계 주입이 가능해졌다.
`AutowireMode.ALL`설정을 통해 `@Autowired`애노테이션을 명시하지 않고, `private final`로 선언된 필드들에 의존관계 주입이 가능하다.



테스트 클래스에 @TestConstructor를 선언해준다.



```java
@RequiredArgsConstructor
@SpringBootTest
@TestConstructor(autowireMode = AutowireMode.ALL)
class ConstructorDiTest {

    private final MemberRepository memberRepository;
		
 		private final MemberService memberService;
  
    ...
}

```



기본 정책은, 선언하지 않을 시에는 생성자 주입을 하지 않고, 생성자 주입 시도를 하면 `ParameterResolutionException`  예외를 던진다 







## 원리가 무엇일까?

@ExtendedWith(SpringExtension.class)를 무조건 명시해주어야 어플리케이션 컨텍스트가 로딩된다.

그래서 Spring Boot 2.0.x (Spring 5.0.x) 버전 떄까지 다음과 같이 사용해야만 했다.

```java
@ExtendedWith(SpringExtension.class)
@SpringBootTest
class ConstructorDiTest {
		
  	@Autowired
    private MemberRepository memberRepository;
		
  	@Autowired
 		private MemberService memberService;
  
    ...
}
```



Spring Boot Test 2.1.x 부터@SpringBootTest 어노테이션에 @ExtendedWith(SpringExtension.class)가 포함되어서

@ExtendedWith(SpringExtension.class) 이 없이 사용할 수 있게 되었다. 

어플리케이션 컨텍스트를 로딩하는 다른 테스트 [@WebMvcTest](https://github.com/spring-projects/spring-boot/blob/2.1.x/spring-boot-project/spring-boot-test-autoconfigure/src/main/java/org/springframework/boot/test/autoconfigure/web/servlet/WebMvcTest.java#L80), [@DataJpaTest](https://github.com/spring-projects/spring-boot/blob/2.1.x/spring-boot-project/spring-boot-test-autoconfigure/src/main/java/org/springframework/boot/test/autoconfigure/orm/jpa/DataJpaTest.java#L75), [@JsonTest](https://github.com/spring-projects/spring-boot/blob/2.1.x/spring-boot-project/spring-boot-test-autoconfigure/src/main/java/org/springframework/boot/test/autoconfigure/json/JsonTest.java#L68) 등등과 같은 Slice Test 류에도 동일하게 적용되었다.



@SpringBootTest를 붙이면, @ExtendedWith(SpringExtension.class)로 인해 SpringExtension 클래스가 테스트를 도와주는데,

이 SpringExtension 클래스가 ParameterResolver 인터페이스를 구현하였고, 결국 SpringExtension 클래스가 적합한 생성자 매개변수에 의존성을 주입해주는 것이다.

> 기존에는 Jupiter가 생성자 주입을 요구하는경우, 생성자 매개변수를 처리할 **ParameterResolver**을 찾지만, 적합한 ParameterResolver가 없어서 @Autowired가 아닌 경우에는 예외를 던져야만 했다. 

이 ParameterResolver 인터페이스가 하는 역할은 supportsParameter 메소드로 파라미터 주입이 지원 가능한지 확인하고,

지원 가능하면 resolveParameter 메소드로 매개변수를 주입한다.



```java
// SpringExtension.class의 supportsParameter 메소드
@Override
public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
    Parameter parameter = parameterContext.getParameter();
    Executable executable = parameter.getDeclaringExecutable();
    Class<?> testClass = extensionContext.getRequiredTestClass();
    return (TestConstructorUtils.isAutowirableConstructor(executable, testClass) ||
        ApplicationContext.class.isAssignableFrom(parameter.getType()) ||
        ParameterResolutionDelegate.isAutowirable(parameter, parameterContext.getIndex()));
}
```

자세히 보면, TestConstructorUtils.isAutowirableConstructor(executable, testClass) 메소드가 있다.

* 이 메소드의 역할은 주어진 테스트 클래스에 대해  autowirable 생성자인지 확인한다.

```java
// TestConstructorUtils 클래스의 isAutowirableConstructor메소드

public static boolean isAutowirableConstructor(Executable executable, Class<?> testClass,
			@Nullable PropertyProvider fallbackPropertyProvider) {

		return (executable instanceof Constructor &&
				isAutowirableConstructor((Constructor<?>) executable, testClass, fallbackPropertyProvider));
	}
```

* isAutowirableConstructor 메소드를 호출하여 확인한다.
  * 이름만봐도 Autowired를 사용했는지 확인하는 메서드인거같다



```java
// TestConstructorUtils 클래스의 isAutowirableConstructor메소드

public static boolean isAutowirableConstructor(Constructor<?> constructor, Class<?> testClass,
			@Nullable PropertyProvider fallbackPropertyProvider) {

		// Is the constructor annotated with @Autowired?
		if (AnnotatedElementUtils.hasAnnotation(constructor, Autowired.class)) {
			return true;
		}

		AutowireMode autowireMode = null;

		// Is the test class annotated with @TestConstructor?
		TestConstructor testConstructor = TestContextAnnotationUtils.findMergedAnnotation(testClass, TestConstructor.class);
		if (testConstructor != null) {
			autowireMode = testConstructor.autowireMode();
		}
		else {
			// Custom global default from SpringProperties?
			String value = SpringProperties.getProperty(TestConstructor.TEST_CONSTRUCTOR_AUTOWIRE_MODE_PROPERTY_NAME);
			autowireMode = AutowireMode.from(value);

			// Use fallback provider?
			if (autowireMode == null && fallbackPropertyProvider != null) {
				value = fallbackPropertyProvider.get(TestConstructor.TEST_CONSTRUCTOR_AUTOWIRE_MODE_PROPERTY_NAME);
				autowireMode = AutowireMode.from(value);
			}
		}

		return (autowireMode == AutowireMode.ALL);
	}
```

이 메소드가 핵심이다.

이 메소드는, 테스트 클래스를 받아서 테스트 클래스가 @TestConstructor 어노테이션을 사용했고, AutowireMode.ALL 이라면 true를 반환한다. 





이 메소드의 순서를 보면 다음과 같다

- true는 AutowirableConstructor 이란것이다.



1. @Autowired 어노테이션이 달려있으면 true를 반환한다. - 아니면 다음 코드로 그냥 넘어간다. 
2. autowireMode 변수를 선언하고, null로 초기화한다. - AutowireMode인지 확인하기 위한 변수이다. 
3. `TestClass`가 @TestConstructor 어노테이션이 달려있는지 확인한다.
   1. 달려있으면 해당 어노테이션의 autowireMode 프로퍼티를 autowireMode 변수에 할당한다.
   2. 달려있지 않으면 아래 분기를 탄다.
      1. 클래스 패스에 있는 spring.properties 파일에서 spring.test.constructor.autowire.mode 프로퍼티를 가져온다.
      2. 프로퍼티를 AutowireMode enum으로 변환해보고 성공하면 autowireMode 변수에 할당한다.
4. autowireMode 변수가 AutowireMode.ALL과 같으면 true, 다르면 false를 반환한다.



즉 true를 반환하면 @TestConstructor 어노테이션을 사용했고, AutowireMode.ALL  인것이다.



그렇게 해서 ExecutableInvoker 클래스를 통해서 생성자 주입을 해주는 것이다.



## 정리



SpringExtension이 ParemterResolver를 구현해서, 해당 테스트 클래스가 @TestConstructor 어노테이션을 사용했고, AutowireMode.ALL 이라면 @Autowired 없이 생성자 주입을 해준다. 

* 물론 주입 받을 클래스들은 Bean으로 등록되어있어야 한다.



#### spring.properties 파일을 통한 @Autowired 어노테이션 생략도 가능하다. 

* 참조를 확인하자. 



### 참조

* https://perfectacle.github.io/2020/12/25/dependency-injection-in-junit/