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

* https://velog.io/@rainmaker007/spring-data-jpa-ID-%EC%83%9D%EC%84%B1%EC%A0%84%EB%9E%B5%EC%97%90-%EB%94%B0%EB%A5%B8-%EB%8F%99%EC%9E%91-%EB%B0%A9%EC%8B%9D-%EC%A0%95%EB%A6%AC
