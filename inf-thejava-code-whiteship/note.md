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
