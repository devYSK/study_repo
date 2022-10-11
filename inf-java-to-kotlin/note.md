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

# Lec 02. 코틀린에서 null을 다루는 방법



1. Kotlin에서의 null 체크
2. Safe Call과 Elvis 연산자
3. 널 아님 단언!!
4. 플랫폼 타입



## 1. Kotlin에서의 null 체크

```java
public booleaan startsWithA(String str) {
  return str.startsWith("A");
}
```

* 다음 코드는 안전한 코드가 아니다. 
* `str`에 `null`이 들어오면 NPE(NullPointerException)가 발생

* 다음과 같이 검증하는 작업 필요 

```java
// 1. str이 null이면 예외 발생 
public boolean startsWithA1(String str) {
  if (str == null) {
    throw new IllegalArgumentException("null 들어왔습니다.");
  }
  
  return str.startsWith("A");
}

// 2. str이 null 일 경우 null 반환 (boolean은 null을 return 할 수 없으므로, Boolean 타입으로)
public Boolean startsWithA2(String str) {
  if (str == null) {
    return null;
  }
  return str.startsWith("A");
}

// 3. str이 null일경우 false 반환
public booleaan startsWithA3(String str) {
  if (str == null) {
    return false;
  }
  return str.startsWith("A");
}
```



그러나 코틀린에서는

```kotlin
// 자바 1번 예제
fun startsWithA1(str: String?) : Boolean { 
  if (str == null) {
    throw IllegalArgumentException("null이 들어왔습니다.")
  }
  
  return str.startsWith("A")
}

// 자바 2번 예제
fun startsWithA3(str: String?) : Boolean? { // null이 들어갈 수 있는 불리언 타입 
  if (str == null) {
    return null
  }
  
  return str.startsWith("A")
}

// 자바 3번 예제
fun startsWithA3(str: String?) : Boolean {
  if (str == null) {
    return false;
  }
  
  return str.startsWith("A")
}
```



### 코틀린에서의 null 체크

```kotlin
fun startsWithA(str: String) : Boolean {
  return str.startsWith("A")
}
```

* 코틀린에서는 null이 가능한 타입을 완전히 다르게 취급한다. 

* 코틀린에서 `?` 을 사용하게 되면 반드시 널 체크를 해야 한다. 
  * 널 체크를 안하면 컴파일 에러가 발생한다.
  * 컴파일러가 자동으로 추측해준다.

> 코틀린에서는 null이 가능한 타입을 완전히 다르게 취급한다.



## 2. Safe Call과 Elvis 연산자



### Safe Call

```kotlin
val str: String? = "ABC"
str.length // 불가능. 왜? null 체크 안했으니까.
str?.length // 가능. 
```

* Safe Call : 안전한 호출 
  * 변수명? 뜻은, 변수가 null이 아니면 .메서드나 .프로퍼티 기능을 실행 시키고, null이면 실행하지 않는다. (그대로 null 유지) 



### Elvis 연산자

```kotlin
val str: String? = "ABC"
str?.length ?: 0
```

* Elvis 연산자 : `?:`
  * 앞의 연산 결과가 null이면 : 뒤의 값을 사용
  * 예제에서는 0 을 사용한다. 
* 앞의 연산 결과가 널이 아니면 length 호출. 널이면 0을 대입하거나 리턴 

```kotlin
val str: String? = null
println(str?.length ?: 0) // print 3

println(str?.length ?: 0) // print 0 . str이 null 이기 때문에
```

* 0대신 어떤 값도 리턴할 수 있다. (반환 자료형에 맞게.)  



<br>

* 다음처럼 실행 구문도 가능하다.

```kotlin
println(str?.length ?: throw IllegalArgumentException("null 임"))
```

* IllegalArgumentException 을 throw 한다.



## 3. 널 아님 단언!!

nullable type이지만, 아무리 생각해도 null이 될 수 없는 경우에 사용 -> 단언하는것

```kotlin
fun startsWith(str: String?) : Boolean {
    return str!!.startsWith("A")
}
```

* `!!` 연산자를 사용하면, null 체크를 안해도 컴파일 에러가 나지 않는다. 
* 다만 null이 들어오면, Runtime에서 NullPointerException이 발생한다.  



## 4. 플랫폼 타입

코틀린에서 자바 코드를 가져다 사용할 때 어떻게 처리되나

```java
public class Person {

  private final String name;

  public Person(String name) {
    this.name = name;
  }

  @Nullable // 널 일 수도 있다는 어노테이션 
  public String getName() {
    return name;
  }

}
```



만약 다음과 같은 코틀린함수가 있다면

```kotlin
fun startsWithA(str: String): Boolean { // null을 허용하지 않는다.
  return str.startsWith("A")
}

fun main() {
    val person = Person("영수르");
    startsWith(person.name) // 컴파일 에러 -> Type Mismatch
}
```



컴파일 에러가 되면서 함수를 실행할 수 없다. 

* @Nullable 어노테이션 때문이다. 
* null 일수도 있기 때문에 문법에 맞지 않는것.



반대로, @NotNull 어노테이션을 쓰면 문제가 없어진다.  

```java
public class Person {
  ...
  @NotNull
  public String getName() {
    return name;
  }  
}
```



이런식으로 코틀린에서 자바 코드를 가져다 쓸 때는 ₩null에 대한 어노테이션`으로 코틀린이 이해할 수 있다. 

- javax.annotation 패키지
- android.support.annotation 패키지
- org.jetbrains.annotation 패키지
- 의 어노테이션이다. 

### @Nullable이 없다면?!!

* Kotlin에서는 이 값이 nullable인지 non-nullable인지 알 수가 없다

#### 플랫폼 타입
* 코틀린이 null 관련 정보를 알 수 없는 타입
* Rumtime 시 Exception이 날 수 있다



즉, 코틀린이 널 관련 정보를 알 수 없는 타입을 `플랫폼 타입` 이라고 하며, Runtime시에 Exception이 일어날 수 있다.

* ex) 위 예제에서 getName()에 @Nullable이나 @NotNull 어노테이션이 없으면, 런타임에 null이 들어오는 순간 에러 발생

코틀린에서 자바 코드를 사용할 때는 널 관련 정보를 꼼꼼히 작성해주거나,   
외부 라이브러리를 쓸 때는 라이브러리를 열어서 null이 들어갈 수 있는지 없는지 확인하는 것이 좋다.   



* 코틀린에서 null이 들어갈 수 있는 타입은 완전히 다르게 간주된다

- 한 번 null 검사를 하면 non-null임을 컴파일러가 알 수 있다

- 코틀린에서 null이 들어갈 수 있는 타입은 완전히 다르게 간주된다
  - 한 번 null 검사를 하면 non-null임을 컴파일러가 알 수 있다

- null이 아닌 경우에만 호출되는 Safe Call (?.) 이 있다
- null인 경우에만 호출되는 Elvis 연산자 (?:) 가 있다

* null이 절대 아닐때 사용할 수 있는 널 아님 단언 (!!) 이 있다
* Kotlin에서 Java 코드를 사용할 때 플랫폼 타입 사용에 유의해야한다
  - Java 코드를 읽으며 널 가능성 확인 / Kotlin으로 wrapping



