# SpringBoot AutoConfiguration 제외 방법



SpringBoot에서 사용되는 각 Spring 의존성마다 XXXX AutoConfiguration이 있다

이 클래스들을 제외시키면된다



예를들어 yml이나 properties로부터 DataSource를 구성해주는 DataSourceAutoConfiguration이라면



## 1. 어노테이션기반 AutoConfigruation 제외



```java
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
```

* DataSourceAutoConfigration 제외



 다른 빈을 자동 구성하는 데 영향을 미치지 않으며 Spring Boot의 DataSourceAutoConfigruation을 비활성화할 수 있다.





## 2. 구성 설정 파일(yml, properties)을 통한 AutoConfiguration 제외



```yml
spring:
  autoconfigure:
    exclude:
    - org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
```

* spring.autoconfigure.exclude 하고 패키지명과 XXXXAutoConfiguration 클래스명을 입력하면된다.









### 참조

* https://www.baeldung.com/spring-boot-failed-to-configure-data-source