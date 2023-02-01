## URI의 개념과 UriComponentsBuilder의 필요성

인터넷상에 존재하는 **모든** **자원(Resource)은** URI를 이용하여 그 위치를 나타낸다.

아래와 같이 다양한 구성요소로 이루어진 URI를 개발자가 **직접 문자열로 작성하는 것은 상당히 불편**한데,

```
scheme:[//[user[:password]@]host[:port]][/path][?query][#fragment]
```

* Scheme
* UserInfo
* Host
* Port
* Path
* Query
* Fragment



스프링에서는 URI를 보다 쉽게 다룰 수 있도록 도와주는 **UriComponentsBuilder**를 제공한다.



## UriComponents 클래스

UriComponents 클래스는 말 그대로, URI를 구성하는 Components들을 효과적으로 다룰 수 있도록 하는 클래스

URI에 대한 정보를 각 구성요소별로 분리하여 변수에 저장하여 가지고 있는 URI 구성요소의 집합



## UriComponentsBuilder 클래스

UriComponents 를 Build할 수 있도록 도와주는 클래스

**UriComponents** 클래스의 생성자는 모두 **package-private** 또는 **private** 이기 때문에 Builder클래스를 이용한다.

**Static Factory Method** 중에 하나를 이용하여 **UriComponentsBuilder** 객체를 생성한다.



### Static Factory Method의 종류

```java
- newInstance()

- fromPath(String)

- fromUri (URI)

- fromUriString (String)

- fromHttpUrl (String)
  
- fromHttpRequest (HttpRequest)

- fromOriginHeader (String)


```

### 대응되는 각 메서드를 이용하여 URI 구성요소를 설정하거나 인코딩을 설정

```java
- scheme(String) 

- userInfo(String)

- host(String) 

- port(String or int)

- path(String)

- queryParam(String, Object...)

- queryParams(MultiValueMap<String, String>)

- fragment(String) 

- encode(void or Charset) : void일 경우 UTF-8 로 인코딩

- expand(Map<String, ?> or Object... or UriTemplateVariables) : URI 템플릿 변수 값을 지정
```



**build()** 메서드를 이용하여 UriComponents 인스턴스를 Build 한다.

또는 **buildAndExpand()** 메서드를 이용하여 **URI 템플릿 변수를 설정한 후 Build**한다.



위 세 단계를 거치게 되면 최종적으로 **UriComponents** 인스턴스가 생성된다. 

```java
// 1. 간단한 링크 생성
UriComponents uriComponents1 = UriComponentsBuilder.newInstance()
    .scheme("https").host("blog.naver.com/aservmz").path("/222313864092").build(); 
    // https://blog.naver.com/aservmz/222313864092
        
//  2. URI 인코딩
UriComponents uriComponents2 = UriComponentsBuilder.newInstance()
    .scheme("http").host("www.example.com").path("/encodeTest test").build().encode();
    // http://www.example.com/encodeTest%20test
        
//  3. path 내에 템플릿 변수설정 방법 => path() 내에 {}로 변수명 지정 후 buildAndExpand()메서드에서 값 설정
UriComponents uriComponents3 = UriComponentsBuilder.newInstance()
    .scheme("http").host("www.example.com").path("/{키워드는}/{아무렇게나 지정해도 됩니다.}")
    .buildAndExpand("UriTemplate", "setting");
    // http://www.example.com/UriTemplate/setting
        
// 4. Query Parameter가 포함된 URI 생성
UriComponents uriComponents4 = UriComponentsBuilder.newInstance()
    .scheme("http").host("www.example.com").path("/{경로}")
    .query("q={queryValue1}").query("p={queryValue2}").buildAndExpand("testPath", "value1", "value2");
    // http://www.example.com/testPath?q=value1&p=value2

// 5. toUriString()을 이용한 방법
UriComponents uriComponents5 = UriComponentsBuilder
    .fromUriString("https://example.com/test/{testVariable}")
    .queryParam("q", "{q}").encode().buildAndExpand("test", "12345");
    // https://example.com/test/test?q=12345
```



### URI Encoding의 두 가지 종류

```java
UriComponentsBuilder 클래스의 encode()

- URI 템플릿을 먼저 인코딩한 후, URI 템플릿 변수 위치에 URI 변수 값을 삽입할 때 그 값을 인코딩한다.
```

```java
UriComponents 클래스의 encode()

- URI에 포함된 템플릿 변수 위치에 URI 변수 값을 삽입한 후에 URI 컴포넌트를 인코딩한다.
```



**UriComponents****의** **encode()**를 이용하는 경우, 이러한 값들이 **그대로 URI**에 나타나게 되며,

**UriComponentsBuilder****의** **encode()**를 이용하는 경우,  이 값들은 **Percent-Encoding** 되어 각각 **"%3B", "%2B"** 로 나타난다.

```java
UriComponents uriComponents1 = UriComponentsBuilder
    .fromUriString("https://example.com/test/{testVariable}")
    .queryParam("q", "{q}")
    .buildAndExpand("testVariable;", "123+45");

    uriComponents1.encode(); // UriComponents의 encode() 메서드 이용

// https://example.com/test/testVariable;?q=123+45

UriComponents uriComponents2 = UriComponentsBuilder
    .fromUriString("https://example.com/test/{testVariable}")
    .queryParam("q", "{q}")
    .encode() // UriComponentsBuilder의 encode() 메서드 이용 => 
    .buildAndExpand("testVariable;", "123+45");

// https://example.com/test/testVariable%3B?q=123%2B45
```





# ServletUriComponentsBuilder



### 2xx 코드의 종류

`2xx`코드의 종류는 아래와 같다.

- `201 Created` (새로운 리소스가 생성됨)
  - 요청 성공 후에 새로운 리소스가 생성된 경우이다.
  - 보통 생성된 리소스는 응답의 `Location` 헤더 필드로 식별한다.
- `202 Accepted` (요청 접수가 완료됐지만, 예약된 시간에 실행될 것임)
  - 즉시 어떠한 행동을 하지 않고 스케쥴링된 시간에 행위를 하겠다는 의미이다.
  - 이를테면 1시간 뒤에 배치 처리를 하겠다는 명령이 될 수 있다.
- `204 No Content` (서버가 요청을 성공적으로 수행했지만, 응답 페이로드 본문에 보낼 데이터가 없음)
  - 요청에 대한 응답은 성공적이지만, 딱히 성공으로 인해 생성된 데이터 등을 보낼 이유가 없을 때이다.
  - 이를테면 사용자 정보 수정 페이지에서 사용자 정보가 수정되었을 때, 화면에 이미 수정된 사용자 정보가 나와있다면, 굳이 반환해줄 이유가 없다.



**UriComponentsBuilder** 클래스를 상속하는 

**ServletUriComponentsBuilder** 클래스는 **이전 요청의 URI를 재사용**하여 보다 편리하게 URI를 사용할 수 있도록 하는 클래스

이 클래스는 **UriComponentsBuilder**의 **Static Factory Method**와 더불어, **추가적인 메서드를 제공**.

이 메서드들은 현재 **HttpServletRequest 객체의 정보를 이용**하여 URI를 구성

```java
- fromContextPath(HttpServletRequest)

- fromServletMapping(HttpServletRequest)

- fromRequestUri(HttpServletRequest)

- fromRequest(HttpServletRequest)

// 이 메서드들은 요청 객체를 RequestContextHolder로부터 얻는다는 점을 제외하고 위의 메서드들과 동일.

- fromCurrentContextPath()

- fromCurrentServletMapping()

- fromCurrentRequestUri()

- fromCurrentRequest()
```

예제

```java
// scheme, host, port, path, query string 을 재사용
ServletUriComponentsBuilder ucb = ServletUriComponentsBuilder.fromRequest(request)
        .replaceQueryParam("accountId", "{id}").build()
        .expand("123")
        .encode();

// scheme, host, port, context path 를 재사용
ServletUriComponentsBuilder ucb = ServletUriComponentsBuilder.fromContextPath(request)
        .path("/accounts").build()

// scheme, host, port, context path, Servlet prefix 를 재사용
ServletUriComponentsBuilder ucb = ServletUriComponentsBuilder.fromServletMapping(request)
        .path("/accounts").build()
```

위처럼 이전 요청의 URI에서 현재 필요한 정보들을 뽑아내어 다시 재작성하는 일 없이 유연하게 URI를 설정할 수 있다.

만약 현재 **fromRequest()**로 가져온 URI에 쿼리스트링이 있고, 이 값을 변경하고 싶다면 **replaceQueryParam()** 메서드를 이용





## 201 Created ServletUriComponentsBuilder URI 생성

`fromCurrentRequest()`를 사용하여 현재 요청된 Request 값을 사용하고,

`path`로 반환시킬 위치,

`buildAndExpand`에 새롭게 설정한 id값을 `path`에 지정시켜준다.

마지막으로 `toUri()`로 URI로 반환시켜준다.

리턴하기 위해 void를 `ResponseEntity<User>`로 우리가 도메인으로 지정했던 User를 반환하게 만든다.

```java
public class UserController {
    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user){
        User savedUser = service.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }
}
```



### 참조



* [Spring 공식문서 - Spring MVC Uri Building](https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-uri-building)

* [Spring Framework docs - UriComponentsBuilder](https://docs.spring.io/spring-framework/docs/5.3.6/javadoc-api/org/springframework/web/util/UriComponentsBuilder.html)

* [Spring Framework docs - UriComponents](https://docs.spring.io/spring-framework/docs/5.3.6/javadoc-api/org/springframework/web/util/UriComponents.html)

*  [Baeldung.com - Spring UriComponentBuilder](https://www.baeldung.com/spring-uricomponentsbuilder)

*  [Google Developers - URI Encoding](https://developers.google.com/maps/documentation/urls/url-encoding)

* https://blog.naver.com/PostView.naver?blogId=aservmz&logNo=222322019981