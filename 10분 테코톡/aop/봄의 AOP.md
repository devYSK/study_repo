# AOP

우아한 테크코스의 10분 테코톡을 정리한 글입니다





AOP란 (Aspect Oriented Programming) - 관점 지향 프로그래밍

횡단 관심사(Cross-Cutting Concern)의 분리를 허용함으로써 모듈성을 증가시키는 것이 목적인 프로그래밍 패러다임

여러 객체에 공통으로 적용할 수 있는 기능을 분리해서 개발자는 반복 작업을 줄이고 핵심 기능 개발에만 집중할 수 있다.

* 스프링에만 존재하는 개념은 아니다. 

- AOP는 OOP와 같은 패러다임이다.
- AOP는 OOP를 프로그램구조에 대한 다른 생각의 방식을 제공함으로써 보완하고있다.
- 각 언어마다 AOP의 구현체가 있다. 자바는 AspectJ를 사용한다.



서비스에서 필요한 내용은 비즈니스 로직이라고 불리는 핵심기능만 수행하면 된다.

그이외에 시간을 재거나 권한을 체크한다거나 transaction을 거는 것은 모두 **인프라 로직**이라 불린다.

- 인프라 로직 : 성능검사, 로깅, 성능검사, 권한체크, 트랜잭션
  - 인프라 로직은 애플리케이션 전 영역에서 나타날 수 있다.
  - 그러다보니 중복코드를 만들어낼 가능성 때문에 유지 보수가 힘들어진다.
  - 비지니스 로직과 함께 있으면 비지니스 로직을 이해하기 어려워진다.
  - 횡단으로 나타나기에 횡단 관심사라고도 부른다.



클래스를 변경하는 이유가, 부가기능 추가라면 SRP를 위반하는 행동이라고 할 수 있다.

##  AOP 용어 정리

Spring AOP에서만 사용되는 것이 아닌, AOP 라는 개념에서 사용되는 용어들이다

<img src="https://blog.kakaocdn.net/dn/bRXNPM/btscPcBZDmC/4eBHQSCVk1SWOQb8P8KCJK/img.png" width = 800 height = 430>

- **Target Object**  : 부가 기능을 부여할 대상
- **Aspect** : AOP의 기본 모듈.
  - 그 자체로 애플리케이션의 핵심 기능을 담고있진 않지만, 애플리케이션을 구성하는 중요한 한 가지 요소.
  - 부가될 기능을 정의한 Advice와 Advice를 어디에 적용할지 결정하는 Pointcut을 함께 가진다.
- **Advice** : 타깃에게 제공할 부가 기능을 담은 모듈.
  - 타깃이 필요 없는 순수한 부가 기능.
  - Aspect가 무엇인지 언제 할지를 정의하고 있다.
  - 어떤 부가기능? before, afterRunning, afterThrowing, After, Around
- **Join point** : 프로그램의 실행 내부에서 Advice가 적용될 수 있는 위치
  - 어디에 적용할 것인가? 
  - 메서드, 필드, 객체, 생성자 등 여러 상황에서 부가기능을 사용할 수 있으나 Spring AOP에는 메서드가 실행될 때만으로 한정하고 있다.
- **Pointcut** : Advice에 적용할 JoinPoint를 선별하는 작업 또는 그 기능을 정의한 모듈

## Spring에서의 AOP 용어

- **Target**  : 스프링 AOP는 런타임 프록시로 구현되므로 타깃은 항상 프록시이다.
- **Aspect** : 트랜잭션 관리가 가장 좋은 예시이다. @Aspect를 사용해서 구현
- **Advice** : Around, Before, After, Throwing 등 다양한 어드바이스가 존재한다.
- **Join point** : 스프링 AOP는 프록시 방식을 사용하므로 조인포인트는 항상 메서드 실행 지점이다.
- **Pointcut** : 스프링 AOP의 조인포인트는 메서드 실행이므로. 스프링의 포인트컷은 메서드를 선정하는 기능을 한다.

## Spring AOP와 AspectJ의 차이점

|            | Spring AOP                                | AspectJ                                                      |
| ---------- | ----------------------------------------- | ------------------------------------------------------------ |
| 목표       | 간단한 AOP기능 제공                       | 완벽한 AOP 기능 제공                                         |
| join Point | 메서드 레벨만 지원                        | 생성자, 필드, 메서드 등 다양한 지원                          |
| weaving    | 런타임시에만 가능                         | 런타임은 제공하지 않음<br />. compile-time, post-compile, load-time제공 |
| 대상       | Spring Container가 관리하는 Bean에만 가능 | 모든 Java Object에 가능                                      |

## 예시

```java
public interface Calculator {
  long factorial (long num);
}
```

팩토리얼 연산을 하는 계산기 인터페이스

```java
public class BasicCalculator implements Calculator {

  @Override
  public long factorial (final long num) {
    long result = 1;

	  for (long i = 1; i <= num; it+) {
  	  result *= i;
  	}
  
  	return result;
  }  
}
```

반복문으로 factorial 연산을 진행하는 구현체 



1. 요구사항 등장 - 팩토리얼 연산의 실행시간 측정

2. 다른 요구사항 등장 - 반복문 말고로도 재귀로도 구현하는 구현체
3. 다른 요구사항 등장 - 밀리초 말고 나노초로 반환시간 측정 



만약 요구사항을 구현할라면, 기존의 코드들이 수정되어야 하고 코드의 중복이 발생한다. 



**이를 해결하기위해 프록시란 디자인 패턴  사용**

### 프록시

<img src="https://blog.kakaocdn.net/dn/TYhfh/btscQbJkI1Z/4xlFXKEOHk6iGOa45KQJGk/img.png" width = 600 height = 400>

- 자신이 클라이언트가 사룡하려고 하는 실제 대상인 것처럼 위장해서 클라이언트의 요청을 받아주는 것 (대리인, 대리자)
- 사용 목적에 따라
  - 클라이언트가 타깃에 접근하는 방법을 제어하기 위해서 -> 프록시 패턴
  - 타깃에 부가적인 기능을 부여해주기 위해서 -> 데코레이터 패턴



프록시를 이용하여 구현

```java
package aop;

public class ExecutionTimeCalculator implements Calculator {

  private Calculator delegate;

  public ExecutionTimeCalculator (final Calculator delegate) {
    this.delegate = delegate;
  }
   
  @Override
  public long factorial(final long num) {
    long start = System.nanoTime();
    long result = delegate.factorial(num); // 이부분이 핵심 
    long end = System.nanoTime();
    system.out.printf("Sg의 fatorial(8d) 실행 시간 -> %d \n", 
                       delegate.getClass().getSimpleName(), num,(end - start));
    return result;
  }
}
```

같은 Calculator 인터페이스를 구현하고, 내부적으로 다른 Calculator 인터페이스를 구현하는 구현체를 참조하여(컴포지트)

기존 코드를 변경하지 않고 실행 시간측정이라는 부가적인 기능 추가

<img src="https://blog.kakaocdn.net/dn/OaiHg/btscRqMFzPg/WkvWebb6sGPWwn8mjrpPYk/img.png" width = 500 height = 350>

즉, 

<img src="https://blog.kakaocdn.net/dn/bBsU0V/btscS6mrMTb/FS0e5hYNXYyZEhXqbWIqvk/img.png" width = 750 height = 350>

부가 기능은 횡으로 공통부분만 추출했기 때문에, 횡단 관심사 라고도 부른다. 

- 핵심 기능과 부가 기능의 관점을 분리하여 부가 기능에서 바라보는 공통된 부분을 추출하는 것이 AOP의 개념이다
- 핵심 기능에 공통 기능을 삽입하는 것
- 핵심 기능의 코드를 수정하지 않으면서 공통 기능의 구현을 추가하는 것

# AOP를 구현하는 방법

1. **컴파일 시점**에 코드에 공통 기능 삽입
   * java를 calss로 컴파일 할때 해당하는 Asepct를 끼워넣는다.
2. **클래스 로딩 시점**에 바이트 코드에 공통 기능 삽입
   * 컴파일 완료되고 메모리상에 올릴때 그때 AOP를 적용하는 방식이다.
3. **런타임 시점**에 프록시 객체를 생성하여 공통 기능 삽입
   * 스프링 AOP에서 사용하는 방식, 특정 타겟 Class를 부가기능을 제공하는 프록시로 감싸서 실행하는 방식이다.



**컴파일과 클래스 로딩 시점에는**

AOP 프레임워크인 AspectJ가 제공하는 컴파일러나 클래스 로더 조작기같은

새로운것을 사용함으로써 유연한 AOP를 구현할 수 있지만 부가적인 의존성을 추가해야 한다는 단점이 있다.

<img src="https://blog.kakaocdn.net/dn/dfyh7V/btscRrSiaUY/Gm89NijG6EHjgsFBOnCfaK/img.png" width = 400 height = 400>



**런타임 시점**은 이미 자바언어가 시작된 시점으로 자바언어에서 제공하는 기능인 프록시를 사용하여 공통 기능을 구현하면 된다.

 

## 3가지 방법중 스프링은 어떻게 AOP를 구현하나?

스프링에서는 런타임시점에 프록시를 생성하여 공통기능을 삽입하는 방법을 사용한다. 

* 컴파일러나 클래스 로더 조작기를 설정하지 않아도 된다.

- 프록시는 메서드 오버라이딩 개념으로 동작하기 때문에. 스프링 AOP는 메서드 실행 시점에만 AOP를 적용할 수 있다.
- 스프링 AOP는 스프링 컨테이너가 관리할 수 있는 빈에만 AOP를 적용할 수 있다.
- Aspect J를 직접 사용하는 것이 아니라. Aspect J의 문접을 차용하고 프록시 방식의 AOP를 적용
- 스프링에서는 
  - IoC/DI 컨테이너, 
  - Dynamic Proxy, 
  - 데코레이터 패턴/프록시 패턴, 
  - 자동 프록시 생성 기법, 
  - 빈 오브젝트 후처리 조작 기법 등 다양한 기술을 조합하여 스프링 AOP를 지원한다.



### Spring에서의 AOP 사용법

1. 먼저 Bean으로 등록한다. 

```java
@Component // 빈으로 등록 
public class BasicCalculator implements Calculator {

  @Override
  public long factorial (final long num) {
    long result = 1;

	  for (long i = 1; i <= num; it+) {
  	  result *= i;
  	}
  
  	return result;
  }  
}
```

2. Aspect를 클래스와 함께 정의한다.

```java
@Component
@Aspect
public class ExecutionTimeAspect {

	@Pointcut("execution (* fact* (..))")
	private void publicTarget() {	}

	@Around("publicTarget ( )")
	public Object measure(ProceedingJoinPoint joinPoint) throws Throwable {
		long start = System.nanoTime();
		try {
			Object result = joinPoint.proceed();
			return result;
		} finally {
			long finish = System.nanoTime();
			Signature sig = joinPoint.getSignature();
			System.out.printf("gs. 8s (8s) 실행 시간 : %d \n"
				joinPoint.getTarget().getClass().getSimpleName(), sig.getName(),
				Arrays.toString(joinPoint.getArgs()), (finish - start)
			);
		}
	}
}
```

* @Aspect 어노테이션을 붙이고 메서드 이름이 fact~로 시작하는 경우라는 Point Cut을 정의

* Aspect도 마찬가지로 Bean이여야 한다.



### Spring에서 구현 가능한 Advice 종류

- Around Advice(@Around) : 메서드 실행 전, 후 또는 익셉션 발생 시점
- Before Advice(@Before) : 메서드 호출 전
- After Returning Advice(@AfterReturning) : 메서드가 익셉션 없이 실행된 이후
- After Throwing Advice : 메서드를 실행하는 도중 익셉션이 발생한 경우
- After Advice : 익셉션 발생 여부 상관 없이 메서드 실행 후



## Spring AOP 주의사항

Target Object가 TargetObject에 있는 메서드를 실행할 때는 AOP가 적용되지 않는다

* 타겟 오브젝트 내부에서 실행한 메서드 이기 때문에 이때는 이제 AOP가 적용이 안된 타겟 오브젝트의 메서드를 실행한다.
* 하지만 이것도 방법이 있다. 



### 참조

* [10분 테코톡 봄의 AOP와 Spring AOP](https://www.youtube.com/watch?v=hjDSKhyYK14)
* [10분 테코톡 제이의 Spring AOP](https://www.youtube.com/watch?v=Hm0w_9ngDpM)