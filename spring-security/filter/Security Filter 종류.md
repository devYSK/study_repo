# SecurityFilter 종류

![image-20221218165656546](/Users/ysk/study/study_repo/spring-security/filter/images//image-20221218165656546.png)



시큐리티 필터들은 필터 체인이라는 필터 리스트에 순서대로 묶여서 동작하며 다양한 필터들이 들어간다.

각각의 필터는 순서를 유지하며, 요청이 필터의 순서대로 지나가면서 각 필터들은 해야할 일들을 한다.

각각의 필터는 단일 필터 단일 책임 원칙(SRP) 처럼 각기 서로 다른 관심사를 해결한다

- 필터는 넣거나 뺄 수 있고 순서를 조절할 수 있다. (이때 필터의 순서가 매우 critical 할 수 있기 때문에 기본 필터들은 그 순서가 어느정도 정해져 있다.)



- *HeaderWriterFilter* : Http 해더를 검사한다. 써야 할 건 잘 써있는지, 필요한 해더를 더해줘야 할 건 없는가?
- *CorsFilter* : 허가된 사이트나 클라이언트의 요청인가?
- *CsrfFilter* : 내가 내보낸 리소스에서 올라온 요청인가?
- *LogoutFilter* : 지금 로그아웃하겠다고 하는건가?
- *UsernamePasswordAuthenticationFilter* : username / password 로 로그인을 하려고 하는가? 만약 로그인이면 여기서 처리하고 가야 할 페이지로 보내 줄께.
- *ConcurrentSessionFilter* : 여거저기서 로그인 하는걸 허용할 것인가?
- *BearerTokenAuthenticationFilter* : Authorization 해더에 Bearer 토큰이 오면 인증 처리 해줄께.
- *BasicAuthenticationFilter* : Authorization 해더에 Basic 토큰을 주면 검사해서 인증처리 해줄께.
- *RequestCacheAwareFilter* : 방금 요청한 request 이력이 다음에 필요할 수 있으니 캐시에 담아놓을께.
- *SecurityContextHolderAwareRequestFilter* : 보안 관련 Servlet 3 스펙을 지원하기 위한 필터라고 한다.(?)
- *RememberMeAuthenticationFilter* : 아직 Authentication 인증이 안된 경우라면 RememberMe 쿠키를 검사해서 인증 처리해줄께
- *AnonymousAuthenticationFilter* : 아직도 인증이 안되었으면 너는 Anonymous 사용자야
- *SessionManagementFilter* : 서버에서 지정한 세션정책을 검사할께.
- *ExcpetionTranslationFilter* : 나 이후에 인증이나 권한 예외가 발생하면 내가 잡아서 처리해 줄께.
- *FilterSecurityInterceptor* : 여기까지 살아서 왔다면 인증이 있다는 거니, 니가 들어가려고 하는 request 에 들어갈 자격이 있는지 그리고 리턴한 결과를 너에게 보내줘도 되는건지 마지막으로 내가 점검해 줄께.
- 그 밖에... OAuth2 나 Saml2, Cas, X509 등에 관한 필터들도 있습니다.