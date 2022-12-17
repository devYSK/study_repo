

# Authentication, AuthenticationManager, AuthenticationProvider



일반적인 Spring Security에서 인증 객체를 얻는 과정은 다음과 같다.



AuthenticationManager 를 구현한 ProviderManager가, 내부적으로 `List<AuthenticationProvider>`를 가지고 있다

이 ProviderManager가 List에서 실제로 인증을 처리하는 객체인 AuthenticationProvider을 찾아서 인증을 진행시켜 인증된 Authentication 객체를 반환한다. 



## Authentication (인증)

사용자를 표현하는 인증 토큰 `인터페이스`이며, 인증주체를 표현하는 Principal 그리고 사용자의 권한을 의미하는 GrantedAuthority 목록을 포함한다.

`인증이 완료되거나 혹은 인증되지 사용자를 모두를 포괄적으로 표현`하며, 인증 여부를 확인할 수 있다

*  모든 Authentication 객체가 인증된 사용자는 아니다. 인증이라는 행위를 표현하는 인터페이스. 



Authentication 인터페이스의 구현체들은 xxxx 토큰들로 구성되어 잇다.

- AnonymousAuthenticationToken 클래스는 익명 사용자를 표현하기 위한 Authentication 인터페이스 구현체

- UsernamePasswordAuthenticationToken 클래스는 로그인 아이디/비밀번호 기반 Authentication 인터페이스 구현체

- RememberMeAuthenticationToken 클래스는 remember-me 기반 Authentication 인터페이스 구현체



다음과 같은 정보를 가지고 있다.

* principal : 사용자를 식별하는 객체인 principal을 가지고 있다. 사용자 이름/비밀번호로 인증할때는 보통 UserDetails를 구현한 객체

* credentials :  주체가 올바르다는 것을 증명하는 자격 증명. 주로 비밀번호. 대부분은 유출되지 않도록 사용자를 인증한 다음 비운다.

* authorities : 사용자에게 부여한 권한은 GrantedAuthority 클래스로 추상화 하고 List로 보유하고 있다.

  * 예시로 role, scope, authority 가 있다.

  

인증(Authentication)은 AuthenticationManager가 하며, AuthenticationManager의 입력으로 사용되어 인증에 사용할 사용자의 credential을 제공한다.

- 인자로 받은 Authentication이 유효한 인증인지 확인하고 Authentication 객체를 리턴한다.

- 인증을 확인하는 과정에서 비활성 계정, 잘못된 비번, 잠긴 계정 등의 에러를 던질 수 있다.



사용자의 인증 완료 여부에 따라 Principal 값이 달라진다.

- 로그인 전 Principal — 로그인 아이디 (String)
- 로그인 후 Principal — org.springframework.security.core.userdetails.User 를 상속받거나 구현한 객체 

```java
// Authentication

public interface Authentication extends Principal, Serializable {
  Collection<? extends GrantedAuthority> getAuthorities();
  
  Object getCredentials();
  
  Object getDetails();
  
  boolean isAuthenticated();

  void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException;

}

//Principal
public interface Principal {
   ....
}
```







# AuthenticationManager

`AuthenticationManager`는 Spring Security의 filter가 인증을 수행하는 방법을 정의하는 API를 제공하는 인터페이스이다.

일반적으로 ProviderManager를 이용하여 구현하며 `Authentication#authenticate()` 메서드 1개만 정의하고 있다. 이 메서드는 인자로 받은 Authentication이 유효한지 확인하고 Authentication 객체를 리턴한다.

```java
public interface AuthenticationManager {

	Authentication authenticate(Authentication authentication) throws AuthenticationException;

}
```

반환된 Authentication은 AuthenticationManager를 호출 한 Spring Security의 filter에 의해 SecurityContextHolder에 저장된다.

기본 구현체로 ProviderManager 클래스가 있다.



# ProviderManager

AuthenticationManager을 구현한 이 구현체는 1개 이상의 AuthenticationProvider 인터페이스 구현체로 구성되어 있다.

```java
public class ProviderManager implements AuthenticationManager, MessageSourceAware, InitializingBean {

	private static final Log logger = LogFactory.getLog(ProviderManager.class);

	private AuthenticationEventPublisher eventPublisher = new NullEventPublisher();

	private List<AuthenticationProvider> providers = Collections.emptyList(); // Provider List

	protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

	private AuthenticationManager parent; // 부모 Manager

	private boolean eraseCredentialsAfterAuthentication = true;

  ...
    
}
```

ProviderManager는 특정 인증 유형을 확인할 수 있는 `AuthenticationProvider`들을 List로 가지고 있다.
List에 포함된 각각의 AuthenticationProvider는 인증 성공 여부를 반환 하는 역할을 한다.

AuthenticationProvider 인터페이스 구현체가 실제 사용자 인증을 처리하게 됨

- 1개 이상의 AuthenticationProvider 인터페이스 구현체 중 어떤 AuthenticationProvider가 실제 인증을 처리할지 결정할 수 있음
  - 주어진 Authentication 객체에 대해 supports(Class<?> authentication) 메소드가 true 를 반환하는 AuthenticationProvider 객체가 인증을 처리함
  - 예를 들어 UsernamePasswordAuthenticationToken 타입의 인증 요청은 DaoAuthenticationProvider가 처리함

<img src="https://blog.kakaocdn.net/dn/NPsUo/btrTQ1L6dNC/DPFqpAuXfBxh2rvkkEzKsK/img.png" width = 700 height=350 >



스프링 시큐리티는 AuthenticationProvider interface를 통해 여러 유형의 인증을 지원하고 단일 인증 관리자만(ProviderManager) 노출하면서 매우 구체적인 유형의 인증을 수행할 수 있다.



```java
private List<AuthenticationProvider> providers = Collections.emptyList();
// ... 생략 ...
@Override
public Authentication authenticate(Authentication authentication) throws AuthenticationException {
	Authentication result = null;
	Authentication parentResult = null;
	// ... 생략 ...
  for (AuthenticationProvider provider : getProviders()) { // 1
		if (!provider.supports(toTest)) { // 2
			continue;
		}
		if (logger.isTraceEnabled()) {
			logger.trace(LogMessage.format("Authenticating request with %s (%d/%d)",
					provider.getClass().getSimpleName(), ++currentPosition, size));
		}
		try {
			result = provider.authenticate(authentication); // 3
			if (result != null) {
				copyDetails(authentication, result);
				break;
			}
		}
		catch (AccountStatusException | InternalAuthenticationServiceException ex) {
			prepareException(ex, authentication);
			// SEC-546: Avoid polling additional providers if auth failure is due to
			// invalid account status
			throw ex;
		}
		catch (AuthenticationException ex) {
			lastException = ex;
		}
	}
  if (result == null && this.parent != null) { // 4 부모 AuthentcationManager에게 요청 
			// Allow the parent to try.
			try {
				parentResult = this.parent.authenticate(authentication);
				result = parentResult;
			}
			catch (ProviderNotFoundException ex) {
				// ignore as we will throw below if no other exception occurred prior to
				// calling parent and the parent
				// may throw ProviderNotFound even though a provider in the child already
				// handled the request
			}
			catch (AuthenticationException ex) {
				parentException = ex;
				lastException = ex;
			}
		}
		if (result != null) {
			if (this.eraseCredentialsAfterAuthentication && (result instanceof CredentialsContainer)) {
				// Authentication is complete. Remove credentials and other secret data
				// from authentication
				((CredentialsContainer) result).eraseCredentials();
			}
			// If the parent AuthenticationManager was attempted and successful then it
			// will publish an AuthenticationSuccessEvent
			// This check prevents a duplicate AuthenticationSuccessEvent if the parent
			// AuthenticationManager already published it
			if (parentResult == null) {
				this.eventPublisher.publishAuthenticationSuccess(result);
			}

			return result;
		}
	// ... 생략 ...
}
// ... 생략 ...
```

1. 자기 Providers 만큼 반복문을 돌려서 진행한다
2. 현재 인덱스에 해당하는 provider가 인증을 지원하지 않으면 continue
3. 현재 provider가 인증을 진행해서 성공하면 result에 authentication을 담는다.
   * 만약 인증이 성공했따면 (result != null) 구문으로 가서 eraseCredentials를 수행하고 인증된 Authentication 객체를 반환한다
4. 만약 자신들이 가지고 있는 provider에서 인증을 할 수 없다면, parent AuthenticationManager에게 같은 인증요청을 수행한다.





> ProviderManager 내부에 구현한 authenticate 메소드 내에서, List 로 가지고 있는 AuthenticationProvider 만큼 반복문을 돌려 
>
> 실제적으로는 AuthenticationProvider 인터페이스를 구현한 Provider 구현체로 인증을 진행하고 Authentication 객체를 반환한다. 

<img src="https://blog.kakaocdn.net/dn/zJfTo/btrTVT6LfOW/vEEpTGSxtJdTFgRlzDFwnk/img.png" width= 600 height = 300>

또한 자신이 가지고 있는 Provider들로 인증 확인을 수행할 수 있는 경우, 

ProviderManager가 내부적으로 가지고 있는 AuthenticationManager타입의 parent로 인증을 진행한다. 



AuthenticationProvider 가 여러 개가 있다는 것은 여러 방식으로 인증을 수행할 수 있다는 것이다

>  root path를 구분지어서 `/server` 로 들어오는 요청은 appkey라는 파라미터를 통해 인증을 진행,
> `/api`로 들어오는 요청은 JWT토큰을 통해 사용자 인증을 진행



# Authenticationprovider

실질적으로 인증 요청을 수행하는 인터페이스이다. 

**각각의 AuthenticationProvider는 특정 유형의 인증을 수행한다.**

인증에 성공하면 완전히 채워진 *인증* 개체를 반환한다. *인증에 실패하면 AuthenticationException* 유형의 예외가 발생한다 .

인증요청을 진행하는 authenticate 메소드와 인증을 지원하는지 여부를 알 수 있는 supports 메소드를 가지고 있다.

```java
public interface AuthenticationProvider {

	Authentication authenticate(Authentication authentication) throws AuthenticationException;

	boolean supports(Class<?> authentication);

}
```

특정 유형의 인증

* DaoAuthenticationProvider, LdapAuthenticationProvider : form 기반 로그인이나, HTTP Bagic authentication 인증을 구현한 구현체
* RememberMeAuthenticationProvider : Remember-Me에 사용되는 구현체 

* AnonymousAuthenticationProvider: Anonymous 유저가 유효한지 확인하고 인증 개체 반납



## DaoAuthenticationProvider



유저가 입력한 username, password를 DB에서 가져온 유저 정보와 비교하여 인증을 처리하는 구현체

AbstractUserDetailsAuthenticationProvider를 상속받고있다. 

- password를 비교하기 위해서 PasswordEncoder를 설정해주어야 한다
  - bcrypt나 등 
- DB에서 유저 정보를 가져오기 위해서 UserDetailsService를 설정해주어야 한다



### UserDetailsService

 사용자가 입력한 username을 기반으로 저장된 유저정보를 가져와서 UserDetails 객체로 변환하여 돌려주는 메소드

* 보통 email을 username으로 사용한다. 

사용자를 찾을 수 없거나 사용자에게 GrantedAuthority가 없는 경우 UsernameNotFoundException을 던진다

```java
public interface UserDetailsService {

	UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

}

```

- UserDetails loadUserByUsername(String username) throws UsernameNotFountException; 메소드 하나를 가지고 있는 인터페이스

Spring Security에서 관련 구현체로 In-Memory Authentication, JdbcDaoImpl이 있으며

우리가 직접 구현해서 사용할수도 있다. 



### Authenticationprovider 커스텀

```java
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication auth) 
      throws AuthenticationException {
        String username = auth.getName();
        String password = auth.getCredentials()
            .toString();

        if ("externaluser".equals(username) && "pass".equals(password)) {
            return new UsernamePasswordAuthenticationToken
              (username, password, Collections.emptyList());
        } else {
            throw new 
              BadCredentialsException("External system authentication failed");
        }
    }

    @Override
    public boolean supports(Class<?> auth) {
        return auth.equals(UsernamePasswordAuthenticationToken.class);
    }
}
```

* 이제 *CustomAuthenticationProvider* 와 메모리 내 인증 공급자를 Spring Security Config에 추가

```java
@EnableWebSecurity
public class MultipleAuthProvidersSecurityConfig {

    @Autowired
    CustomAuthenticationProvider customAuthProvider;

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = 
          http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(customAuthProvider);
        authenticationManagerBuilder.inMemoryAuthentication()
            .withUser("memuser")
            .password(passwordEncoder().encode("pass"))
            .roles("USER");
        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authManager) 
      throws Exception {
        http.httpBasic()
            .and()
            .authorizeRequests()
            .antMatchers("/api/**")
            .authenticated()
            .and()
            .authenticationManager(authManager);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

> [MultiProvider 적용](https://velog.io/@freddiey/Spring-security-%EB%8B%A4%EC%A4%91-provider-%EC%A0%81%EC%9A%A9)

* https://velog.io/@freddiey/Spring-security-%EB%8B%A4%EC%A4%91-provider-%EC%A0%81%EC%9A%A9

* 어려운 부분은 없이 `WebSecurityConfigurer`를 두번 정의해주면 된다.



## eraseCredentialsAfterAuthentication

인증이 완료되고 나면 Authentication 객체 에서 보안을 위해 자격 증명 및 기타 비밀 데이터 제거를 해야하는데 이때 사용하는 것이

CredentialsContainer 인터페이스의 메소드인 eraseCredentials()이다.



요청 처리가 완료된 thread의 인증 정보는 FilterChainProxy가 clear 한다

```java
// in FilterchainProxy.class
@Override
public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// 생략
		try {
			request.setAttribute(FILTER_APPLIED, Boolean.TRUE);
			doFilterInternal(request, response, chain);
		}
		catch (Exception ex) {
      // 생략
		}
		finally {
			SecurityContextHolder.clearContext(); // 클리어
			request.removeAttribute(FILTER_APPLIED);
		}
}
```

ProviderManager는 Authentication에서 반환된 인증에 성공한 객체에서 인증 정보를 clear 하고 이로 인해 암호와 같은 인증 정보가 HttpSession에서 필요한 시간보다 오래 보존되지 않는 장점이 있다.

만약 캐시 사용 등의 이유로 clear 하는 기능을 비활성화하려면 ProviderManager의 `eraseCredentialsAfterAuthentication` 필드를 false로 설정하면 된다.

* ProviderManager가 필드를 가지고 있다

```java
public class ProviderManager implements AuthenticationManager, MessageSourceAware, InitializingBean {
  
  ...
    
  private boolean eraseCredentialsAfterAuthentication = true;
  ...
    
}
  
```





실제로 Authentication의 구현체인 UsernamePasswordAuthenticationToken은 eraseCredentials을 구현하여 credentials를 지우울 수 있는 기능을 제공한다. 

```java
public class UsernamePasswordAuthenticationToken extends AbstractAuthenticationToken {

	private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

	private final Object principal;

	private Object credentials;

	@Override
	public void eraseCredentials() {
		super.eraseCredentials();
		this.credentials = null;
	}

}

```

