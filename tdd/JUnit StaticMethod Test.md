# JUnit5 Static Method 테스트 방법 - Mocking



먼저 방법부터 보고싶다면 여기로 이동하자.  
[Mocking하는 방법은 다음과 같다.](#Mocking하는-방법은-다음과-같다.)

* [API 문서](https://javadoc.io/static/org.mockito/mockito-core/3.4.0/org/mockito/Mockito.html)

# **java 에서 static 이란**

 

* static :**정적인, 움직임이 없는, 고정된**   
  

자바나 여러 언어에서는 static 이라는 키워드가 존재한다.  
 주로 사용하는 의미는 정적인, 미리 선언하여 공유된 메모리의 영역을 사용할 때 사용하는 키워드  쯤으로 말할 수 있다.

* block : {} 
* variable  
* method
* class

 

static 을 사용하는 이유는 바로 **Shared Resource**, 즉 공유를 위해서 사용한다.  
 

static 으로 선언한 코드는 JVM 메모리영역에 static 영역에 올라가게 되는데, 
JVM 상 동적 메모리 할당의 heap 영역이나 변수의 stack 영역에 그 만큼 여유 공간이 생겨 메모리 관리 측면에서는에서는 좋은 선택지라고 볼 수도 있다.  

또한 정적 메서드로 만들면 `클래스를 메모리에 로드하는 시점`에 메서드가 결정되므로 **인스턴스를 만들지 않아도 된다는 장점**이 있다.

그런 이유로 static method는 주로 Input/Output 이 명확하고 functional 한   
util class의 method 혹은 static constructor 로 사용되곤 한다.

  


그러나 static의 단점은, 공유되는 메모리로 인한  race condition(경쟁 상태), mutex(상호 배제), locking(락킹) 이다.  


static을 사용할 때는 항상 이 단점들을 고려하고 사용해야 한다. 

  


## 단위 테스트와 Static Method

통합 테스트와 다르게 단위 테스트에서는, 의존관계에 있는 다른 컴포넌트들에 대해서 적절한 Test Double이 필요하다.  


* **테스트 더블** : 테스트하려는 객체와 연관된 객체를 사용하기가 어렵고 모호할 때 대신해 줄 수 있는 객체 

* 테스트 더블은 크게 **Dummy**, **Fake**, **Stub**, **Spy**, **Mock** 이 있다.

  


테스트 시에, 의존관계에 있는 다른 컴포넌트의 메소드가 static method 라면 테스트를 하기 어렵다.

왜냐하면 JUnit이나 Mockito를 사용해서 모킹하기 전에 이미 메모리에 올라가있기 때문이다.

또한 mockito 에서는 mock 객체를 만들기 위해서 다이나믹 code 생성을 위해서 cglib 를 사용한다.  
이것은 런타임에 클래스를 상속하게 되는데 static 멤버들에 대해서는 재정의가 불가능하기 때문이다.    

   


결국 static method 를 재정의하기 위해서는 runtime 의 byte code 조작이 필요한 것이다.

* https://stackoverflow.com/questions/4482315/why-doesnt-mockito-mock-static-methods



또한, 사실 static method 를 mocking 하겠다는 것은 **설계가 잘못 되었을 가능성이 높다**. 

따라서 테스트 가능성을 높이기 위해 코드를 리팩터링할 수 있는지 항상 고민하고 생각해봐야 한다.    
물론 이것이 항상 가능한 것은 아니라서 때로는 정적 메서드를 mocking할 수 밖에 없곤 한다.  
그러므로 정적 메소드(static method)를 mocking하는 방법을 보자.

<br>

다음의 의존성이  필요하다.

```xml
<dependency>
  <groupId>org.mockito</groupId>
  <artifactId>mockito-inline</artifactId>
  <scope>test</scope>
</dependency>

```



# static method mocking



* [API 문서](https://javadoc.io/static/org.mockito/mockito-core/3.4.0/org/mockito/Mockito.html)

Mockito 3.4.0부터 `Mockito.mockStatic( Class<T> classToMock )메서드`를 사용하여 정적 메서드 호출에 대한

mocking을 할 수 있다.   


이 메서드는 범위가 지정된 모의 개체인 유형에 대한 `MockedStatic` 개체를 반환한다  
  


단위테스트에서 mocking된 static method는 범위가 한정되어있다.

스레드 로컬 상의 명시적 범위를 가진다.

범위가 지정되어있는 mocking은 이 mocking을 하는 곳에서 mocking을 하여 자원을 열고 닫아줘야 한다.

테스트 실행 중에 정적 메서드 호출을 가지고 장난을 치거나 남용한다면 테스트 실행의 동시적이고 순차적인 특성으로 인해 테스트 결과에 악영향을 미칠 가능성이 높다.

그래서 static method를 mocking하게 된다면 지정한곳에서 자원 할당한 것을  반드시 `해제 해 줘야 한다`

`안그러면 다른 테스트 코드에 까지 영향을 미치기 때문이다. `  



> 자원할당 해제는 close() 메소드를 사용하거나 try-wth-resource를 사용하면 된다. 

<br>

* 다른 테스트 코드에서도 Mocking이 유지되어 에러가 발생하거나 원하는 결과를 얻을 수 없다. 
* 한 스레드에서 staticMocking 등록은 한번밖에 못하므로, 생략할경우 아래와 같은 에러메시지를 만난다



> ```
> org.mockito.exceptions.base.MockitoException: 
> For utils.Randoms, static mocking is already registered in the current thread
> 
> To create a new mock, the existing static mock registration must be deregistered
> ```

  


### Mocking하는 방법은 다음과 같다.



```java
MockedStatic<모킹할 메소드를 가진 클래스> mockedStaticClass = Mockito.mockStatic(모킹할 클래스.class);


given(모킹할 메소드를 가진 클래스.메소드명()).willReturn(모킹할 값); 
// 또는
when(모킹할 메소드를 가진 클래스.메소드명()).thenReturn(모킹할 값);

mockedStaticClass.verify(() -> 모킹할 메소드를 가진 클래스.모킹한메소드()); // 모킹한 객체를 이용하여 verify

mockedStaticClass.close() // 반드시 close 
```

* close() 후에는 verify 할 수 없다.

​    


## 매개변수가 없는 static method mocking

<br>

매개 변수가 없는 static method를 테스트 해야 할 경우에 사용한다.  
다음은 현재 쓰레드 이름을 반환하는 간단한 static method이다.    

이 예제는 그냥 아주 간단하게 하기 위해 사용한것이다. 

```java
class StringUtil {

    private StringUtil() {
    }

    public static String name() { // 정해진 값을 리턴하는 static method  
        return "name";
    }

}
```

<br>

위 static method를 다음과 같이 mocking 할 수 있다.

<br>

```java
class MockStaticTest {

    @Test
    void staticMockingTest() {
      
        // 할당
        MockedStatic<StringUtils> mockedStaticClass = Mockito.mockStatic(StringUtils.class);


        when(StringUtils.name())
            .thenReturn("otherName");

        assertEquals("otherName", StringUtils.name());
		
	      mockedStaticClass.verify(StringUtils::name); // 검증
        mockedStaticClass.close(); // 반드시 해제 
    }
}   
```



`Mockito.mockStatic()` 을 사용하여 클래스를 모킹한 다음, when/thenReturn 또는 given/willReturn 으로 모킹할 수 있다. 
이 때 반드시 사용한 후에는 close() 해서 자원 할당을 해제해줘야 하며,    

MockedStatic 인터페이스와 그를 사용하는 구현체는 `AutoCloseable`이 구현되어 있으므로    
다음과 같이 `try-with-resource`로 사용할 수도 있다. 



```java
class MockTest {

    @Test
    void staticMockingTryWithResources() {

        try (MockedStatic<StringUtils> mockedStaticClass = Mockito.mockStatic(StringUtils.class)) {
            when(StringUtils.name())
                .thenReturn("otherName");

            assertEquals("otherName", StringUtils.name());
 	          mockedStaticClass.verify(StringUtils::name); // 검증 
        }
    }
}
```



 

## 매개변수가 있는 static method mocking



예제 class

```java
public class IntCalculatorUtil {

    private IntCalculatorUtil() {}

    public static int add(int a, int b) {
        return a + b;
    }

    public static int subtract(int a, int b) {
        return a - b;
    }
}
```



```java
public class MockTest {
     @Test
    void staticMockingTest2() {

        int a = 5;
        int b = 5;

        try (MockedStatic<IntCalculatorUtil> mockedStaticClass = Mockito.mockStatic(IntCalculatorUtil.class)) {
            when(IntCalculatorUtil.add(a, b))
                .thenReturn(a + b);

            assertEquals(a + b, IntCalculatorUtil.add(a , b));

            mockedStaticClass.verify(() -> IntCalculatorUtil.add(a, b));
        }


    }
}
```



## 결론



* 반드시 정적 메소드(static method)를 이용해야 하나 한번 다시 설계를 살펴보자.

- mockito 3.4.0 버전 이상에서 제공하는 mockStatic을 이용해 static method를  모킹할 수 있다.
- 자원이 할당된 후에 자원 할당 해제가 중요하다. 안그러면 충돌을 일으킨다. 

```java
MockedStatic<StaticClass> mockedStaticClass = Mockito.mockStatic(StaticClass.class)

mockedStaticClass.close()
```

*  AutoCloseable이 구현되어있으니, 가능하면` try-with-resource를` 이용하자.



## 참조



* https://www.baeldung.com/mockito-mock-static-methods
* https://groups.google.com/g/mockito/c/wjd9jIj_oBs
*  https://wonit.tistory.com/631
* https://www.digitalocean.com/community/tutorials/mockito-mock-static-method-powermock
