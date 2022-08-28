# 목표

자바의 상속에 대해 학습하세요.

# 학습할 것 (필수)

- 자바 상속의 특징
- super 키워드
- 메소드 오버라이딩
- 다이나믹 메소드 디스패치 (Dynamic Method Dispatch)
- 추상 클래스
- final 키워드
- Object 클래스







# 자바 상속의 특징

상속 : Inheritance

상속이란, 부모 클래스의 변수와 메소드를 자식 클래스가 상속 받는 것을 의미한다

상속을 사용하면, 코드를 재사용하기에 편리하고, 클래스간의 계층구조를 분리하며 관리하기 쉬워진다. 

상속 키워드 : extends

```java
public class Parent {}
public class Child extends Parent{}
```

extends 키워드를 사용하여 부모 클래스를 명시하고 상속받을 수 있다.



상속 받는 클래스 (ex: child)를 하위클래스, 자식클래스, 서브클래스 라고 부르며 모두 같은 의미이다.

상속 하는 부모 클래스 (ex: parent)를 상위클래스, 부모클래스, 슈퍼클래스 라고 부르며 모두 같은 의미이다 



## 상속의 특징

1. 자바에서는 단일 상속만 허용한다.
   * 다중 상속을 허용하지 않는다. extends 키워드로 `클래스`는 여러 부모를 가질 수 없다.
   * 하지만 인터페이스는 여러 인터페이스를 extends 키워드로 상속받을 수 있다. 
2. 상속하는 부모 클래스는 다시 부모클래스한테 상속받을 수 있으며, 궁극적으로 자바의 모든 최상위 클래스는 Object 클래스이다.
   * Multi-level inheritance 라고 한다.
3. 최상위 클래스인 Object를 제외한 모든 클래스들은 암묵적으로 Object의 서브클래스이며 Object 클래스를 상속받고 있다.

4. 부모 클래스의 생성자와 초기화 블럭은 자식 클래스에게 상속되지 않는다
5. 자식 클래스와 부모 클래스의 패키지가 다르면, 부모 클래스의 private 접근 제한을 갖는 필드와 메서드는 상속받지 않는다.
   * protected 제한을 갖는 필드와 메서드는 상속받는다.
6. 자식 클래스가 부모 클래스와 동일한 패키지에 있으면 부모 클래스의 *package-private* 멤버도 상속합니다. 
   * 필드에 액세스하기 위한 공개 또는 보호 메소드가 있는 경우 하위 클래스에서도 사용할 수 있습니다.
   * ex ) getter, setter 같은 메서드나 비슷한 의미를 갖는 메서드
   * 이건 5, 6 번 동일합니다.
7. 동일한 이름의 필드가 자식 클래스와 부모 클래스에 둘 다 존재할 경우, 자식 클래스의 변수로 오버라이딩 된다
8. 자식 클래스의 인스턴스를 생성하면, 부모 클래스(그위에 조상도)의 필드와 메서드가 합쳐진 하나의 인스턴스로 생성된다.

### 다중 상속의 문제점

다이아 몬드 형태로의 상속이 가능해지면,  상속받은 복수의 부모 클래스에 중복되는 필드나 메서드로 인해 모호해질 수 있다.

같은 클래스를 두 번 이상 상속 받을 수 있다.



# super 키워드

`super` 키워드는 자식 클래스에서 부모 클래스를 호출하는 키워드이다.

super 키워드를 통해, 부모 클래스의 필드나 메서드를 호출할 수 있다.

멤버 변수와, 지역 변수의 이름이 같을 때 `this` 키워드를 이용해서 구별했듯이, 상속 받은 멤버와 자신의 멤버를

`super` 키워드를 통해서 구별할 수 있다.



## super() 메서드

`super` 키워드와 비슷한 `super()` 메서드는, 부모 클래스의 생성자를 호출하는 메서드이다. 

객체를 생성하면, 그 클래스의 부모 클래스의 기본 생성자를 자동으로 호출한다.

* 상속의 특징 7 : 자식 클래스의 인스턴스를 생성하면, 부모 클래스(그위에 조상도)의 필드와 메서드가 합쳐진 하나의 인스턴스로 생성된다.
* super() 키워드를 생략해도, 컴파일러가 부모 클래스의 기본 생성자(no-arg constructor)를 호출해준다.
  * `그러나 부모 클래스에 기본 생성자가 없으면컴파일 에러가 발생한다 `
  * 생성자의 첫 줄에서 조상클래스의 생성자를 호출해야하는 이유는 **자손 클래스의 멤버가 조상 클래스의 멤버를 사용할 수도 있으므로 조상의 멤버들이 먼저 초기화되어 있어야 하기 때문이다.**
  * 부모, 조상 클래스 생성자의 호출은 Object 클래스까지 올라가며 호출되면서 반복된다.
  * 상속을 사용할 일이 있으면, 기본 생성자는 기본적으로 구현하도록 하자.

* this와 마찬가지로, static 메서드에서는 사용할 수 없다. 

* 매개변수가 있는 생성자는 super(매개변수1, 매개변수2...) 를 이용해서 호출할 수 있다.
  * 당연히 부모클래스에서 매개변수 타입과 갯수가 일치하는 생성자가 있어야 한다.



## 업캐스팅(Upcasting)과 다운 캐스팅(Downcasting)

캐스팅(Casting) : 타입을 변환하는것. 형변환 

자바의 상속관계에 있는 부모와 자식 클래스 간에는 서로 간의 형 변환이 가능하다 .



자바에서의 캐스팅은 좌변의 자료형이 요구하는 정보를 우변이 갖추었을 때 가능하다.

좌변의 자료형에 맞게 우변의 형을 변형하는 것이다.

```java
int intNum = 10;
long longNum = 50L;

intNum = (int)longNum;
String strNum = "999";

intNum = (int)strNum; // 컴파일 에러 
```

* 좌변의 자료형의 요구하는 정보인 숫자형을 둘다 만족하므로 캐스팅이 가능하다.
* 요구형이 맞지 않으면 컴파일 에러가 난다.



### 업캐스팅(Upcasting)

업캐스팅이란, 자식 클래스의 객체가 부모 클래스 타입으로 형변환 하는 것이다

```java
public class Parent {...}
public class Child extends Parent{...}

Parent parent = new Child(); // 업캐스팅 

Child child = (Child) parent; // 다운캐스팅 
```

* 부모 클래스 `타입` 이므로 자식 클래스의 필드나 메서드에 접근 못하고 부모 클래스의 필드나 메서드만 사용 가능하다. 

* 업캐스팅이란, 하위 클래스의 정보를 담을 수 있는 객체에 상위클래스의 자료형을 부여해서, 상위클래스처럼 사용하게 하는 것
* 자식클래스에서 부모클래스의 메서드를 오버라이딩 하면, 오버라이딩된 자식 클래스의 메서드가 호출된다. 

* `(부모클래스타입)자식 인스턴스` 타입을 명시해주지 않아도 된다.

### 왜 사용하는데?

업캐스팅을 통해 부모클래스에서 정의된 서브클래스의 메서드를 호출할때 사용

자식 클래스들은 부모 클래스의 메서드를 상속받거나 오버라이딩 하는데 

이 때 부모 클래스 타입으로 메서드를 호출하면 공통적으로 메서드를 부모와 자식을 한 타입으로 사용할 수 있기 때문이다. 

즉 상속을 받은 서브클래스가 몇 개이든 하나의 타입의 인스턴스로 묶어서 관리할 수 있다.



### 다운캐스팅(Downcasting)

업캐스팅과 반대로 부모 클래스를 자식 클래스의 타입으로 형변환 하는 것

```java
Child child = (Child) parent; // 다운캐스팅 
```

업캐스팅된 자식 클래스 객체의 자료형을 자식 클래스의 정보를 담는 기능을 하도록 자료형을 바꾸어 되돌려 놓는것.

원칙적으로 다운캐스팅 혼자만 쓰면 우변이 좌변에서 필요한 정보를 모두 채워주지 못하기 때문에 불가능한 문장이 되고,

 꼭 업캐스팅이 선행되어야 한다는 것을 명심해야한다





* 반드시 명시적으로 `(자식클래스타입) 부모 인스턴스` 타입을 명시해줘야 한다.



업캐스팅, 다운캐스팅 둘 다 OOP의 다형성을 만족하기 위한것이다. 



### instanceof 연산자

참조변수가 참조하고 있는 인스턴스가 실제로 어떤 타입인지를 알기 위해 사용 

```java
참조변수 instanceof 클래스명
```

* 연산자의 왼쪽에는 변수명, 오른쪽에는 타입(클래스)명 을 사용
* 참조변수가 참조하고 있는 인스턴스 타입이 오른쪽 클래스와 같으면 true를 반환, 다르면 false를 반환
* null을 가리키면 false를 반환한다. 

```java
Parent parent = new Parent();
Child child = new Child();

Parent p2 = new Child();
Child c2 = (Child) p2;


System.out.println(parent instanceof Parent);// true
System.out.println(parent instanceof Child);// false
System.out.println(child instanceof Parent);// true
System.out.println(child instanceof Child); // true
System.out.println(p2 instanceof Parent);   // true
System.out.println(p2 instanceof Child);    //true
System.out.println(c2 instanceof Child);    //true
System.out.println(c2 instanceof Parent);   //true
```



* instanceof가 true이면 검사한 타입(클래스)으로 형 변환이 가능하단 것이다. 



# 메소드 오버라이딩

오버라이딩이란, 상속 관계에 있는 부모클래스에서 이미 정의된 메서드를 자식 클래스에서 같은 메서드 시그니쳐로 `재정의하는것`

메서드의 선언부가 기존 메서드와 완전히 같아야 하며(이름, 매개변수, 반환타입)  `@Override 어노테이션`을 붙인다

```java
class Child extends Parent {
  @Override
  void parentMethod(매개변수1, 매개변수2...) {...}
}
```

* 메서드 오버로드랑은 다르다!

* 메서드를 오버라이딩 할 시에는 명시적으로 @Override 어노테이션을 붙여주는게 좋다
  * 컴파일 시점에 재정의 할 메소드가 없을 경우 컴파일 시점에 오류를 발생해 주기 때문에 런타임시 오작동을 방지할 수 있다.



### **공변 반환 유형 (covariant return type)**

클래스는 자식 클래스의 오버라이딩 기능으로 가장 가까운 부모클래스에서 상속한 다음 필요에 따라 동작을 수정할 수 있다. 

여기서 반환 형식을 리턴 타입(부모클래스 타입)의 하위 타입으로 반환할 수 있는데, 이를 **공변 반환 유형**이라고 한다.

```java
class Parent{
	Parent of(int type){
		return new Parent();
	}
}

class Child extends Parent {
	// covariant return type
	@Override 
  Parent of(int type){
		if(type == 1) return new Parent();
		else return new Child();
	}
}
```

* 즉 리턴타입이 부모 클래스 타입인 Parent를 자식 유형인 Child 객체로 반환할 수 있다. 



#### 오버라이딩의 조건

1. 접근 제어자는 부모 클래스의 메서드보다 좁은 범위로 변경할 수 없다. 
   * 부모클래스가 public 이면 protected, private, default로는 변경 불가
   * 부모 클래스가 protected면 public으로는 변경 가능 
2. 부모 클래스의 메서드보다 많은 수의 예외를 던질 수 없다. 
3. static 메서드는 오버라이딩 할 수 없다
4. final로 선언된 메서드는 오버라이딩 할 수 없다
5. private 메서드인 경우는 오버라이딩이 불가능 하다. 



### hide? hiding 하이딩 (은닉, 숨기기)

만약 자식클래스가 부모클래스의 static 메서드와 동일한 시그니처를 가진 정적 메서드를 정의하는 경우 

자식클래스 메서드는 부모클래스의 메서드를 **hiding(숨기기)**한다.

* static 메서드에 @Override를 달면 컴파일 에러가 발생한다

```java
class Parent{
	
  public static void parentStatic() {}
}

class Child extends Parent {
	// @Override 오버라이드를 붙이면 컴파일 에러
  public static void parentStatic() {}
  
}

Parent parent = new Child(); // 업캐스팅 
parent.parentStatic(); // 업캐스팅 했는데도 Child의 메서드가 아닌 Parent의 메서드를 호출한다 
```

* 업캐스팅을 하여 자식 클래스 객체라도 static 메서드는 Parent의 static 메서드를 호출한다. 
  * 자식 클래스의 static 메서드를 호출하지 않는다.  



왜?

* 언제든지 전역에서 접근 가능해야 하는 static의 특성 때문에 hidding(은닉)으로써 super 키워드를 사용하지 않고 접근이 가능하다.
* 다형성이란 runtime 시에 해당 메서드를 구현한 실체 객체를 찾아 호출하는데, static은  컴파일시에 선언된 객체의 메서드를 찾아 호출하기 때문에 적용되지 않는다. 
  * 이미 JVM 의 static 영역에 올라가있기 때문. 

* 객체지향과 다형성을 위해 쓰지 않는것이 좋다 





# 다이나믹 메소드 디스패치 (Dynamic Method Dispatch)

dispatch : 보내다 란 뜻을 가지고 있다. (물건을 보내다, 무엇무엇을 보내다.)

dynamic은 runtime의 동의어로 사용되며, dispatch는 어떤 메소드를 호출할지 결정하는 것이다.

메소드 디스패치란 프로그램이 어떤 메소드를 호출할 것인가를 결정하여 그것을 실행하는 과정을 말한다.

* 오버라이딩 된 메서드 재정의를하고 동일한 이름의 메서드를 호출할 때,

  어떤 것을 호출할지를 런타임에서 결정하는것. 

* 컴파일 타임에는 메서드의 클래스타입이 정해져있지 않지만 런타임에 정해져서 메서드를 호출하는 것

  

자바는 묵시적으로 메서드를 호출한 객체를 인자로 넘기기 때문에 메서드 내부에서 호출 객체를 참조할 수 있다.
런타임에서 메서드를 호출하는 객체도 결정된다.

즉,

Runtime polymorphism이다 .

*  "같은 클래스를 상속하고 있는 여러 클래스 중 어느 서브클래스를 사용할 것인가"를 런타임 시점까지 미룸으로서, 클래스 재사용성을 높이는 테크닉.



디스패치는 두 종류가 있다

1. 정적 디스패치 : 컴파일 타임에 호출 할 메서드를 알 수 있으면 정적 디스패치
   * 실행 시점이 아니라도 컴파일 시점에 어느 메서드로 호출되는지 결정되는것
   * 상속이나, 인터페이스를 구현(implements)을 하지 않은 클래스의 메서드를 호출하는 경우는 대부분 바로 알 수 있다.
2. 다이나믹 디스패치 : 컴파일 타임이 아닌 런타임에 호출 할 메서드를 알 수 있으면 다이나믹 디스패치 
   * 상속이나 인터페이스를 구현을 한 클래스가 메서드를 호출하는 경우 어떤 메서드를 호출 할 수 있는지 모를때 일어난다. 
   * 인터페이스, 추상클래스, 수퍼클래스의 메서드를 오버라이딩 할 때 일어난다. 
   * 컴파일러가 컴파일 시에는 알 수 없기 때문에 런타임 시에 할당된 객체를 확인 후에 결정한다.



### Double Dispatch

더블 디스패치는 다이나믹 디스패치(런타임 디스패치)를 두번 시도하는것이다. 

* [블로그 참조](https://velog.io/@maigumi/Dynamic-Method-Dispatch)

예제 : 텍스트와 사진 두 가지의 Post를 올릴 수 있고,

 한 번에 모든 SNS에 Post를 업로드 하는 기능을 구현했다고 가정

```java
interface Post { void postOn(SNS sns); } // 인터페이스. 기능은 SNS로 포스트를 올린다 

static class Text implements Post { // 텍스트만 업로드 하는 기능을 가진 클래스
        @Override
        public void postOn(SNS sns) { 

            if(sns instanceof Instagram) {
                // logic which is applicable to Instagram
            }
            else if (sns instanceof Twitter) {
                // logic which is applicable to Twitter
            }

        }
    }

static class Picture implements Post {  // 사진만 업로드하는 클래스 
        @Override
        public void postOn(SNS sns) {
            if(sns instanceof Instagram) {
                // logic which is applicable to Instagram
            }
            else if (sns instanceof Twitter) {
                // logic which is applicable to Twitter
            }
        }
    }

interface SNS { }

static class Instagram implements SNS{ }

static class Twitter implements SNS{ }
```

 문제점

* 다른 타입의 SNS가 추가될 때마다 if 문과 instanceof로 구분을 해주는 로직을 추가해야 한다.
* 만약 insta, twitter가 아닌 facebook에도 업로드 하려고 하면, if문을 추가하여 로직을 다시 작성해야 하는 번거로움이 있다. 
* 다음과 같이 바꾸면 해결이 가능하다

```java
interface Post { void postOn(SNS sns); }

static class Text implements Post {
        @Override
        public void postOn(SNS sns) {
            sns.post(this);
        }
    }

static class Picture implements Post {
        @Override
        public void postOn(SNS sns) {
            sns.post(this);
        }
    }

interface SNS {
        void post(Text post);
        void post(Picture post);
    }

static class Instagram implements SNS {
        @Override
        public void post(Text post) {
           // text upload logic which is applicable to Instagram
        }

        @Override
        public void post(Picture post) {
        	//picture upload logic which is applicable to Instagram
        }
    }
static class Twitter implements SNS {
        @Override
        public void post(Text post) {
          	// text upload logic which is applicable to Twitter
        }

        @Override
        public void post(Picture post) {
            // picture upload logic which is applicable to Twitter
        }
    }
```



* 결과적으로 facebook을 추가해도 POST의 코드에 if문과 instanceOf를 건드릴 필요 없게 된다.
* SNS를 구현하는 facebook만 구현하는 코드를 작성하면 되기 때문 



어떻게 된건데?

* 파라미터가 무엇인지에 따라서 메서드를 호출하는게 아닌, 파라미터가 메서드를 호출하게 한다.

1. Post를 상속받은 Text나 Picture에서 postOn 메서드 사용 시, 파라미터가 어떤 타입인지 알 필요가 없다.
2. postOn 메서드에서 어떤 구현체의 post 함수를 사용할지 고민할 필요가 없게 된다. 그냥 넘어온 파라미터의 메서드를 호출하면 된다. 



2번의 디스패치 즉

1. 인터페이스가 구현된 `Post가 어떤 Text나 Picture의` postOn 메서드를 호출할지.
2. postOn의 파라미터로 넘어온 `SNS의 어떤 구현체`의 .post() 메서드를 사용할지

가 이루어 진다.  



결론 : 다이나믹 디스패치의 조건을 파라미터에 대해 걸지 말자! 파라미터로 넘어온 객체의 메소드를 호출하며 자기 자신을 넘기자.



# 추상 클래스

Abstract : 추상

추상이란, 사물이 지니고 있는 여러가지 측면 가운데서 특정한 측면 만을 가려내어 포착하는 것이다.(위키백과)

* 공통점을 찾고, 그것을 기반으로 묶는것이 추상화. 
* 장점으로서는
  * 객체간의 차이점을 무시하고 객체간의 공통점들을 파악하기 쉽다.
  * 불필요한 세부사항을 제거함으로써 중요한 부분들만 강조할 수 있다. 



클래스이지만 구체적이지 않은 클래스를 말한다.

인터페이스와는 다르고, 클래스를 만들기 위한 일종의 설계도이다.

추상클래스를 사용하기 위해서는 자식 클래스에서 상속을 받아 클래스를 모두 구현해야 한다. 

```java
abstract class 추상클래스명 {
  
  abstract void print(); // 추상메서드. 구현 되어 있지 않다. 
}
```

* 추상 클래스는 반드시 하나 이상의 추상 메서드(abstract method. 구현되어있지 않다.)를 포함 한다.
  * 추상 메서드가 없어도 추상 클래스(abstract class)로 선언 할 수 있다. 
* 생성자와 멤버변수, 일반 메서드 모두를 가질 수 있다. 

* 하지만 자체 인스턴스 (new를 사용한)를 생성할 수 없다. 
  * 구현한 구체적인 내용이 없기 때문이다.



자식 클래스가 추상 클래스를 상속받게 되면, 반드시 추상 메서드를 구현 해야 한다.

* 추상 메서드는 선언부만 작성하고 구현은 되어있지 않다.
* 추상 메서드의 접근 제한자는 private을 사용할 수 없다 - 자식이 구현해야 하니까 
* 자식이 추상 메서드를 구현하지 않을 경우 자식 클래스도 추상 클래스가 된다. 



상속을 받아 구현할 때  `extends` 키워드를 사용한다.

```java
public 구현클래스명 extends 추상클래스명 {
  void print() {
    ...
  }
}
```

* 추상 메서드중 print() 가 있기 때문에 구현 해야 한다.
* 구현하지 않으면 이 클래스도 추상 클래슥 ㅏ된다. 



## 인터페이스 (interface)

추상 클래스 하면 인터페이스(interface)라는 개념도  있다.

interface는 클래스가 아니다. 

추상 클래스보다 추상화 정도가 높은 것이 인터페이스이다. 

```java
public interface 인터페이스 {
  void print();
 
  void getName();
  
  void calculate(int a, int b);
}
```

인터페이스를 선언하려면 interface 키워드를 사용하면 된다. 



* 인터페이스는 오직 추상 메서드와 상수만을 멤버로 가질 수 있다.
* 모든 멤버 변수는 public static final이어야 하며, 이를 생략할 수도 있다. 
* 메소드 역시 모두 public abstract
  *  public 상수
  * public 메소드 signature,
  *  default method, 
  * static method
  *  java 8부터 default method와 static method를 사용할 수 있으며, java 9부터 private method를 사용할 수 있다.
* 인터페이스는 인터페이스들을 다중 상속이 가능하다. 

```java
public interface 인터페이스 extends interface1, interface2, interface3 {}
```

* 인터페이스 내에 default 또는 static으로 선언되지 않은 메소드는 암묵적으로 abstract로 간주된다.



---

클래스가 인터페이스를 구현할 때는 `implements` 키워드를 사용한다

```java
public class 구현클래스 implements 인터페이스 {
 ... 
}
```

* 인터페이스에 선언된 메서드 들을 구현해야 한다. 



## 추상클래스 vs 인터페이스 (abstract class vs interface)

 ### 공통점

* 인스턴스화(instantiation) 가 불가능하다.
* 선언만 있고 구현부가 없는 추상 메서드가 존재한다
* 자식 클래스가 상속받은 메서드의 구체적인 동작을 구현한다.



### 차이점

가장 큰 차이점은 객체간의 연관성의 유무이다.

서로 연관있는 클래스 끼리 사용하길 원한다면 abstract, 그렇지 않다면 interface를 사용한다

(ex: Comparable, Comparator, Cloneable은 서로 연관없이 다양한 클래스에서 사용되고 interface로 구현되있음 )



* 추상 클래스는 클래스이지만 인터페이스는 클래스가 아니다.
* 추상 클래스는 단일 상속만 가능하고 (조상으로부터 상속이지만 단 1개씩) 인터페이스는 다중 상속이 가능하다
* 추상 클래스는 접근 제한자를 자유롭게 사용하지만 인터페이스는 모두 public

* 추상 클래스는 추상 메서드를 자식 클래스가 구체화 하여 그 기능을 확장하는데 목적, 
* 반면 인터페이스는 연관성이 없는 클래스들이 같은 기능을 구현하는 목적 

* 인터페이스가 추상클래스보다 추상화 정도가 높다 

# final 키워드

final : 마지막의, 최종의, 수정할 수 없는



final 키워드는 메서드, 클래스, 필드에 사용할 수 있다.



* final method : 오버라이딩 을 금지 재정의 될 수 없다. 
  * 메서드 오버라이딩 할 수 없음 

* final class : 상속 및 오버라이딩 금지. 해당 클래스는 상속받을 필요도 없고 상속할 수가 없다. 
  * 대표적으로 String class

* final feld : 변경 금지. 클래스 변수 (static)
  * 상수들이 사용된다. 



# Object 클래스

모든 클래스가 암묵적으로 상속받는 Java의 최상위 클래스이다.

* https://docs.oracle.com/en/java/javase/13/docs/api/java.base/java/lang/Object.html



* `protected Object Clone()` : 객체 자신의 복사본을 반환한다.
  * 복제 가능한 클래스는 정해져 있어서 모든 클래스에 대해 호출할 수는 없다. 각 클래스의 API 규격서에서 Cloneable이라고 쓰여진 클래스만 가능하다.
  * 복제 가능한 클래스를 직접 만들기 위해서는 Cloneable 인터페이스를 구현하거나 clone 메소드를 오버라이딩 하면 된다.
  * 메모리 재할당을 하지 않는다.
* `public boolean eqauls(Object obj)` : 객체가 같은지 비교한다. 같으면 true 다르면 false
  * primtive Type이 아닌 Reference Type을 비교한다.
  * `equals`는 두 객체의 내용이 같은지, 동등성(equality) 를 비교하는 연산자
  * 구현된것을 보면 ==를 통해 비교하는데, 이것은 같은 레퍼런스인가를 비교한다. 
    * 객체의 값(필드 등)을 통한 비교가 아니다.
  * 만약 객체안의 값이 같으면 같은 객체라고 인식하게 구현하기 위해서는 eqauls를 오버라이딩해서 재정의 해야 한다. 
* `public final native Class<?> getClass()` : 객체 자신의 클래스 정보를 담고 있는 Class인스턴스를 반환한다.
* ` public native int hashCode()`; JVM으로 부터 객체 자신의 고유값인 해시코드를 반환한다
  * 실행 중에(Runtime) 객체의 유일한 integer값을 반환
  * `hashCode`는 두 객체가 같은 객체인지, 동일성(identity) 를 비교하는 연산자
* `public String toString()` : 객체 자신의 정보를 문자열로 반환한다.
  * 구현된 코드를 보면, 자기자신의 클래스 이름 + 해시코드 값을 출력하므로, 객체 안의 내용을 출력하고 싶으면 오버라이딩 한다

* `public final native void notify()` ; 객체 자신을 사용하려고 wait된 기다리는 쓰레드를 하나만 깨운다.
* `public final native void notifyAll()` ; 객체 자신을 사용하려고 waite된 기다리는 모든 쓰레드를 깨운다.
* `public final void wait()` : 스레드를 일시적으로 중지할 때 호출한다.
* `public final void wait(long timeout)` : 주어진 시간 만큼 스레드를 일시적으로 중지할 때 호출한다.
* `public final void wait()long timeout, int nanos)` : 주어진 시간 만큼 스레드를 일시적으로 중지할 때 호출한다.

*  `protected void finalize() throws Throwable `: 오버라이딩 하였을 때 객체가 소멸될 때 가비지 컬렉터에 의해 호출된다.
  * 객체가 소멸되는 시점에 특정한 동작을 수행해야할 때 사용한다.
  * 하지만 finalize()는 지양하는 것이 좋다. 
  * 수행하는데 오랜 시간이 걸린다면 OOME(Out Of Memory)가 발생할 수 있다. 







## 추가적으로, 객체의 eqauls() and hashcode() 의 재정의



## equals() 메서드를 재정의 안해도 되는 경우

1. `각 인스턴스가 본질적으로 고유하다` : 값을 표현하는 개체가 아니라 동작하는 개체를 표현하는 클래스일 때

   * ex : Thread. 유일 할 경우

2. 인스턴스의 논리적 동치성(logical equality)를 검사할 일이 없는 경우

   * 인스턴스들 끼리 eqauls() 메서드를 사용해서 논리적으로 같은지 검사할 필요가 없는 경우
   * 값을 통한 비교가 필요 없는 경우

3. 상위 클래스에서 재정의한 eqauls가 하위 클래스에도 들어맞을 때

   * Set 구현체는 AbstractSet이 구현한 equals를 상속받아 쓰므로 오버라이딩 할 필요가 없다 

4. 클래스가 private이거나 package-privated이고 eqauls 메서드를 호출할 일이 없으면.

   * 호출되는걸 막고 싶다면?

   * ```java
     @Ovveride public boolean eqauls(Object o) {throw new AssertionError();}
     ```

     * 예외를 던져버리자

   * [이너클래스나 중첩 클래스 일때도 의미한다.](https://donghyeon.dev/%EC%9D%B4%ED%8E%99%ED%8B%B0%EB%B8%8C%EC%9E%90%EB%B0%94/2021/01/04/eqauls%EB%A5%BC-%EC%9E%AC%EC%A0%95%EC%9D%98-%ED%95%98%EB%8A%94-%EB%B0%A9%EB%B2%95/) 



## equals() 메서드를 재정의 해야 하는 경우.

두 객체가 물리적으로 같은지를 비교하는게 아니라 (같은 메모리 주소를 참조하는지)

논리적인 동치성(객체가 같은지가 아니고 객체가 가지고 있는 것들 필드 등이 같은지 )을 알고 싶은경우인데,

상위 클래스의 eqauls가 재정의 되지 않은 경우  재정의 해야 한다

* ex) String을 비교할 때, 같은 문자열이지만 `== 비교`를 하게 되면 메모리 주소값을 비교하는 것이므로 false가 나온다 

equals의 논리적 동치성을 재정의하는 경우에 

Map의 key 와 Set의 원소로 사용가능하다.



만약 값 클래스라 해도, 값이 **같은 인스턴스가 둘 이상 만들어지지 않음을 보장**하면 equals()를 재정의 하지 않아도 된다. 

* Enum도 여기에 해당 된다.

같은 값 클래스라면, 내부적으로 동일한 객체를 반환 해주니, **논리적 동치성(equals)이 즉 객체 식별성(==)과 똑같은 의미가 된다.**

 

## 재정의 하는 방법

equals를 재정의 할때 다음의 규약을 따라야 한다.

Object 명세에 적힌 규약이다 

* https://docs.oracle.com/en/java/javase/13/docs/api/java.base/java/lang/Object.html
* 메서드 상세 안에 equals 를 보자



1. 반사성(reflexivity) : null이 아닌 모든 참조값 x에 대해 x.eqauls(x)는 true다 

   * 즉 자기 자신이 null이 아니면 자기 자신을 비교할때는 당연히 true여야 한다. 같으니까 

   * ```java
     Member member = new Member();
     member.equals(member); // 항상 true
     ```

     

2. 대칭성(symmetry) : null이 아닌 모든 참조 값 x, y 에 대해 x.equals(y)가 true이면 y.equals(x) 도 true이다 

   * 즉 다른 객체가 자신과 같았을때 자신을 기준으로 다른 객체를 비교하여 같았을 때는 다른 객체를 기준으로 자기자신을 비교해도 true이다 

   * ```java
     Member ys부캐 = new Member();
     Member ys부캐2 = new Member();
     ys부캐.equals(ys부캐2); // true라면
     ys부캐2.equals(ys부캐); // true여야 한다.
     ```

3. 추이성(transitivity) : null이 아닌 모든 참조값 x,y,z에 대해, x.equals(y)가 true이고, y.equals(z) 도 true이면, x.equals(z)도 트루이다.

   * x랑 y랑 같고, y랑 z랑 같다는 소리는 셋다 같단 소리이다. x = y = z 

4. 일관성(consistency) : null이 아닌 모든 참조 값 x,y 에 대해 x.equals(y)를 반복해서 호출하면 항상 true를 반환하거나 항상 false를 반환한다. 

5. null-아님 : null이 아닌 모든 참조 값 x에 대해 x.equals(null)은 항상 false이다. 



### equals 메서드 재정의 (구현) 단계



여기서 `o` 는 Object equlas(Object o) 메서드의 매개변수 o를 의미한다.  



1. == 연산자를 사용하여 입력 값이 자신의 참조인지 비교한다.

   * 자기 자신이면 true를 반환한다. 

   * ```java
     if (this == o) return true;
     ```

2.  instanceof 연산자로 입력이 올바른 타입인지 확인한다.

   * ```java
     if (!(o instanceof 자기 타입)) return false;
     ```

   * 올바른 타입이 아니라면 false를 반환한다.

   * 정의된 클래스 일 수도 있고, 그 클래스가 구현한 인터페이스가 될 수도 있다.

   *  어떤 인터페이스는 자신을 구현한 (서로 다른) 클래스끼리도 비교할 수 있도록 equals 규약을 수정하기도 한다. 

     * 이런 인터페이스를 구현한 클래스라면 equals에서 (클래스가 아닌) 해당 인터페이스를 사용해야 한다.
     *  Set, List, Map, Map.Entry 등의 컬렉션 인터페이스들이 여기 해당한다.
     * Stack과 LinkedList를 서로 equals() 비교를 할 때, 이 둘은 List의 구현체이므로, AbstracList에 정의된 equals()를 사용한다.

3. 입력을 올바른 타입으로 형 변환한다.

   * 2번에서 instanceof 검사를 통과하면, 이 단계는 100% 성공한다. 

   * ```java
     MyType obj = (MyType)o; 
     ```

4. 입력 된 객체 o와 자기 자신의 대응되는 핵심 필드들이 모두 일치하는지 하나하나 검사한다.

   * 모든 필드가 일치하면 true를 다르면 false를 반환한다.
   * 2단계에서 인터페이스를 사용했다면, 입력의 필드값을 가져올 때에도 그 인터페이스의 메서드를 사용한다.

5. equals를 재정의 할 땐 hashcode도 반드시 재정의 한다.

6. 너무 복잡하게 해결할 필요 없다.

   * 필드들의 동치성만 검사해도 equals 규약을 어렵지 않게 지킬 수 있다
   * [향로님의 블로그](https://jojoldu.tistory.com/134)

7. Object 이외의 타입을 매개 변수로 받는 equals 메서드를 선언하지 말자

   * 이 방법은 오버라이딩이 아니고 메서드 오버로딩이다

   * ```java
     public boolean equals(MyType type) {}
     ```



### equals 자동 생성

요즘 IDE (Intelij)나 라이브러리(롬복)를 이용해서 equals를 자동생성 할 수 있다. 

* 다음 코드는 인텔리제이가 자동생성한 코드이다

```java
class Car {
    private String name;
    private int speed;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return speed == car.speed && Objects.equals(name, car.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, speed);
    }
}
```









### 참조

* https://blog.naver.com/swoh1227/222181505425
* https://velog.io/@maigumi/%EC%9E%90%EB%B0%94-%EC%83%81%EC%86%8D
* https://xxxelppa.tistory.com/199?category=858435
* https://docs.oracle.com/en/java/javase/13/docs/api/java.base/java/lang/Object.html
* https://velog.io/@roeniss/%EC%9E%90%EB%B0%94-%EA%B8%B0%EC%B4%88%EC%A7%80%EC%8B%9D-%EC%A0%95%EB%A6%AC#6%EC%A3%BC%EC%B0%A8--%EC%83%81%EC%86%8D
* https://docs.oracle.com/javase/tutorial/java/IandI/override.html
* https://docs.oracle.com/javase/tutorial/java/IandI/subclasses.html
* https://donghyeon.dev/%EC%9D%B4%ED%8E%99%ED%8B%B0%EB%B8%8C%EC%9E%90%EB%B0%94/2021/01/04/eqauls%EB%A5%BC-%EC%9E%AC%EC%A0%95%EC%9D%98-%ED%95%98%EB%8A%94-%EB%B0%A9%EB%B2%95/