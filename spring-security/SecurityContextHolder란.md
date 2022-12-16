# SecurityContext, SecurityContextHolder, SecurityContextHolderStrategy, Authentcation



먼저 인증이 진행되어 인증객체가 생성되는 간단한 지식을 정리하자.

스프링 시큐리티는 servlet filter를 기반으로 인증 기능을 지원한다.
spring boot의 기본 설정을 사용한다면 `springSecurityFilterChain` filter를 자동으로 등록해 주고 이 filter를 이용하여 스프링 시큐리티의 인증 과정의 전체적인 동작을 주관한다.

FilterChain 이름에서 보이다시피, 필터들은 다양하게 순서대로 엮여서 Chain을 구성하며, 이 필터들의 체인을 요청이 거쳐가면서 인증과정이 진행된다. 

인증 과정이 끝나고 인증이 된다면, 이 요청의 사용자가 누구인지 알아야 하고  이 사용자가 올바른 인증된 사용자인지, 올바른 권한을 가지고 있는지 판단하기 위해서 인증 객체를 사용하게 된다. 그래서 올바른 권한을 가진 인증된 사용자가 요청을 한 것이라면 요청을 수행하고, 그렇지 않다면 인증예외나 인가 예외를 발생 시켜 요청을 거부한다. 





## SecurityContext

- SecurityContext 는 접근 주체와 인증에 대한 정보를 담고 있는 Context
- Authentication 객체가 저장되는 보관소로 필요 시 언제든지 Authentication 객체를 꺼내어 쓸 수 있도록 제공되는 인터페이스
- 구현체는 SecurityContextImpl 클래스이다.
- ThreadLocal에 저장되어 애플리케이션 아무 곳에서나 참조가 가능하도록 설계하였다.
- 인증이 완료되면 HttpSession에 저장되어 어플리케이션 전반에 걸쳐 전역적인 참조가 가능하다.
- SecurityContext 자체는 어떤 특별한 기능을 제공하지 않고 Authentication 객체를 Wrapping 하고 있음



<img src="https://blog.kakaocdn.net/dn/qmmKj/btrTTpdsYzd/4cQCQMTQemKiiSbOLKp7xk/img.png" width= 900 height = 350>

https://docs.spring.io/spring-security/site/docs/current/reference/html5/#servlet-authentication-securitycontextholder



SecurityContextHolder 가 SecurityContextHolderStrategy 를 통해 Authentication 객체(정보가) 담겨있는 SecurityContext 를 반환한다.



SecurityContext는 SecurityContextPersistenceFilter에 의해 생성, 저장, 조회된다.

1. 익명 사용자

- 새로운 SecurityContext 객체를 생성하여 SecurityContextHolder에 저장한다.
- AnonmousAuthenticationFilter에서 AnonymousAuthenticationToken 객체를 SecurityContext에 저장한다.



2. 인증 시

- 새로운 SecurityContext 객체를 생성하여 SecurityContextHolder에 저장
- UsernamePasswordAuthenticationFilter에서 인증 성공 후 UsernamePasswordAuthenticationToken 객체를 SecurityContext에 저장한다.
- 인증이 최종 완료되면 Session에 SecurityContext를 저장한다.



3. 인증 후

- Session에서 SecurityContext를 꺼내에 SecurityContextHolder에 저장한다.
- SecurityContext 안에 Authentication 객체가 존재하면 계속 인증을 유지한다.



4. 최종 응답 시 공통

- SecurityContextHolder.clearContext()

* FilterChainProxy 구현을 보면 finally 블록에서 SecurityContextHolder.clearContext() 메소드를 호출



## SecurityContextHolder

`SecurityContextHolder`는 시큐리티가 인증한 내용들을 가지고 있으며, `SecurityContext`를 포함하고 있고 SecurityContext를 현재 스레드와 연결해 주는 역할을 한다.

* 이름에서 보이다 시피 SecurityContext를 holding하는 역할을 한다.

* 이 클래스의 목적은 주어진 JVM에 대해 사용해야 하는 전략을 지정하는 편리한 방법을 제공하는 것이다.

시큐리티를 사용하는 이유 중 하나는 인증된 사용자 정보를 확인하는 것 이고, 인증된 사용자의 정보는  SecurityContextHolder를 통해 확인이 가능하다.

```java
SecurityContext context = SecurityContextHolder.getContext(); // 현재 Context를 가져옴
Authentication authentication = context.getAuthentication(); // 현재 인증 객체인 Authentication을 가져옴
String username = authentication.getName();	// 인증객체에 들어잇는 유저이름. 이메일이 될 수도, 이름이나 닉네임이 될 수도 있다.
Object principal = authentication.getPrincipal(); // 인증정보를 담는 객체
Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities(); // 인증 객체의 권한
Object credentials = authentication.getCredentials(); // 신용, 신임여부 
boolean authenticated = authentication.isAuthenticated(); // 인증 되었는지 여부 
```

SecurityContextHolder.clearContext() 메서드를 사용하여 전역에서 SecurityContext를 초기화할 수도 있다

* SecurityContextHolder.clearContext() : SecurityContext 기존 정보 초기화

* FilterChainProxy 구현을 보면 finally 블록에서 SecurityContextHolder.clearContext() 메소드를 호출

- 모든 처리가 완료 된후 finally로 사용 . doFilterInternal이 요청 시작지점. 
  - 이것은 HTTP 요청 처리가 완료되고 Thread가 ThreadPool에 반환되기전 ThreadLocal 변수 값을 제거하기 위함



세션을 사용한다면, 어차피 SecurityContext가 세션에 저장되기 때문에 세션->SecurityContext-> 인증객체를 찾아도 괜찮다.

```java
Authentication authentication1 = SecurityContextHolder.getContext().getAuthentication()
  
SecurityContext context = (SecurityContext)session.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);

Authenticatino authentication2 = context.getAuthentication();
```





### 전략?

SecurityContextHolder에서 말하는 전략은, SecurityContext를 어떻게 저장하고 공유할 것인가에 대한 전략이다.

웹 요청은 보통 1요청이 들어오고, 이 1요청 내에서 요청된 사용자가 누구인지, 인증되었는지, 인가되었는지  알아야 하고 어디서든 꺼내서 쓸 수 있게 하려고 SecurityContext를 사용하는 것인데 이를 어떻게 저장하고 공유할 것인가를 개발자가 지정할 수 있다.

이 전략은 SecurityContextHolderStrategy로 위임해서 사용하는데, SecurityContextHolderStrategy는 인터페이스이며, 전략패턴으로 구현되어 있다. 

이 전략은 다음 3가지 Mode로 설정할 수 있다. default는 **MODE_THREADLOCAL** 이며 ThreadLocal을 사용하고 Thread-per-request 모델을 사용한다



**1. MODE_THREADLOCAL** - 스레드당 SecurityContext 객체를 할당, 기본값

ThreadLocalSecurityContextHolderStrategy 클래스를 구현체로 사용하며, ThreadLocal 을 사용하여 SecurityContext 를 공유합니다. ThreadLocal 은 같은 쓰레드 내에서 공유할 수 있는 자원입니다.

아무 설정을 하지 않으면 기본적으로 설정되는 모드 입니다.

 

**2. MODE_INHERITABLETHREADLOCAL** - 메인 스레드와 자식 스레드에 관하여 동일한 SecurityContext 를 유지 

InheritableThreadLocalSecurityContextHolderStrategy 클래스를 구현체로 사용하며, `InheritableThreadLoca`l 을 사용하여 SecurityContext 를 공유한다. 이는 자식 쓰레드까지 공유할 수 있는 자원입니다. "MODE_INHERITABLETHREADLOCAL" 이름을 넘겨서 설정할 수 있다.

* spring Security는 요청당 여러개의 쓰레드를 사용할 수 있는 Webflux도 지원하기 때문에 사용하는 전략 같다.  



**3. MODE_GLOBAL**

GlobalSecurityContextHolderStrategy 클래스를 구현체로 사용하며, static 선언하여 SecurityContext 를 저장합니다. 따라서, 해당 JVM 내의 인스턴스들은 모두 공유할 수 있다.

* 한 서버의 JVM 내에서 여러 인스턴스 들이 동작할 수 있는데, 이 인스턴스들이 같은 SecurityContext를 공유할 수 있게 하기 위한 설정 같다. 



#### **저장, 공유전략 설정**방법

1. 시스템 변수 설정

```
-Dspring.security.strategy=MODE_INHERITABLETHREADLOCAL
```

2. SecurityContextHolder 의 static method 로 설정

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig{
  @Override
  protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      // 시큐리티 홀더의 공유 전략 설정 - 쓰레드가 생성하는 하위 쓰레드까지 자원공유
      SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
  }
 }
```

2번의 방법으로 공유전략을 중간에 바꿀 수도 있지만, 해당 메소드의 주석에도 설명되어 있듯이 중간에 전략을 바꾸는 것은 의도한 대로 동작하지 않을 가능성이 크기 때문에 지양하는게 좋다.



3. 커스텀 전략 추가 

```java
public final class CustomSecurityContextHolderStrategy implements SecurityContextHolderStrategy {

    private static final ThreadLocal<SecurityContext> contextHolder = new InheritableThreadLocal<>();

    public void clearContext() {
        contextHolder.remove();
    }

    public SecurityContext getContext() {
        System.out.println("Custom strategy start");
        SecurityContext ctx = contextHolder.get();

        if (ctx == null) {
            ctx = createEmptyContext();
            contextHolder.set(ctx);
        }
        System.out.println("Custom strategy end : " + ctx.getClass());
        return ctx;
    }

    public void setContext(SecurityContext context) {
        Assert.notNull(context, "Only non-null SecurityContext instances are permitted");
        contextHolder.set(context);
    }

    public SecurityContext createEmptyContext() {
        return new SecurityContextImpl();
    }
}
```

* 구현한 내용은 getContext 가 호출되면 로그를 찍도록 함 



## SecurityContextHolderStrategy 

SecurityContext를 어떻게 저장하고 공유할 것인가에 대한 전략은 SecurityContextHolderStrategy로 위임해서 사용하는데, SecurityContextHolderStrategy는 인터페이스이며, `전략패턴`으로 구현되어 있다. 

* 기본 전략으로 MODE_THREADLOCAL를 사용한다- 스레드당 SecurityContext 객체를 할당.
  * Thread-Per-Request Model 방식 사용 

```java
public class SecurityContextHolder {
	// ... 생략 ...
	private static SecurityContextHolderStrategy strategy;

	public static void clearContext() {
		strategy.clearContext();
	}

	public static SecurityContext getContext() {
		return strategy.getContext();
	}

	public static void setContext(SecurityContext context) {
		strategy.setContext(context);
	}
  // ... 생략 ...
}

/**
 * SecurityContextHolderStrategy 전략패턴 인터페이스
 */
public interface SecurityContextHolderStrategy {

	void clearContext();

	SecurityContext getContext();

	void setContext(SecurityContext context);

	SecurityContext createEmptyContext();

}

/**
 * 기본 전략인 SecurityContextHolderStrategy 인터페이스 ThreadLocal 구현체
 */
final class ThreadLocalSecurityContextHolderStrategy implements SecurityContextHolderStrategy {

	private static final ThreadLocal<SecurityContext> contextHolder = new ThreadLocal<>();

	@Override
	public void clearContext() {
		contextHolder.remove();
	}

	@Override
	public SecurityContext getContext() {
		SecurityContext ctx = contextHolder.get();
		if (ctx == null) {
			ctx = createEmptyContext();
			contextHolder.set(ctx);
		}
		return ctx;
	}

	@Override
	public void setContext(SecurityContext context) {
		Assert.notNull(context, "Only non-null SecurityContext instances are permitted");
		contextHolder.set(context);
	}

	@Override
	public SecurityContext createEmptyContext() {
		return new SecurityContextImpl();
	}

}
```





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





### 참조

* 인프런 스프링 시큐리티 정수원님 강의
* 인프런 스프링 시큐리티 백기선님 강의
* https://godekdls.github.io/Spring%20Security/authentication/#103-authentication
* https://00hongjun.github.io/spring-security/securitycontextholder/