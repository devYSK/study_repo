# 목표

자바의 예외 처리에 대해 학습하세요.

# 학습할 것 (필수)

- 자바에서 예외 처리 방법 (try, catch, throw, throws, finally)
- 자바가 제공하는 예외 계층 구조
- Exception과 Error의 차이는?
- RuntimeException과 RE가 아닌 것의 차이는?
- 커스텀한 예외 만드는 방법



# 자바에서 예외 처리 방법 (try, catch, throw, throws, finally)

프로그램 실행도중에 발생하는 `에러`는 어쩔 수 없지만, `예외(Exception)`은 프로그래머가 미리 대처를 할 수 있다.

  

그러나 사용자의 원인으로 발생하는 예외는 개발자가 미리 대처를 해줄 수 있다.

​    


예외 처리(exception handling)란, 프로그래머가 예기치못한 예외의 발생에 미리 대처하는 코드를 작성하는 것으로, 실행중인 프로그램의 비정상적인 종료를 막고, 상태를 정상상태로 유지하는 것이 목적이다. 

  

### 예외처리의 목적


> 정의 - 프로그램 실행 시 발생할 수 있는 발생할 수 있는 예외에 대비한 코드를 작성하는것
>
> 목적 - 프로그램의 비정상 종료를 막고 정상적인 실행상태를 유지하는것
>
> * Error(에러클래스) 와 예외(Exception)은 모두 runtime 시에 발생하는 오류이다

  


발생한 예외를 처리하지 못하면 프로그램은 비정상적으로 종료되며 처리되지 못한 예외(uncatght exception)는 JVM의 예외처리기(UncaughtExceptionHandler) 가 받아서 예외의 원인을 화면에 출력한다. 



예외를 처리하기 하기위해서는 try - catch 문을 사용하며 그 구조는 다음과 같다.

* finally 구문로 코드가 실행된 후에 무조건 실행할 코드도 작성할 수 있다.

```java
public static void exceptionHandling(){
    try {
        // do something - 예외가 발생할 수 있는 코드
    } catch (Exception e) {
        // Exception 예외시 처리할 코드
    } catch (OtherException e2) {
        // 위에서 캐치한 Exception과 다른 OtherException 예외시 처리할 코드  
    } catch (AnotherException e3 | AnotherException2 e4) {
        // AnotherException 과 AnotherException2 예외를 같은 동작으로 처리할 코드
    }
  	finally {
        // 예외와 상관없이 항상 실행할 코드
    }
  // try-catch가 종료되면 메소드를 나온다. 
}
```

* try 블럭 ({}) 안에는 예외가 발생할 수 있는 코드를 작성한다. 
* try 블럭 다음 catch 블럭({}) (예외이름, 객체명) 1개 이상의 catch 블럭 이 올 수 있다.
  * 여러 블럭이 올 수 있지만 이중 발생한 예외의 종류와 일치하는 단 한개의 catch블럭만 수행된다. 
* 멀티 catch : `catch (AnotherException e3 | AnotherException2 e4) {`
  * JDK 1.7부터 추가된 기능.
  * " | " 기호를 사양해 여러 catch블럭을 하나로 합칠 수 있으며 합칠 수 있는 예외 클래스의 개수는 제한이 없다.
  * 그러나 같이 catch 되어 있는 예외 클래스가 부모/자식 관계, 즉 상속받은 예외클래스라면 컴파일 에러가 발생 
  * 왜냐하면, 자식 클래스로 잡아낼 수 있는 예외는 부모 클래스로도 잡아낼 수 있기 때문에 코드가 중복된 것이나 마찬가지이기 때문이다. 
  * 또한 멀티캐치는 하나의 블록으로 여러 예외를 처리하는 것이기 때문에 멀티 캐치 블록 내에서는 발생한 예외가 정확이 어디에 속한 것인지 알 수 없다. 그래서 **참조 변수 e에는 ‘|’로 연결된 예외들의 공통 조상 클래스에 대한 정보가 담긴다.**

* try 블럭만 존재할 수 없으며, catch블럭도 세트로 존재한다. 
* if문과 달리 try 블럭이나 catch 블럭 내에 포함된 문장이 하나뿐이여도 괄호{} 를 생략할 수 없다.

* catch 블럭 다음에는 finally가 올 수 있으며, finally 블럭은 예외가 발생하던 발생하지 않던 상관없이 항상 실행할 코드를  작성한다.
* 만약 try문에서 예외가 발생하게 된다면 발생한 라인 밑부분의 코드는 실행이 되지않고 catch문으로 넘어가게 된다. 이와 상관없이 항상 실행할 코드를 finally문에 작성한다



### **멀티 catch 문을 작성할때의 주의점**

> 상위 예외클래스 블럭이 하위 예외 클래스블럭보다 아래에 위치해야 한다.  
>
> 하위 예외 클래스가 이미 상위 예외 클래스에 속해있기 때문에, 상위 예외 클래스 블럭이 더 앞에있으면 하위 예외 클래스 캐치블럭을 처리하지 않고 상위 예외클래스 캐치블럭이 먼저 처리한다. 

​    

* catch 블럭에 있는  Exception Class는 자바에 있는 Exception과 Error의 superClass인 Throwable을 상속한 클래스 중 하나여야만 한다.

​    

## 참조 변수 중복

catch 블럭 안에 다시 try-catch 구문을 사용할 수 있는데, 이때 **상위 catch 블록 안에 예외 참조 변수의 이름이 중복되어서는 안 된다.**

* 변수의 스코프

```typescript
public class ExceptionDemo {
    public static void main(String[] args) {
        try {
            methodA();
        } catch (RuntimeException e) {
            try {
                methodB();
            } catch (IllegalArgumentException e) {   /// 에러 발생: 해당 변수의 이름을 e로 할 수 없음
                …
            }
        }
    }
}
```



### finally block

finally block은 try block이 끝난 후 또는 예상치 못한 에러가 발생 했을때도 실행을 보장한다.

* 하지만 만약에 JVM이 try catch block을 실행 중에 종료된다면 finally block은 실행되지 않는다.

마찬가지로 Thread가 try catch block을 실행 중에 interrupt 당하거나 kill 당한다면 finally block은 실행되지 않는다

* finally block은 cleanup code를 넣어서 resource leak(리소스 오버 등)을 막을 용도로 사용하는게 좋다.

​       



### printStackTrace(), getMessage()

예외처리를 할 때 발생한 예외의 대한 정보가 담긴 메서드이다.
catch 블럭의 참조변수를 통해 인스턴스에 접근할 수 있다. (Exception `e`) = e가 참조변수이다 

- printStackTrace()
  - 예외 발생 당시에 호출스택(CallStack)에 있었던 메서드의 정보와 예외 메시지를 화면에 출력한다.
- getMessage()
  - 발생한 예외클래스의 인스턴스에 저장된 메시지를 얻을 수 있다.



> printStaticTrace(PrintStream s) 또는 printStatckTrace(PrintWriter s)를 사용하면 발생한 예외에 대한 정보를 파일에 저장도 가능하다



  

### try-catch문에서의 흐름

* try 블럭 내에서 예외가 발생한 경우
  1. 발생한 예외와 일치하는 catch블럭이 있는지 확인
  2. 일치하는 catch 블럭을 찾게되면 그 catch블럭을 수행하고 try-catch문을 빠져나간다.
  3. 만약 일치하는 catch블럭을 차지 못하면 예외는 처리되지 못하며 메소드 밖으로 던져지게 된다

* try 블럭 내에서 예외가 발생하지 않은 경우
  1. catch블럭을 거치지 않고 finally문으로 넘어가거나, finally 문이 없다면 전체 try-catch문을 빠져나가서 다음 코드를 수행하거나 메소드는 종료된다.

  

## 예외 발생시키기

`throw` 키워드를 이용해서 개발자가 고의로 직접 예외를 발생시킬 수 있다.

* 연산자 new를 이용해서 발생시키려는 예외클래스의 객체를 만든다음에 키워드 throw 를 통해서 예외를 발생시킨다

```java
Exception e = new Exception("고의 예외");
throw e;

// 또는
throw new Exception("고의 예외");
```

* RuntimeException과 UncheckedException 은 처리 하지 않아도 실행은 되지만, CheckedException은 처리하지 않으면 컴파일이 되지 않는다.

   


## throws - 메서드에 예외 선언해서 try-catch문 사용하지 않고 예외 던지기

throws 키워드를 통해 메서드에 예외를 선언할 수 있다. 여러 개의 메서드를 쉼표로 구분해서 선언할 수 있다. 형태는 다음과 같다.

```java
void method() throws Exception1, Exception2, … ExceptionN {
    // 메서드 내용/
}
```

throws는 메서드 선언부에 예외를 선언해둠으로써,  해당 메서드를 사용하는 사람들이 어떤 예외를 처리해야 하는 지를 알려주는 역할을 한다.

  


만일, 아래와 같이 모든 예외의 최고 조상인 Exception 클래스를 메서드에  throws로 선언하면 이 메서드는 모든 종류의 예외가 발생할 가능성이 있다는 뜻이다.

```java
void someMethod() throws Exception {
  // 메서드 내용  
}
```

* 이렇게 예외를 선언하면 이 예외뿐만 아니라 그 자손타입의 예외까지도 발생할 수 있다는 점에 주의해야한다.  


throws 자체는 예외의 처리와는 관계가 없다.   

throws는 해당 메서드에서 예외를 처리하지 않고, 해당 메서드를 사용하는 쪽이 예외를 처리하도록 책임을 전가하는 역할을 한다.

* 이 메서드를 사용하는 쪽에서는 이에 대한 처리를 강제함으로써 견고한 프로그램 코드를 작성할 수 있도록 도와준다.

```java
public class Test {
  
  public void someMethod() throws Exception {
	  // 메서드 내용  
	}
  
  public void OtherMethod() {
    try {
      someMethod(); // 이 메소드 호출 시 예외처리를 하지 않는다면 컴파일 에러. 
    } catch(Exception e) {
      // 예외 처리
    }
  }
  
}
```

* 만약 OtherMethod에서도 예외 처리를 하지 않고 밖으로 던진다면 다음과 같이 메소드에 throws를 추가해주면 된다

```java
public void OtherMethod() throws Exception {
  someMethod();
}
```



> **throws를 사용하는 것은 주의하고 한번 더 생각해보자.**
>
> 물론 메서드안에서 try/catch문을 사용하면 예외처리를 함으로써 메서드가 복잡해져 읽기 어렵고, 추가적인 예외가 발생할 수 있다.
>
> 이런 상황에서 throws를 사용하면 다른 팀원들이 메서드를 사용하는 상황에 맞게 예외처리를 할 수 있어진다.
> 또한 메서드 이름 옆에 발생할 수 있는 예외가 명시적으로 작성이 되므로 코드작성에 주의를 할 수 도 있다.
>
> 하지만 throws를 계속해서 사용하다보면 main 메서드까지 가게 되는데,
> main 메서드 또한 예외를 throws 하게 된다면 JVM이 대신 처리하게 된다.
> 결국 예외를 처리하지 않은 것과 다름없다..
>
> 때문에 그저 예외처리가 귀찮아서 throws를 사용하는 것은 금물이다.





# 자바가 제공하는 예외 계층 구조

### 자바 예외 클래스의 구조

<img src="https://blog.kakaocdn.net/dn/cx6aG4/btrR9lefyW4/D586eh4Kgha1ynXvhZgGmk/img.png" width= 650 height=500>



자바는 실행 시 발생할 수 있는 오류들(Error와 Exception)을 클래스로 정의하였다.

* 예외 클래스도 클래스이고, 모든 클래스의 조상은 Object 클래스이므로 Exception과 Error 클래스 역시 Object 클래스의 Object 클래스의 자손들이다. 



> 예외 클래스들은 2그룹으로 나눌 수 있다.
>
> 1. Exception 클래스를 상속받고 RuntimeException을 상속받지 않은 예외들 - CheckedException (확인된 예외)
> 2. RuntimeException을 상속받은 예외들 - UncheckedException(확인되지 않은 예외)



* [Throwable](https://docs.oracle.com/javase/8/docs/api/index.html?java/lang/Throwable) : Java의 모든 오류 및 예외의 상위 클래스. 이 클래스(또는 해당 하위 클래스 중 하나)의 인스턴스인 개체만 JVM에 의해 throw되거나 Java `throw`문에 의해 throw될 수 있다. 
  * 마찬가지로 이 클래스 또는 해당 하위 클래스 중 하나만 `catch`절로 catch할 수 있다.
  * 예외의 컴파일 타임 검사 목적을 위해, `Throwable`하위 클래스 도 `Throwable`아닌 하위 클래스는 확인된 예외로 간주한다. [`RuntimeException`](https://docs.oracle.com/javase/7/docs/api/java/lang/RuntimeException.html)[`Error`](https://docs.oracle.com/javase/7/docs/api/java/lang/Error.html)

* [Exception](https://docs.oracle.com/javase/8/docs/api/index.html?java/lang/Exception.html) :  클래스 Exception과 해당 서브클래스는 애플리케이션이 파악하고자 하는 예외 조건을 보여주는 Throwable 이다.
  * 예외 클래스 및 RuntimeException을 상속받은 하위 클래스가 `아닌` ,  모든 하위 클래스 예외는 `checked 예외`이다.
    * RuntimeException은 Unchecked 예외이다. 
  * 체크된 예외는 메서드 또는 구성 요소의 실행에 의해 슬로우며 메서드 또는 구성 요소의 `경계 밖으로 전파될 수 있는 경우` 메서드 또는 구성 요소의 throw 구로 선언해야 한다.
* [Error]() : 클래스 Error 는 애플리케이션이 `catch` 하는것을 시도해서는 안되며, 심각한 문제를 나타내는 Throwable의 하위 클래스. 
  * 이러한 오류는 대부분 비정상적인 상태이다. 
  * ThreadDeath 오류는 "정상적인" 조건이지만 대부분의 응용 프로그램이 오류를 catch하려고 시도해서는 안 되기 때문에 Error의 하위 클래스이다.
  * 메소드 실행 중에 발생할 수 있지만 포착되지 않은 Error의 하위 클래스를 throws 절에서 선언할 필요는 없다.
  * 즉, Error 와 Error의  하위 클래스는 컴파일 타임 예외 검사를 위해 `unchecked 예외`로 간주된다.

# Exception과 Error의 차이는?



## 에러와 예외



>  오류(error) : 시스템에 무엇인가 비정상적인 상황이 발생한 경우. 프로그램이 실행 중 어떤 원인에 의해서 오작동을 하거나 비정상적으로 종료되는 경우가 있는데 이러한 결과를 초래하는 원인.



* **Java에서는 주로 JVM(Java Virtual Machine)에서 발생시키는 것**이고, **`예외(Exception)`와 반대로 Application Code에서 잡아도 처리할 제대로 방법이 없다.**
* `OutOfMemoryError`, `ThreadDeath`, `StackOverflowError` 등



에러는 발생 시점에 따라서 3가지로 분류할 수 있다. 

<img src="https://blog.kakaocdn.net/dn/lAT0k/btrSa8kH9u4/WC7O3aNpXkUdlFKbqmT7D1/img.png" width=650 height=200>

* `Compile Error (컴파일 에러)` : 컴파일 할 때 발생하는 에러. syntax error(문법 오류)도 포함됨. - 컴파일 시 발생하는 에러
  * 소스코드를 컴파일을 하면 `컴파일러`가 소스코드에 대해 `오타`나 `Syntax Error(잘못된 구문)`, 자료형 체크등의 기본적인 검사를 수행하여 오류를 잡아낼 수 있다. 
  * 소스코드를 컴파일러가 컴파일하는 시점에서  발생하는 에러를 컴파일 에러라 하며 이 시점에서 발생하는 문제들을 수정 후 컴파일을 성공적으로 마칠경우 클래스 파일(*.class) 파일이 생성된다.

* `Runtime Error (런타임 에러)` : 프로그램 실행 도중에 발생하는 에러 - 실행 시 발생하는 에러 
  * 컴파일러는 컴파일 시점에서 문법 오류나 오타같은 컴파일 시점에서 예측 가능한오류는 잡아줄 수 있지만, 실행중 발생할 수 있는 잠재적인 에러까지는 잡을 순 없다.
  * 컴파일은 완료되어서 컴파일 에러는 없지만 실행 중 발생할 수 있는 에러이다.
* `Logical Error (논리적 에러)` :  : 컴파일도 잘되고 실행도 잘되지만 의도한 것과 다르게 동작하는 것.
  * 컴파일도 정상적으로 되고 런타임 에러도 발생하지 않았지만 개발자가 의도한 대로 동작하지 않는 에러를 말한다.
  * API가 응답값을 주도록 했으나 응답값을 안주거나, 아무 동작을 안하거나, 버그가 생기는 등을 의미한다



> 예외(exception) : 입력 값에 대한 처리가 불가능하거나, 프로그램 실행 중에 참조된 값이 잘못된 경우 등 정상적인 프로그램의 흐름을 어긋나는 것



* Java에서 **`Exception`은 개발자가 직접 Application Code내에서 처리** 할 수 있어, **예외 상황에 대해 미리 예측하여 Handling**할 수 있다.
* Exception, RuntimException, IOException 등





`오류가 시스템 레벨에서 발생한다면 예외(Expceiton)은 개발자가 구현한 로직에서 발생한다.`



### 런타임 시점에서 발생하는 오류는 에러(error)와 예외(exception)으로 나뉜다.

컴파일러가 소스코드 컴파일중 발생한 에러를 잡았다 해서 실행 도중에 발생할 수 있는 잠재적인 오류까지는 검사할 수 없다 .   이것이 런타임 에러이다.

런타임 에러를 방지하기 위해서는 프로그램 실행 도중 발생할 수 있는 모든 경우의수를 고려하여 이에 대한 대비를 하는것이 필요한데

자바에서는 실행 시(rumtime) 발생할 수 있는 프로그램 오류를 에러(rror)와 예외(exception) 두가지로 구분하였다 



> 따라서 개발자는 예외 처리(exception handling)를 통해 예외 상황을 처리할 수 있도록 코드의 흐름을 바꿀 필요가 있다.



• 에러(Error): 메모리 부족(OutOfMemoryError)이나 스택오버플로우(StackOverflowError)와 같이 일단 발생하면 복구할 수 없는 심각한 오류

• 예외(exception): 인자값 Null 에러, NPE(NullPointException)같은 발생하더라도 수습이 가능한 덜 심각한 오류. 

# 예외 전파

아래와 같은 코드에서 doSomething() 메서드 수행 시 Exception이 발생하면 어떻게 될까?

try 의 별도 catch 문이 존재하지 않아서 Exception은 상위로 throw 가 될까? 아니면 skip 될까?

```java
try{
  
	 doSomething();
  
} finally{
  
	 System.out.println("finally");
}
```



### **Call Stack**

* https://docs.oracle.com/javase/tutorial/essential/exceptions/definition.html

<img src="https://blog.kakaocdn.net/dn/bZB8J6/btrSaJk8fXs/tDH3gtxHlDbwavFOR2H6J0/img.png" width=650 height=500>



JVM에서  메소드가 실행되면 메모리의 stack 영역에 Stack Frame이 쌓이게 된다.

( Execution Stack , Call Stack)

- 메서드의 작업에 필요한 메모리 공간을 제공

- 메서드 호출시 호출 스택에 호출된 메서드를 위한 메모리가 할당되며, 메서드가 작업을 수행하는 동안 지역변수(매개변수 포함) 들과 연산의 중간결과등을 저장하는데 사용됨

- 메소드 작업이 끝나면 할당되었던 메모리공간은 반환되어 비워짐

- 특징

  - 메서드가 호출되면 수행에 필요한 만큼의 메모리를 스택에 할당
  - 메서드가 수행을 마치고나면 사용했던 메모리를 반환하고 스택에서 제거
  - 호출스택의 제일 위에 있는 메서드가 현재 실행중인 메서드
  - 아래에 있는 메서드가 바로 위의 메서드를 호출한 메서드

  


>  즉, stack 영역은 Stack Frame 을 저장하는 Stack 이며, JVM은 오직 Stack Frame을 Push 하고 Pop 하는 작업만 하게 된다.

  


* Java에서는 Exception 이 발생했을 경우 Exception Handler 가 처리한다. 

  * 메서드에서 Exception이 발생하면 해당 메서드는 **Exception Object(exception의 이름과 설명, 프로그램의 현재 상태, exception 발생 위치의 메서드 리스트[call stack]를 포함)**를 생성하고 JVM에 넘긴다. 

  * 이러한 과정을 `Exception을 던진다(throwing an Exception)`라고 표현한다.

  


1. Exception이 발생했을 때, Exception Handler는  Exception Handler 를 call stack 을 이용하여 역방향으로 Exception Handler를 탐색한다. 
   * 즉 예외가 발생한 메서드를 실행시키기 위해 실행했던 모든 메서드를 반대 순서대로 찾는다는 것이다. 

 

2. 탐색하면서 Exception Handler 가 처리할 수 있는 Exception 타입인지 체크 한 뒤 맞다면 처리하고 아니라면 상위로 전달된다. 
   * 즉, 발생한 Exception 에 대해서 처리가 가능한 Handler 를 찾을 때까지 상위로 전달되면서 찾아가게 된다.



3. 적절한 Handler 를 찾지 못한다면 JVM 까지 전달되며, 최종적으로 JVM `default exception handler`가   Exception 을 처리하게 되는데,  모든 call stack을 찾아도 적절한 handler를 찾지 못했다면 default exception handler가 실행된다.

4. default exception handler는 exception 정보를 노출하며 해당 thread를 중지시킨다.



# RuntimeException과 RE가 아닌 것의 차이는?

### RuntimeException



RumtimeException은 JVM 정상 작동 중, 즉 애플리케이션 실행 시점에 발생할 수 있는 예외들의 상위 클래스이다.



* **`RuntimeException`은 Program 실행 중에 발생**하며 **System 환경적으로나, Input Value가 잘못된 경우, 의도적으로 개발자가 설정한 조건을 위배했을 때 throw** 되게 하는 등, **Application 실행 도중에 발생하는 `Exception`**이다.

* `RuntimeException`은 `명시적으로 예외 처리를 하지 않아도 되는`  `Exception`이다.
  * Exception 처리를 명시하지 않아도 Program 구동에 아무 문제가 없다.
  * 그러나 다른 문제를 야기할 수 있으니 처리할 수 있는 만큼 처리해주는 것이 좋다.



> RuntimeException 과 ErrorClass들을 `UnchckedException` 이라고 묶으며,   
> RuntimeException 과 ErrorClass들을 제외한 예외 클래스들을 `CheckedException`로 묶는다,





# CheckedException과 UnchcekdException



## CheckedException (확인된 예외)

> RuntimeException을 상속받지 않은 클래스들을 CheckedExcpetion이라고 한다.
>
> 예외가 발생할 수 있는것이 확인되기 때문에 반드시 명시적으로 처리해야 한다. 

`CheckedException`은 반드시 예외 처리 해야하는 `Exception`**으로, **Compile 시점에서 `Exception` 발생이 확인될 수 있으므로 반드시 명시적으로 처리해야 한다. 그렇지 않으면 컴파일러가 에러를 잡는다

* 메소드 내에서 try catch를 하든, 메소드 밖으로 throws를 통해서 던져서 `명시적으로` 처리해야 한다.
  * **이것은 문법적으로 강제적이다.**

* CheckedException은 `Error와 RuntimeException을 상속받지 않은 Exception 들을 모두 포함한다.
  * *`Error`, `FileNotFoundException`, `ClassNotFoundException`*



> Spring @Transaction 어노테이션  Rollback `기본 정책`이 Unchcked Exception과 error들이다.  
> 즉 설정을 바꾸지 않으면  `CheckedException`은 Transaction 처리시에 Exception이 발생해도 Rollback을 하지 않는다.  
>
> 하지만 어떤 예외가 발생하면 롤백을 할 것이냐 말 것이냐는  **개발자가 정하는 것이다.**
>
> *  CheckedException들이 롤백이 안되는 것이 아니다.
>
> *  @Transactional(rollbackFor=Exception.class) 옵션으로 모든 예외에 대해서 롤백할 수 있다.
> *  Checked Exception을 try-catch문으로 더 구체적인 Unchecked Exception으로 감싸주면 롤백이 가능하다.



## UncheckedException (확인되지 않은 예외)

> RuntimeException을 상속받은 모든 예외 클래스들을 UnchckedException 이라고 한다.
>
> 예외가 발생할 수 있는 것이 확인되지 않기 때문에 처리할 방법을 고민해야 한다.

* **`UncheckedException`은 명시적으로 예외 처리 해주지 않아도 되는 `Exception`**으로, **`Runtime` 시점에서 `Exception` 이 발생해야 확인되고, 컴파일 시점에는 확인되지 않는 예외이다.

* 주로 프로그래머들의 실수나 예견되지 못한 값 처리 등, 잘못된 값 입력 등에 의해서 일어나는 예외이다.
  * 그러니까 확인되지 않은 예외(UncheckedException)이다.
* **`UncheckedException`은 `RuntimeException`을 상속받는 `Exception`들을 포함**한다.
  * `NullPointerException`, `ClassCastException`, `IllegalArgumentException` 등 



### Checked Exception 과 Unchecked(Runtime) Exception

|                                                   | **체크 예외 (Checked Exception)**                            | **언체크 예외 (Uncheked Exception)**                         |
| ------------------------------------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 예외 발생 이유                                    | 외부 영향, **사용자**의 실수                                 | **프로그래머**의 실수                                        |
| 예외 처리 여부                                    | **예외 처리 필수** try catch - 해당 메소드에서 처리 throw -호출한 곳에서 처리하도록 상위로 넘김 | 예외 처리 필수 아님                                          |
| 예외 확인 시점                                    | **컴파일 시점 **에러 처리를 해주지않을 경우 컴파일 시점에 에러표시 | **런타임 시점**  컴파일 시 문제 없음                         |
| 예외 종류                                         | * Excpetion 하위 예외 중 RuntimeException 제외 FileNotFoundException ClassNotFoundException | * RuntimeException 하위 예외 NullPointerException ArrayIndexOutOfBoundsException |
| Spring에서 예외 발생 시 트랜잭션 처리 - 기본 정책 | 롤백(rollback)하지 않음 - 기본 정책 <br /> rollbackFor옵션으로 변경 가능 | 롤백(rollback) - 기본 정책 <br />rollbackFor옵션으로 변경 가능 |



- spring의 기본적인 트랜잭션 설정은 checked exception은 롤백하지 않고, unchecked exception는 롤백한다.
  - default 정책 설정일 뿐 변경할 수 있다.



# 커스텀한 예외 만드는 방법

* https://dzone.com/articles/implementing-custom-exceptions-in-java?fromrel=true

### 커스텀 예외를 만들때 참고해야 할 4가지 Best Practices

### 1. Always Provide a Benefit - 향상된 혜택을 제공

자바 표준 예외들에는 포함되어 있는 다양한 장점을 가지는 기능들이 있다.

이미 JDK가 제공하고 있는 방대한 수의 예외들과 비교했을 때 만들고자 하는 커스텀 예외는 어떠한 장점도 제공하지 못한다면? 커스텀 예외를 만드는 이유를 다시 생각해볼 필요가 있다.

어떠한 장점을 제공할 수 없는 예외를 만드는 것 보다 오히려 UnsupportedOperationException 이나, IllegalArugmentException 과 같은 표준 예외 중 하나를 사용하는 것이 낫다. 그러면 코드와 API를 더 쉽게 이해할 수 있습니다.[ ](https://docs.oracle.com/javase/9/docs/api/java/lang/UnsupportedOperationException.html)

* (https://docs.oracle.com/javase/9/docs/api/java/lang/IllegalArgumentException.html)  
  

### 2. Follow the Naming Convention - 네이밍 규칙을 따르자 

JDK가 제공하는 예외 클래스들을 보면 클래스의 이름이 모두 "Exception" 으로 끝나는 것을 알 수 있다.  

이러한 네이밍 규칙은 자바 생태계 전체에 사용되는 규칙이다.   

사용자 지정 예외도 따라야 한다.   

즉, 만들고자 하는 커스텀 예외 클래스도 이 네이밍 규칙을 따르는 것이 좋다.    


### 3. Provide javadoc Comments for Your Exception Class - 예외 클래스에 대한 Javadoc 주석 제공

많은 커스텀 예외들이 어떠한 javadoc 코멘트도 없이 만들어진 경우들이 많다.

기본적으로 API의 모든 클래스, 멤버변수, 생성자들에 대해서는 문서화 하는 것이 일반적인 Best Practices 이다.

문서화되지 않은 API는 사용하기가 매우 어렵다.  

예외 클래스는 API의 가장 명확한 부분이 아닐 수 있지만 사실상 API의 일부이다.    

클라이언트와 직접 관련된 메소드들 중 하나가 에외를 던지면 그 예외는 바로 예외 API의 일부가 된다.   

이는 문서화와 [ 좋은 Javadoc](http://blog.joda.org/2012/11/javadoc-coding-standards.html) 과 문서화가 필요함을 의미한다.  

avaDoc은 예외가 발생할 수도 있는 상황과 예외의 일반적인 의미를 기술한다.  

목적은 다른 개발자들이 API를 이해하고 일반적인 에러상황들을 피하도록 돕는 것이다.  

```java
/**
 * The MyBusinessException wraps all checked standard Java exception and enriches them with a custom error code.
 * You can use this code to retrieve localized error messages and to link to our online documentation.
 * 
 * @author TJanssen
 */
public class MyBusinessException extends Exception { ... }
```



### 4. Provide a Constructor That Sets the Cause - 원인을 설정하는 생성자를 제공하자

커스텀 예외를 던지기 전에 표준 예외를 Catch 하는 케이스가 꽤 많다.

예외가 발생한 사실을 숨기면 안된다.

* 스택으로 같이 남겨야 한다.

보통 캐치된 예외에는 제품에 발생한 오류를 분석하는데 필요한 중요한 정보가 포함되어 있다.  

다음 예제에서 [*NumberFormatException*](https://docs.oracle.com/javase/9/docs/api/java/lang/NumberFormatException.html) 은 오류에 대한 자세한 정보를 제공한다.    

MyBusinessException의 cause 처럼 cause 정보를 설정하지 않으면  [*NumberFormatException*](https://docs.oracle.com/javase/9/docs/api/java/lang/NumberFormatException.html) 이라는 중요한 정보를 잃을 것이다.  



```java
public void wrapException(String input) throws MyBusinessException {
    try {
        // do something
    } catch (NumberFormatException e) {
        throw new MyBusinessException("A message that describes the error.", e, ErrorCode.INVALID_PORT_CONFIGURATION);
    }
}
```



### Exception과 RuntimeException

Exception과 RuntimeException은 **`예외의 원인을 기술하고 있는 Throwable`** 을 받을 수 있는 생성자 메소드를 제공한다.

만들고자 하는 커스텀 예외도 이렇게 하는 것이 좋다.

**발생한 Throwable 를 파라미터를 통해 가져올 수 있는 생성자를 최소한 하나를 구현하고 수퍼클래스에 Throwable을 전달해줘야 한다.**

```java
public class MyBusinessException extends Exception {
    public MyBusinessException(String message, Throwable cause, ErrorCode code) {
            super(message, cause);
            this.code = code;
        }
        ...
}
```



* [ 9가지 모범 사례](https://stackify.com/best-practices-exceptions-java/) 와 [ 7가지 일반적인 실수 가 포함된 내 게시물을 살펴보세요.](https://stackify.com/common-mistakes-handling-java-exception/)  

    
  

### Implementing a Custom Exception (Checked Exception) - 확인된 예외, 체크드 익셉션 구현 

Custom Checked Exception 의 생성은 간단하다.

* `**Checked Exception**`을 구현하기 위해서는 **Exception 클래스를 상속받아야 하는데, 커스텀 예외를 구현하기 위해 필요한 유일한 필수사항이다.**



하지만 위에 4가지 Best Practices에서 설명했듯이 발생한 예외를 생성자에 주입하기 위한 생성자 메소드를 제공해야 하며, 표준 예외보다 더 나은 이점들을 제공해야 한다.

  


아래 예제는 설명해 온 것들을 보여준다.

- 예외를 기술하는 Javadoc 주석을 추가했으며,  
- 수퍼클래스에 발생한 예외를 주입하는 생성자 메소드를 구현했다.  
- 또한 표준 예외보다 더 나은 장점을 제공하기 위해 MyBusinessException은 문제 식별을 위한 에러코드를 저장하는 커스텀  enumeration을 사용한다.  
  - ErrorCode를 적은 Custom Enum을 사용하자
- 클라이언트들은 에러메세지를 보여주기 위해 오류 코드를 사용할 수 있으며, support ticket 내에 이 코드를 포함하도록 유도할 수 있다.

  


```java
/**
 * The MyBusinessException wraps all checked standard Java exception and enriches them with a custom error code.
 * You can use this code to retrieve localized error messages and to link to our online documentation.
 * 
 * @author TJanssen
 */
public class MyBusinessException extends Exception {
    private static final long serialVersionUID = 7718828512143293558 L;
    private final ErrorCode code;
    public MyBusinessException(ErrorCode code) {
        super();
        this.code = code;
    }
    public MyBusinessException(String message, Throwable cause, ErrorCode code) {
        super(message, cause);
        this.code = code;
    }
    public MyBusinessException(String message, ErrorCode code) {
        super(message);
        this.code = code;
    }
    public MyBusinessException(Throwable cause, ErrorCode code) {
        super(cause);
        this.code = code;
    }
    public ErrorCode getCode() {
        return this.code;
    }
}
```

이 것들이 Checked 예외 구현을 위해 필요한 것들이다.

* 사용자는 코드에서 MyBusinessException 을 던지거나 또는 메소드 시그니쳐에 표기하거나  try-catch 절에서 처리할 수 있다.

```java
public void handleExceptionInOneBlock() {
    try {
        wrapException(new String("99999999"));
    } catch (MyBusinessException e) {
        // handle exception
        log.error(e);
    }
}

private void wrapException(String input) throws MyBusinessException {
    try {
        // do something
    } catch (NumberFormatException e) {
        throw new MyBusinessException("A message that describes the error.", e, ErrorCode.INVALID_PORT_CONFIGURATION);
    }
}
```



### Implementing an Unchecked Exception - 확인되지 않은 예외 구현

`**Custom Unchecked Exception**` 예외 구현은 checked exception 예외 구현가 동일하다.

* 한가지 차이가 있는데 **"Exception"을 확장하는 것이 아닌 "RuntimeException"을 확장한다.**

```java
/**
 * The MyUncheckedBusinessException wraps all unchecked standard Java exception and enriches them with a custom error code.
 * You can use this code to retrieve localized error messages and to link to our online documentation.
 * 
 * @author TJanssen
 */
public class MyUncheckedBusinessException extends RuntimeException {
    private static final long serialVersionUID = -8460356990632230194 L;
    private final ErrorCode code;
    public MyUncheckedBusinessException(ErrorCode code) {
        super();
        this.code = code;
    }
    public MyUncheckedBusinessException(String message, Throwable cause, ErrorCode code) {
        super(message, cause);
        this.code = code;
    }
    public MyUncheckedBusinessException(String message, ErrorCode code) {
        super(message);
        this.code = code;
    }
    public MyUncheckedBusinessException(Throwable cause, ErrorCode code) {
        super(cause);
        this.code = code;
    }
    public ErrorCode getCode() {
        return this.code;
    }
}
```

다른 Unchecked 예외를 사용하는 것 처럼 MyUncheckedBusinessException 을 사용할 수 있다.  

코드에서 이 예외를 던질 수 있으며 Catch 절에서 이 예외를 사용할 수 있다.  

작성한 메소드가 이 예외를 던진다고 기술 할 수도 있지만 굳이 그럴 필요는 없다.    


```java
private void wrapException(String input) {
    try {
        // do something
    } catch (NumberFormatException e) {
        throw new MyUncheckedBusinessException("A message that describes the error.", e, ErrorCode.INVALID_PORT_CONFIGURATION);
    }
}
```



### 커스텀 예외 만들기 정리

`Checked` Exception을 구현할 때는 `Exception` 을 확장

`Unchecked` Exception을 구현할 때는 `RuntimeException` 을 확장.

추가적으로 4가지 Best Practices를 따르는 것이 좋다.

1. Java 표준 예외를 사용하는 것 보다 작성한 Custom 예외를 사용하는게 더 많은 이익을 얻는다고 생각할 경우에만 Custom Exception을 구현하자.
2. 작성한 Custom Exception 클래스의 이름의 끝은 "Exception"으로 끝나야 한다. 그래야 다른 개발자가 보더라도 이해할 수 있다.
3. API 메소드가 어떤 하나의 예외를 기술하고 있다면, 그 예외는 API의 한 부분이 되는 것이며 그 예외를 문서화 해야 한다. - javadoc
4. 예외의 Cause 를 설정할 수 있는 생성자를 제공해야 한다.





# Spring MVC 예외처리

### 커스텀한 예외 만드는 방법

```java
public static class MyException extends Exception {
        private final Integer code;

        public MyException(String message, Integer code) {
            super(message);
            this.code = code;
        }

        @Override
        public synchronized Throwable getCause() {
            System.err.println("code = " + code);
            return super.getCause();
        }
    }

    public static void exception3() throws MyException {
        // 특정 조건 발생시 -> ex) 5 / 0
        throw new MyException("some-message", 500);
    }
}
```

- Spring

```java
@RestControllerAdvice
public static class ExceptionHandlers {
    @ExceptionHandler(RuntimeException.class)
    ResponseEntity<ErrorResponse> handleConstraintViolationException(RuntimeException e) {
        final ErrorResponse response = ErrorResponse.of(ErrorCode.MEMID_NOT_FOUNDED);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
```

Exception이 발생했을 경우 이를 캐치해줄 ExceptionHandler를 만들어준다

여기서는 RuntimeException이 발생한 경우 내가 커스텀한 Response로 예외를 뿌려주게 된다

```java
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {

    private String message;
    private int status;
    private String code;

    private ErrorResponse(final ErrorCode code) {
        this.message = code.getMessage();
        this.status = code.getStatus();
        this.code = code.getCode();
    }

    public static ErrorResponse of(final ErrorCode code) {
        return new ErrorResponse(code);
    }

}
```

리턴할 ResponseEntity에 담아줄 ErrorResponse 객체를 만들준다

```java
public enum ErrorCode {
    MEMID_NOT_FOUNDED(400, "member.memId", "memId는 반드시 입력되어야 합니다."),
		MEMCODE_NOT_FOUNDED(401, "member.memCode", "memCode는 반드시 입력되어야 합니다.");

    private final String code;
    private final String message;
    private final int status;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public String getCode() {
        return code;
    }

    public int getStatus() {
        return status;
    }
}
```

enum을 만들어 각 CASE에 대한 값을 지정해 넣어준다.

```java
public static class NoResultMemCodeException extends RuntimeException{
        public NoResultMemCodeException(String message) {
            super(message);
        }

        public NoResultMemCodeException() {
            super();
        }
    }
```

마지막으로 커스텀 Exception을 만들어 준다



### 참조

* https://www.notion.so/3565a9689f714638af34125cbb8abbe8