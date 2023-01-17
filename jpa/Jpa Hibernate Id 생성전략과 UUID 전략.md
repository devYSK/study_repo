## JPA Hibernate Id 생성 전략과 UUID 전략

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

> hibernate6 에서는 GenerationType.UUID가 추가되어 5가지가 있다. 

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
*  IdentityGenerationTypeStrategy
*  SequenceGenerationTypeStrategy
*  TableGenerationTypeStrategy
*  UUIDGenerationTypeStrategy



## UUID 전략

springboot 3.0  and hibernate 6.x대에서는 GenerationType.UUID라는 타입을 지원한다. 

* 아쉽게도 2.7.x대에는 GenerationType에 UUID라는 Type이 없다.

 Hibernate 5에서 도입된 UUIDGenerator  전략은 간단하게 선언할 수 있다.

다음과 같이 3가지로 지정해서 사용할 수 있으며, `String 타입 ,UUID 타입을 모두 지원`한다.

1. @GenerateValue만 사용

2. @GenericGenerator(strategy = "uuid2") 사용

3. @GeneratedValue(strategy = GenerationType.UUID) 사용 (springboot 3.0 & hibernate6 에서만 지원)



> UUID의 컬럼 타입은 MySQL 기준으로 BINARY(16)을 사용해야 한다 (*A single UUID needs 16 bytes.*)
> 그렇지 않으면 조회하지 못할 수도 있다
>
> 저장할 때 **남는 길이는 오른쪽으로 패딩** 처리하여 저장하기 때문이다. 
>
> [컬리 기술 블로그](#https://helloworld.kurly.com/blog/jpa-uuid-sapjil/)



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
