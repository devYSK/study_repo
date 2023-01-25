# MockMvc 테스트시 andExpect(jsonpath())를 이용한 검증방법

- [spring-framework/spring-test 테스트 예제 코드 Git](https://github.com/spring-projects/spring-framework/blob/master/spring-test/src/test/java/org/springframework/test/web/servlet/samples/standalone/resultmatchers/JsonPathAssertionTests.java)
- Hamcrest 프레임워크 : [hamcrest.org/JavaHamcrest/idnex](http://hamcrest.org/JavaHamcrest/index)
- [공식문서 깃허브 - 여기에 다 적혀있다. https://github.com/json-path/JsonPath](https://github.com/json-path/JsonPath에)

* 좋은 예제 - https://advenoh.tistory.com/28
  * https://joojimin.tistory.com/52



MockMvc를 이용하면 Json 형식의 API Response를 검증할 수 있다.

이 때 사용되는 것이 JsonPath() 이다.  



#### 주요 표현 예

다음은 https://github.com/json-path/JsonPath에서 제시하는 표현식 중 자주 사용되는 표현식에 대한 예이다. 

> 추가적인 표현식은 원문에 자세히 나와있다.



| 표현             | 의미                                                 |
| ---------------- | ---------------------------------------------------- |
| $                | 전달받은 json 객체의 root element                    |
| $.name           | root element의 name 속성                             |
| $.hobby[2]       | root element 중 hobby 속성이 배열이고 2번 index 요소 |
| $.hobby[2].name  | root element의 hobby 속성 중 2번 index의 name 속성   |
| $.hobby.length() | root element의 hobby 속성의 길이                     |





## JsonPath 표현법

- Dot 표현법

  - `$.store.book[0].title`

  

- Bracket 표현법
  - `$[’store’][‘book’][0][’title’]`



대표 연산자

| 연산자            | 설명                                                         |
| ----------------- | ------------------------------------------------------------ |
| $                 | 모든 Path 표현식의 시작, 루트 노드로 부터 시작하는 기호      |
| @                 | 처리되고 있는 현재 노드를 나타내고 필터 조건자에서 사용      |
| *                 | 와일드카드, 모든 요소와 매칭                                 |
| .                 | Dot 표현식의 자식노드                                        |
| [start:end]       | 배열 slice 연산자                                            |
| [?(<expression>)] | 필터 표현식으로 필터 조건자가 참인 경우에 매칭되는 모든 요소를 만을 처리한다 ex. book[?(@.price == 49.99)] |





- JsonPath 함수
  - JsonPath는 표현식 맨 마지막에 붙여 실행할 수 있는 다양한 함수를 지원합니다



제공 함수

| 기능     | 설명                                        | 반환값                 |
| -------- | ------------------------------------------- | ---------------------- |
| min()    | 숫자 배열의 최솟값 제공                     | Double                 |
| max()    | 숫자 배열의 최댓값 제공                     | Double                 |
| avg()    | 숫자 배열의 평균값 제공                     | Double                 |
| stddev() | 숫자 배열의 표준 편차값을 제공              | Double                 |
| length() | 배열의 길이 제공                            | Integer                |
| sum()    | 숫자 배열의 합계 제공                       | Double                 |
| keys()   | 속성의 키값들을 제공                        | Set<E>                 |
| concat() | 입력된 파라미터와 연결한 새로운 객체를 반환 | 파라미터와 동일한 객체 |
| append() | jsonPath 출력 배열에 새로운 아이템 추가     | 파라미터와 동일한 객체 |



- JsonPath 필터

  - 필터 [?(<expression>)] 표현 식을 가지며 <expression>에는 논리 연산자(ex. ==, <, >)와 기타연산자(ex. in, size, empty)로 true, false 값을 반환하는 표현 식이 들어갑니다

  - $[?(@.age == 23 )] : age가 23인 데이터만 반환한다

  - $[?(@.name == ‘Frank’)] : 이름인 Frank인 데이터만 반환한다

    

제공 필터

| 기능     | 설명                                                         |
| -------- | ------------------------------------------------------------ |
| ==       | 왼쪽 결과값과 오른쪽 값이 동일한지 비교 ( 단 1과 '1'은 다르다 ) |
| !=       | 왼쪽 결과값과 오른쪽 값이 동일하지 않은지 비교               |
| <        | 왼쪽 결과값보다 오른쪽 값이 클 경우                          |
| <=       | 왼쪽 결과값보다 오른쪽 값이 크거나 같을 경우                 |
| >        | 왼쪽 결과값이 오른쪽 값보다 클 경우                          |
| >=       | 왼쪽 결과값이 오른쪽 값보다 크거나 같을 경우                 |
| =~       | 왼쪽 결과값이 오른쪽에 정의된 정규식에 매칭되는 경우         |
| in       | 왼쪽 결과값이 오른쪽에 정의된 배열에 포함되는 경우           |
| nin      | not in, 왼쪽 결과값이 오른쪽에 정의된 배열에 포함되지 않는 경우 |
| subsetof | 왼쪽 결과값들이 오른쪽에 정의된 배열의 서브셋인 경우         |
| anyof    | 왼쪽 결과값들이 오른쪽에 정의된 배열 값 중에 매칭되는게 한개라도 있는 경우 |
| noneof   | 왼쪽 결과값들이 오른쪽에 정의된 배열 값 중에 매칭되는게 한개도 없는 경우 |
| size     | 왼쪽 결과값의 size(String or array)가 오른쪽 값과 같은 경우  |
| empty    | 왼쪽 결과값이 아무 것도 없을 경우                            |

## [반환된 모든 요소에 하위 문자열이 포함되어 있는지 테스트](https://stackoverflow.com/questions/68181725/mockmvc-testing-jsonpath-test-every-element-returned-contains-substring)



```java
mockMvc.perform(get(uri))
  .andExpect(jsonPath("$[*].name", Matchers.everyItem(Matchers.containsString("div3"))));
```



**테스트 메소드 예제 목록**

- JsonPath에 해당하는 값이 존재하는지
- JsonPath에 해당하는 값이 존해하지 않는지
- JsonPath에 해당하는 값의 동일성 비교
- JsonPath로 가져온 값을 hamcrestMatcher로 검증하기

 

**JsonPath에 해당하는 값이 존재하는지**

```java
@Test
public void exists() throws Exception {
    String expectByUsername = "$.[?(@.username == '%s')]";
    String addressByCity = "$..address[?(@.city == '%s')]";

    this.mockMvc.perform(get("/api/members"))
            .andExpect(jsonPath(expectByUsername, "우김영").exists())
            .andExpect(jsonPath(addressByCity, "서울").exists())
            .andExpect(jsonPath(addressByCity, "제주").exists())
            .andExpect(jsonPath("$..['username']").exists())
            .andExpect(jsonPath("$[0]").exists())
            .andExpect(jsonPath("$[1]").exists())
            .andExpect(jsonPath("$[2]").exists());
    }
```

- "$.[?(@.username == '%s')]" Json 표현식에 %s에 파라미터를 바인딩하여 검색할 수 있음
- 검색한 값이 존재하는 지 검증 할 수 있음
- "$..address[?(@.city == '%s')]"와 같이 JsonPath 표현식을 잘 조합하면 자식까지 검색 가능
- "$..['username']" 처럼 해당 요소 자체가 존재하는 지 검증 가능
- "$[0]" 처럼 몇 번째 요소가 존재 하는 지 검증 가능

 

**JsonPath에 해당하는 값이 존재하지 않는지**

```java
@Test
public void doesNotExist() throws Exception {
    this.mockMvc.perform(get("/api/test/members"))
       .andExpect(jsonPath("$.[?(@.username == 'Kyeongho Yoooooooo')]").doesNotExist())
       .andExpect(jsonPath("$.[?(@.username == 'Cheolsu Kiiiiiiim')]").doesNotExist())
      .andExpect(jsonPath("$[3]").doesNotExist());
}
```

- doesNotExist()를 사용하면 JsonPath로 값을 색적하여 값이 존재하지 않는지 검증할 수 있음

 

**JsonPath에 해당하는 값의 동일성 비교**

```java
@Test  
public void equality() throws Exception {
  this.mockMvc.perform(get("/api/test/members"))   
    .andExpect(jsonPath("$[0].username").value("Kyeongho Yoo"))  
    .andExpect(jsonPath("$[1].username").value("Minho Yoo"));

  // Hamcrest matchers를 이용한 검증
  this.mockMvc.perform(get("/api/test/members"))
    .andExpect(jsonPath("$[0].username").value(equalTo("Kyeongho Yoo")))
    .andExpect(jsonPath("$[0].address.city").value(equalTo("서울")));
}
```

- JsonPath로 특정한 값을 동일성 비교하여 검증할 수 있음

 

**JsonPath로 가져온 값을 hamcrestMatcher로 검증하기**

```java
@Test
public void hamcrestMatcher() throws Exception { 
  this.mockMvc.perform(get("/api/members")) 
    .andExpect(jsonPath("$[0].username", startsWith("김")))   
    .andExpect(jsonPath("$[0].username", endsWith("수")))    
    .andExpect(jsonPath("$[0].username", containsString("영")))     
    .andExpect(jsonPath("$[0].username", is(in(Arrays.asList("기명수", "으악")))));
}
```

- startsWith(), endsWith(), containsString(), is(in()) 등 Hamcrest가 제공하는 검증 메소드를 사용하여 다양한 방법으로 값을 검증할 수 있다.

 

```java
@Test
public void hamcrestMatcherWithParameterizedJsonPath() throws Exception {
  String expectByUsername = "$[%s].username";
      
  String addressByCity = "$[%s].address.city";

  this.mockMvc.perform(get("/api/test/members"))
    .andExpect(jsonPath(expectByUsername , 0).value(startsWith("김")))
    .andExpect(jsonPath(expectByUsername , 2).value(endsWith("수"))) 
    .andExpect(jsonPath(expectByUsername , 1).value(containsString("영")))
    .andExpect(jsonPath(addressByCity , 1).value(is(in(Arrays.asList("서울", "제주", "부산")))));
}
```

- 위와 같이 Hamcrest 검증 메소드를 jsonPath().value()에 사용할 수도 있다.



### 참조

* https://ykh6242.tistory.com/entry/MockMvc%EB%A5%BC-%EC%9D%B4%EC%9A%A9%ED%95%9C-REST-API%EC%9D%98-Json-Response-%EA%B2%80%EC%A6%9D

* https://goodteacher.tistory.com/265
