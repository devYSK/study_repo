### Period와 Duration

* `Period` 클래스 : 두 날짜 간의 차이를 표현하기 위한것. 날짜 - 날짜
* `Duration` 클래스 : 시간의 차이를 표현하기 위한 클래스. 시간 - 시간 







### duration에서 시/분/초/나노초 구하기 



- duration에서 시/분/초/나노초 구하기 - 1 ( 덜 안전함)
  - 불편하지만 사용할 수 있는 방법

```java
시간 : long hour = du.getSeconds() / 3600;
분 : long min = (du.getSeconds() - hour*3600) / 60;
초 : long sec = (du.getSeconds() - hour*3600 - min * 60) % 60;
나노 : nano = du.getNano();
```

- duration에서 시/분/초/나노초 구하기 - 2 (좀 더 안전한 방법)
  - Duration을 LocalTime으로 변환한 후 LocalTime이 가지고 있는 get 메서드들을 사용

```java
Local Time tmpTime = LocalTime.of(0,0).plusSeconds(du.getSeconds());

int hour = tmpTime.getHour();
int min = tmpTime.getMinute();
int sec = tmpTime.getSecond();
int nano = du.getNano();
```



> Period는 년월일을 분리해서 저장한다.

### between()과 until()의 차이

> `until()` 은 `between()`과 같은 역할이지만,
> `between()`은 static 메소드이고, `until()`은 인스턴스 메소드이다.
> D-day를 구하는 경우, 두 개의 매개변수를 받는 `until()`을 사용하는 것이 낫다.

   


```java
long sec = LocalTime.now().until(endTime, ChronoUnit.SECONDS);
```

  


- of()
  - Period : of(), ofYears(), ofMonths(), ofWeeks(), ofDays()
  - Duration : of(), ofDays(), ofHours(), ofMinutes(), ofSeconds() 등
  - 사용법 : LocalDate와 LocalTime과 다름

```java
Period pe = Period.of(1, 12, 31); // 1년 12개월 31일
Duration du = Duration.of(60, ChronoUnit.SECONDS); // 60초
// Duration du = Duration.ofSeconds(60); // 위의 문장과 동일
```

- with()

```java
pe = pe.withYear(2); // 1년에서 2년으로 변경. withMonths(), withDays();
du = du.withSeconds(120); // 60초에서 120초로 변경. withNanos()
```

- multipliedBy(), dividedBy()
  - plus(), minus()외에 곱셈과 나눗셈을 위한 메서드도 있음
  - Period - 나눗셈을 위한 메서드가 없음
    날짜와 기간을 표현하기 위한 것 : 나눗셈을 위한 메서드가 별로 유용하지 않기 떄문에 넣지 않은 것

```java
pe = pe.minusYear(1).multipliedBy(2); // 1년을 빼고, 2배를 곱한다
du = du.plusHours(1).dividedBy(60); // 1시간을 더하고 60으로 나눈다.
```

- isNegative(), isZero()
  - isNegative() : 음수인지 확인, isZero() : 0인지 확인
  - 두 날짜 또는 시간을 비교할 때, 사용하면 어느 쪽이 앞인지 또는 같은지 알아낼 수 있음

```java
boolean sameDate = Period.between(date1, date2).isZero();
boolean isBefore = Duration.between(time1, time2).isNegative();
```

- negate(), abs()
  - negate() : 부호를 반대로 변경, abs() : 부호를 없애는 것
  - Period에는 abs()가 없어서 다음 코드를 써야함

```java
duration = duration.abs();
if(duration.isNegative())
    duration = duration.negated();
```

- Period의 noramlized()
  - 이 메서드는 월(month)의 값이 12를 넘지 않게, 1년 13개월을 2년으로 바꿔줌
    일(day)의 길이는 일정하지 않으므로, 그대로 놔둠

```java
pe = Period.of(1,13,32).normalized();
// 1년 13개월 32일 > 2년 1개월 32일
```

- 다른 단위로 변환 - toTotalMonths(), toDays(), toHours(), toMinutes()
  - Period와 Duration을 다른 단위의 값으로 변환하는데 사용
  - get()은 특정 필드의 값을 그대로 가져오지만, 아래의 메서드들은 특정 단위로 변환한 결과를 반환한다는 차이
  - 반환타입은 모두 정수(long 타입)이라, 지정된 단위 이하의 값들은 버려진다는 뜻

| 클래스       | 메서드                  | 설명                                            |
| :----------- | :---------------------- | :---------------------------------------------- |
| **Period**   | **long toTotalMonth()** | 년월일을 월단위로 변환해서 반환(일 단위는 무시) |
| **Duration** | **long toDay()**        | 일단위로 변환해서 반환                          |
| **Duration** | **long toHours()**      | 시간단위로 변환해서 반환                        |
| **Duration** | **long toMinutes()**    | 분단위로 변환해서 반환                          |
| **Duration** | **long toMillis()**     | 천분의 일초 단위로 변환해서 반환                |
| **Duration** | **long toNanos()**      | 나노초 단위로 변환해서 반환                     |

