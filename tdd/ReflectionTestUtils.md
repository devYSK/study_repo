

# RefectionTestUtils - private 필드 값 주입, private 테스트

https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/test/util/ReflectionTestUtils.html

ReflectionTestUtils 클래스는 Spring Test Context 프레임워크의 일부이다.  단위 및 통합 테스트에서 사용되는 리플렉션 기반 유틸리티 메서드들을 모은 클래스이며, private field 를 설정하고 private field 호출하고 을 dependencies을 주입할 수 있다. 



일반적인 비즈니스 애플리케이션을 개발한다면 사실 프로덕션 코드에서 리플렉션을 사용할 경우는 거의 없다. 라이브러리나 프레임워크 등을 개발할 때 주로 사용될 수 있다. 하지만 테스트 코드에서는 리플렉션이 필요한 상황이 있는데, 대표적인 경우가 

* 도메인 엔티티의 기본 키 값에 IDENTITY전략을 설정하여 @Setter나 생성자를 제공하지 않을 때
* @Value로 가져오는 값을 설정해줄 때 이다.



일반적으로 private으로 변수를 선언하므로 리플렉션으로 직접 변수를 set해주어야 한다. Spring Framework에서 테스트 할 때 [ReflectionTestUtils](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/test/util/ReflectionTestUtils.html)를 제공하므로 테스트 코드에서 리플렉션을 사용할 때 참고하도록 하자.

- getField: 객체에서 필드의 실제 값을 찾음
- setField: 객체에서 필드의 실제 값을 설정함
- invokeMethod: 객체의 메소드를 호출함
- 기타 등등



ReflecitonTestUtils의 메소드는 접근 가능이 불가능한 상태여도 바로 호출이 가능하도록 내부적으로 makeAccessible을 호출해주고 있다. 그러므로 프로덕션 코드에서만 사용할 수 있는 ReflectionUtils보다 편리하게 리플렉션을 사용할 수 있다.



ReflectionTestUtils는 org.springframework.test.util 패키지에 포함되어 있다. 

* spring-context 5.1.2 / spring-test 5.1.2 모듈 (Dependencies)이 필요하다.



## 다음은 ReflectionUtils 클래스에 대한 설명이다. 

> ReflectionTestUtils is a collection of reflection-based utility methods for use in unit and integration testing scenarios.
> There are often times when it would be beneficial to be able to set a non-public field, invoke a non-public setter method, or invoke a non-public configuration or lifecycle callback method when testing code involving, for example:
>
> * ORM frameworks such as JPA and Hibernate which condone the usage of private or protected field access as opposed to public setter methods for properties in a domain entity.
> * Spring's support for annotations such as @Autowired, @Inject, and @Resource which provides dependency injection for private or protected fields, setter methods, and configuration methods.
> * Use of annotations such as @PostConstruct and @PreDestroy for lifecycle callback methods.
>
> In addition, several methods in this class provide support for static fields and static methods — for example, setField(Class, String, Object), getField(Class, String), invokeMethod(Class, String, Object...), invokeMethod(Object, Class, String, Object...), etc.
>
> 



ReflectionTestUtils는 단위 및 통합 테스트 시나리오에서 사용하기 위한 리플렉션 기반 유틸리티 메서드 collection입니다.

다음과 같은 코드를 테스트할 때 non-public 필드를 set 하거나, non-public setter 메서드를 호출하거나, non-public configuration 또는 lifecycle callback method들을 호출할 때 유용하게 사용할 수 있습니다. 

* JPA 및 Hibernate와 같은 ORM 프레임워크는 도메인 엔터티의 속성에 대한 public setter 메서드와 반대로 private 또는 protected field에 접근하여 사용을 용납합니다.
* @Autowired, @Inject 및 @Resource와 같은 주석에 대한 Spring의 지원은 비공개 또는 보호 필드, setter 메서드 및 구성 메서드에 대한 의존성 주입을 제공합니다.
* lifecycle callback method에 @PostConstruct 및 @PreDestroy와 같은 주석을 사용합니다.

또한 이 클래스의 여러 메서드는 static field 및 static field 대한 지원을 제공합니다. 

* 예를 들어 setField(Class, String, Object), getField(Class, String), invokeMethod(Class, String, Object...), invokeMethod( 객체, 클래스, 문자열, 객체...) 등



## *ReflectionTestUtils를* 사용하여 private field 값 설정

단위 테스트(Unit Test), 또는 슬라이스 테스트(Slice Test)에서 공용 setter 메서드 없이 개인 필드가 있는 클래스의 인스턴스를 사용해야 한다고 가정한다.

일반적으로 JPA 사용시 id값 생성 전략을 sequence나 identity로 설정한다면, DB에 저장되어야만 id값을 알 수 있지만, 

ReflectionTestUtils을 통해 저장하지 않아도 아이디를 set 할 수 있다. 



```java
public class Employee {
    private Integer id;
    private String name;
	
    private String employeeToString(){
      return "id: " + getId() + "; name: " + getName();
    }
}
```

public setter method가 없기 때문에 테스트용 값을 할당하기 위해 id에  할 수 없다.

이 때, *ReflectionTestUtils.setField* 메서드를 사용하여 private member field인  *id* 에 값을 할당할 수 있다.

```java
@Test
public void whenNonPublicField_thenReflectionTestUtilsSetField() {
    Employee employee = new Employee();
    ReflectionTestUtils.setField(employee, "id", 1);
 
    assertTrue(employee.getId().equals(1));
}
```



```java
ReflectionTestUtils.setField(객체, "필드명", 값);
```



## *ReflectionTestUtils를* 사용하여 private method 호출

클래스  외부에서 access  할 수 없는 경우에도 private method에 대한 단위테스트를 작성할 수 있다. 



```java
@Test
public void whenNonPublicMethod_thenReflectionTestUtilsInvokeMethod() {
    Employee employee = new Employee();
    ReflectionTestUtils.setField(employee, "id", 1);
    employee.setName("Smith, John");
 
    assertTrue(ReflectionTestUtils.invokeMethod(employee, "employeeToString")
      .equals("id: 1; name: Smith, John"));
}
```



```java
ReflectionTestUtils.invokeMethod(객체, "메소드이름")
```



## *ReflectionTestUtils를* 사용하여 의존성 주입 (의존객체 주입 )

*@Autowired* 어노테이션이 있는 field가 있는 다음 Spring Bean에 대한 단위 테스트를 작성한다고 가정

```java
@Component
public class EmployeeService {
 
    @Autowired
    private HRService hrService;

    public String findEmployeeStatus(Integer employeeId) {
        return "Employee " + employeeId + " status: " + hrService.getEmployeeStatus(employeeId);
    }
}
```

다음과 같이 *HRService가* 구현되어있다고 가정

```java
@Component
public class HRService {

    public String getEmployeeStatus(Integer employeeId) {
        return "Inactive";
    }
}
```

[또한 Mockito를](https://www.baeldung.com/mockito-annotations) 사용하여 *HRService* 클래스 에 대한 mock 객체를 생성할 수 있다 . 이 목 객체를 EmployeeService 인스턴스 에 주입 하고 단위 테스트에서 사용할 수 있다.

```java
RService hrService = mock(HRService.class);
when(hrService.getEmployeeStatus(employee.getId())).thenReturn("Active");
```



*hrService는* public setter가 없는 private field 이므로 *ReflectionTestUtils.setField* 메서드를 사용하여 위에서 만든 목 객체를 이 private field에 주입할 수 있다.

```java
EmployeeService employeeService = new EmployeeService();
ReflectionTestUtils.setField(employeeService, "hrService", hrService);
```



그러면 다음처럼 사용할 수 있게 된다.

```java
@Test
public void whenInjectingMockOfDependency_thenReflectionTestUtilsSetField() {
    Employee employee = new Employee();
    ReflectionTestUtils.setField(employee, "id", 1);
    employee.setName("Smith, John");

    HRService hrService = mock(HRService.class);
    when(hrService.getEmployeeStatus(employee.getId())).thenReturn("Active");
    EmployeeService employeeService = new EmployeeService();

    // Inject mock into the private field
    ReflectionTestUtils.setField(employeeService, "hrService", hrService);  

    assertEquals(
      "Employee " + employee.getId() + " status: Active", 
      employeeService.findEmployeeStatus(employee.getId()));
}
```

이 기술은 우리가 bean 에서 필드 주입(@Authwried)을 사용하고 있을 때에만 해결할 수 있는 방법이라는 점에 유의해야 한다.   
[생성자 주입](https://www.baeldung.com/constructor-injection-in-spring) 으로 전환하면 이 접근 방식이 필요하지 않다.





## Reflection의 단점

Reflection을 이용하면 강제적으로 private 메소드를 호출할 수 있다. 다만 이렇게 하면 접근하려는 메소드,필드명을 String값으로 넘겨지게 되므로, compile time에 메소드명의 오타가 검증되지 못하고, refactoring으로 메소드명을 바꾸어도 자동으로 String으로 적힌 부분은 바뀌지 않는 단점이 있다. 부작용을 감수하고서라도 쓰겠다고 각오가 된 곳에 제한적으로 사용하는 것을 권장한다. 



* Compile time에 Type, Exception 등의 검증을 진행할 수 없다. *Runtime에서 가져오기때문*

* `성능 오버헤드`: 리플렉션 연산은 당연히 리플렉션이 없는 것보다 느리다. 그래서 자주 호출되는 성능에 민감한 코드에는 적용하지 말아야 한다. 그러나 테스트시에 좀더 정확하고 디테일하기 위해 사용하는 정도는 괜찮다고 생각한ㄷ다.

* Java Reflection API가 느리고 높은 비용을 사용한다 :   Reflection을 통한 초기 호출 시 JVM이 해당 정보를 미리 최적화할 수 없기 때문이다. JIT Compiler의 Bytecode Caching, Opcode Optimization.. 등
  * 즉 JVM 최적화를 수행할 수 없다.



많은 경우에 private method는 public 메소드에서 추출되어서 나온 메소드들이라, public을 통해서 간접적으로 테스트를 하는 것이 옳다고 생각한다. public 메소드들을 다양한 방면으로 테스트 하면 되니까.

private한 메소드나 필드와 같은 무언가를 테스트 해야되는 상황이 온다면 클래스와 메소드의 역할과 책임 설정을 처음부터 잘못했거나 테스트 위치가 잘못되었을 수도 있다. 다시한번 신중하게 생각해보고 설계를 되돌아 봐야 할것같다.  





[리플렉션 공식 문서](https://docs.oracle.com/javase/tutorial/reflect/index.html)

# **리플렉션 사용을 위한 ReflectionUtils**



스프링은 리플렉션 API를 보다 편리하게 사용하기 위한 [ReflectionUtils](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/util/ReflectionUtils.html)를 제공한다. 스프링은 내부적으로 리플렉션을 적극 활용하고 있기 때문에 리플렉션이 필요한 상황이라면 해당 클래스를 살펴보도록 하자.

- accessibleConstructor: 접근가능한 생성자를 찾음
- declaresException: 메소드가 명시적으로 throw하고 있는지를 검사함
- findField: 클래스에서 특정 필드를 찾음
- findMethod: 클래스에서 특정 메소드를 찾음
- getDeclaredMethods: 클래스의 모든 메소드를 찾음
- getAllDeclareMethods: 부모 클래스를 포함해 클래스의 모든 메소드를 찾음
- getField: 객체에서 필드의 실제 값을 찾음
- setField: 객체에서 필드의 실제 값을 설정함
- invokeMethod: 특정 메소드를 호출함
- makeAccessible: 접근 가능여부를 true로 설정함
- 기타 등등



getField나 setField처럼 실제 객체의 값을 조작하거나 메소드를 호출하는 경우에는 접근 제어자가 private이면 접근이 불가능하다. 만약 접근이 불가능한 상태에서 해당 기능을 호출하려고 하면 다음과 같은 예외가 발생한다. 그러므로 접근 가능 여부를 true로 바꿔주어야 하는데, 필드 객체의 setAccessible를 true로 설정하던가 ReflectionUtils에서 makeAccesible을 호출해주어야 한다.

```
java.lang.IllegalStateException: Could not access method or field
```





### 참조

* https://www.baeldung.com/spring-reflection-test-utils

* https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/test/util/ReflectionTestUtils.html

* https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/util/ReflectionUtils.html

* https://mangkyu.tistory.com/238