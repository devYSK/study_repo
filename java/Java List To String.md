# Java List to String array



자바에서 `List<String>` 타입을 String array (String 배열)로 변환하는 방법은 다음과 같다

<br>

<br>



## 1. `String.join 메소드`



```java
List<String> list = Arrays.asList("수", "영", "김");
String joined = String.join(" and ", list); 
```



---

### 2. `Collectios.joining 메소드`



```java

class Person {
  private String firstName;
  
  private String lastName; 
  
  
  public Person(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }
  
  public String getFirstName() {
    return this.firstName;
  }
  
  public String getLastName() {
    return this.lastName;
  }
  
}

// client code 

List<Person> list = Arrays.asList(
  new Person("영수", "김"),
  new Person("수영", "김"),
  new Person("별", "김")
);

String joinedFirstNames = list.stream()
  .map(Person::getFirstName)
  .collect(Collectors.joining(", "))
```



---

## 3. Java.util.StringJoiner

* https://docs.oracle.com/javase/8/docs/api/java/util/StringJoiner.html

 

```java
StringJoiner sj = new StringJoiner(":", "[", "]");
sj.add("George").add("Sally").add("Fred");

String desiredString = sj.toString();
 

List<Integer> numbers = Arrays.asList(1, 2, 3, 4);

String commaSeparatedNumbers = numbers.stream()
    .map(i -> i.toString())
    .collect(Collectors.joining(", "));
```



---

## 4. apach commns

* org.apache.commons.lang.StringUtils.join(list, conjunction);



<br>

---

## 5. Spring

* org.springframework.util.StringUtils.collectionToDelimitedString(list, conjunction);

