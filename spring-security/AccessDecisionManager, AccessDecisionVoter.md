# AccessDecisionManager, AccessDecisionVoter - 인가 결정 심의자



`AccessDecisionManager`는 `AbstractSecurityInterceptor`에서 호출하며, 최종적인 접근 제어를 결정한다. \

* FilterSecurityInterceptor (AbstractSecurityInterceptor)

`AccessDecisionManager`는 세 가지 메소드가 있다:

```java
void decide(Authentication authentication, Object secureObject,
    Collection<ConfigAttribute> attrs) throws AccessDeniedException;

boolean supports(ConfigAttribute attribute);

boolean supports(Class clazz);
```

* decide() : 권한을 결정하기 위해 필요한 모든 정보를 건내 받는다. 
  * 특히, 보안 `Object`를 건내 받으면 실제 보안 객체를 실행할 때 넘긴 인자를 검사할 수 있다. 예를 들어 보안 객체가 `MethodInvocation`이었다고 가정해보자. `MethodInvocation`으로 모든 `Customer` 인자를 쉽게 찾을 수 있으며, `AccessDecisionManager` 안에선 일련의 보안 로직으로 principal이 customer 관련 동작을 실행하도록 허가할 수 있다.
  * 접근을 거절한 경우엔 `AccessDeniedException`을 던진다.
* supports(ConfigAttribute): `ConfigAttribute`의 처리 가능 여부를 결정
  * 시큐리티 인터셉터 구현체가 호출하며, 설정해둔 `AccessDecisionManager`가 시큐리티 인터셉터가 제출할 보안 객체 타입을 지원하는지 확인한다.

### AccessDecisionManager 인터페이스

* 인증,요청,권한 정보를 이용해서 사용자의 자원접근을 허용/거부 여부를 최종 결정하는 주체다.

* 여러 개의 Voter들을 가질 수 있고, Voter들로부터 접근허용,거부,보류에 해당하는 각각의 값을 리턴받아 판단&결정한다.
  * AccessDecisionVoter들의 투표(vote)결과를 취합하고, 접근 승인 여부를 결정하는 3가지 구현체를 제공함
  * 추상클래스인 AbstractAccessDecisionManager가 AccessDecisionVoter 목록을 갖고 있음

* 최종 접근 거부시 예외 발생



##  접근결정의 **세가지 유형** - AccessDecisionManager 구현체

> 인가와 관련한 모든 동작을 제어하고 싶다면 커스텀 `AccessDecisionManager`를 사용해도 되지만, 
>
> 스프링 시큐리티는 투표(Vote)를 기반으로 동작하는 몇 가지 `AccessDecisionManager` 구현체를 제공한다







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

`AccessDecisionVoter`는 메소드 세 개를 가진 인터페이스다:

```java
public interface AccessDecisionVoter<S> {

	int ACCESS_GRANTED = 1;

	int ACCESS_ABSTAIN = 0;

	int ACCESS_DENIED = -1;

	boolean supports(ConfigAttribute attribute);

	boolean supports(Class<?> clazz);

	int vote(Authentication authentication, S object, Collection<ConfigAttribute> attributes);

}
```



구현체는 `AccessDecisionVoter`의 스태틱 필드 `ACCESS_ABSTAIN`, `ACCESS_DENIED`, `ACCESS_GRANTED` 중 하나를 의미하는 `int` 값을 리턴한다. 

접근을 허용하면  `ACCESS_DENIED`,  접근을 허용하지 않으면  `ACCESS_GRANTED`를 리턴해야 한다.



* 판단을 심사하여 투표한다. (위원)

* 각각의 Voter마다 사용자의 요청마다 해당 자원에 접근할 권한이 있는지 판단 후 AccessDecisionManager에게 반환하는 역할

* 추상클래스인 AbstractAccessDecisionManager가 AccessDecisionVoter List(목록)을 갖고 있음



### Custom Voters

당연히 커스텀 `AccessDecisionVoter`로도 원하는 곳에 접근 제어 로직을 넣을 수 있다. 보통 어플리케이션에 특화된 로직이나 (비지니스 로직 관련) 보안 관리 로직을 구현하는 데 사용한다. 예를 들어 스프링 웹사이트에 있는 [블로그 문서](https://spring.io/blog/2009/01/03/spring-security-customization-part-2-adjusting-secured-session-in-real-time)에서 설명하는 방법으로, 실시간으로 정지된 계정의 접근을 거절할 수 있다.



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

