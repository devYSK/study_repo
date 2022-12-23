## Jackson 직렬화 무시 어노테이션 사용 - @JsonIgnore @JsonIgnoreProperties @JsonIgnoreType



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



### @JsonIgnore

@JsonIgnore 어노테이션이 붙은 필드는 직렬화에서 제외된다.

완전히 제외하려는 경우 접근자(종종 getter 메서드이지만 setter, 필드 또는 생성자 매개변수일 수 있음) 중 하나에 주석을 추가하기만 하면 된다

```java
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotation
public @interface JsonIgnore
{
    boolean value() default true; // true이면 직렬화 무시. false이면 직렬화 시도. 기본값 true
}

```



```java
public class User {
    public int id;
    public String name;
  	@JsonIgnore
    public List<Item> userItems;
}
```



```
{
	"id" : 1,
	"name" : ysk
}
```



### @JsonIgnoreProperties 

무시할 속성이나 속성 목록을 표시하는 데 사용

```java
@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE,
    ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotation
public @interface JsonIgnoreProperties
{
  public boolean allowGetters default false; //특정 필드가 직렬화는 허용하지만 역직렬화는 허용하지 않게 해주는 어노테이션
  public boolean allowSetters() default false; // allowGetters와 반대로 역직렬화는 허용하나 직렬화는 허용하지 않게 해주는 어노테이션 
}
```



```java
@JsonIgnoreProperties({"userItems", "id"})
public class User {
    public int id;
    public String name;
  
    public List<Item> userItems;
}
```

```
{
  "name" : ysk
}
```



###  @JsonIgnoreType

- 주석이 달린 형식의 모든 속성을 무시하도록 지정하는 데 사용
- 즉 클래스 자체를 JSON 데이터 맵핑에 사용불가

```java
public class User {
	private int id;
	private Name name;
  
	private List<Item> userItems;
  
  @JsonIgnoreType
  public static class Name {
    
    private String firstName;    
    private String lastName;
  }
}
```



```java
{
	"id" : 1
}
```



### @JsonAutoDetect

**멤버변수**로만 Jackson을 구성하고 싶은 경우 `@JsonProperty`를 일일이 붙이는 것보다 아래와 같이 설정하는 것이 더 편리.

```java
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class User {
   private String name;
}
```

`@JsonAutoDetect`는 **멤버변수** 뿐만 아니라, *`Getter`*, *`Setter`*의 데이터 매핑 정책도 정할 수 있다.  
 만약 아래의 경우는 **멤버변수** 뿐만 아니라, 기본정책인 *`Getter`*역시 데이터 매핑이 진행된다.

```java
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class User {
   private String name;
   
   public String getJob() {
       return "Developer";
  }
}
```

*`Getter`*를 제외하고 싶다면, `@JsonIgnore` **API**를 써도 된다.

```java
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class User {
   private String name;
   
   @JsonIgnore
   public String getJob() {
       return "Developer";
  }
}
```

하지만, 역시 일일이 붙여야 하는 상황이 온다면 매핑 정책을 바꾸는게 좋다.

```java
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NON_PRIVATE)
public class User {
   private String name;
   
   public String getJob() {
       return "Developer";
  }
}
```

> *`Getter`*정책으로 *private* 만 데이터 바인딩에 제외.
>
> 이렇듯, 제외 범위를 설정할 수 있다. 자세한건 아래를 참고
>
> https://fasterxml.github.io/jackson-annotations/javadoc/2.9/com/fasterxml/jackson/annotation/JsonAutoDetect.Visibility.html

```java
@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotation
public @interface JsonAutoDetect
{
    // 자동 감지되는 메서드(및 필드)를 제한하는 데 사용할 수 있는 가능한 가시성 임계값(최소 가시성)에 대한 enum.
    enum Visibility {
        
        ANY, // private부터 public까지 모든 종류의 aceess modifier가 허용됨을 의미하는 값.
      
        NON_PRIVATE, // 'private' 이외의 다른 액세스 수정자는 자동 감지 가능한 것으로 간주됨을 의미하는 값.
        
        PROTECTED_AND_PUBLIC, // 'protected' 및 'public'을 의미하는 값은 자동 감지 가능(및 'private' 및 "package access" == 수정자가 아님) 
        
        PUBLIC_ONLY, // 'public' 만 자동 감지 가능한 것으로 간주됨을 나타내는 값.
        
        NONE, // 지정된 유형에 대한 자동 감지를 명시적으로 비활성화하는 데 사용
        
        DEFAULT; // 기본 가시성 수준(컨텍스트에 따라 다름)이 사용됨을 나타내는 값

    }
    
}
```





### 참조

* https://mommoo.tistory.com/83