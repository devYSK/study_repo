# NGrinder

[nGrinder 공식 깃허브 문서](#https://naver.github.io/ngrinder/)  

`nGrinder`란 네이버에서 진행한 오픈 소스 프로젝트로 **`서버의 부하 테스트를 위한 도구`** 입니다

* The Grinder라는 오픈소스 기반에서 개발. 

웹 애플리케이션을 서비스하기 전에 서버가 얼마나 많은 사용자를 수용할 수 있는지 요청을 전송해봄으로써 서버의 성능을 측정해볼 수 있다.

* 부하테스트는 성능테스트의 일종이지만, 성능테스트랑은 다르다.  
  

nGrinder는 다음과 같은 주요 기능을 제공합니다:

- 스크립트 레코딩: 웹 브라우저에서 사용자 동작을 녹화하여 테스트 스크립트를 생성할 수 있습니다.
- 분산 테스트: 다수의 에이전트 머신을 사용하여 대규모 테스트를 수행할 수 있습니다.
- 실시간 모니터링: 테스트 진행 중에 성능 지표를 실시간으로 모니터링할 수 있습니다.
- 분석 및 리포팅: 테스트 결과를 다양한 그래프와 테이블로 분석하고, 리포트를 생성할 수 있습니다.

  


nGrinder는 무료로 사용할 수 있으며, 개발자와 테스트 전문가들에게 매우 유용한 도구입니다.

  


다른 성능 테스트 도구인 JMeter와의 차이점은 다음과 같습니다.

- JMeter 는 단일 데스크톱 컴퓨터에서 수행되는 반면에 nGrinder 는 컨트롤러 및 에이전트로 구성된 분산 아키텍처로 수행된다.
  - JMeter의 단점. 단일 데스크톱 컴퓨터에서 멀티 스레딩을 이용한다. 
- nGrinder는 jython 또는 Groovy 같은 스크립트 언어를 사용하여 스크립트를 작성한다.
  - 스크립트를 이용한 자동화는 매우 편리합니다. 
- 도커, 클라우드 환경에서도 실행 가능하다

# nGrinder의 구조

`nGrinder`는 `Controller`와 `Agent`로 이루어져 있습니다. 

<img src="https://blog.kakaocdn.net/dn/bYoLZr/btscHQklRgh/yeTfN7CTykHZkD1YQSCp91/img.png" width = 700 height=600>

**Controller**

* 성능 측정을 위한 웹 인터페이스를 제공하며,  Web Application으로 Tomcat과 같은 웹서버 엔진을 이용하여 구동할 수 있습니다.
* 사용자와의 인터페이스를 담당하여 테스트 프로세스 정의, 스크립트 작성 등을 지원합니다.
* 스크립트 수정 기능 제공하며, Agent에 스크립트를 전달하여 테스트를 일임합니다.
*  테스트 프로세스 조정
* 테스트 통계를 수집하고 표시

**Agent**

* Controller의 명령을 받아 실행하며, target에 프로세스와 스레드를 실행시켜 부하를 발생시킴
* 에이전트 모드에서 실행할 때 대상 시스템에 부하를 주는 프로세스 및 스레드를 실행
* 모니터 모드에서 실행 시 대상 시스템 성능(CPU/메모리) 모니터링

**Target**

* 테스트하려는 target 애플리케이션, 머신

# nGrinder 설치

### 선행조건

nGrinder의 Controller와 agent를 설치하기 위해선 JDK 설치가 필요합니다. 

* nGrinder는 JVM위에 Python이 올라가기때문

nGrinder는 agent간 통신을 위해 여러 포트를 사용합니다. 아래의 포트들이 방화벽에 걸리지 않는지 확인이 필요하다.

- Agent: Any ==> Controller: 16001
- Agent: Any ==> Controller: 12000 ~ 12000+(동시 테스트 허용수만큼)
- Controller: Any ==> Monitor: 13243
- Controller ==> Public user: 톰캣설정에 따르지만 기본은 8080이다.



공식 설치 가이드 : https://github.com/naver/ngrinder/wiki/Installation-Guide

## 설치 방법

[nGrinder 깃허브 설치 릴리즈 페이지](https://github.com/naver/ngrinder/releases)

Controller와 Agent 설치 2가지로 나뉩니다. 둘다 하셔야 합니다 

### Controller 설치 방법 

- https://github.com/naver/ngrinder/releases 에서 ngrinder-controller WAR 파일을 받는다.

먼저, 관리의 편의성 때문에 ngrinder용 디렉토리를 따로 생성해주는것이 좋습니다. 

```sh
$ mkdir ngrinder
$ cd ngrinder
```

WAR 파일을 받고 디렉토리로 옮깁니다. 

* **wget 명령어로도 설치가 가능합니다.** 



### 실행

```sh
$ java -jar ngrinder-controller-3.5.8.war --port= 7070  
```

이때 tmpdir 을 셋팅하라는 에러메세지가 출력 된다면?

```sh
$ java -Djava.io.tmpdir=${NGRINDER_HOME}/lib -jar ngrinder-controller.war
```

* ${NGRINDER_HOME} 는 war 파일가 존재하는 디렉토리 또는 설정할 수 있습니다.
* 백그라운드 실행시 가장 뒤에 & 를 붙여주세요.

포트번호를 설정하려면?

```sh
$ java -Djava.io.tmpdir=${NGRINDER_HOME}/lib -jar ngrinder-controller.war --port 7070
```



저같은 경우, 포트번호를 7070로 설정했기 때문에 포트가 7070으로 열립니다

* 기본값은 8080 입니다



loclahost:7070/login 접속시 다음과 같은 화면이 나옵니다. 

<img src="https://blog.kakaocdn.net/dn/cDWOLG/btscHL4lrhk/JMlXY0Wa34QzZf232Csa41/img.png" width=500 height = 450>

* 초기 아이디 패스워드는 admin/admin 



### Agent 설치 방법

agent는 사이트에 로그인한 이후 메뉴에서 admin > Download Agent를 클릭하면 agent가 다운로드 할 수 있습니다.

* nGrinder 3.3 이후로는 Agent를 Controller에서 다운로드 받습니다. 

<img src="https://blog.kakaocdn.net/dn/yASHL/btscyHJdt7T/kjtQk4sbLVB273tPMWA8R0/img.png" width = 500 height =450>

* **에이전트 다운로드 클릭(Download Agent)**
* 마찬가지로 모니터도 여기서 다운로드 받을 수 있습니다.

  


다운로드 받은 해당 tar 파일을 원하는 디렉토리로 옮겨주시고 압축을 해제합니다.

```sh
# 압축 해제
tar -xvf ngrinder-agent-{version}-localhost.tar
# 폴더로 이동
cd ngrinder-agent
```

에이전트를 실행합니다

* 실행파일은 ngrinder-agent 디렉토리 아래에 존재합니다. 

```sh
# Agent쉘스크립트 파일 실행
./run_agent.sh

# Agent 백그라운드 실행
./run_agent_bg.sh
```

<img src="https://blog.kakaocdn.net/dn/Pcm5i/btscBDzv6qA/GvoyEp7FeNtIEwMMGQTQE1/img.png" wifth = 800 height = 200>

nGrinder 3.3 이후로는 연결된 Agent는 Controller에 의해 자동적으로 승인됩니다.

* (Agent 시작후 Controller에서 바로 사용할 수 있음을 의미)  
  


다음 다시 localhost:7070/home 화면으로 돌아가서 에이전트 관리(Agent Management)를 클릭해줍니다.

* admin -> AgentManagement
* `agent` 가 정상적으로 실행되고 잘 등록되었는지 `admin > Agent Management`에서 확인 가능합니다 

<img src="https://blog.kakaocdn.net/dn/bgUOFE/btscJqrPzNs/XobaAVlsvFkoIThoLHFEwk/img.png" width=900 height=200>

에이전트가 실행되어있는것을 알 수 있습니다. 

## 시나리오 정의 및 실행 스크립트 작성

1. 상단에 Script(스크립트) 창에 들어가서 create script를 통한 스크립트를 만들 수 있습니다.

<img src="https://blog.kakaocdn.net/dn/bMxLt5/btscHd073LR/aGPRSKZTzk9xi9aeSAUIsk/img.png" wifth = 500 height = 350>

* 스크립트(script) -> 만들기(create script)  

  


2. 다음 스크립트 이름과 테스트할 URL을 지정해줍니다.

<img src="https://blog.kakaocdn.net/dn/m1MqK/btscIIfccvq/3SkOKXc8Sv7WwIXjOKo771/img.png" width=750 height=300>



3. 만들게되면 기본 스크립트를 보여줍니다.

```groovy
import static net.grinder.script.Grinder.grinder
import static org.junit.Assert.*
import static org.hamcrest.Matchers.*
import net.grinder.script.GTest
import net.grinder.script.Grinder
import net.grinder.scriptengine.groovy.junit.GrinderRunner
import net.grinder.scriptengine.groovy.junit.annotation.BeforeProcess
import net.grinder.scriptengine.groovy.junit.annotation.BeforeThread
// import static net.grinder.util.GrinderUtils.* // You can use this if you're using nGrinder after 3.2.3
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith

import org.ngrinder.http.HTTPRequest
import org.ngrinder.http.HTTPRequestControl
import org.ngrinder.http.HTTPResponse
import org.ngrinder.http.cookie.Cookie
import org.ngrinder.http.cookie.CookieManager

/**
* A simple example using the HTTP plugin that shows the retrieval of a single page via HTTP.
*
* This script is automatically generated by ngrinder.
*
* @author admin
*/
@RunWith(GrinderRunner)
class TestRunner {

	public static GTest test
	public static HTTPRequest request
	public static Map<String, String> headers = [:]
	public static Map<String, Object> params = [:]
	public static List<Cookie> cookies = []

	@BeforeProcess
	public static void beforeProcess() {
		HTTPRequestControl.setConnectionTimeout(300000)
		test = new GTest(1, "google.com")
		request = new HTTPRequest()
		grinder.logger.info("before process.")
	}

	@BeforeThread
	public void beforeThread() {
		test.record(this, "test")
		grinder.statistics.delayReports = true
		grinder.logger.info("before thread.")
	}

	@Before
	public void before() {
		request.setHeaders(headers)
		CookieManager.addCookies(cookies)
		grinder.logger.info("before. init headers and cookies")
	}

	@Test
	public void test() {
		HTTPResponse response = request.GET("http://google.com", params)

		if (response.statusCode == 301 || response.statusCode == 302) {
			grinder.logger.warn("Warning. The response may not be correct. The response code was {}.", response.statusCode)
		} else {
			assertThat(response.statusCode, is(200))
		}
	}
}

```

Groovy는 Java와 비슷하며 자세한 문법은 아래 링크를 참고하세요.

* http://www.groovy-lang.org/syntax.html

여기에서는 작동방법만 확인할 예정으로 다른 테스트는 구현하지 않습니다.

* 경우에 따라 요청 메시지에 들어가는 파라미터와 헤더, 쿼리를 수정할 수 있습니다

  


4. **검증(Validate) 버튼**을 클릭하면 스크립트 정상인지 확인할 수 있습니다

---

#### General error during conversion: Unsupported class file major version 61 에러

* https://shanepark.tistory.com/402

* `class file major version 61`은 로드 하려고 시도하고 있는 클래스 파일이 자바 17 혹은 그 이상의 버전에서 컴파일이 되었으며, 자바 17 이상에서만 사용 될 수 있을 때 발생한다고 합니다.

* 해결방법
  1. JDK SDK 버전 변경
  2. 실행시 Java HOME 지정  

## 부하테스트 작성 및 실행

### 1. Performance Test에서 Create Test 를 누르면 셋팅할 수 있는 화면을 보여줍니다.

* nGrinder Admin Web > Performance Test(성능 테스트) > "Create Test(테스트 생성)" 

<img src="https://blog.kakaocdn.net/dn/bUcNG0/btscAp2KsY0/VloVEslukdpPbgr6MY0RV0/img.png" wifth = 650 height = 500>

**새로운 창이 뜨는데, 각각의 옵션 설명은 다음과 같습니다.**

- Agent : 성능 측정에 사용할 Agent
  - Agent를 여러개로 구성하고 싶은 경우 Docker 나 cloud service 를 고려해 볼 수 있다.
  - 일반적인 로컬에서 테스트를 실행할 경우 1이 고정값
- Vuser per agent : `프로세스 수 * 스레드 수`가 각 agent당 **가상 사용자의 수**가 된다.
  - Agent 당 설정할 가상 사용자 수
  - 동시에 요청을 날리는 사용자
- Process / Thread
  - 하나의 Agent에서 생성할 프로세스와 스레드 개수
  - 한 Agent에 얼마나 많은 worker process를 쓸지이다. 그리고 이 process들은 또 스레드들을 가지게 된다
- Script
  - 성능 측정 시 각 Agent 에서 실행할 스크립트
  - 방금 우리가 생성한 Script를 추가한다.
- Duration : 얼마나 오래 테스트가 지속될지 정하는 것
  - 성능 측정 수행 시간 (0:00:10 (시:분:초로 10초 의미))
  - 어느 블로그의 말을 따르면 어느정도 길게 확보해줘야 의미있는 평균치가 나온다고 한다.
- Run Count
  - 스레드 당 테스트 코드를 수행하는 횟수
  - Run Count와 Duration의 경우 둘 중 하나만 선택해서 기간 동안 실행하거나, Run Count 만큼 실행하게 한다.
- Enable Ramp-up
  - 성능 측정 과정에서 가상 사용자를 점진적으로 늘리도록 활성화
- Initial Count
  - 처음 시작 시 가상 사용자 수
- Initial Sleep Time
  - 테스트 시작 시간
- Incremental Step
  - Process 또는 thread 를 증가시키는 개수
- Interval
  - Process 또는 Thread를 증가시키는 시간 간격

* Ramp-up
  * Ramp-up을 체크하면 프로세스 수가 점진적으로 늘어난다. **점진적인 부하를 테스트**하고 싶을 때 쓰면 될 것 같다.

### 2. 지금 실행 여부를 묻는 팝업이 뜨면 "Run Now(지금 시작)" 클릭합니다. (미래 스케쥴링도 가능)

* 실행중인 로그는 위에서 실행한 에이전트.sh 로그를 보면 된다.

### 3. 테스트 결과 확인

테스트 실행 종료 후 "Detailed Report(상세 보고서)" 클릭합니다.

<img src="https://blog.kakaocdn.net/dn/cRF3ZW/btscJmb0gBN/qCzefA53aZwhkRQEkK83o0/img.png" width = 850 height = 650>

결과를 확인할 수 있습니다. 

- MTT 는 평균적인 1회 수행 시간으로 중요한 지표이다.
- TPS 는 초당 트랜잭션 개수를 말한다.

- Total Vusers: Vusers 수 (버츄얼 유저)
- TPS: 평균 TPS
- Peak TPS: 최고 TPS
- Mean Test Time: 평균 테스트시간
- Executed Tests: 테스트 실행 횟수
- Successful Tests: 테스트 성공 횟수
- Errors: 에러 횟수
- Run time: 테스트 실행시간

## nGridner 성능 향상 방법

nGrinder는 최대한 OS설정과 독립적으로 운영되도록 구현되어 있습니다. 따라서 agent 성능을 최적화 하기 위한 0S 튜닝 포인트가 아래 리스트를 제외하고는 거의 없습니다.

- ﻿﻿ulimit 파일 오픈 가능 카운트를 1만 이상 유지
  - ﻿﻿성능 테스트시, 소켓을 많이 열 수 있기 때문
- ﻿﻿0S영역을 제외하고 3G정도의 Free 메모리를 유지할 것
- ﻿﻿socket linger option 설정을 조정하여, 소켓이 사용 후 바로 반납 되도록 할 것
  - ﻿﻿스크립트의 socket linger를 설정하지 않으면 OS TCP 설정을 따르기 때문에 둘 중 하나를 선택 선택 선택 
  - ﻿﻿jython socket 패키지를 사용할 경우 스크립트 상에 다음과 같이 설정하면 가능
    - ﻿﻿clientsock.setsockopt(socket.SOL_SOCKET,socket.SO_LINGER,struct.pack('i
       i', L_onoff, [_linger))

또한 controller의 성능 향상을 위해 db를 변경해도 개선이 되지 않습니다. (e.g, embedded db -> cubrid)



실제 유저 행위를 시뮬레이션하여 10초 씩 thinktime을 준다면, agent당 1만 vuser(thread)를 견딜 수 있습니다.

* agent는 메모리가 가장 중요한 이슈



### 참조

* https://liltdevs.tistory.com/169?category=1084276
* https://leezzangmin.tistory.com/42

* https://jy-p.tistory.com/141
* https://velog.io/@injoon2019/%EB%B6%80%ED%95%98%ED%85%8C%EC%8A%A4%ED%8A%B8-z8jb3vvv

