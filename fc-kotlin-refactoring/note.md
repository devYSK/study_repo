# 패스트캠퍼스 자바 투 코틀린 앤드 리팩터링

[toc]



* 코틀린 기초 : https://github.com/digimon1740/fastcampus-kotlin-basic
* 코틀린 고급 : https://github.com/digimon1740/fastcampus-kotlin-advanced
* 스프링 부트 자동 설정 : https://github.com/digimon1740/fastcampus-springboot
* 커스텀 스프링부트 스타터 : https://github.com/digimon1740/fastcampus-custom-spring-boot-starter
* https://github.com/digimon1740/fastcampus-kotlin-java
* 리팩토링 전(자바) : https://github.com/digimon1740/fastcampus-todo-java
* 리팩토링 후 (코틀린) : https://github.com/digimon1740/fastcampus-todo-kotlin
* 실습 프로젝트 코드 : https://github.com/digimon1740/fastcampus-issue-service
* 리액티브 예제 : https://github.com/digimon1740/fastcampus-async-programming
* 스프링 웹플럭스 이해하기 예제 : https://github.com/digimon1740/fastcampus-spring-webflux
* 회원 인증 서비스 예제 : https://github.com/digimon1740/fastcampus-userservice
* 프로젝트 마무리 및 프론트 코드 : https://github.com/digimon1740/jara





# 코틀린

## 코틀린을 배워야 하는 이유

* 자바에 비해 문법이 간결하기 때문에 가독성과 생산성이 높고 오류 가능성이 적어진다
* 자바에 익숙하다면 쉽게 익힐 수 있다. 설령 자바를 모르더라도 분명 학습하기 쉬운 언어이다 
* 자바와 상호 운용이 가능하기 때문에 기존 자바 프로젝트에 쉽게 적용할 수 있고 자바로 작성된 오픈소스를 그대로 사용할 수 있다

* NULL SAFE

  

* Ktor (https://github.com/ktorio/ktor) 젯브레인사에서 코틀린으로 만든 서버 프레임워크
  100% Kotlin으로 작성
* Exposed (https://github.com/JetBrains/Exposed) 젯브레인사에서 코틀린으로 만든 ORM 프레임워크
  100% Kotlin으로 작성



# 자바 vs 코틀린

1. 자바 체크드 익셉션 처리시 의미없는 처리를 반복하며 catch로 에러를 해결하는 일은 드물고 오히려 생산성을 감소시킨다. 때문에 코틀린은 체크드 익셉션을 강제시키지 않는다.

2. 코틀린은 레퍼런스 타입만 지원하며, 알아서 최적화된 방식으로 컴파일 된다 
3. 코틀린은 companion object로 static을 대체한다.  
4. 코틀린은 삼항연산자가 없고 if-else로 대신한다
5. 코틀린은 객체의 함수나 프로퍼티(속성)을 확장해서 사용할 수 있다. 



# 스프링 코틀린

- ﻿﻿기본 제공되는 Junit5 기반의 테스트를 특별한 설정 없이 그대로 사용이 가능하다
- ﻿﻿모의 객체를 만들어 테스트하려면 Mockito 대신 MocKK를 사용할 수 있다
  - Mockito에서 제공하는 @MockBean @SpyBean 을 대체하는 SpringMockK의 @MockkBean , @SpykBean



# 코틀린 제네릭의 변성

제네렉에서 파라미터화된 타입이 서로 어떤 관계에 있는 지 설명하는 개념

- ﻿﻿변성은 크게 공변성, 반공변성 그리고 무공변성으로 나뉜다
- ﻿﻿이펙티브 자바에선 공변성과 반공변성을 설명할때 PECS 규칙을 언급한다
- ﻿﻿PECS는 Producer-Extends, Consumer Super의 약자입니다
- ﻿﻿공변성은 자바 제네릭의 extends 키워드이고 코틀린에선 out
- ﻿﻿반공변성은 자바 제네릭의 super 키워드이고 코틀린에선 in

### 변성이 왜 필요한가요?

제네릭 타입을 사용할 때, **종종 우리는 하위 타입 관계(subtyping)를 유지하면서 타입을 더 유연하게 사용하고 싶습니다**

. 예를 들어, 모든 고양이는 동물입니다(`Cat`은 `Animal`의 하위 타입). 그렇다면 고양이 리스트도 동물 리스트로 간주될 수 있어야 합니다. 

이게 바로 "공변성"이 필요한 이유입니다

## 2.1 공변성  Covariance

공변성은 하위 타입 관계가 유지되는 방식으로 타입을 사용할 수 있게 합니다. 

즉, `Type<B>`가 `Type<A>`의 하위 타입이면, `B`가 `A`의 하위 타입일 때, `Type<B>`는 `Type<A>`의 하위 타입입니다. 

코틀린에서는 `out` 키워드를 사용해 이를 선언합니다.

```kotlin
class MyGenerics<T>(val t: T)

fun main() {
		val generics = MyGenerics<String>("테스트")
		val charGenerics : MyGenerics<CharSequence> = generics // 컴파일 오류
}
```

- 현재 코드에서 실수로 String이 아닌 타입을 넣을 경우를 막으므로 의미 있는 컴파일 오류
   지만 특정 상황에선 공변성이 필요할 수 있다
- ﻿﻿공변성 키워드인 out 을 사용해서 해결한다

```kotlin
class MyGenerics<out T>(val t: T)
	
fun main() {
	val generics = MyGenerics<String>("테스트")
	val anygenerics : MyGenerics<CharSequence> = generics
}
```

* 공변성은 CharSeqeunce가 String의 상위 타입일때 `MyGenerics<CharSequence>`가
`MyGenerics<String>`의 상위 타입이므로 공변성이다

## 2.2 반공변성 Contravariance

반공변성은 공변성의 반대로 작용합니다. `Type<A>`가 `Type<B>`의 하위 타입이면, `B`가 `A`의 하위 타입일 때, `Type<B>`는 `Type<A>`의 상위 타입입니다. 코틀린에서는 `in` 키워드로 이를 선언합니다.

* 더 일반적인 타입(동물)을 입력으로 받을 수 있는 함수가 더 특정한 타입(개)도 처리할 수 있게 하는 것입니다.

```kotlin
class Bag<T> {

  fun saveAll( to: MutableList<T>, from: MutableList<T>) {
		to.addAll(from)
	}
} 

fun main() {
	val bag = Bag<String>()
	// in이 없다면 컴파일 오류
	bag.saveAll(mutableListOf<CharSequence>(""), mutableListOf<String>(""))
}
```

이때 반공변성 키워드인 in 을 사용해 상위 타입도 전달 받을 수 있도록 한다

* 공변성과는 반대로 String이 CharSequence의 하위 타입일때` Bag<String>`가
`Bag<CharSequence>`의 상위 타입이 되므로 반공변성이다*

## 무공변성 (Invariance)

무공변성은 제네릭 타입이 다른 타입 파라미터에 대해 그대로 사용될 때를 의미합니다. 즉, `Type<A>`와 `Type<B>`는 서로 다른 타입입니다. **대부분의 클래스와 인터페이스는 기본적으로 무공변입니다.**



# 지연초기화 (lazy init)

지연초기화 는 대상에 대한 초기화를 미뤘다가 실제 사용시점에 초기화하는 기법을 말한다
초기화 과정에서 자원을 많이 쓰거나 오버헤드가 발생할 경우 지연초기화를 사용하는게 유리 할 수 있다

지연초기화는 많은 상황에서 쓰이고 있다

* 웹페이지에서 특정 스크롤에 도달했을때 컨텐츠를 보여주는 무한 스크롤
* 싱글톤 패턴의 지연초기화

초기화 비용이 크거나, 생성 시점에 필요한 모든 정보가 준비되지 않았거나, 해당 속성이 항상 사용되지 않는 경우, 지연 초기화를 사용하면 효율적입니다. 이는 초기 부하를 줄이고 애플리케이션의 시작 시간을 단축할 수 있습니다.



## by lazy

변수를 선언한 시점엔 초기화 하지 않다가 특정 시점 이후 초기화가 필요한 때

때 코틀린에서 제공하는 by lazy 를 사용하면 불변성을 유지하면서 지연초기화가 가능하다.

lazy 함수 내부에 초기화 로직을 구현한다

**`lazy` 위임 속성 사용:**

- `lazy`는 `val` (불변) 속성에 사용되며, 해당 속성의 초기화를 처음 접근할 때까지 지연시킵니다.
- `lazy`는 내부적으로 동기화를 제공하며, 스레드 안전한 초기화를 보장합니다.
- `lazy` 초기화 블록은 해당 변수가 처음 호출될 때 한 번만 실행되고, 결과값은 캐싱됩니다.

```kotlin
class HelloBot {
	val greeting: String by lazy { getHello() }
	fun sayHello() = println(greeting)
} 

fun getHello() = "안녕하세요"

fun main() {
	val helloBot = HelloBot()
	// ...
	helloBot.sayHello()
} // 안녕하세요
```

* by lazy를 사용하면 사용 시점에 1회만 초기화 로직이 동작한다
* 이후에는 그대로 초기화된 그대로 사용 

* by lazy는 기본적으로 멀티-스레드 환경에서도 안전하게 동작하도록 설계되었다

* 기본 모드는 LazyThreadSafetyMode. SYNCHRONIZED 과 동일하다
  만약 LazyThreadSafetyMode. NONE 모드로 변경한 뒤 멀티-스레드 환경에서 실행하면? -> 실행할때마다 결과가 계속 달라지는 것을 볼 수 있다.

```kotlin
val greeting: String by lazy(LazyThreadSafetyMode.NONE) {
	println("초기화 로직 수행")
	getHello()
}
```

* 멀티-스레드 환경이 아닌 경우에는 동기화 작업이 오버헤드가 될 수 있으므로 NONE 모드를
  사용하고 멀티-스레드 환경이어도 동기화가 필요하지 않은 경우라면 PUBLICATION 모드를 사용



`lazy`는 `Lazy` 인터페이스를 구현하는 객체로 컴파일됩니다. 이 인터페이스는 초기화 코드를 저장하며, 처음 접근될 때만 실행되고 그 결과를 캐싱합니다. `lazy`는 동기화 메커니즘을 사용하여 멀티스레딩 환경에서도 안전하게 동작합니다.

```java
public class Lazy<T> {
    private T value = null;
    private Supplier<T> initializer;
    private final Object lock = new Object();

    public Lazy(Supplier<T> initializer) {
        this.initializer = initializer;
    }

    public T getValue() {
        if (value == null) {
            synchronized (lock) {
                if (value == null) {
                    value = initializer.get();
                }
            }
        }
        return value;
    }
}
```



## lateinit

- `lateinit`은 주로 가변(mutable) 속성에 대해 사용됩니다. (var!!!!!!!!)
- `lateinit`으로 선언된 속성은 나중에 초기화할 수 있지만, 최초 사용 전에 반드시 초기화가 되어야 합니다.
- 주로 의존성 주입, 단위 테스트 또는 초기화가 복잡한 경우에 사용됩니다.
- 가변 프로퍼티에 대한 지연초기화가 필요한 경우가 있다
  * 특정 프레임워크나 라이브러리에서 지연초기화가 필요한 경우
  * 예를 들어 테스트 코드를 작성할때 특정 애노테이션에 초기화 코드를 작성해야하는 경우가 있다

```kotlin
class `7_LateExample` {

  @Autowired
	lateinit var service: TestService
	lateinit var subject: TestTarget

  @SetUp
	fun setup() {
		subject = TestTarget()
	} 
  
  @Test
	fun test() {
		subject.doSomething()
	}
}
```

즉 lateinit은 가변이어야하며 non-null이기에 예시와 같이 사용한다



초기화 전에 사용하면 UninitializedPropertyAccessException 발생.

* 특정 DI와 같이 외부에서 초기화를 해주는 경우를 염두해두고 만든
  기능이기 때문에 초기화 전에 사용하더라도 컴파일 오류가 발생하지 않는다

초기화 여부를 파악하고 사용하려면 isInitialized 프로퍼티를 사용하라

```kotlin
class `7_LateInit` {

  lateinit var text: String	
	fun printText() {

    if (this::text.isInitialized) {
			println("초기화됨")
			println(text)
		} else {
			text = "안녕하세요"
			println(text)
		}
	}
}
```

* isInitialized는 내부에선 사용할 수 있지만 외부에선 사용 불가

```java
class MyClass {
    lateinit var heavyObject: HeavyObject

    fun initialize() {
        heavyObject = HeavyObject()
    }

    fun useObject() {
        if (::heavyObject.isInitialized) { // 초기화 확인
            heavyObject.doSomething()
        }
    }
}

```



# 스코프 함수 

코틀린의 표준 라이브러리에는 객체의 컨텍스트 내에서 코드 블록을 실행하기 위해서만 존재하는 몇가지 함수가 포함되어 있는데 이를 스코프 함수 라고 부른다

* 스코프 함수를 제대로 사용하면 불필요한 변수 선언이 없어지며 코드를 더 간결하고 읽기 쉽게 만든다
* 스코프 함수의 코드 블록 내부에서는 변수명을 사용하지 않고도 객체에 접근할 수 있는데 그 이유는 수신자 객체에 접근할 수 있기 때문이다
* 수신자 객체는 람다식 내부에서 사용할 수 있는 객체의 참조이다
* 스코프 함수를 사용하면 수신자 객체에 대한 참조로 this 또는 it 를 사용한다

스코프 함수들에 대한 속성 표

| 함수명  | 수신자 객체 참조 방법 | 반환 값       | 확장 함수 여부 |
| ------- | --------------------- | ------------- | -------------- |
| `let`   | `it`                  | 함수의 결과   | O              |
| `run`   | `this`                | 함수의 결과   | O              |
| `with`  | `this`                | 함수의 결과   | X              |
| `apply` | `this`                | 컨텍스트 객체 | O              |
| `also`  | `it`                  | 컨텍스트 객체 | O              |

- **수신자 객체 참조 방법**: 스코프 함수 내에서 수신자 객체(즉, 함수가 호출되는 대상 객체)를 참조하는 방식입니다. `this`는 현재 컨텍스트 객체를 나타내며, `it`는 현재 객체에 대한 명시적인 매개변수입니다.
- **반환 값**: 각 함수가 호출된 후에 반환하는 값의 종류입니다. 일부 함수는 처리 결과를 반환하며, 다른 일부는 컨텍스트 객체(함수가 호출된 객체)를 반환합니다.
- **확장 함수 여부**: 해당 함수가 확장 함수인지 여부를 나타냅니다. 확장 함수는 특정 타입의 객체에 함수를 추가할 수 있게 해줍니다. `with` 함수만이 확장 함수가 아닙니다.

## let

null이아닌 경우 사용될 로직을 작성하고 결과를 반환하고 싶을때 사용

## run

수신 객체의 프로퍼티를 구성하거나 새로운 결과를 반환하고 싶을 때

* let을 사용할 순 있으나 it을 사용해야하기 때문에 불편

## with

결과 반환 없이 내부에서 수신 객체를 이용해 다른 함수를 호출하고 싶을때 사용

* 다른 스코프 함수와 다른 점은 with는 확장 함수가 아니다

## apply

수신 객체 프로퍼티를 구성하고 수신객체 그대로 결과로 반환하고 싶을때 사용 

* let, run, with는 함수의 결과가 반환타입으로 변환되는데 반해서 apply는 수신객체 그대로 반환된다

## also

부수 작업을 수행하고 전달받은 수신 객체를 그대로 결과로 반환하고 싶을때 사용 

```
fun main() {
	User(name = "tony", password = "1234").also {
		it.validate()
	}
}
```

## 스코프 함수 사용시 유의할점

이와 같은 스코프 함수는 모두 기능이 유사하기 때문에 실무에선 섞어쓰는 경우도 많다

* this는 키워드이다 키워드는 사전에 정의된 예약어이기 때문에 다른 의미로 사용할 수 없지 만 it은 특정 용도에서만 작동하는 소프트 키워드이기 때문에 다른 용도로 사용할 수 있다



# 코틀린 고급 예외처리

## try-with-resources를 use라는 확장함수를 이용해 사용

```kotlin
FileWriter("test.txt").use { it.write("Hello Kotlin") }
```

## runCatching을 사용해 우아하게 예외 처리하기

코틀린은 try-catch를 통한 예외처리외에도 함수형 스타일의 Result 패턴 을 구현한 runCatching 을 제공한다

* Result 패턴이란 함수가 성공하면 캡슐화된 결과를 반환하거나 예외가 발생하면 지정한 작업을 수행하는 패턴이다

```kotlin
fun getStr(): Nothing = throw Exception("예외 발생 기본 값으로 초기화")

fun main() {
	val result = try {
		getStr()
	} catch (e: Exception) {
		println(e.message)
		"기본값"
	}
  
  println(result)
} 
```

이것을

```kotlin
val result2 = runCatching { getStr() }
		.getOrElse {
			println(it.message)
			"기본값"
		}
println(result2)
```

runChaing 내부 구현

```kotlin
public inline fun <R> runCatching(block: () -> R): Result<R> {

  return try {
    Result.success(block())
	} catch (e: Throwable) {
		Result.failure(e)
	}
}
```



- `getOrElse`는 함수가 예외를 던질 때, 제공된 람다를 실행하여 기본 값을 반환합니다.
- `getOrThrow`는 성공적으로 값이 반환되면 그 값을, 실패하면 예외를 던집니다.
- `map`은 성공적인 결과에 대해 변환 작업을 수행합니다.
- `mapCatching`은 `map`과 유사하지만, 변환 중 예외가 발생할 경우 이를 잡아내고 다룰 수 있습니다.
- `recover`는 실패한 작업을 회복하기 위해 사용되며, 예외가 발생한 경우 원하는 값으로 대체할 수 있습니다.
- `recoverCatching`은 `recover`와 유사하지만, 회복 함수 내에서도 예외 처리가 가능합니다.

```kotlin
// getOrElse : 실패 시 Throwable을 수신하고 함수의 결과를 반환
val result1 = runCatching { getStr() }
    .getOrElse { throwable -> 
        "기본 값" // 예외 발생 시 "기본 값"으로 초기화
    }
println(result1) // 출력: 기본 값

// getOrThrow : 성공 시 값 반환, 실패 시 예외 발생
try {
    val result2 = runCatching { getStr() }
        .getOrThrow()
    println(result2)
} catch (e: Exception) {
    println("Exception in thread \"main\" $e") 
}

// map : 성공 시 원하는 값으로 변경
val result3 = runCatching { "안녕" }
    .map { it + "하세요" }
    .getOrThrow()
println(result3) // 출력: 안녕하세요

// map에서 예외 발생 시
val result4 = runCatching { "안녕" }
    .map { getStr() }
    .getOrDefault("기본값")
println(result4) // 출력: 기본값

// mapCatching : map과 같으나 예외 발생 시 재처리 가능
val result5 = runCatching { "안녕" }
    .mapCatching { getStr() }
    .getOrDefault("기본값")
println(result5) // 출력: 기본값

// recover : 실패 시 원하는 값으로 변경
val result6 = runCatching { "정상" }
    .recover { "복구" }
    .getOrNull()
println(result6) // 출력: 정상

// 실패 시
val result7 = runCatching { getStr() }
    .recover { "복구" }
    .getOrNull()
println(result7) // 출력: 복구

// recoverCatching : recover와 같으나 내부에서 예외가 발생할 경우 재처리 가능
val result8 = runCatching { getStr() }
    .recoverCatching { getStr() }
    .getOrDefault("복구")
println(result8) // 출력: 복구

```



# 리액티브와 코루틴

리액티브가 코루틴으로 변환되는 방식
```kotlin
//Mono → suspend
fun handler(): Mono<Void> -> suspend fun handler()
//Flux → Flow
fun handler(): Flux<T> -> fun handler(): Flow<T>
```

* Mono는 suspend 함수로 변환
* Flux는 Flow로 변환



R2DBC를 코루틴으로

```kotlin
interface ContentReactiveRepository : ReactiveCrudRepository<Content, Long> {
    fun findByUserId(userId: Long): Mono<Content>
    fun findAllByUserId(userId: Long): Flux<Content>
}

class ContentService(
    val repository: ContentReactiveRepository
) {
    fun findByUserIdMono(userId: Long): Mono<Content> {
        return repository.findByUserId(userId)
    }

    suspend fun findByUserId(userId: Long): Content {
        return repository.findByUserId(userId).awaitSingle()
    }
}
```

CoroutineCrudRepository 를 사용하면 awaitXXX 코드 없이 사용 가능

```kotlin
interface ContentCouroutineRepository : CoroutineCrudRepository<Content, Long> {
    suspend fun findByUserId(userId: Long): Content?
    fun findAllByUserId(userId: Long): Flow<Content>
}

class ContentService(
    val repository: ContentCouroutineRepository
) {
    suspend fun findByUserId(userId: Long): Content? {
        return repository.findByUserId(userId)
    }
}
```

