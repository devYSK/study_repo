

# Cannot call sendError() after the response has been committed - 순환 참조 문제



이 문제는 Jackson을 이용한 직렬화/ 역직렬화 과정에서 발생한다.

일반적으로 JPA Entity에서 양방향 관계를 맺었을 떄, 컨트롤러의 API 응답결과로 Entity를 반환하면 Jackson은 응답 결과 객체의 필드를 바탕으로 JSON을 만든다. 이 때 양방향 관계로 서로를 참조하고 있으니 무한하게 재귀적으로 참조를 하다가 StackOverFlow가 발생하여 직렬화를 못하고 에러를 발생시키는 문제이다.



* 응답이 커밋된 후 sendError()를 호출할 수 없다.

##  **Infinite Recursion** - 무한 재귀 원인

먼저 Jackson 무한 재귀 문제 원인을 살펴보자

간단한 일대 다 관계를 가진 User와 Item이라는 두 개의 엔티티가 있따.

```java
public class User {
    private int id;
    private String name;
    private List<Item> userItems;
}

public class Item {
    private int id;
    private String itemName;
    private User owner;
}
```



```java
public class TestController {
  
  private final UserService userService;
  
  @GetMapping("/api/user/{userId}")
  public User getUser(@PathVariable Long userId) {
  	return userService.getUser(userId);  
  }
}
```

위와 같이 User를 직렬화 하려고 하면 서로 무한히 참조되는 무한 재귀 문제가 발생한다.

User는  Item들을 참조하고, Item은 User를  서로 계속 참조한다. Controller 를 통해 Response 되어 Json으로 표시될 때는 이렇게 두 개의 entity 가 계속해서 서로를 불러오면서 페이지 가득 똑같은 데이터가 중복되어 노출된 것이다.



## 해결 방법



1. @JsonIgnore 어노테이션 사용 - 이 어노테이션을 붙이면 json 데이터에 해당 프로퍼티는 null
1. @JsonManagedReference, @JsonBackReference 사용 -  순환참조를 방어하기 위한 어노테이션
1. DTO를 사용 - entity 자체를 return하지 말고, dto 객체를 만들어 필요한 데이터만 옮겨 담아 client로 리턴
1. 연관관계 매핑  - 만약 양쪽에서 접근할 필요가 없다면 단방향 맵핑을 하면 자연스레 순환참조가 해결



>  @JsonIgnore의 경우는 실제로 property에 null을 할당하는 방식이고 @JsonManagedReference와 @JsonBackReference는 본질적으로 순환참조를 방어하기 위한 Annotation이다. 
>
> 
>
>   json serialize 과정에서 null로 세팅하고자 하면 @JsonIgnore 사용하면 되고, 순환참조에 대한 문제를 해결하고자 한다면 부모 클래스측에 @JsonManagedReference를, 자식측에 @JsonBackReference를 Annotation에 추가해주면 된다.

## 1. Jackson 직렬화 무시 어노테이션 사용 - @JsonIgnore @JsonIgnoreProperties @JsonIgnoreType



> 직렬화와 역직렬화
>
> 직렬화 -**직렬화는 객체를 파일의 형태 등으로 저장하거나, 통신하기 쉬운 포맷으로 변환하는 과정**
>
> * 객체의 직렬화는 객체의 내용을 바이트 단위로 변환하여  파일 또는 네트워크를 통해서 스트림(송수신)이 가능하도록 하는 것을 의미한다. 
>
> 역직렬화 - **특정 포맷으로 직렬화된 데이터는 역직렬화라는 과정을 통해 다시 객체로 변환**
>
> * 직렬화된 파일 등을 역으로 직렬화하여 다시 객체의 형태로 만드는 것을 의미한다. 저장된 파일을 읽거나 전송된 스트림 데이터를 읽어 원래 객체의 형태로 복원한다.



@JsonIgnore  필드 레벨에서 무시 될 수있는 속성을 표시하는 데 사용된다.

> @JsonIgnore 어노테이션은 클래스의 속성(필드, 멤버변수) 수준에서 사용
> @JsonIgnoreProperties 어노테이션은 클래스 수준(클래스 선언 바로 위에)에 사용.
> @JsonIgnoreType 어노테이션은 클래스 수준에서 사용되며 전체 클래스를 무시.



## 2.  @JsonManagedReference, @JsonBackReference 사용

순환참조를 방어하기 위한 Annotation이다. 부모 클래스에 @JsonManagedReference를, 자식 클래스측 에 @JsonBackReference 어노테이션을 추가해주면 된다.

> - @JsonManagedReference -> 참조의 전방 부분을 관리하고 이 주석으로 표시된 필드는 직렬화되는 필드.
> - @JsonBackReference -> 참조의 반대 부분을 관리하고 이 주석으로 표시된 필드/컬렉션은 직렬화되지 않는다.(생략)

즉, ManagedReference쪽에서만 직렬화를 시도하고 BackReference쪽은 직렬화를 시도 안하니 순환 참조가 해결된다. 

```java
public class User {
    private int id;
    private String name;
  	@JsonManagedReference
    private List<Item> userItems;
}

public class Item {
    private int id;
    private String itemName;
    @JsonBackReference
    private User owner;
}
```



## 3. DTO를 사용 

발생하게 된 주 원인은 양방향 매핑이기도 하지만, 더 정확하게는 entity 자체를 response로 리턴한데에 있다.

엔티티에서 앙방향 관게가 맺어있으니 서로 무한히 참조하는 관계가 만들어진것이다.

양방향 참조가 되어있는 엔티티 자체를 리턴하지 말고, DTO들 끼리 단방향 참조만 하는 DTO를 만들어  필요한 데이터만 옮겨 담아 client로 리턴하면 순환참조와 관련된 문제는 애초에 발생하지 않게 방어할 수 있다. 



## 4. 연관관계 매핑 재설정

이 엔티티들이 양방향 참조가 필요한지 다시 생각해보고 설계를 바꾸던지 하면된다. 

만약 양쪽에서 참조하여 앙방향 매핑을 할 필요 없고, 접근할 필요가 없다면 단방향 맵핑을 하면 자연스레 순환참조가 해결된다.



## 추가로 읽어보면 좋다

https://www.baeldung.com/jackson-bidirectional-relationships-and-infinite-recursion



