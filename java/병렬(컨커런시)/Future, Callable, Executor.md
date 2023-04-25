# Java Future Callable

자바에서 지원하는 Concurrent 프로그래밍 관련 클래스들입니다.

- **멀티 프로세싱 --> `ProcessBuilder` 사용**
- **멀티 스레드 --> `Thread`, `Runnable`, `Executors`, `Callable` 등 사용**



Thread와 Runnable은 멀티 스레드를 구현하는것을 지원하지만 

Thread와 Runnable을 직접 사용하는 방식은 다음과 같은 한계점이 있습니다. 

- 지나치게 저수준의 API(쓰레드의 생성)에 의존함
- 값의 반환이 불가능 (void type)
- 매번 쓰레드 생성과 종료하는 오버헤드가 발생
- 쓰레드들의 관리가 어려움



특히 결과를 반환하도록 추가된것이 Callble과 Future 입니다.

- `Callable`과 `Runnable`의 차이점은 작업의 결과를 받을 수 있다는 사실이다.
- `Future`는 비동기적인 작업의 현재 상태를 조회하거나 반환 값을 가져올 수 있다.

## Callable, Future 인터페이스

**Callable**

- Runnable과 유사하지만 return을 통해 작업의 결과를 받아볼 수 있다.

* 인자로 Runnable 대신 Callable Type을 타입을 주면 제네릭을 사용해  Return 값을 Future로 받는다.

```java
@FunctionalInterface
public interface Callable<V> {
    V call() throws Exception;
}
```

**Future** (미래)

- 비동기적인 작업의 현재 상태를 조회하거나 결과를 가져오기 위한 객체
- <u>Future는 결과를 얻으려면 블로킹 방식으로 대기를 해야 한다는 단점이 있다</u> -> 블로킹 콜 (Blocking call)
- Runnable / Callable 상태를 조회하거나 결과를 확인하기 위해 사용
- `시간이 걸릴 수 있는 작업`을 `Future 내부`에 작성하고,
  호출자 스레드가 결과를 기다리는 동안  다른 유용한 작업을 할 수 있음
  --> 실행을 맞기고 `미래 시점에 결과를 얻는 것`으로 이해 가능
- 처리 결과에 대한 콜백을 정의할 수 없어서 이후에 CompletableFuture이  등장함

```java
public interface Future<V> {

    boolean cancel(boolean mayInterruptIfRunning);

    boolean isCancelled();

    boolean isDone();

    V get() throws InterruptedException, ExecutionException;

    V get(long timeout, TimeUnit unit)
        throws InterruptedException, ExecutionException, TimeoutException;
}
```

**Future의 메소드**

* get
  - 블로킹 방식으로 결과를 가져옴
  - 타임아웃(대기할 시간) 설정 가능

- isDone, isCancelled
  - isDone은 작업의 완료 여부, isCancelled는 작업의 취소 여부를 반환
  - 완료 여부를 boolean으로 반환

- cancel
  - true값을 주면 작업을 취소시키며, 취소 여부를 boolean으로 반환
    - 취소할 수 없는 경우에는 false, 취소한 경우는 true
  - cancle 후에 isDone()는 항상 true를 반환
  - parameter로 true를 전달하면 현재 진행중인 쓰레드를 interrupt하고 그러지 않으면 현재 진행중인 작업이 끝날때까지 기다린다.
  - cancel 했을 경우 interrupt 안하고 cancel하더라도 값을 가져올 수 없다. 작업이 완료된 채 종료된게 아니기 때문이다.
  - CancellationException 예외 발생

```java
public class Main {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Callable<String> hello = () -> "Hello!";
        Future<String> future = executorService.submit(hello);
        
        try{
            System.out.println(future.get()); // Hello!
        } catch (Exception e) {
            throw e;
        }
        executorService.shutdown();
    }

}
```

## Executors, Excutor ExecutorService



## Executors 클래스

쓰레드를 다루기 쉽게 제공해주는 팩토리 클래스입니다.

애플리케이션이 사용할 Thread poll을 만들어 관리해주며 Thread의 생명주기를 관리합니다

- 개발자는 Thread를 만들고 관리하는 것을  Executors에게 위임합니다.
- Runnable만 개발자가 만들고 생성, 종료, 없애기 작업(일련의 작업)들은 Executors가 해줍니다.
- ExecutorService를 만들고 반환하는 메서드를 지원합니다
- 인터페이스는 크게 Executor와 ExecutorService가 있으나 실질적으로 ExecutorService를 사용합니다.
- 개발자는 작업만 소스코드로 작성하면 됩니다.



다음과 같은 메소드들을 지원합니다

- newFixedThreadPool()
  - 고정된 쓰레드 개수를 갖는 쓰레드 풀을 생성함
  - ExecutorService 인터페이스를 구현한 ThreadPoolExecutor 객체가 생성됨
- newCachedThredPool()
  - 필요할 때 필요한 만큼의 쓰레드를 풀 생성함
  - 가능한 한 이미 생성된 쓰레드를 재사용 하려고 한다.
- newScheculedThreadPool()
  - 일정 시간 뒤 혹은 주기적으로 실행되어야 하는 작업을 위한 쓰레드 풀을 생성함
  - ScheduledExecutorService 인터페이스를 구현한 ScheduledThreadPoolExecutor 객체가 생성됨
- newSingleThreadExecutor(), newSingleThreadScheduledExecutor()
  - 1개의 쓰레드만을 갖는 쓰레드 풀을 생성
  - 각각 newFixedThreadPool와 newScheculedThreadPool에 1개의 쓰레드만을 생성하도록 한 것
- newWorkStealingPool()
  *  메서드의 입력 파라미터로 반드시 ExecutorService 객체를 전달해야한다. 그리고 해당 객체를 표준 ExecutorService 객체로 위임해서 결과를 리턴한다.
  *  ExecutorService를 구현한 여러 클래스의 기능 중 ExecutorService의 메서드만 호출하고 나머지 기능을 사용하지 못하도록 제한할 필요가 있을때 사용한다.



Executors를 통해 쓰레드의 개수 및 종류를 정할 수 있으며, 이를 통해 쓰레드 생성과 실행 및 관리가 매우 용이 해집니다. 



하지만 쓰레드 풀 생성 시에는 주의해야 할 점이 있습니다. 

* 만약 newFixedThreadPool을 사용해 2개의 쓰레드를 갖는 쓰레드 풀을 생성한다면, 3개의 작업을 동시에 실행시킨다면 1개의 작업은 실행되지 못한다. 
* 그러다가 쓰레드가 작업을 끝내고 반환되어 가용가능한 쓰레드가 생기면 남은 작업이 실행된다.



## Executor 인터페이스

```java
public interface Executor {
    void execute(Runnable command);
}
```

Executor 인터페이스는 쓰레드 풀의 구현을 위한 인터페이스입니다. 

* 제공된 작업(Runnable 구현체)을 실행 하는 객체가 구현해야할 인터페이스입니다. 
* Executor 인터페이스를 구현한 클래스는 스레드 생성, 스레드 풀 관리 등 작업 실행에 대한 세부 사항을 추상화합니다.
* 작업 등록과 작업 실행중에 작업 실행만을 책임진다 
  * 작업은 Runnable 인터페이스를 구현하는 객체



쓰레드는 작업을 등록하고, 실행하는 과정을 거칩니다.

그중 <u>등록된 작업을 실행만 하는 책임</u> 을 갖는것이 Executor 인터페이스이며, 실행하는 execute 메소드만 가지고 있습니다. 

```java
@Test
void executorRun() {
    final Runnable runnable = () -> System.out.println("Thread: " + Thread.currentThread().getName());

    Executor executor = new StartExecutor();
    executor.execute(runnable);
}

static class StartExecutor implements Executor {

    @Override
    public void execute(final Runnable command) {
        new Thread(command).start(); // 새 스레드를 생성하고 실행해야만 다른 스레드에서 실행합니다. 
    }
}
```

* 새 스레드를 생성하고 실행해야만 다른 스레드에서 실행합니다.  그렇지 않으면 메인 스레드에서 실행하게 됩니다. 

**Java Executor 의 구조**

<img src="https://blog.kakaocdn.net/dn/bsGwr3/btscfzip9V9/s3r3z5txDRUSeTYMOfW900/img.png" width = 700 height=400>

## ExecutorService 인터페이스

Executor 인터페이스를 상속하고 스레드 작업(Runnable, Callable)등록과 실행을 위한 인터페이스입니다.

ExecutorService는 등록 뿐만 아닌 실행의 책임도 갖고 있습니다.

Executor(작업 실행 인터페이스)를 종료시키거나, 여러 Callable을 동시에 실행하는 등의 기능을 제공한다.



주로 Executors 클래스의 static method들을 이용하여 생성합니다. 

```java
public interface ExecutorService extends Executor {

    void shutdown();

    List<Runnable> shutdownNow();

    boolean isShutdown();

    boolean isTerminated();

    boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException;

    <T> Future<T> submit(Callable<T> task);

    <T> Future<T> submit(Runnable task, T result);

    Future<?> submit(Runnable task);
    
    <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks)
        throws InterruptedException;
    
    ...
      
    <T> T invokeAny(Collection<? extends Callable<T>> tasks,
                    long timeout, TimeUnit unit)
        throws InterruptedException, ExecutionException, TimeoutException;
    ...
}
```

* 쓰레드 풀은 기본적으로 ExecutorService 인터페이스를 구현합니다

쓰레드풀의 대표적인 구현체로 ThreadPoolExecutor 클래스가 있습니다

* 내부적으로 블로킹 큐(BlockingQueue<Runnable> workQueue) 를 사용하며 작업들을 큐에 등록합니다.



executorService가 제공하는 퍼블릭 메소드들은 다음과 같이 분류가 가능합니다.

- 스레드(Executor 인터페이스)의 라이프사이클 관리를 위한 기능들
- 비동기 작업(작업 수행)을 위한 기능들

### 라이프 사이클 관리를 위한 기능

- `void shutdown()`
  - 호출 전에 제출된 작업들은 그대로 실행이 끝나고 종료됨(Graceful Shutdown)
  - 이미 전달된 작업은 실행되지만, 새로운 작업들을 더 이상 받아들이지 않음
- `List<Runable> shutdownNow()`
  - 현재 실행되고 있는 작업을 모두 중지 시키고, 현재 대기 하고 있는 작업을 멈추게 합니다.
    - shutdown 기능에 더해 이미 제출된 작업들을 인터럽트시킴
  - 실행을 위해 대기중인 작업 목록(List<Runnable>)을 반환함

- `boolean isShutdown()`
  - Executor의 shutdown 여부를 반환함
- `boolean isTerminated()`
  - shutdown 실행 후 모든 작업의 종료 여부를 반환함
- `boolean awaitTermination(long timeout, TimeUnit unit); `
  - shutdown()을 실행한 뒤, 지정한 시간 동안 모든 작업이 종료될 때 까지 대기한다. 
  - 지정한 시간 이내에서 실행중인 모든 작업이 종료되면 true를 리턴하고, 여전히 실행중인 작업이 남아 있다면 false를 리턴한다.
  - 지정한 시간 내에 모든 작업이 종료되었는지 여부를 반환함



**주의사항**

ExecutorService를 만들어 작업을 실행하면, shutdown이 호출되기 전까지 계속해서 다음 작업을 대기하게 됩니다.

그러므로 작업이 완료되었다면 반드시 **shutdown을 명시적으로 호출**해주어야 합니다.

그렇지 않으면 프로세스가 끝나지 않고 계속 실행상태가 됩니다.

* 메모리를 확인해보면 Java가 실행되어있는것을 알 수 있습니다.

shutdown과 shutdownNow 시에 중요한 것은, 

만약 실행중인 작업들에서 인터럽트 여부에 따른 처리 코드가 없다면 계속 실행됩니다. 

그러므로 필요하다면 다음과 같이 인터럽트 시에 추가적인 조치를 구현해야 합니다.

```java
@Test
void shutdownNow() throws InterruptedException {
    Runnable runnable = () -> {
        System.out.println("Start");
        while (true) {
            if (Thread.currentThread().isInterrupted()) {
                System.out.println("Interrupted");
                break;
            }
        }
        System.out.println("End");
    };

    ExecutorService executorService = Executors.newFixedThreadPool(10);
    executorService.execute(runnable);

    executorService.shutdownNow();
    Thread.sleep(1000L);
}
```

### 비동기 작업(작업 수행)을 위한 기능들

ExecutorService는 Runnable과 Callbale을 작업으로 사용하기 위한 메소드를 제공합니다. 

동시에 여러 작업들을 실행시키는 메소드도 제공하고 있는데, 비동기 작업의 진행을 추적할 수 있도록 Future를 반환합니다. 

* 반환된 Future들은 모두 실행된 것이므로 반환된 isDone은 true 

* 하지만 작업들은 정상적으로 종료되었을 수도 있고, 예외에 의해 종료되었을 수도 있으므로 항상 성공한 것은 아니다. 



이러한 ExecutorService가 갖는 비동기 작업 실행을 위한 메소드들을 정리하면 다음과 같습니다.

- `<T> Future< T >submit(Callable< T > task)`

  * 결과값을 리턴하는 작업을 추가한다. (Future)

  - Future의 get을 호출하면 성공적으로 작업이 완료된 후 결과를 얻을 수 있음

- `Future<?> submit(Runnable task)`

  * 결과값이 없는 작업을 추가한다. ( Runnable )

  * Future의 get을 호출하면 성공적으로 작업이 완료된 후 결과를 얻을 수 있음

- `<T> Future< T > submit(Runnable task, T result) `

  * 새로운 작업을 추가한다. result는 작업이 성공적으로 수행될 때 사용될 리턴 값을 의미한다.

- `<T> List<Future< T > > invokeAll(Collections<? extends Callable< T > > tasks) `

  * 주어진 작업을 모두 실행한다. 각 실행 결과값을 구할 수 있는 Future의 list 를 리턴한다.

- `<T> List<Future< T > > invokeAll(Collections<? extends Callable< T > > tasks , long timeout, TimeUnit unit)`

  * 위의 invokeAll() 과 동일하다. 지정한 시간 동안 완료되지 못한 작업은 취소되는 차이점이 있다.

- `<T> invokeAny(Collection<? extends Callable< T > > tasks) `

  * 작업을 수행하고, 작업 결과 중 성공적으로 완료된 것의 결과를 리턴한다. 정상적으로 수행된 결과가 발생하거나 예외가 발생하는 경우 나머지 완료되지 않은 작업은 취소된다.

- `T invokeAny(Collections<? extends Callable< T > > tasks, long timeout, TimeUnit unit) `

  * invokeAny() 와 동일하다. 지정한 시간 동안만 대기한다는 차이점이 있다.



#### invokeAll()과 invokeAny()의 차이점

#### invokeAll

- 태스크를 실행하고 모두 완료되거나 시간 초과가 만료될 때, 상태 및 결과를 저장하고 있는 `Future` 목록을 반환한다.
  - `List<Future<T>>`
- `Future.isDone`은 반환된 목록의 각 요소에 대해서 적용됩니다.
- 반환시에 완료되지 않은 태스크는 취소됩니다.
- 이 작업이 진행되는 동안 지정한 컬렉션이 수정되면 메서드의 결과가 정의되지 않습니다.
- 동시에 실행한 작업중에 가장 오래걸리는 작업만큼 시간이 소요된다.

#### invokeAny

- 성공적으로 완료된 태스크의 결과를 반환합니다.
- 이 작업이 진행되는 동안 지정된 컬렉션이 수정되면 이 메서드의 결과가 정의되지 않는다.
- 동시에 실행한 작업중에 제일 짧게 걸리는 작업만큼 시간이 걸린다.
- 이 역시 또한 블록킹 콜이다.

<u>invokeAll</u>은 최대 쓰레드 풀의 크기만큼 작업을 `동시에` 실행시킵니다. 

그러므로 쓰레드가 충분하다면 동시에 실행되는 작업들 중에서 가장 오래 걸리는 작업만큼 시간이 소요된다. 

* 5초, 5초, 10초 짜리 3 개의 작업이 있다면 가장 오래걸리는 10초만큼 걸립니다.

* 하지만 만약 쓰레드가 부족하다면 대기되는 작업들이 발생하므로 가장 오래 걸리는 작업의 시간에 더해 추가 시간이 필요하다.

<u>invokeAny</u>는 가장 빨리 끝난 작업 결과만을 구하므로, 동시에 실행한 작업들 중에서 **가장 짧게 걸리는 작업만큼** 시간이 걸립니다. 

* 또한 가장 빠르게 처리된 작업 외의 나머지 작업들은 완료되지 않았으므로 cancel 처리되며, 작업이 진행되는 동안 작업들이 수정되면 결과가 정의되지 않습니다



## ScheduledExecutorService 인터페이스

ScheduledExecutorService는 ExecutorService의 하위 인터페이스로, ExecutorService 인터페이스에서 제공하는 기능 외에 추가적으로 Callable 혹은 Runnable 태스크를 특정 시간 이후에 실행하도록 구현할 수 있습니다.

* 특정 시간 이후에 또는 주기적으로 작업을 실행시키는 메소드가 추가
* 특정 시간대에 작업을 실행하거나 주기적으로 작업을 실행하고 싶을때 등에 사용할 수 있다

```java
public interface ScheduledExecutorService extends ExecutorService {

    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit);

    public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit);

    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit);
                                                  
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit);
}
```



이 인터페이스 역시 컨커런트 API에서 제공하는 Executors 클래스를 이용해서 생성하는 것이 일반적입니다.

* Executors 클래스의 메소드
  * newScheduledThreadPool()
    * 스레드가 특정 시간 이후, 혹은 일정 시간 간격으로 실행되도록 하는 스레드 풀을 생성한다.
    *  스레드 풀의 크기를 지정한다.
  * newSingleThreadScheduledExecutor()
    * 스레드가 특정 시간 이후, 혹은 일정 시간 간격으로 실행되도록 하는 스레드 풀을 생성한다. 
    * 하나의 스레드만 실행되며 나머지 스레드는 실행 시간이 지정되더라도 현재 실행중인 스레드가 종료될때까지 대기한다.
  * unconfigurableScheduledExecutorService()
    * 메서드의 입력 파라미터로 반드시 ScheduledExecutorService 객체를 전달해야한다. 그리고 해당 객체를 표준 ScheduledExecutorService 객체로 위임해서 결과를 리턴한다.
    *  ScheduledExecutorService 를 구현한 여러 클래스의 기능 중 ExecutorService의 메서드만을 호출하고 나머지 기능을 제한할 필요가 있을때 사용한다.

**ScheduledExecutorService 인터페이스의 메소드**

- `schedule(Runnable command, long delay, TimeUnit unit)`
  - 특정 시간(delay) 이후에 작업을 실행시킴
  - Runnable 클래스를 delay 시간만큼 후에 실행한다. 한번만 실행되며 반복 호출하지 않는다.

* `schedule(Callable<V> callable, long delay, TimeUnit unit)`
  *  Callable 클래스를 delay 시간만큼 후에 실행한다. 한번만 실행되며 반복 호출하지 않는다.

- `scheduleAtFixedRate()`
  - initialDelay 값만큼 대기했다가 실행한다. 
  - 특정 시간(delay) 이후 처음 작업을 실행시킴
  - 작업이 실행되고 특정 시간마다 작업을 실행시킴

- `scheduleWithFixedDelay`
  - initialDelay 값만큼 대기했다가 실행하고 **종료 여부와 상관 없이** 다시 세번째 파라미터인 period 값 주기로 반복해서 실행한다
  - 특정 시간(delay) 이후 처음 작업을 실행시킴
  - 작업이 완료되고 특정 시간이 지나면 작업을 실행시킴
  - scheduleWithFixedDelay와 다른 점은 스레드 종료 여부와는 상관 없이 period 값만큼 반복한다는 점이다.



### TimeUnit 클래스

컨커런트 API에서 제공하는 TimeUnit은 7개의 속성을 제공합니다

| DAYS         | 하루를 의미하며, 24시간과 동일하다. (설정할 수 있는 최댓값) |
| ------------ | ----------------------------------------------------------- |
| HOURS        | 한시간을 의미하며, 60분과 동일하다.                         |
| MINUTES      | 일분을 의미하며, 60초와 동일하다.                           |
| SECONDS      | 일초를 의미한다.                                            |
| MILLISECONDS | 1/1000 초를 의미한다. 1초는 1000밀리초이다.                 |
| MICROSECONDS | 1/1000 밀리초를 의미한다. 1밀리초는 1000마이크로초이다.     |
| NANOSECONDS  | 1/1000 마이크로초를 의미한다. 1마이크로초는 1000나노초이다. |



## ExecutorService의 장점

각기 다른 Thread를 생성해서 작업을 처리하고, 처리가 완료되면 해당 Thread를 제거하는 작업을 **손수** 진행해야하는 것을 ExecutorService 클래스를 이용하면 쉽게 처리가능합니다.

ExecutorService에 Task만 지정해주면 ThreadPool을 이용해서 Task를 실행하고 관리합니다

## Task는 내부적으로 어떻게 관리가 될까요

블로킹 Queue로 관리됩니다.

ThreadPool에 있는 Thread수보다 Task가 많으면, 미실행된 Task는 Queue에 저장되고, 실행을 마친 Thread로 할당되어 순차적으로 수행됩니다





### 참조

* https://devfunny.tistory.com/807

* https://jaehoney.tistory.com/149

* https://mangkyu.tistory.com/259