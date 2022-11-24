

# AuthenticationPrincipal Null 이슈 - 주입받지 못함 



### 결론부터

* AuthenticationPrincipalArgumentResolver 가 빈으로 등록되지 않음. 빈으로 등록해줘야함
* @EnableWebSecurity는 설정했고, @EnableWebMvc는 등록안했다가 등록하니 해결됌 



> spring boot version : 2.7.4
>
> ```kotlin
> implementation("org.springframework.boot:spring-boot-starter-security:2.7.4")
> ```
>
> 

---



스프링 시큐리티를 사용하였을 때, 사용자가 인증된다면 Controller 에서 파라미터로 `@AuthenticationPrincipal` 을 주입 받을 수 있다.



### @AuthenticationPrincipal

로그인한 사용자의 정보를 파라메터로 받고 싶을때 기존에는 다음과 같이 Principal 객체로 받아서 사용한다.

- 하지만 이 객체는 SecurityContextHolder의 Principal과는 다른 객체이다.  





스프링 시큐리티로 인증관련 부분을 개발하다가 @AuthenticationPrincipal을 사용했는데, 값이 주입 안되어서 고생을 좀 했었다.  



* UserDetails 정상 구현. 
* @EnableWebSecurity 정상 설정
* 의존성 문제 없음
* org.springframework.security.core.userdetails. UserDetails를 adapter 패턴으로 구현도 해봄,
  * 물론 엔티티에 직접 구현도 해봄
* org.springframework.security.core.userdetailsUser를 상속 받기도 해봄.
* SecurityContextHolder.getContext().getAuthentication() null 아님. UserDetailsService에서 반환한 값 제대로 주입됌

  


문제 발생 환경은 다음과 같았다.



1. WebConfigruation 추가 설정을 해줘야 하는 부분이 있어서 WebConfig 클래스에 WebMvcConfigurationSupport를 상속받음
2. @EnableMvc 어노테이션을 사용하지 않음
3. @AuthenticationPrincipal 어노테이션 변수에 주입해주는 행동은 `AuthenticationPrincipalArgumentResolver` 가 해주는데 아무리 디버깅 해도 걸리지도 않음. 즉 활성화 되지 않았단 소리. 
4. @EnableWebMvc 어노테이션을 붙이니까 해결됌. 자동으로 빈 등록해주어서 받아올 수 있었다. 



그래서 왜 그러는가 원인을 분석해 보았는데, 답은찾지 못했고,  @EnableWebMvc 어노테이션을 붙이니까 해결되었다. 

> This issue is because `DelegatingFlashMessagesConfiguration` from `CustomFlashMessagesConfigurer` eagerly initializes the handlerExceptionResolver which prevents the AuthenticationPrincipalArgumentResolver from being registered until after Spring MVC has constructed the DispatcherServlets custom argument resolvers.



## @EnableWebMvc

EnableWebMvc어노테이션을 사용하면 스프링이 제공하는 웹과 관련된 최신 전략 빈들이 등록된다.

이 어노테이션을 사용 안하면 스프링과 관련된 웹과 관련된 빈들이 등록 안되는 부분이 있다.



아무래도 이부분에 대해서 더 공부 해봐야 겠다.



## 공부할만한 링크



* https://ncucu.me/21
* https://incheol-jung.gitbook.io/docs/q-and-a/spring/enablewebmvc

* https://goodgid.github.io/Spring-Enable-MVC-Annotation/
* https://mangkyu.tistory.com/176
* https://docs.spring.io/spring-security/reference/servlet/integrations/mvc.html#mvc-authentication-principal
* https://stackoverflow.com/questions/29657039/authenticationprincipal-is-empty-when-using-enablewebsecurity