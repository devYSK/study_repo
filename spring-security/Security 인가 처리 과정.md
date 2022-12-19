

# Security 인가 처리 개념 및 과정 (Authorization), AccessDecisionManager



인가(Authorization) : 인증 된 사용자가 특정 자원에 접근하고자 할 때 접근 할 자격이 되는지 증명하는 것

<img src="https://blog.kakaocdn.net/dn/y9Wac/btrTUEKzGeg/kxdOYKVPeAwLxdlEuiDAq0/img.png" width = 900 height =400>

* 사용자가 특정 자원에 접근하고자 요청(Request)을 하면 그 사용자가 인증을 받았는지 확인한다.
  * 그림상 사용자의 자격(권한)은 Manager이다

* 인증을 받은 사용자라면 해당 사용자의 자격(권한)이 해당 자원에 접근할 자격이 되는지 확인한다.
  * 일반적으로, 인가를 처리하는 Spring Seucirty FilterSecurityInterceptor에 도착하면 인증이 완료되어있다.
  * 익명 사용자도 인증이 완료된 것으로 간주하며, ROLE_ANONYMOUS 권한을 갖음

* 인가 처리(Authorization) 에서 사용자의 권한(자격) Manager이기 때문에  User Section, Manager Section까지는 접근이 가능하다.

* 그러나 Admin Section의 Resources는 권한 부족으로 접근이 허용되지 않는다.



## 스프링 시큐리티가 지원하는 권한 계층

#### 1. 웹 계층

* URL요청에 따른 메뉴 혹은 화면단위의 레벨 보안 
* → /user 경로로 자원 접근을 할 때 그 자원에 설정된 권한(ROLE_USER)과 사용자가 가진 권한을 서로 심사해서 결정하는 계층

<img src="https://blog.kakaocdn.net/dn/8RIq3/btrT4R81UIA/B3cNJRIDNJC7xKdcE9muX1/img.png" width = 400 height =100 style="float: left">

#### 2. 서비스 계층

* 화면 단위가 아닌 메소드 같은 기능 단위의 레벨 보안 
* → user() 라는 메소드에 접근하고자 할 때 해당 메소드에 설정된 권한과 사용자가 가진 권한을 서로 심사해서 결정하는 계층

<img src ="https://blog.kakaocdn.net/dn/Jv30m/btrT2Rva9O1/Jvo5brV1YN1JFE9Hv3SWk1/img.png" width=400 hegiht=100 style="float: left">

#### 3. 도메인 계층(Access Control List, 접근 제어 목록)

* 객체 단위의 레벨 보안 
* → 객체(user)를 핸들링 하고자 할 때 도메인에 설정된 권한과 사용자가 가진 권한을 서로 심사해서 결정하는 계층

<img src="https://blog.kakaocdn.net/dn/xS8M8/btrT5amYkjs/NnwSTrSzMegGGDabCcv8vK/img.png" width = 400 height =100 style="float:left">





> Spring Security는 Filter Chain의 마지막에 존재하는 FilterSecurityInterceptor 에서 인가를 결정한다.



## 인가(Authorization) 처리 권한 설정 

* 선언적 방식 - SecurityConfig에서 http 설정을 통한 설정 
  * URL : http.antMatchers("/api/v1/**").hasRole("USER")
  * Method : @PreAuthorize("hasRole('USER')") 
    * @EnableMethodSecurity를 활성화 해야 한다. 

* 동적 방식 - DB에 연동해서 프로그래밍 한다 
  * URL
  * Method



## FilterSecurityInterceptor

필터 체인 상에서 가장 마지막에 위치하며, 사용자가 갖고 있는 권한과 리소스에서 요구하는 권한을 취합하여 접근을 허용할지 결정한다.

* 권한 제어 방식 중 `HTTP 자원의 보안`을 처리하는 필터

- 실질적으로 접근 허용 여부 판단은 AccessDecisionManager 인터페이스 구현체에서 이루어짐



FilterSecurityInterceptor에서는 요청정보, 권한정보, 인증정보가 필요하며 이것을 획득하면 AccessDecisionManager에게 보내서 이곳에서 인가처리를 하게 된다. 



**인증객체** **없이** **보호자원에** **접근을 시도할 경우** **AuthenticationException** **을 발생**

**인증 후 자원에 접근 가능한 권한이 존재하지 않을 경우** **AccessDeniedException** **을 발생**



- 해당 필터가 호출되는 시점에서 사용자는 이미 인증이 완료되고, Authentication 인터페이스의 getAuthorities() 메소드를 통해 인증된 사용자의 권한 목록을 가져올수 있음
  - 익명 사용자도 인증이 완료된 것으로 간주하며, ROLE_ANONYMOUS 권한을 갖음
  - 일단 인증이 되있는거다. 인가 여부에 따라 응답해줄지, 예외를 던질지 달라진다. 
  - AbstractSecurityInterception.attemptAuthorization에서 실제 권한체크를 한다. 
    - accessDecisionManager.decide(authenticated, object, attributes);



## FilterSecurityInterceptor 인가 Flow

<img src="https://blog.kakaocdn.net/dn/LmZ6Y/btrT499rm3y/IXbFn3FHaApUXea3U1b4jK/img.png" width = 900 height =500>

1. 사용자가 자원 접근(Request)

2. FilterSecurityInterceptor에서 요청을 받아서 인증여부를 확인한다.
   *  인증객체를 가지고 있는지 확인한다. 
   * 인증객체가 없으면(null) AuthenticationException 발생 
   * ExceptionTranslationFilter에서 해당 예외를 받아서 다시 로그인 페이지로 이동하던가 후처리를 해준다.

3. 인증객체가 있을 경우 SecurityMetadataSource는 자원에 접근하기 위해 설정된 권한정보를 조회해서 전달해준다.
   * 권한 정보를 조회한다.
   * 권한정보가 없으면(null) 권한 심사를 하지 않고 자원 접근을 허용한다.

4. 권한 정보가 있을 경우 AccessDecisionManager 에게 권한 정보를 전달하여 위임한다.
   * *→ AccessDecisionManager는 최종 심의 결정자다.*

5. AccessDecisionManager가 내부적으로 AccessDecisionVoter(심의자)를 통해서 심의 요청을 한다.

6. 반환된 승인/거부 결과를 가지고 사용자가 해당 자원에 접근이 가능한지 판단한다.
   * 접근이 거부되었을 경우 AccessDeniedException이 발생한다.
   * ExceptionTranslationFilter에서 해당 예외를 받아서 다시 로그인 페이지로 이동하던가 후처리를 해준다.

7. 접근이 승인되었을 경우 자원 접근이 허용된다.





# AccessDecisionManager, AccessDecisionVoter - 인가 결정 심의자



### AccessDecisionManager 인터페이스

* 인증,요청,권한 정보를 이용해서 사용자의 자원접근을 허용/거부 여부를 최종 결정하는 주체다.

* 여러 개의 Voter들을 가질 수 있고, Voter들로부터 접근허용,거부,보류에 해당하는 각각의 값을 리턴받아 판단&결정한다.
  * AccessDecisionVoter들의 투표(vote)결과를 취합하고, 접근 승인 여부를 결정하는 3가지 구현체를 제공함
  * 추상클래스인 AbstractAccessDecisionManager가 AccessDecisionVoter 목록을 갖고 있음

* 최종 접근 거부시 예외 발생



##  접근결정의 **세가지 유형** - AccessDecisionManager 구현체



1. AffirmativeBased 클래스 - OR연산자와 같은 논리. 하나라도 참이면 참

   * AccessDecisionVoter가 승인하면 이전에 거부된 내용과 관계없이 접근이 승인됨 (기본값)

   * 여러개의 Voter클래스 중 하나라도 접근 허가로 결론을 내면 접근 허가로 판단한다.

<img src="https://blog.kakaocdn.net/dn/d6LoM5/btrT4znep3K/nUroEwkAkHFGkxLkZkxO7K/img.png" width=350 height=130 style="float : left">

2. ConsensusBased 클래스 - 투표 결과가 다수결의 원칙을 따름
   * 다수표(승인 및 거부)에 의해 최종 결정을 판단한다.
   * 동수일 경우 기본은 접근허가이나 allowIfEqualGrantedDeniedDecisions을 false로 설정할 경우 접근 거부로 결정된다.

<img src="https://blog.kakaocdn.net/dn/DiV0F/btrT2RhDqXv/hZU8yIR6pbkGGBsRQi1d20/img.png" width=350 height=130 style="float: left">

3. UnanimousBased 클래스 - AND와 동일한 논리. 모두 참이여야 한다 
   * 모든 Voter가 만장일치로 접근을 승인해야 하며 그렇지 않은 경우 접근을 거부한다.

<img src="https://blog.kakaocdn.net/dn/vrBgG/btrTVhPcGLL/CZ5F26gQuUCPYEKZ8nmFT1/img.png" width= 350 height=130 style="float : left">



> default로 AffirmativeBased 클래스를 사용한다. 



## 접근결정을 투표하는 클래스  AccessDecisionVoter



판단을 심사하여 투표한다. (위원)

각각의 Voter마다 사용자의 요청마다 해당 자원에 접근할 권한이 있는지 판단 후 AccessDecisionManager에게 반환하는 역할

추상클래스인 AbstractAccessDecisionManager가 AccessDecisionVoter List(목록)을 갖고 있음





## Voter가 권한 부여 과정에서 판단하는 자료들

FilterSecurityInterceptor가 AccessDecisionManager에게 요청하고, AccessDecisionManager에게 아래 정보를 전달받아 판단한다



* `Authenticaion` - 인증정보(user)
  * 인증정보는 SecurityContectHolder에서 현재 인증정보

* `FilterInvocator`  - 요청 정보(antMatcher("/user"))
  * 만약 www.localhost:8080/login 을 입력했다면 request, response, chain을 FilterInvocation의 생성자에 넘겨 저장하고 입력했던 요청정보를 알아낼 수 있다.



* `ConfigAttributes` - 권한 정보(hasRole("USER")
  * 권한정보는 사용자가 접속하려는 요청정보의 권한을 알아내는 것이며, 이 권한정보는 FilterInvocationMetadataSource와 연관이 있다.
  * `SecurityMetadataSource 인터페이스`
  * SecurityConfig에서 URL과 그에 맞는 권한 정보를 설정해주었다면 이것은 ExpressBasedFilterInvocationSecurityMetadataSource의 requsetToExpressionAttributesMap에 저장
  * 이것은 부모 클래스인 DefaultFilterInvocationSecurityMetadataSource으로 넘겨 requsetMap이라는 변수에 저장하고 요청이 들어왔을 때 이 클래스의 getAttributes라는 메서드를 이용하여 권한정보를 반환



#### 결정 방식 -  AccessDecisionManager 은 반환 받은 결정 방식을 사용해서 후처리를 한다.

* ACCESS_GRANTED: 접근 허용(1)

* ACCESS_DENIED: 접근 거부(-1)

* ACCESS_ABSTAIN: 접근 보류(0)
  * Voter가 해당 타입의 요청에 대해 결정을 내릴 수 없는 경우



> ACCESS_GRANTED나 ACCESS_DENIED를 AccessDecisionManager에게 리턴하여 인가 여부를 결정한다





## AccessDecisionManager FLow

<img src="https://blog.kakaocdn.net/dn/cgrs2p/btrT4IqQiPj/mH4bVKQT0qD6pVC88kukE1/img.png" width = 900 height = 450>

1. FilterSecurityInterceptor 가 AccessDecisionManager에 인가처리 위임

2. AccessDecisionManager는 자신이 가지고 있는 Voter들에게 정보decide(authentication, object, configAttributes)를 전달한다.

3. Voter들은 정보들을 가지고 권한 판단을 심사한다.

4. 승인,거부,보류 결정방식을 반환하면  AccessDecisionManager에서는 반환받은 결정 방식을 가지고 후처리를 한다.
   * 승인: FilterSecurityInterceptor에 승인여부 반환
   * 거부: AccessDeniedException 예외를 ExceptionTranslationFilter로 전달

<img src="https://blog.kakaocdn.net/dn/8vB0w/btrT0Rvsnzn/qzBZlgu3HhonczXTGGLar1/img.png" width = 900 height = 600>





## WebExpressionVoter 구현체

- SpEL 표현식을 사용해 접근 승인 여부에 대한 규칙을 지정할 수 있다
- SpEL 표현식 처리를 위해 DefaultWebSecurityExpressionHandler 그리고 WebSecurityExpressionRoot 구현에 의존함
  - DefaultWebSecurityExpressionHandler.createSecurityExpressionRoot() 메소드에서 WebSecurityExpressionRoot 객체를 생성함
- WebSecurityExpressionRoot 클래스는 SpEL 표현식에서 사용할수 있는 다양한 메소드를 제공

| Expression                                                   | Description                                                  |
| :----------------------------------------------------------- | :----------------------------------------------------------- |
| ` access(String)`                                            | 주어진 SpEL 표현식의 평가 결과가 true이면 접근을 허용        |
| `hasRole(String role)`                                       | 현재 principal이 명시한 role을 가지고 있으면 `true`를 리턴한다.  예를 들어, `hasRole('admin')`  기본적으로 파라미터로 넘긴 role이 ‘ROLE_‘로 시작하지 않으면 이 프리픽스를 추가한다. `DefaultWebSecurityExpressionHandler`의 `defaultRolePrefix`를 수정하면 커스텀할 수 있다. |
| `hasAnyRole(String… roles)`                                  | 현재 principal이 명시한 role 중 하나라도 가지고 있으면 `true`를 리턴한다 (문자열 리스트를 콤마로 구분해서 전달한다).  예를 들어 `hasAnyRole('admin', 'user')`  기본적으로 파라미터로 넘긴 role이 ‘ROLE_‘로 시작하지 않으면 이 프리픽스를 추가한다. `DefaultWebSecurityExpressionHandler`의 `defaultRolePrefix`를 수정하면 커스텀할 수 있다. |
| `hasAuthority(String authority)`                             | 현재 principal이 명시한 권한이 있으면 `true`를 리턴한다.  예를 들어 `hasAuthority('read')` |
| `hasAnyAuthority(String… authorities)`                       | 현재 principal이 명시한 권한 중 하나라도 있으면 `true`를 리턴한다 (문자열 리스트를 콤마로 구분해서 전달한다).  예를 들어 `hasAnyAuthority('read', 'write')` |
| `principal`                                                  | 현재 사용자를 나타내는 principal 객체에 직접 접근할 수 있다. |
| `authentication`                                             | `SecurityContext`로 조회할 수 있는 현재 `Authentication` 객체에 직접 접근할 수 있다. |
| `permitAll()`                                                | 항상 `true`로 평가한다.                                      |
| `denyAll()`                                                  | 항상 `false`로 평가한다.                                     |
| `isAnonymous()`                                              | 현재 principal이 익명 사용자면 `true`를 리턴한다.            |
| `isRememberMe()`                                             | 현재 principal이 remember-me 사용자면 `true`를 리턴한다.     |
| `isAuthenticated()`                                          | 사용자가 익명이 아니면 `true`를 리턴한다.                    |
| `isFullyAuthenticated()`                                     | 사용자가 익명 사용자나 remember-me 사용자가 아니면 `true`를 리턴한다. |
| `hasPermission(Object target, Object permission)`            | 사용자가 target에 해당 permission 권한이 있으면 `true`를 리턴한다. 예를 들어 `hasPermission(domainObject, 'read')` |
| `hasPermission(Object targetId, String targetType, Object permission)` | 사용자가 target에 해당 permission 권한이 있으면 `true`를 리턴한다. 예를 들어 `hasPermission(1, 'com.example.domain.Message', 'read')` |
| ` hasIpAddress(String)`                                      | 주어진 IP로부터 요청이 왔다면 접근을 허용                    |
| ` rememberMe()`                                              | 기억하기를 통해 인증된 사용자의 접근을 허용                  |

SpEL 표현식 을 커스텀하여 구현할 수도 있다. 