# FilterSecurityInterceptor - 인가(Authorization) 처리 필터

필터 체인 상에서 가장 마지막에 위치하며, 사용자가 갖고 있는 권한과 리소스에서 요구하는 권한을 취합하여 접근을 허용할지 결정한다.

* 실질적으로 접근 허용 여부 판단은 FilterSecurityInterceptor 내부 프로세스에서 AccessDecisionManager 인터페이스 구현체에서 이루어짐

해당 필터가 호출되는 시점에서 사용자는 이미 인증이 완료되고, Authentication 인터페이스의 getAuthorities() 메소드를 통해 인증된 사용자의 권한 목록을 가져올수 있다

- 익명 사용자도 인증이 완료된 것으로 간주하며, ROLE_ANONYMOUS 권한을 갖음
- 일단 인증이 되어있다.  인가 여부에 따라 응답해줄지, 예외를 던질지 달라진다. 
- AbstractSecurityInterception.attemptAuthorization에서 실제 권한체크를 한다. 
  - accessDecisionManager.decide(authenticated, object, attributes);



> 여기까지 살아서 왔다면 인증(Authentication)이 있다는 거니, 니가 요청하는 request 에 들어갈 자격(인가, Authorization)이 있는지 그리고 결과를 너에게 보내줘도 되는건지 마지막으로 내가 점검해 줄께.



@EnableWebSecurity(debug=true) 옵션을 주면 요청이 들어올때마다 어떤 필터들을를 지났는지 출력해준다

* 시큐리티 필터 체인 순서는 @Order(1) 어노테이션을 선언해서 설정 가능하고, 오더의 순서가 낮을수록(숫자가 낮을수록) 먼저 필터를를 거친다



## 인가(Authorization) 처리 권한 설정 



선언적 방식과 동적 방식으로 나누어진다

* 선언적 방식 - SecurityConfig에서 http 설정을 통한 설정 
  * URL : http.antMAtchers("/api/v1/**").hasRole("USER")
  * Method : @PreAuthorize("hasRole('USER')") 
    * @EnableMethodSecurity를 활성화 해야 한다. 
  
* 동적 방식 - DB에 연동해서 프로그래밍 한다 
  * URL
  * Method



## 선언적 방식 - URL 설정

SecurityConfig에서 HttpSecurity 객체를 이용해서 설정한다 

```java
@Bean
public SecurityFilterChain configure(HttpSecurity http) throws Exception {
    http
        .antMatcher("/shop/**")
        .authorizeRequests()
            .antMatchers("/shop/login", "/shop/users/**").permitAll()
	  				.antMatchers("/shop/mypage").hasRole(“USER”)
            .antMatchers("/shop/admin/pay").access("hasRole('ADMIN')");
	  				.antMatchers("/shop/admin/**").access("hasRole('ADMIN') or hasRole(‘SYS ')");
            .anyRequest().authenticated()
            .and()
            .build();  
}
            
```

> .exceptionHandler()를 통한 커스텀 익셉션 핸들러를 달 수 있으며, 커스텀한 경우에는 반드시 달아야 하고 그렇지 않으면 기본 핸들러가 실행된다. 
>
> * DEBUG 로그 레벨로는 확인할 수 있으며, ExpressionUtils 클래스에 브레이크 포인트를 걸어두고 확인할 수 있음

- mvcMatches() : Spring MVC 패턴하고 매칭이 되는지. antPattern으로 폴 백 됨
  - regexMatchers를 사용하거나 antMatchers도 사용 가능
- hasRole() : 권한이 있는지. Authority가 상위개념
- hasAuthority() : 권한이 있는지. Role의 상위개념
  - hasAuthority() 를 사용시 ROLE_를 붙여줘야 한다 ex) hasAuthority("ROLE_USER");
- permitAll() : 모두에게 허용
- anyRequest()
  - .anonymous() : 익명 사용자에게만 접근할 수 있게 하는것
    - 인증을 하고 접근하면 접근이 거부가 된다.
  - .authenticated() : 인증이 되기만 하면 접근 가능
  - .rememeberMe() : 리멤버 미 기능으로 인증한 사용자 접근 가능
  - .fullyAuthenticated() : 리멤버 미로 인증이 된 사용자에게 다시 인증을 요구
    - 인증이 된 사용자도 한번 더 인증(로그인)을 요구하는것.
    - 가령 장바구니에서 주문할 때 다시 한번 더 인증을 확인하는거
  - .denyAll() : 아무것도 허용하지 않겠다.
  - .not() : 뒤에 조건이 아닌 경우 허용 ex) .not().anonymous() : 익명사용자가 아닌경우 허용



>  **주의 사항** **-** **설정 시 구체적인 경로가 먼저 오고 그것 보다 큰 범위의 경로가 뒤에 오도록 해야 한다**

### 인가 API 표현식

WebSecurityExpressionRoot 클래스는 SpEL 표현식에서 사용할수 있는 다양한 메소드를 제공

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







## 동적 방식 - DB 이용

일반적으로 Spring-security configuration에서 authorizeRequest()를 보면 요청URL과 그 URL에 접근 가능한 권한을 설정하는 부분이 하드코딩 되어있다.

이는 권한을 변경하기 위해 어플리케이션의 코드를 변경해야한다는 것을 의미한다.

이 단점을 극복하기 위해 DB에 요청 URL과 권한에 대한 매핑 정보를 저장하여  URL에 요청할 때마다 해당 DB에 접근하여 권한이 있는지 확인하는 작업을 수행할 수 있다.

* Trade-off로 DB를 계속 뒤져야 하는 단점이 있다.



요청 URL과 권한 정보가 매핑된 데이터를 로딩하기 위해 새롭게 엔티티나 DTO, Repository가 필요하며, 이를 이용하도록 Spring-security 설정이 변경되어야 한다. 



### 1. Entity 및 Repository 생성

```java
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SecurityUrlMatcher {
 	
  	@Id 
    private String url;
  
    private String authority;
    
    public void setUrl(String url) {
        this.url = url;
    }
   
    public void setAuthority(String authority) {
        this.authority = authority;
    }
}

//
@Repository
public interface SecurityUrlMatcherRepository extends JpaRepository<SecurityUrlMatcher, String> {
 
    List<SecurityUrlMatcher> findAll();
}
 
```



### 2. SecurityConfig

```java
@Bean
public SecurityFilterChain configure(HttpSecurity http) throws Exception {
	return http
    .authorizeRequests()
    .antMatchers("/", "/main","/accessDenied").permitAll()    
    .anyRequest().access("@authorizationChecker.check(request, authentication)") 
    .and()
    .build();
}
```

Spring-security에는 URL로 요청이 올 때마다 해당 URL에 대한 접근 가능 여부를 동적으로 확인할 수 있도록 해주는 기능이 있다.

* AuthorizedUrl.access API 사용
* 이 메소드는 입력된 SpEL을 런타임시에 평가하여 현재 사용자가 해당 Url에 대한 접근 권한이 있는지 동적으로 확인
* authorizationChecker 빈의 check 메소드를 호출하여 그것이 반환하는 값(True/False)에 따라 접근 가능 여부를 설정



AuthorizationCheck.check 메소드는 요청한 USER의 권한이 해당 URL에 접근 가능한 권한을 가지고 있는지 판단하는 역할

```java
@Component
@RequiredArgumentConstructor
public class AuthorizationChecker {
    
    private final SecurityUrlMatcherRepository urlMatcherRepository;
 
    private final UserRepository userRepository;
 
    public boolean check(HttpServletRequest request, Authentication authentication) {
        Object principalObj = authentication.getPrincipal();
 
        if (!(principalObj instanceof User)) {
            return false;
        }
 
        String authority = null;
      
        for (SecurityUrlMatcher matcher : urlMatcherRepository.findAll()) {
            if (new AntPathMatcher().match(matcher.getUrl(), request.getRequestURI())) {
                authority = matcher.getAuthority();
                break;
            }
        }
 
        String userId = ((User) authentication.getPrincipal()).getUserId();
        User loggedUser = userRepository.findByUserId(userId);
 				// SecurityContext.getAuthentication()으로도 해결 가능
        List<String> authorities = loggedUser.getAuthority();
 
        if (authority == null || !authorities.contains(authority)) {
            return false;
        }
        return true;
    }
}

```

**개선 필요 사항**

현재 Anonymous User에 대한 고려가 없다. 그래서 모든 요청에 대해 접근 가능한 URL은 Configuration에 하드코딩 되어 있습니다.

* SecurityUrlMatcher의 document에 sorting 기준 필드를 추가해야 한다. 
  *  ["/home/*", "/home/test"] 이 두개의 URL 각각에 대해 권한이 다르다면, 읽는 순서가 중요하다. 
  * 따라서 구체적인 URL을 보다 먼저 읽도록 해야한다



>  Url에 대한 권한 체크를 위해 매번 데이터베이스의 정보를 확인하는 것은 매우 비효율적이다. 
>
> 이 문제를 해결하기 위해 Spring Cache Abstraction을 이용할 수 있다. 





* https://docs.spring.io/spring-security/site/docs/5.1.0.RELEASE/reference/htmlsingle/#el-access-web-beans
* https://soon-devblog.tistory.com/7
* 인프런 정수원님 시큐리티 강의
* 인프런 백기선님 시큐리티 강의 