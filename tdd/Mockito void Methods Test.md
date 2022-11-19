# Mockito Void Method 테스트 방법



> Mockito 프레임워크에서 사용되는 [Mockito Verify](https://www.baeldung.com/mockito-verify) , [Mockito When/Then](https://www.baeldung.com/mockito-behavior) 및 [Mockito의 Mock Methods](https://www.baeldung.com/mockito-mock-methods) )와 함께 사용한다.



Test 코드를 작성하다가, Mocking을 하고 Mocking한 method가 void 일때 Mocking이 안되는 경우를 보았을것이다.  


Mockito를 사용해서 Void 메소드를 실행, 또는 exception을 Throw 할 수 있다.  




> 일반적으로 mocking하는 것과 크게 다르지 않다. 

  


## how?



* Mockito.doNothing()

```java
모킹할 클래스 클래스 = mock(모킹할 클래스.class);

Mockito.doNothing()
  .when(모킹할 클래스)
  .사용할메소드(매개변수) // 매개변수는 줘도되고 안줘도 된다.

verify(모킹한 클래스).사용할메소드(매개변수);
```

  


* Mockito.doThrow()

```java
모킹할 클래스 클래스 = mock(모킹할 클래스.class);


Mockito.doThrow(예외.class)
  .when(모킹한 클래스)
  .사용할 메소드(매개변수) // 매개변수는 줘도되고 안줘도 된다.

verify(모킹한 클래스).사용한메소드(매개변수)
```





## 예제 자바 코드



## 1. 메소드 실행 테스트 

```java
public class MockTest {

    static class TestClass {

        public void print(int value) {
            System.out.println(value);
        }

    }

    @Test
    void voidMethodTest() {
        TestClass mockTestClass = mock(TestClass.class);

        int value = 10;

        doNothing().when(mockTestClass)
            .print(value);

        mockTestClass.print(value);

        verify(mockTestClass)
            .print(value);
    }

}

```



## 2. 메소드 실행시 예외 테스트



```java
public class MockTest {

    static class TestClass {

        public void print(int value) {
          if (value == 10) {
                throw new IllegalArgumentException();
            }
          
            System.out.println(value);
        }

    }

    @Test
    void voidMethodThrowsTest() {
        TestClass mockTestClass = mock(TestClass.class);

        int value = 10;

        doThrow(IllegalArgumentException.class).when(mockTestClass)
            .print(value);

        assertThrows(IllegalArgumentException.class, () -> mockTestClass.print(value));

        verify(mockTestClass)
            .print(value);
    }

}
```



## 참조



* https://stackoverflow.com/questions/16243580/mockito-how-to-mock-and-assert-a-thrown-exception
* https://www.baeldung.com/mockito-void-methods
