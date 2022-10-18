# 목표
자바의 패키지에 대해 학습하세요.

# 학습할 것 (필수)
* package 키워드
* import 키워드
* 클래스패스
* CLASSPATH 환경변수
* -classpath 옵션
* 접근지시자


# package 키워드

패키지 (package)

패키지란 많은 클래스들을 체계적으로 관리하기 위해 존재한다.

패키지의 물리적인 형태는 "파일 시스템의 폴더" 이다.

또한, 파일 시스템의 폴더 기능만이 아니라 `클래스의 일부분`이다.


* 소스에 가장 첫 줄에 있어야하고, 패키지 선언은 소스 하나에 하나만 있어야한다.

* 패키지 이름과 위치한 폴더의 이름이 같아야한다.
* 패키지이름을 java 로 시작하면 안된다.


* 패키지는 폴더 또는 디렉토리로서, 우리는 기능이나 특징이 비슷한 클래스를 한대모아 하나의 패키지 않에 넣는다. 

* 서로 관련된 있는 것을 묶어놓음으로써 효율적으로 관리할 수 있으며 클래스 풀네임(FQCN)의 고유성을 보장하기 위해서 사용함


### FQCN(Fully Qualified Class Name) 
* 클래스가 속한 패키지명까지 모두 포함한 이름을 의미(패키지명.클래스명)
* 모든 클래스에는 정의된 클래스 이름과 패키지 이름이 있다.  
* 이 둘을 합쳐야 완전하게 한 클래스를 표현한다고 할 수 있다
    * 예를 들어 String 클래스의 패키지는 java.lang이며 FQCN은 java.lang.String

### 선언

패키지는 클래스를 컴파일 하는 과정에서 자동적으로 생성되는 폴더이다.

컴파일러는 클래스에 포함되어 있는 패키지 선언을 보고, 파일 시스템의 폴더로 자동 생성 시킨다.

```java
package 상위패키지.하위패키지;
public class ClassName{ ... }
```

패키지 이름은 개발자 임의로 정의해도 되지만, 지켜야할 규칙이 있다.

- 숫자로 시작해서 안된다.
- _, $ 를 제외한 특수문자를 사용해서는 안된다.
- java로 시작하는 패키지는 자바 표준 api 에서만 사용하므로 사용해서는 안된다.
- 모두 소문자로 작성하는 것이 관례이다.

패키지가 서로 중복되지 않도록 회사의 도메인 이름으로 패키지를 만든다.

도메인 이름으로 패키지 이름을 만들 경우, 도메인 이름 역순으로 패키지 이름을 지어준다.

* 포괄적인 이름이 상위 패키지가 되도록 하기 위함


마지막에는 프로젝트 이름을 붙여주는 것이 관례이다.

```
com.samsung.projectname
com.hyundai.proejctname
com.lg.projectname
com.ys.projectname
```


### 패키지 이름 지정 규칙
|패키지| 시작이름|	내용|
|---|---|---|
|java|	자바 기본 패키지(Java vendor 개발)|
|javax|	자바 확장 패키지(Java vendor 개발)|
|org|	일반적으로 비영리단체 (오픈소스) 패키지| 
|com|	일반적으로 영리단체(회사) 패키지|

### 패키지 이름 명명 규칙
* 패키지 이름은 모두 소문자여야한다.
* 자바의 예약어를 사용하면 안된다. (예, int, static)
* 개발 패키지 표준은 정하는 것에 따라 지정하면 된다.

### 네임스페이스(Namespace)

네임스페이스란 이름을 구분 할 수 있게 해주는 공간을 의미한다. 

* /Users/dir1 과 /Users/dir2를 어떻게 구분할까?

* 디렉토리가 바로 공간이고, 자바에서는 패키지(Package)가 바로 그 공간이다.
* 이러한 특성으로 패키지는 이름 중복으로 인한 문제를 방지할 수 있다.
* 패키지 안의 클래스 이름은 고유해야 한다. 
    * 다시말해, 하나의 패키지 안에는 같은 이름의 두개의 클래스를 만들 수 없다.


* 패키지가 다르다면 상관없다.

### 빌트-인 패키지(Built-in Package)

패키지는 크게 2개로 구분할 수 있다.
1. 사용자 정의 패키지
2. 빌트-인 패키지

자바는 개발자들이 사용할 수 있도록 여러 많은 패키지 및 클래스를 제공한다.

가장 자주 쓰이는 패키지로는 **java.lang**과 **java.util**이 있다.

java.lang은 자주 사용하는 패키지이지만 한번도 **import**하여 사용한적이 없다.

**즉, 자바에서 java.lang 패키지는 아주 기본적인 것들이기 때문에 import로 불러오지 않아도 자바가 알아서 java.lang의 클래스를 불러온다.**

예) String, System

```java
import java.lang.String;
import java.lang.System;

public class Main{
	public static void main(String[] args){
		String str = this is from java.lang.String";
		System.out.println(str);
	}
}
```

- **java.lang** : language suppoart 클래스들을 포함사는 패키지
    - 프리미티브 타입이나 수학 연산의 정의가 되는 클래스들
    - 자동으로 import되기 때문에 해당 패키지의 클래스를 바로 사용할 수 있다.
- [**java.io](http://java.io)** : 입출력 기능을 지원하는 클래스들을 포함하는 패키지

밑의 패키지들은 자동으로 포함되지 않는다. 
- **java.util** : 자료구조 구현을 위한 유틸리티 클래스를 포함하는 패키지
- **java.applet** : Applets을 생성하기 위한 클래스들을 포함하는 패키지
- **java.awt** : GUI 컴포넌트를 구현하기 위한 클래스들을 포함하는 패키지
- [**java.net](http://java.net)** : 네트워킹 기능을 지원하기 위한 클래스를 포함하는 패키지


# import 키워드
```java
import 패키지명.클래스명;
```
다른 패키지에 속하는 클래스를 사용할 때 사용하는 키워드

1. **패키지와 클래스를 모두 기술하는 방법**
    
    ex) "com.hankook" 패키지에 소속된 Tire 클래스를 이용해서 필드 선언 후 객체 생성
    
    ```java
    package com.mycompany;
    
    public class Car{
    	com.hankook.Tire tire = new com.hankook.Tire(); // 객체생성
    }
    ```
    
    - com.hankok.Tire : 타입
    - tire : 필드명
    - new com.hankook.Tire(); : 객체생성
    
    → 패키지 이름이 짧을 경우 불편함이 없지만, 길거나 이렇게 사용해야할 클래스가 많다면 전체적으로 코드의 가독성이 떨어진다.
    
2. **import 문 사용** 
    
    사용하고자 하는 패키지를 import 문으로 선언하고, 클래스를 사용할 때는 패키지를 생략한다.
    
    ```java
    package com.mycompany;
    
    import com.hankook.Tire; // 혹은, import com.hankook.*;
    
    public class Car{
    	Tire tire = new Tire();
    }
    ```
    
    - import 문 작성 위치 : 패키지 선언과 클래스 선언 사이에 선언된다.
    - 패키지에 포함된 다수 클래스를 사용해야 한다면, 모든 클래스를 의미하는 **[*]** 기호를 사용한다.
    - import 문의 개수 제한은 없다.
    
---
**주의점**

**import문으로 지정된 패키지의 하위 패키지는 import 대상이 아니다.**

만약 하위 패키지의 클래스를 이용하고 싶다면 import 문을 하나 더 작성해야 한다.

**예)**

com.mycompany 패키지의 클래스 + com.mycompany.project 패키지의 클래스를 둘다 사용하려면 각각 import문을 사용해야 한다.

```java
import com.mycompany.*;
import com.mycompany.project.*;
```
---


다른 패키지에 속하는 클래스를 사용하는 두가지 방법 중 **"패키지와 클래스를 모두 기술하는 방법"**이 필요한 경우?

→ 서로 다른 패키지에 동일한 클래스 이름이 존재하고, 

→ 두 패키지가 모두 import 되어 있는 경우.

자바 컴파일러는 어떤 패키지에서 클래스를 로딩할 지 결정할 수 없어서 컴파일 에러가 발생한다.

이 경우에는 **정확한 패키지 이름 전체**를 기술해야 한다.


## static import

**static import**는 일반적인 import와 다르게 메소드나 변수를 패키지, 클래스명 없이 접근가능하게 해준다.

```java
import static java.lang.System.*;

.. 중략
out.println("TEXT");
```

→ 여기서 import문의 static을 빼면? Compile Error 발생 😂

굳이 이렇게 까지 줄일 필요가 있을까? 

> static import를 사용하면서 매우 편리해 진다. 


특히나, 테스트 프레임워크인 JUnit을 사용하다보면 static import의 다양한 예를 살펴볼 수 있다.

```java
@Test
public void nonStaticImport(){
	Assert.assertThat(1, CoreMatchers.is(1));
}

@Test
public void staticImport(){
	assertThat(1, is(1));
}
```

* 위 테스트는 static import를 적용하지 않은 경우이고, 아래는 static import를 적용한 예시이다.

* static import를 사용하게 되면 코드를 읽을 때, That ~ is ~ 로만 읽어서 의도를 쉽게 파악할 수 있다.

* 이 테스트는 That is 1 이 참인지 거짓인지를 판단하는 것이다. 여기서 That은 1이다.

* 즉, 1은 1이다.는 것을 영어 문장을 읽듯이 코드를 읽을 수 있다.

* 만약 static import를 하지 않는다면 테스트의 의도가 한눈에 들어오지 않을 것이다.

### Plus **Constant Interface** - 상수를 모아놓은 인터페이스 에 대한 ANTI Pattern

Constants.java

```java
public interface Constants{
	int NUMBER = 1000;
	String NAME = "SSON";
}
```

아래와 같이 위 Constants Interface 의 정보를 상수처럼 사용할 수 있다.

```java
public static void main(String[] args){
	System.out.println(Constants.NAME);
}
```

인터페이스에서 선언한 것들은 기본적으로 **"public static final"** 이다. 

* 이렇게 사용하는 것은 ANTI Pattern 


* 아래와 같이 Constants 인터페이스를 implements 하면서 정의한 상수들이 쉐도윙될 수 있다.
*  인터페이스를 정의하는 목적에서 벗어나는 행위이다.

```java
public class MyBook implements Constants{
	private static final String NAME = "myBook";
	public static void main(String[] args){
		System.out.println(MyBook.NAME);
	}
}

// 결과?
-> myBook
```

* 어떠한 목적으로 상수들을 모아놓고 사용하고자 한다면 아래처럼 사용한다.

* **생성자를 private로 선언**하여 인스턴스화 할 수 없도록 방지시킬 수 있다.

```java
public class Constants{
	public static final int NUMBER = 1000;
	public static final String NAME = "SSON";

	private Constants(){
	}
}
```


# 클래스패스 (classPath)
- **CLASSPATH 환경변수**
- **classpath 옵션**

* 컴파일러나 JVM이 클래스의 위치를 찾을 때 사용하는 경로
* 지정해주지 않으면 기본적으로 현재 디렉터리가 클래스 패스로 지정됨

* 자바가 클래스를 찾아 사용을 해야하는데, **클래스들이 어디 있는지 위치를 지정해주는 값.**

* 지정하는 방법
    * 클래스 패스 환경변수
      * 시스템에서 어디서든 참조 할 수 있게 환경변수로 설정
    * -classpath 옵션
        * cmd에서 자바 컴파일 또는 실행시 명령어을 통해 임시로 지정 할 수 있음

```
명령어 -classpath 클래스패스 경로
명령어 -cp 클래스패스 경로


javac -classpath D:\java\jdk1.8.0_101 Test.java
```

**classpath 옵션은 java와 javac 명령어에 모두 사용할 수 있다.**

# 접근지시자

접근제어자는 클래스, 메소드, 인스턴스 및 클래스 변수를 선언할 때, 사용된다.

자바에서 사용하는 접근지시자는 4가지이다.

* **public**
    * 모든 패키지에서 아무런 제한 없이 호출 할 수 있게 한다.
    * 보통 생성자 또는 필드, 메소드가 public이라면, 클래스도 public 접근 제한을 가진다.

* **protected**
    * default 접근 제한과 마찬가지로 같은 패키지에 속하는 클래스에서 호출할 수 있도록 한다.
    * 차이점은 다른 패키지에 속한 클래스가 해당 클래스의 자식(child) 클래스라면 호출 할 수 있다.

* **default**
    * 접근 제한자를 생략했다면 default 접근제한을 가진다.
    * 같은 패키지에서는 제한 없이 호출할 수 있으나, 다른 패키지에서는 호출할 수 없다.

* **private**
    * 오로지 클래스 내부에서만 사용할 수 있다.

||해당 클래스 내	|같은 패키지 내|	상속받은 클래스|	import 한 클래스|
|---|---|---|---|---|
|public	        |   O|	O|	O|	O|
|protected      |	O|	O|	O|	X|
|default|	O|	O|	X|	X|
|private        |	O|	X|	X|	X|





