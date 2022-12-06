* 

  # Spring Data Jpa - JpaRepository에서 save시 select 쿼리가 먼저 실행되는 이유

  

  # Entity의 id가 GeneratedValue가 sequence(또는 identity)가 아니고, 직접 id를 set 할 때,

  

  Id 생성 전략이, 개발자가 직접 Id를 만들 고 엔티티 객체 저장 시 select 쿼리가 나간다.

  

  결론부터 

  > JpaRepository.save(entity) 를 이용해서 엔티티 객체를 저장할 때, entityManager.persist or entityManager.merge 를 호출한다.
  >
  > 이 때, JpaRepository의 구현체인 SimpleJpaRepository는 `isNew` 메서드를 통해
  >
  > 1.  id값이 없으면 새 엔티티라고 판단하고, em.persist를 호출
  > 2.  id값이 있으면 DB에 저장되어 있을 수 있는 엔티티라고 판단하여 SELECT 절을  날려 DB 테이블에 해당 엔티티가 존재하는지 확인한다.
  >     1. 테이블에 지정한 식별자 값을 가진 엔티티가 없는 경우 - 영속 상태로 만들고 insert 쿼리를 쓰기 지연 저장소에 저장
  >     2. 테이블에 지정한 식별자 값을 가진 엔티티가 존재하는 경우에 데이터의 변경이 발생하면 - update 쿼리를 쓰기 지연 저장소에 저장
  >     3. 테이블에 지정한 식별자 값을 가진 엔티티가 존재하지만, 데이터의 변경이 없다면 - 아무것도 실행하지 않는다 

  

  

  >병합(머지,merge)는 비영속 상태의 엔티티 객체를 영속 상태로 만들어야 하는데,    
  >
  >
  >키 값이 있는 엔티티 객체 를 영속 상태로 만들려면 키 값이 존재하니까 데이터베이스에 저장된 엔티티인지 확인이 필요한것이다. 
  >
  >* 키 값이 없는 엔티티 객체라면 데이터베이스에 저장이 안되있을 확률이 매우 높지. 테이블 로우는 키가 필요하니까
  >
  >
  >
  >그러므로, 데이터베이스에 해당 키를 가진 엔티티가 있는지 없는지 확인을 해야만   
  >
  >1. insert를 해도 되는지 판단 가능 - 만약 데이터베이스에 같은 키를 가진 엔티티가 있으면 insert 시 pk 중복으로 에러가 나기 때문
  >
  >2. insert를 할 지 update를 할 지, 아무것도 안할 지  판단 가능 
  >
  >  * 없으면 insert를 하고
  >  * 있으면 영속화 시킨 다음에, 바뀐 부분이 있다면 update를 하고
  >  * 있으면 영속화 시킨 다음에 바뀐 부분이 없으면 아무 행동도, 쿼리도 실행 안함
  >
  >할 수 있기 때문이다. 

  

  # 발생 상황 및 코드 뜯어보기

  

  다음과 같은 엔티티가 있다.  
  이 엔티티는 id 생성 전략을 개발자가 직접 지정해 주는것으로 설정했다.

  ```java
  @Getter
  @Setter
  @ToString
  @NoArgsConstructor
  @Entity
  @Table(name = "members")
  public class Member {
  
      @Id
      private long id;
  
      @Column(name = "first_name")
      private String firstName;
  
  }
  
  // repository
  public interface MemberRepository extends JpaRepository<Member, Long> {
  
  }
  ```

  

  * 다음은 Member 엔티티를 저장 하는 테스트 코드이다

    
  

  ```java
  @Slf4j
  @DataJpaTest
  class MemberRepositoryTest {
  
      @Autowired
      private MemberRepository memberRepository;
  
      @Transactional
      @Test
      void 회원정보가_수정되는지_확인한다() {
  
          long memberId = 9999;
  
          Member member = new Member();
          member.setId(memberId);
          member.setFirstName("ys");
          Member entity = memberRepository.save(member);
          System.out.println("save!!");
          memberRepository.flush(); // 일단 강제로 플러시
  
      }
  }
  ```

    
  

  콘솔에 찍힌 쿼리 결과는 다음과 같다.

  ```
  2022-12-07 02:13:40.030  INFO 81386 --- [           main] o.s.t.c.transaction.TransactionContext   : Began transaction 
  
  (1) for test context [DefaultTestContext@289710d9 ...
  
  //////////////////////////////////////// 이부분을 볼것
  
  Hibernate: 
   select
       member0_.id as id1_0_0_,
       member0_.first_name as first_na2_0_0_ 
   from
       members member0_ 
   where
       member0_.id=?
  save!!
  Hibernate: 
   insert 
   into
       members
       (first_name, id) 
   values
       (?, ?)
  
  ///////////////////////////////////////////////// 이부분을 볼것
  
  2022-12-07 02:13:40.339  INFO 81386 --- [           main] o.s.t.c.transaction.TransactionContext   : Rolled back transaction for test: [DefaultTestContext@289710d9 testClass =...
  ```

  

    
  

  분명히 `memberRepository.save(member);` 를 호출했는데,  `select query`가 발생했다 이유가 뭘까?

    
  

  > save(S) 메소드는 엔터티에 식별자 값이 없으면(null이면) 새로운 엔터티로 판단해서 `EntityManager.persist()`를 호출하고 식별자 값이 있으면 이미 있는 엔터티로 판단해서 `EntityManager.merge()`를 호출한다. 

    
  

  다음은 JpaRepository의 구현체 중 하나인 SimpleJpaRepository 의 save() 메소드이다.

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
  ```

  

  * isNew 메소드가 있다 무엇일까?

  <img src="https://blog.kakaocdn.net/dn/w3Pen/btrS0MacCYb/WirA9IwIYkKq17gw6eV5n0/img.png"  width= 900 height=350>

  isNew 메소드는 주어진 엔티티가 새 엔티티인지 판별하는 메소드이다.

  * 새 엔티티이면 true, 아니면 false를 반환한다.

    
  

  그러므로, JpaRepository.save() 메서드는, 주어진 엔티티가 새 엔티티이면 persist(영속, 저장) 하고 새 엔티티가 아니라면 merge(병합, 영속화)를 하는것을 알 수 있다.  
  

  그렇다면 어떻게 isNew로 새 엔티티인지 알 수 있을까?

  <img src="https://blog.kakaocdn.net/dn/b2KJfw/btrSZz3YeSA/7axztgi5KRBQEqUphuToB0/img.png"  width= 950 height=450>

  * entityInformation 객체는 `JpaMetamodelEntityInformation` 객체
  * JpaMetamodelEntityInformation : EntityInformation 인터페이스를 구현한 추상 클래스인 AbstractEntityInformation 클래스의 서브 클래스

  <img src="https://blog.kakaocdn.net/dn/c1bnJg/btrS0LbjpM8/xGT0RjPg43RKPsKeZ84VL1/img.png"  width= 950 height=450>

  ​    
  


  ### isNEW?

  * https://docs.spring.io/spring-data/jpa/docs/1.5.0.RELEASE/reference/html/jpa.repositories.html
  * 2.2 Persisting entities에 Spring Data JPA offers the following strategies to detect whether an entity is new or not:를 보자

  * 엔티티 상태 감지 전략은 다음과 같다

  The following table describes the strategies that Spring Data offers for detecting whether an entity is new:

  | `@Id`-Property inspection (the default) <br />(@Id-속성 검사(기본값)) | By default, Spring Data inspects the version property of the given entity. If the identifier property is `null` or `0` in case of primitive types, then the entity is assumed to be new. Otherwise, it is assumed to not be new. <br /><br />기본적으로 Spring Data는 지정된 엔티티의 버전 속성을 검사합니다. primitive 유형의 경우 식별자 속성이 null이거나 0이면 엔티티는 new (새로운 것) 로 간주됩니다. 그렇지 않으면,  새로운 것이 아니라고 간주한다. |
  | ------------------------------------------------------------ | ------------------------------------------------------------ |
  | `@Version`-Property inspection                               | If a property annotated with `@Version` is present and `null`, or in case of a version property of primitive type `0` the entity is considered new. If the version property is present but has a different value, the entity is considered to not be new. If no version property is present Spring Data falls back to inspection of the Id-Property. <br /><br />@Version으로 주석이 달린 속성이 존재하고 null이거나 원시 유형 0의 버전 속성인 경우 엔티티는 새로운 것으로 간주됩니다. 버전 속성이 있지만 다른 값을 가지고 있다면 엔티티가 새로운 것이 아니라고 본다. 버전 속성이 없는 경우 Spring data는 Id-Property의 검사( Id 속성 검사, 디폴트 전략)로 돌아갑니다. |
  | Implementing `Persistable`                                   | If an entity implements `Persistable`, Spring Data delegates the new detection to the `isNew(…)` method of the entity. See the [Javadoc](https://docs.spring.io/spring-data/data-commons/docs/current/api/index.html?org/springframework/data/domain/Persistable.html) for details.*Note: Properties of `Persistable` will get detected and persisted if you use `AccessType.PROPERTY`. To avoid that, use `@Transient`.* <br />엔티티가 지속 가능을 구현하는 경우 Spring data 는 상태 탐지 를 엔티티의 isNew(…) 메서드에 위임합니다. 자세한 내용은 [자바독](https://docs.spring.io/spring-data/data-commons/docs/current/api/index.html?org/springframework/data/domain/Persistable.html)을 참조하십시오. <br />참고: 액세스 유형을 사용하면 지속 가능한 속성이 탐지되고 지속됩니다.소유물. 이 문제를 방지하려면 @Transient를 사용하십시오. |
  | Providing a custom `EntityInformation` implementation <br />사용자 지정 엔티티 정보 구현 | You can customize the `EntityInformation` abstraction used in the repository base implementation by creating a subclass of the module specific repository factory and overriding the `getEntityInformation(…)` method. You then have to register the custom implementation of module specific repository factory as a Spring bean. Note that this should rarely be necessary. <br />엔티티 상태 검사를 사용자 지정(커스텀) 할 수 있습니다. <br />모듈별 리포지토리 팩토리의 하위 클래스를 만들고 getEntity를 재정의하여 리포지토리 기본 구현에 사용되는 정보 추상화정보(…) 방법입니다. 그런 다음 모듈별 저장소 팩토리의 사용자 정의 구현을 스프링 빈으로 등록해야 합니다. 이 작업은 거의 필요하지 않습니다. |

  

  즉, 아무것도 설정되어 있지 않다면 @Id 어노테이션이 붙은 식별자 필드가 null 또는 0 일 경우 new 상태로 인식한다. 

  * 주의할 점은 Long 타입과 같이 Wrapper Class 일 경우 null 을 새 엔티티 (newState)라고 인식한다. 
  * int 와 같이 Primitive 일 경우 0일 때 새 엔티티 new로 인식한다.

  

  > 즉 내가 테스트 한 경우는 id값이 null이 아니고 우리가 지정했으니 새 엔티티라고 판정되어 merge를 실행한다. 

  

  ## ID 값이 null이 아니고 우리가 지정 했으니 새 엔티티가 아닌데 왜 SELECT을 하지?

  

  entityManager.merge(entity) 를 호출하는데 이 메서드의 주석을 다시 살펴봤다.

  <img src="https://blog.kakaocdn.net/dn/rYOP8/btrSYmKEe2S/ymh5NXVMP7LJKppvOOFFi0/img.png"  width= 100% height=250>

  * merge는 준영속(detached) 상태의 엔티티를 영속 상태로 변경할 때 사용한다.

  

  > **준영속 엔티티란?**
  >
  > 영속성 컨텍스트가 더는 관리하지 않는 엔티티를 말한다. 임의로 만들어낸 엔티티라도 기존 식별자를 가지고 있는 경우(JPA가 식별할 수 있는 id를 가지고 있는 경우)에는 준영속 엔티티라고 말한다.
  >
  > 
  >
  > **준영속 엔티티를 수정하는 2가지 방법**
  >
  > \1. 변경 감지
  >
  > \2. 병합(merge)

  

  > **merge()의 동작 방식**
  >
  > 1. merge()를 실행
  > 2. 파라미터로 넘어온 준영속 엔티티의 식별자 값으로 1차 캐시에서 엔티티를 조회
  >    - 만약 1차 캐시에 엔티티가 없으면 데이터베이스에 엔티티를 조회하고 1차 캐시에 저장.
  >    - 무조건 1번은 db 조회를 하므로 성능에 좋지 않을 수 있다. 
  > 3. 조회한 영속 엔티티에 member 엔티티의 값을 채워 넣음
  >    - 이때 member의 모든 값을 영속 엔티티에 채워 넣기 때문에 null 값이 들어갈 수 도 있는 문제가 생긴다.
  >    - 이래서 업데이트 시 merge()보단 변경 감지를 사용하자.
  > 4. 영속 상태의 객체를 반환

  

  ### **merge()의 동작 방식**

  

  # 1. merge()를 실행

  

  # 2. 파라미터로 넘어온 준영속 엔티티의 식별자 값으로 1차 캐시에서 엔티티를 조회

  1. 만약 1차 캐시에 엔티티가 없으면 데이터베이스에 엔티티를 조회하고 1차 캐시에 저장.

  - 무조건 1번은 db 조회를 하므로 성능에 좋지 않을 수 있다. 

  

  # 2-1 1차캐시를 조회하고 엔티티가 없으면 1차 캐시에 저장 

  <img src="https://blog.kakaocdn.net/dn/b53lpM/btrSZZ2dYW4/V1WYc57FyZO0Ro0zFBqQk0/img.png"  width= 100% height=800>

  * DefaultMergeEventListener.java 클래스의 onMerge 메소드
  * 영속성 컨텍스트의 캐시에 엔티티가 key로 존재하는지 확인한다.

  * PersistenceContext 인터페이스의 구현체는 StatefulPersistenceContext로 구현되어있고 entitiesByKey가 1차 캐시 역할을 한다
    * DefaultLoadEventListener -> CacheEntityLoaderHelper.INSTANCE.loadFromSessionCache()메소드를 호출 

  

  <img src="https://blog.kakaocdn.net/dn/cW1oKW/btrS1dL9Mks/sarSkRwFmwlA27kLb91K3k/img.png"  width= 100% height=900>

  * entitiesByKey - 이부분이 1차캐시 역할을 한다.

  

  ---

  <img src="https://blog.kakaocdn.net/dn/vtlEy/btrS3p6o8LM/MxpQPBnJWZEkcuwOiHV7jk/img.png"  width= 100% height=500>

  * 1차 캐시에 엔티티가 존재하지 않는걸 확인

  <img src="https://blog.kakaocdn.net/dn/cTJqHG/btrSXqNfzfL/290OiMZmWty3BFrAUv8Wk1/img.png"  width= 100% height=130>

  * 이후 select 쿼리를 날린다

  <img src="https://blog.kakaocdn.net/dn/dgGr7C/btrSYBOxfsX/AvFTBy0k3ZkQLW60z8rQm0/img.png"  width= 100% height=850>

  

  

  3. 조회한 영속 엔티티에 member 엔티티의 값을 채워 넣음

     - 이때 member의 모든 값을 영속 엔티티에 채워 넣기 때문에 null 값이 들어갈 수 도 있는 문제가 생긴다.

     - 이래서 업데이트 시 merge()보단 변경 감지를 사용하자.

  4. 영속 상태의 객체를 반환

  

  

  ## 왜 그런 것일까?

  

  병합(머지,merge)는 비영속 상태의 엔티티 객체를 영속 상태로 만들어야 하는데,    


  키 값이 있는 엔티티 객체 를 영속 상태로 만들려면 키 값이 존재하니까 데이터베이스에 저장된 엔티티인지 확인이 필요한것이다. 

  * 키 값이 없는 엔티티 객체라면 데이터베이스에 저장이 안되있을 확률이 매우 높지. 테이블 로우는 키가 필요하니까

  

  그러므로, 데이터베이스에 해당 키를 가진 엔티티가 있는지 없는지 확인을 해야만   

  1. insert를 해도 되는지 판단 가능 - 만약 데이터베이스에 같은 키를 가진 엔티티가 있으면 insert 시 pk 중복으로 에러가 나기 때문

  2. insert를 할 지 update를 할지 말지 판단 가능 
     * 없으면 insert를 하고
     * 있으면 영속화 시킨 다음에, 바뀐 부분이 있다면 update를 하고
     * 있으면 영속화 시킨 다음에 바뀐 부분이 없으면 아무 행동도, 쿼리도 실행 안함

  할 수 있기 때문이다. 

  

  

  ### 참조

  

  * https://docs.spring.io/spring-data/jpa/docs/1.5.0.RELEASE/reference/html/jpa.repositories.html#2.2

  * https://vladmihalcea.com/jpa-hibernate-first-level-cache/
  * https://www.baeldung.com/jpa-hibernate-persistence-context