# Graceful Shutdown

> Graceful Shutdown의 사전적 의미 : 정상적인 종료



**Graceful shutdown은 프로그램이 종료될 때 최대한 side effect를 내지 않기 위해 로직들을 잘 처리하고 정상적으로 종료하는 것을 의미한다.**

* 운영체제(OS), 애플리케이션, 브라우저 등 모든 소프트웨어에 통틀어서 말할 수 있는 개념이다. 

Graceful Shutdown의 상반되는 의미로 Hard Shutdown이 있다.

> Gracueful Shutdown(정상적인 종료) : 사용자가 소프트웨어 기능을 이용하여 정상적으로 프로세스를 종료시키고 종료하는것
>
> <-> 
>
> Hard Shutdown(강제 종료, 비정상 종료) : 천재지변, 전원 차단(코드뽑기), 하드웨어 오류(메모리부족 등)에 의해 의도치 않게 비정상 종료되는 것



왜 Graceful Shudown이 중요한가를 생각해보자

* `웹 애플리케이션의 경우, 기존 요청이 진행중인데 비정상적으로 종료된다면 사용자는 502 와 같은 에러를 받아 좋은 경험을 하지 못할것이며  DB나, 리소스 데이터의 손실과 비즈니스 로직이 제대로 처리되지 않는 문제가 생길 수 있다.`
  * 예를들어 돈과 관련된 결제 처리중인 애플리케이션이 처리중에 죽어버린다면 ...? 
* 운영체제의 경우 프로그램 및 운영 체제 파일의 데이터 손상이 발생할 수 있다. 



지속가능한 SW와 SW의 안전성을 높이기 위하여 graceful shutdown이 필요하다. 프로세스가 갑작스러운 하드웨어 문제에 의해 죽는 상황이 발생하더라도 문제가 없는 견고한 프로그램을 만들수 있도록 노력해야 한다. 



그렇다면 애플리케이션의 경우 graceful하게 종료하지 못하고 shutdown이 될 수 있는 경우가 무엇이 있을까?

* 천재지변, 서버폭발, 해킹
* 메모리부족
* 개발자나 관리자의 강제 종료
* 애플리케이션 배포



천재지변, 서버폭발, 해킹 등은 개발자가 언제 일어날 지 알 수 없다. 최대한의 대비를 해놓아야 한다. 

해킹, 메모리 부족은 개발자가 프로그램을 잘 설계하고 잘 구현하면 어느정도 예방할 수 있다. 

개발자나 관리자의 강제 종료는 강제 종료하지 않고 정상적으로 종료하도록 프로세스를 구축하면 된다. 



그러나 애플리케이션 배포 같은 경우 강제로 종료시키지 않고  graceful shudown 방식을 통해 정상적으로 종료되게 할 수 있다.

 `kill -9` 같은 명령어같은 행동을 통한 애플리케이션을 강제로 종료시키지 않고, 

kill -15 (SIGTERM)을 이용하여 자바 애플리케이션이 종료될 때 로직들이 잘 처리되고 마지막 요청을 처리하고 종료되도록 한다. 



Java에서 graceful하게 애플리케이션을 종료시키기 위해 다음 두가지의 방법이 있다. 

1. Java의 JVM Shutdown Hook 구현
2. Spring boot에서의 Gradeful Shudown 구현

> kill -9로 종료하는 것은 좋지않다. jvm shutdown hook 또는 Spring의 @PreDestroy의 실행을 보장하기 힘들다.
>
> kill -2(SIGINT) 혹은 kill -15(SIGTERM)을 쓰는 것이 좋다. (-9 옵션을 주지않고 그냥 kill 해도 kill -15와 같은 옵션이다.)





## Kill 명령어와 프로세스 종료

먼저, 프로세스를 종료시키기 위해 프로세스로 전달되는 시그널(신호)이 있다.   
시그널이 전달되면 시스템은 다음과 같이 동작한다.

1. 시그널에대한 핸들러는 커널에 프로그래밍 되어 있으며 프로세스가 시그널을 받게 되면 시그널에 해당하는 bit를 마킹한다.
2. 다음 명령어가 진행될 때 마킹된 bit를 확인 후 커널에게 제어를 넘긴다. (Context Switching 발생)
3. 벡터를 통해 적절한 시그널 핸들러를 찾아 핸들러를 실행시킨다.

프로세스를 종료 시키는 다음과 같은 시그널들이 있다.

> graceful shutdown을 위해서 필요한 SIGKILL과 SIGTERM, SIGINT 만 중심적으로 정리하였다.



- **SIGKILL** : 프로세스를 즉시 종료. 이 신호는 처리(감지), 무시 또는 차단할 수 없다. ("kill -9" 명령은 동일한 신호를 생성한다).
  - kill -9 {Process Id}
- **SIGTERM** : 프로세스를 즉시 종료시키지만, 종료 시키기전에 해당 시그널을 핸들링 할 수 있다
  - 만약 프로세스가 해당 시그널을 핸들링하는 코드를 작성하지 않았다면 즉시 종료시킨다. 
  - graceful shutdown을 위해서라면 해당 시그널을 사용하면 된다. kill 명령어에 어떠한 옵션인자도 주어지지 않는다면 **SIGTERM**을 프로세스로 전달하게 된다.
  - `kill {Process Id} - (kill -15 {Process Id})`
- **SIGINT** :  **SIGTERM**과 동일하지만 시그널을 보내기 위한 트리거를 키보드로 부터 받는다. CTRL + C를 입력하여 프로세스를 종료시킬때 **SIGINT**가 전달된다.
- SIGQUIT, SIGSTP, SIGHUP

> http://programmergamer.blogspot.com/2013/05/clarification-on-sigint-sigterm-sigkill.html 에 자세히 나와있다. 

**주요 시그널**

| 시그널      | 영어      | 설명                        |
| ----------- | --------- | --------------------------- |
| 2) SIGINT   | Interrupt | Ctrl + C, 종료 요청 시그널  |
| 9) SIGKILL  | Kill      | 강제 종료 시그널            |
| 15) SIGTERM | Terminate | `기본 값`, 종료 요청 시그널 |

> SIGTERM / SIGINT vs SIGKILL
>
> SIGKILL은 프로세스를 그냥 kill 한다. catch하고 핸들링이 불가능하다.
>
> 들어온 요청을 다 처리하고 종료한다든가, db에 유저가 작성중이던 텍스트 데이터를 저장한다든가 같은 
>
> 로직을 처리하는 것이 불가능하다. SIGKILL에 대한 시그널 핸들러는 만들 수 없다.  
> 
>
> 반면 SIGTERM, SIGINT와 같이 특정 시그널을 catch 할 수 있다는 것은, 시그널 핸들러를 제작할 수 있다는 뜻이기도 하지만 동시에 무시할 수 있다는 뜻이기도 하다. 
>
> \- ctrl + z 의 경우 SIGSTOP 시그널을 보내는데, 원래라면 프로세스를 정지하는 것이지만 SIGSTOP을 받았을 때 종료되는 형태로 구현된 프로그램들도 종종 있다.



kill -9 $PID(`SIGKILL`)는 강제종료이다. -9인 SIGKILL은 리소스를 정리하는 핸들러를 지정하지 않고 프로세스를 바로 죽인다. 

실행 중인 쓰레드가 있더라도 이를 무시하고 종료하는데 혹시라도 중요한 작업중이라면 최악의 상황이 일어날 수 있으므로 `SIGKILL로 배포시나 재실행시에 프로세스(혹은 애플리케이션)를 죽이는것은 절대 하지 않는것이 좋다.` 



그러므로 kill -9 대신 kill -15 (SIGTERM)을 이용하여 종료해야 한다.

```
-9: 작업중인 모든 데이터를 저장하지 않고 프로세스를 종료하기 때문에 저장되지 않는 데이터는 소멸된다. (강제종료)
-15: 하던 작업을 모두 안전하게 저장한 후 프로세스를 종료한다. (정상종료)
```



# Java의 JVM Shutdown Hook

JVM은 다음과 같은 경우 **비정상 종료 절차**를 밟게 된다. - Shutdown hook을 실행하지 않는다. 

* kill -9 { jvm process id } 같은 OS에서의 종료 신호
* Java 코드에서 Runtime.getRuntime().halt() 호출
  * System.exit(int code)도 내부적으로 Runtime.getRuntime().halt()을 사용하며, code가 0이 아닌 경우 비정상 종료
* 정전, OS패닉 같은 호스트 OS가 의도치않게 강제 종료시 



JVM은 다음과 같은 경우 **정상적인 종료 절차**를 밟게 된다. - 이 때 Shutdown hook을 실행한다. 

- 데몬 스레드가 아닌 일반 스레드가 '모두' 종료되는 시점
- System.exit(0) 메서드가 호출 될 경우 -> code는 0이여야 한다.
  - System.exit(int code)도 내부적으로 Runtime.getRuntime().halt()을 사용하며, code가 0이 아닌 경우 비정상 종료
- 프로세스가 종료 시그널을 받게 된 경우
  - Ctrl + C, 또는 SIGINT, SIGTERM (kill -15)



정상적인 종료인 상황을 통해 JVM이 종료된다면 JVM은 가장 먼저 등록되어있는 모든 Shutdown hook을 실행시킨다.

> shoutdown hook : JVM이 종료하기 직전 등록된 작업들을 처리하는 기능.  일반적으로 리소스를 해제하거나 기타 유사한 작업들을 한다. 



Shutdown hook은 기본적으로 초기화되어있지만 시작되지 않은 스레드다.하나의 JVM에 여러 개의 shutdown-hook을 등록 할 수  있다.  JVM이 종료 프로세스를 시작하면 등록된 모든 hook가 지정되지 않은 순서(random하게)로 시작되므로  thread-safe하게 코드를 작성해야한다. 



모든 후크를 실행한 후 JVM이 중지된다.

### shutdown hook 사용시 주의할 점



*  shutdown hook은 최대한 짧게 작성되어야 한다.
  * 만약 프로세스의 종료가 머신의 종료에 의한 것이라면 JVM은 프로세스가 완전히 종료되는 것을 기다리지 않고, 일정 시간이 지난 후 종료한다. 이 때문에 shutdown hook이 실행되기 전에 종료될 수 도 있고, shutdown hook이 실행되는 도중에 종료될 수도 있다.
* shutdown hook은 반드시 실행되는 것이 아닌 정상적인 종료에서만 호출된다. 비정상적 종료에서는 호출되지 않는다. 

* JVM에서 종료 절차가 시작됐는데 어플리케이션에서 사용하던 스레드가 계속해서 동작 중이라면 종료 절차가 진행되는 과정 내내 기존의 스레드도 계속해서 실행되기도 한다
* JVM은 종료 과정에서 계속해서 실행되고 있는 어플리케이션 내부의 스레드에 대해 중단 절차를 진행하거나 인터럽트를 걸지 않으므로. 계속해서 실행되던 스레드는 결국 종료 절차가 끝나는 시점에 `강제로 종료된다`
* 결국 이렇게 되면 shutdown hook 작업을 하지 않게되며 dead lock이나 hang에 걸릴 수 있으므로 안전하게 개발해야 한다. 



## Java에서 shutdown hook 추가하기

shutdown hook을 추가하기 위해 *[Runtime.getRuntime().addShutdownHook(Thread hook)](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/Runtime.html#addShutdownHook(java.lang.Thread))* 메서드를 사용할 수 있다.



java에서 graceful shutdown을 구현하기 위해서는 thread를 이용해야 한다.

```java
public class ShutdownHook {

    public static void main(String[] args) {
        Thread printingHook = new Thread(() -> System.out.println("call shutdown hook"));
        Runtime.getRuntime().addShutdownHook(printingHook);

        System.out.println("END");
    }
}
```

> 결과
>
> END
>
> call shutdown hook

프로그램이 정상 종료 된 후에 hook thread가 실행 된다.

runtime(Unchecked) 예외가 발생하더라도 hook thread의 내용은 실행된다.

Thread.sleep()을 주고 콘솔에서 인터럽트 (Ctrl + C)를 줄 경우에도 hook thread는 정상적으로 실행된다.

하지만 **만약 main문 밖까지 CheckedException이 발생한다면  비정상적으로 종료된것으로 간주하므로 hook은 실행되지 않는다. **



### Shutdown Hook 제거하기

[Runtime.getRuntime().removeShutdownHook(Thread hook))](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/Runtime.html#removeShutdownHook(java.lang.Thread))  메소드를 사용해서 Thread Hook 인스턴스를 넘기면 hook을 제거할 수 있다. 정상적으로 제거되면 true를 반환한다. 





# Spring Boot Gradeful Shudown 구현

SpringBoot `graceful shutdown`은 애플리케이션 서버 종료시 새로운 요청은 받지 않고 기존 요청을 완전히 처리한 이후 서버를 종료한다. 

> 애플리케이션에서 변경 사항이 있으면, 애플리케이션을 재시작 해야 변경사항이 반영된다.
> 배포 방식에 따라 Blue-Green 배포 형식을 따르게 되면, 이전 버전 앱을 굳이 죽일 필요는 없지만,
> Rolling 배포를 따르는 경우, 순차적으로 구버전의 앱을 죽이고, 신규 버전 앱을 띄어야 한다.
>
> 물론 애플리케이션 윗단에서, 종료 시그널이 오면 더이상 요청을 받지 않고, 이미 와 있는 요청들만
> 처리 후 앱을 종료하면 되지만(AWS Beanstalk등), 앱 자체에서도 정상 종료 프로세스는 꼭 필요하다.
>
> 그러므로 배포시에도 kill -15 (SIGTERM)을 이용하여 종료해야 한다.



Graceful Shutdown 은 Spring Boot 버전에 따라 적용하는 방식이 상이한데,

 `Spring Boot 2.3` 부터는 간단하게 적용할 수 있다. 

또한 Tomcat, Jetty, Undertow, Netty 모두에 대한 정상적인 종료 기능을 지원한다고 한다.

* tomcat, Netty 및 Jetty는 네트워크 계층에서 새 요청 수락을 중지하지만, Undertow는 계속해서 새로운 요청을 수락하지만 즉시 503 Service Unavailable 응답을 클라이언트에 보낸다.

### 설정 방법 - 설정파일 사용 yml, properties 

서블릿 기반 MVC와 reactive stream 기반 webflux에서도 동작한다.

* enum으로 spring.boot.web.server.Shutdown 패키지에 GRACEFUL, IMMEDIATE 옵션이 존재한다. 

셧다운 타임아웃 설정도 추가할 수 있다. - spring.lifecycle.timeout-per-shutdown-phase

* 기본값은 30초이다. 

* 1분은 1m으로 설정할 수도 있다.

단, 요청을 처리하는 시간보다 셧다운 타임아웃으로 설정한 시간이 짧을 경우 클라이언트는 응답을 받을 수 없다.

이 경우 graceful shutdown을 적용하는 게 의미가 없어지니 적절한 시간으로 설정할 필요가 있다.

### application.properties

```properties
server.shutdown= graceful

spring.lifecycle.timeout-per-shutdown-phase= 10s
```

### application.yml

```yaml
server:
  shutdown: graceful

spring:
  lifecycle:
    timeout-per-shutdown-phase: 10s
```





Spring Context는 종료 시점에 사용하던 bean들을 정리하는 등의 Context를 정리하는 코드를 shutdown-hook으로 추가한다.

* **AbstractApplicationContext 클래스**에서 registerShutdownHook() 메소드를 통해 Shutdown hook을 등록한다. 
  * Shutdown hook은 doClose() 메소드를 수행한다



## Spring Boot 2.2 이하에서 구현

Graceful Shutdown Event Listener

```java
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class GracefulShutdownEventListener implements ApplicationListener<ContextClosedEvent> {

    private final GracefulShutdownTomcatConnector gracefulShutdownTomcatConnector;

    public GracefulShutdownEventListener(GracefulShutdownTomcatConnector gracefulShutdownTomcatConnector) {
        this.gracefulShutdownTomcatConnector = gracefulShutdownTomcatConnector;
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        gracefulShutdownTomcatConnector.getConnector().pause();

        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) gracefulShutdownTomcatConnector.getConnector()
          .getProtocolHandler()
          .getExecutor();
        
        // 이 시점부터 새로운 요청이 거부된다. 클라이언트는 503 Service Unavailable 응답을 수신한다.
        threadPoolExecutor.shutdown();

        try { // 이부분에서 처리를 완료하면 된다. 
            // 이 시점에 기존 처리 중인 요청에 대한 응답을 완료한다.
            threadPoolExecutor.awaitTermination(20, TimeUnit.SECONDS);
            log.info("Web Application Gracefully Stopped."); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
            log.error("Web Application Graceful Shutdown Failed.");
        }
    }

}  
```

`ContextClosedEvent`은 스프링의 애플리케이션 컨텍스트가 종료될 때 발생하는 이벤트이다. 애플리케이션을 구동 중인 **JVM**에 종료 시그널(**kill** 명령)이 전달되었을 때가 바로 **ContextClosedEvent**에 해당한다. **ContextClosedEvent**이 특히 중요한 것은 **Spirng Boot**에서의 **Graceful Shutdown** 처리에 있어 중요한 이벤트 발생 지점이기 때문이다.

<br>

GracefulShutdownTomcatConnector 

```java
import org.apache.catalina.connector.Connector;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.stereotype.Component;

@Component
public class GracefulShutdownTomcatConnector implements TomcatConnectorCustomizer {

    private volatile Connector connector;

    @Override
    public void customize(Connector connector) {
        this.connector = connector;
    }

    public Connector getConnector() {
        return connector;
    }

}  
```

Config에 등록

```java
@RequiredArgumentsConstuctor
@Configuration
public class Config {

    private final GracefulShutdownTomcatConnector gracefulShutdownTomcatConnector;

    @Bean
    public ConfigurableServletWebServerFactory webServerFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.addConnectorCustomizers(gracefulShutdownTomcatConnector);

        return factory;
    }
}
```





### Undertow 기반 SpringBoot에서  Graceful Shutdown 하는법

* https://jsonobject.tistory.com/460



### 참조

* https://2kindsofcs.tistory.com/53
* https://www.baeldung.com/jvm-shutdown-hooks
* https://knowjea.github.io/%EA%B0%9C%EB%B0%9C/2018/07/31/add-shutdown-hook.html#disqus_thread
* https://www.geeksforgeeks.org/jvm-shutdown-hook-java/
* https://www.baeldung.com/java-runtime-halt-vs-system-exit
* https://www.baeldung.com/spring-boot-graceful-shutdown
* https://www.springcloud.io/post/2022-02/spring-boot-graceful-shutdown/#gsc.tab=0
* https://bkjeon1614.tistory.com/729