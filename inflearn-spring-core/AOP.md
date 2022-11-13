# 포인트컷, 어드바이스, 어드바이저 - 소개





스프링 AOP를 공부했다면 다음과 같은 단어를 들어보았을 것이다. 항상 잘 정리가 안되는 단어들인데,
단순하지만 중요하니 이번에 확실히 정리해보자.

* 포인트컷( Pointcut ): 어디에 부가 기능을 적용할지, 어디에 부가 기능을 적용하지 않을지 판단하는
  필터링 로직이다. 주로 클래스와 메서드 이름으로 필터링 한다. 이름 그대로 어떤 포인트(Point)에 기능을
  적용할지 하지 않을지 잘라서(cut) 구분하는 것이다.



* 어드바이스( Advice ): 이전에 본 것 처럼 프록시가 호출하는 부가 기능이다. 단순하게 프록시 로직이라
  생각하면 된다.



* 어드바이저( Advisor ): 단순하게 하나의 포인트컷과 하나의 어드바이스를 가지고 있는 것이다. 쉽게
  이야기해서 포인트컷1 + 어드바이스1이다.





정리하면 부가 기능 로직을 적용해야 하는데, 포인트컷으로 어디에? 적용할지 선택하고, 어드바이스로 어떤
로직을 적용할지 선택하는 것이다. 그리고 어디에? 어떤 로직?을 모두 알고 있는 것이 어드바이저이다.

### 쉽게 기억하기



* 조언( Advice )을 어디( Pointcut )에 할 것인가?
* 조언자( Advisor )는 어디( Pointcut )에 조언( Advice )을 해야할지 할지 알고 있다



> 어디(PointCut)에 부가기능(Advice)를 적용할 것인가 = Advisor



### 역할과 책임
이렇게 구분한 것은 역할과 책임을 명확하게 분리한 것이다.

* 포인트컷은 대상 여부를 확인하는 필터 역할만 담당한다.



* 어드바이스는 깔끔하게 부가 기능 로직만 담당한다.



* 둘을 합치면 어드바이저가 된다. 스프링의 어드바이저는 하나의 포인트컷 + 하나의 어드바이스로 구성된다.



![image-20221113002009685](/Users/ysk/study/study_repo/inflearn-spring-core/images//image-20221113002009685.png)



### Pointcut 관련 인터페이스 - 스프링 제공

```java
public interface Pointcut {
	ClassFilter getClassFilter();
	MethodMatcher getMethodMatcher();
}

public interface ClassFilter {
	boolean matches(Class<?> clazz);
}

public interface MethodMatcher {
	boolean matches(Method method, Class<?> targetClass);
//..
}
```



포인트컷은 크게 ClassFilter 와 MethodMatcher 둘로 이루어진다. 이름 그대로 하나는 클래스가
맞는지, 하나는 메서드가 맞는지 확인할 때 사용한다. 둘다 true 로 반환해야 어드바이스를 적용할 수 있다



