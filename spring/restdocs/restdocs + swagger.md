

# RestDocs와 Swagger를 같이 사용하는 방법



## Swagger Vs Spring Rest Docs

스웨거(swagger)나 rest docs 둘다 코드를 통해 API 문서를 만들어주는것은 동일하다. 

그러나 다음과 같은 차이점과 장단점이 있다.

|      |                       Spring Rest Docs                       |                           Swagger                            |
| :--: | :----------------------------------------------------------: | :----------------------------------------------------------: |
| 장점 | - 프로덕션 코드에 영향이 없다. <br />- 테스트 코드가 성공해야(Controller Layer) 문서 작성이 가능하다. | - 문서상에 API를 테스트할 수 있는 기능이있다. <br />- 테스트 코드가 필요없으므로 적용이 쉽다.<br /> (어노테이션을 적용 안해도 Controller를 읽고 자동으로 적용. <br />어노테이션을 추가하면 추가 설명을 적을 수 있다.) |
| 단점 | - 테스트 코드를 작성해야 하므로 적용이 불편하다. <br />- 문서를 위한 테스트 코드를 관리해야 한다. | - 프로덕션 코드에 테스트와 관련된 애노테이션이 추가된다. <br />(프로덕션 코드에 추가되기 때문에 아주 지저분한 코드가 될 수 있다.) |

 

정리하자면, RestDocs의 장점 / 단점은 다음과 같다.



- **테스트가 성공**해야 문서 작성된다.
  - **Spring REST Docs**는 테스트가 성공하지 않으면 문서를 만들 수 없다. 
  - 따라서 **Spring REST Docs**로 문서를 만든다는 것은 API의 신뢰도를 높이고 더불어 테스트 코드의 검증을 강제로 하게 만드는 좋은 문서화 도구이다.
- 실제 코드에 **추가되는 코드가 없다.**
  - 프로덕션 코드와 분리되어있기 때문에 **Swagger**같이 Config 설정 코드나 어노테이션이 우리의 프로덕션 코드를 더럽힐 일이 없다.

* 단점으로는, 적용하기가 어렵고 테스트코드가 길어진다. 



Spring REST Docs를 이용하기 좋았지만 Swagger의 아름다운 UI와 좋은 사용성도 놓치기는 아쉬웠습니다. 동료들과 의견을 나누던 중 'OpenAPI Specification(이하 OAS)'를 이용한 Swagger와 Spring REST Docs의 장점을 합치는 방법을 알게 되었고 이를 구현한 방법





## How??



Spring REST Docs의 장점과 Swagger의 아름다운 UI와 좋은 사용성을 'OpenAPI Specification(이하 OAS)'를 이용해서 합칠 수 있다.



### OpenAPI Specification 

* https://www.openapis.org/

OpenAPI Specification은 예전엔 Swagger Specification으로 알려졌었다.

OpenAPI Specification은 Rest API에 대해 문서화를 하기 위한 사양을 정의한 것으로 특정한 소프트웨어나 라이브러리가 아니다.

SmartBear Software 회사가 자사 swagger framework에서 REST Api를 문서화하기 위해 사용하던 Swagger Specification을 공개하면서 Linux Foundation의 OpenAPI Initiative project로 관리가 이전되었다.



독일 기업 epages에서 Spring REST Docs를 연동하여 [OAS 파일을 만들어주는 오픈소스(restdocs-api-spec)](https://github.com/ePages-de/restdocs-api-spec)를 제공하고있다.

* 이 오픈소스를 이용해서 OAS 파일을 생성하고 Swagger-UI로 띄우면 되는 것





### Swagger

`Swagger`는 `Swagger`와 `Swagger-UI`로 나뉜다.

`Swagger`로 코드를 작성하면 `OpenAPI` 코드가 작성되고, 이를 `Swagger-UI`로 시각화 해주는식으로 동작한다.

즉, 가장 큰 장점이라고 생각되는 `Swagger-UI`와 가장 큰 불편을 느끼는 `Swagger` 코드 작성을 따로 놓고 볼 수 있다는 뜻이다.



> [ ePages-de/restdocs-api-spec GitHub](https://github.com/ePages-de/restdocs-api-spec)



`Spring Rest Docs`의 테스트 코드를 활용해 `OpenAPI`를 생성해주는 오픈소스 라이브러리이다.  

`Spring Rest Docs`의 스펙을 최대한 따라가기 위해 작성됐으며 `Spring Rest Docs`와 동일하게 `MockMvc`, `WebTestClient`, `RestAssured`를 모두 지원한다.  

 `OpenAPI`와 `OpenAPI3.0`으로 둘다 생성할 수 있다.  





### [구현](https://tech.kakaopay.com/post/openapi-documentation/#구현)

구현 순서는 다음과 같다.

1. Swagger-UI standalone, Static Routing 세팅
2. restdocs-api-spec을 이용한 OAS 파일을 생성하는 빌드 환경 구축
3. 생성된 OAS 파일을 Swagger 디렉터리로 복사하는 스크립트 작성(copyOasToSwagger)
4. Controller 코드 작성
5. MockMvc REST Docs Test 코드 작성



### build.gradle 설정

```groovy
// Gradle Plugin DSL을 사용하는 경우
plugins {
    id 'com.epages.restdocs-api-spec' version '0.16.0' // 버전은 현재 버전에 맞게 바꾸면 된다 
}

// buildscript를 사용하는 경우
buildscript {
    repositories {
        maven {
            url "https://plugins.gradle.org/m2/" 
        }
    }
    dependencies {
        classpath "com.epages:restdocs-api-spec-gradle-plugin:0.16.0"
    }
}

apply plugin: 'com.epages.restdocs-api-spec'
```

* https://github.com/ePages-de/restdocs-api-spec#build-configuration 참고하자 여기에 다 적혀있다.

```groovy
repositories { //2.1
    mavenCentral()
}

dependencies {
    //..
    testImplementation('com.epages:restdocs-api-spec-mockmvc:0.16.0') 
    
}

openapi { //2.3  openapi를 사용하는 경우 설정
    host = 'localhost:8080'
    basePath = '/api'
    title = 'My API'
    description = 'My API description'
    tagDescriptionsPropertiesFile = 'src/docs/tag-descriptions.yaml'
    version = '1.0.0'
    format = 'json'
}

openapi3 { //  openapi3.0을 사용하는 경우 설정
    server = 'https://localhost:8080'
    title = 'My API'
    description = 'My API description'
    tagDescriptionsPropertiesFile = 'src/docs/tag-descriptions.yaml'
    version = '0.1.0'
    format = 'yaml'
}

postman { //  PostMan을 사용하는 경우 설정
    title = 'My API'
    version = '0.1.0'
    baseUrl = 'https://localhost:8080'
}
```



**즉 다음과 같이 사용할 수 있다.**

```groovy
// Gradle Plugin DSL을 사용하는 경우
plugins {
    id 'com.epages.restdocs-api-spec' version '0.16.0' // 버전은 현재 버전에 맞게 바꾸면 된다 
}

repositories { //2.1
    mavenCentral()
}

dependencies {
    //..
    testImplementation('com.epages:restdocs-api-spec-mockmvc:0.16.0') 
    
}

openapi3 { //  openapi3.0을 사용하는 경우 설정
    server = 'https://localhost:8080'
    title = 'My API'
    description = 'My API description'
    tagDescriptionsPropertiesFile = 'src/docs/tag-descriptions.yaml'
    version = '0.1.0'
    format = 'yaml'
}

```



여기까지만 작업하고 Test 코드 없이 gradle에서 openapi3을 실행하면 /build/api-spec 디렉터리에 OAS 기본 구조가 작성된 openapi3.yaml 파일이 생성된다

```yaml
# /build/api-spec
openapi: 3.0.1
info:  
	title: My API  
	description: My API description  
	version: 0.1.0
servers:  
	- url: http://localhost:8080
tags: []
paths: {}
components:  
	schemas: {}
```





#### 생성된 OAS 파일을 Swagger 디렉터리로 복사하는 스크립트 작성(copyOasToSwagger)

openapi3 Task를 먼저 실행시켜 /build 디렉터리에 OAS 파일을 생성한 후, Swagger 디렉터리로 복사하는 Task를 작성한다. 

* Task를 실행시키면 test -> openapi3 -> copyOasToSwagger 순으로 Task가 진행되며 Swagger 디렉터리로 파일이 복사된다.



```groovy
tasks.register<Copy>("copyOasToSwagger") {
    delete("src/main/resources/static/swagger-ui/openapi3.yaml") // 기존 OAS 파일 삭제
    from("$buildDir/api-spec/openapi3.yaml") // 복제할 OAS 파일 지정
    into("src/main/resources/static/swagger-ui/.") // 타겟 디렉터리로 파일 복제
    dependsOn("openapi3") // openapi3 Task가 먼저 실행되도록 설정
}
```

Spring REST Docs의 extension을 포함하는 **restdocs-api-spec**과   

MockMvc wrapper를 제공하는 **restdocs-api-spec-mockmvc**를 이용해서 OAS 파일을 생성해야 한다.  
 RestAssured도 지원되니restdocs-api-spec-restassured를 사용해도 된다. 

build.gradle. 파일에 plugins와 dependency를 추가해주면 사용할 수 있다.   
깃헙에 [legacy plugin 예시](https://github.com/ePages-de/restdocs-api-spec#gradle)도 함께 제공되니 편한 것을 이용해서 환경을 잡아주면 된다.   
openapi, openapi3, Postman 등 다양한 output format이 제공되고 커스터마이징할 수 있다.



> OAS 파일 기본 생성 경로는 `/build/api-spec` 이다





### Swagger-UI 페이지 설치

[Swagger-UI 설치 페이지](https://github.com/swagger-api/swagger-ui/releases/tag/v3.51.1)

* https://github.com/swagger-api/swagger-ui



[![Swagger UI 정적 파일](https://tech.kakaopay.com/static/eccf8817b253fa71268b7bd15a33bd83/88c73/swagger-ui-dist.png)](https://tech.kakaopay.com/static/eccf8817b253fa71268b7bd15a33bd83/88c73/swagger-ui-dist.png)Swagger UI 정적 파일





[Swagger UI 정적 파일 설치 사이트](https://swagger.io/docs/open-source-tools/swagger-ui/usage/installation/) 하단에 있는 'Static files without HTTP or HTML' 부분에서 latest release를 다운 받아 /dist 디렉터리만 src/main/resources/static/docs 디렉터리로 복사한다.

* 디렉터리는 자신이 바꿀 수 있다. 

그리고 그중 `index.html`을 열어 스크립트를 변경해준다.

기존에 연동돼있는 데이터가 아닌 `Spring Rest Docs`로 생성된 `OpenAPI` 를 연동해줘야 한다.

- index.html
  - swagger-ui.html으로 이름 변경
  - 내부 js, css 경로를 static routing으로 적용
  - SwaggerUIBundle 경로는 생성될 yaml 파일의 경로로 입력
- 불필요한 파일 삭제
  - oauth2-redirect.html
  - swagger-ui.js
  - swagger-ui-es-bundle-core.js
  - swagger-ui-es-bundle.js

```javascript
<script>
window.onload = function() {
	
    // omitted for brevity
    
    url: "./open-api-3.0.1.json", // Spring Rest Docs로 생성된 OpenAPI를 연동
    
    // omitted for brevity
    
};
</script>
```





### Static Routing 세팅

정적 파일 경로에 알맞게 WebMvcConfigurer의 addResourceHandlers() 메서드를 작성.

```java
@Configurationclass 
public class StaticRoutingConfiguration implements WebMvcConfigurer {
  
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");       
    registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/static/swagger-ui/");
  }
}
```

##### 



### 다음에 테스트 코드를 작성한다.

기존 Spring REST Docs로 작성한 코드를 활용하려면 Spring의 MockMvcRestDocumentation을 MockMvcRestDocumentationWrapper로 바꿔주면 된다.

  


기존 코드의 document 생성 부분에 다음과 같이 MockMvcRestDocumentationWrapper를 추가한다.

document(...)인 부분이 MockMvcRestDocumentationWrapper.document(...)로 바뀐 것 말고 나머지는 모두 그대로다.

```
resultActions
  .andDo(
    MockMvcRestDocumentationWrapper.document(operationName,
      requestFields(fieldDescriptors().getFieldDescriptors()),
      responseFields(
        fieldWithPath("comment").description("the comment"),
        fieldWithPath("flag").description("the flag"),
        fieldWithPath("count").description("the count"),
        fieldWithPath("id").description("id"),
        fieldWithPath("_links").ignored()
      ),
      links(linkWithRel("self").description("some"))
  )
);
```

restassured를 사용한다면 restdocs-api-spec-restassured dependency를 추가하고 동일한 형태로 RestAssuredRestDocumentationWrapper를 추가하면 된다.

webtestclient를 사용한다면 restdocs-api-spec-webtestclient dependency를 추가하고 동일한 형태로 WebTestClientRestDocumentationWrapper를 추가하면 된다. 



## restdocs-spec-maven-plugin 추가하기

gradle plugin의 경우 restdocs-api-spec에서 기본 제공된다.

관련 설정도 해당 프로젝트에 자세히 안내되어 있다.

https://github.com/ePages-de/restdocs-api-spec#gradle-plugin-configuration

maven plugin의 경우 따로 프로젝트가 있다.

https://github.com/BerkleyTechnologyServices/restdocs-spec

asciidoctor 빌드 시 설정한 기존 plugin인 설정에 이어 restdocs-spec-maven-plugin을 추가하면 된다.

```
<plugin>
    <groupId>io.github.berkleytechnologyservices</groupId>
    <artifactId>restdocs-spec-maven-plugin</artifactId>
    <version>${restdocs-spec-maven-plugin.version}</version>
    <executions>
        <execution>
            <goals>
                <goal>generate</goal>
            </goals>
            <configuration>
                <specification>OPENAPI_V3</specification>
            </configuration>
        </execution>
    </executions>
</plugin>
```

좀 더 자세한 설정은 해당 사이트에 안내되어 있다.

별다른 설정 없이 plugin을 추가하면 기본 경로인 ${project.build.directory}/restdocs-spec에 api 문서가 생성된다.



## Swagger UI 설정하기

Swagger UI의 설정은 swagger-initializer.js에서 한다.

해당 파일을 열어보면 처음에는 다음과 같다.

```
window.onload = function() {
  //<editor-fold desc="Changeable Configuration Block">

  // the following lines will be replaced by docker/configurator, when it runs in a docker-container
  window.ui = SwaggerUIBundle({
    url: "https://petstore.swagger.io/v2/swagger.json",
    dom_id: '#swagger-ui',
    deepLinking: true,
    presets: [
      SwaggerUIBundle.presets.apis,
      SwaggerUIStandalonePreset
    ],
    plugins: [
      SwaggerUIBundle.plugins.DownloadUrl
    ],
    layout: "StandaloneLayout"
  });

  //</editor-fold>
};
```

샘플 예제 주소인 [https://petstore.swagger.io/v2/swagger.json를](https://petstore.swagger.io/v2/swagger.json) 호출해서 화면을 보여주는데 이 페이지의 경우 그냥 로컬에 있는 dist 폴더의 index.html을 열어도 확인할 수 있다.

이제 내가 사용하길 원하는 주소를 추가하면 되는데 단순히 url만 바꾸면 swagger ui의 최상단 Explore의 기본 호출 주소가 바뀌고 다른 주소를 가려면 일일이 다시 입력해야 한다.

사용하고자 하는 url만 모아서 보여주는 처리는 기존 url 설정을 지우고 urls 설정을 다음과 같이 추가하면 된다.

```
window.onload = function() {
  //<editor-fold desc="Changeable Configuration Block">

  // the following lines will be replaced by docker/configurator, when it runs in a docker-container
  window.ui = SwaggerUIBundle({
    urls: [ 
		{"name" : "테스트1", "url" : "https://petstore.swagger.io/v2/swagger.json"},
		{"name" : "테스트2", "url" : "https://다른API문서주소"}
	],
    dom_id: '#swagger-ui',
    deepLinking: true,
    presets: [
      SwaggerUIBundle.presets.apis,
      SwaggerUIStandalonePreset
    ],
    plugins: [
      SwaggerUIBundle.plugins.DownloadUrl
    ],
    layout: "StandaloneLayout"
  });

  //</editor-fold>
};
```

이렇게 추가하면 상단의 Explore가 다음과 같이 select 메뉴로 변경된다.



![img](https://blog.kakaocdn.net/dn/ddiNBO/btrE3MCJWyO/Ms31IkfrkL3JvOJh4aM4v1/img.png)



실제 사용하려면 호출 대상 서버에 Cors 설정을 하는 등의 추가 작업이 필요하겠지만 관련한 설명은 생략한다.

그 밖에도 다양한 설정을 할 수 있는데 이러한 설정에 대해서는 Swagger UI 문서를 참고하면 된다.

https://swagger.io/docs/open-source-tools/swagger-ui/usage/configuration/

[ Swagger DocumentationConfiguration How to configure Swagger UI accepts configuration parameters in four locations. From lowest to highest precedence: The swagger-config.yaml in the project root directory, if it exists, is baked into the application configuration object passedswagger.io](https://swagger.io/docs/open-source-tools/swagger-ui/usage/configuration/)

Swagger UI에서 호출하는 API 문서에 host관련 설정이 있으면 Swagger UI에서는 해당 주소를 호출하여 API 동작을 직접 테스트할 수 있다.

호출 주소에 대한 설정은 API 문서를 만드는 대상 프로젝트 maven pom.xml의 restdocs-spec-maven-plugin configuration에 설정하면 된다.

```
<plugin>
    <groupId>io.github.berkleytechnologyservices</groupId>
    <artifactId>restdocs-spec-maven-plugin</artifactId>
    <version>${restdocs-spec-maven-plugin.version}</version>
    <executions>
        <execution>
            <goals>
                <goal>generate</goal>
            </goals>
            <configuration>
                <specification>OPENAPI_V3</specification>
                <host>해당API주소</host>
            </configuration>
        </execution>
    </executions>
</plugin>
```



# 추가 설명



## OpenApi.Tools 소개

이제까지 OpenAPI 문서를 사용하기 위해 Asciidoctor extension인 Spring Rest Docs와 restdocs-api-spec, restdocs-spec-maven-plugin 및 Swagger UI를 사용하는 것에 대해서 설명했다.

OpenAPI는 공개된 specification이기 때문에 이를 이용한 수많은 오픈 소스가 파생되고 있다.

어떤 게 있는지 궁금하다면 아래 사이트를 가보면 된다.

https://openapi.tools/

[ OpenAPI.ToolsWriting YAML by hand is no fun, and maybe you don't want a GUI, so use a Domain Specific Language to write OpenAPI in your language of choice. Name Language v3.1 v3.0 v2.0 GitHub BOATS - BOATS allows for larger teams to contribute to multi-file OpenAPI defopenapi.tools](https://openapi.tools/)

이 글에서는 Swagger UI를 사용하는 것에 대해 설명했지만 위 사이트의 Tool Types에서 documentation 항목을 가보면 다양한 tool이 존재한다.

OpenAPI를 활용하는 방법은 다양하므로 위 사이트를 살펴보면서 상황에 맞는 tool을 찾아보면 좋을 것 같다.



## SwaggerHub 소개

Swagger UI에서 사용하는 API 문서를 직접 만들어보거나 테스트하고 싶거나 혹 다른 사람이 만든 걸 참고하고 싶은 경우 Swagger Hub를 구경하면 된다.

https://app.swaggerhub.com/

swagger hub는 docker hub와 비슷한데 API document를 관리하는 곳이다.

3 public 까진 무료이고 팀 단위나 기업 단위로 사용하려면 비용이 든다.

https://swagger.io/tools/swaggerhub/pricing/

굳이 사용하지 않더라도 다른 사람의 설정들을 참고하기 좋기 때문에 소개해본다.



## 추가 옵션 설정

swagger ui configuration : https://swagger.io/docs/open-source-tools/swagger-ui/usage/configuration/

아래와 같은 값들을 추가로 설정하고 있다.

```
window.ui = SwaggerUIBundle({
    urls: [
	    // … 이하 생략
    ],
    operationsSorter : "alpha",
    withCredentials : true,
    queryConfigEnabled : true,
    //... 이하 생략
}
```



추가로 설정한 옵션들의 역할은 다음과 같다.

| Parameter Name     | Description                                                  |
| ------------------ | ------------------------------------------------------------ |
| urls               | 기본 설정인 url을 사용하면 검색창에 해당 url이 설정되고 다른 url들을 검색할 수 있게 제공함 하지만 urls로 설정하면 설정된 url들만 사용할 수 있는 select 형태로 변경됨 |
| operationSorter    | 기본 설정은 서버가 반환한 순서대로 이지만 'alpha' (주소를 알파벳 순 정렬) 또는 'method' (주소를 HTTP method 순으로 정렬) 로 설정할 수 있음 |
| withCredentials    | true 로 설정 시 요청에 header cookie값을 같이 보냄           |
| queryConfigEnabled | 위에 선언한 urls의 셀렉트 박스를 통해 url을 설정하면 해당 선택에 대한 주소관련 parameter가 붙는데 이 parameter를 사용하는 옵션 (이 옵션을 활성화 해야 주소 복사로 공유가 가능) |





### 이런 이슈도 있다. 

> JWT 적용시에는 어떻게 해야 하는지 궁금합니다.
> 아래와 같이 JWT(Authroization) 설정하였을 경우 json으로 결과물은 제대로 만들어지고
> Swagger ui 입력 폼에도 잘 나오지만 실제로 try it out 버튼을 클릭했을 경우
> Request를 요청할때 Request Header에 Authorization값을 넣지 않고 요청하더라구요.
>
> ```
> .requestHeaders(
>     headerWithName("Authorization").description("bearer ${access_token}")
> )
> ```

* openAPI 설정으로 해결할 수 있을 것 같다.



## [참고문헌](https://tech.kakaopay.com/post/openapi-documentation/#참고문헌)

- https://tech.kakaopay.com/post/openapi-documentation/
- https://luvstudy.tistory.com/186
- https://shirohoo.github.io/backend/test/2021-07-17-swagger-rest-docs/
- Documenting Your Existing APIs: API Documentation Made Easy with OpenAPI & Swagger. https://swagger.io/resources/articles/documenting-apis-with-swagger
- What’s the Difference Between Swagger and OpenAPI?. https://nordicapis.com/whats-the-difference-between-swagger-and-openapi
- Swagger UI - Installation. https://swagger.io/docs/open-source-tools/swagger-ui/usage/installation
- Spring REST Docs API specification Integration. https://github.com/ePages-de/restdocs-api-spec
- Swagger와 Spring Restdocs의 우아한 조합 (by OpenAPI Spec). https://taetaetae.github.io/posts/a-combination-of-swagger-and-spring-restdocs
