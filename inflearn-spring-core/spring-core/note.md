인프런 강의
저자: 김영한  


전체 목차
1. 예제 만들기
2. 쓰레드 로컬 - ThreadLocal
3. 템플릿 메서드 패턴과 콜백 패턴
4. 프록시 패턴과 데코레이터 패턴
5. 동적 프록시 기술
6. 스프링이 지원하는 프록시
7. 빈 후처리기
8. @Aspect AOP
9. 스프링 AOP 개념
10. 스프링 AOP 구현
11. 스프링 AOP - 포인트컷
12. 스프링 AOP - 실전 예제
13. 스프링 AOP - 실무 주의사항
14. 다음으로

사용 예제 
1. 예제 만들기 - advanced
2. 쓰레드 로컬 - ThreadLocal - advanced
3. 템플릿 메서드 패턴과 콜백 패턴 - advanced
4. 프록시 패턴과 데코레이터 패턴 - proxy-start proxy
5. 동적 프록시 기술 - proxy
6. 스프링이 지원하는 프록시 - proxy
7. 빈 후처리기 - proxy
8. @Aspect AOP - proxy
9. 스프링 AOP 개념 - 없음
10. 스프링 AOP 구현 - aop
11. 스프링 AOP - 포인트컷 - aop
12. 스프링 AOP - 실전 예제 - aop13. 스프링 AOP - 실무 주의사항 - aop
13. 다음으로           


# 1. 예제 만들기
예제 프로젝트 만들기 - V0  
학습을 위한 간단한 예제 프로젝트를 만들어보자.  
상품을 주문하는 프로세스로 가정하고, 일반적인 웹 애플리케이션에서 Controller Service
Repository로 이어지는 흐름을 최대한 단순하게 만든다.

## 로그 추적기 - 요구사항 분석 

> 여러분이 새로운 회사에 입사했는데, 수 년간 운영중인 거대한 프로젝트에 투입되었다. 
 전체 소스 코드는 수 십만 라인이고, 클래스 수도 수 백개 이상이다.  
여러분에게 처음 맡겨진 요구사항은 로그 추적기를 만드는 것이다.  
애플리케이션이 커지면서 점점 모니터링과 운영이 중요해지는 단계이다.  
특히 최근 자주 병목이 발생하고 있다.   
어떤 부분에서 병목이 발생하는지, 그리고 어떤 부분에서 예외가 발생하는지를 로그를 통해 확인하는 것이 점점 중요해지고 있다.  
기존에는 개발자가 문제가 발생한 다음에 관련 부분을 어렵게 찾아서 로그를 하나하나 직접 만들어서 남겼다.  
로그를 미리 남겨둔다면 이런 부분을 손쉽게 찾을 수 있을 것이다.   
이 부분을 개선하고 자동화 하는 것이 여러분의 미션이다.

## 요구사항

* 모든 PUBLIC 메서드의 호출과 응답 정보를 로그로 출력
* 애플리케이션의 흐름을 변경하면 안됨
  * 로그를 남긴다고 해서 비즈니스 로직의 동작에 영향을 주면 안됨
* 메서드 호출에 걸린 시간
* 정상 흐름과 예외 흐름 구분
    * 예외 발생시 예외 정보가 남아야 함
* 메서드 호출의 깊이 표현
* HTTP 요청을 구분
  * HTTP 요청 단위로 특정 ID를 남겨서 어떤 HTTP 요청에서 시작된 것인지 명확하게 구분이 가능해야 함
  * 트랜잭션 ID (DB 트랜잭션X), 여기서는 하나의 HTTP 요청이 시작해서 끝날 때 까지를 하나의 트랜잭션이라 함

* ### 예시
```
정상 요청
[796bccd9] OrderController.request()
[796bccd9] |-->OrderService.orderItem()
[796bccd9] | |-->OrderRepository.save()
[796bccd9] | |<--OrderRepository.save() time=1004ms
[796bccd9] |<--OrderService.orderItem() time=1014ms
[796bccd9] OrderController.request() time=1016ms
예외 발생
[b7119f27] OrderController.request()
[b7119f27] |-->OrderService.orderItem()
[b7119f27] | |-->OrderRepository.save()
[b7119f27] | |<X-OrderRepository.save() time=0ms
ex=java.lang.IllegalStateException: 예외 발생!
[b7119f27] |<X-OrderService.orderItem() time=10ms
ex=java.lang.IllegalStateException: 예외 발생!
[b7119f27] OrderController.request() time=11ms
ex=java.lang.IllegalStateException: 예외 발생!
```

## 로그추적기 V1 - 프로토타입 개발 

* 로그 추적기를 위한 기반 데이터를 가지고 있는 `TraceId`, `TraceStaus` 클래스

* TraceId 클래스
  * 로그 추적기는 트랜잭션ID와 깊이를 표현하는 방법이 필요하다.
  * 여기서는 트랜잭션ID와 깊이를 표현하는 level을 묶어서 TraceId 라는 개념을 만들었다

* TraceStatus 클래스 
  * 로그를 시작할 때의 상태 정보를 가지고 있다. 이 상태 정보는 로그를 종료할 때 사용된다.
    * traceId : 내부에 트랜잭션ID와 level을 가지고 있다.

    * startTimeMs : 로그 시작시간이다. 로그 종료시 이 시작 시간을 기준으로 시작~종료까지 전체 수행
시간을 구할 수 있다. 
    * message : 시작시 사용한 메시지이다. 이후 로그 종료시에도 이 메시지를 사용해서 출력한다




























