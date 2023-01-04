# Java Annotation (애노테이션, 주석)





## Annotation이란 



애노테이션은 소스코드에 붙여서 특별한 의미를 부여하는 기능이다.

 주석(comment)처럼 프로그래밍 언어에 영향을 미치지 않으면서도 다른 프로그램에게 유용한 정보를 제공할 수 있다.



`다음은 모든 Annotation의 조상인 Annotation인터페이스의 소스코드의 일부이다.`

```java
package java.lang.annotation;

/**
 * The common interface extended by all annotation interfaces.  Note that an
 * interface that manually extends this one does <i>not</i> define
 * an annotation interface.  Also note that this interface does not itself
 * define an annotation interface.
 *
 * More information about annotation interfaces can be found in section
 * {@jls 9.6} of <cite>The Java Language Specification</cite>.
 *
 * The {@link java.lang.reflect.AnnotatedElement} interface discusses
 * compatibility concerns when evolving an annotation interface from being
 * non-repeatable to being repeatable.
 *
 * @author  Josh Bloch
 * @since   1.5
 */
public interface Annotation { }
```

'/**'로 시작하는 주석 안에 소스코드에 대한 설명들이 있고, 그 안에'@'이 붙은 태그들이 눈에 띌 것이다.

미리 정의된 태그들을 이용해서 주석 안에 정보를 저장하고, javadoc.exe라는 프로그램이 이 정보를 읽어서 문서를 작성하는데 사용한다.



이 기능을 응용해서 프로그램의 소스코드 안에 다른 프로그램을 위한 정보를 미리 약속된 형식으로 포함시킨 것이 바로 애너테이션이다.

> annotation의 뜻은 주석, 주해, 메모이다.



#### 애노테이션의 사용 예시

- @Test는 이 메서드를 테스트 해야 한다는 것을 테스트 프로그램에게 알리는 역할을 할 뿐, 메서드가 포함된 프로그램 자체에는 아무런 영향을 미치지 않는다.

```java
@Test // 이 메서드가 테스트 대상임을 테스트 프로그램에게 알린다.
public void method() {
	. . .
}
```



JDK에서 제공하는 표준 Annotation은 주로 컴파일러를 위한 것으로 컴파일러에게 유용한 정보를 제공한다.

그리고 새로운 애노테이션을 정의할 때 사용하는 메타 Annotation을 제공한다 

> JDK에서 제공하는 Annotation은 'java.lang.annotaion'패키지에 포함되어 있다.\



## **표준 Annotation** (표준 애노테이션, 어노테이션)
\-  **표준 Annotation** Java에서 제공하는 Annotation이다. 
\- *가 붙은 것은 Meta Annotation

| **애노테이션**       | **설 명**                                                  |
| -------------------- | ---------------------------------------------------------- |
| @Override            | 컴파일러에게 오버라이딩하는 메서드라는 것을 알린다.        |
| @Deprecated          | 앞으로 사용하지 않을 것을 권장하는 대상에 붙인다.          |
| @SuppressWarnings    | 컴파일러의 특정 경고 메시지가 나타나지 않게 해준다.        |
| @SafeVarargs         | 제네릭 타입의 가변인자에 사용한다. (JDK 1.7)               |
| @FunctionalInterface | 함수형 인터페이스라는 것을 알린다. (JDK 1.8)               |
| @Native              | native 메서드에서 참조되는 상수 앞에 붙인다.               |
| `@Target` *          | 애노테이션이 적용 가능한 대상을 지정하는데 사용한다.       |
| `@Documented` *      | 애노테이션 정보가 javadoc으로 작성된 문서에 포함되게 한다. |
| `@Inherited` *       | 애노테이션이 자손 클래스에 상속 되도록 한다.               |
| `@Retention` *       | 애노테이션이 유지되는 범위를 지정하는데 사용한다.          |
| `@Repeatable` *      | 애노테이션을 반복해서 적용할 수 있게 한다. (JDK 1.8)       |

Meta Annotation은 Annotation을 정의하는데 사용되는 Annotation이다. 



### 표준 Annotation - @Override

- 메서드 앞에만 붙일 수 있는 Annotation. 
- 오버 라이딩을 올바르게 했는지, 같은 이름의 메서드가 조상에 있는지 컴파일러가 체크하게 한다.
- 오버라이딩할 때는 메서드 선언부 앞에 @Override를 붙이자. - 실수 방지



### 표준 Annotation - @Deprecated
* 앞으로 사용하지 않을 것을 권장하는 필드나 메서드에 붙인다.
* 사용하지 말고, 대체된 클래스나 메서드로 사용하라는 의미가 있다.

```java
@Deprecated 
public int getDate() { return normalize().getDayOfMOnth(); }
```

 @Deprecated가 붙은 대상이 사용된 코드를 컴파일하면 나타나는 메시지

```java
Note : AnnotationEx2.java uses or overrides a deprecated API. Note : Recompile with -Xlint:deprecation for details.
```

* 메시지가 나타나기는 하지만 컴파일도 실행도 잘 된다. 
* 하지만 버전이 업그레이드 됨에 따라 사용되지 않을 수 있으니 대체된 클래스나 메서드를 사용하는 것이 좋다. 



### 표준 Annotation - @FunctionalInterface
* 함수형 인터페이스에 붙이면, 컴파일러가 `함수형 인터페이스를 올바르게 작성했는지 체크한다`
* 함수형 인터페이스에는 하나의 추상 메서드만 가져야 한다는 제약이 있다.

```java
@FunctionalInterface 
public interface Runnable { 
  public abstract void run(); //추상 메서드 
}
```



### 표준 Annotation - @SuppressWarnings

* @SuppressWarnings는 컴파일러의 경고 메시지가 나타나지 않게 억제한다
*  @SuppressWarnings로 억제 할 수 있는 경고 메시지 중 자주 사용되는 것은 아래와 같다.
  * ① deprecation : @Deprecated가 붙은 대상을 사용해서 발생하는 경고를 억제할 때 사용한다.
  *  ② unchecked : 제네릭으로 타입을 지정하지 않았을 때 발생하는 경고를 억제할 때 사용한다.
  * ③ rawtypes : 제네릭을 사용하지 않아서 발생하는 경고를 억제할 때 사용한다.
  * ④ varargs : 가변 인자의 타입이 제네릭 타입일 때 발생하는 경고를 억제할 때 사용한다.



```java
@SuppressWarnings("unchecked") //지네릭스와 관련된 경고를 억제 
ArrayList list = new ArrayList(); //지네릭 타입을 지정하지 않았음 
list.add(obj); //여기서 경고 발생
```

\- 둘 이상의 경고를 동시에 억제

```java
@SuppressWarnings({"deprecation", "unchecked", "varargs"}) 
// deprecation, unchecked, varargs 경고를 억제한다. 
```

\- '-Xlint' 옵션으로 컴파일하면, 경고 메시지를 확인할 수 있다.



### 표준 Annotation - @SafeVarargs
* 가변 인자의 타입이 non-reifiable (**비 구체화 타입**)인 경우 발생하는 unchecked 경고 억제
* 생성자 또는 static이나 final이 붙은 메서드에만 붙일 수이다. (오버 라이딩이 가능한 메서드에 사용 불가)
*  @SafeVarargs에 의한 경고의 억제를 위해 @SuppressWarnings를 사용

```java
@SafeVarargs //'unchecked' 경고 억제 
@SuppressWarnings("varargs") //'varargs' 경고 억제 
public static <T> List<T> asList(T... a) { 
  return new ArrayList<>(a); 
}
```



## 메타 Annotation

`메타 애노테이션은 애노테이션을 정의할 때 사용하는 애노테이션이다.`

애노테이션을 정의할 때, 애노테이션의 적용 대상(target)이나 유지 기간(retention) 등을 지정하는데 사용된다.



### **@Target**

* @Target은 애노테이션이 적용 가능한 대상을 지정하는데 사용된다.
* 여러 개의 값을 지정할 때는 배열에서 처럼 괄호 { } 를 사용해야 한다.



```java
@Target({TYPE, FIELD, METHOD, PARAMETER, CONSTRUCTOR, LOCAL_VARIABLE})
@Retention(RetentionPolicy.SOURCE)
public @interface SuppressWarnings {
    String[] value();
}
```

 

@Target으로 지정 할 수 있는 애노테이션 적용 대상의 종류는 다음과 같다.

 

| **대상 타입**       | **의미**                          |
| ------------------- | --------------------------------- |
| **ANNOTATION_TYPE** | 애노테이션                        |
| **CONSTRUCTOR**     | 생성자                            |
| **FIELD**           | 필드(멤버 변수, enum 상수)        |
| **LOCAL_VARIABLE**  | 지역변수                          |
| **METHOD**          | 메서드                            |
| **PACKAGE**         | 패키지                            |
| **PARAMETER**       | 매개변수                          |
| **TYPE**            | 타입 (클래스, 인터페이스, enum)   |
| **TYPE_PARAMETER**  | 타입 매개변수(JDK 1.8)            |
| **TYPE_USE**        | 타입이 사용되는 모든 곳 (JDK 1.8) |

* ANNOTATION_TYPE은 애노테이션을 선언할 때 붙일 수 있다는 뜻이
* CONSTRUCTOR는 생성자를 선언할 때 붙일 수 있다는 뜻이며
* TYPE_USE는 타입이 사용되는 모든 곳에 붙일 수 있다는 뜻이다.



### @Retention

* Annotation이 유지(retention)되는 기간을 지정하는 데 사용

| 유지 정책 | 의미                                             |
| --------- | ------------------------------------------------ |
| SOURCE    | 소스 파일에만 존재. 클래스파일에는 존재하지 않음 |
| CLASS     | 클래스 파일에 존재. 실행시에 사용 불가. 기본값   |
| RUNTIME   | 클래스 파일에 존재. 실행시에 사용 가능           |



컴파일러에 의해 사용되는 Annotation의 유지 정책은 SOURCE이다

*  (컴파일러를 직접 작성할 것이 아니면, 이 유지 정책은 필요 없다.)

```java
@Target(ElementType.METHOD) 
@Retention(RetentionPolicy.SOURCE) 
public @interface Override {}
```



실행 시에 사용 가능한 Annotation의 정책은 RUNTIME이다.

*   (유지 정책을 RUNTIME으로 하면, 실행 시에 리플렉션을 통해 클래스 파일에 저장된 애노테이션의 정보를 읽어서 처리할 수 있다.)

```java
@Documented 
@Retention(RetentionPolicy.RUNTIME) 
@Target(ElementType.TYPE) 
public @interface FunctionalInterfare {}
```



### **@Inherited**

* @Documented는 애노테이션에 대한 정보가 javadoc으로 작성한 문서에 포함되도록 한다. 

```java
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FunctionalInterface {}
```



### @Inherited

* @Inherited는 애노테이션이 자손 클래스에 상속 되도록 한다.
* @Inherited가 붙은 애노테이션을 조상 클래스에 붙이면, 자손 클래스도 이 애노테이션이 붙은 것과 같이 인식된다. 

```java
@Inherited // @SupperAnno가 자손까지 영향 미치게
@interface SupperAnno {}

@SuperAnno
class Parent {}

class Child extends Parent {} // Child에 애노테이션이 붙은 것으로 인식한다.
```

 

### @Repeatable

*  @Repeatable는 반복해서 붙일 수 있는 애노테이션을 정의할 때 사용한다.

* @Repeatable이 붙은 애너테이션은 반복해서 붙일 수 있다.

```java
@interface ToDos{ // 여러 개의 ToDo 애노테이션을 담을 컨테이너 애노테이션 ToDos
	ToDo[] value(); // ToDo 애노테이션 배열 타입의 요소를 선언. 이름이 반드시 value 이어야 함 
}


// ToDo 애노테이션을 여러 번 반복해서 쓸 수 있게 한다.
@Repeatable(ToDos.class) // 괄호 안에 컨테이너 애노테이션을 지정해 줘야한다.
@interface ToDo {
	String value();
}
```

아래처럼 반복해서 붙일 수 있다.

```java
@ToDo("delete test codes.")
@ToDo("override inherited methods")
class MyClass {
	...
}
```



### **@Native**

* native 메서드에 의해 참조되는 상수에 붙이는 애너테이션

```java
@Native
public static final long MIN_VALUE = 0x8000000000000000L;
```

* **네이티브 메서드는 JVM이 설치된 OS의 메서드를 말한다.**
* 네이티브 메서드는 보통 C 언어로 작성되어 있는데, 자바에서는 메서드의 선언부만 정의하고 구현은 하지 않는다.
* 자바에 정의된 네이티브 메서드와 OS의 메서드를 연결해주는 작업은 JNI(Java Native Interface)가 담당한다.



## Annotation 타입 정의하기 (Custom Annotation)



새로운 Annotation을  정의하는 방법

* Annotation 을 직접 만들어 쓸 수 있다

```java
@interface 애노테이션이름{
    타입 요소이름(); // 애노테이션의 요소를 선언한다.
    . . .
}
```

\- Annotation의 메서드는 추상 메서드이며, Annotation 을 적용할 때 모두 지정해야 한다. (순서 상관없음) 

### Annotation의 요소

* 애노테이션의 요소(element)는 애노테이션 내에 선언된 메서드를 말한다.

 

아래에 선언된 TestInfo 애노테이션은 다섯 개의 요소를 갖는다.

```java
@interface TestInfo {
	int       count();
	String    testedBy();
	String[]  testTools(); // 추상 메서드의 형태
	TestType  testType(); // enum TestType { FIRST , FINAL }
	DateTime  testDate(); // 자신이 아닌 다른 애노테이션(@DateTime)을 포함 할 수 있다.
}

@interface DateTime {
	String yymmdd(); // 추상 메서드의 형태
	String hhmmss(); // 추상 메서드의 형태
}
```

**애노테이션의 요소는 반환 값이 있고, 매개 변수는 없는 추상 메서드의 형태를 가진다.**

 **그리고 애노테이션을 적용할 때 이 요소들의 값을 모두 지정 해야 한다.**

 (요소의 이름도 같이 적어주므로 순서는 상관 없다.)



### **Annotation 요소의 기본값**
* 애노테이션을 적용 시 값을 지정하지 않으면, 사용 될 수 있는 요소의 기본 값을 지정 할 수 있다. 
  * `default` 키워드
  * (기본 값으로 null은 사용 불가)

```java
@interface TestInfo { 
  int count() default 1; //기본값을 1로 지정 
} 

@TestInfo //@TestInfo(count=1) 과 동일 
public class NewClass {}
```

\- 애노테이션의 요소가 하나이고 이름이 value인 경우,  애노테이션을 적용할 때 요소의 이름을 생략 하고 값만 적어도 된다.

```java
@TestInfo(5) // @TestInfo(value=5)와 동일 
public class NewClass {}
```

\- **요소의 타입이 배열인 경우, 중괄호 {}를 사용해서 여러 개의 값을 지정할 수 있다.**

```java
@interface TestInfo {
    String[] testTools();
}

@TestInfo(testTools = {"JUnit", "AutoTester"}) // 값이 여러 개 인 경우
@TestInfo(testTools = "JUnit") // 값이 하나일 때는 괄호 {} 생략 가능
@TestInfo(testTools = {}) // 값이 없을 때는 괄호 {}가 반드시 필요
class NewClass { ... }
```

**- 기본 값을 지정할 때도 괄호{}를 사용할 수 있다.**

 ```java
 @interface TestInfo {
     String[] info() default {"aaa", "bbb"}; // 기본 값이 여러 개인 경우, 괄호 {} 사용
     String[] info2() default "ccc"; // 기본 값이 하나 인 경우, 괄호 생략 가능
 }
 
 @TestInfo // @TestInfo (info={"aaa", "bbb"}, info2="ccc")와 동일 
 @TestInfo(info2={}) // @TestInfo (info={"aaa", "bbb"}, info2={})와 동일 
 class NewClass { ... }
 ```



###  모든 애너테이션의 조상 - java.lang.annotation.Annotation

Annotation은 모든 애노테이션의 조상이지만 상속은 불가능하다. 

```java
@interface TestInfo extends Annotation { // 에러. 허용되지 않는 표현
    int count();
    String testedBy();
    . . .
}
```

 

> 사실, Annotation은 인터페이스로 정의되어 있다.



```java
package java.lang.annotation;

public interface Annotation { // Annotation자신은 인터페이스다.
		boolean equals(Object obj);
    int hashCode(0);
    String toString();
    
    Class<? extends Annotation> annotationType(); // 애너테이션의 타입을 반환한다.
}
```



## 마커 애노테이션 (Marker Anntation)

요소가 하나도 정의되지 않은 Anntation

```java
@Target(ElementType.METHOD) 
@Retention(RetentionPolicy.SOURCE) 
public @interface Override {} //마커 애너테이션. 정의된 요소가 하나도 없다. @Target(ElementType.METHOD) 


@Retention(RetentionPolicy.SOURCE) 
public @interface Test {} //마커 애너테이션. 정의된 요소가 하나도 없다.
```



## **애너테이션 요소의 규칙**

\- 애너테이션의 요소를 선언할 때 아래의 규칙을 반드시 지켜야 한다.
\- 요소의 타입은 기본형, String, enum, 애너테이션, Class만 허용됨
\- 괄호() 안에 매개변수를 선언할 수 없다.
\- 예외를 선언할 수 없다.
\- 요소를 타입 매개변수로 정의할 수 없다.



```java
@interface AnnTest {
    int id = 100;                    // OK. 상수 선언. static final int id = 100;
    String major(int i, int j);      // 에러. 매개변수를 선언할 수 없음
    String minor() Throws Exception; // 에러. 예외를 선언할 수 없음
    ArrayList<T> list();             // 에러. 요소의 타입에 타입 매개변수 사용 불가
}
```





애너테이션을 직접 정의하고, 애너테이션의 요소의 값을 출력하는 방법을 보여주는 예제다.
AnnotationEx5 클래스에 적용된 애너테이션을 실행시간에 얻으려면, 아래와 같이 하면 된다.

```java
import java.lang.annotation.*;

@Deprecated
@SuppressWarnings("1111") // 유효하지 않은 애너테이션은 무시된다.
@TestInfo(testedBy="aaa", testDate=@DateTime(yymmdd="160101", hhmmss="235959"))
class AnnotationsEx6 {
	
  public static void main(String args[]) {
    	  // AnnotationEx5의 Class객체를 얻는다.
        Class<AnnotationEx5> cls = AnnotationEx5. class;
        
        TestInfo anno = (TestInfo)cls.getAnnotation(TestInfo.class);
        System.out.println("anno.testedBy()="+anno.testedBy());
        System.out.println("anno.testDate().yymmdd()="+anno.testDate().yymmdd());
        System.out.println("anoo.testDate().hhmmss()="+anno.testDate().hhmmss());
        
        for(String str : anno.testTools())
        	System.out.println("testTools="+str);
            
        System.out.println();
        // AnnotationEx5에 적용된 모든 애너테이션을 가져온다.
        Annotation[] annoArr = cls. getAnnotations();
        
        for(Annotaion a : annoArr)
        	System.out.println(a);
    } 
  // main의 끝
}

@REtention(RetentionPolicy.RUNTIME) // 실행 시에 사용가능하도록 지정한다.
@interface TestINfo {
    int		count() 	default 1;
    String	testedBy();
    String[]	testTools() 	default "JUnit";
    TestType	testType()	default TestType.FIRST;
    DateTiime	testDate();
}

@Retention (RetentionPolicy.RUNTIME) // 실행 시에 사용가능하도록 지정한다.
@interface DateTime {
	String yymmdd();
    String hhmmss();
}

enum TestType {FIRST, FINAL}
```

> **실행결과**
> anno.testedBy()=aaa
> anno.testDate().yymmdd()=160101
> anno.testDate().hhmmss()=235959
> testTools=JUnit


AnnotationEx5 클래스에 적용된 애너테이션을 실행시간에 얻으려면, 아래와 같이 하면 된다.

```java
Class<AnnotationEx5> cls = AnnotationEx5.class;
TestInfo anno = (TestInfo)cls.getAnnotation(TestInfo.class);
```

'AnnotationEx5.class'는 클래스 객체를 의미하는 리터럴이다.

모든 클래스 파일은 클래스로더(Classloader)에 의해서 메모리에 올라갈 때, 클래스에 대한 정보가 담긴 객체를 생성하는데 이 객체를 클래스 객체라고 한다.

* 이 객체를 참조할 때는 '클래스이름.class'의 형식을 사용한다.

클래스 객체에는 해당 클래스에 대한 모든 정보를 갖고 있는데, `애너테이션의 정보도 포함되어 있다.`
클래스 객체가 갖고 있는 getAnnotation()이라는 메서드에 매개변수로 정보를 얻고자 하는 애너테잇녀을 지정해주거나
getAnnotation()로 모든 애너테이션을 배열로 받아올 수 있다.

```java
TestInfo anno = (TestInfo)cls.getAnnotation(TestInfo.class);

System.out.println("anno.testedBy()=" + anno.testedBy());

// AnnotationEx5에 적용된 모든 애너테이션을 가져온다.
Annotation[] annoArr = cls.getAnnotation();
```



> Class클래스를 Java API에서 찾아보면 클래스의 정보를 제공하는 다양한 메서드가 정의되어 있는 것을 확인할 수 있다.



### 참조

* 자바의 정석