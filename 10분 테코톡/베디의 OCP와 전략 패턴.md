# 10분 테코톡 - 베디의 OCP와 전략 패턴

목차

1. if-else의 문제점
2. OCP
    1. OCP란?
    2. 적용
    3. 장점
3. 전략패턴
4. 연습문제 추천

# 1. If-else의 문제점

- 변경, 확장이 될수록 코드가 복잡해진다
- 코드를 수정하거나 수정할 위치를 찾는데 점점 오래걸린다
- 실수로 추가하지 않고 누락하는 부분이 생길 가능성이 있다.

* if-else 블록이 커지므로 코드는 복잡하면서 추가 수정이 힘들어진다.

> 즉, 유지보수가 점점 어려워진다.

# 2. OCP(Open Close Principle) - 개방 폐쇄의 원칙

시간이 지나도 유지보수와 확장이 쉬운 시스템을 만들고자 로버트 마틴이 명령한 객체지향 설계 5원칙 SOLID중 하나

소프트웨어 구성요소(컴포넌트, 클래스, 모듈, 함수)는 `확장에 대해서는 개방 되어야 하지만 변경에 대해서는 폐쇄` 되어야 한다.

즉, 기존의 코드를 변경하지 않으면서 기능은 추가할 수 있도록 설계가 되어야 한다는 의미.

## OCP 적용방법 2가지

1. 상속 (is -a)
2. 컴포지션 (has -a)

### 1. 상속

* 상위 클래스를 상속받아 하위클래스를 이용한다.
* 하지만 상위 클래스와 하위 클래스의 강한 응집력으로 상위 클래스가 바뀌면 하위 클래스에 끼치는 영향이 매우 큰 단점이 있다.
* 깨지기 쉬운 상위 클래스의 문제를 하위클래스가 그대로 다 받을 수 있다.

### 2. 컴포지션

* 변경 될 것과 변하지 않을것을 엄격히 구분한다.
* 이 두 모듈이 만나는 지점에 인터페이스를 정의
* 구현에 의존하기보다 정의한 인터페이스에 의존하도록 코드를 작성

### 예시

* Java에서 List와 ArrayList의 관계가 있다.
* 타입 선언은 List 이지만 생성할때는 구현체인 ArrayList로 생성한다
    * List<T> list = new ArrayList<>();

1. 변경될 부분과 변하지 않을 부분을 엄격히 구분한다.

* ![img](https://blog.kakaocdn.net/dn/brCRL7/btqALQCoCST/t7zaYX7BqOO8RK7uXaIFE1/img.png)

* 사진에서 B 부분에서 변경이 생기고 있다.
* 변경될 부분을 인터페이스로 추출한다

2. 모듈이 만나는 지점에 인터페이스를 정의

* ![img](https://blog.kakaocdn.net/dn/bLQAf9/btqAKutiV4I/6Ectym7IIOKn5R10KQWKTk/img.png)

* 주의 : 인터페이스에 의존해야지, 다른 코드에 의존하면 안된다.
* 기능 변경 위해서 코드를 수정해야하기 때문
* 인터페이스는 변하는것과 변하지 않는 모듈의 교차점으로 서로를 보호하는 역할을 한다.


3. 인터페이스에 의존하도록 코드를 작성

* ![img](https://blog.kakaocdn.net/dn/bf0Gaq/btqAKvMuJAR/Pva7nNjMkXYI6ENWxDgXZ1/img.png)

* `LottoNumbersAutoGenerator`가 `ShuffleStrategy` 인터페이스에 의존하도록 하고
* 생성자 주입을 통해 구현 클래스의 인스턴스를 외부에서 주입하여 이용합니다.
    * 이런 방식을 **DI(Dependency Injection)** 라고 합니다.

* 다른 기능을 사용하고 싶으면 SuhhleStrategy 인터페이스를 구현한 다른 구현체를 주입해서 사용하면 됩니다.
    * 기능이 바뀌어도 내부의 코드는 변경되지 않고 구현체만 바뀌게 됩니다.

# 3. 전략패턴

* 소프트웨어 디자인 패턴 중 하나 (Strategy pattern)
* 전략 : 어떤 목적을 달성하기 위해 일을 수행하는 방식
* 비즈니스 규칙, 문제를 해결하는 알고리즘 등.

* 컴포지션 적용 자체도 전략 패턴이라고 볼 수 있다.
* 전략을 쉽게 변경할 수 있도록 해주는 디자인 패턴으로, 행위를 클래스를 캡슐화해 동적으로 행위를 자유롭게 바꿀 수 있게 해주는 패턴
* 새로운 기능의 추가가 기존의 코드에 영향을 미치지 못하게 하므로 OCP를 만족시킨다

```
위 예시로 든다면, 로또 번호를 만들어주는 generate() 메서드에 현재는 ShuffleRandomStrategy 로 랜덤하게 구현했지만,
Shuffle 조작 Stragtegy class라는 코드를 조작해서 생성하는 기능을 만든 구현체를 주입하더라도  	
generate() 메서드의 코드 자체에 영향을 미치지 않고 기능 자체는 동작하게 된다.  
```

## Context

* 전략 패턴을 이용하느 ㄴ역할을 수행
* 필요에 따라 동적으로 구체적인 전략을 바꿀 수 있도록 한다 - DI

## Strategy

* 인터페이스나 추상 클래스로 외부에서 동일한 방식으로 알고리즘을 호출하는 방법을 명시한다

## ConcreateStrategy

* 전략 패턴에서 명시한 알고리즘을 실제로 구현한 클래스

위 예시에서 LottoNumbersAutoGenerator가 Context,

ShuffleStrategy 인터페이스가 Strategy 인터페이스,

ShuffleRandomStrategy 같은 구현체가 ConcreateStrategy에 해당한다.

전략패턴을 한줄로 요약하자면 ,

`기존의 코드 변경 없이 행위를 자유롭게 바꿀 수 있게 해주는 OCP의 디자인 패턴`

# 템플릿 메소드 패턴 vs 전략 패턴

* 컴포지션 방법을 사용하여 적용한 것이 `전략 패턴`
* 상속을 이용하여 적용한 것이  `템플릿 메소드 패턴`

* https://happy-coding-day.tistory.com/entry/Template-Pattern%ED%85%9C%ED%94%8C%EB%A6%BF-%ED%8C%A8%ED%84%B4-VS-Strategy-Pattern%EC%A0%84%EB%9E%B5-%ED%8C%A8%ED%84%B4

# 연습문제 추천

- 추상화,다형성
- 인터페이스
- Map
- Enum
- 람다
- 전략패턴

을 이용하여 계산기 문제를 풀어보자

```java
public class Calculator {

    public int calculate(final String operator, final int operand1, final int operand2) {
        if ("+".equals(operator)) {
            return operand1 + operand2;
        } else if ("-".equals(operator)) {
            return operand1 - operand2;
        } else if ("*".equals(operator)) {
            return operand1 * operand2;
        } else if ("/".equals(operator)) {
            return operand1 / operand2;
        }
    }
}
```

1. 인터페이스로 추출 후 구현 클래스 만들기
2. 익명 클래스 + enum으로 구현
3. 람다로 구현

### 구현 내용

* https://dublin-java.tistory.com/38
* https://rutgo-letsgo.tistory.com/167


* 하루에 한화씩 보면서 공부하고 정리하려고 합니다.

* https://www.youtube.com/watch?v=90ZDvHl8ROE&list=PLgXGHBqgT2TvpJ_p9L_yZKPifgdBOzdVH&index=232