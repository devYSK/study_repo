# 영속성 전이(Cascade) ManyToOne 시 주의할점



@ManyToOne(cascade=CascadeType.ALL) 또는 @ManyToOne(cascade=CascadeType.REMOVE) 일 때, Many쪽 (자식 엔티티)를 제거할 때 주의 하여야 한다.

* 연관된 부모도 삭제하기 때문이다.

* 연관된 부모를 삭제하게 된다면 그 부모를 참조하고 있던 자식 엔티티들이 고아객체가 되어버린다. 



### 상황

Member(회원)과 Order(주문) 테이블의 관계가 1:N 이라고 할 때,

Member와 Order는 `1:N 단방향 관계`로 Order는 `@ManyToOne- Member` 필드를 참조한다.

이 때, Order가 @ManyToOne(cascade=CascadeType.ALL) 또는 @ManyToOne(cascade=CascadeType.REMOVE) 일 때

Order를 삭제하게 된다면 참조하고 있는 Member도 같이 지우게 되어서 Member를 참조하고 있는 Order들이 발생하게 된다(고아객체)



@ManyToOne(cascade=CascadeType.ALL)

 \- Order가 삭제되면 Member도 삭제된다.

@OneToMany(cascade=CascadeType.ALL)

 \- Member이 삭제되면 Order도 삭제된다.

 \- Member을 추가할때 Order도 추가한다.



* Member Class

```java
@Getter
@Entity
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	...
  
	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
	private List<Order> orders = new ArrayList<>();
  
}
```

* Order Class

```java
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
public class Order extends BaseEntity{

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	...
    
	@ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
	@JoinColumn(name = "member_id", referencedColumnName = "id")
	private Member member;
}
```

* Test code

```java
@Test
@DisplayName("양방향 연관관계 CasCade 삭제")
void test() {
		//given
		Member member = createMember();
		Order order = createOrder(member);

		memberRepository.save(member);
		orderRepository.save(order);

		//when
		orderRepository.delete(order);

		Optional<Member> memberOptional = memberRepository.findById(member.getId());

		//then
		Assertions.assertThat(memberOptional).isEmpty();
		Assertions.assertThat(memberRepository.count()).isZero();
}
```

* 실행 쿼리

```sql
Hibernate: 
    insert 
    into
        member
        (id, address, age, created_at, created_by, description, name, nick_name) 
    values
        (default, ?, ?, ?, ?, ?, ?, ?)
Hibernate: 
    insert 
    into
        orders
        (created_at, created_by, member_id, memo, order_datetime, order_status, id) 
    values
        (?, ?, ?, ?, ?, ?, ?)
Hibernate: 
    delete 
    from
        orders 
    where
        id=?
Hibernate: 
    delete 
    from
        member 
    where
        id=?
Hibernate: 
    select
        count(*) 
    from
        member m1_0
```







엔티티의 상태 전환은 상위(부모)엔티티에서 하위 엔티티(자식)으로 전파되어야 하며, 그 반대인 자식에서 상위로 전파 되면 안된다고 생각한다.

* 부모가 자식에게 준 것을 관리해야지, 자식이 부모에게 받은것을 맘대로 지워서는 안된다랑 비슷한 의미로 해석해도 될 것 같다.



그러므로, @ManyToOne 을 사용한 자식 엔티티에서 Cascade를 지정할때는 주의 하여야 하며, @OneToOne에서도 부모와 자식, 그리고 전파 방향을 생각해서 알맞은 위치에 써야한다.

* 데이터베이스에서의 Cascade와 JPA Entity에서의 Cascade는 약간 다르다.



먼저 데이터베이스의 cascade와 JPA에서의 cascade에 대해 알아보고 해결 방법을 찾아보았다.



## 데이터베이스에서의 Cascade란?

Cascade의 사전적인 의미 :  종속, 연쇄, 폭포

테이블간의 관계에서, 기본키를 참조하는 외래키가 있을 때 부모 테이블의 값이 수정이나 삭제가 발생하면, 해당 값을 참조하고 있는 자식 테이블의 역시 종속적으로 수정 및 삭제가 일어나도록 하는 옵션이다.



* `on delete cascade` : 참조되고 있는 부모 테이블의 값을 삭제 시 해당 값을 참조하고 있는 자식 테이블의 레코드를 함께 삭제
*  `on update cascade` : 참조되고 있는 부모 테이블의 값을 수정시, 자식 테이블에서 해당 값을 참조하고 있는  값이 함께 수정 



## JPA에서의 Cascade란?(영속성 전이)

cascade 옵션이란 Entity의 상태 변화를 전파시키는 옵션이다.



* @OneToMany 나 @ManyToOne에 어노테이션에서 옵션으로 줄 수 있는 값이다.

만약 Entity의 상태 변화가 있으면 연관되어 있는(ex. @OneToMany, @ManyToOne) Entity에도 상태 변화를 전이시키는 옵션이다.

* 부모 엔티티의 상태 변화가 자식 엔티티의 상태에 전이되어 영향을 끼치는것

@OneToMany 나 @ManyToOne 둘 다 default 값이 없으며, 기본적으로 아무 것도 전이시키지 않는다.



Entity의 Cascade를 알기 위해서는 Entity의 상태와 Cascade Type(Option)에 대해서 알아야 한다.  

### Entity의 상태



1. `Transient(new, 비영속)`: 객체를 생성하고, 값을 주어도 JPA나 hibernate가 그 객체에 관해 아무것도 모르는 상태. 즉, 데이터베이스와 매핑된 것이 아무것도 없다.
2. `Persistent(Managed, 영속)`: 저장을 하고나서, JPA가 아는 상태(관리하는 상태)가 된다. 그러나 .save()를 했다고 해서, 이 순간 바로 DB에 이 객체에 대한 데이터가 들어가는 것은 아니다. JPA가 persistent 상태로 관리하고 있다가 flush 시점에 데이터를 저장한다.(1차 캐시, Dirty Checking(변경 감지), Write Behind(Lazy, 최대한 늦게, 필요한 시점에 DB에 적용) 등의 기능을 제공한다)
3.  `Detached(준영속)`: JPA가 더이상 관리하지 않는 상태. JPA가 제공해주는 기능들을 사용하고 싶다면, 다시 persistent 상태로 돌아가야한다.
4.  `Removed(삭제)`: JPA가 관리하는 상태이긴 하지만, 실제 commit이 일어날 때, 삭제가 일어난다.



* **cascade는 이러한 상태변화를 전이시키는 것이다.**



### Cascade Type(Cascade Option)

여기서 말하는 필드란, @OneToMany 나 @ManyToOne을 사용한 연관된 엔티티 필드이다.

- `CascadeType.PERSIST`
  * 엔티티를 영속화 할 때 이 필드에 보유 된 엔티티도 영속화(유지)한다. EntityManager가 flush 중에 새로운 엔티티를 참조하는 필드를 찾고 이 필드가 CascadeType.PERSIST를 사용하지 않으면 오류이다
- `CascadeType.MERGE`
  * 엔티티를 병합 할 때, 이 필드에 연관 된 엔티티도 병합한다.
- `CascadeType.REFRESH`
  * 상위 엔티티를 새로 고칠 때(Refresh), 이 필드에 연관 된 엔티티도 새로 고친다.
- `CascadeType.REMOVE`
  * 엔티티를 삭제할 때, 이 필드에 연관 된 엔티티도 삭제한다.
- `CascadeType.DETACH`
  * 부모 엔티티가 detach()를 수행하게 되면, 연관된 엔티티도 detach() 상태가 되어 변경사항이 반영되지 않는다.
- `CascadeType.ALL`
  * 모든 Cascade 적용한다





## ManyToOne에서 cascade 없이 삭제하는법?

* `@OnDelete`



JpaEntity의 Cascade는 JPA에 의해 처리되어 JPA에 의해 외래 키를 찾아가며 참조하는 레코드를 제거한다.



그에 반해, @OnDelete는 DB에 의해 직접 처리된다.

@OnDelete(action = OnDeleteAction.CASCADE)을 달아주면  Cascade와 같은 효과를 누릴 수 있다.

* @OnDelete는 DB에서 처리해주기에 단일한 쿼리를 통해 연쇄적으로 제거할 수 있지만 Cascade의 경우에는 여러개의 쿼리를 날린다.

### @OnDelete와 CascadeType.ALL의 차이

* @OnDelete와 CascadeType.ALL은 둘 다 Cascade의 DELETE 기능을 갖고 있다.

* 즉, 기본키 데이터를 삭제할 경우 참조하던 외래키 데이터도 전부 삭제된다.

 

### **@OnDelete**

- DBMS 레벨에서 작동
- @OnDelete(action = OnDeleteAction.CASCADE)
- DDL 생성시 cascade 제약 조건이 생성 됨.
  - DB 테이블에 접근해서 ON DELETE CASCADE를 붙여 생성한다.



### **casecade=CascadeType.REMOVE**

- JPA 레벨에서 작동
- JPA가 부모 엔티티를 삭제할 때 연관된 자식 데이터에 대한 DELETE 쿼리를 실행 함
- CascadeType.ALL 기능은 테이블을 바꾸는 것이 아니라 런타임 도중에 JPA에 의해 실행되도록 만드는 것



1. cascade = CascadeType.REMOVE를 설정할 경우

   * 자식 엔티티를 삭제하는 쿼리가 먼저 실행되고, 그 다음 부모 엔티티를 삭제하는 쿼리가 실행 된다. 

   * 자식 엔티티 한 건을 삭제 하려고 하면 부모 테이블에 연관되어 있는 데이터가 존재 하므로 에러가 발생한다.

     (부모와 연관된 자식이 한건이라면 이 에러가 발생하지 않는다.)





그러나 @OnDelete가 효과적으로 보일 순 있으나 on delete cascade에 의해 어떠한 레코드의 참조 레코드까지 연쇄적으로 삭제해버릴 수 있는 여지가 존재한다는 단점이 존재한다.



### **@OnDelete(action= OnDeleteAction.CASCADE)만 설정**

```sql
Hibernate: 
   alter table if exists orders 
   add constraint FKpktxwhj3x9m4gth5ff6bkqgeb 
   foreign key (member_id) 
   references member  
   on delete cascade
```

```java
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
public class Order extends BaseEntity{

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	...
    
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", referencedColumnName = "id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Member member;
}
```





JPA 에는 CascadeType.REMOVE 가 달려있고 DDL 에는 ON DELETE CASCADE 가 달려있지 않는 경우가 관리적인 면에서 좀 더 안정적으로 볼 수 있다고 생각한다. 

CascadeType.REMOVE를 사용하는 DDL 에는 ON DELETE CASCADE 가 걸려있지 않으므로 DB 에 직접 접근하여 DELETE 쿼리를 사용하는 경우 의존성이 걸린 레코드 삭제가 힘들다. 따라서 운영에서 있을 실수를 줄일 수 있다. 의존성이 걸린 레코드를 깔끔하게 삭제하고 싶다면 오롯이 JPA 의 delete 인터페이스에 의존해야한다. 하지만 프로그램을 재배포 하지않는 이상 프로그램을 통해 레코드를 삭제하려는 시도는 하지 못한다. 즉 이미 동작중인 서버에서 데이터를 완전히 날려버리는 대참사는 일어나기 쉽지 않다는 의미다.



* JPA 에는 CascadeType.REMOVE 가 달려있고 DDL 에는 ON DELETE CASCADE 가 달려있지 않다고 했을 때 

| Jpa 인터페이스 delete(Member member)                         | cascade 반영해줌                                             |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| Jpa 인터페이스 deleteAll(List`<Member>` members)             | cascade 반영해줌delete 쿼리가 n+1 번 발생cascade 가 걸려있으면 cascade delete 쿼리도 n+1 번 발생 |
| @Query("DELETE FROM MemberEntity m WHERE m IN :memberEntities") public void deleteAll(`List<Member>` memberEntities); | cascade 반영 안해줌 delete 쿼리가 1 번 발생                  |
| deleteInBatch(List`<Member>` members) 인터페이스             | cascade 반영 안해줌 delete 쿼리가 1 번 발생                  |



### 참조

* https://ynzu-dev.tistory.com/27

* https://stir.tistory.com/163
* https://kok202.tistory.com/174

* https://wale.tistory.com/entry/JPA-%EC%98%81%EC%86%8D%EC%84%B1-%EC%A0%84%EC%9D%B4CASCADE%EC%99%80-%EA%B3%A0%EC%95%84-%EA%B0%9D%EC%B2%B4

* https://myunji.tistory.com/492
* https://gilssang97.tistory.com/71
* https://qodbtn.tistory.com/315
* https://tecoble.techcourse.co.kr/post/2021-08-15-jpa-cascadetype-remove-vs-orphanremoval-true/