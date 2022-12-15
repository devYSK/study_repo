# ExceptionTranslationFilter (예외 전환 필터 )

FilterSecurityInterceptor 바로 위에 위치하며, FilterSecurityInterceptor 실행 중 발생할 수 있는 예외(AccessDeniedException과 AuthenticationException을)를 잡고 처리한다

* FilterSecurityInterceptor는 FilterChainProxy에서 필터 가장 마지막에 위치한다.

- 필터 체인에서 발생하는 AccessDeniedException과 AuthenticationException을 처리하는 필터

* 처리할 수 없는 예외는 rethrow 하여 그냥 앞으로 던져버린다.



> 필터 체인(Filter Chain) 상에서 ExceptionTranslationFilter의 위치를 주의해서 볼 필요가 있다.
>
>  ExceptionTranslationFilter는 필터 체인 실행 stack에서 `자기 아래에 오는 필터들`에서 발생하는 예외들에 대해서만 처리할 수 있다. 커스텀 필터를 추가해야 하는 경우 이 내용을 잘 기억하고, 커스텀 필터를 적당한 위치에 두어야 한다.
>
> * 즉 아무것도 안건드린 default 필터 순서 상에서는, FilterSecurityInterceptor에서 발생하는 에외들만 처리 가능.
>   * 추가적으로 처리하고 싶다면 ExceptionTranslationFilter 뒤나 앞에 HandlingFilter를 추가하면 된다.  
> * Security 필터들은 순서가 정해져 있으므로 순서를 잘 맞춰야 한다.
> * 이 때, HttpSecurity 객체의 addFilterBefore()와 addFilterAfter() 메소드를 이용해서 필터 순서를 지정할 수 있다. 
> * HttpSecurity 객체의 addFilterBefore(new 커스텀 필터(), 커스텀 필터 뒤에 둘 필터)
>   * 뒤 인자는 기존 필터를 받는데, 이 필터 앞에 설정하겠다는것 
> * HttpSecurity 객체의 addFilterAfter(new 커스텀 필터(), 커스텀 필터 앞에 둘 필터)
>   * 뒤 인자는 기존 필터를 받는데, 이 필터 뒤에 설정하겠다는것 



1. ExceptionTranslationFilter 보다 아래에 위치한 필터들 실행
2. 예외가 발생했을 때, 처리할 수 없는 예외이면 그냥 rethrow해서 밖으로 던짐
3. 처리가 가능한 예외면 (AuthenticationException과 AccessDeniedException 예외) 예외 처리
   * AuthenticationException 예외 인 경우 handleAuthenticationException() 호출
     * AuthenticationException 예외 인 경우 간단하게 사용자를 인증가능한 페이지로 리다이렉트
   * AccessDeniedException 예외 인 경우 handleAccessDeniedException() 호출
     * 사용자가 인증되지 않은 익명 사용자이거나 RemeberMe를 통한 인증된 사용자 인 경우에는
       * 사용자를 인증가능한 페이지로 리다이렉트
     * 익명사용자도 아니고 RememberMe로 인증된 사용자가 아니라면 
       * accessDeniedHandler를 통해 처리 - 403 응답 



### AccessDeniedException 예외 발생 순서

- AbstractSecurityInterceptor.beforeInvocation() 호출
- attemptAuthorization(object, attributes, authenticated); 호출
- this.accessDecisionManager.decide(authenticated, object, attributes); 호출
  - decide 메소드는  액세스 제어 결정을 확인
  - 내부적으로 Voter를 이용한 연산을 하는데, 여기서 권한에 맞지 않으면 throw AccessDeniedException()



#### Voter

Spring Security는 **투표를 기반**으로 request에 대한 access에 대한 승인 여부를 결정

* https://00hongjun.github.io/spring-security/accessdecisionmanager/







인증(Authentication)

- 사용자의 신원을 확인하는 과정

  - 아이디/패스워드 기반 로그인
  - OAuth2.0 프로토콜을 통한 Social 인증



인가(Authorization) 

- 어떤 개체가 어떤 리소스에 접근할 수 있는지 또는 어떤 동작을 수행할 수 있는지를 검증하는 것, 즉 접근 권한을 얻는 일을 의미
- 적절한 권한이 부여된 사용자들만 특정 기능 수행 또는 데이터 접근을 허용함





## AuthenticationException과 AccessDeniedException 예외

- AuthenticationException 예외는 인증 관련 예외이며, 사용자를 로그인 페이지로 보냄
- AccessDeniedException 예외는 AccessDecisionManager에 의해 접근 거부가 발생했을 때 접근 거부 페이지를 보여주거나 사용자를 로그인 페이지로 보냄



* AuthenticationException 예외는 AuthenticationEntryPoint를 호출하여 인증 가능하도록(로그인 등) 리다이렉트 등을 한다.
* AccessDeniedException 예외는 AccessDeniedHandler를 호출하고 AccessDeniedHandler의 기본 구현체는 AccessDeniedHandlerImpl이고, 내부 handle() 메소드에서 errorPage가 정의되어 있지 않으면 그냥 response.sendError()로 403응답과 에러 메시지를 리턴한다. 



### 인증 예외 (AuthenticationException) (권한이 요구되는 페이지에 권한없이 접근할 때)

1. AuthenticationEntryPoint 호출

   - 로그인 페이지 이동, 401 오류 코드 전달, SecurityContext 초기화

   - AuthenticationEntryPoint를 직접 구현해서 인증 예외 처리를 할 수도 있다.

   - *this.authenticationEntryPoint.commence(request, response, reason);*

2. 인증 예외가 발생하기 전의 요청 정보를 저장
   - RequestCache 인터페이스 : 사용자의 이전 요청 정보를 세션에 저장하고 이를 꺼내 오는 캐시 메커니즘
     - 사용자가 다시 로그인하여 성공 후 이전 가고자 했던 리소스 정보를 캐시에서 가지고 와서 해당 리소스로 이동하게 됨
     - SavedRequest 인터페이스 : 사용자가 요청했던 request paramter, header 값들을 저장



RequestCacheAwareFilter는 requestCache에 값이 존재하는 경우 해당 값을 꺼내서 다음 Filter로 넘겨주는 역할을 한다.

RequestCacheAwareFilter에서 전달 된 requestCache의 값을 가지고 인증 성공 시 이전 요청된 정보를 가지고 해당 리소스로 이동한다.



>  AuthenticationEntryPoint 는 SpringSecurity가 구현체를 제공한다
>
> - **AuthenticationEntryPoint 사용자가 직접 구현해서 호출할수도 있다,**
>
> 인증된 사용자만 서버자원에 접근이 가능한데, 만약 인증없이 서버자원에 접근할려면 로그인 페이지로 이동하게 처리한다. 
>
> 이때 인증 예외가 발생하기전에 그 사용자가 가고자 했던 자원정보를 저장한다. 
>
> 그렇게 된다면 로그인에 성공을 하게 되면 이전에 사용자가 접근하려고 했던 서버자원으로 이동하게 처리한다 (RequestCache)





### 인가 예외 (AccessDeniedException) (권한이 요구되는 페이지에 요구되는 권한이 아닌걸 가지고 요청할 때)

- 익명 사용자라면 AuthenticationEntryPoint 실행
- 익명 사용자가 아니면(인증된 사용자가 아니라면) AccessDeniedHandler에게 예외를 처리하도록 호출 - 위임
- AccessDeniedException 예외는 AccessDecisionManager에 의해 접근 거부가 발생했을 때 접근 거부 페이지를 보여주거나 사용자를 로그인 페이지로 보냄



- **AccessDeniedException -> AccessDeniedHandler ->response.redirect(/denied) 이자원에 접근할수 없다고 확인 하는 페이지를 호출한다** 
- AccessDeniedHandler의 기본 구현체는 AccessDeniedHandlerImpl이고, 내부 handle() 메소드에서 errorPage가 정의되어 있지 않으면 그냥 response.sendError()로 403응답과 에러 메시지를 리턴한다. 
  - 에러페이지가 정의되어있다면 에러페이지로 forward 시킨다. 

![image-20221215085009172](/Users/ysk/study/study_repo/spring-security/filter/images//image-20221215085009172.png)



#### 다시한번 정리



1. ExceptionTranslationFilter 보다 아래에 위치한 필터들 실행
2. 예외가 발생했을 때, 처리할 수 없는 예외이면 그냥 rethrow해서 밖으로 던짐
3. 처리가 가능한 예외면 (AuthenticationException과 AccessDeniedException 예외) 예외 처리
   * AuthenticationException 예외 인 경우 handleAuthenticationException() 호출
     * AuthenticationException 예외 인 경우 간단하게 사용자를 인증가능한 페이지로 리다이렉트
   * AccessDeniedException 예외 인 경우 handleAccessDeniedException() 호출
     * 사용자가 인증되지 않은 익명 사용자이거나 RemeberMe를 통한 인증된 사용자 인 경우에는
       * 사용자를 인증가능한 페이지로 리다이렉트
     * 익명사용자도 아니고 RememberMe로 인증된 사용자가 아니라면 
       * accessDeniedHandler를 통해 처리 - 403 응답 
       * AccessDeniedHandler의 기본 구현체는 AccessDeniedHandlerImpl이고, 내부 handle() 메소드에서 errorPage가 정의되어 있지 않으면 그냥 response.sendError()로 403응답과 에러 메시지를 리턴한다. 
         - 에러페이지가 정의되어있다면 에러페이지로 forward 시킨다. 





## 예외처리 기능 작동할 수 있도록 하는법

```java
protected void configure(HttpSecurity http) throws Exception {
	 http.exceptionHandling() 					
		.authenticationEntryPoint(authenticationEntryPoint())     		// 인증실패 시 처리
		.accessDeniedHandler(accessDeniedHandler()) 			// 인증실패 시 처리
}
```



업그레이드 된 버전

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return 
      http.exceptionHandling() 					
      .authenticationEntryPoint(authenticationEntryPoint())     		// 인증실패 시 처리					
      .accessDeniedHandler(accessDeniedHandler()) 			// 인증실패 시 처리
      .build();
}
```



### AccessDeniedHandler 커스텀



```java
@Configuration
@EnableWebSecurity
public class WebSecurityConfigure {

    private final Logger log = LoggerFactory.getLogger(WebSecurityConfigure.class);

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http.authorizeRequests()
            .antMatchers("/me").hasAnyRole("USER", "ADMIN")
            .antMatchers("/admin").access("hasRole('ADMIN') and isFullyAuthenticated()") // 어드민 권한은 가진 사용자이고 리멤버미를 통하여 인증된 사용자가 아닌사용자만
            .and()
          
            .exceptionHandling() // 이부분 
          	  .accessDeniedHandler(accessDeniedHandler())
            .and()
            .build();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() { // 커스텀하기 
        return (req, res, e) -> {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Object principal = authentication != null ? authentication.getPrincipal() : null;

            log.warn("{} is denied", principal, e);

            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
            res.setContentType("text/plain");

            res.getWriter().write("## ACCESS DENIED!! ##");
            res.getWriter().flush();
            res.getWriter().close();
        };
    }
}
```

* httpSecurity.exceptionHandling()
            	  .accessDeniedHandler(커스텀 액세스 디나이 핸들러()) 를 추가하면 된다. 





## UsernamePasswordAuthenticationFilter에서 발생한 인증 에러는?

- public class UsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter

  - AbstractAuthenticationProcessingFilter.unsuccessfulAuthentication(request, response, failed);

    ```java
    Authentication authResult;
    
    try {
        authResult = attemptAuthentication(request, response);
        if (authResult == null) {
            // return immediately as subclass has indicated that it hasn't completed
            // authentication
            return;
        }
        sessionStrategy.onAuthentication(authResult, request, response);
    }
    catch (InternalAuthenticationServiceException failed) {
        logger.error(
                "An internal error occurred while trying to authenticate the user.",
                failed);
        unsuccessfulAuthentication(request, response, failed);
    
        return;
    }
    catch (AuthenticationException failed) {
        // Authentication failed
        unsuccessfulAuthentication(request, response, failed);
    
        return;
    }
    ```

    

* SimpleUrlAuthenticationFailureHandler.saveException(request, exception)

```java
// public class SimpleUrlAuthenticationFailureHandler implements
		AuthenticationFailureHandler
public void onAuthenticationFailure(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException exception)
			throws IOException, ServletException {

    if (defaultFailureUrl == null) {
        logger.debug("No failure URL set, sending 401 Unauthorized error");

        response.sendError(HttpStatus.UNAUTHORIZED.value(),
            HttpStatus.UNAUTHORIZED.getReasonPhrase());
    }
    else {
        saveException(request, exception);

        if (forwardToDestination) {
            logger.debug("Forwarding to " + defaultFailureUrl);

            request.getRequestDispatcher(defaultFailureUrl)
                    .forward(request, response);
        }
        else {
            logger.debug("Redirecting to " + defaultFailureUrl);
            redirectStrategy.sendRedirect(request, response, defaultFailureUrl);
        }
    }
}

protected final void saveException(HttpServletRequest request,
			AuthenticationException exception) {
    if (forwardToDestination) {
        request.setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, exception);
    }
    else {
        HttpSession session = request.getSession(false);

        if (session != null || allowSessionCreation) {
            request.getSession().setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION,
                    exception);
        }
    }
}
```

