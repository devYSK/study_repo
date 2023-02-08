# 더 자바, “코드를 조작하는 다양한 방법

인프런 더 자바, 코드를 조작하는 다양한 방법 을 학습하고 정리한 글 입니다.



> 자바 개발자라면 한 번쯤은 사용해보거나 들어봤을 스프링, 스프링 데이터 JPA,
> 하이버네이트, 롬복 등의 기반이 되는 자바 기술에 대해 학습합니다
>
> 이 강좌는 자바가 제공하는 기술 중에 소스 코드, 바이트 코드 그리고 객체를 조작하는 기술에
> 대해 학습합니다. 그러려면 우선 JVM의 기본적인 구조와 클래스로더의 동작 방식에 대해
> 이해하는 것이 좋습니다. 따라서 이번 강좌는 "JVM", "바이트코드 조작", "리플렉션", "다이나믹
> 프록시 기법" 그리고 "애노테이션 프로세서"에 대해서 학습합니다. 따라서, 자바 기초 학습
> 이후에 어떤 것을 학습하면 좋을지 고민이었던 분들께 추천합니다.
> 이번 강좌를 학습하고 나면 여러분은 한층 더 자바에 대해 깊이 있는 지식을 습득할 수 있으며
> 자바를 둘러싼 여러 기술을 학습할 때에도 더 쉽게 이해할 수 있을 것으로 기대합니다. 또한 이
> 강좌에서 학습한 기술에서 파생해서 GC, 서비스 프로바이더, 프로파일러 등 보다 다양한 자바
> 기술에도 관심을 두는 계기가 되길 바랍니다.



### JVM 이해하기

[자바, JVM, JDK 그리고 JRE](https://www.inflearn.com/course/the-java-code-manipulation/unit/23413)

[JVM 구조](https://www.inflearn.com/course/the-java-code-manipulation/unit/23414)

[클래스 로더](https://www.inflearn.com/course/the-java-code-manipulation/unit/23415)

### 바이트코드 조작

[코드 커버리지는 어떻게 측정할까?](https://www.inflearn.com/course/the-java-code-manipulation/unit/23417)

[모자에서 토끼를 꺼내는 마술](https://www.inflearn.com/course/the-java-code-manipulation/unit/23418)

[javaagent 실습](https://www.inflearn.com/course/the-java-code-manipulation/unit/23419)

[바이트코드 조작 정리](https://www.inflearn.com/course/the-java-code-manipulation/unit/23420)

### 리플렉션

[스프링 Dependency Injection은 어떻게 동작할까?](https://www.inflearn.com/course/the-java-code-manipulation/unit/23422)

[리플렉션 API 1부: 클래스 정보 조회](https://www.inflearn.com/course/the-java-code-manipulation/unit/23423)

[애노테이션과 리플렉션](https://www.inflearn.com/course/the-java-code-manipulation/unit/23424)

[리플렉션 API 2부: 클래스 정보 수정 또는 실행](https://www.inflearn.com/course/the-java-code-manipulation/unit/23425)

[나만의 DI 프레임워크 만들기](https://www.inflearn.com/course/the-java-code-manipulation/unit/23426)

[리플렉션 정리](https://www.inflearn.com/course/the-java-code-manipulation/unit/23427)

### 다이나믹 프록시

[스프링 데이터 JPA는 어떻게 동작할까?](https://www.inflearn.com/course/the-java-code-manipulation/unit/23429)

[프록시 패턴](https://www.inflearn.com/course/the-java-code-manipulation/unit/23430)

[다이나믹 프록시 실습](https://www.inflearn.com/course/the-java-code-manipulation/unit/23431)

[클래스의 프록시가 필요하다면?](https://www.inflearn.com/course/the-java-code-manipulation/unit/23432)

[다이나믹 프록시 정리](https://www.inflearn.com/course/the-java-code-manipulation/unit/23433)

### 애노테이션 프로세서

[롬복(ProjectLombok)은 어떻게 동작할까?](https://www.inflearn.com/course/the-java-code-manipulation/unit/23435)

[애노테이션 프로세서 실습 1부](https://www.inflearn.com/course/the-java-code-manipulation/unit/23436)

[애노테이션 프로세서 실습 2부](https://www.inflearn.com/course/the-java-code-manipulation/unit/23437)

[애노테이션 프로세서 정리](https://www.inflearn.com/course/the-java-code-manipulation/unit/23438)

[마무리](https://www.inflearn.com/course/the-java-code-manipulation/unit/23440)



# JVM 이해하기



## 1. 자바, JVM, JDK, JRE

![image-20230105132804458](/Users/ysk/study/study_repo/inf-thejava-code-whiteship/images//image-20230105132804458.png)

JVM (Java Virtual Machine)

* 자바 가상 머신으로 자바 바이트 코드(.class 파일)를 OS에 특화된 코드로 변환(인터프리터와 JIT 컴파일러)하여 실행한다.
  * 바이트 코드는 빌드된 후 .class파일 내에 들어있다. 
* 바이트 코드를 실행하는 표준(JVM 자체는 표준)이자 구현체(특정 밴더가 구현한JVM)다.
* JVM 스팩: https://docs.oracle.com/javase/specs/jvms/se11/html/
* JVM 밴더: 오라클, 아마존, Azul, ...
* 특정 플랫폼에 종속적



JRE (Java Runtime Environment): JVM + 라이브러리 

* 자바 애플리케이션을 실행할 수 있도록 구성된 배포판.
* JVM과 핵심 라이브러리 및 자바 런타임 환경에서 사용하는 프로퍼티 세팅이나 리소스 파일을 가지고 있다.
* 개발 관련 도구는 포함하지 않는다. (그건 JDK에서 제공)



JDK (Java Development Kit): JRE + 개발 툴

* JRE + 개발에 필요할 툴

* 소스 코드를 작성할 때 사용하는 자바 언어는 플랫폼에 독립적.
*  오라클은 자바 11부터는 JDK만 제공하며 JRE를 따로 제공하지 않는다.
*  Write Once Run Anywhere



자바

* 프로그래밍 언어
* JDK에 들어있는 자바 컴파일러(javac)를 사용하여 바이트코드(.class 파일)로 컴파일 할 수 있다.
*  자바 유료화? 오라클에서 만든 Oracle JDK만 11 버전부터 상용으로 사용할 때 유료.
  * https://medium.com/@javachampions/java-is-still-free-c02aef8c9e04



JVM 언어

* JVM 기반으로 동작하는 프로그래밍 언어
* 클로저, 그루비, JRuby, Jython, Kotlin, Scala, ...



참고

* JIT 컴파일러: https://aboullaite.me/understanding-jit-compiler-just-in-time-compiler/
  * JDK, JRE 그리고 JVM: https://howtodoinjava.com/java/basics/jdk-jre-jvm/
  *  https://en.wikipedia.org/wiki/List_of_JVM_languages



## JVM 구조

![image-20230105134731723](/Users/ysk/study/study_repo/inf-thejava-code-whiteship/images//image-20230105134731723.png)

클래스 로더 시스템

* .class 에서 바이트코드를 읽고 메모리에 저장

* 로딩: 클래스 읽어오는 과정
* 링크: 레퍼런스를 연결하는 과정
* 초기화: static 값들 초기화 및 변수에 할당

메모리

* 메소드 영역에는 클래스 수준의 정보 (클래스 이름, 부모 클래스 이름, 메소드, 변수) 저장. 공유 자원이다.
* 힙 영역에는 객체를 저장. 공유 자원이다.
* 스택 영역에는 쓰레드 마다 런타임 스택을 만들고, 그 안에 메소드 호출을 스택프레임(메소드 콜)이라 부르는 블럭으로 쌓는다. 쓰레드 종료하면 런타임 스택도 사라진다.
*  PC(Program Counter) 레지스터: 쓰레드 마다 쓰레드 내 현재 실행할 instruction의 위치를 가리키는 포인터가 생성된다.
*  네이티브 메소드 스택

* https://javapapers.com/core-java/java-jvm-run-time-data-areas/#Program_Counter_PC_Register



실행 엔진

* 인터프리터: 바이트 코드를 한줄 씩 실행.
* JIT 컴파일러: 인터프리터 효율을 높이기 위해, 인터프리터가 반복되는 코드를 발견하면 JIT 컴파일러로 반복되는 코드를 모두 네이티브 코드로 바꿔둔다. 그 다음부터 인터프리터는 네이티브 코드로 컴파일된 코드를 바로 사용한다.
* GC(Garbage Collector): 더이상 참조되지 않는 객체를 모아서 정리한다.
  * 각 상황마다 맞는 GC의 종류를 사용하는것이 좋다. 



JNI(Java Native Interface)

* 자바 애플리케이션에서 C, C++, 어셈블리로 작성된 함수를 사용할 수 있는 방법 제공

* Native 키워드를 사용한 메소드 호출

* https://medium.com/@bschlining/a-simple-java-native-interface-jni-example-in-java-andscala-68fdafe76f5f

네이티브 메소드 라이브러리

* C, C++로 작성 된 라이브러리



참고

* https://www.geeksforgeeks.org/jvm-works-jvm-architecture/
* https://dzone.com/articles/jvm-architecture-explained
* http://blog.jamesdbloom.com/JVMInternals.html



## 클래스 로더

![image-20230105141409495](/Users/ysk/study/study_repo/inf-thejava-code-whiteship/images//image-20230105141409495.png)

클래스 로더

* 로딩, 링크, 초기화 순으로 진행된다.
* 로딩
  * 클래스 로더가 .class 파일을 읽고 그 내용에 따라 적절한 바이너리 데이터를 만들고 “메소드” 영역에 저장.
  * 이때 메소드 영역에 저장하는 데이터
    * FQCN -Fully Qualified Class Name - 클래스가 속한 패키지명을 모두 포함한 이름
    * class,  interface, enum
    * 메소드와 변수 
  * 로딩이 끝나면 해당 클래스 타입의 Class 객체를 생성하여 “힙" 영역에 저장.
    * `Class<Any>` 타입을 클래스명.class나 객체.getClass()를 통해 얻을 수 있다. 
* 링크
  * Verify, Prepare, Reolve(optional) 세 단계로 나눠져 있다.
  * 검증(Verify): .class 파일 형식이 유효한지 체크한다.
  * Preparation: 클래스 변수(static 변수)와 기본값에 필요한 메모리 - 메모리를 준비해두는 과정
  * Resolve: 심볼릭 메모리 레퍼런스를 메소드 영역에 있는 실제 레퍼런스로 교체한다. 
    * 옵셔널이다. 교체할수도있고 안할수도있다.
    * 심볼릭레퍼런스 : 어떤 객체가 다른 객체를 레퍼런스할 때 링크 과정에서는 실제 레퍼런스가 아닌 심볼릭 레퍼런스를(논리적인 레퍼런스)를 가르키고 있다. Resolve과정에서 실제 레퍼런스를 가리킬수도있고 아닐수도있다.  
    * static들은 Resolve시에 다 할당이된다. 
* 초기화
  * Static 변수의 값을 할당한다. (static 블럭이 있다면 이때 실행된다.)
* 클래스 로더는 계층 구조로 이뤄져 있으면 기본적으로 세가지 클래스 로더가 제공된다.
  * 부트 스트랩 클래스 로더 - JAVA_HOME\lib에 있는 코어 자바 API를 제공한다. 최상위 우선순위를 가진 클래스 로더
  * 플랫폼 클래스 로더(과거에는 extension 클래스 로더) - JAVA_HOME\lib\ext 폴더 또는 java.ext.dirs 시스템 변수에 해당하는 위치에 있는 클래스를 읽는다.
  * 애플리케이션 클래스 로더 - 애플리케이션 클래스패스(애플리케이션 실행할 때 주는 -classpath 옵션 또는 java.class.path 환경 변수의 값에 해당하는 위치)에서클래스를 읽는다

```java
public class App {

    public static void main(String[] args) {
        ClassLoader classLoader = App.class.getClassLoader();
        System.out.println(classLoader);
        System.out.println(classLoader.getParent());
        System.out.println(classLoader.getParent().getParent());
    }
}
```

> 결과
>
> jdk.internal.loader.ClassLoaders$AppClassLoader@ed17bee
> jdk.internal.loader.ClassLoaders$PlatformClassLoader@531be3c5
> null

App이라는 클래스를 읽은 클래스 로더는 AppClassLoader(ApplicationClassLoader)이다. 99퍼센트 모든 클래스 읽는다. 

AppClassLoader의 부모는 PlatformClassLoader이다.

PlatformClassLoader의 부모는 읽은수는 없지만(null), 플랫폼 클래스 로더이고 네이티브 코드로 구현되어있기 때문에 읽을 수 없다.



### 클래스로더 동작 원리

어떤 클래스를 읽을라고 부모가 못읽으면 조상이읽고 , 조상도 못읽으면 본인이 읽고, 본인도 못읽으면 classNotFoundException이 발생한다.





# 바이트코드 조작

소스파일 없이 바이트코드를 조작할 수 있다. 



## 코드 커버리지는 어떻게 측정할까?

코드 커버리지? 테스트 코드가 확인한 소스 코드를 %

* JaCoCo를 써보자.
* https://www.eclemma.org/jacoco/trunk/doc/index.html
* http://www.semdesigns.com/Company/Publications/TestCoverage.pdf
  * 동작원리 논문이다. 읽어보는게 좋다.

간단하게 동작원리를 설명하면, 바이트코드(빌드된 후 )를 읽어서 코드커버리지를 챙겨야 하는 부분을 갯수를 센 다음에 코드가 실행된 다음에 몇개가 지나갔는지 카운팅을하고, 어디를 테스트하고 어디를 테스트하지 않았는지 테스트해서 비교해서 알려주는것.



## 모자에서 토끼를 꺼내는 마술 - 바이트코드 조작

바이트코드 조작 라이브러리

* asm : https://asm.ow2.io/
* javassist: https://www.javassist.org/

* bytebuddy : https://bytebuddy.net/#/



bytebuddy가장 사용하기 쉽고 성능이 좋다. 



* 의존성 추가

```groovy
// https://mvnrepository.com/artifact/net.bytebuddy/byte-buddy
implementation 'net.bytebuddy:byte-buddy:1.12.20'
```

```java
try {
  new ByteBuddy().redefine(Moja.class)
    .method(named("pullOut")).intercept(FixedValue.value("Rabbit!"))    
    .make().saveIn(new File(
    "/Users/ysk/study/board/build/classes/")); // build 결과물 조작
} catch (IOException e) {
  throw new RuntimeException(e);  
}
```



## javaagent 실습

javaagent JAR 파일 만들기

* https://docs.oracle.com/javase/8/docs/api /java/lang/instrument/package-summary.html
* 붙이는 방식은 시작시 붙이는 방식 premain과 런타임 중에 동적으로 붙이는 방식 agentmain이 있다.
* Instrumentation을 사용한다.



Javaagent 붙여서 사용하기

* 클래스로더가 클래스를 읽어올 때 javaagent를 거쳐서 변경된 바이트코드를 읽어들여
  사용한다.



## 바이트코드 조작 정리

프로그램 분석

* 코드에서 버그 찾는 툴
*  코드 복잡도 계산

클래스 파일 생성

* 프록시
*  특정 API 호출 접근  제한
*  스칼라 같은 언어의 컴파일러

그밖에도 자바 소스 코드 건리지 않고 코드 변경이 필요한 여러 경우에 사용할 수 있다.

*  프로파일러 (newrelic)
*  최적화
*  로깅
*  ...



스프링이 컴포넌트 스캔을 하는 방법 (asm)

* 컴포넌트 스캔으로 빈으로 등록할 후보 클래스 정보를 찾는데 사용
* ClassPathScanningCandidateComponentProvider -> SimpleMetadataReader
* ClassReader와 Visitor 사용해서 클래스에 있는 메타 정보를 읽어온다.

참고
● https://www.youtube.com/watch?v=39kdr1mNZ_s
● ASM, Javassist, ByteBuddy, CGlib



# 리플렉션



## 스프링의 Depedency Injection은 어떻게 동작할까?

```java
@Service
public class BookService {
	@Autowired
	BookRepository bookRepository;
}
```

* bookRepository 인스턴스는 어떻게 null이 아닌걸까?
* 스프링은 어떻게 BookService 인스턴스에 BookRepository 인스턴스를 넣어준 것일까?



## 9.리플렉션 API 1부: 클래스 정보 조회
리플렉션의 시작은 Class<T>

 https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html



> ## Reflection이란?
>
> 리플렉션은 힙 영역에 로드된 Class 타입의 객체를 통해, 원하는 클래스의 인스턴스를 생성할 수 있도록 지원하고, 인스턴스의 필드와 메소드를 접근 제어자와 상관 없이 사용할 수 있도록 지원하는 API이다.
>
> 여기서 로드된 클래스라고 함은, JVM의 클래스 로더에서 클래스 파일에 대한 로딩을 완료한 후, 해당 클래스의 정보를 담은 **Class 타입의 객체**를 생성하여 메모리의 힙 영역에 저장해 둔 것을 의미한다. new 키워드를 통해 만드는 객체와는 다른 것임을 유의하자. 만약 해당 Class 타입의 객체에 이해가 부족하다면 `java.lang.class` 객체의 JDK 문서를 확인하면 좋다.



Class<T>에 접근하는 방법

* 모든 클래스를 로딩 한 다음 Class<T>의 인스턴스가 생긴다. “타입.class”로 접근할 수 있다.
* 모든 인스턴스는 getClass() 메소드를 가지고 있다. “인스턴스.getClass()”로 접근할 수 있다.
* 클래스를 문자열로 읽어오는 방법
  *  Class.forName(“FQCN”)
  *  클래스패스에 해당 클래스가 없다면 ClassNotFoundException이 발생한다.

Class<T>를 통해 할 수 있는 것

* 필드 (목록) 가져오기
* 메소드 (목록) 가져오기
*  상위 클래스 가져오기
*  인터페이스 (목록) 가져오기
*  애노테이션 가져오기
*  생성자 가져오기
* ...



### 리플렉션이란?

리플렉션은 힙 영역에 로드된 Class 타입의 객체를 통해, 원하는 클래스의 인스턴스를 생성할 수 있도록 지원하고, 인스턴스의 필드와 메소드를 접근 제어자와 상관 없이 사용할 수 있도록 지원하는 API이다.

 

### 리플렉션의 장단점

- 장점
  - 런타임 시점에서 클래스의 인스턴스를 생성하고, 접근 제어자와 관계 없이 필드와 메소드에 접근하여 필요한 작업을 수행할 수 있는 유연성을 가지고 있다.
- 단점
  - 캡슐화를 저해한다.
  - 런타임 시점에서 인스턴스를 생성하므로 컴파일 시점에서 해당 타입을 체크할 수 없다.
  - 런타임 시점에서 인스턴스를 생성하므로 구체적인 동작 흐름을 파악하기 어렵다.
  - 단순히 필드 및 메소드를 접근할 때보다 리플렉션을 사용하여 접근할 때 성능이 느리다. (모든 상황에서 성능이 느리지는 않음.)



### 리플렉션은 언제 사용하는가?

규모가 작은 콘솔 단계에서는 개발자가 충분히 컴파일 시점에 프로그램에서 사용될 객체와 의존 관계를 모두 파악할 수 있다. 하지만 프레임워크와 같이 큰 규모의 개발 단계에서는 수많은 객체와 의존 관계를 파악하기 어렵다. 이때 리플렉션을 사용하면 동적으로 클래스를 만들어서 의존 관계를 맺어줄 수 있다.

가령, Spring의 Bean Factory를 보면, @Controller, @Service, @Repository 등의 어노테이션만 붙이면 Bean Factory에서 알아서 해당 어노테이션이 붙은 클래스를 생성하고 관리해 주는 것을 알 수 있다. 개발자는 Bean Factory에 해당 클래스를 알려준 적이 없는데, 이것이 가능한 이유는 바로 리플렉션 덕분이다. 런타임에 해당 어노테이션이 붙은 클래스를 탐색하고 발견한다면, 리플렉션을 통해 해당 클래스의 인스턴스를 생성하고 필요한 필드를 주입하여 Bean Factory에 저장하는 식으로 사용이 된다.





## 10. 애노테이션과 리플렉션
중요 애노테이션

* @Retention: 해당 애노테이션을 언제까지 유지할 것인가? 소스, 클래스, 런타임
*  @Inherit: 해당 애노테이션을 하위 클래스까지 전달할 것인가?
*  @Target: 어디에 사용할 수 있는가?

리플렉션

* getAnnotations(): 상속받은 (@Inherit) 애노테이션까지 조회
* getDeclaredAnnotations(): 자기 자신에만 붙어있는 애노테이션 조회



## 11. 리플렉션 API 1부: 클래스 정보 수정 또는 실행
Class 인스턴스 만들기

* Class.newInstance()는 deprecated 됐으며 이제부터는 생성자를 통해서 만들어야 한다.

생성자로 인스턴스 만들기

* Constructor.newInstance(params)

필드 값 접근하기/설정하기

* 특정 인스턴스가 가지고 있는 값을 가져오는 것이기 때문에 인스턴스가 필요하다.
* Field.get(object)
* Filed.set(object, value)
* Static 필드를 가져올 때는 object가 없어도 되니까 null을 넘기면 된다.

메소드 실행하기

* Object Method.invoke(object, params)

나만의 DI 프레임워크 만들기

* @Inject 라는 애노테이션 만들어서 필드 주입 해주는 컨테이너 서비스 만들기



```java
public class BookService {
	@Inject
	BookRepository bookRepository;
}
```

Co ntainerService.java

```java
public static <T> T getObject(T classType)
```

 classType에 해당하는 타입의 객체를 만들어 준다.

 단, 해당 객체의 필드 중에 @Inject가 있다면 해당 필드도 같이 만들어 제공한다.



inject

```java
// 필드 주입 예제
@Retention(RetentionPolicy.RUNTIME)
public @interface Inject { }
```



repository, service

```java
public class BookRepository {
}
//

public class BookService {
	@Inject
	private BookRepository bookRepository;

}
```



testcode

```java

import com.ys.demo.framework.ContainerService;

class ContainerServiceTest {

	@Test
	void getObject_BookService() {

		BookService bookService = ContainerService.getObject(BookService.class);

		assertNotNull(bookService.getBookRepository());
		assertNotNull(bookService);

	}
}
```



container service

```java

public class ContainerService {

	public static <T> T getObject(Class<T> classType) {

		T instance = createInstance(classType);

		Arrays.stream(classType.getDeclaredFields())
			.forEach(f -> {
				if (f.getAnnotation(Inject.class) != null) {
					Object fieldInstance = createInstance(f.getType());
					f.setAccessible(true);

					try {
						f.set(instance, fieldInstance);
					} catch (IllegalAccessException e) {
						throw new RuntimeException(e);
					}
				}
			});
		return instance;
	}

	private static <T> T createInstance(Class<T> classType) {
		try {
			return classType.getConstructor(null)
				.newInstance();
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException |
				 NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}

}

```



## 13. 리플렉션 정리
리플렉션 사용시 주의할 것

* 지나친 사용은 성능 이슈를 야기할 수 있다. 반드시 필요한 경우에만 사용할 것
* 컴파일 타임에 확인되지 않고 런타임 시에만 발생하는 문제를 만들 가능성이 있다.
* 접근 지시자를 무시할 수 있다.

스프링

* 의존성 주입

* MVC 뷰에서 넘어온 데이터를 객체에 바인딩 할 때

하이버네이트

* @Entity 클래스에 Setter가 없다면 리플렉션을 사용한다.

JUnit

* https://junit.org/junit5/docs/5.0.3/api/org/junit/platform/commons/util/ReflectionUtils.html

참고

* https://docs.oracle.com/javase/tutorial/reflect/index.html



# 4부. 다이나믹 프록시

스프링 데이터 JPA는 어떻게 동작하나?
스프링 데이터 JPA에서 `인터페이스 타입`의 `인스턴스`는 누가 만들어 주는것인가?

* Spring AOP를 기반으로 동작하며 RepositoryFactorySupport에서 프록시를 생성한다. 



핵심 클래스 : java.lang.reflect.Proxy 클래스



JpaRepository 인터페이스를 상속받은 인터페이스는 인터페이스의 구현체도 구현해주고, 구현체도 빈으로 등록해준다.

* 스프링 AOP를 사용중이다.

**org.springframework.aop.framework.`ProxyFactory` 클래스**

* 자바에서 제공하는 Proxy를 추상화해놓은클래스
* RepositoryFactorySupport에서 이 클래스를 이용한다 

이 클래스에서 만들어진 구현체가 Repository 인터페이스에 주입해준다.



## 프록시 패턴

![image-20230205020131835](/Users/ysk/study/study_repo/inf-thejava-code-whiteship/images//image-20230205020131835.png)

* 프록시와 리얼 서브젝트가 공유하는 인터페이스가 있고, 클라이언트는 해당
  인터페이스 타입으로 프록시를 사용한다.
* 클라이언트는 프록시를 거쳐서 리얼 서브젝트를 사용하기 때문에 프록시는 리얼
  서브젝트에 대한 접근을 관리거나 부가기능을 제공하거나, 리턴값을 변경할 수도 있다
* 리얼 서브젠트는 자신이 해야 할 일만 하면서(SRP) 프록시를 사용해서 부가적인
  기능(접근 제한, 로깅, 트랜잭션, 등)을 제공할 때 이런 패턴을 주로 사용한다.

참고

* https://www.oodesign.com/proxy-pattern.html
*  https://en.wikipedia.org/wiki/Proxy_pattern
*  https://en.wikipedia.org/wiki/Single_responsibility_principle



## 다이나믹 프록시 실습
런타임에 특정 인터페이스들을 구현하는 클래스 또는 인스턴스를 만드는 기술
an application can use a dynamic proxy class to create an object that implements multiple arbitrary event listener
nterfaces”

- https://docs.oracle.com/javase/8/docs/technotes/guides/reflection/proxy.html
- 프록시 인스턴스 만들기
-  Object Proxy.newProxyInstance(ClassLoader, Interfaces, InvocationHandler)

```java
BookService bookService = (BookService) Proxy.newProxyInstance(BookService.class.getClassLoader(), new
Class[]{BookService.class},
new InvocationHandler() {

  BookService bookService = new DefaultBookService();

  @Override

  public Object invoke(Object proxy, Method method, Object[] 
                       args) throws Throwable {

    if (method.getName().equals("rent")) {
      System.out.println("aaaa");
			Object invoke = method.invoke(bookService, args);
			System.out.println("bbbb");
			return invoke;
		}

    return method.invoke(bookService, args);
		}
});
```

* 유연한 구조가 아니다. 그래서 스프링 AOP 등장!
*  스프링 AOP에 대한 더 자세한 토비의 스프링 3.1, 6장 AOP를 참고하세요.

참고

* https://docs.oracle.com/javase/8/docs/technotes/guides/reflection/proxy.html
*  https://docs.oracle.com/javase/8/docs/api/java/lang/reflect/Proxy.html#newProxyInstance-java.lang.ClassLoader-java.lang.Class:A-java.lang.reflect.InvocationHandler
