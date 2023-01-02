

# 14주차 과제: 제네릭

# 목표

자바의 제네릭에 대해 학습하세요.

# 학습할 것 (필수)

- 제네릭 사용법
- 제네릭 주요 개념 (바운디드 타입, 와일드 카드)
- 제네릭 메소드 만들기
- Erasure



# 제네릭(Generics)

> **제네릭 프로그래밍**([영어](https://ko.wikipedia.org/wiki/영어): generic programming)은 데이터 형식에 의존하지 않고, 하나의 값이 여러 다른 데이터 타입들을 가질 수 있는 기술에 중점을 두어 재사용성을 높일 수 있는 [프로그래밍](https://ko.wikipedia.org/wiki/프로그래밍) 방식이다



제네릭(generic)이란 `데이터 타입(data type)`을 `일반화(generalize)하는 것`을 의미한다

- 컴파일 시 타입을 체크해 주는 기능(compile-time type check) - JDK1.5에서 도입
- 객체의 타입 안전성을 높이고 형 변환의 번거로움을 줄여준다 - 타입 안정성, 유연성 향상
  - (하나의 컬렉션에는 대부분 한 종류의 객체만 저장)

- **파라미터 타입이나 리턴 타입에 대한 정의를 외부로 미룬다**
- **런타임 환경에 아무런 영향이 없는 컴파일 시점의 전처리 기술이다**

> **타입을 유연하게 처리하며, 런타임에 발생할 수 있는 타입에러를 컴파일전에 검출한다.**



### 제네릭의 장점

1. 타입 안전성을 제공한다. (컴파일시 강한 타입 체크)
2. 타입 체크와 형 변환을 생략할 수 있으므로 코드가 간결해진다.
   * 캐스팅을 제거할 수 있다.

>  "타입 안정성을 높인다"는 것은 의도하지 않은 타입의 객체가 저장되는 것을 막고, 
>
> 저장된 객체를 꺼내올 때원래의 타입과 다른 타입으로 잘못 형변환 되어 발생할 수 있는 오류를 줄여준다는 뜻이다.



### 제네릭을 사용하는 이유

- 제네릭 타입을 사용함으로써 **잘못된 타입이 사용될 수 있는 문제를 컴파일과정에서 제거할 수 있기 때문이다.**
- 자바 컴파일러는 코드에서 잘못 사용된 타입 때문에 발생하는 문제점을 제거하기 위해 제네릭 코드에 대해 **강한 타입 체크**를 한다.
  - ClassCastException 과 같은 UncheckedException으로부터의 안전을 보장받을 수 있다.
  - **실행 시 타입 에러가 나는 것보다 컴파일 시에 미리 타입을 강하게 체크해서 에러를 사전에 방지하는 것이 좋다.**
- 제네릭 코드를 사용하면 타입을 국한하기 때문에 요소를 찾아올 때 **타입 변환을 할 필요가 없어 프로그램 성능이 향상되는 효과를 얻을 수 있다.**

## 제네릭 용어

| 한글 용어                | 영문 용어               | 예                                     |
| ------------------------ | ----------------------- | -------------------------------------- |
| 매개변수화 타입          | parameterized type      | List`<String>`                         |
| 실제 타입 매개 변수      | actual type parameter   | String                                 |
| 제네릭 타입              | generic type            | List`<E>`                              |
| 정규 타입 매개 변수      | formal type parameter   | E                                      |
| 비한정적 와일드카드 타입 | unbounded wildcard type | List`<?>`                              |
| 로 타입                  | raw type                | List                                   |
| 한정적 타입 매개변수     | bounded type bound      | `<T extends Number>`                   |
| 재귀적 타입 한정         | recursive type bound    | `<T extends Comparable<T>>`            |
| 한정적 와일드카드 타입   | bounded wildcard type   | List`<? extends Number>`               |
| 제네릭 메서드            | generic method          | static `<E>` List`<E>` asList(E`[]` a) |
| 타입 토큰                | type token              | String.class                           |

> “로(raw) 타입을 사용하면 런타임에 예외가 일어날 수 있으므로 사용하면 안 된다”



제네릭을 사용하지 않을 때의 코드를 보자.

```java
public static void main(String[] args) {        
  List list = new ArrayList();
  list.add("white");
  list.add("study");

  for (Object o : list) {
    String str = (String) o;
    System.out.println(str); 
  } 
}
```

 

- 제네릭이 생긴 이후 `List<Strng>`이 아닌 `List` 같은  `<>`가 생략된 타입을 을 로(raw) 타입이라 한다.
- 로 타입은 타입을 지정하지 않고 (Object 하위타입) 을 읽어들이고, Object 형으로 데이터를 가진다.
- **raw 타입**은 제네릭을 사용하지 않았던 과거 자바 버전과의 호환성을 위해서 존재하는 타입 매개변수가 없는 제네릭 타입이다.
- 따라서 String 으로 쓰기 위해서는 명시적인 타입 캐스팅이 필요하다.
  - (String)list.get(0);

명시적인 타입 캐스팅은 귀찮으며, 실수를 유발한다.

또한 로 타입 컬렉션은 Object 타입을 넣을 수 있기 때문에, String 만 들어가리라는 보장이 없다.

잘못된 타입을 캐스팅 하는 경우  **ClassCastException 이 발생한다.**

>  ClassCastException 는 RuntimeException 의 하위 예외로, UncheckedException 이다.

컴파일타임에 알 수 없고, 런타임에서 예외를 발생하여 문제를 일으킨다.



# **제네릭의 특징**

### **1. 불공변**

- 제네릭과 비교하여 배열은 공변이다.
- 공변이라 함은, Sub 가 Super 의 하위타입일 때, Sub[] 는 Super[] 의 하위타입인 것이다.
- 예를 들어, String 은 Object 의 하위타입이기 때문에, String[] 은 Object[] 의 하위타입이다.
- 따라서 아래와 같은 코드는 컴파일오류를 뱉지 않으며, 런타임에서야 예외를 던지는 문제가 발생한다.

```java
Obect[] ob = new String[5]; // 가능
ob[0] = 1; // ArrayStoreException 을 런타임에 발생
```

- 그에 반해, 제네릭은 불공변이다.
- List<String> 은 List<Object> 의 하위타입이 되지 않는다.
- 따라서 아래와 같은 코드는 컴파일타임에 에러를 내기 때문에 사전에 차단할 수 있다.

```java
List<Object> obList = new ArrayList<String>(); // 컴파일에러 !
obList.add(1);
```

| 상태                        | 설명                                                         |
| --------------------------- | ------------------------------------------------------------ |
| 불공변(무공변, (Invariant)) | `List<String>`은 `List<Object>`의 하위타입이 아니다.         |
| 공변 (Covariant)            | String 이 Object의 서브타입이면, `List<String>`은 `List<? extend Object> `의 서브타입이다. |
| 반공변 (Contravariant)      | String 이 Object의 서브타입이면,  `List<Object>`은 `List<? super String>` 의 서브타입이다. |



### **2. 타입추론**

- 타입추론은 메서드를 호출하는 코드에서 타입인자가 정의한대로 제대로 쓰였는지 살펴보는 컴파일러의 기능이다.
- 제네릭 메서드나, 제네릭 타입이 사용된 코드를 보고 사용 가능하다. 

 

### **3. 소거 (Erasure)**

- 제네릭은 **타입의 정보가 런타임에는 소거** 된다.
- 원소의 타입을 컴파일타임에만 검사하고 보증함으로써, 런타임에는 타입 정보를 알 수 조차 없게 한다.
- 이를 **실체화가 되지 않는다** 라고 한다.
- 이는 제네릭이 생기기 이전의 레거시코드가 호환될 수 있도록 한 조치이다.
- 반면에 배열은 타입 정보를 런타임에도 가지고 있으며, 이를 실체화 된다고 한다. 



실제 제네릭을 사용한 코드의 바이트 코드를 보면 알 수 있다.

* 바이트코드는 런타임에 읽는 코드이므로 런타임에 타입 정보가 소거된다면 여기에 타입정보가 없다. 

List를 사용한다면

```
Ljava/util/List
```

모두 이런 식으로 타입 정보는 소거되어 있다.  


equals 에서는

```
Ljava/lang/Object
```

처럼 모든 타입을 포괄하는 Object 로 바꾼 것을 볼 수 있다.

 

이처럼 런타임에는 제네릭 타입정보는 모두 **소거** 된다.

 

타입 소거에 대한 예시는 아래와 같다.

## 타입 소거

| 매개변수화 타입              | 소거 후    |
| ---------------------------- | ---------- |
| `List<String>`               | List       |
| `Map.Entry<String,Long>`     | Map.Entry  |
| `Pair<Long,Long>[]`          | Pair[]     |
| `Comparable<? super Number>` | Comparable |

| 타입 파라미터                           | 소거 후       |
| --------------------------------------- | ------------- |
| `<T>`                                   | Object        |
| `<T extends Number>`                    | Number        |
| `<T extends Comparable<T>>`             | Comparable    |
| `<T extends Cloneable & Comparable<T>>` | Cloneable     |
| `<T extends Object & Comparable<T>>`    | Object        |
| `<S, T extends S>`                      | Object,Object |

| 매개변수화 메서드                                            | 소거 후                                                      |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| `Iterator<E> iterator()	`                                 | Iterator iterator()                                          |
| `<T> T[] toArray(T[] a) 	`                                | Object[] toArray(Object[] a)                                 |
| `<U> AtomicLongFieldUpdater<U> newUpdater(Class<U> tclass, String fieldName)	` | AtomicLongFieldUpdater newUpdater(Class tclass,String fieldName) |





# 제네릭 사용법

제네릭은 `클래스`, `인터페이스` , `메소드`에 사용할 수 있다. 이 때 중요한 것은 매개변수로 '타입'을 전달할 수 있다는 것이다.



## 제네릭 클래스, 인터페이스 에서 사용

```java
public class 클래스명<T, T2, K, ...> {...}
public interface 인터페이스명<T, T2, K, ...> {...}
```

* 클래스명 다음에 `<T>` 를 (정규) 타입 파라미터(타입 변수)라 한다.

  * 의미적으로는 해당 클래스나 인터페이스 내에서는 T 타입을 사용하겠다는 것이다.

  

* 꺾쇠 괄호( `<>` )로 구분 된 타입 매개 변수는 클래스 이름 뒤에 온다. 객체가 생성될 때 타입 파라미터를 받는 부분이다.

> <> 는 비공식적으로 `다이아몬드 연산자`라고 하기도 한다.
>
> `다이아몬드 연산자` 안에는 `primitive 타입을 쓸 수 없다`. int 는 Integer 로 처럼 Wrapper 클래스를 사용해야 한다.



제네릭 static 필드는 사용할 수 없다.

 왜냐하면 클래스가 인스턴스가 되기 전에 static은 메모리에 올라가는데 이 때 필드의 타입인 T가 결정되지 않기 때문에 사용할 수 없다.

* 제네릭 메소드는 static이 가능하다. 
* 제네릭 메소드는 호출 시에 타입을 지정하기 때문에 static이 가능하다. 

### 타입 변수

- 임의의 참조형 타입을 의미하며 아무런 이름이나 지정해도 컴파일하는데 전혀 상관이 없다.
- 현존하는 클래스를 사용해도되고 존재하지 않는 것을 사용해도 된다.
- 여러 개의 타입 변수는 쉼표(,)로 구분하여 명시할 수 있다.
- 타입 변수는 클래스에서뿐만 아니라 메소드의 매개변수나 반환값으로도 사용할 수 있다.
- 꼭 'T' 를 사용안하고 어떠한 문자를 사용해도되지만 아래의 네이밍을 지켜주는 것이 좋다.

### 제네릭 타입의 이름 정하기

- E : 요소 (Element, 자바 컬렉션에서 주로 사용됨)
- K : 키 (key)
- N : 숫자
- T : 타입 (type)
- V : 값 (value)
- S,U,V : 두번 째, 세 번째, 네 번째에 선언된 타입

#### 멀티 타입 파라미터

- 타입 파라미터가 두개 이상 필요한 경우가 있다.
- 그럴 땐, 타입 파라미터를 `<>` 안에 쉼표로 필요한 만큼 선언하면 된다.



Java SE 7 부터 컴파일러가 선언을 살펴본 후 타입을 추론 할 수 있다면 일반 클래스의 생성자를 호출하는 데 필요한 타입 인자를 빈 타입 인자 `<>`로 바꿀 수 있다.

```java
public class Box<T> {
    private T type;

    public static void main(String[] args) {
        //선언부에 Integer로 명시되어 있기 때문에 타입 추론을 통해 다이아몬드로도 객체 생성 가능.
        Box<Integer> box; = new Box<>();
    }
}
```





## 제네릭 메소드 사용

```java
public <타입파라미터, ...> 리턴타입 메서드명(매개변수 ...) {}
```

* **제네릭 메소드**는 메소드의 선언 부에 적은 제네릭으로 리턴 타입, 파라미터의 타입이 정해지는 메소드이다.  

- 제네릭 메서드는 매개변수 타입과 리턴 타입으로 타입 파라미터를 갖는 메서드이다.
- return type 앞에 `<>` 를 추가하여 타입 파라미터를 적는다.
- 매개변수의 타입과 리턴타입, 메서드 안에서 사용하는 타입 파라미터를 적는다.



> 제네릭 메소드에서는 메소드를 정의할 때, 해당 메소드 내부에서 사용 할 타입 파라미터가 무엇인지 메서드 return type 앞에 반드시 명시 해주어야 한다. 
>
> 제네릭 메소드는 호출 시에  타입을 지정하기 때문에 static이 가능하다. 



### 제네릭 메소드는 호출 시에  타입을 지정하기 때문에 static이 가능하다. 

```java
public class Box<T> {
    
    static <T> T get(T id) {
        return id;
    }
}
```

클래스에 표시하는 `<T>`는 **인스턴스 변수**라고 생각하자. 

인스턴스가 생성될 때 마다 지정되기 때문이다. 

그리고 제네릭 메소드에 붙은 T는 **지역변수**를 선언한 것과 비슷하다. **(메소드의 붙은 모든 T는 클래스에 붙은 T와 다르다)**

* 지역변수의 타입을 명시해주는것. 



>  static을 사용하면 클래스 이름으로 접근하여 객체를 생성하지 않고 여러 인스턴스에서 공유해서 사용할 수 있다.
>
> 변수같은 경우 해당 값을 사용하려면 값의 타입을 알아야 하지만
>
> 메소드의 경우 해당 기능을 공유해서 사용하는 것이기 때문에 제네릭 타입 변수 T 를 매개변수로 사용한다고 하면
>
> `제네릭 타입 변수 T 값은 메소드 안에서 지역 변수로 사용`되기 때문에 변수와 달리 메소드는 static 으로 선언 되어 있어도 제네릭을 사용할 수 있다.



```java
public static <K, V> boolean compare(Pair<K, V> m1, Pair<K, V> m2) {
  boolean keyCompare = m1.getKey().equals(m2.getKey());
  boolean valueCompare = m1.getValue().equals(m2.getValue());
  return keyCompare && valueCompare;
}
```

위의 메서드가 런타임시에 다음과 같이 동작하게 된다.

```java
public static <String, Integer> boolean compare(Pair<String, Integer> m1, Pair<String, Integer> m2) {
  boolean keyCompare = m1.getKey().equals(m2.getKey());
  boolean valueCompare = m1.getValue().equals(m2.getValue());
  return keyCompare && valueCompare;  
}
```





# 제네릭 주요 개념 (바운디드 타입, 와일드 카드)



## 바운디드 타입 (Bounded Type)

제네릭 타입에서 타입 인자로 사용할 수있는 타입을 제한하려는 경우 특정 클래스를 상속받은 하위 클래스만 사용하려고 할 때 사용한다. 

* 특정 타입의 서브 타입으로 제한하는 기능

바운디드 타입 파라미터를 선언하려면 `<타입 파라미터의 이름, extends , 상위 바운드> `를 나열한다

*  **'~중에 아무거나'**  라는 뜻

```java
<T extends UpperBound> // UpperBound를 상속받거나 구현한 하위 클래스만
```

여기서의 `extends` 키워드는  `implements`의 기능까지 포함하기 때문에 상위 바운드는 인터페이스가 될 수 있다.  


또는 여러 개의 상위 바운드를 가질 수 도 있다.

```java
<T extends B1 & B2 & B3>
```

만약 `여러 상위 바운드 중에서 클래스가 있다면 해당 클래스가 가장 앞`에 와야한다. 안그러면 컴파일 에러가 발생한다

```java
<T extends Class1 & Interface1 & Interface2>
```



Bounded Type은 제네릭 메소드에서도 사용할 수 있다.

```java
public static <T extends Serializable> write(T object) {  
  File file = new File("test.txt");    
  try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file))) {
    outputStream.writeObject(object);
  } catch (IOException e) {
    e.printStackTrace();
  }
}
```

- ObjectOutputStream 의 메서드 writeObject() 는 직렬화할 수 있는 객체만 매개변수로 넣을 수 있다.
- 객체가 Serializable 을 구현하고 있지 않으면, NotSerializableException 을 뱉는다.
- 물론, try-catch 예외처리를 통해 해결할 수 있지만, write 메소드에서 Serializable 의 하위 타입만 받아서 컴파일 타임에 에러를 잡아낼 수 있다.



바운디드 타입의 또 다른 특징은 상위 바운드에 해당하는 클래스의 메소드를 코드에서 사용할 수 있다는 점이다.

```java
public class GeTest<T extends SuperClass> {

    private T field;

    public GeTest(T field) {
        this.field = field;
    }

    public void call() {
        field.call(); // 상위 클래스의 메서드 호출 
        field.callSuper();
    }

    public static void main(String[] args) {
        GeTest<SuperClass> superGeneric = new GeTest<>(new SuperClass());

        superGeneric.call();

        GeTest<SubClass> subGeneric = new GeTest<>(new SubClass()); // 하위 타입을 파라미터로 전달

        subGeneric.call();

    }
}

class SuperClass {

    public void call() {
        System.out.println("im super");
    }

    public void callSuper() {
        System.out.println("super call");
    }
}

class SubClass extends SuperClass {

    @Override
    public void call() {
        System.out.println("im child");
    }

    public void callSub() {
        System.out.println("Sub call");
    }
}
// 결과
im super
super call

im child
super call
```

* 서브클래스에서 call() 메소드가 오버라이딩이 되었다면 오버라이딩 된 메소드가 호출된다.
* 다만, 바운디드 타입에서는 자식 클래스의 메소드는 호출하지 못한다. - callSub()메소드



## 와일드 카드 - WildCard

제네릭으로 구현된 메소드의 경우 선언된 타입으로만 매개변수를 입력해야한다. 

이를 상속받은 클래스 혹은 부모 클래스를 사용하고 싶어도 불가능하고, 어떤 타입이 와도 상관없는 경우에 대응하기 좋지 않다. 

```java
class SomeClass<T extend Box> 
```

* 이 제네릭은 Box 또는 Box를 상속받은 클래스만 사용할 수 있다. Box의 Super 클래스도, 다른 타입도 사용하지 못한다 

이를 위한 해법으로 Wildcard를 사용한다.



와일드카드는 물음표(?) 를 사용하며, 알 수 없는 유형을 나타낸다.

와일드 카드는 파라미터 변수, 필드 또는 지역변수의 타입 등 다양한 상황(때때로 리턴 타입에도 사용할 수 있음.)에서 사용할 수 있다.

 와일드 카드는 제네릭 메서드 호출, 제네릭 클래스 인스턴스 생성 또는 수퍼 타입의 타입 인자로는 사용될 수 없다.

> 와일드카드는 제네릭이 **불공변**이기 때문에 사용한다는 관점도 있다.
> 매개변수로 객체를 받는데 아무 타입의 객체를 받고 싶다면 모든 객체의 부모인 Object 를 사용하면 된다.
>
> 하지만 만약  매개변수로 리스트를 받는데 아무 타입의 리스트를 받고 싶을 때에 `List<Object>` 는 모든 `List<>`를 받지 못한다. 
>
> 제네릭은 불공변이기 때문에 `List<>`의 부모타입이 아니다.
> 따라서 List<?> 라는 와일드카드가 나왔다.



- 와일드카드는 다이아몬드 연산자 `<>` 안에 `?` 를 쓴 `<?>` 로 표현된다.
- 와일드카드는 의미적으로, 어떤 용도로도 쓰일 수 있는 비장의 카드라는 뜻이다.

비슷하지만 면밀히 보면 다르다. 제일 확연한 차이는 사용되는 위치이다.

- 타입 매개변수는 제네릭 타입에서는 클래스 이름 옆에 `<T>`, 메서드의 반환타입 앞의 `<T>` 를 붙였었다. 
- 이는 해당 클래스에서, 해당 메서드에서는 T 타입을 사용할 것이라는 뜻이었다. **해당 클래스와 메서드 전체에 적용되는 타입 매개변수를 선언해준 격이다.**



### 와일드 카드 종류



1. Unbounded WildCard - `<?>` - 제한이 없는 

2. Upper Bounded Wildcard - `<? extends Foo>`  - 상한 와일드카드

3. Lower Bounded Wildcard -`<? super Foo>` - 하한 와일드카드



>  상하한을 동시에 지정할 수는 없다.

### Unbounded WildCard (언바운디드 와일드카드, )

Unbounded : 무한한, 끝이 없는

- Unbounded Wildcard는 `List<?>` 와 같은 형태로 물음표만 가지고 정의되어지게된다. 
  - 컴파일 시 `내부적으로 Object로 정의`되어서 사용되고 모든 타입의 인자를 받을 수 있다. 
  - 타입 파라미터에 의존하지 않는 메소드만을 사용하거나 Object 메소드에서 제공하는 기능으로 충분한 경우에 사용한다.
- Object 클래스에서 제공되는 기능을 사용하여 구현할 수 있는 메서드를 작성하는 경우에 사용
- 타입 파라미터에 의존적이지 않은 일반 클래스의 메소드를 사용하는 경우에 사용 
  - ex) List.clear, List.size 등등

### Upper Bounded Wildcard (상한 와일드카드)

- Upper Bounded Wildcard는 `List<? extends Foo>` 와 같은 형태로 사용하고 특정 클래스의 `자식 클래스만`을 인자로 받는다는 것이다. 
- Foo 클래스를 상속받는 어느 클래스가 와도 되지만 사용할 수 있는 기능은 Foo클래스에 정의된 기능만 사용이 가능하다.

### Lower Bounded Wildcard (하한 와일드카드)

- Lower Bounded Wildcard는 `List<? super Foo>`와 같은 형태로 사용하고, Upper Bounded Wildcard와 다르게 특정 클래스의 `부모 클래스만`을 인자로 받는다는 것이다.



> **<? extends T>**
> T와 그 자손 타입만 가능(upper bound)
>
> **<? super T>**
> T와 그 조상 타입만 가능(lower bound)
>
> **<?>**
> 제한 없이 모든 타입이 가능. <? extneds Object>와 동일한 표현



* `List<Integer>` 와 `List<? extends Integer>` 는 무엇이 다른가?

`List<Integer>` 는 `List<? extends Integer>` 보다 제한이 많다.

왜냐하면,  `List<Integer>` 는 Integer 타입만 사용 가능하고,  `List<? extends Integer>` 는 Integer 타입과 Integer 타입의 super type에도 사용할 수 있기 때문이다  



**와일드 카드 주의사항** 

와일드 카드를 사용한 제네릭 List 타입은 비공식적으로 read-only로 간주된다.  
 하지만 아래 작업이 가능하기 때문에 이 말이 완전히 보장되지는 않는다.

- `null` 을 추가 할 수 있다 .
- `clear` 를 호출 할 수 있다 .
- iterator를 가져오고 `remove`를 호출 할 수 있다 .
- 와일드 카드를 캡처하고 List에서 읽은 요소를 쓸 수 있다.



**와일드 카드 캡처**

`헬퍼 메소드`를 이용하여 컴파일러에게 와일드 카드 타입을 유추할 수 있도록 도와주는 방식을 와일드 카드 캡처라고 한다.

컴파일러는 기본적으로 `List<?>`에 대해 `List<Object>`로 처리하려고 하며 set() 메소드에 엘리먼트 타입을 컴파일 타임에 확인할 수 없기 때문에 오류가 발생한다.

```java
public class WildCardTest {

    static void foo (List<?> i) {
        //컴파일 오류
        i.set(0, i.get(2));
    }

    public static void main(String[] args) {
        List<Integer> li = Arrays.asList(1,2,3);
        System.out.println(li);
        foo(li);
        System.out.println(li);
    }
}
```

헬퍼 메소드를 추가해서 컴파일러가 와일드 카드 타입을 추론할 수 있도록 해준다.

```java
public class WildCardTest {

    static void foo (List<?> i) {
        originalMethodNameHelper(i);
    }

    private static <T> void originalMethodNameHelper(List<T> i) {
        i.set(0, i.get(2));
    }

    public static void main(String[] args) {
        List<Integer> li = Arrays.asList(1,2,3);
        System.out.println(li);
        foo(li);
        System.out.println(li);
    }
}
```



# Erasure - 제네릭 타입 소거 (Type Erasure)

타입 소거란((Type Erasure)), 제네릭은 컴파일 타입에만 제약을 가하고, 런타임시 타입에 대한 정보를 버리는 것이다.

여기서 타입을 버린다는 것은 다음과 같이 교체되는 것을 의미한다.

- **bounded type** **->** **bound type**
- **unbounded type** **->** **Object**



* 제네릭에 primitive 타입을 사용하지 못하는 이유는 이 기본 타입은 Object 클래스를 상속받고 있지 않기 때문이라고 생각한다.

> Type Erasure를하고 형변환을 넣어주는 이유는, 제네릭이 도입되기 이전의 소스코드와의 호환성을 유지하기 위해서다





### Type Erasure의 규칙

- 컴파일 시 제네릭 타입의 타입 파라미터가 `Bounded Type인 경우에는 타입 파라미터를 한계 타입`으로, 
  - `<E extends Comparable>` -> `<Comparable>`
- `Unbounded 경우(<?>, <T>)` 모든 타입 파라미터를 `Object`로 바꾼다 . 
  - `<?>, <T>` -> `<Object>`
- 따라서 생성 된 바이트 코드에는 보통의 클래스, 인터페이스 및 메서드 만 포함된다.

* 제네릭 메소드도 같은 규칙으로 대체된다.



#### 제네릭의 Type Erasure 시기

* 자바 코드에서 선언되고 사용된 제네릭 타입은 컴파일 시 컴파일러에 의해 자동으로 검사되어 타입 변환된다

* 그리고서 코드 내의 모든 제네릭 타입은 제거되어, 컴파일된 class 파일에는 어떠한 제네릭 타입도 포함되지 않게 된다.

* 이런 식으로 동작하는 이유는 제네릭을 사용하지 않는 코드와의 호환성을 유지하기 위해서이다.





- type-safety를 유지하기 위해 필요한 경우 타입 캐스팅을 사용할 수 있다.
- 제네릭 타입을 상속받은 클래스에서는 다형성을 유지하기 위해 브릿지(Bridge) 메서드를 생성한다.



### 제네릭의 브릿지 메서드 

`제네릭 클래스를 상속`받거나  `제네릭 인터페이스를 구현하는 클래스 또는 인터페이스`를 컴파일 할 때 컴파일러는 타입 Erasure 프로세스의 일부로 `*브릿지 메서드*` 라는 합성 메서드를 만들어야 할 수도 있다 .   
일반적으로 브리지 메서드에 대해서는 걱정할 필요가 없지만 stack trace에 나타나는 경우 당황 할 수 있기 때문에 알아두는 것이 좋다.



> 확장된 제네릭 타입에서 다형성 보존을 위해 어떠한 클래스나, 인스턴스를 상속 혹은 구현할때 bridge method를 생성한다.  

  

```java
public class MyComparator implements Comparator<Integer> {
   public int compare(Integer a, Integer b) {
      //
   }
}
```

Comparator 인터페이스를 구현한  MyComparator 예제 코드이다.   

런타임에서는 bridge 메소드가 어떻게 생길까? 

```java
public class MyComparator implements Comparator {
   public int compare(Integer a, Integer b) {
      //
   }
}
```

1. 위와 같이 타입이 소거된 상태로 변한다. 
2. 그리고 Comparator의 compare 메소드의 매개변수 타입은 Object로 바뀐다.



> 이러한 메소드 시그니처 사이에 불일치를 없애기 위해서 컴파일러는 런타임에 해당 제네릭 타입의 타임소거를 위한 `bridge method`를 만들어 준다.

 

```java
public class MyComparator implements Comparator<Integer> {
   public int compare(Integer a, Integer b) {
      //
   }

   //THIS is a "bridge method"
   public int compare(Object a, Object b) {
      return compare((Integer)a, (Integer)b);
   }
}
```

그러면 매개변수가 Integer 타입의 compare 메소드를 사용할 수 있게 된다 .





* bridge 메서드는 type erasure 이후 클래스에서의  같은 이름의 브릿지 메서드를 가지고 원래의 메서드의 역할을 대신한다.
  * 제네릭 타입을 사용할 수 있는 일반 class, interface, method에만 소거 규칙을 적용한다.
  * 타입 안정성 측면에서 필요하면 type casting을 넣는다.



다음과 같은 예제도 있다.

```java
public class Node<T> {
    public T data;
    public Node(T data) { this.data = data; }
    public void setData(T data) {
        System.out.println("Node.setData");
        this.data = data;
    }
}

// 컴파일 할 때 (타입 소거 전) 
public class MyNode extends Node<Integer> {
    public MyNode(Integer data) { super(data); }

    public void setData(Integer data) {
        System.out.println("MyNode.setData");
        super.setData(data);
    }
}

// 런타임 때 (타입 소거 후)
public class MyNode extends Node {

    // Bridge method generated by the compiler
    public void setData(Object data) {
        setData((Integer) data);
    }

    public void setData(Integer data) {
        System.out.println("MyNode.setData");
        super.setData(data);
    }
    // ...
}
```





### 제네릭의 예 - Collections.sort()

자바의 Collections는 요소를 정렬하는 static 메소드 sort()를 제공하는데, 이 메소드가 제네릭 타입을 사용하는 제네릭 메소드의 예 중 하나이다.

 

Collection.sort(List, Comparator)의 선언부

```java
static <T> void sort(List<T> list, Comparator<? super T> c)
```

`List<T>`와 `Comparator<? super T>` 타입의 두 개의 매개변수를 받는 sort() 메소드이다.

첫 번째 매개변수는 정렬 대상이고 두 번째 매개변수는 정렬할 방법이 정의된 Comparator이다.



* Comparator는 `<? super T>` 라는 제네릭 타입이 선언돼 있는데, 이는 정렬 대상 요소들의 타입 혹은 그 조상 타입의 Comparator를 매개변수로 한다는 뜻이다.

 

Collection.sort(List)의 선언부

```java
public static <T extends Comparable<? super T>> void sort(List<T> list)
```

`List<T> `한 개를 매개변수로 받는 sort() 메소드이다.

타입 T를 요소로 하는 List를 넘기면 되는데, T 또는 그 조상 클래스가 Comparable을 구현해야 한다는 의미이다.





### 참조

* https://alkhwa-113.tistory.com/entry/%EC%A0%9C%EB%84%A4%EB%A6%AD

* https://itkjspo56.tistory.com/275
* https://www.notion.so/4735e9a564e64bceb26a1e5d1c261a3d
* https://woodcock.tistory.com/37

* https://medium.com/@joongwon/java-java%EC%9D%98-generics-604b562530b3

* https://rockintuna.tistory.com/102#%EC%A0%9C%EB%84%A4%EB%A6%AD-%EB%A9%94%EC%86%8C%EB%93%9C