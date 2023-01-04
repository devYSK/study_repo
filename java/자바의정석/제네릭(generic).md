# Java Generics

# 1. 제네릭스(Generics)

지네릭스는 다양한 타입의 객체들을 다루는 메서드나 컬렉션 클래스에 컴파일 시의 타입 체크를 해주는 기능

 `데이터 타입(data type)`을 `일반화(generalize)하는 것`을 의미한다

- 컴파일 시 타입을 체크해 주는 기능(compile-time type check) - JDK1.5에서 도입

  

제네릭의 장점

1. 타입 안전성을 제공한다.
2. 타입 체크와 형 변환을 생략할 수 있으므로 코드가 간결해진다. 

> 객체의 타입 안전성을 높이고 형 변환의 번거로움을 줄여준다 - 타입 안정성, 유연성 향상



제네릭 타입은 클래스와 메서드에 선언할 수 있다.

```java
class Box<T>  { // 지네릭 타입 T를 선언. T는 타입변수
  T item;
  
  void setItem(T item)  {
    this.item = item;
  }
  
  T getItem() {
    return item;
  }
}
```

지네릭 클래스가 된 Box 클래스의 객체를 생성할 때는
다음과 같이 참조변수와 생성자에 타입 `T` 대신 사용될 실제 타입을 지정해야 한다.

```java
Box<String> b = new Box<String>();  // 타입 T대신 실제 타입 지정
b.setItem(new Object());  // 에러. String 외의 타입은 지정 불가
b.setItem("ABC"); // OK. String 타입이므로 가능
```





### 제네릭의 용어 

* `Box<T>` : 지네릭 클래스. 'T의 Box' 또는 'T Box'라고 읽는다.
* `T` : 타입 변수 또는 타입 매개변수. (T는 타입 문자)
* `Box` : 원시 타입(raw type,  < > 가 없는 경우) 

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



> 컴파일 후에 < > 꺽쇠 안에 있던 `Box<String>` 또는 `Box<Integer>` 는 이들의 원시타입인 Boxfㅗ 바뀐다.
>
> 즉 제네릭 타입은 컴파일 후에 제거된다. 



### **제네릭스의 제약사항**
\- static 멤버에는 타입 변수 T를 사용할 수 없다.

```java
class Box<T> { 
  static T item; //에러 
  static int compare(T t1, T t2){} //에러 
}
```

\- 제네릭 타입의 배열 T[]를 생성하는 것은 허용되지 않는다.

```java
class Box<T> { 
  T[] itemArr; // T타입의 배열을 위한 참조변수 
  
  T[] toArray() { 
    T[] tmpArr = new T[itmeArr.lenght]; //지네릭 배열 생성 불가 
    return tmpArr; 
  } 
}
```

- 모든 객체에 대해 동일하게 동작해야하는 static멤버에 타입변수 T를 사용할 수 없다.
  T는 인스턴스 변수로 간주되기 때문이다. (static 멤버는 인스턴스 변수를 참조할 수 없다.)



- 제네릭 배열 타입의 참조변수를 선언하는 것은 가능하지만,
  `new T[10]` 과 같이 배열을 생성하는 것은 안된다.
  new 연산자 때문인데, 이 연산자는 컴파일 시점에 타입 T가 무엇인지 정확히 알아야 한다.
  instanceof 연산자도 같은 이유로 T를 피연산자로 사용할 수 없다.



## 제**네릭 클래스의 객체 생성과 사용**
\- 지네릭 클래스 Box<T>의 선언

```java
class Box<T> { 
  ArrayList<T> list = new ArrayList<T>(); 
  
  void add(T item) {
    list.add(item);
  } 
  
  T get(int i) {
    return list.get(i);
  } 
  
  ArrayList<T> getList() {
    return list;
  } 
  
  int size() {
    return list.size();
  } 
  
  public String toString() {return list.toString();} 
}
```

\-` Box<T>`의 객체 생성. 참조 변수와 생성자에 대입된 타입이 일치해야 함

```java
Box<Apple> appleBox = new Box<Apple>(); 
Box<Apple> appleBox = new Box<Grape>(); //에러. 대입된 타입이 다르다. 
Box<Fruite> appleBox = new Box<Apple>(); //에러. 대입된 타입이 다르다.
```

\- 두 제네릭 클래스가 상속관계이고, 대입된 타입이 일치하는 것은 괜찮다.

```java
Box<Apple> appleBox = new FruitBox<Apple>(); // 다형성 
Box<Apple> appleBox = new Box<>(); //JDK1.7부터 생략 가능
```

\- 대입된 타입과 다른 타입의 객체는 추가할 수 없다.

```java
Box<Apple> appleBox = new Box<Apple>(); 
appleBox.add(new Apple()); 
appleBox.add(new Grape()); //에러. Box<Apple>에는 Apple객체만 추가 가능
```



## 제한된 제네릭 클래스 (Bounded Type Generic)

지네릭 타입에 `extends`를 사용하면, 특정 타입의 자손들만 대입할 수 있게 제한할 수 있다.

```java
class FruitBox<T extends Fruit> { //Fruit의 자손만 타입으로 지정 가능
  ArrayList<T> list = new ArrayList<T>();
  ...
}
```

인터페이스를 구현해야 한다는 제약이 필요하다면, 이때도 implements 대신 `extends`를 사용한다.

```java
interface Eatable {}

class FruitBox<T extends Eatable> {
  ...
}
```

클래스 Fruit의 자손이면서 Eatable 인터페이스도 구현해야하면 `&` 기호로 연결한다.

```java
class FruitBox<T extends Fruit & Eatable> {
  ...
}
```

* 인터페이스의 경우에도 'implements'가 아닌, 'extends'를 사용



## 와일드 카드**'?'**

제네릭 타입은 컴파일러가 컴파일할 때만 사용하고 제거해버린다.
때문에 제네릭 타입이 다른 것만으로는 Overloading이 성립하지 않고 ‘메서드 중복 정의’가 된다.



이럴 때 사용하는 것이 `와일드 카드`이며, 와일드 카드는 어떤 타입도 될 수 있다. 기호 `?`로 표현한다.

`?`만으로는 Object타입과 다를 게 없으므로,
`extends`와 `super`로 상한과 하한을 제한할 수 있다.

```java
<? extends T> T와 그 자손들만 가능
<? super T> T와 그 조상들만 가능
<?> 모든 타입 가능. <? extends Object>와 동일
```



> **와일드 카드에는 `&`을 사용할 수 없다.**



## 제네릭 메소드

> 메소드의 선언부에 제네릭 타입이 선언된 메소드가 제네릭 메소드다.
> 제네릭 메소드는제네릭 클래스가 아닌 클래스에도 정의할 수 있다.

* 반환 타입 앞에 제네릭 타입이 선언된 메서드

```java
class FruitBox<T> {
    ...
  static <T> void sort(List<T> list, Comparator<? super T> c) {
    ...
  }
}
```

- 제네릭 클래스에 정의된 타입 매개변수와 제네릭 메소드에 정의된 타입 매개변수는 전혀 별개의 것이다.
- static 멤버에는 타입 매개변수를 사용할 수 없지만, 이처럼 메소드에 제네릭 타입을 선언하고 사용하는 것은 가능하다.
  - 메소드에 선언된 제네릭 타입은 `지역 변수를 선언한 것과 비슷하다`
  - 타입 매개변수는 메소드 내에서만 지역적으로 사용될 것이므로 메소드가 static이건 아니건 상관없다.
  - 같은 이유로, 내부 클래스에 선언된 타입 문자가 외부 클래스의 타입 문자와 같아도 구별될 수 있다.

## **제네릭 타입의 형 변환**
\- 제네릭 타입과 원시 타입 간의 형 변환은 불가능

```java
Box box = null; 
Box<Object> objBox = null; 
box = (Box)objBox; //제네릭 타입 -> 원시 타입. 경고 발생 
objBox = (Box<Object>)box; //원시 타입 -> 지네릭 타입. 경고 발생
```

\- 와일드카드가 사용된 제네릭 타입으로는 형 변환 가능

```java
Box<? extends Object> wBox = new Box<String>(); 
FruitBox<? extends Fruit> box = null; 
FruitBox<Apple> appleBox = (FruitBox<Apple>)box; //미확인 타입으로 형변환 경고
```



## **제네릭 타입의 소거(제거)** - Type Erasure
\- 컴파일러는 제네릭 타입을 제거하고, 필요한 곳에 형 변환을 넣는다.

- 제네릭은 **타입의 정보가 런타임에는 소거** 된다.
- 원소의 타입을 컴파일타임에만 검사하고 보증함으로써, 런타임에는 타입 정보를 알 수 조차 없게 한다.
- 이를 **실체화가 되지 않는다** 라고 한다.
- 이는 제네릭이 생기기 이전의 레거시코드가 호환될 수 있도록 한 조치이다.
- 반면에 배열은 타입 정보를 런타임에도 가지고 있으며, 이를 실체화 된다고 한다. 

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





## 참조

* 자바의 정석

* https://ryan-han.com/post/dev/java-generics/

