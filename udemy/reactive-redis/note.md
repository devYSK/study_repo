

# Redis ACL 명령어 

| 명령어                               | 설명                                                         |
| ------------------------------------ | ------------------------------------------------------------ |
| `ACL LIST`                           | 현재 ACL 사용자 목록과 규칙을 보여줍니다.                    |
| `ACL WHOAMI`                         | 현재 연결된 사용자의 이름을 반환합니다.                      |
| `ACL SETUSER [username] >[password]` | 지정된 사용자를 생성하거나 업데이트하고 비밀번호를 설정합니다. |
| `ACL SETUSER [username] nopass`      | 지정된 사용자에 대해 비밀번호 없이 접근을 허용합니다.        |
| `ACL SETUSER [username] on`          | 지정된 사용자를 활성화합니다.                                |
| `ACL SETUSER [username] off`         | 지정된 사용자를 비활성화합니다.                              |
| `ACL DELUSER [username]`             | 지정된 사용자를 삭제합니다.                                  |
| `ACL USERS`                          | 현재 정의된 모든 사용자를 보여줍니다.                        |
| `ACL GETUSER [username]`             | 지정된 사용자의 상세 정보와 권한을 보여줍니다.               |



레디스 권한관련.

| 규칙                        | 설명                                                         |
| --------------------------- | ------------------------------------------------------------ |
| `allcommands` / `+@all`     | Redis의 모든 명령에 대한 접근 권한을 부여합니다.             |
| `-get`, `-set`              | `GET`, `SET` 명령어에 대한 접근을 제한합니다.                |
| `+@set`, `+@hash`, `+@list` | `SET`, `HASH`, `LIST` 관련 명령어 그룹에 대한 접근 권한을 부여합니다. |
| `allkeys` / `~*`            | Redis의 모든 키에 대한 접근 권한을 부여합니다.               |
| `~numbers:*`                | `numbers:`로 시작하는 키에 대한 접근 권한을 부여합니다.      |

- **`allcommands` / `+@all`**: 이 규칙을 사용자에게 적용하면, 해당 사용자는 Redis에서 제공하는 모든 명령을 사용할 수 있습니다. 시스템 관리자나 특정 신뢰할 수 있는 사용자에게만 이 권한을 부여하는 것이 좋습니다.
- **`-get`, `-set`**: 특정 명령어(예: `GET`, `SET`)에 대한 사용을 명시적으로 금지합니다. 이를 통해 사용자가 데이터를 조회하거나 변경하는 것을 방지할 수 있습니다.
- **`+@set`, `+@hash`, `+@list`**: `SET`, `HASH`, `LIST`와 같은 특정 카테고리에 속한 명령어 그룹에 대한 사용을 허용합니다. 예를 들어, 사용자가 데이터 구조를 관리할 수 있도록 특정 작업을 수행할 수 있게 합니다.
- **`allkeys` / `~\*`**: 사용자가 Redis 내의 모든 키에 접근할 수 있도록 합니다. 이는 사용자에게 데이터베이스 내의 모든 데이터에 대한 광범위한 접근 권한을 부여하는 것이므로, 주의해서 사용해야 합니다.
- **`~numbers:\*`**: 특정 패턴을 가진 키에 대한 접근 권한을 제한합니다. 여기서는 `numbers:`로 시작하는 모든 키에 대한 접근을 허용하는 규칙입니다. 이를 통해 사용자가 데이터베이스 내에서 특정 형태의 키에만 접근하도록 제한할 수 있습니다.

| 명령어                                 | 설명                                                         |
| -------------------------------------- | ------------------------------------------------------------ |
| `ACL LOAD`                             | ACL 설정을 디스크에서 다시 로드합니다.                       |
| `ACL SAVE`                             | 현재 메모리에 있는 ACL 설정을 디스크로 저장합니다.           |
| `ACL LOG [count]`                      | ACL 보안 위반 로그를 조회합니다.                             |
| `ACL LOG RESET`                        | ACL 보안 위반 로그를 초기화합니다.                           |
| `ACL GENPASS [bits]`                   | 사용할 수 있는 안전한 비밀번호를 생성합니다.                 |
| `ACL CAT [categoryname]`               | 명령어의 카테고리를 나열하거나, 특정 카테고리에 속한 명령어들을 조회합니다. |
| `ACL SETUSER [username] +[command]`    | 지정된 사용자에게 특정 명령어의 사용을 허용합니다.           |
| `ACL SETUSER [username] -[command]`    | 지정된 사용자에게 특정 명령어의 사용을 제한합니다.           |
| `ACL SETUSER [username] ~[keypattern]` | 지정된 사용자가 접근할 수 있는 키의 패턴을 설정합니다.       |
| `ACL SETUSER [username] ![keypattern]` | 지정된 사용자가 접근할 수 없는 키의 패턴을 설정합니다.       |
| `ACL SETUSER [username] allchannels`   | 지정된 사용자가 모든 Pub/Sub 채널에 접근할 수 있도록 허용합니다. |
| `ACL SETUSER [username] allcommands`   | 지정된 사용자가 모든 명령어를 사용할 수 있도록 허용합니다.   |
| `ACL SETUSER [username] allkeys`       | 지정된 사용자가 모든 키에 대한 접근을 허용합니다.            |
| `ACL SETUSER [username] resetkeys`     | 지정된 사용자의 키 접근 권한 설정을 초기화합니다.            |

- 특정 명령어 제한

  : 사용자가` FLUSHALL` 또는  `FLUSHDB`

  와 같은 위험한 명령어를 사용하는 것을 방지하려면, 해당 명령어를 제한할 수 있습니다.

  ```
  ACL SETUSER myuser -FLUSHALL -FLUSHDB
  ```

- 읽기 전용 사용자 생성

  : 데이터를 읽을 수만 있고 쓸 수 없는 사용자를 생성하려면, 

  ```
  +@read
  ```

  로 모든 읽기 관련 명령어를 허용하고, 

  ```
  -@write
  ```

  로 쓰기 관련 명령어를 제한할 수 있습니다.

  ```
  ACL SETUSER readonlyuser +@read -@write ~*
  ```

- 특정 키 패턴에 대한 접근 제한

  : 사용자가 특정 패턴을 가진 키에만 접근할 수 있도록 하려면, 해당 패턴을 설정할 수 있습니다.

  ```
  ACL SETUSER appuser ~app:*
  ```

  * 이 설정은 `appuser` 사용자가 `app:`로 시작하는 키에만 접근할 수 있음을 의미합니다.





# Redis Geometry

예제 클래스와 redisson

```java
@Data
@ToString
public class Restaurant {

    private String id;
    private String city;
    private double latitude;
    private double longitude;
    private String name;
    private String zip;

}


public class Lec17GeoSpatialTest extends BaseTest {

    private RGeoReactive<Restaurant> geo;
    private RMapReactive<String, GeoLocation> map;

    @BeforeAll
    public void setGeo(){
        this.geo = this.client.getGeo("restaurants", new TypedJsonJacksonCodec(Restaurant.class));
        this.map = this.client.getMap("us:texas", new TypedJsonJacksonCodec(String.class, GeoLocation.class));
    }

    @Test
    public void add(){
        Mono<Void> mono = Flux.fromIterable(RestaurantUtil.getRestaurants())
                .flatMap(r -> this.geo.add(r.getLongitude(), r.getLatitude(), r).thenReturn(r))
                .flatMap(r -> this.map.fastPut(r.getZip(), GeoLocation.of(r.getLongitude(), r.getLatitude())))
                .then();

        StepVerifier.create(mono)
                .verifyComplete();
    }
}
```

Redis의 Geo 데이터는 내부적으로 정렬된 세트(Sorted Set, `zset`)를 사용하여 구현됩니다. 그렇기 때문에 `TYPE restaurants` 명령어의 결과로 `zset`이 반환

```shell
127.0.0.1:6379> type restaurants
zset
127.0.0.1:6379>
```



### Geo 데이터 조회

`restaurants`가 Geo 데이터 구조로 저장되어 있다면, 위치 정보를 조회하기 위한 여러 명령어를 사용할 수 있습니다. 예를 들어:

- **특정 지점 주변의 위치 정보 조회**:

  ```
  shellCopy code
  GEORADIUS restaurants <longitude> <latitude> <radius> <unit> [WITHCOORD] [WITHDIST]
  ```

  이 명령어는 지정된 중심점 주변의 점들을 조회합니다. `<longitude>`와 `<latitude>`는 중심점의 경도와 위도이며, `<radius>`와 `<unit>`은 검색 반경과 단위(m, km, mi, ft)입니다. `[WITHCOORD]` 옵션을 사용하면 각 점의 경도와 위도도 함께 반환됩니다.

- **두 위치 사이의 거리 계산**:

  ```
  shellCopy code
  GEODIST restaurants <member1> <member2> [unit]
  ```

  `<member1>`과 `<member2>`는 `restaurants` 키에 저장된 두 점의 멤버(레스토랑) 이름입니다. `[unit]`은 거리의 단위(m, km, mi, ft)를 지정합니다.

Geo 데이터 구조를 사용할 때는 별도로 인덱스를 설정할 필요가 없습니다. Redis의 Geo 기능은 내부적으로 정렬된 세트(Sorted Set, `zset`)를 사용하며, Geo 해시를 스코어로 사용하여 위치 데이터를 자동으로 인덱싱합니다

**Geo 데이터 추가**: `GEOADD` 명령을 사용해 Geo 데이터를 추가할 때, Redis는 제공된 경도와 위도 값을 기반으로 Geo 해시를 계산하고, 이 해시 값을 `zset`의 스코어로 사용하여 데이터를 저장합니다. 이 스코어는 데이터의 위치 정보를 나타내며, Redis는 이를 통해 공간적 쿼리를 수행할 수 있습니다.



다양한 테스트

```java

public class Lec17GeoSpatialTest extends BaseTest {

    private RGeoReactive<Restaurant> geo;
    private RMapReactive<String, GeoLocation> map;

    @BeforeAll
    public void setGeo(){
        this.geo = this.client.getGeo("restaurants", new TypedJsonJacksonCodec(Restaurant.class));
        this.map = this.client.getMap("us:texas", new TypedJsonJacksonCodec(String.class, GeoLocation.class));
    }

    @Test
    public void add(){
        Mono<Void> mono = Flux.fromIterable(RestaurantUtil.getRestaurants())
                .flatMap(r -> this.geo.add(r.getLongitude(), r.getLatitude(), r).thenReturn(r))
                .flatMap(r -> this.map.fastPut(r.getZip(), GeoLocation.of(r.getLongitude(), r.getLatitude())))
                .then();

        StepVerifier.create(mono)
                .verifyComplete();
    }

    @Test
    public void search(){
        // 'map'에서 키 값 '75224'에 해당하는 요소를 비동기적으로 검색합니다.
        // 이 map은 아마도 Redisson의 RMapReactive 객체일 수 있으며, 비동기적으로 특정 키에 대한 값을 조회합니다.
        Mono<Void> mono = this.map.get("75224")
            // 검색된 요소에 대해 map 연산을 수행하여 GeoSearchArgs 객체를 생성합니다.
            // 여기서는 검색된 지리적 위치(경도, 위도)를 기반으로 반경 5마일 내의 객체를 검색하는 인자를 설정합니다.
            .map(gl -> GeoSearchArgs.from(gl.getLongitude(), gl.getLatitude()).radius(5, GeoUnit.MILES))
            // 생성된 GeoSearchArgs를 사용하여 geo.search 메소드를 통해 해당 조건에 맞는 객체들을 비동기적으로 검색합니다.
            .flatMap(r -> this.geo.search(r))
            // 검색 결과를 Iterable로 변환하여 각 요소에 대해 동작을 수행합니다.
            // 여기서는 Function.identity()를 통해 검색 결과 자체를 다음 단계로 전달합니다.
            .flatMapIterable(Function.identity())
            // 검색된 각 요소에 대해 System.out.println을 통해 콘솔에 출력합니다.
            .doOnNext(System.out::println)
            // 모든 검색 결과에 대한 처리가 완료된 후에는 아무런 결과도 반환하지 않는 Mono<Void>를 완료합니다.
            .then();

        // StepVerifier를 사용하여 생성된 Mono<Void>가 성공적으로 완료되는지 검증합니다.
        // 이는 리액티브 스트림의 테스트를 위한 Project Reactor의 유틸리티로,
        // 비동기적인 작업의 결과를 테스트하기 위해 사용됩니다.
        StepVerifier.create(mono)
            .verifyComplete();

    }


    @Test
    @DisplayName("가장 가까운 순으로 10개 조회 - GEORADIUS key longitude latitude 5 mi WITHCOORD ASC COUNT 10")
    public void searchClosest102() {
        this.map.get("75224")
            .map(gl -> GeoSearchArgs.from(gl.getLongitude(), gl.getLatitude())
                .radius(5, GeoUnit.MILES)
                .order(GeoOrder.ASC)
                .count(10))
            .flatMapMany(r -> this.geo.search(r))
            .doOnNext(System.out::println)
            .then()
            .as(StepVerifier::create)
            .verifyComplete();
    }

    @Test
    @DisplayName("가장 가까운 순으로 20개 조회 - GEORADIUS key longitude latitude 5 mi WITHCOORD ASC COUNT 20")
    public void searchClosest20() {
        this.map.get("75224")
            .map(gl -> GeoSearchArgs.from(gl.getLongitude(), gl.getLatitude())
                .radius(5, GeoUnit.MILES)
                .order(GeoOrder.ASC)
                .count(20))
            .flatMapMany(r -> this.geo.search(r))
            .doOnNext(System.out::println)
            .then()
            .as(StepVerifier::create)
            .verifyComplete();
    }

    @Test
    @DisplayName("이름에 'm'이 들어가는 가장 가까운 순 10개 조회 - GEORADIUS key longitude latitude 5 mi ASC COUNT 10")
    public void searchNameContainsMClosest10() {
        this.map.get("75224")
            .flatMapMany(gl -> this.geo.search(
                GeoSearchArgs.from(gl.getLongitude(), gl.getLatitude())
                    .radius(50, GeoUnit.MILES)
                    .order(GeoOrder.ASC)
                    .count(10))) // Redis 명령어: GEORADIUS key longitude latitude 5 mi ASC COUNT 10
            .flatMapIterable(Function.identity()) // List<Restaurant>를 Flux<Restaurant>로 변환
            .filter(restaurant -> restaurant.getName().contains("M")) // 이름에 'M'이 포함된 요소 필터링
            .take(10) // 최대 10개만 선택
            .doOnNext(System.out::println)
            .then()
            .as(StepVerifier::create)
            .verifyComplete();
    }


    @Test
    @DisplayName("Zip이 가장 작은 순 10개부터 조회 - Redis에서 직접 지원하지 않음, 애플리케이션 레벨에서 처리 - GEORADIUS key longitude latitude 50 mi WITHCOORD ASC\n")
    public void searchBySmallestZip10() {
        this.map.get("75224")
            .flatMapMany(gl -> this.geo.search(
                GeoSearchArgs.from(gl.getLongitude(), gl.getLatitude())
                    .radius(50, GeoUnit.MILES))) // 넓은 반경 설정
            .flatMapIterable(Function.identity()) // List<Restaurant>를 Flux<Restaurant>로 변환
            .sort(Comparator.comparing(Restaurant::getZip)) // Zip 코드를 기준으로 정렬
            .take(10) // 최대 10개만 선택
            .doOnNext(System.out::println)
            .then()
            .as(StepVerifier::create)
            .verifyComplete();
    }

}

```

