## 형식화 클래스

###  DecimalFormat

- DecimalFormat을 이용하면 숫자 데이터를 정수, 부동소수점, 금액 등의 다양한 형식으로 표현할 수 있다.

- 반대로 일정한 형식의 텍스트 데이터를 숫자로 쉽게 변환도 가능하다.

- 형식화 클래스는 패턴을 정의하는 것이 전부다.

- DecimalFormat 사용법

  ```java
  double number = 1234567.89;
  
  // 1.원하는 출력형식의 패턴을 작성하여 DecimalFormat 인스턴스를 생성
  DecimalFormat df = new DecimalFormat("#.#E0"); 
  
  // 2.출력하고자 하는 문자열로 format 메소드를 호출
  String result = df.format(number);
  ```



* docs : https://docs.oracle.com/javase/7/docs/api/java/text/DecimalFormat.html

  


### SimpleDateFormat

- 날짜 데이터를 원하는 형태로 다양하게 출력

- 사용방법

  - 원하는 출력 형식의 패턴을 작성하여 SimpleDateFormat 인스턴스를 생성

  - 출력하고자 하는 Date 인스턴스를 가지고 format(Date d)를 호출

    ```java
    Date today = new Date();
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    String result = df.format(today);
    //오늘 날짜를 yyyy-MM-dd 형태로 반환
    ```

- `parse(String source)`를 사용하여 날짜 데이터의 출력형식 변환 가능

- SimpleDateFormat의 parse(String source) 는 문자열 source를 날짜 Date 인스턴스로 변환해줌

  ```java
  DateFormat df = new SimpleDateFormat("yyyy년 MM월 dd일");
  DateFormat df2 = new SimpleDateFormat("yyyy/MM/dd");
  Date d = df.parse("2018년 10월 3일");
  
  System.out.println(df2.format(d)); // 2018/10/03
  ```

* Date 인스턴스를 Calendar 인스턴스로 변환할 때는 Calendar클래스의 setTime()을 사용하면 된다.

  


#### Date And Time Patterns (시간과 날짜에 사용되는 패턴들)

| Letter | Date or Time Component                 | Presentation                                                 | Examples                                    |
| ------ | -------------------------------------- | ------------------------------------------------------------ | ------------------------------------------- |
| `G`    | 연대(BC, AD)                           | [Text](https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html#text) | `AD`                                        |
| `y`    | 년도                                   | [Year](https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html#year) | `1996`; `96`                                |
| `Y`    | 주 년                                  | [Year](https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html#year) | `2009`; `09`                                |
| `M`    | 올해의 달                              | [Month](https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html#month) | `July`; `Jul`; `07`                         |
| `w`    | 연중 주 (몇 번째 주)                   | [Number](https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html#number) | `27`                                        |
| `W`    | 월의 주 (월의 몇번째 주)               | [Number](https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html#number) | `2`                                         |
| `D`    | 년의 몇 번째 일 (1~366)                | [Number](https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html#number) | `189`                                       |
| `d`    | 월의 몇 번째 일(1~31)                  | [Number](https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html#number) | `10`                                        |
| `F`    | 월의 몇 번째 요일(1~5)                 | [Number](https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html#number) | `2`                                         |
| `E`    | 주의 요일 이름                         | [Text](https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html#text) | `Tuesday`; `Tue`                            |
| `u`    | 요일 번호(1 = 월요일, ..., 7 = 일요일) | [Number](https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html#number) | `1`                                         |
| `a`    | 오전/오후(AM/PM) 마커                  | [Text](https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html#text) | `PM`                                        |
| `H`    | 시간(0-23)                             | [Number](https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html#number) | `0`                                         |
| `k`    | 시간(1-24)                             | [Number](https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html#number) | `24`                                        |
| `K`    | 오전/오후 시간(0-11)                   | [Number](https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html#number) | `0`                                         |
| `h`    | 오전/오후 시간(1-12)                   | [Number](https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html#number) | `12`                                        |
| `m`    | 시간의 분                              | [Number](https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html#number) | `30`                                        |
| `s`    | 분당 초                                | [Number](https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html#number) | `55`                                        |
| `S`    | 밀리초                                 | [Number](https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html#number) | `978`                                       |
| `z`    | 시간대 (General time zone)             | [General time zone](https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html#timezone) | `Pacific Standard Time`; `PST`; `GMT-08:00` |
| `Z`    | 시간대 (RFC 822 timezone)              | [RFC 822 time zone](https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html#rfc822timezone) | `-0800`, `+0900`                            |
| `X`    | 시간대 (ISO 8601 timezone)             | [ISO 8601 time zone](https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html#iso8601timezone) | `-08`; `-0800`; `-08:00`                    |

* https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html

  


### ChoiceFormat

- ChoiceFormat은 특정 범위에 속하는 값을 문자열로 변환
- 연속적/불연속적인 범위의 값 처리에 유용(예를 들어 90점까지 A, 80점까지 B, ..)
- 패턴을 사용할 경우 limit#value 형태로 사용
  - 구분자로 `#`는 경계값 포함, `<`는 미포함

```java
public int someMethod(int score) {
 
  double[] limits = {60, 70, 80, 90}; // 낮은 값부터 큰 값의 순서로 적어야함
  // limits과 ㅎrades 간의 순서와 갯수를 맞춰야함
  String[] grades = {"D", "C", "B", "A"};
  
  ChoiceFormat form = new ChoiceFormat(limits, grades);
  
  return form.format(score);
}
```

   


###  MessageFormat

* MessageFormat은 데이터를 정해진 양식에 맞게 출력할 수 있도록 도와준다

```java
public void someMethod() {
  String msg = "Name : {0}, Tel: {1}, age:{2}, birthday:{3}";
  
  Object[] args = {"영수", "010-1111-1234", "28", "02-23"}
  
  String result = MessageFormat.format(msg, args);
}
```

