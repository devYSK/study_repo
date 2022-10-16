# 도서관리 애플리케이션 



# 테스트 코드란 무엇인가
* 프로그래밍 코드를 사용해 무엇인가를 검증한다
* 자동으로 (사람의 손을 거치지 않고) 테스트를 할 수 있다!

* 리팩토링을 해야 코드의 품질이 유지되고, Business가 안정적으로 유지되는데, 리팩토링을 할때마다 사람이 직접 검증한다면.. 범위가 너무 넓고 비용이 많이 든다.
## 테스트 코드의 장점 
1. 개발 과정에서 문제를 미리 발결한 수 있다.
2. 기능 추가와 리팩토링을 안심하고 할 수 있다.
3. 빠른 시간 내 코드의 동작 방식과 결과를 확인할 수 있다.
4. 좋은 테스트 코드를 작성하려 하다보면, 자연스럽게 좋은 코드가 만들어진다.
5. 잘 작성한 테스트는 문서 역할을 한다.(코드 리뷰를 돕는다.)



# 자바 프로젝트에서 코틀린 코드 사용하는법

1. plugin 필요
  *  id 'org.jetbrains.kotlin.jvm' version '1.6.21'
```gradle
plugins {
    id 'org.springframework.boot' version '2.6.8'
    id 'io.spring.dependency-management' version '1.0.11.RELEASE'
    id 'java'
    id 'org.jetbrains.kotlin.jvm' version '1.6.21'
}
```

2. dependencies 필요
  *  implementation 'org.jetbrains.kotlin:kotlin-stdlib-jdk8'

```gradle
dependencies {
    implementation 'org.jetbrains.kotlin:kotlin-stdlib-jdk8'
}
```

3. compileKotlin 필요
  * kotlinOptions {jvmTarget = "11"}

```gradle
compileKotlin {
    kotlinOptions {
        jvmTarget = "11"
    }
}
```

4. compileTestKotlin 필요

```gradle
compileTestKotlin {
    kotlinOptions {
        jvmTarget = "11"
    }
}
```

* 다음 src/main/kotlin 이름으로 디렉토리를 만든다.
  * src/test/kotlin도 마찬가지

* 그리고 kotlin Directory에 마우스를 올리고 우클릭 > `Mark Directory as` > `Source Root` 를 클릭
  * 폴더가 파란색이거나 초록색이 아니라면 실행. intelij idea에서! 


![](.note_images/6a019c42.png)


* 그러면 폴더의 색이 변한다. 
![](.note_images/599c5824.png)


* 다음, src/main/java/ 와 동일하게 src/main/kotlin 내부에 프로젝트 패키지를 만든다.

테스트 메소드를 들여다보면, 크게 3가지 과정으로 구성되어 있는 것을 확인할 수 있다.
1. 테스트 대상을 만들어 준비하는 과정
2. 실제 우리가 테스트 하고 싶은 기능을 호출하는 과정
3. 호출 이후 의도한대로 결과가 나왔는지 확인하는 과정

* 이것을 given - when - then 패턴이라고 한다


수동으로 만든 테스트 코드에는 다음과 같은 단점이 있다.
1. 테스트 클래스가 많아지고 메소드가 생길 때마다 메인 메소드에 수동으로 코드를 작성
해 주어야 하고, 메인 메소드가 아주 커질 것이다.
    * 테스트 메소드를 개별적으로 실행하기도 어렵다.
2. 테스트가 실패한 경우 무엇을 기대하였고, 어떤 잘못된 값이 들어와 실패했는지 알려주
지 않는다. 또한 예외를 던지거나, try catch를 사용해야 하는 등 직접 구현해야 할 부분
이 많다.
3. 테스트 메소드별로 공통적으로 처리해야 하는 기능이 있다면, 메소드마다 중복이 생기
게 된다.
   * 예를 들어, 현재 모든 테스트 메소드에서는 5를 초기값으로 가지고 있는 계산기를
만들어 준다.



이러한 단점을 극복하기 위해 테스트 프레임워크가 등장하였는데, Java-Kotlin 진영에서 가
장 많이 사용되는 Juni5를 알아보자


# 코틀린에서의 Junit5


```kotlin
import org.junit.jupiter.api.*

class JunitTest {

    companion object {
        @JvmStatic
        @BeforeAll
        fun beforeAll() {
            println("모든 테스트 시작 전")
        } @JvmStatic
        @AfterAll
        fun afterAll() {
            println("모든 테스트 실행 후")
        }
    }

    
    @BeforeEach
    fun beforeEach() {
        println("각 테스트 시작 전")
    }

    @AfterEach
    fun afterEach() {
        println("각 테스트 실행 후")
    }

    @Test
    fun test1() {
        println("테스트 1")
    }

    @Test
    fun test2() {
        println("테스트 2")
    }
}
```

### 각 어노테이션은 다음과 같은 의미를 가진다.
* @Test : 테스트 메소드를 지정한다. 테스트 메소드를 실행하는 과정에서 오류가 없으면
성공이다.

* @BeforeEach : 각 테스트 메소드가 수행되기 전에 실행되는 메소드를 지정한다.

* @AfterEach : 각 테스트가 수행된 후에 실행되는 메소드를 지정한다.

* @BeforeAll : 모든 테스트를 수행하기 전에 최초 1회 수행되는 메소드를 지정한다.
  * 코틀린에서는 @JvmStatic 을 붙여 주어야 한다.

* @AfterAll : 모든 테스트를 수행한 후 최후 1회 수행되는 메소드를 지정한다.
  * 코틀린에서는 @JvmStatic 을 붙여 주어야 한다

* 테스트 순서

![](.note_images/e0680bf1.png)

Junit5를 사용하면, 메소드 단위로 테스트를 실행시킬 수 있고 클래스 단위로 테스트를 실행
시킬 수도 있다.


* 단언문은 assertThat(확인하고싶은값) 으로 시작하고 뒤에 .isEqualTo( ) 가 붙게 된다.
* 이때 isEqualTo 는 정확히 동일한 것을 기대한다는 의미이다



### 자주 사용되는 단언문을 몇 가지

* 주어진 값이 true인지 / false인지 검증한다.
```kotlin
val isNew = true
assertThat(isNew).isTrue
assertThat(isNew).isFalse
```

* 주어진 컬렉션이 size가 원하는 값인지 검증한다.
```kotlin
val people = listOf(Person("A"), Person("B"))
assertThat(people).hasSize(2)
```

* 주어진 컬렉션 안의 item 들에서 name 이라는 프로퍼티를 추출한 후 (extracting), 그 값을 검증한다.
    * 이때 순서는 중요하지 않다.

```kotlin
val people = listOf(Person("A"), Person("B"))
assertThat(people).extracting("name").containsExactlyInAnyOrder("A", "B")
```

* 주어진 컬렉션 안의 item 들에서 name 이라는 프로퍼티를 추출한 후 (extracting), 그값을 검증한다.
    * 이때 순서도 중요하다.

```kotlin
val people = listOf(Person("A"), Person("B"))
assertThat(people).extracting("name").containsExactly("A", "B")
```

* 함수( function1() )를 실행했을 때 원하는 예외가 나오는지 검증한다.
```kotlin
assertThrows<IllegalArgumentException> {
  function1()
}
```

* 예외 메시지까지 검증할 수 있다.

```kotlin
val message = assertThrows<IllegalArgumentException> {
  function1()
}.message
assertThat(message).isEqualTo("잘못된 값이 들어왔습니다")
```

## Kotlin No-arg-constructor

```
Class 'Book' should have [pubilc, protected] no-arg constructor
```
* 이 에러가 발생하는 이유는 다음과 같다.
    * JPA를 사용하기 위해서는 아무런 argument를 받지 않는 기본 생성자가 필요하다

* 하지만 코틀린 코드에서는 ‘주 생성자'를 만들 때 프로퍼티를 함께 만들어주는 방식을 사
용함으로써, 아무런 argument를 받지 않는 기본 생성자가 존재하지 않는다.

* 이 에러를 해결해주기 위해서는 다음과 같은 kotlin-jpa 플러그인이 필요하다


```gradle
// build.gradle
plugins {
id "org.jetbrains.kotlin.plugin.jpa" version "1.6.21" 
}
```

## 코틀린에러

코틀린으로 스프링 사용 시 ClassNotFoundException: kotlin.reflect.full.KClasses

에러시 kotlin-reflect 의존성을 추가해주면 해결된다.


```
이 에러는 코틀린 클래스에 대한 리플렉션을 할 수 없어 발생하는데, 이를 해결하기 위해
Kotlin 리플렉션 라이브러리를 넣어주어야 한다. 리플렉션이란, 클래스나 메소드 등을 런타
임으로 제어하기 위한 기술을 의미한다
```

```xml
<!-- https://mvnrepository.com/artifact/org.jetbrains.kotlin/kotlin-reflect -->

<dependency>

    <groupId>org.jetbrains.kotlin</groupId>

    <artifactId>kotlin-reflect</artifactId>

    <version>사용 버전에 맞게</version>

</dependency>
```

```gradle
dependencies {
    implementation 'org.jetbrains.kotlin:kotlin-reflect'
}
```









































