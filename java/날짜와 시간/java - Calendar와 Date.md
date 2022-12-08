## 1. Calendar 와 Date

- `Date`는 JDK1.0, `Calendar`는 JDK1.1 부터 제공되었다.

- JDK1.8부터 `java.time 패키지`로 Calendar와 Date의 단점을 개선한 클래스들이 추가되었다.

- `Calendar`는 추상클래스라서 객체를 직접 생성할 수 없고, 메소드를 통해서 완전히 구현된 클래스의 인스턴스를 얻어야 한다.

  ```java
  // 에러. 추상클래스는 인스턴스를 생성할 수 없다.
  Calendar cal = new Calendar(); 
  
  // 굿. getInstance()는 Calendar 클래스를 구현한 클래스의 인스턴스를 반환
  Calendar cal = Calendar.getInstance();
  ```

- Calendar.getInstance() 가 static인이유

  - Calendar는 추상클래스
  - 메소드 내의 코드에서 인스턴스 변수를 사용하거나 인스턴스 메소드를 호출하지 않기 때문
  - Canlender 내부에서 TimeZone과 Locale을 이용하는 createCalendar()가 Canlender를 생성한다
  - 구현체로 GregorianCalendar와 BuddhistCalendar(불교에서 사용하는 연대 표기법) 등을 제공한다

* GregorianCalendar: 양력, 그레고리력.
  * 태국을 제외한 나라에서 사용
  * https://docs.oracle.com/javase/7/docs/api/java/util/GregorianCalendar.html



### 1.1. Calendar 와 Date 간 변환

- Calendar가 추가되면서 Date는 대부분의 메소드가 `deprecated` 되었다.

  ```java
  //1. Calendar를 Date로 변환
      Calendar cal = Calendar.getInstance();
        ...
      Date date = new Date(cal.getTimeInMillis());
  
  //2. Date를 Calendar로 변환
      Date date = new Date();
        ...
      Calendar cal = Calendar.getInstance();
      cal.setTime(d);
  ```

- get(Calendar.MONTH)로 얻어오는 값은 1~12가 아니라 0~11이다. 

  * 0은 1월 
  * 11은 12월을 의미한다.

### 1.2. 두 날짜간의 차이 구하기

- 두 날짜를 최소단위인 초단위로 변경한 다음 그 차이를 구한다.
- getTimeInMillis()는 1/1000초 단위로 값을 반환한다.

### 1.3. 시간상의 전후 알기

- 두 날짜간의 차이가 양수인지 음수인지 판단
- `boolean after(Object when)` 또는 `boolean before(Object when)` 사용

### 1.4. 특정 날짜/시간 기준 일정 기간 전후의 날짜/시간 알기

- `add(int field, int amount)` : 지정한 필드의 값 만큼 증가 또는 감소 시킨다

```java
Date date = new Date();
date.add(Calendar.DATE, 1); // 1일 후
date.add(Calendar.DATE, -6); // 6일 전
```

* YEAR, MONTH,  WEEK_OF_YEAR, WEEK_OF_MONTH, DATE, DAY_OF_MONTH, DAY_OF_YEAR, DAY_OF_WEEK 등 다양하게 옵션이 상수로 정의되어 있다.



- `roll(int field, int amount)` : 지정한 필드의 값을 증가 또는 감소, 다른 필드에 영향을 미치지 않음

  - ```java
    Date date = new Date();
    date.roll(Calendar.DATE, 1); // 1일 후
    ```

  - add메서드로 31일 만큼 증가시키면 다음달로 넘어가지만, roll메서드로 31일만큼 증가시켜도 달에는 영향을 미치지 않음

  - `add()`와의 차이점은 다른 필드에 영향 미치지 않음

  

  - 단, Calendar.DATE가 `말일`일 때, `roll`로 Calendar.MONTH를 변경하면 Calendar.DATE 필드에 영향을 미친다.

### 1.5. 해당 월의 마지막날 알기

- 다음 달의 1일에서 하루 빼기
- `getActualMaximum(Calendar.DATE)` 사용

### 1.6. 일 수 계산

- Calendar는 1970년 1월 1일을 기준으로 계산
- 1970년 1월 1일 이전에 날짜에 대해 `getTimeInMillis()`를 호출하면 음수 반환



### Date클래스의 기능들

```java
boolean isLeapYear(int year) : year가 윤년이면 true 아니면 false

int dayDiff(int y1, int m1, int d1, int y2, int m2, int d2) : 두 날짜간의 차이를 일 단위로 반환

int getDayOfWeek(int year, int month, int day) : 지정한 날짜의 요일을 반환

String convertDayToDate(int day) : 일단위의 값을 년월일의 형태의 문자열로 반환
  
int converDateToDay(int year, int month, int day) : 년월일을 입력받아서 일 단위로 반환
```

