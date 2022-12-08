# UUID란



UUID(Universally Unique Identifier)는 온라인 대전 시스템이나 랭킹 시스템 등에서 전 세계에서 유일한 값으로 서버에 연결하지 않고 만들 경우 활용하는 식별자다.

  


UUID는 전 세계에 중복 없는 독특한 값이 되도록 설계되어 있으며 고유성이 중앙 서버 등 요소에 의존하지 않도록 되어 있다.

  


이런 특징을 가진 UUID를 데이터베이스 기본키에 이용하면 타인 데이터베이스와 결합하거나 데이터를 다른 데이터베이스로 이동하는 걸 자유롭게 할 수 있게 된다. 반면 누구나 UUID를 생성할 수 있기 때문에 현재 전 세계에 존재하는 UUID를 추적할 수 없다는 단점도 함께 갖고 있다.



전 세계에 중복이 없는 독특한 가치를 실현하는 방법은 다수 존재한다. 하지만 UUID 구현은 IETF의 RFC 4122를 기반으로 5가지 생성 방법을 정의한다. 여기에 정의된 UUID는 128비트 열이 되고 보통 이 비트 열을 16진수로 표기해 8-4-4-4-12 형식으로 표시한다.

```
123e4567-e89b-12d3-a456-556642440000
```



* UUID의 장점 중, 데이터들이 나중에 단일 DB로 통합되거나, 같은 채널에서 전송되더라도 식별자가 중복될 확률이 매우 낮다는 점이 있었다



## 역사

UUID는 현재까지 5개의 버전이 있다.

1. version1 : MAC 주소 + date-time기반으로 생성, 매우 빠른 처리를 수행하는 경우 중복된 값 생성가능..(시간기반이기때문에)
2. version2: DCE Security, 시간을 version1보다 훨씬 잘게 나눔
3. version3: MD5 hash + namespace기반으로 생성, 특정 name으로 생성된 UUID를 다른시스템에서도 같은 name과 호환성있게 사용하고 싶을때
4. version4: random으로 생성 → 단순히 unique한 id생성하려면 이것이 좋다
5. version5: SHA-1 hash + namespace, MD5보다 보안성이 좋은 SHA-1알고리즘 활용



### UUID 버전

***B*** 는 버전을 나타냅니다. 언급된 UUID( ***B*** 값)의 버전은 4다.  

Java는 UUID의 변형 및 버전을 가져오는 방법을 제공한다.

```java
UUID uuid = UUID.randomUUID();
int variant = uuid.variant();
int version = uuid.version();
```



Java는 v3 및 v4에 대한 구현을 제공하지만 모든 유형의 UUID를 생성하기 위한 *생성자* 도 제공 .

```java
UUID uuid = new UUID(long mostSigBits, long leastSigBits);
```



###  JAVA에서 UUID 생성



- version3만들기

  `public static [UUID](<https://docs.oracle.com/javase/7/docs/api/java/util/UUID.html>) nameUUIDFromBytes(byte[] name)`

- version4만들기

  `public static [UUID](<https://docs.oracle.com/javase/7/docs/api/java/util/UUID.html>) randomUUID()`

- UUID 64bits씩 넣어서 만들기

```java
public UUID(long mostSigBits,long leastSigBits)
```

- 이 외에 JAVA UUID 관련 method

  [UUID (Java Platform SE 7 )](https://docs.oracle.com/javase/7/docs/api/java/util/UUID.html)





## UUID 장/단점



장점

- 모든 테이블, 데이터베이스를 통틀어 유일한 값
- 유추하기가 어려움
- offline으로 생성됨



🦋 단점

- 저장공간이 늘어남
- debug의 어려움
- 성능문제(정렬되어있지 않고 길이가 길다)



## UUID In MySQL

1. UUID_TO_BIN
   - human-readable format(char/varchar)  → compact format(binary) : 길이를 줄일 수 있음(저장공간 확보)
2. BIN_TO_UUID
   - compact format(binary) → human-readable format(char/varchar)
3. IS_UUID
   - 해당 string이 UUID인지 판별



### UUID 관련 문제

- UUID에는 36자가 있어 부피가 크다.
- InnoDB는 PRIMARY KEY 순서로 데이터를 저장하고 모든 보조 키도 PRIMARY KEY를 포함한다. 
  - 따라서 UUID를 PRIMARY KEY로 사용하면 메모리에 맞지 않는 인덱스가 더 커진다.
- 삽입은 무작위이며 데이터는 흩어져 있다.



UUID의 문제에도 불구하고 사람들은 여전히 UUID가 모든 테이블에서 고유하고 어디에서나 생성될 수 있기 때문에 선호한다. 

### UUID의 타임스탬프 부분을 재정렬하여 UUID를 효율적으로 저장하는 방법



타임스탬프는 다음과 같이 매핑된다.

* 타임스탬프에 (60비트) 16진수 값이 있는 경우: 1d8 eebc 58e0a7d7 . 
  * UUID의 다음 부분이 설정 . **58e0a7d7 – eebc – 1 1d8** -9669-0800200c9a66. 
  * 타임스탬프의 가장 중요한 숫자(11d8) 앞 의 1 은 UUID 버전을 나타내며 시간 기반 UUID의 경우 1이다.



* 4번째와 5번째 부분은 단일 서버에서 생성되는 경우 대부분 일정하다. 
* 처음 세 숫자는 타임스탬프를 기반으로 하므로 단조롭게 증가합니다. 
* UUID를 순차에 가깝게 만들기 위해 전체 순서를 재정렬 하자.
  * 이렇게 하면 삽입 및 최근 데이터 조회가 더 빨라진다. 대시('-')는 의미가 없으므로 제거.
    58e0a7d7-eebc-11d8-9669-0800200c9a66 => 11d8eebc58e0a7d796690800200c9a66



## UUID 값 저장에 대한 결론

- - UUID 필드를 재정렬하여 사용하는 함수 생성

```sql
DELIMITER //

CREATE DEFINER=`root`@`localhost` FUNCTION `ordered_uuid`(uuid BINARY(36)) 
RETURNS binary(16) DETERMINISTIC 
RETURN UNHEX(CONCAT(SUBSTR(uuid, 15, 4),SUBSTR(uuid, 10, 4),SUBSTR(uuid, 1, 8),SUBSTR(uuid, 20, 4),SUBSTR(uuid, 25)));

//DELIMITER ;
```



* INSERT

```sql
INSERT INTO events_uuid_ordered VALUES (ordered_uuid(uuid()),'1','M',....);
```



* SELECT

```sql
SELECT HEX(uuid),is_active,... FROM events_uuid_ordered ;
```

- 바이너리에는 문자 집합이 없으므로 UUID를 binary(16)로 정의.





## 결론



- UUID는 128-bit로 이루어진, “실용적인 측면에서 충분히 고유한” universal 식별자이다
- UUID를 사용했을 때의 이점들 중 몇 가지를 뽑자면 다음과 같다
  - UUID의 고유성은 중앙 등록 기관(예를 들면 데이터베이스 서버) 등에 의존되지 않고, standard method를 통해 독립적으로 생성 가능
  - 별도로 분리되어 있던 데이터들을 통합하거나, 하나의 채널에서 전송하더라도 충돌이 발생하지 않는다
  - UUID는 널리 채택되어 있고 많은 컴퓨팅 플랫폼들에서 UUID 생성, 파싱을 지원하고 있음
    - 예를 들면 Exposed의 UUIDTable class이 될 것 같다





## 참조



- https://docs.spring.io/spring-framework/docs/current/reference/html
- https://www.baeldung.com/java-uuid

* https://hojongs.github.io/uuid-definition/

* https://techrecipe.co.kr/posts/22058

* https://www.percona.com/blog/2014/12/19/store-uuid-optimized-way/