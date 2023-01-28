# SpringBoot 테스트시 초기 SQL 데이터 삽입



Spring Boot 테스트 시 데이터를 로딩하는 방법은 [직접 코드를 작성하는 방법](https://docs.spring.io/spring-framework/docs/current/spring-framework-reference/testing.html#testcontext-executing-sql-programmatically)과 [`@Sql`을 사용하는 방법](https://docs.spring.io/spring-framework/docs/current/spring-framework-reference/testing.html#testcontext-executing-sql-declaratively) 2가지가 있다.



# 1. 직접 코드를 작성하여 데이터 로딩

* https://docs.spring.io/spring-framework/docs/current/spring-framework-reference/testing.html#testcontext-executing-sql-programmatically)



```java
@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductReviewControllerTest {

    @Autowired
    private DataSource dataSource;

    @BeforeAll
    public void beforeAll() throws Exception { 
        System.out.println("BeforeAll");
        try (Connection conn = dataSource.getConnection()) { 
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("/data/insert.sql"));  
        }
    }

    @AfterAll
    public void afterAll() throws Exception {
        System.out.println("AfterAll");
        try (Connection conn = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("/data/truncate.sql"));
        }
    }
    
}
```

> 경로는 resources/ 아래이다.



일반적으로 `@BeforeAll`은 static 메서드로 사용해서 `@Autowired`로 주입받은 변수를 참조해서 사용할 수 없다, 

그러나 `@TestInstance(TestInstance.Lifecycle.PER_CLASS)`를 사용하면 `@BeforeAll`을 붙인 메서드가 static 이 아니여서 그래서 주입받은 `dataSource`를 이용해서 DB connection 을 얻을 수 있다.



@TestInstance(TestInstance.Lifecycle.PER_CLASS)를 사용할때는 다음의 경우에만 사용하도록 노력해야 한다. 

- 테스트 데이터 구성
- 대용량 파일 로딩
- 기타 자원 로딩

>  어떤 한 인스턴스 변수를 테스트 사이에 공유하는 게 아니라 비용이 많이 드는 자원을 로딩해서 읽기로만 사용하는 수준이라면 도를 넘지 않는 수준의 경제적인 상태 공유라고 할 수 있다.  

테스트는 모두 독립적이어야 하므로 이렇게 테스트 클래스 인스턴스 하나를 계속 유지해서 테스트 메서드 사이에 상태를 공유하는 건 원칙적으로는 안티패턴일 수도 있는데, 인스턴스를 여러 번 생성해서 사용하는 것보다 훨씬 경제적인 것이다.  

그래도 쓰기 가능한 상태로 공유된다면 읽기만 결국에는 오염될 것이므로 이럴 때는 읽기만 수행하는 테스트 메서드만을 하나의 클래스에 따로 모아서 `@TestInstance(TestInstance.Lifecycle.PER_CLASS)`를 붙여 사용하면 된다.



# 2. @Sql 어노테이션 사용

@Sql 애노테이션은 지정한 스크립트를 실행해주는 애노테이션이다.  

스프링 3 버전에 추가된 ResourceDatabasePopulator를 사용해도 되지만, @Sql 애노테이션을 이용하면 매우 편리하게 테스트에서 데이터를 초기화할 수 있다.



# schema.sql 파일

때로는 JPA 기본 스키마 생성 메커니즘에 의존하고 싶지 않다면, 이러한 경우 사용자 지정 *schema.sql* 파일을 만들 수 있다 .

```java
CREATETABLE country (
id   INTEGERNOTNULL AUTO_INCREMENT,
name VARCHAR(128)NOTNULL,PRIMARYKEY (id)
);
```

Spring은이 파일을 선택하여 스키마 생성에 사용한다.

이 방법 사용 시 자동 스키마 생성을 해제하는 것도 중요하다.

```properties
spring.jpa.hibernate.ddl-auto=none
```



## 사용방법

1. 테스트를 실행하기 전에 사용할 쿼리 목록을 담은 파일을 작성한다. (각 쿼리는 ';'로 구분한다.)
2. @Sql 애노테이션을 테스트 클래스나 테스트 메서드에 적용한다.

```java
@Sql({"/employees_schema.sql", "/import_employees.sql"})
public class SpringBootInitialLoadIntegrationTest {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Test
	public void test LoadDataForTestClass() {
        assertEquals(3, employeeRepository.findAll().size());
    }
}
```

*@Sql* 어노테이션 의 속성은 다음 과 같다.

- **config**  SQL 스크립트에 대한 로컬 구성. - `@SqlConfig`
- **executionPhase** – BEFORE_TEST_METHOD 또는 *AFTER_TEST_METHOD* 중 스크립트를 실행할시기를 지정할 수도 있eㅏ.
- **구문(statements)** - 실행할 인라인 SQL 문을 선언 할 수 있다.
- **script(스크립트) – 값** 실행할 SQL 스크립트 파일의 경로를 선언 할 수 있습니다. *value* attribute. 의 별칭(alias).

*@Sql의* 어노테이션은 **클래스 레벨 또는 메소드 레벨에서 사용할 수 있습니다** . 해당 메소드에 어노테이션을 달아 특정 테스트 케이스에 필요한 추가 데이터를로드 할 수 있습니다.

```java
@Test
@Sql({"/import_senior_employees.sql"})
public void testLoadDataForTestCase() {
    assertEquals(5, employeeRepository.findAll().size());
}
```



### @SqlConfig

*@SqlConfig* 어노테이션 을 사용하여 **SQL 스크립트** 를 **구문 분석하고 실행하는 방법을 구성** 할 수 **있다** .

*@SqlConfig* 는 전역 구성 역할을하는 클래스 수준에서 선언 하거나, 특정 *@Sql* 어노테이션 을 구성하는 데 사용할 수 있다.

SQL 스크립트의 인코딩과 스크립트 실행을위한 트랜잭션 모드를 지정하는 예

```java
@Test
@Sql(scripts = {"/import_senior_employees.sql"}, 
     config = @SqlConfig(encoding = "utf-8", transactionMode = TransactionMode.ISOLATED))
public void testLoadDataForTestCase() {
   assertEquals(5, employeeRepository.findAll().size());
}

//
Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface SqlConfig {

	String dataSource() default "";

	String transactionManager() default "";

	TransactionMode transactionMode() default TransactionMode.DEFAULT;

	String encoding() default "";

	String separator() default "";

	String commentPrefix() default "";

	String[] commentPrefixes() default {};

	String blockCommentStartDelimiter() default "";

	String blockCommentEndDelimiter() default "";

	ErrorMode errorMode() default ErrorMode.DEFAULT;

	enum TransactionMode {

		DEFAULT,

		INFERRED,

		ISOLATED
	}

	enum ErrorMode {

		DEFAULT,

		FAIL_ON_ERROR,

		CONTINUE_ON_ERROR,

		IGNORE_FAILED_DROPS
	}

}

```

*@SqlConfig* 의 다양한 속성들

- *blockCommentStartDelimiter* – SQL 스크립트 파일에서 블록 어노테이션의 시작을 식별하는 구분 기호
- *blockCommentEndDelimiter* – SQL 스크립트 파일에서 블록 어노테이션의 끝을 나타내는 구분 기호
- *commentPrefix* – SQL 스크립트 파일에서 한 줄 어노테이션을 식별하기위한 접두사
- *dataSource* – 스크립트 및 명령문이 실행될 *javax.sql.DataSource* Bean의 이름
- *encoding* – SQL 스크립트 파일의 인코딩, 기본값은 플랫폼 인코딩.
- *errorMode* – 스크립트 실행 중 오류가 발생했을 때 사용되는 모드
- *separator* – 개별 명령문을 구분하는 데 사용되는 문자열, 기본값은 "–".
- *transactionManager* – 트랜잭션에 사용될 *PlatformTransactionManager* 의 빈 이름
- *transactionMode* – *트랜잭션* 에서 스크립트를 실행할 때 사용되는 모드



## 예제

예를 들어, 아래 코드는 각 테스트 메서드를 실행하기 전에

src/test/resources/test_sql 디렉토리의 아래의 

 item_default_option.sql 파일과 item_category.sql 파일, item.sql에 입력한 쿼리 목록을 실행한다.

```java
@SpringBootTest
class ApplicationTest {

	@Autowired
	private ItemRepository itemRepository;

	@Sql(scripts = {"/test_sql/item_default_option.sql", 			
                  "/test_sql/item_category.sql", 	
                  "/test_sql/item.sql"})
	@Test
	void insertTest() {
	  ...
	}
  
}
```

또는

```java
@Sql(scripts = {"/test_sql/item_default_option.sql", 
                "/test_sql/item_category.sql", 
                "/test_sql/item.sql"})
@SpringBootTest
class ApplicationTest {

	@Autowired
	private ItemRepository itemRepository;

	@Test
	void insertTest() {
	  ...
	}
  
}
```

@Sql에 명시한 파일은 테스트를 실행하는데 적합한 상태로 DB를 초기화하기 위해 DELETE, TRUNCATE, INSERT, CREATE와 같은 쿼리를 포함하게 된다.  
  

클래스에 @Sql 애노테이션을 적용하면 각 테스트 메서드마다 적용되며, 테스트 메서드에 적용하면 해당 테스트를 실행할 때에만 사용된다.   
  


예를 들어, 아래 코드와 같이 테스트 클래스와 메서드에 **각각 @Sql 애노테이션을 적용**하면, 

* `item_insert() 메서드`를 실행할 때에는 `"item.sql"` 만을 사용해서 쿼리를 실행, 
* `insert_and_find() 메서드`를 실행할 때에는 `"item_default_option.sql"``과 "item_category.sql"`을 사용해서 쿼리를 실행한다.

```java
@SpringBootTest
@Sql(scripts = {"/test_sql/item_default_option.sql", 
                "/test_sql/item_category.sql"})
public class ShopIntTest {

    @Autowired private MemberService memberService;

    @Test
    public void insert_and_find() {
        …
    }

    @Sql(scripts = {"/test_sql/item.sql"})
    @Test
    public void item_insert() {
        …
    }

}
```



**@Sql은 테스트 메서드 실행 전과 실행 후 중에서 언제 쿼리를 실행할지 여부를 지정할 수 있다. **

@Sql의 **executionPhase** 속성의 값으로 **ExecutionPhase enum 타입**에 정의된 `BEFORE_TEST_METHOD`나 `AFTER_TEST_METHOD`를 설정하면 된다. 

* 기본 값은 BEFORE_TEST_METHOD 이다.

다음 코드는 executionPhase 속성의 설정 예를 보여주고 있다.

```java
@Sql("init.sql")
@Sql(scripts="remove.sql", executionPhase=ExecutionPhase.AFTER_TEST_METHOD)
@Test public void someTest() { … }
```

자바8을 사용하면 @Sql 애노테이션을 여러 개 사용해서 실행할 쿼리를 지정할 수 있다. 

자바 7 이하 버전을 사용한다면, 아래 코드처럼 @SqlGroup 애노테이션을 이용하면 여러 개의 @Sql을 한 테스트 클래스나 메서드에 적용할 수 있다.

```java
@SqlGroup({
    @Sql("init.sql"), @Sql(scripts="clear.sql", executionPhase=ExecutionPhase.AFTER_TEST_METHOD)} )
@Test public void someTest() { … }
```



@Sql 애노테이션은 별도 설정을 하지 않으면 @ContextConfiguration에 지정한 설정 정보에 있는 DataSource 빈을 사용해서 스크립트를 실행하고, `트랜잭션 관리자가 존재할 경우 해당 트랜잭션 관리자를 이용해서 트랜잭션 범위 내에서 스크립트를 실행`한다.



## **@SqlGroup** 

Java 8 이상에서는 반복되는 어노테이션을 사용할 수 있다. 이 기능은 `@Sql` 어노테이션에도 사용할 수 있다.

 Java 7 이하의 경우  -  `@SqlGroup`   를 사용. **@SqlGroup** **어노테이션을** 사용하여 여러 @Sql 어노테이션을 선언 할 수 있다 .

```java
@SqlGroup({
	@Sql(scripts = "/employees_schema.sql", 
  	   config = @SqlConfig(transactionMode = TransactionMode.ISOLATED)),
	@Sql("/import_employees.sql")
})
public class SpringBootSqlGroupAnnotationIntegrationTest {
   
   @Autowired
   private EmployeeRepository employeeRepository;
   
   @Test
   public void testLoadDataForTestCase() {
     assertEquals(3, employeeRepository.findAll().size());
   }
}
```



## @SQl 주의할점

아래와 같이 `@Sql`을 사용해도 될 것 같지만 지정한 SQL 스크립트는 실행되지 않는다.

```java
@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ControllerTest {

    @Sql({"/data/insert.sql"})
    @BeforeAll
    public void beforeAll() throws Exception {
        System.out.println("BeforeAll");
    }

    @Sql({"/data/truncate.sql"})
    @AfterAll
    public void afterAll() throws Exception {
        System.out.println("AfterAll");
    }

}
```



## 결론 - *@Sql, @SqlConfig* 및 *@SqlGroup* 어노테이션

이 접근 방식은 기본적이고 간단한 시나리오에 더 적합하며, 모든 고급 데이터베이스 처리에는 [Liquibase](https://recordsoflife.tistory.com/liquibase-refactor-schema-of-java-app) 또는 [Flyway](https://recordsoflife.tistory.com/database-migrations-with-flyway) 와 같은보다 고급적이고 세련된 도구가 필요하다.



참조

- https://docs.spring.io/spring-framework/docs/current/reference/html
- https://www.baeldung.com/spring-boot-data-sql-and-schema-sql

* https://github.com/HomoEfficio/dev-tips/blob/master/Spring-Boot-JUnit5-Initial-Data.md

* https://javacan.tistory.com/entry/spring41-AtSql-annotation-intro
