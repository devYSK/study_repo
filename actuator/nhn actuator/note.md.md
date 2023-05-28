# Nhn Actuator

* docs : https://docs.spring.io/spring-boot/docs/current/actuator-api/htmlsingle/
* https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html#actuator

nhn : http://forward.nhnent.com/hands-on-labs/java.spring-boot-actuator/04-endpoint.html

* http://forward.nhnent.com/hands-on-labs/java.spring-boot-actuator/03-configuration.html



# 기본 웹 엔드포인트 확인

http://localhost:8080/actuator 에 접속해서 웹 환경에서 기본으로 제공되는 엔드포인트를 확인해 봅니다.

```json
{
  "_links": {
    "self": {
      "href": "http://localhost:8080/actuator",
      "templated": false
    },
    "health-path": {
      "href": "http://localhost:8080/actuator/health/{*path}",
      "templated": true
    },
    "health": {
      "href": "http://localhost:8080/actuator/health",
      "templated": false
    }
  }
}
```

* 기본적으로 Web 환경에는 `health`, `info` 2가지의 엔드포인트를 제공합니다.

## 주요 웹 엔드포인트 구성

```properties
management.server.port=
management.server.servlet.context-path=

management.endpoints.jmx.exposure.include=health,info
management.endpoints.web.exposure.exclude=
management.endpoints.web.base-path=/actuator
management.endpoints.web.path-mapping=
```

- 엔드포인트 서버 포트. 설정하지 않으면 기본 웹 애플리케이션 포트와 동일(default:8080)
- 엔드포인트 context-path 는 기본적으로 빈 값
- 기본 포함되는 엔드포인트 : `health`, `info`
- 제외 엔드포인드는 기본적으로 없음
- 엔드포인트 기본 경로 : `/actuator`
  - `management.server.servlet.context-path` 경로 하위에 상대경로로 설정
- 엔드포인트 경로 재매핑은 기본적으로 없음

### 모든 웹 엔드포인트 노출 구성

액추에이터의 모든 웹 엔드포인트에 접근할 수 있게 **직접** 설정해 보겠습니다. 아래에 스프링 부트 레퍼런스 문서 일부를 힌트로 제공하니 참고하시길 바랍니다.

아래의 기술별 포함 및 제외 속성을 사용하여 노출되는 엔드포인트를 변경할 수 있습니다.

| Property                                    | Default          |
| ------------------------------------------- | ---------------- |
| `management.endpoints.jmx.exposure.exclude` |                  |
| `management.endpoints.jmx.exposure.include` | `*`              |
| `management.endpoints.web.exposure.exclude` |                  |
| `management.endpoints.web.exposure.include` | `info`, `health` |

include 속성은 노출되는 엔드포인트의 ID 목록을 나열합니다. exclude 속성은 노출되지 않아야 할 엔드포인트의 ID 목록을 나열합니다. exclude 속성은 include 속성보다 우선시됩니다. include와 exclude 속성은 엔드포인트 ID 목록으로 구성할 수 있습니다.

예를 들어, JMX를 통해 모든 엔드포인트 노출을 중지하고 health와 info 엔드포인트만 노출하려면 다음 속성을 사용하세요:

```properties
management.endpoints.jmx.exposure.include=health,info
```

`*` 는 모든 엔드포인트를 선택하는 데 사용될 수 있습니다. 

예를 들어, env와 beans 엔드포인트를 제외한 모든 것을 HTTP를 통해 노출하려면 다음 속성을 사용하세요:

```properties
management.endpoints.web.exposure.include=* 
management.endpoints.web.exposure.exclude=env,beans
```

### 모든 웹 엔드포인트 구성

property 파일에 다음과 같이 설정합니다. 

```yml
management:
  endpoints:
    web:
      exposure:
        include: "*"
```

* yml은 "" 문자로 묶어야 합니다. 

또는

```properties
management.endpoints.web.exposure.include=*
```

애플리케이션을 재시작한 후http://localhost:8080/actuator 를 확인하면 아래와 같은 결과를 확인할 수 있습니다.

```json
{
    "_links": {
        "self": {
            "href": "http://localhost:8080/actuator",
            "templated": false
        },
        "auditevents": {
            "href": "http://localhost:8080/actuator/auditevents",
            "templated": false
        },
        "beans": {
            "href": "http://localhost:8080/actuator/beans",
            "templated": false
        },
        "health": {
            "href": "http://localhost:8080/actuator/health",
            "templated": false
        },
        ..// 이하 엄청 많이 나옴.
    }
}
```

## 웹 엔드포인트 활성화 구성

엔드포인트를 노출시킨다고 해서 모두 노출되는 것은 아닙니다. 노출 구성 이전에 해당 엔드포인트가 **활성화** 되어 있어야 합니다.

하지만 기본적으로 대부분의 엔드포인트들이 활성화 되어 있기 때문에 노출 설정 만으로도 확인할 수 있습니다.

```properties
# 모든 엔드포인트들을 전체 활성화시키거나 비활성화. 비어 있으면 각 엔드포인트 활성화 설정에 위임
management.endpoints.enabled-by-default=
management.endpoint.beans.enabled=true
management.endpoint.conditions.enabled=true
management.endpoint.configprops.enabled=true
management.endpoint.env.enabled=true
management.endpoint.health.enabled=true
management.endpoint.logfile.enabled=true
management.endpoint.loggers.enabled=true
management.endpoint.mappings.enabled=true
management.endpoint.prometheus.enabled=true
# shutdown 엔드포인트는 기본이 비활성화
management.endpoint.shutdown.enabled=false
```

* `shutdown` 엔드포인트는 애플리케이션을 종료시킬 수 있기 때문에 기본이 비활성화. 만약 사용할 경우 보안적으로 권한이 요구되는 설정이 필수로 해야합니다.



# 주요 엔드포인트

## beans

애플리케이션의 모든 Spring Bean의 전체 목록을 표시

- json을 직접 눈으로 보기에는 너무 정보가 많습니다.
- http://localhost:8080/actuator/beans

`beans` 관련 구성

```properties
management.endpoint.beans.cache.time-to-live=0ms
management.endpoint.beans.enabled=true
```

## health

애플리케이션의 상태 정보를 표시. **필수적으로 사용하는 엔드포인트**

- 일반적으로 LoadBalancer(ex:L4)에서 해당 애플리케이션 인스턴스의 상태 정보를 확인합니다.
- http://localhost:8080/actuator/health

기본 구성 상테애서 http://localhost:8080/actuator/health 접근하게 되면 아래와 같이 응답합니다.

```
{
    "status": "UP"
}
```

### 상세 조회 옵션

원래 `health` 엔드포인트는 여러가지 상태 정보를 조합해서 애플리케이션 상태를 나타냅니다. 예를 들면

- 장비 디스크 용량
- DB, Redis, Elasticsearch, Rabbitmq 등 의존하는 인프라의 상태
- 사용자 정의 Health Indicator 상태

하지만 현재는 그냥 `UP` 만 나올 뿐이네요. 이는 `health` 엔드포인트 기본 구성 때문입니다.

```properties
# health 상세 조회 옵션 : *never|when-authorized|always
management.endpoint.health.show-details=never
```

* never | when-authorized | always
* `never` 옵션이기 때문에 노출이 되지 않습니다. 위 속성 값을 `always` 로 변경합니다.

```properties
management.endpoint.health.show-details=never
```

다시 애플리케이션을 기동하고 http://localhost:8080/actuator/health 에 접근해 봅시다.

```json
// 20230527163631
// http://localhost:8080/actuator/health

{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP",
      "details": {
        "database": "H2",
        "validationQuery": "isValid()"
      }
    },
    "diskSpace": {
      "status": "UP",
      "details": {
        "total": 994662584320,
        "free": 630073815040,
        "threshold": 10485760,
        "path": "/Users/ysk/study/study_repo/spring-actuator/actuator/.",
        "exists": true
      }
    },
    "ping": {
      "status": "UP"
    }
  }
}
```

* `when-authorized` 옵션은 조회 권한이 있는 경우에만 상세롤 노출하는 옵션입니다.
*  `management.endpoint.health.roles` 속성으로 추가된 권한들에 한해서 상세 보기가 가능합니다.

### 인프라 의존성과 `health`

`details` 하위에 있는 모든 항목의 상태가 **UP** 여야지 애플리케이션 상태가 **UP** 가 됩니다.

- 만약 단 하나의 항목이라도 **DOWN** 상태라면 애플리케이션의 상태도 **DOWN** 이 됩니다.
- **UP** 상태일 시 응답 HTTP 상태코드는 200 입니다.
- **DOWN** 상태일 시 응답 HTTP 상태코드는 503 (서비스를 사용할 수 없음) 입니다.

#### 모든 인프라의 상태에 의존한다면?

현재 개발 중인 애플리케이션이 MySql, Redis, Elasticsearch, Rabbitmq 에 의존한다고 가정해 보겠습니다.

그 상황에서 `health` 엔드포인트를 확인하면 아래와 같은 출력될 것 입니다.

```properties
{
    "status" : "UP",
    "details" : {
        "diskSpace" : {
            "status" : "UP"
        },
        "db" : {
            "status" : "UP"
        },
        "redis" : {
            "status" : "UP"
        },
        "elasticsearch" : {
            "status" : "UP"
        },
        "rabbitmq" : {
            "status" : "UP"
        }
    }
}
```

- 위 출력은 실제 응답에 비해 간소화 됐습니다.

해당 애플리케이션은 Redis 를 캐시 용도로 사용하는데, 만약 Redis 인프라가 shutdown 되거나 연결된 네트워크에 이상이 생기면, 즉 Redis 상태가 **DOWN** 된다면 애플리케이션 또한 **DOWN** 상태가 될 것 입니다.

```json
{
    "status" : "DOWN",
    "details" : {
        "diskSpace" : {
            "status" : "UP"
        },
        "db" : {
            "status" : "UP"
        },
        "redis" : {
            "status" : "DOWN"
        },
        "elasticsearch" : {
            "status" : "UP"
        },
        "rabbitmq" : {
            "status" : "UP"
        }
    }
}
```

- Redis 가 다운된 경우 `health` 엔드포인트 응답

장애가 전파되면서 (Redis -> Application) 해당 애플리케이션은 사용할 수 없게 될 것 입니다. 하지만 Redis 가 **DOWN** 됐다고 해서 애플리케이션이 DOWN 되지 않고 **가용성을 확보해야** 할 것 입니다.

그렇다면 `health` 설정을 어떻게 해야할까요?

```properties
management.health.redis.enabled=false
```

- 바로 위와 같이 `health` 엔드포인트에 Redis 상태가 포함되지 않게 비활성화 시키면 됩니다.

일반적으로 DiskSpace 나 DB 가 DOWN 되면 애플리케이션은 정상적인 서비스가 불가능 할 것 입니다.

 하지만 Redis, Elasticsearch, Rabbitmq 와 같은 인프라가 DOWN 될지라도 서비스는 가용성을 확보해야할 것 같습니다.

그렇다면 아래와 같이 설정을 하는 것을 추천합니다.

```properties
management.health.redis.enabled=false
management.health.elasticsearch.enabled=false
management.health.rabbit.enabled=false
```

위 구성에 따른 `health` 엔드포인트 응답

```json
{
    "status" : "UP",
    "details" : {
        "diskSpace" : {
            "status" : "UP"
        },
        "db" : {
            "status" : "UP"
        }
    }
}
```

### 로드밸런서와 `health`

스프링 부트 애플리케이션으로 운영하는 경우 상태 체크는 일반적으로 `health` 엔드포인트를 사용하게 됩니다.

기동된 애플리케이션의 인스턴스 상태를 확인하는 책임은

- 전통적으로는 로드밸런서(ex:L4), 클라우드 환경에서는 서비스 레지스트리(ex:NetflixEureka, Consul 등)가 담당합니다.

로드밸런서는 서비스의 고가용성과 확장성 그리고 **무중단 배포** 를 위해 사용합니다. 여기에서는 이 **무중단 배포** 에 대한 이야기를 해봅시다.



## conditions

스프링 부트의 자동설정(`*AutoConfiguration`)과 개발자가 직접 구성한 설정에서 평가된 조건(`@Conditional`)에 관한 정보.

평가가 성공되면 해당 설정이 로딩되고 실패하면 무시합니다.

스프링 부트는 자동설정의 평가조건(`@Conditional`)에 따라서 설정이 로딩되거나 무시합니다.

- http://localhost:8080/actuator/conditions
- 평가가 성공했으면 `positiveMatches` 속성의 항목, 실패했으면 `negativeMatches` 속성의 항목이 됩니다.
- 각 평가에 대한 성공/실패 메시지가 표시됩니다.
  - 성공 예시 : `@ConditionalOnEnabledHealthIndicator management.health.defaults.enabled is considered true`
  - 실패 예시 : `@ConditionalOnClass did not find required class 'com.google.gson.Gson'`

## configprops

`@ConfigurationProperties` 에 대한 정보가 표시.

- http://localhost:8080/actuator/configprops

* `management.endpoint.configprops.keys-to-sanitize` 속성을 통해서 민감한 속성은 가릴 수 있습니다. 
* 예를 들면 비밀번호나 토큰과 같은 정보는 보안 상 노출 시키는 것이 위험하기 때문입니다.

```json
{
    "spring.datasource-org.springframework.boot.autoconfigure.jdbc.DataSourceProperties": {
        "prefix": "spring.datasource",
        "properties": {
            "password": "******",   // !!
            "driverClassName": "com.mysql.jdbc.Driver",
            "url": "jdbc:mysql://1.1.1.1:3306/db",
            "username": "username"
        }
    }
}
```

## env

스프링의 모든 환경변수 정보를 표시.

- http://localhost:8080/actuator/env
- 애플리케이션 변수들(`application.properties`) 노출
- OS, JVM 환경변수들 노출

`management.endpoint.env.keys-to-sanitize` 속성을 통해서 민감한 속성은 가릴 수 있습니다. 

예를 들면 비밀번호나 토큰과 같은 정보는 보안 상 노출 시키는 것이 위험하기 때문입니다.

```json
{
    "spring.datasource.password": {
        "value": "******"
    }
}
```

## info

임의의 애플리케이션 정보를 표시.

- http://localhost:8080/actuator/info
- 기본적으로 빈 Json Object 반환

기본적으로 `info` 엔드포인트는 내용이 없습니다. 개발자가 구성하길 바라는 것입니다.

 `info` 엔드포인트에 내용을 표현하는 방법 2가지(+2)를 알아보겠습니다.

### info.** 환경변수

환경 변수에 `info.` 로 시작하는 변수를 추가합니다. 일반적으로 `application.properties` 에 추가합니다.

```properties
info.app.name=actuator-levle1
info.app.version=1.0
info.app.corporation=ysk
```

- http://localhost:8080/actuator/info 로 확인해보겠습니다.

```
{
    "app": {
        "name": "actuator-levle1",
        "version": "1.0",
        "corporation": "ysk"
    }
}
```

### 빌드 정보

gradle

```groovy
springBoot {
    buildInfo()
}
```

* 위를 추가

이렇게 되면 빌드 산출물 jar 파일 내에 `META-INF/build-info.properties` 가 생성되며, 이 정보를 바탕으로 `info` 엔드포인트가 표현됩니다.

하지만 IDE 에서 실행할 경우 확인되지 않습니다. IDE 에서 부트 애플리케이션을 실행시키면 메이븐, 그레이들과 같은 툴로 빌드하는 것이 아니라 IDE 자체에서 빌드하기 때문에 `META-INF/build-info.properties` 이 생성되지 않습니다. 아래 과정을 따라서 해보시길 바랍니다.

1. 터미널 도구를 이용해서 프로젝트 root로 이동.
2. macOs: `./mvnw clean package`, windows: `mvnw.bat clean package`
3. `java -jar target/*.jar` 로 애플리케이션 실행.

> - 이미 기동된 애플리케이션이 있으면 8080 포트가 충돌하므로, 기존 애플리케이션은 종료하십시오. 아마도 IDE Run 창에 실행하고 있을 것 입니다.

```json
// 20230527163407
// http://localhost:8080/actuator/info

{
  "build": {
    "artifact": "actuator",
    "name": "actuator",
    "time": "2023-05-27T07:33:15.521Z",
    "version": "0.0.1-SNAPSHOT",
    "group": "com.ys"
  }
}
```

그 외 방법

- [InfoContributor 구현](https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-endpoints.html#production-ready-application-info-custom)
- [Git Commit 정보](https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-endpoints.html#production-ready-application-info-git)

## logfile

로그 파일의 내용을 반환.

- 현재 웹 애플리케이션 상태에서는 노출되지 않습니다. 아래 2가지 조건을 만족해야합니다. 

- `logging.file` 또는 `logging.path`라는 부트 속성을 이용해서 로그 파일 출력이 활성화

  - 만약 다른 방법으로 로그 파일을 관리한다면 `management.endpoint.logfile.external-file` 속성으로 가능합니다.

  - 웹 애플리케이션

현재 샘플 애플리케이션은 웹 애플리케이션 이긴 하지만 로그파일 출력 설정이 되어 있지 않기 때문에 노출되지 않습니다. `application.properties` 에 `logging.file` 속성을 추가한 후에 확인할 수 있습니다.

```properties
logging.file=target/application.log
```

위와 같이 설정하고 애플리케이션을 재가동 후 아래 엔드포인트에 접근하면 로그를 확인할 수 있습니다. 추가적으로 [HTTP range requests](https://developer.mozilla.org/ko/docs/Web/HTTP/Range_requests) 를 통해서 로그의 특정 범위만 요청하거나 분할 요청할 수 있습니다.

- http://localhost:8080/actuator/logfile

## loggers

애플리케이션의 Logger 구성을 표시하거나 *변경*.

- http://localhost:8080/actuator/loggers

### `loggers` 변경

**1. 기본상태**

```
GET http://localhost:8080/actuator/loggers/com.nhnent.forward.springbootactuator
{
    "configuredLevel": null,
    "effectiveLevel": "INFO"
}
```

**2. DEBUG로 변경**

```
POST http://localhost:8080/actuator/loggers/com.nhnent.forward.springbootactuator

{
    "configuredLevel": "DEBUG"
}
```

- `com.nhnent.forward.springbootactuator` 에 대한 로그 레벨을 `DEBUG` 로 변경

**3. DEBUG로 변경 확인**

GET http://localhost:8080/actuator/loggers/com.nhnent.forward.springbootactuator

```
{
    "configuredLevel": "DEBUG",
    "effectiveLevel": "DEBUG"
}
```

## `threaddump`

스레드 덤프 수행.

- http://localhost:8080/actuator/threaddump
- 스레드 덤프 파일을 생성하는 것이 아니라 스레드 덤프 결과를 json 으로 반환합니다.

## `heapdump`

GZip으로 압축된 hprof 힙 덤프 파일을 다운로드.

- http://localhost:8080/actuator/heapdump
- 웹 애플리케이션 경우에만 사용 가능.

Java 애플리케이션에서 **STW(Stop The world)** 가 발생하므로 운영 중인 서비스에서는 사용하지 않는 것이 좋습니다.

[Eclipse Mat](https://www.eclipse.org/mat/) 같은 JVM 메모리 분석 도구를 이용해서 해당 내용을 분석할 수 있다



## `metrics`

현재 애플리케이션의 각종 지표(metrics)정보를 표시.

- http://localhost:8080/actuator/metrics
- 애플리케이션에 대한 지표 정보를 나열.
  - 특정 지표에 대해서 단 건 조회 가능합니다. 아래 프로세스 CPU 사용률 확인 참고하세요.
- 각종 의존성 라이브러리 추가에 따라서 지표도 추가됩니다.
  - 만약 DB를 사용한다면 ConnectionPool의 각종 DB 커넥션 수의 정보도 지표로 조회 가능합니다.

```json
{
    "names": [
        "jvm.memory.max",
        "jvm.gc.pause",
        "http.server.requests",
        "process.files.max",
        "jvm.gc.memory.promoted",
        "tomcat.cache.hit",
        "system.load.average.1m",
        "tomcat.cache.access",
        "jvm.memory.used",
        "jvm.gc.max.data.size",
        "jvm.memory.committed",
        "system.cpu.count",
        "process.cpu.usage",
        "#중략"
    ]
}
```

### 프로세스 CPU 사용률 확인

- http://localhost:8080/actuator/metrics/process.cpu.usage

```json
{
    "name": "process.cpu.usage",
    "description": "The \"recent cpu usage\" for the Java Virtual Machine process",
    "baseUnit": null,
    "measurements": [
        {
            "statistic": "VALUE",
            "value": 0.011448519312787644
        }
    ],
    "availableTags": []
}
```



## `httptrace`

최근 100개 HTTP 요청을 반환.

- http://localhost:8080/actuator/httptrace
- 응답 모델이 매우 복잡하기 때문에 직접 호출해서 확인해 봅시다.

`httptrace` 관련 구성

```properties
management.trace.http.include= request-headers,response-headers,cookies,errors
```

- 노출 시 포함 시킬 Trace 관련 요소들 지정 가능합니다.

## `mappings`

모든 `@RequestMapping` 경로를 표시.

- http://localhost:8080/actuator/mappings

## `shutdown`

애플리케이션을 정상적으로(gracefully) 종료.

- `POST http://localhost:8080/actuator/shutdown`
- GET 명령으로는 실행되지 않습니다.

`shutdown` 관련 기본 구성

```
management.endpoint.shutdown.enabled=false
```

### `shutdown` 활성화

**종료** 라는 무게만큼 기본적으로 비활성화 되어 있습니다. 먼저 활성화 시켜보겠습니다.

```properties
management.endpoint.shutdown.enabled=true
```

### `shutdown` 수행

- http://localhost:8080/actuator/shutdown

하지만 위 페이지로 접근해도 405 (Method Not Allowed) 오류만 발생합니다.

기본적으로 shutdown 은 행위(동사) 리소스이기 때문에 POST 로 호출해야 합니다.

```json
$ http POST localhost:8080/actuator/shutdown
HTTP/1.1 200
Content-Type: application/vnd.spring-boot.actuator.v2+json;charset=UTF-8
Date: Thu, 25 Oct 2018 08:59:50 GMT
Transfer-Encoding: chunked

{
    "message": "Shutting down, bye..."
}
```

### Please No `kill -9 {PID}`

반적으로 스프링 부트 애플리케이션을 기동할 시에는 java -jar 명령어나 docker 와 같은 컨테이너에 의존합니다.

하지만 `kill` 과 같은 명령어로 애플리케이션을 종료하는 경우 실행 중인 스레드가 정상적으로 종료되지 않고 강제로 종료되기 때문에 이슈가 발생할 가능성이 있습니다. 그래서 애플리케이션을 종료할 시에는 정상적으로(Gracefully) 종료하는 것이 중요합니다. 

이를 위해서 꼭 `shutdown` 과 같은 방식을 이용해서 정상적으로 종료할 수 있는 환경을 구축하기를 바랍니다.

> 전통적인 방식으로 linux에 직접 스프링 부트 애플리케이션을 설치할 시 OS의 서비스로 등록하는 것도 괜찮은 운영 전략입니다.
>
> - https://docs.spring.io/spring-boot/docs/2.0.6.RELEASE/reference/html/deployment-install.html

### `shutdown` 과 보안과 엔드포인트

`shutdown` 엔드포인트는 보안이 매우 중요합니다. 만약 노출한다면 꼭 보안을 필수로 설정해야합니다.

보안에 관한 세부 내용은 추후 제공될 다음 단계의 Hands-On Labs를 통해서 알아보도록 하며, 여기에서는 간단한 보안 설정만 알아보겠습니다.

*아래 코드 내용은 실습하지 않고 그냥 읽어봅시다*

```java
@Configuration
public class ActuatorSecurity extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.requestMatcher(EndpointRequest.toAnyEndpoint())
          .authorizeRequests()    // 모든 액추에이터 앤드포인트에     
          .anyRequest().hasRole("ENDPOINT_ADMIN") // ENDPOINT_ADMIN 권한일 경우에만 접근 가능
          .and()
       .httpBasic();
    }
}
```