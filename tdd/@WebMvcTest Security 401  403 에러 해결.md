# @WebMvcTest Security 401  403 응답 해결



@WebMvcTest는 MVC와 관련된 애노테이션(Controller, ControllerAdvice, Filter, WebMvcConfigurer 등..)이 적용된 Bean들만 불러오고, @Component, @Service, @Repository와 같은 Bean들은 불러오지 않는다.

* https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/test/autoconfigure/web/servlet/WebMvcTest.html



그리고 @WebMvcTest는 **Spring Security를 auto-configure** 한다

여기서 문제가 되는 것은 **직접 구현한 Spring Security Configuration, Bean들도 불러오지 않는다**

내가 구현한 SecurityConfig 클래스 대신, 스프링 시큐리티가 자동으로 구성하는 Configuration 파일들을 불러와서 사용한다.

* 자동 구성되는 클래스들이 엄청 많지만 대표적으로 **SpringBootWebSecurityConfiguration** 가 있다.

```java
@Configuration(proxyBeanMethods = false)
@ConditionalOnDefaultWebSecurity
@ConditionalOnWebApplication(type = Type.SERVLET)
class SpringBootWebSecurityConfiguration {

	@Bean
	@Order(SecurityProperties.BASIC_AUTH_ORDER)
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeRequests().anyRequest().authenticated().and().formLogin().and().httpBasic();
		return http.build();
	}

}

```

> 모든 request에 인증을 걸고 formLogin을 해야만 가능하도록 구현해놓았다. 



**그래서 모든 path에 접근가능하도록 직접 구현한 SecurityConfig가 적용되지 않으므로 401, 403 응답 발생한다. **

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain httpSecurity(HttpSecurity http) throws Exception {
		return http
			.csrf().disable()

			.authorizeRequests()
			.antMatchers("/**").permitAll()
			.anyRequest().permitAll()
			.and()
			.build();
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return web -> web.ignoring()
			.antMatchers("/**", "/api/v1/**");
	}

}
```

* SecurityConfig에 모든 요청을 허용하게 설정했다. 모든 요청을 허용했으니 어떤 요청이던 패스가 되어야한다.



이후 컨트롤러 테스트를 위해 mockMvc를 이용한 테스트시, 401 Unauthorized 응답을 반환한다. 

* SpringBootWebSecurityConfiguration 클래스에서 모든 요청에 대해 권한이 필요하도록 기본적으로 적용이 되어있기 때문



* **401 Unauthorized**는 비로그인 상태 에서 인증이 되지 않고 권한이 필요한 요청을 했을 때 발생하는 응답

* **403 Forbidden**은 **로그인을 하여 인증은 되었으나 권한이 맞지 않는 경우**에 발생하는 응답



**@WithMockUser**, **@WithAnonymousUser ** ,**@WithUserDetails**  어노테이션을 사용해더라도 403 Forbbiden이 발생한다.



그 이유는 **csrf**와 관련된 문제이다. 

## 해결방법 1 - .with(csrf())

**"org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf"** 를 추가해주면 된다

```java
@Test
void findAllByType_fail(String itemType) throws Exception {
	mockMvc.perform(get(BASE_REQUEST_URI)
        .with(csrf()) // here
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.param("kinds", itemType)
				.characterEncoding(StandardCharsets.UTF_8)
			)
			.andExpect(status().isBadRequest())
			.andDo(print());
}
```

mockMvc.perform(method().with(`csrf()`))



`.with(csrf())` 를 사용하면 addFilters=false 옵션이나 csrf().disable() 설정 없이 테스트를 통과할 수 있다. 



* with(csrf())를 사용하지 않은 경우 -> 세션에 저장된 CSRF 값과 매치되지 않으므로 403 이 발생한다. 



> **csrf(Cross-Site Request Forgery)**
>
> \- 공격자가 악의적인 코드를 심어놓은 사이트를 만들어놓고, 로그인 된 사용자가 클릭하게 만들어 **사용자 의지와 무관한 요청을 발생**시키는 공격
> \- **사용자는 로그인 한 상태**고 쿠키, 권한을 갖고있기 때문에 공격자가 위조한 웹사이트에 방문하게 되면 사용자 모르게 악의적인 POST, DELETE 요청을 정상 수행하도록 만들어버리는 공격
>
> \- 이를 해결하기 위해 스프링 시큐리티에서는 "**CSRF 토큰**" 을 이용해 토큰 값을 비교해서 일치하는 경우에만 메서드를 처리하도록 만든다. (**Synchronizer Token Pattern** 이라고 한다)



> **Synchronizer Token Pattern**
>
> \- 서버가 뷰를 만들어줄 때 **사용자 별 랜덤값을 만들어 세션에 저장**한 다음 이를 뷰 페이지에 같이 담아 넘겨주게 된다.
>
> \- 클라이언트는 HTTP 요청마다 숨겨진 csrf 토큰을 같이 넘겨줘야 하는 방식.
>
> \- 서버는 HTTP Request에 있는 csrf 토큰값과 세션에 저장되어있는 **토큰값을 비교해 일치하는 경우에만 처리를 진행**하는 방식이다
>
> -> 위조된 사이트의 경우 **csrf 토큰값이 일치하지 않기 때문에** 공격자가 악의적인 코드를 심어놔도 이를 실행하지 않음. 



* GET 요청에 대해서는 csrf 검증을 수행하지 않는다

 

\* with(csrf()) 를 추가한 경우 파라미터로 _csrf 값을 같이 보내주는 것을 실제로 확인할 수 있다.





### Session Vs Rest

csrf 토큰 방식을 살펴보면 각 사용자에 대한 세션을 이용하는 방식이라는 것을 확인할 수 있다. 

때문에 웹 브라우저를 통한 접근을 하는 경우, **세션/쿠키를 사용해 상태를 유지하려고 하는 경우** csrf를 사용하는 것이 안전하다.

 

하지만 REST API의 경우는 대개 무상태성을 유지하며 JWT와 같은 토큰 방식으로 인증하게 되면 요청이 세션에 의존하지 않기 때문이다



* 타임리프와 같은 템플릿 엔진을 통해 View를 같이 제공하는 애플리케이션 / 웹 브라우저를 통해 요청을 받는 애플리케이션 -> csrf 사용 권장

* Rest API만 제공하는 애플리케이션 = csrf 사용 안해도 무방.



# 해결방법 2 

@WebMvcTest를 사용하는 컨트롤러 테스트 클래스에

```java
@AutoConfigureMockMvc(addFilters = false)
```

를 사용한다.

@AutoConfigureMockMvc(addFilters=false) 옵션으로 세팅해두면 **스프링 시큐리티의 필터가 동작하지 않기 때문에 보안 관련 체크를 거의 제외**시킬 수 있다.  csrf 체크를 진행하지 않기 때문에 테스트가 통과할 수 있다..

* 오류를 생성한 cors Filter를 사용하지 않게 할 수 있다. 

**그러나 상당히 위험한 옵션이므로 사용하지 않는것이 좋다 **



# 해결방법 3

직접 구현한 Security Config class (bean)를 Import 한다.

```java
@Import(SecurityConfig.class)
```





### 참조

* https://kth990303.tistory.com/408

* https://csy7792.tistory.com/243