# Apache JMeter

[JMeter 깃허브](https://github.com/apache/jmeter)

웹 어플리케이션 성능 테스트를 툴은 자바 오픈 소스 Apache Bench, Apache JMeter, 네이버에서 Grinder를 이용해서 만든 nGrinder, ,k6, Artillery, Gatling 등등이 있습니다.

*  [JMeter와 k6를 비교한 글](https://k6.io/blog/k6-vs-jmeter/)

Apache JMeter는 서버의 **성능 및 부하를 측정할 수 있는 오픈 소스 테스트 도구**입니다. 

JMeter는 순수 Java 애플리케이션 오픈소스이며 서버나 네트워크 또는 개체에 대해 과부하를 시뮬레이션하여 강도를 테스트하거나 다양한 부하 유형에서 전체 성능을 분석하는 데 사용할 수 있습니다.

 

다음은 Apache JMeter가 가진 특징입니다

- 다양한 프로토콜/서버를 테스트할 수 있다.
  - 웹 - HTTP, HTTPS
  - SOAP / REST 웹 서비스
  - FTP
  - 데이터베이스 (JDBC 사용)
  - Mail (SMTP, POP3, IMAP)
  - JMS - Message-oriented middleware (MOM)
  - TCP
  - Java Objects
- CLI 지원
  - CI 또는 CD 툴과 연동할 때 편리하다.
  - UI를 사용하는 것보다 메모리 등 시스템 리소스를 적게 사용한다.
- Swing으로 제작한 GUI를 지원
- 시나리오 기반 테스트가 가능하다.
- JMeter에서 측정한 지표와 값을 HTML로 작성된 DashBoard로 출력이 가능하다.
- 다양한 외부 플러그인을 사용하여 기능 확장이 가능하다.



JMeter의 타 도구 대비 장점은 아래의 세 가지로 요약됩니다.

- 설치, 사용하기 쉬움
- 커뮤니티, 레퍼런스 방대하고 최근까지도 유지보수 진행됨
- 분산환경 지원
- 스크립트 레코딩
- HTML 형식의 보고서가 풍부한 정보를 제공해줌
- CLI로 실행 가능.

단점은

* 분산환경 설치 가 쉽지 않음
* 프록시 서버 기반 스크립트 레코딩이 불편하다.

* 기능이 엄청 많다.

## JMeter 주요 개념 및 용어 

JMeter를 사용하기 전에 알아야 하는 개념입니다.

> Thread Group: 몇 개의 쓰레드가 동시에 요청을 보내는 지
>
> Sampler: 어떤 유저가 해야 하는 액션
>
> Listener: 응답을 받았을 때 어떤 동작을 취하는 지 (검증, 리포트, 그래프 그리기 등)
>
> Configuration: Sampler 또는 Listener가 사용할 설정 값 (쿠키, JDBC 커넥션 등)
>
> Assertion: 응답 결과의 성공 여부를 판단하는 조건 (응답 코드, 본문 내용 등)  

  


1. ﻿﻿﻿Active User : 실제 서버에 연결된 상태로 요청을 처리 중인 사용자
2. ﻿﻿﻿Inactive User : 웹브라우저에 결과 화면이 출력된 상태에서 화면의 내용을 읽거나 정보를 입력하고 있는 사용자
3. ﻿﻿﻿Concurrent User : 특정 시점에 시스템에 접속하여 사용하고 있는 사용자
   * Concurrent User = Active User + Inactive User

4. Virtual User : 가상 사용자 수, Apache JMeter에서는 Thread 수로 표현하기도 함

5. ﻿﻿﻿Response Time/Load Time : 응답시간 또는 처리시간, 요청을 보낸 후 응답이 완료되어 사용자 화면에 출력될 때까지의 시간

6. ﻿﻿﻿Latency : 요청을 보낸 후 데이터를 받기 시작할 때까지 시간

7. ﻿﻿﻿Think Time : 하나의 요청에 응답을 수신하고 다음 요청을 보낼 때까지 시간

8. ﻿﻿﻿Request Interval Time : 요청을 보낸 후 다음 요청을 보낼 때까지 시간

9. Ramp-Up Period : Thread 생성에 걸리는 시간

10. ﻿﻿﻿﻿Transaction : 업무 처리의 단위. 화면 조작 및 응답을 트랜잭션으로 정의

11. ﻿﻿﻿﻿Throughput : 단위 시간당 대상 서버에서 처리되는 요청의 수, 
    * Apache JMeter에서는 시간 단위를 보통 TPS(Transaction Per Second)로 표현하며 TPM(Transaction Per Minute), TPH(Transaction Per Hour) 등이 있음 있음 있음 

#### 테스트 플랜 항목 

**1. Thread Group**

* Thread Group은 모든 테스트 플랜 작성의 시작 지점으로 Thread Group 아래에 모든 컨트롤러와 샘플러가 위치한다. 
* Thread Group에는 실행하는 스레드 수, 램프업 시간, 테스트 수행 시간을 지정한다. 
* 하나의 테스트 플랜에는 여러개의 Thread Group을 지정할 수 있다.

**2. Samplers**

* 샘플러는 JMeter가 대상 시스템에 요청해야 하는 정보를 설정한다. 
* 예를 들어 HTTP 프로토콜을 이용해서 테스트한다면 "HTTP Request Sampler"를 추가하고 여기에 연결한 URL 정보와 파라미터 값을 설정한다. 
* 각각의 샘플러는 해당 프로토콜에 맞는 속성값들을 저으이하게 되어 있으며 테스트시 해당 프로토콜에 대한 이해없이는 설정이 거의 불가능 하다.

**3. Logical Controllers**

* Logical Controller는 JMeter가 언제 서버에 요청을 전달할지를 결정한다. 
* 전체 부하 테스트 중에서 로그인은 한번만 하는 경우, 혹은 HTTP URL 이 2개이고 해당 요청의 순서가 존재한다면 "HTTP Request Sampler"를 2개 등록하고 Logical Controller로 2개의 상관 관계를 정의할 수 있다.

**4. Listener**

* JMeter를 통해 테스트하는 결과 정보 및 진행 상태 정보를 표시한다. 
* 일반적으로 [Graph Result]를 이용해서 진행되는 추이를 그래픽하게 확인한다. 또한 수집된 정보는 XML 혹은 CSV로 저장이 가능하다.

**5. Timers**

* JMeter의 테스트 플랜에 샘플러를 등록하면 순차적으로 진행이 되지만 현실 세계에서 하나의 사용자가 요청을 순차적으로 매우 빠른 시간내에 수행하는 것은 불가능하다. 
* 이처럼 요청과 요청 사이에 특정한 시간 간격을 두려면 Timers를 이용해서 설정할 수 있다.

**6. Assertion**

* JMeter의 HTTP 프로토콜을 이용해서 성능 테스트를 할 경우 요청별 성공/실패 여부는 HTTP 응답 코드의 값을 이용해서 판단한다. 
* HTTP 응답 코드가 200이면 성공을, 그외에 다른 코드 값은 실패로 규약되어 있다.
*  JMeter에서도 이를 그대로 사용하며 200번 코드가 리턴되면 테스트는 성공으로 인식한다.

하지만 업무적으로 200번 코드가 리턴되더라도 실패로 판단해야 하는 경우도 많이 있다. 이 경우 Assertion를 이용해서 응답 정보에 특정한 메시지를 필터링해서 성공/실패 여부를 판단할 수 있다.

**7. Configuration Elements**

* Configuration Elements는 샘플러와 밀접한 관련이 있다. 비록 직접적으로 요청을 수해하지는 않지만 샘플러의 요청 정보를 관리할 수 있다. 

* 예를 들어 테스트 플랜이 복잡해서 HTTP Request Sampler 작성을 많이 해야 하는데, 서버 IP나 포트 등 공통적으로 많이 사용되는 부분이 있다면 Configuration Elements의 "HTTP Request Defaults"에 설정하면 된다. 그러면 해당 설정 정보가 관련된 HTTP Request에 모두 적용된다.

**8. Pre-Processor Elements**

* 샘플러를 실행하기 전에 수행해야 할 내용을 정의한다. 예를 들어 요청을 하기 전에 파라미터 값을 초기화하는 등의 작업에 사용한다.

**9. Post-Processor Elements**

* 샘플러를 실행한 후에 수행해야 할 내용을 정의한다.

 

위 항목들은 JMeter 실행시 우선순위가 존재하는데, 다음과 같습니다.

Configuration -> Pre-Processor -> Timer -> Sampler -> Post-Processor -> Assertions -> Listener

 

위 순서가 중요한 이유는, JMeter가 성능 테스트를 수행할 때 작성된 테스트 플랜과 Thread Group에 정의 되어 있는 내용중 위의 순서 규칙을 참조해서 동작하기 때문입니다. 이 내용을 이해하지 못하면, 실제 테스트 시에도 정확한 결과를 얻지 못할 수 있습니다.



# JMeter 설치 방법

1. [JMeter 설치 페이지 접속](https://jmeter.apache.org/download_jmeter.cgi) 후 다운로드

2. Mac 의 경우 brew로 설치 가능

```shell
# 설치
brew install jmeter —-with-plugins 
# or
brew install jmeter
---
# 실행
open /usr/local/bin/jmeter
# or
jmeter

# 폴더 오픈
open /opt/homebrew/Cellar/jmeter
```

* 맥에선 sh파일로, 윈도우에선 bat파일을 실행하면 됩니다.

한글메뉴로 보고 싶으면 맥기준 Option -> Choose -> Language Korean 을 선택합니다.



**플러그인 설치 방법**

* 플러그인 검색 사이트 : https://jmeter-plugins.org/

* 플러그인 매니저 설치를 먼저 해야한다.
  * https://jmeter-plugins.org/wiki/PluginsManager/ 사이트 접속
  * Dowload the Plugins Manager `Jar file` < 링크 클릭 [2023.4.24 현재 링크](https://jmeter-plugins.org/get/ )
    * jmeter-plugins-manager-1.8.jar 라는 파일 다운로드
  * jmeter-plugins-manager-1.8.jar 파일을 /lib 아래에 둬야 한다
    * homebrew 설치 맥 기준 : /opt/homebrew/Cellar/jmeter/5.5/libexec/lib
* 다음 Jmeter 재시작

<img src="https://blog.kakaocdn.net/dn/wfl8K/btscvZgZA9I/9WGjuu2ljz68emxotMV3g1/img.png" width= 900 height= 300>

* 우측 상단 플러그인 매니저를 클릭하여 원하는 플러그인을 설치한다. 
* 메뉴 Option 에서도 볼 수 있다. 

원하는 플러그인을 체크한 뒤 `Apply Changes and Restart JMeter`버튼을 누릅니다

매니저가 플러그인을 마치면 재시동되고 설치한 플러그인을 정상적으로 활용할 수 있습니다



* **brew로 jmeter를 실행시키다가 test-plan이 저장되지 않는 문제가  생긴다면?
  * https://stackoverflow.com/questions/57190107/why-do-i-get-a-noclassdeffound-error-when-i-try-to-save-my-test-plan
  * 같은 문제가 발생하시는 분들은 darcular가 아닌 다른 테마를 선택하시면 해결됩니다. 

### JMeter 한글이 깨진다면?

bin/jmeter.properties 열어서 아래값 추가

```
# The encoding to be used if none is provided (default ISO-8859-1)
#sampleresult.default.encoding=ISO-8859-1
sampleresult.default.encoding=UTF-8 # <<< 이부분 추가 
```

## 테스트 만들기

**테스트 전 유의사항**

테스트 하는 웹 어플리케이션 서버와 테스트를 돌리는 서버는 서로 달라야 합니다.

JMeter를 돌리는 서버와 웹 어플리케이션 서버가 같으면 같은 **메모리**를 사용하기 때문에 정확한 값을 측정할 수 없습니다.



1. 쓰레드 그룹 생성 

   * 테스트 계획 (Test plan) 우클릭 -> add(추가) -> Threads(Users)  -> Thread Group 클릭 

     - `Number of Threads`: 몇 개의 쓰레드(유저 수)로 테스트할 지

     - `Ramp-up period`: {Number of Thread} 만큼의 쓰레드를 몇초에 걸쳐서 만들 지

     - `Loop Count`: 요청을 몇번을 반복할 지 (설정된 값에 따라 **Number of Threads X Ramp-up period** 만큼 요청을 다시 보낸다.)

     - `Action to be taken after a Sample error` 는 에러 처리가 되었을 때 취할 액션이다. 이는 Assertion의 결과로 판단한다.

2. **Sampler** 정의 : 우리가 정의했던 각각의 유저가 해야 할 일을 Sampler에서 정의한다.

   * 1에서 만든 Thread Group 우클릭 -> Add -> Sampler -> HTTP Request 클릭
   * 부하를 줄 url 을 지정
     * `Name` : 요청 이름
     * `Protocol`  : 사용할 프로토콜(http 혹은 https)을 선택합니다. 지정하지 않으면 http로 실행됩니다.
     * `Server Name of IP`  :서버의 ip주소를 입력합니다. 로컬의 경우 localhost or 127.0.0.1을 입력합니다.
     * `Port Number` : 사용하는 포트를 입력합니다
     * `Path` : API Endpoint를 입력해줍니다
   * http://localhost:8080/api/users/me 라면?
     * <img src="https://blog.kakaocdn.net/dn/dLW5kW/btsbXJ8ggra/iVLJdIv02UlLzkEaI6CY5K/img.png" width = 800 height = 300>
   * 한글 인코딩 UTF-8 
     * 만약 한글이 깨진다면?
     * bin / jmeter.properties 에 sampleresult.default.encoding=UTF-8 값 추가

   ```
   #The encoding to be used if none is provided (default ISO-8859-1)
   #sampleresult.default.encoding=ISO-8859-1
   sampleresult.default.encoding=UTF-8 # << 이부분을 추가한다 
   ```

   * 헤더를 추가하려면 - Http Header Manager 추가
     * 요청 우클릭 - > Add -> Config Element -> HTTP Header Manager 
     * <img src="https://blog.kakaocdn.net/dn/bde0z2/btscjiV1boy/Kbp19Aj4s2Ovq44nRh0vqk/img.png" width= 800 height = 300>
     * 헤더가 필요할 경우 추가해줍니다. name , value로 추가할 수 있습니다 
     * key-value 형식으로 `Authorization` 정보나 `Content-Type` 등 필요한 헤더를 추가합니다.
     * 마찬가지로, 쿠키도 가능합니다.
     * 각 요청의 depth마다 쿠키 사용, 미사용도 가능합니다. 



3. Listener 추가 - 실행 결과를 보기 위해 리스너 추가
   * 2에서 만든 Sampler가 받아오는 리턴 값을 바탕으로 그래프, 레포팅을 만들어주는 Listener
     * `View Result Tree`, `Summary Report`, `Graph Results`, `View Results in Table` 등등이있다.
   * 2에서 만든 HTTP Request에 오른쪽 클릭 -> Add -> Listener -> View Results Tree, Summary Report, View Results in Table 생성
   * <img src="https://blog.kakaocdn.net/dn/bEpGgO/btsbUQNxHcm/aiOnAyZ0c1RbeCsP75519K/img.png" width = 800 height = 450>
   * Summary Report
     * `#Samples` : 서버에 요청한 횟수
     * `Average` : 평균응답시간(ms)
     * `Min` : 최소응답시간(ms)
     * `Max` : 최대응답시간(ms)
     * `Std. Dev.` : 표준편차. 요청에 대한 응답시간의 일정하고 안정적인가를 확인, 값이 작을수록 안정적이다.
     * `Error` : Error율(%)
     * `Throughput` : 처리량 (초당 처리건수)
     * `KB/sec` : 처리량(초당 처리 KB)



4. 초록색 스타트 버튼을 누르면 실행된다. 

   * <img src="https://blog.kakaocdn.net/dn/yfDdZ/btsbVbYhddp/KZBeGfhsawlC1kzzPXUCdK/img.png" width = 800 height = 350>
   * 실행을 하기 전에는 Test Plan 설정을 저장 해 줘야 한다

   * 결과 초기화는 `View Result Tree`, `Summary Report`, `Graph Results`, `View Results in Table` 등등 우클릭 하고 초기화 하면 된다



5. 테스트 결과 출력 설정
   * Summary Report(요약 보고서) -> 테이블 데이터 저장 -> 저장
   * .csv, .xml 중에서 선택할 수 있다. 

<img src="https://blog.kakaocdn.net/dn/mmsSC/btsbV7aqMgJ/kYQFkq3jZgpsId6rTd1iJ0/img.png" width = 600 height=600>



### 테스트 수행 결과 - Summary Report (요약 보고서)

<img src="https://blog.kakaocdn.net/dn/whDRR/btscoD6FHD8/AmzmdwiIL8ZALbuVkhTSh1/img.png" width = 900 height = 200>

* 총 10번의 요청. 평균(Average), 최소, 최대의 단위는 ms (미리세컨드)단위
  * 평균 40ms의 응답시간, 최소 32ms, 최대 70ms
  *  Label : Sampler 명
  *  Samples : 샘플 실행 수 (Number of Threads X Ramp-up period)
  *  Average : 평균 걸린 시간 (ms)
  * Min : 최소
  *  Max : 최대
  * Std. Dev. : 표준편차
  * Error % : 에러율
  *  Throughput : 분당 처리량
  *  Received KB/sec : 초당 받은 데이터량
  *  Sent KB/sec : 초당 보낸 데이터량
  * Avg. Bytes : 서버로부터 받은 데이터 평균
* 오류 0%는 HTTP 응답 코드로 판단합니다. 예외에 해당하는 400번대 또는 500번대 응답이 없었음을 예상할 수 있습니다.
* 처리량은 Throughput입니다. Transaction Per Second를 의미하는 TPS라고도 합니다. 48 TPS가 측정되었네요.

### 테스트 수행 결과 - View Results in Table (결과 트리)

View Results Tree를 Table 형식으로 보여줍니다. 데이터는 동일합니다.

<img src="https://blog.kakaocdn.net/dn/sBx58/btsb1RLR1hG/PvGstEJfQiixviP5pVcN61/img.png" width = 800 height = 700>



### 테스트 수행 결과 - **결과 그래프** (Result Tree)

- 테스트 결과를 그래프 형태로 확인할 수 있습니다.
- 쓰레드 그룹에서 반복 횟수를 무한으로 했을 경우, 실시간으로 그래프의 변동을 확인할 수 있습니다.





## Assertion - 요청 성공 여부 검증 

만약 성공 여부를 판단하는 조건이 조금 더 복잡하게 만들고 싶다면 Assertion을 사용하면 됩니다.

* Test Plan 우클릭 -> Add -> Assertions -> Response Assertion
  * Http Request(HTTP 요청) 우클릭 -> Add -> Assertions -> Response Asserion 도 가능 

<img src="https://blog.kakaocdn.net/dn/bkFJ3L/btscwMBB7DY/aS8Ydl0XkbUp4DfOOSk8nk/img.png" width = 900 height= 300>

해당 설정은 Http StatusCode가 200번대가 아니라 딱 200번 응답만 성공으로 보겠다는 것



Json응답을 검증하는 Json Assertion 도 가능합니다.

* Http Request(HTTP 요청) 우클릭 -> Add -> Assertions -> Json Asserion

* `$.name` 같이 mockMvc jsonPath() 와 같은 json 표현식 방식으로 검증이 가능합니다. 

<img src="https://blog.kakaocdn.net/dn/VhhR7/btsbVcbNTGN/4wzxaVkKoe4ibZXfvIkrr1/img.png" width = 800 height=300>

* ResponseBody의 $.name이 study1이 아니면 실패로 보겠다는 것

## HTML 보고서 생성

- 도구 - HTML 보고서 생성 메뉴를 클릭합니다.
- 앞서 생성해둔 csv파일과, bin폴더 내에 있는 jmeter.properties 파일을 선택해줍니다.
- 출력 디렉토리는 빈 디렉토리를 지정해줘야만 합니다. 편한 경로 내에 없는 폴더명을 작성해주면 됩니다.
- 보고서 생성 버튼을 누르면 보고서가 생성됩니다.

<img src="https://blog.kakaocdn.net/dn/vOEFo/btscfAP116U/Sx4oM95eoecKa9v0RdFiok/img.png" width = 500 height = 250>

<img src="https://blog.kakaocdn.net/dn/co76cK/btscoGoJIuy/dVSdp9XdtiP4BEEN9H8jgk/img.png" width=800 height=250>

## 이전 테스트 이력 제거 및 테스트 실행

<img src="https://blog.kakaocdn.net/dn/kLfeN/btscoFXGkVw/PTsplKbc3kxKzQbkqsvWPk/img.png" width = 800 height = 120>

빗자루 1개 : 그냥 초기화

빗자루 2개 : 전체 이력 초기화

캡쳐 상 가장 오른쪽에 있는 버튼을 눌러 이전 테스트 이력을 제거할 수 있습니다. 



### GUI 기반 부하 기반 테스트시 주의할점

* https://like-tomato.tistory.com/299

메모리가 부족하여 뻗어버릴 수 있습니다. 

일반적으로 GUI로 시나리오를 작성한 jmx 파일을 만들고, 해당 파일을 CLI 명령어로 실행하는것이 좋습니다.

위 블로그를 참고해서 해결하면 좋을 것 같습니다. 

## JMeter CLI

작성한 TestPlain을 저장하면 .jmx확장자로 저장됩니다. 

해당 테스트 플랜을 커맨드로 실행할 수 있습니다. 

jmeter 명령어 또는 jmeter가 설치된 경로에서 사용할 수 있습니다  

```sh
# 설치된 경로 : /opt/homebrew/Cellar/jmeter/5.5/bin
jmeter -n -t /opt/homebrew/Cellar/jmeter/5.5/first_test_dadok/dadok_user_me.jmx

# path = /opt/homebrew/Cellar/jmeter/5.5/bin
{path}/bin/jmeter -n -t /opt/homebrew/Cellar/jmeter/5.5/first_test_dadok/dadok_user_me.jmx

# 다른 사용 방법 
# jmeter 실행
jmeter -n -t <파일명>.jmx -l <로그파일명>.log

# 사용예시
/jmeter -n -t user_me_test.jmx -l test_result.log

```

* {path}대신 apache jmeter가 설치된 경로를 넣어주면 됩니다.

- n : non GUI 모드
- -t : 우리가 생성한 jmx 파일 경로 (테스트 설정이나 시나리오 등을 저장한 파일)
- -l : 테스트 결과를 기록할 로그 파일 경로

부하 테스트를 할 준비가 되었으니, 모니터링 툴을 준비한 뒤에 테스트 -> 모니터링 -> 설정 변경 을 반복하면 될 것 같습니다.

추가적으로 BlazeMeter을 사용하면 크롬에서 액션하는 것을 녹화한 후 JMeter에 추가할 수 있습다.

* [chrome.google.com/webstore/detail/blazemeter-the-continuous/mbopgmdnpcbohhpnfglgohlbhfongabi?hl=en](https://chrome.google.com/webstore/detail/blazemeter-the-continuous/mbopgmdnpcbohhpnfglgohlbhfongabi?hl=en)

## 다양한 시각화 옵션들

\- jp@gc - Response Times Over Time 테스트 시간에 따른 응답 시간을 그래프로 보여줌

\- jp@gc - Response Times Over Time 테스트 시간에 따른 응답 시간을 그래프로 보여줌

\- jp@gc – Transactions per Second 초당 시스템의 처리량을 그래프로 보여줌 (TPS)

# JMeter JDBC Test (JDBC Request Test(Database load Test)

1. Thread Group 생성하기 - HTTP Request와 동일
   * 왼쪽 상단 Test Plan 우클릭 후, Add > Threads(Users) > Thread Group을 클릭하여 생성합니다.
2. Thread Properties 설정하기 HTTP Request와 동일
   * Thread Group 을 클릭하여 오른쪽에 있는 Thread Properties를 설정합니다.
3. JDBC Connection Configuration 추가 및 설정하기
   1. JDBC 테스트 진행을 위해서는 Thread Group 내에 JDBC Connection 설정 추가 및 설정 값을 지정합니다.
   2. Thread Group > Add > Config Element > JDBC Connection Configuration 클릭하여 추가
   3. JDBC Connection Configuration 가장 하단에 있는 *Database Connection Configuration*부분입니다.
      해당 부분의 설정값 (Database URL, JDBC Driver Class, Username, Password)를 입력해주시면 해당 Database URL로 접근 권한을 가지고 있는 Username으로 설정하신 Database를 접근할 수 있습니다.

4. Request Sampler 추가하여 진행 - JDBC 요청(SQL 쿼리)을 보낼 수 있습니다.

   *  Thread Group 우클릭 > Add > Sampler > JDBC Request 선택해 JDBC Request를 생성합니다.

   * SQL 쿼리 중 하나로 실제 SQL 쿼리를 작성할 수 있습니다. SQL Query Type은 하단의 정보를 참고하시면 됩니다.

     * Select Statement

     * Update Statement – use this for Inserts and Deletes as well

     * Callable Statement

     * Prepared Select Statement

     * Prepared Update Statement – use this for Inserts and Deletes as well

     * Commit

     * Rollback

     * Autocommit (false)

     * Autocommit (true)

# 설정 관련 팁

#### Test Plan 설정

성능 테스트에 Test Plan의 이름과 설명이 영향을 주지는 않지만 향후 여러 버전 혹은 여러 대상 애플리케이션을 작업해야 한다면 이름과 설명을 제대로 작성해두는 것이 좋다. 또한 실행에 직접적으로 영향을 주는 옵션이 제공되는데, 일반적으로는 잘 사용하지 않지만 알아두면 좋다.

 

### User Defined Variables

변수 선언이 필요할때 사용한다. 테스트 전반에 걸쳐서 선언한 변수와 값을 이용할 수 있다.

#### 변수 설정 - Variable

JMeter에서 변수를 사용하는 방법을 알면 테스트 플랜을 작성할때나 실제 테스트를 수행할때 유용하게 사용할 수 있다. 특히 샘플러를 이용한 테스트에서 변수를 사용하지 않게 되면 POST 혹은 GET으로 전달하는 파라미터 값을 한개 밖에 사용할 수 없다. 이는 성능 테스트에 매우 큰 문제를 발생시키는데 대부분의 미들웨어나 데이터베이스가 같은 값으로 조회하면 캐싱 기능을 통해서 매우 빠르게 응답하기 때문이다.  


때문에 테스트 수행시 적절하게 파라미터 값을 변경해가면서 성능 테스트를 하는 것이 일반적이다. 이러한 요구를 수횽하기 위해 JMeter는 CSV 파일을 이용해서 여러 개의 파라미터 데이터를 정의해서 사용할 수 있다.  


JMeter에서 변수명에 대한 값을 참조할 때는 ${변수명} 형태로 사용한다. 여기서 변수명은 [CSV Data Set Config]에서 지정한 변수명과 일치해야 한다. 설정을 완료하면, JMeter 테스트 수행시 스레드가 CSV 파일의 데이터를 한줄씩 읽어가면서 테스트를 수행한다.  


향후 변경의 소지가 높은 값들을 추가로 변수 처리해서 테스트 플랜을 작성하면 유용하다. 대표적인 것이 서버 주소와 포트 정보인데, 아무리 Config Elements의 HTTP Request Defaults를 이용해서 정의하더라도 이역시 해당 설정이 많으면 변경해야 하는 경우가 많다. 테스트 플랜에서 변수를 지정한 후에 해당 값을 사용하기 위해서 ${host_name}이나 ${service_port} 형태로 설정하면 변수에 설정된 값으로 치환해서 테스트가 실행된다.   


 

이 외에도 [Add]-[Config Element]-[User Defined Variables] 메뉴에서 정의할수도 있고 Random Variables처럼 특정 구간의 숫자 범위에 해당하는 값을 랜덤하게 취해서 사용하는 변수를 정의할 수도 있다.

### Run Thread Groups consecutively

Test Plan에 여러 개의 Thread Group이 있는 경우 이 옵션을 선택하면 병렬로 동작하는 것이 아니라 순차적으로 실행된다.

### Run tearDown Thread Groups after shutdown of main threads

테스트를 종료한 후 후속 테스트를 수행하도록 설정할 수 있다. 이를 위해서는 별도의 "tearDown Thread Group"을 설정해야 한다.

### Functional Test Mode

이 옵션을 선택하게 되면 테스트 시에 서버로부터 응답받은 데이터를 JMeter가 저장하다. 특히 파일 Listener를 Test Plan에 추가한 경우 응답 데이터를 파일에 저장할 수 있으며 저장된 데이터를 보고 정상적으로 Test Plan이 작성되었는지 확인할 수 있다. 이 옵션은 JMeter의 성능에 큰 영향을 주기 때문에 실제 테스트시에는 절대 선택해서는 안된다.

### Add directory or jar to classpath

Test Plan에 사용할 JAR 혹은 디렉터리를 클래스 패스에 추가하는 것으로 주로 데이터베이스 드라이버, JMS 클라이언트 등을 사용한다.

위의 옵션 중 'Functional Test Mode'는 작성한 성능 테스트 모델을 검증하기 위해서 종종 사요한다. 이 옵션을 통해 응답 데이터를 저장하고 해당 저장된 내용이 원하는대로 처리되고 전달 되었는지 1차적으로 검증할 필요가 있다. 실제 성능 테스트를 수행할 때는 이 옵션을 반드시 해제해야 한다는 점이다.

### Scheduler

테스트에 대한 스케줄링 시간을 설정한다.

### 대기시간 설정 - 타이머

성능 테스트 시나리오를 작성할때 실제 동작과 유사하게 모델링을 해야 성능 테스트의 정확도를 높일 수 있다. JMeter에서는 동작사이의 대기시간을 타이머로 설정할 수도 있다. Thread Group에서 마우스 오른쪽 버튼을 클릭하고 [Add]-[Timer]-[Constant Timer]를 선택하면 된다. 이 화면에서 대기에 필요한 시가을 기입하면 실제 성능 테스트시 해당시간만큼 대기하였다가 다음 작업을 진행한다.



 



### 참조

* https://12bme.tistory.com/503

* https://creampuffy.tistory.com/m/209
* https://jaehoney.tistory.com/224

* 인프런, 백기선님 강좌, 더 자바, 애플리케이션을 테스트하는 다양한 방법

* https://vntgcorp.github.io/apacheJmeter/
