

# Spring Data Jpa Cursor based Pagenation (커서 기반 페이지네이션) 예제



### 환경

* Spring boot 2.7.6

* hibernate 5.6.4



> QueryDsl를 사용하면 더 편리하겠지만, Spring Data Jpa Repository Interface만을 이용하여 조회하는 예제이다.
>
> Repository는 Interface이고, Interface에 페이징을 계산하는 비즈니스 연산을 넣는것은 옳지 않다고 생각되어 Service Class에서 연산하여 Repository에 위임한다. 



---



Post라는 게시글의 cursor를 postId로 가정하고, postId와 createdAt의 역순으로 조회하는 커서 기반 페이지네이션  예이다.

이 때, 다음 게시물이 있는지 없는지 여부를 표현하기 위해 응답객체에 hasNext를 추가했다.



* Post Class

```java
@Entity
@Table(name = "posts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank
    @Size(min = 1)
    private String title;

    @Column(nullable = false)
    @NotBlank
    private String content;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    private Long createdBy;
  
}
```

* 예제를 간단하게 하기 위해 연관관계 및 AbstractCreatedColumn(BaseEntity 역할)을 지우고 그냥 필드로 넣었다.



* PostService

```java
@Service
public class PostService {

    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public PostResponses findAllByIdCursorBased(Long cursorId, int pageSize) {

        Pageable pageable = PageRequest.of(0, pageSize + 1);

        List<Post> posts = findAllByCursorIdCheckExistsCursor(cursorId, pageable);

        boolean hasNext = hasNext(posts.size(), pageSize);

        return new PostResponses(
            toSubListIfHasNext(hasNext, pageSize, posts).stream()
                .map(PostResponse::new)
                .collect(Collectors.toList()),
            cursorId, hasNext);
    }

    private List<Post> findAllByCursorIdCheckExistsCursor(Long cursorId, Pageable pageable) {
        return cursorId == null ? postRepository.findAllByOrderByIdDesc(pageable)
            : postRepository.findByIdLessThanOrderByIdDescCreatedAtDesc(cursorId, pageable);
    }

    private boolean hasNext(int postsSize, int pageSize) {
        if (postsSize == 0) {
            throw new EntityNotFoundException(Post.class);
        }

        return postsSize > pageSize;
    }

    private List<Post> toSubListIfHasNext(boolean hasNext, int pageSize, List<Post> posts) {
        return hasNext ? posts.subList(0, pageSize) : posts;
    }

}
```



* PostRepository

```java
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByOrderByIdDescCreatedAtDesc(Pageable pageable);

    List<Post> findByIdLessThanOrderByIdDescCreatedAtDesc(@Param("id") long id, Pageable pageable);

}
```

* 쿼리메소드가 끝이다. 
* findAllByOrderByIdDescCreatedAtDesc : 모든것을 조회하되 Id랑 CreatedAt 을 Desc 로 정렬
* findByIdLessThanOrderByIdDescCreatedAtDesc : Id 보다 작은것을 조회하되, Id랑 CreatedAt 을 Desc 로 정렬



## 코드 설명

```java
private List<Post> findAllByCursorIdCheckExistsCursor(Long cursorId, Pageable pageable) {
        return cursorId == null ? postRepository.findAllByOrderByIdDescCreatedAtDesc(pageable)
            : postRepository.findByIdLessThanOrderByIdDescCreatedAtDesc(cursorId, pageable);
    }

```

커서 기반 페이지네이션은, 커서가 없을때는 (커서가 null일때) 최근데이터 (혹은 가장 오래된 데이터)를 조회해야 한다.

그래서 cursorId가 null일때 와 null이 아닐때 를 나누어 쿼리하도록 했다.

* findAllByOrderByIdDescCreatedAtDesc 과 findByIdLessThanOrderByIdDescCreatedAtDesc는 쿼리 메소드 네이밍으로 만든 JpaRepository 메서드이다. 

 

#### findAllByOrderByIdDescCreatedAtDesc 의 실행 쿼리 - cursorId가 null일때 

```sql
Hibernate: 
    select
        post0_.id as id1_0_,
        post0_.created_at as created_2_0_,
        post0_.created_by as created_3_0_,
        post0_.content as content4_0_,
        post0_.title as title5_0_,
        post0_.user_id as user_id6_0_ 
    from
        posts post0_ 
    order by
        post0_.id desc,
        post0_.created_at desc limit ?
```



#### findByIdLessThanOrderByIdDescCreatedAtDesc의 실행 쿼리 = cursorId가 null이 아닐 때

```sql
Hibernate: 
    select
        post0_.id as id1_0_,
        post0_.created_at as created_2_0_,
        post0_.created_by as created_3_0_,
        post0_.content as content4_0_,
        post0_.title as title5_0_,
        post0_.user_id as user_id6_0_ 
    from
        posts post0_ 
    where
        post0_.id<? 
    order by
        post0_.id desc,
        post0_.created_at desc limit ?
```



### HasNext

```java
@Service
public class PostService {

    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public PostResponses findAllByIdCursorBased(Long cursorId, int pageSize) {

        Pageable pageable = PageRequest.of(0, pageSize + 1);

        List<Post> posts = findAllByCursorIdCheckExistsCursor(cursorId, pageable);

        boolean hasNext = hasNext(posts.size(), pageSize);

        return new PostResponses(
            toSubListIfHasNext(hasNext, pageSize, posts).stream()
                .map(PostResponse::new)
                .collect(Collectors.toList()),
            cursorId, hasNext);
    }
		
  	...
      
    private boolean hasNext(int postsSize, int pageSize) {
        if (postsSize == 0) {
            throw new EntityNotFoundException(Post.class);
        }

        return postsSize > pageSize;
    }
		...
}


```

* `Pageable pageable = PageRequest.of(0, pageSize + 1);` 이부분이 다음 페이지가 있는지 알 수 있는 핵심이다
  * 1개가 아니여도 일부러 몇개를 더 조회해서, 요청한 pageSize 와 비교를 해서 다음 페이지가 있는지 여부를 알 수 있다.
  * 데이터 조회한 갯수가 요청한 페이지 수 보다 많다면 다음 페이지가 존재
  * 데이터 조회한 갯수가 요청한 페이지 수 보다 적다면 다음 페이지가 존재하지 않고 마지막 페이지 인것이다. 
* postsSize는 조회한 데이터 List의 Size인데, 조회한 데이터가 없다면 404를 응답하기위해 예외를 던졌다.
* 만일 예외를 던지지 않으려면, 그냥 false를 리턴해도 된다. 



### toSubListIfHasNext

```java
private List<Post> toSubListIfHasNext(boolean hasNext, int pageSize, List<Post> posts) {
        return hasNext ? posts.subList(0, pageSize) : posts;
}
```

* 다음 페이지가 있는지 여부(hasNext)를 알기 위해 일부러 몇개 더 조회했으니, 다음 페이지가 있다면 요청한 페이지 사이즈 보다 데이터가 많을것이다.
* 그러므로 요청한 페이지 수만큼 데이터를 맞춰서 돌려줘야 한다.
* 현재 예제에서 클라이언트가 만약 10개를 요청했고, 데이터가 10개보다 많다면, 11개를 조회했으니 요청한 10개만큼 잘라서 돌려줘야 한다. 
* 데이터가 클라이언트 가 요청한 수 보다 작다면, 그냥 조회 한 만큼 돌려주면 되는것이다. 
* 이 때 `정렬 순서`에 따라 `잘라야 하는 위치가 달라지니까` 꼭 테스트를 해봐야 한다. 



