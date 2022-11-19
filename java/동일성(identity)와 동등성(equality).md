

# 동일성과 동등성 (Identity, Equality)



자바에서 객체를 비교할 때는 동일성과 동등성이 무엇인지 알고 비교해야 한다.

> 동일성(Identity)  : 메모리 내 주소값이 같은지 비교한다.
> 동등성(Equality) : 논리 값, 같은 정보가 동등한지 비교한다.

  


### 동일성

동일성 (Identity) : 동일하다는 뜻 



동일성은 동일하다는 뜻으로 객체 비교 시 두 객체가 `완전히 같은 경우`를 의미한다.  
비교 대상의 두 객체가 가르키는 메모리 주소가 같은것을 의미한다

<img src="https://blog.kakaocdn.net/dn/byH9B2/btrRBfL0fK9/TbCJAd4j3ZQH57ETIAVgA1/img.png" width=550 height=350>

```java
Member member1 = new Member("영수");
Member member2 = member1;
```



자바에서 메모리 주소를 비교하는 연산은 `==` 연산자로 확인할 수 있다.

변수는 stack영역에 생성되는데, 이 stack영역에 있는 변수는 heap영역에 있는 객체를 가리키게 된다.  
이 때 두 변수가 동일한 객체를 가리키게 되므로 두 변수는 동일하다고 할 수 있다. 

> Primitive 타입은 객체가 아닌 값을 비교하므로 == 연산자를 통한 비교시 내용이 같으면 동일하다고 한다.  
>
> * 변수 선언부는 JVM Runtime Data Area의 Stack 영역에 저장되고, 해당 변수에 저장된 상수는 Runtime Constant Pool에 저장된다.  
>
> * JVM Stack의 변수 선언부는 해당 Runtime Constant Pool의 주소값을 가지게 되고 어떠한 변수가 같은 상수를 저장하고 있다면, 이 두 변수는 같은 Runtime Constant Pool의 주소값을 가지기 때문에 엄밀히 말하자면 primitive type 역시 주소값 비교가 이루어지는 것이다.



### 동등성

동등성 (equality) : 동등하다는 뜻



두 객체가 `같은 정보(필드 등)를 갖고 있는 경우`를 의미한다.  
동등성은 변수가 참조하는 객체의 메모리상 다르더라도 내용만 같으면 두 변수는 동등하다고 이야기 할 수 있다.    

<img src="https://blog.kakaocdn.net/dn/GfNBx/btrRDYJpNp3/NxxjsMT2lOM52hH4DmjAOK/img.png" width=550 height=350>

```java
Member member1 = new Member("영수");
Member member2 = new Member("영수");
```

자바에서 동등성을 비교하는 연산은 `equals()` 메소드를 사용하여 판별할 수 있다.   
new 키워드를 사용하여 두 객체를 메모리에 할당하고 변수가 가리키게 했으므로 두 변수는 heap영역에 있는 서로 다른 객체를 참조하게 된다.        



> Primitive 타입은 객체가 아니므로 .equals() 메소드를 사용할 수 없다. 
>
> 또한 String 클래스는 예외이다 이유는 밑에 적도록 하겠다. 



자바에서 객체 비교를 할 때에는 equals() 메소드를 오버라이딩 하고 비교 해야 한다.  
그 이유는 모든 자바 클래스의 최상위 타입 `Object` 클래스의 equals() 메소드에 있다.

> JPA에서 사용되는 Entity를 비교할 때는 다르다. 영속성 컨텍스트가 동일성을 보장해 주기 때문이다. 

## Object.equals()와 Equals() 오버라이딩



Object 클래스에 있는 equals 메소드를 보자.

```java
// in Object.java

public boolean equals(Object obj) {
  return (this == obj);
}
```

객체를 ` == 비교` 하고 있다. 



== 비교는 힙 메모리 상 주솟값을 비교하는데, 두 객체가 가진 값(필드 등)이 같더라도 new 연산을 통해 새로 생성되었다면 무조건 false라고 나오게 된다.   
이 때 equals 메소드를 오버라이딩 해서 사용할 수 있다.  



다음 두 예제를 보자.   
첫 번째는 오버라이딩 안한 equals()가 숨겨져 있다.  
두 번째는 equals()를 오버라이딩 한 예제이다. 

```java
//1 번 예제
public class Number {
    
  	private Long id;
  
  	private String name;

    public boolean equals(Object obj) {
  		return (this == obj);
		}
}
```

* 자바에서 모든 클래스는 Object 클래스를 상속받고, 그에 따른 Object.equals()를 상속 받아서 오버라이딩을 안하게 된다면 ==비교를 사용하는 equals() 메서드를 사용하게 되므로 동등성 비교에 실패하게 된다.
* 그러므로 다음과 같이 오버라이딩이 필요하다. 

```java
public class Member {
    
    private Long id;

    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Member member = (Member) o;
        return Objects.equals(id, member.id) && Objects.equals(name, member.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
```

equals()와 hashcode() 메소드를 오버라이딩 하여서 필드의 값을 비교하게 되었으므로 두 객체의 연산 결과는 true가 될 것이다.



>  그러므로 객체 동등성 비교 (equals()) 시에는 equals()와 hashcode()를 같이 사용해야 하며, 직접 비교 대신 Objects.equals() 메소드를 사용해야 한다 !
>
> * Objects.equals() ? 
>   * null safe하게 equals 연산을 할 수 있는 Object의 유틸 클래스이다. 



> equals() 메소드를 오버라이딩 할 때는 hashcode()도 같이 오버라이딩 해야 한다! 



# **HashCode**

> 객체 해시 코드란? 
>
> * 객체를 식별하는 하나의 정수 값

 

자바 클래스의 최상위 타입인 **Object의 hashCode() 메서드**는 **객체의 메모리 번지수를 이용해 해시 코드를 만들어 리턴**한다.

즉, Heap 영역에 있는 객체마다 다른 값을 가지게 된다.



#### hashcode() 오버라이딩

객체의 값을 동등성(equals()) 비교시 hashCode()를 오버라이딩해야 하는 이유는, 자바 컬렉션 프레임워크에서 HashSet, HashMap, HashTable은 다음과 같은 방법으로 두 객체가 동등한지 비교하기 때문이다.  


1.  **hashCode()** 메소드를 실행해서 리턴된 해시코드 값이 같은지를 본다. 
2. 해시 코드값이 다르면 다른 객체로 판단하고, 해시 코드값이 같으면 **equals()**메소드로 다시 비교한다. 



이 hashcode가 같고 equals() 가 true여야  동등 객체로 판단한다.   
즉, 해시코드 값이 다른 엔트리끼리는 동치성 비교를 `시도조차 하지 않는다`.



그리고  Hash Table을 사용하는 자료형에서는 데이터가 존재하는지 확인하기 위해 해싱 알고리즘을 사용한다.      


 해싱된 결과를 주소값으로 찾아가서 그곳에 같은 자료가 있는지 확인해야 하기 때문이다.   


해싱 알고리즘에 사용되는 값이  hashCode이다.    




>  그러므로 equals()를 오버라이딩 해야 할 때는 hashcode도 오버라이딩 해야 한다



### hashCode()는 어떻게 작동하는가?

자바에서의 해시코드 규약

- equals 비교에 사용되는 정보가 변경되지 않았다면, 애플리케이션이 실행되는 동안 그 객체의 hashCode 메소드는 몇 번을 호출해도 일관되게 항상 같은 값을 반환해야 한다.(단, 애플리케이션을 다시 실행한다면 이 값이 달라져도 상관없다.)
- equals(Object)가 두 객체를 같다고 판단했다면, 두 객체의 hashCode는 똑같은 값을 반환해야 한다.
- equals(Object)가 두 객체를 다르다고 판단했더라도, 두 객체의 hashCode가 서로 다른 값을 반환할 필요는 없다. 단, 다른 객체에 대해서는 다른 값을 반환해야 해시테이블의 성능이 좋아진다.



# 결론



1. 동일성(Identity) :  == 연산을 통해 메모리 내 주소값이 같은지 비교하는 것.

2. 동등성(Equality) : equals() & hashCode() 를 통해 같은 정보(필드 등)가 같은지 비교하는 것.

3. equals() 메소드에서는 "두 객체가 같다" 의 기준이 될 필드들을 비교하도록 재정의하며, hashcode() 도 재정의 해야 한다.

4. hashCode 메소드에서는 "두 객체가 같다" 의 기준이 되는 값을 위해 필드들의 값으로 hashCode를 만들도록 재정의한다.



객체를 비교하기 위해서는 equals()와 hashcode()를 오버라이딩 하자.    




> 동일하면 동등하지만, 동등하다고 동일한 것은 아니다. 
>
> hashcode가 같아도 equals()는 true가 아닐 수 있다.









# String은 동일성 비교(==)를 해도 true가 나올 수 있다?



* https://0soo.tistory.com/76

JVM에서는 String을 조금 특별히 관리한다.    
String을 생성하는 방법은 2가지가 있는데 new 연산자와 리터럴("")를 이용해서 생성할 수 있다.    
 JVM은 String에 값을 할당할 때,  객체의 영역인 heap 영역이 아니라, constant pool 영역으로 찾아간다. 그리고 constant pool 영역에 이전에 같은 값을 가지고 있는 String 객체가 있다면, 그 객체의 주소값을 반환하여 참조하도록 한다.

  


String 클래스의의 equals()를 보자

```java
public boolean equals(Object anObject) {
    if (this == anObject) {
        return true;
    }
    if (anObject instanceof String) {
        String anotherString = (String)anObject;
        int n = value.length;
        if (n == anotherString.value.length) {
            char v1[] = value;
            char v2[] = anotherString.value;
            int i = 0;
            while (n-- != 0) {
                if (v1[i] != v2[i])
                    return false;
                i++;
            }
            return true;
        }
    }
    return false;
}
```



String 클래스는 위와 같이 `equals()` 를 재정의하여 인자로 전달된 String의 문자열을 비교하고 있다.   

1. `==` 키워드를 통해 두 객체의 동일성 여부를 판단하고, 
2. 두 객체가 동일하지 않다면 String 인지  판단, 
3. 문자 배열로 바꾼 뒤 문자 하나 하나가 같은지 비교한다. 



이 때, 모든 조건을 통과한다면 두 String 객체의 내용이 같은 것이므로 동등하다고 판별한다..

  



## 참조



* https://steady-coding.tistory.com/534
* https://pomo0703.tistory.com/17
* https://creampuffy.tistory.com/140