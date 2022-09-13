

# Java Stream groupby, groupingBy, sorting Lists after groupingBy



Java 8 Stream을 이용하여 특정 키값으로 벨류 값 (Value List) 들을 모으는 방법과 그 요소들을 정렬 하는 방법

Java 8 grouping, sorting Lists after groupingBy



Java 8 Stream을 이용하여 특정 키값으로 벨류 값 (Value List) 들을 모으는 방법입니다.

```java
Map<Key, List<Value>>
```

* 특정 key 값으로 특정 key에 대응하는 요소 리스트를 Map으로 만들 수 있다.
* 또한, 요소 리스트를 특정 프로퍼티의 속성에 맞게 정렬 할 수 있습니다

---

간단한 예제로, 게시물을 예제로 들어보겠습니다.

게시물 들의 값으로,  해당하는 값의 게시물들을 뽑아와야 하는 요구사항이 생겼습니다. 

우리는 게시물의 리스트 List<Article>을 가지고 있습니다. 

게시물의 리스트 `List<Article>`를 가격으로 묶은 Map 을 만들고 싶다면?

* Java 8의 Stream 사용
* Collectors 클래스의 정적 메서드 groupingBY 메서드입니다. 
* Collectors.groupingBy
* groupingBy(classifier, toList()); -> classifier(분류자)로 List를 분류하여 그룹핑 할 수 있습니다.
* 유형 T의 입력 요소에 대해 "그룹화 기준" 작업을 구현하고 분류 함수에 따라 요소를 그룹화하고 결과를 Map에 반환하는 Collector를 반환합니다. 
* 분류 함수는 요소를 일부 키 유형 K에 매핑합니다. 
* 수집기는 키가 입력 요소에 분류 함수를 적용한 결과 값이고 해당 값이 입력 요소를 포함하는 목록인 Map<K, List<T>>를 생성합니다.
* 관련 키에 매핑됩니다. 
* 반환된 Map 또는 List 객체의 `유형, 변경 가능성, 직렬화 가능성 또는 스레드 안전성에 대한 보장은 없습`니다.

```java
// Collectors Class
//groupingBy(classifier, toList());  
public static <T, K> Collector<T, ?, Map<K, List<T>>> 
  groupingBy(Function<? super T, ? extends K> classifier) {
  return groupingBy(classifier, toList());
}
```



게시물의 필드로는 ID값, 내용, 순서, 이름, 가격이 들어있습니다.

```java
@Getter
@NoArgsConstructor    
@AllArgsConstructor
class Article {
  private Long id; 				// id
  private String content; // 내용 
  private Integer order;	// 순서
  private String name;		// 이름
  private Integer price;	// 가격
}
```



* 가격을 key 값으로, grouping 하고 싶다면?

```java
// 가격을 key 값으로, grouping 하고 싶다면?
List<Article> articles;

Map<Integer, List<Article>> articlesGroupByPrice = articles.stream()
  .collect(Collectors.groupingBy(Article::getPrice));
```



* 그렇다면 가격을 key값으로, key값에 해당하는 값들을 List로 묶고, 그 List 안에서 특정 필드를 기준으로 정렬을 하고 싶다면?
* sorting Lists after groupingBy?  

```java
Map.values().forEach(list -> list.sort(Comparator.comparing(기준)));
```



* 위 게시물을 기준으로, 가격대로 grouping하고, 그 가격 내에서 order 필드로  요소들을 정렬을 하고 싶다면?

```java
//  가격대로 grouping하고, 그 가격 내에서 order 필드로  요소들을 정렬을 하고 싶다면?
List<Article> articles;
articlesGroupByPrice.values()
  .forEach(artcls -> artcls.sort(Comparator.comparing(Article::getOrder)));
```





