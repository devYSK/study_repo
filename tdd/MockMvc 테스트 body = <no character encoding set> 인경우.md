# MockMvc 테스트 body가  '<no character encoding set>' 인경우



@WebMvc 테스트 중 Request Body 데이터가 나오지 않는 상황이 발생.

```
MockHttpServletRequest:
      HTTP Method = POST
      Request URI = /api/v1/items
       Parameters = {_csrf=[89d3f355-ea84-4108-b910-eebcb6795735]}
          Headers = [Content-Type:"application/json", Accept:"application/json", Content-Length:"505"]
             Body = <no character encoding set> // 
    Session Attrs = {}

Handler:
         Type = com.prgrms.bdbks.domain.item.api.ItemController
         Method = com.prgrms.bdbks.domain.item.api.ItemController#createItem(ItemCreateRequest)
```



```
Body = <no character encoding set>
```



RequestBuilder 속성에 `characterEncoding(StandardCharsets.UTF_8)` 또는 `.characterEncoding("utf-8")` 를 추가해주면 된다

```java
mockMvc.perform(post(BASE_REQUEST_URI)
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)

				**.characterEncoding(StandardCharsets.UTF_8) // here**

				.content(objectMapper.writeValueAsString(request)
				)
			).andDo(print())
			.andExpect(status().isCreated());
```