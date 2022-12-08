

#  TemporalAdjusters

- 자주 쓰일만한 날짜 계산들을 대신 해주는 메서드를 정의해 놓은 클래스
  - 예시 : 다음 주 월요일의 날짜를 계산할 때 TemporalAdjusters에 정의된 next()를 사용한 예

```java
LocalDate today = LocalDate.now();
LocalDate nextMonday = today.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
```

- 메서드 종류

| 메서드                                                 | 설명                    |
| :----------------------------------------------------- | :---------------------- |
| **firstDayOfNextYear()**                               | 다음 해의 첫 날         |
| **firstDayOfNextMonth()**                              | 다음 달의 첫 날         |
| **firstDayOfYear()**                                   | 올 해의 첫 날           |
| **firstDayOfMonth()**                                  | 이번 달의 첫 날         |
| **lastDayOfYear()**                                    | 올 해의 마지막 날       |
| **lastDayOfMonth()**                                   | 이번 달의 마지막 날     |
| **firstInMonth(DayOfWeek dayOfWeek)**                  | 이번 달의 첫 번째 ?요일 |
| **lastInMonth(DayOfWeek dayOfWeek)**                   | 이번 달의 마지막 ?요일  |
| **previous(DayOfWeek dayOfWeek)**                      | 지난 ?요일(당일 미포함) |
| **previousOrSame(DayOfWeek dayOfWeek)**                | 지난 ?요일(당일 포함)   |
| **next(DayOfWeek dayOfWeek)**                          | 다음 ?요일(당일 미포함) |
| **nextOrSame(DayOfWeek dayOfWeek)**                    | 다음 ?요일(당일 포함)   |
| **dayOfWeekInMonth(int ordinal, DayOfWeek dayOfWeek)** | 이번 달의 n번째 ?요일   |





#### TemporalAdjuster 인터페이스

```java
@FuntionalInterface
public interface TemporalAdjuster {
    Temporal adjustInto(Temporal temporal);
}
```



- 추상메서드 하나만 정의되어있고, 이 메서드만 구현하면 됨
- 실제로 구현하는 것은 adjustInto()지만, 같이 사용해야 하는 메서드는 with()
  어느 쪽을 사용해도 되긴 함
  adjustInto() : 내부적으로 사용할 의도로 작성
  with() : 되도록 사용할 것
- adjustInto()의 매개변수
  날짜와 시간에 관련된 대부분의 클래스가 Temporal 인터페이스를 구현하였기 때문에 adjustInto의 매개변수가 될 수 있음



#### TemporalAdjuster 직접 구현하기

- 보통은 TemporalAdjusters에 정의된 메서드로 충분
- 필요할 경우, 자주 사용되는 날짜계산을 해주는 메서드를 직접 만들 수도 있음
- LocalDate의 with는 다음과 같이 정의되어있고, TemporalAdjuster인터페이스를 구현한 클래스의 객체를 매개변수로 제공
  LocalDate With(TemporalAdjuster adjuster)
- with()
  LocalTime, LocalDateTime, ZonedDateTime, Instant 등 대부분의 날짜와 시간에 관련된 클래스에 포함되어있음
- TemporalAdjuster 인터페이스 구현
- 날짜와 시간에 관련된 대부분의 클래스가 Temporal 인터페이스를 구현하였기 때문에 adjustInto의 매개변수가 될 수 있음
- 예제) 특정 날짜로부터 2일 후의 날짜를 계산하는 DayAfterTomorrow

```java
Class DayAfterTomorrow implements TemporalAdjuster {
     @Override
     public Temporal adjustInto(Temporal temporal) {
         return temporal.plus(2, ChronoUnit.DAYS); // 2일을 더하기
     }
}
```

