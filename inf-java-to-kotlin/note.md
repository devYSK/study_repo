# 인프런 java-to-kotlin  최태현님 강의



* 코틀린은 자바와 100% 호환 가능하면서도 현대적이고 간결하며 안전한 언어를 사용하기 위해서 탄생 
* 코틀린의 철학 : 현대적인(Modern), 간결한(consise), 안전한(safe) 프로그래밍 언어
* JVM 위에서 동작한다. 자바와 코틀린 말고도 스칼라(scala),그루비 (groovy) 등이 있다
* 코틀린은 멀티 플랫폼 언어로 안드로이드, 아이오에스, 서버, 웹, 임베디드 IoT 등 다양한 플랫폼에서 사용되는것을 목표로 한다. 
* 코틀린은 정적 타입 언어. 프로그램 구성 요소의 타입을 컴파일 시점에 알 수 있고, 필드나 메소드를 사용할 때 컴파일러가 타입을 검증해준다.
* 코틀린은 OOP와 FP(함수형 프로그래밍)을 조화롭게 지원하고 있다. 
* 코틀린은 무료 오픈소스로 아파치 2.0 라이센스를 가지고있다. 
  * 아파치 2.0 라이센스 : 소스코드 공개 의무가 존재하지 않으며 상업적 이용에 제한을 두지 않고 있다. 
* 코틀린의 파일 확장자는 `.kt` 이다
* 코틀린은 세미콜론을(;) 붙이지 않아도 된다.
* 주석 처리 방법은 Java와 동일하다. 
* 코틀린에서는 별도 지시어(public, protected, private)을 붙이지 않으면 모두 public 이다.
* 출력을 할 때에 System.out.println() 대신 println()만 사용하면 된다. 
* 함수 작성시 `fun` 키워드 사용 
* 자바에서는 `타입 변수명(int number)` 를 사용하지만 코틀린에서는 `변수명 : 타입(val number: Int)`를 사용한다.
* 코틀린에서는 변수나 함수 클래스 모두 파일 최상단에 선언할 수 있다. (클래스 내부가 아니여도 된다.)





# Lect 01. 코틀린에서 변수를 다루는 방법



1. 변수 선언 키워드 - var과 val의 차이점
2. Kotlin에서의 Primitive Type
3. Kotlin에서의 nullable 변수
4. Kotlin에서의 객체 인스턴스화



## 1. 변수 선언 키워드 - var과 val의 차이점

코틀린에서는 변수를 선언할 때 키워드를 무조건 선언해줘야 한다 (val, var)

* val(벨, value의 줄임말)

* var(바, varaible의 줄임말)



* 자바에서 long과 `final` long의 차이 = 가변인가 불변인가(read-only)의 차이 



### 자바코드와 코틀린 코드의 차이

```java
long number1 = 10L; // 1
final long number2 = 10L; // 2

Long number3 = 1_000L; // 3
Person person = new Person("김영수"); // 4
```



* kotlin





## 2. Kotlin에서의 Primitive Type

## 3. Kotlin에서의 nullable 변수

## 4. Kotlin에서의 객체 인스턴스화