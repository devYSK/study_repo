# HeaderWriterFilter - 응답헤더에 보안 관련 헤더 추가하는 필터



응답 헤더에 보안 관련 헤더를 추가해준다.

기본적으로 생성되는 필터이며 앞단부분에 위치하는 필터이다



내부적으로 HeaderWrite 인터페이스를 List로 가지고 있다.

```java
private final List<HeaderWriter> headerWriters;
```

기본적으로 5개의 header writer 적용

* XContentTypeOptionsHeaderWriter : MIME타입 스니핑 방어
* XXssProtectionHeaderWriter : 브라우저에 내장된 XSS 필터 적용
* CacheControlHeadersWriter :  캐시 히스토리 취약점 방어
* XFrameOptionsHeaderWriter : HTML삽입 취약점 방어로 iframe, object 등을 삽입을 방지함(clickjacking 방어) 
* HstsHeaderWriter : https 프로토콜 사용을 강제(https를 사용해야함)



관련 이슈에 대해 기본적인 방어 기능만 제공하는것으로 완벽하게 방어되지 않으며 또한 브라우저마다 다르게 동작할 수 있으므로 유의해야한다

### Response의 헤더

```
// Resopnse Header
HTTP/1.1 200
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
Content-Type: text/html;charset=UTF-8
Content-Language: ko-KR
Transfer-Encoding: chunked
Date: Wed, 22 Aug 2019 17:18:41 GMT
Keep-Alive: timeout=60
Connection: keep-alive
```





## XContentTypeOptionsHeaderWriter: 마임 타입 스니핑 방어.

- 브라우서에서 MIME sniffing을 사용하여 Request Content Type 을 추측 할 수 있는데 이것은 XSS 공격에 악용될 수 있음
- 지정된 MIME 형식 이외의 다른 용도로 사용하고자 하는 것을 차단

```
X-Content-Type-Options: nosniff
```

[MIME 형식의 보안위협 완화: X-Content-Type-Options 헤더](https://webhack.dynu.net/?idx=20161120.001)



> X-Content-Type-Options: nosniff
> 이 헤더는 리소스를 다운로드 할때 해당 리소스의 MIMETYPE이 일치하지 않는 경우 차단을 할 수 있다. 위 처럼 설정하는 경우 styleSheet는 MIMETYPE이 text/css와 일치할 때까지 styleSheet를 로드하지 않는다. 또한 공격자가 다른 확장자(jpg)로 서버에 파일을 업로드 한 후 script태그등의 src의 경로를 변경하여 script를 로드 하는 등의 공격을 막아준다.



## XXssProtectionHeaderWriter: 브라우저에 내장된 XSS 필터 적용.

- XSS — 웹 상에서 가장 기초적인 취약점 공격 방법의 일종으로, 악의적인 사용자가 공격하려는 사이트에 스크립트를 넣는 기법을 말함
- 일반적으로 브라우저에는 XSS공격을 방어하기 위한 필터링 기능이 내장되어 있음
- 물론 해당 필터로 XSS공격을 완벽하게 방어하지는 못하지만 XSS 공격의 보호에 많은 도움이 됨

```
X-XSS-Protection: 1; mode=block
```

[X-XSS-Protection헤더 시험 페이지](https://webhack.dynu.net/?idx=20161119.001)

> 이 헤더는 공격자가 XSS공격을 시도할 때 브라우저의 내장 XSS Filter를 통해 공격을 방지할 수 있는 헤더 위 처럼 설정한 경우 브라우저가 XSS공격을 감지하면 자동으로 내용을 치환한다. mode=block 유무에 따라 내용만 치환 하고 사용자화면에 보여주거나 페이지 로드 자체를 block할 수 있다.
> 위 헤더는 브라우저의 내장 XSS Filter에 의해 처리 되므로 각 브라우저마다 처리 방식이 다를 수 있다. 모든 공격을 막을 수는 없기 때문에 추가적으로 Filter를 설정하여 방어해야 한다. X-XSS-Protection: 1; mode=block



## CacheControlHeadersWriter: 캐시 히스토리 취약점 방어.

- 캐시를 사용하지 않도록 설정

  - 브라우저 캐시 설정에 따라 사용자가 인증 후 방문한 페이지를 로그 아웃한 후 캐시 된 페이지를 악의적인 사용자가 볼 수 있음

  ```
  Cache-Control: no-cache, no-store, max-age=0, must-revalidate
  Pragma: no-cache
  Expires: 0
  ```

> 공격자가 브라우저의 히스토리를 통한 공격을 진행 할 수 있기 때문에 cache를 적용하지 않는다는 헤더 Cache-Control: no-cache, no-store, max-age=0, must-revalidate
> Pragma: no-cache
> Expires: 0



## HstsHeaderWriter: HTTPS로만 소통하도록 강제.

```
Strict-Transport-Security: max-age=31536000 ; includeSubDomains
```

[Strict-Transport-Security - HTTP | MDN](https://developer.mozilla.org/ko/docs/Web/HTTP/Headers/Strict-Transport-Security)



> 이 헤더는 한번 https로 접속 하는 경우 이후의 모든 요청을 http로 요청하더라도 브라우저가 자동으로 https로 요청
> Strict-Transport-Security: max-age=31536000;includeSubDomains;preload
> https로 전송한 요청을 중간자가 가로채어 내용을 볼 수 있는(MIMT)기법을 클라이언트 레벨(브라우저)에서 차단
> 또한 2014년 블랙햇 아시아 컨퍼런스에서 "Leonard Nve Egea"가 sslStrip+ 를 공개하면서
> 서브도메인을 통해 우회할 수 있는 방법에 대해 includeSubDomains 를 추가하여 차단할수 있다.



## XFrameOptionsHeaderWriter: clickjacking 방어

- 웹 사용자가 자신이 클릭하고 있다고 인지하는 것과 다른 어떤 것을 클릭하게 속이는 악의적인 기법
- 보통 사용자의 인식 없이 실행될 수 있는 임베디드 코드나 스크립트의 형태

```
X-Frame-Options: DENY
```

[클릭재킹 - 위키백과, 우리 모두의 백과사전](https://ko.wikipedia.org/wiki/클릭재킹)

>  이 헤더는 한번 https로 접속 하는 경우 이후의 모든 요청을 http로 요청하더라도 브라우저가 자동으로 https로 요청
> Strict-Transport-Security: max-age=31536000;includeSubDomains;preload
> https로 전송한 요청을 중간자가 가로채어 내용을 볼 수 있는(MIMT)기법을 클라이언트 레벨(브라우저)에서 차단
> 또한 2014년 블랙햇 아시아 컨퍼런스에서 "Leonard Nve Egea"가 sslStrip+ 를 공개하면서 서브도메인을 통해 우회할 수
> 있는 방법에 대해 includeSubDomains 를 추가하여 차단할수 있습니다.





# 참고



- X-Content-Type-Options:
  - https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/X-Content-Type-Options
- Cache-Control:
  - https://www.owasp.org/index.php/Testing_for_Browser_cache_weakness_(OTGAUTHN-006)
- X-XSS-Protection
  - https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/X-XSS-Protection
  - https://github.com/naver/lucy-xss-filter
- HSTS
  - https://cheatsheetseries.owasp.org/cheatsheets/HTTP_Strict_Transport_Security_Cheat_Sheet.html
- X-Frame-Options○
  - https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/X-Frame-Options
- https://cyberx.tistory.com/171