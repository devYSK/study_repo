

# Jpa Entity Data Type



Entity의 filed Data Type을 Primitve로 지정해야 할까 Wrapper Type으로 지정해야 할까?



---

### 결론

* PK에는 `Long`(Wrapper Class) 을 사용하자
  - Wrapper Class 를 사용함으로써 Null을 대입해놓을 수 있는데, 명시적으로 PK가 아직 할당되지 않았음을 의미할수 있다. 반면 primivite type은 null 을 표시할 방법이 없다
  - JPA에서  Wrapper 클래스가 **Number의 하위 타입**인 경우(int, long 등)에는 **해당 값이 0인 경우**에만 이를 새로운 엔티티라 판단한다.
    * 0이 아니라면, save 시 merge가 동작하여 select + insert 쿼리 2번을 실행하게 된다. 

  


Null이 필요한 컬럼인지 생각하자

* 데이터베이스에서는 Null이 필요한 경우도 있다.

  * 저장하는 컬럼이 비어있는 값을 어떻게 표현할 지, 0을 표현할지 null을 표현할지 선택해서 사용해야 한다.
  * 만약 null이 필요하다면 Wrapper Type을 사용하는 것이 좋다. Wrapper Type은 0도, null도 표현할 수 있기 때문이다. 

* 데이터의 null 여부를 판단할 수 있어야 한다.

  * 데이터의 null 여부를 판단할 수 있어야 한다 null 데이터도 가져올 수 있어야 할 때가 있기 때문이다.
  * null 데이터의 존재 자체를 문제로 말할 수 있지만, 경험상 실무 DB에는 null인 컬럼이 꽤 존재한다.

* 소수 필드의 boxing-unboxing은 배제하자.

  - 컴퓨팅 능력이 충분히 저렴해지고 높아져서 크게 염려 안해도 된다. 

  - **boxing-unboxing** 이슈는 반복문과 같이 사용될 때 발생한다.

* `코딩 스타일의 일관성을 가져갈 수 있다.`

* 일부 **@Ttransient** 필드에 대해서는 사용하기도 한다.

* https://stackoverflow.com/questions/2331630/entity-members-should-they-be-primitive-data-types-or-java-data-types



> * We recommend that you declare consistently-named identifier attributes on persistent classes and that you use a nullable (i.e., non-primitive) type.
>
> * https://docs.jboss.org/hibernate/orm/5.3/userguide/html_single/Hibernate_User_Guide.html#entity-pojo-identifier



- 가장 일반적인 구현체인 Hibernate의 문서를 보면 명시적으로 non-primitive type의 사용을 권장한다..
- id를 primitive type으로 하면 0과 null을 구분할 수 없다.
  - primitive type의 기본 값이 0이기 때문이다.

---





| **Primitive Type** | **Wrapper Class** |
| ------------------ | ----------------- |
| byte               | Byte              |
| short              | Short             |
| int                | Integer           |
| long               | Long              |
| float              | Float             |
| double             | Double            |
| boolean            | Boolean           |
| char               | Character         |



### Primitive Type` VS `Wrapper Class



* Primitive Type : 변수를 선언하면 Stack 저장공간에 데이터가 저장됨,
  - 변수가 Stack 에 저장되고, 값 자체를 담고있음

* Wrapper Class : 참조 변수는 Stack에 저장되고, 데이터는 Heap 영역에 저장된다 





## 두 자료형의 차이점

1. 값을 저장하는가(기본타입), 주소를 참조하는가(참조타입)
2. jvm내부에서 메모리가 어디 영역에 저장되는가 (stack - 기본형이 저장됨, heap - 객체들이 저장됨)

3. primitive Type은 null을 저장할 수 없다. 



## Primitive Type 의 장점

- 공유참조문제가 없다 (하지만 Wrapper Class 도 불변 객체이다 )
- 성능이 조금이라도 더 좋다 (아니 좋을것같다)
- nullPointException 발생이 원천봉쇄된다 (null을 가리킬 수 없다.)

## Wrapper Class 사용의 장점

- 멀티스레드에서 사용할 수 있다
- nullPointException 이 발생할수는 있지만
  - null 임을 표시할수 있다. 숫자0이 의미를 갖는 경우도 분명히 있기때문
- 하지만 명시적으로 vaildataion 을 사용할 수 있다
  - @NonNull, @NotNull.. 등등



## 결론

Primitive Type을 사용하게 되면 null을 표현할 수가 없다.

Primitive Type을 사용하게 되면, 비어있단 값을 0으로 표현해야 하는데, 0이라는 값도 충분히 비지니스 로직에서 필요한 값일 수 있다.

* 또한 Primitive Type으로 엔티티를 찾을 수 없다면 0을 써야하는게 최선 같지만 그럴 수 없다
* 0이라는 값으로 실수로 초기화 할 수 있는 문제도 존재한다. 





1. PK에는 `Long`(Wrapper Class) 을 사용하자

   - Wrapper Class 를 사용함으로써 Null을 대입해놓을 수 있는데, 명시적으로 PK가 아직 할당되지 않았음을 의미할수 있다. 반면 primivite type은 null 을 표시할 방법이 없다

   - JPA에서  Wrapper 클래스가 **Number의 하위 타입**인 경우(int, long 등)에는 **해당 값이 0인 경우**에만 이를 새로운 엔티티라 판단한다.
     * 0이 아니라면, save 시 merge가 동작하여 select + insert 쿼리 2번을 실행하게 된다. 

 

2. Null이 필요한 컬럼인지 생각하자

   * 데이터베이스에서는 Null이 필요한 경우도 있다.
     * 저장하는 컬럼이 비어있는 값을 어떻게 표현할 지, 0을 표현할지 null을 표현할지 선택해서 사용해야 한다.
     * 만약 null이 필요하다면 Wrapper Type을 사용하는 것이 좋다. Wrapper Type은 0도, null도 표현할 수 있기 때문이다. 

   * 데이터의 null 여부를 판단할 수 있어야 한다.
     * 데이터의 null 여부를 판단할 수 있어야 한다 null 데이터도 가져올 수 있어야 할 때가 있기 때문이다.
     * null 데이터의 존재 자체를 문제로 말할 수 있지만, 경험상 실무 DB에는 null인 컬럼이 꽤 존재한다.

   * 소수 필드의 boxing-unboxing은 배제하자.

     - 컴퓨팅 능력이 충분히 저렴해지고 높아져서 크게 염려 안해도 된다. 

     - **boxing-unboxing** 이슈는 반복문과 같이 사용될 때 발생한다.

   * `코딩 스타일의 일관성을 가져갈 수 있다.`

   * 일부 **@Ttransient** 필드에 대해서는 사용하기도 한다.

   * https://stackoverflow.com/questions/2331630/entity-members-should-they-be-primitive-data-types-or-java-data-types



3. Not Null이고, 수량을 표현하는 속성일경우 (Item의 재고 수량 필드(stockCount, quantity) 등) primitive type을 사용해도 상관없다
   * Null 이 필요한 경우도 없고, 따로 입력해 주지 않으면 기본값을 0으로 지정해도 문제가 없기 때문)



### 실제 Hibernate 문서에는 다음과 같이 권고하고 있다.

* https://docs.jboss.org/hibernate/orm/5.3/userguide/html_single/Hibernate_User_Guide.html#entity-pojo-identifier

> * *We recommend that you declare consistently-named identifier attributes on persistent classes and that you use a nullable (i.e., non-primitive) type.*
>
> 
>
> * Persistent 클래스에 일관되게 명명된 식별자 속성을 선언하고 null이 가능한(즉, Primitive가 아닌) 유형을 사용하는 것이 좋습니다





