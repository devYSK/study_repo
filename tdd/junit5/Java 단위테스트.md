# Java의 단위 테스트(Unit Test)

## 테스트 코드를 작성하는 이유

개발 단계에서 테스트 코드를 작성하는 이유는 정말 다양하다.

* 개발 과정에서 미리 문제를 발견할 수 있다.
    * 코드에 잠재되어있떤 문제를 발견하는데 큰 도움이 된다.
* 리팩토링의 리스크가 줄어든다.
    * 서비스 업데이트를 하거나 요구사항이 변경되면 코드가 추거되거나 수정되는데, 그 코드에 연관된 다른 코드에 까지 영향을 준다.
    * 코드를 수정하는 과정에서의 버그들이나 잘못된 점을 빠르게 잡을 수 있다.
* 애플리케이션을 가동해서 직접 테스트하는 것보다 테스트를 빠르게 진행할 수 있다.
    * 테스트 코드를 공유함으로써 이 테스트와 이 기능이 무엇을 하는지 알 수 있다.
* 하나의 명세 문서로서의 기능을 수행한다
* 몇 가지 프레임워크에 맞춰 테스트 코드를 작성하면 좋은 코드를 생산할 수 있다.
* 코드가 작성된 목적을 명확하게 표현할 수 있으며, 불필요한 내용이 추가되는 것을 방지한다.





## Java의 단위테스트의 패턴

단위 테스트의 추세는 주로 한개의 단위 테스트를 3가지로 나눠서 처리하는,

`given - when - then` 패턴을 사용한다고 한다.

* given(준비) : 어떤 데이터를 준비한다.
* when(실행) : 어떤 기능 혹은 메서드를 실행한다 ( 조건을 지정하여 실행한다.)
* then(검증) : 어떠한 결과가 나오면 그 결과를 검증한다
* verify : 메서드가 호출된 횟수, 타임아웃 시간 등을 검사 할떄 사용한다 

```java
@Test
void test() {
  //given
  ...
    
  //when
  ....
    
  //then
  .....
}
```



## JUnit5

자바언어에서 사용되는 대표적인 테스트 프레임워크로서 단위 테스트를 위한 도구를 제공한다.

단위 테스트 뿐만이 아닌 통합 테스트를 기능도 제공한다.

* JUnit5의 구성

![img](https://github.com/devYSK/java-application-test-Various-ways/raw/master/img/2020-12-20-23-55-43.png)

* [JUnit Docs](https://junit.org/junit5/docs/current/user-guide/)

JUnit5는 크게 3가지로 구성되어 있다.

### Junit Platform

JVM에서 테스트를 시작하기 위한 뼈대 역할.

테스트를 실행해주는 런쳐를 제공한다.

테스트 엔진 API를 제공한다 - 인터페이스 형태로

* Test Engine : 테스트를 발견하고 테스트 계획을 생성한다
    * 테스트를 발견하고 테스트를 수행하며 그결과를 보고한다
    * 각종 IDE와의 연동을 보조하는 역할 수행(IDE 콘솔 출력 등)
* PlatForm에는 TestEngine API, Console Launcher, JUnit 4 Based Runner등이 포함되어 있다.

### JUnit Jupiter

Junit Platform에서 제공하는 Test Engine API 인터페이스를 구현한 구현체를 포함하고 있다.

테스트의 실제 구현체는 별도 모듈의 역할을 수행하는데 그중 하나가 Jupiter Engine이다

Jupter Engine은 Juptier API를 활용해서 작성한 테스트 코드를 발견하고 실행하는 역할을 수행한다.

### JUnit Vintage

Junit 3, 4 에 대한 테스트 엔진 API를 구현하며, 3, 4 를 지원한다.

기존에 작성된 3, 4 버전에 테스트 코드를 실행할 때 사용된다.

# JUnit 5 라이브러리 의존성 추가

* Build.gradle에 다음과 같이 추가

#### Spring Boot 2.2 버전 이상의 경우

```groovy
testImplementation("org.springframework.boot:spring-boot-starter-test")
 
test {
    useJUnitPlatform()
}
```

#### Spring Boot 2.1 버전 이하의 경우

```gro
// junit5
testImplementation("org.springframework.boot:spring-boot-starter-test") {
 	exclude module : 'junit'
}

testImplementation("org.junit.jupiter:junit-jupiter-api")
testCompile("org.junit.jupiter:junit-jupiter-params")
testRuntime("org.junit.jupiter:junit-jupiter-engine")

test {
	useJUnitPlatform()
}
```

```
org.springframework.boot:spring-boot-starter-test 의존성 추가시
SpringBoot 2.1 이하의 버전은 Junit4를 기본으로 사용
SpringBoot 2.2 이상의 버전은 Junit5를 기본으로 사용
```

* 2.2 버전 이상의 스프링 부트 프로젝트를 만든다면 기본으로 JUni5 의존성이 추가된다.

### spring-boot-starter-test 라이브러리

JUnit, Mockito, assertJ 등의 다양한 테스트 도구를 제공한다.

대표적인 라이브러리는 다음과 같다

* JUnit 5 : 자바 애플리케이션의 단위 테스트 지원
* Spring Test & Spring Boot Test : 스프링 부트 애플리케이션에 대한 유틸리와 통합 테스트를 지원한다
* AssertJ : 다양한 단정문(assert)을 지원하는 라이브러리
* Hamcrest: Matcher를 지원하는 라이브러리
* Mockto : 자바 Mock 객체를 지원하는 프레임워크
* JSONassert: JSON용 단정문 라이브러리
* JsonPath : JSON용 XPath를 지원한다.



## JUnit의 기본 어노테이션

기본적으로 테스트는 테스트 메서드에 `@Test` 어노테이션을 붙이면 실행 가능한 상태가 되어

독립적으로 실행한다.

JUnit 5부터는 테스트 메서드에 public 키워드를 붙이지 않고 실행할 수 있다.



### 기본 어노테이션

* @Test : 테스트 코드를 포함한 테스트 메서드를 정의한다.
* @BeforeAll : 테스트를 시작하기 전에 호출되는 메서드를 정의한다.
  * 테스트 실행전 딱 한번 실행, 반드시 static void를 사용하여 정의한다
* @BeforeEach :  각 테스트 메서드가 실행되기 전에 동작하는 메서드를 정의한다
  * static일 필요는 없다
* @AfterAll : 각 테스트가 실행된 이후에 호출되는 메서드
  * 반드시 static void를 사용하여 정의한다
* @AfterEach : 각 테스트 메서드가 종료되면서 호출되는 메서드를 정의한다 
* @Disabled : 특정 테스트를 무시한다. 실행하고 싶지 않은 메서드에 사용한다
  * @Ignore 어노테이션이랑 비슷한 동작을 한다.





## 테스트 이름 표시하기

* @DisplayNameGeneration
  * Method와 Class 레퍼런스를 사용해서 테스트 이름을 표기하는 방법을 설정한다
  * `Test Results` 의 클래스 밑에 메서드 이름들이 옵션에 따라 바뀐다. 
  * 클래스를 레퍼런스 하면 클래스 내 모든 테스트 메서드에 적용된다
  * 기본 제공체로 ReplaceUnderscores를 제공하며, 이 제공체는 언더스코어를 다 스페이스바(빈공백)으로 바꾸어 준다



* @DisplayName
  * 어떤 테스트 인지 테스트 이름을 보다 쉽게 표현해줘서 Test Reulsts 에서 더 쉽게 확인이 가능하다.
  * @DisplayNameGeneration 보다 우선순위가 높다



* [참고 레퍼런스 문서](https://junit.org/junit5/docs/current/user-guide/#writing-tests-display-names)



### JUnit 5 Assertion

실제 테스트에서 검증하고자 할떄 사용하는 기능

- 패키지 org.junit.jupiter.api.Assertions

| 메서드 명                              | 기능                                                         |
| -------------------------------------- | ------------------------------------------------------------ |
| assertEqulas(expected, actual)         | 실제 값이 기대한 값과 같은지 확인                            |
| assertNotNull(actual)                  | 값이 null이 아닌지 확인                                      |
| assertTrue(boolean)                    | 다음 조건이 참(true)인지 확인                                |
| assertAll(executables...)              | 모든 확인 구문 확인                                          |
| assertThrows(expectedType, executable) | 예외 발생 확인. 어떤 예외가 발생하는지 비교, 파라미터를 리턴 받아서 어떤 예외인지 알 수 있다. |
| assertTimeout(duration, executable)    | 특정 시간 안에 실행이 완료되는지 확인                        |

- 메서드 마지막 매개변수로 메시지를 줄 수 있는데,
  - Supplier < String > 타입의 인스턴스를 람다 형태로 제공할 수 있다
- 첫번째 assert 깨지면 다음 assert는 실행 되지 않는다.
- assertAll을 사용하면 모든 테스트가 실행되게 할 수 있다.
  - [![img](https://github.com/devYSK/java-application-test-Various-ways/raw/master/img/2020-12-21-01-08-25.png)](https://github.com/devYSK/java-application-test-Various-ways/blob/master/img/2020-12-21-01-08-25.png)
- assertTimeOut [![img](https://github.com/devYSK/java-application-test-Various-ways/raw/master/img/2020-12-21-01-12-30.png)](https://github.com/devYSK/java-application-test-Various-ways/blob/master/img/2020-12-21-01-12-30.png)
- AssertJ, Hemcrest, Truth 등의 라이브러리를 사용할 수도 있다
  - https://joel-costigliola.github.io/assertj/
  - https://hamcrest.org/JavaHamcrest/javadoc/
  - https://truth.dev/

### JUnit 5 조건에 따라 테스트 실행하기

특정한 조건을 만족하는 경우에 테스트를 실행하는 방법.

- #### org.junit.jupiter.api.Assumptions.*

- assumeTrue(조건)

- assumingThat(조건, 테스트)

특정한 조건을 만족하는 경우에만 테스트를 실행한다

```java
class TestClass {
		@Test
    @EnabledOnOs({OS.MAC, OS.WINDOWS})
    void EnabledOnOs() {
        System.out.println("운영체제에 따라서 실행...");
    }

    @Test
    @EnabledOnJre({JRE.JAVA_10, JRE.JAVA_11})
    void EnabledOnJre() {
        System.out.println("JRE 버전에 따라서 실행...");
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "local", matches = "local")
    void EnabledOnIf() {
        System.out.println("Env 에따라서 실행...");
}
```

- @Enabled___ 와 @Disabled___
  - OnOS
  - OnJre
  - IfSystemProperty
  - IfEnvironmentVariable
  - If



### JUnit 5 태깅과 필터링

테스트 태깅 : 여러 테스트들을 테스트 그룹을 만들고 원하는 테스트 그룹만 테스트를 실행할 수 있는 기능.

- @Tag
  - 테스트 메소드에 태그를 추가할 수 있다.
  - 하나의 테스트 메소드에 여러 태그를 사용할 수 있다

인텔리J에서 특정 태그로 테스트 필터링 하는 방법 특정 테스트 Edit Configuration -> Test kind 를 Tags로 수정 -> Tag expression 에 태그명 입력

메이븐에서 테스트 필터링 하는 방법

```
<plugin>
    <artifactId>maven-surefire-plugin</artifactId>
    <configuration>
        <groups>fast | slow</groups>
    </configuration>
</plugin>
```

-> fast나 slow 태그가 붙은 테스트만 실행

- https://junit.org/junit5/docs/current/user-guide/#running-tests-tag-expressions

### JUnit 5 커스텀 태그

JUnit 5 애노테이션을 조합하여 커스텀 태그를 만들 수 있다.

```
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Test
@Tag("fast")
public @interface FastTest {...}// 우리가 새로 만든 어노테이션 
```

------

```
@Test
@DisplayName("fast 그룹 테스트 1")
@Tag("fast")
void test_fast1() {...}
```

#### 위를 다음과 같이 변경이 가능하다.

```
@FastTest
@DisplayName("fast 그룹 테스트 1")
void custom_test_fast1() {...}
```

------

### JUnit 5 테스트 반복하기 1부

반복해서 테스트를 하고 싶을 때 사용

- @RepeatedTest

- 반복 횟수와 반복 테스트 이름을 설정할 수 있다.

  - @RepeatedTest(value, name ) 옵션들

    - {displayName}
    - {currentRepetition}
    - {totalRepetitions}

  - RepetitionInfo 타입의 인자를 받을 수 있다.

  - ```
    @RepeatedTest(10)
    void repeatTest(RepetitionInfo repetitionInfo) {
        System.out.println("반복 테스트 " +
                repetitionInfo.getCurrentRepetition() + " \n" + // 현재 반복 횟수 
                repetitionInfo.getTotalRepetitions()); // 총 반복 횟수(10)
    }
    ```

  - 어노테이션의 name속성에 다음과 같이 이름도 줄 수 있다

  - ```
    @DisplayName("반복 테스트 ")
    @RepeatedTest(value = 10, name = "{displayName}, ! {currentRepetition} / {totalRepetition}")
    void repeatTest(RepetitionInfo repetitionInfo) {
        System.out.println("반복 테스트 " +
                repetitionInfo.getCurrentRepetition() + " \n" +
                repetitionInfo.getTotalRepetitions());
    }
    ```

    ![img](https://github.com/devYSK/java-application-test-Various-ways/raw/master/img/2020-12-21-19-15-02.png)

------

- @ParameterizedTest

- 테스트에 여러 다른 매개변수를 대입해가며 반복 실행한다.

  - @ParameterizedTest(name = "")의 옵션들

    - {displayName}
    - {index}
    - {arguments}
    - {0}, {1}, ...

  - ```
    @ParameterizedTest
    @ValueSource(strings = {"날씨가", "많이", "추워지고", "있습니다."})
    void parameterizedTest(String message) {
        System.out.println(message); // 날씨가 \n 많이 \n 추워지고 \n 있습니다. \n
    }
    ```

    - ValueSource의 인자를 하나씩 루프를 돌며 출력

### JUnit 5 테스트 반복하기 2부

#### 인자 값들의 소스

- 인자 값은 객체로도 받을 수 있다.(ex Study, User, UserDto 등)
- 인자 값(위 예제에서는 String message)에 어노테이션 이름에 맞는 값을 `인자`에 넣어줌
  - Null 이면 Null, Empty면 `" "(빈값)`
- @ValueSource
- @NullSource, @EmptySource, @NullAndEmptySource
- @EnumSource
- @MethodSource
- @CvsSource
- @CvsFileSource
- @ArgumentSource

#### 인자 값 타입 변환

- 암묵적인 타입 변환
  - 레퍼런스 참고
- 명시적인 타입 변환
  - SimpleArgumentConverter 상속 받은 구현체 제공
  - @ConvertWith

```
static class StudyConverter extends SimpleArgumentConverter {
    @Override
    protected Object convert(Object o, Class<?> aClass) throws ArgumentConversionException {
        assertEquals(Study.class, aClass, "Can only convert to Study");
        return new Study(Integer.parseInt(o.toString()));
    }


    @DisplayName("컨버터 테스트")
    @ParameterizedTest(name = "{index} {displayName} message = {0}")
    @ValueSource(ints = {10, 20, 40})
    void parameterizedTest2(@ConvertWith(StudyConverter.class) Study study) {
        System.out.println(study.getLimit());
    }
}
```

SimpleArgumentConverter를 상속받은 컨버터 클래스를 정의하고
@ConvertWith 어노테이션과 함께 사용할 수 있다.

- ArgumentConverter는 하나의 인자값에만 사용 가능!
- 여러개의 인자값을 사용할려면 `ArgmentsAccessor`를 사용해야 한다

\####인자 값 조합

- ArgumentsAccessor
- 커스텀 Accessor
  - [ArgumentsAggregator 인터페이스 구현](https://github.com/devYSK/java-application-test-Various-ways#Custom한-Aggregator를-만들면-된다)
  - @AggregateWith

```
@DisplayName("컨버터 테스트")
@ParameterizedTest(name = "{index} {displayName} message = {0}")
@CsvSource({"10, '자바 스터디'", "20, 스프링"}) // '' 공백을 포함한 문자열을 넣을때 사용
void parameterizedTest3(ArgumentsAccessor argumentsAccessor) {
    Study study = new Study(argumentsAccessor.getInteger(0), argumentsAccessor.getString(1));
        System.out.println(study);
}
```

- ArgumentsAccessor로 부터 인자를 받아 new 인스턴스를 만들어주기도 귀찮다면 ?

- ##### Custom한 `Aggregator`를 만들면 된다. (ArgumentsAggregator 인터페이스를 구현 )

  - `@AggregateWith` 어노테이션을 사용해서 인자로 받는다

```
//Aggregator 인터페이스 구현 
static class StudyAggregator implements ArgumentsAggregator {
    @Override
    public Object aggregateArguments(ArgumentsAccessor argumentsAccessor,
                                     ParameterContext parameterContext)
            throws ArgumentsAggregationException {

        return new Study(argumentsAccessor.getInteger(0), argumentsAccessor.getString(1));
    }


    @DisplayName("컨버터 테스트")
    @ParameterizedTest(name = "{index} {displayName} message = {0}")
    @CsvSource({"10, '자바 스터디'", "20, 스프링"})
    // '' 공백을 포함한 문자열을 넣을때 사용
    void parameterizedTest4(@AggregateWith(StudyAggregator.class) Study study) {
        System.out.println(study);
    }

}
```

- ArgumentsAggregator를 구현한 클래스의 제약조건
  - 반드시 `static inner class` 이거나 `public class` 여야 한다.

#### 참고

- https://junit.org/junit5/docs/current/user-guide/#writing-tests-parameterized-tests





---

---

### JUnit 5 테스트 인스턴스

JUnit은 테스트 메소드 마다 테스트 인스턴스를 새로 만든다.

- 기본 전략.
- 테스트 메소드를 독립적으로 실행하여 예상치 못한 부작용을 방지하기 위함이다.
- 이 전략을 JUnit 5에서 변경할 수 있다.
  - 즉 테스트마다 `필드`를 공유하지 않는다.
  - 테스트 메서드마다 System.out.println(this) 를 찍어보면 인스턴스가 다르단 걸 알 수 있다.

#### `@TestInstance(Lifecycle.PER_CLASS)`

- 테스트 클래스당 인스턴스를 하나만 만들어 사용한다. -> 하나의 인스턴스를 공유한다
- 경우에 따라, 테스트 간에 공유하는 모든 상태를 @BeforeEach 또는 @AfterEach에서 초기화 할 필요가 있다.
- @BeforeAll과 @AfterAll을 인스턴스 메소드 또는 인터페이스에 정의한 default 메소드로 정의할 수도 있다.
  - 이걸 사용하면 static 키워드가 필요한 메소드에서 static이 필요 없게 된다.

### JUnit 5 테스트 순서

- 제대로 된 단위 테스트라면, 다른 테스트와 동시에 실행되더라도 다른 테스트 코드에 영향을 주지 않는다.
  - 서로간에 의존성이 없어야 한다. (다른 코드에 영향 x)
  - 그래서 순서가 상관이 없어야 한다.

실행할 테스트 메소드 특정한 순서에 의해 실행되지만
어떻게 그 순서를 정하는지는 분명히 하지 않는다.
(테스트 인스턴스를 테스트 마다 새로 만드는 것과 같은 이유)

경우에 따라, 특정 `순서대로` 테스트를 실행하고 싶을 때도 있다.
그 경우에는 테스트 메소드를 원하는 순서에 따라 실행하도록
@TestInstance(Lifecycle.PER_CLASS)와 함께 @TestMethodOrder를 사용할 수 있다. (`테스트 메서드의 실행 순서를 정하는것`)

- MethodOrderer 구현체를 설정한다.
- 기본 구현체
- Alphanumeric
- OrderAnnoation
- Random

```
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // <<<!!
class StudyTest {...

  @Order(2)
  @Test
  void test2() {}

  @Order(3)
  @Test
  void test3() {}

  @Order(1)
  @Test
  void test1() {}
}
```

1 -> 2 -> 3 순서로 실행되고

- 낮은 값이 높은 우선순위를 갖는다.
- 값이(순위) 같으면 둘중 아무거나 먼저 실행되는거 같다

### JUnit 5 junit-platform.properties

JUnit 설정 파일로, 클래스패스 루트 (src/test/resources/)에 넣어두면 적용된다.

- src/test/resources/junit-platform.properties

모든 테스트에 공용으로 사용하고 싶을 때 설정한다.

- 테스트 인스턴스 라이프사이클 설정 -> junit.jupiter.testinstance.lifecycle.default = per_class
- 확장팩 자동 감지 기능 -> junit.jupiter.extensions.autodetection.enabled = true
- @Disabled 무시하고 실행하기 (풀 패키지 경로) -> junit.jupiter.conditions.deactivate = org.junit.*DisabledCondition
- 테스트 이름 표기 전략 설정 -> junit.jupiter.displayname.generator.default =
  org.junit.jupiter.api.DisplayNameGenerator$ReplaceUnderscores
  - 언더스코어를 공백으로 바꿔주는것

### JUnit 5 확장 모델

JUnit 4의 확장 모델은 @RunWith(Runner), TestRule, MethodRule. JUnit 5의 확장 모델은 단 하나로 통합 JUnit4 에서 쓰던걸 5에서 못쓴다. `Extension.`

확장팩 등록 방법

- 선언적인 등록 @ExtendWith(클래스.class)
- 프로그래밍 등록 @RegisterExtension
- 자동 등록 자바 ServiceLoader 이용

확장팩 만드는 방법

- 테스트 실행 조건
- 테스트 인스턴스 팩토리
- 테스트 인스턴스 후-처리기
- 테스트 매개변수 리졸버
- 테스트 라이프사이클 콜백
- 예외 처리
- ...

참고

- https://junit.org/junit5/docs/current/user-guide/#extensions

확장팩 예제

```
public class FindSlowTestExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback {

    private static final long THRESHOLD = 1000L;

    @Override
    public void beforeTestExecution(ExtensionContext extensionContext) throws Exception {
        ExtensionContext.Store store = getStore(extensionContext);
        store.put("START_TIME", System.currentTimeMillis());
    }
    
    @Override
    public void afterTestExecution(ExtensionContext extensionContext) throws Exception {
        Method requiredTestMethod = extensionContext.getRequiredTestMethod();
        SlowTest annotation = requiredTestMethod.getAnnotation(SlowTest.class);

        String testMethodName = requiredTestMethod.getName();
        ExtensionContext.Store store = getStore(extensionContext);

        long start_time = store.remove("START_TIME", long.class);
        long duration = System.currentTimeMillis() - start_time;
        if (duration > THRESHOLD && annotation == null) {
            System.out.printf("Please consider mark method [%s] with @SlowTest.\n", testMethodName);
        }

    }
    
    private ExtensionContext.Store getStore(ExtensionContext extensionContext) {
        String testClassName = extensionContext.getRequiredTestClass().getName();
        String testMethodName = extensionContext.getRequiredTestMethod().getName();

        return extensionContext.getStore(ExtensionContext.Namespace.create(testClassName, testMethodName));
    }
}
```

실행하는데 1초 이상 걸리는 메서드들이 @SlowTest 라는 어노테이션이 붙어있지 않으면 어노테이션을 붙여줘야 한다는 메시지를 찍어주는 확장 클래스

- 코딩해서 생성자로 직접 시간을 넘겨주고 싶다면?

- @RegisterExtension 어노테이션을 이용해서 `static` 필드로 선언

  ```
    class StudyTest {
    @RegisterExtension
    static FindSlowTestExtension findSlowTestExtension = 
            new FindSlowTestExtension(1000L);
    ...
    }
  ```

- 자동으로 등록을 하고싶다면?

  - 테스트 설정파일(properties)에
    - `junit.jupiter.extensions.autodetection.enabled = true` 옵션 설정
  - `이 옵션보다 @RegisterExtension 어노테이션으로 명시적으로 설정해주는 것이 좋다.`











---

---

---



## JUnit에서의 Stub, Mock



## Mockito란?

Mockito는 개발자가 동작을 직접 제어할 수 있는 가짜(Mock) 객체를 지원하는 테스트 프레임워크.

Mock: 진짜 객체와 비슷하게 동작하지만 그동작을 직접 그 객체의 행동을 관리하는 객체.

Mockito: Mock 객체를 쉽게 만들고 관리하고 검증할 수 있는 방법을 제공한다. 테스트를 작성하는 자바 개발자 50%+ 사용하는 Mock 프레임워크.

단위 테스트에 고찰

- 참조 : https://martinfowler.com/bliki/UnitTest.html



 Mock Object를 만들어 주입시켜주는 방법을 지원해주는 라이브러리이다.

> Mockito는 과거에는 static메서드를 지원하지 않는 단점이 있어 PowerMock을 대안으로 사용했으나 Mockito 3.4.0부터는 static method도 지원한다.



### Mockito 시작하기

라이브러리 :

- org.mockto:mockto-core
- org.mokito:mockto-junit-jupiter



다음 세 가지만 알면 Mock을 활용한 테스트를 쉽게 작성할 수 있다.

- Mock을 `만드는` 방법
- Mock이 어떻게 `동작`해야 하는지 관리하는 방법
- Mock의 행동을 `검증`하는 방법

Mockito 레퍼런스

- https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html

### Mockito사용법

### 1. Mock 객체 만들기

Mockito에서 Mock object에 의존성을 주입하기 위해서는 크게 3가지의 어노테이션을 사용.

- `@Mock` : Mock 객체를 만들어 반환해주는 어노테이션
  - Mockto.mock() 메서드로도 생성이 가능하다.

- `@Spy`: Stub하지 않은 메소드들을 원본 메소드 그대로 사용하는 어노테이션
- `@InjectMocks`: @Mock 또는 @Spy로 생성된 가짜 객체를 자동으로 주입시켜주는 어노테이션





#### mock을 쓰기에 아주 적절한 경우

- 구현체는 없지만 interface만 알고있어서 interface를 이용해서 코드를 작성하는 경우.

> `@Mock`을 사용하지 않고 `mock()`메서드를 통해서도 mock객체를 만들 수 있다.
> example)
>
> ```java
> Hint hint = mock(Hint.class);
> ```



#### 구현체는 없고 구현하지 않아도 가짜로 객체를 만드는법 2가지

1. Mocito.mock() 메서드로 객체 생성
2. @Mock 어노테이션으로 객체 생성 

---

####  `Mockito.mock()` 메서드로 mock 객체를 만드는법

```
MemberService memberService = Mockito.mock(MemberService.class);
StudyRepository studyRepository = Mockito.mock(StudyRepository.class);
```

- MemberSerivce와 StudyRepository는 `구현체가 없는 interface`이다.
- 구현체가 없고 구현하지 않았음에도 불구하고 mock으로 만들어준다.



#### @Mock 어노테이션으로 만드는 방법

- 반드시 @ExtendWith 어노테이션이 있어야 작동!
- JUnit 5 extension으로 MockitoExtension을 사용해야 한다.( 클래스 선언 위에)
- 필드와 메서드 매개변수로 만들 수 있다

------

- 필드로 만들기

```
@ExtendWith(MockitoExtension.class)
class StudyServiceTest {

  @Mock MemberService memberService;
  @Mock StudyRepository studyRepository;
  ...
}
```

- 메서드 매개변수로 만들기

```
@ExtendWith(MockitoExtension.class)
class StudyServiceTest {
    @Test
    void createStudyService(@Mock MemberService memberService,
                            @Mock StudyRepository studyRepository) {
        StudyService studyService = new StudyService(memberService, studyRepository);
        assertNotNull(studyService);
      }
}
```





# 2. Stup

### Mock 객체 Stubbing

stub이란 토막,꽁초,남은부분,몽당연필.. 이라는 뜻으로 dummy객체가 마치 실제로 동작하는 것 처럼 보이도록 만들어놓은 것

- 즉 Mock 객체의 행동을 조작하는 것

모든 Mock 객체의 행동

- Null을 리턴한다. (Optional 타입은 Optional.empty 리턴. 즉 비어있는 옵셔널 객체)
- Primitive 타입은 기본 Primitive 값.
- 콜렉션은 비어있는 콜렉션.
- Void 메소드는 예외를 던지지 않고 아무런 일도 발생하지 않는다.

- 예제1

- memberService.findById(1L) member 객체를 나오게 할라면 다음과 같이 쓰면 된다.

  ```
  @Test
  void createNewStudy(@Mock MemberService memberService, 
  										@Mock StudyRepository studyRepository) {
  
        Member member = new Member();
        member.setId(1L);
        member.setEmail("youngsoo@naver.com");
  
        Optional<Member> byId = memberService.findById(1L);
        when(memberService.findById(1L))
                .thenReturn(Optional.of(member));
        Study study = new Study(10, "java");
  
        Optional<Member> findById = memberService.findById(1L);
        assertEquals("youngsoo@naver.com", findById.get().getEmail());
  
  }
  ```

  - 만약 2L, 3L 등 다른 member 객체를 나오게 한다면 오류가 난다.
    - 1L로만 member를 나오게 했기 때문
  - 즉 when().thenReturn은 mock 객체를 직접 핸들링한다
  - org.mockito.ArgumentMatchers.`any()`; 메서드를 사용하면
    항상 같은 객체가 나온다 (아무 값이나 상관 없다).

- 예외를 던지는 doThrow() org.mockito.Mockito.doThrow;

  - ```
    doThrow(new IllegalArgumentException())
            .when(memberService).validate();
        
    assertThrows(IllegalArgumentException.class, () -> {
        memberService.validate();
        });
    ```

- 순서대로 호출하기

- ```
  when(memberService.findById(any()))
                .thenReturn(Optional.of(member))
                .thenThrow(new RuntimeException())
                .thenReturn(Optional.empty());
  ```

  - 첫번째 호출 멤버
  - 두번째 호출 런타임 익셉션
  - 세번째 호출 빈 옵셔널 객체를 내보낸다



Mockito에서는 아래의 stub메서드들을 지원.
`doReturn()`: Mock 객체가 특정한 값을 반환해야 하는 경우 사용

```java
    doReturn(3).when(p).add(anyInt(), anyInt());
```

`doNothing()`: Mock 객체가 아무 것도 반환하지 않는 경우 사용(void)

```java
@Test
public void example(){
    Person p = mock(Person.class);
    doNothing().when(p).setAge(anyInt());
    p.setAge(20);
    verify(p).setAge(anyInt());
}
```

`doThrow()`: Mock 객체가 예외를 발생시키는 경우 사용

```java
@Test(expected = IllegalArgumentException.class)
public void example(){
    Person p = mock(Person.class);
    doThrow(new IllegalArgumentException()).when(p).setName(eq("JDM"));
    String name = "JDM";
    p.setName(name);
}
```

### Stub의 doReturn, thenReturn의 차이

Mockito를 사용하다보면 코드들에 `doThrow`, `doReturn`등의 형식이 아닌 

`thenReturn()`, `thenThrow()`등의 형식도 볼 수 있을 것이다. 둘의 차이점은 다음과 같다.

**doReturn**

- 실제로 메서드를 호출하고 리턴값을 임의로 정의할 수 있다.
- 메서드를 실제로 수행하여 메서드 작업이 오래걸릴 경우 끝날 때 까지 기다려야한다.
- 실제 메서드를 호출하기 때문에 대상 메서드에 문제점이 있을 경우 발견할 수 있다.

```java
doReturn(6).when(cal).add(2, 4);
```

**thenReturn**

- 메서드를 실제로 호출하지 않으며 리턴값을 임의로 정의할 수 있다.
- 실제 메서드를 호출하지 않기 때문에 대상 메서드에 문제점이 있어도 알 수 없다.

```java
when(cal.add(2, 4)).thenReturn(6); 
```



### Mock 객체 확인

Mock 객체가 어떻게 사용이 됐는지 확인할 수 있다.

- 특정 메소드가 특정 매개변수로 몇번 호출 되었는지,

- 최소 한번은 호출 됐는지,

- 전혀 호출되지 않았는지

  - Verifying exact number of invocations

  - `org.mockito.Mockito.verify()` 메서드

  - 예제 : 멤버 서비스에서 딱 1번 notify란 메서드가 study라는 매개변수를 가지고 호출 되어야 한다

  - ```
    verify(memberService, times(1)).notify(study);
    ```

  - any()도 가능하다.



- 어떤 순서대로 호출했는지

  - Verification in order

  - `org.mockito.Mockito.inOrder()`, `inOrder.verify()` 메서드

  - 예제 : 멤버 서비스에서 notify(study) 메소드가 notify(member) 보다 먼저 호출되어야 한다.

  - ```
    verify(memberService, times(1)).notify(study);
    verify(memberService, times(1)).notify(member);
    
    InOrder inOrder = inOrder(memberService)
    inOrder.verify(memberService).notify(study);
    inOrder.verify(memberService).notify(member);
    ```

  - 이후에 더이상 아무 액션이 일어나면 안된다 할 때는

  - `verifyNoMoreInteractions(Object mocks...)` 메서드



- 특정 시간 이내에 호출됐는지
  - Verification with timeout
- 특정 시점 이후에 아무 일도 벌어지지 않았는지
  - Finding redundant invocations





### BDD 스타일 Mockito API

BDD:애플리케이션이 어떻게 “행동”해야하는지에 대한 공통된 이해를 구성하는 방법으로, TDD에서 창안했다.

- BDD : Behaviour-Driven Development의 약자
- TDD : Test-Driven Development의 약자

BDD는 시나리오를 기반으로 테스트 케이스를 작성하며 함수 단위 테스트를 권장하지 않는다. 이 시나리오는 개발자가 아닌 사람이 봐도 이해할 수 있을 정도의 레벨을 권장한다. 하나의 시나리오는 Given, When, Then 구조를 가지는 것을 기본 패턴으로 권장하며 각 절의 의미는 다음과 같다.

> Feature : 테스트에 대상의 기능/책임을 명시한다. Scenario : 테스트 목적에 대한 상황을 설명한다. Given : 시나리오 진행에 필요한 값을 설정한다. When : 시나리오를 진행하는데 필요한 조건을 명시한다. Then : 시나리오를 완료했을 때 보장해야 하는 결과를 명시한다.

> 테스트 대상은 A 상태에서 출발하며(Given) 어떤 상태 변화를 가했을 때(When) 기대하는 상태로 완료되어야 한다. (Then) 또는 Side Effect가 전혀 없는 테스트 대상이라면 테스트 대상의 환경을 A 상태에 두고(Given) 어떤 행동을 요구했을 때(When) 기대하는 결과를 돌려받아야 한다. (Then)

행동에대한스팩

- Title
- Narrative
  - As a / I want / so that
- Acceptance criteria
  - Given / When / Then

#### Mockito는 `BddMockito`라는 클래스를 통해 BDD스타일의 API를제공한다.

#### Given

- org.mockito.BDDMockito.given() 메서드

  - 위에서 사용했던 when을 다음과 같이 바꿀 수 있다. when -> given

  - ```
    when(memberService.findById(1L))
            .thenReturn(Optional.of(member));
    when(studyRepository.save(study))
            .thenReturn(study)
    //
    given(memberService.findById(1L))
            .willReturn(Optional.of(member));
    given((studyRepository.save(study)))
            .willReturn(study);
    ```

#### Then

- org.mockito.BDDMockito.then() 메서드

  - 위에서 사용했던 verify를 다음과 같이 바꿀 수 있따. verify -> then

  - ```
    verify(memberService, times(1)).notify(study);
    verifyNoMoreInteractions()
    
    then(memberService).should(times(1)).notify(study);
    then(memberService).shouldHaveNoMoreInteractions();
    ```

  ```
  
  ```

참고

- https://javadoc.io/static/org.mockito/mockito-core/3.2.0/org/mockito/BDDMockito.html
- https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html#BDD_behavior_verification

### 

# 슬라이스 테스트



슬라이스 테스트는 단위 테스트와 통합 테스트의 중간 개념,

레이어드 아키텍처를 기준으로 각 레이어 별로 나누어 테스트를 진행한다.

이말인 즉슨, 레이어별로 단위테스트를 한다는것

단위 테스트를 수행하기 위해서는 모든 외부 요인을 차단하고 테스트를 진행해야 한다.

하지만 컨트롤러는 개념상 웹과 맞닿은 레이어로서  외부 요인을 차단하고 테스트하면 의미가 없기 때문에 슬라이스 테스트를 진행하는 경우가 많다.

스프링은 레이어 별로 잘라서 특정 레이어에 대해서 Bean을 최소한으로 등록시켜 테스트 하고자 하는 부분에 최대한 단위 테스트를 지원해주고 있다.







컨트롤러를 이용한 슬라이스 테스트는 `WebMvcTest` 어노테이션을 이용한다. 

그리고 Spring Data JPA 사용시에도 @SpringBootTest 대신 @DataJpaTest를 사용하는 것이 좋다.

`@SpringBootTest` 어노테이션을 사용하는 경우 단점은 아래와 같다.

- 실제 구동되는 애플리케이션의 설정, 모든 `Bean`을 로드하기 때문에 시간이 오래걸리고 무겁다.
- 테스트 단위가 크기 때문에 디버깅이 어려운 편이다.
- 외부 API 콜같은 Rollback 처리가 안되는 테스트 진행을 하기 어려움

따라서 repository 레이어의 단위테스트의 경우 `@SpringBootTest` 대신 `@DataJpaTest` 사용하여 테스트를 작성하는 경우 통해 속도적인 측면과 의존성 측면에서 이점을 가질 수 있다.



### 슬라이스 테스트를 위해 사용할 수 있는 대표적인 어노테이션들

- `@WebMvcTest`
- `@WebFluxTest`
- `@WebServiceClientTest`
- `@JsonTest`
- `@RestClientTest`
- `@DataJdbcTest`
- `@DataJpaTest`
- `@JdbcTest`
- `@JooqTest`
- `@JsonTest`



그 중 @WebMvcTest와 @DataJpaTest를 정리하였다.



> @Service 클래스의 테스트는 @ExtendWith 와 함께 테스트 하면 된다. 

* https://galid1.tistory.com/772

## @WebMvcTest

- MVC를 위한 테스트.
- 웹에서 테스트하기 힘든 `컨트롤러`를 테스트하는 데 적합.
- 웹상에서 요청과 응답에 대해 테스트할 수 있음.
- 시큐리티, 필터까지 자동으로 테스트하며, 수동으로 추가/삭제 가능.
- @SpringBootTest 어노테이션보다 가볍게 테스트할 수 있음.
- 다음과 같은 어노테이션이 붙은 Bean들만 스캔하도록 제한함. 
  - @Controller, 
  - @ControllerAdvice, 
  - @JsonComponent, 
  - Converter, 
  - GenericConverter, 
  - Filter, 
  - HandlerInterceptor,
  - 따라서 의존성이 끊기기 때문에, 예를 들면 서비스와 같은 객체들은 @MockBean을 사용해서 만들어 사용해야 한다.
- 이 밖에 테스트를 하는 데 필요하지 않은 컴포넌트들(ex. `@Service`, `@Repository`)은 `Bean`으로 등록하지 않는다.

```java
@WebMvcTest(ShelterPostController.class)
public class PostControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected PostService postService;

    @Test
    @DisplayName("게시글 리스트 조회 테스트")
    void getPostsTest() throws Exception {
        // given, when, then
				...
    }
  	
  	@Test
    void testExample() throws Exception {
  	    given(this.postService.getPostsBy("sboot")) // 이 메서드를 호출하면
            .willReturn(new Post("1")); // 지정된 객체를 반환
  	    
        this.mvc.perform(get("/api/posts").accept(MediaType.TEXT_PLAIN))
            .andExpect(status().isOk()).andExpect(content().string(...));
      }
  
}
```



---

### **@MockBean**

* `Mock Bean`은 **기존 `Bean`의 껍데기만 가져오고 내부 구현은 사용자에게 위임**한 형태이다.



spring-boot-test 패키지는 Mockito를 포함하고 있기 때문에 기존에 사용하던 방식대로 Mock 객체를 생성해서 테스트하는 방법도 있지만, spring-boot-test에서는 새로운 방법도 제공한다.

- 바로 @MockBean 어노테이션을 사용해서 이름 그대로 Mock 객체를 빈으로써 등록할 수 있다.
- 기존에 사용되던 스프링 Bean이 아닌 Mock Bean을 주입한다.
- 그렇기 때문에 만일 @MockBean으로 선언된 빈을 주입받는다면 Spring의 ApplicationContext는 Mock 객체를 주입한다.
- 새롭게 @MockBean을 선언하면 Mock 객체를 빈으로써 등록하지만, 
- `만일 @MockBean으로 선언한 객체와 같은 이름과 타입으로 이미 빈으로 등록되어있다면 해당 빈은 선언한 Mock 빈으로 대체된다.`



즉, 해당 `Bean`의 어떤 메서드에 어떤 값이 입력되면 어떤 값이 리턴 되어야 한다는 내용 

모두 `testExample` 메서드와 같이 **개발자 필요에 의해서 조작이 가능**하다.

어떤 로직에 대해 `Bean`이 예상대로 동작하도록 하고 싶을 때, `Mock Bean`을 사용하는 것이다.



또한 @MockBean 어노테이션은 테스트 내용 중 외부 서비스를 호출하는 부분을 Mock해서 쉽게 처리할 수 있다.

```java
@SpringBootTest
public class XXXControllerTest {

    @MockBean  // 외부 서비스 호출에 사용되는 RestTemplate Bean을 Mock
    private RestTemplate mockRT;

    @MockBean  // 외부 서비스 호출에 사용되는 Service Bean을 Mock
    private XXXService xXXService;

}
```



예를 들면 아임포트 등 **외부의 결제 대행 서비스**를 사용하여 결제 기능을 개발한다고 가정하자.

결제 대행 서비스에서는 테스트 코드에서 보낸 요청을 올바르지 않은 요청으로 간주할 것이다.

올바른 요청으로 간주했을 때의 로직을 테스트하고 싶은 경우, `Mock Bean` 을 사용한다.

원하는 결과를 지정한 후, 이후 로직을 진행하면 된다.

```java
@WebMvcTest(PaymentController.class)
public class PaymentControllerTests {
    @AutoWired 
    private MockMvc mvc;
  
  	@MockBean
    private PaymentService paymentService; // PaymentService 내부에서 외부의 결제 대행 서비스를 사용하고 있는 상태라고 가정
  	
    @Test
    public void testPayment() throws Exception {
  	    given(this.payMentService.chargePoint(50000L)) // 5만원 금액 충전: 테스트 환경에서는 실패하는 행위이지만
            .willReturn(new Point(50000L)); // 올바른 요청으로 간주하고 그에 따른 객체를 반환하도록 행위 지정
      }
}
```



### `Mock` 사용 시 주의할 점 및 적절한 사용 방법

슬라이스 테스트 시, 하위 레이어는 `Mock` 기반으로 만들기 때문에 주의할 점들이 있다.

- 실제 환경에서는 제대로 동작하지 않을 수 있다.
- `Mock`을 사용한다면 내부 구현도 알아야 하고, 테스트 코드를 작성하며 테스트의 성공을 의도할 수 있기 때문에 완벽한 테스트라 보기 힘들다.
- 내부 구현이 변경 됐을 때 테스트가 실패하지 않고 통과하게 되면서 혼란이 발생할 수도 있다.

`그렇다면 언제 `Mock` 기반의 테스트를 사용해야 할까?`

- 랜덤의 성격을 띄고 있는 함수
- `LocalDate.now()` 처럼 계속 흘러가는 시간의 순간
- 외부에 존재하여 내가 제어할 수 없는 외부 서버, 외부 저장소 등 제어할 수 없는 영역
- 대규모 어플리케이션(깊은 depth의 레이어)에서 하위 계층들의 테스트 셋업이 방대할 경우



---



# @DataJpaTest



## @DataJpaTest

Spring Data JPA를 테스트하고자 한다면 `@DataJpaTest` 기능을 사용해볼 수 있다.

- 해당 테스트는 기본적으로 in-memory embedded database를 생성하고 `@Entity` 클래스를 스캔한다.
- 일반적인 다른 컴포넌트들은 스캔하지 않는다. 따라서 특정 bean의 의존성이 필요한 경우 아래의 방법 사용
  - @import
  - @DataJpaTest(includeFilters = @Filter(..))

`@DataJpaTest`는 `@Transactional` 어노테이션을 포함하고 있다.

- 따라서 테스트가 완료되면 자동으로 롤백된다.
  ![img](https://velog.velcdn.com/images%2Fljo_0920%2Fpost%2F563be9c9-8607-4a07-bf46-0581e59ab3d2%2FUntitled.png)

만약 `@Transactional` 기능이 필요하지 않다면 아래와 같이 줄 수 있다.

```java
@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class SomejpaTest {
    ...
}
```

`@DataJpaTest` 기능을 사용하면 `@Entity`를 스캔하고 repository를 설정하는 것 이외에도 

테스트를 위한 `TestEntityManager`라는 빈이 생성된다.

- 이 빈을 사용해서 테스트에 이용한 데이터를 정의할 수 있다.

```java
@DataJpaTest
class SomejpaTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("게시글 아이디로 댓글 목록 삭제 테스트")
    void deleteAllByMissingPostIdTest() {
        // given
        LongStream.rangeClosed(1, 3).forEach(idx ->
            entityManager.persist(Comment.builder()
                .missingPost(missingPost)
                .content("내용")
                .account(account)
                .build()
            )
        );

        // when
        commentRepository.deleteAllByMissingPostId(missingPost.getId());
        List<Comment> comments = commentRepository.findAll();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(comments).hasSize(3);
                comments.forEach(foundComment ->
                                 softAssertions.assertThat(foundComment.isDeleted()).isTrue());
            }
        );
    }

}
```

만약 테스트에 내장된 임베디드 데이터베이스를 사용하지 않고 real database를 사용하고자 하는 경우, `@AutoConfigureTestDatabase` 어노테이션을 사용하면 손쉽게 설정할 수 있습니다.

```java
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SomejpaTest {
    ...
}
```





참조

- https://tecoble.techcourse.co.kr/post/2021-05-18-slice-test/
- https://velog.io/@ljo_0920/Spring-Boot-slice-test



---
