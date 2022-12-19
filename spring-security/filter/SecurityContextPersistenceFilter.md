# SecurityContextPersistenceFilter - SecurityContext 영속화 필터

SecurityContext라는 녀석을 영속화 하는 책임을 가진 필터

보통 필터 목록 중 2번째 또는 3번째 위치하며 거의 최상단에 위치하는 필터이다.



SecurityContextRepository 인터페이스 구현체를 통해 사용자의 SecurityContext 객체를 생성, 저장, 조회한다

* SecurityContext가 존재하지 않는다면, empty SecurityContext를 생성

SecurityContextRepository 인터페이스 기본 구현은 Session을 이용하는 HttpSessionSecurityContextRepository 클래스

별도의 변경이 없다면, HttpSessionSecurityContextRepository이 사용되며 HttpSession 의 Attribute에 SecurityContext가  저장된다. 이와 같이 SecurityContext를 영속화 할 뿐만 아니라, 이 녀석은 요청의 세션에서 저장되어 있던 SecurityContext를 꺼내와 SecurityContextHolder라는 홀더에 집어넣어 요청 전반에 걸쳐 SecurityContext를 사용할 수 있게끔 한다. 



이 필터는 서블릿 컨테이너(특히 Weblogic) 비호환성을 해결하기 위해 요청당 한 번만 실행된다. 



> 현재는 Depreated 되어 [`SecurityContextHolderFilter`](https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/web/context/SecurityContextHolderFilter.html) 사용을 권장한다고 한다. 



우리가 만약 커스텀한 인증 필터를 만들어 넣고 싶다면
`반드시 SecurityContextPersistenceFilter필터 뒤에 넣어야 한다!!!`



<img src="https://blog.kakaocdn.net/dn/bJNKXl/btrTRpzEuKH/hKuZUC7e2iwADg8iLCGyc1/img.png" width = 900 height = 550>



## 동작 시나리오

### 익명 사용자가 접근 할 경우

- 새로운 SecurityContext 객체를 생성하여 SecurityContextHolder에 저장한다.
- AnonymousAuthenticationFilter에서 AnonymousAuthenticationToken 객체를 SecurityContext에 저장한다.

### 인증 사용자가 접근 할 경우

- 새로운 SecurityContext 객체를 생성하여 SecurityContextHolder에 저장한다.
- UsernamePasswordAuthenticationFilter에서 인증 성공 후 UsernamePasswordAuthenticationToken 객체를 SecurityContext에 저장한다.
- 인증이 최종 완료되면 SecurityContextPersistenceFilter가 Session에 SecurityContext를 저장한다.

### 인증 된 사용자가 접근 할 경우

- Session에서 SecurityContext 객체를 꺼내서 SecurityContextHolder에 저장한다.
- SecurityContext 안에는 Authentication 객체가 존재하며 계속 인증을 유지한다.

### 최종 응답 시 공통 로직

- SecurityContextHolder.clearContext()를 통해서 SecurityContext를 삭제처리한다.
  - why? 모든 경우에 SecurityContext 객체를 SecurityContextHolder에 저장하기 때문에



> 아래 코드를 분석한것이다

## 코드

```java
public class SecurityContextPersistenceFilter extends GenericFilterBean {
	...

	private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// ensure that filter is only applied once per request
		if (request.getAttribute(FILTER_APPLIED) != null) {
			chain.doFilter(request, response);
			return;
		}
		request.setAttribute(FILTER_APPLIED, Boolean.TRUE);
		if (this.forceEagerSessionCreation) {
			HttpSession session = request.getSession();
			if (this.logger.isDebugEnabled() && session.isNew()) {
				this.logger.debug(LogMessage.format("Created session %s eagerly", session.getId()));
			}
		}
		HttpRequestResponseHolder holder = new HttpRequestResponseHolder(request, response);
		SecurityContext contextBeforeChainExecution = this.repo.loadContext(holder);
		try {
			SecurityContextHolder.setContext(contextBeforeChainExecution);
			if (contextBeforeChainExecution.getAuthentication() == null) {
				logger.debug("Set SecurityContextHolder to empty SecurityContext");
			}
			else {
				if (this.logger.isDebugEnabled()) {
					this.logger
							.debug(LogMessage.format("Set SecurityContextHolder to %s", contextBeforeChainExecution));
				}
			}
			chain.doFilter(holder.getRequest(), holder.getResponse());
		}
		finally {
			SecurityContext contextAfterChainExecution = SecurityContextHolder.getContext();
			// Crucial removal of SecurityContextHolder contents before anything else.
			SecurityContextHolder.clearContext();
			this.repo.saveContext(contextAfterChainExecution, holder.getRequest(), holder.getResponse());
			request.removeAttribute(FILTER_APPLIED);
			this.logger.debug("Cleared SecurityContextHolder to complete request");
		}
	}


}
```

## SecurityContextRepository

요청 간에 SecurityContext를 유지하는 데 사용되는 전략을 정하는 인터페이스. 

SecurityContextPersistenceFilter에서 현재 실행 스레드에 사용해야 하는 SecurityContext를 저장하고 삭제하는데 사용.



이전에는 HttpSessionContextIntegrationFilter 이란 필터가 있었는데, 

`저장소가 반드시 세션일 필요는 없기 때문에 추상화된 객체로 발전된 필터`



기본으로 사용하는 전략은 HTTP Session을 사용한다.

`HttpSessionSecurityContextRepository`

- HTTP Session에서 읽어오는것이 기본 전략.

- Spring-Session과 연동하여 세션 클러스터를 구현할 수 있다



## HttpSessionSecurityContextRepository

SecurityContextRepository의 기본 구현체.  
사용자의 SecurityContext 객체를 생성, 저장, 조회한다.  



### loadContext - 컨텍스트를 세션으로부터 가져오기

```java
@Override	
public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
		HttpServletRequest request = requestResponseHolder.getRequest();
		HttpServletResponse response = requestResponseHolder.getResponse();
		HttpSession httpSession = request.getSession(false);
		SecurityContext context = readSecurityContextFromSession(httpSession);
		if (context == null) {
			context = generateNewContext();
			if (this.logger.isTraceEnabled()) {
				this.logger.trace(LogMessage.format("Created %s", context));
			}
		}
		if (response != null) {
			SaveToSessionResponseWrapper wrappedResponse = new SaveToSessionResponseWrapper(response, request,
					httpSession != null, context);
			requestResponseHolder.setResponse(wrappedResponse);
			requestResponseHolder.setRequest(new SaveToSessionRequestWrapper(request, wrappedResponse));
		}
		return context;
	
}
```

request로부터 session을 가져오고, session으로부터 SecurityContext를 가져온다

* readSecurityContextFromSession(httpSession)

```java
private SecurityContext readSecurityContextFromSession(HttpSession httpSession) {
		if (httpSession == null) {
			this.logger.trace("No HttpSession currently exists");
			return null;
		}
		// Session exists, so try to obtain a context from it.
		Object contextFromSession = httpSession.getAttribute(this.springSecurityContextKey);
		if (contextFromSession == null) {
			if (this.logger.isTraceEnabled()) {
				this.logger.trace(LogMessage.format("Did not find SecurityContext in HttpSession %s "
						+ "using the SPRING_SECURITY_CONTEXT session attribute", httpSession.getId()));
			}
			return null;
		}

		// We now have the security context object from the session.
		if (!(contextFromSession instanceof SecurityContext)) {
			this.logger.warn(LogMessage.format(
					"%s did not contain a SecurityContext but contained: '%s'; are you improperly "
							+ "modifying the HttpSession directly (you should always use SecurityContextHolder) "
							+ "or using the HttpSession attribute reserved for this class?",
					this.springSecurityContextKey, contextFromSession));
			return null;
		}

		if (this.logger.isTraceEnabled()) {
			this.logger.trace(
					LogMessage.format("Retrieved %s from %s", contextFromSession, this.springSecurityContextKey));
		}
		else if (this.logger.isDebugEnabled()) {
			this.logger.debug(LogMessage.format("Retrieved %s", contextFromSession));
		}
		// Everything OK. The only non-null return from this method.
		return (SecurityContext) contextFromSession;
	}
```

* 세션이 null이면 context도 null이다

* 세션이 존재하면 httpSession으로부터 getAttribute로 가져온다

  * ```
    SPRING_SECURITY_CONTEXT_KEY
    ```

  * key는 default 로  "SPRING_SECURITY_CONTEXT" 이다

* 세션으로부터 가져온 context가 SecurityContext가 아니라면 null을 리턴한다
* SecurityContext가 맞다면 세션으로부터 가져온 context를 리턴한다.

 

### saveContext - 세션에 SecurityContext를 저장

```java
@Override
public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
		SaveContextOnUpdateOrErrorResponseWrapper responseWrapper = WebUtils.getNativeResponse(response,
				SaveContextOnUpdateOrErrorResponseWrapper.class);
		if (responseWrapper == null) {
			boolean httpSessionExists = request.getSession(false) != null;
			SecurityContext initialContext = SecurityContextHolder.createEmptyContext();
			responseWrapper = new SaveToSessionResponseWrapper(response, request, httpSessionExists, initialContext);
		}
		responseWrapper.saveContext(context);
}
```

* responseWrapper가 null이라면 EmptyContext(빈 깡통 컨텍스트르)를 생성한다
* responseWraaper.saveContext() 에 들어가보면 SPRING_SECURITY_CONTEXT_KEY의 키값으로 httpSession에 SecurityContext를 저장한다

