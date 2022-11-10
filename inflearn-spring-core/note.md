인프런 강의
저자: 김영한  


전체 목차
1. 예제 만들기
2. 쓰레드 로컬 - ThreadLocal
3. 템플릿 메서드 패턴과 콜백 패턴
4. 프록시 패턴과 데코레이터 패턴
5. 동적 프록시 기술
6. 스프링이 지원하는 프록시
7. 빈 후처리기
8. @Aspect AOP
9. 스프링 AOP 개념
10. 스프링 AOP 구현
11. 스프링 AOP - 포인트컷
12. 스프링 AOP - 실전 예제
13. 스프링 AOP - 실무 주의사항
14. 다음으로

사용 예제 
1. 예제 만들기 - advanced
2. 쓰레드 로컬 - ThreadLocal - advanced
3. 템플릿 메서드 패턴과 콜백 패턴 - advanced
4. 프록시 패턴과 데코레이터 패턴 - proxy-start proxy
5. 동적 프록시 기술 - proxy
6. 스프링이 지원하는 프록시 - proxy
7. 빈 후처리기 - proxy
8. @Aspect AOP - proxy
9. 스프링 AOP 개념 - 없음
10. 스프링 AOP 구현 - aop
11. 스프링 AOP - 포인트컷 - aop
12. 스프링 AOP - 실전 예제 - aop13. 스프링 AOP - 실무 주의사항 - aop
13. 다음으로           


# 1. 예제 만들기
예제 프로젝트 만들기 - V0  
학습을 위한 간단한 예제 프로젝트를 만들어보자.  
상품을 주문하는 프로세스로 가정하고, 일반적인 웹 애플리케이션에서 Controller Service
Repository로 이어지는 흐름을 최대한 단순하게 만든다.

## 로그 추적기 - 요구사항 분석 

> 여러분이 새로운 회사에 입사했는데, 수 년간 운영중인 거대한 프로젝트에 투입되었다. 
 전체 소스 코드는 수 십만 라인이고, 클래스 수도 수 백개 이상이다.  
여러분에게 처음 맡겨진 요구사항은 로그 추적기를 만드는 것이다.  
애플리케이션이 커지면서 점점 모니터링과 운영이 중요해지는 단계이다.  
특히 최근 자주 병목이 발생하고 있다.   
어떤 부분에서 병목이 발생하는지, 그리고 어떤 부분에서 예외가 발생하는지를 로그를 통해 확인하는 것이 점점 중요해지고 있다.  
기존에는 개발자가 문제가 발생한 다음에 관련 부분을 어렵게 찾아서 로그를 하나하나 직접 만들어서 남겼다.  
로그를 미리 남겨둔다면 이런 부분을 손쉽게 찾을 수 있을 것이다.   
이 부분을 개선하고 자동화 하는 것이 여러분의 미션이다.

## 요구사항

* 모든 PUBLIC 메서드의 호출과 응답 정보를 로그로 출력
* 애플리케이션의 흐름을 변경하면 안됨
  * 로그를 남긴다고 해서 비즈니스 로직의 동작에 영향을 주면 안됨
* 메서드 호출에 걸린 시간
* 정상 흐름과 예외 흐름 구분
    * 예외 발생시 예외 정보가 남아야 함
* 메서드 호출의 깊이 표현
* HTTP 요청을 구분
  * HTTP 요청 단위로 특정 ID를 남겨서 어떤 HTTP 요청에서 시작된 것인지 명확하게 구분이 가능해야 함
  * 트랜잭션 ID (DB 트랜잭션X), 여기서는 하나의 HTTP 요청이 시작해서 끝날 때 까지를 하나의 트랜잭션이라 함

* ### 예시
```
정상 요청
[796bccd9] OrderController.request()
[796bccd9] |-->OrderService.orderItem()
[796bccd9] | |-->OrderRepository.save()
[796bccd9] | |<--OrderRepository.save() time=1004ms
[796bccd9] |<--OrderService.orderItem() time=1014ms
[796bccd9] OrderController.request() time=1016ms
예외 발생
[b7119f27] OrderController.request()
[b7119f27] |-->OrderService.orderItem()
[b7119f27] | |-->OrderRepository.save()
[b7119f27] | |<X-OrderRepository.save() time=0ms ex=java.lang.IllegalStateException: 예외 발생!
[b7119f27] |<X-OrderService.orderItem() time=10ms ex=java.lang.IllegalStateException: 예외 발생!
[b7119f27] OrderController.request() time=11ms ex=java.lang.IllegalStateException: 예외 발생!
```

## 로그추적기 V1 - 프로토타입 개발 

* 로그 추적기를 위한 기반 데이터를 가지고 있는 `TraceId`, `TraceStaus` 클래스

* TraceId 클래스
  * 로그 추적기는 트랜잭션ID와 깊이를 표현하는 방법이 필요하다.
  * 여기서는 트랜잭션ID와 깊이를 표현하는 level을 묶어서 TraceId 라는 개념을 만들었다

* TraceStatus 클래스 
  * 로그를 시작할 때의 상태 정보를 가지고 있다. 이 상태 정보는 로그를 종료할 때 사용된다.
    * traceId : 내부에 트랜잭션ID와 level을 가지고 있다.

    * startTimeMs : 로그 시작시간이다. 로그 종료시 이 시작 시간을 기준으로 시작~종료까지 전체 수행
    시간을 구할 수 있다. 
    * message : 시작시 사용한 메시지이다. 이후 로그 종료시에도 이 메시지를 사용해서 출력한다







동기화문제 - 쓰레드로컬



필드 동기화 - 동시성 문제
잘 만든 로그 추적기를 실제 서비스에 배포했다 가정해보자.
테스트 할 때는 문제가 없는 것 처럼 보인다. 사실 직전에 만든 FieldLogTrace 는 심각한 동시성 문제를
가지고 있다.





FieldLogTrace 는 싱글톤으로 등록된 스프링 빈이다. 

이 객체의 인스턴스가 애플리케이션에 딱 1
존재한다는 뜻이다. 

이렇게 하나만 있는 인스턴스의 FieldLogTrace.traceIdHolder 필드를 여러
쓰레드가 동시에 접근하기 때문에 문제가 발생한다.
실무에서 한번 나타나면 개발자를 가장 괴롭히는 문제도 바로 이러한 동시성 문제이다. 





> 싱글톤 빈을 여러 쓰레드가 동시에 접근하기 때문에 발생한 문제 



동시성 문제는 지역 변수에서는 발생하지 않는다. 지역 변수는 쓰레드마다 각각 다른 메모리 영역이 할당된다.
> 동시성 문제가 발생하는 곳은 같은 인스턴스의 필드(주로 싱글톤에서 자주 발생), 또는 static 같은 공용
> 필드에 접근할 때 발생한다.
> 동시성 문제는 값을 읽기만 하면 발생하지 않는다. 어디선가 값을 변경하기 때문에 발생한다

싱글톤 객체의 필드를 사용하면서 동시성 문제를 해결하려면 -> 쓰레드 로컬



## ThreadLocal - 소개
쓰레드 로컬은 해당 쓰레드만 접근할 수 있는 특별한 저장소

쓰레드 로컬을 사용하면 각 쓰레드마다 별도의 내부 저장소를 제공

자바는 언어차원에서 쓰레드 로컬을 지원하기 위한 `java.lang.ThreadLocal` 제공



ThreadLocal 사용법
값 저장: ThreadLocal.set(xxx)
값 조회: ThreadLocal.get()
값 제거: ThreadLocal.remove()

주의

> `해당 쓰레드가 쓰레드 로컬을 모두 사용하고 나면 ThreadLocal.remove() 를 호출해서 쓰레드 로컬에
> 저장된 값을 제거해주어야 한다. ` -> 메모리 누수 발생 가능 



 쓰레드 로컬을 모두 사용하고 나면 꼭 ThreadLocal.remove() 를 호출해서 쓰레드 로컬에 저장된
값을 제거해주어야 한다



쓰레드 로컬 - 주의사항
쓰레드 로컬의 값을 사용 후 제거하지 않고 그냥 두면 WAS(톰캣)처럼 쓰레드 풀을 사용하는 경우에 심각한
문제가 발생할 수 있다.



WAS는 쓰레드풀에서 미리 쓰레드를 생성해놓고, 요청이 요면 쓰레드를 넘겨주는데,

A라는 사용자의 요청이 A쓰레드를 할당받고 쓰레드로컬을 통해 사용자 A의 요청을 처리하고 데이터를 지우지 않았다.

(A쓰레드 전용 저장소에 데이터를 보관.)

그리고 A쓰레드를 반납했다.

B라는 사용자가 요청을 보냈는데 A쓰레드가 다시 할당되었다(물론 다른 쓰레드가 할당될 수 있다. ) 

A쓰레드에서 지워지지않고 남아있떤 사용자 A의 데이터가 조회되는 심각한 문제가 발생하게 된다.

그러므로 요청이 끝나서 쓰레드를 반납하기 전에 꼭 쓰레드 로컬의 값을 ThreadLocal.remove()로 제거하자. 





# 템플릿 메서드 패턴



핵심 기능 vs 부가 기능
핵심 기능은 해당 객체가 제공하는 고유의 기능이다. 

부가 기능은 핵심 기능을 보조하기 위해 제공되는 기능이다. 예를 들어서 로그 추적 로직, 트랜잭션 기능이
있다. 이러한 부가 기능은 단독으로 사용되지는 않고, 핵심 기능과 함께 사용된다

그러니까 핵심 기능을 보조하기 위해 존재한다

부가 기능과 관련된 코드가 중복이니 중복을 별도의 메서드로 뽑아내면 된다.

변하는 것과 변하지 않는 것을 분리
좋은 설계는 변하는 것과 변하지 않는 것을 분리하는 것이다

여기서 핵심 기능 부분은 변하고,  변하지 않는 부분은 부가기능이다.
이 둘을 분리해서 모듈화해야 한다

템플릿 메서드 패턴(Template Method Pattern)은 이런 문제를 해결하는 디자인 패턴이다



```java
@Slf4j
public class TemplateMethodTest {

    @Test
    void templateMethodV0() {
        logic1();
        logic2();
    }

    private void logic1() {
        long startTime = System.currentTimeMillis();

        //비즈니스 로직 실행
        log.info("비즈니스 로직1 실행");

        //비즈니스 로직 종료
        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("resultTime={}", resultTime);
    }

    private void logic2() {
        long startTime = System.currentTimeMillis();

        //비즈니스 로직 실행

        log.info("비즈니스 로직2 실행");
        //비즈니스 로직 종료

        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("resultTime={}", resultTime);
    }
}
```

* 변하는 부분: 비즈니스 로직
* 변하지 않는 부분: 시간 측정

![image-20221104150535581](/Users/ysk/study/study_repo/inflearn-spring-core/spring-core/images//image-20221104150535581.png)

![image-20221104152231274](/Users/ysk/study/study_repo/inflearn-spring-core/spring-core/images//image-20221104152231274.png)

template1.execute() 를 호출하면 템플릿 로직인 AbstractTemplate.execute() 를 실행한다. 여기서
중간에 call() 메서드를 호출하는데, 이 부분이 오버라이딩 되어있다.

 따라서 현재 인스턴스인 SubClassLogic1 인스턴스의 SubClassLogic1.call() 메서드가 호출된다.
템플릿 메서드 패턴은 이렇게 다형성을 사용해서 변하는 부분과 변하지 않는 부분을 분리하는 방법이다



템플릿 메서드 패턴 - 예제3
익명 내부 클래스 사용하기
템플릿 메서드 패턴은 SubClassLogic1 , SubClassLogic2 처럼 클래스를 계속 만들어야 하는 단점이
있다. 익명 내부 클래스를 사용하면 이런 단점을 보완할 수 있다



자바가 임의로 만들어주는 익명 내부 클래스 이름은 실행시키는 메서드가 속한 클래스 이름이다.



템플릿 메서드 패턴 - 정의
GOF 디자인 패턴에서는 템플릿 메서드 패턴을 다음과 같이 정의했다.
> 템플릿 메서드 디자인 패턴의 목적은 다음과 같습니다.
> "작업에서 알고리즘의 골격을 정의하고 일부 단계를 하위 클래스로 연기합니다. 템플릿 메서드를 사용하면
> 하위 클래스가 알고리즘의 구조를 변경하지 않고도 알고리즘의 특정 단계를 재정의할 수 있습니다." [GOF]$

![image-20221104153504506](/Users/ysk/study/study_repo/inflearn-spring-core/spring-core/images//image-20221104153504506.png)

부모 클래스에 알고리즘의 골격인 템플릿을 정의하고, 일부 변경되는 로직은 자식 클래스에 정의하는
것이다. 이렇게 하면 자식 클래스가 알고리즘의 전체 구조를 변경하지 않고, 특정 부분만 재정의할 수 있다.
결국 상속과 오버라이딩을 통한 다형성으로 문제를 해결하는 것이다.
하지만
템플릿 메서드 패턴은 상속을 사용한다. 따라서 상속에서 오는 단점들을 그대로 안고간다. 특히 자식
클래스가 부모 클래스와 컴파일 시점에 강하게 결합되는 문제가 있다. 이것은 의존관계에 대한 문제이다.
자식 클래스 입장에서는 부모 클래스의 기능을 전혀 사용하지 않는다



상속을 받는 다는 것은 특정 부모 클래스를 의존하고 있다는 것이다. 자식 클래스의 extends 다음에 바로
부모 클래스가 코드상에 지정되어 있다. 따라서 부모 클래스의 기능을 사용하든 사용하지 않든 간에 부모
클래스를 강하게 의존하게 된다. 여기서 강하게 의존한다는 뜻은 자식 클래스의 코드에 부모 클래스의
코드가 명확하게 적혀 있다는 뜻이다. UML에서 상속을 받으면 삼각형 화살표가 자식 -> 부모 를 향하고
있는 것은 이런 의존관계를 반영하는 것이다.

자식 클래스 입장에서는 부모 클래스의 기능을 전혀 사용하지 않는데, 부모 클래스를 알아야한다. 이것은
좋은 설계가 아니다. 그리고 이런 잘못된 의존관계 때문에 부모 클래스를 수정하면, 자식 클래스에도 영향을
줄 수 있다.

추가로 템플릿 메서드 패턴은 상속 구조를 사용하기 때문에, 별도의 클래스나 익명 내부 클래스를 만들어야
하는 부분도 복잡하다.

지금까지 설명한 이런 부분들을 더 깔끔하게 개선하려면 어떻게 해야할까?

템플릿 메서드 패턴과 비슷한 역할을 하면서 상속의 단점을 제거할 수 있는 

디자인 패턴이 바로 전략 패턴
(Strategy Pattern)이다



# 전략 패턴 - 시작
전략 패턴의 이해를 돕기 위해 템플릿 메서드 패턴에서 만들었던 동일한 예제를 사용해보자



전략 패턴은 변하지 않는 부분을 Context 라는 곳에 두고, 변하는 부분을 Strategy 라는 인터페이스를
만들고 해당 인터페이스를 구현하도록 해서 문제를 해결한다. 상속이 아니라 위임으로 문제를 해결하는
것이다.
전략 패턴에서 Context 는 변하지 않는 템플릿 역할을 하고, Strategy 는 변하는 알고리즘 역할을 한다.



GOF 디자인 패턴에서 정의한 전략 패턴의 의도는 다음과 같다.
> 알고리즘 제품군을 정의하고 각각을 캡슐화하여 상호 교환 가능하게 만들자. 전략을 사용하면 알고리즘을
> 사용하는 클라이언트와 독립적으로 알고리즘을 변경할 수 있다.



![image-20221104154508407](/Users/ysk/study/study_repo/inflearn-spring-core/spring-core/images//image-20221104154508407.png)



```java
public interface Strategy {
	void call();
}

@Slf4j
public class StrategyLogic1 implements Strategy {
	@Override
	public void call() {
		log.info("비즈니스 로직1 실행");
	}
}

/**
* 필드에 전략을 보관하는 방식
*/
@Slf4j
public class ContextV1 {
	private Strategy strategy;
	
  public ContextV1(Strategy strategy) {
		this.strategy = strategy;
	}
	
  public void execute() {
		long startTime = System.currentTimeMillis();
		//비즈니스 로직 실행
		strategy.call(); //위임
		//비즈니스 로직 종료
		long endTime = System.currentTimeMillis();
		long resultTime = endTime - startTime;
		
    log.info("resultTime={}", resultTime);
	}
}
```

ContextV1 은 변하지 않는 로직을 가지고 있는 템플릿 역할을 하는 코드이다. 전략 패턴에서는 이것을
컨텍스트(문맥)이라 한다.

쉽게 이야기해서 컨텍스트(문맥)는 크게 변하지 않지만, 그 문맥 속에서 strategy 를 통해 일부 전략이
변경된다 생각하면 된다.

Context 는 내부에 Strategy strategy 필드를 가지고 있다. 이 필드에 변하는 부분인 Strategy 의
구현체를 주입하면 된다.

전략 패턴의 핵심은 Context 는 Strategy 인터페이스에만 의존한다는 점이다. 덕분에 Strategy 의
구현체를 변경하거나 새로 만들어도 Context 코드에는 영향을 주지 않는다.

어디서 많이 본 코드 같지 않은가? 

그렇다. 바로 스프링에서 의존관계 주입에서 사용하는 방식이 바로 전략
패턴이다.



![image-20221104155312568](/Users/ysk/study/study_repo/inflearn-spring-core/spring-core/images//image-20221104155312568.png)

익명 내부 클래스를 자바8부터 제공하는 람다로 변경할 수 있다. 람다로 변경하려면 인터페이스에 메서드가
1개만 있으면 되는데, 여기에서 제공하는 Strategy 인터페이스는 메서드가 1개만 있으므로 람다로
사용할 수 있다.
람다에 대한 부분은 자바 기본 문법이므로 자바 문법 관련 내용을 찾아보자.

정리
지금까지 일반적으로 이야기하는 전략 패턴에 대해서 알아보았다. 

변하지 않는 부분을 Context 에 두고 변하는 부분을 Strategy 를 구현해서 만든다. 

그리고 Context 의 내부 필드에 Strategy 를 주입해서 사용했다





### 선 조립, 후 실행

여기서 이야기하고 싶은 부분은 Context 의 내부 필드에 Strategy 를 두고 사용하는 부분이다.

이 방식은 Context 와 Strategy 를 실행 전에 원하는 모양으로 조립해두고,

 그 다음에 Context 를 실행하는 선 조립, 후 실행 방식에서 매우 유용하다.
Context 와 Strategy 를 한번 조립하고 나면 이후로는 Context 를 실행하기만 하면 된다. 

우리가 스프링으로 애플리케이션을 개발할 때 애플리케이션 로딩 시점에 의존관계 주입을 통해 필요한 의존관계를
모두 맺어두고 난 다음에 실제 요청을 처리하는 것 과 같은 원리이다.

이 방식의 단점은 Context 와 Strategy 를 조립한 이후에는 전략을 변경하기가 번거롭다는 점이다. 

물론 Context 에 setter 를 제공해서 Strategy 를 넘겨 받아 변경하면 되지만, Context 를 싱글톤으로
사용할 때는 동시성 이슈 등 고려할 점이 많다. 

그래서 전략을 실시간으로 변경해야 하면 차라리 이전에 

개발한 테스트 코드 처럼 Context 를 하나더 생성하고

 그곳에 다른 Strategy 를 주입하는 것이 더 나은선택일 수 있다.



## 이렇게 먼저 조립하고 사용하는 방식보다 더 유연하게 전략 패턴을 사용하는 방법?

이번에는 전략 패턴을 조금 다르게 사용해보자.

 이전에는 Context 의 필드에 Strategy 를 주입해서사용했다. 

이번에는 전략을 실행할 때 직접 파라미터로 전달해서 사용해보자



```java
@Slf4j
public class ContextV2 { - 변하지 않는 템플릿임 

    
    public void execute(Strategy strategy) {
        long startTime = System.currentTimeMillis();
        //비즈니스 로직 실행
    
        strategy.call(); //위임
    
        //비즈니스 로직 종료
        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("resultTime={}", resultTime);
    
    }
}

```

* ContextV2 는 전략을 필드로 가지지 않는다. 대신에 전략을 execute(..) 가 호출될 때 마다 항상
  파라미터로 전달 받는다 



Context 와 Strategy 를 '선 조립 후 실행'하는 방식이 아니라 Context 를 실행할 때 마다 전략을 인수로 전달한다.

클라이언트는 Context 를 실행하는 시점에 원하는 Strategy 를 전달할 수 있다. 

따라서 이전 방식과 비교해서 원하는 전략을 더욱 유연하게 변경할 수 있다.
테스트 코드를 보면 하나의 Context 만 생성한다. 

그리고 하나의 Context 에 실행 시점에 여러 전략을 인수로 전달해서 유연하게 실행하는 것을 확인할 수 있다.

![image-20221104182656526](/Users/ysk/study/study_repo/inflearn-spring-core/spring-core/images//image-20221104182656526.png)



## 템플릿
지금 우리가 해결하고 싶은 문제는 변하는 부분과 변하지 않는 부분을 분리하는 것이다.
변하지 않는 부분을 템플릿이라고 하고, 그 템플릿 안에서 변하는 부분에 약간 다른 코드 조각을 넘겨서
실행하는 것이 목적이다.
ContextV1 , ContextV2 두 가지 방식 다 문제를 해결할 수 있지만, 어떤 방식이 조금 더 나아 보이는가?

지금 우리가 원하는 것은 애플리케이션 의존 관계를 설정하는 것 처럼 선 조립, 후 실행이 아니다. 

단순히 코드를 실행할 때 변하지 않는 템플릿이 있고, 그 템플릿 안에서 원하는 부분만 살짝 다른 코드를 실행하고
싶을 뿐이다.

따라서 우리가 고민하는 문제는 실행 시점에 유연하게 실행 코드 조각을 전달하는 ContextV2 가 더
적합하다





# 템플릿 콜백 패턴

다른 코드의 인수로서 넘겨주는 실행 가능한 코드를 콜백(callback)이라 한다



### 콜백 정의

> 프로그래밍에서 콜백(callback) 또는 콜애프터 함수(call-after function)는 다른 코드의 인수로서
> 넘겨주는 실행 가능한 코드를 말한다. 
>
> 콜백을 넘겨받는 코드는 이 콜백을 필요에 따라 즉시 실행할 수도 있고, 아니면 나중에 실행할 수도 있다. (위키백과 참고)



쉽게 이야기해서 callback 은 코드가 호출( call )은 되는데 코드를 넘겨준 곳의 뒤( back )에서 실행된다는 뜻이다.

* ContextV2 예제에서 콜백은 Strategy 이다.
* 여기에서는 클라이언트에서 직접 Strategy 를 실행하는 것이 아니라, 클라이언트가
  ContextV2.execute(..) 를 실행할 때 Strategy 를 넘겨주고, ContextV2 뒤에서 Strategy 가 실행된다



### 자바 언어에서 콜백
자바 언어에서 실행 가능한 코드를 인수로 넘기려면 객체가 필요하다. 자바8부터는 람다를 사용할 수 있다.
자바 8 이전에는 보통 하나의 메소드를 가진 인터페이스를 구현하고, 주로 익명 내부 클래스를 사용했다.
최근에는 주로 람다를 사용한다.



#### 템플릿 콜백 패턴
스프링에서는 ContextV2 와 같은 방식의 전략 패턴을 템플릿 콜백 패턴이라 한다. 

전략 패턴에서 Context 가 템플릿 역할을 하고, Strategy 부분이 콜백으로 넘어온다 생각하면 된다.
`참고로 템플릿 콜백 패턴은 GOF 패턴은 아니고, 스프링 내부에서 이런 방식을 자주 사용하기 때문에,
스프링 안에서만 이렇게 부른다.` 

전략 패턴에서 템플릿과 콜백 부분이 강조된 패턴이라 생각하면 된다.
스프링에서는 JdbcTemplate , RestTemplate , TransactionTemplate , RedisTemplate 처럼 다양한
템플릿 콜백 패턴이 사용된다. 

스프링에서 이름에 XxxTemplate 가 있다면 템플릿 콜백 패턴으로 만들어져 있다 생각하면 된다.



![image-20221105140055799](/Users/ysk/study/study_repo/inflearn-spring-core/spring-core/images//image-20221105140055799.png)



별도의 클래스를 만들어서 전달해도 되지만, 콜백을 사용할 경우 익명 내부 클래스나 람다를 사용하는 것이
편리하다.
물론 여러곳에서 함께 사용되는 경우 재사용을 위해 콜백을 별도의 클래스로 만들어도 된다.



### 정리
지금까지 우리는 변하는 코드와 변하지 않는 코드를 분리하고, 더 적은 코드로 로그 추적기를 적용하기 위해
고군분투 했다.
템플릿 메서드 패턴, 전략 패턴, 그리고 템플릿 콜백 패턴까지 진행하면서 변하는 코드와 변하지 않는 코드를
분리했다. 그리고 최종적으로 템플릿 콜백 패턴을 적용하고 콜백으로 람다를 사용해서 코드 사용도 최소화
할 수 있었다.

### 한계
그런데 지금까지 설명한 방식의 한계는 아무리 최적화를 해도 결국 로그 추적기를 적용하기 위해서 원본코드를 수정해야 한다는 점이다. 

클래스가 수백개이면 수백개를 더 힘들게 수정하는가 조금 덜 힘들게
수정하는가의 차이가 있을 뿐, 본질적으로 코드를 다 수정해야 하는 것은 마찬가지이다.

개발자의 게으름에 대한 욕심은 끝이 없다. 수 많은 개발자가 이 문제에 대해서 집요하게 고민해왔고,
여러가지 방향으로 해결책을 만들어왔다. 

지금부터 원본 코드를 손대지 않고 로그 추적기를 적용할 수 있는
방법을 알아보자. `그러기 위해서 프록시 개념을 먼저 이해해야 한다.`

> 참고
> 지금까지 설명한 방식은 실제 스프링 안에서 많이 사용되는 방식이다. XxxTemplate 를 만나면 이번에
> 학습한 내용을 떠올려보면 어떻게 돌아가는지 쉽게 이해할 수 있을 것이다.



* @Import(AppV1Config.class)
  * 클래스를 스프링 빈으로 등록한다. 
  * 여기서는 AppV1Config.class 를스프링 빈으로 등록한다. 일반적으로 @Configuration 같은 설정 파일을 등록할 때 사용하지만, 스프링 빈을 등록할 때도 사용할 수 있다

* @SpringBootApplication(scanBasePackages = "hello.proxy.app") 
  *  @ComponentScan 의기능과 같다. 컴포넌트 스캔을 시작할 위치를 지정한다. 이 값을 설정하면 해당 패키지와 그 하위 패키지를
    컴포넌트 스캔한다. 
  * 이 값을 사용하지 않으면 ProxyApplication 이 있는 패키지와 그 하위 패키지를
    스캔한다. 





# 프록시, 프록시 패턴, 데코레이터 패턴 - 소개
프록시에 대해서 알아보자

![image-20221105152421706](/Users/ysk/study/study_repo/inflearn-spring-core/images//image-20221105152421706.png)

클라이언트와 서버
클라이언트( Client )와 서버( Server )라고 하면 개발자들은 보통 서버 컴퓨터를 생각한다

사실 클라이언트와 서버의 개념은 상당히 넓게 사용된다. 

클라이언트는 의뢰인이라는 뜻이고, 서버는 '서비스나 상품을 제공하는 사람이나 물건'을 뜻한다. 

따라서 클라이언트와 서버의 기본 개념을 정의하면
클라이언트는 서버에 필요한 것을 요청하고, 서버는 클라이언트의 요청을 처리하는 것이다.

이 개념을 우리가 익숙한 컴퓨터 네트워크에 도입하면 클라이언트는 웹 브라우저가 되고, 요청을 처리하는
서버는 웹 서버가 된다.

이 개념을 객체에 도입하면, 요청하는 객체는 클라이언트가 되고, 요청을 처리하는 객체는 서버가 된다.



### 직접 호출과 간접 호출

![image-20221105152505243](/Users/ysk/study/study_repo/inflearn-spring-core/images//image-20221105152505243.png)

그런데 클라이언트가 요청한 결과를 서버에 직접 요청하는 것이 아니라 어떤 대리자를 통해서 대신
간접적으로 서버에 요청할 수 있다. 예를 들어서 내가 직접 마트에서 장을 볼 수도 있지만, 누군가에게 대신
장을 봐달라고 부탁할 수도 있다.
여기서 대신 장을 보는` 대리자를 영어로 프록시(Proxy)`라 한다



예시
재미있는 점은 직접 호출과 다르게 간접 호출을 하면 대리자가 중간에서 여러가지 일을 할 수 있다는
점이다.

* 엄마에게 라면을 사달라고 부탁 했는데, 엄마는 그 라면은 이미 집에 있다고 할 수도 있다. 그러면 기대한 것
  보다 더 빨리 라면을 먹을 수 있다. (접근 제어, 캐싱)
* 아버지께 자동차 주유를 부탁했는데, 아버지가 주유 뿐만 아니라 세차까지 하고 왔다. 클라이언트가 기대한
  것 외에 세차라는 부가 기능까지 얻게 되었다. (부가 기능 추가)
* 그리고 대리자가 또 다른 대리자를 부를 수도 있다. 
  * 예를 들어서 내가 동생에게 라면을 사달라고 했는데, 동생은 또 다른 누군가에게 라면을 사달라고 다시 요청할 수도 있다. 
  * 중요한 점은 클라이언트는 대리자를통해서 요청했기 때문에 그 이후 과정은 모른다는 점이다. 
  * 동생을 통해서 라면이 나에게 도착하기만 하면 된다. (프록시 체인)

![image-20221105152735402](/Users/ysk/study/study_repo/inflearn-spring-core/images//image-20221105152735402.png)



### 대체 가능
그런데 여기까지 듣고 보면 아무 객체나 프록시가 될 수 있는 것 같다.
객체에서 프록시가 되려면, 클라이언트는 서버에게 요청을 한 것인지, 프록시에게 요청을 한 것인지 조차
몰라야 한다.
쉽게 이야기해서 서버와 프록시는 같은 인터페이스를 사용해야 한다. 그리고 클라이언트가 사용하는 서버
객체를 프록시 객체로 변경해도 클라이언트 코드를 변경하지 않고 동작할 수 있어야 한다.

![image-20221105152833257](/Users/ysk/study/study_repo/inflearn-spring-core/images//image-20221105152833257.png)

서버와 프록시가 같은 인터페이스 사용



클래스 의존관계를 보면 클라이언트는 서버 인터페이스( ServerInterface )에만 의존한다. 

그리고 서버와 프록시가 같은 인터페이스를 사용한다. 

따라서 DI를 사용해서 대체 가능하다.

* 클라이언트는 프록시인지 서버인지 알 수가 없다. 인터페이스를 통하기때문 



![image-20221105153312822](/Users/ysk/study/study_repo/inflearn-spring-core/images//image-20221105153312822.png)

이번에는 런타임 객체 의존 관계를 살펴보자. 런타임(애플리케이션 실행 시점)에 클라이언트 객체에 DI를
사용해서 Client -> Server 에서 Client -> Proxy 로 객체 의존관계를 변경해도 클라이언트 코드를
전혀 변경하지 않아도 된다. 클라이언트 입장에서는 변경 사실 조차 모른다.
DI를 사용하면 클라이언트 코드의 변경 없이 유연하게 프록시를 주입할 수 있다.

## 프록시의 주요 기능
프록시를 통해서 할 수 있는 일은 크게 2가지로 구분할 수 있다.

* 접근 제어
  * 권한에 따른 접근 차단
  * 캐싱
  * 지연 로딩
* 부가 기능 추가
  * 원래 서버가 제공하는 기능에 더해서 부가 기능을 수행한다.
  * 예) 요청 값이나, 응답 값을 중간에 변형한다.
  * 예) 실행 시간을 측정해서 추가 로그를 남긴다.



프록시 객체가 중간에 있으면 크게 접근 제어와 부가 기능 추가를 수행할 수 있다







## GOF 디자인 패턴 - 프록시
둘다 프록시를 사용하는 방법이지만 GOF 디자인 패턴에서는 

`이 둘을 의도(intent)에 따라서 프록시 패턴과 데코레이터 패턴으로 구분한다.`

* 프록시 패턴: 접근 제어가 목적
* 데코레이터 패턴: 새로운 기능 추가가 목적



* 프록시 패턴 : 디자인 패턴, 
* 프록시 : 실제 행위를 수행하는 객체



`용어가 프록시 패턴이라고 해서 이 패턴만 프록시를 사용하는 것은 아니다. `

 데코레이터 패턴도 프록시를 사용한다.

이왕 프록시를 학습하기로 했으니 GOF 디자인 패턴에서 설명하는 프록시 패턴과 데코레이터 패턴을 나누어 학습해보자



>  참고: 프록시라는 개념은 클라이언트 서버라는 큰 개념안에서 자연스럽게 발생할 수 있다. 
>
> 프록시는 객체안에서의 개념도 있고, 웹 서버에서의 프록시도 있다. 
>
> 객체안에서 객체로 구현되어있는가, 웹 서버로구현되어 있는가 처럼 규모의 차이가 있을 뿐 근본적인 역할은 같다





## 프록시 패턴 - 예제 코드 작성
프록시 패턴을 이해하기 위한 예제 코드를 작성해보자. 

먼저 프록시 패턴을 도입하기 전 코드를 아주 단순하게 만들어보자.



```java
public interface Subject {
    String operation();
}

//

@Slf4j
public class RealSubject implements Subject { //호출시 1초 걸림

    @Override
    public String operation() { 
        log.info("실제 객체 호출");
        sleep(1000);
        return "data";
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class ProxyPatternClient {

    private Subject subject;

    public ProxyPatternClient(Subject subject) {
        this.subject = subject;
    }

    public void execute() {
        subject.operation();
    }

}
```



테스트코드 

```java

public class ProxyPatternTest {

    @Test
    void noProxyTest() {
        RealSubject realSubject = new RealSubject();
        ProxyPatternClient client = new ProxyPatternClient(realSubject);
        client.execute();
        client.execute();
        client.execute();
    }

}
```

* execute 호출마다 1초씩 걸리므로 총 3초++ 가 걸린다



Proxy를 이용하여 Cache 구현

```java

@Slf4j
public class CacheProxy implements Subject {

    private Subject target;
    private String cacheValue;

    public CacheProxy(Subject target) {
        this.target = target;
    }

    @Override
    public String operation() {
        log.info("프록시 호출");

        if (cacheValue == null) {
            cacheValue = target.operation();
        }

        return cacheValue;
    }
}

//

public class ProxyPatternTest {

    @Test
    void proxyTest() {
        RealSubject realSubject = new RealSubject();

        Subject subject = new CacheProxy(realSubject);
        ProxyPatternClient client = new ProxyPatternClient(subject);
        client.execute();
        client.execute();
        client.execute();
    }
}
```

* 1초 ++가 걸린다. 첫 execute 호출시에만 1초가 걸리고 나머지는 캐싱된 데이터를 리턴하기 때문이다.



client.execute()을 3번 호출하면 다음과 같이 처리된다.
1. client의 cacheProxy 호출 cacheProxy에 캐시 값이 없다. realSubject를 호출, 결과를 캐시에
  저장 (1초)
2. client의 cacheProxy 호출 cacheProxy에 캐시 값이 있다. cacheProxy에서 즉시 반환 (0초)
3. client의 cacheProxy 호출 cacheProxy에 캐시 값이 있다. cacheProxy에서 즉시 반환 (0초)
  결과적으로 캐시 프록시를 도입하기 전에는 3초가 걸렸지만, 캐시 프록시 도입 이후에는 최초에 한번만 1
  초가 걸리고, 이후에는 거의 즉시 반환한다.



### 정리
프록시 패턴의 핵심은 RealSubject 코드와 클라이언트 코드를 전혀 변경하지 않고, 프록시를 도입해서
`접근 제어를 했다는 점이다.`

그리고 클라이언트 코드의 변경 없이 자유롭게 프록시를 넣고 뺄 수 있다. 실제 클라이언트 입장에서는
프록시 객체가 주입되었는지, 실제 객체가 주입되었는지 알지 못한다.



* OCP와 DIP를 지키게 된다. 





## 데코레이터 패턴 - 예제 코드1
데코레이터 패턴을 이해하기 위한 예제 코드를 작성해보자. 먼저 데코레이터 패턴을 도입하기 전 코드를
아주 단순하게 만들어보자



```java
public interface Component {
	String operation();
}
//

@Slf4j
public class RealComponent implements Component {
	@Override
	public String operation() {
		log.info("RealComponent 실행");
		return "data";
	}
}

//


@Slf4j
public class DecoratorPatternClient {

    private Component component;

    public DecoratorPatternClient(Component component) {
        this.component = component;
    }

    public void execute() {
        String result = component.operation();
        log.info("result={}", result);
    }
}

//
@Slf4j
public class DecoratorPatternClientTest {

    @Test
    void noDecorator() {
        Component realComponent = new RealComponent();
        DecoratorPatternClient client = new
            DecoratorPatternClient(realComponent);

        client.execute();
    }

}
```



클라이언트 코드는 단순히 Component 인터페이스를 의존한다.
execute() 를 실행하면 component.operation() 을 호출하고, 그 결과를 출력한다.



### 부가 기능 추가
앞서 설명한 것 처럼 프록시를 통해서 할 수 있는 기능은 크게 접근 제어와 부가 기능 추가라는 2가지로
구분한다. 앞서 프록시 패턴에서 캐시를 통한 접근 제어를 알아보았다. 이번에는 프록시를 활용해서 부가
기능을 추가해보자. `이렇게 프록시로 부가 기능을 추가하는 것을 데코레이터 패턴이라 한다`

데코레이터 패턴: 원래 서버가 제공하는 기능에 더해서 부가 기능을 수행한다.
예) 요청 값이나, 응답 값을 중간에 변형한다.
예) 실행 시간을 측정해서 추가 로그를 남긴다

![image-20221105155915221](/Users/ysk/study/study_repo/inflearn-spring-core/images//image-20221105155915221.png)



```java
@Slf4j
public class MessageDecorator implements Component{

    private Component component;

    public MessageDecorator(Component component) {
        this.component = component;
    }

    @Override
    public String operation() {
        log.info("MessageDecorator 실행");
        String result = component.operation();
        String decoResult = "*****" + result + "*****";
      
      	log.info("MessageDecorator 꾸미기 적용 전={}, 적용 후={}", 
                 result,
                 decoResult);
      
        return decoResult;
    }
}
```



MessageDecorator 는 Component 인터페이스를 구현한다.
프록시가 호출해야 하는 대상을 component 에 저장한다.
operation() 을 호출하면 프록시와 연결된 대상을 호출( component.operation()) 하고, 그 응답 값에
***** 을 더해서 꾸며준 다음 반환한다.
예를 들어서 응답 값이 data 라면 다음과 같다.

* 꾸미기 전: data
* 꾸민 후 : *****data*****

```java

@Slf4j
public class DecoratorPatternClientTest {

    @Test
    void decorator1() {
        Component realComponent = new RealComponent();
        Component messageDecorator = new MessageDecorator(realComponent);
       
        DecoratorPatternClient client = new
            DecoratorPatternClient(messageDecorator);
      
        client.execute();
    }
}
```

실행 결과를 보면 MessageDecorator 가 RealComponent 를 호출하고 반환한 응답 메시지를 꾸며서
반환한 것을 확인할 수 있다.



### 데코레이터 패턴 - 예제 코드3
실행 시간을 측정하는 데코레이터
이번에는 기존 데코레이터에 더해서 실행 시간을 측정하는 기능까지 추가해보자

![image-20221105160134471](/Users/ysk/study/study_repo/inflearn-spring-core/images//image-20221105160134471.png)

* 즉 프록시 체이닝 이 된다

```java
@Slf4j
public class TimeDecorator implements Component {

    private Component component;

    public TimeDecorator(Component component) {
        this.component = component;
    }

    @Override
    public String operation() {
        log.info("TimeDecorator 실행");

        long startTime = System.currentTimeMillis();

        String result = component.operation();

        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("TimeDecorator 종료 resultTime={}ms", resultTime);
        return result;
    }

}

// 
@Slf4j
public class DecoratorPatternClientTest {


    @Test
    void decorator2() {
        Component realComponent = new RealComponent();
        Component messageDecorator = new MessageDecorator(realComponent);
        Component timeDecorator = new TimeDecorator(messageDecorator);
        DecoratorPatternClient client = new DecoratorPatternClient(timeDecorator);
        client.execute();
    }

}
```



실행 결과를 보면 TimeDecorator 가 MessageDecorator 를 실행하고 실행 시간을 측정해서 출력한 것을
확인할 수 있다.

![image-20221105160544429](/Users/ysk/study/study_repo/inflearn-spring-core/images//image-20221105160544429.png)



여기서 생각해보면 Decorator 기능에 일부 중복이 있다. 

꾸며주는 역할을 하는 Decorator 들은 스스로 존재할 수 없다. 

항상 꾸며줄 대상이 있어야 한다. 

따라서 내부에 호출 대상인 component 를 가지고 있어야한다. 그리고 component 를 항상 호출해야 한다.

이 부분이 중복이다. 

이런 중복을 제거하기 위해
component 를 속성으로 가지고 있는 Decorator 라는 추상 클래스를 만드는 방법도 고민할 수 있다.



이렇게 하면 추가로 클래스 다이어그램에서 어떤 것이 실제 컴포넌트 인지, 데코레이터인지 명확하게
구분할 수 있다.

여기까지 고민한 것이 바로 GOF에서 설명하는 데코레이터 패턴의 기본 예제이다.



### 프록시 패턴 vs 데코레이터 패턴
여기까지 진행하면 몇가지 의문이 들 것이다.

* Decorator 라는 추상 클래스를 만들어야 데코레이터 패턴일까?
* 프록시 패턴과 데코레이터 패턴은 그 모양이 거의 비슷한 것 같은데?



### 의도(intent)
사실 프록시 패턴과 데코레이터 패턴은 그 모양이 거의 같고, 상황에 따라 정말 똑같을 때도 있다. 그러면
둘을 어떻게 구분하는 것일까?

디자인 패턴에서 중요한 것은 해당 패턴의 겉모양이 아니라 그 패턴을 만든 의도가 더 중요하다. 

따라서 의도에 따라 패턴을 구분한다.

* 프록시 패턴의 의도: 다른 개체에 대한 접근을 제어하기 위해 대리자를 제공
* 데코레이터 패턴의 의도: 객체에 추가 책임(기능)을 동적으로 추가하고, 기능 확장을 위한 유연한 대안 제공



### 정리
프록시를 사용하고 해당 프록시가 접근 제어가 목적이라면 프록시 패턴이고, 

새로운 기능을 추가하는 것이 목적이라면 데코레이터 패턴이 된다.




## 구체 클래스 기반 프록시

그런데 자바의 다형성은 인터페이스를 구현하든,
아니면 클래스를 상속하든 상위 타입만 맞으면 다형성이 적용된다. 쉽게 이야기해서 인터페이스가 없어도
프록시를 만들수 있다는 뜻이다. 



> 참고: 자바 언어에서 다형성은 인터페이스나 클래스를 구분하지 않고 모두 적용된다. 해당 타입과 그 타입의
> 하위 타입은 모두 다형성의 대상이 된다. 자바 언어의 너무 기본적인 내용을 이야기했지만, 인터페이스가
> 없어도 프록시가 가능하다는 것을 확실하게 집고 넘어갈 필요가 있어서 자세히 설명했다.



## 클래스 기반 프록시의 단점

* super(null) : OrderServiceV2 : 
  * 자바 기본 문법에 의해 자식 클래스를 생성할 때는 항상 super() 로부모 클래스의 생성자를 호출해야 한다. 
  * 이 부분을 생략하면 기본 생성자가 호출된다. 
  * 그런데 부모 클래스인OrderServiceV2 는 기본 생성자가 없고, 생성자에서 파라미터 1개를 필수로 받는다. 
  * 따라서 파라미터를 넣어서 super(..) 를 호출해야 한다.
* 프록시는 부모 객체의 기능을 사용하지 않기 때문에 super(null) 을 입력해도 된다.
* 인터페이스 기반 프록시는 이런 고민을 하지 않아도 된다.





# 인터페이스 기반 프록시 vs 클래스 기반 프록시
인터페이스가 없어도 클래스 기반으로 프록시를 생성할 수 있다.
클래스 기반 프록시는 해당 클래스에만 적용할 수 있다. 

인터페이스 기반 프록시는 인터페이스만 같으면 모든 곳에 적용할 수 있다.
`클래스 기반 프록시는 상속을 사용하기 때문에 몇가지 제약이 있다.`

* 부 모 클래스의 생성자를 호출해야 한다.(앞서 본 예제)

* 클래스에 final 키워드가 붙으면 상속이 불가능하다.
* 메서드에 final 키워드가 붙으면 해당 메서드를 오버라이딩 할 수 없다.

이렇게 보면 인터페이스 기반의 프록시가 더 좋아보인다. 

맞다. 인터페이스 기반의 프록시는 상속이라는 제약에서 자유롭다. 

프로그래밍 관점에서도 인터페이스를 사용하는 것이 역할과 구현을 명확하게 나누기 때문에 더 좋다.



인터페이스 기반 프록시의 단점은 인터페이스가 필요하다는 그 자체이다. 

인터페이스가 없으면 인터페이스 기반 프록시를 만들 수 없다.

* 참고: 인터페이스 기반 프록시는 캐스팅 관련해서 단점도 있다, 





이론적으로는 모든 객체에 인터페이스를 도입해서 역할과 구현을 나누는 것이 좋다. 

이렇게 하면 역할과 구현을 나누어서 구현체를 매우 편리하게 변경할 수 있다.

 하지만 실제로는 구현을 거의 변경할 일이 없는 클래스도 많다.
인터페이스를 도입하는 것은 구현을 변경할 가능성이 있을 때 효과적인데, 구현을 변경할 가능성이 거의
없는 코드에 무작정 인터페이스를 사용하는 것은 번거롭고 그렇게 실용적이지 않다.

 이런곳에는 실용적인 관점에서 인터페이스를 사용하지 않고 구체 클래스를 바로 사용하는 것이 좋다 생각한다.

 (물론인터페이스를 도입하는 다양한 이유가 있다. 여기서 핵심은 인터페이스가 항상 필요하지는 않다는 것이다.)



### 결론
실무에서는 프록시를 적용할 때 V1처럼 인터페이스도 있고, V2처럼 구체 클래스도 있다. 

따라서 2가지 상황을 모두 대응할 수 있어야 한다.

### 너무 많은 프록시 클래스
지금까지 프록시를 사용해서 기존 코드를 변경하지 않고, 로그 추적기라는 부가 기능을 적용할 수 있었다.
그런데 문제는 프록시 클래스를 너무 많이 만들어야 한다는 점이다. 

잘 보면 프록시 클래스가 하는 일은 LogTrace 를 사용하는 것인데, 그 로직이 모두 똑같다. 

대상 클래스만 다를 뿐이다. 

만약 적용해야 하는 대상 클래스가 100개라면 프록시 클래스도 100개를 만들어야한다.
프록시 클래스를 하나만 만들어서 모든 곳에 적용하는 방법은 없을까?
바로 다음에 설명할 `동적 프록시 기술 `이 이 문제를 해결해준다.



# 리플렉션



자바가 기본으로 제공하는 JDK 동적 프록시 기술이나 CGLIB 같은 프록시 생성 오픈소스 기술을 활용하면
프록시 객체를 동적으로 만들어낼 수 있다. 

쉽게 이야기해서 프록시 클래스를 지금처럼 계속 만들지 않아도 된다는 것이다. 

프록시를 적용할 코드를 하나만 만들어두고 동적 프록시 기술을 사용해서 프록시 객체를 찍어내면 된다. 

JDK 동적 프록시를 이해하기 위해서는 먼저 자바의 리플렉션 기술을 이해해야 한다.
리플렉션 기술을 사용하면 클래스나 메서드의 메타정보를 동적으로 획득하고, 코드도 동적으로 호출할 수 있다.
여기서는 JDK 동적 프록시를 이해하기 위한 최소한의 리플랙션 기술을 알아보자

