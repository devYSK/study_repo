# MySQL 낙관적 락과 데드락(dead lock) With JPA Hibernate

프로젝트에서 모임 가입 기능을 구현하면서, 동시성 문제와 데드락까지 경험한 내용 그리고 어떻게 해결하였는지 

고민과정과 해결방법을 정리하려고 합니다.

> 프로젝트 버전
>
> - SpringBoot 2.7.8
> - MySQL 8.028
> - Spring Data Jpa
> - Hibernate 5.6.14

 

## 모임 가입 기능?

저희 프로젝트에는 모임과 모임에 가입할 수 있는 기능이 있습니다.

모임 가입에 대한 비즈니스 요구사항은 다음과 같습니다.

* 모임이 존재하고, 모임에는 인원제한이 있습니다.
* 인원제한이 다 찬 모임에 참여하려는 경우, 참여가 불가능 합니다.
* 모임에 여러명이 동시에 요청해도, 요청한 순서대로 모임에 가입되어야 합니다.



모임과 모임 구성원은 1:N 관계이며, 모임 제한 인원은 **모임 테이블에 존재**합니다.

<img src="https://blog.kakaocdn.net/dn/bnVu0N/btr5quPudBf/1LfRaH9fXjTargxyTJTk01/img.png" width = 300 height = 400 style="float:left">

동시성 문제가 발생할 것이라 에상하고, 처음에 고려했던건 **낙관적 락**을 고려했습니다

> 낙관적 락?(Optimistic Lock)
>
> DB레벨의 Lock을 사용하지 않고 Version을 통해 애플리케이션 레벨에서 처리하는 Lock
>
> 트랜잭션이 충돌하지 않는다고 가정한다.
>
> 트랜잭션 커밋 전 후의 version을 비교해서 충돌 여부를 비교한다. 
>
> 최초 하나의 요청만 성공하고 나머지 요청들은`ObjectOptimisticLockingFailureException`예외가 발생한다

이유는 다음과 같습니다.

* 모임 가입에 트래픽이 많지 않아 동시에 요청할일이 드물다. 때문에 트랜잭션이 충돌할 일이 드물다.
* 비관적 락을 사용하면 동시성이 급격하게 떨어지므로 성능 상 이슈가 있을 수 있을 것 같다.



그렇다면 낙관적 락을 이용해서 먼저 기능을 구현하고 테스트를 해보았습니다.

## 낙관적 락을 이용한 코드

- JPA의 **낙관적 락(Optimisstic Lock)**을 사용하기 위해서는 `@Version` 어노테이션을 사용해서 버전을 관리합니다  

```java
@Version
```

이 어노테이션 적용할 수 있는 데이터 타입은 아래와 같습니다.

1. Long, long
2. Integer, int
3. Short, short
4. Timestamp



**엔티티**

```java
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookGroup extends BaseTimeColumn {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	...
    
  @Version
	private Long version; // << @Version 어노테이션 적용

}
```

### 첫 번째 시도. LockModeType.OPTIMISTIC

테스트 환경

* 모임 가입 가능 인원 5명 
* 현재 인원 1명 
* 동시 요청 사용자 9명

* ExecutorService와 CountDownLatch 클래스 활용하여 9개의 쓰레드에서 동시에 요청

**JpaRepository**

```java
public interface BookGroupRepository extends JpaRepository<BookGroup, Long>, BookGroupSupport {

	@Lock(LockModeType.OPTIMISTIC)
	@Query("SELECT bg FROM BookGroup bg LEFT JOIN FETCH bg.groupMembers WHERE bg.id = :groupId")
	Optional<BookGroup> findByIdWithGroupMembersForUpdate(@Param("groupId") Long groupId);

}
```

OPTIMISTIC을 사용했으나, 테스트는 실패했습니다. 

```java
Expected :5
Actual   :10
```

왜냐하면, 모임의 컬럼이 수정되지 않으므로 모임 테이블의 version 컬럼은 바뀌지 않고, 

모임 구성원 테이블에만 새 row가 insert 되기 때문입니다.

때문에 일반적인 OPTIMISTIC 기능으로는 사용할 수가 없어 OPTIMISTIC_FORCE_INCREMENT를 사용해보았습니다.

* 모임과 모임 구성원 테이블이 수정되지 않았으니 버전도 바뀌지 않으므로 낙관적 락은 제 기능을 하지 못합니다.
* 낙관적 락이 제대로 동작하려면, 인원을 카운트 하는 컬럼이나 모임 가입 가능 상태를 나타내는 컬럼이 모임 테이블에 존재하고, 모임 인원이 다 찼을 시 이 컬럼 값이 바뀌어야 버전이 바뀌게 됩니다

### 두 번째 시도, LockModeType.OPTIMISTIC_FORCE_INCREMENT

#### OPTIMISTIC_FORCE_INCREMENT 

낙관적 잠금을 사용하면서 버전 정보를 강제로 증가시키는 옵션입니다.

다른 트랜잭션에서 해당 엔티티를 수정하는 경우에 충돌이 발생하여, 데이터의 일관성이 유지되도록 하기 위해 사용했습니다.

* 테스트 환경은 위와 같이 설정하였습니다. 

**JpaRepository**

```java
public interface BookGroupRepository extends JpaRepository<BookGroup, Long>, BookGroupSupport {

	@Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
	@Query("SELECT bg FROM BookGroup bg LEFT JOIN FETCH bg.groupMembers WHERE bg.id = :groupId")
	Optional<BookGroup> findByIdWithGroupMembersForUpdate(@Param("groupId") Long groupId);

}
```

<img src="https://blog.kakaocdn.net/dn/HXtNG/btr5oBPkNfe/LBIrs5oqzuWAsLoVYkiPDK/img.png" width=950 height = 220>

**데드락 발생.**

데이터 베이스 레벨의 Lock을 사용하지 않았는데 데드락이 발생했습니다.

InnoDB에서 락을 걸고 있나? 해서 , `SHOW ENGINE innodb status`로 데드락을 확인하였습니다.

```sql
show engine innodb status;
```

<img src="https://blog.kakaocdn.net/dn/b30LJs/btr5sh26GFU/VvTyzCKyKiBdtyPRRJgFn1/img.png">

> lock mode S locks rec but not gap
> lock_mode X locks rec but not gap waiting

* S locks - 공유 락 (Shared) - 다른 트랜잭션이 읽을 수는 있지만 쓸 수는 없다
* X locks - 배타 락 (Exclusive) - 다른 트랜잭션은 읽을 수도 쓸 수도 없다.



같은 record에 s-lock과 x-lock을 시도하고 있음을 알 수 있었고, 아래 2번 트랜잭션을 보면서 다음과 같이 데드락을 진단하였습니다.

1. 트랜잭션 1이 s-lock 획득
2. 트랜잭션 2가 s-lock 획득 (s-lock은 둘 다 획득 가능합니다.)
3. 트랜잭션 1이 x-lock 획득을 위해 대기 (x-lock은 s-lock과 같이 걸 수 없습니다. 트랜잭션 1를 기다려야 합니다)
4. 트랜잭션 2가 x-lock 획득을 위해 대기 (마찬가지,  트랜잭션 2를 기다려야 합니다)



DB 락을 사용하진 않았고, SQL 로그를 확인했음에도 Lock을 걸지 않았음을 확인했습니다.

그렇다면 왜 DB 락이 걸린건지 구글과 MySQL 공식문서에서 답을 찾았습니다.

* [MySQL 8.0 공식 문서](https://dev.mysql.com/doc/refman/8.0/en/innodb-locks-set.html)

> If a `FOREIGN KEY` constraint is defined on a table, any insert, update, or delete that requires the constraint condition to be checked sets shared record-level locks on the records that it looks at to check the constraint. `InnoDB` also sets these locks in the case where the constraint fails.
>
> 
>
> FOREIGN KEY 제약 조건이 테이블에 정의되어 있으면 제약 조건을 확인해야하는 insert, update, delete는 제약 조건을 확인하기 위해 레코드에 s-lock을 설정합니다. InnoDB는 제약 조건이 실패하는 경우에도 이러한 잠금을 설정합니다.



MySQL은 fk가 존재하는 테이블에서 fk를 포함한 record(데이터)를 삽입, 수정, 삭제 하는 경우 제약조건을 확인하기 위해 s-lock을 설정한다고 합니다. 때문에, 모임 구성원 테이블에 insert 될 때 fk인 모임 id를 참조하기 때문에 **s-lock이 걸린 것입니다.**

#### 그렇다면 x-lock은?



마찬가지로, MySQL 공식문서에서 답을 찾았습니다. 

> UPDATE … WHERE … sets an exclusive next-key lock on every record the search encounters. However, only an index record lock is required for statements that lock rows using a unique index to search for a unique row.
>
> update... where 시 검색한 발생하는 모든 레코드에 대해 x-lock을 설정합니다. ...
>
>
> For locking reads (SELECT with FOR UPDATE or FOR SHARE), **UPDATE**, and DELETE statements, the locks that are taken depend on whether the statement uses a unique index with a unique search condition, or a range-type search condition.

MySQL InnoDB는 record(데이터)를 수정할 때에는 항상 x-lock을 건다고 합니다. 

버전이 바뀌거나, 모임 테이블의 값을 변경하는 update 쿼리가 발생하면서 x-lock을 건것입니다. 



정리하자면,

* 모임 구성원 테이블에 새 record를 넣으면 s-lock이 걸린다
* 모임을 update 하면서 x-lock이 걸린다.



그렇다면 결론적으로 FK 제약조건이 있는 테이블에는 낙관적 락을 사용할 수가 없습니다. 

이는 MySQL 데이터베이스에서 데이터의 일관성을 지키기 위해 개발자는 Lock을 걸고 싶지 않아도 Lock을 걸기 때문입니다.



  ## 그렇다면 어떻게 해결할 것인가.

그렇다면 현재 동시성을 고려하면서 해결할 수 있는 방법은 크게 3가지로 고민 해봤습니다.

1. 비관적 락(Pessimistic Lock)
2. 메시지 큐로 요청의 순차성 보장
3. 레디스를 활용한 분산 락(Distrubuted Lock)



우리 프로젝트 에서는 다음과 같은 이유로 비관적 락을 선택하여 해결했습니다.

1. 재시도에 대한 별도 처리가 없다.
2. 모임 가입 기능 이외에 메시지큐를 사용할 상황이 현재는 없다. 러닝커브와 구축시간 등 추가적인 리소스가 너무 많이 들 것이다.
3. 차후 확장할 수는 있지만, 현재 데이터베이스는 분산 DB가 아니다. 레디스를 활용한 분산 락을 사용할 필요가 없다.
4. 동시에 요청할 수는 있지만, 서비스가 크지 않으므로 요청이 그렇게 많지 않을거라는 예측은 아직 동일하다. 그렇다면 비관적 락으로 해결할 수 있을 것이다. 



그러므로 비관적 락을 선택해서 문제를 해결하였습니다.

### 비관적 락 사용

**JpaRepository**

public interface BookGroupRepository extends JpaRepository<BookGroup, Long>, BookGroupSupport {

```java
public interface BookGroupRepository extends JpaRepository<BookGroup, Long>, BookGroupSupport {

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT bg FROM BookGroup bg LEFT JOIN FETCH bg.groupMembers WHERE bg.id = :groupId")
	Optional<BookGroup> findByIdWithGroupMembersForUpdate(@Param("groupId") Long groupId);

}
```

동시성 테스트도 통과하였습니다.

<img src="https://blog.kakaocdn.net/dn/k7YwL/btr5qtbW073/h10cTEWtgWFHHM1KfwCbdK/img.png" width = 900 height = 80>



테스트는 통과하였고 비관적락으로 해결하였지만 끝이 아닙니다. 

앞으로 프로젝트를 진행하고 서비스를 운영하게 된다면 또 다른 이슈가 생길 수도 있습니다.

예를 들어, 모임 가입 요청이 많지 않을거란 예측에 비관적락 을 사용하지만 조회가 늘어날 수도 있는 등의 문제도 마찬가지 입니다.

또한 비즈니스 요구사항이 변경될 수 있으며, 애플리케이션과 DB가 확장되면서 생기는 경우들까지 고려해야 합니다. 

그때까지 더욱 더 동시성에 대한 공부와 다른 처리 방법, 그리고 확장에 대한 대비가 필요합니다. 



### 참조

* https://dev.mysql.com/doc/refman/8.0/en/innodb-locks-set.html

* https://velog.io/@woodyn1002/my-klas-낙관적-락-vs-비관적-락

* https://www.baeldung.com/jpa-optimistic-locking
* https://reiphiel.tistory.com/entry/understanding-jpa-lock
