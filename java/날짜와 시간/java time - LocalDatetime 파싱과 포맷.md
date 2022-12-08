

# 파싱과 포맷



- 날짜와 시간을 원하는 형식으로 출력하고 해석(파싱, parsing)하는 방법
- 형식화(formatting)와 관련된 클래스들은` java.time.format패키지`에 들어있음
  - 이 중 `DateTimeFormatter`가 핵심

```java
LocalDate date = LocalDate.of(2016, 1, 2);

String yyyymmdd = DateTimeFormatter.ISO_LOCAL_DATE.format(date); // "2016-01-02"
String yyyymmdd = date.format(DateTimeFormatter.ISO_LOCAL_DATE); // "2016-01-02"
```

- DateTimeFormatter
  - 자주 쓰이는 다양한 형식들을 기본적으로 정의
  - 그 외의 형식이 필요하다면 직접 정의해서 사용할 수 있음
- DateTimeFormatter에 상수로 정의된 형식들의 목록

| DateTimeFormatter             | 설명                                | 보기                                  |
| :---------------------------- | :---------------------------------- | :------------------------------------ |
| **ISO_DATE_TIME**             | **Date and time with ZoneId**       | 2019-09-14T08:59:23+09:00[Asia/Seoul] |
| **ISO_LOCAL_DATE**            | **ISO Local Date**                  | 2019-09-14                            |
| **ISO_LOCAL_TIME**            | **Time without offset**             | 08:59:23                              |
| **ISO_LOCAL_DATE_TIME**       | **ISO Local Date and Time**         | 2019-09-14T08:59:23                   |
| **ISO_OFFSET_DATE**           | **ISO Date with Offset**            | 2019-09-14+09:00                      |
| **ISO_OFFSET_TIME**           | **Time with offset**                | 08:59:23+09:00                        |
| **ISO_OFFSET_DATE_TIME Date** | **Time with Offset**                | 2019-09-14T08:59:23+09:00             |
| **ISO_ZONED_DATE_TIME**       | **Zoned Date Time**                 | 2019-09-14T08:59:23+09:00[Asia/Seoul] |
| **ISO_INSTANT**               | **Date and Time of an Instant**     | 2019-09-14T08:59:23Z                  |
| **BASIC_ISO_DATE**            | **Basic ISO Date**                  | 20190914                              |
| **ISO_DATE**                  | **ISO Date with or without offset** | 2019-09-14+09:00 2019-08-14           |
| **ISO_TIME**                  | **Time with or without offset**     | 10:15:30+01:00 10:15:30               |
| **ISO_ORDINAL_DATE**          | **Year and day of year**            | 2020-349                              |
| **ISO_WEEK_DATE**             | **Year and Week**                   | 2020-W51-2                            |
| **RFC_1123_DATE_TIME**        | **RFC 1123 / RFC 822**              | Wed, 14 NOV 2019 08:59:23 GMT         |



### DateTimeFormatter의 static메서드 ofLocalizedDate(), ofLocalizedTime(), ofLocalizedDateTime()

- 로케일에 종속된 형식화 : 로케일(locale)에 종속된 포메터를 생성

```java
DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);
String shortFormat = formatter.format(LocalDate.now());
```

- 로케일에 종속된 형식화 - FormatStyle의 종류에 따른 출력 형태

| FormatStyle | 날짜                    | 시간               |
| :---------- | :---------------------- | :----------------- |
| **FULL**    | 2015년 11월 28일 토요일 | N/A                |
| **LONG**    | 2015년 11월 28일 (토)   | 오후 9시 15분 13초 |
| **MEDIUM**  | 2015. 11. 28            | 오후 9:15:13       |
| **SHORT**   | 15. 11. 28              | 오후 9:15          |





###  출력형식 정의

> DateTimeFormatter의 ofPattern()으로 원하는 출력형식을 작성할 수 있다.

```java
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
```



| 기호         | 의미                                               | 보기           |
| :----------- | :------------------------------------------------- | :------------- |
| **G**        | **연대(BC, AD)**                                   | 서기 또는 AD   |
| **y 또는 u** | **년도**                                           | 2015           |
| **M 또는 L** | **월(1~12 또는 1월 ~ 12월)**                       | 11             |
| **Q 또는 q** | **분기(quarter)**                                  | 4              |
| **w**        | **년의 몇 번째 주(1~53)**                          | 48             |
| **W**        | **월의 몇 번째 주(1~5)**                           | 4              |
| **D**        | **년의 몇 번째 일(1~366)**                         | 332            |
| **d**        | **월의 몇 번째 일(1~31)**                          | 28             |
| **F**        | **월의 몇 번째 요일(1~5)**                         | 4              |
| **E 또는 e** | **요일**                                           | 토 또는 7      |
| **a**        | **오전/오후(AM, PM)**                              | 오후           |
| **H**        | **시간(0~23)**                                     | 22             |
| **k**        | **시간(1~24)**                                     | 22             |
| **K**        | **시간(0~11)**                                     | 10             |
| **h**        | **시간(1~12)**                                     | 10             |
| **m**        | **분(0~59)**                                       | 12             |
| **s**        | **초(0~59)**                                       | 35             |
| **S**        | **천 분의 일 초(0~999)**                           | 7              |
| **A**        | **천 분의 일 초 (그 날의 0시 0분 0초부터의 시간)** | 80263808       |
| **n**        | **나노초(0~999999999)**                            | 475000000      |
| **N**        | **나노초 (그 날의 0시 0분 0초부터의 시간)**        | 81069992000000 |
| **V (VV)**   | **시간대 ID**                                      | Asia/Seoul     |
| **z**        | **시간대(time-zone) 이름**                         | KST            |
| **O**        | **지역화된 zone-offset**                           | GMT+9          |
| **Z**        | **zone-offset**                                    | +0900          |
| **X또는 x**  | **zone-offSet(Z는 +00:00를 의미)**                 | +09            |
| **‘**        | **escape 문자 (특수문자를 표현하는데 사용)**       | 없음           |



### 문자열늘 날짜와 시간으로 파싱하기

```java
static LocalDateTime parse(CharSequence text);
static LocalDateTime parse(CharSequence text, DateTimeFormatter formatter);
```



- parse() : 문자열을 날짜와 시간으로 파싱하는 메서드

  - 문자열을 날짜 또는 시간으로 변환할 때 사용
  - 오버로딩 된 메서드가 여러개 있다

  

* 상수로 정의된 형식을 사용할 때는 다음과 같이 사용한다.

```java
LocalDate date = LocalDate.parse(“2019-08-19”, DateTimeFormatter.ISO_LOCAL_DATE);
```



- 자주 사용되는 기본적인 형식의 문자열은 ISO_LOCAL_DATE와 같은 형식화 상수를 사용하지 않아도 됨

```java
LocalDate newDate = LocalDate.parse(“2019-08-19”);
LocalTime newTime = LocalTime.parse(“12:31:30”);
LocalDateTime newDateTime = LocalDateTime.parse(“2019-08-19T12:31:30”);
```





- ofPattern()을 이용하여 파싱할 수도 있음

```java
DateTimeFormatter pattern = DateTimeFormatter.ofPattern(“yyyy-MM-dd HH:mm:ss”);
LocalDateTime endOfYear = LocalDateTime.parse(“2019-08-19 12:31:30”, pattern);
```

