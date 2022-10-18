

# Spring properties, yml 암호화 - Jasypt를 이용한 암호화



Jasypt : Java Simplified Encryption



공식 홈페이지 : http://www.jasypt.org/

GitHub : https://github.com/ulisesbocchio/jasypt-spring-boot



> Jasypt is a java library which allows the developer to add basic encryption capabilities to his/her projects with minimum effort, and without the need of having deep knowledge on how cryptography works.

개발자가 암호화 작동 방식에 대한 깊은 지식 없이도 최소한의 노력으로 
프로젝트에 기본 암호화 기능을 추가할 수 있도록 하는 Java 라이브러리이다. 



* 주로 프로젝트가 공개되어도 프로퍼티 내에 작성된 설정 정보를 암호화하여 노출시키지 않는 목적으로 사용한다.

* DB 접속 정보, 클라우드 접속 정보 등 민감 정보를 관리할때 사용하면 좋다 .
* 어플리케이션이 구동하면서 프로퍼티 정보를 메모리에 올리는데, 이때 Jasypt는 암호화한 정보를 **자동으로 복호화** 하여 메모리에 로딩한다.
* Key 값으로 평문을 암호화 하고, Key값으로 복호화 할 수 있다. 
  * `Key`값이 노출되면 안된다. 



spring-boot-starter를 지원하며, 다음과 같이 의존성을 추가할 수 있다.

* mvnrepository.com 에서 검색해도 된다
* 현재 최신 버전은 3.0.4이다





라이브러리 추가, 암호화, key값 넘겨주기 세 가지 단계로 프로퍼티를 암호화하여 관리할 수 있다.



사용 방법은 3가지가 있습다 .`jasypt-spring-boot`

- 전체 Spring 환경에서 암호화 가능한 속성을 사용 하거나 활성화할 `jasypt-spring-boot-starter`경우 클래스 경로 에 스타터 jar를 추가하기 만 하면 됩니다.`@SpringBootApplication``@EnableAutoConfiguration`
- 전체 Spring 환경에서 암호화 가능한 속성을 활성화하기 `jasypt-spring-boot`위해 클래스 경로에 추가 및 기본 구성 클래스에 추가`@EnableEncryptableProperties`
- 클래스 경로에 추가 `jasypt-spring-boot`하고 다음을 사용하여 개별 암호화 가능한 속성 소스 선언`@EncrytablePropertySource`



# 1. 라이브러리 추가

#### Maven

```xml
<!-- https://mvnrepository.com/artifact/com.github.ulisesbocchio/jasypt-spring-boot-starter -->
<dependency>
    <groupId>com.github.ulisesbocchio</groupId>
    <artifactId>jasypt-spring-boot-starter</artifactId>
    <version>3.0.4</version>
</dependency>
```

#### Gradle

```groovy
// https://mvnrepository.com/artifact/com.github.ulisesbocchio/jasypt-spring-boot-starter
implementation 'com.github.ulisesbocchio:jasypt-spring-boot-starter:3.0.4'
```



# 2 암호화



암호화는 2가지 방식이 있다.

1. 사이트를 통하여 암호화 하기

2. 테스트 코드를 돌려서 만든 암호화된 값으로 프로퍼티 채우기. 





### 2.1 사이트를 통하여 암호화 하기

key-value로 암복호화 할 수 있는 jasypt는 다양한 사이트에서 암호화를 지원한다

* https://www.devglan.com/online-tools/jasypt-online-encryption-decryption



### 2.2 테스트 코드를 돌려서 만든 암호화된 값으로 프로퍼티 채우기



먼저, 암 복호화를 위한 Secret Key를 설정해야 한다.



다음은 Junit5 테스트로 암호화한 예제이다



```java
public class JasyptTest {

    private String encryptKey = "my-secret-password"; // 키 값

    @Test
    void encryptTest() {
        String plainText = "plainText"; // 암호화할 평문

        StandardPBEStringEncryptor jasypt = new StandardPBEStringEncryptor();
        jasypt.setPassword(encryptKey); // 암호화할 키 값 파라미터로 전달
        jasypt.setAlgorithm("PBEWithMD5AndDES"); // 암호화 알고리즘

        String encryptText = jasypt.encrypt(plainText); // 암호화

        System.out.printf("encryptText : %s", encryptText); // 암호화한 문자열 출력
    }
}
```

* 결과

```
encryptText : VZXPnGGkbmfvJ8Bwp1N8/Yko9tPda11O
```



출력된 암호문을 프로퍼티에 등록해야 한다

* 등록할 때 주의사항은 반드시 암호문을 `ENC(...)` 로 감싸서 등록해야 한다.

* Jasypt는 복호화할 대상을 `ENC(암호화한 값)`으로 인식한다.





# 3. Encrypter Bean 등록을 통한 복호화

다음으로는, Jasypt Encrypter Bean을 등록해야 한다. 

`Config` 설정 클래스를 등록하여 암복호화 할 수 있다.

* Encryptor가 어플리케이션 로딩 시점에 key값을 통해 프로퍼티를 복호화 하고, 그 값들이 스프링 설정에 대입되서 정상 실행된다. 
* `제대로 된 key 값이 입력되지 않아 복호화가 실패하면 어플리케이션은 오류를 내뿜으며 실행하지 않는다.`
* 그러므로 먼저 JasyptConfig 설정은 필수이다. 



주의할점은, Bean을 등록하고  Bean 이름을 property에 넣어 지정해줘야 한다

```java
@Configuration
public class JasyptConfig {

    @Value("${jasypt.encryptor.password}") // 이 값을 property 파일이나 외부에서 주입받는다.  외부에서 넣어서 사용하는게 좋다.
    private String encryptKey;

    @Bean
    public StringEncryptor jasyptStringEncryptor() { // Bean 이름 
        PooledPBEStringEncryptor pbeStringEncryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(encryptKey); // 암호화 키
        config.setAlgorithm("PBEWithMD5AndDES"); // 암호화 알고리즘
        config.setKeyObtentionIterations("1000"); // 반복할 해싱 함수
        config.setPoolSize("1"); // 인스턴스 pool
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator"); // salt 생성 클래스
        config.setStringOutputType("base64"); //인코딩 방식
        pbeStringEncryptor.setConfig(config);
        return pbeStringEncryptor;
    }
}
```

```yaml
jasypt:
  encryptor:
    bean: jasyptStringEncryptor // Bean 이름 지정. 

example:
  encrypt-value : ENC(VZXPnGGkbmfvJ8Bwp1N8/Yko9tPda11O)
```



### key 값을 어떻게 관리? 

키 값을 관리하는 방법은 2가지가 있다.

1. property 파일에 직접 넣어 사용하기
2. 외부에서 `-jar 명령어`로 어플리케이션 실행시 전달
   * Password값을 application.yml에 넣는다면 누구나 복호화할 수 있게 되므로 의미가 없어진다.





## 1. property 파일에 직접 키 넣어 사용하기

* application.yml에 직접 key 값 넣어 사용하기 (public repository에서는 노출되므로 의미가 없다. )
* application.yml 예제처럼 key 값을 property 파일에 작성하고 Config 클래스에서 지정해주면 된다

```yaml
jasypt:
  encryptor:
    password: my-secret-password # 키 값. 이 값은 외부에서 넣어서 사용하는게 좋다.
    bean: jasyptStringEncryptor

example:
  encrypt-value : ENC(VZXPnGGkbmfvJ8Bwp1N8/Yko9tPda11O)
```





## 2. 외부에서 `-jar 명령어`로 어플리케이션 실행 시 전달

- java -jar 명령어로 어플리케이션 실행 시 전달

```
--jasypt.encryptor.password=testkey
```

`java명령어`를 사용해 어플리케이션을 실행하는 경우엔 맨 마지막에 **--** 를 사용하여 패스워드를 전달해준다.

'--' 앞의 **jasypt.encryptor.password** 는

복호화를 위한 `Config 클래스 설정` 에서 **@Value** 안에 넣어준 값과 동일해야 한다.



1. java 명령어를 사용해 실행하는법
2. IntelliJ에서 VmOption을 주고 실행하는법
3. gradle build 시 -P 옵션을 주고 실행하는법



### 2. IntelliJ 에서 로컬에서 VmOption을 주고 실행하는법

<img src="https://blog.kakaocdn.net/dn/l1f9D/btrOZJbf1BR/GVqpeMPaVKfkrmnrHbdXY1/img.png" width=700 height=300>

1. EditConfigruations 
2. Environment -> VM options에 -D@Value에 적은 값 - (@Value로 주입받는 프로퍼티  값이 같아야 한다. )
   * 예제에서는 jasypt.encryptor.password 로 했으므로
   * -Djasypt.encryptor.password=my-secret-password 를 넣어주고 실행



<img src="https://blog.kakaocdn.net/dn/dtzLOC/btrO0HDKzu8/FH5hWUiDSoyYcWcSDkxKDK/img.png" width=750 height=650>



---



## 3. gradle에서 build시 프로퍼티로 전달 - test property

- gradle에서 build 시 test의 프로퍼티로 전달

gradle에서 build를 진행하게되면 기본적으로 test도 같이 실행된다.

이때 암호화를 이용하여 같이 테스트를 하려 한다면 build 실행 시 Password를 함께 전달해주어야 한다.

```
-Pjasypt.encryptor.password=my-secret-password
```

우선 build명령어 실행 시 **-P**를 사용하여 gradle의 환경변수로 넘겨준다.

```
test {
    useJUnitPlatform()
    systemProperty 'jasypt.encryptor.password', findProperty("jasypt.encryptor.password")
}
```

* systemProperty는 키, 값 쌍을 인자로 테스트 수행 JVM의 시스템 프로퍼티 지정하는 메소드이다.

* findProperty를 통해 '-P' 이하로 입력된 속성을 찾아서 JVM의 시스템 프로퍼티로 지정해준다.

* 위와 같이 사용하면 public repository에서도 application.yml을 안전하게 보관할 수 있다.





# Spring-Boot-Test 또는 DataJpaTest 에서 명시적으로 암호를 입력하여 테스트 하는 방법



SpringBootTest 또는 DataJpaTest 에서 jasypt를 사용하여 복호화를 하려면 다음과 같이 설정해줘야한다.





* 복호화를 위한 `Config 클래스 설정` 에서 **@Value** 안에 넣어준 값을 그대로 어노테이션에 넣어준다 
  * 위 예제에서 사용한 @Value 값은 `jasypt.encryptor.password` 이다

```java
@Configuration
public class JasyptConfig {
	@Value("${jasypt.encryptor.password}") 
  private String encryptKey;
 		
  ...
}
```



`@SpringBootTest(properties = {jasypt.encryptor.password=my-secret-password})` 또는 
`@DataJpaTest(properties = {jasypt.encryptor.password=my-secret-password})`

* SpringBootTest는 모든 Bean이 등록되지만, DataJpaTest는 DataJpaTest와 관련된 Bean만 등록되기 때문에 `@Import`가 필요하다.



그리고

`@EnableEncryptableProperties`

어노테이션을 붙여줘야 한다





### 1. SpringBootTest

```java
@SpringBootTest(properties = {"jasypt.encryptor.password=my-secret-password"})
@EnableEncryptableProperties
public class IntegrationTest {
}

```



### 2. DataJpaTest

```java
@DataJpaTest(properties = {"jasypt.encryptor.password=my-secret-password"})
@Import({JasyptConfig.class}) // 
@EnableEncryptableProperties
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DataJpaTestBase {

}
```

