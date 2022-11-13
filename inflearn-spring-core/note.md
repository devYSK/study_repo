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



리플렉션은 클래스나 메서드의 메타정보를 사용해서 동적으로 호출하는 메서드를 변경할 수 있다.



```java
package hello.proxy.jdkdynamic;

@Slf4j
public class ReflectionTest {

    @Test
    void reflection1() throws Exception {

        Class classHello = Class.forName("hello.proxy.jdkdynamic.ReflectionTest$Hello");

        Hello target = new Hello();

        //callA 메서드 정보
        Method methodCallA = classHello.getMethod("callA");
        Object result1 = methodCallA.invoke(target);
        log.info("result1 = {}", result1);

        //callB 메서드 정보
        Method methodCallB = classHello.getMethod("callB");
        Object result2 = methodCallB.invoke(target);
        log.info("result2 = {}", result2);
    }

    @Slf4j
    static class Hello {

        public String callA() {
            log.info("callA");
            return "A";
        }

        public String callB() {
            log.info("callB");
            return "B";
        }
    }
}
```

* `class.forName("hello.proxy.jdkdynamic.ReflectionTest$Hello")`  
  * 클래스 메타정보를 획득한다. 
  * 참고로 내부 클래스는 구분을 위해 $ 를 사용한다.

* `classHello.getMethod("call")` : 해당 클래스의 call 메서드 메타정보를 획득한다.
* `methodCallA.invoke(target)` : 획득한 메서드 메타정보로 실제 인스턴스의 메서드를 호출한다
  *  여기서methodCallA 는 Hello 클래스의 callA() 이라는 메서드 메타정보이다.
  * `methodCallA.invoke(인스턴스)` 를 호출하면서 인스턴스를 넘겨주면 해당 인스턴스의 callA() 메서드를
    찾아서 실행한다. 
  * 여기서는 target 의 callA() 메서드를 호출한다.



```java
@Test
    
void reflection2() throws Exception {

  Class classHello = Class.forName("hello.proxy.jdkdynamic.ReflectionTest$Hello");

  Hello target = new Hello();

  //callA 메서드 정보
  Method methodCallA = classHello.getMethod("callA");  
  dynamicCall(target, methodCallA);

  //callB 메서드 정보
  Method methodCallB = classHello.getMethod("callB");
  dynamicCall(target, methodCallB);  
}

   
private void  dynamicCall(Hello target, Method method) throws Exception {
  log.info("start");
  Object result = method.invoke(target);
  log.info("result = {}", result);  
}
```



* `Method method` : 첫 번째 파라미터는 호출할 메서드 정보가 넘어온다.  Method 라는 메타정보를 통해서 호출할 메서드 정보가
  동적으로 제공된다.

* `Object target `: 실제 실행할 인스턴스 정보가 넘어온다.
  *  타입이 Object 라는 것은 어떠한 인스턴스도 받을 수 있다는 뜻이다. 
  * 물론 method.invoke(target) 를 사용할 때 호출할 클래스와 메서드 정보가 서로 다르면 예외가 발생한다



##### 주의
리플렉션을 사용하면 클래스와 메서드의 메타정보를 사용해서 애플리케이션을 동적으로 유연하게 만들 수있다.   
 하지만 리플렉션 기술은 런타임에 동작하기 때문에, 컴파일 시점에 오류를 잡을 수 없다  
가장 좋은 오류는 개발자가 즉시 확인할 수 있는 컴파일 오류이고, 가장 무서운 오류는 사용자가 직접 실행할
때 발생하는 런타임 오류다  

따라서 리플렉션은 일반적으로 사용하면 안된다.   
 지금까지 프로그래밍 언어가 발달하면서 타입 정보를기반으로 컴파일 시점에 오류를 잡아준 덕분에 개발자가 편하게 살았는데,   
`리플렉션은 그것에 역행하는 방식이다.`



주의
> JDK 동적 프록시는 인터페이스를 기반으로 프록시를 동적으로 만들어준다. 
> `따라서 인터페이스가 필수이다`





### 자바 언어가 기본으로 제공하는 JDK 동적 프록시



###  JDK 동적 프록시 InvocationHandler

JDK 동적 프록시에 적용할 로직은 InvocationHandler 인터페이스를 구현해서 작성하면 된다.

#### JDK 동적 프록시가 제공하는 InvocationHandler

```java
package java.lang.reflect;

public interface InvocationHandler {
  
	public Object invoke(Object proxy, Method method, Object[] args)	throws Throwable;

}
```



제공되는 파라미터는 다음과 같다.

* Object proxy : 프록시 자신
* Method method : 호출한 메서드
* Object[] args : 메서드를 호출할 때 전달한 인수



```java
@Slf4j
public class TimeInvocationHandler implements InvocationHandler {

    private final Object target;

    public TimeInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("Time Proxy 실행");

        long startTime = System.currentTimeMillis();
        Object result = method.invoke(target, args);
        long endTime = System.currentTimeMillis() - startTime;
        
        log.info("resultTime = {}", endTime);
        return result;
    }
    
}
```



* `TimeInvocationHandle`r 은 `InvocationHandler 인터페이스를 구현`한다. 
  * 이렇게해서 JDK 동적프록시에 적용할 공통 로직을 개발할 수 있다.
* Object target : 동적 프록시가 호출할 대상
* method.invoke(target, args) : 리플렉션을 사용해서 target 인스턴스의 메서드를 실행한다. 
  * args는 메서드 호출시 넘겨줄 인수이다.



```java
@Slf4j
public class JdkDynamicProxyTest {

    @Test
    void dynamicA() {
        AInterface target = new AImpl();

        TimeInvocationHandler handler = new TimeInvocationHandler(target);

        AInterface proxy = (AInterface)
            Proxy.newProxyInstance(AInterface.class.getClassLoader(), new Class[]{AInterface.class}, handler);

        proxy.call();

        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());
    }

    @Test
    void dynamicB() {
        BInterface target = new BImpl();
        TimeInvocationHandler handler = new TimeInvocationHandler(target);

        BInterface proxy = (BInterface)
            Proxy.newProxyInstance(BInterface.class.getClassLoader(), new Class[]
                {BInterface.class}, handler);

        proxy.call();

        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());
    }
}
```



* new TimeInvocationHandler(target) : 동적 프록시에 적용할 핸들러 로직이다.
* Proxy.newProxyInstance(AInterface.class.getClassLoader(), new Class[] {AInterface.class}, handler)
  * `동적 프록시는 java.lang.reflect.Proxy 를 통해서 생성할 수 있다.` 
  * 클래스 로더 정보, 인터페이스, 그리고 핸들러 로직을 넣어주면 된다. 그러면 해당 인터페이스를
    기반으로 동적 프록시를 생성하고 그 결과를 반환한다.



만들어진 동적 프록시는 핸들러 로직을 실행한다.



실행 순서
1. 클라이언트는 JDK 동적 프록시의 call() 을 실행한다.
2. JDK 동적 프록시는 InvocationHandler.invoke() 를 호출한다. TimeInvocationHandler 가
구현체로 있으로 TimeInvocationHandler.invoke() 가 호출된다.
3. TimeInvocationHandler 가 내부 로직을 수행하고, method.invoke(target, args) 를 호출해서
target 인 실제 객체( AImpl )를 호출한다.
4. AImpl 인스턴스의 call() 이 실행된다.
5. AImpl 인스턴스의 call() 의 실행이 끝나면 TimeInvocationHandler 로 응답이 돌아온다. 시간
로그를 출력하고 결과를 반환한다.



![image-20221112213458100](/Users/ysk/study/study_repo/inflearn-spring-core/images//image-20221112213458100.png)



인터페이스 기반 프록시는 JDK 동적 프록시를 사용해서 동적으로 만들고 Handler 는 공통으로 사용한다.

적용 대상 만큼 프록시 객체를 만들지 않아도 된다.   
그리고 같은 부가 기능 로직을 한번만 개발해서 공통으로 적용할 수 있다.  
 만약 적용 대상이 100개여도 동적 프록시를 통해서 생성하고, 각각 필요한 Handler 만 만들어서 넣어주면 된다



결과적으로 프록시 클래스를 수 없이 만들어야 하는 문제도 해결하고, 부가 기능 로직도 하나의 클래스에
모아서 단일 책임 원칙(SRP)도 지킬 수 있게 되었다.  



### JDK Danymic Proxy에서 특정 메소드 이름을 제외하는법

```java
public class LogTraceFilterHandler implements InvocationHandler {

    private final Object target;
    private final LogTrace logTrace;
    private final String[] patterns;
    
    public LogTraceFilterHandler(Object target, LogTrace logTrace, String[] patterns) {
        this.target = target;
        this.logTrace = logTrace;
        this.patterns = patterns;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws
        Throwable {
        
        // 메서드 이름 필터
        String methodName = method.getName();
        
        if (!PatternMatchUtils.simpleMatch(patterns, methodName)) {
            return method.invoke(target, args);
        }
        
        TraceStatus status = null;
        
      try {
            String message = method.getDeclaringClass().getSimpleName() + "."
                + method.getName() + "()";
            status = logTrace.begin(message);

            //로직 호출
            Object result = method.invoke(target, args);
            logTrace.end(status);

            return result;
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }
}

```

* 메소드 이름으로 걸르면 된다 .



스프링이 제공하는 `PatternMatchUtils.simpleMatch(..) `를 사용하면 단순한 매칭 로직을 쉽게 적용할 수 있다.

* `xxx` : xxx가 정확히 매칭되면 참
* `xxx*` : xxx로 시작하면 참
* `*xxx` : xxx로 끝나면 참
* `*xxx*` : xxx가 있으면 참



#### JDK 동적 프록시 - 한계
JDK 동적 프록시는 인터페이스가 필수이다.



## CGLIB - 소개
#### CGLIB: Code Generator Library

* CGLIB는 바이트코드를 조작해서 동적으로 클래스를 생성하는 기술을 제공하는 라이브러리이다.
* CGLIB를 사용하면 인터페이스가 없어도 구체 클래스만 가지고 동적 프록시를 만들어낼 수 있다.
* CGLIB는 원래는 외부 라이브러리인데, 스프링 프레임워크가 스프링 내부 소스 코드에 포함했다. 따라서
  스프링을 사용한다면 별도의 외부 라이브러리를 추가하지 않아도 사용할 수 있다.  

  


* 참고로 우리가 CGLIB를 직접 사용하는 경우는 거의 없다. 이후에 설명할 스프링의 ProxyFactory 라는
  것이 이 기술을 편리하게 사용하게 도와주기 때문에, 너무 깊이있게 파기 보다는 CGLIB가 무엇인지 대략
  개념만 잡으면 된다.

   



JDK 동적 프록시에서 실행 로직을 위해` InvocationHandler` 를 제공했듯이,   
CGLIB는 `MethodInterceptor` 를 제공한다.



```java
package org.springframework.cglib.proxy;

public interface MethodInterceptor extends Callback {
	Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable;
}
```



* obj : CGLIB가 적용된 객체
* method : 호출된 메서드
* args : 메서드를 호출하면서 전달된 인수
* proxy : 메서드 호출에 사용



```java
@Slf4j
public class TimeMethodInterceptor implements MethodInterceptor {

    private final Object target;

    public TimeMethodInterceptor(Object target) {
        this.target = target;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy)
        throws Throwable {
        log.info("Time Proxy 실행");

        long startTime = System.currentTimeMillis();

        Object result = methodProxy.invoke(target, args);

        long endTime = System.currentTimeMillis() - startTime;

        log.info("resultTime = {}", endTime);
        return result;

    }
}
```



* TimeMethodInterceptor 는 MethodInterceptor 인터페이스를 구현해서 CGLIB 프록시의 실행
  로직을 정의한다.
* JDK 동적 프록시를 설명할 때 예제와 거의 같은 코드이다.
* Object target : 프록시가 호출할 실제 대상
* proxy.invoke(target, args) : 실제 대상을 동적으로 호출한다.
  * 참고로 method 를 사용해도 되지만, CGLIB는 성능상 MethodProxy proxy 를 사용하는 것을 권장한다.



```java
@Slf4j
public class CglibTest {


    @Test
    void cglib() {
        ConcreteService target = new ConcreteService();

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(target.getClass());

        enhancer.setCallback(new TimeMethodInterceptor(target));

        ConcreteService proxy =  (ConcreteService)enhancer.create();


        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());
        
        proxy.call();
    }
}

```

```
23:38:06.908 [Test worker] INFO hello.proxy.cglib.CglibTest - targetClass=class hello.proxy.common.service.ConcreteService

23:38:06.914 [Test worker] INFO hello.proxy.cglib.CglibTest - proxyClass=class hello.proxy.common.service.ConcreteService$$EnhancerByCGLIB$$25d6b0e3
```



* ConcreteService 는 인터페이스가 없는 구체 클래스이다.  



* Enhancer : CGLIB는 Enhancer 를 사용해서 프록시를 생성한다.



* enhancer.setSuperclass(ConcreteService.class) : CGLIB는 구체 클래스를 상속 받아서 프록시를
  생성할 수 있다. 어떤 구체 클래스를 상속 받을지 지정한다.



* enhancer.setCallback(new TimeMethodInterceptor(target))
  * 프록시에 적용할 실행 로직을 할당한다.



* enhancer.create() : 프록시를 생성한다. 
  * 앞서 설정한 enhancer.setSuperclass(ConcreteService.class) 에서 지정한 클래스를 상속 받아서 프록시가
    만들어진다.



JDK 동적 프록시는 인터페이스를 구현(implement)해서 프록시를 만든다. CGLIB는 구체 클래스를 상속
(extends)해서 프록시를 만든다.





#### CGLIB가 생성한 프록시 클래스 이름
CGLIB가 동적으로 생성하는 클래스 이름은 다음과 같은 규칙으로 생성된다.
`대상클래스 $$ EnhancerByCGLIB $$ 임의코드`



### CGLIB 제약
* 클래스 기반 프록시는 상속을 사용하기 때문에 몇가지 제약이 있다.



* 부모 클래스의 생성자를 체크해야 한다. 
  * CGLIB는 자식 클래스를 동적으로 생성하기 때문에 기본
    생성자가 필요하다.



* 클래스에 final 키워드가 붙으면 상속이 불가능하다. 
  * CGLIB에서는 예외가 발생한다.



* 메서드에 final 키워드가 붙으면 해당 메서드를 오버라이딩 할 수 없다. 
  * CGLIB에서는 프록시 로직이 동작하지 않는다.



> ProxyFactory 를 통해서 CGLIB를 적용하면   편리하다





### 정리
남은 문제

* 인터페이스가 있는 경우에는 JDK 동적 프록시를 적용하고, 그렇지 않은 경우에는 CGLIB를 적용하려면
  어떻게 해야할까?



* 두 기술을 함께 사용할 때 부가 기능을 제공하기 위해서 JDK 동적 프록시가 제공하는
  InvocationHandler 와 CGLIB가 제공하는 MethodInterceptor 를 각각 중복으로 만들어서 관리해야할까?



* 특정 조건에 맞을 때 프록시 로직을 적용하는 기능도 공통으로 제공되었으면?



# 스프링이 지원하는 프록시



## 인터페이스가 있는 경우에는 JDK 동적 프록시를 적용하고, 그렇지 않은 경우에는 CGLIB를적용하려면 어떻게 해야할까?



스프링은 유사한 구체적인 기술들이 있을 때, 그것들을 통합해서 일관성 있게 접근할 수 있고, 더욱 편리하게
사용할 수 있는 추상화된 기술을 제공한다.

스프링은 동적 프록시를 통합해서 편리하게 만들어주는 `프록시 팩토리( ProxyFactory )`라는 기능을 제공한다.

이전에는 상황에 따라서 JDK 동적 프록시를 사용하거나 CGLIB를 사용해야 했다면,   
이제는 이 프록시팩토리 하나로 편리하게 동적 프록시를 생성할 수 있다.
프록시 팩토리는 인터페이스가 있으면 JDK 동적 프록시를 사용하고, 구체 클래스만 있다면 CGLIB를
사용한다. 그리고 이 설정을 변경할 수도 있다.



![image-20221112234954474](/Users/ysk/study/study_repo/inflearn-spring-core/images//image-20221112234954474.png)

![image-20221112235016145](/Users/ysk/study/study_repo/inflearn-spring-core/images//image-20221112235016145.png)



## 두 기술을 함께 사용할 때 부가 기능을 적용하기 위해 JDK 동적 프록시가 제공하는 InvocationHandler와 CGLIB가 제공하는 MethodInterceptor를 각각 중복으로 따로 만들어야 할까?

스프링은 이 문제를 해결하기 위해 부가 기능을 적용할 때 `Advice` 라는 새로운 개념을 도입했다.  
 개발자는 InvocationHandler 나 MethodInterceptor 를 신경쓰지 않고, Advice 만 만들면 된다.  

결과적으로 InvocationHandler 나 MethodInterceptor 는 Advice 를 호출하게 된다.  

프록시 팩토리를 사용하면 Advice 를 호출하는 전용 InvocationHandler , MethodInterceptor 를
내부에서 사용한다.



### Advice

![image-20221112235459799](/Users/ysk/study/study_repo/inflearn-spring-core/images//image-20221112235459799.png)



![image-20221112235629004](/Users/ysk/study/study_repo/inflearn-spring-core/images//image-20221112235629004.png)



## 특정 조건에 맞을 때 프록시 로직을 적용하는 기능도 공통으로 제공되었으면?
앞서 특정 메서드 이름의 조건에 맞을 때만 프록시 부가 기능이 적용되는 코드를 직접 만들었다.   
스프링은 `Pointcut` 이라는 개념을 도입해서 이 문제를 일관성 있게 해결한다.



## Proxy Factory(프록시 팩토리)



#### Advice 만들기


Advice 는 프록시에 적용하는 부가 기능 로직이다.   
이것은 JDK 동적 프록시가 제공하는 InvocationHandler 와 CGLIB가 제공하는 MethodInterceptor 의 개념과 유사한다.  
 둘을 개념적으로추상화 한 것이다. 프록시 팩토리를 사용하면 둘 대신에 Advice 를 사용하면 된다



### MethodInterceptor - 스프링이 제공하는 코드

```java
package org.aopalliance.intercept;

public interface MethodInterceptor extends Interceptor {
	Object invoke(MethodInvocation invocation) throws Throwable;
}
```



* MethodInvocation invocation
  * 내부에는 다음 메서드를 호출하는 방법, 현재 프록시 객체 인스턴스, args , 메서드 정보 등이
    포함되어 있다. 기존에 파라미터로 제공되는 부분들이 이 안으로 모두 들어갔다고 생각하면 된다.
* CGLIB의 MethodInterceptor 와 이름이 같으므로 패키지 이름에 주의하자
  * 참고로 여기서 사용하는 org.aopalliance.intercept 패키지는 스프링 AOP 모듈( spring-top )
    안에 들어있다.
* MethodInterceptor 는 Interceptor 를 상속하고 Interceptor 는 Advice 인터페이스를 상속한다.



```java
@Slf4j
public class TimeAdvice implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        log.info("TimeProxy 실행");
        long startTime = System.currentTimeMillis();
        
        Object result = invocation.proceed();
        
        long endTime = System.currentTimeMillis();
        
        long resultTime = endTime - startTime;
        
        log.info("TimeProxy 종료 resultTime={}ms", resultTime);
        return result;
    }
}
```



`프록시 Target을 안넣어줘도 된다! `

-> invocation 안에 들어있다.



* TimeAdvice 는 앞서 설명한 MethodInterceptor 인터페이스를 구현한다. 패키지 이름에 주의하자.
* Object result = invocation.proceed()
  * invocation.proceed() 를 호출하면 target 클래스를 호출하고 그 결과를 받는다.
  * 그런데 기존에 보았던 코드들과 다르게 target 클래스의 정보가 보이지 않는다. target 클래스의
    정보는 MethodInvocation invocation 안에 모두 포함되어 있다.
  * 그 이유는  프록시 팩토리로 프록시를 생성하는 단계에서 이미 target 정보를 파라미터로 전달받기 때문이다.



```java
@Slf4j
public class ProxyFactoryTest {

    @Test
    @DisplayName("인터페이스가 있으면 JDK 동적 프록시 사용")
    void interfaceProxy() {
        ServiceInterface target = new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.addAdvice(new TimeAdvice());

        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();

        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());

        proxy.save();

        assertThat(AopUtils.isAopProxy(proxy)).isTrue();
        assertThat(AopUtils.isJdkDynamicProxy(proxy)).isTrue();
        assertThat(AopUtils.isCglibProxy(proxy)).isFalse();
    }
}

```



* new ProxyFactory(target) : 프록시 팩토리를 생성할 때, 생성자에 프록시의 호출 대상을 함께
  넘겨준다. 
  * 프록시 팩토리는 이 인스턴스 정보를 기반으로 프록시를 만들어낸다. 
  * 만약 이 인스턴스에 인터페이스가 있다면 JDK 동적 프록시를 기본으로 사용하고 인터페이스가 없고 구체 클래스만 있다면
    CGLIB를 통해서 동적 프록시를 생성한다. 
  * 여기서는 target 이 new ServiceImpl() 의 인스턴스이기 때문에 ServiceInterface 인터페이스가 있다. 
  * 따라서 이 인터페이스를 기반으로 JDK 동적 프록시를 생성한다.



* proxyFactory.addAdvice(new TimeAdvice()) : 프록시 팩토리를 통해서 만든 프록시가 사용할 부가
  기능 로직을 설정한다. 
  * JDK 동적 프록시가 제공하는 InvocationHandler 와 CGLIB가 제공하는
    MethodInterceptor 의 개념과 유사하다. 
  * 이렇게 프록시가 제공하는 부가 기능 로직을 어드바이스 ( Advice )라 한다. 번역하면 조언을 해준다고 생각하면 된다.



* proxyFactory.getProxy() : 프록시 객체를 생성하고 그 결과를 받는다.



### 프록시 팩토리를 통한 프록시 적용 확인
프록시 팩토리로 프록시가 잘 적용되었는지 확인하려면 다음 기능을 사용하면 된다.

* AopUtils.isAopProxy(proxy) : 프록시 팩토리를 통해서 프록시가 생성되면 JDK 동적 프록시나,
  CGLIB 모두 참이다.
  * 프록시 팩토리로 만들었을때만 가능하다. 



* AopUtils.isJdkDynamicProxy(proxy) : 프록시 팩토리를 통해서 프록시가 생성되고, JDK 동적
  프록시인 경우 참



* AopUtils.isCglibProxy(proxy) : 프록시 팩토리를 통해서 프록시가 생성되고, CGLIB 동적 프록시인
  경우 경우 참



* 물론 proxy.getClass() 처럼 인스턴스의 클래스 정보를 직접 출력해서 확인할 수 있다.



### 인터페이스가 있지만, CGLIB를 사용해서 인터페이스가 아닌 클래스 기반으로 동적 프록시를 만드는 방법


프록시 팩토리는 proxyTargetClass 라는 옵션을 제공하는데, 이 옵션에 true 값을 넣으면 인터페이스가있어도 강제로 CGLIB를 사용한다. 그리고 인터페이스가 아닌 클래스 기반의 프록시를 만들어준다



##  프록시 팩토리의 기술 선택 방법

* 대상에 인터페이스가 있으면: JDK 동적 프록시, 인터페이스 기반 프록시
* 대상에 인터페이스가 없으면: CGLIB, 구체 클래스 기반 프록시
* proxyTargetClass=true : CGLIB, 구체 클래스 기반 프록시, 인터페이스 여부와 상관없음



## 정리
* 프록시 팩토리의 서비스 추상화 덕분에 구체적인 CGLIB, JDK 동적 프록시 기술에 의존하지 않고, 매우
  편리하게 동적 프록시를 생성할 수 있다.



* 프록시의 부가 기능 로직도 특정 기술에 종속적이지 않게 Advice 하나로 편리하게 사용할 수 있었다.
  이것은 프록시 팩토리가 내부에서 JDK 동적 프록시인 경우 InvocationHandler 가 Advice 를
  호출하도록 개발해두고, CGLIB인 경우 MethodInterceptor 가 Advice 를 호출하도록 기능을
  개발해두었기 때문이다.



## 참고
> 스프링 부트는 AOP를 적용할 때 기본적으로 proxyTargetClass=true 로 설정해서 사용한다.
> 따라서 인터페이스가 있어도 항상 CGLIB를 사용해서 구체 클래스를 기반으로 프록시를 생성



# 포인트컷, 어드바이스, 어드바이저 - 소개





스프링 AOP를 공부했다면 다음과 같은 단어를 들어보았을 것이다. 항상 잘 정리가 안되는 단어들인데,
단순하지만 중요하니 이번에 확실히 정리해보자.

* 포인트컷( Pointcut ): 어디에 부가 기능을 적용할지, 어디에 부가 기능을 적용하지 않을지 판단하는
  필터링 로직이다. 주로 클래스와 메서드 이름으로 필터링 한다. 이름 그대로 어떤 포인트(Point)에 기능을
  적용할지 하지 않을지 잘라서(cut) 구분하는 것이다.



* 어드바이스( Advice ): 이전에 본 것 처럼 프록시가 호출하는 부가 기능이다. 단순하게 프록시 로직이라
  생각하면 된다.



* 어드바이저( Advisor ): 단순하게 하나의 포인트컷과 하나의 어드바이스를 가지고 있는 것이다. 쉽게
  이야기해서 포인트컷1 + 어드바이스1이다.





정리하면 부가 기능 로직을 적용해야 하는데, 포인트컷으로 어디에? 적용할지 선택하고, 어드바이스로 어떤
로직을 적용할지 선택하는 것이다. 그리고 어디에? 어떤 로직?을 모두 알고 있는 것이 어드바이저이다.

### 쉽게 기억하기



* 조언( Advice )을 어디( Pointcut )에 할 것인가?
* 조언자( Advisor )는 어디( Pointcut )에 조언( Advice )을 해야할지 할지 알고 있다



> 어디(PointCut)에 부가기능(Advice)를 적용할 것인가 = Advisor



### 역할과 책임

이렇게 구분한 것은 역할과 책임을 명확하게 분리한 것이다.

* 포인트컷은 대상 여부를 확인하는 필터 역할만 담당한다.



* 어드바이스는 깔끔하게 부가 기능 로직만 담당한다.



* 둘을 합치면 어드바이저가 된다. 스프링의 어드바이저는 하나의 포인트컷 + 하나의 어드바이스로 구성된다.



![image-20221113002009685](/Users/ysk/study/study_repo/inflearn-spring-core/images/image-20221113002009685.png)



어드바이저는 하나의 포인트컷과 하나의 어드바이스를 가지고 있다.
프록시 팩토리를 통해 프록시를 생성할 때 어드바이저를 제공하면 어디에 어떤 기능을 제공할 지 알 수
있다.



```java
@Slf4j
public class AdvisorTest {

    @Test
    void advisorTest1() {
        ServiceInterface target = new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);

        DefaultPointcutAdvisor advisor = new
            DefaultPointcutAdvisor(Pointcut.TRUE, new TimeAdvice());

        proxyFactory.addAdvisor(advisor);

        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();

        proxy.save();
        proxy.find();
    }
}
```



* new DefaultPointcutAdvisor : Advisor 인터페이스의 가장 일반적인 구현체이다. 생성자를 통해
  하나의 포인트컷과 하나의 어드바이스를 넣어주면 된다. 어드바이저는 하나의 포인트컷과 하나의
  어드바이스로 구성된다.



* Pointcut.TRUE : 항상 true 를 반환하는 포인트컷이다. 이후에 직접 포인트컷을 구현해볼 것이다.



* new TimeAdvice() : 앞서 개발한 TimeAdvice 어드바이스를 제공한다.



* proxyFactory.addAdvisor(advisor) : 프록시 팩토리에 적용할 어드바이저를 지정한다. 어드바이저는
  내부에 포인트컷과 어드바이스를 모두 가지고 있다. 따라서 어디에 어떤 부가 기능을 적용해야 할지
  어드바이스 하나로 알 수 있다. `프록시 팩토리를 사용할 때 어드바이저는 필수이다.`



* 그런데 생각해보면 이전에 분명히 proxyFactory.addAdvice(new TimeAdvice()) 이렇게
  어드바이저가 아니라 어드바이스를 바로 적용했다. 이것은 단순히 편의 메서드이고 결과적으로 해당 메서드
  내부에서 지금 코드와 똑같은 다음 어드바이저가 생성된다.  

  `DefaultPointcutAdvisor(Pointcut.TRUE, new TimeAdvice())`



> Advisor(어드바이저)는 여러개 추가할 수 있다.



![image-20221113003801515](/Users/ysk/study/study_repo/inflearn-spring-core/images//image-20221113003801515.png)



## 특정 메소드에만 포인트컷을 적용하는법



### Pointcut 관련 인터페이스 - 스프링 제공

```java
public interface Pointcut {
	ClassFilter getClassFilter();
	MethodMatcher getMethodMatcher();
}

public interface ClassFilter {
	boolean matches(Class<?> clazz);
}

public interface MethodMatcher {
	boolean matches(Method method, Class<?> targetClass);
//..
}
```



포인트컷은 크게 ClassFilter 와 MethodMatcher 둘로 이루어진다. 이름 그대로 하나는 클래스가
맞는지, 하나는 메서드가 맞는지 확인할 때 사용한다. 둘다 true 로 반환해야 어드바이스를 적용할 수 있다



### 커스텀 Pointcut



```java

@Slf4j
public class AdvisorTest {

    @Test
    @DisplayName("직접 만든 포인트컷")
    void advisorTest2() {
        ServiceImpl target = new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(new
            MyPointcut(), new TimeAdvice());
        proxyFactory.addAdvisor(advisor);
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();
        proxy.save();
        proxy.find();
    }


    static class MyPointcut implements Pointcut {

        @Override
        public ClassFilter getClassFilter() {
            return ClassFilter.TRUE;
        }

        @Override
        public MethodMatcher getMethodMatcher() {
            return new MyMethodMatcher();
        }
    }

    static class MyMethodMatcher implements MethodMatcher {

        private String matchName = "save";

        @Override
        public boolean matches(Method method, Class<?> targetClass) {
            boolean result = method.getName().equals(matchName);

            log.info("포인트컷 호출 method={} targetClass={}", method.getName(),
                targetClass);

            log.info("포인트컷 결과 result={}", result);
            return result;
        }

        @Override
        public boolean isRuntime() {
            return false;
        }

        @Override
        public boolean matches(Method method, Class<?> targetClass, Object... args) {
            throw new UnsupportedOperationException();
        }
    }
}
```





### MyPointcut
* 직접 구현한 포인트컷이다. Pointcut 인터페이스를 구현한다.
* 현재 메서드 기준으로 로직을 적용하면 된다. 
* 클래스 필터는 항상 true 를 반환하도록 했고, 메서드 비교 기능은 MyMethodMatcher 를 사용한다.



### MyMethodMatcher
* 직접 구현한 MethodMatcher 이다. MethodMatcher 인터페이스를 구현한다.
* matches() : 이 메서드에 method , targetClass 정보가 넘어온다. 이 정보로 어드바이스를 적용할지
  적용하지 않을지 판단할 수 있다.
* 여기서는 메서드 이름이 "save" 인 경우에 true 를 반환하도록 판단 로직을 적용했다.
  isRuntime() , matches(... args) : isRuntime() 이 값이 참이면 matches(... args) 메서드가
  대신 호출된다. 
* 동적으로 넘어오는 매개변수를 판단 로직으로 사용할 수 있다.
* isRuntime() 이 false 인 경우 클래스의 정적 정보만 사용하기 때문에 스프링이 내부에서 캐싱을
  통해 성능 향상이 가능하지만, isRuntime() 이 true 인 경우 매개변수가 동적으로 변경된다고
  가정하기 때문에 캐싱을 하지 않는다.

### new DefaultPointcutAdvisor(new MyPointcut(), new TimeAdvice())
어드바이저에 직접 구현한 포인트컷을 사용한다



![image-20221113005933326](/Users/ysk/study/study_repo/inflearn-spring-core/images//image-20221113005933326.png)

1. 클라이언트가 프록시의 save() 를 호출한다.
2. 포인트컷에 Service 클래스의 save() 메서드에 어드바이스를 적용해도 될지 물어본다.
3. 포인트컷이 true 를 반환한다. 따라서 어드바이스를 호출해서 부가 기능을 적용한다.
4. 이후 실제 인스턴스의 save() 를 호출한다.



![image-20221113005949827](/Users/ysk/study/study_repo/inflearn-spring-core/images//image-20221113005949827.png)



1. 클라이언트가 프록시의 find() 를 호출한다.
2. 포인트컷에 Service 클래스의 find() 메서드에 어드바이스를 적용해도 될지 물어본다.
3. 포인트컷이 false 를 반환한다. 따라서 어드바이스를 호출하지 않고, 부가 기능도 적용되지 않는다.
4. 실제 인스턴스를 호출한다.





# 스프링이 제공하는 포인트컷( Spring Pointcut)



스프링이 제공하는 `NameMatchMethodPointcut `를 사용



```java
package hello.proxy.advisor;

import hello.proxy.common.advice.TimeAdvice;
import hello.proxy.common.service.ServiceImpl;
import hello.proxy.common.service.ServiceInterface;
import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

@Slf4j
public class AdvisorTest {

    @Test
    @DisplayName("스프링이 제공하는 포인트컷")
    void advisorTest3() {
        ServiceImpl target = new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        
        //
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedNames("save");
        //
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(pointcut, new
            TimeAdvice());
      
        proxyFactory.addAdvisor(advisor);
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();
        proxy.save();
        proxy.find();
    }

}

```

NameMatchMethodPointcut 을 생성하고 setMappedNames(...) 으로 메서드 이름을 지정하면
포인트컷이 완성된다



### 스프링은 무수히 많은 포인트컷을 제공한다



* NameMatchMethodPointcut : 메서드 이름을 기반으로 매칭한다. 
  * 내부에서는 PatternMatchUtils 를 사용한다.
  * 예) *xxx* 허용
* JdkRegexpMethodPointcut : JDK 정규 표현식을 기반으로 포인트컷을 매칭한다.
* TruePointcut : 항상 참을 반환한다.
* AnnotationMatchingPointcut : 애노테이션으로 매칭한다.
* AspectJExpressionPointcut : aspectJ 표현식으로 매칭한다.





## 가장 중요한 것은 aspectJ 표현식



여기에서 사실 다른 것은 중요하지 않다.   
실무에서는 사용하기도 편리하고 기능도 가장 많은 aspectJ 표현식을 기반으로 사용하는 AspectJExpressionPointcut 을 사용하게 된다.
aspectJ 표현식과 사용방법은 중요해서 이후 AOP를 설명할 때 자세히 설명하겠다.
지금은 Pointcut 의 동작 방식과 전체 구조에 집중하자.



## 여러 어드바이저 함께 적용



하나의 프록시, 여러 어드바이저
스프링은 이 문제를 해결하기 위해 하나의 프록시에 여러 어드바이저를 적용할 수 있게 만들어두었다.



![image-20221113010318311](/Users/ysk/study/study_repo/inflearn-spring-core/images//image-20221113010318311.png)



```java

@Slf4j
public class AdvisorTest {


    @Test
    @DisplayName("하나의 프록시, 여러 어드바이저")
    void multiAdvisorTest2() {
        //proxy -> advisor2 -> advisor1 -> target
        DefaultPointcutAdvisor advisor2 = new DefaultPointcutAdvisor(Pointcut.TRUE,
            new Advice2());
        DefaultPointcutAdvisor advisor1 = new DefaultPointcutAdvisor(Pointcut.TRUE,
            new Advice1());
        ServiceInterface target = new ServiceImpl();
        ProxyFactory proxyFactory1 = new ProxyFactory(target);
        
        proxyFactory1.addAdvisor(advisor2);
        proxyFactory1.addAdvisor(advisor1);ServiceInterface proxy = (ServiceInterface) 	proxyFactory1.getProxy();
        
      //실행
        proxy.save();
    }


    @Slf4j
    static class Advice1 implements MethodInterceptor {
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            log.info("advice1 호출");
            return invocation.proceed();
        }
    }
    @Slf4j
    static class Advice2 implements MethodInterceptor {
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            log.info("advice2 호출");
            return invocation.proceed();
        }
    }
    
}
```



* 프록시 팩토리에 원하는 만큼 addAdvisor() 를 통해서 어드바이저를 등록하면 된다.
* `등록하는 순서대로 advisor 가 호출된다. 여기서는 advisor2 , advisor1 순서로 등록했다`



![image-20221113010957302](/Users/ysk/study/study_repo/inflearn-spring-core/images//image-20221113010957302.png)



### 정리
결과적으로 여러 프록시를 사용할 때와 비교해서 결과는 같고, 성능은 더 좋다.



> ### 중요
>
> > 스프링의 AOP를 처음 공부하거나 사용하면, AOP 적용수 만큼 프록시가 생성된다고 착각하게 된다. 
> >
> > 실제 많은 실무 개발자들도 이렇게 생각하는 것을 보았다.
> >
> > 스프링은 AOP를 적용할 때, 최적화를 진행해서 지금처럼 프록시는 하나만 만들고, 하나의 프록시에 여러
> > 어드바이저를 적용한다.
> >
> > `정리하면 하나의 target 에 여러 AOP가 동시에 적용되어도, 스프링의 AOP는 target 마다 하나의
> > 프록시만 생성한다`. 이부분을 꼭 기억해두자.





# 정리

* 프록시 팩토리 덕분에 개발자는 매우 편리하게 프록시를 생성할 수 있게 되었다.
* 추가로 어드바이저, 어드바이스, 포인트컷 이라는 개념 덕분에 어떤 부가 기능을 어디에 적용할 지 명확하게
  이해할 수 있었다.



###  남은 문제

프록시 팩토리와 어드바이저 같은 개념 덕분에 지금까지 고민했던 문제들은 해결되었다. 프록시도 깔끔하게
적용하고 포인트컷으로 어디에 부가 기능을 적용할지도 명확하게 정의할 수 있다. 원본 코드를 전혀 손대지
않고 프록시를 통해 부가 기능도 적용할 수 있었다.
그런데 아직 해결되지 않는 문제가 있다.



#### 문제1 - 너무 많은 설정
* 바로 ProxyFactoryConfigV1 , ProxyFactoryConfigV2 와 같은 설정 파일이 지나치게 많다는 점이다.
* 예를 들어서 애플리케이션에 스프링 빈이 100개가 있다면 여기에 프록시를 통해 부가 기능을 적용하려면 100개의 동적 프록시 생성 코드를 만들어야 한다! 무수히 많은 설정 파일 때문에 설정 지옥을 경험하게 될 것이다.
* 최근에는 스프링 빈을 등록하기 귀찮아서 컴포넌트 스캔까지 사용하는데, 이렇게 직접 등록하는 것도
  모자라서, 프록시를 적용하는 코드까지 빈 생성 코드에 넣어야 한다.

#### 문제2 - 컴포넌트 스캔
* 애플리케이션 V3처럼 컴포넌트 스캔을 사용하는 경우 지금까지 학습한 방법으로는 프록시 적용이 불가능하다.
* 왜냐하면 실제 객체를 컴포넌트 스캔으로 스프링 컨테이너에 스프링 빈으로 등록을 다 해버린 상태이기 때문이다.
* 지금까지 학습한 프록시를 적용하려면, 실제 객체를 스프링 컨테이너에 빈으로 등록하는 것이 아니라 ProxyFactoryConfigV1 에서 한 것 처럼, 부가 기능이 있는 프록시를 실제 객체 대신 스프링 컨테이너에 빈으로 등록해야 한다.



### 두 가지 문제를 한번에 해결하는 방법이 바로 다음에 설명할 빈 후처리기이다
