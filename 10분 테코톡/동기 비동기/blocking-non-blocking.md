# 10분 테코톡 - 멍토의 Non-Blocking/Blocking 과 Async/Sync

목차

1. Blocking Vs Non-Blocking
2. Sychronouse Vs Asynchronous
3. 조합 4가지의 경우
4. 정리

# Blocking Vs Non-Blocking

> 제어권 : 제어권은 자신(함수)의 코드를 실행할 권리 같은 것이다. 제어권을 가진 함수는 자신의 코드를 끝까지 실행한 후, 자신을 호출한 함수에게 돌려준다.

### Blocking

 자신의 작업이 진행되다 다른 주체를 호출하면 호출된 주체의 작업을 끝날 때까지 기다렸다가 작업을 다시 시작

* 제어권을 호출한 함수에게 넘기고 호출한 함수가 일이 끝나면 제어권을 다시 돌려받음



<u>Blocking 예시</u>

직원 : 과장님 여기 작성한 서류입니다.
과장 : 다 읽을 때까지 기다리세요

직원 : (서류를 다 읽을때까지 아무것도 안하고 기다린다.)

과장 : 서류 다 읽었으니 가서 일하셔도됩니다.

직원 : 넵 (다시 일하러 간다.)



### Non-Blocking

다른 주체의 작업에 관련없이 자신의 작업을 함 

* 제어권을 호출된 주체에게 넘겨주면 바로 다시 리턴받음



<u>Non-Blocking 예시</u>

직원 : 과장님 여기 작성한 서류입니다.
과장 : 읽어볼 테니 다시 일하러 가세요.
직원 : 네 알겠습니다.(도로 돌아가 일하러 감)

# Synchronous/Asynchronous

### Synchronous(동기)

작업을 동시에 수행하거나, 동시에 끝나거나, 끝나는 동시에 시작함을 의미. 

* 함수를 호출하고 호출된 작업이 완료되면 호출한 주체가 작업을 시작함(요청이 들어온 **순서**대로 하나씩 처리함)



<u>Synchronous 예시</u>
직원 : 과장님 여기 작업한 서류입니다.
과장 : 알겠습니다.
직원 : 다 읽으셨나요? (과장이 서류를 다 읽을 때까지 기다리거나 다 읽었는지 주기적으로 물어본다.)
과장 : 다 읽었습니다. 

직원 : 넵. 바로 처리하겠습니다.

### Asynchronous(비동기)

시작, 종료가 일치하지 않으며, 끝나는 동시에 시작을 하지 않음을 의미 

함수를 호출하고 호출된 작업을 하더라도 호출자가 자신의 작업을 처리할 수 있다.(callback함수를 전달해서 작업이 완료되면 호출한 함수에게 전달)

<u>Asynchronous 예시</u>
직원 : 과장님 여기 작업한 서류입니다.
과장 : 알겠습니다.
직원 : 자신의 일을 한다. (과장이 서류를 읽든 말든 자기 일 하기 바쁨)
과장 : 다 읽었습니다.
직원 : 네 언젠간 처리하겠습니다.



동기와 비동기는 결과와 순서에 연관되어 있다.



## 동기- 비동기, 블로킹 - 논블로킹의 조합

<img src="https://blog.kakaocdn.net/dn/bhCDkb/btsb1PTN2HD/UKc0SkzzNxSP9lyookHr80/img.png" width = 900, height= 500>



### Blocking/Sync

함수를 호출하면 호출된 함수에게 제어권이 넘어가고 호출된 함수가 작업을 완료하면 호출한 함수는 바로 해당 업무를 처리한다.

**Blocking/Sync의 사용예시**

- main() 메소드에서 입력요청을 하면 입력을 할 때 까지 메서드가 작동을 멈춘다.

```java
public class BS {
	public static void main(String[] args) {
    	Scanner sc = new Scanner(System.in);
        String str = sc.nextLine();
        System.out.println(str);
    }
}
```

### NonBlocking/Sync

함수가 호출되면 호출한 함수는 자신의 일을 하면서 주기적으로 호출된 함수에게 일을 마쳤는지 물어본다. 

호출된 함수가 일을 완료하면 해당 결과를 가지고 바로 업무를 처리한다.



NonBlocking/Sync는 Blokcing - Sync와 별 차이가 없다. 



**NonBlocking/Sync 예시**

- 게임에서 맵을 읽을때 맵이 로드될때까지 기다려야 한다. 
- 특정 어플리케이션을 설치하는 동안 로딩율을 보여줘야 할 때 사용된다.
  

### Blocking/Async

함수를 호출하고 호출한 함수는 제어권을 넘겨주어 대기하고(Blocking), 

호출한 함수가 결과값을 리턴해 준다해서 바로 결과를 처리하지 않음(Async)

이런 경우는 개발자의 실수(Non-Blocking/Async이나 중간에 blocking 과정이 들어감)로 발생함



#### Non-Blocking/Async

함수를 호출한 이후에도 호출한 함수는 자기 자신의 일을 함, 이후 작업의 결과가 리턴되더라도 자신의 일을 다 끝내고나서 처리된 결과를 가지고 일을 함



Non-Blocking/Async예시

- 자바스크립트가 API 요청을 하고 다른 작업을 하다가 콜백을 통해 결과가 리턴되면 결과를 가지고 일을 하는 경우

* 스프링 웹플럭스
* 만약 작업 도중 하나라도 블로킹 작업이 있다면 그 작업은 Async-Non-Blocking으로 작동한다.



### 참조

* [멍토의 10분 테코톡](#https://www.youtube.com/watch?v=oEIoqGd-Sns&t=3s)



