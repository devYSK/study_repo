# 몽고디비 스키마, 다이어그램

* 다이어그램 만들기 : https://www.mongodb.com/ko-kr/docs/relational-migrator/diagrams/manage-diagrams/create-diagrams/#std-label-rm-create-diagrams
* 스키마 분석방법 : https://www.mongodb.com/ko-kr/docs/compass/current/schema/



데이터 모델링 공식 docs : https://www.mongodb.com/ko-kr/docs/manual/data-modeling/

# MongoDB 설계의 6가지 규칙

RDBMS에서는 1:N으로 쓰면 되는 관계를 MongoDB에서는 N의 수가 얼마나 되느냐에 따라 설계를 다르게 해야 한다고 한다.

- One-to-Few
- One-to-Many
- One-to-Squillions
- Two-Way Referencing
- Database denormalization with one-to-many relationships
- Database denormalization with one-to-squillions relationships



- 얼마나 많은 수의 N이 존재하는가?
- N을 직접 접근하여 CRUD를 해야 하는가?
- 자식을 통해 부모를 접근해야 하는가?

#### 스키마 설계 전략

- **임베디드 도큐먼트(Embedded Document)**: 관련된 데이터를 한 도큐먼트 내에 중첩시켜 저장합니다. 예를 들어, 주문(Order) 데이터를 설계할 때, 주문 항목들(Items)을 하나의 도큐먼트로서 주문 도큐먼트에 포함시키는 방식입니다. 이는 자주 함께 조회되는 데이터를 한 곳에 저장하여, 조회 성능을 극대화할 수 있습니다.
- **참조(Reference)**: 데이터를 다른 컬렉션의 도큐먼트와 분리하여 저장하고, 참조를 통해 관계를 맺습니다. 예를 들어, 사용자(User) 데이터와 주문(Order) 데이터를 별도의 컬렉션에 저장하고, 주문 도큐먼트에서 사용자 도큐먼트의 ID를 참조하는 방식입니다. 이 방식은 데이터의 중복을 줄이고, 데이터 일관성을 유지하는 데 유리합니다.
- **정규화(Normalization) vs 비정규화(Denormalization)**: MongoDB에서는 정규화된 스키마보다는 비정규화된 스키마를 사용하는 경우가 많습니다. 자주 조회되는 관련 데이터를 한 도큐먼트에 포함시켜 성능을 높이는 방식입니다.



mongoDB의 경우 매우 유연하게 스키마를 수정이 가능합니다.

우선 스키마 디자인을 할 때 고려할 수 있는 디자인 방식은 `Embedding` or `Referencing` 방식입니다.

### Embedding

------

**장점**

- 하나의 쿼리문으로 필요한 모든 데이터를 가져올 수 있습니다.
- `$lookup` 과 같은 `Join` 동작을 수행하지 않고 데이터를 가져올 수 있습니다.

**한계점**

- `Document`가 커지면 오버헤드 또한 함께 커진다 (문서의 크기를 **제한**하여 쿼리 성능도 챙길 수 있습니다.)
- `Document`는 **16mb 의 크기 제한**을 갖고 있어서 `Embedding`방식을 계속 사용한다면 언젠간 한계에 도달할 수도 있습니다.



## Referencing

참조형 같은 경우 각각의 `Document`들이 가지고 있는 특별한 id 인 `object id` 와 `$lookup` 을 이용해 데이터를 참조할 수 있습니다.

이는 관계형 데이터베이스에서의 `JOIN` 과 **흡사하게 동작**하며, 이러한 구조는 데이터를 효율적이고 관리하기 쉽게 나누면서도 **데이터 간의 관계를 유지**할 수 있습니다.

**장점**

- `Document`를 쪼개어 더욱 작은 단위의 `Document`를 가질 수 있으며, 이를 통해 **16mb 크기 제한**을 피할 수 있습니다.
- 필요하지 않은 정보에 대한 접근을 줄일 수 있습니다.
- `Document` 를 참조하여 데이터를 가져오기에 `Embedding` 방식보다 데이터의 중복을 줄일 수 있습니다.

**한계점**

- 하나의 `Document` 안에 다수의 데이터가 참조형이라면 다수의 쿼리, `$lookup`, `populate` 가 필요합니다. (현재 프로젝트하며 걱정인 부분)

# One-to-One

**User**

```json
{
    "_id": "ObjectId('mdkalsfmk2')",
    "nickname": "junseo",
    "campus": "42seoul",
}
```

위와 같은 `User document` 가 있을 때, `nickname` 이나 `campus` 와 같이 하나만 존재하는 값이 1:1 관계며, `key-value` 로 모델링 할 수 있습니다.



mongodb 에서는 되도록 embedded document 로 데이터를 구성하는 것이 바람직하다.

 embedded document 방식으로 구성할 수 있으면 embedded document 를 사용하는 것이 좋다.

# One-to-Few

**One-to-Few** 관계는 하나의 도큐먼트가 소수의 하위 도큐먼트와 관계를 가지는 경우입니다. 예를 들어, 하나의 제품(Product)이 몇 개의 리뷰(Reviews)를 가질 때 이 관계가 적용됩니다.

아주 적은 수의 Many에 대한 경우다

예를 들어, 하나의 제품(Product)이 몇 개의 리뷰(Reviews)를 가질 때 이 관계가 적용됩니다.

#### 예시:

- 제품(Product)

  ```json
  {
    "_id": ObjectId("64e123abc456def789001234"),
    "name": "Laptop",
    "reviews": [
      { "user": "John", "rating": 5, "comment": "Great laptop!" },
      { "user": "Jane", "rating": 4, "comment": "Good value for money." }
    ]
  }
  ```

이 경우, 리뷰가 몇 개 되지 않으므로(예: 2~3개) 이를 제품 도큐먼트 안에 임베디드(Embedded) 도큐먼트로 저장하는 것이 효율적입니다. 이 방법은 조회할 때 성능이 좋고, 데이터가 자주 변경되지 않는 경우 적합합니다.

장점.

- N의 정보를 가져오기 위해 Join이나 추가적인 쿼리를 할 필요가 없다.

단점.

- N의 정보를 직접 엑세스 할 수 없다.



mongodb 에서 어떤 데이터를를 embedded document 로 구성하려고 했는데
나중에 알고보니 그 embedded document 를 감싸고 있는 부모 도큐멘트 없이 사용할 필요가 생긴다면
해당 데이터는 별로의 스키마로 구성하는 것이 바람직하다.

# One-to-Many 자식 참조 

많은 수의 N에 대한 경우다. 제품과 부품의 관계를 예로 들어 설명이 되어있다.
한 제품에는 수백개의 부품이 존재할 수 있다. 이 경우 부모가 되는 Document에 ObjectID를 저장하여 referencing을 할 수 있다.

예를 들어, 하나의 고객(Customer)이 여러 개의 주문(Orders)을 가질 때 이 관계가 적용됩니다.

#### 예시:

- **고객(Customer)** 컬렉션:

  ```json
  {
    "_id": ObjectId("64e123abc456def789001234"),
    "name": "Alice",
    "order_ids": [
      ObjectId("64e123abc456def789001235"),
      ObjectId("64e123abc456def789001236"),
      ObjectId("64e123abc456def789001237")
    ]
  }
  ```

- **주문(Order)** 컬렉션:

  ```json
  {
    "_id": ObjectId("64e123abc456def789001235"),
    "customer_id": ObjectId("64e123abc456def789001234"),
    "items": ["item1", "item2"],
    "total": 100
  }
  {
    "_id": ObjectId("64e123abc456def789001236"),
    "customer_id": ObjectId("64e123abc456def789001234"),
    "items": ["item3"],
    "total": 50
  }
  {
    "_id": ObjectId("64e123abc456def789001237"),
    "customer_id": ObjectId("64e123abc456def789001234"),
    "items": ["item1", "item4"],
    "total": 150
  }
  ```

이 경우, 주문이 많아질 수 있기 때문에 주문을 별도의 컬렉션에 저장하고, 고객 도큐먼트에서 주문 ID 목록을 참조하는 방식을 사용합니다. 이로 인해 데이터의 중복을 피할 수 있으며, 필요할 때 조인을 통해 데이터를 결합할 수 있습니다.

장점.

- N은 부모에 종속적이지 않기 때문에 N을 독립적으로 추가, 조회, 수정, 삭제 할 수 있다.
- RDBMS와 달리 추가적인 Collection이 없이 Many-to-Many(N:N) 스키마를 설계할 수 있다.

단점

* 하위 컬렉션을 가져오기 위해 추가적인 쿼리를 실행해야 한다 

*가능한 경우* `$lookup` *이나* `populate` *같은* `Join` *을 피하지만 이를 통해 더 나은 스키마 디자인 설계가 가능하다면 걱정하지 말고 사용하기!*



mongodb 에서 JOIN 이나 $lookup 실행에 비용이 많이 든다는 것은 사실이지만
별도의 스키마로 구성하는 것이 디자인 상으로 더 바람직한 설계라는 판단이 선다면 별도의 스키마로 구성하는 것을 망설일 필요는 없다.

# One-to-Squillions 부모 참조 

**One-to-Squillions** 관계는 하나의 도큐먼트가 아주 많은 하위 도큐먼트와 관계를 가지는 경우입니다. 예를 들어, 하나의 블로그 포스트(Blog Post)에 수백만 개의 댓글(Comments)이 달릴 수 있습니다.

MongoDB의 Document의 size는 16MB로 제한되어 있다. 12B를 사용하는 ObjectID가 아주 많아지게 되면 문제가 생긴다.

- **블로그 포스트(Blog Post)** 컬렉션:

  ```json
  { "_id": "post1", "title": "How to Learn MongoDB", "author": "Bob" }
  ```

- **댓글(Comment)** 컬렉션:

  ```json
  { "_id": "c1", "post_id": "post1", "user": "Alice", "comment": "Great post!" }
  { "_id": "c2", "post_id": "post1", "user": "John", "comment": "Very informative!" }
  ...
  ```

이 경우, 댓글이 매우 많아질 수 있으므로, 댓글을 블로그 포스트 도큐먼트에 임베디드하는 대신 별도의 컬렉션으로 분리합니다. 이를 통해 블로그 포스트 도큐먼트의 크기를 작게 유지하고, 댓글에 대해 별도로 인덱스를 걸어 조회 성능을 최적화할 수 있습니다.

장점.

- N은 부모에 종속적이지 않기 때문에 N을 독립적으로 추가, 조회, 수정, 삭제 할 수 있다.

단점.

- 로그정보를 가져올 때 추가적으로 부품정보를 가져오기 위한 쿼리를 실행해야 한다.



**ChatRoom**

```json
{
    "_id": "ObjectId('4321')",
    "createdAt": ISODate("2021-04-28T09:42:41.382Z"),
    "users":"???",
}
```

**Chat**

```json
{
    "_id": "ObjectId('328492489')",
    "chatRoom_id": "ObjectId('4321')", // 부모의 obj id 를 참조
    "createdAt": ISODate("2021-04-29T12:42:41.382Z"),
    "content":"피곤쓰",
}
```

이러한 채팅방, 채팅 스키마를 디자인할 때, `One-to-Many` 방식에서 `Embedding` 구조를 사용한다면, 채팅 데이터가 300개 넘어갔을 때 `Document` 의 **16mb 제한을 초과**해 버릴 것입니다. 더불어 `Referencing` 구조를 사용한다 하더라도 `ObjectID` 가 몇 천 개 쌓인다면 똑같이 용량 초과될 것입니다.

`One-to-Squillions` 구조를 사용한다면 채팅방의 `ObjectID` 로 해당 채팅방에서 주고받은 데이터를 쿼리 할 수 있게 되고, 용량 제한을 피하면서도 채팅방과 채팅의 관계를 유지시킬 수 있습니다.

# Two-Way Referencing

**Two-Way Referencing**은 두 컬렉션 간에 양방향으로 참조를 걸어두는 방식입니다. 예를 들어, 사용자가 특정 주문을 참조하고, 주문도 해당 사용자를 참조할 수 있습니다

#### 예시:

- **고객(Customer)** 컬렉션:

  ```json
  { "_id": "c1", "name": "Alice", "orders": ["o1", "o2"] }
  ```

- **주문(Order)** 컬렉션:

  ```json
  { "_id": "o1", "customer_id": "c1", "items": ["item1", "item2"] }
  ```

이 방식은 필요할 때 두 컬렉션 간의 관계를 빠르게 확인할 수 있습니다. 다만, 데이터 일관성을 유지하기 위해 양쪽 참조가 모두 정확하게 업데이트되어야 합니다.

장점.

- 자식 Document에서 빠르고 쉽게 무보를 찾을 수 있다.

단점.

- 관계의 변경시 두개의 Document를 모두 수정해야 한다. 위 예제에서 task의 owner를 변경할 경우 person의 Documenut도 같이 수정해줘야 한다.

# Database denormalization with one-to-many relationships

**데이터베이스 비정규화**는 데이터 모델을 단순화하고 성능을 높이기 위해 데이터 중복을 허용하는 방법입니다. One-to-Many 관계에서, 데이터를 비정규화하여 한 도큐먼트에 결합된 데이터를 포함시킬 수 있습니다.

#### 예시:

- 고객(Customer)

   컬렉션:

  ```json
  {
    "_id": "c1",
    "name": "Alice",
    "orders": [
      { "_id": "o1", "items": ["item1", "item2"], "total": 100 },
      { "_id": "o2", "items": ["item3"], "total": 50 }
    ]
  }
  ```

이 경우, 고객 도큐먼트에 주문 데이터를 임베디드 도큐먼트로 포함시켜 조회 성능을 높일 수 있습니다. 하지만 주문 데이터가 변경될 때마다 고객 도큐먼트를 업데이트해야 하는 단점이 있습니다.

# Database denormalization with one-to-squillions relationships



**One-to-Squillions 관계에서의 데이터베이스 비정규화**는 매우 많은 하위 데이터를 처리할 때 신중하게 접근해야 합니다. 일반적으로 비정규화하지 않고 별도의 컬렉션으로 분리하는 것이 더 효율적입니다.

#### 예시:

- 비정규화하지 않은 댓글(Comment) 컬렉션

  ```json
  { "_id": "c1", "post_id": "post1", "user": "Alice", "comment": "Great post!" }
  { "_id": "c2", "post_id": "post1", "user": "John", "comment": "Very informative!" }
  ```

댓글이 수백만 개가 되는 상황에서는 비정규화하지 않고, 댓글을 별도의 컬렉션에 저장하고 필요한 경우 특정 포스트에 대한 댓글만 조회하는 방식이 더 적합합니다. 이는 데이터베이스의 성능과 관리 용이성을 높입니다.





`One-to-One` : **key-value 선호**

`One-to-Few` : **Embedding 선호**

`One-to-Many` : **Embedding 선호**

`One-to-Squillions` : **Referencing 선호**

`Many-to-Many` : **Referencing 선호**

| Embed 권장                         | Reference 권장                       |
| ---------------------------------- | ------------------------------------ |
| 변경이 (거의)없는 정적인 데이터    | 변경이 잦은 데이터                   |
| 함께 조회되는 경우가 빈번한 데이터 | 조회되는 경우가 많지 않은 데이터     |
| 빠른 읽기가 필요한 경우            | 빠른 쓰기가 필요한 경우              |
| 결과적인 일관성이 허용될 때        | 즉각적으로 일관성이 충족되어야 할 때 |

* https://dev.gmarket.com/32
* 

