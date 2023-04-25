# CompletableFuture

자바에서 비동기(Asynchronous) 프로그래밍을 지원하는 클래스입니다.

Future의 단점을 극복하기 위해 자바8부터 지원되었습니다.

*  Future의 진화된 형태로써 외부에서 작업을 완료시킬 수 있을 뿐만 아니라 콜백 등록 및 Future 조합 등이 가능

> CompletableFuture : 완료가능한 Future



**Future 인터페이스의 단점**

- 외부에서 완료시킬 수 없고, get의 타임아웃 설정으로만 완료 가능
- 블로킹 코드(get)를 통해서만 이후의 결과를 처리할 수 있음
- 여러 Future를 조합할 수 없음 ex) 회원 정보를 가져오고, 알림을 발송하는 등
- 여러 작업을 조합하거나 **예외 처리할 수 없음**

Future는 외부에서 작업을 완료시킬 수 없고, 작업 완료는 오직 get 호출 시에 타임아웃으로만 가능합니다. 

또한 비동기 작업의 응답에 추가 작업을 하려면 get을 호출해야 하는데, get은 블로킹 호출이므로 좋지 않습니다. 

또한 여러 Future들을 조합할 수도 없으며, 예외가 발생한 경우에 이를 위한 예외처리도 불가능하다. 

그래서 Java8에서는 이러한 문제를 모두 해결한 CompletableFuture가 등장하게 되었습니다.



**CompletableFuture의 장점**

- Future를 외부에서 완료 시키거나 취소할 수 있다.
  - 외부에서 complete 시킬 수 있음. 예) 몇 초 이내에 응답이 안 오면 기본값으로 리턴해라.
- 작업이 끝났을 때 다른 작업을 실행하도록 처리할 수 있다. 
- 여러 Future를 조합할 수 있다. (ex. 유저의 게시글을 가져오고 해당 게시물의 댓글 목록 가져오기)
- 예외 처리용 API를 제공한다.



```java
public class CompletableFuture<T> implements Future<T>, CompletionStage<T> {
  ... 
}
```

CompletableFuture는 `new CompletableFuture<Type>`처럼 생성할 수 있습니다. Type은 작업이 완료되어 저장되는 데이터의 타입을 의미합니다.

또한 다음과 같이 CompletableFuture를 생성하여 일반적인 [Future](https://codechacha.com/ko/java-future/)처럼 사용할 수 있습니다.

```java
CompletableFuture<String> future
        = new CompletableFuture<>();
Executors.newCachedThreadPool().submit(() -> {
    Thread.sleep(2000);
    future.complete("Finished");
    return null;
});

future.get();
```

### CompletableFuture의 비동기 작업

- `CompletableFuture<Void> runAsync(Runnable runnable, Executor executor)`
  - Runnable 구현체를 이용해서 비동기 연산 작업을 하기 위한 새로운 CompletableFuture 객체를 리턴한다.
  - void 타입. 반환값이 없는 경우에 사용
  - 비동기로 작업 실행 콜
- `<U> CompletableFuture<U> supplyAsync(Supplier<U> supplier, Executor executor)`
  - Supplier 인터페이스는 자바에서 기본 제공하는 함수형 인터페이스이며, 입력 파라미터는 없고 리턴 값만 있다. 
  - 반환값이 있는 경우에 사용
  - 비동기로 작업 실행 콜



runAsync와 supplyAsync는 기본적으로 자바7에 추가된 ForkJoinPool의 commonPool()을 사용해 작업을 실행할 쓰레드를 쓰레드 풀로부터 얻어 실행시킵니다. 

* 내부적으로 기본은 ForkJoinPool.commonPool()을 사용합니다 

만약 원하는 쓰레드 풀을 사용하려면, ExecutorService(Executor 인터페이스를 구현하고있음) 를 파라미터로 넘겨주면 됩니다.



### CompletableFuture의 작업 콜백

* **thenApply(Function)** : 리턴값을 받아서 다른 값으로 바꾸는 콜백
  * 반환 값을 받아서 다른 값을 반환함
  * 함수형 인터페이스 Function을 파라미터로 받음
* **thenAccept(Consumer)** : 리턴값을 받지 않고, 다른 작업을 처리하는 콜백
  * 반환 값을 받아 처리하고 값을 반환하지 않음
  * 함수형 인터페이스 Consumer를 파라미터로 받음
* **thenRun(Runnable)** : 리턴값 받지 않고 다른 작업을 처리하는 콜백. 
  * Runnable이 온다고 생각하면 됨. 결과 값을 참고하지 않고 바로 실행.
  * 반환 값을 받지 않고 다른 작업을 실행함
  * 함수형 인터페이스 Runnable을 파라미터로 받음



예제 코드 [참조](#https://mangkyu.tistory.com/263)

thenApply는 값을 받아서 다른 값을 반환시켜주는 콜백

```java
@Test
void thenApply() throws ExecutionException, InterruptedException {
    CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
        return "Thread: " + Thread.currentThread().getName();
    }).thenApply(s -> {
        return s.toUpperCase();
    });

    System.out.println(future.get());
}
```

thenAccept는 반환 값을 받아서 사용하고, 값을 반환하지는 않는 콜백

```java
@Test
void thenAccept() throws ExecutionException, InterruptedException {
    CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
        return "Thread: " + Thread.currentThread().getName();
    }).thenAccept(s -> {
        System.out.println(s.toUpperCase());
    });

    future.get();
} 
```

thenRun은 반환 값을 받지 않고, 그냥 다른 작업을 실행하는 콜백

```java
@Test
void thenRun() throws ExecutionException, InterruptedException {
    CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
        return "Thread: " + Thread.currentThread().getName();
    }).thenRun(() -> {
        System.out.println("Thread: " + Thread.currentThread().getName());
    });

    future.get();
}
```

### CompletableFuture의 작업 조합

- **thenCompose(Function)** : 두 작업이 서로 이어서 실행하도록 조합
  - 두 작업이 이어서 실행하도록 조합하며, 앞선 작업의 결과를 받아서 사용할 수 있음
  - 함수형 인터페이스 Function을 파라미터로 받음
- **thenCombine(Function)** : 두 작업을 독립적으로 실행하고 둘 다 종료 했을 때 콜백 실행
  - 두 작업을 독립적으로 실행하고, 둘 다 완료되었을 때 콜백을 실행함
  - 함수형 인터페이스 Function을 파라미터로 받음
- **allOf(CompletableFuture<?>... cfs)** : 여러 작업을 모두 실행하고 모든 작업 결과에 콜백 실행
  - 여러 작업들을 동시에 실행하고, 모든 작업 결과에 콜백을 실행함
  - 여러 작업을 인자로 받는다.
- **anyOf(CompletableFuture<?>... cfs)** : 여러 작업 중에 가장 빨리 끝난 하나의 결과에 콜백 실행
  - 여러 작업들 중에서 가장 빨리 끝난 하나의 결과에 콜백을 실행함
  - 여러 작업을 인자로 받는다.



thenCompose와 thenCombine 예제의 실행 결과는 같지만 동작 과정은 다릅니다. 

먼저 thenCompose를 살펴보면 hello Future가 먼저 실행된 후에 반환된 값을 매개변수로 다음 Future를 실행합니다.

``` java
@Test
void thenCompose() throws ExecutionException, InterruptedException {
    CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> {
        return "Hello";
    });

    // Future 간에 연관 관계가 있는 경우
    CompletableFuture<String> future = hello.thenCompose(this::getWorld);
    System.out.println(future.get()); // Hello World 출력
}

private CompletableFuture<String> getWorld(String message) {
    return CompletableFuture.supplyAsync(() -> {
        return message + " World";
    });
}
```

하지만 thenCombine은 각각의 작업들이 독립적으로 실행되고, 얻어진 두 결과를 조합해서 작업을 처리합니다

* thenCombine은 두 개를 각각 실행해 얻어지는 결과로 리턴 값을 만든다. 

```java
@Test
void thenCombine() throws ExecutionException, InterruptedException {
    CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> {
        return "Hello";
    });

    CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> {
        return "World";
    });

    CompletableFuture<String> future = hello.thenCombine(world, (h, w) -> h + " " + w);
    System.out.println(future.get()); // Hello World
}
```



### CompletableFuture의 예외 처리 (Exception Handling)

CompletableFuture는 예외 처리를 위한 API를 제공합니다

다른 스레드(ThreadB)에 task를 위임했을 때, 예외가 발생하면

이 경우 예외는 호출자 스레드까지 전파되는데, 이는 get 메서드 명세를 보면 알 수 있습니다.

1. java.util.concurrent.CancellationException – if the computation was cancelled
2. java.util.concurrent.ExecutionException – if the computation threw an exception
3. InterruptedException – if the current thread was interrupted while waiting

 RuntimeException은 2번에 해당되기 때문에 ExecutionException 를 던집니다.



- `exeptionally(Function)`
  - 발생한 에러를 받아서 예외를 처리함
  - 함수형 인터페이스 Function을 파라미터로 받음
  - exceptionallyAsync(), exceptionallyCompose() 등도 지원한다.
- `handle(BiFunction)`, `handleAsync(BiFunction)`
  - (결과값, 에러)를 반환받아 에러가 발생한 경우와 아닌 경우 모두를 처리할 수 있음
  - 예외 발생 유무나, 발생한 예외 타입에 따른 처리를 구현할 수도 있다
  - 함수형 인터페이스 BiFunction을 파라미터로 받음



외에도 아직 완료되지 않았으면 get을 바로 호출하고, 

실패 시에 주어진 exception을 던지게 하는 completeExceptionally와 

강제로 예외를 발생시키는 obtrudeException과 

예외적으로 완료되었는지를 반환하는 isCompletedExceptionally 등과 같은 기능들도 있습니다. 



**completeExceptionally()** 메소드를 이용한다면 발생한 에러를 Future에 포함시킬수도 있습니다.

```java
public Future<Double> getPriceAsync(String product) {
        CompletableFuture<Double> futurePrice = new CompletableFuture<>();
        new Thread(() -> {  //다른 스레드에서 비동기적으로 계산 수행
            try {
                double price = calculatePrice(product);
                futurePrice.complete(price);    //future에 값 설정
            } catch (Exception e) {
                futurePrice.completeExceptionally(e);   //exception이 발생하면 발생한 에러를 포함시켜 future 종료
            }
        }).start();
        return futurePrice; //계산 결과 기다리지 않고 바로 future 반환
}

```



### 참조

* https://jaehoney.tistory.com/149
* https://devfunny.tistory.com/809

* https://mangkyu.tistory.com/263