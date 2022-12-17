# BasicAuthenticationFilter



이 필터는 인증 체계가 Basic이고 Base64로 인코딩된 사용자 이름:비밀번호 토큰이 있는 Authorization의 HTTP 요청 헤더가 있는 요청을 처리하는 역할을 한다.



요청당 1번 실행을 보장하는 것을 목표로 하는 필터 기본 추상 클래스인 OncePerRequestFilter을 구현하고 있다.



- form 인증이 아닐 때 인증을 시도하는 필터이다.
- `Authentication` header를 이용하여 `Basic {token}` 값을 전달하여 인증을 하는 방식이다.
- token 값은 `username:password`를 BASE64로 인코딩하여 전달되는 값을 Filter에서 decode하여 인증을 진행

* stateless 방식이다. 



> 클라이언트와 서버가 분리되어 있는 경우 (예, SPA페이지, 모바일 앱) 서버 사이드 렌더링(SRR) 과는 다르게 프론트에서 뷰를 책임진다.서버에서 페이지를 리다이렉션 시키면서 페이지가 이동되는 것이 아니기 떄문에 스프링 시큐리티 로그인을 구현하기 위해선 BasicAutheticationFilter를 사용해야한다. BasicAutheticationFilter 혹은 JWT(JSON WEB TOKEN)기반의 인증방식을 사용한다.



이 필터를 이용하면, 이전에 form 요청으로 받아온 데이터로 UsernamePasswordAuthenticaton 토큰을 생성하지 않아도, 요청 헤더의 정보를 사용하여 UsernamePasswordAuthenticaton 토큰을 생성할 수 있다.

토큰이 생성된 이후의 과정은 UsernamePasswordAuthenticationFilter 의 과정과 동일하다.



> JwtToken을 인증하는 Filter를 구현하려면 OncePerRequestFilter를 구현해서 UsernamePasswordAuthenticationFilter 필터 앞에 위치시킨다. 이 때 꼭 UsernamePasswordAuthenticationFilter앞이 아니여도 되지만, 필터 앞 뒤의 역할과 순서를 생각해서 잘 지정해야 한다. 
>
> 
>
> -- SecurityConfig의 Http 설정에서 http.addFilterBefore() 메소드.
>
> 
>
> 일반적으로 HttpBasic 인증만 사용한다면, BasicAuthenticationFilter를 상속받아 오버라이딩 해서 마찬가지로 필터 위치를 지정해 사용한다. 

 

**BasicAuthenticationFilter의 특징**

 

* 간단한 설정과 Stateless가 장점이다. - Session Cookie(JSESSIONID) **사용하지 않음**

* 보호자원(인가받아야 하는 자원)에 접근시서버가 클라이언트에게 401 Unauthorized 응답과 함께 WWW-Authenticate header를 기술해 인증이 필요하다는 요구를 보낸다

* Client는 ID:Password 값을 Base64로 Encoding한 문자열을 Authorization Header에 추가한 뒤 Server에게 Resource를요청
  * Header Example : `Authorization: Basic cmVzdDpyZXN0`
* ID, Password가 Base64로 Encoding 되어 있어 ID, Passwor가 외부에 쉽게 노출되는 구조이기 때문에 SSL이나TLS는 필수이다
  *  http 에서는 header에 username:password 값이 base64로 인코딩(decode 하기가 쉽다.)되어 전달 되기 떄문에 보안에 매우 취약하다 , 반드시 https 프로토콜에서 사용할 것을 권장한다 .
* 만약 session 처럼 인증을 매번 하고 싶지 않다면 `RememberMeServices`를 활용하여 인증정보를 캐시 하여 사용할 수 있다.



최초 로그인시에만 인증을 처리하고 , 이후에는 session에 의존한다 , RememberMe 를 설정한 경우 , remember-me 쿠키가 브라우저에 저장되기 떄문에 세션이 만료된 이후라도 브라우저 기반의 앱에서는 장시간 서비스를 로그인 페이지를 거치지 않고 이용할 수 있다 . 

 

\- 인증 예외가 발생하면 401(UnAuthorized) 에러를 내려보낸다



## Http Basic 인증 활성화

시큐리티 설정 파일에서 httpBasic 설정이 필요하다. 

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
 	...
   http.httpBasic();
   ...
}
```



<img src="https://blog.kakaocdn.net/dn/mucbt/btrTQ1yGmX1/Uc5JGiZqV2MzSpEanxAo90/img.png" width= 700 height = 500>



BasicAuthenticationFilter의 내부 구현이다.

```java
@Override
protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		try {
			UsernamePasswordAuthenticationToken authRequest = this.authenticationConverter.convert(request);
			if (authRequest == null) {
				this.logger.trace("Did not process authentication request since failed to find "
						+ "username and password in Basic Authorization header");
				chain.doFilter(request, response);
				return;
			}
			String username = authRequest.getName();
			this.logger.trace(LogMessage.format("Found username '%s' in Basic Authorization header", username));
			if (authenticationIsRequired(username)) {
				Authentication authResult = this.authenticationManager.authenticate(authRequest);
				SecurityContext context = SecurityContextHolder.createEmptyContext();
				context.setAuthentication(authResult);
				SecurityContextHolder.setContext(context);
				if (this.logger.isDebugEnabled()) {
					this.logger.debug(LogMessage.format("Set SecurityContextHolder to %s", authResult));
				}
				this.rememberMeServices.loginSuccess(request, response, authResult);
				this.securityContextRepository.saveContext(context, request, response);
				onSuccessfulAuthentication(request, response, authResult);
			}
		}
		catch (AuthenticationException ex) {
			SecurityContextHolder.clearContext();
			this.logger.debug("Failed to process authentication request", ex);
			this.rememberMeServices.loginFail(request, response);
			onUnsuccessfulAuthentication(request, response, ex);
			if (this.ignoreFailure) {
				chain.doFilter(request, response);
			}
			else {
				this.authenticationEntryPoint.commence(request, response, ex);
			}
			return;
		}

		chain.doFilter(request, response);
	}
```

* `BasicAuthenticationFilter`에 `doFilterInternal`을 통해서 인증을 진행하며, `BasicAuthenticationConverter`를 통해서 `AuthResult`인 token을 covert한다

* 이 때, BasicAuthenticationConverter 에서 헤더에서 값을 꺼낸다. 

```java
Override
public UsernamePasswordAuthenticationToken convert(HttpServletRequest request) {
		String header = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (header == null) {
			return null;
		}
		header = header.trim();
		if (!StringUtils.startsWithIgnoreCase(header, AUTHENTICATION_SCHEME_BASIC)) {
			return null;
		}
		if (header.equalsIgnoreCase(AUTHENTICATION_SCHEME_BASIC)) {
			throw new BadCredentialsException("Empty basic authentication token");
		}
		byte[] base64Token = header.substring(6).getBytes(StandardCharsets.UTF_8);
		byte[] decoded = decode(base64Token);
		String token = new String(decoded, getCredentialsCharset(request));
		int delim = token.indexOf(":");
		if (delim == -1) {
			throw new BadCredentialsException("Invalid basic authentication token");
		}
		UsernamePasswordAuthenticationToken result = UsernamePasswordAuthenticationToken
				.unauthenticated(token.substring(0, delim), token.substring(delim + 1));
		result.setDetails(this.authenticationDetailsSource.buildDetails(request));
		return result;
}
```

* Authorization 이라는 이름의 헤더 값에서 값을 꺼낸다 
  * 헤더가 null이라면 null을 반환하고 인증 인가가 실패하게 된다.
* 헤더에서 값을 꺼내고 decode하여 인증되지 않은 Authentication 객체를 만들어 리턴한다.
  * encode하기 전에 `username:password` 포맷으로 전달하기에 구분자 `:`을 기준으로 앞이 username, 뒤쪽이 password가 되어 `UsernamePasswordAuthenticationToken`으로 변환 후 전달 한다.

* 이후에 BasicAuthenticationFilter로 돌아와서, authenticationManager를 통해서 인증 과정을 수행한다.  
  * this.authenticationManager.authenticate(authRequest)
  * 인증에 실패하면 AuthenticationException 예외가 발생한다
* 그다음 SecurityContextHolder에 인증된 Authentication 객체를 저장한다. 

* `UsernamePasswordAuthenticationFilter`와의 차이가 존재하는데 Authentication을 성공 후에 처리하는 로직이 없다.
  * form을 통한 인증을 하는 경우 이전에 페이지를 기억해두었다가 로그인 후 이전 페이지로 redirect 해주는 부분이 추가가 되어 있지만, HttpBasic 요청은 api처럼 요청하기에 redirect를 굳이 처리할 필요가 없으니 성공 후에 처리가 비어 있다.



인증 성공된 후에 동작하는 로직인 onSuccessfulAuthentication() 메소드는 구현이 되어 있지 않다

```java
protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			Authentication authResult) throws IOException {
	// 구현이 되어있지 않다
}
```

그러므로 `BasicAuthenticationFilter`에서 성공 후 처리하는 로직은 오버라이딩해서 추가적으로 개발해야 한다.

