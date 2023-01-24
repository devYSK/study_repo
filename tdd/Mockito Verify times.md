# Mockito Verify Times

Mockito Verify 메소드는 특정 동작이 발생했는지 확인하는 데 사용된다. mocking한 메서드가 호출되었는지 확인하기 위해 테스트 메서드 코드 끝에 Mockito 검증 메서드를 사용할 수 있다.



* https://github.com/WebJournal/journaldev/tree/master/Mockito-Examples 에 더 많은 예제가 있다. 

## Mockito 검증

- Mockito verify() 메서드를 사용하여 메서드 호출 수 (call count)를 테스트할 수도 있다. 
  - mock 메서드에 대해 정확한 횟수, 적어도 한 번, 최대 호출 횟수를 테스트할 수 있다.
- `verifyNoMoreInteractions()` : 모든 verify() 메서드 호출 후에 모든 것이 확인되었는지 확인할 수 있다 . 메서드 확인이 아직 남아 있으면 실패하고 적절한 메시지를 제공한다.
- `verifyZeroInteractions()` 는  `verifyNoMoreInteractions()`방법과 동일하다.
- `inOrder()`메서드를 사용하여 메서드 호출 순서를 확인할 수 있다 . 메서드 호출을 건너뛸 수 있지만 확인 중인 메서드의 순서는 동일해야 한다.

mockito 검증 방법 예제 중 일부이다.

### Mockito verify() 간단한 예제

```java
@Test
void test() {
	List<String> mockList = mock(List.class);
	mockList.add("ys");
	mockList.size();
	
	verify(mockList).add("ys");
}
```

위 의 verify 메서드 (`add("ys")`) 는 mock 객체에서 `한 번만 호출 되면 통과 ` 한다. 

* 반면 호출되지 않으면 예외가 발생한다. 

아래의 `times(1)` verify 메서드 로 인수를 호출하는 것과 같다  .

```java
verify(mockList, times(1)).size();
```


메서드가 호출되었는지 확인하고 싶지만 인수에 대해 신경쓰지 않는 경우 ArgumentMatchers를 verify 메서드와 함께 사용할 수 있다.

```java
verify(mockList).add(anyString());
verify(mockList).add(any(String.class));
verify(mockList).add(ArgumentMatchers.any(String.class));
```

`org.mockito.Mockito`클래스는 Mockito 프레임워크의 유용한 메서드 대부분에 대한 static 메서드를 제공한다.



# 횟수로 Mockito 검증(Verify )

Mockito verify() 메서드가 [오버로드](https://www.digitalocean.com/community/tutorials/method-overloading-in-java) 되고 두 번째는 `verify(T mock, VerificationMode mode)`. 호출 횟수를 확인하는 데 사용할 수 있다.

```java
verify(mockList, times(1)).size(); // 일반 확인 방법( times () 생략) 과 동일
verify(mockList, atLeastOnce()).size(); // 무조건 1번 이상 호출해야한다. 
verify(mockList, atMost(2)).size(); // 최대 2번 호출 해야 한다
verify(mockList, atLeast(1)).size(); // 한 번 이상 호출해야 한다. 
verify(mockList, never()).clear(); // 절대 호출하면 안된다. 
```

### verifyNoMoreInteractions()

이 메서드는 호출 (인터렉션, 상호작용)이 확인되었는지 확인하기 위해 모든 verify 메서드 후에 사용할 수 있다.  
mock 객체에 확인되지 않은 호출 (인터렉션, 상호작용) 이 있으면 테스트에 실패합니다.

```java
// 모든 상호 작용이 확인되었으므로 아래는 통과 
verifyNoMoreInteractions(mockList);
mockList.isEmpty();
// isEmpty()가 확인되지 않았으므로 아래에서 실패
verifyNoMoreInteractions(mockList);
```

verifyNoMoreInteractions()의 두 번째 호출은 다음과 같은 오류 메시지와 함께 실패한다. .

```
org.mockito.exceptions.verification.NoInteractionsWanted: 
No interactions wanted here:
-> at com.journaldev.mockito.verify.MockitoVerifyExamples.test(MockitoVerifyExamples.java:36)
But found this interaction on mock 'list':
-> at com.journaldev.mockito.verify.MockitoVerifyExamples.test(MockitoVerifyExamples.java:34)
***
For your reference, here is the list of all invocations ([?] - means unverified).
1. -> at com.journaldev.mockito.verify.MockitoVerifyExamples.test(MockitoVerifyExamples.java:18)
2. -> at com.journaldev.mockito.verify.MockitoVerifyExamples.test(MockitoVerifyExamples.java:19)
3. [?]-> at com.journaldev.mockito.verify.MockitoVerifyExamples.test(MockitoVerifyExamples.java:34)
```

Mockito의 뛰어난 기능 중 하나는 예외 메시지이다. 테스트가 실패한 부분을 명확하게 지적하여 쉽게 수정할 수 있다.

### verifyZeroInteractions()

`verifyZeroInteractions()`메서드 동작은 verifyNoMoreInteractions() 메서드와 동일하다.

```java
Map mockMap = mock(Map.class);
Set mockSet = mock(Set.class);
verify(mockList).isEmpty();
verifyZeroInteractions(mockList, mockMap, mockSet);
```

### Mockito 검증 전용 메서드 호출

하나의 메서드만 호출 되는지 확인하려면 `only()`verify 메서드와 함께 사용할 수 있다.

```java
Map mockMap = mock(Map.class);
mockMap.isEmpty();
verify(mockMap, only()).isEmpty();
```

### Mockito 호출 순서 확인

`InOrder`호출 순서를 확인하는 데 사용할 수 있다 . 확인을 위해 모든 메서드를 건너뛸 수 있지만 확인 중인 메서드는 동일한 순서로 호출되어야 한다.

```java
InOrder inOrder = inOrder(mockList, mockMap);
inOrder.verify(mockList).add("Pankaj");
inOrder.verify(mockList, calls(1)).size();
inOrder.verify(mockList).isEmpty();
inOrder.verify(mockMap).isEmpty();
```

## 요약

Mockito verify() 메서드를 사용하여 모의 개체 메서드가 호출되고 있는지 확인할 수 있다. 
메서드 호출이 실수로 삭제된 경우 verify 메서드에서 오류가 발생한다.





### 참조

* https://github.com/WebJournal/journaldev/tree/master/Mockito-Examples
* https://www.digitalocean.com/community/tutorials/mockito-verify