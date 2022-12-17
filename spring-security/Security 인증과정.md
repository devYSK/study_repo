# Spring Security 인증 과정



인증 과정 전에 인증과 인가에 대해 용어 정리를 간략하게 하려고 한다

#### [인증(Authentication)](https://0soo.tistory.com/134#%EC%-D%B-%EC%A-%-D-Authentication-)

- 사용자의 신원을 확인하는 과정
  - 아이디/패스워드 기반 로그인
  - OAuth2.0 프로토콜을 통한 Social 인증

#### [인가(Authorization)](https://0soo.tistory.com/134#%EC%-D%B-%EA%B-%---Authorization-)

- 어떤 개체가 어떤 리소스에 접근할 수 있는지 또는 어떤 동작을 수행할 수 있는지를 검증하는 것, 즉 접근 권한을 얻는 일을 의미
- 적절한 권한이 부여된 사용자들만 특정 기능 수행 또는 데이터 접근을 허용함



<img src="https://blog.kakaocdn.net/dn/bu7VFH/btrTQPruSpV/Kf06duIKw0gP9TY7vKyiw0/img.png" width = 850 height = 600>

1. **사용자가 로그인 정보와 함께 인증 요청을 한다.(Http Request)**

2. **AuthenticationFilter가 요청을 가로채고, 가로챈 정보를 통해 UsernamePasswordAuthenticationToken의 인증용 객체인 authentication를 생성한다. (현재는 인증되어있지 않다. 그냥 객체만 생성한다)**
   * AbstractAuthenticationProcessingFilter에서 처리하는데, 이 필터는 보통 UserNamePasswordAuthenticationFilter이다

3. **AuthenticationManager의 구현체인 ProviderManager에게 생성한 UsernamePasswordToken 객체를 전달한다.**

   AbstractAuthenticationProcessingFilter에서 attemptAuthentication()을 호출

   * requiresAuthentication(request, response)는 이 필터가 이 필터가 현재 호출에 대한 로그인 요청을 처리해야 하는지 여부 확인
   * requiresAuthenticationRequestMatcher = AntPathRequestMatcher
     * 요청 메소드와 url pattern이 /login, Post 인지 확인
     * 로그인 요청이 아니면 즉시 dofilter 호출 하므로 필터가 아무일도 안함.
   * UsernamePasswordAuthenticationToken을 생성 
     * 이 때 아직은 인증되지 않은 Token이다. UsernamePasswordAuthenticationToken.`unauthenticated()`
     * principal 타입이 Object인 이유는 인증되기 전에는 String Type일 수 있고, 인증된 후에는 다른 Type일 수 있어서 Object Type이다

4. **AuthenticationManager는 등록된 AuthenticationProvider(들)을 조회하여 인증을 요구한다.**
   * 일반적인 Manager는 ProviderManager이며, ProviderManager는 내부에 AuthenticationProvider를 리스트로 가지고 있고, 아이디와 패스워드 인증이라면 DaoAuthenticationProvider를 사용한다 
   * 실질적으로 인증처리는 `AuthenticationProvider` 가 한다. 
   * ProviderManager는 parent 라는 변수에 자신의 부모이자 같은 객체인 ProviderManager를 들고 있는데, 자신이 가지고 있는 Prodiver List가 처리할 수 없다면 Parent Provider에게 인증 처리를 요구한다. 
   * DaoAuthenticationProvider는 UserDetailsService를 이용해 인증 처리를 한다

5. **실제 DB에서 사용자 인증정보를 가져오는 UserDetailsService에 사용자 정보를 넘겨준다.**

   * loadUserByUsername() 메소드 사용

6. **넘겨받은 사용자 정보를 통해 DB에서 찾은 사용자 정보인 UserDetails 객체를 만든다.**

7.  **AuthenticationProvider(들)은 UserDetails를 넘겨받고 사용자 정보를 비교한다.**

   * AbstractUserDetailsAuthenticationProvider.preAuthenticationChecks.check(user);
   * AbstractUserDetailsAuthenticationProvider는 DaoAuthenticationProvider의 추상클래스

8. **인증이 완료되면 권한 등의 사용자 정보를 담은 Authentication 객체를 반환한다.**

   * AbstractUserDetailsAuthenticationProvider.createSuccessAuthentication()메소드

9. **다시 인증을 진행했던 필터인 AuthenticationFilter에 Authentication 객체가 반환된다.**

   * 인증을 진행했던 필터인 AbstractAuthenticationProcessingFilter 필터.

10. **인증 객체인Authenticaton 객체를 SecurityContext에 저장한다.**

    * AbstractAuthenticationProcessingFilter.successfulAuthentication() 메소드에서 SecurityContext에 저장한다. 



최종적으로 SecurityContextHolder는 세션 영역에 있는 SecurityContext에 Authentication 객체를 저장한다.

사용자 정보를 저장한다는 것은 Spring Security가 전통적인 세션-쿠키 기반의 인증 방식을 사용한다는 것을 의미한다.



# 폼 로그인을 한다면, 인증이 일어나는 순서

-> AuthenticationManager를 구현한 ProviderManager가 인증 수행

1. ProviderManager.authenticate(Authentication `authentication`) 메소드 내에서 인증 수행

   - 인자로 받은 authentication은 우리가 폼에서 입력한 정보
     (UsernamePasswordAuthenticationToken)

2. authentication 인증 객체는 Provider가 처리하는데, ProviderManager가 가진 `List<AuthenticationProvider> providers` 중에
   현재 인증 객체를 처리 할 수 있는 Provider가 없으면 parent로 간다.
   (다른 Provider가 처리할 수 있게, 링크드 리스트 처럼 연결)

   ```
   if (result == null && this.parent != null) {        
       result = this.parent.authenticate(authentication);
   ```

3. parent Provider에서 다시 인증 할 수 있는지 확인한다. (다른 Provider 클래스)

4. 다른 Provider가 인증을 처리 할 수 있다면, UserDetails서비스를 사용해서 인증한다.

   - AbstractUserDetailsAuthenticationProvider.authenticate() 메서드

5. 캐시된 유저가 있나 등을 확인하고, retrieveUser() 메서드를 호출하여 Provider 안으로 들어간다.(예제는 DaoAuthenticationProvider)

6. 이 Provider의 `protected final UserDetails retrieveUser` 메소드에서
   사용하는 UserDetailsService가 우리가 만든 UserDetailsService를 구현한 AccountService 이다

```java
// DaoAuthenticationProvider 클래스의 retrieveUser 메서드
protected final UserDetails retrieveUser(String username, 
                                          UsernamePasswordAuthenticationToken authentication) 
                                          throws AuthenticationException {
    this.prepareTimingAttackProtection();
    try {
        UserDetails loadedUser = this.getUserDetailsService().loadUserByUsername(username); // <<<<
        if (loadedUser == null) {
            throw new InternalAuthenticationServiceException("UserDetailsService returned null, 
                                                             which is an interface contract violation");
        } else {
            return loadedUser;
        }
    }
}
```

1. 이후 추가적인 체크를 한다 (계정이 lock인지 등)

2. 무사히 다 통과한다면 result 라는 authentication 객체 를 리턴하는데, 이 authentication 객체

   가 UserDetailsService를 구현한 구현체인 AccountService에서 loadUserByUsername 메서드의 리턴값인 UserDetails 이다.

   - 이 `UserDetails` 객체 가 Authencation 안에 들어가는 `principal`객체이다



> ProviderManager의 authenticate 메서드에서 리턴한 result 객체가 인증이 되면,
> principal이 되어 authentication 객체가 되고
> SecurityContextHolder.getContext().getAuthentication 으로 꺼낼 수 있게 된다.
>
> Controller에서도 파라미터로 주입받아 사용할 수 있게 된다. 
>
> -- `@AuthenticationPrincipal`



---

>  밑에는, 다른그림을 기준으로 인증과정을 정리한 내용인데, 위 내용이 이해가 간다면 아래는 필요가 업다.

# 다른 그림의 인증과정



<img src="https://blog.kakaocdn.net/dn/cDp8Iq/btrTT6SXJqS/WJsVLVDigRN405yPU9uGX1/img.png" width= 900 height=550>

인증 처리 과정

* Authentication : 인증 주체. 사용자를 표현하는 객체. 인증되기전과 인증된 후 모두를 표현한다. 
  * 노란색 은 인증 전 Authentication 객체
  * 녹색은 인증된 후 사용자를 표현하는 Authentication 객체 
  * 인증 된 사용자 객체는 GrantedAuthority를 가질 수 있다. (권한)



* AbstractAuthenticationProcessingFilter : 그림의 주황색 AuthenticationProcessingFilter이다.

* AbstractAuthenticationProcessingFilter = UserNamePasswordAuthenticationFilter
  * 이 필터 안에서 인증이 진행되고 인증되면 Authentication 객체를 얻는다
* 먼저, 인증되지 않은 사용자인 Authentication을 generate한다 

* AbstractAuthenticationProcessingFilter에서 attemptAuthentication()을 호출
  * requiresAuthentication(request, response)는 이 필터가 이 필터가 현재 호출에 대한 로그인 요청을 처리해야 하는지 여부 확인
  * requiresAuthenticationRequestMatcher = AntPathRequestMatcher
    * 요청 메소드와 url pattern이 /login, Post 인지 확인
    * 로그인 요청이 아니면 즉시 dofilter 호출 하므로 필터가 아무일도 안함.
  * UsernamePasswordAuthenticationToken을 생성 
    * 이 때 아직은 인증되지 않은 Token이다. UsernamePasswordAuthenticationToken.`unauthenticated()`
    * principal 타입이 Object인 이유는 인증되기 전에는 String Type일 수 있고, 인증된 후에는 다른 Type일 수 있어서 Object Type이다

* AuthenticationManager(ProviderManager class)를 호출하여 authenticate(인증) 진행 
  * ProviderManager는 AuthenticationProvider를 List로 들고있다. 	
  * 실질적으로 인증처리는 `AuthenticationProvider` 가 한다. 
  * ProviderManager는 parent 라는 변수에 자신의 부모이자 같은 객체인 ProviderManager를 들고 있는데 이 ProviderManager가 실질적으로 인증 처리를 한다 
    * 왜냐하면, ProviderManager가 들고 있는 AnonymoursAuthenticationProvider랑 RemeberMeAuthenticationProvider는 실질적으로 UsernamePasswordAuthenticationToken을 처리할 수 없기 때문에. 부모 ProviderManager가 가진 DaoAuthenticationProvider를 이용하여 처리한다.

<img src="https://blog.kakaocdn.net/dn/M7UZ8/btrTR8Rghpq/V98nKRNzVgTbCamwi4gCwK/img.png" width = 800 height =500>

* 즉 AuthenticationManager는 인터페이스이고, 구현체가 Provider들을(List, 또는 parent) 가지고 있는데, 이 Provider마다 처리할 수 있는 Authentication 객체 종류가 다르다. Authentication을 처리할 수 있는 Provider실질적인 인증을 처리한다.

* DaoAuthenticationProvider는 데이터베이스에서 유저를 가지고 온다
  * UserDetailsService().loadUserByUserName(username)
  * 이때 가져오는 User는 USerDetails 객체. 
* 인증이 되면 다시 UsernamePasswordAuthenticationToken을 인증된 객체로 만들어 리턴한다. 

 
