# 백기선 님의 유튜브 온라인 자바 스터디를 정리한 글입니다



# 1주차



## 목표

자바 소스 파일(.java)을 JVM으로 실행하는 과정 이해하기.

## 학습할 것

- JVM이란 무엇인가
- 컴파일 하는 방법
- 실행하는 방법
- 바이트코드란 무엇인가
- JIT 컴파일러란 무엇이며 어떻게 동작하는지
- JVM 구성 요소
- JDK와 JRE의 차이

# JVM이란 무엇인가

* JVM : Java Virtual Machine의 줄임말(자바 가상 머신)

자바를 실행하기 위한 가상 머신. 

자바 바이트코드(java ByteCode)를 실행할 수 있는 주체이다.

자바 바이트코드는 플랫폼(os 등)에 독립적이며 종속받지 않고 실행되는데, 대신 OS 위에서 Java를 실행시킬 것이 필요하다.

그것이 바로 <u>JVM</u>이다.

* <img src="https://velog.velcdn.com/images/god1hyuk/post/cdfbd59a-3518-4c62-bfe7-4a1a3b5d8925/image.png" alt="img" style="zoom:50%;" />

-  .java파일을 Java Compiler가  .class라는  Java ByteCode로 변환시켜준다
  - Byte코드는 기계어가 아니기 때문에 OS에서 바로 실행이 안되고 JVM이 실행시켜야한다.
- JVM은 OS가 ByteCode를 이해할 수 있도록 해석해 주는 역할을 함
  - 따라서 JVM은 c언어 같은 네이티브 언어에 비해 속도가 느렸지만 JIT(Just In Time)컴파일러 구현을 통해이점을 극복
- Byte코드는 JVM위에서 OS상관없이 실행된다.
  - OS에 종속적이지 않고, Java 파일 하나만 만들면 어느 디바이스든 JVM 위에서 실행할 수 있다.
  - `*JVM은 플랫폼(OS)에 독립적이지만 의존적이다.*` 

### JVM 구성요소

* <img src="https://miro.medium.com/max/1400/1*slIuYO633BCuBh_gfYRmGg.png" alt="img" style="zoom:67%;" />

* Java Source File : .java 파일에 작성한 소스 코드
* Java Compiler : .java 파일을 .class 라는 컴파일해서 Java ByteCode로 만들어주는 과정 [컴파일이란](#컴파일이란)
  * Java ByteCode (.class)

* Class Loader : Runtime 시점에 .class에서 바이트 코드를 읽고 메모리에 저장

    * 자바는 동적으로 클래스를 읽어옵니다,  클래스가 참조(요청)될 때 파일로부터 읽어 메모리로 로딩하는 역할
      *  JVM이 클래스에 대한 정보를 갖고 있지 않다는 것을 의미. 
      *  JVM은 클래스의 메소드, 필드, 상속관계 등에 대한 정보를 알지 못한다
    * 프로그램이 실행 중인 런타임에서야 모든 코드가 자바 가상 머신과 연결.
      동적으로 읽어온다는 것은 런타임시에 모든 코드가 JVM에 링크 되는 것을 의미.

  * <img src="https://github.com/devYSK/study_repo/blob/main/whiteship-java/src/main/java/week01/images/image-20220730003633934.png?raw=true" alt="image-20220730003633934" style="zoom:67%;" />
  * 로딩, 링크, 초기화 순으로 진행된다.
  * Loading - 로딩
    - 클래스 로더가 .class 파일을 읽고 그 내용에 따라 적절한 바이너리 데이터를 만들고 “메소드” 영역에 저장
    - 로딩이 끝나면 해당 클래스 타입의 Class 객체를 생성하여 “힙” 영역에 저장
  * Linking - 링크
    * *Linking* 은 로드된 클래스 파일들을 검증하고, 사용할 수 있게 준비하는 과정을 의미
    * Verify, Prepare, Resolve(Optional) 세 단계로 나눠져 있다.
    * Veify (검증) : .class 파일 형식이 유효한지 체크한다.
      - 클래스 파일이 *JVM* 의 구동 조건 대로 구현되지 않았을 경우에는 *VerifyError* 를 던지게 됩니다
    * Preparation: 클래스 변수(static 변수)의 기본값에 따라 필요한 메모리를 할당하고 기본값으로 초기화. 
      - 기본값으로 초기화된 *static field* 값들은 뒤의 *Initialization* 과정에서 코드에 작성한 초기값으로 변경
    * Resolve: 심볼릭 메모리 레퍼런스를 메서드 영역에 있는 실제 레퍼런스로 교체한다.
  * Initialization - 초기화
    * *Java* 코드에서의 *class* 와 *interface* 의 값들을 지정한 값들로 초기화 및 초기화 메서드를 실행
    * Static 변수의 값을 할당한다.(static 블럭이 있다면 이때 실행된다.)
    * SuperClass 초기화를 진행한다.
    * SuperClass를 초기화한 후 해당 Class의 초기화를 진행한다.
  * 클래스 로더는 계층 구조로 이뤄져 있으면 기본적으로 세가지 클래스 로더가 제공된다.
    - Bootstrap 부트 스트랩: JAVA_HOME/lib에 있는 코어 자바 API를 제공한다. 최상위 우선순위를 가진 클래스 로더
    -  Extension : JAVA_HOME/lib/ext 폴더 또는 java.ext.dirs 시스템 변수에 해당하는 위치에 있는 클래스를 읽는다.
    - Application : 앱 ClassPath(앱을 실행 할 때 주는 -classpath 옵션 또는 java.class.path 환경변수의 값에 해당하는 위치)에서 클래스를 읽는다.

* Runtime Data Areas :  JVM이 프로그램을 수행하기 위해 OS로부터 할당받는 메모리 영역

  *  <img src="/Users/ysk/study/study_repo/whiteship-java/src/main/java/week01/images//image-20220729234840197.png" alt="image-20220729234840197" style="zoom:80%;" />

  * `Java Threads` : 각 쓰레드들 마다 Stack, PCRegisters, Native Method Stacks를 가짐

    * PC Register :  CPU가 Instruction을 수행하는 동한 필요한 정보를 저장.

      - Thread별로 1개씩 존재
      - 현재 수행 중인 JVM Instruction 의 주소를 가진다

    * JavaStack(JVM Stack) :  Thread의 Method가 호출될 때 수행 정보(메소드 호출 주소, 매개 변수, 지역 변수, 연산 스택)가 Frame 이라는 단위로 JVM stack에 저장된다.

      * Method 호출이 종료될 때 stack에서 제거

      * `Java Stack 영역이 가득 차게 되면 *StackOverflowError* 를 발생`

        

    * NativeMethodStacks : Java 외의 언어로 작성된 네이티브 코드들을 위한 stack (JNI 등을 통해 호출되는 C, C++ 등의 코드)

  * `Heap` : 런타임시 동적으로 할당하여 사용하는 영역.

    * class를 통해 instance를 생성하면 Heap에 저장됨
    * 문자열에 대한 정보를 가진 String Pool, 인스턴스와 배열이 동적으로 생성되는 공간. 그리고 Garbage Collection의 대상이 되는 영역
    * Heap의 경우 명시적으로 만든 class와 암묵적인 static 클래스(.class 파일의 class)가 담긴다.
    * 또한 암묵적인 static 클래스의 경우 클래스 로딩 시 class 타입의 인스턴스를 만들어 힙에 저장한다. 이는 Reflection에 등장한다.
    * *Heap* 영역이 가득 차게 되면 *OutOfMemoryError* 를 발생
    * `Heap 과 Method는 모든 쓰레드가 공유 나머지는 쓰레드 마다 생성`

  * `Method Area` : 모든 쓰레드가 공유하는 영역 (클래스, 인터페이스, 메소드, 필드, Static 변수 등의 바이트 코드 보관)
    * 이 영역에 등록된 class만이 Heap에 생성될 수 있다.  Method Area는 논리적으로 Heap에 포함된다.



- Execution Engine
  - Load된 Class의 ByteCode를 실행하는 Runtime Module
  - Class Loader를 통해 JVM 내의 Runtime Data Areas에 배치된 바이트 코드는 Execution Engine에 의해 실행(바이트 코드를 명령어 단위로 읽어서 실행)
  - JIT Compiler : (Just-In-Time compiler)란 프로그램이 실제 실행하는 시점(런타임)에 기계어로 변환해 주는 컴파일러를 의미합니다.
    - 동적 번역 (dynamic translation) 라고도 불리는 기법은 Java Program의 실행 속도를 향상시키기 위해 개발 
  - Garbage collector : JVM 상에서 더 이상 사용하지 않는 데이터가 할당되어 있는 메모리를 자동으로 회수해장치
    - 참조되고 있는지에 대한 개념을 *reachability* 라고 하고, 유효한 참조를 *reachable* , 참조되지 않으면 *unreachable* 이라고 합니다
    - Heap 영역 내부에서 더이상 참조되지 않는 객체들은 unreachable로 판정되어 GC가 이루어 집니다. 
    - Full GC가 일어나는 수 초간 모든 Thread가 정지한다면 심각한 장애로 이어질 수 있다.
      - https://xzio.tistory.com/1949
      - https://dongwooklee96.github.io/post/2021/04/04/gcgarbage-collector-%EC%A2%85%EB%A5%98-%EB%B0%8F-%EB%82%B4%EB%B6%80-%EC%9B%90%EB%A6%AC.html

#### JVM 실행과정

1. 프로그램이 실행되면 JVM은 OS로부터 이 프로그램이 필요로 하는 메모리를 할당 받는다. JVM은 이 메모리를 용도에 따라 여러 영역으로 나누어 관리한다.
2. 자바 컴파일러(javac)가 자바소스(.java)코드를 읽어 들여 자바 바이트코드(.class)로 변환시킨다.
3. 이 변경된 Class 파일들을 Class Loader를 통해 JVM 메모리영역(Runtime Data Areas) 영역으로 로딩한다.
4. 로딩된 class파일들은 Execution engine을 통해 해석된다.
5. 해석된 바이트코드는 Runtime Data Areas에 배치되어 실질적인 수행이 이루어지게된다.
   이러한 실행과정속에서 JVM은 필요에 따라 Thread Synchronization과 GC같은 관리 작업을 수행한다.



# 컴파일 하는 방법

## 컴파일이란 

* 사람이 이해하는 언어를 컴퓨터가 이해할 수 있는 언어로 바꾸어 주는 과정.
  * 사람이 작성한 자바 코드를 컴퓨터가 이해할 수 있는 언어(ByteCode)로 바꾸어 주는 과정이다.

* Java Compiler가 `.java` 파일을 `.class` 라는 Java ByteCode로 만드는 과정 

* `javac` 명령어를 이용해 컴파일한다

  * ```shell
    ~$ javac JavaClass.java
    ```

  * javac 파일이름.java

# 실행하는 방법

* 자바 파일을 클래스 파일로 컴파일한다. 

  * javac 명령어 실행 후 컴파일한 .java 파일 경로를 확인해보면 .class 파일이 생성되어있다.

* `java` 명령어를 사용해서 실행한다

  * .class 파일을 실행

  * ```shell
    java 파일이름 (.class 생략 가능)
    ```

# 바이트코드란 무엇인가

* ByteCode란 [자바 컴파일러](##_컴파일이란)를 통해 [JVM(자바 가상 머신)](#JVM이란 무엇인가)이 이해할 수 있는 언어로 변환된 자바 소스코드 
* `이 ByteCode를 JVM이 이해해서 실행시킨다.`
* 자바 바이트코드는 JVM이 실행하는 명령어의 집합
* 컴파일하면 생성되는 .class 파일이 바이트 코드를 담고 있다.

# JIT 컴파일러란 무엇이며 어떻게 동작하는지

### JIT 컴파일

* 동적 번역이라고도 불림 
* 프로그램을 실제 실행하는 시점에 기계어로 번역하는 컴파일 기법 . 
* IT 컴파일러는 인터프리트, 정적 컴파일 두 가지의 방식을 혼합한 방식으로 생각할 수 있는데, 실행 시점에서 인터프리트 방식으로 기계어 코드를 생성하면서 그 코드를 캐싱하여, 같은 함수가 여러 번 불릴 때 매번 기계어 코드를 생성하는 것을 방지
  * 인터프리터 : 바이트코드 명령어를 순차적으로 하나씩 읽어서 해석하고 실행.
    * 바이트코드 하나하나 해석은 빠른 대신, 결과의 실행은 느림
* 인터프리터 방식으로 실행하다가 적절한 시점에 바이트코드 전체를 컴파일하여 네이티브 코드로 변경하고,
  이후에는 해당 메서드를 더 이상 인터프리팅하지 않고 네이티브 코드로 직접 실행하는 방식이다.
* 네이티브 코드를 실행하는 것이 하나씩 인터프리팅하는 것보다 빠르고,
  네이티브 코드는 캐시에 보관하기 때문에 한 번 컴파일된 코드는 계속 빠르게 수행되게 된다.

# JVM 구성 요소

* [JVM 구성요소](###JVM 구성요소)



# JDK와 JRE의 차이



JRE (Java Runtime Environment) : 컴파일된 자바 프로그램을 실행시킬 수 있는 자바 환경

* VM, Java 클래스 라이브러리 및 Java 응용 프로그램을 실행하는 데 필요한 기타 파일의 조합

- JRE는 JVM이 자바 프로그램을 동작시킬 때 필요한 라이브러리 파일과 기타 파일을 가짐
- JRE는 JVM 실행환경을 구현했다고 할 수 있다.
- 자바 프로그램을 실행시키기 위해선 JRE를 반드시 설치
- 자바 프로그래밍 도구는 포함되어 있지 않기 때문에 자바 개발을 하기 위해서는 JDK가 필요하다.

JDK (Java Development Kit) : 자바 프로그래밍 시 필요한 컴파일러 등 포함

- `JDK는 개발을 위해 필요한 도구 (javac, java, javap 등)을 포함한다.`

  - javac 옵션 : http://sjava.net/2008/02/javac-%EB%AA%85%EB%A0%B9%EC%96%B4%EC%9D%98-%EC%98%B5%EC%85%98-%EC%A0%95%EB%A6%AC/
    - https://team621.tistory.com/38

- JAVA9버전 부터는 JRE자체가 사라졌다.

  - JDK를 설치하면 JRE도 함께 설치된다.
  -  JDK = JRE + @

  * JDK가 JRE를 포함하기 때문

  * Java 프로그램을 실행하는데만 포커스를 둔다면, JRE만 설치하면 된다. 반면, Java로 프로그래밍을 하거나 배포를 진행해야 한다면 JDK를 설치해야 한다.

### 참조 및 출처

https://www.baeldung.com/java-stack-heap

https://nesoy.github.io/articles/2020-11/ClassLoader

https://www.tutorialspoint.com/java_virtual_machine/java_virtual_machine_runtime_data_areas.htm

https://jithub.tistory.com/40

https://ram-bak.tistory.com/5

https://tecoble.techcourse.co.kr/post/2021-08-09-jvm-memory/

https://jeongjin984.github.io/posts/JVM/

https://medium.com/@js230023/%EC%9E%90%EB%B0%94-jvm%EA%B3%BC-%EB%B0%94%EC%9D%B4%ED%8A%B8-%EC%BD%94%EB%93%9C-4e754ee02490

https://antstudy.tistory.com/183