[toc]

# Spring Cloud Config

- ﻿﻿분산 시스템에서 서버, 클라이언트 구성에 필요한 설정 정보(application.ym)를 외부 시스템에서 관리
- ﻿﻿하나의 중앙화 된 저장소에서 구성요소 관리 가능
- ﻿﻿각 서비스를 다시 빌드하지 않고, 바로 적응 가능 
- ﻿﻿애플리케이션 배포 파이프라인을 통해 DEV- UAT- PROD 환경에 맞는 구성 정보 사용

git, secureVault, File 에 저장 가능하다.

gradle

```groovy
dependencies {
    implementation 'org.springframework.cloud:spring-cloud-config-server'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    implementation 'org.springframework.boot:spring-boot-starter-actuator'
    implementation 'org.springframework.boot:spring-boot-starter-validation'
    // spring-cloud-starter-bus-amqp와 spring-cloud-starter-netflix-eureka-client는 주석 처리되어 제외됨
    implementation 'org.springframework.cloud:spring-cloud-starter-bootstrap'
}
```

```java
@SpringBootApplication
@EnableConfigServer
public class ConfigServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigServiceApplication.class, args);
    }

}
```



* 레퍼런스 : https://docs.spring.io/spring-cloud-config/docs/current/reference/html/

## Spring Cloud Config 설정 파일 우선 순위

설정 파일은 크게 다음의 위치에 존재할 수 있으며 다음의 순서대로 읽어진다. 나중에 읽어지는 것이 우선순위가 높다.

프로젝트의 application.yaml
설정 저장소의 application.yaml
프로젝트의 application-{profile}.yaml
설정 저장소의 {application name}/{application name}-{profile}

```
로컬의 appliation.yaml, application-local.yaml에 있고, 설정 저장소의 application.yaml, hello/hello-local.yaml에도 있다면 다음의 순서대로 읽어진다.

프로젝트의 application.yaml
설정 저장소의 application.yaml
프로젝트 application-local.yaml
설정 저장소의 hello/hello-local.yaml
```



## Spring Cloud Config 클론

스프링 클라우드 공식 문서에서는 이미 이와 관련된 내용을 Warning으로 다루고 있다. 아래의 내용을 보면 OS에 따라서 주기적으로 임시 디렉토리가 삭제될 수 있으니, basedir을 설정



설정 파일을 클론받는 위치를 직접 지정해서 임시 디렉토리에 받지  않는것이 좋다.

```yaml
spring:
  cloud:
    config:
      server:
        git:
          default-label: main
          uri: https://github.com/repo/spring-cloud-config
          search-paths: test-cloud-config-file/**
          basedir: ./repository
```



##  SpringCloudConfig 프로퍼티 암호화

> basic auth를 사용해서 막아도 된다. 

spring cloud config 서버의 `yml`파일의 맨 밑에 다음과 같이 추가한다.

```
encrypt:
  key: my-secret # 사용할 암호
```

정보를 암호화할 키를 넣는다. 사용할 암호를 임의로 지정해 적용하면 된다.

암호화 하고 싶은 정보를 `Body`에 담아 보내면 결과를 응답해준다.

해당응답 결과를  property에 넣는다.

```yaml
jwt:
  secret: "{cipher}aadfc7c7841c2ef32e1abcc4e6860755f22fb5be06cde48106ae95029413d794"
```

* 쌍따옴표 안에 `{cipher}`를 넣고 암호화된 문자를 넣는다.

따라서 클라이언트에서 해당 정보를 복호화해 사용해야 한다.

환경변수에 `ENCRYPT_KEY=my-secret` 값을 다음과 같이 적용한다. 암호화 관련 설정에서 사용한 비밀키를 넣으면 된다.

## yml 읽어오는법

![image-20240303013613670](./images//image-20240303013613670.png)

3가지가 있다.

* yml
* -name
* -name-profile

```
ecommerce-dev.yml 
ecommerce-uat.yml 
ecommerce-prod.yml
```

없으면 default를 가져간다.

bootstrap.yml을 다음과같이 원하는 프로파일로 수정한다. 

```yaml
spring:
  cloud:
    config:
      uri: http://127.0.0.1:8888
      name: user-service
  profiles:
    active: dev
```

환경변수를 사용하여 참조하고 주입 가능하다

```yaml
spring:
  cloud:
    config:
      uri: http://127.0.0.1:8888
      name: user-service
  profiles:
    active: ${SPRING_PROFILES_ACTIVE:default}
```





## 1. local git repository 사용

```shell
mkdir git-local-repo
cd /Users/ysk/temp/git-local-repo

git init
git add ecommerce.yml
git commit -m 'upload an application yaml file'
```

```yaml
# in ecommerce.yml

token:
  expiration_time: 86400000
  secret: ysk-microservice-practice-jwt-secret-key # ysk-microservice-practice-jwt-secret-key
  
gateway:
	ip: http://192.168.0.4
```

application.yml 에 설정

```yaml
spring:
  application:
    name: config-service
  cloud:
    config:
      server:
        git: #default
          uri: file:///Users/ysk/temp/git-local-repo
          default-label: master
```

접속시

```
http://localhost:8888/ecommerce/default

서버:포트번호/yml파일명/profile 

* profile : spring profile
```

## 2. remote git repository 사용

```yaml
spring:
  application:
    name: config-service
  cloud:
    config:
      server:
        git: #default
	        uri: https://github.com/devysk/spring-cloud-config
```



## 3. private remote git repository 사용

```yaml
spring:
  cloud:
    config:
      server:
        git:
          uri: <http git repo url>
          username: <github id>
          password: <github password>
```

> 보안을 위해 rsa 키 대신 ecdsa을 사용해야 한다

### 공개키 사용 1

1. config-server에서 키 생성

```
ssh-keygen -m PEM -t ecdsa -b 256
```

* 생성 시 파일 이름과 passphrase 관련 질문을 모두 넘기면 다음과 같이 `~/.ssh`에 `id_ecdsa.pub`으로 공개키가 생성된다.

2. public key를 deploy key에 등록하기

그 다음 설정 파일 깃 저장소의 deploy key에 공개키를 저장해주어야 한다. 공개키는 비대칭키를 생성한 서버에서 다음으로 조회가능하다.

```
cat /home1/irteam/.ssh/id_ecdsa.pub
```

* Settings > SSH and GPG keys 탭에서 추가

3. yml 수정

아래의 설정 파일을 복사한 다음에 개인에 맞게 uri, search-paths, hostKey, hostKeyAlgorithm, privateKey, passphrase 부분을 수정

```yaml
spring:
  cloud:
    config:
      server:
        git:
          default-label: master
          uri: git@github.com:MangKyu/spring-cloud-config-sample.git
          search-paths: test-cloud-config-file/**
          ignoreLocalSshSettings: true
          strictHostKeyChecking: false
          hostKey: hostKey // ssh-keyscan 명령어로 확인. hostKey를 base64 인코딩한 값
          hostKeyAlgorithm: ecdsa-sha2-nistp256 // ssh-keyscan 명령어로 확인. 비대칭키 생성 알고리즘
          privateKey: |
            -----BEGIN EC PRIVATE KEY-----
            
            ... 생략
            
            -----END EC PRIVATE KEY-----
          passphrase: passphrase // 비대칭키 생성 시 입력한 값
```

hostkey는 다음과 같이 설정 서버에서 ssh-keyscan으로 찾은 값을 base64 인코딩해주면 된다.

```
ssh-keyscan -t ssh-rsa github.com
```

개인 키는 다음의 명령어로 조회한 값을 입력해주면 된다. 

그런데 주의할 점은 위와 같이 설정 파일에 privateKey를 입력한다면 값에 "|"가 들어가야 하므로 빼먹지 않도록 주의해야 한다는 것이다. 그리고 실행 후 확인해보면 정상적으로 SSL 연결 설정 되었음을 확인할 수 있다.

```
cat /home1/irteam/.ssh/id_ecdsa
```





### 공개키 사용 2

먼저 SSH key 를 생성합니다.

```
$ ssh-keygen -m PEM -t rsa -b 4096 -C "github 이메일"
```

경로는 일반적으로 `~/.ssh` 밑에 생성됩니다. 해당 경로로 들어가보면 공개키와 개인키, 2개의 파일이 생성됩니다.

2. 공개키는 github 에서 사용하고 개인키는 config server 에서 사용해줍니다. -> Settings > SSH and GPG keys 탭에서 추가할 수 있습니다.

### config server에 개인키 추가하기

config server 어플리케이션의 application.yml 에 설정을 수정합니다.

```
spring:
  cloud:
    config:
      server:
        git:
          uri: <github ssh url>
          ignore-local-ssh-settings: true
          private-key: |
            -----BEGIN EC PRIVATE KEY-----
            your-key-here
            your-key-here
            your-key-here
            -----END EC PRIVATE KEY-----
          host-key:  AAAAE2VjZHNhLXNoYTItbm.....
          host-key-algorithm: ecdsa-sha2-nistp256
```

*  **파이프라인 기호( | )를 적어주지 않고 복사하면 에러가 날 수도 있기 때문에 반드시 적어준다. 또한,** -----BEGIN EC PRIVATE KEY----- 부터 -----END EC PRIVATE KEY-----까지 모두 복사해줘야 한다.

RSA 방식이 아닌 ECDSA 형식의 암호화 키를 생성하는법

```bash
$ ssh-keygen -M PEM -t ecdsa -b 256 -C "Github 계정" -f 키파일명
```

키 파일을 생성하는 것만 다르고 Github 계정 Setting에 들어가서 공개키를 업로드하는 부분은 동일하다.

하지만, application.yaml 파일에서 설정을 추가적으로 해줘야한다.

```yaml
private-key: |
             -----BEGIN EC PRIVATE KEY-----
             MHcCAQEEIIQf9VslUAT8vxL6sUBTaKuftWw7E6utoMcsdl4sl3/loAoGCCqGSM49
             ...
             -----END EC PRIVATE KEY-----
host-key: AAAAE2VjZHNhLXNoYTItbmlzdHAy.... # ssh-keyscan github.com의 hostmname
host-key-algorithm: ecdsa-sha2-nistp256    # 호스트 키 알고리즘
```

private-key 는 생성한 개인키를 복사해서 그대로 넣어준다.

그리고 , host-key 부분과 , host-key-algorithm 부분은 ssh-keyscan 명령어를 이용해 찾아서 넣어줄 수 있다.

```bash
$ ssh-keyscan -t ecdsa github.com
# github.com:22 SSH-2.0-babeld-4f04c79d
# host-key-algorith : ecdsa-sha2-nistp256 
# host-key AAAAE2VjZHNhLXNoYTItbmlzdHAyNTYAAAAIbmlzdHAy...
```

이렇게 레포지토리를 수정해준 후 동일하게 프로젝트를 빌드 후 엔드포인트를 호출하면 제대로 가져오는 것을 확인할 수 있다.

## 다른 Service에서 spring cloud config import하기

gradle

```groovy
dependencies {
   implementation 'org.springframework.cloud:spring-cloud-starter-config'
   implementation 'org.springframework.cloud:spring-cloud-starter-bootstrap'
}
```

application.yml 수정

```yaml
spring:
  application:
    name: user-service

  cloud:     # 추가
    config:
      name: ecommerce

# 아래처럼도 사용 가능
spring:
	import: optional:configserver:http://localhost:8888
```

* optional : optional prefix를 설정하지 않으면 config server에 연결할 수 없는 경우 애플리케이션이 종료됩니다.

main/resources에 bootstrap.yml 추가

```yaml
spring:
  cloud:
    config:
      uri: http://127.0.0.1:8888
      name: ecommerce # yml 확장자명을 제외한 파일명
#    profiles:
#      active: dev
			fail-fast: true # fail-fast를 true로 설정하면, Config 서버에 연결할 수 없는 경우 애플리케이션의 시작이 실패
	retry: # Config 서버에 연결 실패 시 재시도할 횟수, 초기 재시도 간격(밀리초), 재시도 간격 증가율을 설정
    max-attempts: 5
    initial-interval: 3000
    multiplier: 1.5
```

### 한 파일에서 여러 profile 관리 (default, dev, prod)

test-db.yml 파일

* 접근 : http://localhost:8888/test-db/profile명

```
test:
  db: good


---
spring:
  profile:
    active: dev

test:
  db: good-dev
  sexy: sexy-dev

---

spring:
  profile:
    active: prod

test:
  db: good-prod
  sexy: sexy-prod

```

접근방법

* http://localhost:8888/test-db/default
* http://localhost:8888/test-db/dev
* http://localhost:8888/test-db/prod



## 여러 구성 파일 사용

* 애플리케이션은 user-service

* profile은 default, local, dev, prod 
* 파일을 db, jwt, redis, aws로 분리

> Spring Cloud Config를 사용할 때, 하나의 파일 내에서 여러 프로필(dev, prod)을 정의하는 것은 직접적으로 지원되지 않는다.
> ---로 나누지말고 각 프로필에 대한 구성을 별도의 파일로 분리하는 것이 표준 접근 방식입니다. 이렇게 함으로써 구성 관리가 명확해지고, 각 환경에 대한 설정을 더 쉽게 관리하고 이해할 수 있습니다.

### `bootstrap.yml` 구성 예시

```yaml
spring:
  application:
    name: user-service
  cloud:
    config:
      uri: http://localhost:8888
      profile: ${SPRING_PROFILES_ACTIVE:prod}
      name: user-service,db,jwt,redis,aws
```

`application.yml 구성 명시 `

```yaml
spring:
  cloud:
    config:
      name: user-service,db,jwt,redis,aws
  profiles:
    active: ${SPRING_PROFILES_ACTIVE:prod}
```

> 반드시 application.yml에 가져올 여러 name(파일명)을 기술해야 한다.

* bootstrap.yml과 application.yml에 동시에 명시 안하면 busrefresh가 동작하지 않는다.

`name` 속성에 명시된 `user-service,db,jwt,redis,aws`에 기반하여, Config 서버는 다음과 같은 파일들을 찾으려고 한다.

- `user-service-dev.yml`, `user-service-prod.yml`
- `db-dev.yml` `db-prod.yml`
- `jwt-dev.yml` `jwt-prod.yml`
- `redis-dev.yml`, ...
- `aws-dev.yml`, ...

## 3. local 파일 시스템 사용

```yaml
spring:
  profiles:
    active: native
  application:
    name: config-service
  cloud:
    config:
      server:
        native:
          search-locations: file:///Users/ysk/temp/git-local-repo
```



## Configuration Change 방법 3가지

1. 서버 재기동
2. Actuator refresh
3. Spring Cloud bus 사용



### Actuator

gradle

```groovy
implementation 'org.springframework.boot:spring-boot-starter-actuator'
```

yml

```yaml
management:
  endpoints:
    web:
      exposure:
        include: refresh, health, beans
```

config 서버 파일 수정 후 /actuator/refresh 호출

```
POST http://localhost:9999/actuator/refresh
```



## Spring Cloud bus로 구성정보 변경

Spring Cloud Bus는 분산 시스템 내의 노드 간에 상태와 메시지를 경량 메시지 브로커를 통해 쉽게 방송할 수 있도록 해주는 도구입니다. 이를 통해 애플리케이션 인스턴스들 간에 상태를 동기화하거나, 설정 변경 같은 이벤트를 전파하는 것이 간편해집니다. Spring Cloud Bus는 주로 Spring Cloud Config 서버와 함께 사용되어, 구성 변경 사항을 자동으로 다른 서비스 인스턴스에 전파하는 데 사용됩니다.

![image-20240303140802872](./images//image-20240303140802872.png)

### 사용 예

Spring Cloud Bus를 사용하여 구성 변경을 자동으로 감지하고 적용하는 예제는 다음과 같습니다:

1. Spring Cloud Config 서버에 연결된 모든 클라이언트(애플리케이션 인스턴스)는 Spring Cloud Bus를 통해 연결됩니다.
2. 구성 변경이 발생하면, 이 변경 사항을 Config 서버가 감지합니다.
3. Config 서버는 Spring Cloud Bus를 통해 변경 사항을 연결된 모든 클라이언트에게 알립니다.
4. 각 클라이언트는 변경 사항을 자동으로 받아, 새로운 구성으로 업데이트됩니다.

```groovy
// 스프링 클라우드 스트림을 사용한 AMQP 지원
implementation 'org.springframework.cloud:spring-cloud-starter-bus-amqp'
implementation 'org.springframework.cloud:spring-cloud-starter-bootstrap'
```

docker-compose

```yaml
version: '3.8'
services:

  rabbitmq:
    image: rabbitmq:3-management-alpine
    container_name: rabbitmq
    ports:
      - "5672:5672" # RabbitMQ 서버
      - "15672:15672" # 관리 인터페이스
    environment:
      RABBITMQ_DEFAULT_USER: user # 기본 사용자 이름 변경
      RABBITMQ_DEFAULT_PASS: password # 기본 사용자 비밀번호 변경
    volumes:
      - "./data:/var/lib/rabbitmq" # 데이터 볼륨
      - "./config:/etc/rabbitmq" # 설정 파일을 위한 볼륨 (선택 사항)
      - "./logs:/var/log/rabbitmq" # 로그 파일을 위한 볼륨 (선택 사항)
```

localhost:15672 접속

bus를 사용하는 application.yml 수정

```yaml
spring:

  rabbitmq:
    host: 127.0.0.1
    port: 5672
    username: user
    password: password
    
# actuator도 수정 필요 -> busrefresh

management:
  endpoints:
    web:
      exposure:
        include: health, refresh, metrics, busrefresh #busrefresh
```

config서버들이 실행될때 config서버의 어떤 데이터를 읽는지 알수 있다. 

```
.c.c.ConfigServicePropertySourceLocator : Fetching config from server at : http://127.0.0.1:8888

2024-03-03T14:20:29.003+09:00  INFO 4940 --- [apigateway-service] [           main] c.c.c.ConfigServicePropertySourceLocator : Located environment: name=ecommerce, profiles=[prod], label=null, version=a4c22e02a8157b84a0450fc9092a8aad4a5fadb5, state=null

2024-03-03T14:20:29.004+09:00  INFO 4940 --- [apigateway-service] [           main] b.c.PropertySourceBootstrapConfiguration : Located property source: [BootstrapPropertySource {name='bootstrapProperties-configClient'}, BootstrapPropertySource {name='bootstrapProperties-file:///Users/ysk/temp/git-local-repo/ecommerce-prod.yml'}, BootstrapPropertySource {name='bootstrapProperties-file:///Users/ysk/temp/git-local-repo/ecommerce.yml'}]
```

* http://127.0.0.1:8888의 prod 프로파일이며 ecommerce-prod.yml



 ecommerce-prod.yml을 변경 하고 config 서버에서 확인해보자.

* http://localhost:8888/ecommerce/prod

```json
{
  "name": "ecommerce",
  "profiles": [
    "prod"
  ],
  "label": null,
  "version": "a4c22e02a8157b84a0450fc9092a8aad4a5fadb5",
  "state": null,
  "propertySources": [
    {
      "name": "file:///Users/ysk/temp/git-local-repo/ecommerce-prod.yml",
      "source": {
        "token.expiration_time": 86400000,
        "token.secret": "ysk-microservice-practice-jwt-secret-key-prod-changed",
        "gateway.ip": "http://192.168.0.4"
      }
    },
    {
      "name": "file:///Users/ysk/temp/git-local-repo/ecommerce.yml",
      "source": {
        "token.expiration_time": 86400000,
        "token.secret": "ysk-microservice-practice-jwt-secret-key-default",
        "gateway.ip": "http://192.168.0.4"
      }
    }
  ]
}
```

* changed로 변경됌

변경을 원하고자 하는 서버의 액츄에이터에 요청을 보낸다

```
POST http://localhost:9999/actuator/busrefresh # 9999는 user-service

api gateway를 사용하고있다면
POST http://api-gateway/actuator/busrefresh
```

* 시간이 좀 걸린다

연결된 서비스에도 변경사항이 전파된다.

> bootstrap.yml과 application.yml에 동시에 명시 안하면 busrefresh가 동작하지 않는다.
> 반드시 같이 명시해야 한다. 



## Spring Cloud config 암호화 처리

1. symmetric Encryption(대칭키 암호화, 공유 키)

대칭키 암호화에서는 암호화와 복호화에 같은 키가 사용. 

암호화와 복호화 과정이 빠르다는 장점이 있습니다. 그러나, 안전하게 키를 공유하는 것이 중요한 도전 과제입니다

2. Asymmetric Encryption (RSA, 비대칭키 암호화 (RSA 키 페어))

비대칭키 암호화에서는 두 개의 키가 사용됩니다: 하나는 공개 키(public key)이며 다른 하나는 개인 키(private key)입니다. 

공개 키는 데이터를 암호화하는 데 사용되며, 누구에게나 제공될 수 있습니다. 

반면, 개인 키는 데이터를 복호화하는 데 사용되며, 반드시 비밀로 유지되어야 합니다.

 이 방식은 키를 안전하게 공유할 필요가 없어 대칭키 방식의 주요 단점을 해결합니다. 그러나, 대칭키 암호화에 비해 계산 과정이 더 복잡하고 느릴 수 있다.



### JCE(Java Cryptography Extension)

JCE 는 Java 에서 암호화와 관련된 다양한 기능을 제공하는 확장 패키지

* JCE : Java 에서 제공하는 비대칭키를 생성하는 기능을 제공하는 암호 관련 라이브러리**
* **JKS : Java 에서 제공하는 JCE 에서 만든 비대칭키를 저장하고 관리하는 저장소**



### 대칭키 암호화

1. Config Server 의 bootstrap.yml 작성

```yml
// Config Server 의 bootstrap.yml 
encrypt:
  key: 0123456789abcdefghijklmnopqrstuvwxyz
```

* 해당 정보는 암호화와 복호화에 사용되는 대칭키

2. Config Server 을 재가동하고 암호화 앤드포인트를 호출

```http
POST http://[Config Server 주소]:[포트]/encrypt

test

response : bf1b2b5b0c9687d9a07157294af4147d3c9b016ef21b35344161aa776e851960
```

* body는 row data로 아무거나 입력. (암호화 할 데이터)

`Config Server` 가 bootstrap.yml 가지고 있는 대칭키로 암호화가 된다 

3. 복호화 방법

```
POST http://[Config Server 주소]:[포트]/decrypt

bf1b2b5b0c9687d9a07157294af4147d3c9b016ef21b35344161aa776e851960 // encrpyt한 데이터

response : test
```



4. 암호화 값 사용

config-server의 설정파일(yml에 다음과 같이 적어 사용한다

```yaml
# in config-server yml file

test: '{cipher}bf1b2b5b0c9687d9a07157294af4147d3c9b016ef21b35344161aa776e851960' 
```



### 비대칭키 암호화

비대칭키 방식은 암/복호화에 사용하는 키가 다른 방식

일반적으로 암호화할 때 Private Key 가 사용되며, 복호화할 때 Public Key 가 사용



### 1. JDK Keytool & JKS 을 통해 Key Store 파일(.jks) 생성

`Keytool` 의 명령어를 통해 `Spring Cloud Config Server` 에서 사용할 key 파일을 생성합니다.

```shell
$ keytool -genkeypair -alias ecommerceEncryptionKey -keyalg RSA \
-dname "CN=ysk, OU=API Development, O=devysk.co.kr, L=KR" -keypass \
"test12" -keystore ecommerceEncryptionKey.jks -storepass "test12"
```

- **-genkeypair : 키쌍(개인키 & 공개키) 을 생성하는 옵션**
- **-alias ecommerceEncrpytionKey : 생성되는 키 쌍에 대한 별칭을 지정 해당 별칭은 Key Store 에서 키 쌍 식별자로 사용(원하는 이름으로 생성하면 됩니다.)**
- **-keyalg RSA : RSA 알고리즘을 사용하여 키 쌍을 생성(RSA 알고리즘은 대표적인 비대칭키 방식의 알고리즘입니다.)**
- **-dname : "CN=ysk, OU=API Development, O=devysk.co.kr, L=KR" :
  인증서에 포함될 사용자의 이름, 조직, 지역 및 국가 정보를 지정**
- **-keypass "test12" : 개인키에 대한 암호를 지정**
- **-keystore ecommerceEncryptionKey.jks : 생성될 키 쌍을 저장할 키 Key Store 파일의 경로와 이름을 지정**
- **-storepass "test12" : Key Store 파일에 대한 암호를 지정**

> ecommerceEncryptionKey.jks 가 생성됌
>
> `Config Server` 가 가지는 키 파일이다.

아래 명령어로 정보 확인 가능

```
keytool -list -keystore ecommerceEncryptionKey.jks -v

password > test12
```

### 2. 공개키와 비밀키 생성

먼저 생성된 Key Store 파일을 이용해 공개키를 위한 인증서

```
$ keytool -export -alias ecommerceEncryptionKey -keystore ecommerceEncryptionKey.jks -rfc -file trust_ecommerceKey.cer
// $ keytool -export -alias [별칭] -keystore [위에서 만들어진 파일명].jks -rfc -file [인증서명].cer
```

`trust_ecommercetKey.cer` 인증서가 생성

다음으로 앞에서 만든 인증서를 통해 `인증된 Key Store 파일` 을 만들어보겠습니다.

```shell
$ keytool -import -alias trust_ecommerceKey -file trust_ecommerceKey.cer -keystore publicKey.jks
// $ keytool -import -alias [별칭] -file [인증서 파일].cer -keystore [새로 만들 파일].jks
```

* publicKey.jks 가 생성된다. 
* `publicKey.jks` 는 공개키만 가지는 Key Store 파일 주로 공개 키를 다른 서비스나 클라이언트에게 제공하는 용도로 사용



### 3. Config Server 에 적용 및 설정 파일 저장소 데이터 암호화

1. config server의 bootstrap.yml에 생성한 key store의 경로 설정.

```
encrypt:
  key-store:
    location: file:///Users/ysk/temp/ecommerceEncryptionKey.jks
    password: test12
    alias: ecommerceEncryptionKey
```

2. Config Server 을 재가동하고 암호화 앤드포인트를 호출

```http
POST http://[Config Server 주소]:[포트]/encrypt

root

response : AQClD~~~
```

* body는 row data로 아무거나 입력. (암호화 할 데이터)

`Config Server` 가 bootstrap.yml 가지고 있는 대칭키로 암호화가 된다 

3. 복호화 방법

```
POST http://[Config Server 주소]:[포트]/decrypt

AQClD~~~ // encrpyt한 데이터

response : test
```

4. config-server의 설정파일(yml에 다음과 같이 적어 사용한다

```yaml
# in config-server yml file

test: '{cipher}AQClD~~~' 
```

