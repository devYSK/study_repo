

# Junit5 AssertJ List 정렬 테스트 ( Check if list is sorted in junit)



JUnit5 에서 assertJ의 Assertions를 이용하여  List가 정렬되었는지 테스트 할 수 있다.



* isSorted() 메서드
* isSortedAccordingTo(Comparator comparator) 메서드



* docs : https://assertj.github.io/doc/
* https://www.baeldung.com/introduction-to-assertj
* https://javadoc.io/doc/org.assertj/assertj-core/latest/org/assertj/core/api/ListAssert.html
* https://javadoc.io/doc/org.assertj/assertj-core/latest/org/assertj/core/api/AbstractListAssert.html#isSorted()
* https://javadoc.io/doc/org.assertj/assertj-core/latest/org/assertj/core/api/AbstractListAssert.html#isSorted()



## 1. isSorted() - ascending, 오름차순 테스트 



* assertThat(List).isSorted() 메서드

```java
package org.assertj.core.api;
              
public abstract class AbstractListAssert // 중간생략
{
  
  Verifies that the actual list is sorted in ascending order according to the natural ordering of its elements.
All list elements must implement the Comparable interface and must be mutually comparable (that is, e1.compareTo(e2) must not throw a ClassCastException for any elements e1 and e2 in the list), examples :
a list composed of {"a1", "a2", "a3"} is ok because the element type (String) is Comparable
a list composed of Rectangle {r1, r2, r3} is NOT ok because Rectangle is not Comparable
a list composed of {True, "abc", False} is NOT ok because elements are not mutually comparable
Empty lists are considered sorted. Unique element lists are considered sorted unless the element type is not Comparable.
Returns:
this assertion object.
Throws:
AssertionError – if the actual list elements are not mutually Comparable.
  
  public SELF isSorted() {
    lists.assertIsSorted(info, actual);
    return myself;
  }

 
}
```

* list가 해당 요소의 자연 순서에 따라 오름차순으로 정렬되는지 확인한다.

* 모든 list의 요소는 Comparable Interface가 구현 되어 있어야 하며 상호 비교 가능해야 한다
  * 즉 e1.compareTo(e2) 를 비교할 때 ClassCastException을 throw해서는 안된다. 

* 예 : {"a1", "a2", "a3"}로 구성된 List는 요소 유형(문자열)이 Comparable이므로 테스트가 가능하다.
* 예 : Rectangle {r1, r2, r3}로 구성된 `List<Rectangle>`는  Rectangle이 Comparable Interface가 구현되어 있지 않으므로, 비교 가능하지 않기 때문에 테스트가 불가능하다.

* 예 : {True, "abc", False}로 구성된 List는 요소들이  비교 가능하지 않기 때문에 테스트가 불가능하다.
* 빈 List (Empty List)는 정렬된 것으로 간주한다.
* unique한 요소들로 이루어진 List는, 요소들이 Comparable이 아닌 경우 정렬된 것으로 간주된다.
* 검증에 사용된 객체를 반환한다. 
* List가 상호 비교 가능하지 않은 경우 AssertionError를 thorw 한다 (Comparable이 구현되어 있지 않은 경우)



다음과 같이 테스트가 가능하다

```java
@Test
void sortedTest() {
	List<Long> longs = List.of(1L, 2L, 3L, 4L, 5L); 
	
	assertThat(longs).isSorted()
}
```



## 2. isSortedAccordingTo(Comparator) - Comparator로 정렬 설정 가능, desceding, 내림차순 + 정렬 전략 설정하여 테스트

```java
package org.assertj.core.api;
              
public abstract class AbstractListAssert // 중간생략
{
  Verifies that the actual list is sorted according to the given comparator. Empty lists are considered sorted whatever the comparator is. One element lists are considered sorted if the element is compatible with comparator.
Params:
comparator – the Comparator used to compare list elements
Returns:
this assertion object.
Throws:
AssertionError – if the actual list elements are not mutually comparable according to given Comparator.
NullPointerException – if the given comparator is null.
  
  public SELF isSortedAccordingTo(Comparator<? super ELEMENT> comparator) {
    lists.assertIsSortedAccordingToComparator(info, actual, comparator);
    return myself;
  }
}
```

* List가 인자로 넘어와서 지정된 Comparator에 따라 정렬되는지 확인한다.

* Empty List (빈 목록)는 Comparator가 무엇이든 정렬된 것으로 간주된다.
* One element lists는 요소가 Comparator와 호환되는 경우 정렬된 것으로 간주된다.
* 검증에 사용된 객체를 반환한다.
* 주어진 Comparator 가 list 요소 비교에 비교 가능 하지 않은 경우 AssertionError를 Throw 한다.
* 주어진 Comparator null이면 NullPointerException을 throw 한다.



다음과 같이 사용할 수 있다. 

```java
@Test
void sortedTest() {
	List<Long> longs = List.of(5L, 4L, 3L, 2L, 1L);
	
	assertThat(longs).isSortedAccordingTo(Comparator.reverseOrder());
}
```

* Comparable 인터페이스가 구현된 경우 Comparator.reverseOrder()로 역순으로 정렬되었는지 테스트 할 수 있다. 



다음과 같은 Post 객체가 있다고 했을 때, 

```java
public class Post {
  private Long id;
  ...
    
  private LocadateTime createdAt;
  ...
}
```



```java
@Test
void sortedTest() {
	List<Post> posts = postService.findAll(condition);
	
	assertThat(posts)
    .map(Post::getId)
    .isSortedAccordingTo(Comparator.reverseOrder());
  
  assertThat(posts)
    .map(Post::getCreatedAt)
    .isSortedAccordingTo(Comparator.reverseOrder());
}
```

* Long, LocalDateTime은 Comparable이 구현 되어 있으므로 테스트가 가능하다.







