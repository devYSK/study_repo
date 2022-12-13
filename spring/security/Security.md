

# websecurityconfigureradapter 대체방법



Spring Security 5.7.0-M2 부터 websecurityconfigureradapter는 컴포넌트 기반의 보안 설정을 권장한다는 이유로 Deprecated 처리되었다. 



그래서 다른 방식으로 시큐리티 설정을 권장한다.



* https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter



## 어떻게 바뀌었나 

기존에 다음과 같이 사용했던 방식은 

```java
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
 
    @Override
    protected void configure(HttpSecurity http) throws Exception {
         
        // configure HTTP security...
         
    }
 
    @Override
    public void configure(WebSecurity web) throws Exception {
         
        // configure Web security...
         
    }      
}
```

많은 설정들을 `WebSecurityConfigurerAdapter`를 `extends` 해서 `configure` 메소드를 오버라이딩 해서 구현했었다.



`WebSecurityConfigurerAdapter` [공식문서](https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/config/annotation/web/configuration/WebSecurityConfigurerAdapter.html)에서 다음과 같이 설명한다.

```
Deprecated.
Use a SecurityFilterChain Bean to configure HttpSecurity or a WebSecurityCustomizer Bean to configure WebSecurity
```

바뀐 방식에서는 상속받아 오버라이딩하지 않고 모두 Bean으로 등록을 한다.  `SecurityFilterChain`를 `Bean`으로 등록해서 사용하여야 한다.



## HttpSecurity 설정

HttpSecurity는 시큐리티의 핵심 클래스이며 특정 http 요청에 대해 웹 기반 보안을 구성할 수 있다.

스프링시큐리티의 각종 설정은 HttpSecurity로 대부분 하게 된다.

- 리소스(URL) 접근 권한 설정
- 인증 전체 흐름에 필요한 Login, Logout 페이지 인증완료 후 페이지 인증 실패 시 이동페이지 등등 설정
- 인증 로직을 커스텀하기위한 커스텀 필터 설정
- 기타 csrf, 강제 https 호출 등등 거의 모든 스프링시큐리티의 설정



다음은, 이전 설정 방법과 바뀐 설정 방법에 대한 예이다



```java
Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((authz) -> authz
                .anyRequest().authenticated()
            )
            .httpBasic(withDefaults());
    }

}
```

앞으로 권장되는 방법은 다음처럼 `SecurityFilterChain` 빈을 등록하는 것이다

```java
@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((authz) -> authz
                .anyRequest().authenticated()
            )
            .httpBasic(withDefaults());
        return http.build();
    }

}
```



기존의 방식과 다른 점은 반환 값이 있고 빈으로 등록한다는 점이다.   
SecurityFilterChain을 반환하고 빈으로 등록함으로써 컴포넌트 기반의 보안 설정이 가능해진다.



### WebSecurity 설정

WebSecurity는 FilterChainProxy를 생성하는 필터이다. 다양한 Filter 설정을 적용할 수 있습니다.

```
web.ignoring().antMatchers("/css/**", "/js/**", "/img/**", "/lib/**");  
```

위 설정을 통해 Spring Security에서 해당 요청은 인증 대상에서 제외시킵니다.



> 스프링시큐리티의 각종 설정은 HttpSecurity로 한다 WebSecurity 스프링시큐리티 앞단의 설정들을 하는 객체이므로 HttpSecurity 설정한 스프링시큐리티 설정이 오버라이드 되는 설정이 있는 경우도 있다.

`기억해야될 부분은 스프링시큐리티의 설정은 HttpSecurity에서 하는 것 이다.`



다음은 이전 설정과 바뀐 설정에 대한 예이다

```java
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/ignore1", "/ignore2");
    }

}
```

앞으로 권장되는 방법은 다음처럼 `WebSecurityCustomizer` 빈을 등록하는 것이다

```java
@Configuration
public class SecurityConfiguration {

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/ignore1", "/ignore2");
    }

}
```

주의할점 : WebSecurity를 통하여 request(요청)을 ignore(무시)하도록 WebSecurity를 설정하는 경우 보다는   HttpSecuritytㅓㄹ정의 authorizeHttpRequests()를 통해 permitAll을 사용하는 것이 좋다. 

* 자세한것은 [javadoc](https://docs.spring.io/spring-security/site/docs/5.7.0-M2/api/org/springframework/security/config/annotation/web/configuration/WebSecurityConfigurerAdapter.html#configure%25org.springframework.security.config.annotation.web.builders.WebSecurity%29) 을 참고

> WebSecurityCustomizer는 WebSecurity를 사용자 정의하는 데 사용할 수 있는 콜백 인터페이스



### JDBC Authentication



```java
Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .build();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        UserDetails user = User.withDefaultPasswordEncoder()
            .username("user")
            .password("password")
            .roles("USER")
            .build();
        auth.jdbcAuthentication()
            .withDefaultSchema()
            .dataSource(dataSource())
            .withUser(user);
    }
}
```

권장되는 방법은 JdbcUserDetailsManager 빈을 등록하는 것.

```java
@Configuration
public class SecurityConfiguration {
    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .addScript(JdbcDaoImpl.DEFAULT_USER_SCHEMA_DDL_LOCATION)
            .build();
    }

    @Bean
    public UserDetailsManager users(DataSource dataSource) {
        UserDetails user = User.withDefaultPasswordEncoder()
            .username("user")
            .password("password")
            .roles("USER")
            .build();
        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
        users.createUser(user);
        return users;
    }
}
```



### In-Memory Authentication

메모리의 기반의 인증

```java
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        UserDetails user = User.withDefaultPasswordEncoder()
            .username("user")
            .password("password")
            .roles("USER")
            .build();
        auth.inMemoryAuthentication()
            .withUser(user);
    }
}
```

권장되는 방법은 InMemoryUserDetailsManager 빈을 등록하는 것

```java
@Configuration
public class SecurityConfiguration {
    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
            .username("user")
            .password("password")
            .roles("USER")
            .build();
        return new InMemoryUserDetailsManager(user);
    }
}
```

주의사항 : 이 예제에서는 가독성을 위해 User.withDefaultPasswordEncoder() 메서드를 사용한다. 프로덕션용이 아니므로 암호를 외부에서 해싱하는 것이 좋다. 이를 수행하는 한 가지 방법은 [문서]( [reference documentation](https://docs.spring.io/spring-security/reference/features/authentication/password-storage.html#authentication-password-storage-boot-cli).)에 설명된 대로 Spring Boot CLI를 사용하는 것.



### Global AuthenticationManager

Spring Security 5.6에서는 특정 SecurityFilterChain에 대한 기본 AuthenticationManager를 재정의하는 HttpSecurity의 [authenticationManager]([HttpSecurity#authenticationManager](https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/config/annotation/web/builders/HttpSecurity.html#authenticationManager%org.springframework.security.authentication.AuthenticationManager))) 메서드를 도입했다.

*  [introduced](https://github.com/spring-projects/spring-security/issues/10040) 



다음은 사용자 정의 AuthenticationManager를 기본값으로 설정하는 예제 구성입다.



### Local AuthenticationManager

```java
Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((authz) -> authz
                .anyRequest().authenticated()
            )
            .httpBasic(withDefaults())
            .authenticationManager(new CustomAuthenticationManager());
        return http.build();
    }

}
```



### Accessing the local AuthenticationManager (AuthenticationManager 접근)

로컬 AuthenticationManager는 사용자 지정 DSL에서 액세스할 수 있다. 이 Spring Security가 HttpSecurity.authorizeRequests()와 같은 메소드를 내부적으로 구현하는 방법이다.

```java
public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
    @Override
    public void configure(HttpSecurity http) throws Exception {
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        http.addFilter(new CustomFilter(authenticationManager));
    }

    public static MyCustomDsl customDsl() {
        return new MyCustomDsl();
    }
}
```

다음 SecurityFilterChain을 빌드할 때 사용자 지정 DSL을 적용할 수 있다

```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    // ...
    http.apply(customDsl());
    return http.build();
}
```





## 변경 예제 : 변경전 설정과 변경 후 설정

코드의 자세한 내용을 보기보다는 전체적인 방식의 차이에 주목하자 (메소드의 리턴)

- 변경전

```java
package com.prgrms.devcource.configures;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

import com.prgrms.devcource.configures.jwt.Jwt;
import com.prgrms.devcource.configures.jwt.JwtAuthenticationFilter;
import com.prgrms.devcource.oauth2.OAuth2AuthenticationSuccessHandler;
import com.prgrms.devcource.user.UserService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfigure extends WebSecurityConfigurerAdapter {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final JwtConfigure jwtConfigure;

	private final UserService userService;

	public WebSecurityConfigure(JwtConfigure jwtConfigure, UserService userService) {
		this.jwtConfigure = jwtConfigure;
		this.userService = userService;
	}

	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers("/assets/**", "/h2-console/**","/api/hello2");
	}

	@Bean
	public AccessDeniedHandler accessDeniedHandler() {
		return (request, response, e) -> {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Object principal = authentication != null ? authentication.getPrincipal() : null;
			log.warn("{} is denied", principal, e);
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			response.setContentType("text/plain;charset=UTF-8");
			response.getWriter().write("ACCESS DENIED");
			response.getWriter().flush();
			response.getWriter().close();
		};
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public Jwt jwt() {
		return new Jwt(
			jwtConfigure.getIssuer(),
			jwtConfigure.getClientSecret(),
			jwtConfigure.getExpirySeconds()
		);
	}

	@Bean
	public OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler () {
		Jwt jwt = getApplicationContext().getBean(Jwt.class);
		return new OAuth2AuthenticationSuccessHandler(jwt, userService);
	}
	

	public JwtAuthenticationFilter jwtAuthenticationFilter() {
		Jwt jwt = getApplicationContext().getBean(Jwt.class);
		return new JwtAuthenticationFilter(jwtConfigure.getHeader(), jwt);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
			.antMatchers("/hello").permitAll()
			.antMatchers("/api/user/me").hasAnyRole("USER", "ADMIN")
			.anyRequest().permitAll()
			.and()
			.formLogin()
			.disable()
			.csrf()
			.disable()
			.headers()
			.disable()
			.httpBasic()
			.disable()
			.rememberMe()
			.disable()
			.logout()
			.disable()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
			.and()

			.exceptionHandling()
			.accessDeniedHandler(accessDeniedHandler())
				.and()
			.addFilterAfter(jwtAuthenticationFilter(), SecurityContextPersistenceFilter.class)
		;
	}
}
```

- 변경 후

```java
package com.kdt.instakyuram.security;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.kdt.instakyuram.security.jwt.Jwt;
import com.kdt.instakyuram.security.jwt.JwtAuthenticationFilter;
import com.kdt.instakyuram.security.jwt.JwtConfigure;
import com.kdt.instakyuram.token.service.TokenService;

@EnableWebSecurity
@Configuration
public class WebSecurityConfigure {
	private final Logger log = LoggerFactory.getLogger(getClass());

	private final JwtConfigure jwtConfigure;

	public WebSecurityConfigure(JwtConfigure jwtConfigure) {
		this.jwtConfigure = jwtConfigure;
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public Jwt jwt() {
		return new Jwt(
			this.jwtConfigure.issuer(),
			this.jwtConfigure.clientSecret(),
			this.jwtConfigure.accessToken(),
			this.jwtConfigure.refreshToken()
		);
	}

	public JwtAuthenticationFilter jwtAuthenticationFilter(Jwt jwt, TokenService tokenService) {
		return new JwtAuthenticationFilter(
			this.jwtConfigure.accessToken().header(),
			this.jwtConfigure.refreshToken().header(),
			jwt,
			tokenService
		);
	}

	@Bean
	public AccessDeniedHandler accessDeniedHandler() {
		log.warn("accessDeniedHandler");
		return (request, response, e) -> {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			response.setContentType("text/plain;charset=UTF-8");
			response.getWriter().write("ACCESS DENIED");
			response.getWriter().flush();
			response.getWriter().close();
		};
	}

	@Bean
	public AuthenticationEntryPoint authenticationEntryPoint() {
		return (request, response, e) -> {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType("text/plain;charset=UTF-8");
			response.getWriter().write("UNAUTHORIZED");
			response.getWriter().flush();
			response.getWriter().close();
		};
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, Jwt jwt, TokenService tokenService) throws
		Exception {
		http
			.authorizeRequests()
			.antMatchers("/api/members/signup", "/api/members/signin").permitAll()
			.anyRequest().authenticated()
			.and()
			.formLogin()
			.disable()
			.csrf()
			.disable()
			.headers()
			.disable()
			.httpBasic()
			.disable()
			.rememberMe()
			.disable()
			.logout()
			.disable()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.exceptionHandling()
			.accessDeniedHandler(accessDeniedHandler())
			.authenticationEntryPoint(authenticationEntryPoint())
			.and()
			.addFilterBefore(jwtAuthenticationFilter(jwt, tokenService), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}
```

## HttpSecurity Configure

- 간단한 HttpSecurity 설정 예시

```java
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
			.antMatchers("/hello").permitAll()
			.antMatchers("/api/user/me").hasAnyRole("USER", "ADMIN")
			.anyRequest().permitAll()
			.and()
			.formLogin()
			.disable()
			.csrf()
			.disable()
			.headers()
			.disable()
			.httpBasic()
			.disable()
			.rememberMe()
			.disable()
			.logout()
			.disable()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
			.and()
			.exceptionHandling()
			.accessDeniedHandler(accessDeniedHandler())
				.and()
			.addFilterAfter(jwtAuthenticationFilter(), SecurityContextPersistenceFilter.class)
		;
	}
```

- 바뀐 방식에서는 **SecurityFilterChain**를 Bean으로 등록하는 방식

```java
    @Bean
	public SecurityFilterChain filterChain(HttpSecurity http, Jwt jwt, TokenService tokenService) throws
		Exception {
		http
			.authorizeRequests()
			.antMatchers("/api/members/signup", "/api/members/signin").permitAll()
			.anyRequest().authenticated()
			.and()
			.formLogin()
			.disable()
			.csrf()
			.disable()
			.headers()
			.disable()
			.httpBasic()
			.disable()
			.rememberMe()
			.disable()
			.logout()
			.disable()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.exceptionHandling()
			.accessDeniedHandler(accessDeniedHandler())
			.authenticationEntryPoint(authenticationEntryPoint())
			.and()
			.addFilterBefore(jwtAuthenticationFilter(jwt, tokenService), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
```

기존 방식에서는 메서드를 오버라이딩해서 설정을 하고 클래스 내부에 설정 정보를 저장하는 방식.

`바뀐 방식에서는 모든것들을 Bean으로 등록해서 스프링 컨테이너가 관리할 수 있도록 변경이 된 듯 하다`

- 반환 값이 void에서 설정 유형(객체, 빈)으로 변경
- 그에 따라 return을 해야한다 (http.build())

## getApplicationContext(), getBean()

기존의 방식에서는 WebSecurityConfigurerAdapter가 getApplicationContext() 함수와 getBean() 함수를 제공해줬기 때문에 getApplicationContext().getBean(Jwt.class); 와 같이 Bean을 코드로써 가지고 올 수 있었다.

```java
	public JwtAuthenticationFilter jwtAuthenticationFilter() {
		Jwt jwt = getApplicationContext().getBean(Jwt.class);
		return new JwtAuthenticationFilter(jwtConfigure.getHeader(), jwt);
	}
```

변경 후에는 WebSecurityConfigurerAdapter를 더 이상 사용하지 않기 때문에 Bean을 저런 방식으로 가지고 올 수 없다.  
 대신 Bean을 매개변수로 받으면 자동으로 주입이 되기 때문에 아래와 같은 방식으로 의존성을 주입 시켜 준다.

- SecurityFilterchain 설정이 Bean으로 등록되기 때문에 필요한 Jwt, TokenService와 같은 것들을 매개변수로 받을 수 있다.

```java
	public JwtAuthenticationFilter jwtAuthenticationFilter(Jwt jwt, TokenService tokenService) {
		return new JwtAuthenticationFilter(
			this.jwtConfigure.accessToken().header(),
			this.jwtConfigure.refreshToken().header(),
			jwt,
			tokenService
		);
	}
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, Jwt jwt, TokenService tokenService) throws
		Exception {
		http
        
        ...중간 생략
        
        .addFilterBefore(jwtAuthenticationFilter(jwt, tokenService), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
        
```





### 참조

* https://docs.spring.io/spring-security/site/docs/5.7.0-M2/api/org/springframework/security/config/annotation/web/builders/HttpSecurity.html#authorizeRequests()

* https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/config/annotation/web/builders/WebSecurity.html

* https://kimchanjung.github.io/programming/2020/07/02/spring-security-02/

* https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter
* https://velog.io/@pjh612/Deprecated%EB%90%9C-WebSecurityConfigurerAdapter-%EC%96%B4%EB%96%BB%EA%B2%8C-%EB%8C%80%EC%B2%98%ED%95%98%EC%A7%80