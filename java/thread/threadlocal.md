# ThreadLocal



* https://docs.oracle.com/javase/8/docs/api/java/lang/ThreadLocal.html



`ThreadLocal`은 JDK 1.2부터 제공된 오래된 클래스다. 이 클래스를 활용하면 스레드 단위로 로컬 변수를 사용할 수 있기 때문에(쓰레드 단위로 로컬 변수를 할당) 마치 전역변수처럼 여러 메서드에서 활용할 수 있다. 다만 잘못 사용하는 경우 큰 부작용(side-effect)이 발생할 수 있기 때문에 다른 스레드와 변수가 공유되지 않도록 주의해야 한다. 이 기능은 ThreadLocal 클래스를 통해서 제공한다.



TheadLocal을 사용 *하면* **특정 스레드** **에서만 액세스할** 수 있는 데이터를 저장할 수 있다 .

* 같은 Thread 범위를 갖는 변수들의 모임이며 동일 Thread 내에서는 언제든 ThreadLocal 변수에 접근할 수 있음



특정 스레드에서만 공유하여 어디서든 사용할 Integer 값 을 갖고 싶다면?

```java
ThreadLocal<Integer> threadLocalValue = new ThreadLocal<>();
```

같은 스레드 어디에서든 이 값을 사용하려면 *get()* 또는 *set()* 메서드만 호출하면 된다. 

결과적으로 *threadLocalValue 에서* *get()* 메서드를 호출하면 요청 스레드에 대한 *Integer* 값을 얻게 된다.



# ThreadLocal

`ThreadLocal` 클래스는 제네릭을 이용하여 저장할 값의 타입을 지정하며, public 메서드가 3개밖에 없다. 

* **ThreadLocal의 내부는 thread 정보를 key로 하여 값을 저장해두는 Map 구조**

```java
public class ThreadLocal<T> {
  ...
}
```



* get() - 스레드 로컬에서 값을 가져옴
* set(value) - 스레드 로컬에 값을 저장
* remove() - 스레드 로컬에 저장된 모든 값을 삭제



```java
// Integer를 저장할 TheadLocal 변수를 생성
ThreadLocal<Integer> threadLocalValue = new ThreadLocal<>();

// ThreadLocal 변수에 값 쓰기
threadLocalValue.set(1);

// ThradLocal 변수에서 값 읽기
Integer result = threadLocalValue.get();

// ThreadLocal 변수 값 제거
threadLocal.remove();
```



### set과 get 메서드

스레드 로컬에 값을 저장하는 `set`메서드, 값을 가져오는 `get` 메서드다.

```java
public void set(T value) {
    Thread t = Thread.currentThread();
    ThreadLocalMap map = getMap(t);
    if (map != null) {
        map.set(this, value);
    } else {
        createMap(t, value); 
    }
}

public T get() {
    Thread t = Thread.currentThread();
    ThreadLocalMap map = getMap(t);
    if (map != null) {
        ThreadLocalMap.Entry e = map.getEntry(this);
        if (e != null) {
            @SuppressWarnings("unchecked")
            T result = (T)e.value;
            return result;
        }
    }
    return setInitialValue();
}

ThreadLocalMap getMap(Thread t) {
    return t.threadLocals;
}

void createMap(Thread t, T firstValue) {
    t.threadLocals = new ThreadLocalMap(this, firstValue);
}
```

현재 스레드를 확인한 후에 `getMap` 메서드를 호출하여 특정 스레드의 `ThreadLocalMap`을 가져온다. 앞서 살펴본 것처럼 `Thread` 클래스에 `ThreadLocalMap` 타입의 필드가 있기 때문에 해당 스레드의 멤버가 직접적으로 반환된다.



### remove 메서드

스레드 로컬 변수 값을 삭제하는 메서드다. `remove` 메서드는 JDK 1.5에서 추가되었다. 스레드 풀(thread pool)을 사용하는 멀티 스레드 환경에서는 스레드 로컬 변수 사용이 끝났다면 `remove`를 명시적으로 호출해야 한다. 스레드가 재활용되면서 이전에 설정했던 스레드 로컬 정보가 남아있을 수 있기 때문이다.

```java
public void remove() {
     ThreadLocalMap m = getMap(Thread.currentThread());
     if (m != null)
         m.remove(this);
 }
```



**ThreadLocal의 기본 사용법**

1. ThreadLocal 객체를 생성한다.
2. ThreadLocal.set() 메서드를 이용해서 현재 쓰레드의 로컬 변수에 값을 저장한다.
3. ThreadLocal.get() 메서드를 이용해서 현재 쓰레드의 로컬 변수 값을 읽어온다.
4. ThreadLocal.remove() 메서드를 이용해서 현재 쓰레드의 로컬 변수 값을 삭제한다.



## ThreadLocal의 초기화



### new 연산을 통한 생성

```java
// Integer를 저장할 TheadLocal 변수를 생성
ThreadLocal<Integer> threadLocalValue = new ThreadLocal<>();

// ThreadLocal 변수에 값 할당
threadLocalValue.set(1);
```



### withInitial 메서드를 이용하면서 초기화

스레드 로컬 변수를 생성하면서 특정 값으로 초기화하는 메서드다. `withInitial` 메서드는 JDK 1.8에서 추가되었다.

```java
public static <S> ThreadLocal<S> withInitial(Supplier<? extends S> supplier) {
    return new SuppliedThreadLocal<>(supplier);
}
```



## 스레드 로컬(thread-local)과 연관된 클래스들의 구성



### ThreadLocalMap

`ThreadLocalMap`은 `ThreadLocal` 클래스의 정적 내부 클래스다. 모두 `private` 클래스로 구성되어 있어 외부에서 접근 가능한 메서드가 없으며, 내부적으로 해시 테이블 정보를 갖는데, 요소는 `WeakReference`를 확장하고 `ThreadLocal` 객체를 키로 사용하는 `Entry` 클래스다.

```java
public class ThreadLocal<T> {
    // ...생략
	static class ThreadLocalMap {
		// ...생략
		static class Entry extends WeakReference<ThreadLocal<?>> {
			// ...생략
		}
	}
}
```



### Thread

`Thread` 클래스는 `ThreadLocalMap` 타입 멤버 필드로 가지고 있는데, 이는 특정 스레드의 정보를 `ThreadLocal`에서 직접 호출할 수 있도록 한다.

```java
// Thread 클래스
public class Thread implements Runnable {
	/* ThreadLocal values pertaining to this thread. This map is maintained
	 * by the ThreadLocal class. */
	ThreadLocal.ThreadLocalMap threadLocals = null;
}
```



## ThreadLocal의 활용

ThreadLocal은 한 쓰레드에서 실행되는 코드가 동일한 객체를 사용할 수 있도록 해 주기 때문에 쓰레드와 관련된 코드에서 파라미터를 사용하지 않고 객체를 전파하기 위한 용도로 주로 사용되며, 주요 용도는 다음과 같다.



* 클라이언트 요청에 대해서 같은 스레드를 사용
  * Controller, Service, Repository, 도메인 모델 어디에서든 명시적인 파라미터 전달 필요없이 ThreadLocal 변수에 접근할 수 있음

- 사용자 인증정보 전파 - Spring Security에서는 ThreadLocal을 이용해서 사용자 인증 정보를 전파한다. - SecurityContext
- 트랜잭션 컨텍스트 전파 - 트랜잭션 매니저는 트랜잭션 컨텍스트를 전파하는 데 ThreadLocal을 사용한다.
- 쓰레드에 안전해야 하는 데이터 보관
-  Spring MVC의 인터셉터(interceptor) 등에서 아래와 같이 클라이언트의 요청 등에서 활용

이 외에도 쓰레드 기준으로 동작해야 하는 기능을 구현할 때 ThreadLocal을 유용하게 사용할 수 있다.



### SecurityContext ThreadLocal 예제

```java
/**
 * SecurityContextHolderStrategy 인터페이스 ThreadLocal 구현체
 */
final class ThreadLocalSecurityContextHolderStrategy implements SecurityContextHolderStrategy {

	private static final ThreadLocal<SecurityContext> contextHolder = new ThreadLocal<>();

	@Override
	public void clearContext() {
		contextHolder.remove();
	}

	@Override
	public SecurityContext getContext() {
		SecurityContext ctx = contextHolder.get();
		if (ctx == null) {
			ctx = createEmptyContext();
			contextHolder.set(ctx);
		}
		return ctx;
	}

	@Override
	public void setContext(SecurityContext context) {
		Assert.notNull(context, "Only non-null SecurityContext instances are permitted");
		contextHolder.set(context);
	}

	@Override
	public SecurityContext createEmptyContext() {
		return new SecurityContextImpl();
	}

}
```





# 스레드 풀(Thread Pool)을 사용할 때의 주의사항

스레드 로컬은 스레드 풀(thread pool)을 사용하는 환경에서는 주의해야 한다. 스레드가 재활용될 수 있기 때문에 사용이 끝났다면 스레드 로컬을 비워주는 과정이 필수적이다. 어떤 상황이 발생할 수 있는지 다음 예제로 알아보자.

스레드 클래스는 동일하지만 스레드 풀을 사용하여 스레드를 실행시키는 점이 다르다.

```java
package threadlocal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadLocalTest {
	static class MadThread extends Thread {
		private static final ThreadLocal<String> threadLocal = new ThreadLocal<>();
		private final String name;

		public MadThread(String name) {
			this.name = name;
		}

		@Override
		public void run() {
			System.out.printf("%s Started,  ThreadLocal: %s%n", name, threadLocal.get());
			threadLocal.set(name);
			System.out.printf("%s Finished, ThreadLocal: %s%n", name, threadLocal.get());
		}
	}

	// 스레드 풀 선언
	private final ExecutorService executorService = Executors.newFixedThreadPool(3);

	public void runTest() {
		for (int threadCount = 1; threadCount <= 5; threadCount++) {
			final String name = "thread-" + threadCount;
			final MadThread thread = new MadThread(name);
			executorService.execute(thread);
		}

		// 스레드 풀 종료
		executorService.shutdown();

		// 스레드 풀 종료 대기
		while (true) {
			try {
				if (executorService.awaitTermination(10, TimeUnit.SECONDS)) {
					break;
				}
			} catch (InterruptedException e) {
				System.err.println("Error: " + e);
				executorService.shutdownNow();
			}
		}
		System.out.println("All threads are finished");
	}

	public static void main(String[] args) {
		new ThreadLocalTest().runTest();
	}
}
```



실행 결과를 살펴보자. 역시나 출력 순서는 본인의 환경에 따라 실행할 때마다 다를 수 있지만 정상적인 상황이라면 스레드가 시작될 때 출력되는 스레드 로컬의 값은 “defaultName” 이어야 한다.

하지만 앞서 스레드 풀을 사용하지 않았을 때와 결과와 다른 점이 보인다. 4번과 5번 스레드가 시작될 때를 보면 이미 스레드 로컬에 값이 들어있음을 확인할 수 있다.

```bash
thread-1 Started,  ThreadLocal: defaultName
thread-3 Started,  ThreadLocal: defaultName
thread-3 Finished, ThreadLocal: thread-3
thread-2 Started,  ThreadLocal: defaultName
thread-2 Finished, ThreadLocal: thread-2
thread-4 Started,  ThreadLocal: thread-3
thread-4 Finished, ThreadLocal: thread-4
thread-1 Finished, ThreadLocal: thread-1
thread-5 Started,  ThreadLocal: thread-2
thread-5 Finished, ThreadLocal: thread-5
All threads are finished
```

이러한 결과가 발생하는 이유는 **스레드 풀을 통해서 스레드가 재사용되기 때문이다.** 이러한 문제를 방지하려면 사용이 끝난 스레드 로컬 정보는 제거될 수 있도록 `remove` 메서드를 마지막에 명시적으로 호출하면 된다.

만약 스레드 로컬 변수를 remove를 통해 초기화 하지 않으면, 다음번에 같은 스레드 사용시에 같은 변수가 남아있게 된다. 





- 동일 Thread내에서 실행되는 Controller, Service, Repository, 도메인 모델 어디에서든 명시적인 파라미터 전달 필요없이 ThreadLocal 변수에 접근할 수 있음
- ThreadPool과 함께 사용하는 경우 Thread가 ThreadPool에 반환되기 직전 ThreadLocal 변수 값을 반드시 제거해야함
- 그렇지 않을 경우 아래와 같은 상황이 발생하고, 미묘한 버그가 생겨날 수 있음
  - 요청을 처리하기 위해 ThreadPool에서 Thread를 하나 가져옴
  - 요청 처리에 필요한 변수를 ThreadLocal에 set함
  - 요청 처리가 완료되고 Thread는 ThreadPool에 반환됨
  - 다른 요청을 처리하기 위해 ThreadPool에서 Thread를 하나 가져왔는데 이전 요청 처리에 사용된 ThreadLocal 변수가 남아있고, 이를 참조하여 잘못된 동작을 수행할 수 있음



### 참조

* https://madplay.github.io/post/java-threadlocal
* https://docs.oracle.com/javase/8/docs/api/java/lang/ThreadLocal.html
* https://www.baeldung.com/java-threadlocal
