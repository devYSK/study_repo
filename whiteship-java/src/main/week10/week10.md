# 목표

자바의 멀티쓰레드 프로그래밍에 대해 학습하세요.

# 학습할 것 (필수)

- Thread 클래스와 Runnable 인터페이스
- 쓰레드의 상태
- 쓰레드의 우선순위
- Main 쓰레드
- 동기화
- 데드락



### 프로세스(Process)

 

- 사전적 의미 : 일의 과정이나 공정
- 실행중인 프로그램을 의미
- 프로그램을 실행하면 OS로부터 실행에 필요한 자원(메모리)을 할당받아 프로세스가 됨

 

### 쓰레드(Thread)

- 프로세스라는 작업공간에서 실제로 작업을 처리하는 일꾼
- 프로세스의 자원을 이용해서 작업을 수행함
- 모든 프로세스에는 최소한 하나 이상의 쓰레드가 존재
- 쓰레드가 하나(싱글 쓰레드)
- 둘 이상의 쓰레드(멀티 쓰레드)



## Process vs Thread

### 프로세스(Process)

- 사전적 의미 : 일의 과정이나 공정
- 프로그램의 인스턴스, 실행된 프로그램
- 이미 메모리에 적재되어 있음
- 프로그램을 실행하면 OS로부터 실행에 필요한 자원(메모리)을 할당받아 프로세스가 됨

 

### 쓰레드(Thread)

* 한 프로세스 내에서 동시에 실행 가능한 단위
* Java 에서는 JVM 이 모든 thread 관리

- 프로세스라는 작업공간에서 실제로 작업을 처리하는 일꾼
- 프로세스의 자원을 이용해서 작업을 수행함
- 모든 프로세스에는 최소한 하나 이상의 쓰레드가 존재
- 쓰레드가 하나(싱글 쓰레드)
- 둘 이상의 쓰레드(멀티 쓰레드)



### 멀티 프로세스와 멀티 스레드의 비교

멀티프로세스와 멀티스레드는 양쪽 모두 **여러 흐름이 동시에 진행된다는 공통점**을 가지고 있다.

하지만, **멀티프로세스**에서 각 프로세스는 **독립적으로 실행되며 각각 별개의 메모리를 차지**하고 있는 것과 달리 **멀티스레드는 프로세스 내의 메모리를 공유해 사용할 수 있다.**

또한, 프로세스 간의 전환 속도보다 스레드간의 전환속도가 빠르다.

**`멀티스레드의 장점`**은 CPU가 여러 개일 경우 각각의 CPU 스레드 하나씩을 담당하는 방법으로 속도를 높일 수 있다. 이러한 시스템에서는 여러 스레드가 실제 시간상으로 동시에 실행될 수 있다.

그러나 **`단점`**은 각각의 스레드 중 어떤 것이 먼저 실행될지 그 순서를 알 수 없다는 것이다.



### 멀티 태스킹(multi-tasking)

- 여러 개의 프로세스가 동시에 실행될 수 있는것을 말한다
- 대부분의 OS가 지원한다

 

### 멀티 쓰레딩(multi-threading) 

하나의 프로세스 내에서 여러 쓰레드가 동시에 작업을 수행하는 것. CPU의 코어(core)가 한 번에 단 하나의 작업만 수행할 수 있으므로, 실제로 동시에 처리되는 작업의 개수와 일치한다.

* 대부분 쓰레드의 수는 코어의 개수보다 훨씬 많기 때문에 각 코어가 아주 짧은 시간 동안 여러 작업을 번갈아 가며 수행함으로써 여러 작업들이 모두 동시에 수행되는 것처럼 보이게한다.
* 따라서 프로세스의 성능이 단순하게 쓰레드의 개수에 비례하는것은 아니며, 하나의 쓰레드를 가진 프로세스보다 두개의 쓰레드를 가진 프로세스가 오히려 더 낮은 성능을 보일 수 있습니다.

 

### 멀티 쓰레딩의 장점

- CPU의 사용률을 향상시킨다
- 자원을 보다 효율적으로 사용할 수 있다
- 사용자에 대한 응답성이 
- 작업이 분리되어 코드가 간결해진다.



### 멀티 쓰레딩에서 주의할 점(신경써야 할 점?)

 여러 쓰레드가 같은 프로세스 내에서 자원을 공유하면서 작업을 하기 때문에 발생할 수 있는 동기화(synchronization), 교착상태(deadlock) 과 같은 문제들을 고려해서 신중히 프로그래밍 해야한다.



### 동시성(concurrency)과 병렬성(parallelism)

멀티 쓰레드가 실행될 때 이 두가지 중 하나로 실행된다.

이것은 CPU의 코어의 수와도 연관이 있는데,

* 하나의 코에서 여러 쓰레드가 실행되는 것을 `**"동시성"**`

* 멀티 코어를 사용할 때 코어별로 개별 쓰레드가 실행되는 것을 `"병렬성"`

* [동시성과 병렬성](https://0soo.tistory.com/119) 참조 



만약 코어의 수가 쓰레드의 수보다 많다면, 병렬성으로 쓰레드를 실행하면 되는데, **코어의 수보다 쓰레드의 수가 더 많을 경우 "동시성"을 고려하지 않을 수 없다.**

###  **`동시성을 고려 한다는 것은`** 하나의 코어에서 여러 쓰레드를 실행할 때 병렬로 실행하는 것처럼 보이지만, 사실은 병렬로 처리하지 못하고 한 수간에는 하나의 쓰레드만 처리할 수 있어서 번갈아 가며 처리하게 된다.



- **동시성(concurrency)**은 어떻게 보면 동시에 하는 것이 아니다, 내부적으로 번갈아가며 진행하는 것이다.

  (context switching이 지속적으로 발생한다.)




# 1. Thread

> 인텔리제이의 기능을 이용하여 Thread클래스 명세를 읽어보며 공부를 해보자.



<img src="https://blog.kakaocdn.net/dn/czpjhi/btrTgdr4eN7/S5TBgKJUK0eLpZXzCWbklK/img.png" width= 800 height =700>



- 스레드는 하나의 프로그램에서의 실행 흐름이다.
- JVM은 병렬적으로 작동하는 여러개의 스레드 실행을 허용한다.
- 모든 스레드는 우선순위가 있다. 우선순위가 높은 스레드는 우선순위가 낮은 스레드보다 먼저 실행된다.
- 어떤 스레드는 데몬스레드가 되거나 되지 않을수 있다.
- 일부 스레드에서 실행중인 코드가 새 스레드 객체를 생성할 때, 새 스레드는 처음에 생선된 스레드의 우선순위와 동일하게 설정된 우선순위를 가지며, 생성스레드가 데몬인 경우에만 데몬스레드가 된다.
- JVM이 시작될 때 일반적으로 메인메서드의 호출로 발생한 단일 비데몬 스레드가 있다.
- JVM은 다음과 같은 상황이 발생할 때 까지 지속된다.
  - Runtime 클래스의 exit() 메서드가 호출되고 security manager가 종료 조작을 허가한 경우.
  - 데몬 스레드가 아닌 모든 스레드가 run()메서드의 호출로 return되었거나, run()메서드를 넘어서 전파되는 예외를 throw하여 죽은경우.
- 스레드는 두 가지의 실행방식이 있다. 
  - 첫 번째는 Thread 클래스의 서브클래스로 선언되는것이다. 이 서브클래스는 반드시 Thread클래스의 run()메서드를 오버라이딩 해야한다. 
  - 그런 다음에야 서브클래스의 인스턴스를 할당하고 시작할 수 있다.
  - 그 후 인스턴스의 start()메서드를 호출하면 스레드를 실행할 수 있다.



![img](https://blog.kakaocdn.net/dn/k85nB/btqT6Hsq3Q5/xpf25QkNsDrsdwLkVLlpzk/img.png)



- 또 다른 방법은 Runnable 인터페이스를 구현하는 클래스를 작성하는 것이다. 그 클래스는 run()메서드를 구현해야한다.
- 새로운 스레드의 인수로 Runnable인스턴스를 인자로 넘긴 후, 해당 스레드를 실행하면 스레드를 실행할 수 있다.
- 모든 스레드는 식별을 위한 이름이 있다.
- 둘 이상의 스레드가 동일한 이름을 가질 수 있다.
- 스레드가 생성될 때 이름이 지정되지 않으면 새 이름이 생성된다.
- 달리 명시되지 않는 한, 이 클래스의 생성자, 또는 메서드에 null 인수를 전달하면 NullPointerException이 throw된다.

> 



# Thread 클래스와 Runnable 인터페이스

자바에서는 쓰레드를 관리하기 위한 메서드와 변수들을 `java.lang.Thread` 클래스에서 제공하고 있다.

쓰레드를 구현하는 방법은 **Thread 클래스를 상속받는 방법**과 **Runnable 인터페이스를 구현하는 방법** 두 가지가 있다. Thread를 상속받으면, 다른 클래스를 상속받을 수 없기 때문에 인터페이스를 구현하는 방법이 일반적이다.

> Runnable인터페이스를 구현하는 방법은 재사용성이 높고 코드의 일관성을 유지할 수 있기 떄문에 보다 객체지향적인 방법이라 할 수 있다.



### Thread와 Runnable은 언제 사용하면 될까?

- 내가 만드는 쓰레드에서 run 말고도 다른 것들을 오버라이딩 할 필요가 있는 경우에는 Thread를 상속받아 오버라이딩하여 구현하면 되는 것이고,
  - Thread 클래스는 다양한 메소드들이 존재하며 Thread를 상속받으면 다양한 메소드들을 원하는대로 오버라이딩하여 구현할 수 있다.
- run만 정의하고자 한다면 Runnable을 구현하는 하면된다. **`왜? Runnable은 run 메소드만 가지고 있기 때문이다.`**



```java
class MyThread extends Thread {

    @Override
    public void run() { ... } // Thread 클래스의 run()을 오버라이딩
}
```



**Runnable 인터페이스를 구현**

```java
class MyThread implements Runnable {
    public void run() { ... } // Runnable 인터페이스의 run()을 구현
}
```



Runnable 인터페이스는 오로지 `run()`만 정의되어 있는 간단한 인터페이스이다. Runnable 인터페이스를 구현하기 위해서 해야 할 일은 추상메서드인 run()의 몸통{ }을 만들어 주는 것 뿐이다.

```java
    public interface Runnable {
        void run();
    }
```



### 쓰레드를 구현한다는 것은, 위의 두 방법 중 어떤 것을 선택하든, 쓰레드를 통해 작업하고자 하는 내용으로 run()의 몸통{ }을 채우는 것일 뿐이다.

```java
class App {
    public static void main(String[] args) {
        ThreadOne t1 = new ThreadOne();

        Runnable r = new ThreadTwo();
        Thread t2 = new Thread(r);      // 생성자 Thread(Runnable Target)

        t1.start();
        t2.start();
    }
}

class ThreadOne extends Thread {

    @Override
    public void run() {
        for(int i = 0; i < 3; i++) {
            System.out.println(getName()); // 조상인 Thread의 getName()을 호출
        }
    }
}

class ThreadTwo implements Runnable {
    public void run() {
        for(int i = 0; i < 3; i++) {
            // Thread.currentThread() : 현재 실행 중인 Thread를 반환
            System.out.println(Thread.currentThread().getName());
        }
    }
}
```



상속받을 때와 구현할 때의 인스턴스 생성 방법이 다르다.

```java
ThreadOne t1 = new ThreadOne();   // Thread의 자손 클래스의 인스턴스를 생성

Runnable r = new ThreadTwo();     // Runnable을 구현한 클래스의 인스턴스를 생성
Thread t2 = new Thread(r);        // 생성자 Thread(Runnable Target)
```



**Thread의 생성자**

- Thread() : 기본 생성자
- Thread(String s) : 스레드의 이름을 설정할 수 있는 생성자
- Thread(Runnable r) : Runnable 인터페이스 객체로 생성하는 생성자
- Thread(Runnable r, String s) : Runnable 인터페이스 객체로 생성하며 이름을 가지는 생성자

* 쓰레드의 이름을 지정하지 않으면 ‘Thread-번호’의 형식으로 이름이 정해진다.



# 쓰레드의 상태

<img src="https://blog.kakaocdn.net/dn/brVYfa/btrTf0zKVQB/H6BtVYgdNimkYcUq9WihJk/img.png" width= 900 height =400>



Thread.State 라는 Enum 타입이 존재한다.

- NEW
  - 쓰레드가 생성된 경우, NEW 상태로 존재
  - 쓰레드가 실행되지 않은 상태
- RUNNABLE
  - 쓰레드가 실행중이거나 즉시 실행될 수 있다는걸 명시
  - JVM 쓰레드 스케줄러의 권한
  - `start` 메소드 호출로 상태 변경
- BLOCKED
  - 쓰레드가 block 된 상태
  - 다른 쓰레드가 락을 해제하기를 기다리는 상태
- WAITING
  - `wait`, `join`, `park` 메소드를 호출하여 대기하고 있는 상태
- TIMED_WAITING
  - `sleep`, `wait`, `join`, `park` 메소드를 호출하여 대기하고 있는 상태
  - **WAITING 과의 차이**: 메소드의 `인자`에 최대 대기 시간 명시하여 시간에 의해 wait
- TERMINATED
  - 쓰레드가 종료된 상태
    - 정상 종료
    - 비정상 종료(segmentation fault, 예외



### Thread의 실행 - start()

쓰레드를 생성했다고 자동으로 실행되는 것이 아니다.

start() 메소드를 호출해야만 쓰레드가 실행된다.

* 만일 하나의 쓰레드에 대해 start()를 두 번 이상 호출하면 실행 시에 `IllegalThreadStateException`이 발생한다.

여기서 start() 메소드가 호출되었다고 해서 바로 실행되는 거싱 아니라, **`실행대기 상태`**에 있다가 자기 자신의 차례가 되어야 실행된다.

> 💡 쓰레드의 실행순서는 OS의 스케쥴러가 작성한 스케쥴에 의해 결정된다.

한번 실행이 종료된 쓰레드는 다시 실행할 수 없다.  

* 즉, 하나의 쓰레드에 대해 start() 메소드는 단 한번만 호출될 수 있다는 뜻이다.  

만일, 쓰레드의 작업을 더 수행해야 한다면 새로운 쓰레드를 생성하여 start()를 호출해야 한다.

* 이미 종료된 쓰레드를 한번더 수행하기 위해 start()를 호출하게 되면 실행 시 **"IllegalThreadStateException"**이 발생하게 된다.



### Thread 생성부터 소멸까지 정리

1. 쓰레드를 생성하고 start()를 호출하면 바로 실행되는 것이 아니라 실행대기열에 저장되어 차례를 기다린다. 실행대기열은 Queue와 같은 구조로 먼저 실행대기열에 들어온 쓰레드가 먼저 실행된다.
2. 실행대기상태에 있다가 자신의 차례가 되면 실행상태가 된다.
3. 주어진 실행시간이 다되거나 yield()를 만나면 다시 실행대기상태가 되고 다음 차례의 쓰레드가 실행상태가 된다.
4. 실행 중에 `suspend()`, `sleep()`, `wait()`, `join()`, `I/O block`에 의해 일시정지상태가 될 수 있다. `I/O block`은 입출력작업에서 발생하는 지연상태를 말한다.
5. 지정된 일시정지시간이 다되거나(time-out), `notify()`, `resume()`, `interrupt()`가 호출되면 일시정지상태를 벗어나 다시 실행대기열에 저장되어 차례를 기다린다.
6. 실행을 모두 마치거나 stop()이 호출되면 쓰레드는 소멸된다.

단, 무조건 번호 순서대로 쓰레드가 수행되는 것은 아니다.



## 쓰레드의 메소드



| 메서드                                                       | 설명                                                         |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| static void sleep(long millis) static void sleep(long millis, int nanos) | 지정된 시간동안 쓰레드를 일시정지시킨다. 지정한 시간이 지나고나면 자동적으로 실행대기상태가 된다. |
| void join( ) void join(long millis) void join(long millis, int nanos) | 지정된 시간동안 쓰레드가 실행되도록 한다. 지정된 시간이 지나거나 작업이 종료되면 join( )을 호출한 쓰레드로 다시 돌아와 실행을 계속한다. |
| void interrupt( )                                            | sleep( )이나 join( )에 의해 일시정지상태인 쓰레드를 깨워서 실행대기상태로 만든다. 해당 쓰레드에서는 InterruptedException이 발생함으로써 일시정지상태를 벗어나게 된다. |
| void stop( )                                                 | 쓰레드를 즉시 종료시킨다.                                    |
| void suspend( )                                              | 쓰레드를 일시정지시킨다. resume( )을 호출하면 다시 실행대기상태가 된다. |
| void resume( )                                               | suspend( )에 의해 일시정지상태에 있는 쓰레드를 실행대기상태로 만든다. |
| static void yield( )                                         | 실행 중에 자신에게 주어진 실행시간을 다른 쓰레드에게 양보하고 자신은 실행대기상태가 된다. |



- main에서 run() 호출
  : 단순히 클래스에 선언된 메서드를 호출하는 것
- start()
  : 새로운 쓰레드가 작업을 실행하는데 필요한 호출스택을 생성한 후 run()을 호출해 호출스택에 run 실행

<img src="https://blog.kakaocdn.net/dn/be7rq9/btrTfOGml7l/4xRUJaY0z2jZTzWFrZ9kTk/img.png" width = 500 height = 400>



* long stackSize() : 새로운 쓰레드의 스택 사이즈를 의미. 
  * 0이면 이 인자는 없는것과 같다.
  * stackSize는 가상 머신이 스레드의 스택에 할당 할 주소 공간의 대략적인 바이트 수입니다.

* sleep() : 쓰레드를 지정한 시간동안 멈춤



* interrupt() : 쓰레드가 작업을 끝내기 전에 취소하는 것

- suspend(): 쓰레드 정지
- resume(): suspend로 정지된 쓰레드를 재실행
- stop(): 호출 즉시 쓰레드 종료



* yield() : 다음 차례에 쓰레드에게 실행시간을 양보할 때 사용
  * yeild()와 interrupt()를 적절히 사용해 응답성이 높은 프로그램을 구현할 수 있다.

* join() 자신이 하던 작업을 잠시 멈추고 다른 쓰레드가 지정된 시간동안 작업을 수행하도록 할 때 사용
* 시간을 지정하지 않으면 해당 쓰레드가 작업을 모두 마칠 때까지 기다리게 되는데, 작업 중에 다른 쓰레드의 작업이 먼저 수행되어야 할 때 사용



### 이외의 메소드

- currentThread() : 현재 실행중인 thread 객체의 참조를 반환.
- destroy() : clean up 없이 쓰레드를 파괴. @Deprecated 된 메소드로 suspend()와 같이 교착상태(deadlock)을 발생시키기 쉽다.
- isAlive() : 쓰레드가 살아있는지 확인하기 위한 메소드. 쓰레드가 시작되고 아직 종료되지 않았다면 살아있는 상태
- setPriority(int newPriority) : 쓰레드의 우선순위를 새로 설정할 수 있는 메소드.
- getPriority() : 쓰레드의 우선순위를 반환.
- setName(String name) : 쓰레드의 이름을 새로 설정.
- getName(String name) : 쓰레드의 이름을 반환.
- getThreadGroup() : 쓰레드가 속한 쓰레드 그룹을 반환합니다. 종료됐거나 정지된 쓰레드라면 null을 반환.
- activeCount() : 현재 쓰레드의 쓰레드 그룹 내의 쓰레드 수를 반환
- enumerate(Thread[] tarray) : 현재 쓰레드의 쓰레드 그룹내에 있는 모든 활성화된 쓰레드들을 인자로 받은 배열에 넣는다. 그리고 활성화된 쓰레드의 숫자를 int 타입의 정수로 반환.
- dumpStack() : 현재 쓰레드의 stack trace를 반환.
- setDaemon(boolean on) : 이 메소드를 호출한 쓰레드를 데몬 쓰레드 또는 사용자 쓰레드로 설정.
  JVM은 모든 쓰레드가 데몬 쓰레드만 있다면 종료됩니다. 이 메소드는 쓰레드가 시작되기 전에 호출되야한다.
- isDaemon() : 이 쓰레드가 데몬 쓰레드인지 아닌지 확인하는 메소드. 데몬쓰레드면 true, 아니면 false 반환
- getStackTrace() : 호출하는 쓰레드의 스택 덤프를 나타내는 스택 트레이스 요소의 배열을 반환.
- getAllStackTrace() : 활성화된 모든 쓰레드의 스택 트레이스 요소의 배열을 value로 가진 map을 반환. key는 thread .
- getId() : 쓰레드의 고유값을 반환. 고유값은 long 타입의 정수 .
- getState() : 쓰레드의 상태를 반환.



> * resume(), stop(), suspend()는 쓰레드를 교착상태(dead-lock)으로 만들기 쉽기 때문에 deprecated 되었다.
>
> 
>
> * stop()을 이용해 쓰레드를 종료시키는 방법은 옳지 않다.
>
> * 자바에서는 쓰레드를 종료할 수 있는 방법이 없다.
>
> * 개발자가 직접 처리해야지 시스템에 맞기는 것이 아니다.



### Object 클래스에 선언된 쓰레드와 관련있는 메소드들

<img src="https://blog.kakaocdn.net/dn/cz8PSI/btrTgrwQKjW/l6antVDAuXXswG2cwICzzk/img.png" width= 750 height =500>



### 아래의 코드는 JVM의 가비지 컬렉터(Garbage collector)를 흉내 내어 간단히 구현한 것.

최대 메모리가 1000인 상태에서 사용된 메모리가 60%를 초과한 경우 gc 쓰레드가 깨어나서 메모리는 비우는 작업을 한다. 

* 이때 만약 join이 없는 상태로 한다면 main쓰레드는 계속해서 메모리를 늘려가고 최악의 경우 1000이 넘었는데도 계속 해서 메모리를 쌓을 수 있다. 
* 이때 gc.join()을 사용해서 gc가 작업할 시간을 주고 main 쓰레드는 지정된 시간동안 대기하는것이 필요하다.

```java
public class ThreadExample {
    public static void main(String[] args) {
        MyThread_1 gc = new MyThread_1();
        gc.setDaemon(true);
        gc.start();

        int requiredMemory = 0;

        for (int i = 0; i < 20; i++) {
            requiredMemory = (int)(Math.random() * 10) * 20;

            // 필요한 메모리가 사용할 수 있는 양보다 크거나 전체 메모리의 60%이상을
            // 사용했을 경우 gc를 깨웁니다.
            if (gc.freeMemory() < requiredMemory || gc.freeMemory() < gc.totalMemory() * 0.4) {
                gc.interrupt(); // 잠자고 있는 gc를 깨운다.
                try {
                    gc.join(100); // join() 을 호출해서 gc가 작업할 시간을 주고 main 쓰레드는 기다립니다.
                }catch (InterruptedException e) {

                }
            }

            gc.usedMemory += requiredMemory;
            System.out.println("usedMemory:" + gc.usedMemory);
        }
    }
}

class MyThread_1 extends Thread {
    final static int MAX_MEMORY = 1000;
    int usedMemory = 0;

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000 * 10);
            }catch (InterruptedException e) {
                System.out.println("Awaken by interrupt().");
            }

            gc();   // garbage collection을 수행합니다.
            System.out.println("Garbage Collected. Free Memory : " + freeMemory());
        }
    }

    public void gc() {
        usedMemory -= 300;
        if (usedMemory < 0) usedMemory = 0;
    }

    public int totalMemory() { return MAX_MEMORY; }
    public int freeMemory() { return MAX_MEMORY - usedMemory; }
}
```

* 출처 : https://parkadd.tistory.com/48



### CountDownLatch

**CountDownLatch**

우리가 사용하는 고급 언어는 대부분 Concurrency(병행성)에 관련된 API를 제공한다.

자바도 마찬가지로 atomic, volitile, semaphore등 기본적인 병행성 관련 메커니즘에서 CyclicBarrier, CountDownLatch등의 고급기능까지 제공한다.



**CountDownLatch는 언제 쓸까?**

* **쓰레드를 N개 실행했을 때,** 일정 개수의 쓰레드가 모두 끝날 때 까지 기다려야지만 다음으로 진행할 수 있거나 다른 쓰레드를 실행시킬 수 있는 경우 사용한다.

* 예를들어서 리스트에 어떤 자료구조가 있고, 각 자료구조를 병렬로 처리한 후 배치(batch)로 데이터베이스를 업데이트 한다거나 다른 시스템으로 push하는 경우가 있다.



**CountDownLatch의 어떤점이 이를 가능하게 하는가?**

* **CountDownLatch를 초기화 할 때 정수값 count를 넣어준다.**

* 쓰레드는 마지막에서 countDown() 메서드를 불러준다. 그러면 초기화 때 넣어준 정수값이 하나 내려간다.

* 즉 각 쓰레드는 마지막에서 자신이 실행완료했음을 countDown 메서드로 알려준다.

이 쓰레드들이 끝나기를 기다리는 쪽 입장에서는 await()메서드를 불러준다. 그러면 현재 메서드가 실행중이 메인쓰레드는 더이상 진행하지않고 CountDownLatch의 count가 0이 될 때까지 기다린다. 0이라는 정수값이 게이트(Latch)의 역할을 한다. 카운트다운이 되면 게이트(latch)가 열리는 것이다.

------

## I/O Blocking

사용자 입력을 받을 때는 사용자 입력이 들어오기 전까지 해당 쓰레드가 일시정지 상태가 된다. 이를 I/O 블로킹이라고 한다.

한 쓰레드 내에서 사용자 입력을 받는 작업과 이와 관련 없는 작업 두 가지 코드를 작성하면, 사용자 입력을 기다리는 동안 다른 작업 또한 중지되기 때문에 CPU의 사용 효율이 떨어진다.

이 경우 사용자 입력을 받는 쓰레드와, 이와 관련 없는 다른 작업을 하는 **쓰레드를 분리해주면 더욱 효율적으로 CPU를 사용**할 수 있다.



# 쓰레드의 우선순위

실제 Thread 클래스안에는 많은 필드가 존재하지만 public 접근 제어자인 필드는 단 3개만 존재.  


모두 쓰레드의 우선 순위에 대한 상수 필드인데 3가지는 다음과 같다.

 

- public final static int MIN_PRIORITY = 1

  * 쓰레드가 가질 수 있는 우선 순위의 최소값.

  

- public final static int NORM_PRIORITY = 5

  * 쓰레드가 가지는 기본 우선 순위 값입니다.

  

- public final static int MAX_PRIORITY = 10

  * 쓰레드가 가질 수 있는 우선 순위의 최대값.



### 쓰레드의 우선순위

쓰레드 스케쥴링은 우선순위방식과 순환할당 방식이 있다.

우선순위 방식은 우선순위가 높은 쓰레드가 실행상태를 더 많이 가지도록 스케쥴링하는 것이다.  우선순위는 개발자가 지정할 수 있다.

```java
public static void test2() {
    PrimeThread1 p1 = new PrimeThread1(143);
    PrimeThread2 p2 = new PrimeThread2(143);
    p1.start();
    p2.start();

}
```

같은 우선순위를 가지는 쓰레드를 실행시키는 경우 번갈아가면서 실행한다.   

 원래 소스코드 실행은 순차적으로 실행되지만 쓰레드의 경우 순차적으로 실행하는 것이 아닌 번갈아가면서 실행되는것을 볼 수 있다.

우선순위가 높다고 해서 무조건 먼저 실행되는것이 아니라 실행 기회를 더 많이 가지는것이기 때문에 정확히 일치하지는 않는다.  

또한 쿼드코어에선 4개의 쓰레드가 병렬성으로 실행될 수 있기때문에 4개 이하의 쓰레드에서는 크게 영향이 없고 5개 이상일때 의미가 생긴다.



**Thread가 관리되는 JVM의 스케쥴링 규칙**

- 라운드 로빈
- 철저한 우선순위 기반
- 가장 높은 우선순위의 스레드가 우선적으로 스케쥴링 됨.
- 동일한 우선순위의 스레드는 돌아가면서 스케쥴링



# Main 쓰레드





모든 자바 애플리케이션은 메인쓰레드를 실행하면서 시작하며 메인 쓰레드는 프로그램이 시작하면 가장 먼저 실행되는 쓰레드이다.  

 모든 쓰레드는 메인 쓰레드로부터 생성된다. 다른 쓰레드를 생성해서 실행하지 않으면, 메인 메서드, 즉 메인 쓰레드가 종료되는 순간

 프로그램도 종료된다.



멀티쓰레드는 메인쓰레드에서 만들어져 실행되는 추가적인 쓰레드인것이다.  싱글 쓰레드 애플리케이션에서는 메인쓰레드가 종료되면 프로세스도 종료되지만 멀티 쓰레드 애플리케이션은 실행중인 쓰레드가 하나라도 있다면 종료되지 않는다. 

여러 쓰레드를 실행하면, 메인 쓰레드가 종료되어도 다른 쓰레드가 작업을 마칠 때까지 프로그램이 종료되지 않는다.

쓰레드는 '사용자 쓰레드(user thread)'와 '데몬 쓰레드(daemon thread)'로 구분되는데,**실행 중인 사용자 쓰레드가 하나도 없을 때 프로그램이 종료된다**.

- main 쓰레드 우선 순위는 기본값(5)
- main 쓰레드에서 실행된 쓰레드들은 따로 우선 순위를 지정하지 않았다면 main 쓰레드 우선순위를 상속 받음

<img src="https://blog.kakaocdn.net/dn/dhKVY7/btrTgJqussl/BGujlhZ9KmvB68vkoBnWQk/img.png" width= 600 height =500>

* 출처: https://www.geeksforgeeks.org/main-thread-java/?ref=lbp



# Daemon Thread

다른 일반 Thread의 작업을 돕는 **보조적인 역할을 수행하는 Thread** 이다.

일반 Thread가 모두 종료되면 Daemon Thread는 강제종료 되는데, 이러한 이유는 데몬 쓰레드는 일반 쓰레드를 돕는 역할을 하는 것이며, 일반 스레드가 종료되면 의미가 없어지기 때문이다.

이 점을 제외하고는 데몬쓰레드와 일반 쓰레드의 차이점은 없다.

* `주로 가비지 컬렉터, (워드 등의) 자동저장, 화면 자동갱신 등에 사용된다.`

- JVM의 가비지 컬렉터(GC)가 데몬쓰레드의 하나이다.



데몬쓰레드의 작성과 실행법은 일반 쓰레드와 동일하다.

다만, 쓰레드를 생성한 다음 실행 전 setDaemon(true) 로 자신이 데몬 쓰레드임을 설정해주도록 한다.

* (추가적으로 데몬 쓰레드가 생성한 쓰레드는 자동적으로 데몬 쓰레드가 된다)

> * 자신을 파생시킨 부모 쓰레드가 끝나면 바로 끝난다!



#### 데몬 쓰레드와 관련된 메소드

- isDaemon() : 쓰레드가 데몬인지 확인. 데몬 쓰레드면 true, 아니면 false
- setDaemon() : 쓰레드를 데몬 쓰레드로 또는 사용자 쓰레드로 변경. true면 데몬 쓰레드.

* 쓰레드를 데몬 쓰레드로 설정하는법

```java
thread.setDaemon(true);
thread.start();
```

### 데몬쓰레드를 만든 이유?

그래서 데몬쓰레드는 왜 만들었을까?

- 예를 들어 모니터링하는 쓰레드를 별도로 띄워 모니터링을 하다가, Main 쓰레드가 종료되면 관련된 모니터링 쓰레드가 종료되어야 프로세스가 종료될 수 있다. 모니터링 스레드를 데몬 쓰레드로 만들지 않으면 프로세스가 종료할 수 없게 된다. 이렇게 부가적인 작업을 수행하는 쓰레드를 선언할 때 데몬 쓰레드를 만든다.



## 쓰레드 그룹

서로 관련된 쓰레드는 쓰레드 그룹으로 묶어서 관리할 수 있다. 

쓰레드 그룹은 다른 쓰레드 그룹을 포함시킬 수 있다. 마치 디렉토리 안에 하위 디렉토리를 둘 수 있는 것과 비슷하다. 쓰레드 그룹은 보안상의 이유로 도입된 개념으로, 자신이 속한 쓰레드 그룹이나 하위 쓰레드 그룹은 변경할 수 있지만 다른 쓰레드 그룹의 쓰레드는 변경할 수 없다.

모든 쓰레드는 반드시 하나의 쓰레드 그룹에 속하며, 쓰레드 생성 시 쓰레드 그룹을 지정해주지 않으면 자동적으로 main 쓰레드 그룹에 속하게 된다. 왜냐하면 쓰레드는 자신을 생성한 쓰레드(부모 쓰레드)의 그룹과 우선순위를 상속받기 때문이다.

쓰레드를 그룹에 포함시키려면 Thread의 생성자를 이용해야 한다.

```java
Thread(ThreadGroup group, String name)
Thread(ThreadGroup group, Runnable target)
Thread(ThreadGroup group, Runnable target, String name)
Thread(ThreadGroup group, Runnable target, String name, long stackSize)
```

쓰레드는 쓰레드 그룹으로 관리되며 쓰레드 그룹에 대한 일괄적인 작업 처리가 가능하다는 것 정도만 알고 넘어가면 될듯 하다.



# 동기화

멀티 쓰레드 프로세스에서는 여러 프로세스가 메모리를 공유하기 때문에, 한 쓰레드가 작업하던 부분을 다른 쓰레드가 간섭하는 문제가 생길 수 있다. 어떤 쓰레드가 진행 중인 작업을 다른 쓰레드가 간섭하지 못하도록 하는 작업을 동기화라고 한다.



> * 만일 쓰레드A가 작업하던 도중 다른 쓰레드 B에게 제어권이 넘어갔을 때, 쓰레드A가 작업하던 공유데이터를 쓰레드B가 임의로 변경하였다면, 다시 쓰레드A가 제어권을 받아서 나머지 작업을 마쳤을 때 의도하지 않던 결과를 얻을 수 있다.



이러한 일을 방지하기 위해 한 쓰레드가 특정 작업을 끝마치기 전에 다른 쓰레드에 의해 방해바지 않도록 하는 것이 필요하다.

그래서 도입된 개념이 바로 **`'임계 영역(Critical Section)' 과 '잠금(락, lock)'`** 이다.

공유 데이터를 사용하는 코드 영역을 임계 영역으로 지정해두고,

공유 데이터가 가지고 있는 lock을 획득한 단 하나의 쓰레드만 이 영역 내의 코드를 수행할 수 있게 한다.

쓰레드가 임계영역 내의 모든 코드를 수행하고 벗어나서 lock을 반납해야만, 다른 쓰레드가 반납된 lock을 획득하여 임계영역의 코드를 수행할 수 있게 된다.

> * 이렇게, 한 쓰레드가 진행 중인 작업을 다른 쓰레드가 간섭하지 못하도록 막는 것을 **`"쓰레드 동기화(synchronization)"`** 이라 한다.



> 임계 영역은 멀티쓰레드 프로그래밍의 성능을 좌우하기 때문에 가능하면 메서드 전체에 락을 거는 것 보다 synchronized 블럭으로 임계영역을 최소화해서 효율적인 프로그램이 되도록 노력해야 한다.



- 이것을 쓰레드에 안전하다고 하다 (Thread-safe)
- 자바에서 동기화 하는 방법은 3가지로 분류된다
  - Synchronized 키워드
  - Atomic 클래스
  - Volatile 키워드



자바에서는 synchronized블럭을 이용해서 쓰레드의 동기화를 지원했지만, JDK1.5부터 `java.util.concurrent.locks`와 `java.util.concurrent.atomic`패키지를 통해서 다양한 방식으로 동기화를 구현할 수 있도록 지원하고 있다.



#### **happens-before relationship**

- 메모리 의존 관계에 대한 내용
- A happens before B
  - A, B 는 Statement
  - 구문 B 가 해당 메모리를 사용하기 전에 구문 A 가 메모리의 사용을 끝냈다는 걸 보장



#### mutex

- 공유 리소스에 대해 하나의 쓰레드(혹은 프로세스)만 접근할 수 있도록 locking 방법을 사용
- lock: critical section 에 접근할 수 있는 권한을 얻음, 종료될 때까지 다른 프로세스(혹은 쓰레드)는 대기
- unlock: critical section 에서 나올 때 권한 해제, 다른 프로세스(혹은 쓰레드)가 해당 구역 진입 가능

```java
mutex lock;
void func(...){
	...
	mutex_lock(lock);
	//Critical Section
	mutex_unlock(lock);
}
```

#### semaphore

- 프로세스(혹은 쓰레드)에서 세마포어 값을 변경하는 동안 다른 프로세스가 동시에 세마포어를 변경하지 못하도록 하는 방법
  - 세마포어 변수: 정수값을 갖는 변수
  - P 명령(혹은 함수) : 세마포어 변수를 활성화하여 다른 프로세스가 수행되지 못하도록 함
  - V 명령(혹은 함수) : 세마포어 변수를 비활성화하여 다른 프로세스가 수행될 수 있도록 함
  - 임계구역(critical section) : P 명령과 V 명령 사이 코드 블럭, 동기화 보장

```java
s: 세마포어 변수, 최초값 0 
p func: P 명령
v func: V 명령

void p(semaphore s, int i){
	while(s < i){
		wait
	}
	s = s - i;
}

void v(s, int i){
	s = s + i;
}

void func(...){
	p(s, i);
	// critical section
	...
	v(s, i);
}
```



#### synchronized 키워드

- synchronized 키워드 사용
  - **happens-before relationship 성립 위함**
  - 현재 리소스를 접근한 쓰레드를 제외한 나머지 쓰레드는 리소스 접근 불가
  - synchronized 키워드를 너무 남용하면 성능 저하 발생
  - synchronized 메소드
    - 동일한 객체에서 synchronized 메소드의 두 호출이 동시에 실행될 수 없음
    - lock 지정 불필요
    - happens-before 관계 보장
    - 생성자의 경우 synchronized 불가
      - 객체를 생성한 쓰레드만 해당 생성자에 접근되어야 함
    - lock 해제는 uncaught exception에 의해 반환이 발생하는 경우에도 발생



```java
1. **메서드 전체를 임계 영역으로 지정**
   public **synchronized** void calcSum() {
   	// …
   }

2. **특정한 영역을 임계 영역으로 지정**
   **synchronized**(객체의 참조변수) {
   	// …
   }
```



첫 번째 방법은 메서드 앞에 synchronized를 붙이는 것인데, synchronized를 붙이면 메서드 전체가 임계 영역으로 설정된다. 쓰레드는 synchronized메서드가 호출된 시점부터 해당 메서드가 포함된 객체의 lock을 얻어 작업을 수행하다가 메서드가 종료되면 lock을 반환한다.

두 번째 방법은 메서드 내의 코드 일부를 블럭으로 감싸고 블럭 앞에 `synchronized(참조변수)`를 붙이는 것인데, 이 때 참조변수는 락을 걸고자 하는 객체를 참조하는 것이어야 한다. 이 블럭을 synchronized블럭이라고 부르며, 이 블럭의 영역 안으로 들어가면서부터 쓰레드는 지정된 객체의 lock을 얻게 되고, 이 블럭을 벗어나면 lock을 반납한다.



*   두 방법 모두 lock의 획득과 반납이 모두 자동적으로 이루어지므로 우리가 해야 할 일은 그저 임계 영역만 설정해주는 것뿐이다.



### 락(lock) Condition 을 이용한 동기화

lock은 일종의 자물쇠 개념이다.



`**"java.util.concurrent.locks"**` 패키지가 제공하는 lock 클래스를 활용할 수 있다.

* synchronized를 이용하는 동기화는 필요한 블럭을 synchronized{ } 이용하여 lock을 건다.

여러 쓰레드가 **`경쟁 상태(race condition)`**에 있을 때 어떤 쓰레드가 진입권을 획득할지 순서를 보장하지 않는다.

**→ 암시적인(Implicit) 락**

Lock은 lock() - unlock() 메서드를 호출함으로써 어떤 쓰레드가 먼저 락을 획득하게 될지 순서를 지정할 수 있다.

**→ 명시적인(explicit) 락**

> * **`경쟁 상태 (race condition)`** 공유하는 자원이 있는데 공유하는 자원에 접근하는 여러 쓰레드중 어떤 것이 먼저 접근하냐에 따라 결과가 달라질 수 있는 경우가 있다. → race condition에 의해 발생되었다. 라고 하기도 한다.



### synchronized와 lock을 구분짓는 키워드는 fairness(공정성) 이다.

여기서 공정성이란 모든 쓰레드가 자신의 작업을 수행할 기회를 공평하게 갖는 것을 의미한다.

공정한 방법에선 큐 안에서 쓰레들이 무조건 순서를 지켜가며 lock을 확보한다.

불공정한 법에선 만약 특정 쓰레드에 lock이 필요한 순간 release가 발생하면 대기열을 건너뛰는 새치기 같은 일이 벌어지게 된다.

> * 다른 쓰레드들에게 우선순위가 밀려 자원을 계속해서 할당받지 못하는 쓰레드가 존재하는 상황을 `starvation(기아 상태)`라고 부르며, 이 기아 상태를 해결하기 위해 공정성이 필요하다.



synchronized는 공정성을 지원하지 않는 반면에 **ReentrantLock**은 생성자의 인자를 통해 공정/불공정을 설정할 수 있다.



#### **`ReentrantLock`**

- 재진입이 가능한 lock. 가장 일반적인 배타 lock
- ReentrantLock은 가장 일반적인 lock이다. ‘reentrant(재진입할 수 있는)’이라는 단어가 앞에 붙은 이유는 wait() & notify()처럼, 특정 조건에서 lock을 풀고 나중에 다시 lock을 얻어 이후의 작업을 수행할 수 있기 때문이다.



#### **`ReentrantReadWriteLock`**

- 읽기에는 공유적이고, 쓰기에는 배타적인 lock
-  ReentrantReadWriteLock은 읽기를 위한 lock과 쓰기를 위한 lock을 제공한다. ReentrantLock은 배타적인 lock이라서 무조건 lock이 있어야만 임계 영역의 코드를 수행할 수 있지만, ReentrantReadWriteLock은 읽기 lock이 걸려있으면, 다른 쓰레드가 읽기 lock을 중복해서 걸고 읽기를 수행할 수 있다. 읽기는 내용을 변경하지 않으므로 동시에 여러 쓰레드가 읽어도 문제가 되지 않는다. 그러나 읽기 lock이 걸린 상태에서 쓰기 lock을 거는 것은 허용되지 않는다. 



####  **`StampedLock`**

* ReentrantReadWriteLock에 낙관적인 lock의 기능을 추가

*   StampedLock은 lock을 걸거나 해지할 때 ‘스탬프(long타입의 정수값)’를 사용하며, 읽기와 쓰기를 위한 lock외에 ‘낙관적 읽기 lock(optimistic reading lock)’이 추가된 것이다. 읽기 lock이 걸려있으면, 쓰기 lock을 얻기 위해서는 읽기 lock이 풀릴 때까지 기다려야하는데 비해 ‘낙관적 읽기 lock’은 쓰기 lock에 의해 바로 풀린다. 따라서 낙관적 읽기에 실패하면, 읽기 lock을 얻어서 다시 읽어 와야 한다. **무조건 읽기 lock을 걸지 않고, 쓰기와 읽기가 충돌할 때만 쓰기가 끝난 후에 읽기 lock을 거는 것이다.**





### java.util.concurrent 패키지 주요 기능

* Locks 패키지
  - 상호 배제를 사용할 수 있는 클래스를 제공

* Atomic 패키지
  - 동기화 되어있는 변수를 제공한다

* Executors 클래스
  - 쓰레드 풀 생성, 생명주기 관리 , Task 등록과 실행 등을 간편하게 처리가능

* Queue 클래스
  - Thread-safe한 FIFO Queue를 제공

- Synchronizers 클래스
  - 특수한 목적의 동기화를 처리하는 5개의 클래스를 제공
    - Semaphore, CountDownLatch, CyclicBarrier,Phaser, Exchanger
      - 간략하게 Semaphore는 동시에 접근이 가능한 쓰레드의 개수를 지정하여 설정이 가능하다



## java.util.concurrent.locks

- synchronized 블록을 사용했을 떄와 동일한 메커니즘으로 동작한다
- 내부적으로 synchronized를 사용하여 구현되어 있고, synchronized를 더욱 유연하고 세밀하게 처리하기 위해 사용하는 것이며 대체하는 목적은 아니다.

## Interface

- Lock

  - 공유 자원에 한번에 한 쓰레드만 read, write를 수행 가능하도록 한다

- ReadWriteLock

  - Lock에서 한단계 발전된 메커니즘을 제공하는 인터페이스이다. 공유 자원에 여러개의 쓰레드가 read를 수행할 수 있지만, write는 한번에 한 쓰레드만 수행이 가능하다

- Condition

  * (Spring의 Condition이 아님)

  - Object 클래스의 monitor method 인 wait, notify, notifyAll 메서드를 대체한다. wait→await, notify → signal, notifyAll → signalAll 로 생각하면 된다.



## Locks Interface의 구현체

- ReentrantLock
  - Lock의 구현체이며 임계 영역의 시작과 종료지점을 직접 명시할 수 있게 해준다
- ReentrantReadWriteLock
  - ReadWriteLock의 구현체

## 주요 메서드

- lock()
  - Lock 인스턴스에 잠금을 걸어둔다. Lock 인스턴스가 이미 잠겨있는 상태라면, 잠금을 걸어둔 쓰레드가 unlock()을 호출할 때까지 실행이 비활성화된다
- lockInterruptibly()
  - 현재 쓰레드가 interrupted 상태가 아닐 때 Lock 인스턴스에 잠금을 건다. 현재 쓰레드가 intterupted 상태면 InterruptedException를 발생시킨다.
- tryLock()
  - 즉시 Lock 인스턴스에 잠금을 시도하고 성공 여부를 boolean 타입으로 반환한다.
  - tryLock(long timeout, TimeUnit timeUnit)
    - tryLock()과 동일하지만, 잠금이 실패했을 때 바로 false를 반환하지 않고 인자로 주어진 시간동안 기다린다.
- unlock()
  - Lock 인스턴스의 잠금을 해제한다.
- newCondition()
  - 현재 Lock 인스턴스와 연결된 Condition 객체를 반환한다.



# 데드락(교착상태 )

데드락은, 두 개 이상의 쓰레드들이 동기화된 블럭 (동시에 하나의 쓰레드만 접근할 수 있는 블럭) 에서 자신의 실행을 멈추고 서로의 실행을 기다리다가 막힌 상태를 말한다.

* 쓰레드1이 object1을 점유하고 object2를 요청하는데 쓰레드 2가 object2를 점유하고 object1을 요청한다면 서로의 자원을 요청하지만 해당 자원을 점유해서 놓지 않고 있기 때문에 데드락 생태에 걸린다.  이를 해결하기 위해선 어느 한쪽의 자원을 풀어줘야 한다.



### 교착상태의 조건

1. [상호배제](https://ko.m.wikipedia.org/wiki/상호배제)(Mutual exclusion) : 프로세스들이 필요로 하는 자원에 대해 배타적인 통제권을 요구한다.
2. [점유대기](https://ko.m.wikipedia.org/w/index.php?title=점유대기&action=edit&redlink=1)(Hold and wait) : 프로세스가 할당된 자원을 가진 상태에서 다른 자원을 기다린다.
3. [비선점](https://ko.m.wikipedia.org/wiki/비선점_스케줄링)(No preemption) : 프로세스가 어떤 자원의 사용을 끝낼 때까지 그 자원을 뺏을 수 없다.
4. [순환대기](https://ko.m.wikipedia.org/w/index.php?title=순환대기&action=edit&redlink=1)(Circular wait) : 각 프로세스는 순환적으로 다음 프로세스가 요구하는 자원을 가지고 있다.



> * 이 조건 중에서 한 가지라도 만족하지 않으면 교착 상태는 발생하지 않는다. 이중 순환대기 조건은 점유대기 조건과 비선점 조건을 만족해야 성립하는 조건이므로, 위 4가지 조건은 서로 완전히 독립적인 것은 아니다.



### 교착상태를 관리 하기위해 예방, 회피, 무시 할 수 있다고 한다.

### **교착 상태의 예방**

- **상호배제 조건의 제거** 교착 상태는 두 개 이상의 프로세스가 공유가능한 자원을 사용할 때 발생하는 것이므로 공유 불가능한, 즉 상호 배제 조건을 제거하면 교착 상태를 해결할 수 있다.
- **점유와 대기 조건의 제거** 한 프로세스에 수행되기 전에 모든 자원을 할당시키고 나서 점유하지 않을 때에는 다른 프로세스가 자원을 요구하도록 하는 방법이다. 자원 과다 사용으로 인한 효율성, 프로세스가 요구하는 자원을 파악하는 데에 대한 비용, 자원에 대한 내용을 저장 및 복원하기 위한 비용, [기아 상태](https://ko.m.wikipedia.org/wiki/기아_상태), 무한대기 등의 문제점이 있다.
- **비선점 조건의 제거** 비선점 프로세스에 대해 선점 가능한 프로토콜을 만들어 준다.
- **환형 대기 조건의 제거** 자원 유형에 따라 순서를 매긴다.

> * 이 교착 상태의 해결 방법들은 자원 사용의 효율성이 떨어지고 비용이 많이 드는 문제점이 있다.



### 교착상태 회피

자원이 어떻게 요청될지에 대한 추가정보를 제공하도록 요구하는 것으로 시스템에 circular wait가 발생하지 않도록 자원 할당 상태를 검사한다.

교착 상태 회피하기 위한 알고리즘으로 크게 두가지가 있다.

1. [자원 할당 그래프 알고리즘](https://ko.m.wikipedia.org/w/index.php?title=자원_할당_그래프_알고리즘&action=edit&redlink=1) (Resource Allocation Graph Algorithm)
2. [은행원 알고리즘](https://ko.m.wikipedia.org/w/index.php?title=은행원_알고리즘&action=edit&redlink=1) (Banker's algorithm)

### 교착상태 무시

예방과 회피방법을 활용하게되면 자연적으로 성능상 이슈가 발생될탠데,

데드락 발생에 대한 상황을 고려하는 것의 코스트가 낮다면 별다른 조치를 하지 않을 수도 있다고 한다.





### 아래 예제는 oracle 문서 예제 이다.

```java
public class Deadlock {
    static class Friend {
        private final String name;
        public Friend(String name) {
            this.name = name;
        }
        public String getName() {
            return this.name;
        }
        public synchronized void bow(Friend bower) {
            System.out.format("%s: %s" + "  has bowed to me!%n", 
                this.name, bower.getName());
            bower.bowBack(this);
        }
        public synchronized void bowBack(Friend bower) {
            System.out.format("%s: %s" + " has bowed back to me!%n",
                this.name, bower.getName());
        }
    }

    public static void main(String[] args) {
        final Friend lee = new Friend("lee");
        final Friend kim = new Friend("kim");

        new Thread(new Runnable() {
            public void run() { lee.bow(kim); }
        }).start();

        new Thread(new Runnable() {
            public void run() { kim.bow(lee); }
        }).start();
    }
}
```

- lee 객체는 bow 메소드를 쓰레드로 실행, kim 객체도 bow 메소드를 쓰레드로 실행
- 교착 상태가 실행될 때 bowBack 메소드를 실행하려 함
- 이때 각 쓰레드는 다른 쓰레드가 bow 를 빠져나오기를 wait 하고 있는 상황 발생
- 무한히 대기
- 해결 방안
  - timeout
    - 데드락 발생 시 무조건 끝날 수 있게 하는 것이 중요
    - 그러나 모든 쓰레드에서 timeout 이 발생하면 livelock 발생 가능
      - livelock: 모든 쓰레드가 deadlock 이 풀렸다가 발생했다가 하는 과정을 반복하는 상태
  - Atomic 변수 사용
    - Atomic 변수는 항상 atomic 을 보장
    - 그러므로 locking 이 필요없음
    - 성능 저하 발생 가능성 유



#### visualVM을 이용하여 thread dump를 떠서 데드락 상태를 확인할 수 있다.

<img src="https://blog.kakaocdn.net/dn/o8b7h/btrTg395QZo/j6kdkHpFOW7uWHOKevgaX1/img.png" width = 600 height =500>

**`heap dump 와 thread dump는 다르다.`** 

* → **heap dump** : 메모리의 스냅샷을 뜨는 것. 
* → **thread dump** : 모든 쓰레드의 스냅샷을 뜨는 것.



# 싱글턴 패턴에서의 Thread-safe (Option)

- 디자인 패턴 중, 단 하나의 공유 객체를 만들어 재사용하는 패턴을 싱글턴패턴이라고 한다.
- 싱글턴 패턴은 Thread-safe를 고려하지 않으면 싱글턴 패턴이 아니게될 가능성이 있다.
- 따라서 싱글턴패턴을 Thread-safe하게 만드는 방법을 추가적으로 정리해보았다.

## synchronized 

```java
public class Singleton {
    private static Singleton instance;

    public static synchronized Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }
}
```

### 문제점

- 7장에서 말한 것과 같이 스레드 문제로부터 안전해졌지만 불필요한 락이 많아 성능상의 이슈가 발생할 확률이 있다.

## Double-Checkd Locking 

```java
package study.moon.Test;

public class Singleton {
    private static volatile Singleton instance;

    public static synchronized Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
```

### volatile 키워드

- 사실 우리는 메인 메모리에 항상 직접 접근하여 연산을 하는것이 아니라 성능상의 이익을 얻기 위해 cpu 캐시에 저장된 값으로 연산을 진행한다. 그래서 멀티쓰레드 환경에서는 특정 쓰레드에서 값을 변경하여도 cpu캐시에서만 값이 변경되어, 메인 메모리에는 반영이 되지 않아 다른 쓰레드에서 문제가 발생할 수 있다. 이 때, volatile 키워드를 사용하면 해당 값을 cpu캐시에 갱신하지 않고 직접 메인 메모리에 갱신하게된다.

### 문제점

- 역시 아직 성능상의 문제가 있다.
- 코드 가독성이 떨어진다.
- 1.4 이하의 버전에서는 적용할 수 없다.

## Eager Initialization

```java
package study.moon.Test;

public class Singleton {
    /*
    static -> 인스턴스화와 상관없이 접근 가능
    private -> 외부에서 instance 바로 접근 방지
     */
    private static Singleton instance = new Singleton();

    /*
    private -> new Singleton() 방지
     */
    private Singleton() {}

    //오로지 해당 메서드를 통해서만 인스턴스 접근 가능
    public static Singleton getInstance() {
        return instance;
    }
}
```

### 문제점

- 싱글턴 객체 사용 유무에 관계없이 클래스가 로딩되는 시점에 객체가 생성되어 메모리를 잡아먹을 수 있다.

## Lazy Initialization

```java
package study.moon.Test;

public class Singleton {

    private static Singleton instance;
    private Singleton() {}

    public static Singleton getInstance() {
        if (instance == null) { // 처음으로 호출될 때 메모리에 할당한다.
            instance = new Singleton();
        }
        return instance;
    }
}
```

### 문제점

- Thread-safe하지 않다.

## Holder

```java
package study.moon.Test;

public class Singleton {

    private Singleton() {}

    private static class Holder{//getInstance()가 호출되기 전에는 참조되지 않는다.
        private static final Singleton instance = new Singleton();//클래스 로딩 시점에 한번만 호출, 재할당 금지
    }

    public static Singleton getInstance() {
        return Holder.instance;
    }
}
```

- 많이 사용되는 방법이라고 한다.

## Enum

```java
package study.moon.Test;

public enum Singleton {
    instance
}
```

- 간단하게 구현할 수 있다. enum의 특성을 이용하여 싱글턴 구현이 가능하다.





# 자바 동시성 프로그래밍의 진화

멀티스레딩을 지원하는 API는 시간이 흐름에 따라 지속적으로 발전하였다. 본 포스팅에서 다룬 `Runnable`과 `Thread`는 멀티스레딩을 지원하기 위해 가장 처음 만들어진 API이고 사용하기도 불편하기 때문에 지금은 거의 사용하지 않는다. 지금은 자바 5에 추가된 `ExecutorService`와 `Callable` 등을 사용한다.

자바가 발전하면서 추가된 동시성 관련 API를 살펴보면 대략 다음과 같다.

- 처음
  - Runnable, Thread
- Java 5
  - ExecutorService: 스레드 실행과 태스크 제출을 분리
  - Callable: Runnable의 발전된 형태. 제네릭 지원, 결과 리턴 가능, 예외 던지기 가능
  - Future: 비동기 결과값을 담기위한 객체
- Java 7
  - java.util.concurrent.RecursiveTask 추가: 포크/조인 구현 지원 (fork & join 프레임워크)
- Java 8
  - CompletableFuture: Future를 조합하는 기능을 추가하면서 동시성 강화
  - Stream: 내부적으로 병렬처리 가능
- Java 9
  - 리액티브 프로그래밍을 위한 API 지원: 발행-구독 프로토콜(java.util.concurrent.Flow) 등



### 참조



* https://www.notion.so/10-18f46b32225745cba615498d374e3584

* https://www.notion.so/ac23f351403741959ec248b00ea6870e

* https://leemoono.tistory.com/26

* https://parkadd.tistory.com/48