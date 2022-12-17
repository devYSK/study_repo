# Jpa 쿼리 파라미터 로그 확인 - With DataJpaTest p6spy

Spring Boot 기본 설정으로는 hibernate SQL 바인딩 변수 자리에 '?' 표시된 SQL 과 바인딩 변수 값이 따로 표시되어 SQL 디버깅이 불편하다.



쿼리 파라미터가 어떤 값을 가지고 있는지 확인할 수 있는 2가지 방법과 커스텀 방법에 대해 정리한다. 

일반적으로 2번째 방법인 p6spy가 더 자세하고 편리하게 사용할 수 있지만, 리소스를 많이 사용하니 운영 환경에서는 사용하지 않는게 좋아 보인다.

# JPA 쿼리 파라미터 로그 체크

## 설정 파일을 통한 로그 설정 

yaml 또는 properties 파일을 통한 로그 레벨 설정으로 쿼리 파라미터 바인딩을 확인할 수 있다.



#### yaml

```yaml
logging:
  level:
    org.hibernate.SQL: debug
    org.hibernate.type.descriptor.sql : trace
```



#### properties

```properties
# show sql data binding
logging.level.org.hibernate.SQL = debug
logging.level.org.hibernate.type.descriptor.sql = trace

```



#### 전체 설정 코드

```yaml
spring:
  datasource:
    driver-class-name: org.h2.Driver
    url: jdbc:h2:mem:order;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false;
    username: sa
    password:
  jpa:
    open-in-view: false
    hibernate:
      ddl-auto: create-drop
    show-sql: false
    properties:
      hibernate.format_sql: true
      dialect: org.hibernate.dialect.H2Dialect
    database: h2

logging:
  level:
    org.hibernate.SQL: debug
    org.hibernate.type.descriptor.sql : trace
```

* show-sql 과 format_sql 을 둘다 true로 하면 실행 쿼리 로그가 중복된다. 



### 실행 코드

```java
@Test
void test() {
  Optional<Member> memberOptional = memberRepository.findById(7441L);  
}
```



### 실행 로그

```
2022-12-17 18:06:36.157 DEBUG 62316 --- [           main] org.hibernate.SQL                        : 
    select
        member0_.id as id1_2_0_,
        member0_.created_at as created_2_2_0_,
        member0_.modified_at as modified3_2_0_,
        member0_.address as address4_2_0_,
        member0_.age as age5_2_0_,
        member0_.description as descript6_2_0_,
        member0_.name as name7_2_0_,
        member0_.nick_name as nick_nam8_2_0_ 
    from
        members member0_ 
    where
        member0_.id=?
        
2022-12-17 18:06:36.166 TRACE 62316 --- [           main] o.h.type.descriptor.sql.BasicBinder      : binding parameter [1] as [BIGINT] - [7441]
```

> o.h.type.descriptor.sql.BasicBinder      : binding parameter [1] as [BIGINT] - [7441]

로 바인딩된 값을 출력하는 로그을 확인할 수 있다.



## 라이브러리를 사용한 로그 설정



> 주의할점 :  외부 라이브러리는 시스템 자원을 사용하기 때문에 개발 중에는 문제가 없으나 운영 시스템에 배포를 하게 된다면 성능 테스트를 사용하고 하는 것이 좋다. 왜냐하면 병목 현상의 원인이 될 수 있기 때문이다.



라이브러리 : p6spy

* https://github.com/p6spy/p6spy

* 다운로드 및 최신 버전 확인 : https://mvnrepository.com/artifact/com.github.gavlyukovskiy/p6spy-spring-boot-starter



> "P6SPY는 데이터베이스 데이터를 원활하게 가로 채고 기존 응용 프로그램에 코드 변경없이 원활하게 가로 채기 할 수있는 프레임 워크입니다. P6SPY 배포판에는 모든 JAVA 응용 프로그램에 대한 모든 JDBC 트랜잭션을 기록하는 응용 프로그램 인 p6log가 포함됩니다." - p6spy 소개 (번역기)



라이브러리만 추가해도 로그를 볼 수 있다.



먼저 의존성을 추가한다. 

### Maven

```xml
<!-- https://mvnrepository.com/artifact/com.github.gavlyukovskiy/p6spy-spring-boot-starter -->
<dependency>
    <groupId>com.github.gavlyukovskiy</groupId>
    <artifactId>p6spy-spring-boot-starter</artifactId>
    <version>1.8.1</version>
</dependency>
```

### Gradle

```groovy
// https://mvnrepository.com/artifact/com.github.gavlyukovskiy/p6spy-spring-boot-starter
implementation 'com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.8.1'
```



다음 properties 또는 yml 설정을 추가한다

#### application.properties

- 아래 설정의 기본값은 true 이나 관리를 위해 명시적으로 추가해 주는게 좋다.
- 그래야 라이브러리를 사용하는지 안하는지 한눈에 보기에도 편하기 때문이다.
- 만약 끄고싶다면 false값을 주면 된다

#### properties

```properties
decorator.datasource.p6spy.enable-logging=true
```



#### yaml

```yaml
decorator:
  datasource:
    p6spy:
      enable-logging: true
```



#### 전체 설정 코드

```yaml

spring:
  datasource:
    driver-class-name: org.h2.Driver
    url: jdbc:h2:mem:order;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false;
    username: sa
    password:
  jpa:
    open-in-view: false
    hibernate:
      ddl-auto: create-drop
    show-sql: false
    properties:
      hibernate.format_sql: true
      dialect: org.hibernate.dialect.H2Dialect
    database: h2

decorator:
  datasource:
    p6spy:
      enable-logging: true
```



### 실행 코드

```java
@Test
void test() {
  Optional<Member> memberOptional = memberRepository.findById(7441L);
  
  Member member = new Member("name", "ncik", 10, "서울시");
  memberRepository.save(member);
}
```



### 실행 로그

```
2022-12-17 18:18:16.630  INFO 62441 --- [           main] p6spy                                    : #1671268696630 | took 12ms | statement | connection 3| url jdbc:h2:mem:order
select member0_.id as id1_2_0_, member0_.created_at as created_2_2_0_, member0_.modified_at as modified3_2_0_, member0_.address as address4_2_0_, member0_.age as age5_2_0_, member0_.description as descript6_2_0_, member0_.name as name7_2_0_, member0_.nick_name as nick_nam8_2_0_ from members member0_ where member0_.id=?
select member0_.id as id1_2_0_, member0_.created_at as created_2_2_0_, member0_.modified_at as modified3_2_0_, member0_.address as address4_2_0_, member0_.age as age5_2_0_, member0_.description as descript6_2_0_, member0_.name as name7_2_0_, member0_.nick_name as nick_nam8_2_0_ from members member0_ where member0_.id=7441;
2022-12-17 18:18:16.657  INFO 62441 --- [           main] p6spy                                    : #1671268696657 | took 0ms | commit | connection 3| url jdbc:h2:mem:order

;
2022-12-17 18:18:16.786  INFO 62441 --- [           main] p6spy                                    : #1671268696786 | took 2ms | statement | connection 4| url jdbc:h2:mem:order
insert into members (id, created_at, modified_at, address, age, description, name, nick_name) values (default, ?, ?, ?, ?, ?, ?, ?)
insert into members (id, created_at, modified_at, address, age, description, name, nick_name) values (default, '2022-12-17T18:18:16.715+0900', '2022-12-17T18:18:16.715+0900', '서울시', 10, NULL, 'name', 'ncik');
2022-12-17 18:18:16.795  INFO 62441 --- [           main] p6spy                                    : #1671268696795 | took 0ms | commit | connection 4| url jdbc:h2:mem:order

;
```



* 단점은 1줄로 길게 나온다는 것이다 .
* 그래서 커스텀이 필요하다



## p6spy 커스텀 (Pretty Print Log)



### 커스텀 클래스 작성

```java

public class CustomP6spySqlFormat implements MessageFormattingStrategy {
    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {
        sql = formatSql(category, sql);
        Date currentDate = new Date();

        SimpleDateFormat format1 = new SimpleDateFormat("yy.MM.dd HH:mm:ss");

        return format1.format(currentDate) + " | "+ "OperationTime : "+ elapsed + "ms" + sql;
    }

    private String formatSql(String category,String sql) {
        if(sql ==null || sql.trim().equals("")) return sql;

        // Only format Statement, distinguish DDL And DML
        if (Category.STATEMENT.getName().equals(category)) {
            String tmpsql = sql.trim().toLowerCase(Locale.ROOT);
            if(tmpsql.startsWith("create") || tmpsql.startsWith("alter") || tmpsql.startsWith("comment")) {
                sql = FormatStyle.DDL.getFormatter().format(sql);
            }else {
                sql = FormatStyle.BASIC.getFormatter().format(sql);
            }
            sql = "|\nHeFormatSql(P6Spy sql,Hibernate format):"+ sql;
        }

        return sql;
    }
}
```

* 또는

```java
public class CustomP6spySqlFormat implements MessageFormattingStrategy {
    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {
        sql = formatSql(category, sql);
        return now + "|" + elapsed + "ms|" + category + "|connection " + connectionId + "|" + sql;
    }

    private String formatSql(String category,String sql) {
        if(sql ==null || sql.trim().equals("")) return sql;

        // Only format Statement, distinguish DDL And DML
        if (Category.STATEMENT.getName().equals(category)) {
            String tmpsql = sql.trim().toLowerCase(Locale.ROOT);
            if(tmpsql.startsWith("create") || tmpsql.startsWith("alter") || tmpsql.startsWith("comment")) {
                sql = FormatStyle.DDL.getFormatter().format(sql);
            }else {
                sql = FormatStyle.BASIC.getFormatter().format(sql);
            }
        }

        return sql;
    }
}
```



### 커스텀 Format 클래스 적용

```java
@Configuration
public class P6spyLogMessageFormatConfiguration {
    @PostConstruct
    public void setLogMessageFormat() {
        P6SpyOptions.getActiveInstance().setLogMessageFormat(CustomP6spySqlFormat.class.getName());
    }
}
```



## DataJpaTest에서 사용하기 

위와 같은 작업을 끝냈더라도 @DataJpaTest에서는 동작하지 않는다. 

* @DataJpaTest는 말 그대로 JPA 관련 테스트를 하기 위한 환경만 올라가기 때문. 

@DataJpaTest 애노테이션을 사용하면서 테스트하는 시점에 SQL log를 출력하기 위한 환경인 `P6spyLogMessageFormatConfiguration` 같이 올리면 된다. 



> `@Import` 어노테이션으로 Bean을 추가로 로딩할 수 있다. 

```java
@Import(P6spyLogMessageFormatConfiguration.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JpaTest {
  
}
```



다음과 같이 커스텀 어노테이션을 만들어 적용할 수도 있다. 



```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@DataJpaTest(showSql = false)
@ImportAutoConfiguration(DataSourceDecoratorAutoConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(P6spyLogMessageFormatConfiguration.class)
public @interface CustomDataJpaTest {
}
```



- DataJpaTest를 사용하도록 붙여주시고 showSql은 false로 줌으로써 Spring Data JPA가 기본으로 제공해주는 SQL 문 출력 기능을 사용하지 않도록 설정 
- @ImportAutoConfiguration 는 자동환경설정 클래스를 Import하는 기능을 한다.
- DataSourceDecoratorAutoConfiguration.class 는 application.yml 파일에서 사용하고 있는 DataSource를 프록시한 객체로 만들어주는 역할을 하는 클래스.
- DataJpaTest 애노테이션을 까보면 @AutoConfigureTestDatabase이 붙어있다. 
  -  이 어노테이션은 기존에 설정되어 있는 데이터베이스 설정 대신 테스트용 인메모리를 강제로 사용하는 Replace.ANY로 설정이 되어 있다. 따라서 이 값은 Replace.NONE 으로 변경해야 저희가 의도했던 대로 동작한다.
- @Import(P6spyLogMessageFormatConfiguration.class) 는 앞서 p6spy Config 세팅을 import 시켜준다.



```java
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@ImportAutoConfiguration
@PropertyMapping("spring.test.database")
public @interface AutoConfigureTestDatabase {

    /**
     * Determines what type of existing DataSource beans can be replaced.
     * @return the type of existing DataSource to replace
     */
    @PropertyMapping(skip = SkipPropertyMapping.ON_DEFAULT_VALUE)
    Replace replace() default Replace.ANY;  // <-- 여기

...
```



이제부터 @DataJpaTest 대신 @CustomDataJpaTest 를 사용하면 원했던 대로 p6spy를 사용할 수 있다.



### CallStack 커스텀

JPA를 사용하다보면 어디에서 호출한 것인지 알기 힘든데 어디서 호출했는지 알 수 있는 방법이 있다.

```java
public class CustomP6spySqlFormat implements MessageFormattingStrategy {
    
    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {
        sql = formatSql(category, sql);
        Date currentDate = new Date();

        SimpleDateFormat format1 = new SimpleDateFormat("yy.MM.dd HH:mm:ss");

        return format1.format(currentDate) + " | "+ "OperationTime : "+ elapsed + "ms" + sql
            + createStack(connectionId, elapsed); // 여기 추가
    }

    private String formatSql(String category,String sql) {
        if(sql ==null || sql.trim().equals("")) return sql;

        // Only format Statement, distinguish DDL And DML
        if (Category.STATEMENT.getName().equals(category)) {
            String tmpsql = sql.trim().toLowerCase(Locale.ROOT);
            if(tmpsql.startsWith("create") || tmpsql.startsWith("alter") || tmpsql.startsWith("comment")) {
                sql = FormatStyle.DDL.getFormatter().format(sql);
            }else {
                sql = FormatStyle.BASIC.getFormatter().format(sql);
            }
            sql = "|\nHeFormatSql(P6Spy sql,Hibernate format):"+ sql;
        }

        return sql;
    }

    // 표기에 허용되지 않는 filter
    private List<String> DENIED_FILTER = Arrays.asList("Test1"
        , this.getClass().getSimpleName());
    
    // 표기에 허용되는 filter
    private String ALLOW_FILTER = "com.test";
    
    // stack 콘솔 표기
    private String createStack(int connectionId, long elapsed) {
        Stack<String> callStack = new Stack<>();
        StackTraceElement[] stackTrace = new Throwable().getStackTrace();

        for (StackTraceElement stackTraceElement : stackTrace) {
            String trace = stackTraceElement.toString();

            // trace 항목을 보고 내게 맞는 것만 필터
            if (trace.startsWith("com.ys") && !isFilterDenied(trace)) {
                callStack.push(trace);
            }
        }

        StringBuffer sb = new StringBuffer();
        int order = 1;
        while (callStack.size() != 0) {
            sb.append("\n\t\t" + (order++) + "." + callStack.pop());
        }

        return new StringBuffer().append("\n\n\tConnection ID:").append(connectionId)
            .append(" | Excution Time:").append(elapsed).append(" ms\n")
            .append("\n\tExcution Time:").append(elapsed).append(" ms\n")
            .append("\n\tCall Stack :").append(sb).append("\n")
            .append("\n--------------------------------------")
            .toString();
    }

    // 포함하고 싶지 않은 필터들로 검사 List<String> DENIED_FILTER  를 이용
    private boolean isFilterDenied(String trace) {
        return DENIED_FILTER.contains(trace);
    }

}
```

* 핵심은 createStack()으로 메시지를 만드는데  `if (trace.startsWith("com.ys") && !isFilterDenied(trace)) {` 를 이용하여 원하는 로그만 추가한다. 



실행 결과

```
--------------------------------------
2022-12-17 18:38:42.694  INFO 62604 --- [           main] p6spy                                    : 22.12.17 18:38:42 | OperationTime : 3ms|
HeFormatSql(P6Spy sql,Hibernate format):
    insert 
    into
        members
        (id, created_at, modified_at, address, age, description, name, nick_name) 
    values
        (default, '2022-12-17T18:38:42.626+0900', '2022-12-17T18:38:42.626+0900', '서울시', 10, NULL, 'name', 'ncik')

	Connection ID:4 | Excution Time:3 ms

	Excution Time:3 ms

	Call Stack :
		1.com.ys.jpa.ConstructorDiTest.test(ConstructorDiTest.java:27)
		2.com.ys.jpa.common.CustomP6spySqlFormat.formatMessage(CustomP6spySqlFormat.java:21)
		3.com.ys.jpa.common.CustomP6spySqlFormat.createStack(CustomP6spySqlFormat.java:51)

--------------------------------------
2022-12-17 18:38:42.730  INFO 62604 --- [           main] p6spy                                    : 22.12.17 18:38:42 | OperationTime : 0ms

	Connection ID:4 | Excution Time:0 ms

	Excution Time:0 ms

	Call Stack :
		1.com.ys.jpa.ConstructorDiTest.test(ConstructorDiTest.java:27)
		2.com.ys.jpa.common.CustomP6spySqlFormat.formatMessage(CustomP6spySqlFormat.java:21)
		3.com.ys.jpa.common.CustomP6spySqlFormat.createStack(CustomP6spySqlFormat.java:51)

--------------------------------------
```

* CallStack을 알 수 있다. 



## P6Spy 원리와 커스텀



### p6spy의 쿼리 캡처 과정

<img src="https://blog.kakaocdn.net/dn/s3Etd/btrTQ1Zymer/3SJ0kaxofWk9tmcRxfA6cK/img.png" width= 700 height = 500>

1. DataSource를 래핑하여 프록시를 만든다.
2. 쿼리가 발생하여 JDBC가 ResultSet 을 반환하면 이를 만들어둔 프록시가 복사하여 가로챈다.
3. 내부적으로 ResultSet의 정보를 분석하고 p6spy의 옵션을 적용한다.
4. Slf4j 를 사용해 로깅한다.



### logMessageFormat - 메시지 포맷팅 

p6spy가 사용하는 포맷은 다음 3가지 종류가 있다.

- SingleLineFormat : 기본 설정이 되어있는 Format
- CustomLineFormat : 커스터마이징 할 포메터가 아니고 SingleLineFormat을 손본 Format
- MultiLineFormat : 한 줄로 쭉 늘어진 log를 쿼리문 두 줄만 밑으로 내려주는 Format



### p6spy의 Format 선택 과정 

P6SpyProperties.java 클래스. p6spy의 Default 설정 파일이다. 

이 클래스파일에서 p6spy Default 설정값을 알 수 있다.

```java
package com.github.gavlyukovskiy.boot.jdbc.decorator.p6spy;

import java.util.regex.Pattern;

@Getter
@Setter
public class P6SpyProperties {

    // 로깅 JDBC 이벤트를 활성화여부
    private boolean enableLogging = true;
    
    // 여러 줄 출력을 활성화
    private boolean multiline = true;
    
    // 로깅 쿼리에 사용할 로깅 - 디폴트로 SLF4j가 사용된다. 
    private P6SpyLogging logging = P6SpyLogging.SLF4J;
   
    // 사용할 로그 파일의 이름(logging=file인 경우에만). 
    private String logFile = "spy.log";
    
    // 용자 정의 로그 형식
    private String logFormat;

    // 관련 속성 추적
    private P6SpyTracing tracing = new P6SpyTracing();

    /**
     * 사용할 클래스 파일(logging=custom인 경우에만)
     * 이 클래스는 구현해야한다 {@link com.p6spy.engine.spy.appender.FormattedLogger}
     */
    private Class<? extends FormattedLogger> customAppenderClass;

    // 로그 필터링 관련 속성
    private P6SpyLogFilter logFilter = new P6SpyLogFilter();

    public enum P6SpyLogging {
        SYSOUT,
        SLF4J,
        FILE,
        CUSTOM
    }

    @Getter
    @Setter
    public static class P6SpyTracing {
        
        //유효 SQL 문자열('?'이 실제 값으로 대체됨)을 추적 시스템에 보고. 이 설정은 로깅 메시지에 영향을 주지 않는다
        private boolean includeParameterValues = true;
    }

    @Getter
    @Setter
    public static class P6SpyLogFilter {
        // 정규식 패턴을 사용하여 로그 메시지를 필터링. 일치하는 메시지만 기록.
        private Pattern pattern;
    }
}
```



 다시 돌아가서 PsSpyConfiguration.java** 에서 실제로 어떤 방식으로 Format을 선택하는지 다음 코드로 알 수 있다.

```java
if (!initialP6SpyOptions.containsKey("logMessageFormat")) {
    if (p6spy.getLogFormat() != null) {
        System.setProperty("p6spy.config.logMessageFormat", "com.p6spy.engine.spy.appender.CustomLineFormat");
        System.setProperty("p6spy.config.customLogMessageFormat", p6spy.getLogFormat());
    }
    else if (p6spy.isMultiline()) {
        System.setProperty("p6spy.config.logMessageFormat", "com.p6spy.engine.spy.appender.MultiLineFormat");
    }
}
```

Default 설정에 따르면 getLogFormat 은 null이므로 else if 문을 타면서 MultiLineFormat 을 선택

MultiLineFormat을 보시면 formatMessage를 재정의 하면서 어떤 포맷으로 출력을 찍어내는지 확인할 수 있다. 결론적으로 이 코드를 수정하면 우리가 원하는 대로 콘솔에 찍어낼 수 있다

```java
package com.p6spy.engine.spy.appender;

public class MultiLineFormat implements MessageFormattingStrategy {

  @Override
  public String formatMessage(final int connectionId, final String now, final long elapsed, final String category, final String prepared, final String sql, final String url) {
    return "#" + now + " | took " + elapsed + "ms | " + category + " | connection " + connectionId + "| url " + url + "\n" + prepared + "\n" + sql +";";
  }
  
}
```



### 참조

* https://backtony.github.io/spring/2021-08-13-spring-log-1/

* https://lemontia.tistory.com/1058

* https://jessyt.tistory.com/27#recentEntries < Querydsl도 자세하게 알 수 있다.

* https://backtony.github.io/spring/2021-08-13-spring-log-1/