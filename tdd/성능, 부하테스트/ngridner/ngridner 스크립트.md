# nGrinder 스크립트 작성법

https://brownbears.tistory.com/27

https://gist.github.com/ihoneymon/a83b22a42795b349c389a883a7bbf356



https://velog.io/@cmsskkk/NGrinder-Redis-Caching

https://jerry92k.tistory.com/48

## 테스트 스크립트 작성방법

테스트를 진행하기 위해서는 먼저 시나리오 기반으로 스크립트를 작성해야합니다. nGrinder는 Groovy나 Jython를 지원합니다. 

Grinder의 TCPProxy를 이용해서 직접 스크립트를 작성하는 것이 아닌 사용자의 인터넷 요청 액션을 recording하여 자동으로 스크립트를 만들 수 있습니다. nGrinder에서도 제공했던 기능이라고 하는데 현재는 안된다고 하네요.



1) nGrinder controller 웹에 접속하여 스크립트 만들기를 클릭한다.
2) 예시 스크립트를 커스텀한다.



예시)[참조](https://velog.io/@max9106/nGrinderPinpoint-test2)

```groovy
로그인 - 언어 기술 목록 조회 - 리뷰어 목록 조회 - 리뷰어 단일 조회 - 내가 받은 리뷰 목록 조회 - 내가 리뷰한 리뷰 목록 조회 - 리뷰 상세 조회
@RunWith(GrinderRunner)
class TestRunner {
	public static GTest test1
	public static GTest test2
	public static GTest test3
	public static GTest test4
	public static GTest test5
	public static GTest test6
	public static GTest test7
	
	public static HTTPRequest request
	public static Map<String, String> headers = [:]
	public static Map<String, Object> params = [:]
	public static List<Cookie> cookies = []

	@BeforeProcess
	public static void beforeProcess() {
		HTTPRequestControl.setConnectionTimeout(300000)
		
		test1 = new GTest(1, "GET /login/oauth?providerName={provider}&code={code}")
		test2 = new GTest(2, "GET /languages")
		test3 = new GTest(3, "GET /teachers?language=java")
		test4 = new GTest(4, "GET /teachers/{id}")
		test5 = new GTest(5, "GET /reviews/teacher/{id}")
		test6 = new GTest(6, "GET /reviews/student/{id} with token")
		test7 = new GTest(7, "GET /reviews/{id}")
		
		request = new HTTPRequest()
		
		grinder.logger.info("before process.")
	}

	@BeforeThread
	public void beforeThread() {
		test1.record(this, "test1")
		test2.record(this, "test2")
		test3.record(this, "test3")
		test4.record(this, "test4")
		test5.record(this, "test5")
		test6.record(this, "test6")
		test7.record(this, "test7")
		
		grinder.statistics.delayReports = true
		grinder.logger.info("before thread.")
	}
	
	private String accessToken
	private String userId = "1"

	@Test
	public void test1() {
		def slurper = new JsonSlurper()
		def toJSON = { slurper.parseText(it) }

		HTTPResponse response = request.GET("http://3.36.68.56:8080/login/oauth?providerName=github&code=a", params)

		def result = response.getBody(toJSON);
		
		accessToken = result.accessToken
		userId = result.id.toString()

		if (response.statusCode == 301 || response.statusCode == 302) {
			grinder.logger.warn("Warning. The response may not be correct. The response code was {}.", response.statusCode)
		} else {
			assertThat(response.statusCode, is(200))
		}
	}
	
	@Test
	public void test2() {
		HTTPResponse response = request.GET("http://3.36.68.56:8080/languages", params)

		if (response.statusCode == 301 || response.statusCode == 302) {
			grinder.logger.warn("Warning. The response may not be correct. The response code was {}.", response.statusCode)
		} else {
			assertThat(response.statusCode, is(200))
		}
	}

	@Test
	public void test3() {
		HTTPResponse response = request.GET("http://3.36.68.56:8080/teachers?language=java&page=30000", params)

		if (response.statusCode == 301 || response.statusCode == 302) {
			grinder.logger.warn("Warning. The response may not be correct. The response code was {}.", response.statusCode)
		} else {
			assertThat(response.statusCode, is(200))
		}
	}

	@Test
	public void test4() {
		HTTPResponse response = request.GET("http://3.36.68.56:8080/teachers/2", params)

		if (response.statusCode == 301 || response.statusCode == 302) {
			grinder.logger.warn("Warning. The response may not be correct. The response code was {}.", response.statusCode)
		} else {
			assertThat(response.statusCode, is(200))
		}
	}
	
	@Test
	public void test5() {
		HTTPResponse response = request.GET("http://3.36.68.56:8080/reviews/teacher/" + userId)

		if (response.statusCode == 301 || response.statusCode == 302) {
			grinder.logger.warn("Warning. The response may not be correct. The response code was {}.", response.statusCode)
		} else {
			assertThat(response.statusCode, is(200))
		}
	}
	
	@Test
	public void test6() {
		headers["Authorization"] = "Bearer " + accessToken
		request.setHeaders(headers)
		HTTPResponse response = request.GET("http://3.36.68.56:8080/reviews/student/" + userId)

		if (response.statusCode == 301 || response.statusCode == 302) {
			grinder.logger.warn("Warning. The response may not be correct. The response code was {}.", response.statusCode)
		} else {
			assertThat(response.statusCode, is(200))
		}
	}
	
	@Test
	public void test7() {
		HTTPResponse response = request.GET("http://3.36.68.56:8080/reviews/1", params)

		if (response.statusCode == 301 || response.statusCode == 302) {
			grinder.logger.warn("Warning. The response may not be correct. The response code was {}.", response.statusCode)
		} else {
			assertThat(response.statusCode, is(200))
		}
	}
}
```

**이때 주의할 점이 있습니다. 저와 같이 하나의 스크립트에 여러 요청이 포함된 경우. 각 테스트의 이름을 test1, test2 이런식으로 해주시는 것이 좋습니다. 그 이유는 제가 해본 결과 각 테스트가 알파벳 순으로 실행되기 때문입니다.**

참고로 테스트의 순서에 따라 공유된 변수를 가져다 쓸 수 있습니다.(위의 accessToken, userId 참고)



3) 스크립트 작성 완료 후 우측 상단의 검증을 눌러 Error가 발생하지 않는다면 스크립트 작성이 완료됩니다.

