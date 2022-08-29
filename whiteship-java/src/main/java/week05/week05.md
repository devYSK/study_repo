# 목표

자바의 Class에 대해 학습하세요.

# 학습할 것 (필수)

- 클래스 정의하는 방법
- 객체 만드는 방법 (new 키워드 이해하기)
- 메소드 정의하는 방법
- 생성자 정의하는 방법
- this 키워드 이해하기

# 마감일시

2020년 12월 19일 토요일 오후 1시까지.

# 과제 (Optional)

- int 값을 가지고 있는 이진 트리를 나타내는 Node 라는 클래스를 정의하세요.
- int value, Node left, right를 가지고 있어야 합니다.
- BinrayTree라는 클래스를 정의하고 주어진 노드를 기준으로 출력하는 bfs(Node node)와 dfs(Node node) 메소드를 구현하세요.
- DFS는 왼쪽, 루트, 오른쪽 순으로 순회하세요.



# Class 정의하는 법



클래스란?

> 객체(Object)를 생성하기 위한 틀로, 객체를 생성하기 위해 사용된다. 

클래스는 클래스 그 자체로 사용할 수 없고 인스턴스가 되어야 사용할 수 있다.

인스턴스가 된다는 것은 객체가 된다고 할 수 있다.

> 자바에서는 new 키워드를 이용해 객체를 생성한다.



클래스를 정의하는 법은 `class` 키워드를 이용하여 정의할 수 있다.

### 작성 규칙

* 하나 이상의 문자로 이루어져야 한다.
* 첫 번째 글자는 숫자가 올 수 없다
* $, _ 이외의 특수 문자는 사용할 수 없다
* 자바 키워드는 사용할 수 없다. (int ,char, long, void,  등)

* 일반적으로 자바에서는 `pascal case`를 이용하여 클래스명을 작성한다

# 이너 클래스, 외부 클래스, 스태틱 이너클래스 정의

### 클래스의 요소

* 필드(field) : 멤버 변수라고도 한다. 해당 클래스의 속성을 나타내며, 필드를 초기화 하는것을 필드 초기화 또는 명시적 초기화 라고 한다. 
  * 인스턴스 변수 :  인스턴스가 갖는 변수. 인스턴스를 생성할 때 만들어진다. heap 영역에 할당되고 gc에 의해 관리된다.
  * 클래스 변수(static) : static 키워드가 인스턴스 변수 앞에 붙은 변수. 
    * 해당 클래스에서 파생되어 만들어진 인스턴스들은 이 변수를 공유한다. 
    * static 영역에 할당되고, gc의 관리를 받지 않는다. public static 키워드가 붙으면 전역 변수이다.
  
  * 지역변수 : 메서드 내에서 선언하여 사용하는 변수. 
    * 메서드 내에서 사용되고 메서드가 종료되면 gc에 의해 사라진다.
    * 스택 메모리에 존재.
  
  * 매개변수 : 메서드 내에 파라미터로 넘어온 변수. 
    * 스택 메모리에 존재한다.  
  
* 메서드(method) : 객체의 행동을 나타낸다.
  * 인스턴스 메서드 : 인스턴스 변수(객체)와 연관된 작업을 하는 메서드. 인스턴스를 생성해서 인스턴스를 통해야만 사용할 수 있다.
  * 클래스 메서드(static) : static 키워드가 메서드 앞에 붙음. 정적 메서드 라고도 한다. public 으로 선언하면 클래스명.메서드명으로 어디에서든지 접근할 수 있다.
  
* 생성자(constructor) : 객체가 생성된 후에 클래스의 객체를 초기화하는데 사용, 리턴타입이 없고 필드 초기화 등을 수행한다.
* 초기화 블록(initializer) : 명시적 초기화에서 불가능한 초기화를 수행할 수 있다.
  * 클래스 초기화 블록 : 클래스 변수 초기화에 사용된다.
  * 인스턴스 초기화 블록 : 인스턴스 변수 초기화에 사용된다
  

<details>
<summary>예제 자바 코드 - 접기/펼치기 </summary>



```java
public class Foo {
    private int age;            // 인스턴스 변수
    private String name;        // 인스턴스 변수

    private String foobar;      // 인스턴스 변수

    private static String bar;  // 클래스변수
    
    public static final String FINAL_BAR = "finalBar";
    
    static {                    // 클래스 초기화 블록
        bar = "bar";
    }

    {                           // 인스턴스 초기화 블록
        foobar = "foobar";
    }
    
    public Foo() {              // 매개변수 없는 기본 생성자
    }

    public Foo(int age) {       // 매개변수 있는 생성자.
        this.age = age;
    }
    
    public void printAge() {    // 인스턴스 메서드
        System.out.println("age : " + age);
    }
    public static void printBar() { //클래스 메서드
        System.out.println("bar : " + bar);
    }

    public static void main(String[] args) {
        Foo.bar = "adlfasdlf"; // 접근가능해서 수정 가능.
        Foo.FINAL_BAR = "ㅁㅇㄹㅁㅇㄹ;"; // 컴파일 에러. final이라 수정 불가능 
        
        Foo foo = new Foo();// 매개변수 없는 기본생성자. printAge() 호출하면 age가 초기화 안되어서 NullPointerException
        foo.printAge(); // 에러. age가 초기화 안됨
        Foo foo2 = new Foo(18); // 매개변수 있는 생성자. age가 초기화 되어서 printAge() 호출해도 에러 안남
        foo2.printAge(); // 에러 아님. 생성자로 age를 초기화함.
    }
}
```

</details>

* static 변수를 final로 설정하지 않으면 어디에서든 접근 가능하므로, final로 선언하여 선언과 동시에 초기화하고 수정하지 못하게 하는것이 일반적. 

> 접근 제어자 - `public`, `protected`, `default`, `private`
> 그 외 - `static`, `final`, `abstract`, `transient`, `synchronized`, `volatile` etc.

`static`이나 `public`같은 키워드를 제어자(modifier)라고 하며, 클래스나 멤버 선언 시 부가적인 의미를 부여한다.



### 접근제어자

- 접근 제어자 - 접근 제어자는 해당 클래스 또는 멤버를 정해진 범위에서만 접근할 수 있도록 통제하는 역할을 한다.

-  클래스는 `public`과 `default`밖에 쓸 수 없다. 범위는 다음과 같다.

  - 생성자(Constructor)는 private, protected 키워드가 사용 가능하다.  
  - 참고로 `default`는 아무것도 덧붙이지 않았을 때를 의미한다.

- 

  ![click to enlarge](https://jeeneee.dev/static/f696bef2cc0494a8ba68c72411b52879/c1b63/access-modifier.png)

- `static` - 변수, 메서드는 객체(인스턴스)가 아닌 클래스에 속한다.

- `final`

  - 클래스 앞에 붙으면 해당 클래스는 상속될 수 없다.
  - 변수 또는 메서드 앞에 붙으면 수정되거나 오버라이딩 될 수 없다.

- `abstract`

  - 클래스 앞에 붙으면 추상 클래스가 되어 객체 생성이 불가하고, 접근을 위해선 상속받아야 한다.
  - 변수 앞에 지정할 수 없다. 
  - 메서드에는 지정이 가능하다.
    - 메서드 앞에 붙는 경우는 오직 추상 클래스 내에서의 메서드밖에 없으며 해당 메서드는 선언부만 존재하고 구현부는 상속한 클래스 내 메서드에 의해 구현되어야 한다. 

- `transient` - 변수 또는 메서드가 포함된 객체를 직렬화할 때 해당 내용은 무시된다.

- `synchronized` - 메서드는 한 번에 하나의 쓰레드에 의해서만 접근 가능하다.

- `volatile` - 해당 변수의 조작에 CPU 캐시가 쓰이지 않고 항상 메인 메모리로부터 읽힌다.

## 중첩 클래스(Nested Class)

중첩 클래스는 말 그대로 다른 클래스의 내부에 존재하는 클래스를 의미한다.

중첩 클래스는 특정 클래스가 한 곳(다른 클래스)에서만 사용될 때 논리적으로 군집화하기 위해 사용한다.

이로 인해 불필요한 노출을 줄이면서 캡슐화를 할 수 있고 조금 더 읽기 쉽고 유지 보수하기 좋은 코드를 작성하게 된다.

* ![img](https://t1.daumcdn.net/cfile/tistory/99DA873B5AC20B7918)

```java
public class OuterClass { // 일반 클래스, 외부 클래스.
  
  class NonStaticInnerClass { // 비정적 내부 클래스
    
  }
  
  static class StaticInnerClass {	// 정적 내부 클래스 
    
  }
}
```



* 중첩 클래스는 왜 쓰는가?
  * 클래스들의 논리적인 그룹을 나타낼 때 쓴다. 주로 model 객체에서 상위 모델과 하위 모델이 있을 때 쓴다고 한다.
  * 캡슐화가 향상
  * 좋은 가독성 및 유지보수성

```
내부 클래스는 비 정적 내부 클래스와 정적 내부 클래스로 나뉜다 
```

## 비 정적 내부 클래스 (Non-static inner class)

* 일반 클래스 내부에 생성된다.
* 외부에 있는 클래스의 자원을 직접 사용할 수 있다.
* 외부 클래스가 내부 클래스를 멤버 변수처럼 사용할 수 있다. 사용하려면 new로 인스턴스를 생성해야 한다.

* 독립적으로 생성할 수 없고 반드시 외부 클래스 객체를 생성한 뒤 생성된 객체를 이용해서 만들어야 한다

```java
void test() {
  OuterClass.NonStaticInnerClass = new OuterClass().new InnerClass();
}
```

* 비정적 내부 클래스는 바깥 클래스에 대한 참조가 필요하다.



## 정적 내부 클래스 (Static inner class)

* 내부 클래스와 비슷하나, static으로 선언한다.
* 외부에 있는 클래스의 변수와 메소드 중에 static이 붙은 것들은 사용할 수 없다.
* static 예약어가 있어서 독립적으로 생성할 수 있다.

```java
void test() {
  OuterClass.StaticInnerClass sic = new StaticInnerClass();  
}
```



# *`비 정적 내부 클래스` 주의할점*



### 메모리 누수 가능성

비정적 내부 클래스의 경우 바깥 클래스에 대한 참조를 가지고 있기 때문에 메모리 누수가 발생할 여지가 있다. 

바깥 클래스는 더 이상 사용되지 않지만 **내부 클래스의 참조로 인해 GC가 수거하지 못해서 바깥 클래스의 메모리 해제를 하지 못하는 경우가 발생**할 수 있다.

* -> 메모리 릭으로 이어질 수 있다. 매우 위험함. 

#### 정적 내부 클래스

정적 내부 클래스의 경우 바깥 클래스에 대한 참조 값을 가지고 있지 않기 때문에 메모리 누수가 발생하지 않는다. 

* 그래도 바깥 클래스에 대한 참조 값을 가지지 않게 주의해야 한다.



만약 **내부 클래스가 독립적으로 사용된다면 정적 클래스로 선언**하여 사용하는 것이 좋다. 

바깥 클래스에 대한 참조를 가지지 않아 메모리 누수가 발생하지 않기 때문이다.

**비정적 클래스를 어댑터 패턴을 이용하여 바깥 클래스를 다른 클래스로 제공**할 때 사용하면 좋다. 

이러한 케이스로 `HashMap`의 `keySet()` 이 있다.

 `keySet()` 을 사용하면 `Map`의 key에 해당하는 값들을 `Set`으로 반환해 주는 데 어댑터 패턴을 이용해서 `Map`을 `Set`으로 제공한다.



* 비정적 내부 클래스는 메모리 누수를 반드시 고려해야 한다 .



#  지역 클래스, 익명 클래스 정의



## 지역 클래스 (Method Local Inner Class)

* 메소드 내부에 클래스를 정의한 경우. 메소드 내의 지역변수처럼 쓰인다.
* 메소드 내부에서 new 한뒤 사용해야 한다. 메소드 밖에서는 사용할 수 없다. 

```java
class OuterClass {
	void test() {
  	class MethodLocalInnerClass {
    	...
  	}
	}
}
```



## 익명 클래스 (Anonymous Inner Class)

* 익명 클래스는 인스턴스 이름이 없다.
* 프로그램으로 일시적으로 한번만 사용되고 버려짐. 
* new 와 동시에 부모 클래스를 상속받아 내부에서 오버라이딩 해서 사용한다.
* 매개변수로 사용 가능
* 주로 상속은 받아야하지만, 한번만 사용할 경우 사용
* 익명클래스 내부에 생성자는 없다
* 익명클래스 외부의 자원은 final이 붙은 자원만 사용 가능 
* 클래스는 이미 선언 되어야 한다.
* 익명 자식 객체는 다음과 같이 3가지 방법으로 구현할 수 있다.
  * 1. 필드의 초기값
  * 2. 로컬변수의 초기값
  * 3. 매개변수의 매개값

<details>
<summary>자바 코드 - 접기/펼치기 </summary>



```java
public class AnonymousClass {
  void print() {
    System.out.println("블라블라");
  }
}

public class CallClass {

    private AnonymousClass anony1 = new AnonymousClass() {
        public void print2() {
            System.out.println("필드에요 저는");
        }
    };

    public void call() {
        AnonymousClass anony2 = new AnonymousClass() {

            public void print3() {  // 호출 안됌 
                System.out.println("저는 메서드 안에 있어요, print3()");
            }

//            @Override             // 오버라이딩 해서 사용 가능
//            void print() {
//                super.print();
//            }
        };

        anony2.print();
        
    }

    public void call2(AnonymousClass anony3) {
        anony3.print();
    }

    public AnonymousClass getAnony1() {
        return anony1;
    }

    public static void main(String[] args) {
        CallClass callClass = new CallClass();
        
        callClass.getAnony1().print();  // print2() 메서드는 접근 불가

        callClass.call();   // 원래 클래스의 print()만 호출된다. 

        callClass.call2(new AnonymousClass(){
            @Override
            void print() {
                //super.print(); // 원래 클래스의 메서드도 호출 가능.
                System.out.println("오버라이딩 했지롱.");
            }
        });

    }

}

```

</details>



## 객체 만드는 방법 (new 키워드 이해하기)



클래스로부터 객체를 만드는 것을 인스턴스화 한다고 하며,

어떤 클래스로 부터 만들어진 객체를 인스턴스 라고 한다.

`new` 키워드를 이용하여 객체 생성.

클래스를 이용하여 인스턴스(객체)를 생성한다.

```java
Class 인스턴스명 = new Class();
```

* new 키워드는 객체(인스턴스)를 만드는데 사용
* 생성자 () 를 이용하여 객체를 생성한다. 
* 런타임에 힙 메모리 영역에 할당
* 모든 객체는 힙 영역에서 메모리를 차지한다. 
* 힙 메모리에 `생성된 주소`를 참조 타입 변수인 객체에 저장하고 객체를 이용하여 클래스에 정의한 메서드를 호출한다.  

* toString()을 오버라이딩 안한 객체를 System.out.pritln으로 콘솔에 찍어보면 주솟값이 나온다. 

```java
public class Car {
  private int speed;
  private String name;
  
  public Car() {
    speed = 0;
  }
  
  public Car(int speed, String name) {
    speed = speed;
    name = name;
  }
}
```

* public Car() 는 매개 변수 없는 생성자이며 기본 생성자라고 한다.
  * new Car() 해서 객체 생성
* public Car(int speed, String name) 은 매개변수 있는 생성자라고 한다
  * new Car(10, "bmw") 해서 객체 생성
* 기본 생성자를 선언하지 않고, 매개변수 있는 생성자를 선언시에는 기본생성자를 통한 객체를 생성할 수 없다. 

## 메서드 정의하는 방법

* c, c++, javascript에서 함수(function)를 정의한 것을 클래스 내부에 작성한걸 메서드 라고 한다. 
* 자바에서는 클래스 내에서만 메서드를 정의할 수 있다.

* ![click to enlarge](https://jeeneee.dev/static/78df1fd61b3bd0e6ca321ac491b59b2c/c1b63/method-declaration.png)

* [접근제어자](##접근제어자) : 해당 메소드에 접근할 수 있는 범위를 명시한다. 
* 반환 타입 : 프리미티브 타입 및 void, 그리고 클래스 타입을 반환할 수 있다.
  * return 문으로 반환한다. 
* 메서드 이름: 메서드 이름은 동사이며, lowerCamel 케이스로 작성하는 것이 관례이다.
* 매개변수 리스트 : 매개변수가 오지 않을 수도 있으며, 프리미티브 타입, 클래스 타입등 다양한 매개변수가 올 수 있다.



### 메서드 오버로딩(Method Overloading)

파라미터의 갯수나 타입이 다르면 `동일한 반환타입`과 `동일한 이름의` 메서드를

`재정의` 하는것을 메서드 오버로딩 이라고 한다

```java
public void print(); 
public void print(String name);
// 동일한 반환타입과 동일한 이름의 메서드를 오버로딩

System.out.println(1234);
System.out.println("name");
// 대표적인 메서드 오버로딩 
```

### 메서드 오버라이딩(Method Overriding)

상위 클래스가 정의한 메서드를 상속받은 하위 클래스가 메서드를 변경하거나 확장하는 방법.

* 메서드 위에 명시적으로 `@Override` 어노테이션을 붙여야 한다.
* 오버라이딩을 해도 되고 안해도 된다. 개발자의 선택이다  

* 상위클래스의 메서드를 호출하려면 super.메서드명()으로 접근

````java
class Car {
  private String brand;
  
  public Car() {
    this.brand = default;
  }
  
  public void print() {
    System.out.println(brand);
  }
}

class BMW extends Car {
  
  @Override
  public void print() {
    System.out.println("난 비엠더블유 ")
  }
  
}
````





## 생성자 정의하는 방법 

객체를 초기화할 때 사용하는것이 생성자.

기본 생성자, 묵시적 생성자, 명시적 생성자가 있다.

* 기본 생성자 : 클래스 내부에 선언된 다른 생성자가 없는 경우 컴파일러가 자동으로 생성해준다
* 묵시적 생성자: 파라미터 값을 가지지 않는 생성자
* 명시적 생성자 : 파라미터 값을 가지는 생성자

```java
class Car {
  
  public Car() {
 		// 기본 생성자, 묵시적 생성자    
  }
  
  public Car(String name) { // 명시적 생성자 
    System.out.println(name + " 생성");
  }
  
}
```

- 생성자는 **리턴 타입을 가지지 않는다**.
- 생성자는 **클래스 이름과 동일**하다.
- 모든 클래스는 **생성자가 반드시 존재**하고, **한개 이상**의 생성자를 가진다.
- 클래스 내부에 생성자를 선언하지 않으면 컴파일러가 기본 생성자를 선언해 사용한다.
- **명시적 생성자만 선언되있는 경우** **파라미터가 없는 생성자를 사용하고 싶다면 묵시적 생성자를 선언**해주어야한다. (생성자가 클래스 내부에 선언되어 있기 때문에 **기본 생성자가 생성되지 않는다**.)

## this 키워드 이해하기

this 키워드는 클래스가 인스턴스화 되었을 때 자기 자신의 메모리 주소를 가진다.

자기 자신을 나타내는 키워드 (this)

this.필드, this.메서드()  등으로 클래스 내에서 접근할 수 있다.

* this 키워드로 메서드나 생성자에서 매개변수 이름이 같을 때 자기자신의 필드와 매개변수를 구분할 수 있다. 

* 괄호가 없다!

#### this()

this()는 자기 자신의 생성자를 호출할 때 사용.

this()는 호출 하는곳의 첫 번째 줄에서 호출되어야 한다.

그렇지 않으면 컴파일 오류가 난다. 



# 과제 (Optional)

- int 값을 가지고 있는 이진 트리를 나타내는 Node 라는 클래스를 정의하세요.
- int value, Node left, right를 가지고 있어야 합니다.
- BinrayTree라는 클래스를 정의하고 주어진 노드를 기준으로 출력하는 bfs(Node node)와 dfs(Node node) 메소드를 구현하세요.
- DFS는 왼쪽, 루트, 오른쪽 순으로 순회하세요.



```java
public class Node {

    private int value;

    private Node left;

    private Node right;

    public Node(int value) {
        this.value = value;
    }

    public void addLeftNode(Node leftNode) {
        this.left = leftNode;
    }

    public void addRightNode(Node rightNode) {
        this.right = rightNode;
    }

    public int getValue() {
        return value;
    }

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }
}

public class BinaryTree {

    private Node root;
    
    public BinaryTree() {
    }

    public BinaryTree(Node rootNode) {
        this.root = rootNode;
    }

    public void insert(int value) {
        Node newNode = new Node(value);

        if (this.root == null) {
            this.root = newNode;
            return;
        }

        Node currentNode = root;
        Node parent;
        while (true) {
            parent = currentNode;
            if (currentNode.getValue() > value) {
                currentNode = currentNode.getLeft();

                if (currentNode == null) {
                    parent.addLeftNode(newNode);
                    this.size += 1;
                    break;
                }

            } else {
                currentNode = currentNode.getRight();

                if (currentNode == null) {
                    parent.addRightNode(newNode);
                    this.size += 1;
                    break;
                }

            }

        }

    }

    public boolean find(int value) {

        Node currentNode = root;

        while (currentNode != null) {
            if (currentNode.getValue() == value) {
                return true;
            } else if (currentNode.getValue() < value) {
                currentNode = currentNode.getLeft();
            } else {
                currentNode = currentNode.getRight();
            }
        }

        return false;
    }

    private List<Integer> preOrder(Node node, List<Integer> order) {
        if (node != null) {
            order.add(node.getValue());
            preOrder(node.getLeft(), order);
            preOrder(node.getRight(), order);
        }

        return order;
    }

    private List<Integer> inOrder(Node node, List<Integer> order) {
        if (node != null) {
            inOrder(node.getLeft(), order);
            order.add(node.getValue());
            inOrder(node.getRight(), order);
        }
        return order;
    }

    private List<Integer> postOrder(Node node, List<Integer> order) {
        if (node != null) {
            postOrder(node.getLeft(), order);
            postOrder(node.getRight(), order);
            order.add(node.getValue());
        }

        return order;
    }


    public List<Integer> dfs(Node node) { // 전위탐색 = 깊이우선탐색

        if (root == null) {
            return null;
        }

        List<Integer> order = new ArrayList<>();

        Stack<Node> stack = new Stack<>();

        Node current = root;
        stack.push(node);

        while (!stack.isEmpty()) {

            while (current.getLeft() != null) {
                if (current.getLeft() != null) {
                    current = current.getLeft();
                    stack.push(current);
                }
            }
            current = stack.pop();
            order.add(current.getValue());

            if (current.getRight() != null) {
                current = current.getRight();
                stack.push(current);
            }
        }


        return order;
    }

    public List<Integer> dfsRecursive(Node node, List<Integer> order) {
        if (node == null) return null;

        if (node.getLeft() != null) {
            dfsRecursive(node.getLeft(), order);
        }

        order.add(node.getValue());

        if (node.getRight() != null) {
            dfsRecursive(node.getRight(), order);
        }
        return order;
    }


    public List<Integer> bfs(Node node) {
        if (node == null) {
            return null;
        }

        List<Integer> order = new ArrayList<>();
        Queue<Node> queue = new LinkedList<>();
        queue.add(node);

        while (!queue.isEmpty()) {
            Node poll = queue.poll();
            order.add(poll.getValue());

            if (poll.getLeft() != null) {
                queue.offer(poll.getLeft());
            }

            if (poll.getRight() != null) {
                queue.offer(poll.getRight());
            }

        }

        return order;
    }


    public Node getRoot() {
        return root;
    }

    public static void main(String[] args) {
        BinaryTree binaryTree = new BinaryTree();

        binaryTree.insert(7);
        binaryTree.insert(5);
        binaryTree.insert(9);
        binaryTree.insert(3);
        binaryTree.insert(6);
        binaryTree.insert(8);
        binaryTree.insert(10);


//        System.out.println(binaryTree.inOrder(binaryTree.getRoot(), new ArrayList<>()));
//        System.out.println(binaryTree.bfs(binaryTree.getRoot()));

        System.out.println(binaryTree.dfs(binaryTree.getRoot()));
        System.out.println(binaryTree.dfsRecursive(binaryTree.getRoot(), new ArrayList<>()));
    }
}
```

