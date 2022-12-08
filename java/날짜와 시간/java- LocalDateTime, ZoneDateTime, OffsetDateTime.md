

# LocalDateTime

- `LocalDate`와 `LocalTime`을 합쳐서 `LocalDateTime`을 만들 수 있다.

  ```java
  LocalDate date = LocalDate.of(2018, 10, 05);
  LocalTime time = LocalTime.of(12,34,56);
  
  LocalDateTime dt = LocalDateTime.of(date, time);
  LocalDateTime dt2 = date.atTime(time);
  LocalDateTime dt3 = time.atDate(date);
  LocalDateTime dt4 = date.atTime(12, 34 ,56);
  LocalDateTime dt5 = time.atDate(LocalDate.of(2018, 10, 05));
  LocalDateTime dt6 = date.atStartOfDay(); // = date.atTime(0,0,0);
  
  LocalDateTime dateTime = LocalDateTime.of(2018, 10, 05, 12, 34, 56);
  LocalDateTime today = LocalDateTime.now();
  ```

- 반대로 `LocalDateTime`을 `LocalDate` 또는 `LocalTime`으로 변환할 수 있다.

  ```java
  LocalDateTime dt = LocalDateTime.of(2018, 10, 05, 12, 34, 56);
  LocalDate date = dt.toLocalDate();
  LocalTime time = dt.toLocalTime();
  ```



# ZonedDateTime

> LocalDateTime에 타임존(tiem-zone)을 추가하면 ZonedDateTime이 된다.
> ZoneId는 일광 절약시간(DST, Daylight Saving Time)을 자동으로 처리해준다.
> LocalDateTime에 atZone()으로 시간대 정보를 추가하면, ZonedDateTime을 얻을 수 있다.

```java
ZoneId zid = ZoneId.of("Asia/Seoul");
ZonedDateTime zdt = dateTime.atZone(zid);
```

> 특정 타임존의 시간 알기

```java
ZoneId nyId = ZoneId.of("America/New_York");
ZonedDateTime nyTime = ZonedDateTime.now().withZoneSameInstant(nyId);
// now() 대신 of() 사용하여 날짜&시간 지정 가능
```

> UTC로부터 얼마나 떨어져있는지를 ZoneOffSet으로 표현



- ZonedDateTime의 변환
  - LocalDateTime처럼 날짜와 시간에 관련된 다른 클래스로 변환하는 메서드들을 가지고 있음

```java
LocalDate toLocalDate()

LocalTime toLocalTime()

LocalDateTime toLocalDateTime()

offsetDateTime toOffsetDateTime()

long toEpochSecond()

Instant toInstant()
```





> **사용 가능한 ZoneId의 목록은 ZoneId.getAvailableZonelds()로 얻을 수 있음**
>
> `Set<String>` 반환

```
Asia/Aden
America/Cuiaba
Etc/GMT+9
Etc/GMT+8
Africa/Nairobi
America/Marigot
Asia/Aqtau
Pacific/Kwajalein
America/El_Salvador
Asia/Pontianak
Africa/Cairo
...

```

* 진짜 많다.. 100개넘는듯? 

### 



# OffsetDateTime

> `ZonedDateTime`은 ZoneId로 구역을 표현하는데,
> ZoneOffset을 사용하는 것이 `OffsetDateTime` 이다.
> ZoneId는 일광절약시간 처럼 타임존과 관련된 규칙들을 포함하지만,
> ZoneOffset은 단지 시간대를 시간의 차이로만 구분

- → 컴퓨터에게 일광절약시간처럼 계절별로 시간을 더했다 뺐다 하는 행위는 위험함
- → 아무런 변화 없이 일관된 시간체계를 유지하는 것이 더 안전함
  - 같은 지역내의 컴퓨터간 데이터를 주고받을 때, 전송시간 : LocalDateTime이면 충분
  - 다른 지역의(다른 시간대에) 존재하는 컴퓨터간의 통신 : OffSetDateTime이 필요



### 