# Spring Bean scope

Spring Bean 스코프란, 스프링 Bean이 앱이 구동되는 동안의 존재할 수 있는 범위를 뜻합니다.

스프링 컨테이너는 빈 객체를 생성하고, 관리하며, 제거하는 것을 담당합니다.

이때 빈 객체의 스코프(scope)를 설정하여 빈 객체의 생명 주기를 제어할 수 있습니다

기본적으로 스프링의 빈은 싱글톤으로 만들어 지지만,  개발자가필요에 의해  빈의 스코프를 지정할 수 있습니다. 



스프링에서는 다음과 같은 다섯 가지 스코프를 제공합니다.

[스프링 공식 문서](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#beans-factory-scopes)

| Scope       | Description                                                  |
| ----------- | ------------------------------------------------------------ |
| singleton   | (기본값) 스프링 IoC 컨테이너당 하나의 인스턴스만 사용 - 한마디로 앱이 구동되는 동안 하나만 쓴다는 거임 |
| prototype   | 매번 새로운 빈을 정의해서 사용                               |
| request     | HTTP 라이프 사이클 마다 한개의 빈을 사용, web-aware 컨택스트에서만 사용가능 - ex. Applicaiton context만 유효 |
| session     | HTTP 세션마다 하나의 빈을 사용, web-aware 컨택스트에서만 사용가능 - ex. Applicaiton context |
| application | ServeltContext 라이프사이클 동안 한개의 빈만 사용, web-aware 컨택스트에서만 사용가능 - ex. Applicaiton context |
| websocket   | websocket 라이프사이클 안에서 한개의 빈만 사용, web-aware 컨택스트에서만 사용가능 - ex. Applicaiton context |

스코프를 설정하는 방법은 @Scope 어노테이션을 사용하거나 XML 설정 파일에서 <bean> 요소의 scope 속성을 사용하는 방법이 있습니다.

> request, session, application 세 개를 묶어서 웹 스코프 라고도 합니다.



## 싱글톤 (Singleton) 스코프

기본적으로 스프링의 빈은 싱글톤으로 만들어집니다. 

싱글톤으로 생성된 빈의 경우 DI, DL 어떤 경우에도 동일한 오브젝트를 얻어오는 것을 확인할 수 있습니다. 

싱글톤의 필드에는 의존관계에 있는 빈에 대한 레퍼런스나 읽기전용 값만 저장해두고, 

DTO와 같은 변수는 파라미터나 리턴값으로 전달하는 것이 바람직합니다.

* singleton bean은 Spring 컨테이너에서 한 번 생성 되며, 컨테이너가 사라질 때 bean도 제거된다.
* 생성된 하나의 인스턴스는 single beans cache에 저장되고, 해당 bean에 대한 요청과 참조가 있으면 캐시된 객체를 반환한다.
  즉, 하나만 생성되기 때문에 동일한 것을 참조한다.



**기본적으로 모든 bean은 scope이 명시적으로 지정되지 않으면 singleton.**



**annotation 설정**
대상 클래스에 @Scope("singletone")

```java
@Component
@Scope("singletone")
public class SingletoneBean {
  
}
```



싱글톤으로 적합한 객체

* 상태가 없는 공유 객체: 상태를 가지고 있지 않은 객체는 동기화 비용이 없다. 따라서 매번 이 객체를 참조하는 곳에서 새로운 객체를 생성할 이유가 없다.
* 읽기용으로만 상태를 가진 공유 객체: 1번과 유사하게 상태를 가지고 있으나 읽기 전용이므로 여전히 동기화 비용이 들지 않는다. 매 요청마다 새로운 객체 생성할 필요가 없다.
* 공유가 필요한 상태를 지닌 공유 객체: 객체 간의 반드시 공유해야 할 상태를 지닌 객체가 하나 있다면, 이 경우에는 해당 상태의 쓰기를 가능한 동기화 할 경우 싱글톤도 적합하다.
* 쓰기가 가능한 상태를 지니면서도 사용빈도가 매우 높은 객체: 애플리케이션 안에서 정말로 사용빈도가 높다면, 쓰기 접근에 대한 동기화 비용을 감안하고서라도 싱글톤을 고려할만하다. 이 방법은 1. 장시간에 걸쳐 매우 많은 객체가 생성될 때, 2. 해당 객체가 매우 작은 양의 쓰기상태를 가지고 있을 때, 3. 객체 생성비용이 매우 클 때에 유용한 선택이 될 수 있다.

비싱글톤으로 적합한 객체

* 쓰기가 가능한 상태를 지닌 객체: 쓰기가 가능한 상태가 많아서 동기화 비용이 객체 생성 비용보다 크다면 싱글톤으로 적합하지 않다.

* 상태가 노출되지 않은 객체: 일부 제한적인 경우, 내부 상태를 외부에 노출하지 않는 빈을 참조하여 다른 의존객체와는 독립적으로 작업

  을 수행하는 의존 객체가 있다면 싱글톤보다 비싱글톤 객체를 사용하는 것이 더 나을 수 있다.
  

## 프로토타입 (Prototype) 스코프

```java
@Component
@Scope(value = "prototype")
public class ProtoTypeBean {
}
```

프로토타입 범위의 bean은 특정 bean에 대한 요청이 있을 때마다 새로운 bean 인스턴스를 생성합니다.

<img src="https://blog.kakaocdn.net/dn/bPk95v/btscRqMBUZL/icoFiKIJgKK3LXbvModLvk/img.png" width = 700 height = 700>

**프로토타입 빈 스코프**는 컨테이너가 **생성 , 의존성 주입 , 초기화**까지만 처리해줍니다.

그러므로 빈 생명주기 관리 어노테이션 중 @PostConstrutor는 수행이 가능하지만, @PreDestory 는 호출 불가합니다.

그렇기에 그 이후 스프링 빈을 클라이언트에 반환한 이후로는 관리하지 않기에 소멸 메서드(@PreDestory) 같은것은 모두 클라이언트에서 자체적으로 관리해야 합니다.

### 프로토타입 스코프 - 싱글톤 빈과 함께 사용시 문제점

싱글톤 스코프의 빈의 프로토타입 스코프의 빈을 주입받는 경우에는 주의해야 할 점이 있습니다.

싱글톤 스코프의 빈이 프로토타입 빈을 주입받으면 싱글톤의 프로토타입 빈은 매번 바뀌지 않고 같은 빈이 쓰입니다. 



**singleton 빈**은 **ApplicationContext**가 처음 앱을 구동할때 빈을 만들고 빈을 주입해서 앱이 종료될 때 까지 계속 사용 되기 때문에 **singleton 빈** 안에 있는 **prototype 빈**도 처음 주입된 채로 그대로 사용되기 때문입니다.

### 해결방법 1. proxy mode를 이용하는 방법

```java
@Component
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ProtoTypeBean {
}
```

프로토타입으로 쓸 빈에 **@Scope** 어노태이션에 **proxyMode = ScopedProxyMode.TARGET_CLASS**를 넣어주면 됩니다. 

만약에 인터페이스라면 **proxyMode = ScopedProxyMode.INTERFACES**도 쓸 수가 있습니다.



ApplicationConxtext가 빈을 처음에 생성할 때 proto 빈을 주입받는게 아니라 prototype 클래스를 상속받은 proxy클래스를 만들어서 빈으로 등록하고 proxy클래스에서 내부적으로 매번 새로운 proto빈을 사용하게 끔 설계 되어있습니다.



### 해결방법 2. Dependency Lookup(DL)과 Provider를 이용하는방법

가장 간단한 방법은 싱글톤 빈이 프로토타입을 사용할 때마다 스프링 컨테이너에 새로 요청하는 것입니다.

```java
@Component
public class SingletoneBean {

    @Autowired
    ObjectProvider<ProtoTypeBean> protoType;

    public ProtoType getProtoType() {
        return protoType.getIfAvailable();
    }
}
```

ObjectProvider는 지정한 빈을 컨테이너에서 대신 찾아주는 DL(Dependency Lookup) 서비스를 제공하는 것입니다. 

* 과거에는 ObjectFactory가 있었는데, 여기에 편의 기능을 추가해서 ObjectProvider가 만들어졌다.

- **ObjectProvider의 getObject()를 호출하면 내부에서는 스프링 컨테이너를 통해 해당 빈을 찾아서 반환한다.(DL)**
- 스프링이 제공하는 기능을 사용하지만, 기능이 단순하므로 단위 테스트를 만들거나 mock 코드를 만들기는 훨씬 쉬워진다.
- 스프링에 의존하지만 별도 라이브러리는 필요 없다.
- ObjectProvider는 DL정도의 기능만 제공한다.



### 해결방법 3. JSR-330 Provider 사용

마지막 방법은 javax.inject.Provider 라는 JSR-330 자바 표준을 사용하는 방법입니다.

이 방법을 사용하려면 javax.inject:javax.inject:1 라이브러리를 gradle에 추가해야 합니다

```groovy
dependencies {
   // 프로바이더 사용을 위한 gradle추가
   implementation 'javax.inject:javax.inject:1'
   ...
}
```



```java
@Component
public class SingletoneBean {
	
	@Autowired
	private Provider<PrototypeBean> provider;

	public int logic() {
  	  PrototypeBean prototypeBean = provider.get();
    	prototypeBean.addCount();
    	int count = prototypeBean.getCount();
    	return count;
	}
} 
```

- 실행해보면 provider.get() 을 통해서 **항상 새로운 프로토타입 빈이 생성**되는 것을 확인할 수 있다.
- **provider 의 get()** 을 호출하면 내부에서는 스프링 컨테이너를 통해 해당 빈을 찾아서 반환한다. (DL)
- **자바 표준**이고, 기능이 단순하므로 단위 테스트를 만들거나 mock 코드를 만들기는 훨씬 쉬워진다.
- Provider 는 지금 딱 **필요한 DL 정도의 기능만 제공**한다.

#### 특징

- **get() 메서드 하나로 기능이 매우 단순**하다.
- **별도의 라이브러리가 필요**하다. (javax.inject)
- **자바 표준**이므로 스프링이 아닌 **다른 컨테이너에서도 사용**할 수 있다.



#### 프로토타입 빈을 언제 사용해야 하는가?

javax.inject 패키지에 가보면 Dependency Lookup(DL)을 언제 사용하는지에 대한 예시가 Document로 작성되어 있습니다.

* 여러 인스턴스를 검색해야 하는 경우
* 인스턴스를 지연 혹은 선택적으로 찾아야 하는 경우
* 순환 종속성을 깨기 위해서 
* 스코프에 포함된 인스턴스로부터 더 작은 범위의 인스턴스를 찾아 추상화 하기 위해서 사용한다.



# Web 스코프 - Request, Session, Application

- request : HTTP 요청 하나가 들어오고 나갈 때까지 유지되는 스코프, 각각의 HTTP 요청마다 별도의 빈 인스턴스가 생성되고 관리된다.
- session : HTTP Session과 동일한 생명주기를 가지는 스코프
- application : 서블릿 컨텍스트(ServletContext)와 동일한 생명주기를 가지는 스코프
- websocket : 웹 소켓과 동일한 생명주기를 가지는 스코프

웹 스코프는 웹 환경에서만 동작합니다. 

웹 스코프는 프로토타입과 다르게 스프링이 해당 스코프의 종료 시점까지 관리 하므로 종료 메서드가 호출됩니다.

<img src="https://blog.kakaocdn.net/dn/chfldL/btscNSXV545/4I0n3TGCV4JTWKDpzJbskK/img.png" width = 900 height = 400>

#### 1. build.gradle에 web 환경 추가

```groovy
//web 라이브러리 추가 
implementation 'org.springframework.boot:spring-boot-starter-web'
```

* 라이브러리 추가시 스프링 부트는 내장 톰캣 서버를 활용해 웹 서버와 스프링을 함께 실행한다.

* 해당 라이브러리가 없으면 AnnotationConfigApplicationContext 를 기반으로 애플리케이션을 구동한다.

* 웹 라이브러리가 추가되면 웹 관련 기능및 설정이 필요하기에 AnnotationConfigServletApplicationContext 를 기반으로 애플리케이션을 구동한다. 



**스프링 빈 등록 시 웹 스코프를 그대로 주입받으면 오류가 발생합니다.** 

싱글톤 빈은 스프링 컨테이너 생성 시 함께 생성되어서 라이프 사이클을 같이하지만, 웹 스코프(여기서는 request 스코프)의 경우 HTTP 요청이 올 때 새로 생성되고 응답하면 사라지기 때문에, 싱글톤 빈이 생성되는 시점에는 아직 생성되지 않습니다. 



**이때 프록시를 사용하면 문제를 해결할 수 있습니다.**

```java
@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MyLogger {
		...
}
```

*  proxyMode = ScopedProxyMode.TARGET_CLASS 속성 활용

* 적용대상이 클래스면 **TARGET_CLASS** 사용
  * CGLIB라는 바이트코드 조작 라이브러리를 사용

* 적용대상이 인터페이스일 경우 **INTERFACES** 사용 
  * 이제 이 클래스는 가짜 프록시를 만들어두고 이 가짜 프록시 빈을 의존관계 주입을 한다.

* 해당 빈이 실제 사용될 때 프록시 빈에서 실제 빈을 가져와 사용할 수 있도록 한다. 
  * 어디서 많이 들어본 것 같은 방법이죠?



스프링 컨테이너에 등록된 가짜 객체인 프록시 객체가 생성되어 빈으로 등록된 후에, 실제 애플리케이션 구동 후에 요청이 오면 생성됩니다.

* 즉 request scope 타입의 진짜 빈은 지연 생성 되는것입니다.



### 참조

* 인프런 김영한님, 백기선님 강의
* https://velog.io/@probsno/Bean-%EC%8A%A4%EC%BD%94%ED%94%84%EB%9E%80