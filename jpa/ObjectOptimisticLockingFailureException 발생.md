# 동시 삭제 요청 ObjectOptimisticLockingFailureException

Spring Boot + Spring Data Jpa 환경에서 

특정 resource를 delete하는 api를 사용할 때 ObjectOptimisticLockingFailureException 예외가 발생하였습니다

슬랙으로 온 예외메시지를 확인하고 원인 분석을 하였습니다.

다음은 슬랙으로 온 예외 메시지를 간략히 줄인 내용입니다.

```
exception : ObjectOptimisticLockingFailureException
time : 202x년 0x월 xx일 1x시 14분 24초 - 24
request_path: /api/contents/21678
request_method: DELETE
log level: warn

cause: org.hibernate.StaleStateException: Batch update returned 
       unexpected row count from update [0]; actual row count: 0; 
       expected: 1; statement executed: delete from content where id=?


message : Batch update returned unexpected row count from update [0]; 
          actual row count: 0; expected: 1;...
```

해당 api 삭제 로직의 간략화한 코드는 다음과 같습니다

```kotlin
// Service
@Service
class ContentService(
	private val contentRepository: ContentRepository
) {
  ... 
  @Transactional
  fun delete(content: Content) {
    // ... 생략
    content.delete() 
    contentRepository.delete(content)
   }
  ...
}

// Entity
@Entity
class Content(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "parentId")
    var parent: Parent,

    @Embedded
    val contentBody: ContentBody,

    var isUsed: Boolean = false,

    ) : AbstractTimeByColumn(), ..., ... {

    fun delete() {
        this.parent.contents.remove(this)
        this.parent = null
    }
   
  ...
}
```

# 원인 분석

ObjectOptimisticLockingFailureException은 일반적으로 낙관적 락과 관련된 예외입니다.

SpringBoot + JPA 환경에서는 낙관적락을 이용할때 `@Version` 컬럼을 이용해서 관리할 수 있는데요, 

애플리케이션 레벨에서 조회 시 version과 update 또는 delete 시 version이 다르면 예외를 발생시킵니다.

위에서 보시다시피 Entity 에서는 @Version 칼럼이 없습니다.

하지만 해당 예외를 Hibernate에서는 **StaleStateException** 예외를 ObjectOptimisticLockingFailureException 으로 래핑해서도 발생시킵니다.

> cause를 보면 StaleStateException가 래핑된것을 알 수 있습니다. 

해당 예외가 발생하는 상황은 다음과 같습니다

1.  버전 번호 또는 타임스탬프 확인이 실패할 때
2. 다른 세션 또는 트랜잭션이 동일한 데이터를 수정한 경우

3. **데이터베이스 행이 존재하지 않는 경우 엔터티를 업데이트하거나 삭제하려고 시도하는경우 예상한 행 수와 실제로 영향을 받은 행 수가 일치하지 않을 때 발생**







다시 예외 메시지를 보면

```
message : Batch update returned unexpected row count from update [0]; actual row count: 0; expected: 1;...
```

예외 메시지에 따르면, `delete from content where id=?`라는 SQL 문장을 실행했을 때, 

**예상한 행 수는 1**이지만 실제로 **영향을 받은 행 수는 0**이라고 나와 있습니다.

이는 해당 ID를 가진 레코드가 존재하지 않아 삭제 작업이 실패했음을 의미합니다.



1. 엔티티에 @Version을 사용하지 않고

2. **예상한 행 수는 1**이지만 실제로 **영향을 받은 행 수는 0**이므로

3번의 이유로 발생했단것을 알 수 있었습니다.



실제 해당 로그를 살펴보니 같은 자원에 대한 요청이 들어와있었습니다.

```
2023-0x-xx 18:14:24.254 INFO  [t-5] org.?. :: delete request... ContentId : 21678, ...생략 
2023-0x-xx 18:14:24.390 INFO  [t-7] org.?. :: delete request... ContentId : 21678, ... 생략
```

같은 Id에 대해서 다른 스레드의 다른 두 트랜잭션이 요청을 했습니다.

1. t-5 스레드의 트랜잭션이 id : 21678 엔티티 조회
2. **동시에** t-7 스레드의 트랜잭션이 id : 21678 엔티티 조회

3. t-5 스레드의 트랜잭션이 id 21678 delete

4. t-7 스레드의 트랜잭션이 삭제 이벤트를 발생시켰으나, 실제 DB에서 이미 지워져 있으므로 업데이트한 row count는 0이 됩니다
5. 예외 발생ㄴ



# 해결 방법

사실 해당 오류가 발생하면 해결할 방법이 마땅하지 않습니다. 일반적으로 삭제한 행 수를 체크하여 정상인지, 오류인지 판단하기 때문입니다. 저는 크리티컬하지 않고, 다른 부분에 발생할 문제가 없어서 native query 사용하여 해당 예외가 발생하지 않도록 하였으나

다른 방법으로는  다시 시도, 오류 처리, 무시, 분산락 과 같은 방법들을 생각해 볼 수 있습니다.

## native query를 사용해서 행을 제거하고, 제거 후 update 된 row count를 체크하지 않는 방법.

```kotlin
@Repository
interface ContentRepository : JpaRepository<Content, Long> {

    @Modifying
    @Query(value = "DELETE FROM content WHERE id = :id", nativeQuery = true)
    fun deleteByIdNative(id: Long)

}
```

이러면, 해당 쿼리가 동작하는 서비스에서는 쿼리만 서빙하는 역할이 되므로 jpa 에서 예외를 발생시키지 않습니다.

하지만 만약 다른 트랜잭션에서 해당 엔티티를 읽어 영속성 컨텍스트에 보관하고 있고,동시에 해당 native 쿼리가 발생하여 실제 DB에서 row를 delete하면  ObjectOptimisticLockingFailureException과 같은 다른 예외가 발생할 수 있으니 주의해야 합니다.





### 참조

* https://docs.spring.io/spring-framework/docs/current/reference/html
* https://www.baeldung.com/hibernate-exceptions
* https://www.inflearn.com/questions/266817/objectoptimisticlockingfailureexception