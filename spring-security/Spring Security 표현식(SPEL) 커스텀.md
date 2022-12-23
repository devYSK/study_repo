

# Web Security Expressions - URL 별로 표현식



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



## 커스텀

사용할 수 있는 표현식을 늘리고 싶다면, 어떤 객체든지 스프링 빈으로 정의하면 쉽게 참조할 수 있다. 예를 들어 `webSecurity`란 이름의 빈이 있고, 이 빈의 메소드 시그니처는 아래와 같다고 가정해보자:

```java
public class WebSecurity {
        public boolean check(Authentication authentication, HttpServletRequest request) {
                ...
        }
}
```

이 메소드는 다음과 같이 참조할 수 있다:

또는 자바 설정을 사용한다면

```java
http
    .authorizeRequests(authorize -> authorize
        .antMatchers("/user/**").access("@webSecurity.check(authentication,request)")
        ...
    )
```

#### Path Variables in Web Security Expressions

URL에 있는 path variable을 참조해야 할 때도 있다. 예를 들어 `/user/{userId}` 형식의 URL path에서 id를 가져와 사용자를 검색하는 RESTful 어플리케이션을 생각해 보자.

path variable도 간단하게 패턴에 지정해서 참조할 수 있다. 예를 들어 `webSecurity`란 이름의 빈이 있고, 이 빈의 메소드 시그니처는 아래와 같다고 가정해보자:

```
public class WebSecurity {
        public boolean checkUserId(Authentication authentication, int id) {
                ...
        }
}
```



ustom class를 Component로 만들던지 method만 bean으로 만들던지 선택해서 직접 검증을 하면 된다

**Component로 만든 경우 SpEL 상에서 @클래스.메서드() 형태로 사용하면 된다**



# Method Security Expressions

메소드 시큐리티는 단순한 허가 또는 거절보다 조금 더 복잡한 규칙을 사용한다

#### @Pre and @Post Annotations



Security Config 어노테이션 설정

```java
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@Configuration
public class SecurityConfig {
    // security beans
}
```

