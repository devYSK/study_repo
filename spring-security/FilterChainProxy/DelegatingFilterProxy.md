# DelegatingFilterProxy



* delegating의 사전적 의미 : 위임하다



DelegatingFilterProxy : 들어온 요청을 서블릿으로 전달하기 전에 Security filter에 `위임을 해서` 인증/인가 처리를 하고 Servlet으로 요청을 전달해주는 역할을 하는 서블릿 `필터`

* javax.servlet.filter 인터페이스를 구현하고 있다.

 

Spring Security를 사용한다면, DelegatingFilterProxy가 생성된다. Spring Boot 사용시 **SecurityFilterAutoConfiguration을** 통해서 **DelegatingFilterProxyRegistrationBean** 빈을 생성하며  **DelegatingFilterProxyRegistrationBean** 은 **DelegatingFilterProxy** 필터를 생성하고,  **ServletContextInitializer을** 통해 서블릿 컨테이너의 필터체인에 DelegatingFilterProxy을 등록하도록 지원한다.



## **서블릿 필터와 스프링 빈**

스프링에서 우리는 필터라는 기능을 자주 사용한다. 이 필터는 요청이 들어왔을 때, DispatcherServlet에 요청이 전달되기 전 과정까지 무언가 `전처리`를 해준다. 그리고 DispatcherServlet에서 응답이 나갈 때도 다시 한번 필터를 거쳐서 나가는 형태가 된다. 

스프링에서 필터 기술을 지원하는 것처럼 보이기 때문에 필터를 스프링 기술이라고 생각할 수 있다. Filter는 엄밀히 말하면 스프링이 아닌 Servlet 2.3부터 제공되는 서블릿 기술이다. **서블릿 필터는 서블릿 컨테이너에서 생성되고 실행된다. 그리고 스프링 빈은 스프링 컨테이너에서 생성되고 실행된다. 따라서 기본적으로 스프링 빈을 필터에서 주입 받아서 사용할 수 없는 구조다.** 

* DelegatingFilterProxy의 등장으로 스프링 빈을 필터에서 주입받아서 사용할 수 있다.
* DelegatingFilterProxy가 내부적으로 ApplicationContext를 가지고 있음.
  * 어디서 호출하는진 모르곘는데.. setApplicationContext()로 ApplicationContext를 받음

* DelegatingFilterProxy.initDelegate(WebApplicationContext)로 getBean(targetBeanName, Filter.class)를 호출하여 빈을 찾아 사용

* [필터(Filter)가 스프링 빈 등록과 주입이 가능한 이유](https://mangkyu.tistory.com/221)





필터의 역할은 어떤 요청이 있을 때 이 요청이 실제로 서블릿으로 들어오는데, 서블릿 자원에 들어오기 전에 처리를 하는 곳이 필터이다.

* 즉, 요청에 대한 최종적인 접근 전, 후로 어떤 처리를 할 수 있도록 하는 기술이 필터이다.



## **DelegatingFilterProxy (서블릿 필터와 스프링 빈 필터의 연결 매개체)** 

Spring Security는 모든 요청에 대해서 필터를 기반으로 인증/인가처리를 한다. 

개발자는 @EnableWebSecurity 가 선언된 SecurityConfigClass에서 시큐리티 설정을 한다. 

즉,  개발자가 등록한 스프링 빈이 서블릿 필터에 영향을 준다는 것이다.



<img src="https://blog.kakaocdn.net/dn/dqCIx9/btrTFKiImUt/HK5QKhFGxDGGrFHwFq9S4K/img.png" width=800 height=250>

DelegatingFilterProxy 는 특정한 이름을 가진 **스프링 빈을** **찾아 그** **빈에게** 요청을 위임한다

* springSecurityFilterChain **이름으로 생성된 빈을** **ApplicationContext** **에서** **찾아** **요청을** **위임**

* DelegatingFilterProxy는 그냥 위임하는것 뿐이지 실제 보안처리를 하지 않는다.



## DelegatingFilterProxy 동작 순서

1. SecurityFilterAutoConfiguration 클래스에서 DelegatingFilterProxyRegistrationBean를 빈으로 등록한다.
2. DelegatingFilterProxyRegistrationBean 클래스에서 getFilter()를 호출하며 DelegatingFilterProxy 객체를 생성한다.

3. DelegatingFilterProxy클래스에서 this.setTargetBeanName(targetBeanName) DelegatingFilterProxy로 요청이 들어올 때 처리를 위임 할 스프링 빈 이름(springSecurityFilterChain)을 설정한다.

4. 요청이 들어오면 가장 먼저 DelegatingFilterProxy의 doFilter() 함수가 호출된다. doFilter() 함수는 요청을 위임할 필터(springSecurityFilterChain)를 찾아서 해당 요청을 위임(invokeDelete()) 호출 한다.
5. delegate 라는 내부 Filter 변수로 요청을 위임하는데, 이 delegate가 `FilterChainProxy` 이다.

6. invokeDelete() 함수 호출 시 내부에서 FilterChainProxy의 doFilter() 함수를 호출하고, doFilterInternal() 함수에서는 등록 된 Filter 목록을 가지고 인증/인가 처리를 진행한다.

<img src="https://blog.kakaocdn.net/dn/M5YNW/btrTEYuZwMo/OWXQFxl5fgNJqFWvBH8KN0/img.png" width=650 height=650>



###  요청은 어떻게 FilterChainProxy로 전달될까? - 위 동작순서의 좀더 디테일한 순서 

* 최초 DelegatingFilterProxy에 도착.

- 웹 요청을 수신한 서블릿 컨테이너는 해당 요청을 

  DelegatingFilterProxy (javax.servlet.Filter 인터페이스 구현체) 로 전달함

  - DelegatingFilterProxy Bean은 SecurityFilterAutoConfiguration 클래스에서 자동으로 등록됨

```java
@Bean
@ConditionalOnBean(name = DEFAULT_FILTER_NAME)
public DelegatingFilterProxyRegistrationBean securityFilterChainRegistration(SecurityProperties securityProperties) {
	DelegatingFilterProxyRegistrationBean registration = new DelegatingFilterProxyRegistrationBean(DEFAULT_FILTER_NAME);
	registration.setOrder(securityProperties.getFilter().getOrder());
	registration.setDispatcherTypes(getDispatcherTypes(securityProperties));
	return registration;
}
```

SecurityFilterAutoConfiguration 구현 발췌 (DelegatingFilterProxyRegistrationBean 을 통해 DelegatingFilterProxy 인스턴스를 생성함)

- DelegatingFilterProxy는 실제적으로 웹 요청을 처리할 Target Filter Bean을 지정해야함
  - Target Filter Bean은 바로 앞에서 알아본 FilterChainProxy

```java
@Override
public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws ServletException, IOException {
	// Lazily initialize the delegate if necessary.
	Filter delegateToUse = this.delegate;
	if (delegateToUse == null) {
		synchronized (this.delegateMonitor) {
			delegateToUse = this.delegate;
			if (delegateToUse == null) {
				WebApplicationContext wac = findWebApplicationContext();
				if (wac == null) {
					throw new IllegalStateException("No WebApplicationContext found: " +
							"no ContextLoaderListener or DispatcherServlet registered?");
				}
				delegateToUse = initDelegate(wac);
			}
			this.delegate = delegateToUse;
		}
	}
	// Let the delegate perform the actual doFilter operation.
	invokeDelegate(delegateToUse, request, response, filterChain);
}

protected Filter initDelegate(WebApplicationContext wac) throws ServletException {
	String targetBeanName = getTargetBeanName();
	Assert.state(targetBeanName != null, "No target bean name set");
	Filter delegate = wac.getBean(targetBeanName, Filter.class);
	if (isTargetFilterLifecycle()) {
		delegate.init(getFilterConfig());
	}
	return delegate;
}

protected void invokeDelegate(Filter delegate, ServletRequest request, ServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {
	delegate.doFilter(request, response, filterChain);
}
```



* 요청을 위임하는 프록시 필터

<img src="https://blog.kakaocdn.net/dn/4Xttp/btrTHOxTJfT/2ExZLXuqOrcCY6Dj1u9iK0/img.png" width=650 height=600>

* https://docs.spring.io/spring-security/site/docs/current/reference/html5/#servlet-architecture

* 딜리게이팅 필터 프록시가 내부적으로 시큐리티 필터 체인을 참조하고있고, 시큐리티 필터들을 지난 후에 다른 필터를 지나 서블렛으로 요청 전달



> * DelegatingFilterProxy는 들어온 요청을 서블릿으로 전달하기 전에 Security filter에 위임을 해서 처리를 하고 Servlet으로 요청을 전달해주는 역할을 한다.
> * 즉, DelegatingFilterProxy는 모든 요청을 받으면 Spring Context에서 "springSecurityFilterChain"이름으로 등록된 빈을 찾아와 인증/인가 처리를 위임한다.
> * 실제 인증/인가 처리는 FilterChainProxy가 하며, FilterChainProxy가 가지고 있는 Filters들 중에서 선택해서 순서대로 처리해준다. 









### 참조

* https://velog.io/@yaho1024/spring-security-delegatingFilterProxy
* 인프런 정수원님 Security 강의
* https://ojt90902.tistory.com/832



