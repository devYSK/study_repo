# 목표
자바의 인터페이스에 대해 학습하세요.

# 학습할 것 (필수)
* 인터페이스 정의하는 방법
* 인터페이스 구현하는 방법
* 인터페이스 레퍼런스를 통해 구현체를 사용하는 방법
* 인터페이스 상속
* 인터페이스의 기본 메소드 (Default Method), 자바 8
* 인터페이스의 static 메소드, 자바 8
* 인터페이스의 private 메소드, 자바 9


# 인터페이스와 추상클래스의 차이점
* 추상(abstract) 클래스와 인터페이스(interface) 공통점과 차이점

* 추상(abstract) : ->사물이 지니고 있는 여러 가지 측면 가운데서 특정한 측면만을 가려내어 포착하는 것


객체의 개념을 이용하여 객체들을 여러 그룹으로 분류함으로써 얻는 장점이 무엇이 있을까?
 

1. 객체 간의 차이점은 무시하고 객체들 간의 공통점을 파악하기 쉬움 

2. 객체의 불필요한 세부사항을 제거함으로써 중요한 부분을 강조할 수 있음


* 공통점
  * 자기 자신을 객체화할 수 없으며 다른 객체가 상속(extends) , 구현(implements)을 하여 객체를 생성할 수 있다.
  * 상속(extends) , 구현(implements)을 한 하위 클래스에서는 상위에서 정의한 추상 메서드(abstract method)를 반드시 구현하여야 한다.

* 차이점

|추상 클래스(abstract class)| 인터페이스(interface)|
|---|---|
|1. 일반 메소드 포함가능	|1. 모든 메서드는 추상메서드 주의) 자바 8 이후 부터 default ,static  메서드 추가가능
|2. 다중상속 불가능	| 2. 다중상속 가능|
|3. 상수, 변수 필드 포함가능	|3. 상수필드만 포함가능|
 


> 그럼 추상클래스만 쓰지 왜 인터페이스가 존재할까?

* 추상 클래스는 `IS - A` ~는 ~이다 
    * youngsoo는 사람이다.

* 인터페이스는 `HAS - A` ~는 ~ 를 할 수 있다. 
    * youngsoo는 수영을 할 수 있다.


 
즉 Person은 수영을 `할 수도 있고`,   
`할 수 없을 수도 있기 때문에` Person이라는 개념에 속하는 것이 어색하다.


## 인터페이스의 장점

1. 개발 기간을 단축 시킬 수 있다.     
    * 인터페이스가 작성되면 이를 사용해서 프로그램을 작성하는 것이 가능하다.
    * 메서드를 호출하는 쪽에서는 선언부만 알면 되기 때문이다.
    
2. 클래스간 결합도를 낮출 수 있다.
    
    코드의 종속성을 줄이고 유지보수성을 높이도록 해준다.
    
3. 표준화가 가능하다.
    
    클래스의 기본틀을 제공하여 개발자들에게 정형화된 개발을 강요할 수 있다.
    
    **→ 자바의 다형성을 극대화 하여 코드의 수정을 줄이고 유지보수성을 높인다.**
<br>

# 인터페이스 정의하는 방법

인터페이스란 객체와 객체 사이에서 일어나는 상호 작용의 매개로 쓰인다.

- 서로 이어주는 다리 역할과 프로젝트의 설계도로 생각할 수 있다.

모든 기능을 추상화로 정의한 상태로 `선언`만 한다. 
* -> 인터페이스 내에서 구현은 하지 않는다. 

인터페이스의 선언은 예약어로 class 대신 "**interface**" 키워드를 사용하며, 

접근 제어자로는 **public** 또는 **default**를 사용한다.

 

1. 추상 메서드와 상수로 구성됨 (자바 8 이후부터 default , static  메서드 추가 가능)

 
2.  모든 메서드 접근 지정자는 public이며 생략이 가능

 

3. 상수는 public static fianl 속성이며 생략이 가능





# 인터페이스 구현하는 방법

인터페이스는 구현한다는 의미 키워드 `implements`를 사용한다  
   
클래스가 인터페이스를 상속받아 인터페이스를 구현한다.

* 인터페이스 선언
```java
public interface Person {
    void move();
}
```

* 인터페이스 구현
```java
public class Youngsoo implements Person {
    @Override
    public void move() {
        System.out.println("영수가 움직인다");
    }
}
```

* 이렇게 오버라이딩 할 때는 부모의 메소드보다 넓은 범위의 접근 제어자를 지정해야한다.

* 만일 구현하는 인터페이스의 메서드 중 일부만 구현한다면, abstract를 붙여서 추상 클래스로 선언해야 한다.

```java
public abstact class Developer implements Person {
    public void move();
}
```


## 익명 구현 

* 구현 클래스를 만들어 사용하는 것이 일반적이고, 클래스를 재사용할 수 있기 떄문에 편리하지만, **일회성의 구현 객체를 만들기 위해** 소스파일을 만들고 클래스를 선언하는 것은 비효율적이다.

* 자바는 소스 파일을 만들지 않고도 구현 객체를 만들 수 있는 방법을 제공하며 이것이 **"익명 구현 객체"** 이다.

```java
인터페이스 변수 = new 인터페이스(){
	// 인터페이스에 선언된 추상 메소드의 실체 메소드 구현
}
```


# 인터페이스 레퍼런스를 통해 구현체를 사용하는 방법

다형성은 자손클래스의 인스턴스를 부모타입의 참조변수로 참조하는 것이 가능하다

인터페이스도 이를 구현한 클래스의 부모라 할 수 있으므로 해당 인터페이스 타입의 참조변수로 클래스의 인스턴스를 참조할 수 있으며, 인터페이스 타입으로 형변환도 가능하다.

```java

interface Person {
    void move();
}

class Youngsoo implements Person {

    @Override
    public void move() {
        System.out.println("영수가 움직인다");
    }

    public void develop() {
        System.out.println("영수가 개발한다.");
    }
}

class Son implements Person {

    @Override
    public void move() {
        System.out.println("손씨가 움직인다");
    }

    public void clap() {
        System.out.println("손씨가 박수친다");
    }
}

public class Main {

    public static void main(String[] args) {
        Person youngsoo = new Youngsoo();

        Person son = new Son();


        youngsoo.move();
        son.move();


        youngsoo.develop(); // 불가능

        son.clap(); // 불가능

        ((Youngsoo)youngsoo).develop(); // 가능

        ((Son)son).clap(); // 가능

    }
}
```
* 인터페이스 타입은 구현체(Youngsoo나 Son)의 내부 구현을 모르니 Youngsoo나 Son의 메소드를 호출할 수 없다. 

* 다시 하위타입인 본래 타입으로 캐스팅해야만 사용 가능하다. 


> 즉, **인터페이스 레퍼런스는** 인터페이스를 **구현한 클래스의 인스턴스를 가리킬 수 있고**,  
해당 `인터페이스에 선언된 메소드`만 호출 할 수 있다.




# 인터페이스 상속
인터페이스는 클래스와 달리 다중 상속이 가능하다.

* 인터페이스의 메서드는 추상 메서드로 구현하기 전의 메서드이기 떄문에 어떤 인터페이스의 메서드를 상속받아도 같기 떄문이다.


자바의 ArrayList도 다중 인터페이스 구현이 되어있다
```java
public class ArrayList<E> extends AbstractList<E>
        implements List<E>, RandomAccess, Cloneable, java.io.Serializable
{
    ...
}
```

그러나, 상위 인터페이스에 있는 메서드 중에서 
*  메서드 명과 파라미터 형식은 같지만 리턴 타입이 다른 메서드가 있다면, 
* 둘중 어떤 것을 상속받느냐에 따라 규칙이 달라지기 때문에 다중 상속이 불가능하다.

또한, static 메소드는 override가 안된다.


## [강한 결합과 느슨한 결합](https://ahnyezi.github.io/java/javastudy-8-interface/)

<img src= "https://www.notion.so/image/https%3A%2F%2Fs3-us-west-2.amazonaws.com%2Fsecure.notion-static.com%2F453fc573-f059-4a6f-a3b1-a26735a35a3b%2FUntitled.png?table=block&id=63de3f96-5051-4268-aa74-43248afa7800&spaceId=af9c3f71-bb46-4a7f-933b-fd22501eabb5&width=2000&userId=65c6caee-498d-4e68-818e-228e54b6e55f&cache=v2">


왼쪽의 그림(강한 결합)부터 살펴보자.

* A는 B에 의존하고 있다. (A가 B를 사용)
* 이 때, A가 C를 사용하게 하려면?
* A는 B를 의존하고 있는 코드를 C를 의존하게끔 변경해야 한다. (강한 결합)


이번엔 오른쪽 그림(느슨한 결합)을 살펴보자.

* A는 I 인터페이스에 의존하고 있고, I 인터페이스를 구현한 B를 사용한다.
* 이 때, A가 C를 사용하게 하려면?
* A는 I에 의존하고 있기 때문에, I 인터페이스를 구현한 C를 사용한다면 따로 코드를 변경하지 않아도 된다. (느슨한 결합)


#### 강한 결합 : 빠르지만 변경에 불리
#### 느슨한 결합 : 느리지만 유연하고 변경에 유리


# 인터페이스의 기본 메소드 (Default Method), 자바 8


인터페이스는 기능에 대한 선언만 가능하기 때문에, 실제 코드를 구현한 로직은 포함될 수 없다.

* 하지만 자바8에서 이러한 룰을 깨트렸다
* 메소드 선언시에 default를 명시하게 되면 인터페이스 내부에서도 코드가 포함된 메소드를 선언할 수 있다.

 

* 접근제어자에서 사용하는 default와 같은 키워드이지만 다르다
    * 접근제어자는 아무것도 명시하지 않은 접근제어자를 default라 하며
    * 인터페이스의 default method는 'default'라는 키워드를 명시해야 한다.

- default method 는 해당 인터페이스를 구현한 **구현체가 모르게 추가된 기능임으로 그만큼 리스크가 따른다.**
    1. 컴파일 에러는 발생하지 않지만, 특정한 구현체의 로직에 따라 런타임 에러가 발생할 수 있다.
        * 사용하게 된다면, 구현체가 잘못사용하지 않도록 반드시 문서화 하자!
    
- Object가 제공하는 기능(equals, hashCode)와 같은 기본 메소드는 제공할 수 없다.  
  * 구현체가 재정의 하여 사용하는 것은 상관없다.
    
- 본인이 수정할 수 있는 인터페이스에만 기본 메소드를 제공할 수 있다.

- 인터페이스를 상속받은 인터페이스에서 다시 추상 메소드로 변경할 수 있다.

- 인터페이스 구현체가 default method를 재정의 할 수 있다.

## 왜 사용할까?
사실 인터페이스는 기능에 대한 구현보다는, 기능에 대한 '선언'에 초점을 맞추어서 사용 하는데, 디폴트 메소드는 왜 등장했을까?

> ...(중략)... 바로 "하위 호환성"때문이다.   
예를 들어 설명하자면, 여러분들이 만약 오픈 소스코드를 만들었다고 가정하자.  
그 오픈소스가 엄청 유명해져서 전 세계 사람들이 다 사용하고 있는데, 인터페이스에 새로운 메소드를 만들어야 하는 상황이 발생했다.   
자칫 잘못하면 내가 만든 오픈소스를 사용한 사람들은 전부 오류가 발생하고 수정을 해야 하는 일이 발생할 수도 있다.   
이럴 때 사용하는 것이 바로 default 메소드다. (자바의 신 2권)

기존에 존재하던 인터페이스를 이용하여서 구현된 클래스를 만들고 사용하고 있는데,

인터페이스를 보완하는 과정에서 추가적으로 구현해야 할, 혹은 필수적으로 존재해야 할 메소드가 있다면,

이미 이 인터페이스를 구현한 클래스와의 호환성이 떨어지게 된다.   
이러한 경우 default 메소드를 추가하게 된다면

하위 호환성은 유지되고 인터페이스의 보완을 진행 할 수 있다.
```java
interface MyInterface {
    default void printHello() {
        System.out.println("Hello World");
    }
}
//구현체 생성
class MyClass implements MyInterface {
}

public class DefaultMethod {
    public static void main(String[] args) {
        MyClass myClass = new MyClass();
        myClass.printHello(); 
    }
}
```

* 상속을 받았으므로, 구현은 해도되고 안해도 되는데, 구현을 안하더라도 컴파일 에러가 나지 않는다. 
* **인터페이스에서 구현되어 있으니까!**

# 인터페이스의 static 메소드, 자바 8

인스턴스 생성과 상관없이 인터페이스 타입으로 호출하는 메소드이다.

static 예약어를 사용하고, 접근제어자는 항상 public이며 생략 할 수 있다.

static method는 일반적으로 우리가 정의하는 메소드와는 다르다.

1. body가 있어야 한다.

2. implements 한 곳에서 override가 불가능하다.

- 해당 인터페이스를 구현한 모든 인스턴스, 해당 타입에 관련되어 있는 유틸리티, 헬퍼 메소드를 제공하고 싶다면? → static method로 제공할 수 있다.
- 인스턴스 없이 수행할 수 있는 작업을 정의할 수 있는 것이라 볼 수 있다.

```java
public interface ICalculator {
 
    int add(int x, int y);
    int sub(int x, int y);
 
    default int mul(int x, int y) {
 
        return x * y;
    }
 
    static void print(int value) {
 
        System.out.println(value);
    }
}
```
```java
public class Main {
 
    public static void main(String[] args) {
 
        ICalculator.print(100); 
        // interface의 static 메소드는 반드시 interface명.메소드 형식으로 호출
    }
}
```

* interface이름.메소드로 호출해야 한다.


# 인터페이스의 private 메소드, 자바 9
java9 에서는 추가적으로 private method 와 private static method 가 추가되었다.

### **왜?**

- java 8 의 default method와 static method는 여전히 불편하게 만든다.
- 단지 특정 기능을 처리하는 내부 method일 뿐인데도, **외부에 공개되는 public method로 만들어야 하기 떄문이다.**
- interface를 구현하는 다른 interface 혹은 class가 해당 method에 엑세스 하거나 상속할 수 있는것을 원하지 않지만 액세스 되버리는 이유 때문이다.

### **private 메소드의 네가지 규칙**

- private 메소드는 구현부를 가져야만 한다.
- 오직 인터페이스 내부에서만 사용할 수 있다.
- private static 메소드는 다른 static 또는 static이 아닌 메소드에서 사용할 수 있다.
- static이 아닌 private 메소드는 다른 private static 메소드에서 사용할 수 없다.

```java
public interface Car {
    void carMethod();

    default void defaultCarMethod() {
        System.out.println("Default Car Method");

        privateCarMethod();
        privateStaticCarMethod();
    }

    private void privateCarMethod() {
        System.out.println("private car method");
    }

    private static void privateStaticCarMethod() {
        System.out.println("private static car method");
    }
}

```