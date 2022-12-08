##  java.time 패키지 - LocalDate, LocalTime

Java의 탄생과 함께한 Date와 Calendar의 단점을 해소하기 위해 JDK 1.8부터 `java.time 패키지`가 추가되었다.

* 이 패키지는 4개의 하위 패키지가 있다.

| 패키지               | 설명                                                 |
| -------------------- | ---------------------------------------------------- |
| `java.time`          | 날짜와 시간을 다루는데 필요한 핵심 클래스들 제공     |
| `java.time.chrono`   | 표준(ISO)이 아닌 달력 시스템을 위한 클래스들 제공    |
| `java.time.format`   | 날짜와 시간을 파싱, 형식화하기 위한 클래스들 제공    |
| `java.time.temporal` | 날짜와 시간의 필드와 단위(unit)을 위한 클래스들 제공 |
| `java.time.zone`     | 시간대(time-zone)와 관련된 클래스들 제공             |

* 이 패키지의 클래스들은 다 불변이다. `Immutable`



###  Immutable & Thread-safe

 날짜나 시간을 변경하면 기존의 객체가 변경되는 것이 아니라, 새로운 객체를 반환한다.

* 기존의 Calendar 클래스는 변경가능 하므로 멀티쓰레드 환경에서 안전하지 않았다.

* 멀티쓰레드 환경에서는 여러 쓰레드가 동시에 같은 객체에 접근할 수 있어서 변경 가능한 객체의 데이터가 잘못 될 가능성이 있다.
* 이를 쓰레드 안전(Thread-safe)하지 못하다고 한다.



###  java.time 패키지의 핵심 클래스

* 시간을 표현할 때는 `LocalTime`,
* 날짜를 표현할 때는 `LocalDate`,
* 모두 표현할 때는 `LocalDateTime`, -> LocalDate(날짜) + LocalTime(시간) = 날짜 & 시간
* 시간대(time-zone)까지 표현하려면 `ZonedDateTime` 사용. -> LocalDateTime + 시간대 

### 타임스탬프(time-stamp)

* 날짜와 시간을 초단위로 표현한 값.
* 이 값은 날짜와 시간을 하나의 정수로 표현할 수 있어서 날짜와 시간의 차이를 계산하거나 순서를 비교하는데 유리하다. 
  * (그래서 데이터베이스에서 많이 사용)



### Period와 Duration

* `Period` 클래스 : 두 날짜 간의 차이를 표현하기 위한것. 날짜 - 날짜
* `Duration` 클래스 : 시간의 차이를 표현하기 위한 클래스. 시간 - 시간 



### 객체 생성하기

java.time 패키지에 속한 클래스의 객체 생성은 `now()`와 `of()`로 한다.

- now() 예시

  ```java
  LocalDate date = LocalDate.now();
  LocalTime time = LocalTime.now();
  ```

- of() 예시

  ```java
  LocalDate date = LocalDate.of(2010, 10, 04); // 2010년 10월 4일(군입대ㅋ)
  LocalTime time = LocalTime.of(23, 59, 59); // 23시 59분 59초
  ```



### Temporal과 TemporalAmount

* LocalDate, LocalTime, LocalDateTime, ZonedDateTime 등 날짜와 시간을 표현하기 위한 모든 클래스들은 `Temporal`, `TemporalAccessor`, `TemporalAdjuster` 인터페이스를 구현했다.
* Duration과 Period는 `TemporalAmount` 인터페이스를 구현했다.



> temporal 과 chrono 라는 단어의 의미가 모두 시간(time)인데도 time 대신 굳이 이런 어려운 용어를 쓰는 이유는 시간(시 , 분, 초)과 더 큰 개념의 시간(년 월 일 시 분 초)를 구분하기 위해서이다.



### TemporalUnit과 TemporalField

`Chrono~`에 열거된 상수들은 날짜/시간 관련 메서드에서 요구하는 `Temporal~` 타입의 매개변수에 대입해서 사용하게 됨.

- `TemporalUnit`: 날짜와 시간의 단위를 정의한 인터페이스

  → `ChronoUnit`: `TemporalUnit`을 구현한 `enum`

- `TemporalField`: 연, 월, 일 등 날짜와 시간의 필드를 정의한 인터페이스

  → `ChronoField`: `TemporaField`을 구현한 `enum`



### LocalDate 클래스 메서드

| 메서드                       | 설명(1999-12-31 23:59:59)                  |
| :--------------------------- | :----------------------------------------- |
| **int getYear()**            | 년도(1999)                                 |
| **int getMonthValue()**      | 월(12)                                     |
| **Month getMonth()**         | 월(DECEMBER) getMonth().getValue() = 12    |
| **int getDayOfMonth()**      | 일(31)                                     |
| **int getDayOfYear()**       | 같은 해의 1월 1일부터 N번째 일(365)        |
| **DayOfWeek getDayOfWeek()** | 요일(FRIDAY) getDayOfWeek().getValue() = 5 |
| **int lengthOfMonth()**      | 같은 달의 총 일수(31)                      |
| **int lengthOfYear()**       | 같은 해의 총 일수(365), 윤년이면(366)      |
| **boolean isLeapYear()**     | 윤년여부 확인(false)                       |



### LocalTime 클래스 메서드

| 메서드              | 설명(1999-12-31 23:59:59) |
| :------------------ | :------------------------ |
| **int getHour()**   | 시(23)                    |
| **int getMinute()** | 분(59)                    |
| **int getSecond()** | 초(59)                    |
| **int getNano()**   | 나노초(0)                 |



- 위의 메서드 외에도 get(), getLong()이 있음

  - 원하는 필드를 직접 지정 가능
  - int get(TemporalField field)
  - long getLong(TemporalField field) → int를 넘을 때, 아래 상수 표의 *에 해당

  


### 상수

- ChronoField에 정의된 모든 상수를 아래에 나열 (ChronoField.상수 형식으로 사용)
- 사용할 수 있는 필드는 클래스마다 다름
- 해당 클래스가 지원하지 않는 필드 사용시, UnsupportedTemporalTypeException 발생
- (예) LocalDate는 날짜를 표현하기 위한 것으로, MINUTE_OF_HOUR과 같은 시간에 관련된 필드를 사용할 수 없음
- 종류

| 상수                             | 의미                                                         |
| :------------------------------- | :----------------------------------------------------------- |
| **ERA**                          | 시대                                                         |
| **YEAR_OF_ERA, YEAR**            | 년                                                           |
| **MONTH_OF_YEAR**                | 월                                                           |
| **DAY_OF_WEEK**                  | 요일(1: 월요일, 2: 화요일, … , 7: 일요일)                    |
| **DAY_OF_MONTH**                 | 일                                                           |
| **AMPM_OF_DAY**                  | 오전/오후                                                    |
| **HOUR_OF_DAY**                  | 시간(0~23)                                                   |
| **CLOCK_HOUR_OF_DAY**            | 시간(1~24)                                                   |
| **HOUR_OF_AMPM**                 | 시간(0~11)                                                   |
| **CLOCK_HOUR_OF_AMPM**           | 시간(1~12)                                                   |
| **MINUTE_OF_HOUR**               | 분                                                           |
| **SECOND_OF_MINUTE**             | 초                                                           |
| **MILLI_OF_SECOND**              | 천분의 일초 (=10^-3초)                                       |
| **MICRO_OF_SECOND \***           | 백만분의 일초 (=10^-6초)                                     |
| **NANO_OF_SECOND \***            | 십억분의 일초(=10^-9초)                                      |
| **DAY_OF_YEAR**                  | 그 해의 N번째 날                                             |
| **EPOCH_DAY \***                 | EPOCH(1970.1.1)부터 N번째 날                                 |
| **MINUTE_OF_DAY**                | 그 날의 N 번째 분(시간을 분으로 환산)                        |
| **SECOND_OF_DAY**                | 그 날의 N 번째 초(시간을 초로 환산)                          |
| **MILLI_OF_DAY**                 | 그 날의 N 번째 밀리초(=10^-3초)                              |
| **MICRO_OF_DAY \***              | 그 날의 N 번째 마이크로초(=10^-6초)                          |
| **NANO_OF_DAY \***               | 그 날의 N 번째 나노초(=10^-9초)                              |
| **ALIGNED_WEEK_OF_MONTH**        | 그 달의 n번째 주(1~7일 1주, 8~14일 2주, …)                   |
| **ALIGNED_WEEK_OF_YEAR**         | 그 해의 n번째 주(1월 1~7일 1주, 8~14일 2주, …)               |
| **ALIGNED_DAY_OF_WEEK_IN_MONTH** | 요일(그 달의 1일을 월요일로 간주하여 계산)                   |
| **ALIGNED_DAY_OF_WEEK_IN_YEAR**  | 요일(그 해의 1월 1일을 월요일로 간주하여 계산)               |
| **INSTANT_SECONDS**              | 년월일을 초단위로 환산 (1970-01-01 00:00:00 UTC를 0초로 계산) Instant에만 사용 가능 |
| **OFFSET_SECONDS**               | UTC와의 시차, ZoneOffset에만 사용 가능                       |
| **PROLEPTIC_MONTH**              | 년월을 월단위로 환산 (2019년 8월 = 2019 * 12 + 8)            |

* 만일 해당 클래스가 지원하지 않는 필드를 사용하면, UnsupportedTemporalTypeException 발생



  


### 필드 값 변경하기

날짜와 시간에서 특정 필드 값을 변경하려면 `with`로 시작하는 메소드 사용

### with 메서드들

- 종류

  ```java
  LocalDate withYear(int year)  
  LocalDate withMonth(int month)  
  LocalDate withDayOfMonth(int dayOfMonth)  
  LocalDate withDayOfYear(int dayOfYear)  
  
  LocalTime withHour(int hour)  
  LocalTime withMinute(int minute)  
  LocalTime withSecond(int second)  
  LocalTime withNano(int nanoOfSecond)
  ```

- with() 사용시 원하는 필드를 직접 지정 가능

  ```java
  LocalDate with(TemporalField field, long newValue)
  ```

- 필드를 변경하는 메소드들은 항상 새로운 객체를 생성해서 반환하므로 대입연산자 (=)를 같이 사용해야 한다.

  ```java
  date = date.withYear(2018); // 년도를 2018년으로 변경
  time = time.withHour(12); // 시간을 12시로 변경
  ```

- LocalTime의 truncatedTo() 는 지정된 것보다 작은 단위의 필드를 0으로 만든다.

  ```java
  LocalTime time = LocalTime.of(12, 34, 56); // 12시 34분 56초
  time = time.truncatedTo(ChronoUnit.HOURS); // 시(hour)보다 작은 단위를 0
  System.out.println(time); // 12:00
  ```



### plus() / minus() : 특정 필드에 값을 더하거나 빼는 plus() / minus()

```java
LocalTime plus(TemporalAmount amountToAdd)
LocalTime plus(long amountToAdd, TemporalUnit unit)
LocalDate plus(TemporalAmount amountToAdd)
LocalDate plus(long amountToAdd, TemporalUnit unit)
minus()도 위와 동일
```

  


- plus로 만든 메서드

  - ```java
    LocalDate plusYears(long yearsToAdd)
    LocalDate plusMonths(long monthsToAdd)
    LocalDate plusDays(long daysToAdd)
    LocalDate plusWeeks(long weeksToAdd)
    LocalTime plusHours(long hoursToAdd)
    LocalTime plusMinutes(long minutesToAdd)
    LocalTime plusSeconds(long secondsToAdd)
    LocalTime plusNanos(long nanosToAdd)
    ```

    

- LocalTime의 truncatedTo()

  - 지정된 것보다 작은 단위 필드를 0으로 만들어버림

    - 예시

    - ```java
      LocalTime time = LocalTime.of(12, 34, 56); // 12시 34분 56초
      time = time.truncatedTo(ChronoUnit.HOURS); // 시보다 작은 단위를 0으로 만듦
      System.out.println(time); // 12:00
      ```

      

    

  - LocalDate는 사용할 수 없음 ← 년, 월, 일은 0이 될 수 없기 때문

  - LocalTime의 turncatedTo()의 매개변수는 아래 표 중 시간과 관련된 필드만 사용 가능

#### 표 - 열거형 ChronoUnit에 정의된 상수 목록

| TemporalUnit(ChronoUnit) | 설명                         |
| :----------------------- | :--------------------------- |
| **FOREVER**              | Long.MAX_VALUE초(약 3천억년) |
| **ERAS**                 | 1,000,000,000년              |
| **MILLENNIA**            | 1,000년                      |
| **CENTURIES**            | 100년                        |
| **DECADES**              | 10년                         |
| **YEARS**                | 년                           |
| **MONTHS**               | 월                           |
| **WEEKS**                | 주                           |
| **DAYS**                 | 일                           |
| **HALF_DAYS**            | 반나절                       |
| **HOURS**                | 시                           |
| **MINUTES**              | 분                           |
| **SECONDS**              | 초                           |
| **MILLIS**               | 천분의 일초(=10^-3)          |
| **MICROS**               | 백만분의 일초(=10^-6)        |
| **NANOS**                | 십억분의 일초(=10^-9)        |





### 날짜와 시간 비교 - isAfter(), isBefore(), isEqual()

-  compareTo()

```java
int result = date1.compareTo(date2);
// 같으면 0, date1이 이전이면 -1, 이후면 1
```

- boolean형 메소드들

```java
boolean isAfter (ChronoLocalDate other)
boolean isBefore (ChronoLocalDate other)
boolean isEqual (ChronoLocalDate other) // localDate에만 있다
```



#### equals()가 있지만 isEqual()을 제공하는 이유

> **isEqual()은 오직 날짜만 비교. **
>
> **연표(chronology)가 다른 두 날짜를 비교하기 위해서 대부분의 경우 equals와 isEqual()의 결과는 같음**
>
> LocalDAte와 JapaneseDate 클래스를 비교하면 둘이 다르다.



### Instant

- 에포크 타임(EPOCH TIME, 1970-01-01 00:00:00 UTC)부터 경과된 시간을 나노초 단위로 표현
  - 사람에게는 불편하지만, 단일 진법으로만 다루기 때문에 계산하기 쉬움
  - 사람이 사용하는 날짜와 시간은 여러 진법이 섞여있기 때문에 계산이 어려움
- 생성 - now()와 ofEpochSecond 사용

```java
Instant now1 = Instant.now();
Instant now2 = Instant.ofEpochSecond(now.getEpochSecond());
Instant now3 = Instant.ofEpochSecond(now.getEpochSecond(), nowgetNano());
```



- 필드에 저장된 값 가져오기

  - ```java
    Instant now = Instant.now();
    long epochSec = now.getEpochSecond();
    int nano = now.getNano();
    ```

  - Instant는 시간을 초 단위와 나노초 단위로 나누어 저장

- toEpochMilli()

  - 오라클 데이터베이스의 타임스탬프(timestamp)처럼 밀리초 단위의 EPOCH TIME을 필요로 하는 경우를 위함
  - long toEpochMilli();

- Instant vs LocalTime

  - Instant : 항상 UTC(+00:00)를 기준
  - LocalTime : 한국은 시간대가 ‘+09:00’이므로 Instant와 9시간의 시간차이가 발생
  - 시간대를 고려해야하는 경우 OffsetDateTime을 사용하는 것이 더 나은 선택일 수 있음

- UTC란?

  - ‘Coordinated Universal Time’ : 세계 협정시
  - 1972년 1월 1일부터 시행된 국제 표준시
  - 이전에 사용되던 GMT(Greenwich Mean Time)와 UTC는 거의 같지만 UTC가 좀더 정확함

- instant와 Date간의 변환

  - Instant는 기존의 java.util.Date를 대체하기 위한 것

```java
//Instant → Date
static Date from(Instant instant)

// Date → Instant
Instant toInstant()
```



