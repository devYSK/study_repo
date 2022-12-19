# AccessDecisionVoter 커스텀

요구사항

* "/admin" URL 접근에 대한 접근 권한 검사
  * Admin 사용자의 로그인 아이디 끝 숫자가 홀수 인 경우 접근 허용
  * URL이 "/admin" 이 아닌 경우 접근을 승인함
* 기본 expressionHandler를 사용
* accessDecisionManager 인터페이스 구현체 중 UnanimousBased를 사용



# SecurityConfig 구현 - HttpSecurity,  AccessDecisionManager 설정

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    public AccessDecisionManager accessDecisionManager() { 
        List<AccessDecisionVoter<?>> decisionVoters = new ArrayList<>();
        decisionVoters.add(new WebExpressionVoter()); // 먼저 넣은 Voter가 실행된다. ROLE_ADMIN 권한 검사가 먼저 이루어짐
        decisionVoters.add(new OddAdminVoter(new AntPathRequestMatcher("/admin")));
        return new UnanimousBased(decisionVoters);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
            .authorizeRequests()

            .antMatchers("/me").hasAnyRole("USER", "ADMIN")
            .antMatchers("/admin").access("hasRole('ADMIN') and isFullyAuthenticated()") // 어드민 권한은 가진 사용자이고 리멤버미를 통하여 인증된 사용자가 아닌사용자만
            .anyRequest().permitAll()
            .accessDecisionManager(accessDecisionManager()) // 정의된 빈을 기본 AccessDecisionManager 로 사용하도록 Spring Security를 구성
            .and()

            .formLogin()
            .defaultSuccessUrl("/")
            .permitAll()
            .and()

						... 생략 
            .build();
    }

}
```

* 커스텀 AccessDecisionManager 구현
  * AccessDecisionManager 구현체로 UnanimousBased 구현체를 사용
  * 순차적으로 AccessDecisionVoter 추가
  * WebExpressionVoter
    * OddAdminVoter — 생성자 인자로 해당 voter가 처리해야 하는 URL 패턴을 넘김
* HttpSecurity 설정



다음처럼 사용해도 된다

```java
@Bean
public AccessDecisionManager accessDecisionManager() {
  List < AccessDecisionVoter << ? extends Object >> decisionVoters = Arrays.asList(
    new AuthenticatedVoter(),
    new RoleVoter(), 
    new WebExpressionVoter(),
    customAccessDecisionVoter
        ); 
  return new UnanimousBased(decisionVoters);
    
}
```





# OddAdminVoter 클래스 구현

```java
public class OddAdminVoter implements AccessDecisionVoter<FilterInvocation> {

    static final Pattern PATTERN = Pattern.compile("[0-9]+$");

    private final RequestMatcher requiresAuthorizationRequestMatcher;

    public OddAdminVoter(RequestMatcher requiresAuthorizationRequestMatcher) {
        this.requiresAuthorizationRequestMatcher = requiresAuthorizationRequestMatcher;
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }

    @Override
    public int vote(Authentication authentication, FilterInvocation object,
        Collection<ConfigAttribute> attributes) {
        HttpServletRequest request = object.getRequest(); //
        if (!requiresAuthorization(request)) { // 요청 URL이 어드민인가.   1
            return ACCESS_GRANTED;
        }
        User user = (User) authentication.getPrincipal();
        String name = user.getUsername();
        Matcher matcher = PATTERN.matcher(name);
        if (matcher.find()) {
            int number = toInt(matcher.group(), 0);
            if (number % 2 == 1) {
                return ACCESS_GRANTED;  // 2
            }
        }
        return ACCESS_DENIED;   // 3
    }

    private boolean requiresAuthorization(HttpServletRequest request) {
        return requiresAuthorizationRequestMatcher.matches(request);
    }

}
```

* vote() 메소드가 핵심이다.
  * 1 요청 URL 검사
  * 2 권한 조건에 맞으면 ACCESS_GRANTED 리턴
  * 3 권한 조건에 맞지 않으면 ACCESS_DENIED 리턴 

* supports() 메소드는 Voter 클래스가 특정 구성을 지원하는지 여부를 반환



## 참조

* https://www.baeldung.com/spring-security-custom-voter

* https://www.javadevjournal.com/spring-security/custom-access-decision-voter/