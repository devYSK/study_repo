#### WEEK 01 :: [JVM은 무엇이며 자바 코드는 어떻게 실행하는 것인가.]()

- JVM 이란 무엇인가
- 컴파일 하는 방법
- 실행하는 방법
- 바이트코드란 무엇인가
- JIT 컴파일러란 무엇이며 어떻게 동작하는지
- JVM 구성요소
- JDK 와 JRE의 차이

#### WEEK 02 :: [자바 데이터 타입, 변수 그리고 배열]()

- 프리미티브 타입 종류와 값의 범위 그리고 기본 값
- 프리미티브 타입과 레퍼런스 타입
- 리터럴
- 변수 선언 및 초기화하는 방법
- 변수의 스코프와 라이프타임
- 타입 변환, 캐스팅 그리고 타입 프로모션
- 1차 및 2차 배열 선언하기
- 타입 추론, var

#### WEEK 03 :: [연산자]()

- 산술 연산자
- 비트 연산자
- 관계 연산자
- 논리 연산자
- instanceof
- assignment(=) operator
- 화살표(->) 연산자
- 3항 연산자
- 연산자 우선 순위
- (optional) Java 13. switch 연산자

#### WEEK 04 :: [제어문]()

- 선택문
- 반복문

- 과제
  - live-study 대시 보드를 만드는 코드를 작성하세요.
    - 깃헙 이슈 1번부터 18번까지 댓글을 순회하며 댓글을 남긴 사용자를 체크 할 것.
    - 참여율을 계산하세요. 총 18회에 중에 몇 %를 참여했는지 소숫점 두자리가지 보여줄 것.
    - Github 자바 라이브러리를 사용하면 편리합니다.
    - 깃헙 API를 익명으로 호출하는데 제한이 있기 때문에 본인의 깃헙 프로젝트에 이슈를 만들고 테스트를 하시면 더 자주 테스트할 수 있습니다.
  - LinkedList를 구현하세요
    - LinkedList에 대해 공부하세요.
    - 정수를 저장하는 ListNode 클래스를 구현하세요.
    - ListNode add(ListNode head, ListNode nodeToAdd, int position)를 구현하세요.
    - ListNode remove(ListNode head, int positionToRemove)를 구현하세요.
    - boolean contains(ListNode head, ListNode nodeTocheck)를 구현하세요.
  - Stack을 구현하세요.
    - int 배열을 사용해서 정수를 저장하는 Stack을 구현하세요.
    - void push(int data)를 구현하세요.
    - int pop()을 구현하세요.
  - 앞서 만든 ListNode를 사용해서 Stack을 구현하세요.
    - ListNode head를 가지고 있는 ListNodeStack 클래스를 구현하세요.
    - void push(int data)를 구현하세요.
    - int pop()을 구현하세요.
  - (optional) Queue를 구현하세요.
    - 배열을 사용해서 한번
    - ListNode를 사용해서 한번.

#### WEEK 05 :: [클래스]()

- 클래스 정의하는 방법
- 객체 만드는 방법 (new 키워드 이해하기)
- 메소드 정의하는 방법
- 생성자 정의하는 방법
- this 키워드 이해하기

- 과제 (Optional)
  - int 값을 가지고 있는 이진 트리를 나타내는 Node 라는 클래스를 정의하세요.
  - int value, Node left, right를 가지고 있어야 합니다.
  - BinrayTree라는 클래스를 정의하고 주어진 노드를 기준으로 출력하는 bfs(Node node)와 dfs(Node node) 메소드를 구현하세요.
  - DFS는 왼쪽, 루트, 오른쪽 순으로 순회하세요.

#### WEEK 06 :: [상속]()

- 자바 상속의 특징
- super 키워드
- 메소드 오버라이딩
- 다이나믹 메소드 디스패치 (Dynamic Method Dispatch)
- 추상 클래스
- final 키워드
- Object 클래스

#### WEEK 07 :: [패키지]()

- package 키워드
- import 키워드
- 클래스패스
- CLASSPATH 환경변수
- -classpath 옵션
- 접근지시자

#### WEEK 08 :: [인터페이스]()

- 인터페이스 정의하는 방법
- 인터페이스 구현하는 방법
- 인터페이스 레퍼런스를 통해 구현체를 사용하는 방법
- 인터페이스 상속
- 인터페이스의 기본 메소드 (Default Method), 자바 8
- 인터페이스의 static 메소드, 자바 8
- 인터페이스의 private 메소드, 자바 9

#### WEEK 09 :: [예외 처리]()

- 자바에서 예외 처리 방법 (try, catch, throw, throws, finally)
- 자바가 제공하는 예외 계층 구조
- Exception과 Error의 차이는?
- RuntimeException과 RE가 아닌 것의 차이는?
- 커스텀한 예외 만드는 방법

#### WEEK 10 :: [멀티쓰레드 프로그래밍]()

- Thread 클래스와 Runnable 인터페이스
- 쓰레드의 상태
- 쓰레드의 우선순위
- Main 쓰레드
- 동기화
- 데드락

#### WEEK 11 :: [Enum]()

- enum 정의하는 방법
- enum이 제공하는 메소드 (values()와 valueOf())
- java.lang.Enum
- EnumSet

#### WEEK 12 :: [애노테이션]()

- 애노테이션 정의하는 방법
- @retention
- @target
- @documented
- 애노테이션 프로세서

#### WEEK 13 :: I/O

- 스트림 (Stream) / 버퍼 (Buffer) / 채널 (Channel) 기반의 I/O
- InputStream과 OutputStream
- Byte와 Character 스트림
- 표준 스트림 (System.in, System.out, System.err)
- 파일 읽고 쓰기

#### WEEK 14 :: [제네릭]()

- 제네릭 사용법
- 제네릭 메소드 만들기
- 제네릭 주요 개념(바운디드 타입, 와일드 카드)
- Erasure

#### WEEK 15 :: [람다식]()

- 람다식 사용법
- 함수형 인터페이스
- Variable Capture
- 메소드, 생성자 레퍼런스