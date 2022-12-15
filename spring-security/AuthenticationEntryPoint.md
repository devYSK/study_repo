

# **Spring Security** **인증** 예외를 처리하는 AuthenticationEntryPoint



AuthenticationEntryPoint 는 인증되지 않은 사용자에 대한 처리를 담당한다. (핸들러 라고 볼 수 있다)

이 때 처리는, 그냥 http 응답이라던가, 페이지 redirect 라 던가 등 개발자가 정의할 수 있다.

* AuthenticationException (인증 예외)예외가 발생했을떄 처리하는 인터페이스이다.

* 헷갈리지 말아야 하는게, AccessDeniedException( 인가 예외) 가 발생했을 때 처리하는 인터페이스가 아니다. 



```java
/**
 * Used by {@link ExceptionTranslationFilter} to commence an authentication scheme.
 */
public interface AuthenticationEntryPoint {

}
```



#### 인증(Authentication)

- 사용자의 신원을 확인하는 과정

  - 아이디/패스워드 기반 로그인
  - OAuth2.0 프로토콜을 통한 Social 인증



#### 인가(Authorization) 

- 어떤 개체가 어떤 리소스에 접근할 수 있는지 또는 어떤 동작을 수행할 수 있는지를 검증하는 것, 즉 접근 권한을 얻는 일을 의미
- 적절한 권한이 부여된 사용자들만 특정 기능 수행 또는 데이터 접근을 허용함



인증이 되지 않은 사용자 라는것은 아이디 패스워드가 틀렸거나해서 로그인 하지 못한 `비 로그인 사용자` 라는것이다.

그러므로 로그인 페이지 등을 통하여 로그인을 하고 인증을 해야 하는데,  로그인 페이지등으로 이동시켜주거나, 401 에러 응답을 보내주는  역할을 담당하는 인터페이스가 AuthenticationEntryPoint이다.



Spring Security에서 FilterSecurityInterceptor 필터가 마지막으로 인증 및 인가 처리를 하는데, 

- 인증객체 없이 보호자원에 접근 시도하면 AuthenticationException
- 인증 후 자원 접근 권한이 없을 경우 AccessDeniedException

예외를 발생시킨다.



이 때, ExceptionTranslationFilter가 이 예외들을 잡아서 처리하는데 AuthenticationException은 인증되지 않았다는 예외로써,

ExceptionTranslationFilter가 `AuthenticationEntryPoint` 를 이용해서 응답을 하거나, 로그인 페이지로 이동 시킨다.



* ExceptionTranslationFilter 클래스 이며, 이 클래스는 내부적으로 AuthenticationEntryPoint 인터페이스를 필드로 가지고 있다. 

```java
protected void sendStartAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			AuthenticationException reason) throws ServletException, IOException {
		// SEC-112: Clear the SecurityContextHolder's Authentication, as the
		// existing Authentication is no longer considered valid
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		SecurityContextHolder.setContext(context);
		this.requestCache.saveRequest(request, response);
  
		this.authenticationEntryPoint.commence(request, response, reason); // 이부분 호출
	}
```



AuthenticationEntryPoint의 구현체는 여러가지가 있으며, 필요에 맞게 사용하거나, 인터페이스를 구현해서 커스텀해서 사용할 수도 있다. 



<img src="https://blog.kakaocdn.net/dn/p9CV7/btrTGlbOx4T/efcqh0s4bGxsEBFUjCDVW0/img.png" width=750 height=300>

* 그냥 401 응답을 내려주거나
* 에러 페이지로 보내주거나
  * response.sendRedirect("/error.html"); 
* 로그인 페이지로 이동시켜주거나 (LoginUrlAuthenticationEntryPoint)
* 커스텀 해서 사용해도 된다. 



### AuthenticationEntryPoint 구현체 적용



```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return 
      http.exceptionHandling() 					
      .authenticationEntryPoint(new CustomAuthenticationEntryPoint())  // << 인증실패 시 처리				
      .accessDeniedHandler(accessDeniedHandler()) 
      .build();
}
```

