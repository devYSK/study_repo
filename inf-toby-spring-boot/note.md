# Toby의 스프링 부트 - 이해와 원리


스프링 부트(Spring Boot)는 스프링을 기반으로 실무 환경에 사용 가능한 수준의 독립실행형 애플리케이션을 복잡한 고민 없이 빠르게 작성할 수 있게 도와주는 여러가지 도구의 모음이다



강의 목표. 

* 스프링부트의 동작 방법, 원리
* 스프링을 어떻게 이용해서 동작하는지

## 스프링 부트의 핵심 목표

* 매우 빠르고 광범위한 영역의 스프링 개발 경험을 제공
* 강한 주장을 가지고 즉시 적용 가능한 기술 조합을 제공하면서, 필요에 따라 원하는 방식으로 손쉽게 변형 가능
* 프로젝트에서 필요로 하는 다양한 비기능적인 기술(내장형 서버, 보안, 메트릭, 상태 체크, 외부 설정 방식 등) 제공
* 코드 생성이나 XML 설정을 필요로 하지 않음



스프링 부트는 2012년 스프링 프레임워크 프로젝트에 이슈로 등록된 “Containerless 웹 개발 아키텍처의지원” 요청에서 논의와 개발 시작되었다.

> https://github.com/spring-projects/spring-framework/issues/14521



## 컨테이너리스(Containerless) 개발

“컨테이너 없는” 웹 애플리케이션 아키텍처란?

![image-20230520231018427](./images//image-20230520231018427.png)

웹 컨테이너의 역할

* 웹 컴포넌트의 라이프사이클 관리

* 여러 웹 컴포넌트를 관리

* 자바의 웹 컴포넌트 : 서블릿

* 서블릿을 관리하는 컨테이너 : 서블릿 컨테이너 (톰캣)
* 스프링 컨테이너는 서블릿 컨테이너의 뒤에 존재하면서, 스프링 컴포넌트(빈)들을 관리한다.

스프링 애플리케이션 개발에 요구되는 서블릿 컨테이너의 설치, WAR 폴더 구조, web.xml,
WAR 빌드, 컨테이너로 배치, 포트 설정, 클래스로더, 로깅 등과 같은 필요하지만 애플리케이션 개발의 핵심이 아닌 단순 반복 작업을 제거해주는 개발 도구와 아키텍처 지원한다

스프링부트는 설치된 컨테이너로 배포하지 않고 **독립실행형(standalone) 자바 애플리케이션**으로 동작

## 강한 주장을 가진(opinionated) 도구

스프링 부트는 매 버전마다 사용할 기술의 종류를 선정하는 것만으로 사전 검증된 추천 기술과 라이브러리 구성, 의존 관계와 적용할 버전, 각 라이브러리의 세부 구성과 디폴트 설정을 제공한다

하지만 원한다면 스프링 부트가 제시한 구성을 오버라이드 하거나 재구성하는 것이 가능한
데, 매우 안전하고 명료한 방법을 통해서 원하는 방법으로 재구성할 수 있다

> "프레임워크를 효과적으로 재사용하기 위해서는 프레임워크의 최종 모 습뿐만 아니라 현재의 모습을 띠게 되기까지 진화한 과정을 살펴 보는 것이 가장 효과적이다. 
>
> 프레임워크의 진화 과정 속에는 프레임워크의 구성 원리 및 설계 원칙, 재사용 가능한 컨텍스트와 변경 가능성에 관련
> 된 다양한 정보가 들어 있기 때문이다. “
>
> 조영호 (프레임워크 3부)
> http://aeternum.egloos.com/2640343



## API 테스트 도구

HTTP 요청을 만들고 응답을 확인하는데 사용되는 도구
웹 브라우저 개발자 도구
curl
[HTTPie](https://httpie.io)
Intellij IDEA Ultimate- http request
Postman API Platform
JUnit Test
각종 API 테스트 도구



## HTTP

**Request**

* Request Line: Method, Path, HTTP Version
* Headers
* Message Body

**Response**

* Status Line: HTTP Version, Status Code, Status Text
* Headers
* Message Body



# 독립 실행형 서블릿 애플리케이션
 서블릿 컨테이너를 동작시키는 방법

## 1. SpringBoot 제거

```
// 아래 두 코드를 제거하고 main()만 남긴다 
@SpringBootApplication
SpringApplication.run(HellobootApplication.class, args);
```

## 2. 서블릿 컨테이너 띄우기 (톰캣 띄우기)

스프링 부트 프로젝트를 만들 때 web 모듈을 선택하면 다음과 같은 내장형 톰캣 라이브러리가 추가된다

```
org.apache.tomcat.embed:tomcat-embed-core:9.0.69
org.apache.tomcat.embed:tomcat-embed-el:9.0.69
org.apache.tomcat.embed:tomcat-embed-websocket:9.0.69
```

내장형 톰캣의 초기화 작업과 간편한 설정을 지원하도록 스프링 부트가 제공하는 TomcatServletWebServerFactory
를 사용하면 톰캣 웹 서버(서블릿 컨테이너)를 실행하는 코드를 만들 수 있다

```java
ServletWebServerFactory serverFactory = new TomcatServletWebServerFactory();
//TomcatServletWebServerFactory 를 언제든지 제티, 언더토우로 교체 가능 
WebServer webServer = serverFactory.getWebServer();
webServer.start();
```

## 3. 서블릿 등록

드에서 서블릿을 등록하려면 ServletContext가 필요하다. ServletContext를 전달해서 서블릿 등록과 같은 초기화
작업을 할 때는 ServletContextInitializer를 구현한 오브젝트를 ServletWebServerFactory의 getWebServer() 메소
드에 전달한다.
ServletContextInitializer는 @FunctionalInterface이므로 람다식으로 전환해서 사용하면 편리하다

```java
@FunctionalInterface
public interface ServletContextInitializer {
void onStartup(ServletContext servletContext) throws ServletException;
}
```

* 서블릿은 HttpServlet 클래스를 상속해서 필요한 메소드를 오버라이딩 하는 방식으로 만들 수 있다.
* https://docs.oracle.com/javaee/7/api/javax/servlet/http/HttpServlet.html

서블릿을 등록할 때는 서블릿 이름과 서블릿 오브젝트를 이용한다. 

* 서블릿 등록 정보에는 매핑할 URL 정보를 지정해야한다

```java
servletContext.addServlet("hello", new HttpServlet() {
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, 	IOException {
	}
}).addMapping("/hello");
```

```java
public class TobyspringApplication {

	public static void main(String[] args) {
		ServletWebServerFactory serverFactory = new TomcatServletWebServerFactory();
		WebServer webServer = serverFactory.getWebServer(servletContext -> {

			servletContext.addServlet("hello", new HttpServlet() {
				@Override
				protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws
					ServletException,
					IOException {
					resp.setStatus(HttpStatus.OK.value());
					resp.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE);
					resp.getWriter().print("Hello Servlet");
				}
			}).addMapping("/hello");
		});
		webServer.start();
	}

}
```

* localhost:8080/hello 로 요청을 보내면 응답이 온다.



## 프론트 컨트롤러 (Front Controller)

![image-20230521020434185](./images//image-20230521020434185.png)

여러 요청을 처리하는데 반복적으로 등장하게 되는 공통 작업을 하나의 오브젝트에서 일괄적으로 처리하게 만드는 방
식을 프론트 컨트롤러 패턴이라고 한다.

* https://martinfowler.com/eaaCatalog/frontController.html

서블릿을 프론트 컨트롤러로 만들려면 모든 요청, 혹은 일정 패턴을 가진 요청을 하나의 서블릿이 담당하도록 매핑해준다.

### 프론트 컨트롤러로 전환하는법

프론트 컨트롤러가 모든 URL을 다 처리할 수 있도록 서블릿 바인딩을 변경한다.
```java
}).addMapping("/*");
```

```java
public class TobyspringApplication {

	public static void main(String[] args) {
		ServletWebServerFactory serverFactory = new TomcatServletWebServerFactory();
		WebServer webServer = serverFactory.getWebServer(servletContext -> {

			servletContext.addServlet("hello", new HttpServlet() {
				@Override
				protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws
					ServletException,
					IOException {
					resp.setStatus(HttpStatus.OK.value());
					resp.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE);
					resp.getWriter().print("Hello Servlet");
				}
			}).addMapping("/*");
		});
		webServer.start();
	}

}
```

서블릿 내에서 HTTP 요청 정보를 이용해서 각 요청을 분리한다. 

만약 처리할 수 있는 HTTP 요청 정보가 없다면 상태코드를 404로 설정한다.

```java
if (req.getRequestURI().equals("/hello") && req.getMethod().equals(HttpMethod.GET.name())) {
...
} else if (req.getRequestURI().equals("/user")) {
...
} else {
resp.setStatus(HttpStatus.NOT_FOUND.value());
}
```



### 컨트롤러 매핑(Controller Mapping)과 바인딩

프론트 컨트롤러가 요청을 분석해서 처리할 요청을 구분한 뒤에 이를 처리할 핸들러(컨트롤러 메소드)로 요청을 전달한다. 

* 프론트 컨트롤러가 HTTP 요청을 처리할 핸들러를 결정하고 연동하는 작업을 <u>**매핑**</u>이라고 한다

* 들러에게 웹 요청 정보를 추출하고 의미있는 오브젝트에 담아서 전달하는 작업을 <u>**바인딩**</u>이라고 한다

프론트 컨트롤러의 두 가지 중요한 기능은 <u>매핑</u>과 <u>바인딩</u>이다



# 독립 실행형 스프링 애플리케이션
## 스프링 컨테이너 사용

![image-20230521021228118](./images//image-20230521021228118.png)

스프링 컨테이너는 애플리케이션 로직이 담긴 평범한 자바 오브젝트(POJO)와 구성 정보(Configuration Metadata)
를 런타임에 조합해서 동작하는 최종 애플리케이션을 만들어낸다



코드로 스프링 컨테이너를 만드는 가장 간단한 방법은 컨테이너를 대표하는 인터페이스인 ApplicationContext를 구현한
GenericApplicationContext를 이용하는 것이다

* https://docs.spring.io/spring-framework/docs/5.3.x/javadoc-api/org/springframework/context/support/GenericApplicationContext.html

이를 통해서 컨테이너에 등록할 빈 오브젝트 클래스 정보를 직접 등록할 수 있다. 이를 참고해서 컨테이너가 빈 오브젝트를 직접 생성한다

```java
GenericApplicationContext applicationContext = new GenericApplicationContext();
applicationContext.registerBean(HelloController.class);
applicationContext.refresh();
```

컨테이너에 필요한 정보를 등록하고 refresh()를 이용해서 초기화 작업을 진행한다.

ApplicationContext의 getBean() 메소드를 이용해서 컨테이너가 관리하는 빈 오브젝트를 가져올 수 있다. 

빈의 타입(클래스, 인터페이스) 정보를 이용해서 해당 타입의 빈을 요청한다.

```java
HelloController helloController = applicationContext.getBean(HelloController.class);
```

## 의존 오브젝트 추가

스프링 컨테이너는 싱글톤 패턴과 유사하게 애플리케이션이 동작하는 동안 딱 하나의 오브젝트만을 만들고 사용되게 만들어준다. 

이런 면에서 스프링 컨테이너는 **싱글톤 레지스트리라**고도 한다



## Dependency Injection
스프링 컨테이너는 DI 컨테이너이다. 스프링은 DI를 적극적으로 활용해서 만들어져 있고, 스프링을 이용해서 애플리케이
션을 개발할 때 DI를 손쉽게 적용할 수 있도록 지원해준다

* https://martinfowler.com/articles/injection.html

**어셈블러** : DI에는 두 개의 오브젝트가 동적으로 의존관계를 가지는 것을 도와주는 제3의 존재

![image-20230521022119888](./images//image-20230521022119888.png)

스프링 컨테이너는 DI를 가능하게 해주는 어셈블러로 동작한다

## DispatcherServlet

https://docs.spring.io/spring-framework/docs/5.3.x/javadoc-api/org/springframework/web/servlet/DispatcherServlet.html

스프링에는 앞에서 만들었던 프론트 컨트롤러와 같은 역할을 담당하는 DispatcherServlet이 있다.
DispatcherServlet은 서블릿으로 등록되어서 동작하면서, 스프링 컨테이너를 이용해서 요청을 전달할 핸들러인 컨트롤러
오브젝트를 가져와 사용한다.
DispatcherServlet이 사용하는 스프링 컨테이너는 GenericWebApplicationContext 를 이용해서 작성한다.



## 스프링 컨테이너로 통합

```java
public class TobyspringApplication {

	public static void main(String[] args) {

		GenericWebApplicationContext applicationContext = new GenericWebApplicationContext() {
			@Override
			protected void onRefresh() {
				super.onRefresh();
				ServletWebServerFactory serverFactory = new TomcatServletWebServerFactory();
				WebServer webServer = serverFactory.getWebServer(servletContext -> {
					servletContext.addServlet("dispatcherServlet",
							new DispatcherServlet(this))
						.addMapping("/*");
				});
				webServer.start();
			}
		};
    
		applicationContext.registerBean(HelloController.class);
		applicationContext.registerBean(HelloService.class);
		applicationContext.refresh();

		}

}

```

스프링 컨테이너의 초기화 작업 중에 호출되는 훅 메소드에 서블릿 컨테이너(톰캣)을 초기화하고 띄우는 코드를 넣는다

* 훅 메소드 : super.onRefresh();

* 상속을 통한 확장을 위해 템플릿 메소드 패턴이 적용되어있다.



## Bean의 생명주기 메소드

톰캣 서블릿 서버팩토리와 DispatcherServlet도 빈으로 등록한 뒤 가져와 사용할 수 있다.

@Bean 메소드에서 독립적으로 생성되게 하는 경우 DispatcherServlet이 필요로 하는 WebApplicationContext 타입 컨
테이너 오브젝트는 스프링 컨테이너의 <u>빈 생애주기 메소드</u>를 이용해서 주입 받게 된다.



* DispatcherServlet은 ApplicationContextAware라는 스프링 컨테이너를 setter 메소드로 주입해주는 메소드를 가진 인
  터페이스를 구현해놨고, 이런 생애주기 빈 메소드를 가진 빈이 등록되면 스프링은 자신을 직접 주입해준다.



스프링이 제공하는 생애주기 메소드는 다음과 같은 것들이 있다

```
BeanNameAware's - setBeanName
BeanClassLoaderAware's - setBeanClassLoader
BeanFactoryAware's - setBeanFactory
EnvironmentAware's v setEnvironment
EmbeddedValueResolverAware's - setEmbeddedValueResolver
ResourceLoaderAware's - setResourceLoader (only applicable when running in an application context)
ApplicationEventPublisherAware's - setApplicationEventPublisher (only applicable when running in an application context)
MessageSourceAware's - setMessageSource (only applicable when running in an application context)
ApplicationContextAware's - setApplicationContext (only applicable when running in an application context)
ServletContextAware's - setServletContext (only applicable when running in a web application context)

postProcessBeforeInitialization methods of BeanPostProcessors

InitializingBean's - afterPropertiesSet

a custom init-method definition

postProcessAfterInitialization methods of BeanPostProcessors
```



## SpringApplication

main()의 코드를 MySpringApplication 클래스를 만들어 run() 메소드로 넣고, 메인 클래스를 파라미터
로 받아서 사용하도록 만들면, 스프링 부트의 main() 메소드가 있는 클래스와 유사한 코드가 만들어진다

```java
@Configuration
@ComponentScan
public class TobyspringApplication {

	public static void main(String[] args) {
		MySpringApplication.run(TobyspringApplication.class, args);
	}
}
//
public class MySpringApplication {

	public static void run(Class<?> applicationClass, String... args) {
		AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext() {
			@Override
			protected void onRefresh() {
				super.onRefresh();
				ServletWebServerFactory serverFactory = new TomcatServletWebServerFactory();
				WebServer webServer = serverFactory.getWebServer(servletContext ->
					servletContext.addServlet("dispatcherServlet",
							new DispatcherServlet(this))
						.addMapping("/*"));
				webServer.start();
			}
		};

		applicationContext.register(applicationClass);
		applicationContext.refresh();
	}
}

```



# DI와 테스트, 애노테이션 활용
## 테스트 코드를 이용한 테스트

### TestRestTemplate

웹 서버에 HTTP 요청을 보내고 응답을 받아서 검증하는 테스트에서는 TestRestTemplate
을 사용하면 편리하다

```java
@Test
void hello() {
	TestRestTemplate restTemplate = new TestRestTemplate();	
	ResponseEntity<String> res = restTemplate.getForEntity(
	"http://localhost:8080/hello?name={name}", String.class, "Spring");

  assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);

  assertThat(res.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE)
             .startsWith(MediaType.TEXT_PLAIN_VALUE)).isTrue();

  assertThat(res.getBody().trim()).isEqualTo("Hello Spring");
}
```

## DI를 이용한 Decorator 패턴과 Proxy 패턴

### Decorator Pattern

기존 코드에 동적으로 책임을 추가할 때 쓰는 패턴.
오브젝트 합성 구조로 확장이 가능하도록 설계되어있고 DI를 적용해서 의존관계를 런타임에주입할 수 있다면 의존 오브젝트와 동일한 인터페이스를 구현한 확장기능(데코레이터)을 동적으로 추가할 수 있다. 

재귀적인 구조로 여러 개의 책임을 부가하는 것도 가능하다.

![image-20230521185327477](./images//image-20230521185327477.png)

데코레이터는 자기가 구현하는 인터페이스 타입의 다른 오브젝트를 의존한다. 

추가 책임, 기능의 적용 중에 의존 오브젝트를 호출한다

### Proxy Pattern

프록시 패턴에서 프록시는 다른 오브젝트의 대리자 혹은 플레이스 홀더 역할을 한다. 

프록시는 리모트 오브젝트에 대한 로컬 접근이 가능하게 하거나, 필요가 있을 때만 대상 오브젝트를 생성하는 필요가 있을 때 사용할 수 있다. 

보안이나 접속 제어 등에 사용하기도 한다.

![image-20230521191706895](./images//image-20230521191706895.png)

![image-20230521191719762](./images//image-20230521191719762.png)

프록시 패턴의 프록시와 일반적용 용어 프록시, 자바의 다이나믹 프록시가 동일한 건 아니다

# 자동 구성 기반 애플리케이션 (AutoConfiguration)


> 왜 어노테이션의 Retention은 RUNTIME으로 줘야 할까?
>
>
> 디폴트값은 class인데, 어노테이션 정보가 컴파일된 클래스파일까지는 살아있지만,
>
>  그 어노테이션이 달린 클래스를 런타임에 메모리로 로딩할때는 해당 정보가 사라지므로 RUNTIME까지 유지되도록 설정해줘야 한다. 

## 메타 애노테이션과 합성 애노테이션

### Meta-annotation

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component // Meta Annotation
public @interface Service {
}
```

애노테이션에 적용한 애노테이션을 메타 애노테이션이라고 한다. 

* 스프링은 코드에서 사용된 애노테이션의 메타 애노테이션의 효력을 적용해준다.

@Service 애노테이션이 부여된 클래스는 @Service의 메타 애노테이션인 @Component가직접 사용된 것처럼 컴포넌트 스캔의 대상이 된다.

### Composed-annotation

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Controller // Meta Annotation
@ResponseBody // Meta Annotation
public @interface RestController {
...
}
```

합성(composed) 애노테이션은 하나 이상의 메타 애노테이션이 적용된 애노테이션을 말한다

* 합성 애노테이션을 사용하면 모든 메타 애노테이션이 적용된 것과 동일한 효과를 갖는다

@RestController를 클래스에 적용하면 @Component와 @ResponseBody를 둘 다 사용한것과 동일한 결과를 가져온다.

## 합성 애노테이션 적용

@Configuration도 @Component를 메타 애노테이션으로 가지는 애노테이션이다

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Configuration {
}
```

# 빈 오브젝트의 역할과 구분

![image-20230521193425138](./images//image-20230521193425138.png)

스프링 부트의 빈 구성 정보는 컴포넌트 스캔에 의해서 등록되는 빈과 자동 구성에 의해서 등록되는 빈으로 구분된다. 일반적으로 애플리케이션 인프라 빈은 자동 구성에 의해서 등록되지만 개발자가 작성한 코드 구성 정보에 의해서도 등록할 수도 있다.
자동 구성 메카니즘을 확장하면 애플리케이션 로직을 담은 라이브러리를 만들어 자동 구성에 의해서 등록되도록 만드는 것도 가능하다.

![image-20230521193635699](./images//image-20230521193635699.png)

* SpringBoot 에서는 TomcatServletWebServerFactory와 DispatcherServlet이 무조건 빈으로 등록되어야 정상적으로 동작한다.  



**애플리케이션 로직 빈**

* 애플리케이션의 비즈니스 로직을 담고 있는 클래스로 만들어지는 빈. 컴포넌트 스캐너에 의해서 빈 구성 정보가 생성되고 빈 오브젝트로 등록된다

**애플리케이션 인프라스트럭처 빈** (Infrastructure bean)

* 빈 구성 정보에 의해 컨테이너에 등록되는 빈이지만 애플리케이션의 로직이 아니라 애플리케이션이 동작하는데 꼭 필요한 기술 기반을 제공하는 빈이다.

* 전통적인 **스프링** 애플리케이션에서는 빈으로 등록되지 않지만 **스프링 부트**에서 구성 정보에 의해 빈으로 등록되어지는 **ServletWebServerFactory**나 **DispatcherServlet** 등도 애플리케이션인프라 빈이라고 볼 수 있다.

**컨테이너 인프라스트럭처 빈**

* 스프링 컨테이너의 기능을 확장해서 빈의 등록과 생성, 관계설정, 초기화 등의 작업에 참여하는빈을 컨테이너 인프라스트럭처 빈, 줄여서 컨테이너 인프라 빈이라고 한다. 

* **개발자가 작성한 구성 정보에 의해서 생성되는 게 아니라 컨테이너가 직접 만들고 사용하는 빈**이기 때문에 애플리케이션 빈과 구분한다.

* 필요한 경우 일부 컨테이너 인프라 빈은 주입 받아서 활용할 수 있다. 
* 빈오프젝트를 생성,초기화,관계 등 라이프 사이클을 관리한다. 



# 인프라 빈 구성 정보의 분리

