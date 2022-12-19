# 스프링 시큐리티 필터 및 아키텍처 정리



<img src="https://blog.kakaocdn.net/dn/lrf4s/btrTVg3ZVwP/D9qkVUIIXkkt4sbJq4YLx0/img.png" width=1000 height = 550>

지금까지 여러 API들과 FIlter에 대해서 아키텍처를 분석 및 공부해보았고, 그 하나하나의 API, Filter들을 합쳐서 사용자가 자원요청(Request)를 할 때 이뤄지는 SpringSecurity의 보안과정 전체Flow를 도식화 한 것



###  여러 케이스별 SpringSecurity Logic

#### ![img](data:image/gif;base64,R0lGODlhAQABAIAAAP///wAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==) 공통 로직 - 초기화



* 사용자가 설정클래스에서 생성&설정한 여러 SecurityConfig클래스로 HttpSecurity에서 Filter를 생성하여 WebSecurity에 전달한다.
* WebSecurity는 FilterChainProxy객체에 Bean객체를 생성하여 생성자로 자신이 가지고 있는 Filter목록을 전달한다.
* DelegatingFilterProxy는 springSecurityFilterChain 라는 이름의 Bean을 가진 Bean Class를 찾는데 그게 FilterChainProxy다.
  * → 사용자가 요청(Request)를 하면 FilterChainProxy 에게 전달하여 요청을 위임한다.

#### 로그인::(인증 전)인증 시도

### SecurityContextPersistenceFilter

1. DelegatingFilterProxy에서 FilterChainProxy에게 인증요청 위임

2. SecurityContextPersistenceFilter 에서 loadContext 함수를 호출해 SecurityContext 가 있는지 확인한다. → ![img](data:image/gif;base64,R0lGODlhAQABAIAAAP///wAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==) 내부적으로 HttpSessionSecurityContextRepository 클래스가 있고, SecurityContext의 생성,저장,조회,참조를 하는 클래스

3. 새로운 SecurityContext를 저장해서 SecurityContextHolder 에 저장한 다음 다음 필터로 이동.

* LogoutFilter
  1. Logout요청을 한 것이 아니므로 다음 필터로 패스



### UsernamePasswordAuthenticationFilter

1. 전달받은 Username + Password를 가지고 인증객체(Authentication)을 만든다.

2. AuthenticationManager 에게 인증처리 위임

3. AuthenticationManager 은 AuthenticationProvider 에게 실제 인증처리를 위임

4. AuthenticationProvider 은 UserDetailsService 를 활용해서 아이디와 패스워드를 검증한다.

5. 인증이 성공했다면 SecurityContextHolder안에 SecurityContext 에 인증에 성공한 인증객체(Authentication)를 생성및 저장한다.
   * → 여기서 SecurityContext은 SecurityContextPersistenceFilter 에서 만들어진 SecurityContext를 참조한 것이다.

6. 인증 후 후속처리를 동시에 하게되는데, 이 과정이 SessionManagementFilter 안의 3가지 과정이다.

   * ConcurrentSession 에서 동시적 세션체크를 하는데 두 가지 전략을 가지고 있는데, 해당 상황에서는 첫 로그인이기에 통과.
     * 이전 사용자 세션 만료 전략 : session.expireNow 로 이전 사용자의 세션을 만료시킨다.
     * 현재 사용자 인증 시도 차단 전략: SessionAuthenticationException 예외를 발생시켜 인증을 차단한다.

   * SessionFixation (세션 고정 보호)에서 인증에 성공을 한 시점에서 새롭게 쿠키를 발급한다.

   * 이 사용자 정보를 SessionInfo를 만들어 저장한다.

7. 인증 성공 후 후처리 로직을 수행하는데, 이 시점에서 동시에 수행되는 로직이 있다. 
   * SecurityContextPersistenceFilter 가 최종적으로 Session에 인증객체(Authentication) 을 담은 SecurityContext를 저장.
   * SecurityContext를 Clear해준다.
   * 정의된 인증 성공 후처리 페이지로 이동한다(Ex: Root page 이동)





#### 인증 후 특정 페이지 이동(자원 요청(Request))



### SecurityContextPersistenceFilter

1. loadContext 로 Session에서  SecurityContext를 꺼내온다.

2. SecurityContextHolder에 꺼내 온 SecurityContext를  저장한다.

3. 다음 필터로 이동



### LogoutFilter, UsernamePasswordAuthenticationFilter 그대로 패스



### ConcurrentSessionFilter

1. 이 필터는 최소 두 명 이상이 동일한 계정으로 접속을 시도하는 경우에 동작한다.

2. Session 이 만료되었는지 isExpired를 통해 확인

3. 만료되지 않았기 때문에 다음 필터로 이동.



###  RememberMeAuthenticationFilter

→현재 사용자가 세션이 만료되었거나 무효화되어 세션 내부의 인증객체(Authentication)이 비어있을 경우 동작한다.

1. 사용자의 요청 정보(header)에 remember-me cookie 값을 확인한다. (없으면 동작하지 않는다.)



###  AnonymousAuthenticationFilter

→ 사용자가 인증시도 없고 권한없이 특정 자원에 접근 시도시 동작한다.

1. 인증되어있는 상태기 때문에 그냥 통과한다.



###  SessionManagementFilter

→ Session에 SecurityContext가 없는 경우나 Session이 없는 경우 동작한다.

1. 인증 후 접근이기에 다음 필터로 이동



### ExceptionTranslationFilter

→ Try Catch로 다음 필터 동작을 감싸서 FilterSecurityInterceptor수행 중 일어나는 예외를 받아서 동작한다.



### FilterSecurityInterceptor

1. AccessDecisionVoter 에게 인가 처리 위임
   * 인증객체가 없을경우 AuthenticationException발생하여 ExceptionTranslationFilter 에게 전달
   * 접근이 인가되지 않을 경우 AccessDeniedException 발생하여  ExceptionTranslationFilter 에게 전달

#### 

## 동일 계정 다른기기 중복 로그인 - 최대 1개 세션 허용 정책



### SecurityContextPersistenceFilter

→ 첫 번째 인증 사용자의 로직과 동일하게 수행



###  LogoutFilter 

→ 인증 시도이기 때문에 다음 필터로 패스



###  UsernamePasswordAuthenticationFilter 

1. 인증 성공 후 SecurityContextHolder안에 인증객체(Authentication)이 저장된 SecurityContext저장

2. ConcurrentSession 에서 정책 확인 
   * 현재 사용자 인증 시도 차단 정책: SessionAuthenticationException 예외를 발생하여 인증 처리 실패
   * 이전 사용자 세션 만료 정책: session.expireNow 를 사용해 이전 사용자의 세션을 만료시킨 후 자신의 인증객체 저장. 

#### → 이전 사용자 세션 만료 정책일 경우 이 다음부터는 기존 인증 요청과 동일하게 로직 수행



## 동일 계정이 다른기기에서 로그인 되어 세션이 만료된 계정에서 자원 요청(Request)

### ConcurrentSessionFilter

1. session.isExpired를 통해 현재 세션이 만료되었는지 확인

2. 만료되었기 때문에  Logout 로직 수행

3. error 응답후 다음 필터 수행 없이 종료.





###



* https://catsbi.oopy.io/f9b0d83c-4775-47da-9c81-2261851fe0d0
* 인프런 정수원님 스프링 시큐리티 강의

