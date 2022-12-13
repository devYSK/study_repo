

# Pagination





## Pagination?

한정된 네트워크 자원을 효율적으로 활용하기 위해 특정한 정렬 기준에 따라 데이터를 분할하여 가져오는 것이다.

서버의 입장에서도 클라이언트의 입장에서도 **`특정한 정렬 기준에 따라 + 지정된 갯수`** 의 데이터를 가져오는 것

* 한 번에 수만개의 데이터를 데이터베이스에서 애플리케이션으로 가져오면 메모리가 모자를 수 있다.
* 많은 데이터를 전송하므로 네트워크의 오버헤드가 생긴다.



즉, 데이터베이스에 수만~수억개의 데이터가 있을 때, 전체를 한번에 전달하는 대신 0번부터 49번까지 50개씩 전달하는 것을 의미한다. 여기서 다음 요청이 들어오면 50번부터 99번까지, 또 다음 요청이 들어오면 100번부터 149번까지 돌려준다. 이렇게 함으로써 네트워크의 낭비를 막고, 빠른 응답을 기대할 수 있게 된다.



- 페이지네이션은 전체 데이터에서 지정된 갯수만 데이터를 전달하는 방법
- 필요한 데이터만 주고 받으므로 네트워크의 오버헤드를 줄일 수 있다.



페이지네이션은 보통 다음과 같은 두 가지 방식으로 처리할 수 있다. 

* 사실 시간 기반으로 페이지네이션도 가능한데 이것도 커서 일종의 커서기반이라고도 볼 수 있다. 



1. 오프셋 기반 페이지네이션 (Offset-based Pagination)
   * DB의 offset 쿼리를 사용하여 '페이지' 단위로 구분하여 요청/응답
2. 커서 기반 페이지네이션 (Cursor-based Pagination)
   * Cursor 개념을 사용하여 사용자에게 응답해준 마지막 데이터  item을 기준으로 다음 요청을 하는 것
   *  클라이언트가 item(커서)와 함께 다음 item들을 n개 요청/응답하게 구현  기준으로 다음 n개 요청/응답





## 1. offset-based Pagination (오프셋 기반 페이지네이션)

MySQL 에서라면 간단하게 OFFSET 쿼리와 LIMIT 쿼리에 콤마를 붙여 '건너 뛸' row 숫자를 지정하여 페이지네이션을 구현한다. 즉, 페이지 단위로 구분한다.

```mysql
select *
from post
order by create_at desc
limit [페이지사이즈] 
offset [페이지번호];
```

* limit : 가져올 데이터 숫자
* offset : 가져올 데이터 숫자로 전체 데이터를 숫자 만큼 나눈값을 0부터 번호를 매긴 페이지 번호



### 장점

* 직관적이고 구현하기 편하다 - JPA에서는 Pageable을 이용해서 쉽게 구현 (Page, Slice, Pageable)
* 유저가 특정 페이지를 선택하고, 이동할 수 있다.
* 전체 페이지의 갯수를 알 수 있다.



### 단점

1. 중복 데이터 발생

2. 데이터를 offset만큼 읽는데, offset이 많아질수록 성능이 느려진다. 성능 저하 -  OFFSET 쿼리의 퍼포먼스 이슈







## Offset 기반 단점 1. - 중복 데이터 발생

- 쓰기 빈도가 빈번한 테이블인 경우, 데이터를 조회하는 도중에 데이터가 추가로 적재되어 다음 페이지를 눌렀지만, 이미 보았던 데이터가 중복되어 보일수도있다.  
- 또한 이 사이에 적재된 데이터가 삭제된다면 다음 페이지 조회 시  없을 수도 있다.



#### 게시판 예



1. 게시판에 페이지 로딩

2. **최신 게시글을 보여주기 위해 id가 20~11인 게시글을 가져와서 보여준다.**

```sql
select *
from post
order by id desc
limit 10 # 가져올 개수
offset (0 * 10) # (몇 번째 페이지인지 * 가져올 개수)
```

* 이러면 id 가 desc 니까 11~20까지 가져오게 된다. 



3. **사용자가 글을 읽는 사이 사이 다른 사용자들이 새로운 게시글 5개를 생성했다.**

4. ** 사용자가 게시글을 다 구경하고, 다음 페이지 버튼을 눌러 요청했다.**

```sql
select *
from post
order by id desc
limit 10 # 가져올 개수
offset (1 * 10) # (몇 번째 페이지인지 * 가져올 개수) == 10개 데이터를 건너뛴다
```

- 현재 총 게시글 개수는 23개이다. (위에서 게시글 5개 추가 생성)
- 그러면 게시글 5개가 생겼으니 마지막 아이디는 25
- id가 25 ~ 16 인 게시글을 가져오게 되고, id가 16 ~20인 이미 보았던 중복 데이터를  또 가져오게 된다 

- 반대로 게시글이 삭제되는 경우에는, 특정 게시글이 조회되지 않는 현상도 발생한다.
  - 반대로 5개 글을을 삭제했다면 2페이지로 넘어갔을때 고객은 5개의 글을을 보지 못하게 된다.



> 평소에 게시판류 서비스를 이용하면서 다음 페이지로 넘어갔는데 이전 페이지에 있던 게시글이 보인다면, 해당 서비스는 오프셋 기반 페이지네이션으로 구현됐다고 보면 된다.





## Offset 기반 단점 2. 성능 저하 -  OFFSET 쿼리의 퍼포먼스 이슈

offset pagination은 offset 위치를 계산하고, 필요한 데이터를 찾을 때까지 테이블을 전체 스캔한다.

즉 offset크기만큼 지정된 데이터를 모두 읽고 모두 지정된 갯수(limit만큼)를 순회하여 자르는 방식이다.

* 때문에 퍼포먼스는 이에 비례하여 떨어지게 되어 있다. -> 뒤 페이지로 갈수록 성능은 더 느려짐 

- 만약 DB 메모리보다 스캔해야하는 데이터가 더 커지는 경우 오류가 난다 



예를 들어 `offset 10000, limit 20` 이라 하면 최종적으로 **10,020개의 행을 읽고**. (10,000부터 20개를 읽어야하니)

그리고 이 중 앞의 10,000 개 행을 버리게 된다. (실제 필요한건 마지막 20개뿐이니) -> DB 부하

* 뒤페이지로 갈수록 **버리지만 읽어야 할 행의 개수가 많아** 점점 뒤로 갈수록 느려지는 것 -> DB 부하



[Faster Pagination in Mysql – Why Order By With Limit and Offset is Slow?](https://www.eversql.com/faster-pagination-in-mysql-why-order-by-with-limit-and-offset-is-slow/)



> You may ask yourself “who the heck is going to skip to page 50,000 in my application?”.
> Let’s list few possible use cases:
>
> - Your favorite search engine (Google / Bing / Yahoo / DuckDuckGo / whatever) is about to index your ecommerce website. You have about 100,000 pages in that website. How will your application react when the search bot will try to fetch those last 50,000 pages to index them? How frequently will that happen?
> - In most web applications, we allow the user to skip to the last page, and not only the next page. What will happen when the user will try to skip to page 50,000 after visiting page 2?
> - What happens if a user landed in page 20,000 from a Google search result, liked something there and posted it on facebook for another 1000 friends to read?
>
> 아마 당신은 이런 의문이 들 수도 있어요. "도대체 어떤 할 일 없는 놈이 우리 앱에서 50,000 페이지나 스킵하겠어?"
>
> 가능한 케이스를 나열해볼까요?
>
> 1. 당신이 즐겨 쓰는 검색엔진 (구글/빙/야후/덕덕고/왓에버) 이 인덱싱을 위해 당신의 이-커머스 웹사이트에 방문하려고 해요. 당신의 서비스는 대충 10만개의 페이지를 갖고 있네요. 이런 상황에서 검색엔진 봇이 뒤에 있는 5만 페이지를 인덱싱하려고 할 때, 당신의 서비스 코드는 어떻게 응답해야 할까요? 이건 또 얼마나 자주 있을까요?
> 2. 대부분의 웹 어플리케이션은 유저에게 '마지막 페이지'로 바로 갈 수 있는 링크를 제공해요. 다음 페이지만 제공하는게 아니구요. 2페이지에 방문했던 유저가 갑자기 50,000번째 페이지로 넘어가려고 한다면 무슨 일이 일어날까요?
> 3. 어떤 유저는 구글 검색결과 페이지에 의해 20,000번째 페이지로 바로 접속했어요. 유저는 이 결과가 마음에 들었고, 페이스북에 이 주소를 1,000명의 친구에게 공유했어요. 그럼 어떤 일이 일어날까요?





다음과 같은 경우에는 offset 기반 페이지네이션을 사용해도 된다.



1. 데이터의 변화가 거의 없다시피하여 중복 데이터가 노출될 염려가 없는 경우
2. 일반 유저에게 노출되는 리스트가 아니라 중복 데이터가 노출되어도 크게 문제 되지 않는 경우
3. 검색엔진이 인덱싱 할 이유도, 유저가 마지막 페이지를 갈 이유도, 오래 된 데이터의 링크가 공유 될 이유도 없는 경우
4. 애초에 row 수가 그렇게 많지 않아 특별히 퍼포먼스 걱정이 필요 없는 경우

 이런 경우에까지 커서 기반을 고려할 필요가 없다. 많은 유저가 접속하는 서비스 페이지에는 철저하게 커서 기반을 사용해야 성능상 이슈가 없고, 백오피스나 어드민은 편한 오프셋 기반을 사용해도 된다. 



> 성능 저하 문제는 둘째 치더라도, **데이터 중복문제는 해결할 수 없다.**
> Cursor-based Pagination을 사용하면 위 문제점들을 모두 해결할 수 있다.



## Custor-based Pagination (커서 기반 페이지네이션)

**Cursor** 라는 개념을 사용하여 **offset을 사용하지 않**고 **Cursor** 를 기준으로 다음 n개의 데이터를 응답해주는 방식이다

* **Cursor**란 사용자에게 응답해준 마지막의 데이터의 식별자 값이 **Cursor**가 된다.

오프셋 기반 페이지네이션은 우리가 원하는 데이터가 '몇 번째 페이지'에 있다는 데에 집중하고 있다면, 커서 기반 페이지네이션은 우리가 원하는 데이터가 '어떤 데이터의 **다음**'에 있다는 데에 집중한다.

*  **n개의 row를 skip 한 다음 10개 주세요** 가 아니라, **이 row 다음꺼부터 10개 주세요** 를 요청하는 식.



여기서 cursor가 되는 컬럼은 **Unique**하거나 , 정렬가능(Orderable), 불변(Immutable)한 컬럼을 선택해야 한다. Time-based paging도 Cursor-based paging이지만 unique하지 않았다.

* 중복되지 않는 고유의 id를 정렬하여 커서로 페이지네이션 한다면 문제 될것이 없지만 중복될 수 있는 생성 날짜등으로 정렬하여 커서로 사용시 문제가 생길수 있다.
* 클러스터 인덱스인 PK를 조회 시작 부분 조건문으로 사용하면 빠르게 조회할 수 있다.
  * https://jojoldu.tistory.com/476

```mysql
SELECT * 
FROM post 
WHERE id <= {cursor} 
ORDER BY id DESC 
LIMIT {limit}
```

즉,

```mysql
SELECT *
FROM post
WHERE 조건문
AND id < 마지막조회ID # 직전 조회 결과의 마지막 id
ORDER BY id DESC
LIMIT 페이지사이즈
```

이전에 조회된 결과를 한번에 건너뛸수 있게 마지막 조회 결과의 ID를 조건문에 사용하는 것으로 이는 **매번 이전 페이지 전체를 건너 뛸 수 있음**을 의미한다.



그러므로 어떤 페이지를 조회하든 항상 원하는 데이터 개수만큼만 읽기 때문에 성능상 이점이 존재한다는 것이다.



이 방식을 사용하면 offset pagination의 2가지 문제를 해소할 수 있다.

- 인덱스가 적용된 값을 비교하기 때문에 테이블 풀스캔을 하지 않는다.
- id(unique, orderable, immutable) 같은 값으로 데이터를 조회하기 때문에, 데이터 쓰기가 빈번한 테이블이여도 다음 페이지네이션 조회 시 값이 누락되지 않는다. 
- 데이터 중복이 발생하지 않고, offset과 다르게 **이전의 데이터를 읽지 않고** 바로 다음 cursor에 대한 정보를 주면 되므로 **대량의 데이터를 다룰 때 성능상 좋다.**

* 대신 where절에 여러 조건이 들어가면 성능이 offset보다 안 좋다고 한다.
  - 여러 블로그에서 이야기해줬고 '페이스북이나 인스타그램도 그래서 정렬이 없구나'라고 생각했지만 유튜브에는 또 무한 스크롤로 정렬 기능이 있어서 더 알아봐야 할 것 같다.

### 게시판의 id를 기준으로 하는 예제

```mysql
# 첫 페이지 진입시 발생 쿼리
select *
from post 
order by id desc
limit 10;

# 이후 페이지 요청시 발생 쿼리
select *
from post
where id < 10 # ex) cursor값이 10인 경우
limit 10;
```

* 첫 페이지에 진입했을 때의 쿼리는 그냥 limit으로 10개 짤라서 주면 된다.

* 이후 페이지에 대한 요청은, 사용자에게 응답한 데이터 중 마지막 게시글 id가 Cursor가 된다.



## Cursor base Pagenation Example



```java
@Override
public Page<Post> findPostsByCategory(Long cursorId, Category category, Pageable pageable) {

    List<Post> findPosts = queryFactory.selectFrom(post)
						...
            .where(
                    cursorId(cursorId), // 동적 쿼리
      						  categoryEquals(category)
						...
            )
      			.orderBy(post.id.desc()) // 최신순으로
            .limit(pageable.getPageSize()) //  지정된 사이즈만큼
            .fetch();

    ...
      
    return ...
}

private BooleanExpression cursorId(Long cursorId){
    return cursorId == null ? null : post.id.lt(cursorId);
}
```

* **첫 페이지 조회할 때**와 **두번째 페이지부터 조회할 때** 사용되는 쿼리가 달라 동적 쿼리가 필요하다
* 첫 페이지를 조회할때는 기준이 되는 id 값을 알 수 없기 때문

- 첫 페이지를 조회할때는 `post.id.lt(postId)` 가 조건문에 없어야 하며
- 두번째 페이지부터는 `post.id.lt(bookId)`가 조건문에 들어가야 한다.
  - 첫번째 페이지는 그냥 orderby desc 한 최신순으로 보여주기만 하면 되기 때문이다.
  - querydsl의 where절에서 BooleanExpression가 null이면 where문을 실행하지 않는다. 



### 마지막 페이지에서 스크롤을 한다면?

* 데이터는 존재하지 않기 때문에 아무 데이터도 응답하지 않는다.
* 간단하게 다음 데이터가 존재하지 않는다는 의미를 포함한 필드를 추가해주면 좋다. 



### OR 연산자의 문제점

1. 대부분의 RDBMS는 WHERE에 `OR` 연산자 사용하면 인덱싱을 제대로 못 태움. < 가장 큰 이유. OR절 사용을 조심하자

2. **Cursor** 데이터들, 즉 ORDER BY에 걸려있는 모든 필드를 알아야 하고 매 페이지 요청시마다 이 값들을 클라이언트에서 알고  보내줘야 한다. 

   - 이 중 1번은 사용하는 DB가 어떤 것이냐에 따라 처리하는 방식이 달라, 고려해야 할 사항 역시 달라지게 된다
   - 그리고 커스텀 커서를 만들때 cursor 데이터를 통해 만들기 때문에 클라이언트가 전달해야하는 건 똑같으므로, 문제점이라고 볼 수 없다.

   

### 실행계획을 확인하자.

실행계획에서 인덱스 여부를 보려면 type부분을 보면 된다. 아래는 type 컬럼에 대한 설명이다.

| 구분            | 설명                                                         |
| --------------- | ------------------------------------------------------------ |
| system          | 테이블에 단 한개의 데이터만 있는 경우                        |
| const           | SELECT에서 PK 혹은 UK를 상수로 조회하는 경우로, 많아야 한건의 데이터만 존재 |
| eq_ref          | 조인할 때 PK 혹은 UK로 매칭하는 경우                         |
| ref             | 조인할 때 PK 혹은 UK가 아닌 Key로 매칭하는 경우              |
| ref_or_null     | ref와 같지만 NULL이 추가되어 검색되는 경우                   |
| index_merge     | 두 개의 인덱스가 병합되어 검색이 이루어지는 경우             |
| unique_subquery | IN절 안에 서브쿼리 결과가 PK인 경우                          |
| index_subquery  | unique_subquery와 같고, 서브쿼리 결과가 PK가 아닌 일반 인덱스인 경우 |
| range           | Index Range Scan 하는 경우                                   |
| index           | Index Full Scan 하는 경우                                    |
| all             | Table Full Scan 하는 경우                                    |



#### **단점**

모든 기술엔 trade-off가 있듯이 커서기반페이지네이션의 단점이 있다. 

1. where에 사용되는 기준 key가 중복이 가능할 경우이다. 이러면 정확한 값이 나오지 않는다.
   * **Cursor** 기반 페이지네이션을 구현할 때 **Cursor** 중 하나는 반드시 유니크한 값을 가져야 한다.

2. 회사 혹은 서비스 정책상 (or UX 관점에서) More 버튼은 안되며, 무조건 페이징 버튼 형식으로만 해야한다고 하면 답이 없습니다.
   - NoOffset 은 순차적으로 다음페이지 이동만 가능하며, 1페이지에서 갑자기 9페이지로 바로 건너 뛰는 등 일반적인 페이징 버튼으로서의 기능은 사용할 수 없다.

 

중요한 점은 커서기반 페이지네이션은 **인덱스를 통해서** 원하는 페이지의 게시글에 바로 접근하는 기술이다. 무턱대고 커서기반을 적용한다고 속도가 빠른 것은 아니다!



## Cussor based 페이지네이션 커스텀 (커서 기반 페이지네이션 커스텀)

* https://velog.io/@minsangk/%EC%BB%A4%EC%84%9C-%EA%B8%B0%EB%B0%98-%ED%8E%98%EC%9D%B4%EC%A7%80%EB%84%A4%EC%9D%B4%EC%85%98-Cursor-based-Pagination-%EA%B5%AC%ED%98%84%ED%95%98%EA%B8%B0
* https://velog.io/@znftm97/%EC%BB%A4%EC%84%9C-%EA%B8%B0%EB%B0%98-%ED%8E%98%EC%9D%B4%EC%A7%80%EB%84%A4%EC%9D%B4%EC%85%98Cursor-based-Pagination%EC%9D%B4%EB%9E%80-Querydsl%EB%A1%9C-%EA%B5%AC%ED%98%84%EA%B9%8C%EC%A7%80-so3v8mi2



## Time-based paging

최신순으로 정렬된 결과를 반환하고 반환된 결과 중 마지막 item의 timestamp를 다음 페이지를 요청 시 사용한다.

```mysql
// 한 페이지당 10개의 item을 보여주는 경우
select *
from post
where timeStamp < 10 # ex) cursor값이 10인 경우
limit 10;

```

Offset-based paging에서 발생하던 누락현상은 없어지지만 timestamp가 같은 item이 있을 경우 누락이 발생할 수 있다. 



## 결론

다음과 같은 경우에는 offset 기반 페이지네이션을 사용해도 된다.



1. 데이터의 변화가 거의 없다시피하여 중복 데이터가 노출될 염려가 없는 경우
2. 일반 유저에게 노출되는 리스트가 아니라 중복 데이터가 노출되어도 크게 문제 되지 않는 경우
3. 검색엔진이 인덱싱 할 이유도, 유저가 마지막 페이지를 갈 이유도, 오래 된 데이터의 링크가 공유 될 이유도 없는 경우
4. 애초에 row 수가 그렇게 많지 않아 특별히 퍼포먼스 걱정이 필요 없는 경우

 이런 경우에까지 커서 기반을 고려할 필요가 없다. 많은 유저가 접속하는 서비스 페이지에는 철저하게 커서 기반을 사용해야 성능상 이슈가 없고, 백오피스나 어드민은 편한 오프셋 기반을 사용해도 된다. 

---



그외 거의 모든 리스트는 커서 기반 페이지네이션을 사용하는 것이 무조건적으로 좋다.



1. 서버의 쿼리 퍼포먼스/ 클라이언트의 사용 편의를 위해서 커서는 사용할 값을 별도로 정의하고, 이 값을 활용한 WHERE / LIMIT 으로 커서 기반 페이지네이션을 구현 할수 있다.

2. 여기서 cursor가 되는 컬럼은 **Unique**하거나 , 정렬가능(Orderable), 불변(Immutable)한 컬럼을 선택해야 한다.

3. 인덱스에 대해 공부해야 한다. 

4.  where절에 여러 조건이 들어가면 성능이 offset보다 안 좋을 수 있고, OR 연산자의 문제점을 끌어 안을 수 있다. 
5. 마지막 페이지인지 아닌지 모를 수 있으니 hasNext같은 필드를 만들어 사용하자
6. **첫 페이지 조회할 때**와 **두번째 페이지부터 조회할 때** 사용되는 쿼리가 달라 동적 쿼리가 필요하다



### 참조



* https://jojoldu.tistory.com/528
* https://giron.tistory.com/131
* https://velog.io/@znftm97/%EC%BB%A4%EC%84%9C-%EA%B8%B0%EB%B0%98-%ED%8E%98%EC%9D%B4%EC%A7%80%EB%84%A4%EC%9D%B4%EC%85%98Cursor-based-Pagination%EC%9D%B4%EB%9E%80-Querydsl%EB%A1%9C-%EA%B5%AC%ED%98%84%EA%B9%8C%EC%A7%80-so3v8mi2

* https://velog.io/@minsangk/%EC%BB%A4%EC%84%9C-%EA%B8%B0%EB%B0%98-%ED%8E%98%EC%9D%B4%EC%A7%80%EB%84%A4%EC%9D%B4%EC%85%98-Cursor-based-Pagination-%EA%B5%AC%ED%98%84%ED%95%98%EA%B8%B0
* https://wonyong-jang.github.io/database/2020/09/06/DB-Pagination.html