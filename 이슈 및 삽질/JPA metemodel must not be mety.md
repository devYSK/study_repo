

# JPA + JUnit Slice Test Error [JPA metamodel must not be empty! 해결]



Controller 단위테스트를 위해 @WebMvcTest 어노테이션을 이용해서 테스트하던 중에

```
JPA metamodel must not be empty
```

오류를 만났다.

JPA Audit 기능을 사용하기 위해  @EnableJpaAuditing을 추가했었는데, @SpringBootApplication 클래스에 등록해놓은 것이 에러의 원인이었다.



* `JPA metamodel must not be empty!` 는  JPA 메타 모델은 비워둘 수 없다는 메시지다.

 

`@WebMvcTest`는 Web기능과 관련된 Bean만 등록하기 때문에 JPA 생성과 관련된 기능이 전혀 존재하지 않는 테스트 어노테이션이다.

JPA Auditing 기능을 사용할때  `@SpringBootApplication`에 `@EnableJPaAuditing`을 추가하여 사용하지 않으면 에러가 발생한다. Spring 컨테이너를 요구하는 테스트는 `XXXApplication` 클래스가 항상 로드되지만 Application은 로드하지 않기 때문이다. 



## 해결방법



### 1. @MockBean 추가

테스트 코드 클래스단에 `@MockBean(JpaMetamodelMappingContext.class)`을 추가한다.

 모든 테스트 클래스마다 입력해줘야 한다는 단점(?)이 존재한다.

```java
@ExtendWith({SpringExtension.class, RestDocumentationExtension.class})
@WebMvcTest(UserController.class)
@MockBean(JpaMetamodelMappingContext.class)
class UserControllerTest {
    ...
}
```

* 그러면 자동으로 JPA 에 대한 Context 를 넣어주므로 해결 된다.



### 2. @Configration 분리

`@EnableJpaAuditing` Configuration Class를 따로 분리한다.

```java
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {

}
```

* 그러나 2번째 방법을 사용해서 **@DataJpaTest**를 테스트할 때 주의점이 있다.

*  ``@DataJpaTest``는  Jpa에 관련된 Test에 필요한 최소한의 빈을 불러오는데, 거기에는 @Configuration 빈이 포함되어있지 않다.

   따라서 @Configuration을 사용하여 따로 설정 파일을 만들었을 경우  Import(JpaAuditingConfig.class)를 넣어줘야 한다.

```java
@Import(JpaAuditingConfig.class)
@DataJpaTest
class UserRepositoryTest {
  
}
```

