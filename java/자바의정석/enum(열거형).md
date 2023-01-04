# Java enum (열거형)



## 열거형(Enums)

관련된 상수들을 같이 묶어 놓은 클래스. 



자바의 열거형은 C언어의 열거형보다 더 향상된 것으로 열거형이 갖는 값뿐만 아니라 타입도 관리하기 때문에 보다 논리적인 오류를 줄일 수 있다.

기존의 상수의 값이 바뀌면, 해당 상수를 참조하는 모든 소스를 다시 컴파일 해야한다. 하지만 열거형 상수를 사용하면 기존의 소스를 다시 컴파일하지 않아도 된다.



* 자바의 열거형은 typesafe enum 이라서 실제 값이 같아도 타입이 다르면 컴파일 에러가 발생한다. - 값뿐만아니라 타입도 체크한다

```java
if(Card.CLOVER == Card.TWO) // true지만 false이어야 의미상 맞음
if(Card.Kind.CLOVER == Card.Value.TWO) // 컴파일 에러가 발생 값은 같지만 타입이 다르다
```



## 열거형의 정의와 사용

열거형을 정의하는 방법은 간단하다.
괄호 {} 안에 상수의 이름만 나열하면 된다.

> enum 열거형 이름 { 상수명1, 상수명2, ... }

예를 들어 동서남북 4방향을 상수로 정의하는 열거형 Direction은 다음과 같다.

```java
enum Direction { EAST, SOUTH, WEST, NORTH }
```

이 열거형에 정의된 상수를 사용하는 방법은 '열거형이름.상수명'이다.

`클래스의 static변수를 참조하는 것과 동일하다.`

```java
class Unit {
	int x, y;	// 유닛의 위치
  Direction dir;	// 열거형을 인스턴스 변수로 선언

	void int() {
    	idr = Dircetion.EAST;	// 유닛의 방향을 EAST로 초기화
    }
}
```

- 열거형 상수 간의 비교에는 `==`을 사용할 수 있다.
  -  `equals()`가 아닌 `==`으로 비교가능한 것은 그만큼 빠르다는 것이다.
- 하지만 비교연산자(<, >)는 사용할 수 없고 `compareTo()`는 사용 가능하다.
  - compareTo()는 두 비교대상이 같으면 0, 왼쪽이 크면 양수, 오른쪽이 크면 음수를 반환한다.
- switch문의 조건식에 열거형 사용하기

```java
void move(Direction dir) {
    switch(dir) {
        case EAST: x++; //Direction.EAST라고 쓰면 안된다.(열거형 이름 없이 상수 이름만 적어야 한다.)
          break;
        case WEST: x--;
          break;
        case SOUTH: y++;
          break;
        case NORTH: y--;
          break;
    }
}
```

* enum의 이름은 적지 않고, 상수의 이름만 적어야 한다는 제약이 있다.



## **모든 열거형의 조상 - java.lang.Enum**
\- 모든 열거형은 Enum의 자손이며, 아래의 메서드를 상속받는다.

| 메서드                                      | 설명                                                      |
| ------------------------------------------- | --------------------------------------------------------- |
| `Class<E> getDeclaringClass()`              | 열거형의 Class 객체를 반환한다.                           |
| `String name()`                             | 열거형 상수의 이름을 문자열로 반환한다.                   |
| `int ordinal()`                             | 열거형 상수가 정의된 순서를 반환한다. (0부터 시작)        |
| `T valueOf(Class<T> enumType, String name)` | 지정된 열거형에서 name과 일치하는 열거형 상수를 반환한다. |

* 컴파일러가 자동적으로 추가해 주는 메서드도 있다.

```java
static E values() //  해당 열거형의 모든 상수를 배열에 담아 반환한다.
static E valueOf(String name)  //  전달된 문자열과 일치하는 해당 열거형의 상수를 반환한다.

//사용
Direction d = Direction.valueOf("WEST");
```



## 열거형에 멤버 추가하기

Enum클래스에 정의된 ordinal()이 열거형 상수가 정의된 순서를 반환하지만,
이 값을 열거형 상수의 값으로 사용하지 않는 것이 좋다.

* 이 값은 내부적인 용도로만 사용되기 위한 것이기 때문이다.



열거형 상수의 값이 불연속적인 경우에는 이때는 다음과 같이 열거형 상수의 이름 옆에 원하는 값을 괄호()와 함게 적어주면 된다.

```java
enum Direction { 
  EAST(1), 
  SOUSTH(5), 
  WEST(-1), 
  NORTH(10) 
}
```

**- 괄호()를 사용하려면, 인스턴스 변수와 생성자를 새로 추가해 줘야 한다.**

```java
enum Direction {
    EAST(1), SOUTH(5), WEST(-1), NORTH(10); // 끝에 ';'를 추가해야 한다.
    
    private final int value; // 정수를 저장할 필드(인스턴스 변수)를 추가
    Direction(int value) { this.value = value; } // 생성자를 추가 - private Direction(int value)와 동일
    
    public int getValue() { return value; }
}
```

 

**- 열거형의 생성자는 묵시적으로 private 이므로, 외부에서 열거형의 객체를 생성 할 수 없다.**

```java
Direction d = new Direction(1); // 에러. 열거형의 생성자는 외부에서 호출 불가
```



## **열거형에 추상 메서드 (Abstract Method) 추가하기**
열거형 Transportation은 운송 수단의 종류 별로 상수를 정의하고 있으며,
각 운송 수단에 기본 요금(BASCI_FARE)이 책정되어 있다.

```java
enum Transportation {
	BUS(100), TRAIN(150), SHIP(100), AIRPLANE(300);
    
  private final int BASIC_FARE;
    
  private Transportation(int basicFare) {
    	BASEIC_FARE = basicFare;
    }
  
  int fare() { // 운송 요금을 반환한다
    return BASIC_FARE;
  }
}
```

하지만 이것만으로는 부족하다. 거리에 따라 요금을 계산하는 방식이 각 운송 수단마다 다를 것이기 때문이다.


이럴 때, 열거형에 추상 메서드'fare(int distance)'를 선언하면
각 열거형 상수가 이 추상 메서드를 반드시 구현해야 한다.

```java
enum TRansportation {
	BUS(100) {
    	int fare(int distance) { return distance*BASIC_FARE;}
   	},
  
    TRAIN(15) { 
      int fare(int distance) { // 추상메서드 구현
        return distance * BASIC_FARE;
      }
    },
    
    SHIP(100) { 
      int fare(int distance) {  // 추상메서드 구현
        return distance * BASIC_FARE;
      }
    };
  
  
    abstract int fare(int distance); // 거리에 따른 요금을 계산하는 추상 메서드
  
    protected final int BASIC_FARE; // protected로 해야 각 상수에서 접근 가능
  
    Transportation(int basicFare) {
    	BASIC_FARE = basicFare;
    }
  
    public int getBasicFare() { return BASIC_FARE; }
}
```

위 코드는 열거형에 정의된 추상 메서드를 각 상수가 어떻게 구현되는지 보여준다.
마치 익명 클래스를 작성한 것처럼 보일 정도로 유사하다.



## **열거형의 이해**

열거형 Direction이 아래와 같이 선언되어 있을 때, **사실은 열거형 상수 하나 하나가 객체이다.**

 

```java
enum Direction { EAST, SOUTH, WEST, NORTH }
```

열거형 Direction은 아래와 같은 클래스로 선언된 것과 유사하다. 

```java
class Direction {
    static final Direction EAST = new Direction("EAST");
    static final Direction SOUTH = new Direction("SOUTH");
    static final Direction WEST = new Direction("WEST");
    static final Direction NORTH = new Direction("NORTH");
    
    private String name;
    
    private Direction(String name){
    	this.name = name;
    }
}
```

Direction 클래스의 static 상수 EAST, SOUTH, WEST, NORTH의 값은
객체의 주소이고, 이 값은 바뀌지 않는 값이므로 '=='로 비교가 가능한 것이다.

> enum의 상수는 static이고, 객체의 주소를 담고 있고 값이 바뀌지 않으므로 ==로 동일성 비교가 가능하다. 



### 참조

* 자바의 정석