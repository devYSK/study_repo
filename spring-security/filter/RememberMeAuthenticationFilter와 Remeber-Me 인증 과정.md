

# RememberMeAuthenticationFilter 와 Remember-ME 인증과정



## Remember-ME?

세션이 만료되고 웹 브라우저가 종료된 후에도 어플리케이션이 사용자를 기억하는기능이다.

보통 로그인 창에는 체크박스로 로그인 기억(로그인 저장) 등을 지원하는데, 사용자는 일반적으로 로그인 후에 서버에서 Remember-me 관련 쿠키로(cookie)  보내주면 다음 요청시, 명시적으로 로그아웃 하지 않는이상 계속 로그인 되있는 것처럼 쿠키 기반으로 인증을 하는 방식이다.

 <img src="https://blog.kakaocdn.net/dn/bpZKfF/btrTQ0z3iQp/n8s08eHhvTjD3YW1neOAb1/img.png" width = 400 height =350>

  

Remember-Me가 무엇인지 정리하자면 다음과 같다

* 일반적으로 로그인 한 후에 브라우저를 끄거나, 일정 시간이 지나 세션이 만료되는 경우 명시적으로 로그아웃 하지 않더라도 로그아웃이 된다.
* 이 때 재 로그인 하지 않더라도  세션이 만료되고 웹 브라우저가 종료된 후에도 어플리케이션이 사용자를 기억하는기능이다.
* 인증되지 않은 사용자의 HTTP 요청이 remember-me 쿠키(Cookie)를 갖고 있다면, 사용자를 자동으로 인증처리한다



페이지에 접속하면 서버에서 세션이 생성되고 웹 브라우저 쿠키에 세션 아이디 정보(JSessionID)가 담긴다.
로그인을 하면 서버는 해당 세션을 인증된 세션으로 취급.

> JSESSIONID 쿠키가 동작하는 경우, 세션 필터에 의해 먼저 인증되어서 rememberme는 동작하지 않는다.
>
> `세션 기능을 끌라면 http.sessionManagement().disable()` 를 사용 하자 



## Remember-Me 기능 활성화

일반적으로 RememberMeAuthenticationFilter가 처리한다.

SecurityConfig에서 기능을 활성화 하지 않으면, `RememberMeAuthenticationFilter`가  filter chain에 등록되지 않는다

* 또한, DefaultLoginPageGeneratingFilter에서도 체크박스를 만들지 않는다. 



SecurityConfig Remember-me 활성화 

```java
@Configuration
@EnableWebSecurity
public class WebSecurityConfigure {

    private final Logger log = LoggerFactory.getLogger(WebSecurityConfigure.class);

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      
      http
        ...생략
        .rememberMe()
        .rememberMeParameter(“remember”) // 기본 파라미터명은 remember-me 
        .tokenValiditySeconds(3600) // Default 는 14일		
        .alwaysRemember(true) // 리멤버 미 기능이 활성화되지 않아도 항상 실행		
        .userDetailsService(userDetailsService)
    }
  
}
```

* rememberMe() : 리멤버 미 활성화, 설정 시작
* rememberMeParameter(파라미터 명) : RememberMe 쿠키의 이름을 지정한다. default Name은  remember-me 이다. 
  * RememberMe 쿠키 이름은 AbstractRememberMeServices클래스에 default로 정의 되어 있으며, 이 메소드로 파라미터 이름을 바꾸게 된다면 내부적으로 쿠키 이름이 바뀌게 된다
* tokenValiditySeconds(seconds) : 초 단위로 RememberMe 쿠키의 유효 기간을 정한다. default는 14일 (3600초) 이다 
* alwaysRemember(boolean) : remember-me 매개변수가 설정되지 않은 경우에도 쿠키를 항상 생성해야 하는지 여부
  * default는 false이다 
* userDetailsService(userDetailsService 구현체) : Remember-Me 토큰이 유효한 경우 User를 조회하는데 사용되는 UserDetailsService를 지정한다. 설정이 없다면 기본으로 InMemoryUserDetailsManager를 사용한다. 
  * default :  InMemoryUserDetailsManager. (UserDetailsService 빈이 필요.)
  * TokenBasedRememberMeServices.processAutoLoginCookie() 에서 loadUserByUserName으로 사용자 정보를 가져오기 때문이다. 



> 화면을 개발자가 임의로 개발한다면 여러 형태로 바뀌게 될 것인데, 이 때 Remember Me를 제대로 사용하기 위해서는 rememberMeParameter() 메소드의 파라미터와  HTML 요소의 name과 동일하게 맞추어 줘야한다.
>
> * ex) `<input type="checkbox" name="remember">`







## Remember-Me의 쿠키의 Life Cycle

- 인증 성공 :  사용자가  로그인 기억 체크박스를 체크하고, 인증에 성공하면  Remeber-Me쿠키를 헤더에 설정 (Remember-Me쿠키 설정)
- 인증 실패 : 로그인에 실패하거나 쿠키가 time-out 된 경우(쿠키가 존재하면 쿠키 무효화)
  - 즉, 로그인이 성공했어도 사용자가 임의로 로그인 페이지로 돌아간 후 인증에 실패하면, 있는 쿠키도 무효화 시킨다.
- 로그아웃 : 로그아웃시 (쿠키가 존재하면 쿠키 무효화)
- 만료시간이 지날 경우에도 무효화



# Remember-Me 인증과정

<img src="https://blog.kakaocdn.net/dn/be9zhl/btrTRedVJw2/P4MxJQEkMjkslbPdpIgV8k/img.png" width = 900 height = 500>

먼저, RememberMeAuthenticationFilter에서 인증이 진행되는데 이 필터가 존재하려면 Remember-Me 기능이 활성화 되어야 한다.

* SecuritiyConfig 설정 

RememberMeAuthenticationFilter는  default로 DefaultLoginPageGeneratingFilter나 UsernamePasswordAuthenticationFilter보다 뒤에 존재한다

```java
private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if (SecurityContextHolder.getContext().getAuthentication() != null) {
			this.logger.debug(LogMessage
					.of(() -> "SecurityContextHolder not populated with remember-me token, as it already contained: '"
							+ SecurityContextHolder.getContext().getAuthentication() + "'"));
			chain.doFilter(request, response);
			return;
		}
		Authentication rememberMeAuth = this.rememberMeServices.autoLogin(request, response);
		if (rememberMeAuth != null) {
			// Attempt authenticaton via AuthenticationManager
			try {
				rememberMeAuth = this.authenticationManager.authenticate(rememberMeAuth);
				// Store to SecurityContextHolder
				SecurityContext context = SecurityContextHolder.createEmptyContext();
				context.setAuthentication(rememberMeAuth);
				SecurityContextHolder.setContext(context);
				onSuccessfulAuthentication(request, response, rememberMeAuth);
				this.logger.debug(LogMessage.of(() -> "SecurityContextHolder populated with remember-me token: '"
						+ SecurityContextHolder.getContext().getAuthentication() + "'"));
				this.securityContextRepository.saveContext(context, request, response);
				if (this.eventPublisher != null) {
					this.eventPublisher.publishEvent(new InteractiveAuthenticationSuccessEvent(
							SecurityContextHolder.getContext().getAuthentication(), this.getClass()));
				}
				if (this.successHandler != null) {
					this.successHandler.onAuthenticationSuccess(request, response, rememberMeAuth);
					return;
				}
			}
			catch (AuthenticationException ex) {
				this.logger.debug(LogMessage
						.format("SecurityContextHolder not populated with remember-me token, as AuthenticationManager "
								+ "rejected Authentication returned by RememberMeServices: '%s'; "
								+ "invalidating remember-me token", rememberMeAuth),
						ex);
				this.rememberMeServices.loginFail(request, response);
				onUnsuccessfulAuthentication(request, response, ex);
			}
		}
		chain.doFilter(request, response);
}
```



1. RememberMeAuthenticationFilter의 doFilter()에 요청이 도착한다. 
2. 현재 인증된 객체가 없어야 인증을 시작한다 `(SecurityContextHolder.getContext().getAuthentication() != null)`
   * 세션이 동작하는 경우,  세션 필터에 의해 JSESSIONID 쿠키가 동작하여  먼저 인증되어서 rememberme는 동작하지 않는다.
   * `세션 기능을 끌라면 http.sessionManagement().disable()` 를 사용  
3. 실제 사용자 인증은 RememberMeServices  인터페이스 구현체를 통해 처리된다. 
   * `TokenBasedRememberMeServices`와 `PersistentTokenBasedRememberMeServices` 구현체가 있다
   *  `TokenBasedRememberMeServices`는 **메모리에 있는 토큰**과 사용자가 request header에 담아서 보낸 토큰을 비교하여 인증을 한다. (기본적으로 14일만 토큰을 유지한다. http.tokenValiditySeconds(seconds)로 변경 가능 )
   *  `PersistentTokenBasedRememberMeServices`는 **DB에 저장된 토큰**과 사용자가 request header에 담아서 보낸 토큰을 비교하여 인증을 한다. (영구적인 방법)

4. RememberMeServices.authLogin(request, response)를 통해 인증과정이 진행된다. 

5. request로부터 rememberMeCookie를 가져온다. `extractRememberMeCookie(request)`

   * rememberMeCookie가 없는경우 그냥 다음 필터로 넘어간다.

   * 쿠키의 길이가 0인 경우에도 다음 필터로 넘어가는데, 지정되어 있던 쿠키를 삭제한다

6. 쿠키가 존재한다면 decode한다 (`decodeCookie(rememberMeCookie)`)

   * 일반적으로 Base64로 인코딩 되어있으며 다음과 같다
     * 인코딩 된 값 : dXNlcjoxNjcxMzU3MDQyMjE3OmU4ZGQ3MDUwYjYyMjk1M2E1ZWE3OTcxYTljNTczNzQ1
     * 디코딩 한 값 : user:1671357042217:e8dd7050b622953a5ea7971a9c573745
       * userId, 만료시간, signatureValue

7. TokenBasedRememberMeService에게 인증을 위임하여 processAutoLoginCookie() 메소드를 통해 인증을 진행한다.

   * 파싱하여 token 으로 쪼갠 길이는 무조건 3이여야 한다
   * 토큰으로부터 토큰 만료시간을 가져온다

8. UserDetailsService로부터 loadUserByUserName(username) 으로 유저 정보를 가져온다

   * 시큐리티 Config에서 UserDetailsService를 설정하지 않으면 default UserDetailsService는 InemoryUserDetailsManager이다

9. signatureValue를 검증한다.

   * signatureValue는 `username + ":" + tokenExpiryTime + ":" + password + ":" + getKey();` 형태로 MD5 해시 알고리즘을 이용하여 만들어진다.
   * 즉, signatureValue의 형식은 username, 만료시간, 비밀번호, 키 값인데 이 값들이 다르면 signatureValue값도 달라지니까 이걸로 검증할 수 있는것이다
   * signatureValue가 다르면 예외를 던져 RememberMe 인증을 중지한다

10. 정상적으로 UserDetails 객체가 반환되면 createSuccessfulAuthentication()을 통해 Authentication 객체를 반환한다.

    * RememberMeAuthenticationToken (Authentication 구현체)이다. 

11. Authentication 객체를 **AuthenticationManager**(ProviderManager, `RememberMeAuthenticationProvider`를 사용한다.) 에게 인증처리를 위임하고  SecurityContext에 저장하고 리턴한다.



<img src="https://blog.kakaocdn.net/dn/cjOYvV/btrTReSzDnL/jnbWvljRGP4KFw2MiZsz5K/img.png" width= 900 height = 550>



### 참조

* 인프런 정수원님 스프링 시큐리티 강의 



## RememberMeAuthenticationFilter

세션이 사라지거나 만료가 되더라도 쿠키 또는 DB를 사용하여 저장된 토큰 기반으로 Remember-Me 인증에 사용되는 필터이다. 

먼저, RememberMeAuthenticationFilter에서 인증이 진행되는데 이 필터가 존재하려면 Remember-Me 기능이 활성화 되어야 한다.

이 필터는 필터들은 중 뒷부분에 존재한다 

* default로는 DefaultLoginPageGeneratingFilter나 UsernamePasswordAuthenticationFilter보다 뒤에 존재함



`RememberMeAuthenticationFilter` 필터가 실행될 조건은 다음과 같다.

1. 스프링 시큐리티에서 사용하는 인증객체(Authentication)가 Security Context에 없어야 함
   - 세션 만료(time out)가 되어야한다. 세션이 있고 유효하면 인증객체가 있다라는 소리이다.  
     - 로그인이 정상적으로 되었고, 회원 정보도 정상적으로 세션에서 찾을 수 있다라는 이야기이다. 따라서 이 필터가 실행될 필요가 없다.
   - 세션 기능을 꺼버려도 이 필터가 동작한다
     - 일반적으로 세션이 유효햐면(JSESSIONID) 인증 객체(Authentication)를 만들어버리기 떄문에 필터가 실행되지 않는다. 
     -   `세션 기능을 끌라면 http.sessionManagement().disable()` 를 사용  
2. 사용자 request header에 remember-me 쿠키 토큰이 존재해야 한다.





## Remember-Me 기능 활성화

일반적으로 RememberMeAuthenticationFilter가 처리한다.

SecurityConfig에서 기능을 활성화 하지 않으면, `RememberMeAuthenticationFilter`가  filter chain에 등록되지 않는다

* 또한, DefaultLoginPageGeneratingFilter에서도 체크박스를 만들지 않는다. 

SecurityConfig Remember-me 활성화 

```java
@Configuration
@EnableWebSecurity
public class WebSecurityConfigure {

    private final Logger log = LoggerFactory.getLogger(WebSecurityConfigure.class);

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      
      http
        ...생략
        .rememberMe()
        .rememberMeParameter(“remember”) // 기본 파라미터명은 remember-me 
        .tokenValiditySeconds(3600) // Default 는 14일		
        .alwaysRemember(true) // 리멤버 미 기능이 활성화되지 않아도 항상 실행		
        .userDetailsService(userDetailsService)
    }
  
}
```

* rememberMe() : 리멤버 미 활성화, 설정 시작
* rememberMeParameter(파라미터 명) : RememberMe 쿠키의 이름을 지정한다. default Name은  remember-me 이다. 
  * RememberMe 쿠키 이름은 AbstractRememberMeServices클래스에 default로 정의 되어 있으며, 이 메소드로 파라미터 이름을 바꾸게 된다면 내부적으로 쿠키 이름이 바뀌게 된다
* tokenValiditySeconds(seconds) : 초 단위로 RememberMe 쿠키의 유효 기간을 정한다. default는 14일 (3600초) 이다 
* alwaysRemember(boolean) : remember-me 매개변수가 설정되지 않은 경우에도 쿠키를 항상 생성해야 하는지 여부
  * default는 false이다 
* userDetailsService(userDetailsService 구현체) : Remember-Me 토큰이 유효한 경우 User를 조회하는데 사용되는 UserDetailsService를 지정한다. 설정이 없다면 기본으로 InMemoryUserDetailsManager를 사용한다. 
  * default :  InMemoryUserDetailsManager. (UserDetailsService 빈이 필요.)
  * TokenBasedRememberMeServices.processAutoLoginCookie() 에서 loadUserByUserName으로 사용자 정보를 가져오기 때문이다. 



> 화면을 개발자가 임의로 개발한다면 여러 형태로 바뀌게 될 것인데, 이 때 Remember Me를 제대로 사용하기 위해서는 rememberMeParameter() 메소드의 파라미터와  HTML 요소의 name과 동일하게 맞추어 줘야한다.
>
> * ex) `<input type="checkbox" name="remember">`





## RememberMeSevices

실제 사용자 인증은 RememberMeServices  인터페이스 구현체를 통해 처리된다. 

* RememberMeSevices 는 `인증을 진행하는 것이 아닌, 인증 객체(UserDetails 객체)를 찾아오는 역할` 이며, UserDetails 객체를 반환한다.
* Filter로 정상적으로 UserDetails 객체가 반환되면 Authentication 객체를 **AuthenticationManager**(ProviderManager, `RememberMeAuthenticationProvider`를 사용한다.) 에게 인증처리를 위임하고  SecurityContext에 저장하고 리턴한다.

  


* RememberMeAuthenticationToken

  - remember-me 기반 Authentication(인증 객체) 인터페이스 구현체

  - RememberMeAuthenticationToken 객체는 언제나 인증이 완료된 상태만 존재함

  




이 인터페이스의 기본 구현체는 2가지가 있다.

  


### TokenBasedRememberMeServices — MD5 해시 알고리즘 기반 쿠키 검증

- 기본적으로 이 구현체가 사용된다
- onLoginSuccess 메소드에서 유저아이디로 userDetailsService에서 loadUserByUSerName으로 패스워드를 얻어온다.  
- signatureValue를 잘보자
  - 이걸 같이 이용해서 쿠키를 만든다 
  - 쿠키를 디코딩하면 signatureValue가 보인다 
  - 이것은 RememberMeAuthenticationFilter -> AbstractRememberMeServices에서 디코딩 한다 
  - TokenBasedRememberMeServices.processAutoLoginCookie() 에서 signatureValue를 검증한다.
    - signatureValue는 `username + ":" + tokenExpiryTime + ":" + password + ":" + getKey();` 형태로 MD5 해시 알고리즘을 이용하여 만들어진다.
    - 즉, signatureValue의 형식은 username, 만료시간, 비밀번호, 키 값인데 이 값들이 다르면 signatureValue값도 달라지니까 이걸로 검증할 수 있는것이다
    - signatureValue가 다르면 예외를 던져 RememberMe 인증을 중지한다

  


### PersistentTokenBasedRememberMeServices — 외부 데이터베이스에서 인증에 필요한 데이터를 가져오고 검증함

- 사용자마다 고유의 Series 식별자가 생성되고, 인증 시 마다 매번 갱신되는 임의의 토큰 값을 사용하여 보다 높은 보안성을 제공함
  - 사용하기 위해서는 별도의 데이터베이스가 필요하다 
- 포멧 : series:token
  - series 값이 키가 된다. 일종의 채널이라고 보면 편리하다.
- 토큰에 username이 노출되지 않고, 만료시간은 서버에서 정하고 노출하지 않고 서버는 로그인 시간만 저장한다.
  - 재로그인이 될 때마다 token 값을 갱신해 주니까 토큰이 탈취되어 다른 사용자가 다른 장소에서 로그인을 했다면 정상 사용자가 다시 로그인 할 때, CookieTheftException 이 발생하게 되고, 서버는 해당 사용자로 발급된 모든 remember-me 쿠키값들을 삭제하고 재로그인을 요청하게 됩니다.
  - InmemoryTokenRepository 는 서버가 재시작하면 등록된 토큰들이 사리진다. 따라서 자동로그인을 설정했더라도 다시 로그인을 해야 한다. 재시작 후에도 토큰을 남기고 싶다면 JdbcTokenRepository를 사용하거나 이와 유사한 방법으로 토큰을 관리해야 하면된다
  - 로그아웃하고 다른 곳에 묻혀놓은 remember-me 쿠키값도 쓸모가 없게 된다. 만약 다른 곳에서 remember-me로 로그인한 쿠키를 살려놓고 싶다면, series 로 삭제하도록 logout 을 수정해야 한다.

   


> UsernameAuthenticationFilter(AbstractAuthenticationProcessingFilter)에서 인증이 완료 된 후에 실행되는  successfulAuthentication()  메서드 내에서 `rememberMeServices.loginSuccess(request, response, authResult);` 로 쿠키를 생성하여 response 헤더에 저장한다. 



   


### RememberMeAuthenticationProvider

- RememberMeAuthenticationToken 기반 인증 처리를 위한 AuthenticationProvider
- 앞서 remember-me 설정 시 입력한 key 값을 검증함

```java
@Override
public Authentication authenticate(Authentication authentication) throws AuthenticationException {
	if (!supports(authentication.getClass())) {
		return null;
	}
	if (this.key.hashCode() != ((RememberMeAuthenticationToken) authentication).getKeyHash()) {
		throw new BadCredentialsException(this.messages.getMessage("RememberMeAuthenticationProvider.incorrectKey",
				"The presented RememberMeAuthenticationToken does not contain the expected key"));
	}
	return authentication;
}
```



## RememberMeAuthenticationToken vs UsernamePasswordAuthenticationToken

RemebmerMe를 이용한 경우 RememberMeAuthenticationToken 을 사용한다

로그인을 한 경우 UsernamePasswordAuthenticationToken 사용된다

- `remember-me 기반 인증과 로그인 아이디/비밀번호 기반 인증 결과가 명백히 다르다`
  - remember-me 기반 인증 결과 — RememberMeAuthenticationToken
  - 로그인 아이디/비밀번호 기반 인증 결과 — UsernamePasswordAuthenticationToken



remember-me 기반 인증은 로그인 기반 인증 보다 보안상 다소 약한 인증이다. 따라서, 모두 동일하게 인증된 사용자라 하더라도 권한을 분리할 수 있다

* isFullyAuthenticated — 명시적인 로그인 아이디/비밀번호 기반으로 인증된 사용자만 접근 가능

* 이 메소드는 RemeberMe를 사용해서 들어오지 않은 경우만 True로 반환해서, RememberMe를 통한 인증은 허락하지 않는다. 

  * 추가적인 인증을 요구할 수 있게 그렇게 해야 한다.
  * 물론 익명 사용자도 허락하지 않는다 



명심해야 하는게 `두 방식에서 사용되는 Authentication 객체는 다르다. `



### 참조

* 인프런 정수원님 스프링 시큐리티 강의 
