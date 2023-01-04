## 프로세스와 쓰레드

- 프로세스란 실행중인 프로그램이다.
- 프로그램을 실행하면 OS로부터 실행에 필요한 자원(메모리)를 할당받아 프로세스가 된다.
- 프로세스의 자원을 이용해서 실제로 작업을 수행하는 것이 쓰레드다.

- 모든 프로세스에는 최소한 하나 이상의 쓰레드가 존재한다.
- 쓰레드가 작업을 수행하려면 개별적인 메모리 공간(호출스택)이 필요하다.
- CPU의 코어(core)가 한 번에 단 하나의 작업만 수행할 수 있으므로, 동시에 처리되는 작업의 수는 코어의 개수와 일치한다.



> 프로세스의 구성요소 : 실행 중인 프로그램(메모리). 자원(resource)과 스레드로 구성



## 멀티 프로세스 vs. 멀티 스레드

* 멀티 태스킹(멀티 프로세싱) : 동시에 여러 프로세스를 실행시키는 것
* 멀티 스레딩 : 하나의 프로세스 내에 동시에 여러 스레드를 실행시키는 것
* 프로세스를 생성하는 것보다 스레드를 생성하는 비용이 적다.
* 같은 프로세스 내의 스레드들은 서로 자원을 공유한다.
  * 각 스레드는 자신만의 스택을 가지고,  heap 영역은 공유한다 

### 멀티스레드의 장단점

\- 대부분의 프로그램이 멀티스레드로 작성되어 있다.

장점

* 시스템 자원을 보다 효율적으로 사용할 수 있다. 
* 사용자에 대한 응답성(responseness)이 향상된다. 
* 작업이 분리되어 코드가 간결해 진다.

단점

* 동기화(synchronization)에 주의해야 한다. 
* 교착상태(dead-lock)가 발생하지 않도록 주의해야 한다. 
* 각 스레드가 효율적으로 고르게 실행될 수 있게 해야 한다.



## 쓰레드의 구현과 실행

- 쓰레드를 구현하는 방법은 Thread 클래스를 상속받는 방법과 Runnable 인터페이스를 구현하는 방법이 있다.
- Thread 클래스를 상속받으면 다른 클래스를 상속받을 수 없기 때문에, Runnable 인터페이스를 구현하는 방법이 일반적이다.

### Thread 클래스 상속 (Thread extends) - run()을 오버라이딩

```java
class MyThread extends Thread {
  public void run() { // Thread 클래스의 run()을 오버라이딩
    작업 내용
  }
}
```

### Runnable 인터페이스 구현 (interface implements) - run()을 구현

> Runnable 인터페이스는 오로지 run()만 정의되어 있는 간단한 인터페이스이다.
> Runnable 인터페이스를 구현하기 위해서 해야 할 일은 추상메서드인 run()의 몸통 {}을 만들어 주는 것 뿐이다.

```java
public interface Runnable {
  public abstract void run();
}
```

> 쓰레드를 구현한다는 것은 두 가지 방법 모두, 그저 쓰레드를 통해 작업하고자 하는 내용으로 run()의 몸통{}을 채우는 것 뿐이다.

### 인스턴스 생성 방법

- Thread 클래스 상속

```java
  ThreadA t1 = new ThreadA();
```

- Runnable 인터페이스 구현

```java
  Runnable r = new ThreadB();
  Thread t2 = new Thread(r);
```

> Thread클래스를 상속받으면, 자손 클래스에서 조상인 Thread클래스의 메서드를 직접 호출할 수 있지만,
> Runnable을 구현하면 Thread클래스의 static메서드인 currentThread()를 호출하여 쓰레드에 대한 참조를 얻어 와야만 호출이 가능하다.

```java
class ThreadA extends Thread  {
  public void run() {
    System.out.println(getName());  // 조상인 Thread의 getName() 호출
  }
}
class ThreadB implements Runnable {
  public void run() {
    System.out.println(Thread.currentThread().getName());
    // Thread.currentThread() - 현재 실행중인 Thread 반환
  }
}
```

위의 두가지 방법으로 구현 시 인스턴스 생성방법이 다르다. 

extends 방법은 해당 클래스를 바로 인스턴스화 하면되지만 

implements 방법은 Runable을 통해 생성자 **Thread**를 통해 인스턴스화 해야한다.

* Runable로 인스턴스화 후 Thread클래스에 생성자의 매개변수로 넣어주어야 한다. 

또 하나의 차이는 현재 실행중인 Thread가 무엇인지 확인하는 `getName()메서드`는 Thread를 상속받은 방법으로는 바로사용이 가능하지만, 

 Runable로 구현된 방법에는 메서드가 구현이 되어있지 않기에 현재 실행중인 Thread를 반환하는 `Thread.currentTread()`을 통해 .getName()으로 확인해야 한다.

---

### 쓰레드의 실행

- 쓰레드를 생성했다고 자동을 실행되지 않는다. start()를 호출해야만 쓰레드가 실행된다.
- start()가 호출되어도 바로 실행되는 것이 아니라, 실행대기 상태에 있다가 자신의 차례가 되어야 실행된다.
- 쓰레드의 실행 순서는 OS의 스케쥴러가 작성한 스케쥴에 의해 결정된다.
- 한 번 실행이 종료된 쓰레드는 다시 실행할 수 없다. (하나의 쓰레드에 start()가 한 번만 호출될 수 있다.)

### Start() 와 run()

- main메서드에서 run()을 호출하는 것은 생성된 쓰레드를 실행시키는 것이 아니라, 단순히 클래스에 선언된 메서드를 호출하는 것일 뿐이다.
- start()는 새로운 쓰레드를 위해 호출스택(call stack)을 생성한 후에 run()을 호출해서, 생성된 호출스택에 run()이 첫 번째로 올라가게 한다.
  1. main메서드에서 쓰레드의 start()를 호출한다.
  2. start()는 새로운 쓰레드를 생성하고, 쓰레드가 작업하는데 사용될 호출스택을 생성한다.
  3. 새로 생성된 호출스택에 run()이 호출되어, 쓰레드가 독립된 공간에서 작업을 수행한다.
  4. 호출스택이 2개가 되었으므로 스케쥴러가 정한 순서에 의해 번갈아 가면서 실행된다.
- 주어진 시간동안 작업을 마치지 못한 쓰레드는 다시 자신의 차례가 돌아올 때까지 대기 상태로 있는다.
- 작업을 마친 쓰레드, 즉 run()의 수행이 종료된 쓰레드는 사용하던 호출스택이 모두 비워지고 사라진다.
  - 이는 자바프로그램을 실행하면 호출스택이 생성되고 main메서드가 처음으로 호출되고, main메서드가 종료되면 호출스택이 비워지면서 프로그램도 종료되는 것과 같다.
- 한 쓰레드가 예외가 발생해서 종료되어도 다른 쓰레드의 실행에는 영향을 미치지 않는다.



> run()으로 스레드를 실행시키지 말고, start()로 시켜야 한다.



## 싱글쓰레드와 멀티쓰레드

쓰레드의 I/O 블로킹

- 싱글 쓰레드의 경우 사용자로부터 입력을 기다리는 구간 아무일도 하지 않는다
- 멀티 쓰레드의 경우 사용자로부터 입력을 기다리는 구간 다른 쓰레드의 작업이 수행될 수 있다



## 쓰레드의 우선순위

쓰레드는 우선순위 속성을 가지고 있다. 작업의 중요도에 따라 우선순위를 정하면 실행시간이 달라지게 되며, 낮은 우선순위는 1 높은 우선순위는 10으로 1~10까지 설정이 가능하며 보통 우선순위는 5로 지정한다. 

* 우선순위의 경우 쓰레드를 생성한 쓰레드로부터 상속받는다. 

* main메서드를 수행하는 쓰레드는 우선순위가 5이며 main메서드 내에서 생성하는 쓰레드의 우선순위는 자동적으로 5로 된다.

  


작업의 중요도에 따라 스레드의 우선순위를 다르게 하여 특정 스레드가 더 많은 작업 시간을 갖게 할 수 있다.

```java
void setPriority(int newPrioroty)	//스레드의 우선순위를 지정한 값으로 변경한다.
int getPriority()					//스레드의 우선순위를 반환한다.

public static final int MAX_PRIORITY = 10;	//최대우선순위
public static final int MIN_PRIORITY = 1;	//최소우선순위
public static final int NORM_PRIORITY = 5;	//보통우선순위
```



## 스레드 그룹(ThreadGroup)

* 쓰레드 그룹은 서로 관련된 쓰레드를 그룹으로 다루기 위한 것

- 모든 스레드는 반드시 하나의 스레드 그룹에 포함되어 있어야 한다.

- 스레드 그룹을 지정하지 않고 생성한 스레드는 'main 스레드 그룹'에 속한다.

- 자신을 생성한 스레드(부모 스레드)의 그룹과 우선순위를 상속받는다.

> 쓰레드 그룹은 보안상의 이유로 도입된 개념이며, 자신이 속한 쓰레드 그룹이나 하위 쓰레드 그룹은 변경할 수 있지만 다른 쓰레드 그룹의 쓰레드를 변경할 수는 없다. 쓰레드를 쓰레드 그룹에 포함시키려면 Thread의 생성자를 이용해야 한다.

| 생성자 / 메서드                                              | 설명                                                         |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| ThreadGroup(String name)                                     | 지정된 이름의 새로운 스레드 그룹을 생성                      |
| ThreadGroup(ThreadGroup parent, String name)                 | 지정된 스레드 그룹에 포함되는 새로운 스레드 그룹을 생성      |
| int activeCount()                                            | 스레드 그룹에 포함된 활성상태에 있는 스레드의 수를 반환      |
| int activeGroupCount()                                       | 스레드 그룹에 포함된 활성상태에 있는 스레드 그룹의 수를 반환 |
| void checkAccess()                                           | 현재 실행중인 스레드가 스레드 그룹을 변경할 권한이 있는지 체크 |
| void destroy()                                               | 스레드 그룹과 하위 스레드 그룹까지 모두 삭제한다.            |
| int enumerate(Thread[] list) int enumerate(Thread[] list, boolean recurse) int enumerate(ThreadGroup[] list) int enumerate(ThreadGroup[] list, boolean recurse) | 스레드 그룹에 속한 스레드 또는 하위 스레드 그룹의 목록을 지정된 배열에 담고 그 개수를 반환, 두 번째 매개변수인 recurse의 값을 true로 하면 스레드 그룹에 속한 하위 스레드 그룹에 스레드 또는 스레드 그룹까지 배열에 담는다. |
| int getMaxPrioroty()                                         | 스레드 그룹의 최대우선순위를 반환                            |
| String getName()                                             | 스레드 그룹의 이름을 반환                                    |
| ThreadGroup getParent()                                      | 스레드 그룹의 상위 스레드그룹을 반환                         |
| void interrupt()                                             | 스레드 그룹에 속한 모든 스레드그룹을 반환                    |
| boolean isDaemon()                                           | 스레드 그룹이 데몬 스레드그룹인지 확인                       |
| boolean isDestroyed()                                        | 스레드 그룹이 삭제되었는지 확인                              |
| void list()                                                  | 스레드 그룹에 속한 스레드와 하위 스레드그룹에 대한 정보를 출력 |
| voboolean parentOf(ThreadGroup g)                            | 지정된 스레드 그룹의 상위 스레드 그룹인지 확인슬             |
| void setDaemon(boolean daemon)                               | 스레드 그룹을 데몬 스레드그룹으로 설정/해제                  |
| void setMaxPriority(int pri)                                 | 스레드 그룹의 최대우선순위를 설정                            |



쓰레드는 반드시 쓰레드 그룹에 포함되어야 하며, 위와같이 쓰레드 그룹에 지정하는 생성자를 사용하지 않은 쓰레드는 기본적으로 자신을 생성한 쓰레드와 같은 그룹에 속하게 된다. JVM은 main과 system이라는 쓰레드 그룹을 만든다. main메서드를 수행하는 main이라는 이름의 쓰레드는 main 쓰레드 그룹에 속하고 가비지컬렉션을 수행하는 Finalizer쓰레드는 system쓰레드 그룹에 속한다.

## 데몬 쓰레드

데몬 쓰레드는 다른 일반 쓰레드의 작업을 돕는 보조적인 역할을 수행한다. 우리가 사용하는 일반 쓰레드가 모두 종료되면 데몬 쓰레드는 강제적으로 종료가 된다. 데몬 쓰레드의 경우 무한루프와 조건문을 이용해서 실행 후 대기상태로 있다 특정 조건이 만족하게되면 작업을 수행하고 다시 대기하도록 작성을 한다. 

* 일반 스레드(non-daemon thread)의 작업을 돕는 보조적인 역할을 수행
* 일반 스레드가 모두 종료되면 자동적으로 종료된다.
* 가비지 컬렉터, 자동 저장, 화면 자동갱신 등에 사용된다.

* 일반 쓰레드 생성과 같으며 `setDaemon(true)`를 호출하기만 하면 된다.  -start() 전에 호출해야 한다.
* 데몬 쓰레드가 생성한 쓰레드는 자동적으로 데몬 쓰레드가 된다.



## 쓰레드의 실행제어

* 스레드의 실행을 제어(스케줄링)할 수 있는 메서드가 제공된다.

* resume(), stop(), suspend()는 스레드를 교착상태로 만들기 쉽기 때문에 deprecated 되었다.

| 메서드                                                       | 설명                                                         |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| static void sllep(long millis) static void sleep(long millis, int nanos) | 지정된 시간(천분의 일초 단위) 동안 스레드를 일시정지시킨다. 지정한 시간이 지나고 나면, 자동적으로 다시 실행대기상태가 된다. |
| void join() void join(long millis) void join(long millis, int nanos) | 지정된 시간동안 스레드가 실행되도록 한다. 지정된 시간이 지나거나 작업이 종료되면 join()을 호출한 스레드로 다시 돌아와 실행을 계속한다. |
| void interrupt()                                             | sleep()이나 join()에 의해 일시정지상태인 스레드를 깨워서 실행대기상태로 만든다. 해당 스레드에서는 interruptedException이 발생함으로써 일시정지 상태를 벗어나게 된다. |
| void stop()                                                  | 스레드를 즉시 종료시킨다.                                    |
| void suspend()                                               | 스레드를 일시정지시킨다. resume()을 호출하면 다시 실행대기상태가 된다. |
| static void yield()                                          | 실행 중에 자신에게 주어진 실행시간을 다른 스레드에게 양보(yield)하고 자신은 실행대기상태가 된다. |

### sleep(long millis)

* 일정 시간동안 쓰레드를 멈추게 한다.

```java
static void sleep(long millis)
static void sleep(long millis, int nanos)
```

- sleep()에 의해 일시정지 상태가 된 쓰레드는 지정된 시간이 지나거나, interrupt()가 호출되면, InterruptedException이 발생되어 잠에서 깨어나 실행대기 상태가 된다.
- 그래서 sleep()을 호출할 때는 항상 try-catch문으로 예외를 처리해줘야 한다.

```java
  void delay(long millis) {
    try {
      Thread.sleep(millis);
    } catch(InterruptedException e) {}
  }
```

- sleep()은 항상 실행중인 쓰레드에 대해 작동한다.
- 그래서 sleep()은 static으로 선언되어 있으며,
  `th1.sleep(2000)` 처럼 참조변수를 이용해서 호출하기 보다는 `Thread.sleep(2000)`과 같이 해야한다.

### interrupt()와 interrupted()

* 쓰레드의 작업을 취소한다.

```java
void interrupt()  // 쓰레드의 interrupted 상태를 false에서 true로 변경
boolean isInterrupted() // 쓰레드의 interrupted상태를 반환
static boolean interrupted()  // 현재 쓰레드의 interrupted상태를 반환 후, false로 변경
```

- interrupt()는 쓰레드에게 작업을 멈추라고 요청한다. (강제 종료는 아니다.)
- interrupted()는 쓰레드에 대해 interrupt()가 호출되었는지 알려준다. (않았다면 false, 호출 되었다면 true 반환)
- 쓰레드가 sleep(), wait(), join()에 의해 일시정지(waiting) 상태에 있을 때, 해당 쓰레드에 대해 interrupt()를 호출하면
  sleep(), wait(), join()에서 `Interrupted Exception`이 발생하고 쓰레드는 실행대기(Runnable) 상태로 바뀐다.
  즉, 멈춰있던 쓰레드를 깨워서 실행가능한 상태로 만드는 것이다.



### yield()

* 쓰레드 자신에게 주어진 실행시간을 다음 차례의 쓰레드에게 양보한다.

### join()

* 쓰레드 자신이 하던 작업을 잠시 멈추고 다른 쓰레드가 지정된 시간 동안 작업을 수행하도록 할 때 join()을 사용한다.

```java
void join()
void join(long millis)
void join(long millis, int nanos)
```

- 시간을 지정하지 않으면, 해당 쓰레드가 작업을 모두 마칠 때 까지 기다리게 된다.
- 작업 중에 다른 쓰레드의 작업이 먼저 수행되어야 할 필요가 있을 때 join()을 사용한다.
- join()이 호출되는 부분을 try-catch로 감싸야한다.
- sleep()과 다른 점은 join()은 현재 쓰레드가 아닌 특정 쓰레드에 대해 동작하므로 static메서드가 아니라는 것이다.

```java
try {
  th1.join(); // 현재 실행중인 쓰레드가 쓰레드 th1의 작업이 끝날때 까지 기다린다.
} catch(InterruptedException e) {}
```



### suspend(), resume(), stop()

* 모두 deprecated 된 메서드 들이다. 
* suspend()는 sleep()처럼 쓰레드를 멈추게 한다. suspend()에 의해 정지된 쓰레드는 resume()을 호출해야 다시 실행대기 상태가 된다.  
* stop()은 호출되는 즉시 쓰레드가 종료된다. 
* suspend()와 stop()은 교착상태(Deadlock)를 일으키기 쉬우므로 권장되지 않는다.



## **스레드의 상태(state of thread)**

| 상태                             | 설명                                                         |
| -------------------------------- | ------------------------------------------------------------ |
| `NEW`                            | 스레드가 생성되고 아직 start()가 호출되지 않은 상태          |
| `RUNNABLE`                       | 실행 중 또는 실행 가능한 상태                                |
| `BLOCKED`                        | 동기화블럭에 의해서 일시정지된 상태(lock이 풀릴 때까지 기다리는 상태) |
| `WAITING  `<br />`TIMED_WAITING` | 스레드의 작업이 종료되지는 않았지만 실행가능하지 않은(unrunnable) 일시정지 상태. TIMED_WAITING은 일시정지시간이 지정된 경우를 의미한다. |
| `TERMINATED`                     | 스레드의 작업이 종료된 상태                                  |



## 쓰레드의 동기화

> 한 쓰레드가 진행 중인 작업을 다른 쓰레드가 간섭하지 못하도록 막는 것.

### 임계영역(critical section)과 잠금(lock)

공유 데이터를 사용하는 코드 영역을 임계영역으로 지정해놓고,
공유 데이터(객체)가 가지고 있는 lock을 획득한 단 하나의 쓰레드만 이 영역 내의 코드를 수행할 수 있게 한다.
그리고 해당 쓰레드가 임계영역 내의 모든 코드를 수행하고 벗어나서 lock을 반납해야
다른 쓰레드가 반납된 lock을 획득하여 임계 영역의 코드를 수행할 수 있게 된다.

### Synchronized를 이용한 동기화

* 한 번에 하나의 스레드만 객체에 접근할 수 있도록 객체에 락(lock)을 걸어서 데이터의 일관성을 유지하는 것

* 임계영역을 설정하는데 사용

```java
1. 메서드 전체를 임계 영역으로 지정
  public synchronized void calcSum()  {
    ...
  }
  
2. 특정한 영역을 임계 영역으로 지정
  synchronized(객체의 참조변수)  {
    ...
  }
```

쓰레드는 synchronized메서드가 호출된 시점부터 해당 메서드가 포함된 객체의 lock을 얻어 작업을 수행하다가 메서드가 종료되면 lock 반환한다.
lock의 획득과 반납이 모두 자동으로 이루어지므로, 개발자는 그저 임계영역만 설정해주면 된다.

모든 객체는 lock을 하나씩 가지고 있고, 해당 객체의 lock을 가지고 있는 쓰레드만 임계영역의 코드를 수행할 수 있다. 때문에 다른 쓰레드들은 lock을 얻을 때 까지 기다리게 되므로,

가능하면 메서드 전체에 lock을 거는 것 보다, synchronized블럭으로 임계영역을 최소화하는 것이 좋다.

### wait()과 notify()

> 동기화된 임계영역의 코드를 수행하다가 작업을 더 이상 진행할 상황이 아니면,
> wait()을 호출하여 쓰레드가 lock을 반납하고 기다리게 한다.
> 그러면 다른 쓰레드가 lock을 얻어 해당 객체에 대한 작업을 수행할 수 있게 된다.
> 나중에 작업을 진행할 수 있는 상황이 되면 notify()를 호출해서,
> 작업을 중단했던 쓰레드가 다시 lock을 얻어 작업을 진행할 수 있게 한다.

```java
wait(), notify(), notifyAll()
  - 특정 객체에 대한 것이므로 Object클래스에 정의되어 있다.
  - 동기화 블록(synchronized)내에서만 사용할 수 있다.
  void wait() // notify() 또는 notifyAll()을 기다린다.
  void wait(long timeout)
  void wait(long timeout, int nanos)  // 지정된 시간동안만 기다린다. (시간 지나면 자동으로 notify()가 호출되는 것과 같다
  void notify()
  void notifyAll()
```

- notify() : waiting pool에서 대기 중인 스레드 중의 하나를 깨운다.
- notifyAll() : waiting pool에서 대기 중인 모든 스레드를 깨운다.
- wait() : 객체의 lock을 풀고 스레드를 해당 객체의 waiting pool에 넣는다.
- waiting pool은 객체마다 존재하므로 notifyAll()이 호출된다고 모든 객체의 waiting pool에 있는 쓰레드가 깨워지는 것은 아니다.
- 호출된 객체의 waiting pool에 대기 중인 쓰레드만 해당한다.

### Lock과 Condition을 이용한동기화

> 동기화할 수 있는 방법은 synchronized블럭 외에도 `java.util.concurrent.locks` 패키지가 제공하는 lock클래스들을 이용하는 방법이 있다.
> 같은 메서드 내에서만 lock을 걸 수 있는 synchronized블럭의 제약이 불편할 때 lock클래스를 사용한다.



### Lock - lock클래스의 종류 3가지



- ReentrantLock
  - 재진입이 가능한 lock. 가장 일반적인 배타 lock
  - 특정 조건에서 lock을 풀고 나중에 다시 lock을 얻고 임계영역으로 들어와서 이후의 작업을 수행할 수 있다.
- ReentrantReadWriteLock
  - 읽기에는 공유적, 쓰기에는 배타적인 lock
  - 읽기 lock이 걸린 상태에서 쓰기 lock을 거는 것은 허용되지 않는다. (vice versa)
- StampLock
  - ReentrantReadWriteLock에 낙관적 읽기 lock을 추가했다.
  - JDK1.8부터 추가되었으며, 다른 lock과 달리 Lock인터페이스를 구현하지 않았다.
  - lock을 걸거나 해지할 때 스탬프(long타입의 정수값)를 사용한다.
  - 무조건 읽기 lock을 걸지 않고, 쓰기와 읽기가 충돌할 때만 쓰기가 끝난 후에 읽기 lock을 거는 것이다.



### 일반적인 StampLock을 이용한 낙관적 읽기의 예

```java
int getBalance()  {
  long stamp = lock.tryOptimisticRead();  // 낙관적 읽기 lock을 건다.
  int curBalance = this.balance;  // 공유 데이터인 balance를 읽어 온다.
  
  if(lock.validate(stamp))  { // 쓰기 lock에 의해 낙관적 읽기 lock이 풀렸는지 확인
    stamp = lock.readLock();  // lock이 풀렸으면, 읽기 lock을 얻으려고 기다린다.
    
    try {
      curBalance = this.balance;  // 공유 데이터를 다시 읽어온다.
    }finally {
      lock.unlockRead(stamp); // 읽기 lock을 푼다.
    }
  }
  return curBalance;  // 낙관적 읽기 lock이 풀리지 않았으면 곧바로 읽어온 값을 반환
}
```

자동으로 lock의 잠금과 해제가 관리되는 synchronized블럭과 달리,
ReentrantLock과 같은 ` lock클래스들은 수동으로 lock을 잠그고 해제해야 한다.`

임계 영역 내에서 예외가 발생하거나 return문으로 빠져나가게 되면 lock이 풀리지 않을 수 있다.   
`그러므로 unlock()은 try-finally 문으로 감싸는 것이 일반 적이다.`

```java
lock.lock();  //ReentrantLock lock = enw ReentrantLock();
try {
  // 임계영역
} finally {
  lock.unlock();
}
```

### Condition

wait()과 notify()로 쓰레드의 종류를 구분하지 않고, 공유 객체의 waiting pool에 같이 몰아 넣는 대신, 각 쓰레드를 위한 Condition을 각각 만들어서 각각의 waiting pool에서 따로 기다리도록 한다.

Condition은 이미 생성된 lock으로부터 newCondition()을 호출해서 생성한다.

```java
private ReentrantLock lock = new ReentrantLock(); // lock을 생성
// lock으로 condition을 생성
private Condition forCook = lock.newCondition();
private Condition forCust = lock.newCondition();
```

wait()과 notify() 대신 Condition의 await()과 signal()을 사용하면 된다.

### volatile

멀티 코어 프로세서에서는 코어마다 별도의 캐시를 가지고 있다.
코어는 메모리에서 읽어온 값을 캐시에 저장하고 캐시에서 값을 읽어서 작업한다.
다시 같은 값을 읽어올 때는 먼저 캐시에 있는지 확인하고 없을 때만 메모리에서 읽어온다. 

때문에 도중에 메모리에 저장된 변수의 값이 변경되었는데도 캐시에 저장된 값이 갱신되지 않아서 메모리에 저장된 값이 다른 경우가 발생한다.

`변수 앞에 volatile을 붙이면, 코어가 변수의 값을 읽어올 때 캐시가 아닌 메모리에서 읽어오기 때문에 캐시와 메모리간의 값의 불일치가 해결된다.`

변수에 volatile을 붙이는 대신에 synchronized블럭을 사용해도 같은 효과를 얻을 수 있다.
쓰레드가 synchronized블럭으로 들어갈 때와 나올 때, 캐시와 메모리간의 동기화가 이루어져서 값의 불일치가 해소되기 때문이다.

> JVM은 데이터를 4 byte(=32bit) 단위로 처리하기 때문에, int와 int보다 작은 타입들은 한 번에 읽거나 쓰는 것이 가능하다.
>
>  즉, 단 하나의 명령어로 읽거나 쓰기가 가능하다.
> 이는 더 이상 나눌 수 없는 최소의 작업단위이므로, 작업의 중간에 다른 쓰레드가 끼어들 틈이 없다.
>
> 하지만 크기가 8byte인 long과 double 타입의 변수는 하나의 명령어로 값을 읽거나 쓸 수 없다. 때문에 변수의 값을 읽는 과정에서 다른 쓰레드가 끼어들 여지가 있다.

이를 방지하기 위해 변수를 읽고 쓰는 모든 문장을 synchronized블럭으로 감싸거나 변수를 선언할 때 volatile을 붙이는 것이다.

상수에는 volatile을 붙일 수 없다. 즉, 변수에 final과 volatile을 같이 붙일 수 없다.
상수는 변하지 않는 값이므로 멀티쓰레드에 안전(thread-safe)하다.

Synchronized블럭은 여러 문장을 원자화함으로써 쓰레드의 동기화를 구현하는 것이다.

`하지만 volatile은 변수의 읽거나 쓰기를 원자화 할 뿐, 동기화하는 것은 아니다.`

그러므로 동기화가 필요할 때, synchronized블럭 대신 volatile을 사용할 수 없다.



## fork & join 프레임워크

* RecursiveAction 클래스 :  반환값이 없는 작업을 구현할 때 사용
* RecursiveTask 클래스 : 반환값이 있는 작업을 구현할 때 사용

두 클래스 모두 compute()라는 추상 메서드를 가지고 있어서, 상속을 통해 이 추상메서드를 구현하기만 하면 된다.

> 하나의 작업을 작은 단위로 나눠서 여러 쓰레드가 동시에 처리하는 것을 쉽게 만들어 준다.



쓰레드를 시작할 때 run()이 아니라 start()로 호출하는 것처럼,
fork & join 프레임워크로 수행할 작업도 compute()가 아닌 invoke()로 시작한다.

```java
ForkJoinPool pool = new ForkJoinPool(); //쓰레드 풀 생성
SumTask task = new SumTask(from, to); // 수행할 작업 생성. 이 Task는 RecursiveTask를 상속받았다.

Long result = pool.invoke(task);  // invoke()를 호출해서 작업을 시작
```

ForkJoinPool은 fork & join프레임워크에서 제공하는 thread pool이다.

* 지정된 수의 쓰레드를 생성해서 미리 만들어 놓고 반복해서 재사용할 수 있게 한다.

* 쓰레드를 반복해서 생성하지 않아도 되는 장점과, 너무 많은 쓰레드가 생성되어 성능이 저하되는것을 막아주는 장점이 있다.



쓰레드 풀은 쓰레드가 수행해야하는 작업이 담긴 큐를 제공하며,
각 쓰레드는 자신의 작업 큐에 담긴 작업을 순서대로 처리한다.

>  쓰레드 풀은 기본적으로 코어의 개수가 동일한 개수의 쓰레드를 생성한다.



## fork & join

fork()는 작업을 쓰레드의 작업 큐에 넣는 것이고, 작업 큐에 들어간 작업은 더 이상 나눌 수 없을 때까지 나뉜다.
즉, compute()로 나누고 fork()로 작업 큐에 넣는 작업이 계속 반복된다.
나눠진 작업은 각 쓰레드가 골고루 나눠서 처리하고 작업의 결과는 join()을 호출해서 얻을 수 있다.

### fork()와 join()의 차이

> fork() - 해당 작업을 쓰레드 풀의 작업 큐에 넣는다. 비동기 메서드
> join() - 해당 작업의 수행이 끝날 때까지 기다렸다가, 수행이 끝나면 그 결과를 반환한다. 동기 메서드

비동기 메서드는 일반 메서드와 달리 메서드를 호출만 할 뿐 그 결과를 기다리지 않는다.

* 내부적으로는 다른 쓰레드에게 작업을 수행하도록 지시만 하고 결과를 기다리지 않고 다음 문장을 수행한다.



```java
public Long compute() {
  ...
  SumTask leftSum = new SumTask(from, half);
  SumTask rightSum = new SumTask(half+1, to);
  leftSum.fork(); // 비동기 메서드. 호출 후 결과를 기다리지 않는다.
  
  return rightSum.compute()+leftSum.join(); // 동기메서드. 호출 결과를 기다린다.
}
```

> 항상 멀티쓰레드로 처리하는것이 빠르진 않다. 반드시 테스트 해보고 이득점이 있을 떄만 멀티쓰레드로 처리해야 한다.