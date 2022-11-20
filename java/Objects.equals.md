# Java - 객체의 비교에는 Objects.equals를 사용하자



자바에서 객체를 비교할 때는 객체.equals() 보다 Objects.equals 를 사용해서 비교하는것이 NPE(NullPointerException)으로 부터 안전하다. 

  


먼저 객체의 비교에서는 `동일성` 비교(`==` 비교)와 `동등성` 비교(`.equals()`)를 이용해서 비교할 수 있다.  

### 동일성

동일성 (Identity) : 동일하다는 뜻 

### 동등성

동등성 (equality)



다음과 같이 두 객체를 비교하는 경우가 있다고 하자. 

```java
public void print(Text originText, Text otherText) {
  
  if (originText.equals(otherText)) {
    System.out.println("두 객체가 같다")
  	return;
  } 
  
  System.out.println("두 객체가 다르다")
}
```



객체를 비교할때는  .equals() 메서드를 이용해서 비교할 수 있다.

```java
originText.equals(otherText)
```



그런데, 이 비교하는 과정에서 치명적인 문제가 있을 수 있다. 과연 무엇일까?   


바로 NullPointerException 을 발생시킬 수 있기 때문이다. 

* originText와 otherText는 외부에서 메소드를 호출하면서 파라미터로 넘겨준다.
* 만약에, originText가 null 값 이라면? 



충분히 일어날 수 있는 버그라고 생각한다.   

* 객체에서 NPE가 발생할 때는 객체를 사용할 때 발생한다.  
* `== null` 비교를 하면 안전하다.
* 그런데, 객체.equals()를 호출할 때 마다  모든 메서드마다 `== null` 체크를 해야할까?.. 음..



이 때, 두 객체를 비교하는 안전한 방법이 있다.  
바로 `Objects 클래스의 equals() 메소드`를 사용하여 비교하는 것이다. 



# Objects 클래스와 Objects.equals()?



* https://docs.oracle.com/javase/8/docs/api/java/util/Objects.html

### Objects 클래스

Object 와 유사한 이름을 가진 java.util.Objects 클래스는 객체 비교, 해시 코드 생성, null 여부,

객체 문자열 리턴 등의 연산을 수행하는 정적 메소드들로 구성된 Object 클래스의 유틸리티 클래스이다.    
유틸리티 클래스여서 private 생성자를 통한 객체 생성을 금지한다. 



<img src ="https://blog.kakaocdn.net/dn/tg9F8/btrRAgR4RcL/wWocZTa0dvVCYYQ0h2TC20/img.png" width=900 height=350>



이 Objects 클래스에서는 equals() 메소드를 지원한다.  
그리고 이 equals() 메소드는 null에 대해 비교적 훨씬 안전하다.   
왜인지 이 메소드를 분석해보자 .  

```java
public static boolean equals(Object a, Object b) {
	return (a == b) || (a != null && a.equals(b));
}
```

1. Object a, b 를 인자로 받는다
2. a == b 동일성 비교 즉 주솟값 비교를 한다.
3. a가 null이 아니라면 a.equals(b) 를 호출한다

* 참고로 두 객체 다 null 이면 둘 다 null 이므로 true를 반환하는게 맞다.   
  

> 이것이 왜 안전한가? 

  


```java
Member member = null;
```

자바에서 null값을 가지고 있는 객체에 접근해서 메소드나 필드에 접근하게 된다면 NPE가 발생하게 된다.  

```java
member.getName(); // NullPointerException 발생 
```

  


하지만 동일성 비교인 == 연산을 통해 null 비교를 하게 되면 NPE는 발생하지 않는다. 

```java
System.out.println(member == null); // true 리턴 
```



먼저 == 비교를 통해 null인지 판단하고, null 이 아니라면 메소드를 실행시키기 때문에 NullPointerException으로부터 비교적 안전한 것이다. 

* a.equals(b) 를 사용할 때는, `a.equals() 내부에서 null 체크`를 한다면 NPE가 발생하지 않겠지?
  * b가 null이면 false를 리턴하면 된다. (b == null) return false 



그러므로 다음과 같이 Objects.equals() 메소드를 사용해서 비교하게 되면 안전하게 객체를 비교할 수 있다.

```java
public void print(Text originText, Text otherText) {
  
  if (Objects.equals(originText, otherText)) {
    System.out.println("두 객체가 같다")
  	return;
  } 
  
  System.out.println("두 객체가 다르다")
}
```



1. originText 와 otherText 동일성 비교를 한다. 
2. originText가 null이 아니라면 originText.equals(otherText)를 호출한다. 

<br>



그러므로 객체의 비교에는 Objects.equals(객체 1, 객체 2); 를  사용하자!

* 또한 객체를 비교할 시에는 equals()와 hashcode() 오버라이딩이 필요하다 !
