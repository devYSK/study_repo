# 백기선 님의 유튜브 온라인 자바 스터디를 정리한 글입니다

# 4주차  -  제어문

# 목표

자바가 제공하는 제어문을 학습하세요.

# 학습할 것 (필수)

* 선택문
* 반복문



# 제어문

* 제어문이란 주어진 조건에 따라 코드 블록을 실행하거나 반복실행 할 때 사용한다.
* 일반적으로 코드는 위에서 아래로 순차적으로 실행된다. 
* 제어문은 코드의 실행순서를 인위적으로 제어할 수 있다.

# 선택문

* 제어문 중 `if`, `if ~ else`, `switch`문 입니다.



## if문

* 문법 : if (condition) {코드 실행 블록}  
  * condition은 조건식이나 조건문. 변수나 조건식 메서드가 들어갈 수 있습니다.

* 조건이 참(true)이면 코드 블록을 실행, 거짓(false) 라면 코드 블록을 실행하지 않습니다. 
* 실행 블록 내의 코드가 한 줄인 경우 중괄호를 생략할 수 있습니다.

```java
int age = 28;
if (age < 30) {
  System.out.println("30살 보다 어립니다.");
}

if (age > 30) {
  System.out.println("30살 보다 많습니다.");
}

boolean isSteal = false;

if (!isSteal) {
  System.out.println("훔치지 않았습니다.");
}
```

결과

`30살 보다 어립니다`

`훔치지 않았습니다`

* 위 코드에서는 age가 28 이므로 위 age < 30 condition이 참이 되어 age < 30 코드블록만 실행합니다.
* 반대로 age > 30 조건문은 거짓이므로 코드블록을 실행하지 않습니다.
* isSteal 은 false이지만 not 연산을 통해 true로 바꾸었으므로 참이 되어 코드 블록을 실행합니다. 

 

## if~else문 



* 문법 : if (condition) { 조건이 참일 때 코드 실행 블록 } else {조건이 거짓일 때 코드 실행 블록}

```java

boolean isSteal = true;

if (isSteal) {
  System.out.println("훔친게 맞습니다.");
} else {
  System.out.println("훔치지 않았습니다.");
}

```

결과

`훔친게 맞습니다.`

* 조건이 참이므로 if 조건식이 참일 때 코드 실행 블록만 실행합니다. 거짓일 때 코드블록은 실행하지 않습니다. 



## 다중 if~else 문



* 문법 : if (condtion) {코드 실행 블록} else if (conditon) {코드 실행 블록} else {그 외 나머지 경우 코드 실행 블록}

* 처음 if에 조건식이 거짓이면 다음 else if의 조건식을 내려가면서 순차적으로 비교하고 그래도 조건이 맞는게 없다면 else블록을 실행합니다. 

```java
int age = 28;

if (age > 40) {
  System.out.println("40살보다 많습니다");
} else if (age > 30) {
  System.out.println("30살보다 많고 40살보다 적습니다");  
} else if (age > 20) {
    System.out.println("20살보다 많고 30살보다 적습니다");
} else {
    System.out.println("20살보다 적습니다");
}

```



```
if 문의 특징은 한 번이라도 조건에 만족하는 경우를 찾으면, 그 다음 조건에 대해서는 생략한다는 것이다.
```

* 한 메서드 안에 if, else가 난무하면 코드가 매우 복잡하고 깨끗하지 않으므로 주의해서 사용하도록 하는게 코드 품질에 좋습니다. 



## switch문

* 문법 : switch (수식) {case :  .....}
* switch - case`문은 `switch(수식)`에 결과 값에 따라 `case`문에서 실행되는 문장을 제어할 수 있습니다.
* 수식의 결과 타입은 논리형은 불가능하고, 정수형이나 문자형 타입만 올 수 있다.
* `break`를 만나게 되면 해당 case를 빠져나와 다음 case문으로 넘겨져갑니다.
  * 다음 수식을 실행할 것이 아니라면 break나 return을 꼭 해줘야 합니다.

```java
public void printMyState(String day) {
  switch (day) {
    case "MONDAY":
    case "TUESDAY":
    case "WEDNSESDAY":
    case "THURSDAY":
    case "FRIDAY":
      System.out.println("일하는날은 피곤해서 상태가 좋지 않네..");
      break;
    case "SATURDAY":
    case "SUNDAY":
      System.out.println("쉬는날이라 기분이 좋아졌어!");
      break;
    default : 
    	System.out.println("해당하는 요일이 없습니다. "); 
      // 이 부분은 예외입니다. 일주일의 요일들을 제외한 다른값이기 때문이죠  
  }
}

String day = "SATURDAY"
printMyState(day);
```

* case문에서 일치하는 값이 없을경우 default 문으로 넘어갑니다. 

# 반복문

* 반복문은 어떤 조건(condition)이 만족하는 동안 같은 내용을 정해둔 반복 수 만큼 반복하는 문입니다.
* 종류는 크게 3가지가 있습니다
  * for
  * while
  * do while



* while이나 do while문은 잘못된 조건식이면 실행 블록이 무한히 반복되어 프로그램에 큰 에러를 일으킬 수 있습니다
* 보편적으로 for문을 많이 사용합니다.



## for문

* for(초기식; 조건식; 증감식 ) {실행 코드 블럭}
  * 초기식은 for문이 시작될 때 단 한번만 실행됩니다.
  * 조건식이 거짓이면 더이상 for문 내의 코드블럭을 반복하지 않고 for문을 종료합니다.
  * 조건식이 참이면 실행 블록을 실행하고 증감식을 실행한 다음 다시 조건식을 실행합니다. 

```java
String[] greetings = {"hi", "hello", "하이", "ㅎㅇ", "아뇽하세요"};

for (int index = 0; index < greetings.length; index ++ ) {
  System.out.println(greetings[i]);
  System.out.println((index + 1) + " 번 째 반복" );
}
```

* 자바 5버전 (jdk 5)부터 배열 혹은 컬렉션의 순회시에 다음과 같이 조금 더 향상된 for 문을 사용할 수 있습니다. 

```java
for (타입 변수명 : 배열 or 컬렉션 ) {
  반복될 코드;
}
```

* foreach 스타일 for문
  * for (값 : 컬렉션 / 조건식)
    * 레퍼런스를 가져오기 때문에 값(value)는 변경할 수 없습니다. 
  * 어떤 컬렉션이든 순회할 수 있습니다.
  * 반복자나 인덱스 변수를 제거해 오류 가능성을 줄여주는 장점이 있습니다.

```java
int[] intArray = {0, 1, 2, 3, 4, 5, 6};
for (int value : intArray) {
  System.out.println(value);
}
```



* `break` 문을 통하여 반복문을 정지할 수 있습니다.

```java
int[] intArray = {0, 1, 2, 3, 4, 5, 6};
for (int value : intArray) {
  
  if (value > 3) { // value가 3을 초과하면 break;
    break;
  }
  System.out.println(value);
}

System.out.println("반복문 탈출!");
```

* value 값이 3을 초과하게 된다면 break로 반복문을 종료하고 다음 코드 (System.out.println("반복문 탈출!!")를 실행합니다 

## while문

* while (condition) {실행 코드 블럭}
* 조건식이 항상 참일 경우에는 계속해서 해당 코드들을 실행합니다
  * 잘못하다간 무한루프에 빠져 오류가 생기거나 다음 코드들을 실행못하는 경우가 생길수도 있습니다.

```java
int index = 0;

while (index++ < 10) { // 인덱스 값을 계속 증가시키면서 10보다 작을때까지
	System.out.println("index : " + index);
}

// 
while (true) { // 조건식이 계속 참이므로 무한루프.
  System.out.println("무한루프....");
}
```

* 마찬가지로 break문을 통해 반복문을 정지할 수 있습니다.

```java
int index = 0;

while (index++ < Integer.MAX_VALUE) { // 인덱스 값을 계속 증가시키면서 10보다 작을때까지
	System.out.println("index : " + index);
  
  if (index > 1000) {
    break;
  }
}
System.out.println("1000까지만 출력하고 while문 탈출!");
```

## do-while문 

* 문법 : do { 실행 코드 블럭} while(조건식);
* 조건식의 참 - 거짓 여부에 상관 없이 일단 최소 1번은 먼저 코드 블럭을 실행하고 조건식을 판별합니다.

```java

int index = 1;

do {
  System.out.println("1번만 실행됨");
} while(index > 0
```



# 과제 (옵션)

## 과제 0. JUnit 5 학습하세요.

- 인텔리J, 이클립스, VS Code에서 JUnit 5로 테스트 코드 작성하는 방법에 익숙해 질 것.
- 이미 JUnit 알고 계신분들은 다른 것 아무거나!
- [더 자바, 테스트](https://www.inflearn.com/course/the-java-application-test?inst=86d1fbb8) 강의도 있으니 참고하세요~



* 출처 ; https://velog.io/@seongwon97/Unit-Test-%EB%8B%A8%EC%9C%84-%ED%85%8C%EC%8A%A4%ED%8A%B8









## 과제 1. live-study 대시 보드를 만드는 코드를 작성하세요.

- 깃헙 이슈 1번부터 18번까지 댓글을 순회하며 댓글을 남긴 사용자를 체크 할 것.
- 참여율을 계산하세요. 총 18회에 중에 몇 %를 참여했는지 소숫점 두자리가지 보여줄 것.
- [Github 자바 라이브러리](https://github-api.kohsuke.org/)를 사용하면 편리합니다.
- 깃헙 API를 익명으로 호출하는데 제한이 있기 때문에 본인의 깃헙 프로젝트에 이슈를 만들고 테스트를 하시면 더 자주 테스트할 수 있습니다.



* 의존성 추가

```groovy
// https://mvnrepository.com/artifact/org.kohsuke/github-api
implementation 'org.kohsuke:github-api:1.303'
```





## 과제 2. LinkedList를 구현하세요.

- LinkedList에 대해 공부하세요.
- 정수를 저장하는 ListNode 클래스를 구현하세요.
- ListNode add(ListNode head, ListNode nodeToAdd, int position)를 구현하세요.
- ListNode remove(ListNode head, int positionToRemove)를 구현하세요.
- boolean contains(ListNode head, ListNode nodeTocheck)를 구현하세요.

## 과제 3. Stack을 구현하세요.

- int 배열을 사용해서 정수를 저장하는 Stack을 구현하세요.
- void push(int data)를 구현하세요.
- int pop()을 구현하세요.

## 과제 4. 앞서 만든 ListNode를 사용해서 Stack을 구현하세요.

- ListNode head를 가지고 있는 ListNodeStack 클래스를 구현하세요.
- void push(int data)를 구현하세요.
- int pop()을 구현하세요.

## 과제 5. Queue를 구현하세요.

- 배열을 사용해서 한번
- ListNode를 사용해서 한번.



* 과제 구현 링크 - https://github.com/devYSK/study_repo/tree/main/whiteship-java/src/main/java/week04































출처 및 참조 

https://palpit.tistory.com/entry/Java-3-%EC%84%A0%ED%83%9D%EB%AC%B8

https://xxxelppa.tistory.com/197?category=858435

https://www.notion.so/Live-Study-4-ca77be1de7674a73b473bf92abc4226a