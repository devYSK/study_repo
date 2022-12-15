# FilterChainProxy



* https://docs.spring.io/spring-security/site/docs/3.1.4.RELEASE/reference/security-filter-chain.html



### Spring Security란

> Spring Security는 일반적인 공격에 대한 인증, 권한 부여 및 보호를 제공하는 프레임워크입니다. 
> 명령형 및 반응형 애플리케이션 모두를 보호하기 위한 최고 수준의 지원을 통해 Spring 기반 애플리케이션을 보호하기 위한 사실상의 표준입니다. -스프링 시큐리티 문서-



1. 인증과 인가
2. 일반적인 해킹 공격으로부터 보호(CSRF, 세션고정 등)



> Spring Security의 Servlet 지원은 FilterChainProxy에 포함되어 있습니다. FilterChainProxy는 Spring Security에서 제공하는 특수 필터로 SecurityFilterChain을 통해 많은 Filter 인스턴스에 위임 할 수 있습니다. FilterChainProxy는 Bean이므로 일반적으로 DelegatingFilterProxy로 래핑됩니다.



Spring security는 요청이 들어오면 Servlet FilterChain을 자동으로 구성하고 거치게한다. 

`FilterChain은 여러 filter를 chain형태로 묶어놓은 것이다.`



FilterChainProxy 역시 처리를 위임하기 위한 SecurityFilterChain을 들고 있다. SecurityFilterChain을 담도록 선언되어 있는 필드도 List<>이며 각각의 다른 SecurityFilter들이 들어있는 SecurityFilterChain들이 담길 수 있다.

```java
public class FilterChainProxy extends GenericFilterBean {

	private static final Log logger = LogFactory.getLog(FilterChainProxy.class);

	private static final String FILTER_APPLIED = FilterChainProxy.class.getName().concat(".APPLIED");

	private List<SecurityFilterChain> filterChains; // 이부분. 여러 필터 체인을 가지고 있다.

}
```



즉, 설정에 따라서 SecurityFilterChain이 하나가 될 수도 있고, 여러개가 될 수도 있으며 SecurityFilterChain안에서 필터링 될 Security Filter들을 다르게 설정할 수 있다.
또한 한 요청의 URL 패턴에 따라 SecurityFilterChain 하나를 선택하여 해당 되는 Security Filter 들을 타도록 설정할 수도 있다. 



또한 FilterChainProxy는 고정된 bean name으로 "springSecurityFilterChain" 이며 Bean으로 등록된다.

* WebSecurityConfiguration 클래스에서 Bean으로 등록된다.







## FilterChainProxy

` FilterChainProxy는, Security에서 인증과 인가에 사용되는 과정들을 담은 여러 filter들을 chain 형태로 묶어놓은 클래스이다.` 

* FilterChainProxy 역시 처리를 위임하기 위해 내부적으로  SecurityFilterChain을 들고 있다



FilterChainProxy는 다음과 같은 특징을 가지고 있다.

- springSecurityFilterChain의 이름으로 생성되는 필터 빈이다.
- DelegatingFilterProxy로부터 요청을 위임 받고 실체로 보안을 처리한다.
- 스프링 시큐리티 초기화 시 생성되는 필터들을 관리하고 제어한다.
  - 스프링 시큐리티가 기본적으로 생성하는 필터
    - WebAsyncManager
    - SecurityContextPersistenceFilter
    - HeaderWriterFilter
    - LogoutFilter
    - AnonymousAuthenticationFilter
    - SessionManagementFilter
  - 설정 클래스에서 API 추가 시 생성되는 필터
- 사용자의 요청을 필터 순서대로 호출하여 전달한다.
- 사용자 정의 필터를 생성해서 기존의 필터 전후(앞뒤)로 추가 가능하다.
  - 필터의 순서를 잘 정의해야 한다. 
- 마지막 필터까지 인증 및 인가 예외가 발생하지 않으면 보안을 통과한다.





**FilterChainProxy는 Spring Security 초기화 시, 설정된 API를 바탕으로 만들어진 Filter를 인덱싱 형태로 관리한다.**

* 배열로 관리하고 인덱스로 찾을 수 있다. 순서를 바꿀수도 있다. 

FilterChainProxy가 하는 일은 자기가 관리하는 모든 필터들을 순서대로 호출하며 인증/인가 처리를 해주는 역할을 한다. 현재는 14개로 되어있는데, 사용자가 Security 설정 클래스에서 어떤 설정을 하느냐에 따라 필터가 더 늘어날 수도, 줄어들 수도 있다. 

모두 끝났으면 Spring MVC의 **DispatcherServlet**으로 전달해서 실제 요청에 대한 서블릿 처리를 하게 된다.





## FilterChainProxy의 생성 과정



> WebSecurityConfiguration 클래스에서  webSecurity.build() 메서드 호출 시 springSecurityFilterChain이름의 FilterChainProxy 빈을 생성한다.



filter는 엄밀히 말하면 스프링이 아닌 Servlet 2.3부터 제공되는 `서블릿 기술`이다. 서블릿 필터는 서블릿 컨테이너에서 생성되고 실행된다. 그리고 스프링 빈은 스프링 컨테이너에서 생성되고 실행된다. 따라서 기본적으로 스프링 빈을 필터에서 주입 받아서 사용할 수 없는 구조다.

1. 그러나 **DelegatingFilterProxyRegistrationBean** 은 **DelegatingFilterProxy** 필터를 생성하고,  **ServletContextInitializer을** 통해 서블릿 컨테이너의 필터체인에 DelegatingFilterProxy을 등록하도록 지원한다.

2. DelegatingFilterProxy를 통해 스프링 빈을 필터에서 주입 받아서 사용하고, 
3. DelegatinFilterProxy가 필터로 등록될 때, **springSecurityFilterChain**의 이름으로 등록한다. 
4. 내부적으로 이 이름으로 등록한 이름을 찾는다. 그 이름을 가진 Bean이 바로 **FilterChainProxy**이다.



> DelegatingFilterProxy 덕분에 스프링 빈을 필터에서 주입받아서 사용할 수 있다.
>
> * DelegatingFilterProxy의 등장으로 스프링 빈을 필터에서 주입받아서 사용할 수 있다.
> * DelegatingFilterProxy가 내부적으로 ApplicationContext를 가지고 있음.
>   * 어디서 호출하는진 모르곘는데.. setApplicationContext()로 ApplicationContext를 받음
>
> * DelegatingFilterProxy.initDelegate(WebApplicationContext)로 getBean(targetBeanName, Filter.class)를 호출하여 빈을 찾아 사용
>
> * [필터(Filter)가 스프링 빈 등록과 주입이 가능한 이유](https://mangkyu.tistory.com/221)
>





### Spring Bean Filter가 등록되는 과정

1. SecurityFilterAutoConfiguration 클래스가 호출된다. 여기서 DEFAULT_FILTER_NAME으로 `DelegatingFilterProxyRegistrationBean이 생성`된다.
   * DEFAULT_FILTER_NAME은 "springSecurityFilterChain"
   * AbstractSecurityWebApplicationInitializer 클래스에 정의되어 있다.
2.  **DelegatingFilterProxyRegistrationBean** 은 **DelegatingFilterProxy** 필터를 생성.
   *  **ServletContextInitializer을** 통해 서블릿 컨테이너의 필터체인에 DelegatingFilterProxy을 등록.
3. WebSecurityConfiguration 클래스 에서 springSecurityFilterChain()를 통해 this.webSecurity.build()를 통해 FilterChainProxy를 만든다.
4. webSecurity.build() 통해 doBuild() 메서드로 넘어오게 된다. 여기서 init() 메서드를 호출하면 우리가 설정한 Security 설정 정보를 바탕으로 FilterChainProxy를 만들기 시작한다. 



>  SecurityFilterAutoConfig를 통해서 "springSecurityFilterChain"이라는 이름의 빈을 등록해주었고, WebSecurityConfiguration 클래스에서 실제로 FilterChainProxy를 만들어준다.



* DelegatingFilterProxy : 들어온 요청을 서블릿으로 전달하기 전에 Security filter에 `위임을 해서` 인증/인가 처리를 하고 Servlet으로 요청을 전달해주는 역할을 하는 서블릿 `필터`



>  Filter에서 @Service가 붙은 스프링 빈을 주입받아서 특정 메서드를 사용할 때, 메서드 안에서 Exception이 발생한다면 @ExceptionHandler로 처리할 수 `없다`. 
>
> 필터는 스프링이 아닌 웹 서버의 영역에서 실행되므로 스프링 기술인 @ExceptionHandler 영역에 도달하기 전에 예외가 발생하므로 에러 처리가 되지 않는다.



## 필터를 통해  Request를 전달하는 과정

<img src="https://blog.kakaocdn.net/dn/okZI6/btrTGbnAVR8/5AkduaqIT5cKAkH2NgDE2K/img.png" width=900 height=350>

왼쪽은 서블렛 컨테이너, 오른쪽은 스프링 컨테이너의 영역이다.



1. 시작을 하게 되면 서블릿 컨테이너와 스프링 컨테이너로 나누어져있다. 주로 서블릿 컨테이너는 Tomcat, WAS로 대변되고, 스프링 컨테이너는 WAS에서 들어오는 요청을 처리해주는 형식이다. 
2. 사용자가 처음 요청을 하면, 서블릿 컨테이너에서 요청을 받는다. 이 때 여러 서블릿 필터들이 처리를 하게 된다. 이 필터 중에 DelegatingFilterProxy 클래스가 있다. 이 클래스는 사용자의 요청이 오면 "springSeucirytFilterChain"이라는 이름으로 된 스프링 빈을 찾아와서, delegate.request()를 이용해 요청을 위임한다. 
3. springSecurityFilterChain 이름으로 스프링 컨테이너에 등록된 스프링빈은 FilterChainProxy 클래스다. 
4. FilterChainProxy는 리스트 형식으로 스프링 시큐리티에서 사용할 서블릿 필터 타입의 스프링 빈을 가지고 있다. 리스트 형식이기 때문에 실행 순서가 보장되는 방식으로 필터의 체이닝이 시작된다. 
5. FilterChianProxy에서 마지막 FilterSecurityInterceptor까지 완료되면 DispatcherServlet으로 사용자 요청을 넘겨준다. 즉, Spring Web 계층으로 사용자 요청을 넘겨주는 셈이다. 





## FilterChainProxy를 구성하는 Filter 목록

- 정말 다양한 필터 구현을 제공함
- 결국 Spring Security를 잘 이해하고 활용한다는 것은 이들 Filter 를 이해하고, 적절하게 사용한다는 것을 의미함

[주요 Security Filter](https://www.notion.so/6983fc3e2aa4420a8a02e38574e849a0)

* `ChannelProcessingFilter` : 웹 요청이 어떤 프로토콜로 (http 또는 https) 전달되어야 하는지 처리 
* `SecurityContextPersistenceFilter` : SecurityContextRepository를 통해 SecurityContext를 Load/Save 처리
* `LogoutFilter` : 로그아웃 URL로 요청을 감시하여 매칭되는 요청이 있으면 해당 사용자를 로그아웃 시킴
* `UsernamePasswordAuthenticationFilter` : ID/비밀번호 기반 Form 인증 요청 URL(기본값: /login) 을 감시하여 사용자를 인증함
* `DefaultLoginPageGeneratingFilter` : 로그인을 수행하는데 필요한 HTML을 생성함
* `RequestCacheAwareFilter` : 로그인 성공 이후 인증 요청에 의해 가로채어진 사용자의 원래 요청으로 이동하기 위해 사용됨
* `SecurityContextHolderAwareRequestFilter` : 서블릿 3 API 지원을 위해 HttpServletRequest를 HttpServletRequestWrapper 하위 클래스로 감쌈
* `RememberMeAuthenticationFilter` : 요청의 일부로 remeber-me 쿠키 제공 여부를 확인하고, 쿠키가 있으면 사용자 인증을 시도함
* `AnonymousAuthenticationFilter` : 해당  인증 필터에 도달할때까지 사용자가 아직 인증되지 않았다면, 익명 사용자로 처리하도록 함
* `ExceptionTranslationFilter` : 요청을 처리하는 도중 발생할 수 있는 예외에 대한 라우팅과 위임을 처리함
* `FilterSecurityInterceptor` : 접근 권한 확인을 위해 요청을 AccessDecisionManager로 위임





## 요청 URL 마다 다르게 필터체인을 설정하는법 

이 Filter Chain들은 여러 Chaie을 만들 수 있으며 여러 Chain들을 만들 시에는 @Order를 반드시 정해야 한다.



```java
@Configuration
@Import({SecurityConfig.FooSecurityConfig.class, SecurityConfig.BarSecurityConfig.class, SecurityConfig.AllSecurityConfig.class})
public class SecurityConfig {


  @Order(100)
  static class FooSecurityConfig extends WebSecurityConfigurerAdapter {

      @Override
      protected void configure(HttpSecurity http) throws Exception {
          http.mvcMatcher("/foo/**");
      }

  }

  @Order(200)
  static class BarSecurityConfig extends WebSecurityConfigurerAdapter {

      @Override
      protected void configure(HttpSecurity http) throws Exception {
          http.mvcMatcher("/bar/**");
      }

  }

  @Order(300)
  static class AllSecurityConfig extends WebSecurityConfigurerAdapter {

      @Override
      protected void configure(HttpSecurity http) throws Exception {
          http.mvcMatcher("/**");
      }

  }
```

* 이렇게 되면, 각 URL 마다 다르게 필터체인들을 설정할 수 있다. 

* Order를 정해야만 하는 이유는 SecurityFilterChain 들이 FilterChainProxy의 filterChains 리스트에 담기는 순서가 중요하기 때문이다.

* 만약, AllSecurityConfig의 Order가 가장 낮다면 모든 요청은 해당 SecurityFilterChain에 걸리게 됩니다. 아무리 /foo/***\***, /bar/**  를 보내도 원하는 FilterChain에 도달하지 않고 /** 에 먼저 걸리기 때문이다

* 우선순위에 맞게 요청이 필터체인에 걸린다. 



`filterChain은 하나의 SecurityConfig로 만들어지고 여러개의 FilterChain을 만든다면 URL 패턴 정의와 그에 따른 순서 조정이 필요하다`





### 참조

* 인프런 정수원님 Spring Security 강의
* https://mangkyu.tistory.com/

* https://velog.io/@yaho1024/Spring-Security-FilterChainProxy
