# hibernate Custom Id Generator

 JPA 및 Hibernate 내에서 엔티티에 대한 식별자(Id)를 생성하는 다양한 옵션에 대해 정리한다.

식별자는 엔티티 기본 키를 모델링하고 특정 엔터티를 고유하게 식별하는 데 사용된다. 기본 키는 데이터베이스 내의 행을 고유하게 식별한다. 이론적으로 식별자는 기본 키와 일치할 필요가 없다. 식별자는 각 행을 고유하게 식별하는 열에 매핑되기만 하면 되기 때문이다. 

* 여기서는 기본 키와 식별자(Id) 는 일부로 같은 의미로 사용한다.



기본키와(pk) 식별자(Id)는 다음 제약조건이 제한된다

- `UNIQUE`- 값은 각 행을 고유하게 식별해야 한다.
- `NOT NULL`- 값은 null일 수 없다. 복합 ID (복합 키)의 경우 어떤 부분도 null이 될 수 없다.
- `IMMUTABLE`- 한 번 삽입된 값은 절대 변경할 수 없고, 변경되면 안된다..

식별자는 단일 값(단순) 또는 여러 값(복합)일 수 있다. 모든 엔터티는 식별자를 정의해야 한다.



## Generated Identifiers - 기본생성된 식별자

Hibernate는 간단한 식별자가 생성되는 기능을  `@GeneratedValue` 를 통해 제공한다. 

식별자 속성에 어노테이션을 추가하면 된다. Hibernate는 엔터티가 persistence 되면 식별자 값을 생성한다.



* javax.persistence.`GeneratedValue` 어노테이션 : 식별자 값이 generated한다는 것을 나타낸다.
* javax.persistence.`GenerationType` 어노테이션 : 식별자 값이 생성되는 방법을 지정한다. 

값이 생성되는 방법은 4가지 주요 키 생성 전략(strategy)가 있다.

* hibernate6 에서는 GenerationType.UUID가 추가되어 5가지가 있다. 

- `AUTO`- persistence provider(Jpa 구현체) 가  자동으로 특정 데이터베이스에 대해 적절한 생성 전략 결정
  - IdGeneratorStrategyInterpreter를 사용했지만, hibernate6부터 deprecated 되었다.
  - hibernate6부터 IdentityGenerationTypeStrategy로 대체되었다 
- `IDENTITY`- persistence provider(Jpa 구현체) 가 기본키 생성을 데이터베이스에게 위임한다.
  - MySQL의 경우 AUTO_INCREMENT를 사용하여 기본키를 생성한다.
  - hibernate6부터 IdentityGenerationTypeStrategy로 사용된다. 
- `SEQUENCE`- persistence provider(Jpa 구현체)가 데이터베이스 시퀀스를 사용하여 엔터티에 대한 기본 키를 할당한다.
  - `SequenceStyleGenerator` 클래스를 이용한다. 
- `TABLE`- persistence provider(Jpa 구현체)가 데이터베이스에 키 생성 전용 테이블을 만들고 이를 사용하여 기본키를 할당한다
- `UUID`(hibernate6 에서부터 지원) : RFC 4122 Universally Unique IDentifier를 생성하여 기본키 할당



## 미리 정의되어 있는 Hibernate Strategy

식별자를 생성하는 데 사용할 수 있는 Hibernate에서 제공하는 많은 일반 Generator가 구현되어있다. 모든 Generator는  `org.hibernate.id.IdentifierGenerator` 인터페이스를 구현한다.

```java
@Entity
public class MyEntity {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(
        name = "uuid",
        strategy = "org.hibernate.id.UUIDGenerator",
        parameters = {
            @Parameter(
                name = "uuid_gen_strategy_class",
                value = "org.hibernate.id.uuid.CustomVersionOneStrategy"
            )
        }
    )
    public UUID id;
```



IdentifierGeneratorFactory 클래스에 저장되어 있는 Hibernate Shortcuts(generator)의 의미는 다음과 같다. 

* 하이버네이트 5.x대와 6.x 버전대에 지정되어있는 generator들이 다르다. 

### 하이버네이트 5.x대 generator

DefaultIdentifierGeneratorFactory 클래스 

```java
public class DefaultIdentifierGeneratorFactory
		implements MutableIdentifierGeneratorFactory, Serializable, ServiceRegistryAwareService {

	public DefaultIdentifierGeneratorFactory(boolean ignoreBeanContainer) {
		this.ignoreBeanContainer = ignoreBeanContainer;
		register( "uuid2", UUIDGenerator.class );
		register( "guid", GUIDGenerator.class );			// can be done with UUIDGenerator + strategy
		register( "uuid", UUIDHexGenerator.class );			// "deprecated" for new use
		register( "uuid.hex", UUIDHexGenerator.class ); 	// uuid.hex is deprecated
		register( "assigned", Assigned.class );
		register( "identity", IdentityGenerator.class );
		register( "select", SelectGenerator.class );
		register( "sequence", SequenceStyleGenerator.class );
		register( "seqhilo", SequenceHiLoGenerator.class );
		register( "increment", IncrementGenerator.class );
		register( "foreign", ForeignGenerator.class );
		register( "sequence-identity", SequenceIdentityGenerator.class );
		register( "enhanced-sequence", SequenceStyleGenerator.class );
		register( "enhanced-table", TableGenerator.class );
	}
  ...   
}
```

- `increment` - 다른 프로세스가 동일한 테이블에 데이터를 삽입하지 않는 경우에만 고유한 long, short 또는 int 유형의 식별자를 생성
- `identity` - DB2, MySQL, MS SQL Server, Sybase 및 HypersonicSQL의 ID 열(AUTO_INCREMENT))을 지원
- `sequence` - DB2, PostgreSQL, Oracle, SAP DB, McKoi의 시퀀스 또는 Interbase의 생성기를 사용.
- `hilo` - hi 값의 소스로 테이블과 열이 주어진 경우 hi/lo 알고리즘을 사용하여 long, short 또는 int 유형의 식별자를 효율적으로 생성.
- `seqhilo` - 지정된 데이터베이스 시퀀스가 주어지면 hi/lo 알고리즘을 사용하여 long, short 또는 int 유형의 식별자를 효율적으로 생성
- `uuid` - 사용자 지정 알고리즘을 기반으로 128비트 UUID를 생성
- `uuid2` - IETF RFC 4122 호환(버전 2) 128비트 UUID를 생성
- `guid` -  MS SQL Server 및 MySQL에서 데이터베이스 생성 GUID 문자열을 사용
- `native` - 기본 데이터베이스의 기능에 따라 ID, 시퀀스 또는 hilo를 선택
- `assigned` - save()가 호출되기 전에 응용 프로그램이 entity에 식별자를 할당할 수 있다.
- `select` - 고유한 키로 행을 선택하고 기본 키 값을 검색하여 데이터베이스 트리거에 의해 할당된 기본 키를 검색
- `foreign` - 연결된 다른 개체의 식별자를 사용
- `sequence-identity` - 실제 값 생성을 위해 데이터베이스 시퀀스를 활용하는 특수 시퀀스 생성 전략



### 하이버네이트 6.x대 generator

`org.hibernate.id.factory.internal` 패키지의 StandardIdentifierGeneratorFactory에서 알 수 있다.

```java
register( "uuid2", UUIDGenerator.class );
// can be done with UuidGenerator + strategy
register( "guid", GUIDGenerator.class );
register( "uuid", UUIDHexGenerator.class );			// "deprecated" for new use
register( "uuid.hex", UUIDHexGenerator.class ); 	// uuid.hex is deprecated
register( "assigned", Assigned.class );
register( "identity", IdentityGenerator.class );
register( "select", SelectGenerator.class );
register( "sequence", SequenceStyleGenerator.class );
register( "increment", IncrementGenerator.class );
register( "foreign", ForeignGenerator.class );
register( "enhanced-sequence", SequenceStyleGenerator.class );
register( "enhanced-table", TableGenerator.class );
```

* `uuid2` - IETF RFC 4122 호환(버전 2) 128비트 UUID를 생성
* `guid` -  MS SQL Server 및 MySQL에서 데이터베이스 생성 GUID 문자열을 사용
* `uuid` - 사용자 지정 알고리즘을 기반으로 128비트 UUID를 생성
* `uuid.hex` - 길이가 32인 문자열을 반환하는 UUIDGenerator
* `assigned` - save()가 호출되기 전에 응용 프로그램이 entity에 식별자를 할당할 수 있다.
* `identity`  - 기본 키로 사용되는 ANSI-SQL IDENTITY 열과 함께 사용하기 위한 generater
  *  DB2, MySQL, MS SQL Server, Sybase 및 HypersonicSQL의 ID 열(AUTO_INCREMENT))을 지원
* `select` - 고유한 키로 행을 선택하고 기본 키 값을 검색하여 데이터베이스 트리거에 의해 할당된 기본 키를 검색
  * 방금 삽입한 행을 선택하여 데이터베이스에서 할당한 식별자 값을 결정, 하나의 매핑 매개변수가 필요
* `sequence` - DB2, PostgreSQL, Oracle, SAP DB, McKoi의 시퀀스 또는 Interbase의 생성기를 사용.
* `increment` - 다른 프로세스가 동일한 테이블에 데이터를 삽입하지 않는 경우에만 고유한 long, short 또는 int 유형의 식별자를 생성
* `foreign` - 연결된 다른 개체의 식별자를 사용
* `enhanced-sequence` : 실제 값 생성을 위해 데이터베이스 시퀀스를 활용하는 특수 시퀀스 생성 전략
* `enhanced-table` : Table Generator 사용. 

## 기본 Custom Generator들



*  AutoGenerationTypeStrategy
* IdentityGenerationTypeStrategy
* SequenceGenerationTypeStrategy
* TableGenerationTypeStrategy
* UUIDGenerationTypeStrategy



## UUID 전략



springboot 3.0  and hibernate 6.x대에서는 GenerationType.UUID라는 타입을 지원한다. 

* 아쉽게도 2.7.x대에는 GenerationType에 UUID라는 Type이 없다.

 Hibernate 5에서 도입된 UUIDGenerator  전략은 간단하게 선언할 수 있다.

다음과 같이 3가지로 지정해서 사용할 수 있으며, `String 타입 ,UUID 타입을 모두 지원`한다.

1. @GenerateValue만 사용

2. @GenericGenerator(strategy = "uuid2") 사용

3. @GeneratedValue(strategy = GenerationType.UUID) 사용 (springboot 3.0 & hibernate6 에서만 지원)



### 1. GenerateValue만 사용

```java
@Entity
public class UUIDEntity {

    @Id
    @GeneratedValue
    private UUID id;

    // ...
}
// repository
public interface UUIDRepository extends JpaRepository<UUIDEntity, UUID> { }
```

* 이 방법은 필드 타입이 String인 경우 UUID를 생성해주지 않고 에러를 생성한다.
  * org.springframework.orm.jpa.JpaSystemException: Unknown integral data type for ids : java.lang.String

### 2. @GenericGenerator(strategy = "uuid2") 사용

hibernate에서 `UuidGenerator` 클래스를 이용해서 키값을 생성해서 사용할 수 있다. 

* org.hibernate.id.uuid 패키지

```java
@Entity
@Getter
@NoArgsConstructor
public class UUIDEntity {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private UUID id;
  
}
//repository
public interface UUIDRepository extends JpaRepository<UUIDEntity, UUID> { }

----------------------------------------------------------------
// 또는
@Entity
public class UUIDEntity {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
  
}
//repository
public interface UUIDRepository extends JpaRepository<UUIDEntity, String> { }
```

* ddl-auto 를 사용하면 db 컬럼 타입은 지정된 필드의 타입(uuid 또는 String) 으로 지정된다.

  * 필드 타입이 uuid인 경우 db 컬럼 타입은 uuid. String인 경우 varchar

  



### 3. @GeneratedValue(strategy = GenerationType.UUID) 사용 - springboot 3.0 & hibernate 6.x대 

```java
@Entity
public class UUIDEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id; // java.util.uuid

}

// repository
public interface UUIDRepository extends JpaRepository<UUIDEntity, UUID> { }

----------------------------------------------------------------
// 또는
  
@Entity 
public class UUIDEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

}
//repository
public interface UUIDRepository extends JpaRepository<UUIDEntity, String> { }
```

* ddl-auto 를 사용하면 db 컬럼 타입은 지정된 필드의 타입(uuid 또는 String) 으로 지정된다.
  * 필드 타입이 uuid인 경우 db 컬럼 타입은 uuid. String인 경우 varchar
* `springboot 3.0 & hibernate 6.x대 ` 에서만 지원하는 `GenerationType.UUID`

  



> 다른 전략은 `org.hibernate.id.factory.internal` 패키지의 StandardIdentifierGeneratorFactory에서 알 수 있다.





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



`@GenericGenerator` 를 사용하여 Generator를 정의한다.



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



생성되는 ID는 새로운 Entity객체를 생성하고, Persist가 되는 단계에 개발된 Generator가 동작하게 된다.



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

또한 `configure()` 메서드 에서 접두사(Prefix) 속성 값을 설정할 수 있도록 Configurable 인터페이스를 구현하는데, Configurable 인터페이스는 IdentifierGenerator가 상속받고 있따. 

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









# spring data jpa ID 생성전략에 따른 동작 방식



JpaRepository.save 동작방식은 persist 하거나 merge를 한다.

```java
@Transactional
@Override
public <S extends T> S save(S entity) {

		Assert.notNull(entity, "Entity must not be null.");

		if (entityInformation.isNew(entity)) {
			em.persist(entity);
			return entity;
		} else {
			return em.merge(entity);
		}
	}

}
```

* 엔티티에 Id 값이 있으면 DB를 조회 (select)한 후에 insert 하게 된다.



생성전략이 존재하지 않을 경우 merge 로 동작.

- merge 는 입력받은 entity 를 복사한 후 진행하기에 리소스 소모가 있다.

생성전략이 존재하는 경우 persist 로 동작한다.





## ID 생성 전략이 없고 애플리케이션에서 Set하는 방식 - 

애플리케이션에서 ID 를 생성해줬기에 DB 에 값이 있나 확인하기 위해 select 를 날려본 후에, INSERT 가 이루어진다.

* 즉, merge 방식으로 동작





## ID 생성 전략이 존재할 때



### GenerationType.IDENTITY

- IDENTITY 는 id 값을 세팅하지 않고, insert 쿼리를 날리면 database 시스템에서 자동으로 가져와 엔티티에 지정한다.
- id 값 생성에 대해서 database 에서 관여하기에 save 메소드를 수행시 persist 로 동작한다.
  - select 하지 않고 바로 insert 1번을 날린다



### GenerationType.SEQUENCE

* database 의 sequence 기능을 사용하는 기능
* persist 를 호출하면 (spring data jpa 의 경우 save) sequence 를 가져온다.
* 가져온 Sequence 를 id 에 할당하고 (영속성 상태), transaction 이 commit 될 때, insert 쿼리를 날린다.



### GenerationType.Table

- 키 생성 전용 테이블을 만들어서 시퀀스처럼 동작하게 하는 방식.
- 모든 데이터베이스에서 사용할 수 있지만 문제점이 있다.
- Sequence 나 Identity 방식은 하나의 Request 로 처리가 가능하지만 테이블 생성전략은 3개의 step이 필요하다
- lock 을 잡고, seq 를 증가시키고 데이터베이스에 저장한다. (리소스 소모)
- 그렇기에 운영할 때  잘 사용하지 않는다.





## Id를 Application에서 지정할 때 select query를 먼저 조회하지 않는 방법



Spring Data Jpa에서는 대부분 JpaRepository interface를 상속받는데, 이 때 기본 구현체로 SimpleJpaRepository.class가 사용된다. 



다음은 SimpleJpaRepository의 save method 코드이다.

```java
@Transactional
@Override
public <S extends T> S save(S entity) {

		Assert.notNull(entity, "Entity must not be null.");

		if (entityInformation.isNew(entity)) {
			em.persist(entity);
			return entity;
		} else {
			return em.merge(entity);
		}
	}

}
```

* JpaRepository.save 동작방식은 persist 하거나 merge를 한다.
* 엔티티에 Id 값이 있으면 DB를 조회 (select)한 후에 insert 하게 된다.



Spring Data Jpa는 Id 생성전략이 존재하지 않을 경우 merge 로 동작하고 생성전 략이 존재하는 경우 persist 로 동작한다.

여기서의 핵심은 isNew() method인데 isNew() method에서 select query를 수행하여 새로운 데이터인지 확인한다.

select query 하지 않게하려면 Entity class에 Persistable 인터페이스를 구현하면된다.

```java
package org.springframework.data.domain;

public interface Persistable<ID> {

	// 엔티티의 아이디를 반환하며, null 일 수 있.
	@Nullable
	ID getId();

	// Persistable이 new 이거나 이미 persistence 인 경우. 
	boolean isNew();
}
```

**엔티티**

```java
@Entity
public class User implements Persistable<String> {

    @Id
    @GeneratedValue
    private String id;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return false;
    }
}
```

isNew() 메소드로 구분하는데 true이면 select 하지 않고 insert 한다. 그렇다고 무조건 true로 두게 되면 update할 때에도 insert를 수행하게 된다.



>  그러면 어떻게 isNew값을 제대로 뽑을 수 있을까? 

일반적으로 @EnableJpaAuditing 의 createdDate를 이용하면 된다.

@CreatedAt을 사용하면 생성시에 createdAt이 무조건 생성되게 된다. 이 createdAt이 null인지 아닌지 비교하는 방법으로 알 수 있다.



```java
@Entity
public class User implements Persistable<String> {

    @Id
    @GeneratedValue
    private String id;

    @CreatedDate
    private LocalDateTime createdAt;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return createdAt == null;
    }
    
}
```

1. @CreatedDate Annotation을 사용하면 data가 insert 될 때 자동으로 현재 시간이 입력된다.
2. createdDate가 null이라면 새로운 데이터로 취급한다.



위처럼 변경하더라도 이미 존재하는 PK를 가진 데이터를 insert 시도하면 에러가 발생한다. 



기존 동작인 select - insert를 유지하는 경우 엄청난 문제가 발생할 수 있기 때문에 로직을 잘 짜야 한다. 

업데이트할 때도 업데이트해야 할 필드만 선택적으로 하는 것이 아닌 객체 전체가 변경되기 때문에 merge() 기능을 의도하고 사용해야 할 일은 거의 없다. 변경을 위해선 변경 감지(dirty-checking)를, 저장을 위해선 persist()만이 호출되도록 유도해야 실무에서 성능 이슈 등을 경험하지 않을 수 있다.










### 참조

* https://www.baeldung.com/hibernate-identifiers

* https://docs.jboss.org/hibernate/orm/5.4/userguide/html_single/Hibernate_User_Guide.html#identifiers-generators-GenericGenerator

* https://kapentaz.github.io/jpa/Hibernate%EC%97%90%EC%84%9C-Custom-Value-%EC%83%9D%EC%84%B1%ED%95%98%EA%B8%B0/#

* https://techblog.woowahan.com/2607/
* https://kwonnam.pe.kr/wiki/java/hibernate/id_generator

* https://www.javanibble.com/introduction-identifiers-spring-data-jpa/





* https://yjh5369.tistory.com/entry/JPA%EC%97%90%EC%84%9C-insert-update-delete-%ED%95%A0-%EB%95%8C-%EC%9E%90%EB%8F%99%EC%9C%BC%EB%A1%9C-select-%ED%95%98%EC%A7%80-%EC%95%8A%EA%B2%8C-%ED%95%98%EB%8A%94-%EB%B0%A9%EB%B2%95







* https://velog.io/@rainmaker007/spring-data-jpa-ID-%EC%83%9D%EC%84%B1%EC%A0%84%EB%9E%B5%EC%97%90-%EB%94%B0%EB%A5%B8-%EB%8F%99%EC%9E%91-%EB%B0%A9%EC%8B%9D-%EC%A0%95%EB%A6%AC