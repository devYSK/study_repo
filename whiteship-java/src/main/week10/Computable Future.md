# Executors - CompletableFuture

<참조. 인프런 The Java8 강의>

### 고수준 (High-Level) Concurrency 프로그래밍

- 쓰레드를 만들고 관리하는 작업을 애플리케이션에서 분리
- **그런 기능을 Executors 에게 위임**

### Executors가 하는 일

- **쓰레드 만들기** : 애플리케이션이 사용할 쓰레드를 만들어 관리한다.
- **쓰레드 관리** : 쓰레드 생명 주기를 관리한다.
- **작업 처리 및 실행** : 쓰레드로 실행할 작업을 제공할 수 있는 API 를 제공한다.

### 주요 인터페이스

- **Executor** : execute(Runnable)

- ExecutorService

   : Executor 상속 받은 인터페이스로

  - Callable도 실행할 수 있으며
  - executor를 종료 시키거나
  - 여러 Callable을 동시에 실행하는 등의 기능을 제공한다.

- ScheduledExecutorService

   : ExecutorService를 상속받은 인터페이스로

  - 특정 시간 이후에 또는 주기적으로 작업을 실행할 수 있다.

**우리는 Runnable 만 제공하고**

그 이후 쓰레드와 관련한 기능은 Executor가 처리한다.는 개념이라고 볼 수 있다.

------

### 예제코드로 알아보자

**ExecutorService 사용**

- .execute() 혹은 submit() 으로 Runnable을 실행할 수 있다.
- 하단 코드에서 submit() (=execute()) 로 Runnable을 실행하면 쓰레드는

```java
package me.ssonsh.java8to11.completableFuture;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App {
    public static void main(String[] args) {
        // Thread를 하나만 쓰는 Thread : newSingleThreadExecutor();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        // executorService.execute(() -> System.out.println("Thread " + Thread.currentThread().getName()));
        executorService.submit(() -> System.out.println("Thread " + Thread.currentThread().getName()));
    }
}
```

실행이 되고 종료되는 것이 아님을 볼 수 있다. (원하는 print를 찍고 나서도 실행중임)

- executorService는 **다음 작업이 들어올 때 까지 계속 대기하고 있다.**

![image-20221209164419717](/Users/ysk/study/study_repo/whiteship-java/src/main/week10/images//image-20221209164419717.png)

→ 다음 작업을 기다리는 것이 아니라 종료 시키길 원한다면 .shutdown()을 통해 종료한다.

**.shutdown() : 그래이스풀 셧다운으로 돌고있는 Thread가 모두 처리되고 아름답게 종료..**

.shutdownNow() : 아무런 조건 상관없이 그 즉시 삭제

```java
executorService.shutdown();
```

**두개의 ThreadPool을 이용하여 작업을 요청해보자**

- 두개의 ThreadPool : newFixedThreadPool(2);

  ```java
  package me.ssonsh.java8to11.completableFuture;
  
  import java.util.concurrent.ExecutorService;
  import java.util.concurrent.Executors;
  
  public class App {
      public static void main(String[] args) {
          ExecutorService executorService = Executors.newFixedThreadPool(2);
          executorService.submit(getRunnable("Hello"));
          executorService.submit(getRunnable("Sson"));
          executorService.submit(getRunnable("The"));
          executorService.submit(getRunnable("Java"));
          executorService.submit(getRunnable("Thread"));
          executorService.shutdown();
      }
  
      private static Runnable getRunnable(String message) {
          return () -> System.out.println(message + "|" + Thread.currentThread().getName());
      }
  }
  ```

  위 예제에서는 2개의 Thread Pool을 사용하고 , 5개의 작업을 submit() 하였다.

  **결과**

  → 2개의 Thread를 이용하여 5개 일을 수행하였다.

  ```java
  /Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/bin/java -javaagent:/Applications/IntelliJ IDEA.app/Contents/lib/idea_rt.jar=60459:/Applications/IntelliJ IDEA.app/Contents/bin -Dfile.encoding=UTF-8 -classpath /Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/charsets.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/ext/cldrdata.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/ext/dnsns.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/ext/jaccess.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/ext/jfxrt.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/ext/localedata.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/ext/nashorn.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/ext/sunec.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/ext/sunjce_provider.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/ext/sunpkcs11.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/ext/zipfs.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/jce.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/jfr.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/jfxswt.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/jsse.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/management-agent.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/resources.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/rt.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/lib/ant-javafx.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/lib/dt.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/lib/javafx-mx.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/lib/jconsole.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/lib/packager.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/lib/sa-jdi.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/lib/tools.jar:/Users/sson/Dev/workspace/java8to11/target/classes:/Users/sson/.m2/repository/org/springframework/boot/spring-boot-starter/2.4.1/spring-boot-starter-2.4.1.jar:/Users/sson/.m2/repository/org/springframework/boot/spring-boot/2.4.1/spring-boot-2.4.1.jar:/Users/sson/.m2/repository/org/springframework/spring-context/5.3.2/spring-context-5.3.2.jar:/Users/sson/.m2/repository/org/springframework/spring-aop/5.3.2/spring-aop-5.3.2.jar:/Users/sson/.m2/repository/org/springframework/spring-beans/5.3.2/spring-beans-5.3.2.jar:/Users/sson/.m2/repository/org/springframework/spring-expression/5.3.2/spring-expression-5.3.2.jar:/Users/sson/.m2/repository/org/springframework/boot/spring-boot-autoconfigure/2.4.1/spring-boot-autoconfigure-2.4.1.jar:/Users/sson/.m2/repository/org/springframework/boot/spring-boot-starter-logging/2.4.1/spring-boot-starter-logging-2.4.1.jar:/Users/sson/.m2/repository/ch/qos/logback/logback-classic/1.2.3/logback-classic-1.2.3.jar:/Users/sson/.m2/repository/ch/qos/logback/logback-core/1.2.3/logback-core-1.2.3.jar:/Users/sson/.m2/repository/org/apache/logging/log4j/log4j-to-slf4j/2.13.3/log4j-to-slf4j-2.13.3.jar:/Users/sson/.m2/repository/org/apache/logging/log4j/log4j-api/2.13.3/log4j-api-2.13.3.jar:/Users/sson/.m2/repository/org/slf4j/jul-to-slf4j/1.7.30/jul-to-slf4j-1.7.30.jar:/Users/sson/.m2/repository/jakarta/annotation/jakarta.annotation-api/1.3.5/jakarta.annotation-api-1.3.5.jar:/Users/sson/.m2/repository/org/springframework/spring-core/5.3.2/spring-core-5.3.2.jar:/Users/sson/.m2/repository/org/springframework/spring-jcl/5.3.2/spring-jcl-5.3.2.jar:/Users/sson/.m2/repository/org/yaml/snakeyaml/1.27/snakeyaml-1.27.jar:/Users/sson/.m2/repository/org/slf4j/slf4j-api/1.7.30/slf4j-api-1.7.30.jar me.ssonsh.java8to11.completableFuture.App
  Hello|pool-1-thread-1
  Sson|pool-1-thread-2
  The|pool-1-thread-2
  Java|pool-1-thread-2
  Thread|pool-1-thread-2
  
  Process finished with exit code 0
  ```

  2개 Thread를 가지고 있는데 어떻게 5개 일을 수행했는가?

  ![image-20221209164433301](/Users/ysk/study/study_repo/whiteship-java/src/main/week10/images//image-20221209164433301.png)

**ScheduledExecutorService 사용**

- SchedulredExecutorService 는 ExecutorService를 확장한 것이다.
- Delay
- Period

```java
package me.ssonsh.java8to11.completableFuture;

import ch.qos.logback.core.util.TimeUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class App {
    public static void main(String[] args) {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

        // 1초 Delay를 가지고 2초 간격으로 getRunnable을 수행하라
        executorService.scheduleAtFixedRate(getRunnable("Hello"), 1, 2, TimeUnit.SECONDS);

    }

    private static Runnable getRunnable(String message) {
        return () -> System.out.println(message + "|" + Thread.currentThread().getName());
    }
}
```

→ 만약 executorService.shutdown(); 이 존재하였다면, 아무 동작도 하지 않을 것이다.

→ Scheduled를 통해 수행되어야 하지만 shutdown()을 통해 interrupt 되면서 작업이 중단된다.

------

### Future는?

- 지금까지는 Runnable void 리턴 타입이였다.

- 하지만, 어떤 쓰레드 작업을 통해 결과를 가져오고 싶을때? 는 Runnable로 할 수 없다.

  → Java 1.5에는 Callable 이 있다.

  **→ Callable은 Runnable과 같지만 Return 할 수 있다.**

- Runnable 대신 Callable Type을 타입을 주면 Return 값을 받아야 하는데 이것을 Future라 한다.

# Callable, Feature

### Callable

- Runnable과 유사하지만 작업의 결과를 받을 수 있다.
  - Runnable은 리턴타입이 void 임으로 결과 값을 받을 수 없다.

### Future

- 비동기적인 작업의 현재 상태를 조회하거나 결과를 가져올 수 있다.

### Callable/Future 예제

App.java

- Callable 생성
- ExecutorService 생성
- 생성한 Callable을 ExecutorService로 실행
  - 결과를 Future로 받아온다.

```java
package me.ssonsh.java8to11.completableFuture;

import ch.qos.logback.core.util.TimeUtil;

import java.util.concurrent.*;

public class App {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        // ExecutorService 생성 (singleThread)
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        // Runnable 대신 Callable 생성
        // 제네릭 <String> 을 볼 수 있듯이 이 Callable은 String을 반환할 수 있다.
        Callable<String> hello = () -> {

            Thread.sleep(2000L);
            return "hello";
        };

        // executorService로 callable을 submit 하면 Future를 반환받을 수 있다.
        Future<String> submit = executorService.submit(hello);
        System.out.println("Started!!");

        // Future를 get()하기 전까지는 실행되고 있다가.
        // get() 하게 되면 그때 가져온다.
        // 즉 get()을 만난 순간 기다린다. = Blocking Call
        submit.get();

        System.out.println("End!");
        executorService.shutdown();

    }

}
```

- executorService.submit(hello); 의 반환값으로 Future<T>를 받을 수 있다.

- Future<T> 에서 **.get();** 메소드를 통해 그 결과를 가져올 수 있다.

  → 이때 .get();이 호출되기 전까지는 Callable Thread가 실행되고 있는 상태이며,

  → .get(); 을 만난 순간 그 때 가져오게 된다.

  → 즉, 위 예시처럼 .get();을 만난순간 2s의 sleep이 이뤄지고 그 결과가 반환되면 가져오게된다.

  → **blocking call**의 개념이다.

**Q. 그렇다면? get(); 으로 결과를 가져오기 전까지 마냥 기다려야 할까?**

→ 중간 상태를 알 수 있다. **.isDone();**

→ 진행중이면 false, 종료되었으면 true를 반환한다.

```java
// executorService로 callable을 submit 하면 Future를 반환받을 수 있다.
Future<String> submit = executorService.submit(hello);

// get() 할때까지 마냥 기다려야 하나? 그 중간 상태를 알 수 없는가?
// Future의 isDone은 끝났으면 ture / 안끝났으면 false
System.out.println(submit.isDone());
```

**Q. Callable을 중간에 취소시킬 수 있는가?**

→ 취소시킬 수 있다. **.cancel(boolean)**

→ cancel 처리 후 isDone을 호출하면 true가 반환된다.

: 이는, 종료되었기 때문에 true가 반환되는 것이 아니라 cancel에 의해 취소되어 true가 반환된다.

**→ cancel 파라미터로 true를 전달하면, 현재 진행중인 쓰레드를 interrupt하고, 그렇지 않으면 현재 진행중인 작업이 끝날 때 까지 기다린다.**

```java
// cancel을 통해 인터럽트 하여
// cancel 하면 isDone은 무조건 ture가 되며 이는 작업이 끝났음을 의미하는 것이 아니라 취소되어 true가 반환된다.
submit.cancel(false);
System.out.println(submit.isDone());
```

**plus.**

cancel을 통해 취소한 이후

.get(); 으로 값을 가져오려고 하는 경우?

```java
Exception in thread "main" java.util.concurrent.CancellationException
	at java.util.concurrent.FutureTask.report(FutureTask.java:121)
	at java.util.concurrent.FutureTask.get(FutureTask.java:192)
	at me.ssonsh.java8to11.completableFuture.App.main(App.java:39)
```

### Callable을 여러개 한번에 실행 : invokeAll, invokeAny

### invokeAll

- 실행하는 모든 Thread가 끝날 때 까지 기다리고 그 결과를 List로 가져올 수 있다.
- 동시에 실행한 작업 중 제일 오래 걸리는 작업 만큼 시간이 소요된다.
- 아래 예시를 참고하면, hello와 java Thread가 끝나더라도 가장 오래 걸리는 sson을 기다렸다가 전체 결과를 가져오게 된다.
- executorService.invokeAll(..);

![https://s3-us-west-2.amazonaws.com/secure.notion-static.com/2868ffa5-8319-4fcc-9d2c-f13b5ac1379c/Untitled.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/2868ffa5-8319-4fcc-9d2c-f13b5ac1379c/Untitled.png)

```java
package me.ssonsh.java8to11.completableFuture;

import ch.qos.logback.core.util.TimeUtil;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class App {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        // ExecutorService 생성 (singleThread)
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        // 3개의 Callable 생성
        Callable<String> hello = () -> {
            Thread.sleep(2000L);
            return "hello";
        };
        Callable<String> sson = () -> {
            Thread.sleep(3000L);
            return "sson";
        };
        Callable<String> java = () -> {
            Thread.sleep(1000L);
            return "java";
        };

        List<Future<String>> futures = executorService.invokeAll(Arrays.asList(hello, sson, java));
        for (Future<String> future : futures) {
            System.out.println(future.get());
        }

        executorService.shutdown();

    }

}
```

### invokeAny

- 실행되는 여러개 Thread 중 종료되는 Thread로 부터 바로 결과를 가져온다.
- Blocking Call
- invokeAll 과 다르게 반환값이 1개 T 형태로 반환됨을 알 수 있다.
- 동시에 실행한 작업 중 제일 짧게 걸리는 작업 만큼 시간이 걸린다.
- 예
  - 3개 서버에 동일하게 파일을 Copy 해둔 다음, 3개 중 아무대서나 파일을 읽어올 때 가장 빠른 결과를 가지고 파일을 처리하고자 한다면?

![https://s3-us-west-2.amazonaws.com/secure.notion-static.com/be3f95ff-2c2e-4fa9-bcc3-a23e61d6c693/Untitled.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/be3f95ff-2c2e-4fa9-bcc3-a23e61d6c693/Untitled.png)

```java
package me.ssonsh.java8to11.completableFuture;

import ch.qos.logback.core.util.TimeUtil;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class App {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        // ExecutorService 생성 (singleThread)
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        // 3개의 Callable 생성
        Callable<String> hello = () -> {
            Thread.sleep(2000L);
            return "hello";
        };
        Callable<String> sson = () -> {
            Thread.sleep(3000L);
            return "sson";
        };
        Callable<String> java = () -> {
            Thread.sleep(1000L);
            return "java";
        };

        String s = executorService.invokeAny(Arrays.asList(hello, sson, java));
        System.out.println(s);
        executorService.shutdown();

    }

}
```

<aside> 💡 실제로 짧게 걸리는 java가 출력될 것 같지만..

executorService의 Thread가 singleThread여서 하나씩 들어가고 처리되고 나오면서 이와 같은 현상이 발생할 수 있다.

- Executors.newFixedThreadPool(4);

위와 같이 ThreadPool을 위와 같이 선언한다면, 원하는 결과를 받을 수 있을 것이다.

</aside>

------

### plus.

Future를 사용하면서 발생되는 문제점!

- 예외처리가 되지 않는다.
- 예외처리를 할 수 있는 api가 제공되지 않는다.
- 여러 Future를 조합하는 것이 쉽지 않다.
- Future의 .get();을 하기 전까지는 어떤것도 할 수 없다.
  - 무언가 하기 위해서는 .get(); 이후에 할 수 있다.
  - .get(); 은 blocking call 이다.

⇒ Java 8 에서는 이러한 점들을 개선하여 **CompletableFuture** 가 제공된다.

- implements **Future**

- implements 

  CompletionStage

  - 외부에서 Complete을 시킬 수 있다.

## CompletableFuture

비동기(Asynchronous) 프로그래밍을 가능케하는 인터페이스

→ Future를 사용해도 가능하였지만, 힘든일들이 많았다.

<aside> 💡 Future를 사용하면서 발생되는 문제점!

- 예외처리가 되지 않는다.
- 예외처리를 할 수 있는 api가 제공되지 않는다.
- 여러 Future를 조합하는 것이 쉽지 않다.
- Future의 .get();을 하기 전까지는 어떤것도 할 수 없다.
  - 무언가 하기 위해서는 .get(); 이후에 할 수 있다.
  - .get(); 은 blocking call 이다.

</aside>

⇒ Java 8 에서는 이러한 점들을 개선하여 **CompletableFuture** 가 제공된다.

- implements **Future**
- implements **CompletionStage**

ex.

명시적으로 값을 주어 실행해보기

```java
package me.ssonsh.java8to11.completableFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class App {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        CompletableFuture<String> future = new CompletableFuture<>();
        future.complete("sson");

        String result = future.get();
        System.out.println(result);
    }

}

///

package me.ssonsh.java8to11.completableFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class App {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        CompletableFuture<String> future = CompletableFuture.completedFuture("sson");
        System.out.println(future.get());
    }

}
```

------

### 비동기로 작업 실행하기

- 리턴 값이 없는 경우 : **runAsync();**

  ```java
  package me.ssonsh.java8to11.completableFuture;
  
  import java.util.concurrent.CompletableFuture;
  import java.util.concurrent.ExecutionException;
  
  public class App {
      public static void main(String[] args) throws ExecutionException, InterruptedException {
  
          CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
              System.out.println("SSON : " + Thread.currentThread().getName());
          });
  
          future.get();
      }
  
  }
  ```

  **결과**

  ```java
  /Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/bin/java -javaagent:/Applications/IntelliJ IDEA.app/Contents/lib/idea_rt.jar=61558:/Applications/IntelliJ IDEA.app/Contents/bin -Dfile.encoding=UTF-8 -classpath /Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/charsets.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/ext/cldrdata.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/ext/dnsns.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/ext/jaccess.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/ext/jfxrt.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/ext/localedata.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/ext/nashorn.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/ext/sunec.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/ext/sunjce_provider.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/ext/sunpkcs11.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/ext/zipfs.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/jce.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/jfr.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/jfxswt.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/jsse.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/management-agent.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/resources.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/rt.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/lib/ant-javafx.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/lib/dt.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/lib/javafx-mx.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/lib/jconsole.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/lib/packager.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/lib/sa-jdi.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/lib/tools.jar:/Users/sson/Dev/workspace/java8to11/target/classes:/Users/sson/.m2/repository/org/springframework/boot/spring-boot-starter/2.4.1/spring-boot-starter-2.4.1.jar:/Users/sson/.m2/repository/org/springframework/boot/spring-boot/2.4.1/spring-boot-2.4.1.jar:/Users/sson/.m2/repository/org/springframework/spring-context/5.3.2/spring-context-5.3.2.jar:/Users/sson/.m2/repository/org/springframework/spring-aop/5.3.2/spring-aop-5.3.2.jar:/Users/sson/.m2/repository/org/springframework/spring-beans/5.3.2/spring-beans-5.3.2.jar:/Users/sson/.m2/repository/org/springframework/spring-expression/5.3.2/spring-expression-5.3.2.jar:/Users/sson/.m2/repository/org/springframework/boot/spring-boot-autoconfigure/2.4.1/spring-boot-autoconfigure-2.4.1.jar:/Users/sson/.m2/repository/org/springframework/boot/spring-boot-starter-logging/2.4.1/spring-boot-starter-logging-2.4.1.jar:/Users/sson/.m2/repository/ch/qos/logback/logback-classic/1.2.3/logback-classic-1.2.3.jar:/Users/sson/.m2/repository/ch/qos/logback/logback-core/1.2.3/logback-core-1.2.3.jar:/Users/sson/.m2/repository/org/apache/logging/log4j/log4j-to-slf4j/2.13.3/log4j-to-slf4j-2.13.3.jar:/Users/sson/.m2/repository/org/apache/logging/log4j/log4j-api/2.13.3/log4j-api-2.13.3.jar:/Users/sson/.m2/repository/org/slf4j/jul-to-slf4j/1.7.30/jul-to-slf4j-1.7.30.jar:/Users/sson/.m2/repository/jakarta/annotation/jakarta.annotation-api/1.3.5/jakarta.annotation-api-1.3.5.jar:/Users/sson/.m2/repository/org/springframework/spring-core/5.3.2/spring-core-5.3.2.jar:/Users/sson/.m2/repository/org/springframework/spring-jcl/5.3.2/spring-jcl-5.3.2.jar:/Users/sson/.m2/repository/org/yaml/snakeyaml/1.27/snakeyaml-1.27.jar:/Users/sson/.m2/repository/org/slf4j/slf4j-api/1.7.30/slf4j-api-1.7.30.jar me.ssonsh.java8to11.completableFuture.App
  SSON : ForkJoinPool.commonPool-worker-1
  
  Process finished with exit code 0
  ```

- 리턴 값이 있는 경우 : **supplyAsync();**

  ```java
  package me.ssonsh.java8to11.completableFuture;
  
  import java.util.concurrent.CompletableFuture;
  import java.util.concurrent.ExecutionException;
  
  public class App {
      public static void main(String[] args) throws ExecutionException, InterruptedException {
  
          CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
              String name = "SSON";
              System.out.println(name + " : " + Thread.currentThread().getName());
              return name;
          });
  
          String result = future.get();
          System.out.println(result);
      }
  
  }
  ```

  결과

  ```java
  /Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/bin/java -javaagent:/Applications/IntelliJ IDEA.app/Contents/lib/idea_rt.jar=61569:/Applications/IntelliJ IDEA.app/Contents/bin -Dfile.encoding=UTF-8 -classpath /Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/charsets.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/ext/cldrdata.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/ext/dnsns.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/ext/jaccess.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/ext/jfxrt.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/ext/localedata.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/ext/nashorn.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/ext/sunec.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/ext/sunjce_provider.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/ext/sunpkcs11.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/ext/zipfs.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/jce.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/jfr.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/jfxswt.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/jsse.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/management-agent.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/resources.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/rt.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/lib/ant-javafx.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/lib/dt.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/lib/javafx-mx.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/lib/jconsole.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/lib/packager.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/lib/sa-jdi.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/lib/tools.jar:/Users/sson/Dev/workspace/java8to11/target/classes:/Users/sson/.m2/repository/org/springframework/boot/spring-boot-starter/2.4.1/spring-boot-starter-2.4.1.jar:/Users/sson/.m2/repository/org/springframework/boot/spring-boot/2.4.1/spring-boot-2.4.1.jar:/Users/sson/.m2/repository/org/springframework/spring-context/5.3.2/spring-context-5.3.2.jar:/Users/sson/.m2/repository/org/springframework/spring-aop/5.3.2/spring-aop-5.3.2.jar:/Users/sson/.m2/repository/org/springframework/spring-beans/5.3.2/spring-beans-5.3.2.jar:/Users/sson/.m2/repository/org/springframework/spring-expression/5.3.2/spring-expression-5.3.2.jar:/Users/sson/.m2/repository/org/springframework/boot/spring-boot-autoconfigure/2.4.1/spring-boot-autoconfigure-2.4.1.jar:/Users/sson/.m2/repository/org/springframework/boot/spring-boot-starter-logging/2.4.1/spring-boot-starter-logging-2.4.1.jar:/Users/sson/.m2/repository/ch/qos/logback/logback-classic/1.2.3/logback-classic-1.2.3.jar:/Users/sson/.m2/repository/ch/qos/logback/logback-core/1.2.3/logback-core-1.2.3.jar:/Users/sson/.m2/repository/org/apache/logging/log4j/log4j-to-slf4j/2.13.3/log4j-to-slf4j-2.13.3.jar:/Users/sson/.m2/repository/org/apache/logging/log4j/log4j-api/2.13.3/log4j-api-2.13.3.jar:/Users/sson/.m2/repository/org/slf4j/jul-to-slf4j/1.7.30/jul-to-slf4j-1.7.30.jar:/Users/sson/.m2/repository/jakarta/annotation/jakarta.annotation-api/1.3.5/jakarta.annotation-api-1.3.5.jar:/Users/sson/.m2/repository/org/springframework/spring-core/5.3.2/spring-core-5.3.2.jar:/Users/sson/.m2/repository/org/springframework/spring-jcl/5.3.2/spring-jcl-5.3.2.jar:/Users/sson/.m2/repository/org/yaml/snakeyaml/1.27/snakeyaml-1.27.jar:/Users/sson/.m2/repository/org/slf4j/slf4j-api/1.7.30/slf4j-api-1.7.30.jar me.ssonsh.java8to11.completableFuture.App
  SSON : ForkJoinPool.commonPool-worker-1
  SSON
  
  Process finished with exit code 0
  ```

- **runAsync() 이든, supplyAsync() 이든, 선언만 한다고 해서 아무일도 일어나지 않는다.**

  → .get(); 을 만나는 순간 처리된다.

### Callback 받기

- callback 받기 종류
  - 리턴타입이 있는 경우 : **thenApply (Function<T, R>)**
  - 리턴타입이 없는 경우 : **thenAccept(Consumer<T>)**
  - 리턴 받을 필요가 없다. 그냥 무언가 하기만 하면된다. : **thenRun(Runnable)**
- 기존 Future 에서는 Callback을 받을 수 없었다.
- but. CompleatableFuture에서는 Callback을 받을 수 있다. (큰.장.점)

**thenApply (Function<T, R>)**

```java
package me.ssonsh.java8to11.completableFuture;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class App {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            String name = "sson";
            System.out.println(name + " : " + Thread.currentThread().getName());
            return name;
        }).thenApply((s) -> {
            System.out.println(Thread.currentThread().getName());
            return s.toUpperCase();
        });

        String result = future.get();
        System.out.println(result);
    }

}
```

결과

```java
/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/bin/java -javaagent:/Applications/IntelliJ IDEA.app/Contents/lib/idea_rt.jar=61575:/Applications/IntelliJ IDEA.app/Contents/bin -Dfile.encoding=UTF-8 -classpath /Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/charsets.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/ext/cldrdata.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/ext/dnsns.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/ext/jaccess.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/ext/jfxrt.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/ext/localedata.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/ext/nashorn.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/ext/sunec.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/ext/sunjce_provider.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/ext/sunpkcs11.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/ext/zipfs.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/jce.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/jfr.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/jfxswt.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/jsse.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/management-agent.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/resources.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/rt.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/lib/ant-javafx.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/lib/dt.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/lib/javafx-mx.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/lib/jconsole.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/lib/packager.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/lib/sa-jdi.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/lib/tools.jar:/Users/sson/Dev/workspace/java8to11/target/classes:/Users/sson/.m2/repository/org/springframework/boot/spring-boot-starter/2.4.1/spring-boot-starter-2.4.1.jar:/Users/sson/.m2/repository/org/springframework/boot/spring-boot/2.4.1/spring-boot-2.4.1.jar:/Users/sson/.m2/repository/org/springframework/spring-context/5.3.2/spring-context-5.3.2.jar:/Users/sson/.m2/repository/org/springframework/spring-aop/5.3.2/spring-aop-5.3.2.jar:/Users/sson/.m2/repository/org/springframework/spring-beans/5.3.2/spring-beans-5.3.2.jar:/Users/sson/.m2/repository/org/springframework/spring-expression/5.3.2/spring-expression-5.3.2.jar:/Users/sson/.m2/repository/org/springframework/boot/spring-boot-autoconfigure/2.4.1/spring-boot-autoconfigure-2.4.1.jar:/Users/sson/.m2/repository/org/springframework/boot/spring-boot-starter-logging/2.4.1/spring-boot-starter-logging-2.4.1.jar:/Users/sson/.m2/repository/ch/qos/logback/logback-classic/1.2.3/logback-classic-1.2.3.jar:/Users/sson/.m2/repository/ch/qos/logback/logback-core/1.2.3/logback-core-1.2.3.jar:/Users/sson/.m2/repository/org/apache/logging/log4j/log4j-to-slf4j/2.13.3/log4j-to-slf4j-2.13.3.jar:/Users/sson/.m2/repository/org/apache/logging/log4j/log4j-api/2.13.3/log4j-api-2.13.3.jar:/Users/sson/.m2/repository/org/slf4j/jul-to-slf4j/1.7.30/jul-to-slf4j-1.7.30.jar:/Users/sson/.m2/repository/jakarta/annotation/jakarta.annotation-api/1.3.5/jakarta.annotation-api-1.3.5.jar:/Users/sson/.m2/repository/org/springframework/spring-core/5.3.2/spring-core-5.3.2.jar:/Users/sson/.m2/repository/org/springframework/spring-jcl/5.3.2/spring-jcl-5.3.2.jar:/Users/sson/.m2/repository/org/yaml/snakeyaml/1.27/snakeyaml-1.27.jar:/Users/sson/.m2/repository/org/slf4j/slf4j-api/1.7.30/slf4j-api-1.7.30.jar me.ssonsh.java8to11.completableFuture.App
sson : ForkJoinPool.commonPool-worker-1
main
SSON

Process finished with exit code 0
```

**thenAccept(Consumer<T>)**

```java
package me.ssonsh.java8to11.completableFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class App {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
            String name = "sson";
            System.out.println(name + " : " + Thread.currentThread().getName());
            return name;
        }).thenAccept((s) -> {
            System.out.println(Thread.currentThread().getName());
            System.out.println(s.toUpperCase());
        });

        future.get();
    }

}
```

결과

```java
/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/bin/java -javaagent:/Applications/IntelliJ IDEA.app/Contents/lib/idea_rt.jar=61583:/Applications/IntelliJ IDEA.app/Contents/bin -Dfile.encoding=UTF-8 -classpath /Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/charsets.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/ext/cldrdata.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/ext/dnsns.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/ext/jaccess.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/ext/jfxrt.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/ext/localedata.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/ext/nashorn.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/ext/sunec.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/ext/sunjce_provider.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/ext/sunpkcs11.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/ext/zipfs.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/jce.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/jfr.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/jfxswt.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/jsse.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/management-agent.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/resources.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/rt.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/lib/ant-javafx.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/lib/dt.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/lib/javafx-mx.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/lib/jconsole.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/lib/packager.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/lib/sa-jdi.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/lib/tools.jar:/Users/sson/Dev/workspace/java8to11/target/classes:/Users/sson/.m2/repository/org/springframework/boot/spring-boot-starter/2.4.1/spring-boot-starter-2.4.1.jar:/Users/sson/.m2/repository/org/springframework/boot/spring-boot/2.4.1/spring-boot-2.4.1.jar:/Users/sson/.m2/repository/org/springframework/spring-context/5.3.2/spring-context-5.3.2.jar:/Users/sson/.m2/repository/org/springframework/spring-aop/5.3.2/spring-aop-5.3.2.jar:/Users/sson/.m2/repository/org/springframework/spring-beans/5.3.2/spring-beans-5.3.2.jar:/Users/sson/.m2/repository/org/springframework/spring-expression/5.3.2/spring-expression-5.3.2.jar:/Users/sson/.m2/repository/org/springframework/boot/spring-boot-autoconfigure/2.4.1/spring-boot-autoconfigure-2.4.1.jar:/Users/sson/.m2/repository/org/springframework/boot/spring-boot-starter-logging/2.4.1/spring-boot-starter-logging-2.4.1.jar:/Users/sson/.m2/repository/ch/qos/logback/logback-classic/1.2.3/logback-classic-1.2.3.jar:/Users/sson/.m2/repository/ch/qos/logback/logback-core/1.2.3/logback-core-1.2.3.jar:/Users/sson/.m2/repository/org/apache/logging/log4j/log4j-to-slf4j/2.13.3/log4j-to-slf4j-2.13.3.jar:/Users/sson/.m2/repository/org/apache/logging/log4j/log4j-api/2.13.3/log4j-api-2.13.3.jar:/Users/sson/.m2/repository/org/slf4j/jul-to-slf4j/1.7.30/jul-to-slf4j-1.7.30.jar:/Users/sson/.m2/repository/jakarta/annotation/jakarta.annotation-api/1.3.5/jakarta.annotation-api-1.3.5.jar:/Users/sson/.m2/repository/org/springframework/spring-core/5.3.2/spring-core-5.3.2.jar:/Users/sson/.m2/repository/org/springframework/spring-jcl/5.3.2/spring-jcl-5.3.2.jar:/Users/sson/.m2/repository/org/yaml/snakeyaml/1.27/snakeyaml-1.27.jar:/Users/sson/.m2/repository/org/slf4j/slf4j-api/1.7.30/slf4j-api-1.7.30.jar me.ssonsh.java8to11.completableFuture.App
sson : ForkJoinPool.commonPool-worker-1
main
SSON

Process finished with exit code 0
```

**Q.**

지금까지 예제를 살펴보면,

기존과는 다르게 별도의 ThreadPool 을 만들지 않고 어떻게 실행이 가능케 한거냐? 라는 궁금증이 생긴다.

→ **ForkJoinPool** 때문에 실행이 가능한 것이다.

------

### 원하는 Executor(Thread Pool)를 사용해서 실행할 수도 있다.

- 기본 : ForkJoinPool.common.Pool();

- 원한다면, 얼마든지 만들어서 사용할 수 있다.

  - **runAsync(), supplyAsync()** 호출 시 두번째 인자로 사용할 수 있다.

  - thenApply(), thenAccept(), thenRun() 처리 시 별도의 풀로 처리하고자 한다면?

    → 두번째 인자로 사용할 수 있으며

    **→ thenApplyAsync(), thenAcceptAsync(), thenRunAsync()** 로 사용하도록 한다.

- executorService를 사용했다면..? 꼭...!

  - executorService.shutdown();

```java
package me.ssonsh.java8to11.completableFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(4);
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
            String name = "sson";
            System.out.println(name + " : " + Thread.currentThread().getName());
            return name;
        }, executorService).thenAcceptAsync((s) -> {
            System.out.println(Thread.currentThread().getName());
            System.out.println(s.toUpperCase());
        }, executorService);

        future.get();
        executorService.shutdown();
    }

}
```

**결과**

currentThread().getName() 이 설정 전과/후 다름을 볼 수 있다.

```java
/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/bin/java -javaagent:/Applications/IntelliJ IDEA.app/Contents/lib/idea_rt.jar=61601:/Applications/IntelliJ IDEA.app/Contents/bin -Dfile.encoding=UTF-8 -classpath /Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/charsets.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/ext/cldrdata.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/ext/dnsns.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/ext/jaccess.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/ext/jfxrt.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/ext/localedata.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/ext/nashorn.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/ext/sunec.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/ext/sunjce_provider.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/ext/sunpkcs11.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/ext/zipfs.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/jce.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/jfr.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/jfxswt.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/jsse.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/management-agent.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/resources.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/jre/lib/rt.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/lib/ant-javafx.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/lib/dt.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/lib/javafx-mx.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/lib/jconsole.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/lib/packager.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/lib/sa-jdi.jar:/Users/sson/Library/Java/JavaVirtualMachines/corretto-1.8.0_275/Contents/Home/lib/tools.jar:/Users/sson/Dev/workspace/java8to11/target/classes:/Users/sson/.m2/repository/org/springframework/boot/spring-boot-starter/2.4.1/spring-boot-starter-2.4.1.jar:/Users/sson/.m2/repository/org/springframework/boot/spring-boot/2.4.1/spring-boot-2.4.1.jar:/Users/sson/.m2/repository/org/springframework/spring-context/5.3.2/spring-context-5.3.2.jar:/Users/sson/.m2/repository/org/springframework/spring-aop/5.3.2/spring-aop-5.3.2.jar:/Users/sson/.m2/repository/org/springframework/spring-beans/5.3.2/spring-beans-5.3.2.jar:/Users/sson/.m2/repository/org/springframework/spring-expression/5.3.2/spring-expression-5.3.2.jar:/Users/sson/.m2/repository/org/springframework/boot/spring-boot-autoconfigure/2.4.1/spring-boot-autoconfigure-2.4.1.jar:/Users/sson/.m2/repository/org/springframework/boot/spring-boot-starter-logging/2.4.1/spring-boot-starter-logging-2.4.1.jar:/Users/sson/.m2/repository/ch/qos/logback/logback-classic/1.2.3/logback-classic-1.2.3.jar:/Users/sson/.m2/repository/ch/qos/logback/logback-core/1.2.3/logback-core-1.2.3.jar:/Users/sson/.m2/repository/org/apache/logging/log4j/log4j-to-slf4j/2.13.3/log4j-to-slf4j-2.13.3.jar:/Users/sson/.m2/repository/org/apache/logging/log4j/log4j-api/2.13.3/log4j-api-2.13.3.jar:/Users/sson/.m2/repository/org/slf4j/jul-to-slf4j/1.7.30/jul-to-slf4j-1.7.30.jar:/Users/sson/.m2/repository/jakarta/annotation/jakarta.annotation-api/1.3.5/jakarta.annotation-api-1.3.5.jar:/Users/sson/.m2/repository/org/springframework/spring-core/5.3.2/spring-core-5.3.2.jar:/Users/sson/.m2/repository/org/springframework/spring-jcl/5.3.2/spring-jcl-5.3.2.jar:/Users/sson/.m2/repository/org/yaml/snakeyaml/1.27/snakeyaml-1.27.jar:/Users/sson/.m2/repository/org/slf4j/slf4j-api/1.7.30/slf4j-api-1.7.30.jar me.ssonsh.java8to11.completableFuture.App
sson : pool-1-thread-1
pool-1-thread-2
SSON

Process finished with exit code 0
```

### **여러 작업들을 조합하는 방법,**

- 기존 Future는 Callback을 할 수 있는 방법이 없기 때문에

- 비동기적 작업을 조합하여 처리하기 쉽지 않았다.

- **thenCompose(..)**

  두 Future 간의 의존성이 있는 경우

  ```java
  package me.ssonsh.java8to11.completableFuture;
  
  import java.util.concurrent.CompletableFuture;
  import java.util.concurrent.ExecutionException;
  import java.util.concurrent.ExecutorService;
  import java.util.concurrent.Executors;
  
  public class App {
      public static void main(String[] args) throws ExecutionException, InterruptedException {
  
          CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> {
              System.out.println("Hello : " + Thread.currentThread().getName());
              return "Hello";
          });
  
          // hello 작업 이후에 이어서 할 수 있게 .thenCompose()
          CompletableFuture<String> future = hello.thenCompose(App::getWorld);
  
          System.out.println(future.get());
  
      }
  
      private static CompletableFuture<String> getWorld(String message) {
          return CompletableFuture.supplyAsync(() -> {
              System.out.println("World : " + Thread.currentThread().getName());
              return message + " World";
          });
      }
  
  }
  ```

- **thenCombine(..)**

  두 Future 가 연관관계가 없는 경우

  ```java
  package me.ssonsh.java8to11.completableFuture;
  
  import java.util.concurrent.CompletableFuture;
  import java.util.concurrent.ExecutionException;
  import java.util.concurrent.ExecutorService;
  import java.util.concurrent.Executors;
  
  public class App {
      public static void main(String[] args) throws ExecutionException, InterruptedException {
  
          CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> {
              System.out.println("Hello : " + Thread.currentThread().getName());
              return "Hello";
          });
  
          CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> {
              System.out.println("World : " + Thread.currentThread().getName());
              return " World";
          });
  
          // hello의 결과와 world 의 결과가 모두 들어 왔을 때
          // 그 결과를 조합하여 어떤 행위를 진행하고자 한다.
          CompletableFuture<String> future = hello.thenCombine(world, (h, w) -> h + " " + w);
          System.out.println(future.get());
      }
  }
  ```

**allOf**

- 2개 이상의 task 들을 모두 합쳐서 처리하는 방법?
- 모든 작업을 기다렸다가 처리 하는 방식
- 이슈가 있다...
  - 모든 task들의 결과가 동일한 type이라는 보장이 없음..
  - 어떤 task는 오류가 났을 수 있다..
  - 즉, result 결과가 무의미하다.

```java
CompletableFuture.allOf(hello, world)
        .thenAccept(System.out::println);
```

결과

```java
null
```

위 이슈를 해결할 수 있게 코드를 작성하면 아래와 같다.

```java
package me.ssonsh.java8to11.completableFuture;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class App {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> {
            System.out.println("Hello : " + Thread.currentThread().getName());
            return "Hello";
        });

        CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> {
            System.out.println("World : " + Thread.currentThread().getName());
            return " World";
        });

        // 작업들을 가지고 있는 Lists
        List<CompletableFuture<String>> futures = Arrays.asList(hello, world);
        CompletableFuture[] futuresArray = futures.toArray(new CompletableFuture[futures.size()]);

        // thenApply 가 호출되는 시점에는 모든 task들의 작업이 끝났다.
        // => get() 이 호출가능하다. (checkedException) 발생
        // => uncheckedException 인 join() Tkdyd
        CompletableFuture<List<String>> results = CompletableFuture.allOf(futuresArray)
                .thenApply(v -> futures.stream().map(CompletableFuture::join).collect(Collectors.toList()));

        results.get().forEach(System.out::println);

        
    }
}
```

**anyOf**

- task 들 중 가장 먼저 작업이 끝나는 결과를 받아서 처리하는 방식
- 즉, 결과가 하나가 온다.

```java
package me.ssonsh.java8to11.completableFuture;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class App {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> {
            System.out.println("Hello : " + Thread.currentThread().getName());
            return "Hello";
        });

        CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> {
            System.out.println("World : " + Thread.currentThread().getName());
            return " World";
        });

        CompletableFuture<Void> future = CompletableFuture.anyOf(hello, world).thenAccept(System.out::println);
        future.get();

    }
}
```

------

### **예외를 처리하는 방법**

- **.exceptionally**((ex) → {});

```java
package me.ssonsh.java8to11.completableFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class App {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        boolean throwError = true;
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> {
            if(throwError){
                throw new IllegalArgumentException();
            }

            System.out.println("Hello : " + Thread.currentThread().getName());
            return "Hello";
        }).exceptionally(ex -> {
            System.out.println("ex : " + ex);
            return "ERROR";
        });

        CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> {
            System.out.println("World : " + Thread.currentThread().getName());
            return " World";
        });

        CompletableFuture<Void> future = CompletableFuture.anyOf(hello, world).thenAccept(System.out::println);
        future.get();

    }
}
```

- **.handle**

  정상 케이스와 에러 케이스 모두 처리할 수 있는 방법

  → BiFunction 으로 정상 케이스오 에러 케이스를 받아 처리한다.

  ```java
  package me.ssonsh.java8to11.completableFuture;
  
  import java.util.concurrent.CompletableFuture;
  import java.util.concurrent.ExecutionException;
  
  public class App {
      public static void main(String[] args) throws ExecutionException, InterruptedException {
  
          boolean throwError = true;
          CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> {
              if(throwError){
                  throw new IllegalArgumentException();
              }
  
              System.out.println("Hello : " + Thread.currentThread().getName());
              return "Hello";
          }).handle((result, ex) -> {
              // result : 정상
              // ex : 에러
              if(ex != null){
                  System.out.println("ERROR!");
                  return "ERROR!";
              }
              System.out.println(result);
              return result;
          });
  
          CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> {
              System.out.println("World : " + Thread.currentThread().getName());
              return " World";
          });
  
          CompletableFuture<Void> future = CompletableFuture.anyOf(hello, world).thenAccept(System.out::println);
          future.get();
  
      }
  }
  ```





* https://www.notion.so/ac23f351403741959ec248b00ea6870e