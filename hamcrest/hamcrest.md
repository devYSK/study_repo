# Hamcrest 



> `- wikipedia`
>
> **Hamcrest** 는 [Java ](https://en.wikipedia.org/wiki/Java_(programming_language))[프로그래밍 언어로](https://en.wikipedia.org/wiki/Programming_language) 소프트웨어 테스트 작성을 지원하는 프레임워크입니다.  
> 사용자 지정 Assertion Matcher('Hamcrest'는 '매처'의 [아나그램](https://en.wikipedia.org/wiki/Anagram) ) 생성을 지원하여 일치 규칙을 [선언적](https://en.wikipedia.org/wiki/Declarative_programming) 으로 정의할 수 있습니다 . [[1\]](https://en.wikipedia.org/wiki/Hamcrest#cite_note-1) 이러한 매처는 [JUnit](https://en.wikipedia.org/wiki/JUnit) 및 jMock 과 같은 [단위 테스트 프레임워크에서 사용됩니다. ](https://en.wikipedia.org/wiki/Unit_testing)



* [공식문서](https://hamcrest.org/)

* [공식문서 - Java](https://hamcrest.org/JavaHamcrest/)

* [공식문서 - javadoc](https://hamcrest.org/JavaHamcrest/javadoc/2.2/)



* assert : 검증, 단언하다. 즉 테스트할 때 검증(assert). **실행한 코드가 설계한대로 정확하게 동작했는지를 검증한다**.



[검증부는 하드코딩 하는게 좋다고 한다? - 향로님(jojoldu)](https://jojoldu.tistory.com/615)



간결한 코드로 객체의 상태를 테스트할 수 있다.  
또한, 테스트가 실패되었을 때, 어떤 이유로 테스트가 실패하였는지 자세한 Failure 메시지를 출력한다.

> Hamcrest라는 용어는 단순히 Matchers의 알파벳 위치를 변경하여 만들어졌다.



JUnit 4 라이브러리에 포함되어 있었는데 JUnit 5로 오면서 빠지게된 라이브러리이다.  


spring-boot-starter-test 라이브러리에 포함되어 있으므로  spring-boot-starter-test를 사용하지 않을 경우 의존성 추가해야 한다.



 

## Hamcrest를 사용하는 이유

Junit의 Assert를 이용한 테스트와 Hamcrest를 이용한 테스트를 비교하면서, Hamcrest를 사용하는 이유에 대해서 알아보자.

- Failure 메시지의 가독성
- 테스트 코드의 가독성
- 다양한 Matcher 제공



## Failure 메시지의 가독성

아래 코드는 Junit에서 제공하는 기본적인 Assert로 변수 a와 b가 다른지 체크하는 테스트이다

#### 👉 JUnit Assertions 

```java
@Test
public void test_using_junit() {
    int a = 10;
    int b = 10;

    assertNotEquals(a, b);
}
```

위 테스트는 당연히 실패하게 되는데, 실패 메시지를 보면 뭐가 잘못되었다는 것인지 한눈에 들어오지 않는다다. 

* <org.opentest4j.AssertionFailedError: expected: not equal but was: <10>> 라는 메시지 때문에!     

로그와 함께 코드를 보면, a와 b의 값이 같아서 테스트가 실패되었구나 이해하게 됩니다.

```txt
expected: not equal but was: <10>
org.opentest4j.AssertionFailedError: expected: not equal but was: <10>
```

  


위의 Assertions 코드는 다음과 같이 Hamcrest를 사용하는 테스트로 구현할 수도 있다.

#### 👉 Hamcrest Assertions

```java
@Test
public void test_using_hamcrest() {
    int a = 10;
    int b = 10;

    assertThat(a, is(not(equalTo(b))));
}
```

이 테스트도 당연히 실패하게 되는데, 실패 로그를 보면 위의 로그보다 이해하기가 편합니다.

* 기대했지만, 그러나 라고 간결하게 나온다. 

```txt
Expected: is not <10>
     but: was <10>
```

  


## 테스트 코드의 가독성 (1)

`assertNotEquals()`는 Junit에서 기본적으로 제공하는 Assert이다.   
이 Assert는 인자로 전달된 a와 b가 다른지 확인한다.

#### 👉 JUnit Assertions 

```java
assertNotEquals(a, b);
```

다음은 Hamcrest를 사용하여 위와 동일한 조건을 체크하는 테스트 코드이다.  
 코드를 보면 위와 다르게 완벽한 `영어 문장`이 된다.
#### 👉 Hamcrest Assertions

```java
assertThat(a, is(not(equalTo(b))));
```

어서 약간 가독성이 떨어진다고 생각할 수 있지만, 다음과 같이 `is()`를 제거해도 결과는 동일하며, 좀 더 코드가 간결해 보인다.

#### 👉 Hamcrest Assertions

```java
assertThat(a, not(equalTo(b)));
```

## 테스트 코드의 가독성 (2)

다음은 같이 str 변수가 3개의 조건을 모두 만족하는지 테스트하는 코드이다.   
물론, 3개의 assert로 각각 테스트하는 것이 좋지만, 1개의 assert로 구현해야하는 상황이 있다고 가정.

####  👉 JUnit Assertions 

```java
public void test_allOf() {
    String str = "MyTest";
    boolean result = str.equals("MyTest")
            && str.startsWith("My")
            && str.contains("Test");

    assertTrue(result);
}
```

Hamcrest에서는 `allOf()` Matcher를 제공하며, `인자로 전달되는 모든 Matcher`가 패스해야 테스트가 성공한다,   
즉, `allOf()`는 논리 연산자에서 AND를 의미.  
 `&&` 대신에 `all Of`라는 표현이 들어가서 코드를 이해하는데 도움이 될 수 있다.  

#### 👉 Hamcrest Assertions

```java
@Test
public void test_allOf() {
    String str = "MyTest";

    assertThat(str, allOf(is("MyTest"), // << 
                startsWith("My"),
                containsString("Test")));
}
```

반대로 논리연산자 `OR`에 해당하는 Matcher는 `anyOf()`.  
 인자로 전달되는 Matcher 중에 하나만 패스되면 테스트가 패스된다.

```java
assertThat(str, anyOf(is("MyTest"),
            startsWith("Me"),
            containsString("Test")));
```

## 다양한 Matcher 제공

`a와 b의 절대값 차이가 0.5 이하`라는 것을 테스트하려면 다음과 같이 assertTrue로 구현할 수 있다.

#### 👉 JUnit Assertions 

```java
@Test
public void test_closeTo() {
    double a = 10.9;
    double b = 10.0;

    assertTrue(Math.abs(a-b) < 0.5);
}
```

하지만 이 코드는 가독성도 좋지 못하고, 실패했을 때 다음과 같이 왜 실패했는지 이해하기 어려운 로그를 출력한다.

```txt
expected: <true> but was: <false>
Expected :true
Actual   :false
```

Hamcrest에는 `closeTo()`라는 Matcher를 제공하고, 이것을 이용하여 `절대값의 차이가 0.5 이하인지 테스트` 할 수 있다.

#### 👉 Hamcrest Assertions 

```java
@Test
public void test_using_hamcrest3() {
    double a = 10.9;
    double b = 10.0;

    assertThat(a, closeTo(b, 0.5));
}
```

테스트가 실패했을 때도, 왜 실패했는지에 대한 로그가 출력된다. 

```txt
Expected: a numeric value within <0.5> of <10.0>
     but: <10.9> differed by <0.40000000000000036> more than delta <0.5>
```



Hamcrest에는 `closeTo()` 외에도 다양한 Matcher들을 제공한다 .     
API에 대한 자세한 내용은 [Hamcrest JavaDoc](http://hamcrest.org/JavaHamcrest/javadoc/2.2/)을 참고하시면 된다 .

- allOf, anyOf
- not, is
- hasEntry, hasKey, hasValue
- closeTo
- greaterThan, greaterThanOrEqualTo, lessThan, lessThanOrEqualTo
- equalToIgnoringCase, equalToIgnoringWhiteSpace
- containsString, endsWith, startsWith



## Hamcrest 패키지 구조



<img src="https://blog.kakaocdn.net/dn/kS0ni/btrQ3TCiDAu/cYoOnEnOFvtrumSTsGF16k/img.png" width=400 height=650>

  

* org.hamcrest.**core** : 오브젝트나 값들에 대한 기본적인 Matcher들

* org.hamcrest.**beans**: Java 빈(Bean)과 그 값 비교에 사용되는 Matcher들

* org.hamcrest.**collection** : 배열과 컬렉션 비교에 사용되는 Matcher들

* org.hamcrest.**number**: 숫자 비교를 하기 위해 사용되는 Matcher들

* org.hamcrest.**object** : 오브젝트와 클래스들을 비교에 사용되는Matcher들

* org.hamcrest.**text** : 문자열, 텍스트 비교에 사용되는 Matcher들

* org.hamcrest.**xml**: XML  비교에 사용되는 Matcher들



Hamcrest에는 유용한 매처 라이브러리가 제공된다. 가장 중요한 것들은 아래와 같다.

* [튜토리얼 문서](https://hamcrest.org/JavaHamcrest/tutorial)

### Core

* anything - 항상 일치하며, 테스트 중인 개체가 어떤 것이든 상관 없는 경우에 유용하다.

* describedAs - 사용자가 직접 테스트 실패에 대한 설명을 추가하는 decorator

* is - 가독성을 높이기 위한 decorator

 is matcher는 기본 matcher에 기본 동작을 추가하지 않는 wrapper일 뿐이다.  
다음 assertion은 동일하다.

```java
assertThat(theBiscuit, equalTo(myBiscuit)); 
assertThat(theBiscuit, is(equalTo(myBiscuit))); 
assertThat(theBiscuit, is(myBiscuit));
```

### Logical

* allOf - 모든 matcher가 true를 반환하면 통과 (like Java &&)

* anyOf - 적어도 하나의 matcher가 true를 반환하면 통과 (like Java ||)

* not - 랩핑된 matcher가 false를 반환하면 통과

### Object

* `equalTo` - Object.equals을 사용해 객체가 동일한지 판단한다.

* `hasToString` - Object.toString 메소드 값과 일치 여부를 판별한다.

* `instanceOf`, `isCompatibleType` - 동일 인스턴스인지 타입 비교

* `notNullValue`, `nullValue` - Null인지 아닌지 판별

* `sameInstance `- Object가 완전히 동일한지 비교. equals비교 X 주소비교 (==)

### Beans

* `hasProperty` - JavaBeans properties 테스트, 해당 property를 가지고 있는지 판단



### 컬렉션(Collection)

* `array`- 매처의 배열에 대해 배열의 요소를 테스트

* `hasEntry`, `hasKey`, `hasValue`- 항목, 키 또는 값이 포함된 맵 테스트

* `hasItem`, `hasItems`- 컬렉션에 요소가 포함되어 있는지 테스트

* `hasItemInArray`- 배열에 요소가 포함되어 있는지 테스트



### 숫자(Number)

* `closeTo`- 테스트 부동 소수점 값이 주어진 값에 가깝습니다.

* `greaterThan`, `greaterThanOrEqualTo`, `lessThan`, `lessThanOrEqualTo`- 테스트 주문



### 텍스트(문자, Text)

* `equalToIgnoringCase`- 대소문자를 무시하고 문자열 동등성 테스트

* `equalToIgnoringWhiteSpace`- 공백 실행의 차이를 무시하고 문자열 동등성 테스트

* `containsString`, `endsWith`, `startsWith`- 문자열 일치 테스트



### Custom Matchers(우리가 만들 커스텀 매쳐)



Hamcrest에는 유용한 matcher가 번들로 제공되지만 테스트 요구사항에 맞게 때때로 적절한 matcher를 생성해야 할 때가 있다.
즉, 동일한 속성들을 하나의 묶음으로써  여러 테스트 코드에서 반복되는코드가 테스팅 중인 것을 발견하고,  
이를 하나의 assertion으로 묶으려하는 경우가 될 수 있다.  

* 커스텀 matcher를 사용하면 코드 중복을 제거하고 테스트의 가독성을 증가시킬 수 있다.



다음은 값이 NaN(숫자가 아님)인지 테스트 하기 위한 커스텀 matcher를 생성한다.
아래는 수행하고자 하는 테스트 코드이다.



```java
@Test
public void testSquareRootOfMinusOneIsNotANumber() { 
  assertThat(Math.sqrt(-1), is(notANumber())); 
}
```

notANumber() 메소드를 포함하는 커스텀 matcher ` IsNotANumber`는 다음과 같다.

```java
package org.hamcrest.examples.tutorial;

import org.hamcrest.Description; 
import org.hamcrest.Matcher; 
import org.hamcrest.TypeSafeMatcher;

public class IsNotANumber extends TypeSafeMatcher {

  @Override 
  public boolean matchesSafely(Double number) { 
    return number.isNaN(); 
  }

  public void describeTo(Description description) { 
    description.appendText("not a number"); 
  }

  public static Matcher notANumber() { 
    return new IsNotANumber(); 
  }

} 
```

assertThat method는 assertion 하고자 하는 유형에 따라 매개 변수로써 Matcher를 사용하는 generic method이다.



> assertThat에서는 notANumber()메소드를 호출하지만, 실제 메소드는 IsNotANumber의 생성자를 호출하고 있다.  
> 오버라이딩된 matchesSafely 메소드가 실제로 해당 값이 NaN인지 판단하지만 이는 숨겨져있다.
> matcher가 싱글톤 객체로서 존재하고, 재활용될 수 있으므로 주의해야 한다.












## 참조

* https://hamcrest.org/JavaHamcrest/tutorial
* https://codechacha.com/ko/how-to-use-hamcrest-in-junit/