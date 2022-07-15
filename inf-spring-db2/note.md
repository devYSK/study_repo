# 인프런 Spring DB 2편 - 데이터 접근 기술 - 김영한 님

# 전체 목차

1. 데이터 접근 기술 - 시작
2. 데이터 접근 기술 - 스프링 JdbcTemplate
3. 데이터 접근 기술 - 테스트
4. 데이터 접근 기술 - MyBatis
5. 데이터 접근 기술 - JPA
6. 데이터 접근 기술 - 스프링 데이터 JPA
7. 데이터 접근 기술 - Querydsl
8. 데이터 접근 기술 - 활용 방안
9. 스프링 트랜잭션 이해
10. 스프링 트랜잭션 전파1 - 기본
11. 스프링 트랜잭션 전파2 - 활용
12. 다음으로

## 적용 데이터 접근 기술

* JdbcTemplate
* MyBatis
* JPA, Hibernate
* 스프링 데이터 JPA
* Querydsl

## SQL Mapper 주요기능

* 개발자는 SQL만 작성하면 해당 SQL의 결과를 객체로 편리하게 매핑해준다.
* JDBC를 직접 사용할 때 발생하는 여러가지 중복을 제거해주고, 기타 개발자에게 여러가지 편리한 기능을제공한다.

## ORM 주요 기능

* JdbcTemplate이나 MyBatis 같은 SQL 매퍼 기술은 SQL을 개발자가 직접 작성해야 하지만, JPA를 사용하면 기본적인 SQL은 JPA가 대신 작성하고 처리해준다.
* 개발자는 저장하고 싶은 객체를 마치 자바 컬렉션에 저장하고 조회하듯이 사용하면 ORM 기술이 데이터베이스에 해당 객체를 저장하고 조회해준다.
* JPA는 자바 진영의 ORM 표준이고, Hibernate(하이버네이트)는 JPA에서 가장 많이 사용하는 구현체이다.
* 자바에서 ORM을 사용할 때는 JPA 인터페이스를 사용하고, 그 구현체로 하이버네이트를 사용한다고 생각하면 된다.
* 스프링 데이터 JPA, Querydsl은 JPA를 더 편리하게 사용할 수 있게 도와주는 프로젝트



# JdbcTemplate 소개와 설정
* SQL을 직접 사용하는 경우에 스프링이 제공하는 JdbcTemplate은 아주 좋은 선택지다.
* JdbcTemplate은 JDBC를 매우 편리하게 사용할 수 있게 도와준다


### 장점
* 설정의 편리함
  * JdbcTemplate은 spring-jdbc 라이브러리에 포함되어 있는데, 이 라이브러리는 스프링으로 JDBC를 사용할 때 기본으로 사용되는 라이브러리이다. 그리고 별도의 복잡한 설정 없이 바로 사용할
수 있다.

* 반복 문제 해결
  * JdbcTemplate은 템플릿 콜백 패턴을 사용해서, JDBC를 직접 사용할 때 발생하는 대부분의 반복 작업을 대신 처리해준다.
  * 개발자는 SQL을 작성하고, 전달할 파리미터를 정의하고, 응답 값을 매핑하기만 하면 된다.
  * 우리가 생각할 수 있는 대부분의 반복 작업을 대신 처리해준다.
    * 커넥션 획득
    * statement 를 준비하고 실행
    * 결과를 반복하도록 루프를 실행
    * 커넥션 종료, statement , resultset 종료
    * 트랜잭션 다루기 위한 커넥션 동기화
    * 예외 발생시 스프링 예외 변환기 실행

* 단점
  * 동적 SQL을 해결하기 어렵다.

* 라이브러리 추가
```groovy
//JdbcTemplate 추가
implementation 'org.springframework.boot:spring-boot-starter-jdbc'
```

* 기본
* dataSource 를 의존 관계 주입 받고 생성자 내부에서 JdbcTemplate 을 생성한다. 
* 스프링에서는 JdbcTemplate 을 사용할 때 관례상 이 방법을 많이 사용한다.
  * JdbcTemplate 을 스프링 빈으로 직접 등록하고 주입받아도 된다.
* `save() `
* template.update() : 데이터를 변경할 때는 update() 를 사용하면 된다.
  * INSERT , UPDATE , DELETE SQL에 사용한다.
  * template.update() 의 반환 값은 int 인데, 영향 받은 로우 수를 반환한다.
  * 데이터를 저장할 때 PK 생성에 identity (auto increment) 방식을 사용하기 때문에, PK인 ID 값을
  개발자가 직접 지정하는 것이 아니라 비워두고 저장해야 한다. 
    * 그러면 데이터베이스가 PK인 ID를 대신 생성해준다.
  * 문제는 이렇게 데이터베이스가 대신 생성해주는 PK ID 값은 데이터베이스가 생성하기 때문에, 데이터베이스에 INSERT가 완료 되어야 생성된 PK ID 값을 확인할 수 있다.
  * KeyHolder 와 connection.prepareStatement(sql, new String[]{"id"}) 를 사용해서 id 를 지정해주면 INSERT 쿼리 실행 이후에 데이터베이스에서 생성된 ID 값을 조회할 수 있다.
  * 물론 데이터베이스에서 생성된 ID 값을 조회하는 것은 순수 JDBC로도 가능하지만, 코드가 훨씬 더 복잡하다.
  * JdbcTemplate이 제공하는 SimpleJdbcInsert 라는 훨씬 편리한 기능이 있다.

* `update()`
* template.update() : 데이터를 변경할 때는 update() 를 사용하면 된다.
  * ? 에 바인딩할 파라미터를 순서대로 전달하면 된다.
  * 반환 값은 해당 쿼리의 영향을 받은 로우 수 이다. 

* `findById()`
* template.queryForObject()
  * 결과 로우가 하나일 때 사용한다.
* RowMapper 는 데이터베이스의 반환 결과인 ResultSet 을 객체로 변환한다.
  * 결과가 없으면 EmptyResultDataAccessException 예외가 발생한다.
  * 결과가 둘 이상이면 IncorrectResultSizeDataAccessException 예외가 발생한다.

* ### queryForObject() 인터페이스 정의
```java
<T> T queryForObject(String sql, RowMapper<T> rowMapper, Object... args) throws DataAccessException;
```

* `findAll()`
  * 데이터를 리스트로 조회한다. 그리고 검색 조건으로 적절한 데이터를 찾는다.
* template.query()
  * 결과가 하나 이상일 때 사용한다.
* RowMapper 는 데이터베이스의 반환 결과인 ResultSet 을 객체로 변환한다.
* 결과가 없으면 빈 컬렉션을 반환한다.

* `itemRowMapper()`
* 데이터베이스의 조회 결과를 객체로 변환할 때 사용한다.
* JDBC를 직접 사용할 때 ResultSet 를 사용했던 부분을 떠올리면 된다.
* 차이가 있다면 다음과 같이 JdbcTemplate이 다음과 같은 루프를 돌려주고, 개발자는 RowMapper 를 구현해서 그 내부 코드만 채운다고 이해하면 된다

```java
while(resultSet 이 끝날 때 까지) {
        rowMapper(rs, rowNum)
}
// ----------------------
private RowMapper<Item> itemRowMapper() {
    return (rs, rowNum) -> {
        Item item = new Item();
        item.setId(rs.getLong("id"));
        item.setItemName(rs.getString("item_name"));
        item.setPrice(rs.getInt("price"));
        item.setQuantity(rs.getInt("quantity"));
        return item;
    };
}
```


### jdbc 로그 추가(sql)
```properties
## jdbcTemplate sql log
logging.level.org.springframework.jdbc=debug
```

## JdbcTemplate - 이름 지정 파라미터 1

1. 순서대로 바인딩. 
* JdbcTemplate을 기본으로 사용하면 파라미터를 순서대로 바인딩 한다
```java
String sql = "update item set item_name=?, price=?, quantity=? where id=?";
template.update(sql,
        itemName,
        price,
        quantity,
        itemId);
```

* 여기서는 itemName , price , quantity 가 SQL에 있는 ? 에 순서대로 바인딩 된다.

* 파라미터를 순서대로 바인딩 하는 것은 편리하기는 하지만, 순서가 맞지 않아서 버그가 발생할 수도 있으므로 주의해서 사용해야 한다.

2. 이름 지정 바인딩
* NamedParameterJdbcTemplate 라는 이름을 지정해서
  파라미터를 바인딩 하는 기능 제공

* this.template = new NamedParameterJdbcTemplate(dataSource)
  * NamedParameterJdbcTemplate 도 내부에 dataSource 가 필요하다.
  * JdbcTemplateItemRepositoryV2 생성자를 보면 의존관계 주입은 dataSource 를 받고 내부에서 NamedParameterJdbcTemplate 을 생성해서 가지고 있다. 스프링에서는 JdbcTemplate 관련 기능을 사용할 때 관례상 이 방법을 많이 사용한다.
  * NamedParameterJdbcTemplate 을 스프링 빈으로 직접 등록하고 주입받아도 된다

* NamedParameterJdbcTemplate 은 데이터베이스가 생성해주는 키를 매우 쉽게 조회하는 기능도
  제공해준다.
```java
template.update(sql, param, keyHolder); 
Long key = keyHolder.getKey().longValue();
```

* `insert into item (item_name, price, quantity) values (:itemName, :price, :quantity)"`
  * ':파라미터 명' 를 이용해서 파라마티를 받는다.  

* 파라미터를 전달하려면 Map 처럼 key , value 데이터 구조를 만들어서 전달해야 한다.
* 여기서 key 는 `:파리이터이름` 으로 지정한, 파라미터의 이름이고 , value 는 해당 파라미터의값이 된다.

* 이름 지정 바인딩에서 자주 사용하는 파라미터의 종류
1. Map
2. SqlParameterSource
   1. MapSqlParameterSource
   2. BeanPropertySqlParameterSource

### 1. Map
* 단순히 Map 사용
```java
Map<String, Object> param = Map.of("id", id);
Item item = template.queryForObject(sql, param, itemRowMapper());
```

### 2. MapSqlParameterSource
* Map과 유사하지만 SQL 타입을 지정할 수 있는 특화된 기능 제공
* SqlParameterSource의 구현체
* MapSqlParameterSource는 메서드 체인을 통해 편리한 사용법 제공

```java
SqlParameterSource param = new MapSqlParameterSource()
    .addValue("itemName", updateParam.getItemName())
    .addValue("price", updateParam.getPrice())
    .addValue("quantity", updateParam.getQuantity())
    .addValue("id", itemId); //이 부분이 별도로 필요하다.
template.update(sql, param);
```

### 3. BeanPropertySqlParameterSource
* 자바 빈 프로퍼티 규약을 통해서 자동으로 파라미터 객체 생성
* SqlParameterSource 인터페이스의 구현체이다.
  * 예) getXxx() -> xxx, getItemName() -> itemName
  * 예를 들어서 getItemName() , getPrice() 가 있으면 다음과 같은 데이터를 자동으로 만들어낸다.
    * key=itemName, value=상품명 값
    * key=price, value=가격 값

* BeanPropertySqlParameterSource 가 많은 것을 자동화 해주기 때문에 가장 좋아보이지만,
BeanPropertySqlParameterSource 를 항상 사용할 수 있는 것은 아니다.
* 예를 들어서 update() 에서는 SQL에 :id 를 바인딩 해야 하는데, update() 에서 사용하는
ItemUpdateDto 에는 itemId 가 없다. 따라서 BeanPropertySqlParameterSource 를 사용할 수 없고, 대신에 MapSqlParameterSource 를 사용.

### BeanPropertyRowMapper

```java
// 기존 RowMapper
private RowMapper<Item> itemRowMapper() {
        return (rs, rowNum) -> {
        Item item = new Item();
        item.setId(rs.getLong("id"));
        item.setItemName(rs.getString("item_name"));
        item.setPrice(rs.getInt("price"));
        item.setQuantity(rs.getInt("quantity"));
        return item;
        };
}

// BeanPropertyRowMapper
private RowMapper<Item> itemRowMapper() {
        return BeanPropertyRowMapper.newInstance(Item.class); 
        //camel 변환 지원
}
```

* BeanPropertyRowMapper 는 ResultSet 의 결과를 받아서 자바빈 규약에 맞추어 데이터를 변환한다.
* 리플렉션 같은 기능을 사용하여 변환. 

### 별칭

* select item_name 의 경우 setItem_name() 이라는 메서드가 없기 때문에 골치가 아프다.
* 이런 경우 개발자가 조회 SQL을 다음과 같이 고치면 된다.
  * `String sql = "select item_name as itemName"`

### 자바 언어의 네이밍 관례와 데이터베이스의 네이밍 관례

* 자바 객체는 카멜( camelCase ) 표기법을 사용한다. 
* 반면에 관계형 데이터베이스에서는 주로 언더스코어를 사용하는 snake_case 표기법을 사용한다.
* BeanPropertyRowMapper 는 언더스코어 표기법을 카멜로 자동 변환해준다.
* 따라서 select item_name 으로 조회해도 setItemName() 에 문제 없이 값이 들어간다.
* 정리하면 snake_case 는 자동으로 해결되니 그냥 두면 되고, 컬럼 이름과 객체 이름이 완전히 다른
경우에는 조회 SQL에서 별칭을 사용하면 된다


## SimpleJdbcInsert

* JdbcTemplate은 `INSERT SQL를 직접 작성하지 않아도 되도록 SimpleJdbcInsert 라는 편리한
  기능을 제공한다.`

```java
public class JdbcTemplateItemRepositoryV3 implements ItemRepository {

  private final NamedParameterJdbcTemplate template;
  private final SimpleJdbcInsert jdbcInsert;

  public JdbcTemplateItemRepositoryV3(DataSource dataSource) {
    this.template = new NamedParameterJdbcTemplate(dataSource);
    // 이부분 
    this.jdbcInsert = new SimpleJdbcInsert(dataSource)
            .withTableName("item")
            .usingGeneratedKeyColumns("id");
  }
}
```

* withTableName : 데이터를 저장할 테이블 명을 지정한다.
* usingGeneratedKeyColumns : key 를 생성하는 PK 컬럼 명을 지정한다.
* usingColumns : INSERT SQL에 사용할 컬럼을 지정한다. 특정 값만 저장하고 싶을 때 사용한다. 생략할 수 있다.
* SimpleJdbcInsert 는 생성 시점에 데이터베이스 테이블의 메타 데이터를 조회한다. 
* 따라서 어떤 컬럼이 있는지 확인 할 수 있으므로 usingColumns 을 생략할 수 있다. 
* 만약 특정 컬럼만 지정해서 저장하고 싶다면 usingColumns 를 사용하면 된다.

## JdbcTemplate 기능 정리

* 주요 기능
  * JdbcTemplate : 순서 기반 파라미터 바인딩을 지원한다.
  * NamedParameterJdbcTemplate : 이름 기반 파라미터 바인딩을 지원한다. (권장)
  * SimpleJdbcInsert : INSERT SQL을 편리하게 사용할 수 있다.
  * SimpleJdbcCall 스토어드 프로시저를 편리하게 호출할 수 있다.
> 참고
> 스토어드 프로시저를 사용하기 위한 SimpleJdbcCall 에 대한 자세한 내용은 다음 스프링 공식 메뉴얼을
참고하자.
>
> https://docs.spring.io/spring-framework/docs/current/reference/html/dataaccess.html#jdbc-simple-jdbc-call-1


참고
> 스프링 JdbcTemplate 사용 방법 공식 메뉴얼  
> https://docs.spring.io/spring-framework/docs/current/reference/html/dataaccess.html#jdbc-JdbcTemplate

### 단건 조회 - 숫자 조회
```java
int countOfActorsNamedJoe = jdbcTemplate.queryForObject(
"select count(*) from t_actor where first_name = ?", Integer.class, "Joe");
```

### 단건 조회 - 객체 조회

```java
Actor actor = jdbcTemplate.queryForObject(
    "select first_name, last_name from t_actor where id = ?",
    (resultSet, rowNum) -> {
        Actor newActor = new Actor();
    newActor.setFirstName(resultSet.getString("first_name"));
    newActor.setLastName(resultSet.getString("last_name"));
    return newActor;
    },
1212L);
```


### 변경(INSERT, UPDATE, DELETE)
* 데이터를 변경할 때는 jdbcTemplate.update() 를 사용하면 된다. 참고로 int 반환값을 반환하는데, SQL 실행 결과에 영향받은 로우 수를 반환한다.

### 스토어드 프로시저 호출
```java
jdbcTemplate.update(
        "call SUPPORT.REFRESH_ACTORS_SUMMARY(?)",
        Long.valueOf(unionId));
```

참고
> JOOQ라는 기술도 동적쿼리 문제를 편리하게 해결해주지만 사용자가 많지 않아서 강의에서 다루지는 않는다


# JDBC 테스트

* 테스트에서 매우 중요한 원칙은 다음과 같다.
* `테스트는 다른 테스트와 격리해야 한다.`
* `테스트는 반복해서 실행할 수 있어야 한다.`


## 테스트 - 데이터 롤백

* 트랜잭션과 롤백 전략  

  * 테스트가 끝나고 나서 트랜잭션을 강제로 롤백해버리면 데이터가 깔끔하게 제거된다.  
  * 테스트를 하면서 데이터를 이미 저장했는데, 중간에 테스트가 실패해서 롤백을 호출하지 못해도 괜찮다.  
  * 트랜잭션을 커밋하지 않았기 때문에 데이터베이스에 해당 데이터가 반영되지 않는다.  
  * 이렇게 트랜잭션을 활용하면 테스트가 끝나고 나서 데이터를 깔끔하게 원래 상태로 되돌릴 수 있다
  
### 1. 테스트에 직접 트랜잭션 추가

```java
@SpringBootTest
class ItemRepositoryTest {

  @Autowired
  ItemRepository itemRepository;

  @Autowired
  PlatformTransactionManager transactionManager;

  TransactionStatus status;

  @BeforeEach
  void beforeEach() {
    //트랜잭션 시작
    status = transactionManager.getTransaction(new DefaultTransactionDefinition());
  }

  @AfterEach
  void afterEach() {
    //MemoryItemRepository 의 경우 제한적으로 사용
    if (itemRepository instanceof MemoryItemRepository) {
      ((MemoryItemRepository) itemRepository).clearStore();
    }

    transactionManager.rollback(status); // 트랜잭션 종료 
  }
}
```
* PlatformTransactionManager transactionManager

* TransactionStatus status

* 트랜잭션 시작 : transactionManager.getTransaction(new DefaultTransactionDefinition())
* 트랜잭션 종료 : transactionManager.rollback(status)


* 트랜잭션 관리자는 PlatformTransactionManager 를 주입 받아서 사용하면 된다. 
  * 참고로 스프링 부트는 자동으로 적절한 트랜잭션 매니저를 스프링 빈으로 등록해준다. (앞서 학습한 스프링 부트의 자동 리소스
  등록 장을 떠올려보자.)

* @BeforeEach : 각각의 테스트 케이스를 실행하기 직전에 호출된다. 따라서 여기서 트랜잭션을 시작하면
  된다. 그러면 각각의 테스트를 트랜잭션 범위 안에서 실행할 수 있다.
  * transactionManager.getTransaction(new DefaultTransactionDefinition()) 로 트랜잭션을 시작한다.

* @AfterEach : 각각의 테스트 케이스가 완료된 직후에 호출된다. 따라서 여기서 트랜잭션을 롤백하면 된다.
  그러면 데이터를 트랜잭션 실행 전 상태로 복구할 수 있다.
  * transactionManager.rollback(status) 로 트랜잭션을 롤백한다

### 2. @Transactional 어노테이션 

* 스프링은 테스트 데이터 초기화를 위해 트랜잭션을 적용하고 롤백하는 방식을 `@Transactional`
  애노테이션 하나로 깔끔하게 해결해준다.

```java
@Transactional
@SpringBootTest
class ItemRepositoryTest {
  ...
}
```

## `@Transactional 원리`

* 스프링이 제공하는 @Transactional 애노테이션은 로직이 성공적으로 수행되면 커밋하도록 동작한다
* @Transactional 이 테스트에 있으면 스프링은 테스트를 트랜잭션 안에서 실행하고, 테스트가 끝나면 트랜잭션을 자동으로 롤백시켜 버린다!


참고
* 테스트 케이스의 메서드나 클래스에 @Transactional 을 직접 붙여서 사용할 때 만 이렇게 동작한다.
* 그리고 트랜잭션을 테스트에서 시작하기 때문에 서비스, 리포지토리에 있는 @Transactional 도 테스트에서 시작한 트랜잭션에 참여한다. 
* 테스트에서 트랜잭션을 실행하면 테스트 실행이 종료될 때 까지 테스트가 실행하는 모든 코드가 같은 트랜잭션 범위에 들어간다고 이해하면 된다. 
* 같은 범위라는 뜻은 쉽게 이야기해서 같은 트랜잭션을 사용한다는 뜻이다. 그리고 같은 트랜잭션을 사용한다는 것은 같은 커넥션을 사용한다는 뜻이기도 하다.)


* @Transactional 덕분에 아주 편리하게 다음 원칙을 지킬수 있게 되었다.
  * 테스트는 다른 테스트와 격리해야 한다.
  * 테스트는 반복해서 실행할 수 있어야 한다

### @Commit - 강제로커밋하기 
* @Transactional 을 테스트에서 사용하면 테스트가 끝나면 바로 롤백되기 때문에 테스트 과정에서 저장한 모든 데이터가 사라진다. 
* 가끔은 데이터베이스에 데이터가 잘 보관되었는지 최종 결과를 눈으로 확인하고 싶을 때는 @Commit 을 클래스 또는 메서드에 붙이면 테스트 종료후 롤백 대신 커밋이 호출된다. 
* 참고로 @Rollback(value = false) 를 사용해도 된다


## 테스트용 임베디드 모드 DB

테스트 케이스를 실행하기 위해서 별도의 데이터베이스를 설치하고, 운영하는 것은 상당히 번잡한
작업이다.   
단순히 테스트를 검증할 용도로만 사용하기 때문에 테스트가 끝나면 데이터베이스의 데이터를
모두 삭제해도 된다. 더 나아가서 테스트가 끝나면 데이터베이스 자체를 제거해도 된다.  

* 임베디드 모드
  * H2 데이터베이스는 자바로 개발되어 있고, JVM안에서 메모리 모드로 동작하는 특별한 기능을 제공한다.
    그래서 애플리케이션을 실행할 때 H2 데이터베이스도 해당 JVM 메모리에 포함해서 함께 실행할 수 있다.
    DB를 애플리케이션에 내장해서 함께 실행한다고 해서 임베디드 모드(Embedded mode)라 한다. 물론
    애플리케이션이 종료되면 임베디드 모드로 동작하는 H2 데이터베이스도 함께 종료되고, 데이터도 모두
    사라진다. 쉽게 이야기해서 애플리케이션에서 자바 메모리를 함께 사용하는 라이브러리처럼 동작하는
    것이다.
  
### 설정 빈 등록

```java
@Slf4j
@Import(JdbcTemplateV3Config.class)
@SpringBootApplication(scanBasePackages = "hello.itemservice.web")
public class ItemServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItemServiceApplication.class, args);
    }

    @Bean
    @Profile("test") // 테스트 프로필에서만 실행 
    public DataSource dataSource() {
        log.info("메모리 데이터베이스 초기화");
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:db;DB_CLOSE_DELAY=-1");
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        return dataSource;
    }
}
```

* @Profile("test")
  * 프로필이 test 인 경우에만 데이터소스를 스프링 빈으로 등록한다.
  * 테스트 케이스에서만 이 데이터소스를 스프링 빈으로 등록해서 사용하겠다는 뜻이다.
* dataSource()
  * jdbc:h2:mem:db : 이 부분이 중요하다. 데이터소스를 만들때 이렇게만 적으면 임베디드 모드(메모리 모드)로 동작하는 H2 데이터베이스를 사용할 수 있다.
  * DB_CLOSE_DELAY=-1 : 임베디드 모드에서는 데이터베이스 커넥션 연결이 모두 끊어지면 데이터베이스도 종료되는데, 그것을 방지하는 설정이다.
* 이 데이터소스를 사용하면 메모리 DB를 사용할 수 있다.

## 스프링 부트 - 기본 SQL 스크립트를 사용해서 데이터베이스를 초기화하는 기능

* 스프링 부트는 SQL 스크립트를 실행해서 애플리케이션 로딩 시점에 데이터베이스를 초기화하는 기능을 제공한다.

* src/test/resources/schema.sql 에 초기화용 스크립트 작성 

참고
> SQL 스크립트를 사용해서 데이터베이스를 초기화하는 자세한 방법은 다음 스프링 부트 공식 메뉴얼을 참고하자.  
> https://docs.spring.io/spring-boot/docs/current/reference/html/howto.html#howto.datainitialization.using-basic-sql-scripts

* `스프링 부트는 임베디드 데이터베이스에 대한 설정도 기본으로 제공한다.`
  * 위에서 작성한 테스트 프로필용 DataSource Bean을 작성 안해도 된다. 
* 스프링 부트는 데이터베이스에 대한 별다른 설정이 없으면 임베디드 데이터베이스를 사용한다.

* `src/test/resources 에 작성한 데이터베이스 접근 설정정보가 없어야 한다. `
* 렇게 별다른 정보가 없으면 스프링 부트는 임베디드 모드로 접근하는 데이터소스( DataSource )를
  만들어서 제공한다. 바로 앞서 우리가 직접 만든 데이터소스와 비슷하다 생각하면 된다

> 로그를 보면 다음 부분을 확인할 수 있는데 jdbc:h2:mem 뒤에 임의의 데이터베이스 이름이 들어가
있다. 이것은 혹시라도 여러 데이터소스가 사용될 때 같은 데이터베이스를 사용하면서 발생하는 충돌을
방지하기 위해 스프링 부트가 임의의 이름을 부여한 것이다.  
`conn0: url=jdbc:h2:mem:d8fb3a29-caf7-4b37-9b6c-b0eed9985454`

* 임베디드 데이터베이스 이름을 스프링 부트가 기본으로 제공하는 jdbc:h2:mem:testdb 로 고정하고
  싶으면 application.properties 에 다음 설정을 추가하면 된다
  
  * spring.datasource.generate-unique-name=false

* 참고
> 임베디드 데이터베이스에 대한 스프링 부트의 더 자세한 설정은 다음 공식 메뉴얼을 참고하자.  
> https://docs.spring.io/spring-boot/docs/current/reference/html/
data.html#data.sql.datasource.embedded









