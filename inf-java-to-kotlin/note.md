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



# Lec 03 코틀린에서 Type을 다루는 방법



1. 기본 타입
2. 타입 캐스팅
3. Kotlin의 3가지 특이한 타입
4. String Interpolation, String indexing





## 1. 기본 타입

Byte
Short
Int
Long
Float
Double
부호 없는 정수들



* 코틀린에서는 선언된 기본 값을 보고 타입을 추론한다. 

```kotlin
val number1 = 3 // int
val number2 = 3L // Long
val number3 = 3.0f // Float
val number4 = 3.0 // Double
```



* Java에서는 기본 타입간의 변환은 암시적으로 이루어 질 수 있다.
* 그러나 코틀린에서는 기본타입간의 변환은 명시적으로 이루어져야 한다.

### in java

```java
int number1 = 4;
long number2 = number1; // 암시적으로 변경 되었다. 
```

### in kotlin

```kotlin
val number1: Int = 4
val number2: Long = number1.toLong() // 명시적으로 to변환타입() 사용
```

* 코틀린에서는 to변환타입()을 사용해야 한다.

* to기본타입() 함수를 지원한다. 

### 만약 변수가 nullable이라면 적절한 처리가 필요하다.

```kotlin
val number1: Int? = 3
val number2: Long = number1?.toLong ?: 0
```

* number1이 널이라면 0 반환 

## 2. 타입 캐스팅



코틀린에서의 타입 캐스팅은 `is` ~ `as` 를 사용한다 

> 코틀린에서 자바의 instanceof는 `is` 로 사용한다. 
>
> 코틀린에서 자바의 (타입)은 as 타입 으로 사용한다.

### in java

```java
public static void printAgeIfPerson(Object obj) {
  if (obj instanceof Person) {
    Person person = (Person) obj;
  }
}
```

### in kotlin

```kotlin
fun printAgeIfPerson(obj : Any) {
  if (obj is Person) {
    val person = obj as Person
  }
}
```

* obj을 Person 타입으로 간주한다. 

* 또한 스마트 캐스트로 `as 타입` 을 생략할 수 있다.



### instanceof의 반대는? (!instanceOf)

```kotlin
if (obj !is Person) {
  ...
}
```

* `is` 앞에 `!` 을 붙여서 `!is` 를 사용한다.



### 만약 obj가 nullable 이라면? 

```kotlin
fun printAgeIfPerson(obj: Any?) {
  val person = obj as? Person
}
```

* `as` 뒤에 `?` 를 붙여 `as?` 를 사용한다.
* null이라면 Safe Call 처럼 전체가 null이 되어서 바로 사용 못하고 `변수명?.메서드` 를 사용한다.
  * person?.age



## 3. Kotlin의 3가지 특이한 타입

* Any
* Unit
* Nothing



### Any

* Java의 Object 역할. (모든 객체의 최상위 타입)

- 모든 Primitive Type의 최상의 타입도 Any이다.
- Any 자체로는 null을 포함할 수 없어 null을 포함하고 싶다면, Any?로 표현.
- Any 에 equals / hashCode / toString 존재



### Unit

- Unit은 Java의 void와 동일한 역할.
- (살짝 어려운 내용) void와 다르게 Unit은 그 자체로 타입 인자로
  사용 가능하다.
  - void와 다른점은,( 소문자 보이드, 대문자는 클래스타입) Unit 자체만으로 리턴타입, 또는 타입이 될 수 있다.   
- 함수형 프로그래밍에서 Unit 은 단 하나의 인스턴스만 갖는 타입을
  의미. `즉, 코틀린의 Unit은 실제 존재하는 타입이라는 것을 표현`



### Nothing

* Nothing은 함수가 정상적으로 끝나지 않았다는 사실을 표현하는역할

- 무조건 예외를 반환하는 함수 / 무한 루프 함수 등

```kotlin
fun fail(message: String): Nothing {
  throw IllegalArgumentException(message)
}
```

* 이 함수는 무조건 예외가 나온다. 
* 예외를 반환하는 함수나 무한루프 함수처럼  Nothing을 반환타입으로 지정한다.



## 4. String Interpolation, String indexing



### in java

```java
Person person = new Person("김영수", 28);
String log = String.format("이름 %s, 나이 %s세", person.getName(), person.getAge());
```

### in kotlin

```kotlin
val person = Person("김영수", 28)
val log = "이름 ${person.name}, 나이 ${person.age}"
```



* `${변수}` 를 사용하면 값이 대입된다. 
* `$변수` 도 사용 가능하다. -> 중괄호 생략. 
  * 즉 String.format을 $로 지원

* 하지만, 변수 이름만 사용하더라도 ${변수}를 사용하는 것이

  1) 가독성

  2. 일괄 변환

  3. 정규식 활용

에서 좋다. 



* 코틀린에서는 여러 줄에 걸친 문자열을 작성해야 할 때 `""" `를 사용하면 좀더 편하게 사용할 수 있다.
  * 자바에서는 append, StringBuilder와 함께 사용해야함

```kotlin
val number1: Int = 3

println(
"""
       ABCD
       EFG
       ${number1}
""".trimIndent())
```

* 공백이 제거되고 개행문자(\n) 포함해서 출력된다.

```
ABCD
EFG
3
```



### 코틀린에서의 특정 문자 가져오기 (String indexing)



### in java

```java
String str = "ABCDE";
char ch = str.charAt(1);
```



### in kotlin

```kotlin
val str = "ABCDE"
val ch = str[1]
```

* 자바의 배열처럼 `[]` 을 사용할 수 있다. 



# Lec04. 코틀린에서 연산자를 다루는 방법

1. 단항 연산자 / 산술 연산자
2. 비교 연산자와 동등성, 동일성
3. 논리 연산자 / 코틀린에 있는 특이한 연산자
4. 연산자 오버로딩





## 1. 단항 연산자 / 산술 연산자

* Java, Kotlin 완전 동일하다.



### 단항 연산자

* ++
* --

### 산술 연산자

* +
* -
* *
* /
* %


### 산술대입 연산자

* +=
* -=
* *=
* /=
* %=



## 2. 비교 연산자와 동등성, 동일성

* `<`, `>`,  `>=`, `<=`  는 java, kotlin 사용법을 동일하다.

* 코틀린은 Java와 다르게 객체를 비교할 때 비교 연산자를 사용하면 자동으로  `compareTo`를 호출해준다. 
  * 객체를 비교할 때 클래스에 Comparable을 구현하여 compateTo 메소드를 구현해야한다. 



### 동등성과 동일성

* 동등성(Equality) : 두 객체의 값이 같은가?! 
* 동일성(Identity) : 완전히 동일한 객체인가? = 주소가 같은가? 

<br>

* Java에서는 동일성에 `==` 를 사용 동등성에 equals 직접 호출
* Kotlin에서는 동일성에서 `===` 를 사용, 동등성에 == 를 호출 (==를 호출하면 간접적으로 equlas를 호출해준다. )





## 3. 논리 연산자 / 코틀린에 있는 특이한 연산자

* 논리연산자 
  * && , ||, !   

<br>

Java와 완전히 동일하며 `Lazy 연산` 을 수행한다. 

* Lazy연산
  * fun1은 true를 반환, fun2는 false를 반환한다고 가정
  * if (fun1() || fun2()) 로 비교
  * 두 논리연산을 했을 때, 앞에 연산이 true라면 뒤의 연산은 실행하지 않는 연산 

### 특이한 연산자

* in / !in
- 컬렉션이나 범위에 포함되어 있다, 포함되어 있지 않다
* a..b
  * a부터 b 까지의 범위 객체를 생성한다
* a[i]
  * a에서 특정 Index i로 값을 가져온다

- a[i] = b
  - a의 특정 index i에 b를 넣는다

## 4. 연산자 오버로딩



코틀린에서는 객체마다 연산자를 직접 정의할 수 있다.



### in java

```java

public class JavaMoney implements Comparable<JavaMoney> {

  private final long amount;

  public JavaMoney(long amount) {
    this.amount = amount;
  }

  public JavaMoney plus(JavaMoney other) {
    return new JavaMoney(this.amount + other.amount);
  }

  // toString이 구현되었다고 가정 
}

public static void main(String[] args) {
  JavaMoney money1 = new JavaMoney(1000L);
  JavaMoney money2 = new JavaMoney(2000L);
  
  System.out.println(money1.plus(money2));
}
```

* 자바에서는 이런식으로 메소드를 호출해야 연산이 된다.  



### in kotlin

```kotlin
data class Money (
    val amount: Long
) {
    operator fun plus(other: Money) : Money {
        return Money(this.amount  + other.amount)
    }
}

fun main() {
    val money1 = Money(1000L)
    val money2 = Money(2000L)

    println(money1 + money2);
}
```

* 연산자 오버로딩이 가능하다. 
* 직접 플러스, 마이너스, 단한 연산, 비교 연산 등을 직접  정의할 수 있다.



### 정리

- 단항연산자, 산술연산자, 산술대입연산자 Java와 똑같다

- 비교 연산자 사용법도 Java와 똑같다
- 단, 객체끼리도 자동 호출되는 compareTo를 이용해
비교 연산자를 사용할 수 있다.
- in, !in / a..b / a[i] / a[i] = b 와 같이 코틀린에서 새로 생긴
연산자도 있다.
- 객체끼리의 연산자를 직접 정의할 수 있다



# Lec05. 코틀린에서 조건문을 다루는 방법



1. if문
2. Expression과 Statement
3. switch와 when



## 1. if문

```kotlin
fun validateScoreIsNotNegative(score: Int) {
	if (score < 0) {
		throw IllegalArgumentException("${score}")
	}
} 
```

* 메서드 뒤에 반환형인 void (Unit) 이 생략되었다. 



## 2. Expression과 Statement



```kotlin
fun getPassOrFail(score: Int): String {
  if (score >= 50) {
    return "P"
  } else {
    return "F"
  }
}
```

자바와 코틀린의 if-else의 차이점은,  

자바에서는 Statement 이지만,  
코틀린에서는  Expression 이다.

 

* Statement: 프로그램의 문장 전체, 하나의 값으로 도출되지 않는다. 

* Expression : 반드시 하나의 값으로 도출되는 문장

![image-20221012104143303](/Users/ysk/study/study_repo/inf-java-to-kotlin/image-20221012104143303.png)

* Expression은   StateMent에 속해있고, Statement중 하나의 값으로 도출되는 문장들이 Expression이다.   



### 예제

```java
int score = 30 + 40;
```

* 30 + 40은 70이라는 하나의 결과가 나온다. -> Expression이면서 Statement



```java
String grade = if (score >= 50) {
  "P";
} else {
  "F";
}
```

* 이 문법은 에러이다. if문을 하나의 값으로 취급하지 않는다. -> Statement



```java
String rade = score >= 50 ? "P" : "F"
```

* 3항 연산자는 하나의 값으로 취급하므로 에러가 없다. -> Expression이면서 statement



> 그러나 코틀린은 다르다. 

```kotlin
fun getPassOrFail(score: Int): String {
  return if (score >= 50) {
    "P"
  } else {
    "F"
  }
}
```

* 다음과 같은 블록을 return 할 수 있다 -> Expression ->  식 자체를 하나의 값으로 취급한다
* 그러나 코틀린에서는 if-else를 expression으로 사용할 수 있기 때문에 `3항 연산자가 없다.` 

<br>

또한 어떤 값이 특정 범위에 포함되어 있는지 다음과 같이 변환할 수 있다.

in java

```java
if (0 <= score && score <= 100)
```

in kotlin

```kotlin
if (score in 0..100)
```



## 3. switch와 when



```kotlin
fun getGradeWithSwitch(score: Int): String {
  return when (score / 10) {
    9 -> "A"
    8 -> "B"
    7 -> "C"
    else -> "D"
  }
}
```

* switch 없이 when으로 가능하다.
* case 대신 바로 경우를 정하고, 화살표(->)로 리턴값을 정한다.

```kotlin
fun getGradeWithSwitch(score: Int): String {
  return when (score) {
    in 90..99 -> "A"
    in 80..89 -> "B"
    in 70..79 -> "C"
    else -> "D"
  }
}
```

* 위와 같이 다양한 조건으로 분기를 가질수도 있다. 

```
when (값) {
	조건부 -> 실행구문
  조건부 -> 실행구문
  else -> 나머지 실행 구문 
}
```

* 조건부에는 어떠한 expression이라도 들어갈 수 있다. (ex. is Type)

```kotlin
fun startsWith(obj: Any) : Boolean {
    return when(obj) {
        is String -> obj.startsWith("A")
        else -> false
    }
}
```

<br>

```kotlin
fun gudgeNumber(number: Int) {
  when (number) {
    1, 0, -1 -> println("어디서 많이 본 숫자입니다.")
    else -> println("1, 0, -1이 아닙니다.")
  }
}
```



- if / if – else / if - else if – else 모두 Java와 문법이 동일하다.
- 단 Kotlin에서는 Expression으로 취급된다.
  - 때문에 Kotlin에서는 삼항 연산자가 없다
- Java의 switch는 Kotlin에서 when으로 대체되었고, when은 더 강력한 기능을 갖는다.



# Lec06.코틀린에서 반복문을 다루는 방법



1. for-each문
2. 전통적인 for문
3. Progression과 Range
4. while 문



## 1. for-each문

리스트를 in절로 접근할 수 있다.

```kotlin
val numbers = listOf(1L, 2L, 3L)

for (number in numbers) {
  println(number)
}
```

* listOf 함수는 컬렉션을 만든다. 
* `in ~` 는 iterable이 구현된 타입이라면 모두 들어갈 수 있다. 

## 2. 전통적인 for문

* 

```kotlin
for (i in 1..3) {
  println(i)
}
```

* 1..3 : 1부터 3까지 접근 



### 2...n 씩 증가 (step n)

```kotlin
for (i in 1..5 step 2) {
  println(i)
}
```

* step 키워드 사용. 



### -- (감소연산) 

```kotlin
for (i in 3 downTo 1) {
  print(i)
}
```

* i가 1씩 `-` 줄이면서 내려간다   

* downTo 2로 설정하면 2씩 줄인다.



## 3. Progression과 Range

동작 원리

* .. 연산자 : 범위를 만들어 내는 연산자
  * Progression(등차수열) 클래스를 이용
* 1..3 : 1부터 3의 범위

* `Range` 라는 클래스 

  * IntRange.. 등

  * ```kotlin
    public operator fun rangeTo(other: Int): IntRange
    ```

* Range라는 클래스는 Progression(등차수열) 을 상속 받고있다.

등차수열 - (시작 값..끝 값 step (or downTo) 공차)
1) 시작 값
2) 끝 값
3) 공차



* downTo, step도 함수이다. (중위 호출 함수)

## 4. while 문



java와 완전히 동일

```kotlin
var i = 1 // 가변 변수
while (i <= 3) {
  println(i)
  i++
}
```



# Lec07. 코틀린에서 예외를 다루는 방법

1. try catch finally 구문
2. Checked Exception과 Unchecked Exception
3. try with resources



## 1. try catch finally 구문

* 주어진 문자열을 정수로 변경하는 예제 

### in java

```java
private int parseIntOrThrow(@NotNull String str) {
  try {
    return Integer.parseInt(str);
  } catch (NumberFormatException e) {
    throw new IllegalArgumentException();
  }
}
```



### in kotlin

```kotlin
fun parseIntOrThrow(str: String): Int {
  try {
    return str.toInt()
  } catch (e: NumberFormatException) {
    throw IllegalArgumentException()
  }
}
```

---

### in java

```java
private int parseIntOrThrow(@NotNull String str) {
  try {
    return Integer.parseInt(str);
  } catch (NumberFormatException e) {
    return null
  }
}
```



### in kotlin

```kotlin
fun parseIntOrThrow(str: String) : Int? {
  try {
    return str.toInt()
  } catch (e: NumberFormatException) {
    null
  }
}
```

* 코틀린에서는 try-catch 구문 역시 expression 이다.
* 즉 리턴할 수 있다.

```kotlin
fun parseIntOrThrow(str: String) : Int? {
  return try {
    str.toInt()
  } catch (e: NumberFormatException) {
    null
  }
}
```



## 2. Checked Exception과 Unchecked Exception

* 예제 : 프로젝트 내 파일의 내용물을 읽어오는 예제

### in java

```java
public void readFile() throws IOException {
  File currentFile = new File(".");
  File file = new File(currentFile.getAbsolutePath() + "/a.txt");
  BufferedReader reader = new BufferedReader(new FileReader(file));
  
  reader.close();
}
```

* IOException은 Checked Exception이라사용하는 곳에서  try-catch로 캐치해서 unchecked Exception으로 전환해야한다
* 그래서 메소드 시그니처에 IOException을 적어놓았다.

### in kotlin

```kotlin
fun readFile() {
  val currentFile = File(".")
  val file = File(currentFile.absolutePath + "/a.txt")
  val reader = BufferedReader(FileReader(file))
  
  reader.close()
}
```

* 코틀린에서는 throws 구문이 없다.
* 코틀린에서는  Checked Exception과 UncheckedException을 구분하지 않는다.
* 모두 Unchecked Exception이다. 



## 3. try with resources



### in java

```java
public void readFile(String path) throws IOException {
  try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
    ...
  }
}
```



### in kotlin

코틀린에는 try-with-resources 구문이 없다.   

대신 `use` 라는 `inline 확장함수 `를 사용해야 한다. 



```kotlin
fun readFile(path: String) {
  BufferedReader(FileReader(path)).use {
    reader -> ...
  }
}
```



# Lec08. 코틀린에서 함수를 다루는 방법

1. 함수 선언 문법
2. default parameter
3. named argument (parameter)
4. 같은 타입의 여러 파라미터 받기 (가변인자)





## 1. 함수 선언 문법

* 함수는 fun 키워드로 선언한다. 

* 접근 지시어 public은 생략 가능하다.

```kotlin
접근지시어 fun 함수명(매개변수, 매개변수명: 타입): 반환 타입
```

* 반환 타입이 Unit(void) 인 경우 생략 가능하다. 

* 만약 함수의 반환값이 1개라면, `block({})` 대신 `=` 이 사용가능하다 

```kotlin
fun max(a: Int, b: Int): Int = 
	if (a > b) {
    a
  } else {
    b
  }

// 한줄로도 변경 가능
fun max(a: Int, b: Int): Int = if (a > b) a else b
```

* a가 b보다 크면 a 리턴, 아니면  b 리턴 

* `=` 를 사용하는 경우 반환 타입 생략 가능 

* block{} 을 사용하는 경우, 반환 타입이 Unit이 아니면 명시적으로 타입을 작성해주어야 한다. 

<br> 

* 함수는 클래스 안에 있을 수도, 파일 최상단에 있을 수도 있다다. 
  * 또한, 한 파일 안에 여러 함수들이 있을 수도 있다.



## 2. default parameter



### in java

```java
public void repeat(String str, int num, boolean useNewLine) {
  for (int i = 1; i<= num; i++) {
    if (useNewLine) {
      System.out.println(str);
    } else {
			System.out.print(str);
    }
  }
}
```



* 많은 코드에서 useNewLine 을 사용한다면 `OverLoading`을 활용하여 다음과 같이 쓸 수 있다.

```java
public void repeat(String str, int num) {
  repeat(str, num, true);
}
```



> 코틀린은 default 파라미터 값을 줄 수 있다.

### in kotlin

```kotlin
fun repeat(
	str: String,
  num: Int = 3,
  useNewLine: Boolean = true
) {
  for (i in 1..num) {
    if (useNewLine) {
      println(str)
    } else {
      print(str)
    }
  }
}
```



* 자동으로 오버로딩이 된다.
* 물론 코틀린에도 오버로드 기능은 있다. 

```kotlin
fun main() {
  repeat("hello", 3, true)
  repeat("hello")
  repeat("hello", 2)
}
```



## 3. named argument (parameter)

매개 변수 이름을 통해 직접 지정할 수 있다.  
지정되지 않은 매개변수는 기본값을 사용한다.

```kotlin
repeat("hello", useNewLine = false)
```



* builder를 직접 만들지 않고 builder의 장점을 가지게 된다. 

> 그러나, Kotlin에서 Java 함수를 가져다 사용할 때는 named argument를 사용할 수 없다

* 코틀린 함수만 사용할 수 있다.
* JVM 상에서, Java가 바이트 코드로 변환됐을 때 parameter 이름을 보존하고 있지 않다 보니, 코틀린에서는 자바 함수를  named argument를 이용해서  사용할 수 없다. 



## 4. 같은 타입의 여러 파라미터 받기 (가변인자)



### in java

```java
public static void printAll(String... strings) {
  for (String str : strings) {
    System.out.println(str);
  }
}
```



### in kotlin

```kotlin
fun printAll(vararg strings: String) {
  for (str in strings) {
    println(str)
  }
}
```



만약, 배열을 바로 넣는 다면 스프레드 연산자(*)를 붙여야 한다. 

```kotlin
var array = arrayOf("A", "B", "C")
printAll(*array)

printAll("A", "B", "C")
```



# Lec 09.코틀린에서 클래스를 다루는 방법



1. 클래스와 프로퍼티
2. 생성자와 init
3. 커스텀 getter, setter
4. backing field



## 1. 클래스와 프로퍼티



### in java

```java
public class JavaPerson {
    
    private final String name;
    
    private int age;

    public JavaPerson(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}

```



### in kotlin

코틀린에서는 필드만 만들면 getter, setter를 자동으로 만들어 준다. 

프로퍼티 = 필드 + getter + setter

```kotlin
class Person constructor(name: String, age: Int) {
    val name = name
    var age = age
}
```

* constructor는 생략할 수 있다.
* public 은 생략할 수 있다.

```kotlin
class Person (
	val name: String,
	var age: Int
)
```

* 객체.필드를 통해 getter와 setter를 바로 호춣한다. 

* Java클래스에 대해서도 .필드 로 getter, setter를 사용한다. 



## 2. 생성자와 init



### in java

```java
public class JavaPerson {
  public JavaPerson(String name, int age) {
    if (age <= 0) {
      throw new IllegalArgumentException();
    }
    
    this.name = name;
    this.age = age;
  }
}
```



### in kotlin

```kotlin
class Person(
	val name: String,
  var age: Int,
) {
  
  init {
    if (age < 0) {
      throw IllegalArgumentException();
    }
    println("초기화 블록")
  }
  
  // 다른 생성자 
  constructor(name: String) : this(name, 1) {
    println("부 생성자 1")
  }
  
  constructor() : this("김영수") {
    println("부 생성자 2")
  }
  
}
```

* 주 생성자(primary constructor) 반드시 존재해야 this 사용 가능
* 단, 주 생성자에 파라미터가 하나도 없다면 생략 가능

* 부 생성자(secondary constructor)는 최종적으로 주 생성자를 this로 호출해야 한다.
  * body를 가질 수 있다.

부 생성자2 -> 부 생성자 1 -> 초기화 블록 순으로 호출된다. 

* 현재 2에서 1 호출,  1에서 주 생성자 호출되는 구조임



### 하지만 부 생성자보다는 default parameter를 권장한다.

```kotlin
class Person (
	val name: String = "김영수",
  var age: Int = 1,
) {
  init {
    if (age < 0) {
      throw IllegalArgumentException();
    }
  } 
}
```



* 다른 객체로 변환해야 하는 Converting과 같은 경우 부생성자를 사용할 수 있지만, 그보다는 정적 팩토리 메소드(companoin object) 사용
  * 코틀린은 static이 없다.  



## 3. 커스텀 getter, setter

* 성인인지 확인하는 기능 

```kotlin
fun isAdult(): Boolean {
  return this.age >= 20
}
```

* 함수를 정의해도 되지만, 함수 대신 프로퍼티로 만든다. 

```kotlin
val isAdult: Boolean get() = this.age >= 20
// 또는
val isAdult: Boolean 
	get() {
    return this.age >= 20
  }
```

* 위 코드와 아래 코드는 같은 기능을 한다. 
* get() 했을 때 어떤 로직을 실행 시킬지 정의한다. 
* 객체의 속성이라면, custom getter 
* 그렇지 않다면 함수를 정의해서 사용하는것이 좋다. 

또한,  custom getter를 사용하면 자기 자신을 변형해서 돌려줄수도 있다. 

```kotlin
class Person(
	name: String,
  var age: Int,
) {
  
	val name: String = name
		get() = field.uppercase()
  
}
```

* `field`를 사용하는 이유는, 무한루프를 막기위한 예약어이다. 자기 자신을 가리킨다. 
  * 무한루프?
    * name은 name에 대한 getter를 호출하니 다시 get을 호출
    * get은 다시 name이 있고, 다시  name을 호출하고 get을 호출 -> 무한루프 
* `filed` = backing field

<br>

* set()도 똑같이 사용해주면 된다.

```kotlin
var name: String = name
	set(value) {
    field = value.uppercase()
  }
```



### 정리



- 코틀린에서는 필드를 만들면 getter와 (필요에 따라) setter가
자동으로 생긴다.
- 때문에 이를 프로퍼티 라고 부른다.
- 코틀린에서는 주생성자가 필수이다.
- 코틀린에서는 consturctor 키워드를 사용해 부생성자를 추가로
  만들 수 있다.
- 단, default parameter나 정적 팩토리 메소드를 추천한다.
- 실제 메모리에 존재하는 것과 무관하게 custom getter와
  custom setter를 만들 수 있다.
- custom getter, custom setter에서 무한루프를 막기 위해
field라는 키워드를 사용한다.
- 이를 backing field 라고 부른다.



# Lec 10.코틀린에서 상속을 다루는 방법

1. 추상 클래스
2. 인터페이스
3. 클래스를 상속할 때 주의할 점
4. 상속 관련 지시어 정리







## 1. 추상 클래스

Animal이란 추상클래스를 구현한 Cat, Penguin

```kotlin
abstract class Animal (
    protected val species: String,
    protected open val legCount: Int, // 추상 프로퍼티가 아니라면, 상속받을때 꼭 open을 붙여야 한다.
        ) {
    
    abstract fun move()
}

class Cat(
    species: String
) : Animal(species, 4){

    override fun move() {
        println("${species}라는 고양이가 걸어가.")
    }
}

class Penguin (species: String,) : Animal(species, 2) {

    private val wingCount: Int = 2

    override fun move() {
        println("펭귄이 움직인다")
    }

    override val legCount: Int
        get() = super.legCount + this.wingCount
}
```

* 상속은  `extends` 키워드를 사용하지 않고 `:` 를 사용한다
* 상위 클래스의 생성자를 바로 호출한다. 
  * 무조건 상위 클래스의 생성자를 호출해야 한다. 
* @Override어노테이션 대신 override 키워드를 사용한다. 
* 추상클래스에서 만들어진 getter를 오버라이드 할라면 open을 붙여야 한다. 
  * property(getter, setter) override
  * `protected open val legCount: Int`

> Java, Kotlin 모두 추상 클래스는 인스턴스화 할 수 없다. 



## 2. 인터페이스

Flyable과 Swimmable을 구현한 Penguin

```kotlin
interface Swimmable {
  
  val swimAblitiy: Int
  
    fun act() {
        println("어푸 어푸")
    }
}

interface Flyable {

    fun act() {
        println("파닥 파닥")
    }
    
}

class Penguin (species: String,) : Animal(species, 2), Swimable, Flyable {

    override fun act() {
        super<Flyable>.act()
        super<Swimable>.act()
    }
  
  	override fun swimAbility: Int
  		get() = 3
  
}
```

* 코틀린 인터페이스의 디폴트 메소드는  default 키워드 없이 메소드 구현이 가능하다. 
  * 구현을 하면 디폴트 메소드, 구현 안하면 추상 메소드 

* 인터페이스 구현도 : 를 사용한다.
* 중복되는 인터페이스를 특정할 때  super<타입>.함수 사용
* 코틀린에서는 backing field가 없는 프로퍼티를 Interface에 만들 수 있다.
  * 인터페이스에 프로퍼티를 정의하고, getter에 대한 것을 구현한 서브 클래스 에서 오버라이딩한다. 
  * swimAblitiy에 대한 getter를 오버라이딩 했다. 



## 3. 클래스를 상속할 때 주의할 점

다른 클래스가 상속을 받을라면 슈퍼 클래스에  `open` 키워드를 명시해야 한다. 

* open 키워드를 사용하지 않으면 final class
* property도  override 해줘야 한다. 

```kotlin
open class Base (open val number: Int = 100) {

    init {
        println("Base Class")
        println(number)
    }
}

class Derived(
    override val number: Int
) : Base(number) {
    init {
        println("derived Class")
    }
}
```

* 상위 클래스의 init이 먼저 호출된다
  * 상위클래스 init이 먼저 호출되었고 하위 클래스에서 override 하고 있는 프로퍼티(number)에 접근하는데, 하위 클래스는 아직 초기화가 안되었다.   
  * 그러므로 하위 클래스의 field인 number에 접근하면 안된다. 
    * 0이 호출된다. 

> 상위 클래스를 설계할 때, 생성자 또는 초기화 블록에 사용되는 프로퍼티에는  open을 피해야 한다.



1. final : override를 할 수 없게 한다. default로 보이지 않게 존재한다.
   * 그러므로 open 키워드를 사용해야 상속 및 override가 가능하다. 
2. open : override를 열어 준다.
3. abstract : 반드시 override 해야 한다.
4. override : 상위 타입을 오버라이드 하고 있다.

---

* 상속 또는 구현을 할 때에 : 을 사용해야 한다.

- 상위 클래스 상속을 구현할 때 생성자를 반드시 호출해야 한다.
- override를 필수로 붙여야 한다.
- 추상 멤버가 아니면 기본적으로 오버라이드가 불가능하다.
- open을 사용해주어야 한다.
- 상위 클래스의 생성자 또는 초기화 블록에서 open 프로퍼티를
사용하면 얘기치 못한 버그가 생길 수 있다



# Lec11. 코틀린에서 접근 제어를 다루는 방법



1. 자바와 코틀린의 가시성 제어
2. 코틀린 파일의 접근 제어
3. 다양한 구성요소의 접근 제어
4. Java와 Kotlin을 함께 사용할 경우 주의할 점



## 1.자바와 코틀린의 가시성 제어



1. java와 kotlin의 접근제어 ( 왼쪽(주황)은 Java, 오른쪽(파랑)은 kotlin)



<img src="images//image-20221014164854528.png" width=850 height=400>





* 모듈 : 한 번에 컴파일 되는 Kotlin 코드
  * IDEA Module
  * Maven Project\
  * Gradle Source Set
  * Ant Task <kotlinc>의 호출로 컴파일 파일의 집합



#### 표로 정리

| java      | kotlin    | 설명                                                         |
| --------- | --------- | ------------------------------------------------------------ |
| public    | public    | 모든곳에서 접근 가능                                         |
| protected | protected | 자바 : 같은 패키지 또는 하위 클래스 <br />코틀린 : `선언된 클래스` 또는 하위 클래스 |
| default   | internal  | 같은 모듈에서만 접근 가능                                    |
| private   | private   | 선언된 클래스에서만 접근 가능                                |



* 코틀린에서는 기본적으로 패키지 라는 개념을, namespace를 관리하기 위한 용도
  * 어떤 클래스가 어떤 패키지에 있다.
  * 가시성 제어는 사용하지 않는다. -> protected가 달라진 이유 



## 2.코틀린 파일의 접근 제어

* 코틀린의 `기본 접근 지시어는 public`

* 코틀린은 .kt파일에 변수 함수, 클래스 여러개를 바로 만들 수 있다.

<img src="images//image-20221014162816270.png" width=400 height=400>





## 3.다양한 구성요소의 접근 제어

* 생성자에 접근 지시어를 붙이려면 constructor를 써야 한다

```kotlin
class Bus internal constructor(
	val price: Int
)
```

* public은 생략되어있다.

* final도 기본적으로 붙여져있는데, 생략되어있다.



### java에서의 유틸성 코드

```java
public abstract class StringUtils {

  private StringUtils() {}

  public boolean isDirectoryPath(String path) {
    return path.endsWith("/");
  }

}

```

* 자바에서는 유틸성 코드를 만들 때 abstact class + private constructor를 사용해서 인스턴스 화도, 상속도 할 수 없게  막았다.



### kotlin에서의 유틸성 코드

* 파일 최상단에 유틸 함수를 작성하면 매우 편리하다.

```kotlin
fun isDirectoryPath(path: String) : Boolean {
    return path.endsWith("/")
}

class StringUtils {
}

// bytecode -> java 로 변환

@Metadata(
   mv = {1, 5, 1},
   k = 2,
   d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0000\u001a\u000e\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003¨\u0006\u0004"},
   d2 = {"isDirectoryPath", "", "path", "", "kotlin-with-java"}
)
public final class StringUtilsKt {
   public static final boolean isDirectoryPath(@NotNull String path) {
      Intrinsics.checkNotNullParameter(path, "path");
      return StringsKt.endsWith$default(path, "/", false, 2, (Object)null);
   }
}
```

* StringUtils라는 kt파일이 StringUtilsKt 라는 클래스로 바뀌고, 이 안에 static 메서드로 isDirectoryPath가 존재한다.



자바에서도 StringUtilsKt.isDirectoryPath() 라는 메서드를, 마치 정적 메서드가 있는것처럼 사용이 가능하다

```java
public class Test {
  public static void main(String[] args) {
    StringUtilsKt.isDirectoryPath("~~~");
  }
}
```



### property, field의 가시성(접근제어) 제어방법

```kotlin
class Car (
	internal val name: String,
  private val owner: String,
	_price: Int
) {
	var price = _price
		private set
}
```

* getter, setter 한번에 접근 지시어를 정하거나 -> internal 
* setter에만 접근제어를 부여할 수 있다 -> private set 





## 4. Java와 Kotlin을 함께 사용할 경우 주의할 점

* internal은 바이트코드상 public이 되므로, Java코드에서는 Kotlin 모듈의 internal 코드를 가져올 수 있다. 

* Kotlin의 protected와 Java의 protected는 다르다.
  * Java는 같은 패키지의 Kotlin protected 멤버에 접근할 수 있다.



## 정리

* Kotlin에서 패키지는 namespace 관리용이기 때문에 protected는 의미가 달라졌다.

- Kotlin에서는 default가 사라지고, 모듈간의 접근을 통제하는
internal이 새로 생겼다.
- 생성자에 접근 지시어를 붙일 때는 constructor를 명시적으로
써주어야 한다.
- 유틸성 함수를 만들 때는 파일 최상단을 이용하면 편리하다

* 프로퍼티의 custom setter에 접근 지시어를 붙일 수 있다.

- Java에서 Kotlin 코드를 사용할 때 internal과 protected는
주의해야 한다



# Lec12. 코틀린에서 object키워드를 다루는 방법



1. static 함수와 변수
2. 싱글톤
3. 익명 클래스



## 1. static 함수와 변수

* java에서는 object라는 지시어가 없지만, kotlin에서 추가되었다.

* 코틀린은 static 이라는 키워드가 없다 
* compain object(동행 객체) 라는 키워드를 이용한다. 

### in java

```java
public class JavaPerson {

  private static final int MIN_AGE = 1;

  public static JavaPerson newBaby(String name) {
    return new JavaPerson(name, MIN_AGE);
  }

  private String name;

  private int age;

  private JavaPerson(String name, int age) {
    this.name = name;
    this.age = age;
  }

}
```



### in kotlin

```kotlin
class Person(
    val name: String,
    val age: Int,
) {

    companion object Factory : Log{
        private const val MIN_AGE = 1
        fun newBaby(name: String): Person {
            return Person(name, MIN_AGE)
        }

        override fun log() {
            println("나는 Person 클래스의 companion object이다.")
        }
    }
}

interface Log {
    fun log()
}

fun main() {
    val factory = Person.Factory.newBaby("영수")
		// val factory = Person.newBaby("영수") // 생략
    println(factory.name)

}
```



* static : 클래스가 인스턴스화 될 때 새로운 값이 복제되는게아니라 정적으로 인스턴스 끼리의 값을 공유
* companion object: 클래스와 동행하는 유일한 오브젝트 
  * 클래스라는 설계도와 동행하는 오브젝트

* const라는 키워드는 컴파일 시에 변수가 할당
  * 붙이지 않으면 런타임시에 할당된다.
  * const는 즉, 진짜 상수에 붙이기 위한 용도. 기본타입과 String에만 붙일 수 있다. 

* companion object는 하나의 객체로 간주된다. 
  * 이름을 붙일 수도 있고, interface를 구현할 수도 있다.
    * 이름은 사용시에 생략이 가능하다.  
  * 위 예제에서는  Log 인터페이스를 구현했다.
  * 

* companion object에 유틸성 함수들을 넣어도 되지만, 유틸성 함수들은 파일 최상단을 이용하는 것이 좋다. 



### Java에서 코틀린의 companion object(static)을 사용하고 싶다면?

* 반드시  코틀린 코드에서` @JvmStatic`을 붙여야 한다.

```java
Person person = Person.newBaby("A");
```

```kotlin
companion object {
  private const val MIN_AGE = 0
  
  @JvmStatic
  fun newBaby(name: String): Person {
    return Person(name, MIN_AGE)
  }
}
```

* 만약 companion object의 이름이 있다면 이름을 사용하면 된다. 
  * 이름이 없다면 `Companion` 이라는 이름이 생략된것. 

## 2. 싱글톤



```java
public class JavaSingleton {

  private static final JavaSingleton INSTANCE = new JavaSingleton();

  private JavaSingleton() { }

  public static JavaSingleton getInstance() {
    return INSTANCE;
  }

}
```

* 동시성 처리를 더 해주거나, enum class를 활용하는 방법도 있다.
* 코틀린에서는 object 키워드를 붙이면 싱글톤 클래스다..

```kotlin
object Singleton {
  var a: Int = 0
}

fun main() {
  println(Singleton.a)
}
```

* 클래스 이름으로 접근할 수 있다. 



## 3. 익명 클래스



익명클래스 : 특정 인터페이스나 클래스를 상속받은 구현체를 일회성으로 사용할 때 쓰는 클래스

```java
public interface Movable {

  void move();

  void fly();

  
}

private static void moveSomething(Movable movable) {
  movable.move();
  movable.fly();
}

public static void main(String[] args) {
  moveSomething(new Movable() {
    @Override
    public void move() {System.out.println("move");}
    
    @Override
    public void fly() {System.out.println("난다~~")}
 
  });
}
```



### in kotlin

```kotlin
fun main() {

    moveSomething(object : Movable{
        override fun move() {
            println("move")
        }

        override fun fly() {
            println("fly")
        }
    })

}

private fun moveSomething(movable: Movable) {
    movable.move()
    movable.fly()
}
```



* 자바에서는 new 타입이름(), 코틀린에서는 object : 타입



### 정리

- Java의 static 변수와 함수를 만드려면,
Kotlin에서는 companion object를 사용해야 한다.
- companion object도 하나의 객체로 간주되기 때문에 이름을
붙일 수 있고, 다른 타입을 상속받을 수도 있다.
- Kotlin에서 싱글톤 클래스를 만들 때 object 키워드를 사용한다.
- Kotlin에서 익명 클래스를 만들 때 object : 타입을 사용한다.



# Lec13.코틀린에서 중첩 클래스를 다루는 방법

1. 중첩 클래스의 종류
2. 코틀린의 중첩 클래스와 내부 클래스

## 1.중첩 클래스의 종류

1. static을 사용하는 중첩 클래스
   1. 클래스 안에 static을 붙인 클래스! 밖의 클래스 직접 참조 불가
2. static을 사용하지 않는 중첩 클래스
   1. 클래스 안의 클래스, 밖의 클래스 직접 참조 가능!



<img src="images//image-20221014173753932.png" width= 800 height = 300>



### 자바의 내부클래스 주의점

Effective Java 3rd Edition – Item24, Item86
1. 내부 클래스는 숨겨진 외부 클래스 정보를 가지고 있어,
참조를 해지하지 못하는 경우 메모리 누수가 생길 수 있고,
이를 디버깅 하기 어렵다.
2. 내부 클래스의 직렬화 형태가 명확하게 정의되지 않아
직렬화에 있어 제한이 있다



> 클래스 안에 클래스를 만들 때는 static 클래스를 사용하라



* Kotlin에서는 이러한 Guide를 충실히 따르고 있다

## 2. 코틀린의 중첩 클래스와 내부 클래스



### in java

```java

public class JavaHouse {

  private String address;
  private LivingRoom livingRoom;

  public JavaHouse(String address) {
    this.address = address;
    this.livingRoom = new LivingRoom(10);
  }

  public LivingRoom getLivingRoom() {
    return livingRoom;
  }

  public class LivingRoom {
    private double area;

    public LivingRoom(double area) {
      this.area = area;
    }

    public String getAddress() {
      return JavaHouse.this.address;
    }
  }

}
```



### in kotlin

```kotlin
class House (
    private var address: String,
    private var livingRoom: LivingRoom = LivingRoom(10.0)
) {

    class LivingRoom(
        private var area: Double,
    )
}
```



* 기본적으로 바깥 클래스에 대한 연결이 없는 중첩 클래스가 만들어진다. 



> 코틀린에서는 내부 클래스는 static으로 만들어지게 권장하는 것을 따르고 있다. 



### 권장되지 않은 inner 클래스인 내부 클래스를 만들라면? (non-static inner class)

* 명시적으로 `inner` 라는 키워드를 사용해야 한다.

```kotlin
class House (
    private var address: String,
    ) {
    private var livingRoom = this.LivingRoom(10.0)
    
    inner class LivingRoom(
        private var area: Double,
    ) {
        val address: String
            get() = this@House.address
    }

}
```

* 바깥 클래스 참조를 위해  `this@바깥클래스`를 사용한다. 



Kotlin에서는 이러한 Guide를 충실히 따르고 있다.

기본적으로 바깥 클래스 참조하지 않는다.

바깥 클래스를 참조하고 싶다면 inner 키워드를 추가한다



- 코틀린에서는 이러한 가이드를 따르기 위해
- 클래스 안에 기본 클래스를 사용하면 바깥 클래스에 대한 참조가 없고
- 바깥 클래스를 참조하고 싶다면, inner 키워드를 붙여야 한다.
- 코틀린 inner class에서 바깥 클래스를 참조하려면 this@바깥클래스를 사용해야 한다





<img src="images//image-20221014175341495.png" width=900 height=350 >



# Lec14.코틀린에서 다양한 클래스를 다루는 방법

1. Data Class
2. Enum Class
3. Sealed Class, Sealed Interface



## 1. Data Class



### 1. DTO



#### in java

```java
public class JavaPersonDto {

  private final String name;
  private final int age;

  public JavaPersonDto(String name, int age) {
    this.name = name;
    this.age = age;
  }

  public String getName() {
    return name;
  }

  public int getAge() {
    return age;
  }
}
```



* 데이터(필드)
* 생성자와 getter
* equals, hashCode
* toString



IDE를 활용할 수도 있고, lombok을 활용할 수도 있지만   
클래스가 장황해지거나,  
클래스 생성 이후 추가적인 처리를 해줘야 하는 단점이 있다.  



#### in kotlin

```kotlin
data class PersonDto(
    val name: String,
    val age: Int,
) {
}
```

* `data` 키워드를 붙여주면 equals, hashCode, toString을 자동으로 만들어준다 
* named argument까지 활용하면 builder pattern을 쓰는 것 같은 효과를 얻을 수 있다.



### java 16

Java에서는 JDK16부터 Kotlin의 data class 같은 record class를 도입했다. 





## 2. Enum Class



### in java

```java
public enum JavaCountry {

  KOREA("KO"),
  AMERICA("US"),
  ;

  private final String code;

  JavaCountry(String code) {
    this.code = code;
  }

  public String getCode() {
    return code;
  }

}
```

enum의 특징

* 추가적인 클래스를 상속받을 수 없다. 

* 인터페이스는 구현할 수 있으며, 각 코드가 싱글톤이다



### in kotlin

```kotlin
enum class Country(
    val code: String
) {
    
    KOREA("KO"),
    AMERICA("US"),
    ;
}
```



#### when

when은 Enum Class 혹은 Sealed Class와 함께 사용할 경우, 더욱더 진가를 발휘한다



``` kotlin
private fun handleCountry(country: Country) {
    when (country) {
        Country.KOREA -> TODO()
        Country.AMERICA -> TODO()
    }
}
```

* 조금 더 읽기 쉬운 코드.
* 컴파일러가 모든 타입을 알기 때문에 else를 쓰지 않아도 괜찮다.

* IDE 단에서 warning을 주거나 하는 방식으로 알 수 있다.

## 3. Sealed Class, Sealed Interface



Sealed : 봉인을 한 이라는 뜻 



### ex

상속이 가능하도록 추상클래스를 만들까 하는데...
외부에서는 이 클래스를 상속받지 않았으면 좋겠어!! 하위 클래스를 봉인하자!!!



### Shealed Class

* 컴파일 타임 때 하위 클래스의 타입을 모두 기억한다.

* 즉, 런타임때 클래스 타입이 추가될 수 없다.

* 하위 클래스는 같은 패키지에 있어야 한다.

* Enum과 다른 점

  - 클래스를 상속받을 수 있다.

  - 하위 클래스는 멀티 인스턴스가 가능하다 

```kotlin
sealed class HyundaiCar(
    val name: String,
    val price: Long
)


class Avante : HyundaiCar("아반떼", 1000L)

class Sonata : HyundaiCar("소나타", 2000L)

class Grandeur : HyundaiCar("그렌저", 3000L)
```



* `컴파일 타임 때 하위 클래스의 타입을 모두 기억한다.`
  * 즉, 런타임때 클래스 타입이 추가될 수 없다.



* 추상화가 필요한 Entity or DTO에 sealed class를 활용
* JDK17 에서도 Sealed Class가 추가



### 정리

- Kotlin의 Data class를 사용하면 equals, hashCode, toString을
자동으로 만들어준다.
- Kotlin의 Enum Class는 Java의 Enum Class와 동일하지만,
when과 함께 사용함으로써 큰 장점을 갖게 된다.
- Enum Class보다 유연하지만, 하위 클래스를 제한하는
Sealed Class 역시 when과 함께 주로 사용된다.



# Lec15. 코틀린에서 배열과 컬렉션을 다루는 방법

1. 배열
2. 코틀린에서의 Collection – List, Set, Map
3. 컬렉션의 null 가능성, Java와 함께 사용하기





## 1. 배열

* 사실 잘 사용하지 않지만 문법을 간략히 설명한다.

* 이펙티브자바 - 배열보단 리스트를 사용하라 

### in java

```java
int[] array = {100, 200};

for (int i = 0; i < array.legth; i++) {
  System.out.printf()
}

```



### in kotlin

```kotlin
val array = arrayOf(100, 200)

for (i in array.indices) {
  println("%{i} %{array[i]}")
}
```

* indices : 0부터 마지막 index 까지의 Range

```kotlin
val array = arrayOf(100, 200)

for ((idx, value) in array.withIndex()) {
  println("${idx} ${value}")
}
```

* withIndex()를 사용하면 인덱스와 값을 한번에 가져올 수 있다. 

* array.plus(value) 를 사용하면 값을 넣을 수 있다.

```kotlin
val array = arrayOf(100, 200)
array.plus(300)
```



## 2. 코틀린에서의 Collection – List, Set, Map

* 컬렉션을 만들어줄 때 불변인지, 가변인지를 설정해야 한다



<img src="images//image-20221015160113344.png" width=600 height=400>



* 가변(Mutable) 컬렉션 : 컬렉션에 element를 추가, 삭제할 수 있다.
* 불변 컬렉션 : 컬렉션에 element를 추가, 삭제할 수 없다.

Collection을 만들자 마자 Collections.unmodifiableList() 등을 붙여준것이 불변  
불변 컬렉션이라 하더라도 Reference Type인 Element의 필드는 바꿀 수 있다

* list.get(index)에 접근해서 요소 내의 필드는 바꿀 수 있다. - 자바랑 같음

> Kotlin은 불변/가변을 지정해 주어야 한다는 사실



### in java

```java
final List<Integer> numbers = Arrays.asList(100, 200);
```



### in kotlin

```kotlin
val numbers = listOf(100, 200)
```

* listOf를 통해 `불변리스트` 를 만든다. 

```kotlin
val emptyList = emptyList<Int>()
```

* emptyList<타입>()
  * 이 비어있는 리스트에 들어올 타입을 명시적으로 지정



### in java

```java
// 한 요소 접근
System.out.println(numbers.get(0));

//foreach
for (int number: numbers) {
  System.out.println(number);
}

// 전통적 for문
for (int i = 0; i < numbers.size(); i++) {
  System.out.printf("%s %s", i, numbers.get(i));
}
```



### in kotlin

```kotlin
// 한 요소 접근
println(numbers[0])

// for each
for (number in numbers) {
  println(number)
}

// 전통적인 for문 느낌
for ((index, number) in numbers.withIndex()) {
  println("$index %number")
}

```



### 코틀린에서 가변(mutable) 리스트를 만들고 싶다면?

```kotlin
val numbers = mutableListOf(100, 200)
numbers.add(300)
```

* 기본 구현체는 ArrayList이고, 사용법은 Java와 동일 



> 우선 불변 리스트를 만들고, 꼭 필요한 경우 가변 리스트로 바꾸자!



### collection - Set

집합은 List와 다르게 순서가 없고, 같은 element는 하나만 존재할 수 있다.  

자료구조적 의미만 제외하면 모든 기능이 List와 비슷

```kotlin
val numbers = setOf(100, 200)

// for each
for (number in numbers) {
  println(number)
}

// 전통적인 for문 느낌
for ((index, number) in numbers.withIndex()) {
  println("$index %number")
}
```

* 가변(Mutable) 집합을 만들고 싶다면?

```kotlin
val numbers = mutableSetOf(100, 200)
```

* 기본 구현체는 LinkedHashSet



### collection - Map

Kotlin도 동일하게 MutableMap을 만들어 넣을 수도 있고,
정적 팩토리 메소드를 바로 활용할 수도 있다.



```kotlin
val map = mutableMapOf<Int, String>()
map[1] = "MONDAY"
map[2] = "TUESDAY"

mapOf(1 to "MONDAY", 2 to "TUESDAY")
```

* 자바처럼 map.put()을 쓸 수도 있고, map[key] = value를 사용할 수도 있다.
* `mapOf(key to value)`를 사용해 불변 map을 만들 수 있다. 



## 3. 컬렉션의 null 가능성, Java와 함께 사용하기

* `List<Int?>` : 리스트에 null이 들어갈 수 있지만, 리스트는 절대 null이 아님
* `List<Int>?` : 리스트에는 null이 들어갈 수 없지만, 리스트는 null일 수 있음
* `List<Int?>?` : 리스트에 null이 들어갈 수도 있고, 리스트가 null일 수도 있음



> ? 위치에 따라 null가능성 의미가 달라진다.



but, Java는 읽기 전용 컬렉션과 변경 가능 컬렉션을 구분하지 않는다.



### 버그 조심 2가지

1. 코틀린의 불변 리스트를 자바에서 가져와 사용할 때 요소를 추가하여 코틀린에서 사용한다면, 버그가 생긴다
2. 코틀린의 non-nullable 리스트를 자바에서 가져와 사용할 때 null을 넣고 코틀린에서 사용한다면 버그가 생긴다.





### 그렇다면?

* Kotlin 쪽의 컬렉션이 Java에서 호출되면 컬렉션 내용이 변할 수 있음을 감안해야 한다.
* 코틀린 쪽에서 `Collections.unmodifableXXX()`를 활용하면 변경 자체를 막을 수는 있다!
* Kotlin에서 Java 컬렉션을 가져다 사용할때 플랫폼 타입을 신경써야 한다

* Java 코드를 보며, 맥락을 확인하고 Java 코드를 가져오는 지점을 wrapping한다



### 정리

- 코틀린에서는 컬렉션을 만들 때도 불변/가변을 지정해야 한다.
- List, Set, Map 에 대한 사용법이 변경, 확장되었다.
- Java와 Kotlin 코드를 섞어 컬렉션을 사용할 때에는 주의해야 한다.
- Java에서 Kotlin 컬렉션을 가져갈 때는 불변 컬렉션을 수정할수도 있고, non-nullable 컬렉션에 null을 넣을 수도 있다.
- Kotlin에서 Java 컬렉션을 가져갈 때는 플랫폼타입을 주의해야한다



# Lec16.코틀린에서 다양한 함수를 다루는 방법



1. 확장함수
2. infix 함수
3. inline 함수
4. 지역함수





## 1. 확장함수

코틀린은 Java와 100% 호환하는 것을 목표로 하고 있다



Java로 만들어진 라이브러리를 유지보수, 확장할 때 Kotlin 코드를 덧붙이고 싶다면?!  

* 어떤 클래스안에 있는 메소드처럼 호출할 수 있지만, 함수는 밖에 만들 수 있게 하자!!!

```kotlin
fun String.lastChar(): Char {
  return this.[this.length - 1]
}
```

* `String`.lastChar() 라는건 ` String` Class를 확장한다는 뜻
* this를 활용해 인스턴에 접근한다. 

```
fun 확장하려는클래스명.함수이름(파라미터): 리턴타입 {
	this를 이용해 실제 클래스 안의 값에 접근
}
```

* this : 수신객체
* 확장하려는클래스명 : 수신객체 타입 

```kotlin
val str: String = "ABC"
str.lastChar() // 확장함수 
```

* 원래 String에 있는 멤버 함수 처럼 사용할 수 있다. 



### 확장함수 의 캡슐화?

확장함수가 public이고, 확장함수에서 수신객체클래스의 private 함수를 가져오면 캡슐화가 깨지는거 아닌가?!!



> 확장함수는 클래스에 있는 private 또는 protected 멤버를 가져올 수 없다!!



### 멤버함수와 확장함수의 시그니처가 같다면?!

* 멤버함수가 우선적으로 호출된다.
* 확장함수를 만들었지만, 다른 기능의 똑같은 멤버함수가 생기면 오류가 발생할 수 있다!!



### 확장함수 오버라이드

```kotlin
open class Train(
    val name: String = "새마을 기차",
    val price: Int = 5_000,
) {

}

fun Train.isExpensive(): Boolean {
    println("Train의 확장 함수")
    return this.price >= 10000
}

class Srt: Train("SRT", 40_000)

fun Srt.isExpensive(): Boolean {
    println("Srt의 확장 함수")
    return this.price >= 10000
}

fun main() {
    val train: Train = Train()
    train.isExpensive()

    val srt1: Train = Srt()
    srt1.isExpensive()

    val srt2: Srt = Srt()
    srt2.isExpensive()
}
```

* 실행결과

```
Train의 확장 함수
Train의 확장 함수
Srt의 확장 함수
```

* 해당 변수의 `현재 타입` 즉 정적인 타입에 의해 어떤 확장함수가 호출될지 결정된다.
  * srt1같은 경우 Train 타입이니 Train의 확장함수가 호출된다.  
  * srt2같은 경우 현재 타입이 Srt 이니 Srt의 확장함수가 호출된다.



1. 확장함수는 원본 클래스의 private, protected 멤버 접근이 안된다!
2. 멤버함수, 확장함수 중 멤버함수에 우선권이 있다!
3. 확장함수는 현재 타입을 기준으로 호출된다!



### Java에서 Kotlin 확장함수를 가져다 사용할 수 있나?!

정적 메소드를 부르는 것처럼 사용 가능하다

* 확장함수 라는 개념은 확장프로퍼티와도 연결

#### 확장 프로퍼티

* 확장 프로퍼티의 원리는 확장함수 + custom getter와 동일

```kotlin
fun String.lastChar(): Char {
  return this[this.length - 1]
}

val String.lastChar: Char
	get() = this[this.length - 1]
```



## 2. infix 함수

infix : 중위함수, 함수를 호출하는 새로운 방법



원래는 함수를 호출할 때 `변수.함수이름(argument)` 를 사용했지만, 대신에 

```kotlin
변수 함수이름 argument
```

를 호출

* downTo, step 도 함수이다 (infix 함수)

```kotlin
fun Int.add(other: Int): Int {
  return this.order
}

infix fun Int.add2(other: Int): Int {
  return this + other
}

fun main() {
  3 add2 4 // infix 함수 
}
```

* infix 함수는 `infix` 키워드를 fun 키워드 앞에 사용한다. 

```kotlin
```





## 3. inline 함수

함수가 호출되는 대신, 함수를 호출한 지점에 함수 본문을 그대로 복붙하고 싶은 경우에 사용



```kotlin
fun main() {
  3.add(4)
}

inline fun Int.add(other: Int): Int {
  return this + other
}
```

to java

```java
public static final void main() {
  byte $this$add$iv = 3;
  int other$iv = 4;
  int $i$f$add = false;
  int var10000 = $this$add$iv + other$iv;
}
```

* 함수를 파라미터로 전달할 때에 오버헤드를 줄일 수 있다.
* 하지만 inline 함수의 사용은 성능 측정과 함께 신중하게 사용되어야 한다.



## 4. 지역함수

함수 안에 함수를 선언할 수 있다



다음과 같은 코틀린 함수를

```kotlin
fun createPerson(firstName: String, lastName: String): Person {
    if (firstName.isEmpty()) {
        throw IllegalArgumentException()
    }

    if (lastName.isEmpty()) {
        throw IllegalArgumentException()
    }

    return Person(firstName, lastName, 1)
}
```


다음과 같이 변경

```kotlin
fun createPerson(firstName: String, lastName: String): Person {

  	fun validateName(name: String, fieldName: String) {
      if (name.isEmpty()) {
        throw IllegalArgumentException()
      }
    }
 	
  validateName(firstName, "firstName")
  validateName(lastName, "lastName")
  
  return Person(firstName, lastName, 1)
}
```



* 함수로 추출하면 좋을 것 같은데, 이 함수를 지금 함수 내에서만 사용하고 싶을 때 사용
* but depth가 깊어지기도 하고, 코드가 그렇게 깔끔하지는 않다



# Lec17. 코틀린에서 람다를 다루는 방법

1. Java에서 람다를 다루기 위한 노력
2. 코틀린에서의 람다
3. Closure
4. 다시 try with resources



## 1. Java에서 람다를 다루기 위한 노력



## 2. 코틀린에서의 람다

Java와는 근본적으로 다른 한 가지가 있다.
코틀린에서는 함수가 그 자체로 값이 될 수 있다.
변수에 할당할수도, 파라미터로 넘길 수도 있다.



```kotlin
fun main() {
    // 람다 직접 호출 1
    isApple(Fruit("사과", 1000))

    // 람다 직접 호출 2
    isApple.invoke(Fruit("사과", 1000))
}

// 람다를 만드는 방법 1
val isApple = fun(fruit: Fruit): Boolean {
    return fruit.name == "사과"
}

// 람다를 만드는 방법 2
val isApple2 = {fruit: Fruit -> fruit.name == "사과"}
```

* 함수의 타입 : (파라미터타입 ..) -> 반환 타입



* 코틀린에서 함수는 `1급 시민(first citizen)`이다(Java에서는 2급)

## 3. Closure



Java에서는 람다를 쓸 때 사용할 수 있는 변수에 제약이 있다.

```java
String targetFruitName = "바나나";
targetFruitName = "수박";

filterFruits(fruits, (fruit) -> targetFruitName.equals(fruit.getName()));
```



코틀린에서는 아무런 문제 없이 동작한다

```kotlin
var targetFruitName = "바나나"
targetFruitName = "수박"

filterFruits(fruits) {it.name == targetFruitName}
```

코틀린에서는 람다가 시작하는 지점에 참조하고 있는 변수들을 모두 포획하여 그 정보를 가지고 있다.

* 그러므로 모든 변수의 정보를 가지고 있다. 



>  이렇게 해야만, 람다를 진정한 일급 시민으로 간주할 수 있다.
> 이 데이터 구조를 `Closure`라고 부른다





## 4. 다시 try with resources



```kotlin
fun readFile(path: String) {
  BufferedReader(FileReader(path)).use {
    reader -> println(reader.readLine())
  }
}
```

* 사실 이 `use`는 Closeable 구현체에 대한 확장함수이다.

```kotlin
public inline fun <T : Closeable?, R> T.use(block: (T) -> R): R {...}
```





- 함수는 Java에서 2급시민이지만, 코틀린에서는 1급시민이다.
- 때문에, 함수 자체를 변수에 넣을 수도 있고
파라미터로 전달할 수도 있다.
- 코틀린에서 함수 타입은 (파라미터 타입, ...) -> 반환타입 이었다.

- 코틀린에서 람다는 두 가지 방법으로 만들 수 있고, { } 방법이 더
많이 사용된다.

- 함수를 호출하며, 마지막 파라미터인 람다를 쓸 때는 소괄호 밖으로
람다를 뺄 수 있다
- 람다의 마지막 expression 결과는 람다의 반환 값이다



# Lec18. 코틀린에서 컬렉션을 함수형으로 다루는 방법

1. 필터와 맵
2. 다양한 컬렉션 처리 기능
3. List를 Map으로
4. 중첩된 컬렉션 처리



## 1. 필터와 맵



Fruit 예제의 filter

```kotlin
val apples = fruits.filter {fruit -> fruit.name == "사과"}
```



* 필터에서 인덱스(index)가 필요하다면?

```kotlin
val apples = fruits.filterIndexed {idx, fruit -> 
    println(idx)
    fruit.name == "사과"
}
```



* map

```kotlin
val applePrices = fruits.filter {fruit -> fruit.name == "사과"}
	.map { fruit -> fruit.currentPrice }
```

* map에서 인덱스가 필요하다면?

```kotlin
val applePrices = fruits.filter {fruit -> fruit.name == "사과"}
	.mapIndexed { idx, fruit ->
      println(idx)         
      fruit.currentPrice 
   }
```



* filter / filterIndexed
* map / mapIndexed / 
* mapNotNull



## 2. 다양한 컬렉션 처리 기능

* all : 조건을 모두 만족하면 true 그렇지 않으면 false

```kotlin
val isAllApple = fruits.all {fruit -> fruit.name == "사과"}
```



* none : 조건을 모두 불만족하면 true 그렇지 않으면 false

```kotlin
val isNoApple = fruits.none {fruit -> fruit.name == "사과"}
```



* none : 조건을 모두 불만족하면 true 그렇지 않으면 false

```kotlin
val isNoApple = fruits.any {fruit -> fruit.factoryPrice >= 10_000}
```



* count : 개수를 센다
* sortedBy : (오름차순) 정렬을 한다
* sortedByDescending : (내림차순) 정렬을 한다
* distinctBy : 변형된 값을 기준으로 중복을 제거한다

```kotlin
val distinctFruitNames = fruits.distinctBy {fruit -> fruit.name} 
	.map {fruit -> fruit.name}
```

* first : 첫번째 값을 가져온다 (무조건 null이 아니어야함)
* firstOrNull : 첫번째 값 또는 null을 가져온다
* last : 마지막 값을 가져온다 (무조건 null이 아니어야함)
* lastOrNull : 첫번째 값 또는 null을 가져온다



## 3. List를 Map으로

* 과일 이름 기준으로 묶은 List<과일> Map이 필요하다면?

```kotlin
val map: Map<String, List<Fruit>> = fruits.groupBy {fruit -> fruit.name}
```

* id 값을 기준으로 묶은 과일이 Map이 필요하다면

```kotlin
val map: Map<Long, Fruit> = fruits.associateBy {fruit -> fruit.id}
```



* Key와 value를 동시에 처리할 수도 있다.

```
filter / filterIndexed
map / mapIndexed / mapNotNull
all / none / any
count / sortedBy / sortedByDescending / distinct
first / firstOrNull / last / lastOrNull
groupBy / associateBy
flatMap / flatten
```





## 4. 중첩된 컬렉션 처리