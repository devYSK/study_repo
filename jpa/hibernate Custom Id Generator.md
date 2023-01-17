

## Jpa Hibernate Custom Id Generator



## Custom 생성 전략

custom ID를 생성하기 위해 Custom Generator 를 생성하기 위해서는 `IdentifierGenerator` 인터페이스를 이용해서 `generate()` 메서드를 구현해야 한다.



> `IdentifierGenerator` 인터페이스구현 클래스는 반드시 `public` 으로 선언된 기본 생성자가 있어야한다



```java
package org.hibernate.id;

public interface IdentifierGenerator extends Configurable, ExportableProducer {
  ...
  Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException;

}
```

IdentifierGenerator를 상속받아 generated 메소드를 구현해야 하며, 이 메소드가 id값을 생성하는 메소드이다. 

### 예제 - 랜덤 생성 전략

예제로 1~10000까지  랜덤으로 Id 생성하는 generate를 만들었다

```java
public class RandomIdGenerator implements IdentifierGenerator {

    // hibernate 6에서는 Serializable 반환 타입이 아닌 Object 반환 타입이다.
    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object)
        throws HibernateException {
        Random random = new Random();
        int randomId = random.nextInt(10000);
        return (long) randomId;
    }

}
```

  

다음 Entity에 생성한 Generator를 지정해줘야 한다.

```java
@Entity
public class RandomIdEntity {

    @Id
    @GeneratedValue(generator = "random_id")
    @GenericGenerator(
        name = "random_id",
        strategy = "com.ys.jpa_example.idgenerator.RandomIdGenerator"
    )
    private Long id;

}
```

`GenericGenerator` annotation과 `GeneratedValue` annotation을  ID 생성이 필요한 컬럼 속성값 위에 정의하면 사용할 수 있다.

>  `@GenericGenerator` 를 사용하여 Generator를 정의한다.

* `@GenericGenerator` : 
  * `name` :  @GeneratedValue의 generator modifier에서 사용할 이름
    * `name`은 개발자가 지정하고 unique 해야한다.
  * `stategy` :  IdentifierGenerator 인터페이스를 구현한 클래스 이름. 전체 패키지를 포함한 클래스 이름을 적는다. 
    * fully qualified class name을 지정
  * **만약 GenericGenerator에서 지정한 name과 GeneratedValue의 generator 값이 다르다면, hibernate_sequence 테이블을 생성하고 sequence를 생성해서 값을 넣게 되므로 주의해야 한다. **

* `@GeneratedValue`
  * strategy 를 통하여 정의되어있는 기본 전략들을 사용할 수 있다. default AUTO
    * GenerationType.AUTO, TABLE, SEQUENCE, IDENTITY
  * SequenceGenerator, TableGenerator에  기본 키 generator 이름을 지정한다. default persistence가 제공하는 ID Generator

**생성되는 ID는 새로운 Entity객체를 생성하고, Persist가 되는 단계에 개발된 Generator가 동작하게 된다.**



## 예제 - 접두사(Prefix) 아이디 생성 전략 

문자열 접두사 (prefix) 와 숫자 를 포함하는 Generator

```java
@Slf4j
public class PrefixGenerator implements IdentifierGenerator {

    private String prefix;

    @Override
    public Serializable generate(
        SharedSessionContractImplementor session, Object obj)
        throws HibernateException {

        String query = String.format("select %s from %s",
            session.getEntityPersister(obj.getClass().getName(), obj)
                .getIdentifierPropertyName(),
            obj.getClass().getSimpleName());

        log.info("query : {} ", query);

        Stream ids = session.createQuery(query).stream();

        Long max = ids.map(o -> o.toString().replace(prefix + "-", ""))
            .mapToLong(value -> Long.parseLong(value.toString()))
            .max()
            .orElse(0L);

        return prefix + "-" + (max + 1);
    }

    @Override
    public void configure(Type type, Properties properties,
        ServiceRegistry serviceRegistry) throws MappingException {
        prefix = properties.getProperty("prefix");
    }

}
```

먼저, `prefix-XX` 형식의 기존 기본 키에서 가장 높은 숫자를 찾는다. 그런 다음 찾은 최대 수에 1을 더하고 *접두사* 속성(prefix)을 추가하여 새로 생성된 id 값을 가져온다.

또한 `configure()` 메서드 에서 접두사(Prefix) 속성 값을 설정할 수 있도록 Configurable 인터페이스를 구현하는데, Configurable 인터페이스는 IdentifierGenerator가 상속받고 있eㅏ. 

* Configurable는 org.hibernate.id 패키지에 존재한다.
* Id값을 설정하는 Entity 내의 @GenericGenerator에서 파라미터 값을 설정할 수 있다.

```java
@Entity
@Getter
public class PrefixEntity {

    @Id
    @GeneratedValue(generator = "hkbks-generator")
    @GenericGenerator(name = "hkbks-generator",
        parameters = @Parameter(name = "prefix", value = "ys"),
        strategy = "com.ys.jpa_example.idgenerator.PrefixGenerator")
    private String id;

}
```

* @Parameter로 필요한 key - value 형식으로 파라미터를 넘길 수 있는데, 이 값은  Configurable인터페이스를 상속받고 configure() 메서드에서 properties에서 꺼낼 수 있다.
* @Parametrs로 여러 @Parameter를 넘길 수도 있다.

```java
@Slf4j
public class PrefixGenerator implements IdentifierGenerator {

    private String prefix;
    
    ... 생략
      
    @Override
    public void configure(Type type, Properties properties,
        ServiceRegistry serviceRegistry) throws MappingException {

        String prefix = ConfigurationHelper.getString("prefix", properties);
      
        prefix = properties.getProperty("prefix");
    }

}
```

* 방법 1 : org.hibernate.internal.util.config.ConfigurationHelper.getString(String name, Map values) 로 꺼내기
* 방법 2 : 파라미터로 넘어온 properties에서 꺼내기 



다만, 이렇게 된다면 jpa가 select로 먼저 조회를 하고, 후에 insert 쿼리를 날리게 된다.

```sql
// query 1 

Hibernate FormatSql(P6Spy sql, Hibernate format): 
    select
        prefixenti0_.id_id as col_0_0_ 
    from
        prefix_entity prefixenti0_

----
// query2

 Hibernate FormatSql(P6Spy sql, Hibernate format): 
    insert 
    into
        prefix_entity
        (id) 
    values
        ('ys-1')
```

 

### @GenericGenertor 와 @GeneratedValue

```java
@Entity
@Table(name="table")
public class Entity {

    @Id
    @GenericGenerator(name = "idGenerator", // @GeneratedValue의 generator modifier에서 사용할 이름
            strategy = "SeqGenerator", // IdentifierGenerator 인터페이스를 구현한 클래스 이름. 전체 패키지를 포함한 클래스 이름을 적어야 합니다.
            parameters = @org.hibernate.annotations.Parameter( // Configurable 인터페이스 구현 클래스에 넘겨줄 파라미터 설정
                    name = SeqGenerator.SEQ_GENERATOR_PARAM_KEY, // 파라미터의 키 이름. SeqGenerator 클래스에 선언해둔 상수를 사용
                    value = PROCEDURE_PARAM // 위의 name modifier에 선언한 키에 넘겨줄 파라미터 값
            )
    )
    @GeneratedValue(generator = "idGenerator") // @GenericGenerator의 name modifier 에 지정한 이름
    @Column(name = "ID")
    private String id;
}
```



## 예제 - LocalDateTime + UUID을 조합한 아이디 생성 전략

`현재 날짜 + YS + UUID`라는 문자를 조합해서 만들고 싶다면? 

```
20230117YSbe50e687-57c2-4a4a-a221-e9598eaa9979
```

### 1. Generator 정의

```java
public class TimeAndUuidGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object)
        throws HibernateException {

        String format = LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE);

        return String.format("%sYS%s", format, UUID.randomUUID());
    }

}
```

* String.format을 보면 현재날짜만(20230117) 뽑고 YS + randomUUID를 조합한다.

### 2. Entity 정의

```java
@Entity
@Getter
public class TimeEntity {

    @Id
    @GeneratedValue(generator = "time_uuid_generator")
    @GenericGenerator(
        name = "time_uuid_generator",
        strategy = "com.ys.jpa_example.idgenerator.TimeAndUuidGenerator"
    )
    private String id;

}
```

### 3. 쿼리

```sql
Hibernate FormatSql(P6Spy sql, Hibernate format): 
    insert 
    into
        time_entity
        (id) 
    values
        ('20230117YS!82b4b3e9-a660-455d-9271-c541ab77fcc8')

------------------


saveId : 20230117YS!be50e687-57c2-4a4a-a221-e9598eaa9979
```

* P6spy를 써서 insert하는 쿼리가 보인다. 












### 참조

* https://www.baeldung.com/hibernate-identifiers

* https://docs.jboss.org/hibernate/orm/5.4/userguide/html_single/Hibernate_User_Guide.html#identifiers-generators-GenericGenerator

* https://kapentaz.github.io/jpa/Hibernate%EC%97%90%EC%84%9C-Custom-Value-%EC%83%9D%EC%84%B1%ED%95%98%EA%B8%B0/#

* https://techblog.woowahan.com/2607/
* https://kwonnam.pe.kr/wiki/java/hibernate/id_generator

* https://www.javanibble.com/introduction-identifiers-spring-data-jpa/

* https://zet-it-story.tistory.com/1

