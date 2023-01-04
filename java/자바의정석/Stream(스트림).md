# Stream

- 스트림은 데이터의 연속적인 흐름
- 다양한 데이터 소스를 표준화된 방법으로 다루기 위한 것 - 데이터 소스 추상화
  - 무엇이던 간에 같은 방식으로 다룰 수 있게 되었다는 것과 코드의 재사용성이 높아진다는 것을 의미
- jdk1.8에서부터 통일됨
- 스트림 사용3단계 1.스트림만들기 2.중간연산 3.최종연산
- 중간연산, 연산결과가 스트림인 연산 '반복적'으로 적용가능하다
- 최종연산, 연산결과가 스트림이 아닌 연산 '한번만' 적용가능(스트림 요소를 소모)



> Collection이나 Iterator 같은 인터페이스를 이용해서 컬렉션을 다루는 방식을 표준화 했지만, 각 컬렉션 클래스에는 같은 기능의 메서드들이 중복해서 정의되어 있다. List를 정렬할 때는 Collection.sort()를 사용해야하고, 배열을 정렬할 때는 Arrays.sort()를 사용해야 한다. 이렇게 데이터 소스마다 다른 방식으로 다루어야하는 `문제점을 해결해주는 것이 Stream 이다.`



예를 들어서 문자열 배열과 같은 내용의 문자열을 저장하는 List가 있을 때

```java
String[] strArr = { "aaa", "ddd", "ccc" };
List<String> strList = Arrays.asList(strArr);
```

이 두 데이터 소스를 기반으로 하는 스트림은 아래 코드와 같이 생성한다.

```java
Stream<String> strStream1 = strList.stream(); // 스트림을 생성
Stream<String> strStream2 = Arrays.stream(strArr); // 스트림을 생성
```

이 두 스트림으로 데이터 소스의 데이터를 읽어서 정렬하고 화면에 출력하는 방법은 아래 코드와 같다.

* 기존 데이터 소스가 정렬되는 것은 아니라는 것에 유의하자. 
* 정렬을 한 상태로 출력해주는것 뿐이다. 

```java
strStream1.sorted().forEach(System.out::println);
strStream2.sorted().forEach(System.out::println);
```



# 스트림의 주요 특징



## 스트림은 ReadOnly이다. 즉 데이터 소스로부터 데이터를 읽기만 할 뿐 변경하지 않는다.

스트림은 데이터 소스로부터 데이터를 읽기만 할 뿐, 데이터 소스를 변경하지 않는다는 차이가 있다.
필요하다면 정렬된 결과를 컬렉션이나 배열에 담아서 반환할 수도 있다.

```java
//정렬된 결과를 새로운 List에 담아서 반환한다.
List<String> sortedList = strStream2.sorted().collect(Collections.toList());
```



## 스트림은 Iterator처럼 일회용이다. 

스트림은 Iterator처럼 일회용이다.
Iterator로 컬렉션의 요소를 모두 읽고 나면 다시 사용할 수 없는 것처럼,
스트림도 한 번 사용하고 나면 닫혀서 다시 사용할 수 없다.
필요하다면 스트림을 다시 생성해야한다.

```java
strStream1.sorted().forEach(System.out::println);
int numOfStr = strStream1.count(); // 에러. 스트림이 이미 닫혔음
```



## 스트림은 작업을 내부 반복으로 처리한다.

스트림을 이용한 작업이 간결할 수 있는 비결중의 하나가 바로 '내부 반복'이다.
내부 반복이라는 것은 반복문을 메서드의 내부에 숨길 수 있다는 것을 의미한다.
forEach()는 스트림에 정의된 메서드 중의 하나로 매개변수에 대입된 람다식을 데이터 소스의 모든 요소에 적용한다.

```java
for(String str : strList)
	System.out.println(str);

==>
stream.forEach(System.out::println);
```

* 메서드 참조 System.out::println를 람다식으로 표현하면 (str) -> System.out.println(str)과 같다.

즉, forEach()는 메서드 안으로 for문을 넣은 것이다.

수행할 작업은 매개변수로 받는다.

```java
void forEach(Consumer<? super T> action) {
	Objects.requireNonNull(action); // 매개변수의 null을 체크
    
    for(T t : src) {	// 내부 반복
    	action.accept(T);
    }
}
```



## **스트림의 연산**

스트림이 제공하는 다양한 연산을 이용해서 복잡한 작업들을 간단하게 처리할 수 있다.
마치 데이터베이스에 SELECT문으로 질의(쿼리, query)하는 것과 같은 느낌이다.

> 스트림에 정의된 메서드 중에서 데이터 소스를 다루는 작업을 수행하는 것을 연산(operation)이라고 한다.

스트림이 제공하는 연산은 중간 연산과 최종 연산으로 분류할 수 있는데,
중간 연산은 연산결과를 스트림으로 반환하기 때문에 중간 연산을 연속해서 연결할 수 있다.

`반면에 최종 연산은 스트림의 요소를 소모하면서 연산을 수행 하므로 단 한번만 연산이 가능하다.`

> **중간 연산 :** 연산 결과가 스트림인 연산. 스트림에 연속해서 중간 연산할 수 있음
> **최종 연산 :** 연산 결과가 스트림이 아닌 연산. 스트림의 요소를 소모하므로 단 한번만 가능하다.



```java
stream.distinct().limit(5).sorted().forEach(System.out::println)

distinct() : 중간 연산
limit(5) : 중간 연산
sorted() : 중간 연산
forEach(System.out::println) : 최종 연산
```

모든 중간 연산의 결과는 스트림이지만, 연산 전의 스트림과 같은 것은 아니다.
위 문장과 달리 모든 스트림 연산을 나눠서 쓰면 아래와 같다.
각 연산의 반환 타입을 눈여겨 보자.

```java
String[] strArr = {"dd, "aaa", "CC", "cc", "b"}; // 배열 소스 생성
  
Stream<String> stream = Stream.of(strArr); // 문자열 배열이 소스인 스트림
Stream<String> filteredStream = stream.filter(); // 걸러내기 (중간 연산)
Stream<String> distinctedStream = stream.distinct(); // 중복제거 (중간 연산)
Stream<String> sortedStream = stream.sort(); // 정렬 (중간 연산)
Stream<String> limitedStream = stream.limit(5); // 스트림 자르기 (중간 연산)
int total = stream.count(); // 요소 개수 세기(최종 연산)
```



## 스트림의 중간 연산 목록

```java
Stream<T> distinct() -  중복을 제거

Stream<T> filter(Predicate<T> predicate) - 조건에 안 맞는 요소 제외

Stream<T> limit (long maxSize)     - 스트림의 일부를 잘라낸다

Stream<T> skip(long n) - 스트림의 일부를 건너뛴다.

Stream<T> peek(Consumer<T> action) - 스트림의 요소에 작업을 수행

Stream<T> sorted() - 스트림의 요소를 정렬한다.

Stream<T> sorted(Comparator<T> comparator) - 스트림의 요소를 정렬한다.
  

//스트림의 요소를 변환한다.
Stream<R> map(Function<T,R> mapper)

DoubleStream mapToDouble(ToDoubleFunction<T> mapper)

IntStream mapToInt(ToIntFunction<T> mapper)

LongStream mapToLong(ToLongFunction<T> mapper)

Stream<R> flatMap(Function<T,Stream<R>> mapper)

DoubleStream flatMapToDouble(Function<T.DoubleStream> m)

IntStream flatMapToInt(Function<T, IntStream> m)

LongStream flatMapToLong(Function<T,LongStream> m)
```



## 스트림의 최종 연산 목록

```java
void forEach(Consumer<? super T> action>) - 각요소 에 지정된 작업 실행

void forEachOrdered(Consumer<? super T>action) - 각 요소에 지정된 작업 수행

long count() - 스트림의 요소의 개수 반환

Optional<T> max (Comparator<? super T> comparator) - 스트림의 최대값을 반환

Optional<T> min (Comparator<? super T> comparator) 스트림의 최소값을 반환

//스트림의 요소 하나를 반환
Optional<T> findAny() //아무거나 하나

Optional<T> findFirst() //첫번째 요소


// 주어진 조건을 모든 요소가 만족시키는지, 만족시키지 않는지 확인
boolean allMatch(Predicate<T> p) //모두 만족 하는지

boolean anyMatch(PredicaAte<T> p ) //하나라도 만족하는지

boolean nonMatch(Predicate<> p) //모두 만족하지 않는지



//스트림의 모든 요소를 배열로 반환
Object[] toArray()    

A[] toArray(IntFunction<A[]> generator)

// 스트림의 요소를 하나씩 줄여가면서(리듀싱) 계산한다.
Optional<T> reduce(BinaryOperator<T> accumulator)

T reduce (T identity, BinaryOperator<T> accumulator)

U reduce (U identity, BiFunction<U,T,U> accumulator, BinaryOperator<U> combiner)

//스트림의 요소를 수집한다. 주로 요소를 그룹화하거나 분할한 결과를 컬렉션에 담아 반환하는데 사용된다.

R collect(Collector<T,A,R> collector)

R collect(Supplier<R> supplier, BiConsumer<R,T> accumulator, BiConsumer<R,R> combiner)
```

| 연산    | 비고                                                 |
| ------- | ---------------------------------------------------- |
| forEach | 스트림에 각 요소를 람다를 통해 특정 작업을 실행한다. |
| count   | 스트림의 요소 개수를 반환한다. (long)                |
| collect | 스트림을 컬렉션 형태로 반환한다.                     |

* 중간 연산은 map()과 flatMap(), 최종연산은 reduce()와 collect()가 핵심이다.



## **지연된 연산**
스트림 연산에서 한 가지 중요한 점은  `최종 연산이 수행되기 전까지는 중간 연산이 수행되지 않는다` 는 것이다.
스트림에 대해서 distinct()나 sort()같은 중간 연산을 호출해도 즉각적인 연산이 수행되는 것은 아니다.

* 중간 연산을 호출하는 것은 단지 어떤 작업이 수행되어야하는지를 `지정해주는 것일 뿐`이다.
* 최종 연산이 수행되어야 비로소 스트림의 요소들이 중간 연산을 거쳐 최종 연산에서 소모된다.



## Primitive Type Stream - 기본형 스트림. 
오토박싱&언방식으로 인한 비효율을 줄이기 위해서 데이터 소스의 요소를 기본형(Primitive Type) 으로 다루는 스트림

* IntStream
* LongStream
* DoubleStream

일반적으로 Stream< Integer> 대신 IntStream을 사용하는 것이 더 효율적이고,
IntStream에는 int타입의 값으로 작업하는데 유용한 메서드들이 포함되어 있다.



## **병렬 스트림**
- 병렬스트림은 내부적으로 fork&join 프레임웍을 이용해서 자동적으로 연산을 병렬로 수행한다. 
- 스트림에 parallel() 메서드를 호출하면 병렬로 연산하고, parallel()을 취소하려면 sequential()을 호출한다.
- parallel()과 sequential()은 새로운 스트림을 생성하는 것이 아니라, 그저 스트림의 속성을 변경할 뿐이다.

모든 스트림은 기본적으로 병렬 스트림이 아니므로 sequential()을 호출할 필요가 없다.
sequential( 메서드는 parallel()을 호출하는 것을 취소할 때만 사용한다.

> parallel()과 sequential()은 새로운 스트림을 생성하는 것이 아니라, 그저 스트림의 속성을 변경할 뿐이다.

```java
int sum = strStream.parallel() // strStream을 병렬 스트림으로 전환
				   .mapToint (s->s.length())
           .sum();
```

* 병렬처리가 항상 더 빠른 결과를 얻게 해주는 것은 아니라는 것을 명심하자.



# 스트림 만들기

스트림의 소스가 될 수 있는 대상은 배열, 컬렉션, 임의의 수 등 다양하다.



## 컬렉션(Collection)

컬렉션의 최고 조상인 Collection에 stream()이 정의되어 있어서,  
 Collection의 자손인 List와 Set을 구현한 컬렉션 클래스들은 모두 stream()으로 스트림을 생성할 수 있다.

```java
Stream<T> Collection.stream()

// List로부터 스트림 생성
List<Integer> list = Arrays.asList(1,2,3,4,5);
Stream<Integer> intStream = list.stream();  // list를 소스로 하는 컬렉션 생성
```



## 배열

배열을 소스로 하는 스트림을 생성하는 메서드는 다음과 같이 Stream과 Arrays에 static메서드로 정의되어 있다.

```java
Stream<T> Stream.of(T... values)  // 가변 인자
Stream<T> Stream.of(T[])
Stream<T> Arrays.stream(T[])
Stream<T> Arrays.stream(T[] array, int startInclusive, int endExclusive)
```

이 외에도 long과 double타입의 배열로부터 LongStream과 DoubleStream을 반환하는메서드들이 있다.

### 문자열 스트림 생성 

```java
Stream<String> strStream = Stream.of("a","b","c"); // 가변인자
Stream<String> strStream = Stream.of(new String[]{"a","b","c"});
Stream<String> strStream = Arrays.stream(new String[]{"a","b","c"});
Stream<String> strStream = Arrays.stream(new String[]{"a","b","c"}, 0, 3);
```

### int, long, double과 같은 기본형 배열을 소스로 하는 스트림 생성

```java
IntStream IntStream.of(int...values)
IntStream IntStream.of(int[])
IntStream Arrays.stream(int[])
IntStream Arrays.stream(int[] array, int startInclusive, endExclusive)
```



### IntStream - **특정 범위의 정수**

IntStream과 LongStream은 다음과 같이 지정된 범위의 연속된 정수를 스트림으로 생성해서
반환하는 rance()와 rangeClosed()를 갖고 있따.

```java
IntStream IntStream.range(int begin, int end)
IntStream IntStream.rangeClosed(int begin, int end)
```

range()의 경우 경계의 끝인 end가 범위에 포함되지 않고, rangeClosed()의 경우는 포함된다.

```java
IntStream intStream = IntStream.range(1,5); // 1,2,3,4
IntStream intStream = IntStream.rangeClosed(1,5); // 1,2,3,4,5
```



### 임의의 수

난수를 생상하는 Random클래스에는 아래와 같은 인스턴스 메서드들이 포함되어 있다.

이 메서드들은 해당 타입의 난수들로 이뤄진 스트림을 반환한다.

```java
IntStream 	 ints()
LongStream 	 longs()
DoubleStream 	 doubles()
```

* 이 메서드들이 반환하는 스트림은 크기가 정해지지 않은 '무한 스트림(infinite stream)'이므로
  `limit()도 같이 사용해서 스트림의 크기를 제한해 줘야 한다.` 

* limit()은 스트림의 개수를 지정하는데 사용되며, 무한 스트림을 유한 스트림으로 만들어 준다.

```java
IntStream intStream = new Random().ints(0; // 무한 스트림
intStream.limit(5).forEach(System.out::println); // 5개의 요소만 출력한다.
```



매개변수로 스트림의 크기를 지정하면 limit()을 사용하지 않아도 된다.

```java
Int Stream	ints(long streamSize)
Long Stream	long(long StreamSize)
Double Stream	doubles(long streamSize)

IntStream intStream = new Random().ints(5); // 크기가 5인 난수 스트림을 반환한다.
```



지정된 범위(begin~end)의 난수를 발생시키는 스트림을 얻는 메서드는 아래와 같다.
단, end는 범위에 포함되지 않는다.

```java
IntStream    ints(int begin, int end)
LongStream   longs(long begin, long end)
DoubleStream doubles(double begin, double end)

IntStream    ints(long streamSize, int begin, int end)
LongStream   longs(long streamSize, long begin, long end)
DoubleStream doubles(long streamSize, double begin, double end)
```



### 람다식 - iterate(), generate()

Stream 클래스의 iterate()와 generate()는 람다식을 매개변수로 받아서, 이 람다식에 의해 계산되는 값들을 요소로 하는 

`무한 스트림을 생성한다.`

```java
static <T> Stream<T> iterate(T seed, UnaryOperator<T> f)
static <T> Stream<T> generate(Supplier<T> s)
```

- iterate()는 씨앗값(seed)으로 지정된 값부터 시작해서 람다식 f에 의해 계산된 결과를 다시 seed값으로 계산을 반복한다.
- generate()는 iterate()와 달리 이전 결과를 이용해서 다음 요소를 계산하지 않는다.
- iterate()와 generate()에 의해 생성된 스트림은 기본형 스트림 타입의 참조변수로 다룰 수 없다.

generate()도 iterate()처럼, 람다식에 의해 계산되는 값을 요소로 하는 무한 스트림을 생성해서 반환하지만,
iterate()와 달리, 이전 결과를 이용해서 다음 요소를 계산하지 않는다.

```java
Stream<Double> randomStream = Stream.generate(Math::random);
Stream<Integer> oneSTream = Stream.generate(()->1);
```

그리고 generate()에 정의된 매개변수의 타입은 Supplier< T>이므로 매개변수가 없는 람다식만 허용된다.
한 가지 주의할 점은 iterate()와 generate()에 의해 생성된 스트림을
아래 코드와 같이 기본형 스트림 타입의 참조변수로 다룰 수 없다는 것이다.

```java
IntStream evenSTream = Stream.iterate(0, n -> n + 2); // 에러
DoubleStream randomStream = Strea.generate(Math::random); // 에러
```

굳이 필요하다면 아래 코드처럼 mapToInt()와 같은 메서드로 변환을 해야 한다.

```java
IntStream evenStream = Stream.iterate(0, n-> n + 2).mapToInt(Integer::valueOf);
Stream<Integer> stream = evenSTream.boxed(); // IntStream -> Stream<Integer>
```

반대로 IntStream타입의 스트림을 Stream< Integer> 타입으로 변환하려면, boxed()를 사용하면 된다.



### 파일 스트림 (file stream)

java.nio.file.Files는 파일을 다루는데 필요한 유용한 메서드들을 제공하는데,
list()는 지정된 디렉토리(dir)에 있는 파일의 목록을 소스로 하는 스트림을 생성해서 반환한다.

>  Path는 하나의 파일 또는 경로를 의미한다.

```java
Stream<Path> Files.list(Path dir)
```

이 외에도 Files클래스에는 Path를 요소로 하는 스트림을 생성하는 메서드가 더 있지만,
이 장의 주제를 벗어나므로 설명을 생략한다.

그리고, 파일의 한 행(line)을 요소로 하는 스트림을 생성하는 메서드도 있다.
아래의 세번째 메서드는 BufferedReader클래스에 속하는 것인데,
파일 뿐만 아니라 다른 입력대상으로부터도 데이터를 행단위로 읽어올 수 있다.

```java
Stream<String> Files.lines(Path path)
Stream<String> Files.lines(Path path, Charest cs)
Stream<String> lines() // BufferedReader클래스의 메서드
```



### 빈 스트림 (Empty Stream)

요소가 하나도 없는 빈 스트림을 생성할 수 있다.

>  스트림에 연산을 수행한 결과가 하나도 없을 때, null보다 빈 스트림을 반환하는 것이 낫다.

```java
Stream emptyStream = Stream.empty();  // empty()는 빈 스트림을 생성해서 반환한다.
long count = emptyStream.count();   // count의 값은 0
```



### 두 스트림의 연결

Stream의 static 메서드인 concat()을 사용해서 두 스트림을 하나로 연결할 수 있다.

* 두 스트림은 같은 타입이어야 한다.

```java
String[] str1 = {"123", "456", "789"}
String[] str2 = {"ABC", "abc", "DEF"}

Stream<String> strs1 = Stream.of(str1);
Stream<String> strs2 = Stream.of(str2);
Stream<String> strs3 = Stream.concat(strs1, strs2);   // 두 스트림을 하나로 연결
```



## 스트림의 중간연산

### 스트림 자르기

* Stream skip(long n) // 처음 n개의 요소 건너뛰기
* Strema limit(long maxSize) // 스트림의 요소를 maxSize개로 제한

```java
IntStream exampleStream = IntStream.rangeClosed(1, 10); // 1~10의 요소를 가진 스트림
exampleStream.skip(3).limit(5).forEach(System.out::print);  // 45678
```



### 스트림 요소 걸러내기

* distinct()는 스트림에서 중복된 요소들 제거
* filter()는 주어진 조건(Predicate)에 맞지 않는 요소를 걸러낸다.

```java
// distinct()
IntStream exampleStream = IntStream.of(1,2,2,3,3,3,4,5,5,6);
exampleStream.distinct().forEach(System.out::print);  // 123456

// filter()
IntStream example2Stream = IntStream.rangeClosed(1, 10);
example2Stream.filter(i -> i % 2 == 0).forEach(System.out::print); // 246810

// filter()를 다른 조건으로 여러 번 사용. 두 문장의 결과는 같다.
example2Stream.filter(i -> i % 2 != 0 && i % 3 != 0).forEach(System.out::print);  //157
example2Stream.filter(i -> i % 2 != 0).filter(i -> i % 3 != 0).forEach(System.out::print);
```



## 스트림 정렬 - sorted()

* 스트림을 정렬할 때는 sorted()를 사용하면 된다.

```java
Stream<T> sorted()
Stream<T> sorted(Comparator<? super T> comparator)
```

* sorted()는 지정된 Comparator로 스트림을 정렬하는데, Comparator대신 int값을 반환하는 람다식을 사용하는 것도 가능하다.
* Comparator를 지정하지 않으면 스트림 요소의 기본 정렬 기준(Comparator)으로 정렬한다.
  * 단, 스트림의 요소가 Comparable을 구현한 클래스가 아니면 예외가 발생한다.



```java
Stream<String> strStream = Stream.of("dd","aaa","CC","cc","b");
strStream.sorted().forEach(System.out::print); // CCaaabccdd

// 기본정렬 역순
strStream.sorted(Comparator.reverseOrder());

// 대소문자 구분 없이
strStream.sorted(String.CASE_INSESITIVE_ORDER)

// 길이 순 정렬
strStream.sorted(Comparator.comparing(String::length))

//정렬 조건을 추가할 때는 thenComparing() 사용
studentStream.sorted(Comparator.comparing(Student::getBan)
                      .thenComparing(Student::getTotalScore)
                      .thenComparing(Student::getName)
                      .forEach(System.out::println);
```



| 문자열 스트림 정렬 방법                                      | 출력 결과  |
| ------------------------------------------------------------ | ---------- |
| `Stream<String> strStream = Stream.of("dd", "aaa", "CC", "cc", "b");` |            |
| 기본 정렬 // strStream.sorted() <br />기본 정렬 // strStream.sorted(Comparator.naturalOrder()) <br />람다식도 가능 // StrStream.sorted((s1, s2) -> s1.compareTo(s2)); <br />위의 문장과 동일 // strStream.sorted(String::compareTo); | CCaaabccdd |
| 기본 정렬의 역순 // strStream.sorteD(Comparator.reverseOrder())<br /> `strStream.sorted(Comparator.<String>naturalOrder().reversed())` | ddccbaaaCC |
| 대소문자 구분 안함 // strStream.sorted(String.CASE_INSENSITIVE_ORDER) | aaabCCccdd |
| strStream.sorted(String.CASE_INSENSITIVE_ORDER.reversed())   | ddCCccbaaa |
| 길이 순 정렬 // strStream.sorted(Comparator.comparing(String::length)) <br />no 오토박싱 // strStream.sorted(Comparator.comparingInt(String::length)) | bddCCccaaa |
| strStream.sorted(Comparator.comparing(String::length).reversed()) | aaaddCCccb |



### 스트림의 요소 변환하기 : map()

스트림의 요소에 저장된 값 중에서 필드만 뽑아내거나 특정 형태로 변환해야 할 때 사용

```java
Stream<R> map(Function<? super T,? extends R> mapper)
```

* 매개변수로 T타입을 R타입으로 변환해서 반환하는 함수를 지정해야 한다.

### 스트림 조회 - peek()

연산과 연산 사이에 올바르게 처리됐는지 확인하고 싶다면, peek()를 사용하자.
forEach()와 달리 스트림의 요소를 소모하지 않으므로 연산 사이에 여러 번 끼워 넣어도 문제가 되지 않는다.

```java
fileStream.map(File::getName) // Stream<File> -> Stream<String>
.filter(s -> s.indexOf(',')! = -1) // 확장자가 없는 것은 제외
.peek(s -> System.out.printf("filename=%s%n", s)) // 파일명을 출력한다.
.map(s -> s.substring(s.indexOf(',')+1)) // 확장자만 추출
.peek(s -> System.out.printf("extension=%s%n, s)) // 확장자만 출력한다.
.forEach(System.out::println);
```

filter()나 map의 결과를 확인할 때 유용하게 사용될 수 있다.



### 스트림을 기본 스트림(Primitive Stream)으로 변환 : mapToInt(), mapToLong(), mapToDouble()

map()은 연산의 결과로 Stream< T>타입의 스트림을 반환하는데, 스트림의 요소를 숫자로 변환하는 경우
IntStream과 같은 기본형 스트림으로 변환하는 것이 더 유용할 수 있다.
Stream< T>타입의 스트림을 기본형 스트림으로 변환할 때 사용하는 것이 아래의 메서드들이다.

```java
Double Stream mapToDouble(ToDoubleFunction<? super T> mapper)
IntStream mapToInt(ToIntFunction<? super T> mapper)
LongStream mapToLong(ToLongFunction<? super T> mapper)
```



count()만 지원하는 Stream< T>와 달리 IntStream과 같은 기본형 스트림은 아래와 같이 숫자를 다루는데 편리한 메서드들을 제공한다.

**max()와 min()은 Stream에도 정의되어 있지만, 매개변수로 Comparator를 지정해야 한다는 차이가 있다.**

```java
int		sum()		/ 스트림의 모든 요소의 총합
OptionalDouble	average() sum() / (double)count()
OptionInt	max()		/ 스트림의 요소 중 제일 큰 값
OptionInt	min()		/ 스트림의 요소 중 제일 작은 값
```



### flatMap() - Stream<T[]>를 Stream< T>로 변환

스트림의 요소가 배열이거나 map()의 연산결과가 배열인 경우, 2차원 3차원 일 수록 연산하기 복잡해진다.

그럴 때 flat(평평하게 ) 1차원으로 만들어 편리하게 사용할 수 있다.

```java
// 요소가 문자열 배열(String[])인 스트림
Stream<String[]> strArrStrm = Stream.of(
                              new String[]{"abc", "def", "ghi"},
                              new String[]{"ABC", "DEF"m "JKLMN"}
                              );

// Stream<String[]>을 map(Arrays::stream)으로 변환한 결과는 Stream<String>이 아닌 Stream<Stream<String>>.
// 즉 스트림의 스트림이다.
Stream<Stream<String>> strStrStrm = strArrStrm.map(Arrays::stream);

//map() 대신 flatMap() 사용하면 각 배열이 하나의 스트림의 요소가 된다.
Stream<String> strStrm = strArrStrm.flatMap(Arrays::stream);
```



# Optional

Optional은 제네릭 클래스로 ‘T타입의 객체’를 감싸는 `래퍼 클래스`다.
그래서 Optional타입의 객체에는 모든 타입의 참조변수를 담을 수 있다.

```java
public final class Optional<T>
{
	private final T value
	...
}
```

최종 연산의 결과를 그냥 반환하는 것이 아니라 Optional 객체에 담아서 반환하면,
반환된 결과가 null인지 매번 if문으로 체크하는 대신 Optional에 정의된 메서드를 통해 간단히 처리할 수 있다.



## Optional 객체 생성하기

of() 또는 ofNullable() 사용

```java
//참조변수의 값이 null일 가능성이 있으면 of()대신 ofNullable() 사용
Optional<String> optVal = Optional.of(null); //NullPointerException 발생
Optional<String> optVal = Optional.ofNullable(null);  // OK

//Optional<T> 타입의 참조변수를 기본값으로 초기화할 때 empty() 사용
//null로 초기화할 수 있지만 empty()로 초기화하는 것이 바람직
Optional<String> optVal = null;
Optional<String> optVal = Optional.empty();  // 빈 객체로 초기화
```

## Optional 객체의 값 가져오기 : get(), orElse(), orElseGet(), orElseThrow()

Optional 객체에 저장된 값을 가져올 때는 get()을 사용한다.

* 값이 null일 때는 NoSuchElementException이 발생하며, 이를 대비해서 orElse()로 대체할 값을 지정할 수 있다.

```java
Optional<String> optVal = Optional.of("abc");

String str1 = optVal.get();   // optVal에 저장된 값을 반환. null이면 예외발생
String str2 = optVal.orElse("");  // optVal에 저장된 값이 null이면 ""를 반환
String str3 = optVal.orElseGet(String::new);  // null을 대체할 값을 반환하는 람다식 지정
String str4 = optVal.orElseThrow(NullPointerException::new);  // null일 때 지정된 예외를 발생
```

* isPresent() 메소드는 Optional 객체의 값이 null이면 false를, 아니면 true 반환
* ifPresent(Consumer block)메소드 는 값이 있으면 주어진 람다식 실행, 없으면 아무일 안함



### 기본형 값을 감싸는 래퍼 클래스 : OptionalInt, OptionalLong, OptionalDouble

```java
public final class OptionalInt
{
	private final boolean isPresent;	//값이 저장되어 있으면 true
  
  private final int value;			//int타입의 변수
}
```



### Optional의 값 가져오기 

| Optional 클래스 | 값을 반환하는 메서드 |
| --------------- | -------------------- |
| Optional<T>     | T get()              |
| OptionalInt     | int getAsInt()       |
| OptionalLong    | long getAsLong()     |
| OptionalDouble  | double getAsDouble() |



# 스트림의 최종 연산

최종 연산은 스트림의 요소를 소모해서 결과를 만든다. 그래서 최종 연산후에는 스트림이 닫혀서 더이 상 사용할 수 없다.

* 최종 연산의 결과는 스트림 요소의 합과 같은 단일 값이거나, 스트림의 요소가 담긴 배열 또는 컬렉션일 수 있다.

## forEach()

반환 타입이 void이므로 스트림의 요소를 출력하는 용도로 많이 사용

```java
void forEach(Consume<? super T> action)
```



## 조건 검사 - allMatch(), anyMatch(), noneMatch(), findFirst(), findAny()

스트림의 요소에 대한 지정된 조건에 모든 요소가 일치하는 지, 일부가 일치하는지 아니면
어떤 요소도 일치하지 않는지 확인하는데 사용할 수 있는 메서드들이다.
이 메서드들은 모두 매개변수로 Predicate를 요구하며, 연산결과로 boolean을 반환한다.

```java
boolean allMatch (Predicate<? super T> predicate)
boolean anyMatch (Predicate<? super T> predicate)
boolean noneMatch (Predicate<? super T> predicate)
```

findAny()와 findFirst()의 반환 타입은 `Optional<T>`이며, 스트림의 요소가 없을 때는 비어있는 Optional객체를 반환한다.



> 비어있는 Optional객체는 내부적으로 null을 저장하고 있다.



* findFirst(), findAny() : 스트림의 요소 중에서 조건에 일치하는 첫 번재 것을 반환

병렬 스트림인 경우에는 findFirst()대신 findAny()를 사용해야 한다.

```java
// 병렬스트림의 경우 findFirst() 대신 findAny() 사용
Optional<Student> stu = parallelStream.filter(s->s.getTotalScore()<=100).findAny();
```

### findFirst()와 findAny()의 차이점

Stream을 직렬로 처리할 때 `findFirst()`와 `findAny()`는 동일한 요소를 리턴하며, 차이점이 없다. 

하지만 Stream을 병렬로 처리할 때는 차이가 있다.

`findFirst()`는 여러 요소가 조건에 부합해도 Stream의 순서를 고려하여 가장 앞에 있는 요소를 리턴한다.

반면에 `findAny()`는 Multi thread에서 Stream을 처리할 때 가장 먼저 찾은 요소를 리턴한다.  
따라서 Stream의 뒤쪽에 있는 element가 리턴될 수 있다.



## 통계 - count(), sum(), average(), max(), min()

```java
long	count()
Optional<T> max(Comparator<? super T> comparator)
Optional<T> min(Comparator<? super T> comparator)
```

* 기본형 스트림의 min(), max()와 달리 매개변수로 Comparator를 필요로 한다는 차이가 있다



## 리듀싱 - reduce()

reduce()는 스트림의 요소를 줄여나가면서 연산을 수행하고 최종결과를 반환한다.
그래서 매개변수의 타입이 BinaryOperator 이다.
처음 두 요소를 가지고 연산한 결과로 그 다음 요소와 연산한다.
모든 스트림의 요소를 소모하게 되면 그 결과를 반환한다.

```java
Optional<T> reduce(BinaryOperator<T> accumulator)
```

> educe()의 사용방법은 초기값과 어떤 연산(BinaryOperator)으로 스트림의 요소를 줄여나갈 것인지만 결정하면 된다.





# collect()

스트림의 요소를 수집하는 최종 연산
collect()가 스트림의 요소를 수집하기 위한 수집 방법이 정의된 것이 collector.
collector는 Collector인터페이스를 구현한 것.

> collect() : 스트림의 최종연산, 매개변수로 컬렉터를 필요로 한다.
> Collector 인터페이스 :  컬렉터는 이 인터페이스를 구현해야한다.
> Collectors 클래스 : static메서드로 미리 작성된 컬렉터를 제공한다.

collect()의 매개변수 타입은 Collector인데. 매개변수가 Collector를 구현한 클래스의 객체이어야 한다는 뜻이다.
그리고 collect()는 이 객체에 구현된 방법대로 스트림의 요소를 수집한다.

````java
public interface Collector<T, A, R>		//T(요소)를 A에 누적한 다음, 결과를 R로 변환해서 반환
{
  	Supplier<A> supplier();				//StringBuilder::new		누적할 곳
    BiConsumer<A, T> accumulator();		//(sb, s) -> sb.append(s)	누적 방법
    BinaryOperator<A> combiner();		//(sb1, sb2) -> sb1.append(sb2)		결합 방법(병렬)
    Function<A, R> finisher();			//sb -> sb.toString()				최종변환
    set<Characteristics> characteristics();	//컬렉터의 특성이 담긴 Set을 반환
}
````

Collectors 클래스는 다양한 기능의 컬렉터(Collector를 구현한 클래스)를 제공

| 변환          | mapping(), toList(), toSet(), toMap(), toCollection(), ...   |
| ------------- | ------------------------------------------------------------ |
| 통계          | counting(), summingInt(), averagingInt(), maxBy(), minBy(), summarizingInt(), ... |
| 문자열 결합   | joining()                                                    |
| 리듀싱        | reducing()                                                   |
| 그룹화와 분할 | groupingBy(), partitioningBy(), collectingAndThen()          |



## 스트림을 컬렉션과 배열로 변환 - toList(), toSet(), toMap(), to Collection(), toArray()

스트림의 모든 요소를 컬렉션에 수집하려면, Collectors클래스의 toList()와 같은 메서드를 사용하면 된다.  
```java
List<String> names = stuStream.map(Student::getName)
                              .collect(Collectors.toList());
```



List나 Set이 아닌 특정 컬렉션을 지정하려면, toCollection()에 해당 컬렉션의 생성자 참조를 매개변수로 넣어주면 된다.

```java
ArrayList<String> list = names.stream()
  .collect(Collectors.toCollection(ArrayList::new));
```

Map은 객체의 어떤 필드를 키와 값으로 사용하지 지정해야 한다.

```java
Map<String, Person> map = personStream
  .collect(Collectors.toMap(p -> p.getRegId(), p -> p);
```

스트림에 저장된 요소들을 T[] 타입의 배열로 변환하려면 toArray() 사용

* 단, 해당 타입의 생성자 참조를 매개변수로 지정해줘야 한다. 지정하지 않으면 반환되는 배열의 타입은 Object[]

```java
Student[] stuNames = studentStream.toArray(Student[]::new);   // OK
Student[] stuNames = studentStream.toArray();                 // 에러
Object[] stuNames = studentStream.toArray();                  // OK
```



## 통계 - counting(), summingInt(), averagingInt(), maxBy(), minBy()

Collectors 클래스에서 static 메소드를 제공한다.

* 나중에 groupingBy()와 함께 사용하면 유용하다



## 리듀싱 - reducing()

리듀싱 역시 collect()로 가능하다



## 문자열 결합 -joining()

문자열 스트림의 모든 요소를 하나의 문자열로 연결해서 반환한다.
구분자를 지정해줄 수도 있고, 접두사와 접미사도 지정가능하다.
스트림의 요소가 String이나 StringBuffer처럼 CharSequence의 자손인 경우에만 결합이 가능하므로
스트림의 요소가 문자열이 아닌 경우에는 먼저 map()을 이용해서 스트림의 요소를 문자열로 변환해야 한다.



* Collections.joining() 메소드

```java
String studentNames = stuStream.map(Student::getName).collect(joining());
String studentNames = stuStream.map(Student::getName).collect(joining(","));
String studentNames = stuStream.map(Student::getName).collect(joining(",", "[", "]"));

// 만약 map()없이 스트림에 바로 joining()하면, 스트림의 요소에 toString()을 호출한 결과를 결합한다.
String studentInfo = stuStream.collect(joining(","));
```



## 그룹화와 분할 - groupingBy(), partitioningBy()

groupingBy()

* 그룹화는 스트림의 요소를 특정 기준으로 그룹화하는 것
* groupingBy()는 스트림의 요소를 Function 분류 

partitioningBy()

* 분할은 스트림의 요소를 두 가지, 지정된 조건에 일치하는 그룹과 아닌 그룹으로 분할하는 것
* partitioningBy()는 스트림의 요소를 Predicate로 분류



보통 스트림의 두 개의 그룹으로 나눠야 하면 partitioningBy()쓰는 것이 더 빠르고, 그 외에는 groupingBy()를 쓰면 된다.
그룹화와 분할의 결과는 Map에 담겨 반환된다.



###  partitioningBy()에 의한 분류

한번 분류한 결과에 또 분류를 할 수 있다.

.collect(partitioningBy(Predicate, partitioningBy(Predicate) );

```java
Collector partitioningBy(Predicate predicate)
Collector partitioningBy(Predicate predicate, Collector downstream)

// 1. 기본 분할
Map<Boolean, List<Student>> stuBySex 
              = stuStream.collect(partitioningBy(Student::isMale));  // 학생들을 성별로 분할
List<Student> maleStudent = stuBySex.get(true);   // Map에서 남학생 목록을 얻는다.
List<Student> femaleStudent = stuBySex.get(false);  // 여학생 목록

// 2. 기본 분할 + 통계 정보
Map<Boolean, Long> stuNumBySex = stuStream.collect(partitioningBy(Student::isMale, counting()));
System.out.println(stuNumBySex.get(true));  // 8 (남학생수)
System.out.println(stuNumBySex.get(false));  // 10 (여학생수)


// 남학생 1등 구하기, mapBy()의 반환타입은 Optional<Student>
Map<Boolean, Optional<Student>> topScoreBySex
                = stuStream.collect(partitioningBy(Student::isMale, maxBy(comparingInt(Student::getScore))));
System.out.println(topScoreBySex.get(true));  // Optional{[남일등, 남, 1, 1, 300]}


// mapBy()의 반환타입이 Optional<Student>가 아닌 Student를 반환 결과로 얻으려면,  
// collectiongAndThen()과 Optional::get 함께 사용
Map<Boolean, student> topScoreBySex 
            = stuStream.collect(
                        partitioningBy(
                            Student::isMale, collectingAndThen(
                                              maxBy(comparingInt(Student::getScore))
                                              , Optional::get)));
                                              
// 3. 이중 분할
Map<Boolean, Map<Boolean, List<Student>>> failedStuBySex 
                         = stuStream.collect(
                                      partitioningBy(Student::isMale, partitioningBy(s->s.getScore()<150)));
List<Student> failedMaleStu = failedStuBySex.get(true).get(true);
```



### groupingBy()에 의한 분류

groupingBy()로 그룹화하면 기본적으로 List에 담는다.



```java
Collector groupingBy(Function classifier)
Collector groupingBy(Function classifier, Collector downstream)
Collector groupingBy(Function classifier, Supplier mapFactory, Collector downstream)
  
  
// 1. 학생 스트림을 반 별로 그룹지어 Map에 저장 
Map<Integer, List<Student>> stuByBan 
                  = stuStream.collect(groupingBy(Student::getBan, toList()));   //toList()생략가능

Map<Integer, HashSet<Student>> stuByHak 
                  = stuStream.collect(groupingBy(Student::getHak, toCollection(HashSet::new)));


// 2. 학생 스트림을 성적의 등급(Student.Level)으로 그룹화
Map<Student.Level, Long> stuByLevel
            = stuStream.collect(
                        groupingBy(s-> { if(s.getScore()>=200)      return Student.Level.HIGH;
                                         else if(s.getScore()>=100) return Student.Level.MID;
                                         else                       return Student.Level.LOW;
                                       }, counting()));


// 3. groupingBy() 다중 사용하기.
// 학년별로 그룹화하고 다시 반별로 그룹화
Map<Integer, Map<Integer, List<Student>>> stuByHakAndBan
            = stuStream.collect(groupingBy(Student::getHak,
                                groupingBy(Student::getBan)));
                                

// 4. 각 반별 1등 추출
Map<Integer, Map<Integer, Student>> topStuByHakAndBan
            = stuStream.collect(groupingBy(Student::getHak,
                                groupingBy(Student::getBan, 
                                           collectingAndThen(
                                                        maxBy(comparingInt(Student::getScore)),
                                                                                      Optional::get))));


// 5. 학년별, 반별 그룹화한 후에 성적그룹으로 변환하여 Set에 저장
Map<Integer, Map<Integer, Set<Student.Level>>> stuByHakAndBan
            = stuStream.collect(groupingBy(Student::getHak,
                                groupingBy(Student::getBan,
                                           mapping(s-> {
                                                        if(s.getScore()>=200)      return Student.Level.HIGH;
                                                        else if(s.getScore()>=100) return Student.Level.MID;
                                                        else                       return Student.Level.LOW;
                                                       } , toSet()))));
```



## 스트림 변환 정리 표

| from                                          | to                                                           | 변환 메서드                                                  |
| --------------------------------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 1. 스트림 -> 기본형 스트림                    |                                                              |                                                              |
| `Stream<T>`                                   | IntStream LongStream DoubleStream                            | `mapToInt(ToIntFunction<T> mapper)`<br /> `mapToLong(ToLongFunction<T> mapper) `<br />`mapToDouble(ToDoubleFunction<T> mapper)` |
| 2. 기본형 스트림 -> 스트림                    |                                                              |                                                              |
| IntStream LongStream DoubleStream             | `Stream<Integer>`<br />` Stream<Long> `<br />`Stream<Double>` | boxed()                                                      |
| `Stream<U>`                                   | `mapToObj(DoubleFunction<U> mapper)`                         |                                                              |
| 3. 기본형 스트림 -> 기본형 스트림             |                                                              |                                                              |
| IntStream LongStream DoubleStream             | LongStream DoubleStream                                      | asLongStream() asDoubleStream()                              |
| 4. 스트림 -> 부분 스트림                      |                                                              |                                                              |
| `Stream<T> IntStream`                         | `Stream<T> IntStream`                                        | skip(long n) limit(long maxSize)                             |
| 5. 두 개의 스트림 -> 스트림                   |                                                              |                                                              |
| `Stream<T>, Stream<T>`                        | `Stream<T>`                                                  | `concat(Stream<T> a, Stream<T> b)`                           |
| IntStream, IntStream                          | IntStream                                                    | concat(IntStream a, IntStream b)                             |
| LongStream, LongStream                        | LongStream                                                   | concat(LongStream a, LongStream b)                           |
| DoubleStream, DoubleStream                    | DoubleStream                                                 | concat(DoubleStream a, DoubleStream b)                       |
| 6. 스트림의 스트림 -> 스트림                  |                                                              |                                                              |
| `Stream<Stream<T>>`                           | `Stream<T>`                                                  | flatMap(Function mapper)                                     |
| `Stream<IntStream>`                           | IntStream                                                    | flatMapToInt(Function mapper)                                |
| `Stream<LongStream>`                          | LongStream                                                   | flatMapToLong(Function mapper)                               |
| `Stream<DoubleStream>`                        | doubleStream                                                 | flatMapToDouble(Function mapper)                             |
| 7. 스트림 <-> 병렬 스트림                     |                                                              |                                                              |
| `Stream<T>` IntStream LongStream DoubleStream | `Stream<T> IntStream `LongStream DoubleStream                | parallel()  //스트림 -> 병렬 스트림 sequential()  //병렬 스트림 -> 스트림 |
| 8. 스트림 -> 컬렉션                           |                                                              |                                                              |
| `Stream<T> IntStream `LongStream DoubleStream | `Collection<T>`                                              | collection(Collectors.toCollection(Supplier factory)         |
| `List<T>`                                     | collect(Collectors.toList())                                 |                                                              |
| `Set<T>`                                      | collect(Collectors.toSet())                                  |                                                              |
| 9. 컬렉션 -> 스트림                           |                                                              |                                                              |
| `Collection<T> List<T> ``Set<T>`              | `Stream<T>`                                                  | stream()                                                     |
| 10. 스트림 -> Map                             |                                                              |                                                              |
| `Stream<T> IntStream `LongStream DoubleStream | Map<K, V>                                                    | collect(Collectors.toMap(Function key, Function value)) <br />collect(Collectors.toMap(Function, Function, BinaryOperator)) <br />collect(Collectors.toMap(Function, Function, BinaryOperator merge, Supplier mapSupplier)) |
| 11. 스트림 -> 배열                            |                                                              |                                                              |
| `Stream<T>`                                   | Object[]                                                     | toArray()                                                    |
| T[]                                           | toArray(IntFunction<A[]> generator)                          |                                                              |
| IntStream LongStream DoubleStream             | int[] long[] double[]                                        | toArray()                                                    |

