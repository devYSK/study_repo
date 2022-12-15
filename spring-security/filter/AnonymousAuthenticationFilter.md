

# SpringSecurity - AnonymousAuthenticationFilter - 익명 사용자 인증 처리 필터





AnonymousAuthenticationFilter - 익명 사용자 인증처리 필터

익명 사용자와 인증 사용자를 구분해서 처리하기 위한 용도로 사용되는 필터.

인증을 하지 않은 요청인 경우 인증 객체를 익명 권한이 들어가 있는 객체를 만들어 SecurityContextHolder에 넣어 주는 역할을 한다.

* FilterchainProxy 클래스의 doFilterInternal 메소드에 브레이크 포인트를 찍고 필터 목록을 확인해보면 필터 순서가 꽤 뒤에 위치한다.  



`기본적으로 스프링 시큐리티 필터에 포함`이 되어 있는 필터이며 만들어지는 객체는 아래와 같은 값을 가지고 있다.

- pricial : anonymousUser
- authorities : ROLE_ANONYMOUS



해당 필터가 하는 역할은 다음과 같은 순서로 이루어진다.

- 해당 필터에 요청이 도달할때까지 사용자가 인증되지 않았다면, 사용자를 null 대신 Anonymous 인증 타입으로 표현
  - 현재 SecurityContext에 Authentication이
    - null이면 “익명 Authentication”을 만들어 넣어주고
    - null이 아니면 아무일도 하지 않는다.
- 사용자가 null 인지 확인하는것보다 어떤 구체적인 타입으로 확인할수 있도록 한다
- 화면에서 인증 여부를 구현할 때 `isAnonymous()` 와 `isAuthenticated()`로 구분해서 사용한다.
- *(login / logout 과 같은 기능을 나누어서 처리 가능)*
- 인증객체를 세션에 저장하지 않는다.



```java
public class AnonymousAuthenticationFilter extends GenericFilterBean implements InitializingBean {
  
  	/**
	 * Creates a filter with a principal named "anonymousUser" and the single authority
	 * "ROLE_ANONYMOUS".
	 * @param key the key to identify tokens created by this filter
	 */
	public AnonymousAuthenticationFilter(String key) {
		this(key, "anonymousUser", AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
	}
  
  
  @Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		if (SecurityContextHolder.getContext().getAuthentication() == null) {
			Authentication authentication = createAuthentication((HttpServletRequest) req);
			SecurityContext context = SecurityContextHolder.createEmptyContext();
			context.setAuthentication(authentication);
			SecurityContextHolder.setContext(context);
			if (this.logger.isTraceEnabled()) {
				this.logger.trace(LogMessage.of(() -> "Set SecurityContextHolder to "
						+ SecurityContextHolder.getContext().getAuthentication()));
			}
			else {
				this.logger.debug("Set SecurityContextHolder to anonymous SecurityContext");
			}
		}
		else {
			if (this.logger.isTraceEnabled()) {
				this.logger.trace(LogMessage.of(() -> "Did not set SecurityContextHolder since already authenticated "
						+ SecurityContextHolder.getContext().getAuthentication()));
			}
		}
		chain.doFilter(req, res);
	}

	protected Authentication createAuthentication(HttpServletRequest request) {
		AnonymousAuthenticationToken token = new AnonymousAuthenticationToken(this.key, this.principal,
				this.authorities);
		token.setDetails(this.authenticationDetailsSource.buildDetails(request));
		return token;
	}
  
}
```



1. SecurityContextHolder에서 authentication이 존재하는지 확인 (null 비교로)
2. authentication이 존재하지 않는다면(인증되지 않았다면) createAuthentication()을 호출하여 AnonymousAuthenticationToken(익명사용자 토큰)을 생성하여 SecurityContextHolder에 넣는다.
3. authentication이 존재한다면 로그를 찍고 다음 필터로 넘어간다. 



시큐리티 설정에서 HttpSecurity를 이용하여 기본으로 만들어 사용할 "익명 Authentication" 객체를 설정 할 수 있다.

```java
http.anonymous()
.principal()
.authorities()
.key();
  
// 다음처럼 가능
http.anonymous()
  .principal("thisIsAnonymousUser")
  .authorities("ROLE_ANONYMOUS", "ROLE_UNKNOWN")
```

- Null 대신 Null의 성격인 Object를 생성
  - [Null object pattern](https://en.wikipedia.org/wiki/Null_object_pattern)
  - [Null Object Pattern - John Grib](https://johngrib.github.io/wiki/null-object-pattern)



AnonymousAuthenticationFilter 필터로 인해 시큐리티에서는 SecurityContextHolder.getContext().getAuthentication() 를 하더라도 항상 인증 객체가 있기에 비로그인 사용자(익명 사용자)를 체크하기 위해서는 Role 검사를 해야 한다.

그래서 Security Context는 **여러 곳이나 여러 필터에서 현재 사용자가 현재 사용자가 익명 사용자인지, 인증 사용자인지 구분할 수 있다는 장점이 있는 것**이다.



### 실행순서

만약, authentication 객체가 없다면 마지막 필터에 존재하는 FilterSecurityInterceptor에서 예외를 발생하는데,

이 예외는 FilterSecurityInterceptor의 추상 클래스인 AbstractSecurityInterceptor 에서 발생시킨다.



### FilterSecurityInterceptor

- 인가처리를 담당하는 필터.
- 시큐리티 필터들 중 마지막에 위치하며 인증된 사용자의 특정 요청 승인/거부 최종 결정

- 인증객체 없이 보호자원에 접근 시도하면 AuthenticationException
- 인증 후 자원 접근 권한이 없을 경우 AccessDeniedException

* 익명 객체 일 경우 인증은 되었지만 자원 접근 권한이 없으니까 AccessDeniedException



AbstractSecurityInterceptor 인터셉터는 현재 사용자가 접근하고자 하는 자원에 접근하고자 할때 접근이 가능한지 확인하는 필터이며

null이면 인증예외를 발생시킨다.

```java
// in AbstractSecurityInterceptor beforeInvocation 메소드

if (SecurityContextHolder.getContext().getAuthentication() == null) {
			credentialsNotFound(this.messages.getMessage("AbstractSecurityInterceptor.authenticationNotFound",
					"An Authentication object was not found in the SecurityContext"), object, attributes);
		}
```

익명객체여서 인가를 못하는 예외가 발생하면

ExceptionTranslationFilter의 doFilter에서 try catch로  잡아서 handleSpringSecurityException() 을 호출하고, AuthenticationTrustResolverImpl 클래스의 isAnonymous(authentication) 를 호출하여  현재 사용자가 인증된 사용자인지 판단하고 anonymous 사용자이면 sendStartAuthentication() 을 호출하여 로그인 페이지로 이동하게 한다.



### FilterSecurityInterceptor

- 인가처리를 담당하는 필터.
- 시큐리티 필터들 중 마지막에 위치하며 인증된 사용자의 특정 요청 승인/거부 최종 결정