# 람다식(Lambda Expression)



JDK1.8부터 추가된 람다식의 도입으로 인해서 이제 자바는 객체지향언어인 동시에 **함수형 언어**가 됐다.
객체지향언어가 함수형 언어의 기능까지 갖추게 하는 일은 쉬운 일이 아니었을텐데도 기존의 자바를 거의 변경하지 않고도
함수형 언어의 장점을 잘 접목시키는데 성공했다



람다식이란,

* 메서드를 하나의 식(expression)으로 표현한 것.
* 메서드를 람다식으로 표현하면 메서드의 이름과 반환값이 없어지므로 람다식을 익명함수(anonymous function)라고도 한다.



람다식은 `메서드의 매개변수`로 전달될 수 있고, `메서드의 결과`로 반환될 수 있다.
즉, 메서드를 변수처럼 다루는 것이 가능하다.



**함수와 메서드의 차이**

- 근본적으로 동일하다. 
- 함수는 수학에서 따온 일반적 용어, 
- 메서드는 객체의 행위나 동작을 의미하는 객체지향개념 용어
- 함수는 클래스에 독립적이지만, 메서드는 클래스에 종속적이다

그러나 람다식을 통해 메서드가 독립적인 기능을 하기 때문에 함수라는 용어를 사용하게 되었다.

 

## 람다식 작성방법

람다식은 '익명 함수'답게 메서드에서 **이름과 반환타입을 제거**하고 매개변수 선언부와 몸통{ } 사이에 '->'를 추가한다.

```
반환타입 메서드이름(매개변수 선언) {
	문장들
}

==>

(매개변수 선언) {
	문장들
}
```

예를 들어서 두 값 중에서 큰 값을 반환하는 메서드 max를 람다식으로 변환하면, 다음과 같다.

```java
int max(int a, int b) {
	return a > b ? a : b;
}
```

==>

```java
(int a, int b) -> {
	return a > b ? a : b;
}
```

- 반환값이 있는 메서드는 return 대신 식(expression)으로 대신할 수 있다.(연산 결과가 자동으로 반환값이 되고 `; `생략)
- 매개변수의 타입은 추론가능하면 생략 가능 (대부분 생략 가능) - 람다식에 반환타입이 없는 이유도 항상 추론이 가능하기 때문
- 두 매개변수 중 하나의 타입만 생략하는 것은 불가능
- 매개변수가 하나뿐이면 괄호() 생략 가능
- 중괄호{} 안의 문장이 하나일 때는 중괄호{} 생략 가능 (문장 끝에 `;` 생략)
- 중괄호{} 안의 문장이 return문일경우 중괄호{} 생략 불가능



## 함수형 인터페이스(Functional Interface)

람다식을 다루기 위한 인터페이스.

람다식은 익명 함수가 아닌 사실은 익명 객체이다.

* 람다식은 메서드와 동등한 것이 아니라 익명클래스의 객체와 동등하다.

```java
// 람다식
(int a, int b) -> a > b ? a : b

// 익명클래스의 객체
new Object()  {
  int max(int a, int b) {
    return a > b ? a : b ;
  }
}
```

람다식으로 정의된 익명 객체의 메서드를 호출하려면 참조변수가 필요하다.
이 때, 참조변수의 타입은 클래스 또는 인터페이스가 가능한데,
람다식과 동등한 메서드가 정의되어 있는 것이어야 한다.

이 때, 기본 자바의 규칙을 어기지 않으면서 `객체 참조 변수를 사용하기 위해` `하나의 메서드가 선언된 인터페이스`를

를 사용하기로 하였으면 람다식을 다루기 위한 인터페이스를 `함수형 인터페이스`라고 하기로 한것이다.

```java
// 예를 들어 max() 메서드가 정의된 Myfunction 인터페이스 정의
interface MyFunction  {
  public abstract int max(int a, int b);
}

// MyFunction 인터페이스를 구현한 익명클래스 객체 생성
  
MyFunction f = new MyFunction() {
  public int max (int a, int b) {
  	return a > b ? a : b;
	}
}

int big = f.max(5, 3);  //익명 객체의 메서드 호출

// 위의 익명 객체를 람다식으로 대체
MyFunction f = (int a, int b) -> a > b ? a : b;
int big = f.max(5, 3);
```

위 처럼 MyFunction 인터페이스를 구현한 익명 객체를 람다식으로 대체 가능한 이유는 람다식도 실제로는 익명 객체이고, MyFunction 인터페이스를 구현한 익명 객체의 메서드 max()와 람다식의 매개변수의 타입과 개수, 반환값이 일치하기 때문이다.

단, 함수형 인터페이스에는 `오직 하나의 추상 메서드만` 정의되어 있어야 한다. 그래야 `람다식과 인터페이스가 1:1로 연결되기 때문`이다.  
 반면 static 메서드와 default 메서드의 개수에는 제약이 없다.


@FunctionalInterface를 붙이면 컴파일러가 함수형 인터페이스를 올바르게 정의하였는지 확인해준다.

```java
@FunctionalInterface
interface MyFunction { // 함수형 인터페이스 MyFunction을 정의
	public absteract int max(int a, int b);
}
```

> '@FuncttionalInterface'를 붙이면, 컴파일러가 함수형 인터페이스를 올바르게 정의했는지 확인해주므로, 꼭 붙여야 한다







람다식을 참조변수로 다룰 수 있다는 것은 메서드를 통해 람다식을 주고받을 수 있다는 것을 의미한다.
즉, 변수처럼 메서드를 주고받는 것이 가능해진 것이다.
사실상 메서드가 아니라 객체를 주고받는 것이라 근본적으로 달라진 것은 아무것도 없지만,
람다식 덕분에 예전보다 코드가 더 간결하고 이해하기 쉬워졌다.



### **람다식의 타입과 형변환**

* 함수형 인터페이스로 람다식을 참조할 수 있지만, 람다식의 타입이 함수형 인터페이스의 타입과 일치하는 것은 아니다. 
* 람다식은 익명 객체이고 익명 객체는 타입이 없다. `(정확히는 타입이 있지만 컴파일러가 임의로 이름을 정하기 때문에 알 수 없다.) `

그러므로 아래와 같이 형변환이 필요하다.

```java
@FunctionalInterface
interface MyFunction {
	void myMethod(); // 추상 메서드
}

// -> 형변환
MyFunction f = (MyFunction) (()->{}); //
```

람다식은 이름이 없을 뿐 분명히 `객체`인데도, `Object 타입`으로 형변환 할 수 없다.   
람다식은 `오직 함수형 인터페이스로만 형변환이 가능`하다.   
굳이 Object 타입으로 형변환하려면 아래와 같이 먼저 함수형 인터페이스로 변환해야 한다.

```java
Object obj = (Object)(MyFunction)(()->{});
String str = (Object)(MyFunction)(()->{})).toString();
```



### 외부 변수를 참조하는 람다식

```java
void method(int i) {
	int val = 30; // final int val = 30;
    i = 10; // 에러1, 상수의 값을 변경할 수 없음
    
    MyFunction f = (i) -> { // 에러2, 외부 지역변수와 이름이 중복됨
            	System.out.println("			 i : " + i);
                System.out.println("	 	   val : " + val);
                System.out.println("	  this.val : " + ++this.val);
                System.out.println("Outer.this.val : " + ++Outer.this.val);
    }
};
```

람다식 내에서 외부에 선언된 변수에 접근하는 방법을 보여준다.

* 람다식 내에서 참조하는 지역변수는 final이 붙지 않았어도 상수로 간주된다.
* 람다식 내부에서 참조하는 외부 지역변수는 final이거나 effective final 이여야 한다. 
* 람다식 내에서 지역변수 i와 val을 참조하고 있으므로 람다식 내에서나 다른 어느 곳에서도 이 변수들의 값을 변경하는 일은 허용되지 않는다.
* 그리고 외부 지역변수와 같은 이름의 람다식 매개변수는 허용되지 않는다. 



# java.util.function 패키지 - 함수형 인터페이스 패키지

이 패키지에 자주 쓰이는 형식의 메서드를 함수형 인터페이스로 정의해놓았다.

| 함수형 인터페이스     | 메서드                 | 설명                                                         |
| --------------------- | ---------------------- | ------------------------------------------------------------ |
| java.lang.Runnable    | void run()             | 매개변수도 없고, 반환값도 없음                               |
| `Supplier<T>`         | T get()                | 매개변수는 없고, 반환값만 있음                               |
| `Consumer<T>`         | void accept(T t)       | Supplier와 반대로 매개변수만 있고, 반환값이 없음             |
| `Function<T>`         | R apply(T t)           | 일반적인 함수. 하나의 매개변수를 받아서 결과를 반환          |
| `Predicate<T>`        | boolean test(T t)      | 조건식을 표현하는데 사용. 매개변수는 하나, 반환 타입은 boolean |
| `BiConsumer<T, U>`    | void accept(T t, U u)  | 두개의 매개변수만 있고, 반환값이 없음                        |
| `BiPredicate<T, U>`   | boolean test(T t, U u) | 조건식을 표현하는데 사용됨. 매개변수는 둘, 반환값은 boolean  |
| `BiFunction<T, U, R>` | R apply(T t, U u)      | 두개의 매개변수를 받아서 하나의 결과를 반환                  |

매번 새로운 함수형 인터페이스를 정의하지 말고, 가능하면 이 패키지의 인터페이스를 활용하는 것이 좋다.
그래야 함수형 인터페이스에 정의된 메서드 이름도 통일되고, 재사용성이나 유지보수 측면에서도 좋다.

> 타입 문자 'T'는 'Type'을 'R'은 Return Type'을 의미한다.



Predicate는 `조건식`을 함수로 표현하는데 사용된다. - boolean 반환

- 수학에서 결과로 true 또는 false를 반환하는 함수를 Predicate 라고 한다.
- 매개변수가 2개인 함수형 인터페이스는 이름 앞에 ‘Bi’가 붙는다.
- Supplier는 매개변수는 없고 반환값만 존재하는데, 메서드는 두 개의 값을 반환할 수 없으므로 BiSupplier가 없다.
- 매개변수의 타입과 반환타입이 일치할 때는 Function 대신 UnaryOperator를 사용한다. (매개 변수 2개면 BinaryOperator)

 

### 매개변수의 타입과 반환타입이 일치하는 함수형 인터페이스

| 함수형 인터페이스   | 메서드                    | 설명                                                         |
| ------------------- | ------------------------- | ------------------------------------------------------------ |
| `UnaryOperator<T>`  | T -> T apply(T t) -> T    | Function의 자손. Function과 달리 매개변수와 결과의 타입이 같다. |
| `BinaryOperator<T>` | T, T -> T apply(T t, T t) | BiFunction의 자손. BiFunction과 달리 매개변수와 결과의 타입이 같다. |



## **컬렉션 프레임워크와 함수형 인터페이스**

컬렉션 프레임워크의 인터페이스에 다수의 디폴트 메서드가 추가됐는데, 그 중의 일부는 함수형 인터페이스를 사용한다.



| 인터페이스 | 메서드                                           | 설명                             |
| ---------- | ------------------------------------------------ | -------------------------------- |
| Collection | boolean removeIf(Predicate<E> filter)            | 조건에 맞는 요소를 삭제          |
| List       | void replaceAll(UnaryOperator<E> operator)       | 모든 요소를 변환하여 대체        |
| iterable   | void forEach(Consumer<T> action)                 | 모든 요소에 작업 action을 수행   |
| Map        | V compute(K key, BiFunction<K, V, V> f)          | 지정된 키의 값에 작업 f를 수행   |
|            | V computeIfAbsent(K key, Function<K, V> f)       | 키가 없으면, 작업 f 수행 후 추가 |
|            | V computeIfPresent(K key, BiFunction<K, V, V> f) | 지정된 키가 있을 때, 작업 f 수행 |
|            | V merge(K key, V value, BiFunction<V, V, V> f)   | 모든 요소에 병합작업 f를 수행    |
|            | void forEach(BiConsumer<K, V> action)            | 모든 요소에 작업 action을 수행   |
|            | void replaceAll(BiFunction<K, V, V> f)           | 모든 요소에 치환작업 f를 수행    |

* 단순화하기 위해 와일드 카드는 생략했다



## 기본형(Primitive)을 사용하는 함수형 인터페이스 - Primitive Functional Interface

Wrapper 클래스나 제네릭 대신 primitive만 처리할 수 있는 인터페이스를 제공한다. 

| 함수형 인터페이스     | 메서드                                    | 설명                                               |
| --------------------- | ----------------------------------------- | -------------------------------------------------- |
| `DoubleToIntFunction` | double -> int applyAsInt(double d) -> int | AToBFunction은 입력이 A타입 출력이 B타입           |
| `ToIntFunction<T>`    | T -> int applyAsInt(T value) -> int       | ToBFunction은 출력이 B타입이다. 입력은 제네릭 타입 |
| `IntFunction<R>`      | int -> R apply(int value) -> R            | AFunction은 입력이 A타입이고 출력은 제네릭 타입    |
| `ObjIntConsumer<T>`   | T, int -> void accept(T t, int i)         | ObjAFunction은 입력이 T, int 타입이고 출력은 없다  |



# Function의 합성과 Predicate의 결합

java.util.function패키지의 함수형 인터페이스에는 추상메서드 외에도
디폴트 메서드와 static메서드가 정의되어 있다.

> 원래 Function인터페이스는 반드시 두 개의 타입을 지정해 줘야 하기 때문에, 두 타입이 같아도 Function< T>라고 쓸 수 없다.
> Function<T,T>라고 써야 한다.





## Function의 합성

> 두 람다식을 합성해서 새로운 람다식을 만들 수 있다.

함수 f, g가 있을 때

* `f.andThen(g)는 함수 f를 먼저 적용하고 g 적용.`

* `f.compose(g)는 함수 g를 먼저 적용하고 f 적용.`



### Function타입의 두 람다식을 하나로 합성 - andThen()

```java
Function<String, Integer> f = (s) -> Integer.parseInt(s, 16);	//s를 16진 정수로 변환
Function<Integer, String> g = (i) -> Integer.toBinaryString(i);	//2진 문자열로 변환

Function<String, String> h = f.andThen(g);						// 새로운 함수. f + g -> h
```

* 문자열을 숫자로 변환하는 함수 f
* 숫자를 2진 문자열로 반환하는 함수 g

이 함수 f와 g를 andThen()을 이용해서 합성할 수 있다. 

```java
System.out.println(h.apply("FF")); // "FF" -> 255 -> "1111111"
```



###  Function타입의 두 람다식을 하나로 합성 - compose()

두 함수를 반대의 순서로 합성

```java
Function<Integer,String> g = (i) -> Integer.toBinaryString(i);
Function<String,Integer> f = (s) -> Integer.parseInt(s,16);
Function<Integer, Integer> h = f.compose(g);
```

```java
System.out.println(h.apply(2)); // 2->"10"->16
```



### identify()는 함수를 적용하기 이전과 이후가 동일한 '항등 함수'가 필요할 때 사용

이 함수를 람다식으로 표현하면 'x->x'이다. 아래의 두 코드는 동등하다.

> 항등 함수는 함수에 x를 대입하면 결과가 x인 함수를 말한다. f(x)=x

```java
Function<String, String> f = x -> x;
// Function<String, String> f = Function.identify(); // 위 문장과 동일하다.

System.out.println(f.apply("AAA")); // AAA가 그대로 출력된다.
```



## Predicate의 결합

여러 Predicate를 and(), or(), negate()로 연결해서 하나의 새로운 Predicate로 결합할 수 있다. 

Predicate의 끝에 negate()를 붙이면 조건식 전체가 부정이 된다.

```java
Predicate<Integer> p = i -> i < 100;
Predicate<Integer> q = i -> i < 200;
Predicate<Integer> r = i -> i%2 == 0;
Predicate<Integer> notP = p.negate();

// 100 <= i && (i < 200 || i % 2 == 0)
Predicate<Integer> all = notP.and(q.or(r));
System.out.println(all.test(150));  // true
```



static메서드인 isEqual()은 두 대상을 비교하는 Predicate를 만들 때 사용한다.
먼저, isEqual()의 매개변수로 비교대상을 하나 지정하고, 또 다른 비교대상은 test()의 매개변수로 지정한다.

```java
Predicate<String> p = Predicate.isEqual(str1);		//isEquals()은 static메서드
Boolean result = p.test(str2);						//str1과 str2가 같은지 비교한 결과를 반환

// 위 두 메서드를 합친 결과
boolean result = Predicate.isEqual(str1).test(str2);
```



## 메서드 참조(Method reference)

람다식이 하나의 메서드만 호출하는 경우, 메서드 참조를 통해 람다식을 간략히 할 수 있다.

* 클래스명::메서드명 
* 참조변수::메서드명



```java
// 기존
Function<String, Integer> f = (String s) -> Integer.parseInt(s);

// 메서드 참조
Funcation<String, Integer> f = Integer::parseInt;
```



### 생성자의 메서드 참조

생성자를 호출하는 람다식도 메서드 참조로 변환할 수 있다.

```java
Supplier<MyClass> s = () -> new MyClass(); // 람다식
Supplier<MyClass> s = MyClass::new; // 메서드 참조
```

매개변수가 있는 생성자라면, 매개변수의 개수에 따라서 알맞은 함수형 인터페이스를 사용하면 된다.
필요하다면 함수형 인터페이스를 새로 정의해야 한다.

```java
Function<Integer. MyClass> f = (i) -> new MyClass(i); // 람다식
Function<Integer. MyClass> f2 = MyClass::new; // 메서드 참조

BiFunction<Integer, String, MyClass> bf = (i, s) -> new MyClass(i, s);
BiFunction<Integer, String, MyClass> bf2 = MyClass::new; // 메서드 참조
```



### 배열 생성할 경우

```java
Function<Integer, int[]> f = x -> new int[x]; // 람다식
Function<Integer, int[]> f2 = int[]::new; // 메서드 참조
```



| 종류                          | 람다식                     | 메서드 참조       |
| ----------------------------- | -------------------------- | ----------------- |
| static 메서드 참조            | (x) -> ClassName.method(x) | ClassName::method |
| 인스턴스메서드 참조           | (obj, x) -> obj.method(x)  | ClassName::method |
| 특정 객체 인스턴스메서드 참조 | (x) -> obj.method(x)       | obj::method       |