# Spring consumes와 produces



API의 URI를 Mapping할 때 서버에서 수신하는 데이터와 송신하는 데이터 타입을 강제함으로써 오류 상황을 줄일 수 있다. 



**@RequestMapping의 produces 속성을 이용하여 Response의 Content-Type을 제어할 수 있다.**

* @GetMapping, @PostMapping, @PutMapping, @DeleteMapping 등도 지원한다.



@Consumes : 수신 하고자하는 데이터 포맷을 정의한다. - 수신 데이터 제한

> Cosumes는 **클라이언트가 서버에게 보내는 데이터 타입을 명시**

@Produces : 응답(출력)하고자 하는 데이터 포맷을 정의한다. - 응답 데이터 제한 

> **Produces는 서버가 클라이언트에게 반환하는 데이터 타입을 명시**



동일한 URL인데 **Content-Type 헤더에 따라 다른 Controller method를 실행**시키려면 아래와 같은 @RequestMapping 애노테이션의 consumes, produces 속성을 이용할 수 있다. 



## Consumes

예를 들어서 내가 json타입을 받고 싶다면 아래와 같이 처리가 가능하다.

```java
@PostMapping(path = "/pets", consumes = MediaType.APPLICATION_JSON_VALUE) 
public ResponseEntity<?> create(@RequestBody PostCreateRequest request ) {
    // ...
}
```

이렇게 처리를 하게되면 해당 uri를 호출하는 쪽에서는 헤더에 보내는 데이터가 json이라는 것을 명시해야 한다.

```
Content-Type:application/json
```



> **Content-Type**은 **요청을 받는 쪽에서 데이터를 어떻게 해석해야 하는 지를 명시**하기 위한 헤더로
>  **Request(요청)**과 **Response(응답)**에서 **모두 사용할 수 있다.**
>
> **consumes**는 **해당 컨트롤러 메서드가 처리할 Content-Type(요청 헤더)를 제한한다.**



부정형 또한 지원한다.

공식문서에 보면 아래와 같이 적혀있다.

```
The `consumes` attribute also supports negation expressions — for example, `!text/plain` means any content type other than `text/plain`.
```



기존에 json 방식으로 요청을 날려 수신하던 API를 계속 json 방식을 제공하되, 

추가적으로 `x-www-url-encoded` 방식으로도 서버가 수신하는 API를 제공을 해야한다면?

```java
@PostMapping(path = "/post", consumes = MediaType.APPLICATION_JSON_VALUE)
public ResponseEntity<Dto> create(@RequestBody PostCreateRequest request) {
    // ..
}

@PostMapping(path = "/post", consumes = MediaType.APPLICATION_FORM_URLENCODED)
public ResponseEntity<Resource> createImage(@RequestBody PostImageCreateRequest request) {
    // ..
}
```



>  consumes 때문에 에러가 난다면 415 에러 가 발생한다.

## Produces

produces는 반환하는 데이터 타입을 정의한다.

```java
@GetMapping(path = "/post/{postId}", produces = MediaType.APPLICATION_JSON_VALUE) 
@ResponseBody
public ResponseEntity<?> findPost(@PathVariable Long postId) {
    // ...
}
```

Produces에 타입을 json으로 지정하는 경우 반환 타입이 json으로 강제된다.

내가 보내야 하는 타입이 정해져 있다면 해당 부분을 정의하면 된다.

요청하는 입장에서 특정 타입의 데이터를 원한다면 아래와 같은 내용을 헤더에 추가한다.

```
Accept:application/json
```



> **Accept 헤더**는 **요청하는 입장**에서 **특정 타입의 데이터를 원할 때** 사용한다.



만약 두 개의 Controller method가 아래와 같이 있다고 가정하자.

```java
@PostMapping(path = "/resources", produces = MediaType.APPLICATION_JSON_VALUE)
public ResponseEntity<Dto> createResources(@RequestBody ResourceRequest request) {
    // ..
    return dto;
}

@PostMapping(path = "/resources", produces = MediaType.APPLICATION_OCTET_STREAM)
public ResponseEntity<Resource> findImageResource(@RequestBody StreamResourceRequest request) {
    // ..
    return imageResource;
}
```

이제 해당 url(/resources)로 요청을 보낼 때 **기대하는 결과(Accept 헤더)**에 따라서 **다른 종류의 반환 값을 내려줄 수 있다.**

 

가령 `Accept:application/json` 헤더로 요청한다면 JSON을 내려주고, `Accept:application/octet-stream` 헤더로 요청하면 해당 이미지를 내려주도록 처리가 가능하다.

 



# 요약

- consumes는 클라이언트가 서버에게 보내는 타입을 명시한다.
- produces는 서버가 클라이언트에게 반환하는 데이터 타입을 명시한다.
- consumes와 produces를 이용하여 동일한 URI를 다른 메서드로 처리할 수 있고. **기대하는 결과(Accept 헤더)**에 따라서 **다른 종류의 반환 값을 내려줄 수 있다.**



### 참조

*  https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-ann-requestmapping-consumes
* [https://docs.spring.io/spring-framework/doc](https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-ann-requestmapping-produces)
* https://jaehoney.tistory.com/297