# Java - 객체의 비교에는 Objects.equals를 사용하자



자바에서 객체를 비교할 때는 객체.equals() 보다 Objects.equals 를 사용해서 비교하는것이 NPE(NullPointerException)으로 부터 안전하다. 지금부터 왜 그런지 알아보자. 


먼저 객체의 비교에서는 `동일성` 비교(`==` 비교)와 `동등성` 비교(`.equals()`)를 이용해서 비교할 수 있다.  



### 동일성

동일성 (Identity) : 동일하다는 뜻 



### 동등성

동등성 (equality)

 



```java
A a = new A();
B b = new B();
```



A 객체와 B 객체를 비교할때는 ==비교(동일성, a==b ) 또는 .equals(동등성, a.equals(b))을 이용해서 비교할 수 있다.





동일성



동등성

