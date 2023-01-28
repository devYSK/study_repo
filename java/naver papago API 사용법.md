

# Java Papago 번역 API 사용



* [파파고 번역 개요](https://developers.naver.com/docs/papago/papago-nmt-overview.md)

* [API 문서 (API 레퍼런스)](https://developers.naver.com/docs/papago/papago-nmt-api-reference.md)
* [구현 예제 - 공식문서(Java)](https://developers.naver.com/docs/papago/papago-nmt-example-code.md)



Java를 이용한 Papago API 호출 예제이다.



Papago 번역 API는 일 허용량이 10,000 글자이다.

<img src="https://blog.kakaocdn.net/dn/PUIQb/btrXiOHUBdw/wO7gn4JMzHJ52uS5KFYRgK/img.png">

* 일일 호출양이 끝난 상태.

## 애플리케이션 등록

* https://developers.naver.com/docs/papago/papago-nmt-overview.md#%EC%95%A0%ED%94%8C%EB%A6%AC%EC%BC%80%EC%9D%B4%EC%85%98-%EB%93%B1%EB%A1%9D

1. 애플리케이션 등록

- 네이버 개발자 센터 상단 `Application` -> `애플리케이션 등록` -> `애플리케이션 등록 (API 이용신청)` 페이지
- `애플리케이션 이름`, `사용 API`, `비로그인 오픈 API 서비스 환경` 입력

2. 애플리케이션 등록 확인

- 네이버 개발자 센터 상단 `Application` -> `내 애플리케이션` -> `애플리케이션`에 등록된 Papago 번역 애플리케이션 정보
- Client ID / Client Secret 확인
  - API 요청 시 HTTP 요청 헤더에 필요.



## Code

 `clientId`, `clientSecret` 가 노출되지 않도록 properties 에 설정 하는 등 코드에 노출되지 않도록 한 후 사용해야 한다.

* ObjectMapper를 통해서 String 이 아닌 Object 상태로 리턴받을 수도 있다.

**PapagoApiClient.java**

```java
public class PapagoApiClient {
	
  private final PapagoProperties properties;

  private final ObjectMapper mapper;
  
  public String translate(String text) {
    
    Map<String, String> requestHeaders = new HashMap<>();
		requestHeaders.put("X-Naver-Client-Id", properties.getClientId());
		requestHeaders.put("X-Naver-Client-Secret", properties.getClientSecret());
    
    String response = post(properties.getUrl(), requestHeaders, encodeText(text, "UTF-8"));
    
    ApiResponse apiResponse = mapper.readValue(response, ApiResponse.class);
    
    return apiResponse.getTransaltedText();
  }
  
  private String encodeText(String text, String charset) {
    try {
      return URLEncoder.encode(text, charset);
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException("인코딩 실패", e);
    }
  }
  
  private static String post(String apiUrl, Map<String, String> requestHeaders, String text) {
		HttpURLConnection con = connect(apiUrl);
		String postParams = "source=ko&target=en&text=" + text; //원본언어: 한국어 (ko) -> 목적언어: 영어 (en)
		try {
			con.setRequestMethod("POST");
			for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
				con.setRequestProperty(header.getKey(), header.getValue());
			}

			con.setDoOutput(true);
			try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
				wr.write(postParams.getBytes());
				wr.flush();
			}

			int responseCode = con.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 응답
				return readBody(con.getInputStream());
			} else {  // 에러 응답
				return readBody(con.getErrorStream());
			}
		} catch (IOException e) {
			throw new RuntimeException("API 요청과 응답 실패", e);
		} finally {
			con.disconnect();
		}
	}

	private static HttpURLConnection connect(String apiUrl) {
		try {
			URL url = new URL(apiUrl);
			return (HttpURLConnection)url.openConnection();
		} catch (MalformedURLException e) {
			throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
		} catch (IOException e) {
			throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
		}
	}

	private static String readBody(InputStream body) {
		InputStreamReader streamReader = new InputStreamReader(body);

		try (BufferedReader lineReader = new BufferedReader(streamReader)) {
			StringBuilder responseBody = new StringBuilder();

			String line;
			while ((line = lineReader.readLine()) != null) {
				responseBody.append(line);
			}

			return responseBody.toString();
		} catch (IOException e) {
			throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
		}
	}
  
}
```

**PapagoProperties**

```java
@Getter
@ConstructorBinding
@ConfigurationProperties(prefix = "translator.papago")
public class PapagoProperties {

  private String url;
  
  private String clientId;

  private String clientSecret;
}
```



**ApiResponse**

```java
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResponse {

  private Message message;

  public String getTranslatedText() {
    return getMessage().getResult().getTranslatedText();
  }
  @Getter
  @JsonIgnoreProperties(ignoreUnknown = true)
  private static class Message {
      private Result result;
  }

  @Getter
  @JsonIgnoreProperties(ignoreUnknown = true)
  private static class Result {
      private String srcLangType;
      private String tarLangType;
      private String translatedText;
      private String engineType;
      private String pivot;
      private String dict;
      private String tarDict;
  }
}
```



**@JsonIgnoreProperties**

- 무시할 속성이나 속성 목록을 표시하는 데 사용





[구글 번역 API](https://cloud.google.com/translate/pricing?hl=ko) 는 월 500,000자 까지 사용이 가능하지만, 이후에는 백만 자당 $20가 부과된다.

