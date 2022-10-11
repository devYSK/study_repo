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

코틀린에서는 변수를 선언할 때 키워드를 무조건 선언해줘야 한다 (val, var -> 수정 가능 여부)

* val(벨, value의 줄임말) : 불변성을 가진다. 값을 바꿀 수 없다. 

* var(바, varaible의 줄임말) : 가변성을 가진다. 값을 바꿀 수 있다.



* 자바에서 long과 `final` long의 차이 = 가변인가 불변인가(read-only)의 차이 

* 코틀린에서는 타입(자료형)을 컴파일러가 자동으로 추론해준다. 의무적으로 작성하지 않아도 된다.
  * 만약 타입을 명시하고 싶다면, `변수명: 타입` 으로 명시해줄 수 있다. 
* 값을 초기화 하지 않은 변수는 타입을 명시하지 않으면 컴파일 에러가 난다.

```ko
var number1 // 컴파일 에러
var number1: Long // 컴파일 에러 아님 정상 동작. 
```

* 그러나, 초기화하지 않은 값을 사용한다면 컴파일 에러가 난다. 

### 자바코드와 코틀린 코드의 차이

* java

```java
long number1 = 10L; // 1
final long number2 = 10L; // 2

Long number3 = 1_000L; // 3
Person person = new Person("김영수"); // 4
```

* kotlin

```kotlin
var number1 = 10L;
val number2 = 10L; // final + 타입 추론
    
var number3: Long = 1_000L;
var person = Person("김영수");
```



> 모든 변수는 우선 val로 만들고, 꼭 필요한 경우 var로 변경하는 것이 좋다.



## 2. Kotlin에서의 Primitive Type

```java
long number1 = 10L; // 1
final long number2 = 10L; // 2​
Long number3 = 1_000L; // 3
Person person = new Person("김영수"); // 4
```

* `long`은 primitive type 
* Long은 refence type (wrapper) 



연산을 사용할 때는, Reference Type 대신 Primitive Type을 사용해야 한다.



그러나, 코틀린에서는 상관 없다.

숫자랑, 불리언, 문자에 대해서는 겉으로는 래퍼클래스를 사용하더라도, 내부적으로는 Primitive Type을 사용한다. 

Long 타입을 사용하더라도, 상황에 따라서 필요할 때 long 타입으로 변환해준다.

* 인텔리제이 tolls -> show Kotlin byteCode -> Decompile 클릭
* long 타입으로 변환되어있는걸 확인 가능.  

* 코틀린 공식 문서

> Some types can have a special internal representation - for  
> example, numbers, characters and booleans - can be  
> represented as primitive values at runtime - but to the user they  
> look like ordinary classes.  

> 숫자, 문자, 불리언과 같은 몇몇 타입은 내부적으로 특별한 표현을 갖는다.  
> 이 타입들은 실행시에 Primitive Value로 표현되지만,  
> 코드에서는 평범한 클래스 처럼 보인다.

즉, 프로그래머가 boxing / unboxing을고려하지 않아도 되도록 Kotlin이 알아서 처리 해준다. 

* 내부적으로 Primitive type으로 바꿔서 적절히 처리해준다. 

  

## 3. Kotlin에서의 nullable 변수

코틀린에서 null이 변수에 들어갈 수 있다면 `Type ?` 를 사용해야 한다.

* 기본적으로 모든 변수에 null이 들어갈 수 없게끔 설계 되어 있다.

```kotlin
var number: Long? = 1_000L
number = null
```

* `?` 키워드가 중요.



## 4. Kotlin에서의 객체 인스턴스화

* Java 인스턴스화

```java
Person person = new Person("김영수");
```



* Kotlin 인스턴스화

```kotlin
val person = Person("김영수");
```



> 코틀린에서는 인스턴스화 할 때, `new`를 붙이지 않는다.