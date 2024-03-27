#  Java 가상 스레드(Virtual Thread)의 이해: 종류, 설정, 사용법, 그리고 Spring Boot와의 통합



# 1. Java 가상 스레드(Virtual Thread)  개요 

## 스레드의 종류

스레드 유형: KLT vs. ULT

스레드는 크게 커널 수준 스레드(Kernel-Level Threads, KLT)와 사용자 수준 스레드(User-Level Threads, ULT)로 분류될 수 있습니다.

- **커널 수준 스레드(KLT)**: 스레드의 생성, 스케줄링 및 관리를 직접 OS 커널이 담당하며, 이러한 스레드는 OS에 의존적입니다. KLT는 자원 관리 및 멀티프로세싱 환경에서의 스케줄링 측면에서 장점이 있으나, 스레드 생성 및 컨텍스트 스위칭에 높은 오버헤드가 있을 수 있습니다.
- **사용자 수준 스레드(ULT)**: 사용자 영역의 라이브러리나 애플리케이션에 의해 관리됩니다. ULT는 운영 체제의 커널로부터 독립적으로 스케줄링되며, 스레드 관리에 필요한 모든 작업을 사용자 영역에서 처리합니다. ULT의 장점은 스레드 생성 및 컨텍스트 스위칭이 빠르다는 점입니다. 그러나, 일부 리소스를 공유하는 작업에서는 커널의 도움이 필요할 수 있으며, 자바 같은 경우 1:1로 매핑이 되기떄문에 이점이 줄어들 수 있습니다.

자바에서 스레드 풀은 `ExecutorService` 인터페이스를 통해 제공됩니다.

JVM 힙 메모리에  여러 유저 레벨 스레드를 구현 및 생성하여  풀에 담아두고,  커널 레벨 스레드(KLT)를 JVM 이 유저 레벨 스레드(ULT)로 1:1로 매핑하여 사용합니다. Thread 객체는 JNI을 호출하여 커널 레벨 스레드에 1:1로 매핑하여 사용합니다. 

자바에서 스레드의 스케줄링은 JVM을 통해 운영 체제의 스레드 스케줄러에 위임(스케쥴 알고리즘!)되어 관리되며, 이 스케줄러가 스레드의 실행 타이밍과 프로세서 할당을 결정합니다.

실제 하드웨어의 CPU와 스레드는 무한정 할 수 없습니다. 결국 운영체제의 스케줄러는 아주 빠르게 각 스레드를 돌아가면서 실행하며 이로 인해 컨텍스트 스위칭이 발생합니다. 



### 왜 자바 기존 스레드가 문제가 될 수 있을까요?

1. 오버헤드  : 스레드의 생성과 종료 과정이 커널을 통해 이루어지기 때문에, 사용자 수준에서 발생하는 것보다 더 큰 오버헤드가 발생합니다. 때문에 애플리케이션 시작시 미리 만들어두고 사용하는것입니다. 부족하면 새로 생성하는것에 대해 매우 비싼 비용이 발생합니다.

2. 비싼 메모리 :  기존 유저 레벨 스레드는 매우 무겁습니다. (보통 1~2MB, 운영체제에 따라 다름 )

3. 블로킹 : 애플리케이션에서 I/O 작업(네트워크 요청, 파일 입출력 등)을 만나면 해당 스레드는 작업이 완료될 때까지 블로킹(대기) 상태가 됩니다. 이때, 스레드는 CPU 자원을 사용할 수 없으므로 OS에 CPU 자원을 반환하고, 실행할 수 없는 상태가 됩니다. 
   * 데이터 연산과 입출력은 컴퓨터구조상 담당하는 하드웨어가 다릅니다. 데이터연산은 CPU, 입출력은 I/O장치(NIC, 마우스, 키보드 등)을 담당합니다. 
     * CPU는 I/O 장치와 직접 상호작용하지 않습니다. 대신, 운영 체제는 I/O 요청을 관리하며, I/O 작업이 필요할 때 DMA(Direct Memory Access)와 같은 메커니즘을 사용하여 CPU의 개입 없이 데이터를 메모리와 I/O 장치 사이에서 직접 전송할 수 있도록 합니다. 
   * CPU가 수행하는 작업에 비해 I/O (네트워크, 파일 쓰기 및 읽기 요청)은 상대적으로 매우 느리기 때문에 I/O가 발생하면 그시간동안 스레드가 놀고있으면 아까우니, 제어권을 CPU에 반환해서 다른 스레드가 동작하게 되어 실행할 수 없게 됩니다. 
   * 그러다 I/O 작업이 끝나면 스케쥴러에의해 제어권을 받아 남은 작업을 이어가고, 작업이 끝나고 스레드를 반환합니다 .

이런 문제들로 인해, 자바 애플리케이션에서 처리량을 올리는것에 한계가 생기게 됩니다. 

그리고 이런 단점들을 해결 하기 위해 자바에서는 다른방안을 고민, 버츄얼 스레드를 도입하게 됩니다. 

경량화와 높은 확장성(수만 수백만개 동시 스레드 사용 가능)을 갖게하며 컨텍스트 스위칭과 메모리 사용량을 최소화하면서도 높은 수준의 동시성과 병렬성을 더 쉽게 관리할 수 있게 됩니다.

> 다른 대안으로, 스레드를 공유하고 비동기 - 논 블로킹 방식을 사용하여 처리량을 매우 높이는 반응형 리액티브 기술도 있으나 개발과 디버깅의 어려움이 있다는 단점이 있습니다. 

## 기존 스레드와 버츄얼 스레드의 차이점 -  버츄얼 스레드와 캐리어 스레드 

기존 자바 스레드는 플랫폼 스레드라고도 합니다. 

#### 플랫폼 스레드 (Platform Thread)

* 플랫폼 스레드는 OS가 관리하는 전통적인 자바 스레드 모델에서 사용되는 스레드로, Java 가상 머신(JVM)이 운영 체제의 기능을 활용하여 생성합니다. 
* 높은 연산량을 요구하는 계산 작업등에  작업에 주로 사용되며 상대적으로 많은 리소스를 소비하며, 스레드의 수는 시스템의 리소스에 의해 제한됩니다.

버츄얼 스레드가 나오게 되면서, 새로운 캐리어 스레드라는 개념이 나왔습니다. 

버츄얼 스레드와 캐리어 스레드에 대한 정의를 보겠습니다. 

#### 버추얼 스레드

* 경량 스레드로 JVM 위에서 생성 및 실행되며플랫폼 스레드보다 훨신 가볍고 더 적은 리소스를 사용합니다. 

* 캐리어 스레드 위에서 캐리어 스레드에 의해 관리 및 실행됩니다.

* 플랫폼 스레드의 크기는 1MB ~ 2MB 이고 스택사이즈가 고정되어있지만, 

  버츄얼 스레드는 상대적으로 훨씬 작으며, 고정된 스택 사이즈가 없습니다. 즉  사용량에 따라 크기가 커질수도 작을수도 있습니다. (Stack Chunk Object)

  * 크기에 대한 실험이 궁금하다면 다음  블로그를 참고하세요.(https://blog.ycrash.io/is-java-virtual-threads-lightweight/)

#### 캐리어 스레드 (Carrier Thread, 플랫폼 스레드라고도 할 수 있다.)

* 캐리어 스레드는 Project Loom의 일부로 도입된 개념으로, Virtual Thread를 실행하기 위한 운반체(Carrier) 역할을 합니다. 기존의 OS 수준의 스레드(플랫폼 스레드)를 기반으로 합니다. 버츄얼 스레드가 수행되는 동안 실제로 CPU의 실행 시간을 제공하는 스레드입니다.

* 여러 버츄얼 스레드를 효율적으로 관리하고 실행하기 위해 사용되며, 한 캐리어 스레드는 동시에 여러 버츄얼 스레드의 작업을 처리할 수 있습니다. 즉 여러 버츄얼 스레드를 캐리어 스레드 위에서 시분할 방식으로 실행시킵니다. 
* 이 캐리어 스레드의 모적은 다수의 경량 모델인 버추얼 스레드를 효율저으로 스케줄링하고 실행하는 목적입니다. 
* 캐리어스레드로 인해 애플리케이션은 OS 스레드의 제한(리소스 등)없이 사용할 수 있게 됩니다.

> 즉 하나의 캐리어 스레드가, 여러 버추얼 스레드를 돌아가면서 실행, 관리하는  1:N라고 볼 수 있고, 여러 캐리어 스레드가 존재해요.
>
> 이 캐리어 스레드의 갯수를 조절할 수 있는데, 이건 뒤에서 살펴볼게요 (웬만해서는 건드릴 필요 없는 설정이에요.)



#### 버추얼 스레드 내부의 캐리어 스레드

 모든 버츄얼 스레드는 코드에서도 플랫폼 스레드(캐리어 스레드)를 참조하고 있어요

```java
/**
 * A thread that is scheduled by the Java virtual machine rather than the operating
 * system.
 */
final class VirtualThread extends BaseVirtualThread {
  
  ...// 버추얼 스레드의 실행상태들  
        // carrier thread when mounted, accessed by VM
    private volatile Thread carrierThread;
  ...
}
```

버추얼 스레드의 실행 상태가 있는데, 상태에 따라 Virtual Thread의 상태에 따라 플랫폼 스레드에 마운트/언마운트해 실행을 관리합니다.

*  '마운트'된다는 것은 버추얼 스레드가 플랫폼 스레드에 할당되어 실행되기 시작했다는 것을 의미해요. 즉 버추얼 스레드의 실행 상태가 실제 CPU에서 처리될 수 있도록 플랫폼 스레드가 이를 "운반(캐리어)"하죠.
* '언마운트'란 반대로, 버추얼 스레드가 실행을 마치거나 대기 상태로 전환될 때 플랫폼 스레드에서 언마운트하게 해서, 현재 CPU 할당을 중지하고 다른 버추얼 스레드가 해당 플랫폼 스레드에 의해 실행될 수 있게끔 되는것을 의미해요 

캐리어 스레드가 버추얼 스레드의 실행과 스케줄링을 담당하고 있는데, 실제 코드로도 그렇게 되어있어요.

```java
private void mount() {
...
  carrier.setCurrentThread(this); // 플랫폼(캐리어) 스레드에 실행할 Virtual Thread 객체 this 할당 
... 
}

private void unmount() {

  Thread carrier = this.carrierThread;
  carrier.setCurrentThread(carrier);

  synchronized (interruptLock) {
    setCarrierThread(null); // 플랫폼(캐리어) 스레드에서 Virtual Thread 제거
   }
   carrier.clearInterrupt();
}
```

이 메소드들은 JVM 내부의 스레드 스케줄러에 의해 자동으로 호출됩니다. 

관련해서, 추가로 보면 좋을 내용은 아래 첨부할게요.

* [네이버 D2](https://d2.naver.com/helloworld/1203723)
* [우아한 형제들 기술블로그](https://techblog.woowahan.com/15398/)



# 버추얼 스레드의 사용

Virtual Thread를 사용하려면 인텔리제이와 gradle 프로젝트에서의 설정이 필요합니다.

먼저 인텔리제이에서 project structr의 sdk와 gradle jvm을 java 21로 맞춰 주셔야해요.  

이후 builg.gradle에서 다음 설정을 추가해주셔야 해요 

```groovy
tasks.withType(JavaCompile).configureEach {
    options.encoding = 'UTF-8'
    options.compilerArgs += '--enable-preview' // 프리뷰 해야 structured concurrency 사용 가능
}
```





다음으론, 버추얼 스레드를 사용할 수 있는 다양한 API가 나왔어요. 

* Thread.startVirtualThread(Runnable task);

* Thread.ofVirtual.start(Runnable task);
* ExecutorService.submit(() -> { })

```java
public class SimpleVirtualThreadExample {
  /**
  기본적인 버추얼 스레드 생성
  버추얼 스레드를 생성하고 시작하는 기본적인 방법입니다.
  */
  void createVirtualThreadWithLambda() {
    Thread.startVirtualThread(() -> { // public static Thread startVirtualThread(Runnable task) {}
    	System.out.println("Hello, Virtual Thread!");
		});
  }
  
  void createVirtualThreadWithRunnable() {        
    Runnable runnable = () -> log.info("Hello");

    Thread virtualThread = Thread.ofVirtual()     
      .name("my-virtual1", 1) 
      .unstarted(runnable);

    virtualThread.start();
  }
  
  /*
		Thread.Builder를 사용하여 가상 스레드를 생성하기
		- 가상 스레드는 기본적으로 데몬 스레드입니다.
		- 가상 스레드는 기본적으로 이름이 지정되어 있지 않지만, name으로 지정이 가능하며, name 다음인수로 넘버링 해요
	*/
    void virtualThreadDemo() throws InterruptedException {
        Thread virtualBuilder = Thread.ofVirtual().name("virtual-", 1);
        virtualBuilder.unstarted(() -> { 실행시킬내용 }); // Thread unstarted(Runnable task);
        virtualBuilder.start():
    }
  
  /**
	   ExecutorService를 사용하여 버추얼 스레드 생성하고 작업
	*/
	
  	void startVirtualThread() {
  	  	ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
					executor.submit(() -> {
    				System.out.println("Task running in virtual thread");
					});
				executor.shutdown();	
  	}
  
}
```

Fucture와 CompletableFuture와도 같이 사용할 수 있어요.

```java

String futureWithVirtual() {
  ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
	Future<String> future = executor.submit(() -> {
    Thread.sleep(100); // 비동기 작업 시뮬레이션
    return "Result from Future";
	});
  
  return future.get();
}

//

String completableFutureWithVirtual() {

  var cf = CompletableFuture
    .supplyAsync(() -> "Hello", Executors.newVirtualThreadPerTaskExecutor());

  try {
    return cf.get();
  } catch (InterruptedException | ExecutionException e) {
    throw new RuntimeException(e);  
  } 
}
// thenAccept, thenRun, exceptionally와도 사용 가능. 
String completableFutureWithVirtual() {
    var cf = CompletableFuture
        .supplyAsync(() -> "Hello", Executors.newVirtualThreadPerTaskExecutor())
        .thenApply((s) -> s + " World")
        .exceptionally(ex -> {
            log.info("error - {}", ex.getMessage());
            return null;
        });
    try {
        return cf.get();
    } catch (InterruptedException | ExecutionException e) {
        throw new RuntimeException(e);
    }
}
```

특정 스레드가 가상 스레드인지 확인하는 메서드도 제공해요. 

```java
boolean isVirtualThread = Thread.isVirtual();
```

## 캐리어 스레드 설정 - JDK  가상 스레드 스케줄러를 구성하기 위해 사용할 수 있는 시스템 속성

버추얼 스레드는 캐리어 스레드에 의해 관리 및 실행됩니다. 

이 캐리어 스레드는 기본적으로 우리가 알던 플랫폼 스레드와 동일합니다. 

이 캐리어 스레드의 수를 설정할 수 있습니다(일반적으로, 우리가 따로 관리해야 할 필요는 없어요. )

* 아래 Java `java.lang.Thread`의 공식 문서도 읽어보세요

* https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Thread.html

### 시스템 속성

| 시스템 속성                              | 설명                                                         |
| ---------------------------------------- | ------------------------------------------------------------ |
| `jdk.virtualThreadScheduler.parallelism` | 가상 스레드를 스케줄링하기 위해 사용할 수 있는 플랫폼 스레드의 수입니다. 기본값은 사용 가능한 프로세서의 수입니다. |
| `jdk.virtualThreadScheduler.maxPoolSize` | 스케줄러에 사용할 수 있는 플랫폼 스레드의 최대 수입니다. 기본값은 256입니다. |

이는 응용 프로그램을 시작할 때 최대 풀 크기를 변경할 수 있음을 의미해요.

```
java -Djdk.virtualThreadScheduler.maxPoolSize=512 <다른 인수들...>
```

동시에 실행되는 캐리어 스레드의 수를 제한하고 싶은 경우 다음 두 속성을 이용할 수 있어요. 

#### 캐리어 스레드의 수 제한 

첫 번째 속성을 사용하면 가상 스레드가 사용할 캐리어 스레드의 생성 수를 설정할 수 있습니다. 

기본적으로 캐리어 스레드의 수는 사용 가능한 cpu 코어의 수와 동일합니다. 

생성되는 캐리어 스레드의 수를 설정하려면 다음 속성을 사용할 수 있습니다.

```
 jdk.virtualThreadScheduler.parallelism=5
```

### 캐리어 스레드의 최대 수 

제한 병렬성 값에 의해 설정된 수를 초과하는 캐리어 스레드 수는 가상 스레드가 차단될 때 발생할 수 있습니다. 이러한 새로운 캐리어 스레드는 차단된 가상 스레드와 캐리어 스레드를 수용하기 위해 일시적으로 생성됩니다. 생성될 수 있는 캐리어 스레드의 최대 양을 설정하려면 다음 시스템 속성을 사용하십시오:

```
jdk.virtualThreadScheduler.maxPoolSize=10 
```

* 기본값은 256입니다. 

이 속성을 통해 버추얼 스레드를 실행할 캐리어 스레드의 최대 풀 크기를 설정할 수 있습니다.

`그러나 다시말하지만 일반적으로, 우리가 따로 관리해야 할 필요는 없어요. `

### 설정 방법

설정하는 방법은 주로 Java 애플리케이션을 시작할 때 JVM(Java Virtual Machine)에 전달하는 인수를 통해 이루어집니다. 

### 1. 커맨드 라인을 통한 설정

애플리케이션을 시작할 때, JVM에 전달하는 커맨드 라인 인수에 `-D키=값` 형식을 사용하여 속성을 설정할 수 있습니다. 

예를 들어, 캐리어 스레드의 수를 5로 제한하고, 최대 캐리어 스레드 수를 10으로 설정하려면 다음과 같이 할 수 있습니다:

```sh
java -Djdk.virtualThreadScheduler.parallelism=5 -Djdk.virtualThreadScheduler.maxPoolSize=10 -jar your-application.jar
```

이 명령은 `your-application.jar`라는 Java 애플리케이션을 시작하면서 캐리어 스레드의 병렬성을 5로, 최대 풀 크기를 10으로 설정합니다.

### 2. 프로그램 내에서 설정

시스템 속성은 Java 코드 내에서도 `System.setProperty()` 메서드를 사용하여 설정할 수 있습니다. 이 방법은 애플리케이션의 초기화 단계나 설정이 필요한 특정 시점에서 사용할 수 있습니다. 예를 들어:

```java
public class Main {
    public static void main(String[] args) {
        System.setProperty("jdk.virtualThreadScheduler.parallelism", "5");
        System.setProperty("jdk.virtualThreadScheduler.maxPoolSize", "10");

    }
}

//or

static {
    System.setProperty("jdk.virtualThreadScheduler.parallelism", "5");
    System.setProperty("jdk.virtualThreadScheduler.maxPoolSize", "10");
}
```





## 2. 가상 스레드 설정 방법

- ### 커맨드 라인을 통한 설정

- ### 프로그램 내에서의 설정

## 3. 가상 스레드 스케줄링과 스레드 양보

- ### 가상 스레드 스케줄링의 이해

- ### 스레드 양보 방법과 주의사항

- ### Pinning Thread(고정된 스레드): `synchronized` 사용 주의

## 4. 가상 스레드와 동기화 메커니즘

- ### `synchronized` 대안: 경쟁 방지법과 `ReentrantLock`

- ### 가상 스레드와 라이브러리 호환성: Pinning 예방 및 탐지 방법

## 5. 가상 스레드 활용

- ### 스레드 팩토리: 자식 가상 스레드 생성

- ### 유용한 가상 스레드 메소드

- ### 다양한 ThreadPool Executor와 가상 스레드 통합

## 6. 가상 스레드와 스레드 로컬

- ### Scope Values: 스코프 값의 바인딩 및 상속 문제

- ### 범위 지정된 값의 재바인딩

## 7. 구조화된 동시성(Structured Concurrency)

- ### 구조화된 동시성의 핵심 원칙

- ### 처리 실패 시나리오: 예외 처리와 태스크 관리 전략

## 8. Spring Boot와 가상 스레드의 통합

- ### Spring Boot에서 가상 스레드 사용하기

- ### 가상 스레드를 활용한 효율적인 비동기 처리 전략



# Java Virtual Thread



[toc]



* https://mangkyu.tistory.com/309
* https://mangkyu.tistory.com/317

* https://d2.naver.com/helloworld/1203723

```java
public static void main(String[] args) throws InterruptedException {
    platformThreadDemo1();
    // virtualThreadDemo(); // 가상 스레드 데모는 주석 처리되어 있음
}

/*
    단순한 자바 플랫폼 스레드를 생성하기
 */
private static void platformThreadDemo1(){
    for (int i = 0; i < MAX_PLATFORM; i++) {
        int j = i;
        Thread thread = new Thread(() -> Task.ioIntensive(j));
        thread.start();
    }
}

/*
    Thread.Builder를 사용하여 플랫폼 스레드를 생성하기
*/
private static void platformThreadDemo2(){
    var builder = Thread.ofPlatform().name("vins", 1);
    for (int i = 0; i < MAX_PLATFORM; i++) {
        int j = i;
        Thread thread = builder.unstarted(() -> Task.ioIntensive(j));
        thread.start();
    }
}

/*
    Thread.Builder를 사용하여 플랫폼 데몬 스레드를 생성하기
    - 데몬 스레드는 일반 스레드와 달리 백그라운드에서 실행되며
      메인 스레드가 종료되면 함께 종료됩니다.
*/
private static void platformThreadDemo3() throws InterruptedException {
    var latch = new CountDownLatch(MAX_PLATFORM);
    var builder = Thread.ofPlatform().daemon().name("daemon", 1);
    for (int i = 0; i < MAX_PLATFORM; i++) {
        int j = i;
        Thread thread = builder.unstarted(() -> {
            Task.ioIntensive(j);
            latch.countDown();
        });
        thread.start();
    }
    latch.await(); // 모든 데몬 스레드의 작업이 끝날 때까지 대기
}

/*
    Thread.Builder를 사용하여 가상 스레드를 생성하기
    - 가상 스레드는 기본적으로 데몬 스레드입니다.
    - 가상 스레드는 기본적으로 이름이 지정되어 있지 않습니다.
    - 가상 스레드는 경량 스레드로, 많은 수의 동시 작업을 효율적으로 처리할 수 있습니다.
*/
private static void virtualThreadDemo() throws InterruptedException {
    var latch = new CountDownLatch(MAX_VIRTUAL);
    var builder = Thread.ofVirtual().name("virtual-", 1);
    for (int i = 0; i < MAX_VIRTUAL; i++) {
        int j = i;
        Thread thread = builder.unstarted(() -> {
            Task.ioIntensive(j);
            latch.countDown();
        });
        thread.start();
    }
    latch.await(); // 모든 가상 스레드의 작업이 끝날 때까지 대기
}

```



버츄얼 스레드 생성방법.

Thread.Builder

* Thread.ofPlatform()

* Thread.ofVirtual()





버츄얼 스레드는 자바가 제공하는 스레드. 

힙에 생성하는 자그만한 객체(스레드)이다. 

포크조인풀에 의해서 실행된다. 

캐리어 쓰레드가 직접 버츄얼 스레드를 실행한다.





플랫폼 스레드의 크기는 윈도우 -> 1MB 리눅스 맥 2MB 이다.

또한 스택 사이즈가 고정되어 있다. 

버츄얼 스레드는 고정된 스택 사이즈가 없다. 즉 크기가 커질수도 작을수도 있다.

* Stack Chunk Object라고 한다. 



# 스레드의 종류

### 캐리어 스레드 (Carrier Thread)

- **정의:**캐리어 스레드는 Project Loom의 일부로 도입된 개념으로, 버츄얼 스레드(Virtual Thread)를 실행하기 위한 운반체 역할을 합니다  캐리어 스레드는 버츄얼 스레드를 실행하기 위한 기본 스레드입니다. 이것은 기존의 OS(운영체제) 수준의 스레드를 기반으로 합니다. 버츄얼 스레드가 수행되는 동안 실제로 CPU의 실행 시간을 제공하는 스레드입니다.
- **목적:** 여러 버츄얼 스레드를 효율적으로 관리하고 실행하기 위해 사용됩니다. 한 캐리어 스레드는 동시에 여러 버츄얼 스레드의 작업을 처리할 수 있습니다.캐리어 스레드의 주요 목적은 다수의 경량 버츄얼 스레드를 효율적으로 스케줄링하고 실행하는 것입니다. 이를 통해 애플리케이션은 운영 체제 스레드의 제한을 받지 않고, 매우 높은 수준의 동시성을 달성할 수 있습니다. 캐리어 스레드는 여러 버츄얼 스레드가 공유할 수 있으며, 이를 통해 리소스 사용을 최적화합니다.

### 버츄얼 스레드 (Virtual Thread)

- **정의:** 버츄얼 스레드는 경량 스레드로, Java 가상 머신(JVM) 위에서 실행되며, 운영 체제의 스레드보다 훨씬 가볍고 더 적은 리소스를 사용합니다. 버츄얼 스레드는 캐리어 스레드 위에서 실행되며, 이를 통해 매우 높은 수준의 동시성과 확장성을 달성할 수 있습니다.
- **목적:** 버츄얼 스레드는 동시성을 달성하기 위한 더 간단하고 효율적인 방법을 제공합니다. 이를 통해 개발자는 수천 또는 수백만 개의 동시 작업을 쉽게 관리하고 실행할 수 있습니다. 버츄얼 스레드는 대규모 동시성 작업에 적합합니다.

### 플랫폼 스레드 (Platform Thread)

- **정의:** 플랫폼 스레드는 운영 체제가 직접 관리하는 스레드입니다. 이는 전통적인 자바 스레드 모델에서 사용되는 스레드로, Java 가상 머신(JVM)이 운영 체제의 기능을 활용하여 생성합니다.
- **목적:** 플랫폼 스레드는 고성능을 요구하는 계산 작업이나 시스템 수준의 작업에 주로 사용됩니다. 각 플랫폼 스레드는 상대적으로 많은 리소스를 소비하며, 이러한 스레드의 수는 시스템의 리소스에 의해 제한됩니다.



### 차이점 요약

- **목적과 사용:** 플랫폼 스레드는 Java에서 전통적으로 사용되는 OS 수준의 스레드입니다. 반면, 캐리어 스레드는 버츄얼 스레드를 실행하기 위한 플랫폼 스레드로, Project Loom의 컨텍스트 내에서 사용됩니다.
- **리소스 효율성:** 플랫폼 스레드는 각각 독립적으로 OS의 리소스를 사용합니다. 캐리어 스레드는 여러 버츄얼 스레드를 운반하며, 이를 통해 상대적으로 적은 수의 플랫폼 스레드로 많은 수의 동시 작업을 처리할 수 있게 합니다.



# 버츄얼 스레드 스케줄링

버추얼 스레드 설정 



* https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Thread.html

Java `java.lang.Thread`의 공식 문서를 읽는 것이 도움이 됩니다. 

JDK  가상 스레드 스케줄러를 구성하기 위해 사용할 수 있는 시스템 속성에 대한 설명입니다.

### 시스템 속성

| 시스템 속성                              | 설명                                                         |
| ---------------------------------------- | ------------------------------------------------------------ |
| `jdk.virtualThreadScheduler.parallelism` | 가상 스레드를 스케줄링하기 위해 사용할 수 있는 플랫폼 스레드의 수입니다. 기본값은 사용 가능한 프로세서의 수입니다. |
| `jdk.virtualThreadScheduler.maxPoolSize` | 스케줄러에 사용할 수 있는 플랫폼 스레드의 최대 수입니다. 기본값은 256입니다. |

이는 응용 프로그램을 시작할 때 최대 풀 크기를 변경할 수 있음을 의미해요.

```
java -Djdk.virtualThreadScheduler.maxPoolSize=512 <다른 인수들...>
```

동시에 실행되는 캐리어 스레드의 수를 제한하고 싶은 경우 다음 두 속성을 이용할 수 있어요. 

#### 캐리어 스레드의 수 제한 

첫 번째 속성을 사용하면 가상 스레드가 사용할 캐리어 스레드의 생성 수를 설정할 수 있습니다. 

기본적으로 캐리어 스레드의 수는 사용 가능한 cpu 코어의 수와 동일합니다. 

생성되는 캐리어 스레드의 수를 설정하려면 다음 속성을 사용할 수 있습니다.

```
 jdk.virtualThreadScheduler.parallelism=5
```

### 캐리어 스레드의 최대 수 

제한 병렬성 값에 의해 설정된 수를 초과하는 캐리어 스레드 수는 가상 스레드가 차단될 때 발생할 수 있습니다. 이러한 새로운 캐리어 스레드는 차단된 가상 스레드와 캐리어 스레드를 수용하기 위해 일시적으로 생성됩니다. 생성될 수 있는 캐리어 스레드의 최대 양을 설정하려면 다음 시스템 속성을 사용하십시오:

```
jdk.virtualThreadScheduler.maxPoolSize=10 
```

* 기본값은 256입니다. 

이 속성을 통해 버추얼 스레드를 실행할 캐리어 스레드의 최대 풀 크기를 설정할 수 있습니다.



결론 결론적으로, 동시에 실행되는 캐리어 스레드의 수를 제한하는 것은 Java에서 두 가지 시스템 속성을 사용함으로써 달성될 수 있습니다. jdk.virtualThreadScheduler.parallelism 속성을 설정하여 가상 스레드가 사용할 캐리어 스레드의 생성 수를 정의할 수 있고, jdk.virtualThreadScheduler.maxPoolSize 속성을 설정하여 차단된 가상 스레드를 수용하기 위해 일시적으로 생성될 수 있는 캐리어 스레드의 최대 수를 정의할 수 있습니다.

## 설정 방법

설정하는 방법은 주로 Java 애플리케이션을 시작할 때 JVM(Java Virtual Machine)에 전달하는 인수를 통해 이루어집니다. 

### 1. 커맨드 라인을 통한 설정

애플리케이션을 시작할 때, JVM에 전달하는 커맨드 라인 인수에 `-D키=값` 형식을 사용하여 속성을 설정할 수 있습니다. 예를 들어, 캐리어 스레드의 수를 5로 제한하고, 최대 캐리어 스레드 수를 10으로 설정하려면 다음과 같이 할 수 있습니다:

```sh
java -Djdk.virtualThreadScheduler.parallelism=5 -Djdk.virtualThreadScheduler.maxPoolSize=10 -jar your-application.jar
```

이 명령은 `your-application.jar`라는 Java 애플리케이션을 시작하면서 캐리어 스레드의 병렬성을 5로, 최대 풀 크기를 10으로 설정합니다.

### 2. 프로그램 내에서 설정

시스템 속성은 Java 코드 내에서도 `System.setProperty()` 메서드를 사용하여 설정할 수 있습니다. 이 방법은 애플리케이션의 초기화 단계나 설정이 필요한 특정 시점에서 사용할 수 있습니다. 예를 들어:

```java
public class Main {
    public static void main(String[] args) {
        System.setProperty("jdk.virtualThreadScheduler.parallelism", "5");
        System.setProperty("jdk.virtualThreadScheduler.maxPoolSize", "10");
        
        // 애플리케이션의 나머지 부분
    }
}
//or

static {
    System.setProperty("jdk.virtualThreadScheduler.parallelism", "5");
    System.setProperty("jdk.virtualThreadScheduler.maxPoolSize", "10");
}
```



## 스레드 양보방법

스레드 넘버가 1이고 i % 2 == 0 짝수이면 양보한다

```java
private static void demo(int threadNumber){
    log.info("thread-{} started", threadNumber);
    for (int i = 0; i < 10; i++) {
        log.info("thread-{} is printing {}. Thread: {}", threadNumber, i, Thread.currentThread());
        Thread.yield(); // just for demo purposes
    }
    log.info("thread-{} ended", threadNumber);
}
```



## Pinning Thread(고정된 스레드) - syncronized 사용 주의 

`Pinning` 쓰레드는 가상 스레드(프로젝트 로옴바(Loom)에서 도입된 경량 스레드)가 I/O 작업이나 기타 블로킹 연산을 수행할 때 발생하는 현상을 가리킵니다. 

일반적으로 가상 스레드는 실행 중인 작업이 블로킹 상태가 되면, 그 스레드를 실행 중인 캐리어 스레드(실제 OS 스레드)에서 분리(detach)하여 다른 작업을 수행할 수 있게 합니다. 

하지만, 특정 상황에서는 가상 스레드가 캐리어 스레드에 고정(pinned)될 수 있으며, 이 경우 다른 작업을 위해 해당 캐리어 스레드를 재사용할 수 없습니다.

`synchronized` 블록 내부에서 블로킹 I/O 작업을 수행하면, 해당 작업이 완료될 때까지 가상 스레드가 캐리어 스레드에서 분리되지 않고 고정되어 있어야 함을 의미합니다. 이러한 행위는 스케일링 능력에 영향을 줄 수 있습니다.

따라서 가상 스레드를 사용할 때는 블로킹 I/O 작업이나 `synchronized` 블록의 사용을 신중하게 고려해야 하며, 가능한 경우 비블로킹 I/O 작업을 수행하거나, 동시성을 관리하기 위해 `java.util.concurrent` 패키지의 도구들을 활용하는 것이 좋습니다.



### 가상 스레드 사용시 타 라이브러리가 이렇게 막혀있다면 미리 탐지하여 pinning을 막을 수 있을까? 

```java
public class Lec03SynchronizationWithIO {

    // Use this to check if virtual threads are getting pinned in your application
    static {
        System.setProperty("jdk.tracePinnedThreads", "short");
    }
}
```

아래와같이 로깅이 되서 추적해서 잡을 수 있다.

```java
Thread[#80,ForkJoinPool-1-worker-10,5,CarrierThreads]
    com.ys.example.ioTask(Lec03SynchronizationWithIO.java:47) <== monitors:1
```



### syncronized 대신 경쟁을 방지하는법 

### ReentrantLock

- `synchronized`보다 유연성을 제공합니다.

- 공정성 정책을 지원합니다.

  - 더 오래 기다린 스레드가 잠금을 획득할 기회를 얻습니다.

- 타임아웃이 있는 

  ```
  tryLock
  ```

  을 지원합니다.

  - 스레드가 잠금을 획득하기 위해 대기할 수 있는 최대 시간을 설정할 수 있습니다.

아래처럼 Lock을 사용하면 정합성이 보장된다.

```java
public class Lec04ReentrantLock {

    private static final Logger log = LoggerFactory.getLogger(Lec04ReentrantLock.class);
    private static final Lock lock = new ReentrantLock();
    private static final List<Integer> list = new ArrayList<>();

    public static void main(String[] args) {

        demo(Thread.ofVirtual());

        CommonUtils.sleep(Duration.ofSeconds(2));

        log.info("list size: {}", list.size());
    }

    private static void demo(Thread.Builder builder){
        for (int i = 0; i < 50; i++) {
            builder.start(() -> {
                log.info("Task started. {}", Thread.currentThread());
                for (int j = 0; j < 200; j++) {
                    inMemoryTask();
                }
                log.info("Task ended. {}", Thread.currentThread());
            });
        }
    }

    private static void inMemoryTask(){
        try{
            lock.lock();
            list.add(1);
        }catch (Exception e){
            log.error("error", e);
        }finally {
            lock.unlock();
        }
    }

}
```





## 쓰레드 팩토리 - 자식 버츄얼 스레드 생성하기

```java
public class Lec01ThreadFactory {

    private static final Logger log = LoggerFactory.getLogger(Lec01ThreadFactory.class);

    public static void main(String[] args) {
        // 가상 스레드를 활용한 데모 실행
        demo(Thread.ofVirtual().name("vins", 1).factory());

        // 3초 동안 대기
        CommonUtils.sleep(Duration.ofSeconds(3));
    }

    /*
        몇 개의 스레드를 생성합니다.
        각 스레드는 1개의 자식 스레드를 생성합니다.
        이것은 간단한 데모입니다. 실제로는 ExecutorService 등을 사용합시다.
        가상 스레드는 생성하기에 비용이 적게 듭니다.
     */
    private static void demo(ThreadFactory factory){
        for (int i = 0; i < 30; i++) {
            var t = factory.newThread(() -> {
                log.info("작업이 시작됐습니다. {}", Thread.currentThread()); // "Task started."
                var ct = factory.newThread(() -> {
                    log.info("자식 작업이 시작됐습니다. {}", Thread.currentThread()); // "Child task started."
                    CommonUtils.sleep(Duration.ofSeconds(2));
                    log.info("자식 작업이 끝났습니다. {}", Thread.currentThread()); // "Child task ended."
                });
                ct.start();
                log.info("작업이 끝났습니다. {}", Thread.currentThread()); // "Task ended."
            });
            t.start();
        }
    }

}

```



## 유용한 버츄얼 스레드 메소드

```java
/*
    몇 가지 유용한 스레드 메소드를 보여주는 간단한 데모
*/
public class Lec02ThreadMethodsDemo {

    private static final Logger log = LoggerFactory.getLogger(Lec02ThreadMethodsDemo.class);

    public static void main(String[] args) throws InterruptedException {
        join();
        CommonUtils.sleep(Duration.ofSeconds(1));
    }

    /*
        스레드가 가상인지 확인하기
     */
    private static void isVirtual() {
        var t1 = Thread.ofVirtual().start(() -> CommonUtils.sleep(Duration.ofSeconds(2)));
        var t2 = Thread.ofPlatform().start(() -> CommonUtils.sleep(Duration.ofSeconds(2)));
        log.info("t1이 가상 스레드인가: {}", t1.isVirtual());
        log.info("t2가 가상 스레드인가: {}", t2.isVirtual());
        log.info("현재 스레드가 가상 스레드인가: {}", Thread.currentThread().isVirtual());
    }


    /*
        여러 시간이 많이 걸리는 I/O 호출을 가상 스레드로 오프로드하고 완료될 때까지 기다립니다.
        참고: 실제 애플리케이션에서는 더 나은 방법을 사용할 수 있습니다.
        이것은 간단한 thread.join() 데모입니다.
     */
    private static void join() throws InterruptedException {
        var t1 = Thread.ofVirtual().start(() -> {
            log.info("제품 서비스 호출됨 {}", Thread.currentThread());
            CommonUtils.sleep(Duration.ofSeconds(5));
            log.info("제품 서비스 종료 {}", Thread.currentThread());

        });
        var t2 = Thread.ofVirtual().start(() -> {
            log.info("가격 서비스 호출됨 {}", Thread.currentThread());
            CommonUtils.sleep(Duration.ofSeconds(3));
            log.info("가격 서비스 종료 {}", Thread.currentThread());
        });
        t1.join(); // 이 스레드가 종료될 때까지 기다립니다
        System.out.println("t1 조인");
        t2.join(); // 이 스레드가 종료될 때까지 기다립니다
        System.out.println("t2 조인");
    }

    private static void join2() throws InterruptedException, ExecutionException {
        ExecutorService virtualExecutor = Executors.newVirtualThreadPerTaskExecutor();

        try {
            CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> {
                log.info("제품 서비스 호출됨 {}", Thread.currentThread());
                CommonUtils.sleep(Duration.ofSeconds(5));
                log.info("제품 서비스 종료 {}", Thread.currentThread());
            }, virtualExecutor);

            CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> {
                log.info("가격 서비스 호출됨 {}", Thread.currentThread());
                CommonUtils.sleep(Duration.ofSeconds(3));
                log.info("가격 서비스 종료 {}", Thread.currentThread());
            }, virtualExecutor);

            CompletableFuture<Void> allFutures = CompletableFuture.allOf(future1, future2);
            allFutures.join(); // 모든 스레드(작업)가 완료될 때까지 기다립니다.

            System.out.println("모든 스레드 작업 완료");
        } finally {
            virtualExecutor.shutdown(); // 작업이 끝났으니 ExecutorService를 종료합니다.
        }
    }

    /*
        스레드 실행을 중단/중지하기
        경우에 따라, 자바는 InterruptedException, IOException, SocketException 등을 던질 수 있습니다.

        현재 스레드가 중단되었는지 확인할 수도 있습니다.
        Thread.currentThread().isInterrupted() - boolean을 반환합니다.

        while(!Thread.currentThread().isInterrupted()){
            작업 계속하기
            ...
            ...
        }
     */
    private static void interrupt() {
        var t1 = Thread.ofVirtual().start(() -> {
            CommonUtils.sleep(Duration.ofSeconds(2));
            log.info("제품 서비스 호출됨");
        });
        log.info("t1이 중단됐는가: {}", t1.isInterrupted());
        t1.interrupt();
        log.info("t1이 중단됐는가: {}", t1.isInterrupted());
    }

}
```



# 다양한 threadpool executor와  버츄얼 스레드 스레드풀

```java
public class ExecutorType {

    private static final Logger log = LoggerFactory.getLogger(Lec02ExecutorServiceTypes.class);

    public static void main(String[] args) {
        // main 메소드에서 각 메소드를 호출하여 ExecutorService 타입을 시험해 볼 수 있습니다.
    }

    // 단일 스레드 실행자 - 작업을 순차적으로 실행하기 위함
    private static void single(){
        execute(Executors.newSingleThreadExecutor(), 3);
        // 사용 사례: 작업 실행 순서가 중요할 때 사용.
        // 생성 비용: 낮음. 단일 스레드만 유지하므로 오버헤드가 적습니다.
    }

    // 고정 스레드 풀
    private static void fixed(){
        execute(Executors.newFixedThreadPool(5), 20);
        // 사용 사례: 동시에 실행할 작업의 최대 수가 정해져 있을 때 사용.
        // 생성 비용: 중간. 고정된 수의 스레드를 미리 생성하고 관리해야 하므로 일정한 오버헤드가 있습니다.
    }

    // 탄력적 스레드 풀
    private static void cached(){
        execute(Executors.newCachedThreadPool(), 200);
        // 사용 사례: 실행해야 할 작업의 수가 불규칙하거나 예측 불가능할 때 사용.
        // 생성 비용: 높음. 필요에 따라 스레드 수가 자동으로 조절되므로 관리 오버헤드가 증가할 수 있습니다.
    }

    // 작업 당 가상 스레드를 생성하는 ExecutorService
    private static void virtual(){
        execute(Executors.newVirtualThreadPerTaskExecutor(), 10_000);
        // 사용 사례: 매우 많은 수의 짧은 작업을 처리해야 할 때 사용.
        // 생성 비용: 매우 낮음. 가상 스레드는 경량이며, 생성과 소멸 비용이 매우 낮습니다.
    }

    // 주기적인 작업을 스케줄링
    private static void scheduled(){
        try(var executorService = Executors.newSingleThreadScheduledExecutor()){
            executorService.scheduleAtFixedRate(() -> {
                log.info("실행 중인 작업");
            }, 0, 1, TimeUnit.SECONDS);

            CommonUtils.sleep(Duration.ofSeconds(5));
        }
        // 사용 사례: 주기적으로 반복해야 하는 작업을 스케줄링할 때 사용.
        // 생성 비용: 낮음. 주기적인 작업을 관리하는 데 필요한 리소스가 적습니다.
    }

    private static void execute(ExecutorService executorService, int taskCount){
        try(executorService){
            for (int i = 0; i < taskCount; i++) {
                int j = i;
                executorService.submit(() -> ioTask(j));
            }
            log.info("작업 제출 완료");
        }
    }

    private static void ioTask(int i){
        log.info("작업 시작: {}. 스레드 정보 {}", i, Thread.currentThread());
        CommonUtils.sleep(Duration.ofSeconds(5));
        log.info("작업 종료: {}. 스레드 정보 {}", i, Thread.currentThread());
    }

}

```



threadpoolexecutor에 다음과 같이 virtualThreadFactory를 전달하여 이름을 지정할 수 있다.

```java
var factory = Thread.ofVirtual().name("vins", 1).factory(); // 이름 지정 
execute(Executors.newFixedThreadPool(3), factory);
```

그러나 가상 스레드는 고정된 풀 (newFixedThreadPool)을 사용하면 안됀다. 

지정된 한도 이내에 더 많은 가상 스레드를 만들지 못하기 때문이다.

또한 풀에 담아 사용하지 말고, 그냥 생성해서 사용하자. 1회용이기 때문이다. 

* https://docs.oracle.com/en/java/javase/21/core/virtual-threads.html#GUID-7F5DA570-4B24-4CF6-899C-0424464B6032

스레드 실행 수를 제한하고 싶으면, semaphore를 사용하자

```java
import java.util.concurrent.Semaphore;

public class ConcurrencyLimiter implements AutoCloseable {

	private static final Logger log = LoggerFactory.getLogger(ConcurrencyLimiter.class);

	private final ExecutorService executor;
	private final Semaphore semaphore;
	private final Queue<Callable<?>> queue;

	public ConcurrencyLimiter(ExecutorService executor, int limit) {
		this.executor = executor;
		this.semaphore = new Semaphore(limit);
		this.queue = new ConcurrentLinkedQueue<>();
	}

	public <T> Future<T> submit(Callable<T> callable) {
		this.queue.add(callable);
		return executor.submit(() -> executeTask());
	}

	private <T> T executeTask() {
		try {
			semaphore.acquire();
			
			return (T)this.queue
				.poll()
				.call();
			
		} catch (Exception e) {
			log.error("error", e);
		} finally {
			semaphore.release();
		}
		return null;
	}

	@Override
	public void close() throws Exception {
		this.executor.close();
	}
}


public class ConcurrencyLimitWithSemaphore {

    private static final Logger log = LoggerFactory.getLogger(Lec06ConcurrencyLimitWithSemaphore.class);

    public static void main(String[] args) throws Exception {
        var factory = Thread.ofVirtual().name("vins", 1).factory();
        var limiter = new ConcurrencyLimiter(Executors.newThreadPerTaskExecutor(factory), 3);
      // 3회로 제한한다. 
        execute(limiter, 200);
    }

    private static void execute(ConcurrencyLimiter concurrencyLimiter, int taskCount) throws Exception {
        try(concurrencyLimiter){
            for (int i = 1; i <= taskCount; i++) {
                int j = i;
                concurrencyLimiter.submit(() -> printProductInfo(j));
            }
            log.info("submitted");
        }
    }

    // 3rd party service
    // contract: 3 concurrent calls are allowed
    private static String printProductInfo(int id){
        var product = Client.getProduct(id);
        log.info("{} => {}", id, product);
        return product;
    }
}

```

var limiter = new ConcurrencyLimiter(Executors.newThreadPerTaskExecutor(factory), 3);

*  동시 호출 수를 3회로 제한한다. 





# 가상 스레드와 스레드 로컬

가상 스레드도 스레드 로컬을 사용할 수 있다.

또한, 가상 스레드의 자식 가상 스레드도 스레드 로컬이 전파된다. 

```java
public class ThreadLocal {

    private static final Logger log = LoggerFactory.getLogger(Lec01ThreadLocal.class);
    private static final ThreadLocal<String> SESSION_TOKEN = new ThreadLocal<>();

    public static void main(String[] args) {

        Thread.ofVirtual().name("virtual-1").start( () -> processIncomingRequest());
        Thread.ofVirtual().name("virtual-2").start( () -> processIncomingRequest());

        CommonUtils.sleep(Duration.ofSeconds(1));
    }

    // ** ---- below code is just to demonstrate the workflow --- **

    private static void processIncomingRequest(){
        authenticate();
        controller();
    }

    private static void authenticate(){
        var token = UUID.randomUUID().toString();
        log.info("token={}", token);
        SESSION_TOKEN.set(token);
    }

    private static void controller(){
        log.info("controller: {}", SESSION_TOKEN.get());
        service();
    }

    private static void service(){
        log.info("service: {}", SESSION_TOKEN.get());
        var threadName = "child-of-" + Thread.currentThread().getName();
        Thread.ofVirtual().name(threadName).start(Lec02ThreadLocal::callExternalService);
    }

    // This is a client to call external service
    private static void callExternalService(){
        log.info("preparing HTTP request with token: {}", SESSION_TOKEN.get());
    }

}
```

결과

```
// here
00:11:01.874 [virtual-1] INFO com.ys.Lec02ThreadLocal -- token=03dca3e7-9c5e-4b8c-a535-0cf4b7a8655f

00:11:01.874 [virtual-2] INFO com.ys.Lec02ThreadLocal -- token=b56e4b9a-3569-4525-a5fc-bf1d1cf11807
00:11:01.876 [virtual-2] INFO com.ys.Lec02ThreadLocal -- controller: b56e4b9a-3569-4525-a5fc-bf1d1cf11807

// here
00:11:01.876 [virtual-1] INFO com.ys.Lec02ThreadLocal -- controller: 03dca3e7-9c5e-4b8c-a535-0cf4b7a8655f
00:11:01.876 [virtual-2] INFO com.ys.Lec02ThreadLocal -- service: b56e4b9a-3569-4525-a5fc-bf1d1cf11807

//here
00:11:01.876 [virtual-1] INFO com.ys.Lec02ThreadLocal -- service: 03dca3e7-9c5e-4b8c-a535-0cf4b7a8655f

//here
00:11:01.877 [child-of-virtual-1] INFO com.ys.Lec02ThreadLocal -- preparing HTTP request with token: 03dca3e7-9c5e-4b8c-a535-0cf4b7a8655f
00:11:01.877 [child-of-virtual-2] INFO com.ys.Lec02ThreadLocal -- preparing HTTP request with token: b56e4b9a-3569-4525-a5fc-bf1d1cf11807
```



자식스레드에서 스레드 로컬에 무슨 짓을 하더라도 부모 스레드로는 전파되지 않는다. 자식스레드로 사본을 제공하기 때문이다.

그런데, 문제는 너무 복제가 많으므로 메모리나 관리적에서 힘들 수 있다. 스레드 로컬을 사용한 코드는 지양하는것이 좋다.

이 문제를 자바에서는  Scope Value란것을 도입해서 해결하려고 한다.



## Scope Value

범위가 지정된 값을 사용하는 것이다.



스레드로컬문제에서 보면, 복제된 스레드가 상위 스레드의 스레드 로컬 정보를 가지고 있다.

 서로 다른 스레드에는 서로 다른 데이터가 필요할 수 있으며 다른 스레드가 소유한 데이터에 액세스하거나 재정의할 수 없어야 하는데, 스레드 로컬은 가능하다. 

이로인한 문제는 다음과 같다.

첫째, 모든 스레드-로컬 변수는 변경 가능(mutable)하며, 어떤 코드에서든 언제든지 setter 메소드를 호출할 수 있습니다. 따라서, 데이터는 컴포넌트 사이에서 어떤 방향으로든 흐를 수 있어, 어떤 컴포넌트가 공유 상태를 업데이트하는지와 그 순서를 이해하기 어렵게 만듭니다.

둘째, set 메소드를 사용하여 스레드의 인스턴스를 작성할 때, 데이터는 스레드의 전체 수명 동안 혹은 스레드가 remove 메소드를 호출할 때까지 유지됩니다. 개발자가 remove 메소드를 호출하는 것을 잊어버리면, 데이터는 필요 이상으로 메모리에 유지됩니다.

마지막으로, 부모 스레드의 스레드-로컬 변수는 자식 스레드에 의해 상속될 수 있습니다. 부모 스레드-로컬 변수를 상속하는 자식 스레드를 생성할 때, 새 스레드는 모든 부모 스레드-로컬 변수에 대한 추가 저장 공간을 할당해야 합니다.



범위가 지정된 값을 사용하면 메서드 인수를 사용하지 않고도 **구성 요소 간에 변경할 수 없는 데이터를 안전하고 효율적으로 공유 할 수 있습니다**



사용법 측면에서는 스레드 로컬과 비슷합니다. 

scope value는 스레드당 하나씩 여러 형태로 사용합니다.스레드-로컬 변수와 유사하게, 스코프 값(Scope Values)은 스레드마다 하나씩 여러 인카네이션(incarnation)을 사용합니다. 또한, 이들은 보통 public static 필드로 선언되어 많은 컴포넌트에서 쉽게 접근할 수 있습니다:

```java
public final static ScopedValue<User> LOGGED_IN_USER = ScopedValue.newInstance();
```

반면에, 스코프 값은 한 번 작성되면 변경할 수 없습니다. 스코프 값은 스레드 실행의 제한된 기간 동안만 사용할 수 있습니다:

```java
ScopedValue.where(LOGGED_IN_USER, user.get()).run(
  () -> service.getData()
);
```

`where` 메소드는 스코프 값을 요구하며, 이것이 바인딩될 객체를 필요로 합니다. `run` 메소드를 호출할 때, 스코프 값은 바인딩되어 현재 스레드에 고유한 인카네이션을 생성한 다음, 람다 표현식이 실행됩니다.

`run` 메소드의 수명 동안, 표현식에서 직접적으로나 간접적으로 호출된 어떤 메소드라도 스코프 값을 읽을 수 있는 능력을 가집니다. 그러나, `run` 메소드가 끝나면 바인딩은 파괴됩니다.

스코프 변수의 제한된 수명과 불변성은 스레드 동작에 대한 추론을 단순화하는 데 도움을 줍니다. 불변성은 보다 나은 성능을 보장하는 데 도움을 주며, 데이터는 오직 한 방향으로만 전달됩니다:

### scope value의 바인딩 문제 

`ScopedValue`의 바인딩은 `runWhere`에 전달된 람다 표현식의 실행 컨텍스트 내에서만 유효하며, 이 컨텍스트를 벗어난 후에는 더 이상 유효하지 않습니다. 범위 밖에서 사용하면 예외가 발생하게 됩니다. 

아래 예제를 봅시다

```java
public class ScopedValues {

    private static final Logger log = LoggerFactory.getLogger(ScopedValues.class);
    private static final ScopedValue<String> SESSION_TOKEN = ScopedValue.newInstance();

    public static void main(String[] args) {

        log.info("isBound={}", SESSION_TOKEN.isBound());
        log.info("value={}", SESSION_TOKEN.orElse("default value"));

        Thread.ofVirtual().name("1").start( () -> processIncomingRequest());
        // Thread.ofVirtual().name("2").start( () -> processIncomingRequest());

        CommonUtils.sleep(Duration.ofSeconds(1));

    }

    private static void processIncomingRequest(){
        var token = authenticate();

        ScopedValue.runWhere(SESSION_TOKEN, token, () -> controller());

        System.out.println("good : " + SESSION_TOKEN.get()); // 예외 발생
         //controller(); // 여기서도 예외 발생 
    }

    private static String authenticate(){
        var token = UUID.randomUUID().toString();
        log.info("token={}", token);
        return token;
    }

    // @Principal
    private static void controller(){
        log.info("controller: {}", SESSION_TOKEN.get());
        service();
    }

    private static void service(){
        log.info("service: {}", SESSION_TOKEN.get());
        ScopedValue.runWhere(SESSION_TOKEN, "new-token-" + Thread.currentThread().getName(), () -> callExternalService());
        System.out.println("service end");
    }

    // This is a client to call external service
    private static void callExternalService(){
        log.info("preparing HTTP request with token: {}", SESSION_TOKEN.get());
    }

}

```

`ScopedValue`는 특정한 스레드에서만 잠시 동안 사용할 수 있는 변수입니다. 이 변수는 설정한 스레드 내에서만 값을 가지고 있고, 그 스레드의 작업이 끝나면 그 값은 사라지게 됩니다.

여기서 `ScopedValue.runWhere`라는 메서드를 사용하면, 그 메서드 안에서만 `SESSION_TOKEN` 변수에 특정한 값을 "임시로" 할당할 수 있습니다

`ScopedValue.runWhere` 메서드를 사용하여 `SESSION_TOKEN`의 범위를 지정된 람다 표현식(`() -> controller()`) 실행 동안에만 바인딩합니다

```java
ScopedValue.runWhere(SESSION_TOKEN, token, () -> controller());
```

이 바인딩은 `runWhere` 메서드에 의해 생성된 람다 표현식의 실행 컨텍스트 내에서만 유효합니다.

 즉, `controller`와 `service` 메서드 내에서 `SESSION_TOKEN`에 접근할 수 있으나, `runWhere` 메서드 호출이 완료되고 나면 해당 바인딩은 해제됩니다.

`runWhere` 메서드 내부에서 `SESSION_TOKEN`에 값을 할당했지만, 그 메서드가 끝나는 순간 그 할당한 값은 사라지기 때문에, 메서드 밖에서 그 값을 호출하려고 하면 값을 찾을 수 없게 되는 거죠.



유효한 범위 내에서만 사용하지 않으면 noSuchElementException이 발생하게 됩니다. 



이 값을 상속시키기 위해 구조화된 동시성(StructuredConcorrency)라는 개념이 나오게 됩니다.



### Inheriting Scoped Value (스코프 벨류 상속 )

범위 지정된 값은 `StructuredTaskScope`를 사용하여 생성된 모든 자식 스레드에 자동으로 상속됩니다. 자식 스레드는 부모 스레드에서 설정된 범위 지정된 값에 대한 바인딩을 사용할 수 있습니다:

```java
try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
    Future<Optional<Data>> internalData = scope.fork(
      () -> internalService.getData(request)
    );
    Future<String> externalData = scope.fork(externalService::getData);
    try {
        scope.join();
        scope.throwIfFailed();

        Optional<Data> data = internalData.resultNow();
        // 응답에서 데이터를 반환하고 적절한 HTTP 상태를 설정
    } catch (InterruptedException | ExecutionException | IOException e) {
        response.setStatus(500);
    }
}
```

이 경우, `fork` 메소드를 통해 생성된 자식 스레드에서 실행 중인 서비스에서도 범위 지정된 값을 여전히 접근할 수 있습니다. 하지만, 스레드-로컬 변수와 달리 부모 스레드에서 자식 스레드로 범위 지정된 값이 복사되지는 않습니다.

### 3.3. 범위 지정된 값의 재바인딩

범위 지정된 값은 변경 불가능하기 때문에 저장된 값을 변경하기 위한 set 메소드를 지원하지 않습니다. 하지만, 제한된 코드 섹션의 호출에 대해 범위 지정된 값을 재바인딩할 수 있습니다.

예를 들어, `run`에서 호출된 메소드로부터 범위 지정된 값을 숨기기 위해 `where` 메소드를 사용하여 null로 설정할 수 있습니다:

```java
ScopedValue.where(Server.LOGGED_IN_USER, null).run(service::extractData);
```

하지만, 해당 코드 섹션이 종료되는 즉시 원래 값이 다시 사용 가능해집니다. `run` 메소드의 반환 타입이 void임을 유의해야 합니다. 우리의 서비스가 값을 반환하는 경우, 반환된 값들을 처리할 수 있도록 `call` 메소드를 사용할 수 있습니다.



# 구조화된 동시성(StructuredConcorrency)

구조화된 동시성(Structured Concurrency)은 동시성 프로그래밍에서의 한 패턴으로, 코드의 복잡성을 줄이고, 버그를 쉽게 찾을 수 있도록 돕는 방식입니다. 이 개념은 동시에 실행되는 작업들을 더 잘 관리하고, 코드의 흐름을 이해하기 쉽게 만드는 데 중점을 둡니다. 주요 목표 중 하나는 프로그램에서 생성된 모든 병렬 작업이 명확하게 구조화되고, 제어될 수 있도록 하는 것입니다.

### 구조화된 동시성의 핵심 원칙:

1. **범위 지정**: 구조화된 동시성에서는 모든 병렬 작업이 명시적인 범위 안에서 생성되어야 합니다. 이는 작업이 시작되고 종료되는 생명주기가 명확히 정의되어 있음을 의미합니다. 범위가 종료될 때까지 범위 안에서 시작된 모든 작업이 완료되어야 합니다.
2. **자원 관리**: 구조화된 동시성을 사용하면 생성된 자원(예: 스레드, 핸들 등)이 적절히 관리되고 해제됩니다. 이는 메모리 누수나 자원 고갈 같은 문제를 방지하는 데 도움이 됩니다.
3. **오류 처리**: 오류 발생 시, 오류를 효과적으로 포착하고 처리할 수 있어야 합니다. 구조화된 동시성에서는 범위 내에서 발생한 오류를 범위를 관리하는 상위 코드 블록으로 전파하여 적절히 처리할 수 있습니다.
4. **가독성과 유지보수성 향상**: 코드의 동시적 부분이 명확하게 구조화되어 있으면, 프로그램의 흐름을 더 쉽게 이해할 수 있습니다. 이는 프로그램의 가독성을 향상시키고, 버그를 더 쉽게 찾아내고 수정할 수 있게 합니다.



테스크가 여러 작은 작업으로 나뉠때, 스레드마다 서로 다른 결과가 나올 수 있다. 

아래 케이스에 따라 동시성을 성공과 실패로 나누어 예외를 관리할 수 있따. 

| 시나리오      | 설명                                                         |
| ------------- | ------------------------------------------------------------ |
| 성공/실패     | 서브태스크들이 다른 스레드에서 실행됩니다. 각각의 성공 또는 실패 상태가 될 수 있습니다. (Executor Service 사용과 유사) |
| 모두 성공     | 모든 서브태스크가 성공해야 합니다. 어느 하나라도 실패하면, 다른 실행 중인 서브태스크들을 취소합니다. |
| 첫번째만 성공 | 첫 번째로 성공한 응답을 얻고 나머지는 취소합니다.            |

구조적 동시성이 이러한 다양한 실행 시나리오를 보다 명확하게 관리하고, 효과적으로 실행하기 위한 메커니즘을 제공하기 때문입니다.



구조적 동시성이 없는 경우, 동시성 프로그래밍에서 다음과 같은 여러 문제가 발생할 수 있습니다:

1. **자원 관리의 어려움**: 개별 스레드나 작업을 수동으로 관리해야 하기 때문에, 사용한 자원을 적절히 해제하지 않으면 메모리 누수나 자원 고갈과 같은 문제가 발생할 수 있습니다. 구조적 동시성은 자동으로 자원을 관리하고 해제하여 이러한 문제를 예방합니다.
2. **오류 처리 복잡성**: 복수의 스레드나 태스크에서 발생하는 오류를 효율적으로 관리하고 처리하는 것이 어렵습니다. 오류가 발생했을 때, 모든 관련 태스크를 적절히 취소하거나 오류를 상위로 전파하는 로직을 수동으로 구현해야 합니다. 구조적 동시성은 이를 단순화하여 오류 처리를 더 용이하게 만듭니다.
3. **코드 복잡성 증가**: 개별 스레드의 생명주기를 수동으로 관리하면 코드가 복잡해지고, 이해하기 어려워집니다. 이로 인해 버그가 발생하기 쉬워지고, 유지보수가 어려워집니다. 구조적 동시성은 코드의 구조를 명확하게 하여 이러한 문제를 줄입니다.
4. **동기화 문제**: 여러 스레드가 공유 자원에 접근할 때 동기화를 적절히 관리하지 못하면, 데이터 무결성 문제나 경쟁 상태(race condition)가 발생할 수 있습니다. 구조적 동시성을 사용하면, 이러한 동기화 문제를 더 쉽게 관리할 수 있는 패턴을 제공합니다.
5. **작업 취소 및 종료의 어려움**: 복수의 스레드나 태스크가 실행 중일 때, 특정 조건에서 모든 작업을 취소하거나 안전하게 종료시키는 것이 어려울 수 있습니다. 구조적 동시성은 작업의 범위를 명확하게 정의하고, 범위 내의 모든 작업을 쉽게 제어할 수 있는 메커니즘을 제공합니다.



예시코드

* 구조적 동시성 작업 범위 객체를 정의하고 외부 api 호출을 2개의 가상스레드로 합니다.

```java
/*
    구조화된 태스크 스코프를 사용한 스코프드 값 상속
 */
public class StructuredTaskScopeWithValue {

    private static final Logger log = LoggerFactory.getLogger(StructuredTaskScopeWithValue.class);
    private static final ScopedValue<String> SESSION_TOKEN = ScopedValue.newInstance(); // 세션 토큰을 위한 스코프드 값 생성

    public static void main(String[] args) {

        // 세션 토큰에 "token-123" 값을 할당하고 task 메서드를 실행
        ScopedValue.runWhere(SESSION_TOKEN, "token-123", StructuredTaskScopeWithValue::task);

    }

    private static void task() {
        try (var taskScope = new StructuredTaskScope<>()) { // 가상 스레드를 생성할 수 있는 스코프 생성.

            log.info("token: {}", SESSION_TOKEN.get()); // 현재 세션 토큰 값 로깅

            // 하위 작업 생성
            var subtask1 = taskScope.fork(StructuredTaskScopeWithValue::getDeltaAirfare); // 델타 항공 운임 조회 작업
            var subtask2 = taskScope.fork(StructuredTaskScopeWithValue::getFrontierAirfare); // 프론티어 항공 운임 조회 작업

            taskScope.join(); // 모든 하위 작업이 완료될 때까지 대기

            log.info("subtask1 state: {}", subtask1.state()); // 하위 작업의 상태 (UNAVAILABLE, SUCCESS, FAIL) 반환
            log.info("subtask2 state: {}", subtask2.state());

            log.info("subtask1 result: {}", subtask1.get()); // 하위 작업의 결과 출력
            log.info("subtask2 result: {}", subtask2.get());

        } catch (Exception e) {
            throw new RuntimeException(e); // 예외 발생 시 RuntimeException을 던짐
        }
    }

    private static String getDeltaAirfare() {
        var random = ThreadLocalRandom.current()
                .nextInt(100, 1000); // 100에서 1000 사이의 임의의 값 생성
        log.info("delta: {}", random); // 생성된 무작위 값 로깅
        log.info("token: {}", SESSION_TOKEN.get()); // 현재 세션 토큰 값 로깅
        CommonUtils.sleep("delta", Duration.ofSeconds(1)); // 1초간 대기
        return "Delta-$" + random; // 델타 항공 운임 반환
    }

    private static String getFrontierAirfare() {
        var random = ThreadLocalRandom.current()
                .nextInt(100, 1000); // 100에서 1000 사이의 임의의 값 생성
        log.info("frontier: {}", random); // 생성된 무작위 값 로깅
        log.info("token: {}", SESSION_TOKEN.get()); // 현재 세션 토큰 값 로깅
        CommonUtils.sleep("frontier", Duration.ofSeconds(2)); // 2초간 대기
        failingTask(); // 예외를 발생시키는 작업 실행
        return "Frontier-$" + random; // 프론티어 항공 운임 반환 (이 코드는 실행되지 않음)
    }

    private static String failingTask() {
        throw new RuntimeException("oops"); // RuntimeException 발생
    }

}
```

* UNAVIABLE: 아직 시작되지 않았거나 시작상태가 결정되지 않은상태
* SUCCESS: 테스크 성공, 
* FAIL : 실패. 호출시 예외 발생 가능. 

결과

```
22:25:48.904 [main] INFO com.ys.ScopeValueWithStructedScope -- token: token-123
22:25:48.910 [] INFO com.ys.ScopeValueWithStructedScope -- delta: 949
22:25:48.910 [] INFO com.ys.ScopeValueWithStructedScope -- frontier: 853
22:25:48.910 [] INFO com.ys.ScopeValueWithStructedScope -- detal token: token-123
22:25:48.910 [] INFO com.ys.ScopeValueWithStructedScope -- frontier token: token-123
22:25:50.918 [main] INFO com.ys.ScopeValueWithStructedScope -- subtask1 state: SUCCESS
22:25:50.919 [main] INFO com.ys.ScopeValueWithStructedScope -- subtask1 result: Delta-$949
Exception in thread "main" java.lang.RuntimeException: java.lang.IllegalStateException: Subtask not completed or did not complete successfully
```

* Frontier 호출시 일부러 예외를 발생시켰습니다. 
* 그렇게되면, subtask2의 state는 FAIL이 나오며, 하위작업의 결과를 get하는 과정에서 예외가 상위 스콮으로 번져 전체 테스크는 실패하게 됩니다.
* 그러나 subTask1에는 영향을 미치지 않았습니다. SUCCESS 
* 결과를 보면, 서브테스크로는 ScopeValue가 상속되는것을 볼 수 있습니다. 즉 StructuredTaskScope 내에서는 같은 변수를 공유하는 것이지요.
* `StructuredTaskScope`를 사용하면, 개발자는 여러 작업을 동시에 실행하고, 그들이 모두 완료될 때까지 기다릴 수 있는 명확한 구조를 갖게 됩니다.



### 만약 한 처리가 실패시, 다른 나머지 처리도 실패하고 싶다면?

`taskScope.throwIfFailed` 메소드는 태스크 스코프 내에서 실패한 태스크가 있는 경우, 사용자 정의 예외(`RuntimeException("something went wrong")`)를 발생시키는 방법입니다. 

```java
try (var taskScope = new StructuredTaskScope.ShutdownOnFailure()) {
	var subtask1 = taskScope.fork(Lec05CancelOnFailure::getDeltaAirfare);
	var subtask2 = taskScope.fork(Lec05CancelOnFailure::failingTask);
	
  taskScope.join();
	taskScope.throwIfFailed(ex -> new RuntimeException("something went wrong", ex));
	
  log.info("subtask1 state: {}", subtask1.state());
	log.info("subtask2 state: {}", subtask2.state());
} catch (Exception e) {
	throw new RuntimeException(e);
}

// 결과
-- delta: 661
-- delta is cancelled
Exception in thread "main" java.lang.RuntimeException: java.lang.RuntimeException: something went wrong
```

이 구조는 한 태스크가 실패하면 모든 태스크가 취소되고 종료되는 방식으로 작동합니다. 



### 만약 한 처리가 실패하더라도 예외가 상위 스콮으로 던지지 않고, 나머지 테스크를 종료 시키고  성공적으로 끝내게 하려면? 

**`StructuredTaskScope.ShutdownOnSuccess` 사용**: 이 클래스는 여러 태스크를 동시에 실행할 때, 첫 번째 성공적으로 완료된 태스크가 나타나면 나머지 태스크를 자동으로 셧다운하는 기능을 제공합니다. 이는 여러 대안적인 실행 경로가 있고, 그 중 하나만 성공하면 충분한 경우에 유용합니다.

```java
try (var taskScope = new StructuredTaskScope.ShutdownOnSuccess<>()) {
	var subtask1 = taskScope.fork(Lec06FirstSuccess::failingTask);
	var subtask2 = taskScope.fork(Lec06FirstSuccess::getFrontierAirfare);
	taskScope.join();
	log.info("subtask1 state: {}", subtask1.state());
	log.info("subtask2 state: {}", subtask2.state());
	log.info("subtask result: {}", taskScope.result(ex -> new RuntimeException("all failed", ex)));
} catch (Exception e) {
	throw new RuntimeException(e);
}

// 결과
-- frontier: 753
-- subtask1 state: FAILED
-- subtask2 state: SUCCESS
-- subtask result: Frontier-$753
```

두 개의 비동기 태스크를 실행하고, 첫 번째로 성공적으로 완료되는 태스크가 있을 때 나머지 태스크를 셧다운(종료)하는 패턴입니다. 

만약 모든 태스크가 실패하면, 사용자 정의 예외를 던집니다





# SpringBoot With Virtual Thread

springboot 3.2부터 virtual thread를 사용할 수 있다.

활성화는 다음 property로할 수 있다.

```yaml
# virtual thread enabled/disabled
spring.threads.virtual.enabled=true
```





스레드 모델 조건에 따라 빈을 다르게 생성하기 위한 Condition 어노테이션과 구현체도 추가되었다.

```java
/**
 * {@link Conditional @Conditional} that matches when the specified threading is active.
 *
 * @author Moritz Halbritter
 * @since 3.2.0
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnThreadingCondition.class)
public @interface ConditionalOnThreading {

	/**
	 * The {@link Threading threading} that must be active.
	 * @return the expected threading
	 */
	Threading value();

}
```



Virutal Thread Executor 전용 Bean을 선언할 수 있다.

```java
@Configuration
public class ExecutorServiceConfig {

    @Bean
    @ConditionalOnThreading(Threading.VIRTUAL)
    public ExecutorService virtualThreadExecutor(){
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    @Bean
    @ConditionalOnThreading(Threading.PLATFORM)
    public ExecutorService platformThreadExecutor(){
        return Executors.newCachedThreadPool();
    }

}
```

만약 버츄얼 스레드 이름을을 지정하고 싶다면?

```java
 @Bean
 @ConditionalOnThreading(Threading.VIRTUAL)
 public ExecutorService virtualThreadExecutor() {
     ThreadFactory factory = Thread.ofVirtual().name("my-virtual").factory();
         
     return Executors.newThreadPerTaskExecutor(factory);
 }
```



spring 3.2에 나온 RestClient에도 다음처럼 사용 가능.

```java
@Value("${spring.threads.virtual.enabled}")
private boolean isVirtualThreadEnabled;
private RestClient buildRestClient(String baseUrl) {
	log.info("base url: {}", baseUrl);
	var builder = RestClient.builder()
		.baseUrl(baseUrl);
	
	if (isVirtualThreadEnabled) {
		builder = builder.requestFactory(new JdkClientHttpRequestFactory(
			HttpClient.newBuilder()
				.executor(Executors.newVirtualThreadPerTaskExecutor())
				.build()
		));
	}
	return builder.build();
}
```

