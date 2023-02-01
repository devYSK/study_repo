

* [querydsl docs](http://querydsl.com/static/querydsl/3.7.2/reference/ko-KR/html/ch03s02.html)

## **프로젝션**

프로젝션이란 **"Select절에 조회 대상을 지정"**하는 것이다

 



조회대상이 하나라면 Return Type은 해당 조회 대상의 Type으로 정해진다

조회대상이 여러개라면 QueryDSL에서는 프로젝션 대상으로 여러 필드를 선택하게 되면 **Tuple**이라는 "Map과 비슷한" Type을 return해준다

쿼리에 대한 결과를 엔티티가 아닌 **"특정 객체(DTO.. 등)"**로 받고 싶다면 **Bean Population**을 사용한다







## QueryDsl `Projections` 을 사용해서 **1:N 관계의** `List<Object>` 를 추출하는 코드

- 부모 DTO

```java
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParentDto {
  private UUID id;
  private String name;

  private List<ChildDto> children = new ArrayList<>();
}
```

- 자식 DTO

```java
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChildDto {
  private UUID id;
  private int name;
}
```

- 1:N DTO Projection Query 작성

```java
List<ParentDto> results = new JPAQueryFactory(em)
  .from(parent)
  .innerJoin(child)
  .on(parent.id.eq(child.parent.id))
  .transform(
    groupBy(parent.id).list(
      Projections.fields(
        ParentDto.class,
        parent.id,
        parent.name,
        list( // `GroupByExpression` 의 static method `list()`
          Projections.fields(
            ChildDto.class,
            child.id,
            child.name
          )
        ).as("children")
      )
    )
  );
```



# DTO로 매핑 응용 1



dto

```java
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderByStoreResponse {

	private String orderId;

	private String storeId;

	private OrderStatus orderStatus;

	private String nickname;

	private List<OrderItem> items;

	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class OrderItem {

		private String itemName;

		private Integer quantity;

		private Integer espressoShotCount;

		private Integer vanillaSyrupCount;

		private Integer classicSyrupCount;

		private Integer hazelnutSyrupCount;

		private BeverageOption.Milk milkType = null;

		private BeverageOption.Coffee espressoType = null;

		private BeverageOption.MilkAmount milkAmount = null;

		private BeverageOption.Size cupSize;

		private BeverageOption.CupType cupType;
	}
	
}
```



query

```java
package com.prgrms.bdbks.domain.order.repository;

import static com.querydsl.core.group.GroupBy.*;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;

@RequiredArgsConstructor
public class OrderSupportImpl implements OrderSupport {

	private final JPAQueryFactory query;

	@Override
	public Slice<OrderByStoreResponse> findBy(String storeId, OrderStatus orderStatus, String cursorOrderId,
		Pageable pageable) {

		List<OrderByStoreResponse> result = query.select(order)
			.from(order)
			.leftJoin(order.orderItems, orderItem)
			.leftJoin(orderItem.item, item)
			.leftJoin(orderItem.customOption, customOption)
			.leftJoin(user).on(user.id.eq(order.userId))
			.where(
				generateCursorId(cursorOrderId, pageable.getSort()),
				order.storeId.eq(storeId),
				order.orderStatus.eq(orderStatus)
			)
			.limit(pageable.getPageSize() + 1)
			.orderBy(getOrder(pageable))
			.orderBy(order.id.desc())
			.transform(GroupBy.groupBy(order.id).list(
					Projections.fields(OrderByStoreResponse.class,
						order.id.as("orderId"),
						order.orderStatus.as("orderStatus"),
						user.nickname.as("nickname"),
						order.storeId.as("storeId"),
						GroupBy.list(Projections.list(Projections.fields(
							OrderByStoreResponse.OrderItem.class,
							item.name.as("itemName"),
							orderItem.quantity.as("quantity"),
							customOption.espressoShotCount.as("espressoShotCount"),
							customOption.vanillaSyrupCount.as("vanillaSyrupCount"),
							customOption.classicSyrupCount.as("classicSyrupCount"),
							customOption.hazelnutSyrupCount.as("hazelnutSyrupCount"),
							customOption.milkType.as("milkType"),
							customOption.espressoType.as("espressoType"),
							customOption.milkAmount.as("milkAmount"),
							customOption.cupSize.as("cupSize"),
							customOption.cupType.as("cupType")
						))).as("items")
					)
				)
			);

		return SliceUtil.toSlice(result, pageable);
	}

	private BooleanExpression generateCursorId(String cursorOrderId, Sort sort) {
		if (cursorOrderId == null) {
			return null;
		}

		return order.id.lt(cursorOrderId);
	}

	private OrderSpecifier[] getOrder(Pageable pageable) { .. sort로 get Order함
		List<OrderSpecifier> orders = new ArrayList<>();

		orders.add(order.id.desc());

		for (Sort.Order o : pageable.getSort()) {

			PathBuilder<Object> orderByExpression = new PathBuilder<Object>(Order.class, order.getMetadata());

			orders.add(new OrderSpecifier(
				o.isAscending() ? com.querydsl.core.types.Order.ASC : com.querydsl.core.types.Order.DESC,
				orderByExpression.get(o.getProperty())));
		}

		return orders.toArray(OrderSpecifier[]::new);
	}

}
```

transform을 사용하면 groupBy에 지정된 key를 기준으로 List를 만들 수 있게 된다.

* fetchJoin()이 아니다. dto로 조회시 fetchJoin 할 수 없다.

* key를 기준으로 그룹화하여 매핑하게 된다. 



**주의할 점.  order의 id로 커서기반 페이징을 하는데, orderId가 null 일때, orderBy절에 orderId가 없다면 예외가 발생한다**

> "fromElement" is null 예외







# DTO로 받기 정리 - 빈 생성

쿼리에 대한 결과를 엔티티가 아닌 **"특정 객체(DTO등)"**로 받고 싶다면 **Bean Population**을 사용한다

 

#### ## 1) 프로퍼티 접근

프로퍼티 접근은 결과에 대한 DTO에 **"필드의 getter/setter"**를 생성하고 **Projections.bean()**을 통해서 결과를 받아오면 된다

- 첫번째 파라미터 = 결과에 대한 클래스 (ItemDto.class)
- 두번째/세번째/.... 파라미터 = 매핑될 필드

Projections.bean()은 ***"Setter"를 사용해서 값을 채우게 된다***

그리고 **파라미터가 없는 생성자(access = Public)가 "반드시" 필요**하다

```java
// MemberOrderDto
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberOrderDto {
    private Long memberId;
    private Long orderId;
    private String memberName;
    private Integer orderAmount;
}


// In QuerydslRepository
List<MemberOrderDto> fetch = query.select(
                Projections.bean(
                        MemberOrderDto.class,
                        order.member.id.as("memberId"),
                        order.id.as("orderId"),
                        order.member.username.as("memberName"),
                        order.orderAmount.as("orderAmount")
                )
        ).from(order)
        .innerJoin(order.member)
        .innerJoin(order.product)
        .fetch();

```



## 2. 필드 직접 접근

필드 직접 접근은 **Projections.fileds()**를 이용하여 "필드에 직접 접근"해서 값을 채워 객체를 만든다.

- 여기서 필드를 private로 설정해도 제대로 동작한다

그리고 **파라미터가 없는 생성자(access = Public)가 "반드시" 필요**하다

```java
List<MemberOrderDto> fetch = query.select(
                Projections.fields(
                        MemberOrderDto.class,
                        order.member.id.as("memberId"),
                        order.id.as("orderId"),
                        order.member.username.as("memberName"),
                        order.orderAmount.as("orderAmount")
                )
        ).from(order)
        .innerJoin(order.member)
        .innerJoin(order.product)
        .fetch();
 
```





## 3. 생성자를 이용한 DTO 조회

생성자를 사용한 DTO조회는 **Projections.constructor()**를 통해서 DTO의 생성자를 사용해서 값을 return하게 된다

- 여기서 지정한 프로젝션과 생성자의 파라미터 순서는 "정확하게" 일치해야한다
- 파라미터가 없는 생성자가 없어도 "필드에 대한 생성자"만 정확하게 있으면 동작한다

```java
List<MemberOrderDto> fetch = query.select(
                Projections.constructor(
                        MemberOrderDto.class,
                        order.member.id.as("memberId"),
                        order.id.as("orderId"),
                        order.member.username.as("memberName"),
                        order.orderAmount.as("orderAmount")
                )
        ).from(order)
        .innerJoin(order.member)
        .innerJoin(order.product)
        .fetch();
```



## 4. @QueryProjection - QDTO 클래스

DTO클래스의 생성자 레벨에 **@QueryProjection** 애노테이션을 붙이게 되면 **DTO 클래스도 QType이 생성**되고 이를 그대로 결과로 매핑시켜서 받아오면 된다

- 기본적인 Projections을 통해서 DTO 조회보다 TypeSafe하지만, 물론 QueryDSL에 더 종속적이다는 단점도 존재한다

```java
@Data
public class MemberOrderDto {
    private Long memberId;
    private Long orderId;
    private String memberName;
    private Integer orderAmount;
 
    @QueryProjection
    public MemberOrderDto(Long memberId, Long orderId, String memberName, Integer orderAmount) {
        this.memberId = memberId;
        this.orderId = orderId;
        this.memberName = memberName;
        this.orderAmount = orderAmount;
    }
}


List<MemberOrderDto> fetch = query.select(
                new QMemberOrderDto(
                        order.member.id,
                        order.id,
                        order.member.username,
                        order.orderAmount
                )
        ).from(order)
        .innerJoin(order.member)
        .innerJoin(order.product)
        .fetch();
 
```

- as를 통해서 프로젝션 필드에 대한 Alias를 붙여주지 않아도 DTO QType이 알아서 번역해준다.