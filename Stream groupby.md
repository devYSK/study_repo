# [Springdocs ]Swagger Error - Failed to fetch.



* [환경](##환경)
* [개요 및 원인](##개요-및-원인)
* [해결 방법](##해결-방법)
* [여러 Server를 추가하여 요청 처리](##여러-Server를-지정하여-요청-처리)





## 환경

* springboot 2.6.7
* swagger - springdoc - openapi-ui 1.6.6



```groo
dependencies {
  implementation 'org.springdoc:springdoc-openapi-ui:1.6.6'
}
```



## 개요 및 원인

Spring Boot 애플리케이션의 docs로 Swagger를 사용하던 중 다음과 같은 에러가 발생했습니다.



Failed to fetch.
Possible Reasons:

* CORS
* Network Failure
* URL scheme must be "http" or "https" for CORS request.

<img src="https://blog.kakaocdn.net/dn/b8Ixw5/btrLzRQCANP/eAyoQslf6akUWIKkYEOuC0/img.png" width=600 height=250>



원인은, Server의 Host가 https로 설정되었는데 

스웨거의 requestUrl http로 발생하여서 생긴 문제였습니다.



즉 https로 요청을 받아야 하는데, http로 요청이 날라와 문제가 생긴것.



```sh
curl -X 'GET' \
	'http://서버URL' \
	...
```



스웨거 페이지의 상단의 Servers (요청을 보내는 곳) 설정도 http로 되어있었으니 수정해야합니다.

<img src="https://blog.kakaocdn.net/dn/cR6myo/btrLwz4vb2L/3fueJRdA7acVIAKxzKcj40/img.png" width=750 height=150>





문제를 해결할라면 Swagger의 요청을 https로 바꿔줘야 했습니다.



## 해결 방법

스프링 부트 프로젝트 내에 스웨거의 설정을 추가해서 문제를 해결할 수 있습니다. 



다음 이슈를 참고하여도 해결 할 수 있습니다. 

* https://github.com/springfox/springfox/issues/3483
* https://github.com/springdoc/springdoc-openapi/issues/118



https://idratherbewriting.com/learnapidoc/pubapis_openapi_step3_servers_object.html



* 페이지에 나와있는 servers 설정입니다.

```yaml
servers:
- url: https://api.openweathermap.org/data/2.5/
  description: Production server
- url: http://beta.api.openweathermap.org/data/2.5/
  description: Beta server
- url: http://some-other.api.openweathermap.org/data/2.5/
  description: Some other server
```

* 이 페이지를 보면 Servers에 url을 추가하는 것을 볼 수 있습니다. 



프로젝트 내에 Configration Class로 스웨거 설정을 따로 관리하는데 다음과 같이 추가하였습니다. 



```java
@Configuration
public class SwaggerConfig extends WebMvcConfigurationSupport {

    @Bean
    public OpenAPI customOpenApi() {

        return new OpenAPI()
                .addServersItem(new Server().url("/").description("https 설정"))
                .info(info);
    }
```

* OpenAPI()의 addServerItem 메서드를 호출하여 새 Server를 추가해줍니다.



<img src="https://blog.kakaocdn.net/dn/cfFfiq/btrLApzDUII/YCZgxcfNSvvIawLt2rkc1K/img.png" width=750 height=200>

* "/" url 아이템이 추가된것을 볼 수 있습니다







## 여러 Server를 지정하여 요청 처리

* Servers에 여러 Server를 지정하여 등록할 수 있습니다. 

* 지정하여 처리할 때 url의 규칙을 알아야 합니다. 



---

![img](https://blog.kakaocdn.net/dn/coRU8V/btqUYHzqKoF/aukvQle0MCQnf1hcMAblJ1/img.png)

* 그림 : URL 구조
  * protocol: 통신규약, 사용자가 서버에 접속할 때 어떤 방식으로 통신할 지 정의한다.
    * HTTP(Hyper Text Transfer Protocol): 웹 브라우저와 웹 서버가 서로 데이터(하이퍼 텍스트)를 주고받기 위해 만든 통신규약
    * HTTPS(Hyper Text Transter Protocol Secure): HTTP에서 보안이 강화된 버전. 자세한 설명은 영상을 참고
  * host(domain): 인터넷에 접속되어 있는 각각의 컴퓨터를 가리키는 주소 또는 도메인
  *  port: 포트번호 
    * 한 개의 컴퓨터엔 여러 개의 서버가 존재할 수 있다. 즉, 포트번호를 통해 어떤 서버를 이용할 지 결정한다.
    * 웹서버는 전세계적으로 80번 포트를 이용하는 것이 표준이다. 
    * 따라서 url엔 기본적으로 포트번호 80이 생략되어 있다
  * path:  컴퓨터 내부에 있는 디렉토리의 파일을 또는 자원의 경로를 의미한다.
  * query string: 데이터를 전달하는 데 사용된다.
    * 쿼리 스트링의 시작은 "?"이고, 각 key-value쌍은 "&"로 구분된다.

---

* `프로토콜콰 호스트, 포트를 나누어 처리할 수 있습니다.`

```yaml
// application.yml

server:
  protocol: https
  host: 서버 host
```



* 프로퍼티에 정의한 옵션을 주입받아 사용합니다. 
* 물론 프로파일에 따라 다른 값을 주입받을 수 있습니다. 

```java
@EnableWebMvc
@Configuration
public class SwaggerConfig extends WebMvcConfigurationSupport {

    @Value("${server.protocol}")
    private String protocol;

    @Value("${server.host}")
    private String host;

    @Bean
    public OpenAPI customOpenApi() {

        return new OpenAPI()
                .addServersItem(new Server().url("/"))
                .addServersItem(new Server().url(protocol + "://" + host).description("https 호스트"))
          			// description은 생략 가능. 원하면 추가해도 된다.
    						// 이하 생략 ...
    }
```



* 다음과 같이 Servers에 추가된 것을 볼 수 있습니다.
* 이제 원하는 Server로 요청을 보내면 됩니다.

<img src="https://blog.kakaocdn.net/dn/bkVneX/btrLzoA79nM/fjr2E5fYmh3MTjcHGyyxD0/img.png" width=650 height=300>

* 요청을 보내보면 다음과 같이 https로 잘 동작합니다.

```sh
curl -X 'GET' \
	'https://도메인' \
	...
```





