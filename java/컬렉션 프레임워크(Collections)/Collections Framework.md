# Collections Framework - 컬렉션 프레임워크

컬렉션 :  ‘데이터 그룹을 다루고 표현하기 위한 단일화된 구조’ - Java API문서

JDK1.2 이전까지는 Vector, Hashtable, Properties와 같은 컬렉션 클래스들을 각자의 방식으로 처리

> 데이터를 저장하는 클래스들을 표준화한 설계 - 컬렉션 프레임워크



* 인터페이스와 다형성을 이용한 객체지향적 설계를 통해 표준화되어 있어 편리하고 재사용성이 높은 코드를 제공한다.

<img src="https://blog.kakaocdn.net/dn/cMogi7/btrU1pEl155/6v5RyZzF4yzERZZ7c2ObZ0/img.png" width = 800 height = 400>

컬렉션 프레임웍은 컬렉션 데이터 그룹을 크게 List, Set, Map으로 나눠 3개의 인터페이스를 정의하였다.   
그리고 List와 Set의 공통된 부분으로 Collection 인터페이스를 추가로 정의하였다.  

> 인터페이스 List와 Set을 구현한 컬렉션 클래스들은 서로 많은 공통부분이 있어서,
> 공통된 부분을 다시 뽑아 Collection 인터페이스를 정의할 수 있었지만
>
> Map인터페이스는 이들과 전혀 다른 형태로 컬렉션을 다루기 때문에 같은 상속계층도에 포함되지 못했다.

  


Vector, Stack, Hashtable, Properties와 같은 클래스들은 컬렉션 프레임 워크의 만들어지기 전부터 존재하던 것이기 때문에
컬렉션 프레임워크의 명명법을 따르지 않는다.



### java.util.Collection 인터페이스

| 메서드                                                       | 설명                                                         |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| `boolean add(Object o)`<br /> `boolean addAll(Collection c)` | 지정된 객체(o) 또는 Collection(c)의 객체들을 Collection에 추가 |
| `void clear()`                                               | Collection의 모든 객체를 삭제                                |
| `boolean contains(Object o) ` <br />`boolean containsAll(Collection c)`<br /> | 지정된 객체(o) 또는 Collection에 포함되어 있는지 확인        |
| `boolean equals(Object o)`                                   | 동일한 Collection인지 비교                                   |
| `int hashCode()`                                             | Collection의 hash code를 반환                                |
| `boolean isEmpty()`                                          | Collection이 비어있는지를 확인                               |
| `Iterator iterator()`                                        | Collection의 Iterator를 얻어 반환                            |
| `boolean remove(Object o)`                                   | 지정된 객체를 삭제                                           |
| `boolean removeAll(Collection c)`                            | 지정된 Collection에 포함된 객체들을 삭제                     |
| `boolean retainAll(Collection c)`                            | 지정된 Colletion에 포함된 객체만을 남기고 다른 객체들은 Collection에서 삭제. 이 작업으로 인해 Collection에 변화가 있다면 true, 그렇지 않으면 false를 반환 |
| `int size()`                                                 | Collection에 저장된 객체의 개수를 반환                       |
| `Object[] toArray()`                                         | Collectiono에 저장된 객체를 객체배열(Object[])로 반환        |
| `Object[] toArray(Object[] a)`                               | 지정된 배열에 Collection의 객체를 저장해서 반환              |

반환 타입이 boolean인 메소드들은 작업을 성공하면 true, 실패하면 false를 리턴한다

  



* 왜 add메서드는 Generic (E)으로 Parameter를 받는데, remove는 Object Type으로 받는가?

* [왜 add와 remove의 파라미터가 다른가?](https://stackoverflow.com/questions/104799/why-arent-java-collections-remove-methods-generic)

> **add와 remove의 역할이 서로 다르다.**
>
> add메서드는 잘못된 타입의 객체를 넣으려고 할 경우 컴파일 에러가 발생한다.
>  (당연히 Collection에서 Generic을 받게 정의를 해두었으니 컴파일에러가 발생하는것이 당연하다.)
> 그렇다면 remove메서드는 왜 잘못된 타입의 객체를 삭제하려고 할때 컴파일에러가 발생하지 않는가?
> remove는 Object를 기준으로 인자를 받는다. 
> 이때 remove의 기준은 바로 동일성 (==)이 아닌 동등성 (equals)의 기준으로 Collection에서 객체를 삭제한다. 
> 즉 삭제하려는 객체가 타입이 다르더라고 equals의 메서드 리턴 결과가 true, 즉 동등하다면 삭제할 수 있다는것이다.



### Iterable

> Implementating this interface allows an object to be the target of the enhanced for-statement (for each)

Foreach를 사용할 수 있게 하기 위한 인터페이스 이다. 
해당 Object를 순회할 수 있는 특성이 Object에 존재하는가? 에 대한 인터페이스.

Collection의 상위 인터페이스로는 Iterable를 상속받고 있다. 즉 Collection을 구현하는 모든 클래스는 for-each문을 사용할 수 있다.



## 핵심 인터페이스 (Core Interface)

| 인터페이스 | 특징                                                         |
| ---------- | ------------------------------------------------------------ |
| List       | - 순서가 있는 데이터의 집합 - 데이터의 중복 허용<br />- 예) 대기자 명단<br />- 구현클래스 : ArrayList, LinkedList, Stack, Vector 등 |
| Set        | - 순서를 유지하지 않는 데이터의 집합 - 데이터의 중복을 허용하지 않음 <br />- 예 ) 양의 정수 집합, 소수의 집합<br />- 구현클래스 : HashSet, TreeSet 등 |
| Map        | - 키(key)와 값(value)의 쌍(pair)으로 이루어진 데이터의 집합<br />- 두 값을 연결한다는 의미에서 Map이라 한다 <br /> - 순서는 유지되지 않음<br /> - 키(key)는 중복을 허용하지 않음 - 값(value)은 중복 허용 <br />- 예 ) 우편번호, 지역번호(전화번호)<br />- 구현클래스 : HashMap, TreeMap, Hashtable, Properties 등 |



### List 인터페이스

List 인터페이스는 `중복을 허용`하면서 `저장순서가 유지`되는 컬렉션을 구현하는데 사용된다.

<img src="https://blog.kakaocdn.net/dn/bQGJNz/btrU0uMFBbn/INGTcPZkdmudXhOS4Tyk8k/img.png" width= 800 height= 300>

| 메서드                                                       | 설명                                                         |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| `boolean add(int index, Object element)` <br /> `boolean addAll(int index, Collection c)` | 지정된 위치(index)에 객체(element) 또는 Collection(c)에 포함된 객체들을 Collection에 추가 |
| `Object get(int index)`                                      | 지정된 위치(index)에 있는 객체를 반환                        |
| `int indexOf(Object o)`                                      | List의 첫번째 요소부터 순방향으로 지정된 객체의 위치(index)를 찾아 반환 |
| `int lastIndexOf(Object o)`                                  | List의 마지막 요소부터 역방향으로 지정된 객체의 위치(index)를 찾아 반환 |
| `ListIterator listIterator()` <br />`ListIterator listIterator(int index)` | List의 객체에 접근할 수 있는 ListIterator를 반환             |
| `Object remove(int index)`                                   | 지정된 위치(index)에 있는 객체를 삭제하고 삭제된 객체를 반환 |
| `Object set(int index, Object element)`                      | 지정된 위치(index)에 객체(elemet)를 저장                     |
| `void sort(Comparator c)`                                    | 지정된 비교자(comparator)로 List를 정렬                      |
| `List subList(int fromIndex, int toIndex)`                   | 지정된 범위(fromIndex부터 toIndex)에 있는 객체를 반환        |



#### Set 인터페이스

Set 인터페이스는 `중복을 허용하지 않고`,  `저장순서가 유지되지 않는` 컬렉션 클래스를 구현하는데 사용된다.

<img src="https://blog.kakaocdn.net/dn/cwQKOK/btrUWS1KZWn/p2kfKsMz7PPxwPDdcPJh70/img.png" width = 850 height=250>

Set 인터페이스는 Collection 인터페이스를 상속받으므로, Collection 인터페이스에서 정의한 메소드도 모두 사용할 수 있다.

Set 인터페이스에서 제공하는 주요 메소드

| 메서드                       | 설명                                                     |
| ---------------------------- | -------------------------------------------------------- |
| `boolean add(E e)`           | 해당 집합(set)에 전달된 **요소를 추가**함. (선택적 기능) |
| `void clear()`               | 해당 집합의 **모든 요소를 제거**함. (선택적 기능)        |
| `boolean contains(Object o)` | 해당 집합이 전달된 객체를 **포함하고 있는지를 확인**함.  |
| `boolean equals(Object o)`   | 해당 집합과 전달된 **객체가 같은지를 확인**함.           |
| `boolean isEmpty()`          | 해당 집합이 **비어있는지를 확인**함.                     |
| `Iterator<E> iterator()`     | 해당 집합의 **반복자(iterator)를 반환**함.               |
| `boolean remove(Object o)`   | 해당 집합에서 전달된 **객체를 제거**함. (선택적 기능)    |
| `int size()`                 | 해당 집합의 요소의 **총 개수를 반환**함.                 |
| `Object[] toArray()`         | 해당 집합의 모든 요소를 Object 타입의 **배열로 반환**함. |

 

#### Set 자료구조의 특징

**- 중복** : 중복 불가능 , 유일성을 가짐
\- **순서** : 예측 불가능
\- **정렬** : 정렬 불가능 -> **TreeSet** 클래스 가능
\- **동기화** **(Thread-Safe)** : 동기화 불가능, 불안전함

 

기본적은 Set은 정렬이 불가하지만, 이를 보안하고 등장한게 TreeSet이다.

**HashSet**은 HashMap과 Set 인터페이스를 상속하여 **빠른 연산이 가능**한 자료형이고,

**LinkedHashSet**은 LinkedList로 연결된 HashSet이라 할 수 있고, 입력 **순서를 보장**해준다.

**TreeSet**은 Tree와 Set을 상속한 연산이 느려도 **다양한 정렬을 지원**하는 자료형이다.

- **HashSet**
  - 가장빠른 임의 접근 속도
  -  순서를 예측할 수 없음
  -  정렬이 불가능함
- **TreeSet**
  - 삽입과 동시에 정렬되는 상태를 유지함.
  -  HashSet 보다는 삽입이 느림
  -  다양한 정렬 방법을 지정할 수 있음
  - 정렬 특화되어 있으므로, 최대값, 최솟값 반환 메소드와 통계메소드를 제공함



### Map 인터페이스

Map 인터페이스는 키(key)와 값(value)을 하나의 쌍으로 묶어서 저장하는 컬렉션 클래스를 구현하는데 사용. 
키는 서로 중복될 수 없지만 값은 중복을 허용.
 기존에 저장된 키와 중복된 키를 저장하면 기존 값이 없어지고 마지막 값이 남음

<img src="https://blog.kakaocdn.net/dn/dK4RZi/btrU6Xz5KRJ/9NnvcoirP0FeYTNpvcSM81/img.png" width = 700 height= 400>

| 메서드                                 | 설명                                                         |
| -------------------------------------- | ------------------------------------------------------------ |
| `void clear()`                         | Map의 모든 객체를 삭제                                       |
| `boolean containsKey(Object key)`      | 지정된 key객체와 일치하는 Map의 key객체가 있는지 확인        |
| `boolean containsValue(Object value)`  | 지정된 value객체와 일치하는 Map의 value객체가 있는지 확인    |
| `Set entrySet()`                       | Map에 저장되어 있는 key-value쌍을 Map.Entry타입의 객체로 저장한 Set으로 반환 |
| `boolean equals(Object o)`             | 동일한 Map인지 비교                                          |
| `Object get(Object key)`               | 지정된 key 객체에 대응하는 value 객체를 찾아서 반환          |
| `int hashCode()`                       | 해시코드를 반환                                              |
| `booelan isEmpty()`                    | Map이 비었는지 확인                                          |
| `Set keySet()`                         | Map에 저장된 모든 key객체를 반환                             |
| `Object put(Object key, Object value)` | Map에 저장된 value객체를 key객체에 연결(mapping)하여 저장    |
| `void putAll(Mapt t)`                  | 지정된 Map의 모든 key-value쌍을 추가                         |
| `Object remove(Object key)`            | 지정된 key객체와 일치하는 key-value객체를 삭제               |
| `int size()`                           | Map에 저장된 key-value쌍의 개수를 반환                       |
| `Collection values()`                  | Map에 저장된 모든 value객체를 반환                           |

values()의 반환타입이 Collection이고, keySet()의 반환타입이 Set인 이유는 value는 중복이 가능하지만 key는 중복이 불가능 하기 때문이다. 

#### Map.Entry 인터페이스

Map.Entry 인터페이스는 Map 인터페이스의 내부 인터페이스이다.   
내부 클래스처럼 인터페이스도 인터페이스 안에 인터페이스를 정의할 수 있다.  
 Map에 저장되는 key-value쌍을 다루기 위해 내부적으로 Entry 인터페이스를 정의되었다.

| 메서드                          | 인터페이스                             |
| ------------------------------- | -------------------------------------- |
| `booelan equals(Object o)`      | 동일한 Entry인지 비교                  |
| `Object getKey()`               | Entry의 key객체를 반환                 |
| `Object getValue()`             | Entry의 value객체를 반환               |
| `int hashCode()`                | Entry의 해시코드를 반환                |
| `Object setValue(Object value)` | Entry의 value객체를 지정된 객체로 변경 |



## ArrayList

ArrayList는 Object배열을 이용해서 데이터를 순차적으로 저장한다.  
배열에 저장할 공간이 없다면 더 큰 새로운 배열을 선언하여 기존 배열의 내용을 복사하여 붙여놓은 다음 새로운 내용을 순서대로 저장한다.   

> ArrayList는 List 인터페이스를 구현하기 때문에 데이터의 저장순서가 유지되고 중복을 허용한다는 특징이 있다.

| 메서드                                   | 설명                                                         |
| ---------------------------------------- | ------------------------------------------------------------ |
| ArrayList()                              | 크기가 10인 ArrayList를 생성                                 |
| ArrayList(Collection c)                  | 주어진 컬렉션이 저장된 ArrayList를 생성                      |
| ArrayList(int initialCapacity)           | 지정된 초기용량을 갖는 ArrayList를 생성                      |
| boolean add(Object o)                    | ArrayList의 마지막에 객체를 추가. 성공하면 true.             |
| void add(int index, Object element)      | 지정된 위치(index)에 객체를 저장                             |
| boolean addAll(Collection c)             | 주어진 컬렉션의 모든 객체를 저장                             |
| booelan addAll(int index, Collection c)  | 지정된 위치부터 주어진 컬렉션의 모든 객체를 저장             |
| void clear()                             | ArrayList를 완전히 비움                                      |
| Object clone()                           | ArrayList를 복제                                             |
| boolean contains(Object o)               | 지정된 객체(o)가 ArrayList에 포함되어 있는지 확인            |
| void ensureCapacity(int minCapacity)     | ArrayList의 용량이 최소한 minCapacity가 되도록 함            |
| Object get(int index)                    | 지정된 위치(index)에 저장된 객체를 반환                      |
| int indexOf(Object o)                    | 지정된 객체가 저장된 위치를 찾아 반환                        |
| booelan isEmpty()                        | ArrayList가 비어있는지 확인                                  |
| Iterator iterator()                      | ArrayList의 Iterator객체를 반환                              |
| int lastIndexOf(Object o)                | 객체(o)가 저장된 위치를 끝부터 역방향으로 검색하여 반환      |
| ListIterator listIterator()              | ArrayList의 ListIterator를 반환                              |
| ListIterator listIterator(int index)     | ArrayList의 지정된 위치부터 시작하는 ListIterator를 반환     |
| Object remove(int index)                 | 지정된 위치(Index)에 있는 객체를 제거                        |
| boolean remove(Object o)                 | 지정한 객체(o)를 제거. 성공하면 true, 실패하면 false         |
| boolean removeAll(Collection c)          | 지정한 컬렉션에 저장된 것과 동일한 객체들을 ArrayList에서 제거 |
| boolean retainAll(Collection c)          | ArrayList에 저장된 객체 중에서 주어진 컬렉션과 공통된 것들만을 남기고 나머지는 삭제 |
| Object set(int index, Object element)    | 주어진 객체(element)를 지정된 위치(index)에 저장             |
| int size()                               | ArrayList에 저장된 객체의 개수를 반환                        |
| void sort(Comparator c)                  | 지정된 정렬기준(c)으로 ArrayList를 정렬                      |
| List subList(int fromIndex, int toIndex) | fromIndex부터 toIndex사이에 저장된 객체를 반환               |
| Object[] toArray()                       | ArrayList에 저장된 모든 객체들을 객체배열로 반환             |
| Object[] toArray(Object[] a)             | ArrayList에 저장된 모든 객체들을 객체배열 a에 담아 반환      |
| void trinToSize()                        | 용량을 크기에 맞게 줄임(빈 공간을 없앰)                      |

배열을 이용한 자료구조인 ArrayList는 데이터를 읽어오고 저장하는데 효율적이지만, 용량을 변경해야 할 때는 새로운 배열을 생성한 후 기존의 배열로부터 새로 생성된 배열로 데이터를 복사해야하기 때문에 효율이 떨어진다.

 

> 배열의 객체를 순차적으로 저장하거나 마지막에 있는 객체를 삭제할 때는 System.arraycopy()를 호출하지 않아도 되기때문에 작업시간이 짧다. 하지만 배열의 중간에 객체를 추가하거나 삭제하는 경우 System.arraycopy()를 호출해서 데이터의 위치를 이동시켜 줘야 하기 때문에 작업시간이 오래 걸린다.



##  LinkedList

배열은 데이터를 읽어오는데 걸리는 시간이 빠르다는 장점이 있지만,
크기를 변경할 수 없으므로, 새로운 배열을 생성해서 데이터를 복사해야 한다.

> 배열의 단점
>
> **1. 크기를 변경할 수 없다.**
>
> 크기를 변경할 수 없으므로 새로운 배열을 생성해서 데이터를 복사해야 한다.
>
> 실행속도를 향상시키기 위해서는 충분히 큰 크기의 배열을 생성해야 하므로 메모리가 낭비된다.
>
> 
>
> **2. 비순차적인 데이터의 추가 또는 삭제에 시간이 많이 걸린다.**
>
> 차례대로 데이터를 추가하고 마지막에서부터 데이터를 삭제하는 것은 빠르지만,
>
> 배열의 중간에 데이터를 추가하려면, 빈자리를 만들기 위해 다른 데이터들을 복사해서 이동해야 한다.



이런 배열의 단점을 보완하기 위해 링크드 리스트(Linked List, 연결 리스트)가 생겼다.   
링크드 리스트는 데이터와 자신과 연결된 다음 데이터의 주소 참조값으로 구성되어 있다.





>  LinkedList는 Queue인터페이스(JDK 1.5)와 Dequeue인터페이스(JDK 1.6)를 구현하도록 되었다.
>
> LinkedList도 List인터페이스를 구현했기 때문에 ArrayList와 내부 구현방법만 다를 뿐 제공하는 메소드의 종류와 기능은 거의 같다.

 

| 메소드                                           | 설명                                                         |
| ------------------------------------------------ | ------------------------------------------------------------ |
| **boolean** add(Object o)                        | 객체(o)를 LinkedList의 끝에 추가합니다.저장에 성공하면 true, 그렇지 않으면 false를 반환합니다. |
| **void** add(E o)                                | 마지막으로 전달된 요소를 추가                                |
| **void** add(**int** index Object element)       | 지정된 위치(index)에 객체(element)를 추가합니다.             |
| **boolean** addAll(Collection c)                 | 주어진 컬렉션에 포함된 모든 요소를 LinkedList의 끝에 추가합니다. |
| **void** addFirst(E o)                           | 첫 번째로 전달된 요소 추가                                   |
| **void** clear()                                 | LinkedList의 모든 요소를 삭제합니다.                         |
| **boolean** contains(Object o)                   | 지정된 객체가 LinkedList에 포함되어 있는지 알려줍니다.       |
| **boolean** containsAll(Collection c)            | 지정된 컬렉션의 모든 요소가 포함되어 있는지 알려줍니다.      |
| E element( )                                     | 가장 첫 번째 요소를 반환합니다. 단 삭제 안함.                |
| E emoveFirst( )                                  | 첫 번째 요소를 반환한 후 삭제합니다.                         |
| Object get(**int** index)                        | 지정된 위치(index)의 객체를 반환합니다.                      |
| **int** indexOf(Object o)                        | 지정된 객체가 저장된 위치(앞에서 몇 번째)를 반환합니다.      |
| **boolean** isEmpty()                            | LinkedList가 비어있는지 알려줍니다. 비어있으면 true 그렇지않으면 false를 반환합니다. |
| Iterator iterator()                              | Iterator를 반환합니다.                                       |
| **int** lastIndexOf(Object o)                    | 지정된 객체의 위치(index)를 반환합니다.(끝부터 역순검색)     |
| ListIterator listIterator()                      | ListIterator를 반환합니다.                                   |
| ListIterator listIterator(**int** index)         | 지정된 위치에서부터 시작하는 ListIterator를 반환합니다.      |
| **boolean** offer(E o)                           | 전달된 요소를 마지막 요소로 추가                             |
| E peek( )                                        | 가장 첫 번째 요소를 반환합니다. 단 삭제 안함                 |
| E poll( )                                        | 가장 첫 번째 요소를 반환한 후 삭제합니다.                    |
| **boolean** remove(Object o)                     | 인자로 전달된 객체를 현재 Queue에서 최초로 검출된 요소를 삭제합니다. |
| **boolean** removeAll(Collection c)              | 지정된 컬렉션의 요소와 일치하는 요소를 모두 제거합니다.      |
| **boolean** retainAll(Collection c)              | 지정된 컬렉션의 모든 요소가 포함되어 있는지 확인합니다.      |
| Object set(int index, Object element)            | 지정된 위치(index)의 객체를 주어진 객체로 바꿉니다.          |
| **int** size()                                   | LinkedList에 저장된 객체의 수를 반환합니다.                  |
| List subList(**int** fromIndex, **int** toIndex) | LinkedList의 일부를 List로 반환합니다.                       |
| Object[] toArray()                               | LinkedList에 저장된 객체를 배열로 반환합니다.                |
| Object[] toArray(Object[] a)                     | LinkedList에 저장된 객체를 주어진 배열에 저장하여 반환합니다. |

아래의 메소드는 Queue인터페이스와 Dequeue인터페이스를 구현하면서 추가된 메소드 이다.  


| 메소드                                     | 설명                                                         |
| ------------------------------------------ | ------------------------------------------------------------ |
| Object element()                           | LinkedList의 첫 번째 요소를 반환합니다.                      |
| boolean offer(Object o)                    | 지정된 객체(o)를 LinkedLIst의 끝에 추가합니다.               |
| Object peek()                              | LinkedList의 첫 번째 요소를 반환합니다.                      |
| Object poll()                              | LinkedList의 첫번째 요소를 반환합니다. 이때 LinkedList에서 제거됩니다. |
| Object remove()                            | LinkedList의 첫 번째 요소를 제거합니다.                      |
| **void** addFirst(Object o)                | LinkedList의 맨 앞에 객체(o)를 추가합니다.                   |
| **void** addLast(Object o)                 | LinkedList의 맨 끝에 객체(o)를 추가합니다.                   |
| Iterator descendingIterator()              | 역순으로 조회하기 위한 DescendingIterator를 반환합니다.      |
| Object getFirst()                          | LinkedList의 첫 번째 요소를 반환합니다.                      |
| Object getLast()                           | LinkedList의 마지막 요소를 반환합니다.                       |
| **boolean** offerFirst(Object o)           | LinkedList의 맨 앞에 객체를 추가합니다.                      |
| **boolean** offerLast(Object o)            | LinkedList의 맨 끝에 객체를 추가합니다.                      |
| Object peekFirst()                         | LinkedList의 첫 번째 요소를 반환합니다.                      |
| Object peekLast()                          | LinkedList의 마지막 요소를 반환합니다.                       |
| Object pollFirst()                         | LinkedList의 첫번째 요소를 반환하면서 제거합니다.            |
| Object pollLast()                          | LinkedList의 마지막 요소를 반환하면서 제거합니다.            |
| Object pop()                               | removeFirst()와 동일                                         |
| **void** push(Object o)                    | addFirst()와 동일                                            |
| Object removeFirst()                       | LinkedList의 첫 번째 요소를 제거합니다.                      |
| Object removeLast()                        | LinkedList의 마지막 요소를 제거합니다.                       |
| boolean removeFirstOccurrence(Object o)    | LinkedList에서 첫번째로 일치하는 객체를 제거합니다.          |
| **boolean** removeLastOccurrence(Object o) | LinkedList에서 마지막으로 일치하는 객체를 제거합니다.        |



## ArrayList vs LinkedList

> **1. 순차적으로 추가/삭제하는 경우 ArrayList가 LinkedList보다 빠르다.**
> 단순히 저장하는 시간만 비교할 수 있도록 하기 위해 ArrayList를 생성할 때는 저장할 데이터의 개수만큼 충분한 초기용량을 확보해서
> 저장공간이 부족해서 새로운 ArrayList를 생성해야하는 상황이 일어나지 않도록 했다.
>
> 만일 ArrayList의 크기가 충분하지 않으면, 새로운 크기의 ArrayList를 생성하고
> 데이터를 복사하는 일이 발생하게 되므로 순차적으로 데이터를 추가해도 ArrayList보다 LinkedList가 더 빠를 수도 있다.
>
> 순차적으로 삭제한다는 것은 마지막 데이터부터 역순으로 삭제해나간다는 것을 의미하며,
> ArrayList는 마지막 데이터부터 삭제할 경우마지막 요소의 값을 null로만 바꾸면 되므로 각 요소들의  재배치가 필요하지 않기 때문에 상당히 빠르다.



> **2. 중간 데이터를 추가/삭제하는 경우에는 LinkedList가 ArrayList보다 빠르다.**
> 중간 요소를 추가 또는 삭제하는 경우, LinkedList는 각 요소간의 연결만 변경해주면 되기 때문에 처리 속도가 빠르다.
> 반면에 ArrayList는 각 요소들을 재배치하여 추가할 공간을 확보하거나 빈 공간을 채워야하기 때문에 처리속도가 늦다.



> 배열의 경우 만일 인덱스가 n인 요소의 값을 얻어 오고자 한다면 단순히 아래와 같은 수식으로 계산해서 해결된다.
> **인덱스가 n인 데이터의 주소 = 배열의 주소 + n\*데이터 타입의 크기**



| Collection | 읽기(접근시간) | 추가/삭제 | 비고                                                        |
| ---------- | -------------- | --------- | ----------------------------------------------------------- |
| ArrayList  | 빠르다         | 느리다    | 순차적인 추가삭제는 더 빠르다.  <br />비효율적인 메모리사용 |
| LinkedList | 느리다         | 빠르다    | 데이터가 많을수록 접근성이 떨어짐                           |



## Stack과 Queue



### Stack(스택)

스택은 마지막에 저장한 데이터를 가장 먼저 꺼내게 되는 LIFO(Last In First Out)구조

* 동전통과 같은 구조로 양 옆과 바닥이 막혀 있어서 한 방향으로만 뺄 수 있는 구조
* 순차적으로 데이터를 추가하고 삭제하는 스택에는 `ArrayList와 같은 배열 기반의 컬렉션 클래스가 적합`

<img src="https://blog.kakaocdn.net/dn/Hx4cC/btrUVkdj2uq/Q8PhGHkyj7KM2MPNzwc2v0/img.png" width = 400 height =400>

#### 메소드

- `boolean empty()` : Stack이 비어있는지 알려준다.
- `Object peek()` : Stack의 맨 위에 저장된 객체를 반환. `pop()`과 달리 Stack에서 객체를 꺼내지는 않음
- `Object pop()` : Stack의 맨 위에 저장된 객체를 꺼낸다.
- `Object push(Object item)` : Stack에 객체(item)를 저장한다.
- `int search(Object o)` : Stack에서 주어진 객체(o)를 찾아서 그 위치를 반환. 못찾으면 -1 반환. (배열과 달리 위치는 0이 아닌 1부터 시작)



#### 스택의 활용 (Stack)

* 수식 계산, 수식 괄호 검사, 웹브라우저의 앞으로 / 뒤로



### Queue(큐)

큐는 처음에 저장한 데이터를 가장 먼저 꺼내게 되는 FIFO(First In First Out)구조

* 큐는 양 옆만 막혀 있고 위아래로 뚫려 있어서 한 방향으로만 넣고 한 방향으로만 빼는 파이프와 같은 구조
* 큐는 데이터를 꺼낼 때 항상 첫번째 저장된 데이터를 삭제한다.
* 그러므로 ArrayList와 같은 배열 기반의 컬렉션 클래스를 사용한다면, 데이터를 꺼낼 때마다 빈공간을 채우기 위해 데이터 복사가 발생해서 비효율적.
* 큐는 ArrayList보다 데이터의 추가/삭제가 쉬운 LinkedList가 더 적합하다.

<img src="https://blog.kakaocdn.net/dn/4QFnp/btrU14ms2YD/ACsUvxsIJvc89PYjQjkxhk/img.png" width = 600 height = 200>

#### 메소드

- `boolean add(Object o)` : 지정된 객체를 Queue에 추가한다. 성공하면 `true` 반환. 저장공간 부족하면 `IlligalStateException` 발생
- `boolean offer(Object o) : Queue에 객체를 저장. 성공하면`true`실패하면`false` 반환
- `Object remove()` : Queue에서 객체를 꺼내 반환. 비어있으면 `NoSuchElementException` 발생
- `Object poll()` : Queue에서 객체를 꺼내서 반환. 비어있으면 `null` 반환
- `Object element()` : 삭제없이 요소를 읽어온다. `peek()`과 달리 Queue가 비었을 때 `NoSuchElementException 발생`
- `Object peek()` : 삭제없이 요소를 읽어 온다. Queue가 비어있으면 `null` 반환



#### 큐의 활용(queue)

* 최근 사용 문서, 인쇄 작업 대기 목록, 버퍼(buffer)





> Java에서는 스택을 `Stack 클래스로 구현`하여 제공하고 있지만 큐는` Queue 인터페이스로 정의`해놓고 클래스는 제공하지 않는다.
>
> 대신 Queue 인터페이스를 구현한 클래스들 중 하나를 선택해서 사용하면 된다.



#### PriorityQueue

Queue인터페이스의 구현체 중 하나로, 저장한 순서와 상관 없이 우선순위가 높은 것부터 꺼낸다. 저장공간은 배열을 이용하고, 각 요소는 힙(heap)이라는 자료구조 형태로 저장한다.

*   null은 저장할 수 없다. null을 저장하면 NullPiinterException이 발생한다.

#### Deque(Double-Ended Queue)

Deque는 양쪽 끝에 추가/삭제 가능  
Deque는 스택과 큐를 하나로 합쳐놓은 것과 같으며 스택으로 사용할 수도, 큐로 사용할 수도 있다.

* 구현체로 ArrayDeque, LinkedList 제공



# Iterator

> 컬렉션 프레임워크에서는 컬렉션에 저장된 요소들을 읽어오는 방법을 표준화하였다.
> 컬렉션에 저장된 각 요소에 접근하는 Iterator인터페이스를 정의하고,
> Collection 인터페이스에는 Iterator(Iterator를 구현한 클래스의 인스턴스)를 반환하는 iterator()를 정의하고 있다.

```java
public interface Iterator {
  boolean hasnext();
  Object next();
  void remove();
}

public interface Collection {
  ...
  public Iterator iterator();
  ...
}
```

- 컬렉션 클래스에 대해 iterator()를 호출하여 Iterator를 얻은 다음 반복문(주로 while문)을 사용해서 컬렉션 클래스의 요소들을 읽어 온다.
  - `boolean hasNext()` - 읽어올 요소가 남아있는지 확인. 있으면 true 없으면 false
  - `Object next()` - 다음 요소를 읽어 온다. next()를 호출하기 전에 hasNext()를 호출해서 읽어 올 요소가 있는지 확인하는 것이 안전.
  - `void remove()` - next()로 읽어 온 요소를 삭제한다. next()를 호출한 다음에 remove()를 호출해야 한다.

| 메서드            | 설명                                                         |
| ----------------- | ------------------------------------------------------------ |
| boolean hasNext() | 읽어 올 요소가 남아있는지 확인한다.<br/>있으면 true, 없으면 false를 반환한다. |
| Object next()     | 다음 요소를 읽어 온다.<br/>next()를 호출하기 전에 hasNext()를 호출해서 읽어 올 요소가 있는지 확인하는 것이 안전하다. |
| remove()          | next()로 읽어 온 요소를 삭제한다.<br/>next()를 호출한 다음에 remove()를 호출해야한다. (선택적 기능) |

> **참조변수의 타입을 ArrayList타입이 아니라 Collection타입으로 한 이유는 무엇일까?**
> Collection에 없고 ArrayList에만 있는 메서드를 사용하는 게 아니라면,
> Collection타입의 참조변수로 선언하는 것이 좋다.
>
> 만약 Collection인터페이스를 구현한 다른 클래스,
> 예를 들어 LinkedList로 바꿔야 한다면 선언문 하나만 변경하면 나머지 코드는 검토하지 않아도 된다.
>
> 참조변수 타입이 Collection이므로 Collection에 정의되지 않은 메서드는 사용되지 않았을 것이 확실하므로
> 그러나 참조변수 타입을 ArrayList로 했다면 선언문 이후의 문장들을 검토해야 한다.
> Collection에 정의되지 않은 메서드를 호출했을 수 있기 때문이다.



#### Map 인터페이스를 구현한 컬렉션 클래스는 Key와 Value를 쌍으로 저장하기 때문에 iterator()를 직접 호출할 수 없다.   
대신 keySet()이나 entrySet()을 통해 Key와 Value를 각각 따로 Set의 형태로 얻어 온 후에 다시 iterator()를 호출해야 Iterator를 얻을 수 있다.

```java
Map map = new HashMap();
...
Iterator it = map.keySet().iterator();
```

- List클래스들은 저장순서를 유지하기 때문에 Iterator를 이용해서 읽어 온 결과 역시 저장 순서와 동일하지만 Set클래스들은 각 요소간의 순서가 유지되지 않기 때문에 Iterator를 이용해서 저장된 요소들을 읽어 와도 처음에 저장된 순서와 같지 않다.



### **ListIterator와 Enumeration**
Enumeration은 컬렉션 프레임 워크가 만들어지기 이전에 사용하던 것으로 Iterator의 구버젼으로 보면 된다.
ListIterator는 Iterator를 상속받아서 기능을 추가한 것으로
컬렉션의 요소에 접근할 때 Iterator는 단방향으로만 이동할 수 있지만 ListIterator는 양방향으로 이동이 가능하다.
다만 ArrayList나 LinkedList와 같이 List인터페이스를 구현한 컬렉션에서만 사용할 수 있다.



> **Enumeration :** Iterator의 구버전 (메서드 이름만 다를 뿐 기능은 같다.)
> **ListIterator :** Iterator에 양방향 조회기능추가(List를 구현한 경우만 사용 가능)



## Arrays

Array클래스에는 배열을 다루는데 유용한 메서드가 정의되어 있다.
같은 기능의 메서드가 배열의 타입만 다르게 오버로딩되어 있어서 많아 보이지만, 실제로는 그리 많지 않다.

* Arrays에 정의된 메서드는 모두 static메서드다.



### Arrays - 배열 복사 - copyOf(), copyOfRange()

> copyOf()는 배열 전체 복사, copyOfRange()는 배열의 일부를 복사해서 새로운 배열을 만들어 반환.
> copyOfRange()에 지정된 범위의 끝은 포함되지 않는다.

```java
int[] arr = {0,1,2,3,4,};
int[] arr2 = Arrays.copyOf(arr, 3); // arr2 = [0,1,2]
int[] arr3 = Arrays.copyOfRange(arr, 2, 4); // arr3 = [2,3]
```



### Arrays - 배열 채우기 - fill(), setAll()

> fill()은 배열의 모든 요소를 지정된 값으로 채운다.
> setAll()은 배열을 채우는데 사용할 함수형 인터페이스를 매개변수로 받는다.

```java
int[] arr = new int[5];
Arrays.fill(arr, 9);  // arr = [9,9,9,9,9]
Arrays.setAll(arr, () -> (int)(Math.random()*5)+1); // arr = [1,5,2,1,1]
```



### Arrays - 배열 정렬, 검색 - sort(), binarySearch()

> sort()는 배열 정렬, binarySearch()는 배열에 저장된 요소를 검색
> binarySearch()는 배열에서 지정된 값이 저장된 위치(index)를 찾아서 반환. 
>
> `반드시 배열이 정렬된 상태여야 올바른 결과를 얻는다.`

```java
int[] arr = { 3, 2, 0, 1, 4,};
Arrays.sort(arr); // arr = [0,1,2,3,4]
int idx = Arrays.binarySearch(arr, 2); // idx = 2
```



### Arrays - 문자열의 비교, 출력 - equals(), toString()

> equals(), toString()은 일차원 배열에만 사용 가능.
> `다차원 배열에는 deepEquals(), deepToString() 사용.`
> deepToString()은 배열의 모든 요소를 재귀적으로 접근해서 문자열을 구성하므로 3차원 이상의 배열에도 동작

- 다차원 배열은 ‘배열의 배열’의 형태로 구성하기 때문에 equals()로 비교하면, 문자열을 비교하는 것이 아니라 ‘배열에 저장된 배열의 주소’를 비교하게 된다.



### Arrays - 배열을 List로 반환 - asList(Object…a)

> asList()는 배열을 List에 담아서 반환.

- 매개변수의 타입이 가변인수라서 배열 생성 없이 저장할 요소들만 나열하는 것도 가능
- 한 가지 주의할 점은 asList()가 반환한 List의 크기를 변경할 수 없다.
- 크기 변경가능한 List는 `List list = new ArrayList(Arrays.asList(1,2,3,4,5));`



### Arrays - **parallelXXX(),spliterator(),stream()**

이 메서드들은 보다 빠른 결과를 얻기 위해 여러 쓰레드가 작업을 나눠 처리하도록 한다.
spliterator()는 여러 쓰레드가 처리할 수 있게 하나의 작업을 여러 작업으로 나누는
Spliterator를 반환하며, stream()은 컬렉션을 스트림으로 변환한다.



# Comparable vs Comparator - 인터페이스 

Arrays.sort()를 호출하면 알아서 배열을 정렬하는 것처럼 보이지만 사실은 클래스의 Comparable의 구현에 의해 정렬되었던 것.
Comparble을 구현한 클래스는 정렬이 가능하다는 것을 의미한다.



> Comparator와 Comparable은 모두 인터페이스로 컬렉션을 정렬하는데 필요한 메서드를 정의하고 있으며,
>
>  Comparable을 구현하고 있는 클래스들은 같은 타입의 인스턴스끼리 서로 비교할 수 있는 클래스들,
> 주로 Integer와 같은 wrapper클래스와 String Date, File과 같은 것들이며
> 기본적으로 오름차순, 즉 작은 값에서부터 큰 값의 순으로 정렬되도록 구현되어 있다.



```java
public interface Comparator {
  int compare(Object o1, Object o2);
  boolean equals(Object obj);
}

public interface Comparable {
  public int compareTo(Object o);
}
```

> Comparable은 java.lang 패키지에 있고, Comparator는 java.util 패키지에 있다.

- Comparable : 기본 정렬기준을 구현하는데 사용.
- Comparator : 기본 정렬기준 외에 다른 기준으로 정렬하고자할 때 사용

compare()와 compareTo()는 선언형태와 이름이 약간 다를 뿐 두 객체를 비교한다는 같은 기능을 목적으로 고안된 것이다.
compareTo()의 반환값은 int이지만 실제로는 

* `비교하는 두 객체가 같으면 0,`
* `비교하는 값보다 작으면 음수,`  
* `크면 양수`를 반환하도록 구현해야 한다. 



오름차순 정렬 : o1 < o2 왼쪽값이 오른쪽값보다 작게 만들어 음수를 리턴하게 하면 된다

내림차순 정렬 : o1 > o2 오른쪽값이 왼쪽값보다 작게 만들어 양수를 리턴하게 하면 된다. 



|             | Comparable                        | Comparator                                            |
| ----------- | --------------------------------- | ----------------------------------------------------- |
| 패키지      | java.lang (import 없이 사용 가능) | java.util (import 필요)                               |
| 사용 용도   | 기본 정렬기준을 구현하는데 사용   | 기본 정렬기준 외에 다른 기준으로 정렬하고자할 때 사용 |
| 비교 메서드 | public int compareTo(Object o);   | int compare(Object o1, Object o2);                    |
|             | 클래스의 public 메서드로의 의미   | `유틸리티성 성격(비교 알고리즘)` 적인 뉘앙스          |

> Arrays.sort()는 배열을 정렬할 때 Comparator를 지정해주지 않으면 저장하는 객체(주로 Comparable을 구현한 클래스)에 구현된 내용에 따라 정렬된다



String의 기본 정렬을 반대로 하는 것,
즉 문자열을 내림차순(descending order)을 구현하는 것은 아주 간단하다.
단지 String에 구현된 compareTo()의 결과에 -1을 곱하기만 하면 된다.
또는 비교하는 객체의 위치를 바꿔서 c2.compareTo(c1)과 같이 해도 된다.
다만 compare()의 매개변수가 Object타입이기 때문에 compareTo()를 바로 호출할 수 없으므로
먼저 Comparable로 형변환해야 한다는 것만 확인하자.

```java
class Descending implements Comparator {
	public int compare(Object o1, Object o2){
    	if(o1 instanceof Comparable && o2 instanceof Comparable) {
        	Comparable c1 = (Comparable)o1;
            Comparable c2 = (Comparable)o2;
            return  c1.compareTo(c2) * -1; // 기본 정렬방식의 역으로 변경한다.
        }
        return -1;
    }
}
```



# HashSet

HashSet은 Set인터페이스를 구현한 가장 대표적인 컬렉션.

* Set인터페이스의 특징대로 HashSet은 중복된 요소를 저장하지 않는다.
* 만일 HashSet에 이미 저장되어 있는 요소와 중복된 요소를 추가하고자 한다면 이 메서드들은
  false를 반환함으로써 중복된 요소이기 떄문에 추가에 실패했다는 것을 알린다. - 예외가 발생하진 않는다.
* HashSet은 저장순서를 유지하지 않으므로 저장순서를 유지하고자 한다면 LinkedHashSet 사용.



> HashSet은 내부적으로 HashMap을 이용해서 만들어졌으며,
> HashSet이란 이름은 해싱(hashing)을 이용해서 구현했기 때문에 붙여진 것이다.



HashSet의 add메서드는 새로운 요소를 추가하기 전에 기존에 저장된 요소와 같은 것인지 판별하기 위해
`추가하려는 요소의 equals()와 hashCode()를 호출하기 때문에
equals와 hashCode()를 목적게 맞게 오버라이딩해야 한다.`



###  HashSet과 hashcode()

오버라이딩을 통해 작성된 hashCode()는 다음의 세 가지 조건을 만족 시켜야 한다.

**1. 실행중인 애플리케이션 내의 동일한 객체에 대해서 여러 번 hashCode()를 호출해도 동일한 int값을 반환해야 한다.**
하지만, 실행시마다 동일한 int값을 반환할 필요는 없다.
(단, equals메서드의 구현에 사용된 멤버변수의 값이 바뀌지 않았다고 가정한다.)



**2. equals메서드를 이용한 비교에 의해서 true를 얻은 두 객체에 대해 각각 hashCode()를 호출해서 얻은 결과는 반드시 같아야 한다.**



**3. equals메서드를 호출했을 때 false를 반환하는 두 객체는 hashCode() 호출에 대한 같은
int값을 반환하는 경우가 있어도 괜찮지만, 해싱(hashing)을 사용하는 컬렉션의 성능을
향상시키기 위해서는 다른 int값을 반환하는 것이 좋다.**



> 두 객체에 대해서 equals메서드를 호출한 결과가 true이면, 두 객체의 해시코드는 반드시 같아야하지만, 두 객체의 해시코드가 같다고 해서 equals메서드의 호출결과가 반드시 true이어야 한다는 것은 아니다.



# TreeSet

TreeSet은 이진검색트리(binary search tree) 형태로 데이터를 저장하는 컬렉션 클래스다.

TreeSet은 이진검색트리의 성능을 향상시킨 Red-Black tree로 구현되어 있다.

* `중복된 데이터의 저장을 허용하지 않으며, 정렬된 위치에 저장하므로 저장순서를 유지하지도 않는다.`

TreeSet은 `정렬된 상태를 유지`하기 때문에 단일 값 검색과 범위검색, 예를 들면 3과 7사이의 범위에 있는 값을 검색하는 것이 매우 빠르다.



> TreeSet에 저장되는 객체가 Comparable을 구현하던가 아니면
> TreeSet에게 Comparator를 제공해서 두 객체를 비교할 방법을 알려줘야 한다.
> 그렇지 않으면 TreeSet에 객체를 저장할 때 예외가 발생한다.



# HashMap과 Hashtable

Hashtable과 HashMap의 관계는 Vector와 ArrayList의 관계와 같아서 `Hashtable보다는 새로운 버전인 HashMap을 사용할 것을 권한다.`
HashMap은 Map을 구현했으므로 키와 값을 묶어서 하나의 데이터로 저장한다.
그리고 hashing을 사용하기 때문에 많은 양의 데이터를 검색하는데 뛰어난 성능을 보인다.

```java
public class HashMap extends AbstractMap 
                    implements Map, Cloneable, Serializable  {
  
  transient Entry[] table;
    ...
  
  static class Entry implements Map.Entry {
    final Object key;
    Object value;
      ...
  }
}
```

> HashMap은 Entry라는 내부 클래스를 정의하고, 다시 Entry타입의 배열을 선언한다.
> 키와 값은 별개의 값이 아니라 서로 관련된 값이기 때문에
> 각각의 배열로 선언하기 보다는 하나의 클래스로 정의해서 하나의 배열로 다루는 것이 데이터의 무결성(integrity)적인 측면에서 더 바람직하기 때문.



- HashMap은 키와 값을 각각 Object 타입으로 저장한다.
- (Object, Object)의 형태로 저장하기 때문에 어떠한 객체도 저장할 수 있지만 key는 주로 String을 대문자 또는 소문자로 통일해서 사용한다.



HashMap은 키와 값을 각각 Object타입으로 저장한다.

* **키(key) :** 컬렉션 내의 키(key) 중에서 유일해야 한다.
* **값(value) :** 키(key)와 달리 데이터의 중복을 허용한다.

> `HashMap은 key나 value로 null을 허용한다.` Hashtable은 허용하지 않는다



`HashMap과 같이 해싱을 구현한 컬렉션 클래스들은 저장 순서를 유지하지 않는다`



## 해싱과 해시함수

> 해싱이란 해시함수(hash function)를 이용해서 데이터를 해시테이블(hash table)에 저장하고 검색하는 기법
> 해시함수는 데이터가 저장된 곳을 알려주기 때문에 다량의 데이터 중에서도 원하는 데이터를 빠르게 찾을 수 있다.

- 해싱을 구현한 컬렉션 클래스 - HashSet, HashMap, Hashtable
  - 해싱을 구현한 컬렉션 클래스들은 저장 순서를 유지하지 않는다
- Hashtable은 Collection Framework가 도입되면서 HashMap으로 대체되었으나 이전소스와의 호환성 문제로 남겨두고 있다.



> HashMap과 같이 해싱을 구현한 컬렉션 클래스에서는 Object클래스에 정의된 hashCode()를 해시함수로 사용한다.



## hashCode()

- HashMap과 같이 해싱을 구현한 컬렉션 클래스에서는 Object클래스에 정의된 hashCode()를 해시함수로 사용한다.
- Object클래스에 정의된 hashCode()는 객체의 주소를 이용하는 알고리즘으로 해시코드를 만들기 때문에 모든 객체에 대해 hashCode()를 호출한 결과가 서로 유일한 훌륭한 방법이다.
- String클래스의 경우 Object로 부터 상속받은 hashCode()를 오버라이딩해서 문자열의 내용으로 해시코드를 만들어낸다. 그래서 서로 다른 String인스턴스일지라도 같은 내용의 문자열을 가졌다면 hashCode()를 호출하면 같은 해시코드를 얻는다.

<img src="https://blog.kakaocdn.net/dn/sINz8/btrU55SASAm/3rUEKuKEj1w1gEAGe5x5Ek/img.png" width = 700 height = 400>

테이블이 배열과, 배열의 각 요소마다 링크드 리스트로 이루어져 있는데 

저장할 데이터의 키를 해시함수에 넣으면 배열의 한 요소를 얻게 되고, 다시 그 곳에 연결되어 있는 링크드 리스트에 저장하게 된다.

* 링크드리스트는 검색에 불리한 자료구조. 크기가 커질수록 속도가 느려진다. 

하나의 링크드 리스트에 최소한의 데이터만 저장되려면,
저장될 데이터의 크기를 고려해서 HashMap의 크기를 적절하게 지정해줘야 하고,
해시함수가 서로 다른 키에 대해서 중복된 해시코드의 반환을 최소화해야 한다.
그래야 HashMap에서 빠른 검색시간을 얻을 수 있기 때문이다.



### equals() and hashcode()는 같이 재정의 해야 한다

HashSet에서 이미 봤던 것처럼 서로 다른 두 객체에 대해 equals()로 비교한 결과가 true인 동시에 hashCode()의 반환값이 같아야 같은 객체로 인식한다.

* HashSet은 내부적으로 HashMap을 사용한다. 

HashMap에서도 같은 방법으로 객체를 구별하며, 이미 존재하는 키에 대한 값을 저장하면 기존의 값을 새로운 값으로 덮어쓴다.

그래서 새로운 클래스를 정의할 때 `equals()를 오버라이딩해야 한다면 hashCode()도 같이 재정의`해서   
`equals()의 결과가 true인 두 객체의 해시코드 hashCode()의 결과 값이 항상 같도록 `해줘야 한다.

그렇지 않으면 HashMap과 같이 해싱을 구현한 컬렉션 클래스에서는 equals()의 호출결과가
true지만 해시코드가 다른 두 객체를 서로 다른 것으로 인식하고 따로 저장할 것이다.

**equals()로 비교한 결과가 false이고 hashcode 가 같은 경우는 같은 링크드 리스트에 저장된 서로 다른 두 데이터가 된다.**



##  TreeMap

TreeMap은 이름에서 알 수 있듯이 `이진검색트리의 형태`로` 키와 값의 쌍으로 이루어진 데이터`를 저장한다.
그래서 `검색과 정렬에 적합한 클래스`이다.
검색의 경우 대부분의 경우에서 `HashMap이 TreeMap보다 더 뛰어나다.`
다만 범위검색이나 정렬이 필요한 경우 TreeMap이 낫다.



## Properties

> Properties는 HashMap의 구버전인 Hashtable을 상속받아 구현한 것이다.
> Hashtable은 키와 값을 (Object, Object)의 형태로 저장하는데 비해
> Properties는 (String, String)의 형태로 저장하는 보다 단순화된 컬렉션클래스이다.



- 주로 애플리케이션의 환경설정과 관련된 속성(property)을 저장하는데 사용된다.
- 데이터를 파일로부터 읽고 쓰는 편리한 기능을 제공한다.
- Properties는 컬렉션프레임웍 이전의 구버전이므로 Iterator가 아닌 Enumeration을 사용한다.
- list메소드로 Properties에 저장된 모든 데이터를 화면 또는 파일에 편하게 출력할 수 있다.

```java
  void list(PrintStream out)
  void list(PrintWriter out)
```



# Collections 클래스

> Arrays가 배열과 관련된 메소드를 제공하는 것처럼, Collections는 컬렉션과 관련된 메소드를 제공한다.
> fill(), copy(), sort(), binarySearch() 등의 메소드는 두 클래스 모두에 포함되어 있으며 같은 기능을 한다.
>
> **java.util.Collection은 인터페이스이고, java.utilCollections는 클래스이다**



## 컬렉션(Collection)의 동기화

- 멀티 쓰레드(multi-thread) 프로그래밍에서는 하나의 객체를 여러 쓰레드가 동시에 접근할 수 있기 때문에 데이터의 일관성(consistency)을 유지하기 위해서는 공유되는 객체에 동기화(synchronization)가 필요하다.
- 새로 추가된 ArrayList와 HashMap과 같은 컬렉션은 동기화를 자체적으로 처리하지 않는다.
  - 필요한 경우에만 java.util.Collections 클래스의 동기화 메소드를 이용해서 동기화처리가 가능하도록 변경하였다.

```java
// 제공되는 메서드
static Collections synchronizedCollection (Collection c)
static List synchronizedList (List list)
static Set synchronizedSet (Set s)
static Map synchronizedMap (Map m)
static SortedSet synchronizedSortedSet (SortedSet s)
static SortedMap synchronizedSortedMap (SortedMap m)

// 사용방법
List syncList = Collections.synchronizedList(new ArrayList(...));
```



## 변경불가 컬렉션 만들기 - unmutableCollection
컬렉션에 저장된 데이터를 보호하기 위해서 컬렉션을 변경할 수 없게,
즉 읽기전용으로 만들어야할 때가 있다.
주로 멀티 쓰레드 프로그래밍에서 여러 쓰레드가 하나의 컬렉션을 공유하다보면 데이터가 손상될 수 있는데, 이를 방지하려면 아래의 메서드를 이용하면 된다.

```java
static Collection unmodifiableCollection (Collection c)
static List unmodifiableList (List list)
```



## 싱글톤 컬렉션 만들기 - Singleton Collection

단 하나의 객체만을 저장하는 컬렉션을 만들고 싶을 경우가 있다.
이럴 때는 아래의 메서드를 사용하면 된다.

```java
static List singletonList (Object o)
static Set singleton (Object o) // singletonSet이 아님
static Map singletonMap (Object ket, Object value)
```



## 한 종류의 객체만 저장하는 컬렉션 만들기

컬렉션에 모든 종류의 객체를 저장할 수 있다는 것은 장점이기도 하고 단점이기도 하다.
대부분의 경우 한 종류의 객체를 저장하며,
컬렉션에 지정된 종류의 객체만 저장할 수 있도록 제한하고 싶을 때 아래의 메서드를 사용한다.

```java
static Collection checkedcollection (Collection c, Class type)
static List checkedList (List list, Class type)

// 사용방법
List list = new ArrayList();
List checkedList = checkedList(List, String.class); // String만 저장가능
checkList.add("abc"); // OK
checkList.add(new Integer(3)); // 에러. ClassCastException이 발생
```

* `checked` 라는 prefix가 붙은 메서드를 사용하면 된다. 

컬렉션에 저장할 요소의 타입을 제한하는 것은 제네릭(generics)로 간단히 처리할 수 있는데도 이런 메서드들을 제공하는 이유는 호환성 때문이다.

* 제네릭 JDK1.5부터 도입된 기능이므로 JDK1.5 이전에 작성된 코드를 사용할 때는 이 메서드들이 필요할 수 있다.



# 컬렉션 클래스 요약

<img src="https://blog.kakaocdn.net/dn/A8csa/btrUZX9l2Lw/7KZnByiNuEC1q1B15nvAOK/img.png" width = 650 height = 500>

- `ArrayList` : 배열기반. 데이터의 추가와 삭제에 불리. 순차적인 추가삭제는 제일 빠름. 임의의 요소에 대한 접근성이 뛰어남.
- `LinkedList` : 연결기반. 데이터의 추가와 삭제에 유리. 임의의 요소에 대한 접근성이 좋지 않다.
- `HashMap` : 배열과 연결이 결합된 상태. 추가,삭제,검색,접근성이 모두 뛰어남. 검색에는 최고의 성능.
- `TreeMap` : 연결기반. 정렬과 검색(특히 범위검색)에 적합. 검색성능은 HashMap보다 떨어짐.
- `Stack` : Vector를 상속받아 구현
- `Queue` : LinkedList가 Queue인터페이스를 구현
- `Properties` : Hashtable을 상속받아 구현
- `HashSet` : HashMap을 이용해서 구현
- `TreeSet` : TreeMap을 이용해서 구현
- `LinkedHashMap` : HashMap과 HashSet에 저장순서유지기능 추가
- `LinkedHashSet` : HashMap과 HashSet에 저장순서유지기능 추가

