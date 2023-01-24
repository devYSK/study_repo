# MockMvc 테스트시 201 created URI를 검증하는 방법 

현재 ItemController에서는 Resource 생성 시 201 created 응답과 함께 생성된 자원의 URI를 리턴해주고있다.

```java
@RequestMapping("/api/v1/items")
@RestController
@RequiredArgsConstructor
public class ItemController {

	private final ItemFacadeService itemService;

	@PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<URI> createItem(@RequestBody @Valid ItemCreateRequest itemCreateRequest) {
		Long itemId = itemService.createItem(itemCreateRequest);

		String createdURI = ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString() + "/" + itemId; // 현재 요청이 들어온 URI

		return ResponseEntity.created(URI.create(createdURI)).build();
	}
}
```



> ServletUriComponentsBuilder.fromCurrentRequestUri() : 현재 요청이 들어온 URI



MockMvc를 이용한 테스트시에  **andDo(print()) 메서드**로 다음의 헤더 응답정보가 있을 시에는

```java
MockHttpServletResponse:
           Status = 201
    Error message = null
          Headers = [Location:"<http://localhost/api/v1/items/1>"]
     Content type = null
             Body = 
    Forwarded URL = null
   Redirected URL = <http://localhost/api/v1/items/1>
          Cookies = []
```

아래 두 방법을 사용하면 된다



# 1. header().string(key, value)

```java
import org.hamcrest.Matchers.*;

mvc.perform(request)
  .andExpect(status().isCreated())
  .andExpect(header().string("Location", containsString("/resources/"+id)));
```

# 2. redirectUrlPattern()

```java
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

private static final String BASE_REQUEST_URI = "/api/v1/items";

mvc.perform(request)
  .andExpect(status().isCreated())
  .andExpect(redirectedUrlPattern("http://*" + BASE_REQUEST_URI + "/" + returnId));
```



```
.andExpect(redirectedUrlPattern("http://*/the/resources"))
```

이 패턴은 모든 host 이름과 일치하므로 *localhost* 를 하드코딩할 필요가 없다 . 
 `AntPathMatcher`이다. 
 **[SpringDoc - 여기에서](http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/util/AntPathMatcher.html)** 사용할 수 있는 다양한 패턴에 대해 자세히 알아볼 수 있다. .

