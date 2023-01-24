# Java Collection, List, Array 비교, 검증



예제 List

```java
List<String> list1 = Arrays.asList("1", "2", "3", "4");
List<String> list2 = Arrays.asList("1", "2", "3", "4");
List<String> list3 = Arrays.asList("1", "2", "4", "3");
```



## JUnit - Assert

```java
@Test
public void whenTestingForEquality_ShouldBeEqual() throws Exception {
    Assert.assertEquals(list1, list2);
    Assert.assertNotSame(list1, list2);
    Assert.assertNotEquals(list1, list3);
}
```

## AssertJ - Assertions 

```java
@Test
public void whenTestingForEquality_ShouldBeEqual() throws Exception {
    assertThat(list1)
      .isEqualTo(list2)
      .isNotEqualTo(list3);

    assertThat(list1.equals(list2)).isTrue();
    assertThat(list1.equals(list3)).isFalse();
}
```

### 동일한 요소와 크기의 두 목록 인스턴스가 같은지 비교하는 테스트

```java
List first = Arrays.asList(1, 3, 4, 6, 8);
List second = Arrays.asList(8, 1, 6, 3, 4);
List third = Arrays.asList(1, 3, 3, 6, 6);
```



```java
@Test
void whenTestingForOrderAgnosticEqualityBothList_ShouldBeEqual() {
    assertThat(first).hasSameElementsAs(second);
}
```

* *hasSameElementsAs* 메서드는 중복 요소는 무시한다.

```java
@Test
void whenTestingForOrderAgnosticEqualityBothList_ShouldNotBeEqual() {
    List a = Arrays.asList("a", "a", "b", "c");
    List b = Arrays.asList("a", "b", "c");
    assertThat(a).hasSameElementsAs(b);
}
```

이 테스트에서는 요소가 동일하지만 두 목록의 크기가 같지 않지만 중복 항목을 무시하므로 Assertion은 여전히 참이다.

hasSameElementsAs()와 관련된 문제를 극복하기 위해 containsExactlyInAnyOrderElementsOf()를 사용할 수 있다. 
이 기능 은 두 목록에 순서에 관계없이 정확히 동일한 요소가 포함되어 있는지 확인한다.

```java
assertThat(a).containsExactlyInAnyOrderElementsOf(b);
```

### AssertArrayEquals

*assertArrayEquals* 은 예상 배열과 실제 배열이 동일한지 확인.

```java
@Test
public void whenAssertingArraysEquality_thenEqual() {
    char[] expected = { 'J', 'u', 'p', 'i', 't', 'e', 'r' };
    char[] actual = "Jupiter".toCharArray();

    assertArrayEquals(expected, actual, "Arrays should be equal");
}
```

## Apache Commons

```xml
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-collections4</artifactId>
    <version>4.4</version>
</dependency>
```

*다음은 CollectionUtils* 를 사용한 테스트이eㅏ.

```java
@Test
public void whenTestingForOrderAgnosticEquality_ShouldBeTrueIfEqualOtherwiseFalse() {
    assertTrue(CollectionUtils.isEqualCollection(first, second));
    assertFalse(CollectionUtils.isEqualCollection(first, third));
}
```

*isEqualCollection* 메소드는 주어진 컬렉션 이 동일한 [카디널리티](https://simple.wikipedia.org/wiki/Cardinality#:~:text=In mathematics%2C the cardinality of,the same number of elements.) 를 가진 정확히 동일한 요소를 포함하는 경우 *true* 를 반환한다 .
그렇지 않으면 *false* 를 반환한다 .

# Hamcrest

## 단일 요소가 컬렉션에 있는지 확인 - hasItem()

```java
import static org.hamcrest.Matchers.*;

public static <T> org.hamcrest.Matcher<java.lang.Iterable<? super T>> hasItem(T item) {
    return IsIterableContaining.hasItem(item);
  }
```

.

```java
List<String> collection = Lists.newArrayList("ab", "cd", "ef");
assertThat(collection, hasItem("cd"));
assertThat(collection, not(hasItem("zz")));
```

## 컬렉션에 여러 요소가 있는지 확인 - hasItems()

```java
List<String> collection = Lists.newArrayList("ab", "cd", "ef");
assertThat(collection, hasItems("cd", "ef"));
```



## 컬렉션의 모든 요소 확인

### 엄격한 순서 (with strict order)

```java
List<String> collection = Lists.newArrayList("ab", "cd", "ef");
assertThat(collection, contains("ab", "cd", "ef"));
```

### 순서 상관 없이  (with any order)

```java
List<String> collection = Lists.newArrayList("ab", "cd", "ef");
assertThat(collection, containsInAnyOrder("cd", "ab", "ef"));
```

## 컬렉션이 비어있는지 확인

```java
List<String> collection = Lists.newArrayList();
assertThat(collection, empty());
```

## 배열이 비어 있는지 확인 (Array Check, Empty Array)

```java
String[] array = new String[] { "ab" };
assertThat(array, not(emptyArray()));
```

## Map이 비어있는지 확인 (Map check, Empty Map)

```java
Map<String, String> collection = Maps.newHashMap();
assertThat(collection, equalTo(Collections.EMPTY_MAP));
```

## Iterable이 비어 있는지 확인

```java
Iterable<String> collection = Lists.newArrayList();
assertThat(collection, emptyIterable());
```

## 컬렉션 크기 확인

```java
List<String> collection = Lists.newArrayList("ab", "cd", "ef");
assertThat(collection, hasSize(3));
```

## 모든 요소의 상태 확인 (상태 비교)

```java
List<Integer> collection = Lists.newArrayList(15, 20, 25, 30);
assertThat(collection, everyItem(greaterThan(10)));
```





### 참조

https://www.baeldung.com/hamcrest-collections-arrays