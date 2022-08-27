# Spring Boot JPA Test @DataJpaTest 기본 설정



- jpa관련 테스트(Entity, Repository)를 할때 `@DataJpaTest`를 이용해서 진행하면 JPA관련 Bean과 @Transactional 어노테이션이 달려있어서 테스트가 끝나면 Configuration만 주입받아서 `빠르게 테스트를 진행할 수 있다.`
- @Transactional 어노테이션이 달려있어서 테스트가 끝나면  롤백도 되어서 간단하게 결과를 확인할 수 있는 장점이 있다.
- MySQL, MSSQL, Oracle 처럼 다른 DB를 연동해서 사용할 수도 있지만, 이 글에서는 in-memory DB인 H2를 연동해서 테스트 하는 방법을 정리하고자 한다. 



환경

SpringBoot : 2.7.3

Java : JDK11 

MacOS : Monterey 12.5.1

h2 : h2:2.1.214

MacBookPro :  14 M1Pro

JUnit : JUnit5



* build.gradle

```groovy
plugins {
    id 'org.springframework.boot' version '2.7.3'
    id 'io.spring.dependency-management' version '1.0.13.RELEASE'
    id 'java'
}

group = #
version = '0.0.1-SNAPSHOT'
sourceCompatibility = '11'

configurations {
    compileOnly {
        extendsFrom annotationProcessor
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-web'
    compileOnly 'org.projectlombok:lombok'
    runtimeOnly 'mysql:mysql-connector-java'
    annotationProcessor 'org.projectlombok:lombok'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    testImplementation 'com.h2database:h2'
}

tasks.named('test') {
    useJUnitPlatform()
}
```



* Test Property Source
  * yml 형식을 사용한다.
  * main 디렉토리가 아닌 test 디렉토리의 resources를 사용한다.

<img src="https://blog.kakaocdn.net/dn/c35Nsh/btrKIxS203M/BHezvkO5qUW8iZqiEe6Yn1/img.png">



* application-test.yml

```yaml
spring:

  config:
    activate:
      on-profile: test

  datasource:
    username: sa
    password:
    driver-class-name: org.h2.Driver
    url: jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false;MODE=MySQL

  jpa:
    hibernate:
      ddl-auto: create-drop
    properties:
      hibernate.dialect: org.hibernate.dialect.MySQL8Dialect
      hibernate:
        format_sql: true
        globally_quoted_identifiers: true
    show-sql: true
    generate-ddl: true
    database-platform: org.hibernate.dialect.MySQL8Dialect
    database: h2

  h2:
    console:
      enabled: true

```

* spring.config.activate.on-profile : test
  * profile 환경 설정을 test로 한다

* url : jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false;MODE=MySQL
  * `h2 기반 인메모리 휘발성 테스트로 진행할것이기 때문에 h2:mem으로 설정했다.  `
  * DB_CLOSE_DELAY=-1 : 
    * 기본적으로 데이터베이스에 대한 마지막 연결을 닫으면 데이터베이스가 닫힙니다.
    * 데이터베이스를 열린 상태로 유지하기 위해 가상 머신이 살아있는 동안 메모리 내 데이터베이스의 내용을 유지한다
    * http://www.h2database.com/html/features.html#connection_modes
  * DATABASE_TO_UPPER=false:
    * (기본값: true). true로 설정하면 인용되지 않은 식별자와 데이터베이스의 짧은 이름이 대문자로 변환됩니다.
    * 테이블명이나 컬럼 이름을 소문자로 사용하기 위해서 false로 설정
    *  `DATABASE_TO_LOWER` = true로도 사용 가능 
    * https://www.h2database.com/javadoc/org/h2/engine/DbSettings.html
  * MODE=MYSQL
    * H2 DB의 호환 모드를 MySQL로 설정
* globally_quoted_identifiers: true
  * 데이터베이스 테이블 자동 생성시 키워드나 예약어가 충돌되는 경우가 있습니다.
  * true로 설정하면 충돌을 방지하기 위해 테이블명이나 컬럼명을  ` 로 감싸줍니다.
  * https://www.popit.kr/%EA%B0%80%EC%A7%9C%EB%89%B4%EC%8A%A4%EC%95%84%EC%9B%83-%ED%95%98%EC%9D%B4%EB%B2%84%EB%84%A4%EC%9D%B4%ED%8A%B8-%EB%8D%B0%EC%9D%B4%ED%84%B0%EB%B2%A0%EC%9D%B4%EC%8A%A4-%EC%8A%A4%ED%82%A4%EB%A7%88/



* 다른 옵션들은 http://www.h2database.com/html/features.html 에 잘 나와있습니다.



## Entity, Repository

```java
@Getter
@Entity
@NoArgsConstructor
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private int age;

    @Builder
    public Member(String name, String email, int age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }

}

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

}

```



## TestClass - MemberRepositoryTest

```java
@ActiveProfiles("test")
@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    void save() {
        memberRepository.save(
                Member.builder()
                        .age(1)
                        .email("email@email.com")
                        .name("name")
                        .build()
        );

    }
}
```



* @ActiveProfiles("test") : 테스트 시에 어떤 profile로 설정할지 구성한다.
  * develop, prod로 설정하면 해당 profile로 실행한다.
  * test 환경 설정으로 실행할것이기 때문에 test로 설정 하였다. 
* @DataJpaTest : Spring에서  JPA 관련 테스트 설정만 로드한다. DataSource의 설정이 정상적인지, 제대로 생성 수정 삭제 조회 하는지 등의 테스트가 가능하다. 
* @TestPropertySource(locations = "classpath:application-test.yml")
  * @TestPropertySource 애노테이션을 사용해서 새로운 설정 Source를 정의하고 프로퍼티의 값을 오버라이딩 한다.
  * locations 속성을 통해서 설정 파일의 위치를 변경할 수 있다. 또한 properties 속성을 통해 우선순위가 더 높은 속성을 추가할 수 있다.

* @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
  * @DataJpaTest를 사용하면 자동으로 EmbededDatabase를 사용하기 때문에 내가 설정한 설정값들을 사용할 수 없다.
  * 이 설정을 replace 해서 해당 설정이 동작하지 않고, 내가 설정한 설정파일대로 만들어진 DataSoruce가 Bean으로 등록된다. 
  * yml이나 properties에서 내가 설정한 설정파일대로 사용하려면 (내가 설정한 H2, MySQL,Oracle 등)  NONE 옵션을 사용해서 사용해야 한다 

### `반드시 주의해야 하는게, `

###  `기본 H2가 아닌 내가 설정한 H2대로 설정하려면 `

###  `@AutoConfigureTestDatabase 이 옵션을 반드시 추가해야한다 그렇지 않으면 엄청난 고생을 하게 된다. `

* @AutoConfigureTestDatabase는 기본적으로 h2 인메모리 기반 으로 테스트를 하는데 왜????

* 이 부분은 다른 글로 정리할것이다.. 이틀을 삽질을 했다.. 
* syntax error가 무조건 난다.. 
* 예약어도 아닌데 테이블도 안만들어지고.. select insert 아무것도 안된다..  

<img src="https://blog.kakaocdn.net/dn/c2WPP0/btrKMnvn6ii/MM56xRnUrAwBcUifqVwOg1/img.png">

* 정상적으로 테스트가 되는것을 볼 수 있다.





# @DataJpaTest H2 기반 in-memory 테스트 시 트러블 슈팅



SpringBoot JPA 를 사용하는 환경에서 JUnit을 이용해서 persistence의 단위테스트가 익숙하지 않아서 공부 하려고 했다.



실제 개발 서버 DB와 연동해서 테스트하다가는 다른 이슈가 있을 수 있어  In-memory로 테스트를 하길 원했다.

DBMS를 MySQL로 사용해서 기본 H2 In-memory가 아닌

H2의 MODE와 hibernate의 Dialect를 MySQL 설정하여 사용하려고 하였다.



하지만 다음과 같은 오류로 인해 전혀 테스트를 할 수가 없었다.



Caused by: org.h2.jdbc.JdbcSQLSyntaxErrorException: Syntax error in SQL statement "\000a    create table `member` (\000a       `id` bigint not null auto_increment,\000a        `age` integer not null,\000a        `email` varchar(255) not null,\000a        `name` varchar(255) not null,\000a        primary key (`id`)\000a    ) engine[*]=InnoDB"; expected "identifier"; SQL statement:

    create table `member` (
       `id` bigint not null auto_increment,
        `age` integer not null,
        `email` varchar(255) not null,
        `name` varchar(255) not null,
        primary key (`id`)
    ) engine=InnoDB [42001-214] 

org.hibernate.tool.schema.spi.CommandAcceptanceException: Error executing DDL "
    create table `member` (
       `id` bigint not null auto_increment,
        `age` integer not null,
        `email` varchar(255) not null,
        `name` varchar(255) not null,
        primary key (`id`)
    ) engine=InnoDB" via JDBC Statement

2022-08-27 21:51:20.268  WARN 64106 --- [    Test worker] o.h.t.s.i.ExceptionHandlerLoggedImpl     : GenerationTarget encountered exception accepting command : Error executing DDL "



<details>
<summary> 에러 본문 - 접기/펼치기</summary>

```
2022-08-27 21:51:17.210  INFO 64106 --- [    Test worker] com.ys.test.member.MemberRepositoryTest  : Starting MemberRepositoryTest using Java 11.0.11 on MacBook-Pro.local with PID 64106 (started by ysk in /Users/ysk/study/study_repo/tdd/junit5)
2022-08-27 21:51:17.212  INFO 64106 --- [    Test worker] com.ys.test.member.MemberRepositoryTest  : The following 1 profile is active: "test"
2022-08-27 21:51:17.963  INFO 64106 --- [    Test worker] .s.d.r.c.RepositoryConfigurationDelegate : Bootstrapping Spring Data JPA repositories in DEFAULT mode.
2022-08-27 21:51:18.041  INFO 64106 --- [    Test worker] .s.d.r.c.RepositoryConfigurationDelegate : Finished Spring Data repository scanning in 68 ms. Found 1 JPA repository interfaces.
2022-08-27 21:51:18.165  INFO 64106 --- [    Test worker] beddedDataSourceBeanFactoryPostProcessor : Replacing 'dataSource' DataSource bean with embedded version
2022-08-27 21:51:18.538  INFO 64106 --- [    Test worker] o.s.j.d.e.EmbeddedDatabaseFactory        : Starting embedded database: url='jdbc:h2:mem:05497116-9d3f-43ee-94d6-6de829c82dc6;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=false', username='sa'
2022-08-27 21:51:19.201  INFO 64106 --- [    Test worker] o.hibernate.jpa.internal.util.LogHelper  : HHH000204: Processing PersistenceUnitInfo [name: default]
2022-08-27 21:51:19.260  INFO 64106 --- [    Test worker] org.hibernate.Version                    : HHH000412: Hibernate ORM core version 5.6.10.Final
2022-08-27 21:51:19.454  INFO 64106 --- [    Test worker] o.hibernate.annotations.common.Version   : HCANN000001: Hibernate Commons Annotations {5.1.2.Final}
2022-08-27 21:51:19.591  INFO 64106 --- [    Test worker] org.hibernate.dialect.Dialect            : HHH000400: Using dialect: org.hibernate.dialect.MySQL8Dialect
Hibernate: 
    
    drop table if exists `member`
Hibernate: 
    
    create table `member` (
       `id` bigint not null auto_increment,
        `age` integer not null,
        `email` varchar(255) not null,
        `name` varchar(255) not null,
        primary key (`id`)
    ) engine=InnoDB
2022-08-27 21:51:20.268  WARN 64106 --- [    Test worker] o.h.t.s.i.ExceptionHandlerLoggedImpl     : GenerationTarget encountered exception accepting command : Error executing DDL "
    create table `member` (
       `id` bigint not null auto_increment,
        `age` integer not null,
        `email` varchar(255) not null,
        `name` varchar(255) not null,
        primary key (`id`)
    ) engine=InnoDB" via JDBC Statement

org.hibernate.tool.schema.spi.CommandAcceptanceException: Error executing DDL "
    create table `member` (
       `id` bigint not null auto_increment,
        `age` integer not null,
        `email` varchar(255) not null,
        `name` varchar(255) not null,
        primary key (`id`)
    ) engine=InnoDB" via JDBC Statement
	at org.hibernate.tool.schema.internal.exec.GenerationTargetToDatabase.accept(GenerationTargetToDatabase.java:67) ~[hibernate-core-5.6.10.Final.jar:5.6.10.Final]
	at org.hibernate.tool.schema.internal.SchemaCreatorImpl.applySqlString(SchemaCreatorImpl.java:458) ~[hibernate-core-5.6.10.Final.jar:5.6.10.Final]
	at org.hibernate.tool.schema.internal.SchemaCreatorImpl.applySqlStrings(SchemaCreatorImpl.java:442) ~[hibernate-core-5.6.10.Final.jar:5.6.10.Final]
	at org.hibernate.tool.schema.internal.SchemaCreatorImpl.createFromMetadata(SchemaCreatorImpl.java:325) ~[hibernate-core-5.6.10.Final.jar:5.6.10.Final]
	at org.hibernate.tool.schema.internal.SchemaCreatorImpl.performCreation(SchemaCreatorImpl.java:169) ~[hibernate-core-5.6.10.Final.jar:5.6.10.Final]
	at org.hibernate.tool.schema.internal.SchemaCreatorImpl.doCreation(SchemaCreatorImpl.java:138) ~[hibernate-core-5.6.10.Final.jar:5.6.10.Final]
	at org.hibernate.tool.schema.internal.SchemaCreatorImpl.doCreation(SchemaCreatorImpl.java:124) ~[hibernate-core-5.6.10.Final.jar:5.6.10.Final]
	at org.hibernate.tool.schema.spi.SchemaManagementToolCoordinator.performDatabaseAction(SchemaManagementToolCoordinator.java:168) ~[hibernate-core-5.6.10.Final.jar:5.6.10.Final]
	at org.hibernate.tool.schema.spi.SchemaManagementToolCoordinator.process(SchemaManagementToolCoordinator.java:85) ~[hibernate-core-5.6.10.Final.jar:5.6.10.Final]
	at org.hibernate.internal.SessionFactoryImpl.<init>(SessionFactoryImpl.java:335) ~[hibernate-core-5.6.10.Final.jar:5.6.10.Final]
	at org.hibernate.boot.internal.SessionFactoryBuilderImpl.build(SessionFactoryBuilderImpl.java:471) ~[hibernate-core-5.6.10.Final.jar:5.6.10.Final]
	at org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl.build(EntityManagerFactoryBuilderImpl.java:1498) ~[hibernate-core-5.6.10.Final.jar:5.6.10.Final]
	at org.springframework.orm.jpa.vendor.SpringHibernateJpaPersistenceProvider.createContainerEntityManagerFactory(SpringHibernateJpaPersistenceProvider.java:58) ~[spring-orm-5.3.22.jar:5.3.22]
	at org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean.createNativeEntityManagerFactory(LocalContainerEntityManagerFactoryBean.java:365) ~[spring-orm-5.3.22.jar:5.3.22]
	at org.springframework.orm.jpa.AbstractEntityManagerFactoryBean.buildNativeEntityManagerFactory(AbstractEntityManagerFactoryBean.java:409) ~[spring-orm-5.3.22.jar:5.3.22]
	at org.springframework.orm.jpa.AbstractEntityManagerFactoryBean.afterPropertiesSet(AbstractEntityManagerFactoryBean.java:396) ~[spring-orm-5.3.22.jar:5.3.22]
	at org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean.afterPropertiesSet(LocalContainerEntityManagerFactoryBean.java:341) ~[spring-orm-5.3.22.jar:5.3.22]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.invokeInitMethods(AbstractAutowireCapableBeanFactory.java:1863) ~[spring-beans-5.3.22.jar:5.3.22]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.initializeBean(AbstractAutowireCapableBeanFactory.java:1800) ~[spring-beans-5.3.22.jar:5.3.22]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:620) ~[spring-beans-5.3.22.jar:5.3.22]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:542) ~[spring-beans-5.3.22.jar:5.3.22]
	at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:335) ~[spring-beans-5.3.22.jar:5.3.22]
	at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:234) ~[spring-beans-5.3.22.jar:5.3.22]
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:333) ~[spring-beans-5.3.22.jar:5.3.22]
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:208) ~[spring-beans-5.3.22.jar:5.3.22]
	at org.springframework.context.support.AbstractApplicationContext.getBean(AbstractApplicationContext.java:1154) ~[spring-context-5.3.22.jar:5.3.22]
	at org.springframework.context.support.AbstractApplicationContext.finishBeanFactoryInitialization(AbstractApplicationContext.java:908) ~[spring-context-5.3.22.jar:5.3.22]
	at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:583) ~[spring-context-5.3.22.jar:5.3.22]
	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:734) ~[spring-boot-2.7.3.jar:2.7.3]
	at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:408) ~[spring-boot-2.7.3.jar:2.7.3]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:308) ~[spring-boot-2.7.3.jar:2.7.3]
	at org.springframework.boot.test.context.SpringBootContextLoader.loadContext(SpringBootContextLoader.java:132) ~[spring-boot-test-2.7.3.jar:2.7.3]
	at org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate.loadContextInternal(DefaultCacheAwareContextLoaderDelegate.java:99) ~[spring-test-5.3.22.jar:5.3.22]
	at org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate.loadContext(DefaultCacheAwareContextLoaderDelegate.java:124) ~[spring-test-5.3.22.jar:5.3.22]
	at org.springframework.test.context.support.DefaultTestContext.getApplicationContext(DefaultTestContext.java:124) ~[spring-test-5.3.22.jar:5.3.22]
	at org.springframework.test.context.support.DependencyInjectionTestExecutionListener.injectDependencies(DependencyInjectionTestExecutionListener.java:118) ~[spring-test-5.3.22.jar:5.3.22]
	at org.springframework.test.context.support.DependencyInjectionTestExecutionListener.prepareTestInstance(DependencyInjectionTestExecutionListener.java:83) ~[spring-test-5.3.22.jar:5.3.22]
	at org.springframework.boot.test.autoconfigure.SpringBootDependencyInjectionTestExecutionListener.prepareTestInstance(SpringBootDependencyInjectionTestExecutionListener.java:43) ~[spring-boot-test-autoconfigure-2.7.3.jar:2.7.3]
	at org.springframework.test.context.TestContextManager.prepareTestInstance(TestContextManager.java:248) ~[spring-test-5.3.22.jar:5.3.22]
	at org.springframework.test.context.junit.jupiter.SpringExtension.postProcessTestInstance(SpringExtension.java:138) ~[spring-test-5.3.22.jar:5.3.22]
	at org.junit.jupiter.engine.descriptor.ClassBasedTestDescriptor.lambda$invokeTestInstancePostProcessors$8(ClassBasedTestDescriptor.java:363) ~[junit-jupiter-engine-5.8.2.jar:5.8.2]
	at org.junit.jupiter.engine.descriptor.ClassBasedTestDescriptor.executeAndMaskThrowable(ClassBasedTestDescriptor.java:368) ~[junit-jupiter-engine-5.8.2.jar:5.8.2]
	at org.junit.jupiter.engine.descriptor.ClassBasedTestDescriptor.lambda$invokeTestInstancePostProcessors$9(ClassBasedTestDescriptor.java:363) ~[junit-jupiter-engine-5.8.2.jar:5.8.2]
	at java.base/java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195) ~[na:na]
	at java.base/java.util.stream.ReferencePipeline$2$1.accept(ReferencePipeline.java:177) ~[na:na]
	at java.base/java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1655) ~[na:na]
	at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:484) ~[na:na]
	at java.base/java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474) ~[na:na]
	at java.base/java.util.stream.StreamSpliterators$WrappingSpliterator.forEachRemaining(StreamSpliterators.java:312) ~[na:na]
	at java.base/java.util.stream.Streams$ConcatSpliterator.forEachRemaining(Streams.java:735) ~[na:na]
	at java.base/java.util.stream.Streams$ConcatSpliterator.forEachRemaining(Streams.java:734) ~[na:na]
	at java.base/java.util.stream.ReferencePipeline$Head.forEach(ReferencePipeline.java:658) ~[na:na]
	at org.junit.jupiter.engine.descriptor.ClassBasedTestDescriptor.invokeTestInstancePostProcessors(ClassBasedTestDescriptor.java:362) ~[junit-jupiter-engine-5.8.2.jar:5.8.2]
	at org.junit.jupiter.engine.descriptor.ClassBasedTestDescriptor.lambda$instantiateAndPostProcessTestInstance$6(ClassBasedTestDescriptor.java:283) ~[junit-jupiter-engine-5.8.2.jar:5.8.2]
	at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73) ~[junit-platform-engine-1.8.2.jar:1.8.2]
	at org.junit.jupiter.engine.descriptor.ClassBasedTestDescriptor.instantiateAndPostProcessTestInstance(ClassBasedTestDescriptor.java:282) ~[junit-jupiter-engine-5.8.2.jar:5.8.2]
	at org.junit.jupiter.engine.descriptor.ClassBasedTestDescriptor.lambda$testInstancesProvider$4(ClassBasedTestDescriptor.java:272) ~[junit-jupiter-engine-5.8.2.jar:5.8.2]
	at java.base/java.util.Optional.orElseGet(Optional.java:369) ~[na:na]
	at org.junit.jupiter.engine.descriptor.ClassBasedTestDescriptor.lambda$testInstancesProvider$5(ClassBasedTestDescriptor.java:271) ~[junit-jupiter-engine-5.8.2.jar:5.8.2]
	at org.junit.jupiter.engine.execution.TestInstancesProvider.getTestInstances(TestInstancesProvider.java:31) ~[junit-jupiter-engine-5.8.2.jar:5.8.2]
	at org.junit.jupiter.engine.descriptor.TestMethodTestDescriptor.lambda$prepare$0(TestMethodTestDescriptor.java:102) ~[junit-jupiter-engine-5.8.2.jar:5.8.2]
	at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73) ~[junit-platform-engine-1.8.2.jar:1.8.2]
	at org.junit.jupiter.engine.descriptor.TestMethodTestDescriptor.prepare(TestMethodTestDescriptor.java:101) ~[junit-jupiter-engine-5.8.2.jar:5.8.2]
	at org.junit.jupiter.engine.descriptor.TestMethodTestDescriptor.prepare(TestMethodTestDescriptor.java:66) ~[junit-jupiter-engine-5.8.2.jar:5.8.2]
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$prepare$2(NodeTestTask.java:123) ~[junit-platform-engine-1.8.2.jar:1.8.2]
	at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73) ~[junit-platform-engine-1.8.2.jar:1.8.2]
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.prepare(NodeTestTask.java:123) ~[junit-platform-engine-1.8.2.jar:1.8.2]
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.execute(NodeTestTask.java:90) ~[junit-platform-engine-1.8.2.jar:1.8.2]
	at java.base/java.util.ArrayList.forEach(ArrayList.java:1541) ~[na:na]
	at org.junit.platform.engine.support.hierarchical.SameThreadHierarchicalTestExecutorService.invokeAll(SameThreadHierarchicalTestExecutorService.java:41) ~[junit-platform-engine-1.8.2.jar:1.8.2]
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$6(NodeTestTask.java:155) ~[junit-platform-engine-1.8.2.jar:1.8.2]
	at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73) ~[junit-platform-engine-1.8.2.jar:1.8.2]
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$8(NodeTestTask.java:141) ~[junit-platform-engine-1.8.2.jar:1.8.2]
	at org.junit.platform.engine.support.hierarchical.Node.around(Node.java:137) ~[junit-platform-engine-1.8.2.jar:1.8.2]
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$9(NodeTestTask.java:139) ~[junit-platform-engine-1.8.2.jar:1.8.2]
	at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73) ~[junit-platform-engine-1.8.2.jar:1.8.2]
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.executeRecursively(NodeTestTask.java:138) ~[junit-platform-engine-1.8.2.jar:1.8.2]
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.execute(NodeTestTask.java:95) ~[junit-platform-engine-1.8.2.jar:1.8.2]
	at java.base/java.util.ArrayList.forEach(ArrayList.java:1541) ~[na:na]
	at org.junit.platform.engine.support.hierarchical.SameThreadHierarchicalTestExecutorService.invokeAll(SameThreadHierarchicalTestExecutorService.java:41) ~[junit-platform-engine-1.8.2.jar:1.8.2]
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$6(NodeTestTask.java:155) ~[junit-platform-engine-1.8.2.jar:1.8.2]
	at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73) ~[junit-platform-engine-1.8.2.jar:1.8.2]
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$8(NodeTestTask.java:141) ~[junit-platform-engine-1.8.2.jar:1.8.2]
	at org.junit.platform.engine.support.hierarchical.Node.around(Node.java:137) ~[junit-platform-engine-1.8.2.jar:1.8.2]
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$9(NodeTestTask.java:139) ~[junit-platform-engine-1.8.2.jar:1.8.2]
	at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73) ~[junit-platform-engine-1.8.2.jar:1.8.2]
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.executeRecursively(NodeTestTask.java:138) ~[junit-platform-engine-1.8.2.jar:1.8.2]
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.execute(NodeTestTask.java:95) ~[junit-platform-engine-1.8.2.jar:1.8.2]
	at org.junit.platform.engine.support.hierarchical.SameThreadHierarchicalTestExecutorService.submit(SameThreadHierarchicalTestExecutorService.java:35) ~[junit-platform-engine-1.8.2.jar:1.8.2]
	at org.junit.platform.engine.support.hierarchical.HierarchicalTestExecutor.execute(HierarchicalTestExecutor.java:57) ~[junit-platform-engine-1.8.2.jar:1.8.2]
	at org.junit.platform.engine.support.hierarchical.HierarchicalTestEngine.execute(HierarchicalTestEngine.java:54) ~[junit-platform-engine-1.8.2.jar:1.8.2]
	at org.junit.platform.launcher.core.EngineExecutionOrchestrator.execute(EngineExecutionOrchestrator.java:107) ~[na:na]
	at org.junit.platform.launcher.core.EngineExecutionOrchestrator.execute(EngineExecutionOrchestrator.java:88) ~[na:na]
	at org.junit.platform.launcher.core.EngineExecutionOrchestrator.lambda$execute$0(EngineExecutionOrchestrator.java:54) ~[na:na]
	at org.junit.platform.launcher.core.EngineExecutionOrchestrator.withInterceptedStreams(EngineExecutionOrchestrator.java:67) ~[na:na]
	at org.junit.platform.launcher.core.EngineExecutionOrchestrator.execute(EngineExecutionOrchestrator.java:52) ~[na:na]
	at org.junit.platform.launcher.core.DefaultLauncher.execute(DefaultLauncher.java:114) ~[na:na]
	at org.junit.platform.launcher.core.DefaultLauncher.execute(DefaultLauncher.java:86) ~[na:na]
	at org.junit.platform.launcher.core.DefaultLauncherSession$DelegatingLauncher.execute(DefaultLauncherSession.java:86) ~[na:na]
	at org.junit.platform.launcher.core.SessionPerRequestLauncher.execute(SessionPerRequestLauncher.java:53) ~[na:na]
	at org.gradle.api.internal.tasks.testing.junitplatform.JUnitPlatformTestClassProcessor$CollectAllTestClassesExecutor.processAllTestClasses(JUnitPlatformTestClassProcessor.java:99) ~[na:na]
	at org.gradle.api.internal.tasks.testing.junitplatform.JUnitPlatformTestClassProcessor$CollectAllTestClassesExecutor.access$000(JUnitPlatformTestClassProcessor.java:79) ~[na:na]
	at org.gradle.api.internal.tasks.testing.junitplatform.JUnitPlatformTestClassProcessor.stop(JUnitPlatformTestClassProcessor.java:75) ~[na:na]
	at org.gradle.api.internal.tasks.testing.SuiteTestClassProcessor.stop(SuiteTestClassProcessor.java:61) ~[na:na]
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[na:na]
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62) ~[na:na]
	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[na:na]
	at java.base/java.lang.reflect.Method.invoke(Method.java:566) ~[na:na]
	at org.gradle.internal.dispatch.ReflectionDispatch.dispatch(ReflectionDispatch.java:36) ~[na:na]
	at org.gradle.internal.dispatch.ReflectionDispatch.dispatch(ReflectionDispatch.java:24) ~[na:na]
	at org.gradle.internal.dispatch.ContextClassLoaderDispatch.dispatch(ContextClassLoaderDispatch.java:33) ~[na:na]
	at org.gradle.internal.dispatch.ProxyDispatchAdapter$DispatchingInvocationHandler.invoke(ProxyDispatchAdapter.java:94) ~[na:na]
	at com.sun.proxy.$Proxy2.stop(Unknown Source) ~[na:na]
	at org.gradle.api.internal.tasks.testing.worker.TestWorker$3.run(TestWorker.java:193) ~[na:na]
	at org.gradle.api.internal.tasks.testing.worker.TestWorker.executeAndMaintainThreadName(TestWorker.java:129) ~[na:na]
	at org.gradle.api.internal.tasks.testing.worker.TestWorker.execute(TestWorker.java:100) ~[na:na]
	at org.gradle.api.internal.tasks.testing.worker.TestWorker.execute(TestWorker.java:60) ~[na:na]
	at org.gradle.process.internal.worker.child.ActionExecutionWorker.execute(ActionExecutionWorker.java:56) ~[na:na]
	at org.gradle.process.internal.worker.child.SystemApplicationClassLoaderWorker.call(SystemApplicationClassLoaderWorker.java:133) ~[na:na]
	at org.gradle.process.internal.worker.child.SystemApplicationClassLoaderWorker.call(SystemApplicationClassLoaderWorker.java:71) ~[na:na]
	at worker.org.gradle.process.internal.worker.GradleWorkerMain.run(GradleWorkerMain.java:69) ~[gradle-worker.jar:na]
	at worker.org.gradle.process.internal.worker.GradleWorkerMain.main(GradleWorkerMain.java:74) ~[gradle-worker.jar:na]
Caused by: org.h2.jdbc.JdbcSQLSyntaxErrorException: Syntax error in SQL statement "\000a    create table `member` (\000a       `id` bigint not null auto_increment,\000a        `age` integer not null,\000a        `email` varchar(255) not null,\000a        `name` varchar(255) not null,\000a        primary key (`id`)\000a    ) engine[*]=InnoDB"; expected "identifier"; SQL statement:

    create table `member` (
       `id` bigint not null auto_increment,
        `age` integer not null,
        `email` varchar(255) not null,
        `name` varchar(255) not null,
        primary key (`id`)
    ) engine=InnoDB [42001-214]
	at org.h2.message.DbException.getJdbcSQLException(DbException.java:502) ~[h2-2.1.214.jar:2.1.214]
	at org.h2.message.DbException.getJdbcSQLException(DbException.java:477) ~[h2-2.1.214.jar:2.1.214]
	at org.h2.message.DbException.getSyntaxError(DbException.java:261) ~[h2-2.1.214.jar:2.1.214]
	at org.h2.command.Parser.readIdentifier(Parser.java:5656) ~[h2-2.1.214.jar:2.1.214]
	at org.h2.command.Parser.parseCreateTable(Parser.java:9279) ~[h2-2.1.214.jar:2.1.214]
	at org.h2.command.Parser.parseCreate(Parser.java:6784) ~[h2-2.1.214.jar:2.1.214]
	at org.h2.command.Parser.parsePrepared(Parser.java:763) ~[h2-2.1.214.jar:2.1.214]
	at org.h2.command.Parser.parse(Parser.java:689) ~[h2-2.1.214.jar:2.1.214]
	at org.h2.command.Parser.parse(Parser.java:661) ~[h2-2.1.214.jar:2.1.214]
	at org.h2.command.Parser.prepareCommand(Parser.java:569) ~[h2-2.1.214.jar:2.1.214]
	at org.h2.engine.SessionLocal.prepareLocal(SessionLocal.java:631) ~[h2-2.1.214.jar:2.1.214]
	at org.h2.engine.SessionLocal.prepareCommand(SessionLocal.java:554) ~[h2-2.1.214.jar:2.1.214]
	at org.h2.jdbc.JdbcConnection.prepareCommand(JdbcConnection.java:1116) ~[h2-2.1.214.jar:2.1.214]
	at org.h2.jdbc.JdbcStatement.executeInternal(JdbcStatement.java:237) ~[h2-2.1.214.jar:2.1.214]
	at org.h2.jdbc.JdbcStatement.execute(JdbcStatement.java:223) ~[h2-2.1.214.jar:2.1.214]
	at org.hibernate.tool.schema.internal.exec.GenerationTargetToDatabase.accept(GenerationTargetToDatabase.java:54) ~[hibernate-core-5.6.10.Final.jar:5.6.10.Final]
	... 121 common frames omitted

Hibernate: 
    
    alter table `member` 
       add constraint UK_mbmcqelty0fbrvxp1q58dn57t unique (`email`)
2022-08-27 21:51:20.288  WARN 64106 --- [    Test worker] o.h.t.s.i.ExceptionHandlerLoggedImpl     : GenerationTarget encountered exception accepting command : Error executing DDL "
    alter table `member` 
       add constraint UK_mbmcqelty0fbrvxp1q58dn57t unique (`email`)" via JDBC Statement

org.hibernate.tool.schema.spi.CommandAcceptanceException: Error executing DDL "
```
</details>





### 무슨 오류?

오류 내용을 보면, DDL 수행중 오류, 식별자(indifier) 관련해서 DB관련 예약어 오류로 보인다.

구글링과 인프런 김영한님의 강의 질문에 찾아봐도, 스택오버플로우에도 없었다.

하지만 문제는 그게 아니였다.

2일동안 삽질을 한 탓에 원인을 찾았다.

원인은 @DataJpaTest의 기본 설정을 잘 모르고 사용했던것이였다..

 

## 먼저 결론을 해결방법부터 적어보자면

* 기본값들로 세팅되어있는 H2 가 아닌 내가 설정한 property 파일로 된 H2로 사용하고자 한다면
* `@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)` 를 테스트 클래스 위에 붙여야 한다.



```java
@ActiveProfiles("test")
@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    void save() {
        memberRepository.save(
                Member.builder()
                        .age(1)
                        .email("email@email.com")
                        .name("name")
                        .build()
        );

    }
}
```

* 패키지와 프로퍼티 설정 파일을 같이 첨부한다



<details>
<summary>길어서 접기 펼치기로.. </summary>  



* Test Property Source
  * yml 형식을 사용한다.
  * main 디렉토리가 아닌 test 디렉토리의 resources를 사용한다.

<img src="https://blog.kakaocdn.net/dn/c35Nsh/btrKIxS203M/BHezvkO5qUW8iZqiEe6Yn1/img.png">



* application-test.yml

```yaml
spring:

  config:
    activate:
      on-profile: test

  datasource:
    username: sa
    password:
    driver-class-name: org.h2.Driver
    url: jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false;MODE=MySQL

  jpa:
    hibernate:
      ddl-auto: create-drop
    properties:
      hibernate.dialect: org.hibernate.dialect.MySQL8Dialect
      hibernate:
        format_sql: true
        globally_quoted_identifiers: true
    show-sql: true
    generate-ddl: true
    database-platform: org.hibernate.dialect.MySQL8Dialect
    database: h2

  h2:
    console:
      enabled: true

```

</details>



* @DataJpaTest에는 자동으로 설정해주는 옵션이 상당히 많이 붙어있다.

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@BootstrapWith(DataJpaTestContextBootstrapper.class)
@ExtendWith(SpringExtension.class)
@OverrideAutoConfiguration(enabled = false)
@TypeExcludeFilters(DataJpaTypeExcludeFilter.class)
@Transactional
@AutoConfigureCache
@AutoConfigureDataJpa
@AutoConfigureTestDatabase
@AutoConfigureTestEntityManager
@ImportAutoConfiguration
public @interface DataJpaTest {
	...
}
```

* @ExtendWith(SpringExtension.class) 도 붙어있어서 따로 사용 안해도 된다.
* @Transactional 도 붙어있어서 테스트 종료시 자동으로 롤백도 해준다.
* 다양한  AutoConfigure 가 붙어있어 자동으로 설정해준다.
* Test DataBase라는 이름이 보인다 이 어노테이션을 보자. 
  * @AutoConfigureTestDatabase

```java
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@ImportAutoConfiguration
@PropertyMapping("spring.test.database")
public @interface AutoConfigureTestDatabase {

	Replace replace() default Replace.ANY;

	EmbeddedDatabaseConnection connection() default EmbeddedDatabaseConnection.NONE;

	enum Replace {
		ANY,
		AUTO_CONFIGURED,
		NONE
	}

}
```



* 애플리케이션 정의 또는 자동 구성된 DataSource 대신 사용할 테스트 데이터베이스를 구성하기 위해 테스트 클래스에 적용할 수 있는 어노테이션이다. 
* 즉 우리가 테스트 할 때 사용할  데이터베이스를 직접 정의해서 사용할 수 있게 도와주는 어노테이션이다.
* 기본적으로 정의되어있는 DataSource를 사용해서 H2 데이터베이스를 사용한다.
  * 우리가 property로 작성한 DataSource를 사용하는 것이 아니다 

* 기본 설정은 ANY로 되어있고, ANY로 되어있으면 기본 DataSource를 사용한 H2를 사용한다
  * ANY : 자동 구성 또는 수동 정의 여부에 관계없이 DataSource를 교체
  * AUTO_CONFIGURED : 자동 설정된 경우에만 DataSource를 교체
  * NONE : 기본 DataSource를 교체하지 않음

* 우리가 application.yml에 설정을 해두어도 Replace.ANY설정에 의해 DataSource를 in-memory설정으로 변경한다.



### 이것이 뭐가 문제였을까?

앞서 말했듯이, H2를 MySQL 모드로 사용할 수 있게 기본 H2 In-memory가 아닌

H2의 MODE와 hibernate의 Dialect를 MySQL 설정하여 사용하려고 하였다.

그래서 application-test.yml에 각종 설정들을 수정하는 것을 작성하였지만 적용이 안되고,

자동으로 구성된 H2 In-memory 를 사용하였던 것이다. 



application-test.yml이다

```properties
spring:

  config:
    activate:
      on-profile: test

  datasource:
    username: sa
    password:
    driver-class-name: org.h2.Driver
    url: jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false;MODE=MySQL # url을 잘 보자

  jpa:
    hibernate:
      ddl-auto: create-drop
    properties:
      hibernate.dialect: org.hibernate.dialect.MySQL8Dialect
      hibernate:
        format_sql: true
        globally_quoted_identifiers: true
    show-sql: true
    generate-ddl: true
    defer-datasource-initialization: true
    database-platform: org.hibernate.dialect.MySQL8Dialect
    database: h2

  h2:
    console:
      enabled: true

```







실행해서 나오는 로그들을 보자 

![image-20220827223423787](/Users/ysk/study/study_repo/tdd/junit5/images//image-20220827223423787.png)

* jdbc:h2:mem:05497116-9d3f-43ee-94d6-6de829c82dc6 ??????

나는  jdbc:h2:mem:test로 설정 하였는데?



@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) 옵션이 없는 채로 디버그를 돌려보면

datasource의 커넥션의 메타데이터가 org.h2.jdbc의 JdbcDatabaseMetaData 구현체가 주입되어있다.



옵션을 설청한채로 디버그를 돌려보면 HikariProxyDatabaseMetaData 구현체가 주입되어있다.

정확히 어떻게 구현이 되어있는지는 모르겠으나,

application.properties나 yml에 정의한 설정을 그대로 사용하는 것이 아닌, 기본적으로 내장되어 구현되어 있는 DataSource와 Connection을 사용하여 우리 설정을 사용하지 않아서 구현체로 다른것을 사용한것 같다. 

















```
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
```