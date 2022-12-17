# DefaultLoginPageGeneratingFilter

사용자가 로그인 페이지를 구성하지 않은 경우 기본적으로 로그인 페이지를 만들어주는 필터이다. 

- 별도의 로그인 페이지 설정을 하지 않으면 제공되는 필터

로그인 페이지 자체를 커스텀 구현 가능하며, 이 경우 해당 필터는 비활성화됨

* [docs](https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/web/authentication/ui/DefaultLoginPageGeneratingFilter.html)

<img src="https://blog.kakaocdn.net/dn/XFhdQ/btrTQPrvvqq/MWvSuYk4FEjuKfxHVXLYQ0/img.png" width = 800 height = 300>

프로세스는 단순하다

커스텀 로그인 페이지가 활성화 되어 있고 URL을 설정하였으면 설정된 URL로 가서 커스텀 로그인 페이지를 보여준다.

활성화가 되어있지 않으면 DefaultLoginPageGeneratingFilter를 등록하고 요청이 들어오면 해당 필터에서 Html을 생성해서 로그인 페이지를 보여준다. 



## 우리가 만약 로그인 페이지를 커스텀 해서 사용할 것이면?

시큐리티 설정 내에서 로그인 페이지를 따로 설정 해줘야 한다

```java
http.formLogin()
        .loginPage("/my-login-page")
        .permitAll();
```

- 이렇게 설정 하면 커스텀 페이지를 사용할 것이라고 선언한 거기 때문에

  두 필터를 제공 해주지 않는다. (필터 등록 x )

  - DefaultLoginPageGeneratingFilter 필터 등록이 안되있다
  - 로그 아웃도 마찬가지. (DefaultLogoutPageGeneratingFilter)
  - 다른 필터들은 등록 되어있다.
  - 반드시 .permitAll()을 해줘야 한다 (모든 사용자 접근 가능하게)



## 필터 설명

일반적으로 필터 순서는 바뀔 수 있고,  RequestCacheAwareFilter, RememberMeAuthenticationFilter 등 앞에 존재한다

<img src="https://blog.kakaocdn.net/dn/CjCF2/btrTR8cDO4F/iaVgBhz7UpYmXiuIKws2Y0/img.png" width = 800 height = 550>

* 이 필터 순서는 항상 바뀔 수 있다. 



이 필터가 하는 일은 단순하다. 

```java
@Override
public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
}

private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		boolean loginError = isErrorPage(request);
		boolean logoutSuccess = isLogoutSuccess(request);
		if (isLoginUrlRequest(request) || loginError || logoutSuccess) {
			String loginPageHtml = generateLoginPageHtml(request, loginError, logoutSuccess);
			response.setContentType("text/html;charset=UTF-8");
			response.setContentLength(loginPageHtml.getBytes(StandardCharsets.UTF_8).length);
			response.getWriter().write(loginPageHtml);
			return;
		}
		chain.doFilter(request, response);
}
```

로그인 페이지 URL인지, 에러페이지인지, 로그아웃 성공인지에 대해 확인하고

로그인 페이지 URL이면 generateLoginPageHtml()를 호출하여 페이지를 response에 setUp하고 리턴한다.

* isErrorPage(request): 요청 url과 errorpage URL을 비교하여 boolean 반환
* isLogoutSuccess(request) : this.logOutSuccessUrl 을 비교하여 로그아웃 URL이면 true 아니면 false

```java
private String generateLoginPageHtml(HttpServletRequest request, boolean loginError, boolean logoutSuccess) {
		String errorMsg = "Invalid credentials";
		if (loginError) {
			HttpSession session = request.getSession(false);
			if (session != null) {
				AuthenticationException ex = (AuthenticationException) session
						.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
				errorMsg = (ex != null) ? ex.getMessage() : "Invalid credentials";
			}
		}
		String contextPath = request.getContextPath();
		StringBuilder sb = new StringBuilder();
		sb.append("<!DOCTYPE html>\n");
		sb.append("<html lang=\"en\">\n");
		sb.append("  <head>\n");
		sb.append("    <meta charset=\"utf-8\">\n");
  ... 다 html 만드는 페이지임 
```

* 그냥 진짜 진부하게 html로 하드코딩 되어있다 

* RememberMe Option을 켰다면 RememberMe 박스도 만들어준다



```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

  return http.authorizeRequests()
            ... 생략
            .and()

            // rememberMe 설정
            .rememberMe()
            .rememberMeParameter("remember-me")
            .tokenValiditySeconds(300) // 5분
            .and()
					... 생략
            .build();
}
```















