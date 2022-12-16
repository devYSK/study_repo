

# Spring Test Junit5에서 의존성 주입을 @Autowired로 받아야만 하는 이유 - ParameterResolutionException



보통 Spring을 사용하면 의존성 주입을 생성자 주입을 사용하여 받게 된다.

그래서 JUnit 테스트 에서도 @RequiredArgsConstructor 를 이용한 생성자 주입이 가능할 것 같지만 그렇지 않다.

만약 생성자 주입을 받으려고 하면 다음과 같은 에러를 만난다

```
org.junit.jupiter.api.extension.ParameterResolutionException: No ParameterResolver registered for parameter [final com.ys.jpa.domain.member.MemberRepository memberRepository] in constructor [public com.ys.jpa.ConstructorDiTest(com.ys.jpa.domain.member.MemberRepository)].
```

줄여보면 다음과 같은 오류이다

```
org.junit.jupiter.api.extension.ParameterResolutionException: No ParameterResolver registered for parameter
```

에러로그를 읽어보는것이 중요하다. 해석해보면

Jupiter가 해당 파라미터에 대해 등록된 ParameterResolver가 없다는 예외이다.





---

결론부터 말하자면,

 Junit5의 경우 빈 주입은 `@AutoWired` 어노테이션을 사용해야 한다. 

main 코드의 환경 (스프링 컨테이너)과 test 코드의 환경(Jupiter)가 다르기 때문이다. 

* @Autowired를 통해 스프링 컨테이너에게 빈 주입을 요청해야 한다. 



그러나 ` @TestConstructor를 통해 @Autowired 없이 주입받을 수 있는 방법이 있다. `

* https://0soo.tistory.com/138



---



Jupiter가 무엇인지 부터 보자.



###  Junit의 아키텍처 구조

<img src="https://github.com/devYSK/java-application-test-Various-ways/blob/master/img/2020-12-20-23-55-43.png?raw=true">

### JUnit Platform

테스트를 실행할 수 있는 엔진을 포함하고 여러 Tool(콘솔, 이클립스, Intellij등의 IDE)에 일관성 있는 API를 제공해주는 역할을 담당

- JUnit으로 만든 Test Code를 실행시키는 Launcher를 의미한다.

  Launcher를 통해서 콘솔에서도 테스트 실행이 가능하다.

- TestEngine API를 제공한다.

------

### JUnit Vintage

**JUnit Platform**을 구현하는 구현체

- JUnit3과 4의 구현체이다.

  = TestEngine API 구현체이다.

- Vintage 라는 의미를 보면 하위버전의(Junit3, 4) 지원을 위한 구현체

------

### JUnit Jupiter

**JUnit Platform**을 구현하는 구현체

- JUnit5 구현체이다.

  = TestEngine API 구현체이다.

- Spring Boot에서는 2.2 올리면서 Default로 설정되었다.



> JUnit팀이 이러한 **JUnit Platform**을 기반으로 구현체를 만드는 방식을 사용한 이유가 있다고 한다.
>
> Jupiter나 Vintage처럼 다른 써드파티 구현체들이 **JUnit Platform**의 스펙을 준수해서 API를 구현하게 해서
>
> 오픈소스 기반으로 개발자 생태계에 JUnit이 기여하고 똑똑한 개발자들이 **JUnit Platform**을 기반으로 한 창의적인 테스트 프레임워크를 개발하기를 바랬기 때문이라고 한다.
>
> * 그에 따른 여러 JUnit 기반 오픈소스 테스트 프레임워크들도 속속 등장하고 있다. (Spec, Specsy 등등)
>
> * 이는 결국 JUnit이라는 테스트 프레임워크 생태계를 구축하고 넓히려는데 그 목적이 있다고 볼 수 있다. 
>   * 독점이긴하지만 개발자 입장에선 편리해서 좋다. 



JUnit5에서는 **Vintage 가 아닌 Jupiter 를 사용한다** 



## 왜 Jupiter가 해당 파라미터에 대해 등록된 ParameterResolver가 없다는 예외를 발생시키는가



> Junit의 ParameterResolver는 런타임시 동적으로 파라미터를 결정하는 test extension에 관한 API가 정의되어 있다.
>
> 그래서 테스트 클래스의 생성자나 메서드나 lifeCycle 메서드가 파라미터를 받고싶다면 ParameterResolver를 통해 런타임에 주입받을 수 있다.
>
> 그러나 Spring Bean에 대한 ParameterResolver는 Jupiter에 정의되어 있지 않다. 



main 코드와 test 코드가 실행되는 환경이 다르다.

main 코드는 Spring 컨테이너가 실행되어 Spring 컨테이너가 Application Context에서 빈을 찾아서 주입해주지만,

test 코드는 Jupiter 컨테이너가 SpringExtension을 이용해서 주입하게 된다.

생성자 주입을 요구하는 경우 Jupiter가 생성자 매개변수를 주입해주기 위해 처리할 ParameterResolver를 찾지만, 

처리 할 ParameterResolver가 없기 때문에 예외를 발생시키는 것이다.



하지만 @Autowired 어노테이션을 사용하게 되면, Jupiter가 이를 보고 Spring Container에게 빈 주입을 요청해서 정상적으로 주입할 수 있게 된다. 



결론적으로, main 환경과 test 환경의 관리하고있는 프레임워크의 주체가 다르기 때문에, 테스트 환경을 관리하고 있는 Jupiter는 생성자를 통한 생성자 주입을 할 수 없고, @Autowired를 명시해서 Jupiter가 SpringContainer에게 요청해야만 빈 주입을 할 수 있게 된다.





- Spring main 환경의 경우 

  - Spring Ioc 컨테이너가 등록할 Bean들을 먼저 찾아서 관리.

  - 이후 생성자 주입을 요구하는경우, 적절한 Bean을 찾아서 생성자 매개변수를 처리 

- test framework 환경의 경우 

  - 생성자 매개 변수 관리를 **Jupiter**가 하게된다.
  - 생성자 주입을 요구하는경우, 생성자 매개변수를 처리할 **ParameterResolver**을 찾지만, 주입할 빈들은 스프링 컨테이너가 가지고 있기때문에 처리하지 못하게 예외를 던진다.



### 참조



* https://donghyeon.dev/junit/2021/04/11/JUnit5-%EC%99%84%EB%B2%BD-%EA%B0%80%EC%9D%B4%EB%93%9C/
* https://minkukjo.github.io/framework/2020/06/28/JUnit-23/
