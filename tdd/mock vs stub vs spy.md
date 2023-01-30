# Mock vs. Stub vs. Spy



Mock과 Stub, Spy의 차이점에 대해 알아보기 전에 Test Double에 대하여 먼저 정리해보겠습니다.



**Test Double**

테스트계의 스턴트맨. 영어에서는 Stunt Double이라고 부를 뿐이고, 한국사람들에게 더 익숙한 표현은 스턴트맨입니다. 

테스트 코드에서 대역(가짜)을 쓰는 것에 착안하여 이름을 이렇게 지었고, 영화를 찍기 위해 위험한 액션을 대신해주는 것이 스턴트맨이라면
, 테스트를 통과하기 위해 **액션을 대신해주는 것이**  **Test Double**입니다.

<img src="https://blog.kakaocdn.net/dn/bcjmpj/btrXnKfpBnr/o3cYF65M4ADaAjvyCSaVM1/img.png" width=800 height = 300>

* Test Double의 종류

<img src="https://blog.kakaocdn.net/dn/dzJC6X/btrXqC1OTVJ/lzI2gGGqHC5eAKuMfNUKc1/img.png" width= 800 height = 500>

 

## 더미 객체 (Dummy Object)

Dummy Object는 전달되지만 실제로는 사용되지 않습니다.
일반적으로 파라미터를 전달하기 위한 용도로만 사용합니다. 

## 모의 (Mock)

Mock은 메소드를  호출했을 때 사전에 정의된 명세대로의 결과를 돌려주도록 미리 프로그래밍 합니다.

> 즉 Mock은 메소드 호출을 저장하는 객체이며 dynamic Wrapper(동적 래퍼)라고도 합니다.

Mock은 메소드를 mocking (가짜) 할 수 있으며 

예상치 못한 호출이 있을 경우 예외를 던질 수 있으며, Mocking한 메소드가  몇 번 호출되었는지 확인할 수 있습니다.(.Mock의 필수기능)



mock 객체는 일반적으로 동작 확인(behavior verification)에 사용됩니다.

*  반환값이 없는 함수를 테스트할 때, 특정 객체에서 특정 함수가 호출되었는지 테스트

* 또한 stub이 충분하지 않은 대규모 테스트 suite(모임)를 테스트하는 데 사용됩니다.

Mock은 테스트에 필요한 환경을 가짜 객체로 구축하는 것을 말하며, 테스트 하려는 객체가 의존하는 객체를 **대체**하여 테스트를 진행합니다. 

## 스텁 (Stub)

스텁은 테스트에 필요한 최소한의 메서드로 실제 객체와 유사한 객체.

Stub은 테스트 중 호출된 경우에 대해 미리 만들어진 데이터(결과물)을 응답하게 합니다.

보통 테스트를 위해 만들어지며 테스트 이외에 사용되지 않습니다.

특정 스텁 객체에서 특정 메소드를 호출할 때, 특정한 값이 반환되라고 더미 데이터를 지정할 때 사용합니다.

* `stub이란, 해당 메소드(또는 필드)를 호출했을 때 반환해야 하는 값을 미리 지정하는 것`



>  Stub이라는 단어가 내포하는 바가, 전체 중 일부라는 뜻. 
>
> 모든 기능 대신 일부 기능에 집중하여 임의로 구현하여 테스트하는데 사용합니다.
>
> 일부 기능이라 함은 테스트를 하고자 하는 기능을 의미입니다.



스텁의 주요 기능은 다음과 같습니다.

- 입력에 관계없이 항상 미리 정의된 출력을 반환합니다.
- 데이터베이스 개체와 유사하게 사용할 수 있습니다.
- 스텁은 실제 객체를 생성하는 동안 발생하는 복잡성을 줄이는 데 사용됩니다. 주로 **상태 확인** 을 수행하는 데 사용됩니다 .



Stub은 테스트에 필요한 환경을 가짜 객체로 구축하는 것도 마찬가지로 하지만, 그 객체는 테스트 하려는 객체와 직접적인 상호작용을 하지 않는 테스트를 진행합니다.



따라서, Mock은 의존 객체에 대해서도 테스트를 진행하며, Stub은 의존 객체에 대해서는 테스트를 진행하지 않습니다.

## 스파이 (Spy) 

Spy는 부분적인 Mock을 제공하는 진짜 객체입니다. (부분적인 Mock 객체) 

실제 객체를 스터빙하여 실제 객체의 부분 객체 또는 부분 dummy를 생성합니다.

Spy는  필요한 일부 특정 메서드만 Stup을 할 수 있으며 실제 객체의 일반 메서드를 호출할 수 있습니다.

Spy는 주로 특정 함수만 실제 함수를 호출하게 하고 싶을 때 사용하거나 어떤 동작이 이루어졌는지 검증하는 용도로 사용됩니다.



* 스파이는 메서드로 가득 찬 거대한 클래스가 있고 특정 메서드를 조롱하려는 경우에 유용합니다. 메서드가 스텁되지 않은 경우 실제 메서드 동작을 호출합니다.



## 가짜 객체 (Fake Object)

Fake Object는 실제로 동작하는 객체를 의미합니다.
실제 프로덕션 환경에 적합하지 않지만 지름길의 역할을 합니다.
(인메모리 데이터베이스가 좋은 예시입니다.)

 



## Mock vs Spy

**mock과 spy의 차이점은 실제 메소드가 수행되는지에 따라 나뉘게 됩니다.**

* Mock은 실제 내부 구현체가 수행되지 않습니다.
* Spy는 실제 내부 구현체가 수행됩니다.



| Parameters                    | Stub                                                         | Mock                                                         |
| :---------------------------- | :----------------------------------------------------------- | :----------------------------------------------------------- |
| Data Source                   | 스텁의 데이터 소스는 하드코딩됩니다. <br />일반적으로 테스트 suite와 밀접하게 연결되어 있습니다. | Mock 데이터는 테스트에 의해 설정됩니다.                      |
| Created by                    | 스텁은 일반적으로 손으로 작성되며 일부는 도구로 생성됩니다.  | Mock은 일반적으로 Mockito, JMock 및 WireMock과 같은 타사 라이브러리를 사용하여 생성됩니다. |
| Usage                         | 스텁은 주로 간단한 테스트 suite에 사용됩니다.                | Mock은 주로 대규모 테스트 suite에 사용됩니다.                |
| Graphics User Interface (GUI) | 스텁에는 GUI가 없습니다.                                     | Mock에는 GUI가 있습니다.                                     |

다음은 Mock와 Spy의 몇 가지 차이점입니다.

| Parameters | Mock                                                         | Spy                                                          |
| :--------- | :----------------------------------------------------------- | :----------------------------------------------------------- |
| 사용법     | Mock은 완전히 Mock 또는 더미 개체를 만드는 데 사용됩니다. 주로 대규모 테스트 suite에서 사용됩니다. | 스파이는 부분 또는 절반 Mock 개체를 만드는 데 사용됩니다. 모의와 마찬가지로 스파이도 대규모 테스트 suite에서 사용됩니다. |
| 기본동작   | Mock 객체를 사용할 때 메서드의 기본 동작(스텁되지 않은 경우)은 아무것도 하지 않는 것입니다(아무것도 수행하지 않음). | 스파이 개체를 사용할 때 메서드의 기본 동작(stub 되지 않은 경우)은 실제 메서드 동작입니다. |





# Mock vs Stub - Mock과 Stub의 차이점

Test Double 중 Mock만 `행동(올바른 호출을 했는지)을 검증`합니다.

보통 다른 Test Double들은 일반적으로 `상태(최종 결과물)를 검증을 위해 사용`합니다.

* Mock은 행동 검증 (올바른 호출)

* Stub은 상태 검증 (최종 결과)



마틴 파울러의 포스팅에서는
Stub을 사용하는 테스트 방식을 **classic**이라 하고,
Mock을 사용하는 테스트 방식을 **Mockist**라고 칭합니다.



> 둘 중 어떤 방식의 테스트를 해야 할지는 context를 고려하라고 조언하였습니다. 



### 언제 Stub을 쓰고, 언제 Mock을 써야 하나?

* Stub

  - ﻿﻿테스트의 입력에 집중하는가?

  - ﻿﻿그 입력값에 따라 리턴하는 결과값을 비교하는지?

  - ﻿﻿그 입력값에 따라 exception이 발생하는지?

* Mock

  - ﻿﻿테스트의 출력/결과에 집중하는가?

  - ﻿﻿정상적으로 호출되었는지가 더 중요한지?





Mocks Aren't Stubs by Martin Fowler

https://martinfowler.com/articles/mocksArentStubs.html

> Meszaros는 Test Double이라는 용어를 테스트 목적으로 실제 객체 대신 사용되는 모든 종류의 가상 객체에 대한 일반 용어로 사용합니다. 이름은 영화에서 스턴트 더블이라는 개념에서 유래했습니다.
>
> (그의 목표 중 하나는 이미 널리 사용되는 이름을 사용하지 않는 것이었습니다.) 그런 다음 Meszaros는 5가지 특정 유형의 double을 정의했습니다.
>
> - 더미 오브젝트는 전달되지만 실제로 사용되지는 않습니다. 일반적으로 매개변수 목록을 채우는 데만 사용됩니다.
> - 가짜 개체에는 실제로 작동하는 구현이 있지만 일반적으로 프로덕션에 적합하지 않게 만드는 몇 가지 지름길을 사용합니다(메모리 데이터베이스가 좋은 예입니다.
> - 스텁은 테스트 중에 이루어진 호출에 대한 미리 준비된 답변을 제공하며 일반적으로 테스트를 위해 프로그래밍된 것 이외의 항목에는 전혀 응답하지 않습니다.
> - 스파이는 호출 방법에 따라 일부 정보도 기록하는 스텁입니다. 이것의 한 형태는 전송된 메시지 수를 기록하는 이메일 서비스일 수 있습니다.
> - Mocks는 우리가 여기서 말하는 것입니다: 받을 것으로 예상되는 호출의 지정을 형성하는 기대로 사전 프로그래밍된 개체입니다.
>
> Meszaros uses the term Test Double as the generic term for any kind of pretend object used in place of a real object for testing purposes. The name comes from the notion of a Stunt Double in movies.
>
> (One of his aims was to avoid using any name that was already widely used.) Meszaros then defined f ive particular kinds of double:
>
> - ﻿﻿Dummy objects are passed around but never actually used. Usually they are just used to fill param eter lists.
> - ﻿﻿Fake objects actually have working implementations, but usually take some shortcut which makes th em not suitable for production (an in memory database is a good example.
> - ﻿﻿Stubs provide canned answers to calls made during the test, usually not responding at all to anyt hing outside what's programmed in for the test.
> - ﻿﻿Spies are stubs that also record some information based on how they were called. One form of this might be an email service that records how many messages it was sent.
> - ﻿﻿Mocks are what we are talking about here: objects pre-programmed with expectations which form a pecification of the calls they are expected to receive.
>
> 



## 원문에서 비교하는 Stub기반 테스트와 Mock기반 테스트 비교



Martin Fowler의 글에서 인용한 예제에 따르면,

```java
public interface MailService {
  public void send (Message msg);
}
public class MailServiceStub implements MailService {
  private List<Message> messages = new ArrayList<Message>();
  public void send (Message msg) {
    messages.add(msg);
  }
  public int numberSent() {
    return messages.size();
  }
}            
```

에 대해, Stub기반 테스트 코드는 다음과 같습니다.

```java
class OrderStateTester...

  public void testOrderSendsMailIfUnfilled() {
    Order order = new Order(TALISKER, 51);
    MailServiceStub mailer = new MailServiceStub();
    order.setMailer(mailer);
    order.fill(warehouse);
    assertEquals(1, mailer.numberSent());
  }
```

반면, Mock기반 테스트 코드는 다음과 같습니다.

```java
class OrderInteractionTester...

  public void testOrderSendsMailIfUnfilled() {
    Order order = new Order(TALISKER, 51);
    Mock warehouse = mock(Warehouse.class);
    Mock mailer = mock(MailService.class);
    order.setMailer((MailService) mailer.proxy());

    mailer.expects(once()).method("send");
    warehouse.expects(once()).method("hasInventory")
      .withAnyArguments()
      .will(returnValue(false));

    order.fill((Warehouse) warehouse.proxy());
  }
}
```

위에서 인용한 Stub기반의 코드는 상태기반 테스트이고, Mock기반의 테스트는 행위기반 테스트 입니다. 

만약, Mock을 사용했지만 Stub의 코드와 같이 작성했다면, 사실 Stub기반 테스트 코드를 작성했 던 것이라 할 수 있습니다.



## JUnit5 테스트 Mock 객체 설정



**@Mock**

* Mockito 에서 가장 널리 사용되는 주석 Mockito.mock() 을 직접 호출하지 않아도 Mock 객체를 생성하고 주입할 수 있습니다..

| @Mock                                                | Mockito.mock()                                               |
| ---------------------------------------------------- | ------------------------------------------------------------ |
| @Mock <br />private CatalogRestController controller | @Test <br />void test (){  <br />CatalogRestController controller =    Mockito.mock(new CatalogRestController()); <br />} |

**@InjectMocks**

* @InjectMocks 는 객체를 만들고, @Mock 이나 @Spy 애너테이션으로 생성된 객체를 @InjectMocks 으로 생성한 객체에 주입해줍니다.

  

**MockMvcBuilders.standaloneSetup(Object ... controllers)**

* @Controller 를 하나 이상 입력하고, 프로그래밍 방식으로 Spring MVC infrastructure를 구성하여 MockMvc 인스턴스를 빌드합니다.

* MockMvcBuilder는 DispatcherServlet이 요구하는 "최소한"의 infrastructure를 만들어줍니다. 





# org.mockito.exceptions.misusing.PotentialStubbingProblem: 

```
org.mockito.exceptions.misusing.PotentialStubbingProblem: 
Strict stubbing argument mismatch. Please check:
 - this invocation of 'send' method:
    fooSendService.send(
    FooConfirmRequest(id=1, value=2));
    -> at io.github.freeism.service.foo.FooConfirmServiceTest
          .givenResponseByFooSendService(FooConfirmServiceTest.java:58)
 - has following stubbing(s) with different arguments:
    1. fooSendService.send(
    FooConfirmRequest(id=1, value=1));
    -> at io.github.freeism.service.foo.FooConfirmServiceTest
          .givenResponseByFooSendService(FooConfirmServiceTest.java:57)
 
Typically, stubbing argument mismatch indicates user mistake when writing tests.
Mockito fails early so that you can debug potential problem easily.
However, there are legit scenarios when this exception generates false negative signal:
  - stubbing the same method multiple times using 'given().will()' or 'when().then()' API
    Please use 'will().given()' or 'doReturn().when()' API for stubbing.
  - stubbed method is intentionally invoked with different arguments by code under test
    Please use default or 'silent' JUnit Rule (equivalent of Strictness.LENIENT).
For more information see javadoc for PotentialStubbingProblem class.
```

 `given().will()`혹은 `when().then()` 대신에 `will().given()`, `doReturn().when()`을 사용하라고 권고한다.

단순히 순서를 바꾼 것에 지나지 않는 것 같지만, 하나는 `Strict Mode`로 다른 것은 `Lenient Mode`로 동작한다.



`when().then()`과 `doReturn().when()`은 파라미터 구조가 약간 다릅니다.

만약 아래와 같은 오류가 발생했다면, 괄호의 위치를 잘못 썼을 가능성이 큽니다.

```
org.mockito.exceptions.misusing.UnfinishedStubbingException: 
Unfinished stubbing detected here:
-> at io.github.freeism.service.foo.FooConfirmServiceTest
      .givenResponseByFooSendService(FooConfirmServiceTest.java:58)
 
E.g. thenReturn() may be missing.
Examples of correct stubbing:
    when(mock.isOk()).thenReturn(true);
    when(mock.isOk()).thenThrow(exception);
    doThrow(exception).when(mock).someVoidMethod();
Hints:
 1. missing thenReturn()
 2. you are trying to stub a final method, which is not supported
 3: you are stubbing the behaviour of another mock inside before 'thenReturn' instruction 
    if completed
```



# Stub을 이용한 Service 계층 단위 테스트 - by jojoldu

코드를 stubbing하는데는 크게 2가지 방법이 있습니다.

- 직접 구현한 Stub 객체 사용
- 특정 stubbing 라이브러리를 사용

두번째 예제는 다음과 같이 **우리 시스템 밖의 의존성**을 사용해야할 경우입니다.

- 외부 API를 호출해서 데이터를 전달하고, 보낸 데이터를 검증해야하는 경우
- 이메일 / 슬랙 등으로 메세지를 보내고, 몇개의 메세지가 발송되었는지 검증해야하는 경우

이를테면 다음과 같은 코드입니다.

```typescript
export class OrderService {
    constructor(
        private readonly orderRepository: OrderRepository,
        private readonly billingApi: BillingApi
        ) {
    }

    compareBilling(orderId: number): void {
        const order = this.orderRepository.findById(orderId);
        const billingStatus = this.billingApi.getBillingStatus(orderId);

        if(order.equalsBilling(billingStatus)) {
            return ;
        }

        if(order.isCompleted()) {
            this.billingApi.complete(order);
        }

        if(order.isCanceled()) {
            this.billingApi.cancel(order);
        }
    }
```

> https://jojoldu.tistory.com/637

## 주의사항

이렇게 테스트 더블 (Stub/Mock) 혹은 Mock 라이브러리를 통해 처리하는 경우가 항상 옳은 것은 아닙니다.
그래서 테스트 더블을 사용하는 경우 다음의 주의사항을 꼭 염두해 두어야만 합니다.

### 무분별한 테스트 더블을 활용한 단위 테스트

간혹 Stub, Mock에 빠져 **모든 코드를 Stub, Mock으로 해결**하려는 분들이 있습니다.

특히 대표적인 사례가 다음과 같습니다.

- Service에서 하는 것이라곤 Repository의 메소드들을 호출하는게 전부인데, Repository를 전부 Stub/Mock 처리한 경우

단위 테스트에만 빠지면 안됩니다.

통합/E2E 테스트와 달리 테스트 더블을 통한 단위 테스트는 **각 Layer, Componenet 간 연동이 되어서도 잘 되는 것을 보장하지는 못한다**

stubbing을 통해서 **연동되는 모듈들의 버그 유무는 전혀 고려하지 않은** 상태로 테스트를 하다보니, 실제 연동 과정에서 많은 문제들이 발생할 수 있습니다.

사이드 이펙트가 적은 부분에 한해서 테스트 더블을 사용하는 것이 좋습니다.

가능하다면 **실제 객체**를 사용하는 것이 가장 좋고,
그게 어려울때만 테스트 더블을 사용하는 것이 좋습니다.

### 점점 깨지기 쉬운 테스트

테스트 더블 객체들은 깨지기 쉬운 테스트 케이스가 되기 쉽습니다.
이는 Mock/Stub 처리를 위해 그만큼 **테스트가 구현부를 상세하게 의존**하기 때문입니다.

- [테스트 코드에서 내부 구현 검증 피하기](https://jojoldu.tistory.com/614)

가능하다면 **테스트 더블이 필요 없는 작은 구조**로 구현부의 설계를 개선하는 것이 좋습니다.
그 편이 테스트 더블을 사용 하려고 노력하는 것보다 훨씬 낫습니다.







### 참조

* https://www.javatpoint.com/mock-vs-stub-vs-spy

* https://github.com/HomoEfficio/dev-tips/blob/master/Mockito%20Stub%20%EC%9E%91%EC%84%B1%20%EC%8B%9C%20%EC%A3%BC%EC%9D%98%20%EC%82%AC%ED%95%AD.md
* https://luran.me/343
* https://www.freeism.co.kr/wp/archives/2243#fn-2243-6