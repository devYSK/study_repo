# 하루만에 끝내는 스프링 테스트 - 쥬쥬님

* https://www.inflearn.com/course/%EC%A5%AC%EC%A5%AC%EC%99%80-%ED%95%98%EB%A3%A8%EB%A7%8C%EC%97%90-%EB%81%9D%EB%82%B4%EB%8A%94-%EC%8A%A4%ED%94%84%EB%A7%81%ED%85%8C%EC%8A%A4%ED%8A%B8/dashboard



[toc]





# AAA 패턴 / GWT 패턴

**테스트에서 AAA(Arrange-Act-Assert) 패턴은 테스트 케이스를 작성할 때 사용되는 구조적인 접근 방법.**

** AAA 패턴은 다음과 같은 단계로 구성됩니다.**

1. Arrange (준비) 단계: 테스트를 수행하기 위해 필요한 초기 상태를 설정합니다. 이 단계에서는 테스트할 객체나 변수를 초기화하고, 필요한 데이터를 설정하며, 테스트 환경을 구성합니다.
2. Act (실행) 단계: 테스트 대상 코드를 실행합니다. 이 단계에서는 실제로 테스트할 메서드나 함수를 호출하고, 특정 동작을 수행합니다.
3. Assert (단언) 단계: 예상되는 결과를 확인하고 검증합니다. 이 단계에서는 실행된 코드의 결과나 상태를 기대한 대로 확인하여 테스트가 성공했는지 여부를 판단합니다. 예상된 결과와 실제 결과를 비교하고, 일치하지 않는 경우에는 테스트를 실패로 표시합니다.

AAA 패턴은 테스트 코드를 구조화하여 가독성을 높이고, 테스트의 목적과 결과를 명확하게 전달하는 데 도움을 줍니다. 이 패턴을 사용하면 테스트 케이스를 일관성 있고 구조화된 방식으로 작성할 수 있으며, 테스트 코드의 유지 보수성을 향상시킬 수 있습니다.



> AAA 패턴과 유사한 패턴인 GWT(Given - When - Then) 패턴



# xUnit과 JUnit

**xUnit은 테스트 도구에서 사용되는 테스트 프레임워크의 일종입니다.** xUnit은 다양한 프로그래밍 언어를 지원하며, 테스트 작성, 실행, 결과 분석 등을 효과적으로 수행할 수 있도록 도와줍니다. xUnit은 테스트 주도 개발(Test-Driven Development, TDD) 및 애자일 개발 방법론에서 널리 사용됩니다.

이는 초기에는 단위 테스트를 위해 설계되었지만, 현재는 단위 테스트 뿐만 아니라 통합 테스트, 시스템 테스트 등 다양한 수준의 테스트를 지원합니다. 

xUnit 프레임워크의 주요 특징은 다음과 같습니다.

1. 테스트 케이스 작성: xUnit은 테스트 케이스를 작성할 수 있는 기본적인 구조와 애너테이션 또는 어노테이션을 제공합니다. 개발자는 테스트 메서드를 작성하고, 애너테이션을 사용하여 테스트를 구성할 수 있습니다.
2. 테스트 실행: xUnit은 테스트 케이스를 실행하고 테스트 결과를 수집하는 런처 또는 테스트 실행기를 제공합니다. 이를 통해 테스트를 자동으로 실행하고, 예외 처리 및 테스트 결과를 기록할 수 있습니다.
3. 단언(Assertion): xUnit은 단언(assertion) 메서드를 통해 예상 결과와 실제 결과를 비교하여 테스트 결과를 확인합니다. 이를 통해 코드의 동작이 예상대로 수행되고 있는지를 검증합니다.
4. 테스트 결과 분석: xUnit은 테스트 결과를 분석하고 요약하여 제공합니다. 성공한 테스트, 실패한 테스트, 테스트 커버리지 등의 정보를 통해 테스트의 진행 상황과 코드의 품질을 판단할 수 있습니다.

xUnit 프레임워크는 소프트웨어 테스트의 자동화와 품질 향상을 위해 많이 사용되며, 테스트 주도 개발과 같은 개발 방법론과 함께 사용될 때 가장 효과적으로 활용됩니다.

JUnit 5는 Java 언어를 위한 테스트 프레임워크로서, 단위 테스트를 작성하고 실행하는 데 사용됩니다. JUnit은 소프트웨어 개발자들이 자동화된 테스트를 작성하여 코드의 정확성을 검증하고, 버그를 발견하고, 소프트웨어의 안정성을 향상시키는 데 도움을 줍니다.



# JUnit 테스트에 이름 붙이기

**`@DisplayNameGeneration`** 애너테이션은 테스트 클래스에 사용되며, `테스트 메서드의 이름을 동적으로 생성`하는 데 사용됩니다.

**`@DisplayNameGeneration`** 애너테이션은 **`DisplayNameGenerator`** 인터페이스를 구현한 클래스를 지정합니다.

 이 인터페이스를 구현하여 원하는 방식으로 테스트 메서드의 이름을 생성할 수 있습니다.

*  JUnit 5에서는 **`ReplaceUnderscores`** 및 **`SimpleDisplayNameGenerator`**라는 두 가지 기본 구현체를 제공합니다.

* **`Standard`** class : 디폴트 전략 그대로 반환
* `ReplaceUnderscores` : 언더바(_)를 공백( )으로 바꿔준다 
* **`Simple`** : 클래스의 이름에서 'Test' 접미사를 제거하고 메서드 이름은 그대로 반환. 만약 메서드 이름에 밑줄이 있다면 공백으로 대체
* **`IndicativeSentences`** : 테스트 클래스와 테스트 메서드의 이름을 결합하여 문장 형태로 반환. 'should'이라는 단어가 중간에 들어가며. 밑줄은 공백으로 대체

다음과 같은 방식으로 사용 

```java
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MyTestClass {
    @Test
    void test_method_with_underscores() {}
}

@DisplayNameGeneration(DisplayNameGenerator.Simple.class)
class MyTest {
    @Test
    void myTestMethod() {}
}

```

 

# Mock 객체를 생성하는 여러 방법

```groovy
testImplementation 'org.mockito:mockito-core:5.2.0'
```

## 1. Mockito.mock()

```java
Mockito.mock(StudentScoreRepository.class);
```

## 2. @Mock

```java
class TestClass {
  @Mock
  StudentScoreRepository studentScoreRepository;
}
```



## Spy 사용법

Mockito의 **`spy()`** 메서드는 실제 객체를 사용하면서 일부 메서드를 모킹하여 테스트하는 데 사용되는 기능입니다. **`mock()`** 메서드와는 달리 **`spy()`** 메서드는 기존 객체를 래핑하여 테스트할 수 있습니다. 모킹된 메서드가 호출되면 실제 구현이 실행되지만, 원하는 메서드에 대해서는 테스트에서 명시적인 동작을 설정할 수 있습니다.

사용 예제 :

```java
import org.mockito.Mockito;

public class Example {
    public static class MyObject {
        public String getValue() {
            return "Real Value";
        }
        
        public String getRealValue() {
            return "Real Value";
        }
    }

    public static void main(String[] args) {
        // MyDependency 클래스의 실제 객체 생성
        MyObject realObject = new MyObject();

        // 실제 객체를 스파이 객체로 래핑
        MyObject spyObject = Mockito.spy(realObject);

        // 일부 메서드의 동작을 모킹
        Mockito.when(spyObject.getValue()).thenReturn("Mocked value");

        // 스파이 객체 사용
        System.out.println(spyObject.getValue()); // 출력: "Mocked value"
        System.out.println(spyObject.getRealValue()); // 출력: "Real Value"
    }
}
```

**`spy()`** 메서드는 **`mock()`** 메서드와는 다르게 실제 객체를 사용하며, 필요한 경우 일부 메서드의 동작을 원하는대로 조작할 수 있습니다. 기존 객체를 테스트 중에 래핑하여 사용하면, 테스트 대상 객체와의 상호작용과 더 가까이 시뮬레이션하여 보다 정확한 테스트를 할 수 있게 됩니다.



# 목 객체 패턴

## Test Data Builder Pattern

객체에 빌더가 없으면 구현하기 불편함

```java
@Builder
public class User {
    private String name;
    private int age;
    // ... (생략)
}

public class UserTestDataBuilder {
    public static User.Builder validUser() {
        return User.builder()
                 .name("jyujyu")
                 .age(20);
    }

    public static User.Builder invalidUser() {
        return User.builder()
						      // 잘못된 데이터로 설정
    }
}
```

## Object Mother 패턴

Object Mother 패턴은 Fixture Object 패턴과 유사

Object Mother 패턴의 핵심 아이디어는 다음과 같습니다:

1. 여러 테스트에서 사용되는 테스트 데이터를 관리하는 클래스를 생성합니다.
2. 해당 클래스에는 다양한 테스트 데이터를 생성하고 초기화하는 메소드들이 포함됩니다.
3. 각 테스트 메소드에서 필요한 테스트 데이터를 Object Mother 클래스의 메소드를 호출하여 가져옵니다.

```java
public class User {
    private String name;
    private int age;
    // ... (생략)
}

public class UserMother {
    public static User createValidUser() {
        User user = new User();
        user.setName("jyujyu");
        user.setAge(20);
        // ... (다른 필드 설정)
        return user;
    }

    public static User createInvalidUser() {
        User user = new User();
        // 잘못된 데이터로 설정
        return user;
    }
}
```



## Fixture Object 패턴 - TestFixture

Fixture Object 패턴은 테스트에서 사용되는 공통 데이터와 객체들을 별도의 Fixture 클래스로 분리하여 관리하는 패턴

* 테스트 클래스가 더 간결해지고, 
* 테스트 데이터의 관리가 편리해지며, 
* 테스트 환경 설정과 정리 작업을 효과적으로 처리할 수 있습니다

Fixture Object 패턴의 기본 아이디어는 다음과 같습니다:

1. 테스트 데이터를 생성하고 초기화하는 작업을 담당하는 Fixture 클래스를 만듭니다.
2. Fixture 클래스는 테스트에 필요한 객체와 데이터를 논리적으로 그룹화합니다.
3. 각 테스트 메소드에서 필요한 Fixture 객체를 생성하여 사용합니다.

```java
public class User {
    private String name;
    private int age;
    // ... (생략)
}

public class UserFixture {
    public static User createValidUser() {
        User user = new User();
        user.setName("jyujyu");
        user.setAge(20);
        // ... (다른 필드 설정)
        return user;
    }

    public static User createInvalidUser() {
        User user = new User();
        // 잘못된 데이터로 설정
        return user;
    }
}

// enum으로도 구현합니다
public enum UserFixture {
  VALID_USER(new User("jyujyu", 20),
  INVALID_USER(new User(잘못된값));
  
  private User user;

  UserFixture(User user);

  public User getUser() {
    return user;
  }
}
```



# 통합테스트 - testConatiners

```groovy
testImplementation 'org.testcontainers:testcontainers:1.19.0'
```

```java
@SpringBootTest
public class IntegrationTest {

	static DockerComposeContainer rdbms;

	static {
		// 루트 디렉토리
		rdbms = new DockerComposeContainer(new File("docker-compose-test.yml"))
			.withExposedService(
				"local-db", // 컴포즈내의 서비스명 
				3306, // 서비스 포트 
				Wait.forLogMessage(".*ready for connections.*", 1) // 로그 메시지 
					.withStartupTimeout(Duration.ofSeconds(300))
			) 
			.withExposedService(
				"local-db-migrate", // 컴포즈 내의 서비스명 
				0, // 서비스 포트. 없으면 0 
				Wait.forLogMessage("(.*SuccessFully applied.*) | (.*Successfully validated.*)", 1)
					.withStartupTimeout(Duration.ofSeconds(300))
				)
		;
    
		rdbms.start();
	}
}

```

다음과 같이 실행 가능

```java
class TestApplicationTests extends IntegrationTest{

	@Test
	void contextLoads() {
	}

}
```

## 테스트 Flow

![image-20231008151353485](./images//image-20231008151353485.png)

1. 테스트 시작 : 테스트를 시작합니다
2. 컨테이너 생성 : testcontainers가 Docker를 이용해 테스트 필요한 컨테이너들(MySQL, Redis 등..)을 생성합니다
3. 동적 프로퍼티 주입 : 생성한 컨테이너의 정보를 기반으로 스프링 프로퍼티 동적으로 주입(overwrite)합니다
4. 테스트 수행 : 생성한 컨테이너를 활용하여 테스트를 수행합니다
5. 테스트 종료: 테스트 종료와 동시에 생성했던 컨테이너들을 정리합니다

