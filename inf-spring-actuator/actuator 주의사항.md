* https://techblog.woowahan.com/9232/

안녕하세요, 우아한형제들 SOC팀에서 Application Security를 담당하고 있는 권현준입니다.

오늘 준비한 주제는 개발자에게 편리함을 제공하나, 잘못 사용하면 매우 위험한 Actuator를 안전하게 사용하는 방법입니다.

이 글이 개발자분들이나 보안담당자분들께 조금이나마 도움이 되었으면 좋겠습니다. 바로 내용 들어가죠!

# 🍀 Spring Actuator란

Spring Actuator는 *org.springframework.boot:spring-boot-starter-actuator* 패키지를 Dependency에 추가만 해주면 바로 사용할 수 있는 기능으로, Spring boot를 사용하여 Backend를 구현할 경우 애플리케이션 모니터링 및 관리 측면에서 도움을 줄 수 있습니다.

```java
# build.gradle 예시
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-actuator'
}
```

HTTP 방식과 JMX 방식이 있으며 대표적으로 많이 쓰는 것이 Health Check 용도의 actuator health endpoint입니다.

# 🍀 Actuator 보안 이슈

애플리케이션 모니터링 및 관리 측면에서 개발자에게 편의를 주는 기능이나, 잘못 사용할 경우 비밀번호, API KEY, Token 등 Credential들이나 내부 서비스 도메인, IP 주소와 같은 중요 정보들이 유출될 수 있습니다. 그뿐만 아니라 서비스를 강제로 중단시켜 가용성을 침해할 수도 있습니다.

정리하자면 불필요한 endpoint를 활성화시켜 문제가 발생한다고 할 수 있습니다.
여러 가지 상황이 있을 수 있으나 대표적으로 아래 3가지 상황을 예시로 들어보겠습니다.

## 1. 환경변수로 중요 정보를 저장해 둔 경우

서비스를 개발할 때 보통 Git을 이용하여 협업을 하고, Github이나 Gitlab, bitbucket과 같은 Code Repository에 push하고, master 브랜치에 merge하는 등의 작업을 하게 됩니다.
이때 중요 정보를 소스코드에 하드코딩할 경우, 유출될 우려가 있기 때문에 소스코드에 API KEY나 DB Password와 같은 중요 정보를 하드코딩하지 않도록 권고합니다.
하지만 개발자분들 입장에서는 반드시 그 정보들을 사용해야만 합니다.
그래서 개발자분들께서 주로 사용하시는 방법이 소스코드에서는 환경 변수를 사용하도록 코드를 작성하고, 필요한 중요 정보를 환경 변수에 대입하여 사용하는 방법입니다.

이때, **Spring Actuator의 env endpoint가 필요하여 enable시키고 expose까지 시켜두었다면, 서비스에서 사용 중인 환경 변수를 볼 수 있게 되기 때문에, 의도치 않게 설정해둔 중요 정보가 유출**될 수 있습니다. 아래 스크린샷과 같이 actuator의 env endpoint를 호출함으로써 서비스에서 사용 중인 환경 변수를 확인할 수 있습니다.
![img](https://techblog.woowahan.com/wp-content/uploads/2022/10/image2022-7-22_0-29-33.png)

[Spring Framework 공식 문서](https://docs.spring.io/spring-boot/docs/current/actuator-api/htmlsingle/#env.entire)를 통해 Actuator env endpoint에 대한 HTTP Request와 Response샘플을 확인할 수 있습니다.
![img](https://techblog.woowahan.com/wp-content/uploads/2022/10/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA-2022-10-04-%E1%84%8B%E1%85%A9%E1%84%8C%E1%85%A5%E1%86%AB-6.43.07.png)

## 2. 중요 정보가 메모리에 올라가 있는 경우

서비스 운영 중 사용한 중요 정보가 아직 메모리에 남아있는 경우에도 문제가 될 수 있습니다.
Spring Actuator는 heapdump라는 endpoint를 제공함으로써 현재 서비스가 점유 중인 heap메모리를 덤프 하여 그 데이터를 제공해 주는 기능이 있어, 덤프 된 메모리 값을 통해 중요 정보가 유출될 위험이 있습니다.

아래 스크린샷을 통해 Spring Actuator의 heapdump endpoint를 이용하여 웹 애플리케이션의 heap메모리를 덤프 하여, 실제 application.properties에 작성되어 사용되고 있는 Database의 정보를 확인할 수 있습니다.

![img](https://techblog.woowahan.com/wp-content/uploads/2022/10/image2022-7-22_0-33-30-e1666837821921.png)

![img](https://techblog.woowahan.com/wp-content/uploads/2022/10/image2022-7-22_0-38-41-e1666837852652.png)

[Spring Framework 공식 문서](https://docs.spring.io/spring-boot/docs/current/actuator-api/htmlsingle/#heapdump)를 통해 Actuator heapdump endpoint에 대한 HTTP Request와 Response샘플을 확인할 수 있습니다.
![img](https://techblog.woowahan.com/wp-content/uploads/2022/10/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA-2022-10-04-%E1%84%8B%E1%85%A9%E1%84%8C%E1%85%A5%E1%86%AB-7.03.04.png)

## 3. Shutdown endpoint를 enable/expose한 경우

많지 않은 경우이지만, Shutdown endpoint를 활성화하여 사용할 경우 문제가 될 수 있습니다.
기본적으로 Shutdown endpoint는 비활성화되어 있는데, 이를 임의로 활성화시킨 뒤 사용하고자 할 때 발생하는 문제로, Shutdown endpoint를 사용할 경우 임의로 웹 애플리케이션을 말 그대로 중지시켜버릴 수 있기 때문에, 서비스 가용성에 문제가 발생할 수 있습니다.

[Spring Framework 공식 문서](https://docs.spring.io/spring-boot/docs/current/actuator-api/htmlsingle/#shutdown)를 통해 Actuator shutdown endpoint에 대한 HTTP Request와 Response샘플을 확인할 수 있습니다.
![img](https://techblog.woowahan.com/wp-content/uploads/2022/10/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA-2022-10-04-%E1%84%8B%E1%85%A9%E1%84%8C%E1%85%A5%E1%86%AB-7.06.43.png)

# 🍀 Spring Actuator 안전하게 사용하는 방법

지금까지 Spring Actuator가 무엇인지, 그리고 어떤 경우에 문제가 발생하는지를 알아보았는데요, 이제 본격적으로 안전하게 사용하는 방법을 알아보도록 하겠습니다.

### – Actuator endpoint는 all disable 상태에서 필요한 것만 include하여 화이트리스트 형태로 운영한다.

Actuator는 shutdown endpoint를 제외한 나머지 endpoint는 enable 되어있는 것이 기본 설정입니다. 하지만 이 기본 설정 그대로 유지할 경우, 불필요한 endpoint가 활성화되어 추후 잠재적 위험이 될 수 있어, **기본 설정을 따르지 않겠다는 설정**을 해주어야 합니다.

이때 사용하는 것이 *management.endpoints.enabled-by-default* 속성입니다.
해당 속성을 false로 만들어 줌으로써, 모든 endpoint에 대하여 disable 상태를 유지할 수 있습니다. 운영 중 필요한 endpoint가 있다면 *management.endpoint.[endpoint name].enable* 속성을 true로 설정하면 됩니다.

### – Actuator endpoint expose(노출)가 필요한 경우, 꼭 필요한 것만 include하여 화이트리스트 형태로 운영한다. 또한 asterisk(*)를 이용하여 include하지 않는다.

Actuator endpoint들은 enable 시킨다고 바로 사용할 수 있는 구조는 아닙니다. enable된 endpoint를 expose(노출) 시켜야 이용할 수 있다는 특징이 있습니다. 기본적으로 설정된 값이 있는데, JMX와 HTTP(WEB) 방식이 각각 기본적으로 expose 되어있는 endpoint가 다릅니다.

아래 스크린샷과 같이 JMX의 경우 사용할 수 있는 모든 endpoint가 기본적으로 expose 되어있으며, HTTP(WEB)은 그와 반대로 health endpoint만이 유일하게 기본적으로 expose되어 있습니다.
![img](https://techblog.woowahan.com/wp-content/uploads/2022/10/image2022-7-22_1-1-0.png)

HTTP(WEB)의 경우 기본적으로 expose 되어있는 endpoint가 매우 적기 때문에, 필요한 endpoint를 expose하기 위해서는 *management.endpoints.web.exposure.include* 속성에 필요한 endpoint 리스트를 작성해야 합니다.

이때 와일드카드도 입력이 가능하기 때문에 Asterisk(*)를 넣는 경우가 있는데, 이렇게 설정할 경우 필요치 않은 endpoint가 노출되기 때문에 추후 잠재적 위험이 될 수 있어, **반드시 와일드카드 형태가 아닌 필요한 endpoint를 각각 추가해 주어야 합니다.**

### – shutdown endpoint는 enable하지 않는다.

shutdown endpoint는 말 그대로 웹 애플리케이션을 shutdown 시킬 수 있는 기능을 제공하기에, 서비스 가용성을 침해할 우려가 있음을 위에서 예시로 설명했었습니다. **shutdown endpoint는 기본적으로 disable되며, expose도 되지 않기 때문에, 절대로 enable하지 않도록 각별히 신경을 써주어야 합니다.**

### – JMX형태로 Actuator 사용이 필요하지 않을 경우, 반드시 disable한다.

JMX는 Default로 expose되어있는 endpoint가 많기 때문에, 사용하지 않음에도 enable 시켜두면 잠재적 위험이 될 수 있습니다.

이에 JMX형태로 Actuator 사용을 하지 않는 경우 *management.endpoints.jmx.exposure.exclude = **형태로 속성을 추가함으로써,** 모든 endpoint가 JMX로 사용 불가하게 설정해 주어야 합니다.**

### – Actuator는 서비스 운영에 사용되는 포트와 다른 포트를 사용한다.

Actuator가 다양한 기능을 가진 만큼, 공격자들은 웹 사이트를 공격할 때 Actuator 관련 페이지가 존재하는지를 스캐닝 하는 경우가 굉장히 빈번합니다.

이때 서비스를 운영하는 포트와 다른 포트로 Actuator를 사용할 경우, 공격자들의 스캔으로부터 1차적으로 보호받을 수 있다는 장점이 있습니다. 이에 *management.server.port* 속성을 통해 **서비스를 운영하는 포트와 다른 포트로 설정하여 사용**할 것을 추천합니다.

### – Actuator Default 경로를 사용하지 않고, 경로를 변경하여 운영한다.

포트 관련 항목에서 언급했듯이, Actuator가 다양한 기능을 가진 만큼, 공격자들은 웹 사이트를 공격할 때 Actuator 관련 페이지가 존재하는지를 스캐닝 하는 경우가 많은데, 이때 주로 알려진 URI형태의 스캐닝을 많이 수행하게 됩니다.

그렇기에 Actuator 서비스에서 주로 사용하는 알려진 기본 경로(/actuator/[endpoint]) 대신 다른 경로를 사용함으로써 외부 공격자의 스캐닝으로부터 보호받을 수 있기 때문에 경로 변경을 추천하고자 합니다.
*management.endpoints.web.base-path* 속성을 통해 설정이 가능하며, **유추하기 어려운 문자열의 경로로 설정함으로써 보안성을 향상시킬 수 있습니다.**

### – Actuator에 접근할 때에는, 인증되었으며 권한이 있는 사용자만이 접근가능하도록 제어한다.

Actuator는 권한이 충분하며 인증된 관리자만이 다룰 수 있어야 하기에, 세션 또는 인증토큰을 통해 인증 여부와 접근 권한 유무를 파악한 뒤, 적절한 인가 검증 과정을 거쳐 접근할 수 있도록 제어를 해줄 것을 추천합니다. 다만 이 경우 Actuator를 사용할 때 반드시 인증을 위한 과정을 거쳐야 하기 때문에 health check와 같은 용도로는 부적합할 수 있어, 환경과 상황에 맞게 검토가 필요합니다.
아래 스크린샷은 Spring Security를 사용하여 Actuator에 접근 시 인증 과정을 탈 수 있도록 설정한 WebSecurityConfig 파일 샘플입니다.

```java
// WebSecurityConfig 샘플
protected void configure(HttpSecurity http) throws Exception {
    http
        .authorizeRequests() // 접근에 대한 인증 설정
          .antMatchers("/login", "/signup", "/user").permitAll() // 누구나 접근 허용
          .antMatchers("/actuator/**").hasRole("ADMIN") // ADMIN만 접근 가능
          .anyRequest().authenticated() // 나머지 요청들은 권한의 종류에 상관 없이 권한이 있어야 접근 가능
        .and()
          .logout() // 로그아웃
            .logoutSuccessUrl("/login") // 로그아웃 성공시 리다이렉트 주소
            .invalidateHttpSession(true) // 세션 날리기
    ;
  }
```

지금까지 소개드린 보안대책들이 모두 반영된 application.properties 는 아래와 같습니다.

```yaml
# Actuator 보안 설정 샘플

# 1. Endpoint all disable
management.endpoints.enabled-by-default = false

# 2. Enable specific endpoints
management.endpoint.info.enabled = true
management.endpoint.health.enabled = true

# 3. Exclude all endpoint for JMX and Expose specific endpoints
management.endpoints.jmx.exposure.exclude = *
management.endpoints.web.exposure.include = info, health

# 4. Use other port for Actuator
management.server.port = [포트번호]

# 5. Change Actuator Default path
management.endpoints.web.base-path = [/변경된 경로]
```

지금까지 안전하게 Actuator를 사용하는 방법에 대해서 작성을 해보았습니다.
제가 작성한 보안대책은 이론상 Best Practice이기 때문에 보안적으로 최대한 안전한 상태를 가이드하고 있습니다. **그렇기에 서비스 환경에 따라 조치 방안이 달라질 수 있다는 점 참고를 부탁드립니다.** 반드시 현재 환경과 상황에 맞추어 충분한 검토 후 적용하실 것을 추천드립니다.

# 끗