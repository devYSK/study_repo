# 10분 테코톡 - Spring vs Spring boot

## 어노테이션(Annotation)

* 주석 이라는 뜻

어노테이션은 기능을 가지고 있지 않다.

어노테이션은 마킹이다.

마킹된 어노테이션을 처리해주는 프로세서가 있다.

### Spring boot 에서의 어노테이션 처리



```java
// 어노테이션 선언
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface WoowaAnnotation {
}


// 어노테이션 어떤 체리를 해줄지 작성 
@Component
@Aspect
public class AnnotationAspect {
    @Around("@Annotation(com.ys.woowa_techotalk.WoowaAnnotation)")
    public Object printHeadAntTail(ProceedingJointPoint jointPoint) throws Throwable {
        // Do Something Before
        Object ret = jointPoint.proceed();
        // Do Something After

        return ret;
    }
}

// 어노테이션 적용
@Component
public class Anooteatated {
  
  @WoowaAnnotation
  public static void annotatedMethod() {
    // Do Something
  }
}
```



* 어노테이션이 적용된 곳에 약속된 처리가 적용.

## Spring?



* Spring framework를 Spring 또는 스프링으로 부르겠습니다.



최신 Java 기반 앤터프라이즈 애플케이션을 위한 포괄적인 프로그래밍 구성모델을 제공하는 프레임워크

스프링을 이용해서 기업용 애플리케이션을 쉽게 만들 수 있다.

객체지향의 특징을 잘 활용할 수 있게 해주며, 개발자들은 핵심 비즈니스 로직 구현에만 집중할 수 있게 해주는 프레임워크이다. 



> 엔터프라이즈 애플리케이션?

* 기업용 애플리케이션
* 대규모 데이터 처리와 트랜잭션이 동시에 여러 사용자로부터 행해지는 매우 큰 규모의 환경



### Spring의 기본 전략 

1. 비즈니스 로직을 담는 코드와 엔터프라이즈 기술을 처리하는 코드를 분리시키는것
   * 개발자는 설계와 비즈니스 로직에 초점을 맞출 수 있다.
2. DI(Dependency Injection), IOC(Inversion of Control) 을 이용해 객체지향의 다형성을 극대화 시킨다
   * 객체간의 결합도를 낮추어 코드 재사용성을 향상시키고 느슨하게 결합된 애플리케이션을 만들 수 있다.



### Spring으로 무엇을 개발 할 수 있나?

<img src="https://blog.kakaocdn.net/dn/b4gPMT/btrJ8meeaLR/KnlSU7rao5EhaVIolMKjS1/img.png" width=700 height=400>



* 스프링으로  개발 할 수 있는 여러 서비스들





<img src="https://blog.kakaocdn.net/dn/6CYrn/btrKaw8ftne/iTopL22UKad6Vk0vMHFOEK/img.png" width=700 height=400 >



* 스프링의 여러 프로젝트들과 그 프로젝트 별로 개발되어있는 하위 모듈들



### Spring 생태계

<img src="https://blog.kakaocdn.net/dn/ct8jPR/btrKatci6RJ/4U4ONRrvYiA2lksPg6jnZk/img.png" width=700 height=400>

* Spring framework와 Spring boot를 기반으로 한 여러 프로젝트 들이 있다.



> Why Spring framework is called Spring?



```
Whatever happened next, the framework needed a name. In the book it was referred to as the “Interface21 framework” (at that point it used com.interface21 package names), but that was not a name to inspire a community. Fortunately Yann stepped up with a suggestion: “Spring”. His reasoning was association with nature (having noticed that I'd trekked to Everest Base Camp in 2000); and the fact that Spring represented a fresh start after the “winter” of traditional J2EE. We recognized the simplicity and elegance of this name, and quickly agreed on it.
```

* [Spring 공식문서 Spring.io의 글](https://spring.io/blog/2006/11/09/spring-framework-the-origins-of-a-project-and-a-name)

* Spring을 통해서 개발자들의 겨울은 끝났다. 봄이 왔다 해서 Spring이라 불림



## Spring Boot?

```
Spring Boot makes it easy to create stand-alone, production-grade Spring based Applications that you can "just run".

We take an opinionated view of the Spring platform and third-party libraries so you can get started with minimum fuss. Most Spring Boot applications need minimal Spring configuration.
```

* [Spring 공식문서 Spring Boot](https://spring.io/projects/spring-boot)

* makes it easy : 쉽게
* to create stand-alone : 단돈적인
* production-grade : 상용화 수준의



스프링 부트는 독립적이며, 운영할 수 있는 수준의 스프링 기반 애플리케이션을 `쉽게` 만들 수 있게 도와준다.

최소한의 설정으로 스프링 플랫폼과 서드파티 라이브러리들을 쉽게 사용할 수 있도록 한다.

스프링 부트 애플리케이션은 최소한의 스프링 설정을 필요로 한다.

> -> 스프링 부트는 스프링 애플리케이션을 쉽게 만들고 설정할 수 있게 도와주는 프로젝트이다.



### 어떤걸 쉽게 만들어주는가?

* 기존 XML 기반의 다양한 설정들.
  * 설정이 반이였던 과거 XML 기반 스프링의 사실상 가장 복잡했던 스프링의 문제이다.
* 외장 톰캣을 내장 톰캣으로 실행시켜 간단하게 관리하고 실행시킴
* 



## Spring Boot embedded server

기존 Spring은 톰캣 같은 웹서버를 외부에서 가져와서 설정하는 번거로움 등이 있었는데,

Spring Boot에서는 톰캣 등을 내장해서 실행하면 사용할 수 있는 편리함을 제공해준다.

* tomact을 사용하기 싫으면?
* jettey를 사용하고싶다?
* 다음의 의존성 추가하면 된다. 

```groovy
// bulid.gradle

dependeincies {
  implementation('org.springframework.boot:spring-boot-starter-web')
  implementation('org.springframework.boot:spring-boot-starter-jetty')
}

```



> java -jar javafile.jar & 

내장 서블릿 컨테이너 덕분에 jar 파일로 간단 배포가 가능하다. 





# Spring VS Spring Boot



### Spring의 목적

* DI (Dependency Injection) - 의존성 주입
  * 의존성 주입을 통해 객체간의 결합도를 낮추고 코드 재사용성을 향상 시키고, 단위 테스트를 용이하게 해준다.
* 중복된 코드 제거
  * 반복되는 코드들을 제거함으로써 개발자가 비즈니스 로직에만 집중할 수 있도록 한다.
* 다른 프레임워크와의 통합
  * 다른 프레임워크들과 통합하여 개발자가 비즈니스 로직 외에 신경 써야 할 부분을 덜어줌으로써 생산성 향상에 도움이 된다.



### Spring Boot의 목적

* Auto Configuration - 자동 설정
* 쉬운 의존성 관리
* 내장 서버



### Auto Configuration

* 스프링 기능들을 사용하기 위한 자동 설정
* starter 의존성을 통해 간단히 다양한 설정들을 할 수 있다.



### Auto CInfiguration의 작동

* 스프링 부트 애플리케이션의 시작지점인 메인 메서드에 있는 `@SpringBootApplication` 어노테이션으로부터 시작된다. 

### Spring boot의 Main Class

```java
@SpringBootApplication
public class WoowaTechotalkApplication {

    public static void main(String[] args) {
        SpringApplication.run(WoowaTechotalkApplication.class, args);
    }

}
```



* @SpringBootApplication 어노테이션을 잘 보고 열어보자.

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(excludeFilters = { @Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
		@Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class) })
public @interface SpringBootApplication {
  ...
}
```

다양한 어노테이션이 또 안에 적용되어 있다.

* 스프링 부트는 Bean을 두번에 걸쳐 등록한다.



#### `@ComponentScan`

* @Component 라는 어노테이션이 붙은 클래스들을 자동으로 Bean으로 등록해주는 어노테이션
* 이 과정에서 개발자가 설정한 Bean들을 생성한다.
  * @Controller, @Repository, @Service, @Component, @Repository 등 
* @ComponentScan 어노테이션이 달려있는 클래스(Configuration 파일이나 메인 메서드)의 아래 패키지들을 전부 등록
  * 스캔할 패키지 명을 지정할 수도 있다.



#### `@EnableAutoConfiguration`

* @ComponentScan이  먼저 동작하고 그다음 EnableAutoConfigurationd 이 작동
* 추가적인 Bean들을 읽어서 등록한다
  * [spring.factories](##spring.factories?) 안에 들어있는 수 많은 자동 설정들이 조건에 따라 적용이되어 등록된다

* `SpringFactoriesLoader.class`의 `FACTORIES_RESOURCE_LOCATION` 변수를 보면 알 수 있듯이, **`META-INF/spring.factories` 파일에 정의된 자동 설정할 클래스들을 먼저 불러와 Bean으로 등록한다.**
* `spring.factories` 파일의 모든 클래스가 Bean으로 등록되지 않고, `@ConditionalOnClass` 어노테이션의 옵션에 맞는 클래스만 Bean으로 등록된다.



##### 개발자가 정의한 Bean과 Spring Boot에서 Bean이 중복되는 경우?

그런 경우는 발생하지 않게 정해져있다.

- AutoConfigurationImportSelector.getAutoConfigurationEntry()에서
  `removeDuplicates()` 메소드로 **중복된 설정을 제외**시키고,
  `getExcludsions()` 메소드로 **제외할 설정을 제외**시킨다.



* SpringBoot의 @Condition과 @Conditional(Spring 4.0 버전부터 추가됨)을 이용해서 문제를 해결

*  위 @EnableAutoConfiguration 설명에서도 언급되었듯, Auto-configuration 빈들은 필요한 상황에만 자신이 실행될 수 있도록 @Condition, @Conditional로 생성되어있기 때문입니다. 

  

### 스프링 부트의 쉬운 의존성 관리 (Dependencies)

1. starter 의존성(spring-boot-stater)을 통해 의존성을 쉽게 관리해준다.

* 예를들어 spring-boot-starter-jpa 의존성을 추가하면 다음과 같은 일이 벌어진다
  * jpa에 관련된 의존성을들 알아서 추가해준다
  * spring-aop, spring-jdbc등의 의존성을 추가
  * classpath를 뒤져서 어떤database를 사용하는지 파악하고 자동으로 entityManager를 구성 
  * properties나 yml파일을 통해 해당 모듈들에 필요한 설정을 해준다. 

2. io.spring.dependency-management

* 스프링 부트의 의존성을 관리해주는 플러그인 으로 dependency manager가 관리하는 프로젝트들은 버전관리를 알아서 해준다
  * 해당 의존성에 대한 버전을 명시할 필요가 없다.
    * 직접 버전을 명시하는 경우 해당 버전으로 오버라이딩
* 한 줄의 의존성 추가로 수 많은 프로젝트들의 의존성을 버전 관리를 할 수 있게된다



### 정리

1. 간편한 설정
2. 편리한 의존성 관리 & 자동 권장 버전 관리
3. 내장 서버로 인한 간단한 배포 서버 구축
4. Spring Seucirty, Data JP 등 다른 스프링 프레임워크 요소를 쉽게 사용 





---

#### spring.factories?

스프링 부트가 실행될 때 classpath에 있는 spring.factories 파일을 찾는다

이 파일은 resources/META-INF/spring.factories에 있다.

프로젝트에 등록된 의존성들(dependencies) - 

* External Libraries - org.springframework.boot:spring-boot-autoconfigure.jar
  * META_INF
    * spring.factories



* 각 클래스 파일들의 설정 파일들은 spring-autoconfigure-metadata-properties 파일에 정의되어 있다.

이 파일을 열어보면 스프링 부트가 실행할 설정 클래스들의 이름들을 담고 있다.

정의된 각 org.springframework.boot.autoconfigure.xxx 들의 설정 클래스들을 실행시킨다.



* 실행 조건은 xxxxAutoConfiguration 클래스들의 `@ConditonalOnCLass` 어노테이션의 옵션을 보면 언제 실행될지 조건이 걸려있다.



각 `xxxx`AutoConfigure 클래스들은 각 `xxxx`Properties 클래스에 

`@ConfigurationProperties(prefix= "???")` 어노테이션이,  

application.properties prefix가 ???인 설정들을 읽어서 등록한다.



* 다 prefix가 아니여도 각 클래스마다 설정이 다를 수 있으므로 찾아보면 바로 나온다.

우리가 DB에 접속정보를 입력하는 것 처럼,

물론 application.properties나 .yml로 각 프로퍼티 설정을 바꿔서 이용할 수 있다.

---





참조

* [에어의 Spring vs Spring boot](https://www.youtube.com/watch?v=Y11h-NUmNXI&list=PLgXGHBqgT2TvpJ_p9L_yZKPifgdBOzdVH&index=12)
* [또링의 Spring vs Spring boot](https://www.youtube.com/watch?v=OdpPvdB7qZY)
  * https://ssoco.tistory.com/66

* [러너덕의 Spring autoConfiguration](https://www.youtube.com/watch?v=OXILjfY8edw&list=PLgXGHBqgT2TvpJ_p9L_yZKPifgdBOzdVH&index=231)
* https://velog.io/@guswns3371/Spring-VS-Spring-Boot

