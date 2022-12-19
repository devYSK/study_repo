# SessionManagementFilter, ConcurrentSessionFilter - 세션 관리 필터

SpringSecurity에서 세션 관리에 관련된 기능을 제공해주는 필터이다 

* https://docs.spring.io/spring-security/site/docs/5.1.5.RELEASE/reference/htmlsingle/#session-mgmt

* 필터 순서가 뒷 부분에 존재한다.
* ConcurrentSessionFilter와 함께 동시세션 만료를 해준다 



SessionManagementFilter는 다음과 같은 역할을 한다.

1. 세션 관리 : 인증시 사용자의 세션정보를 등록, 조회, 삭제 등의 세션이력을 관리
2. 동시적 세션 제어 : 동일 계정 - 접속 허용 최대 세션수 제한
   * 동시 세션을 확인하고, 필요시 세션을 만료처리해준다
3. 세션 고정 보호(session-fixation attack protection) : 인증할 때마다 세션 쿠키 새로 발급 → 공격자 쿠키 조작 방지
4. 세션 생성 정책 설정 : 세션이 생성될때 어떻게 생성될것인지 정책을 설정한다
   - IF_REQUIRED — 필요시 생성함 (기본값, default )
   - NEVER — Spring Security에서는 세션을 생성하지 않지만, 세션이 존재하면 사용함
   - STATELESS — 세션을 완전히 사용하지 않음 (JWT 인증이 사용되는 REST API 서비스에 적합)
   - ALWAYS — 항상 세션을 생성함



> SessionManagementFilter는 인증하는 과정에서 동시 세션을 확인하고, 필요시 세션을 만료처리해준다. 



SessionManagementFilter에서 가장 중요한 기능은 세션 고정 보호 (session-fixation protection)을 막는것이다.



### session-fixation attack - 세션 고정 공격

* session-fixation attack — 세션 하이재킹 기법중 하나로 정상 사용자의 세션을 탈취하여 인증을 우회하는 기법

*  https://secureteam.co.uk/articles/web-application-security-articles/understanding-session-fixation-attacks/
* https://www.geeksforgeeks.org/session-fixation-attack/



> sessionId를 이용당해서 누군가에의해서 권한을 빼앗길 수 있는 문제이다.
> 보통 처음 사이트에 접속하면 sessionId key(이하 key)를 발급 받아 서비스를 이용하게된다. 만약 이 key가 내가 서비스로부터 직접 발급받은 것이 아닌 누군가 미리 발급받아 몰래 넣어둔 것이라면 그 key로 내가 로그인 해버리 순간 그 권한을 침투자도 사용할 수 있게되는 것이다. 핵심은 ‘첫 접속 시 발급받은 key’ 이다. 이미 로그인 된 key를 훔쳐가는 부분은 CSRF나 CORS의 영역.
>
> https://mystria.github.io/archivers/fail-case-sessioin-fixation



인증 전에 발급 받은 세션 ID가 인증 후에도 동일하게 사용되면 발생할 수 있는 문제다

- 즉, 인증 전에 사용자가 가지고 있던 세션이 인증 후에는 사용되지 않도록 하면 해당 공격에 효과적으로 대응할 수 있음



## Session Managing 커스텀

- Custom AuthenticationProcessingFilter 사용 시 Security Config의 sessionManagement의 설정이 동작하지 않을 수 있음
- 다르게 말하면, 직접 만든 AuthenticationProcessingFilter를 사용한다면, 별도로 strategy를 적용해주어야 함
- Filter에 해당 로직을 넣지 말고,

```java
oAuth2ProcessingFilter.setSessionAuthenticationStrategy(new SessionFixationProtectionStrategy());
http.addFilterBefore(oAuth2ProcessingFilter, UsernamePasswordAuthenticationFilter.class);
```

이런 방식(WebSecurityConfigurerAdapter에서 HttpSecurity의 config)도 가능



## Session Fixation 해결책

- 로그인 후 sessionId를 새로 발급해주면 해결(간단!)
- 확인해보니 Spring 기본 설정으로 방어 로직(session migration)이 적용되어 있다?



## 세션 설정 - SecurityConfig

```java
@Configuration
@EnableWebSecurity
public class WebSecurityConfigure {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http

            .sessionManagement()
            .sessionFixation().changeSessionId() // 전략 설정 가능
            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // 세션 생성 전략
            .invalidSessionUrl("/")
            .maximumSessions(1)
            .maxSessionsPreventsLogin(false)
            .and()

            .build();
    }

}

```



### 세션 고정 보호 정책 설정 - sessionFixation()

Spring Security에서는 4가지 설정 가능한 옵션을 제공한다

이 중 써블릿 3.1 이상 사용시 default 정책으로 changeSessionId()를 사용하고,

써블릿 3.0 이하 사용 시 migrateSession()을 default 정책으로 사용한다. 

```java
http.sessionManagement().sessionFixation().전략설정()
```



- none() — 방지 하지 않는다. (세션을 그대로 유지한다)
- newSession() — 매번 새로운 세션을 만들고, 기존 데이터는 복제하지 않는다(SpringSecurity 속성만 복사)
- migrateSession() — 인증 성공 시 새로운 세션을 만들고, 데이터를 모두 복제한다
- changeSession() — 인증 성공시 새로운 세션을 만들지 않지만, session-fixation 공격을 방어함 (단, servlet 3.1 이상에서만 지원)



### 세션 생성 정책 설정 - sessionCreationPolicy()

세션이 생성될때 어떻게 생성될것인지 정책을 설정한다

```java
http.sessionManagement()
  .sessionCreationPolicy(SessionCreationPolicy.전략);
```

- IF_REQUIRED — 필요시 생성함 (기본값, default )
- NEVER — Spring Security에서는 세션을 생성하지 않지만, 세션이 존재하면 사용함
- STATELESS — 세션을 완전히 사용하지 않음 (JWT 인증이 사용되는 REST API 서비스에 적합)
- ALWAYS — 항상 세션을 생성함



### 유효하지 않은 세션을 리다이렉트 시킬 URL 설정 - invalidSessionUrl(path)

```java
http.sessionManagement()
	.invalidSessionUrl("/")
```



### 세션 동시성 제어 - maximumSessions()

동일 사용자의 중복 로그인 감지 및 처리를 하기 위해 추가 로그인을 막을지 여부 설정 (기본값, false)

```java
http.sessionManagement()
  .sessionManagement(1)
  .maxSessionsPreventsLogin(false);

```



- maximumSessions(갯수) — 동일 사용자의 최대 동시 세션 갯수
- maxSessionsPreventsLogin() — 최대 갯수를 초과하게 될 경우 인증 시도를 차단할지 여부 (기본값 false)
  - 추가 로그인을 막을지 여부이다. 
  - 세션이 1개인데 `false로 두고` 다중 로그인을 하면 로그인이 되면서
    다른로그인 중이던 아이디는 세션이 해제되고 session expire라는 문구가 나온다.
  - 반대로 `true로 두고` 다중 로그인을 시도하면 아예 로그인이 안된다

- 기본적으로는 한 계정으로 여러 로그인 할 수 있게 설정 되어 있다.





## SessionManagementFilter Code

SessionManagementFilter는 인증하는 과정에서 동시 세션을 확인하고, 필요시 세션을 만료처리해준다. 

```java
public class SessionManagementFilter extends GenericFilterBean {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
	}

	private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if (request.getAttribute(FILTER_APPLIED) != null) {
			chain.doFilter(request, response);
			return;
		}
		request.setAttribute(FILTER_APPLIED, Boolean.TRUE);
		if (!this.securityContextRepository.containsContext(request)) {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication != null && !this.trustResolver.isAnonymous(authentication)) {
				// The user has been authenticated during the current request, so call the
				// session strategy
				try {
					this.sessionAuthenticationStrategy.onAuthentication(authentication, request, response);
				}
				catch (SessionAuthenticationException ex) {
					// The session strategy can reject the authentication
					this.logger.debug("SessionAuthenticationStrategy rejected the authentication object", ex);
					SecurityContextHolder.clearContext();
					this.failureHandler.onAuthenticationFailure(request, response, ex);
					return;
				}
				// Eagerly save the security context to make it available for any possible
				// re-entrant requests which may occur before the current request
				// completes. SEC-1396.
				this.securityContextRepository.saveContext(SecurityContextHolder.getContext(), request, response);
			}
			else {
				// No security context or authentication present. Check for a session
				// timeout
				if (request.getRequestedSessionId() != null && !request.isRequestedSessionIdValid()) {
					if (this.logger.isDebugEnabled()) {
						this.logger.debug(LogMessage.format("Request requested invalid session id %s",
								request.getRequestedSessionId()));
					}
					if (this.invalidSessionStrategy != null) {
						this.invalidSessionStrategy.onInvalidSessionDetected(request, response);
						return;
					}
				}
			}
		}
		chain.doFilter(request, response);
	}

	public void setInvalidSessionStrategy(InvalidSessionStrategy invalidSessionStrategy) {
		this.invalidSessionStrategy = invalidSessionStrategy;
	}

	public void setAuthenticationFailureHandler(AuthenticationFailureHandler failureHandler) {
		Assert.notNull(failureHandler, "failureHandler cannot be null");
		this.failureHandler = failureHandler;
	}

	public void setTrustResolver(AuthenticationTrustResolver trustResolver) {
		Assert.notNull(trustResolver, "trustResolver cannot be null");
		this.trustResolver = trustResolver;
	}

}

```



1. SecurityContextRepository에서 SecurityContext가 존재하는지 확인한다.
   * 인증 객체가 존재하고, 어나니머스 유저가 아니라면 인증을 수행한다 
2. SessionAuthenticationStrategy에 인증을 위임하는데, 기본 구현체로 **CompositeSessionAuthenticationStrategy**를 사용한다
   * CompositeSessionAuthenticationStrategy 는 내부적으로 SessionAuthenticationStrategy를 List로 가지고 있다.
   *  SessionAuthenticationStrategy로 인증을 위임해 처리한다. -  세션 처리 전략이다. 
     * SecurityConfig에서 설정한 `sessionFixation().changeSessionId()` 세션 처리 전략이며 4가지가 있다.
     * ConcurentSessionControlAuthenticationStrategy
     * ChangeSessionIdAuthenticationStrategy
     * RegisterSessionAuthenticationStrategy
     * CsrfAuthenticationStrategy
   * SessionAuthenticationStrategy로 위임해 처리한다 



CompositeSessionAuthStrategy는 내부적으로 위임 전략 4가지 정도를 가지고 있고, API 설정에 따라 For문을 돌면서 해당 기능을 처리해준다. 





# **동시 세션 관리 : SessionManagementFilter + ConcurrentSessionFilter**

Spring Security는 사용자(principal)이 같은 어플리케이션에 동시에 인증(로그인) 할 수 있는 횟수를 제한할 수 있다.

이를 통해 로그인 아이디를 공유하는 것을 막을 수 있다.



동시성 제어는 SessionAuthenticationStrategy를 구현한 ConcurrentSessionControlAuthenticationStrategy가 처리한다



> SessionManagementFilter는 인증하는 과정에서 동시 세션을 확인하고, 필요시 세션을 만료처리해준다. 
>
> * 보통 필터체인 뒷부분에 존재
>
> ConcurrentSessionFilter은 세션이 만료되었는지 확인하고, 만료된 경우 핸들러를 이용해  Logout을 처리 및 오류 페이지를 응답해주는 역할을 한다. 
>
> * 보통 필터체인 앞부분에 존재



**ConcurrentSessionFilter는 동시 세션을 관리하는 필터다. 사용자의 요청이 들어올 때 마다, 이 필터는 매번 세션이 만료되었는지를 살펴본다.** 세션이 만료되었을 경우, 세션을 즉시 만료하고 현재 요청자를 로그아웃 시킨 후, 세션 만료가 되었다는 메시지를 사용자에게 응답해주는 역할을 한다. **물리적으로 세션을 만료시키는 역할을 한다.** 

<img src="https://blog.kakaocdn.net/dn/bhEVKH/btrTToz1iRQ/QbbZYfYQQaHOW1laEL6MsK/img.png" width = 900 height= 450>

1. 사용자가 인증 요청(로그인)을 한다.
2. 인증 요청을 하는 과정에서 SessionManagementFilter로 넘어온다. 
   * SessionManagementFilter는 SessionAuthenticationStrategy.onAuthentication()로 위임한다, 
   * SessionAuthenticationStrategy.onAuthentication는 CompositeSessionAuthenticationStrategy이다. 
   * CompositeSessionAuthenticationStrategy 는 내부적으로 List로 가지고 있는 SessionAuthenticationStrategy들 중  
   * ConcurrentSessionControlAuthenticationStrategy에게 위임하여 동일 계정의 세션 갯수를 살펴본다. 

3. ConcurrentSessionControlAuthenticationStrategy 내에서 sessionCount랑 allowedSessions(허용된 세션 갯수)를 비교한다
   * 현재 사용자의 세션 갯수가, 서버에서 세팅한 세션 갯수보다 적다면 올바른 로그인 요청이니 그냥 넘어간다. 
   * 세션 갯수가 초과하면 allowableSessionsExceeded() 메소드를 실행시켜서 현재 사용자는 로그인 처리를 해주고, 이전 사용자의 세션을 (SessionInformaion 객체) session.expireNow()를 이용해 만료처리한다. 이 때, 실제 물리적인 세션이 만료되는 것이 아닌 Session Info의 Expired 변수(boolean expried)가 만료된다. (true로 설정)

4. 이전 사용자가 특정 자원에 접근 요청할 때, ConcurrentSessionFilter에서 세션 만료를 검사한다. 이 때, ConcurrentSessionFilter는 SessionManagementFilter에서 expired.Now()를 했는지 isExpired() 메서드를 이용해 확인한다. 이 때, Session Info를 확인하고, 물리적으로 Session을 만료처리한다. 

5. 세션이 만료된 경우 즉시 ConcurrentSessionFilter가 핸들러를 이용해 Logout 해버리고 오류 페이지를 응답해준다. 



### ConcurrentSessionFilter 에서 로그아웃을 시키는 과정 

* ConcurrentSessionFilter는 필터 앞부분 존재한다 

```java
private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpSession session = request.getSession(false);
		if (session != null) {
			SessionInformation info = this.sessionRegistry.getSessionInformation(session.getId());
			if (info != null) {
				if (info.isExpired()) {
					// Expired - abort processing
					this.logger.debug(LogMessage
							.of(() -> "Requested session ID " + request.getRequestedSessionId() + " has expired."));
					doLogout(request, response);
					this.sessionInformationExpiredStrategy
							.onExpiredSessionDetected(new SessionInformationExpiredEvent(info, request, response));
					return;
				}
				// Non-expired - update last request date/time
				this.sessionRegistry.refreshLastRequest(info.getSessionId());
			}
		}
		chain.doFilter(request, response);
}
```

* 요청한 사용자의 세션을 얻어온다. 세션이 널인지 아닌지 검사한다
* 세션 Id로 SessionInformaion을 얻어온다 . 
  * SessionInformaion는 세션의 기록을 의미한다. 
* 세션(SessionInformaion 객체로 확인 가능)이 expired 됐는지 확인한다. 
  * 만약 SessionManagementFilter에서 ConcurrentSessionControlAuthenticationStrategy를 사용하여 세션 갯수를 검사했을 때, 세션 갯수 초과라면, Session Info의 Expired 가 만료되서 true로 설정된다.
* 세션 expired라면 doLogout을 수행한다. 
  * 내부적으로 4가지의 LogoutHandler를 가지고 있는데, 반복해서 로그아웃 시킨다
  * TokenBasedRememberMeServices.logout()
  * CsrfLogOutHandler.logout()
  * SecurityContextLogoutHandler().logout()
  * LogoutSuccessEventPublishingLogoutHandler.logout()

```
This session has been expired (possibly due to multiple concurrent logins being attempted as the same user).
```

메시지가 출력된다. 



정리하자면, ConcurrentSessionFilter는 현재 요청한 사용자의 세션 Id을 얻어와 Expired(만료) 됐는지 확인하고, 만료 되었다면 직접 logout 핸들러를 이용해 로그아웃 시키고 오류 메시지를 출력시킨다.  



### 참조

* 인프런 정수원님 스프링 시큐리티 강의