# RestDocs 문서 분리

* RestDocs 공식문서 - https://docs.spring.io/spring-restdocs/docs/2.0.5.RELEASE/reference/html5/



다음과 같이 User, Post 같이 여러 API들이 한 **index.adoc** 파일에 존재한다면 파일이 엄청 길어질 수 있어 분리하고 

include 하여 사용할 수 있다.

<img src="https://blog.kakaocdn.net/dn/DVxny/btrXwjOlxS1/DhBjSH2EhKbjJIk9XCXbZK/img.png" width=650 height=600>



1. 먼저 분리할 목차에 마우스 커서를 올리고 맥 기준 option + enter를 누르면 다음과 같은 창이 나온다 

<img src="https://blog.kakaocdn.net/dn/bAwaIb/btrXn6JJTYv/JdrXGlvE5pN2kysTzapkZ0/img.png" width = 800 height = 350>



2. Extract Include Directive를 클릭하면 인텔리제이가 해당 문서를 분리해준다.
   * src/docs/asciidoc 위치로 해당 API를 빼주게 된다.





3. **사용할 곳에서 include::filename.adoc[]** 을 이용하면 된다.

as-is

```text
.... 위에 더 많이 존재

==== request-fail - 존재하지 않는 게시글

include::{snippets}/posts-findAll-NotFound/http-response.adoc[]

include::{snippets}/posts-findAll-NotFound/response-fields.adoc[]



== Common
=== ErrorResponseFormat

include::{snippets}/posts-update-all-FailEmptyValue/http-response.adoc[]

include::{snippets}/posts-update-all-FailEmptyValue/response-fields.adoc[]
```



to-be

```text
==== request-fail - 존재하지 않는 게시글

include::{snippets}/posts-findAll-NotFound/http-response.adoc[]

include::{snippets}/posts-findAll-NotFound/response-fields.adoc[]


include::common.adoc[] // << 
```



분리된 common.adoc

```
== Common
=== ErrorResponseFormat

include::{snippets}/posts-update-all-FailEmptyValue/http-response.adoc[]

include::{snippets}/posts-update-all-FailEmptyValue/response-fields.adoc[]
```

