# DefaultLogoutPageGeneratingFilter

사용자가 로그아웃 페이지를 구성하지 않은 경우 기본적으로 로그아웃 페이지를 만들어주는 필터이다.



일반적으로 DefaultLoginPageGeneratingFilter 바로 뒤에 존재한다.



/login 으로 get 요청을 보낼 경우 기본적으로 제공해주는 로그인페이지로 보내주고 /logout 으로 get 요청을 보낼 경우 기본적으로 제공하는 로그아웃 페이지를 보내준다. 



> 실제 로그아웃 처리는 LogoutFilter가 한다. 이 필터는 로그아웃 페이지를 만들어주는 필터이다 



로그아웃 URL을 변경하고 싶다면?

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http.authorizeRequests()
          	.. 생략
            .anyRequest().permitAll()
            .and()
            .formLogin()
            .defaultSuccessUrl("/")
            .permitAll()

            .and()
            // logout 설정
            .logout()
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .logoutSuccessUrl("/")
            .invalidateHttpSession(true)
            .clearAuthentication(true)

            .build();
    }
```

* 다음과 같이 설정하면 된다. 



만약 커스텀한 로그인-아웃 페이지를 사용하고 싶다면



만약 SecurityConfig에서 다음과 같이 설정해서 커스텀한 로그인-아웃 페이지를 사용한다면

```java
http.formLogin()
        .loginPage("/my-login-page")
        .permitAll();
```

DefaultLoginPageGeneratingFilter 와 DefaultLogoutPageGeneratingFilter 는 FilterChainProxy에서 제외된다.

그리고, 직접 로그인 페이지와 로그아웃 페이지를 구현하면 된다.

물론 구현할 페이지와 configuration 클래스에서 설정한 값들은 맞춰주어야 한다.



실제 로그아웃은 `LogOutFilter`가 진행한다.



## LogOutAPI

SecurityConfig에서 다음과 같이 작성하면된다.

하지만 굳이 직접 할 필요 없이 LogoutFilter가 LogOut을 진행해준다. 

```java
http.logout()
  .logoutUrl("/logout")   // 로그아웃 처리 URL (= form action url)  
  //.logoutSuccessUrl("/login") // 로그아웃 성공 후 targetUrl,   
  // logoutSuccessHandler 가 있다면 효과 없으므로 주석처리.
     
  .addLogoutHandler((request, response, authentication) -> {   
    // 사실 굳이 내가 세션 무효화하지 않아도 됨.      
    // LogoutFilter가 내부적으로 해줌.   
    HttpSession session = request.getSession();
    if (session != null) {  
      session.invalidate();  
    }    
  })  // 로그아웃 핸들러 추가    
  .logoutSuccessHandler((request, response, authentication) -> {    
    response.sendRedirect("/login");
  }) // 로그아웃 성공 핸들러
  .deleteCookies("remember-me"); // 로그아웃 후 삭제할 쿠키 지정
```

* 스프링 시큐리티는 로그아웃을 기본으로 POST 방식을 사용하도록 한다.
  GET 방식으로 바꿀 수 있다

## DefaultLogoutPageGeneratingFilter 내부



```java
public class DefaultLogoutPageGeneratingFilter extends OncePerRequestFilter {

	private RequestMatcher matcher = new AntPathRequestMatcher("/logout", "GET");

	private Function<HttpServletRequest, Map<String, String>> resolveHiddenInputs = (request) -> Collections.emptyMap();

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		if (this.matcher.matches(request)) {
			renderLogout(request, response);
		}
		else {
			if (logger.isTraceEnabled()) {
				logger.trace(LogMessage.format("Did not render default logout page since request did not match [%s]",
						this.matcher));
			}
			filterChain.doFilter(request, response);
		}
	}
```

* LogOutFilter의 내부 동작은 간단하다. logout URL 인지 비교하고 true이면 HTML을 하드하게 response에 넣어서 페이지를 만들어준다



# LogOutFilter

직접적으로 LogOut을 담당하는 Filter이다

- 세션 무효화
- 인증객체 삭제
- SecurityContext 삭제
- LogOutEvent 발생
- LogOut 후 지정된 URL로 Redirect 

등의 일을 한다. 



```java
@Override
public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
	}

private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if (requiresLogout(request, response)) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (this.logger.isDebugEnabled()) {
				this.logger.debug(LogMessage.format("Logging out [%s]", auth));
			}
			this.handler.logout(request, response, auth);
			this.logoutSuccessHandler.onLogoutSuccess(request, response, auth);
			return;
		}
		chain.doFilter(request, response);
}
```

* requiresLogout()으로 로그아웃 URL인지 확인하고
* this.handler.logout(request, response, auth); 를 호출하여 로그아웃 시킨다. 

* logoutSuccessHandler.onLogoutSuccess(request, response, auth); 
  * `http.logout().logoutSuccessHandler()` api 를 통해서 직접
    `SuccessHandler`를 작성했다면 해당 `Handler`가 호출되고 끝난다.
  * 그렇지 않다면 `SimpleUrlLogoutSuccessHandler` 가 기본으로
    호출되고 내부적으로 redirect 시킬 targetUrl 을 결정한 후, redirect 시킨다.
  * 만약 어떠한 사용자 지정도 없다면 `targetUrl="/login?logout"` 이 되고,
    만약 사용자가 `http.logout().logoutSuccessUrl("/login")` API 로 targetUrl 을 지정했다면 해당 위치로 redirect 시킨다.





>  SecuritiyContext를 초기화 하는 역할을 하는 핸들러는 SecurityContextLogoutHandler이다.



LogoutFilter 내에 있는 handler는 기본 구현체가 CompositeLogoutHandler 클래스이다.

이름그대로 Composite(조합, 합성) 패턴으로 구현되어 있는데, 내부적으로 다른 LogoutHandler를 가지고 있다

```java
public final class CompositeLogoutHandler implements LogoutHandler {

	private final List<LogoutHandler> logoutHandlers;

	public CompositeLogoutHandler(LogoutHandler... logoutHandlers) {
		Assert.notEmpty(logoutHandlers, "LogoutHandlers are required");
		this.logoutHandlers = Arrays.asList(logoutHandlers);
	}

	public CompositeLogoutHandler(List<LogoutHandler> logoutHandlers) {
		Assert.notEmpty(logoutHandlers, "LogoutHandlers are required");
		this.logoutHandlers = logoutHandlers;
	}

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		for (LogoutHandler handler : this.logoutHandlers) {
			handler.logout(request, response, authentication);
		}
	}

}
```

logout() 메소드 요청이 오면 자신이 가지고 있는 LogoutHandler로 logout 요청을 위임한다.

<img src="https://blog.kakaocdn.net/dn/zjXhu/btrTRCkSXFp/bkE6ZJxInckiX8jaKXNgy1/img.png" width = 850 height = 200>



### TokenBasedRememberMeServices 

 AbstractRememberMeServices의 서브클래스로, 실제 logout 메소드 처리는 AbstractRememberMeServices에서 한다



* logout() 메소드에서 cancelCookie()를 호출한다.
* 로그인을 비활성화하기 위해 응답에 "쿠키 "(maxAge = 0 사용)를 설정한다

```java
protected void cancelCookie(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("Cancelling cookie");
		Cookie cookie = new Cookie(this.cookieName, null);
		cookie.setMaxAge(0);
		cookie.setPath(getCookiePath(request));
		if (this.cookieDomain != null) {
			cookie.setDomain(this.cookieDomain);
		}
		cookie.setSecure((this.useSecureCookie != null) ? this.useSecureCookie : request.isSecure());
		response.addCookie(cookie);
	}
```



### CsrfLogoutHandler

CsrfLogoutHandler는 로그아웃 시 CsrfToken를 제거한다. 그리고 다음 요청 시 프레임워크에서 새 CsrfToken을 생성한다.



```java
@Override
public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

  this.csrfTokenRepository.saveToken(null, request, response);
	
}
```

* 이 때 csrfTokenRepository는 다양한 구현체가 있다
  * CookieCsrfTokenRepository
  * HttpSessionCsrfTokenRepository
  * LazyCsrfTokenRepository
  * TestCsrfTokenRepository



### SecurityContextLogoutHandler

이친구가 핵심. 

SecurityContextHolder를 초기화 하고 로그아웃을 수행한다. 

또한 isInvalidateHttpSession()이 true이고 세션이 null이 아닌 경우 HttpSession을 무효화 시킨다.. 

또한 clearAuthentication이 true(기본값)로 설정된 경우 현재 SecurityContext에서 인증을 제거합니다.

```java
@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		Assert.notNull(request, "HttpServletRequest required");
		if (this.invalidateHttpSession) {
			HttpSession session = request.getSession(false);
			if (session != null) {
				session.invalidate();
				if (this.logger.isDebugEnabled()) {
					this.logger.debug(LogMessage.format("Invalidated session %s", session.getId()));
				}
			}
		}
		SecurityContext context = SecurityContextHolder.getContext();
		SecurityContextHolder.clearContext();
		if (this.clearAuthentication) {
			context.setAuthentication(null);
		}
	
}
```



### LogoutSuccessEventPublishingLogoutHandler

 LogoutSuccessEvent를 발생시키는 핸들러이다 .



