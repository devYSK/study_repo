

# Spring MVC의 기본 요청 처리 방식 - Thread Per Request Model



Thread Per Request Model이란, 1개의 Request당 1개의 쓰레드를 사용하는 웹 요청 처리 모델이다 



일반적으로 Spring Web MVC를 사용하면, 1개의 요청에 1개의 쓰레드를 사용하게 되고, Controller, Service, Repository, 도메인 모델 어디에서든 같은 쓰레드를 사용하게 된다. 

* event Loop 모델과는 다르다



1요청당 1쓰레드를 사용하기 때문에 Spring MVC에서는 ThreadLocal을 이용하여 MVC 전역에서 다양한 처리들을 할 수 있다.

- 사용자 인증정보 전파 - Spring Security에서는 ThreadLocal을 이용해서 사용자 인증 정보를 전파한다. - SecurityContext
- 트랜잭션 컨텍스트 전파 - 트랜잭션 매니저는 트랜잭션 컨텍스트를 전파하는 데 ThreadLocal을 사용한다.
- 쓰레드에 안전해야 하는 데이터 보관
- Spring MVC의 인터셉터(interceptor) 등에서 아래와 같이 클라이언트의 요청 등에서 활용



Spring mvc 방식은 다소 복잡하고 일반적으로 Blocking 방식으로 처리된다. 예를들어, 데이터를 수정하거나 가져오는 데이터베이스 호출하는 경우에 해당된다.



Spring Boot에서는 내장된 tomcat을 사용하여 WAS를 구성하는데, 이 때 WAS는 쓰레드를 효율적으로 관리하기 위해 미리 쓰레드를 생성하고 ThreadPool로 관리한다. 

* Tomcat의 기본 쓰레드 갯수는 200개이다.

<img src="https://blog.kakaocdn.net/dn/DEHUD/btrTR0kUMA5/UkXDC7p3DR61k6KkAX3Qf1/img.png" width = 700 height = 350>



### WAS가 Request를 처리하는 방법



1. WAS는 Thread를 효율적으로 관리하기 위해 Thread와 ThreadPool을 생성한다 - (Tomcat의 기본 쓰레드 갯수 200이다)

2. HTTP 요청이 들어오면 요청은 Queue에 적재되고, ThreadPool 내의 특정 Thread가 Queue에서 요청을 가져와 처리하게된다
   * 이 때, 일반적인 HTTP 요청은 처음부터 끝까지 동일한 Thread에서 처리된다
   * 레이어드 아키텍처라면 1 request = 1 thread = Controller-service-repostiory 같은 쓰레드이다
3. HTTP 요청 처리가 끝나면 Thread는 다시 ThreadPool에 반납된다.
   * 즉, WAS의 최대 동시 처리 HTTP 요청의 갯수는 ThreadPool의 갯수와 같다.



그럼 다음과 같은 의문이 생길 수 있다. 



### WAS의 성능을 늘리려면 쓰레드풀 내의 쓰레드 수를 늘리면 되지 않을까?



그러나 사실은 그렇지 않다.

Thread 갯수를 늘리면 동시 처리 갯수가 늘어나겠지만, 쓰레드 끼리의Context switching에 의한 오버헤드도 커지기 때문에 성능이 

쓰레드 갯수에 비례해서 선형적으로 증가하지는 않는다. 

오히려 많은 쓰레드 자체를 관리하는 오버헤드도 커지기 때문이다. 



### Spring mvc에서 사용하는 Thread per request 모델의 한계점

만약, Spring mvc로 구성된 서버에 여러 요청이 들어온다면, 사용가능한 Thread 수만큼 Request가 처리되며

Spring mvc는 일반적으로 Blocking 방식으로 처리한다. 이 때 Blocking IO처리를 할 때 해당 Thread는 아무것도 하지 않고 대기 상태가 된다. Thread의 대기상태(Idle)가 길게되면 많은 요청(Request)를 처리할 수 없게되고 병목이 발생할 수 있게된다.
이를 해결하기 위해 Thread pool을 늘리는 방법도 있겠으나, Core 수에 대비하여 너무 많은 Thread pool을 구성하게 되면, Context Swiching에 대한 Cost도 발생하게 되어 문제가 발생 할 수 있다.



그래서, 이러한 문제를 해결하기 위해 (Thread 의 Idle 상태를 줄이기 위해) EventLoop 방식의 Reactive Programming이 개발되었다.

* WebFlux 같은 기술은 Thread 갯수를 작은 갯수로 유지하며 HTTP  요청을 동시 처리 할 수 있도록 함

- HTTP 요청은 하나 이상의 Thread에 바인딩되어 처리될 수 있음
- [대용량-트래픽을-감당하기-위한-Spring-WebFlux-도입](https://happyer16.tistory.com/entry/)







### 참조

* https://www.youtube.com/watch?v=oDw_LHxFTeo
* https://dzone.com/articles/spring-webflux-eventloop-vs-thread-per-request-mod

* https://dev-jj.tistory.com/entry/Spring-WebFlux-EventLoop-Non-Blocking