

# 인프런 - 비전공자의 전공자 따라잡기 - 조현영님 강의 

- https://www.inflearn.com/course/%EC%A0%84%EA%B3%B5%EC%9E%90-%EB%94%B0%EB%9D%BC%EC%9E%A1%EA%B8%B0-%EB%84%A4%ED%8A%B8%EC%9B%8C%ED%81%AC-http/dashboard



* 노드 js 실행코드 : https://github.com/zerocho/cs-network-http

[toc]





# 1 OSI 7계층

![image-20230730140741789](./images//image-20230730140741789.png)



## L3 계층

![image-20230730150118801](./images//image-20230730150118801.png)

L2프레임에서 네트워크 데이터가 들어있다.

### 공인 IP vs 사설 IP

네트워크에는 대표 주소인 IP가 있음 

* but 개수 한정으로 내부 네트워크는 사설 Ip를 사용

ex) 공인 IP - 123.45.61.8 사용 - 요청을 받을때는 이 아이피를 사용

* 사설 IP - 192.168 ~~, 172.16 ~ 172.31
  * ex) 공유기 - 192.168.0.1
  * 공기청정기 - 192.168.0.23
  * 에어컨 - 192.168.0.34 

## 전송 계층

포트

0~ 1023 은 Well Known Port로 역할이 정해져있다.

총 0~65535까지 65536개

## 데이터 흐름

![image-20230730153034647](./images//image-20230730153034647.png)

ex) 네이버로 전송 과정

1. 내 라우터로 먼저 전송( 목적지 MAC(내 네트워크), 출발지 MAC( 내 MAC))
2. 내라우터는 Naver 라우터의 MAC주소를 안다. 그래서 네이버 라우터로 전송한다
   1. 목적지 MAC(네이버 네트워크 라우터), 출발지 MAC ( 내 네트워크 라우터)
3. 네이버 라우터는 목적지 MAC(네이버 MAC), 출발지 MAC(네이버 네트워크 라우터)를 담은 정보를 전송
4. 네이버 서버컴퓨터가 요청을 읽고 응답한다

**라우터간 주소 획득 과정**

* 라우팅테이블에서 경로를 물어물어 찾아가는 것
* 네이버 IP는 처음부터 알고있다 (DNS 덕분에)

>
> 라우터끼리의 데이터 전송에서도 MAC주소와 IP 주소 전부 사용된다.
>
> `다른 라우터의 MAC 주소를 전부 알지는 못하고 바로 옆 라우터만 알고 있다`.  거기다가 네이버IP를 알고 있는 것
>
> 바로 옆 라우터(next hop)한테 네이버 찾아달라고 물어보고, 옆 라우터는 옆옆 라우터의 MAC주소를 알고 있으므로 옆옆 라우터한테 물어보고 해서 물어 물어 찾는 방식. 
>
> 이게 ip 라우팅(라우팅테이블)이다.

```
어떤 사람의 주소가 용인시 수지구라는 사실을 알아내는 것이 DNS이고, 
용인시 수지구까지 어떻게 가야하는지를 물어물어 아는 것이 라우팅 테이블
(강남구는 분당구한테 물어보고, 분당구는 수지구한테 물어보고...)
```

## DNS 레코드

DNS란?

- https://www.cloudflare.com/ko-kr/learning/dns/what-is-dns/

#### DNS 조회의 8단계:

1. 사용자가 웹 브라우저에 'example.com'을 입력하면, 쿼리가 인터넷으로 이동하고 DNS 재귀 확인자(DNS 리졸버)가 이를 수신합니다.
2. 이어서 리졸버가 DNS 루트 이름 서버(.)를 쿼리합니다.
3. 다음으로, 루트 서버가, 도메인에 대한 정보를 저장하는 최상위 도메인(TLD) DNS 서버(예: .com 또는 .net)의 주소로 확인자에 응답합니다. example.com을 검색할 경우의 요청은 .com TLD를 가리킵니다.
4. 이제, 확인자가 .com TLD에 요청합니다.
5. 이어서, TLD 서버가 도메인 이름 서버(example.com)의 IP 주소로 응답합니다.
6. 마지막으로, 리졸버가 도메인의 이름 서버로 쿼리를 보냅니다.
7. 이제, example.com의 IP 주소가 이름 서버에서 확인자에게 반환됩니다.
8. 이어서, DNS 확인자가, 처음 요청한 도메인의 IP 주소로 웹 브라우저에 응답합니다.
9. DNS 조회의 8단계를 거쳐 example.com의 IP 주소가 반환되면, 이제 브라우저가 웹 페이지를 요청할 수 있습니다.

10. 브라우저가 IP 주소로 [HTTP](https://www.cloudflare.com/learning/ddos/glossary/hypertext-transfer-protocol-http/) 요청을 보냅니다.
11. 해당 IP의 서버가 브라우저에서 렌더링할 웹 페이지를 반환합니다(10단계).

### 레코드

<img src="./images//image-20230730155522246.png" width = 500 height = 450>

## 와이어샤크 사용하기

https://www.wireshark.org/

설치

https://hongpossible.tistory.com/entry/Wireshark%EB%9E%80-%EC%84%A4%EC%B9%98%EB%B2%95

https://luckygg.tistory.com/379

https://doongdangdoongdangdong.tistory.com/249

https://no-more-assignment.tistory.com/175

와이어샤크 3way handshake - https://stay-present.tistory.com/103

* https://whitekeyboard.tistory.com/858

* https://sh-safer.tistory.com/142



네트워크 공부시? - https://www.cloudflare.com/ko-kr/learning/



## 3way handshake, TLS 핸드쉐이크

![image-20230730164146805](./images//image-20230730164146805.png)

TCP는 장치들 사이에 논리적인 접속을 성립(establish)하기 위하여 three-way handshake를 사용한다.

 

**TCP 3 Way Handshake는 TCP/IP프로토콜을 이용해서 통신을 하는 응용프로그램이 데이터를 전송하기 전에** 

**먼저** **정확한 전송을 보장하기 위해 상대방 컴퓨터와 사전에 세션을 수립하는 과정을 의미한다..**

 

* Client > Server : **TCP SYN**

* Server > Client : **TCP SYN, ACK**

* Client > Server : **TCP ACK**

여기서 SYN은 'synchronize sequence numbers', 그리고 ACK는'acknowledgment' 의 약자이다.

이러한 절차는 TCP 접속을 성공적으로 성립하기 위하여 반드시 필요하다.

**TCP의 3-way Handshaking 역할**

* 양쪽 모두 데이타를 전송할 준비가 되었다는 것을 보장하고, 실제로 데이타 전달이 시작하기전에

  한쪽이 다른 쪽이 준비되었다는 것을 알수 있도록 한다.

* 양쪽 모두 상대편에 대한 초기 순차일련변호를 얻을 수 있도록 한다. 

![img](./images/img.png)https://seongonion.tistory.com/74

**TCP의 3-way Handshaking 과정**

**[STEP 1]**

A클라이언트는 B서버에 접속을 요청하는 SYN 패킷을 보낸다.

이때 A클라이언트는 SYN 을 보내고 SYN/ACK 응답을 기다리는 **SYN_SENT** 상태, **B서버는 Wait for Client** 상태이다.

**[STEP 2]** 

B서버는 SYN요청을 받고 A클라이언트에게 요청을 수락한다는 ACK 와 SYN flag 가 설정된 패킷을 발송하고

A가 다시 ACK으로 응답하기를 기다린다. 이때 **B서버**는 **SYN_RECEIVED** 상태가 된다.

**[STEP 3]**

A클라이언트는 B서버에게 ACK을 보내고 이후로부터는 연결이 이루어지고 데이터가 오가게 되는것이다.

이때의 **B서버 상태가 ESTABLISHED** 이다.

위와 같은 방식으로 통신하는것이 신뢰성 있는 연결을 맺어 준다는 TCP의 3 Way handshake 방식이다.

### **2. 4-way Handshaking** 

3-Way handshake는 TCP의 연결을 초기화 할 때 사용한다면, 

4-Way handshake는 세션을 종료하기 위해 수행되는 절차입니다.

> 4-Way handshake는 TCP (Transmission Control Protocol) 연결 종료 과정에서 사용되는 메커니즘입니다. TCP 연결을 정상적으로 종료하기 위해 필요한 네 단계의 메시지 교환 과정

![img](./images/img-20230730164226202.png)https://seongonion.tistory.com/74

*** TCP의 4-way Handshaking 과정**

**[STEP 1]**

클라이언트가 연결을 종료하겠다는 FIN플래그를 전송한다. 이때 **A클라이언트는 FIN-WAIT** 상태가 된다.

**[STEP 2]** 

B서버는 FIN플래그를 받고, 일단 확인메시지 ACK 보내고 자신의 통신이 끝날때까지 기다리는데 이 상태가

**B서버의 CLOSE_WAIT**상태다.

**[STEP 3]**

연결을 종료할 준비가 되면, 연결해지를 위한 준비가 되었음을 알리기 위해 클라이언트에게 FIN플래그를 전송한다. 이때 B서버의 상태는 **LAST-ACK**이다.

**[STEP 4]**

클라이언트는 해지준비가 되었다는 ACK를 확인했다는 메시지를 보낸다.

**A클라이언트의 상태가 FIN-WAIT ->****TIME-WAIT** 으로 변경된다.

그런데 만약 "Server에서 FIN을 전송하기 전에 전송한 패킷이 Routing 지연이나 패킷 유실로 인한 재전송 등으로 인해 FIN패킷보다 늦게 도착하는 상황"이 발생한다면 어떻게 될까요? 

 

Client에서 세션을 종료시킨 후 뒤늦게 도착하는 패킷이 있다면 이 패킷은 Drop되고 데이터는 유실될 것입니다. 

 

**A클라이언트**는 이러한 현상에 대비하여 Client는 Server로부터 FIN을 수신하더라도 일정시간(디폴트 240초) 동안 세션을 남겨놓고 잉여 패킷을 기다리는 과정을 거치게 되는데 이 과정을 **"****TIME_WAIT****"** 라고 합니다. 일정시간이 지나면, 세션을 만료하고 연결을 종료시키며, **"CLOSE"** 상태로 변화합니다. 

> -4Way handshake가 필요한 이유
>
> 양측 모두 독립적으로 데이터를 보낼 수 있으므로, 양측 모두 독립적으로 연결을 종료할 준비가 되었다는 것을 확인해야한다

# HTTP

## 네트워크 탭 사용하기

브라우저에서 f12 누르면 된다.

* Disable cache를 눌러 끄면 좋다. 캐시땜에 제대로 못받는 데이터가 많다

## RFC 보는방법

RFC(Request for Comments)는 인터넷을 구성하는 여러 기술과 프로토콜에 대한 사양, 보고서, 정보를 기록한 문서

* rfc 번역 잘된 티스토리 블로그 : https://roka88.dev/category/RFC

* 모질라 HTTP 리소스와 명세 https://developer.mozilla.org/ko/docs/Web/HTTP/Resources_and_specifications

rfc 7231 : http 1.1 프로토콜 문서 :https://datatracker.ietf.org/doc/html/rfc7231

rfc 2616 :  http 1.1 프로토콜 세부 사양 문서  : https://datatracker.ietf.org/doc/html/rfc2616

최신 문서 - RFC 9110 - https://www.rfc-editor.org/rfc/rfc9110

* 여기가 가장 디테일하다
* 이 문서는 RFC 3864를 업데이트하고 RFC 2818, 7231, 7232, 7233, 7235, 7538, 7615, 7694 및 7230의 일부를 폐기한다고 한다.

TLS 최신 문서 - https://datatracker.ietf.org/doc/html/rfc8446

HTTP docs Mozila - https://developer.mozilla.org/en-US/docs/Web/HTTP

> MDN 에서 본 후 스펙가서 보는게 정확하다



## 주소 구성 체계 (URL, URI, Origin)

**URL (Uniform Resource Locator)**: 웹에서 정보를 찾는데 사용하는 가장 일반적인 방법으로, 웹 페이지를 가리키는 주소입니다. 

URL의 구성요소

* 프로토콜 (http, https 등), 
* 서버 이름 (또는 IP 주소), 
* 경로 (서버 상의 파일 위치), 
* 선택적으로 포트 번호와 쿼리 문자열

예시: `https://www.example.com:80/path/to/myfile.html?key1=value1&key2=value2`

- `https://`는 프로토콜
- `www.example.com`는 서버 이름
- `80`은 포트 번호. 이는 선택사항이며 일반적으로 생략다. https의 기본 포트는 443, http의 기본 포트는 80
- `/path/to/myfile.html`는 서버 상의 파일 위치
- `key1=value1&key2=value2`는 쿼리 문자열로, 서버로 전달될 추가 파라미터

**URI (Uniform Resource Identifier)**: 리소스를 식별하는 고유한 문자열로, URL과 URN (Uniform Resource Name)의 상위 개념

*  모든 URL은 URI지만 모든 URI가 URL인 것은 아니다
* **URI** (Uniform Resource Identifier)는 인터넷에서 자원을 고유하게 식별하고 구별하기 위한 문자열. 즉, 어떤 자원이 있는지를 알려줍니다. URI의 주요한 두 가지 하위 카테고리는 URL과 URN.
* **URL** (Uniform Resource Locator)은 인터넷 상의 자원의 정확한 위치를 가리키고, 그 위치에 접근하는 방법을 설명하는 URI입니다. 즉, 그 자원이 어디에 있는지와 어떻게 접근할 수 있는지를 알려줍니다.
* **URN** (Uniform Resource Name)은 리소스의 이름을 제공하는 URI의 한 형태로, 그 위치와는 상관 없이 리소스를 고유하게 식별합니다.



* https://nodejs.org/api/url.html#url

```
┌────────────────────────────────────────────────────────────────────────────────────────────────┐
│                                              href                                              │
├──────────┬──┬─────────────────────┬────────────────────────┬───────────────────────────┬───────┤
│ protocol │  │        auth         │          host          │           path            │ hash  │
│          │  │                     ├─────────────────┬──────┼──────────┬────────────────┤       │
│          │  │                     │    hostname     │ port │ pathname │     search     │       │
│          │  │                     │                 │      │          ├─┬──────────────┤       │
│          │  │                     │                 │      │          │ │    query     │       │
"  https:   //    user   :   pass   @ sub.example.com : 8080   /p/a/t/h  ?  query=string   #hash "
│          │  │          │          │    hostname     │ port │          │                │       │
│          │  │          │          ├─────────────────┴──────┤          │                │       │
│ protocol │  │ username │ password │          host          │          │                │       │
├──────────┴──┼──────────┴──────────┼────────────────────────┤          │                │       │
│   origin    │                     │         origin         │ pathname │     search     │ hash  │
├─────────────┴─────────────────────┴────────────────────────┴──────────┴────────────────┴───────┤
│                                              href                                              │
└────────────────────────────────────────────────────────────────────────────────────────────────┘
```

- **protocol (스키마)**: 이는 리소스에 액세스하기 위해 사용되는 프로토콜을 나타냅니다. 예: 'https:', 'http:', 'ftp:'
- **auth**: URL의 사용자 이름과 비밀번호를 나타냅니다. 예: 'user:pass'. 대부분의 웹사이트에서는 보안상의 이유로 사용되지 않습니다.
- **host**: URL의 호스트 이름과 포트 번호를 나타냅니다. 
  - 예: 'sub.example.com:8080'. 호스트 이름은 웹 서버의 이름 또는 IP 주소
  -  포트 번호는 해당 서버에서 리소스에 액세스하기 위해 사용되는 네트워크 포트를 나타냅니다.
- **hostname**: 이는 URL의 호스트 이름을 나타냅니다. 예: 'sub.example.com'.
- **port**: 이는 URL의 네트워크 포트 번호를 나타냅니다. 예: '8080'.
- **path (pathname)**: 이는 서버에서 리소스의 위치를 나타냅니다. 예: '/p/a/t/h'.
- **search**: 이는 URL의 쿼리 문자열을 나타냅니다. 예: '?query=string'. 이는 일반적으로 웹 서버에 추가 정보를 제공하는 데 사용됩니다.
- **query**: 이는 URL의 쿼리 파라미터를 나타냅니다. 예: 'query=string'.
- **hash**: 이는 URL의 프래그먼트 식별자를 나타냅니다. 예: '#hash'. 이는 특정 페이지 내의 특정 위치(일반적으로 앵커)를 참조하는 데 사용됩니다. - 프론트엔드에서만 식별 가능
- **origin**: 이는 URL의 출처를 나타냅니다. 출처는 스키마, 호스트, 포트 번호로 구성됩니다. 예: '[https://sub.example.com:8080](https://sub.example.com:8080/)'.
- **href**: 이는 전체 URL을 나타냅니다.



**Origin**: 웹 보안에서 매우 중요한 개념으로, 웹 컨텐츠의 출처를 정의합니다. 같은 출처 정책(Same-Origin Policy)은 웹 브라우저가 스크립트를 실행할 때 해당 스크립트가 데이터에 접근할 수 있는 범위를 제한합니다.

Origin은 URI 스킴 (프로토콜), 호스트 (도메인 이름 또는 IP 주소), 그리고 포트 번호로 정의됩니다. 

* https://www.example.com:443`는 웹 페이지 `https://www.example.com/index.html`의 origin

# HTTP 헤더

1. HTTP 메서드와 REST API
2. 안전한 메서드, 멱등성 메서드
3. 상태 코드(1XX, 2XX)
4. 직접 서버 실행해보기 + 3XX 상태 코드
5. 에러 상태 코드(4XX, 5XX)
6. 컨텐츠 협상과 MIME Type
7. Keep-Alive, Date, Transfer-Encoding
8. Authorization, 기타 헤더, 커스텀 헤더
9. 쿠키
10. 캐시(Cache-Control)
11. 캐시 신선도 검사
12. CORS

## HTTP 메서드와 REST API

https://www.rfc-editor.org/rfc/rfc9110#name-methods

### REST API

- redhat 공식문서(한글, 간단함) - https://www.redhat.com/ko/topics/api/what-is-a-rest-api
- aws 공식문서(한글, 간단함) -https://aws.amazon.com/ko/what-is/restful-api/
- 가비아 공식문서 - restful api 설계 가이드(한글, 디테일함) - https://library.gabia.com/contents/8339/
- 마이크로소프트 Learn 공식 문서 Restful web api design(한글, 디테일함) - https://learn.microsoft.com/ko-kr/azure/architecture/best-practices/api-design
- http의 멱등성 - https://hudi.blog/http-method-idempotent/
- http의 멱등성, 안정성, 캐시성 - [https://inpa.tistory.com/entry/WEB-%F0%9F%8C%90-HTTP%EC%9D%98-%EB%A9%B1%EB%93%B1%EC%8[…\]2%AF-%EC%99%84%EB%B2%BD-%EC%9D%B4%ED%95%B4%ED%95%98%EA%B8%B0](https://inpa.tistory.com/entry/WEB-🌐-HTTP의-멱등성-·-안정성-·-캐시성-💯-완벽-이해하기)





## 안전한 메서드, 멱등성 메서드

* 안전한 메서드 : https://www.rfc-editor.org/rfc/rfc9110#name-safe-methods
* 멱등성 메서드 : https://www.rfc-editor.org/rfc/rfc9110#section-9.2.2

| HTTP 메서드 | 안전성 | 멱등성 |
| ----------- | ------ | ------ |
| GET         | O      | O      |
| POST        | X      | X      |
| PUT         | X      | O      |
| PATCH       | X      | X      |
| DELETE      | X      | O      |
| HEAD        | O      | O      |
| OPTIONS     | O      | O      |
| CONNECT     | X      | X      |
| TRACE       | O      | O      |

* https://blog.tossbusiness.com/articles/dev-1



**안전한 메서드** : 안전한 메서드를 호출하는 동안 부작용을 일으키는 동작을 포함하지 않는다.

> 안전성이 보장된 메서드는 리소스를 변경하지 않는다.

-> 아무리 요청해도 서버의 상태를 바꾸지 않는 메서드

-> 정의한 요청 방식 중 [GET](https://www.rfc-editor.org/rfc/rfc9110#GET) , [HEAD](https://www.rfc-editor.org/rfc/rfc9110#HEAD) , [OPTIONS](https://www.rfc-editor.org/rfc/rfc9110#OPTIONS) , [TRACE](https://www.rfc-editor.org/rfc/rfc9110#TRACE) 방식은 안전한 것으로 정의되어 있다. 

**멱등성 메서드** : 연산을 여러 번 적용해도 결과가 달라지지 않는 성질을 갖는 메서드. 주로 분산 시스템에서 고려된다.

웹에서 HTTP 메서드 중 GET, PUT, DELETE 등은 멱등성을 가진다.

* GET 요청은 서버의 상태를 변경하지 않으므로, 한 번 요청하든 여러 번 요청하든 같은 결과를 얻는다. 
* DELETE 요청도 마찬가지로, 한 번이든 여러 번이든 요청한 리소스를 제거한다.
* PUT 요청은 한 번 사용하든 여러 번 사용하든 요청한 리소스의 상태는 항상 마지막에 전송된 요청 페이로드로 설정되기 때문에 결과가 같기 때문이다
*  반면에, PATCH 메서드는 리소스의 부분적인 수정을 가능하게하는데, 같은 PATCH 요청을 여러 번 보낼 경우, 각 요청이 리소스의 상태에 추가적인 변경을 가할 수 있다. 예를 들어, 특정 필드의 값을 증가시키는 PATCH 요청을 연속해서 보내면, 해당 필드의 값은 매번 증가한다.
  * 그러나 이는 PATCH 요청의 내용에 따라 달라질 수 있다.
  * 약 PATCH 요청이 특정한 상태로 리소스를 설정하는 방식이라면, 그 PATCH 요청은 멱등할 수 있다.

하지만 `PUT`은 리소스를 수정하고, DELETE는 메소드를 제거하므로 안전한 메소드라고는 이야기할 수 없다.



또한 대부분의 캐시 구현은 GET 및 HEAD만 지원한다.

## HTTP 상태 코드

https://developer.mozilla.org/ko/docs/web/http/status

* https://datatracker.ietf.org/doc/html/rfc2616#section-10

1. **1xx (Informational)**: 요청이 받아졌고, 프로세스가 계속되고 있다는 것을 나타냅니다.
   - 예: 100 Continue, 101 Switching Protocols
2. **2xx (Successful)**: 요청이 성공적으로 받아졌고, 이해되었으며, 승인되었다는 것을 나타냅니다.
   - 예: 200 OK, 201 Created, 204 No Content
3. **3xx (Redirection)**: 클라이언트는 요청을 완료하기 위해 추가적인 동작을 취해야 한다는 것을 나타냅니다. 대부분의 경우 URL 리디렉션을 나타냅니다.
   - 예: 301 Moved Permanently, 303 See Other, 304 Not Modified
4. **4xx (Client Error)**: 클라이언트 요청에 오류가 있음을 나타냅니다. 잘못된 문법, 유효하지 않은 요청 등이 이에 해당됩니다.
   - 예: 400 Bad Request, 401 Unauthorized, 404 Not Found
5. **5xx (Server Error)**: 서버가 유효한 요청을 수행하지 못했음을 나타냅니다. 서버 오류나 외부 의존성 실패 등이 이에 해당됩니다.
   - 예: 500 Internal Server Error, 502 Bad Gateway, 503 Service Unavailable

## 컨텐츠 협상과 MIME Type

```http
Accept: text/plain, text/html
Accept-Encoding: gzip, br
Accept-Language: ko-KR;q=0.9
Accept-Charset: utf-8
Transfer-Encoding: chunked
Content-Type: text/html; charset=utf-8
Content-Encoding: br
Content-Language: ko-KR
```

- `Accept`: 클라이언트가 이해할 수 있는 미디어 타입을 나타냅니다.
- `Accept-Encoding`: 클라이언트가 이해할 수 있는 콘텐츠 인코딩을 나타냅니다.
- `Accept-Language`: 클라이언트의 언어 선호도를 나타냅니다.
- `Accept-Charset`: 클라이언트가 이해할 수 있는 문자 인코딩을 나타냅니다.
- `Transfer-Encoding`: 메시지 바디가 어떻게 인코딩되었는지 나타냅니다. "chunked"는 메시지 바디가 여러 조각으로 분할되었음을 의미합니다.
- `Content-Type`: 실제 메시지 바디의 미디어 타입을 나타냅니다.
- `Content-Encoding`: 실제 메시지 바디가 어떻게 인코딩되었는지 나타냅니다.
- `Content-Language`: 메시지 내용의 자연어를 나타냅니다.

## Keep-Alive, Date, Transfer-Encoding

* https://developer.mozilla.org/ko/docs/Web/HTTP/Headers/Connection



1. **Keep-Alive**: Keep-Alive 헤더는 HTTP/1.1에서 도입되었으며, 클라이언트와 서버 사이에 열린 연결이 얼마나 오랫동안 유지될 것인지를 나타냅니다. 이것은 네트워크 연결에 대한 오버헤드를 줄이고 응답 시간을 향상시키는데 도움이 됩니다. Keep-Alive 헤더는 일반적으로 두 개의 파라미터, 즉 timeout과 max를 포함합니다. timeout은 연결이 유휴 상태로 있을 수 있는 최대 시간을 초 단위로 지정하고, max는 연결을 통해 전송될 수 있는 최대 요청 수를 지정합니다.
2. **Date**: Date 헤더는 메시지가 생성된 날짜와 시간을 나타냅니다. 이 헤더는 로깅, 응답 시간 측정, 캐시 유효성 등에 사용될 수 있습니다. Date 헤더의 값은 HTTP-date 형식의 타임스탬프입니다.
   * 생성된 시각이지, 도착한시각이 아니다 
3. **Transfer-Encoding**: Transfer-Encoding 헤더는 메시지 본문이 어떻게 인코딩되어 전송되었는지를 나타냅니다. 이 헤더의 가장 흔한 값은 "chunked"로, 이는 메시지 본문이 여러 개의 청크 또는 조각으로 분할되어 전송되었음을 나타냅니다. 청크 전송 인코딩은 메시지의 본문 길이를 미리 알 수 없을 때 유용하며, 본문을 생성하는 동안 동시에 전송을 시작할 수 있도록 해줍니다. 이것은 대규모 데이터를 전송할 때 유용합니다.

## Authorization, 기타 헤더, 커스텀 헤더

https://developer.mozilla.org/ko/docs/Web/HTTP/Headers/Authorization

* Authorization: Basic

* Authorization: Bearer

* Authorization: Digest

https://developer.mozilla.org/ko/docs/Web/HTTP/Authentication

### basic

**Basic**: Basic 인증 방식은 사용자 이름과 비밀번호를 Base64로 인코딩하여 전송한다. 

예를 들면, 사용자 이름이 'user'이고 비밀번호가 'pass'인 경우, `Authorization: Basic dXNlcjpwYXNz`와 같이 헤더가 설정된다. 

이 방식은 비교적 간단하지만 보안에 취약하다. 

인증 정보가 암호화되지 않고 단순히 인코딩만 되기 때문에 중간자 공격에 취약하다.

```http
GET /resource HTTP/1.1
Host: example.com
Authorization: Basic dXNlcjpwYXNz // user:pass
```

여기서 `dXNlcjpwYXNz`는 "user:pass"를 Base64로 인코딩한 결과

- 장점
  - 구현이 매우 간단하다. 클라이언트는 사용자 이름과 비밀번호를 Base64로 인코딩하기만 하면 되기 때문이다.
  - HTTP 프로토콜에 내장되어 있어서 대부분의 HTTP 클라이언트에서 지원한다.
- 단점
  - 보안성이 매우 낮다. Basic 인증은 사용자 이름과 비밀번호를 암호화하지 않고 Base64 인코딩만 사용하기 때문에, 트래픽을 가로채는 중간자 공격에 취약하다. 따라서 반드시 HTTPS와 같은 안전한 연결 상에서만 사용되어야 한다.
  - 서버에 인증 정보를 계속 보내야 하므로 세션 관리가 불가능하다.

### Bearer

**Bearer**: Bearer 인증 방식은 주로 OAuth 2.0에서 토큰을 전달하는 데 사용된다. 

이 방식은 토큰 기반의 인증 방식으로, 'Bearer' 다음에 공백을 한 칸 두고 토큰을 추가한다

예를 들면, `Authorization: Bearer your_token`와 같이 설정됩니다. 이 토큰을 이용해 서버는 클라이언트를 식별하고 인증한다.

```http
GET /resource HTTP/1.1
Host: example.com
Authorization: Bearer your_token
```

여기서 `your_token`은 실제 발급받은 토큰을 사용

- 장점
  - OAuth 2.0과 함께 사용하면, 클라이언트는 유저의 비밀번호를 서버에 보낼 필요 없이 토큰만으로도 인증을 할 수 있어 보안성이 높다.
  - Bearer 토큰은 자체적으로 인증 정보를 포함하고 있기 때문에, 분산 시스템에서 유용하게 사용될 수 있다.
- 단점
  - 토큰이 탈취당하면, 토큰을 탈취한 사람은 토큰이 만료되기 전까지 해당 유저로서의 모든 권한을 가진다.
  - 토큰 관리가 필요하다. 토큰의 만료 시간, 토큰의 갱신 등을 관리해야 하기 때문이다.

### Digest

**Digest**: Digest 인증 방식은 Basic 인증 방식의 보안 문제를 개선한 방식이다. 

사용자 이름과 비밀번호를 그대로 전송하는 대신, 비밀번호의 해시값을 전송하여 중간자 공격을 방지한다. 

그러나 이 방식은 설정이 복잡하고, 최신 인증 방식에 비해 사용이 감소하고 있다. 

Digest 인증 방식을 사용할 때는 헤더가 `Authorization: Digest username="user", realm="realm", nonce="nonce", uri="/", response="response"`와 같이 설정된다. 

* 여기서 'realm', 'nonce', 'response' 등의 값은 Digest 인증 절차에 따라 생성된다.

```http
GET /resource HTTP/1.1
Host: example.com
Authorization: Digest username="Mufasa",
                        realm="testrealm@host.com",
                        nonce="dcd98b7102dd2f0e8b11d0f600bfb0c093",
                        uri="/dir/index.html",
                        qop=auth,
                        nc=00000001,
                        cnonce="0a4f113b",
                        response="6629fae49393a05397450978507c4ef1",
                        opaque="5ccc069c403ebaf9f0171e9517f40e41"
```

Digest 인증에서는 비밀번호 해시를 생성하기 위해 여러 파라미터가 사용됩니다.

 `username`, `realm`, `nonce`, `uri`, `qop`, `nc`, `cnonce`, `response`, `opaque`는 서버와 클라이언트 간의 통신에 사용되는 값들

이 예제에서 사용된 각 값은 임의의 값을 사용한 것이며, 실제 사용 시에는 각 인증 요청에 맞는 값이 필요하다.

1. **username**: 인증할 때 쓰는 사용자 이름이다. 이메일이나 ID가 될 수 있다.
2. **realm**: 보호해야 하는 영역의 이름이다. 사용자한테 인증 요청할 때 이 이름을 보여준다.
3. **nonce**: 서버가 만들어 낸 일회용 랜덤 숫자다. 이걸로 클라이언트가 이전 인증 요청을 다시 쓰는 걸 방지한다.
4. **uri**: 요청한 주소, 즉 URI다.
5. **qop**: "quality of protection"의 줄임말로, 클라이언트가 알아들을 수 있는 보호 수준을 정의하는 거다. 보통 "auth"라는 값이 많이 쓰인다.
6. **nc**: "nonce count"의 줄임말로, 클라이언트가 nonce를 사용해서 몇 번 요청을 보냈는지를 나타내는 거다. 이건 16진수로 나타내고, 요청을 할 때마다 값이 증가한다.
7. **cnonce**: "client nonce"의 줄임말로, 클라이언트가 만든 일회용 랜덤 숫자다. 이걸로 서버가 클라이언트를 인증한다.
8. **response**: 클라이언트가 만든 특별한 해시 값이다. nonce, 사용자 이름, 비밀번호, HTTP 메서드, 요청한 URI 등을 기반으로 이 값을 계산한다.
9. **opaque**: 서버가 주는 문자열인데, 클라이언트는 이걸 그대로 모든 인증 요청에 넣어야 한다. 이 문자열은 클라이언트가 처음 인증 요청을 보낼 때 서버에게 받고, 그 후의 모든 인증 요청에서 클라이언트는 이 값을 포함해야 한다. 이건 서버가 각 인증 세션을 추적하기 위한 거다.

- 장점
  - Basic 인증보다 보안성이 높다. 비밀번호를 해시 형태로 전송하기 때문에, 트래픽을 가로채는 중간자 공격에 비밀번호가 노출되는 것을 방지할 수 있다.
  - 비밀번호를 평문으로 저장할 필요가 없다.
- 단점
  - 설정과 관리가 복잡하다.
  - 중간자 공격에 완전히 안전한 것은 아니다. 예를 들어, 중간자가 통신을 가로채서 자신을 클라이언트로 가장할 수 있다.
  - HTTPS와 같이 사용하지 않으면, 다른 보안 위협에 노출될 수 있다.
  - 최신 웹 기술에서는 잘 사용되지 않는다.

## 쿠키

https://developer.mozilla.org/ko/docs/Web/HTTP/Headers/Cookie

* **`Cookie`** HTTP 요청 헤더는 [`Set-Cookie`](https://developer.mozilla.org/ko/docs/Web/HTTP/Headers/Set-Cookie) 헤더와 함께 서버에 의해 이전에 전송

https://developer.mozilla.org/ko/docs/Web/HTTP/Headers/Set-Cookie

* **`Set-Cookie`** HTTP 응답 헤더는 서버에서 사용자 브라우저에 쿠키를 전송하기 위해 사용

https://developer.mozilla.org/ko/docs/Web/HTTP/Cookies

* HTTP 쿠키(웹 쿠키, 브라우저 쿠키)는 서버가 사용자의 웹 브라우저에 전송하는 작은 데이터 조각

> 문서에 정말 다양하게 적혀있으니 반드시 읽어보자 

쿠키는 주로 세 가지 목적을 위해 사용됩니다:

- 세션 관리(Session management)

  서버에 저장해야 할 로그인, 장바구니, 게임 스코어 등의 정보 관리

- 개인화(Personalization)

  사용자 선호, 테마 등의 세팅

- 트래킹(Tracking)

  사용자 행동을 기록하고 분석하는 용도

```
Set-Cookie: <cookie-name>=<cookie-value>
Set-Cookie: <cookie-name>=<cookie-value>; Expires=<date>
Set-Cookie: <cookie-name>=<cookie-value>; Max-Age=<non-zero-digit>
Set-Cookie: <cookie-name>=<cookie-value>; Domain=<domain-value>
Set-Cookie: <cookie-name>=<cookie-value>; Path=<path-value>
Set-Cookie: <cookie-name>=<cookie-value>; Secure
Set-Cookie: <cookie-name>=<cookie-value>; HttpOnly

Set-Cookie: <cookie-name>=<cookie-value>; SameSite=Strict
Set-Cookie: <cookie-name>=<cookie-value>; SameSite=Lax

// Multiple directives are also possible, for example:
Set-Cookie: <cookie-name>=<cookie-value>; Domain=<domain-value>; Secure; HttpOnly
```



## 캐시(Cache-Control)

"Cache-Control"은 HTTP 헤더의 일부로, 웹 콘텐츠의 캐싱 동작을 제어하는 데 사용됩니다. 웹 브라우저나 프록시 서버 등은 이 헤더를 사용하여 웹 콘텐츠를 캐시(즉, 임시로 저장)하고, 이를 통해 더 빠른 응답 시간을 제공하며, 서버의 부하를 줄이고 네트워크 대역폭 사용을 최소화할 수 있습니다.

* https://developer.mozilla.org/ko/docs/Web/HTTP/Headers/Cache-Control

![image-20230730231330639](./images//image-20230730231330639.png)

"Cache-Control" 헤더는 다음과 같은 여러 디렉티브를 포함할 수 있습니다:

- `no-cache`: 클라이언트가 캐시된 응답을 사용하기 전에 원래 서버를 통해 이를 재검증해야 함을 나타냅니다.
- `no-store`: 클라이언트가 응답을 어떤 형태로든 캐시하지 말아야 함을 나타냅니다. 이는 민감한 정보가 포함된 웹 콘텐츠를 다룰 때 주로 사용됩니다.
- `max-age`: 웹 콘텐츠가 신선하게 유지되는 최대 시간(초)을 나타냅니다.
- `private`: 응답이 특정 사용자만을 위한 것이므로 공유 캐시에 저장되지 않아야 함을 나타냅니다.
- `public`: 응답이 공유 캐시에 저장될 수 있음을 나타냅니다. 이 디렉티브는 응답이 특정 사용자에게 개인화되어 있지 않은 경우에 주로 사용됩니다.

## 캐시 신선도 검사

## CORS