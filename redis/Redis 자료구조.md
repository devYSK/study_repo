# Redis 자료구조

종류

* Strings
* Lists
* Sets
* Hashes
* Sorted sets
* Streams
* Geospatial indexes
* Bitmaps

* Bitfields
* HyperLogLog





## Keys

Redis의 키는 이진 안전(binary safe)하며, 이는 "foo"와 같은 문자열부터 JPEG 파일의 내용까지 모든 이진 시퀀스를 키로 사용할 수 있다는 것을 의미합니다. 빈 문자열도 유효한 키입니다.

* 바이너리 세이프 하단 뜻은 키 이름으로 어떠한 바이너리를 사용해도 무방하다는 뜻입니다. 



Redis Key에 대한 규칙 

- 너무 긴 key는 좋지 않습니다.
  - 예를 들어 1024바이트의 키는 메모리 측면에서 뿐만 아니라 데이터 세트에서 키를 조회하려면 비용이 많이 드는 몇 가지 키 비교가 필요할 수 있기 때문에 좋지 않습니다.
  - 당면한 작업이 큰 값의 존재를 일치시키는 것일지라도 이를 해싱(예: SHA1 사용)하는 것이 특히 메모리와 대역폭의 관점에서 더 나은 생각입니다.
  - 따라서 메모리 측면에서 봤을 떄 긴 키값 그대로 사용하는것보다는 이를 해싱(예: SHA1 사용)하는 것이 특히 메모리와 대역폭의 관점에서 더 나으므로 길이를 줄여서 사용하는 것을 추천합니다.
- 너무 짧은 키도 역시 좋지 않습니다.
  - 대신 "user:1000:followers"라고 쓸 수 있다면 "u1000flw"를 키로 쓰는 것은 별 의미가 없습니다.
  - 후자는 더 읽기 쉽고 추가된 공간은 키 개체 자체와 값 개체에서 사용하는 공간에 비해 작습니다.
  - 짧은 키는 확실히 메모리를 조금 덜 사용하지만 **올바른 균형을 찾는 것이 여러분의 임무**입니다.
- 스키마를 고수하십시오.
  - 예를 들어 "object-type:id"는 "user:1000"과 같이 좋은 생각입니다.
  - .(점) 또는 -(대시)는 "comment:4321:reply.to" 또는 "comment:4321:reply-to"와 같이 여러 단어로 된 필드에 자주 사용됩니다.
- 허용되는 최대 키 크기는 512MB입니다.



## Syntax (문법)

```
redis 127.0.0.1:6379> {명령어} {키}
```

### Example (예제)

```
redis 127.0.0.1:6379> SET study:redis redis
OK
redis 127.0.0.1:6379> DEL study:redis
(integer) 1
```

위 예제에서 **DEL, SET** 은 명령어 입니다. 그리고 **study:redis** 가 바로 key입니다. 

처음에는 SET 명령어로 study:redis라는 키에 redis라는 값을 입력을 했습니다. 

그리고나서 DEL명령어로 해당 키를 삭제를 했습니다. 키를 삭제하면 키에 매핑되어있는 값에 접근할 방법이 없어지겠죠. 

키를 삭제할 때 키가 존재하고 정상적으로 삭제가 되었다면 **(integer) 1** 가 출력될 것이고, 아니면 **(integer) 0** 가 출력될 것입니다.

## Redis keys commands ( 레디스 키 명령어 )

Redis의 `SET` 명령어는 key-value 형태로 데이터를 저장하는데 사용되는 명령어입니다.

 `SET` 명령어를 사용하여 key-value 쌍을 저장하면, 해당 key에 대한 value 값을 가져오거나 수정할 수 있습니다.

`SET` 명령어의 기본 문법은 다음과 같습니다.

```shell
SET key value [EX seconds] [PX milliseconds] [NX|XX]
```

- `key`: 데이터를 저장할 key 이름
- `value`: key에 저장할 데이터 값
- `EX seconds`: key-value 쌍을 저장할 때 지정된 시간 (초) 동안 유효하도록 만듭니다.
- `PX milliseconds`: key-value 쌍을 저장할 때 지정된 시간 (밀리초) 동안 유효하도록 만듭니다.
- `NX|XX`: `NX`는 key가 존재하지 않을 때만 데이터를 저장하고, `XX`는 key가 존재할 때만 데이터를 저장합니다.

예를 들어, `SET` 명령어를 사용하여 `hello`라는 key에 `world`라는 값을 저장하면 다음과 같습니다.

```
markdownCopy code
> SET hello world
OK
```

위의 예제에서 `OK`는 데이터가 성공적으로 저장되었음을 의미합니다. `SET` 명령어를 사용하여 저장된 데이터를 가져오려면, `GET` 명령어를 사용합니다.

- Key 확인, 조회: [EXISTS ](http://redisgate.kr/redis/command/exists.php), [KEYS  ](http://redisgate.kr/redis/command/keys.php), [SCAN  ](http://redisgate.kr/redis/command/scan.php), [SORT  ](http://redisgate.kr/redis/command/sort.php)
- Key 이름 변경: [RENAME ](http://redisgate.kr/redis/command/rename.php), [RENAMENX ](http://redisgate.kr/redis/command/renamenx.php)
- Key 삭제: [UNLINK ](http://redisgate.kr/redis/command/unlink.php), [RM ](http://redisgate.kr/redis/command/rm.php)
- Key 자동 소멸 관련: [EXPIRE ](http://redisgate.kr/redis/command/expire.php), [EXPIREAT ](http://redisgate.kr/redis/command/expireat.php), [TTL ](http://redisgate.kr/redis/command/ttl.php), [PEXPIRE ](http://redisgate.kr/redis/command/pexpire.php), [EXPIREAT ](http://redisgate.kr/redis/command/expireat.php), [PTTL ](http://redisgate.kr/redis/command/pttl.php), [PERSIST ](http://redisgate.kr/redis/command/persist.php)
- 정보 확인: [TYPE ](http://redisgate.kr/redis/command/type.php), [OBJECT ](http://redisgate.kr/redis/command/object.php)
- 샘플링: [RANDOMKEY ](http://redisgate.kr/redis/command/randomkey.php)
- Data 이동: [MOVE ](http://redisgate.kr/redis/command/move.php), [DUMP ](http://redisgate.kr/redis/command/dump.php), [RESTORE ](http://redisgate.kr/redis/command/restore.php), [MIGRATE](http://redisgate.kr/redis/command/migrate.php)



| 명령어                                                       | 설명                                                         |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| KEYS pattern                                                 | 지정한 패턴과 일치하는 모든 키를 반환                        |
| EXISTS key                                                   | 지정한 키가 존재하는지 여부를 반환                           |
| DEL key [key ...]                                            | 지정한 키 및 연관된 값을 삭제. 삭제할 키들을 공백으로 구분해서 작성할 수도 있으며 삭제한 키들의 개수를 반환합니다 |
| TYPE key                                                     | 지정한 키의 데이터 유형을 반환                               |
| RANDOMKEY                                                    | 임의의 키를 반환                                             |
| RENAME key newkey                                            | 기존 키의 이름을 새로운 이름으로 변경                        |
| RENAMENX key newkey                                          | 새로운 이름으로 지정한 키가 존재하지 않으면, 기존 키의 이름을 새로운 이름으로 변경 |
| EXPIRE key seconds                                           | 지정한 키의 만료 시간을 설정                                 |
| TTL key                                                      | 지정한 키의 만료까지 남은 시간을 초 단위로 반환              |
| PTTL key                                                     | 지정한 키의 만료까지 남은 시간을 밀리초 단위로 반환          |
| PERSIST key                                                  | 지정한 키의 만료 시간을 제거하여 영속 키로 변경. 키에 설정된 만료일시를 삭제 |
| SCAN cursor [MATCH pattern] [COUNT count]                    | 패턴 및 개수 기반으로 키 스캔                                |
| UNLINK key                                                   | 지정한 Key를 삭제 (expire 시간과 무관하게 즉시 삭제)         |
| RM key (subquery)                                            | 지정한 Key를 삭제 (Enterprise server에서 사용 가능)          |
| SORT key [BY pattern] [LIMIT offset count] [GET pattern [GET pattern ...]] [ASC \| DESC] [ALPHA] [STORE destination] | 지정한 키의 값을 정렬하여 반환                               |
| EXPIREAT key timestamp                                       | 지정한 시간(Unix timestamp) 후 Key 자동 삭제                 |
| OBJECT subcommand key                                        | 지정한 Key에 대한 정보 조회                                  |
| PEXPIRE key milliseconds                                     | 지정한 시간(milliseconds) 후 Key 자동 삭제                   |
| PEXPIREAT key milliseconds-timestamp                         | 지정한 시간(Unix milliseconds-timestamp) 후 Key 자동 삭제    |
| MOVE key db                                                  | 지정한 Key를 다른 DB로 옮김. 성공시 1 실패시 0 반환          |
| DUMP key                                                     | 지정한 Key의 데이터를 dump                                   |
| RESTORE key ttl serialized-value [REPLACE]                   | 지정한 Key에 데이터를 restore                                |
| MIGRATE host port key destination-db timeout [COPY] [REPLACE] | 데이터를 다른 Redis Server로 이동/복사                       |
| SCAN cursor [MATCH pattern] [COUNT count]                    | 패턴 및 개수 기반으로 Key 스캔                               |
| [RENAME key newkey](http://www.tutorialspoint.com/redis/keys_rename.htm) | 키 이름을 새로운 키로 변경합니다.                            |
| [RENAMENX key newkey](http://www.tutorialspoint.com/redis/keys_renamenx.htm) | 키 이름을 새로운 키로 변경합니다. (단, 새로운 키와 동일한 이름의 키가 존재하지 않을 경우에만) |



#### **키 삭제 (unlink)**

레디스 db에 키 데이터가 많을때 del 커멘드는 수행시간이 오래 걸리므로 unlink명령어를 사용하는 것이 좋습니다.
unlink의 경우에는 back ground 작업으로 키를 제거합니다.

```
> SET key1 "Hello"
"OK"

> UNLINK key1 
(integer) 2
```

#### **모든 키 삭제 (flushall)**

```
> flushall 
```

#### **키 자동 삭제 (expire / ttl)**

**설정한 시간내로 key를 자동 삭제**하는 명령어 입니이다.

다만, 기간 삭제를 등록하고 바로 **set, getset**의 명령어를 expire 명령어 이후에 key에 다시 적용하면 expire 명령은 무효가 됩니다.

그러나 incr, lpush, sadd, zadd, hset과 같은 명령어는 유지되어 동작합니다. 

#### **데이터 영구저장 (save / bgsave)**

- 현재 입력한 key/value 값을 파일로 저장 가능
- config에서 설정하여 자동 저장도 가능
- exit 후 redis-server의 working directory 안에 rdb파일이 생성

```shell
> save # 포그라운드로 rdb 파일로 저장

> bgsave # 백그라운드로 저장
```





# Strings

Redis 문자열(Strings)은 텍스트, 직렬화된 개체 및 이진 배열을 포함하여 바이트 시퀀스를 저장합니다. 

따라서 문자열은 가장 기본적인 Redis 데이터 유형입니다. 캐싱에 자주 사용되지만 카운터를 구현하고 비트 연산을 수행할 수 있는 추가 기능도 지원합니다.

- 일반적인 문자열
- 값은 최대 512 MB이며, String으로 될 수 있는 binary data도, JPEG 이미지도 저장 가능하다.
- 단순 증감 연산에 좋음
- string-string 매핑을 이용하여 연결되는 자료 매핑을 할 수도 있다. HTML 매핑도 가능



## 예

- Redis에서 문자열을 저장하고 검색합니다.

```
> SET user:1 salvatore
OK
> GET user:1
"salvatore"
```

- 직렬화된 JSON 문자열을 저장하고 지금부터 100초 후에 만료되도록 설정합니다.

```
> SET ticket:27 "\"{'username': 'priya', 'ticket_id': 321}\"" EX 100
```

- 카운터 증가:

```
> INCR views:page:2
(integer) 1
> INCRBY views:page:2 10
(integer) 11
```



## 기본 명령

### 문자열 가져오기 및 설정

- [`SET`](https://redis.io/commands/set) : 문자열 값을 저장합니다.
- [`SETNX`](https://redis.io/commands/setnx) : 키가 아직 존재하지 않는 경우에만 문자열 값을 저장합니다. lock 구현에 유용합니다.
- [`GET` ](https://redis.io/commands/get) : 문자열 값을 검색합니다.
- [`MGET` ](https://redis.io/commands/mget): 단일 작업에서 여러 문자열 값을 검색합니다.

### 카운터 관리

- [`INCRBY`](https://redis.io/commands/incrby): 주어진 키에 저장된 카운터를 원자적으로 증가(및 음수를 전달할 때 감소)합니다.
- 부동 소수점 카운터에 대한 또 다른 명령이 있습니다: [INCRBYFLOAT](https://redis.io/commands/incrbyfloat) .

### 비트 연산

문자열에 대해 비트 연산을 수행하려면 [비트맵 데이터 유형](https://redis.io/docs/data-types/bitmaps) docs를 참조하세요.

[문자열 명령의 전체 목록을](https://redis.io/commands/?group=string) 참조하십시오 .

## 성능

대부분의 문자열 작업은 O(1)이므로 매우 효율적입니다. 그러나 O(n)이 될 수 있는 [`SUBSTR`](https://redis.io/commands/substr), [`GETRANGE`](https://redis.io/commands/getrange)및 명령에 주의하십시오. [`SETRANGE`](https://redis.io/commands/setrange)이러한 임의 액세스 문자열 명령은 큰 문자열을 처리할 때 성능 문제를 일으킬 수 있습니다.

## 대안

[구조화된 데이터를 직렬화된 문자열로 저장하는 경우 Redis 해시](https://redis.io/docs/data-types/hashes) 또는 [RedisJSON 을](https://redis.io/docs/stack/json) 고려할 수도 있습니다 .



| 명령어                                                       | 설명                                                         |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| SET key value [EX seconds] [PX milliseconds] [NX\|XX]<br />'SET user:name Alice EX 60' | key에 value를 저장합니다.<br />EX seconds만큼 유효 기간을 초 단위로 설정.<br />PX milliseconds만큼 유효기간을 초 단위로 설정.<br />`NX` 옵션을 사용하면 key가 존재하지 않을 경우에만 데이터를 저장하고,<br /> `XX` 옵션을 사용하면 key가 이미 존재할 경우에만 데이터를 저장합니다. <br />옵션을 생략하면 기본값인 `NX`가 적용됩니다. |
| GET key                                                      | key에 저장된 value를 반환합니다.                             |
| MGET key [key ...]                                           | 여러 개의 key에 대한 value를 반환합니다.                     |
| GETSET key value                                             | key에 저장된 이전 value를 반환하고, 새로운 value를 저장합니다. |
| SETNX key value                                              | key에 value가 없을 경우에만 저장합니다.                      |
| SETEX key seconds value                                      | key에 value와 TTL을 저장합니다.                              |
| PSETEX key milliseconds value                                | key에 value와 TTL(millisecond)을 저장합니다.                 |
| MSET key value [key value ...]                               | 여러 개의 key-value 쌍을 저장합니다.                         |
| MSETNX key value [key value ...]                             | 여러 개의 key-value 쌍을 저장합니다. 단, key가 모두 없을 때만 저장됩니다. |
| APPEND key value                                             | key에 value를 추가합니다.                                    |
| STRLEN key                                                   | key에 저장된 value의 길이를 반환합니다.                      |
| INCR key                                                     | key에 저장된 값을 1 증가시킵니다. 신규이면 1로 set           |
| INCRBY key increment                                         | key에 저장된 값을 increment 만큼 증가시킵니다. <br />신규이면 increment로 set |
| DECR key                                                     | key에 저장된 값을 1 감소시킵니다.                            |
| DECRBY key decrement                                         | key에 저장된 값을 decrement 만큼 감소시킵니다.               |
| GETRANGE key start end                                       | key에 저장된 value에서 start부터 end까지의 문자열을 반환합니다. |
| SETRANGE key offset value                                    | key에 저장된 value에서 offset 위치부터 value로 덮어쓰기합니다. |
| GETBIT key offset                                            | key에 저장된 value에서 offset 위치의 비트를 반환합니다.      |
| SETBIT key offset value                                      | key에 저장된 value에서 offset 위치의 비트를 value로 설정합니다. |



# Bitmaps(Bits)

- bitmaps은 string의 변형
- bit 단위 연산 가능하다.
- String이 512MB 저장 할 수 있듯이 2^32 bit까지 사용 가능하다.
- 저장할 때, 저장 공간 절약에 큰 장점이 있다.



Redis Bitmaps는 Redis의 하나의 데이터 타입으로, 각 비트(bit)가 하나의 값을 가질 수 있는 비트 배열(bit array)입니다. 

Redis Bitmaps를 사용하면, 단일 비트에서 참(True) 또는 거짓(False) 여부를 저장할 수 있으며, 이를 사용하여 여러 비트를 그룹화하고 비트맵의 전체 상태를 추적할 수 있습니다.

Redis Bitmaps는 다음과 같은 일반적인 용도로 사용됩니다.

- 사용자의 로그인 여부를 추적
- 요일별 또는 시간대별 트래픽 추적
- 정기적인 이벤트의 수집
- 필터링 또는 범주화를 위한 set의 추적



**성능**

[`SETBIT`](https://redis.io/commands/setbit)및 [`GETBIT`](https://redis.io/commands/getbit)O(1)입니다. [`BITOP`](https://redis.io/commands/bitop)는 O(n)이며, 여기서 *n* 은 비교에서 가장 긴 문자열의 길이입니다.



| 명령어                                                       | 설명                                                         |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| GETBIT key offset                                            | 지정한 key의 offset 위치에 있는 bit값을 가져옵니다.          |
| SETBIT key offset value                                      | 지정한 key의 offset 위치에 있는 bit값을 설정합니다.          |
| BITCOUNT key [start end]                                     | 지정한 key에서 start와 end 범위 내에 있는 모든 bit값이 1인 bit 수를 반환합니다. |
| BITOP operation destKey key [key ...]                        | 여러 개의 Key들의 Value를 지정한 Bit operation으로 처리한 후 결과 값을 destKey에 저장합니다. bit 연산( AND, OR, XOR, NOT |
| BITFIELD key [GET type offset] [SET type offset value] [INCRBY type offset increment] | 지정한 key의 bit값을 다양한 방식으로 조작합니다. GET, SET, INCRBY 등의 서브 커맨드를 이용할 수 있습니다. |



# Lists

Redis 목록은 문자열 값의 연결된 목록입니다. Redis 목록은 다음과 같은 용도로 자주 사용됩니다.

- 스택과 큐를 구현
- background worker systems을 위한 queue를 구축

- array 형식의 데이터 구조. 데이터를 순서대로 저장 
- 추가 / 삭제 / 조회하는 것은 O(1)의 속도를 가지지만, 중간의 특정 index 값을 조회할 때는 O(N)의 속도를 가지는 단점이 있다.
- 즉, 중간에 추가/삭제가 느리다. 따라서 head-tail에서 추가/삭제 한다. (push / pop 연산)
- 메세지 queue로 사용하기 적절하다.



## Lists 명령어

- **SET (PUSH)**: LPUSH, RPUSH, LPUSHX, RPUSHX, LSET, LINSERT, RPOPLPUSH
- **GET**: LRANGE, LINDEX, LLEN
- **POP**: LPOP, RPOP, BLPOP, BRPOP
- **REM**: LREM, LTRIM
- **BLOCK**: BLPOP, BRPOP, BRPOPLPUSH
- **Enterprise**: LREVRANGE, LPUSHS, RPUSHS (subquery)



| 명령어                                                       | 문법                                  | 설명                                                         |
| ------------------------------------------------------------ | ------------------------------------- | ------------------------------------------------------------ |
| [LPUSH](http://redisgate.kr/redis/command/lpush.php)         | LPUSH key value [value ...]           | 리스트의 왼쪽에 데이터를 저장                                |
| [RPOP](http://redisgate.kr/redis/command/rpop.php)           | RPOP key                              | 리스트의 오른쪽에서 데이터를 꺼내오고, 리스트에서는 삭제     |
| [LPOP](http://redisgate.kr/redis/command/lpop.php)           | LPOP key                              | 리스트의 왼쪽에서 데이터를 꺼내오고, 리스트에서는 삭제       |
| [RPUSH](http://redisgate.kr/redis/command/rpush.php)         | RPUSH key value [value ...]           | 리스트의 오른쪽에 데이터를 저장                              |
| [LRANGE](http://redisgate.kr/redis/command/lrange.php)       | LRANGE key start stop                 | 인덱스로 범위를 지정해서 리스트를 조회                       |
| [LLEN](http://redisgate.kr/redis/command/llen.php)           | LLEN key                              | 리스트에 있는 데이터의 총 갯수를 조회                        |
| [LINDEX](http://redisgate.kr/redis/command/lindex.php)       | LINDEX key index                      | 인덱스로 특정 위치의 데이터를 조회                           |
| [LSET](http://redisgate.kr/redis/command/lset.php)           | LSET key index value                  | 인덱스로 특정 위치의 값을 변경                               |
| [LREM](http://redisgate.kr/redis/command/lrem.php)           | LREM key count value                  | 값을 지정해서 삭제                                           |
| [LTRIM](http://redisgate.kr/redis/command/ltrim.php)         | LTRIM key start stop                  | 인덱스로 지정한 범위 밖의 값들을 삭제                        |
| [RPOPLPUSH](http://redisgate.kr/redis/command/rpoplpush.php) | RPOPLPUSH source destination          | 리스트의 오른쪽에서 데이터를 꺼내서 다른 리스트 왼쪽에 넣음  |
| [BLPOP](http://redisgate.kr/redis/command/blpop.php)         | BLPOP key [key ...] timeout           | 리스트에 값이 없을 경우, 지정한 시간만큼 기다려서 값이 들어오면 LPOP 실행 |
| [BRPOP](http://redisgate.kr/redis/command/brpop.php)         | BRPOP key [key ...] timeout           | 리스트에 값이 없을 경우, 지정한 시간만큼 기다려서 값이 들어오면 RPOP 실행 |
| [BRPOPLPUSH](http://redisgate.kr/redis/command/brpoplpush.php) | BRPOPLPUSH source destination timeout | 리스트에 값이 없을 경우, 지정한 시간만큼 기다려서 값이 들어오면 RPOPLPUSH 실행 |
| [LINSERT](http://redisgate.kr/redis/command/linsert.php)     | LINSERT key BEFORE\|AFTER pivot value | 지정한 값 앞/뒤에 새 값을 저장                               |

| Commands                                                     | Syntax             | Description                              |
| ------------------------------------------------------------ | ------------------ | ---------------------------------------- |
| [LPUSHX](http://redisgate.kr/redis/command/lpushx.php)       | key value          | 기존에 리스트가 있을 경우에만 LPUSH 실행 |
| [RPUSHX](http://redisgate.kr/redis/command/rpushx.php)       | key value          | 기존에 리스트가 있을 경우에만 RPUSH 실행 |
| [LPOS](http://redisgate.kr/redis/command/lpos.php)           | key element        | 값으로 인덱스를 조회                     |
| [LMOVE](http://redisgate.kr/redis/command/lmove.php)         | source destination | 리스트간 데이터 이동                     |
| [BLMOVE](http://redisgate.kr/redis/command/blmove.php)       | source destination | 리스트간 데이터 이동 - 대기              |
| [LLS](http://redisgate.kr/redis/command/lls.php)             | key pattern        | 패턴(pattern)으로 값(value) 조회         |
| [LRM](http://redisgate.kr/redis/command/lrm.php)             | key pattern        | 패턴(pattern)으로 값(value) 삭제         |
| [LREVRANGE](http://redisgate.kr/redis/command/lrevrange.php) | key key start stop | 인덱스로 범위를 지정해서 역순으로 조회   |
| [LPUSHS](http://redisgate.kr/redis/command/lpush_subquery.php) | key (subquery)     | 서브쿼리로 데이터를 저장                 |
| [RPUSHS](http://redisgate.kr/redis/command/rpush_subquery.php) | key (subquery)     | 서브쿼리로 데이터를 저장                 |





# HyperLogLogs

HyperLogLogs는 대규모의 고유한 요소(원소) 개수를 추정하기 위해 사용됩니다.

HyperLogLogs는 기본적으로 비트맵을 사용하여 데이터를 저장하므로 매우 적은 메모리를 사용하여 대규모의 데이터를 다룰 수 있습니다.

요소(element)가 중복되지 않는다는 것이 HyperLogLogs의 가장 중요한 특징입니다. 이를 위해 Redis는 MurmurHash3 알고리즘을 사용하여 요소를 해싱합니다.

HyperLogLogs는 다양한 Redis 명령을 사용하여 조작할 수 있습니다. 이 명령어들은 요소(element)를 추가하거나, 구성 요소(component)를 합치거나, 하나 이상의 HyperLogLog 구조체를 결합하여 하나의 구조체로 만들거나, HyperLogLog 구조체의 추정된 기준치를 조회하는 등의 작업을 수행합니다.

HyperLogLogs는 대규모의 고유한 요소 개수를 추정해야하는 경우에 특히 유용합니다. 예를 들어, 웹사이트 방문자 수를 추정하거나, 클릭 수를 추적하는 등의 경우에 사용됩니다.

- 굉장히 많은양의 데이터를 dump할때 사용
- 중복되지 않는 대용량 데이터를 count할때 주로 많이 사용한다. (오차 범위 0.81%)
- set과 비슷하지만 저장되는 용량은 매우 작다 (저장 되는 모든 값이 12kb 고정)
- 하지만 저장된 데이터는 다시 확인할 수 없다. (데이터 보호에 적절)

> 엄청 크고 유니크 한 값 카운팅 할 때 사용
> → 웹 사이트 방문 ip 개수 카운팅, 하루 종일 크롤링 한 url 개수 몇개 인지, 검색 엔진에서 검색 한 단어 몇개 인지



## HyperLogLog 명령어

| 명령어                                                   | 문법                                      | 설명                                                         |
| -------------------------------------------------------- | ----------------------------------------- | ------------------------------------------------------------ |
| [PFADD](http://redisgate.kr/redis/command/pfadd.php)     | PFADD key ele [ele ...]                   | HyperLogLog 구조체에 하나 이상의 원소(element)를 추가합니다. |
| [PFCOUNT](http://redisgate.kr/redis/command/pfcount.php) | PFCOUNT key [key ...]                     | HyperLogLog 구조체의 원소(element) 개수를 반환합니다.        |
| [PFMERGE](http://redisgate.kr/redis/command/pfmerge.php) | PFMERGE destkey sourcekey [sourcekey ...] | 하나 이상의 HyperLogLog 구조체를 합쳐(destkey) 하나의 구조체로 만듭니다. |





# Sets

Redis Set은 고유한 원소(unique element)만을 저장하는 정렬되지 않은(Unordered) 데이터 구조입니다. Set은 멤버가 있는지 여부를 확인하고, 교집합, 합집합, 차집합 등 다양한 집합 연산을 지원합니다.

Redis Set의 내부 구조는 hash table로 구현되어 있으며, 각각의 키(Key)에 대해 하나의 값(Value)만을 가지고 있습니다. Set은 멤버의 추가, 제거, 존재 여부 확인 등의 연산을 O(1)의 시간 복잡도로 수행합니다.

- 중복된 데이터를 담지 않기 위해 사용하는 자료구조 (js의 set이라고 생각하면 된다)
- 유니크한 key값
- 정렬되지 않은 집합
- Redis set의 최대 크기는 2^32 - 1
- 중복된 데이터를 여러번 저장하면 최종 한번만 저장된다.
- Set간의 연산을 지원. 교집합, 합집합, 차이를 매우 빠른 시간내에 추출할 수 있다.
- 단, 모든 데이터를 전부 다 갖고올 수 있는 명령이 있으므로 주의해서 사용해야 한다.
- 멤버의 추가, 제거, 존재 여부 확인 등의 연산을 O(1)의 시간 복잡도로 수행합니다.
- 멤버의 개수가 많아지면 O(N)의 시간 복잡도로 연산 속도가 느려질 수 있습니다.

- 팔로워 리스트, 친구리스트 ⇒ 특정 그룹의 사용
- Spring Security Oauth의 Access Token을 저장하는 Redis Token Store 방식

## Sets 명령어

- **SET**: SADD, SMOVE
- **GET**: SMEMBERS, SCARD, SRANDMEMBER, SISMEMBER, SSCAN
- **POP**: SPOP
- **REM**: SREM
- **집합연산**: SUNION, SINTER, SDIFF, SUNIONSTORE, SINTERSTORE, SDIFFSTORE
- **Enterprise**: SLS, SRM, SLEN, SADDS (subquery)

| 명령어                                                       | 문법                                           | 설명                                                         |
| ------------------------------------------------------------ | ---------------------------------------------- | ------------------------------------------------------------ |
| [SADD](http://redisgate.kr/redis/command/sadd.php)           | SADD key member [member ...]                   | 집합에 member를 추가                                         |
| [SREM](http://redisgate.kr/redis/command/srem.php)           | SREM key member [member ...]                   | 집합에서 member를 삭제                                       |
| [SMEMBERS](http://redisgate.kr/redis/command/smembers.php)   | SMEMBERS key                                   | 집합의 모든 member를 조회                                    |
| [SCARD](http://redisgate.kr/redis/command/scard.php)         | SCARD key                                      | 집합에 속한 member의 갯수를 조회                             |
| [SUNION](http://redisgate.kr/redis/command/sunion.php)       | SUNION key [key ...]                           | 합집합을 구함                                                |
| [SINTER](http://redisgate.kr/redis/command/sinter.php)       | SINTER key [key ...]                           | 교집합을 구함                                                |
| [SDIFF](http://redisgate.kr/redis/command/sdiff.php)         | SDIFF key [key ...]                            | 차집합을 구함                                                |
| [SUNIONSTORE](http://redisgate.kr/redis/command/sunionstore.php) | SUNIONSTORE dest_key src_key [src_key ...]     | 합집합을 구해서 새로운 집합에 저장                           |
| [SINTERSTORE](http://redisgate.kr/redis/command/sinterstore.php) | SINTERSTORE dest_key src_key [src_key ...]     | 교집합을 구해서 새로운 집합에 저장                           |
| [SDIFFSTORE](http://redisgate.kr/redis/command/sdiffstore.php) | SDIFFSTORE dest_key src_key [src_key ...]      | 차집합을 구해서 새로운 집합에 저장                           |
| [SISMEMBER](http://redisgate.kr/redis/command/sismember.php) | SISMEMBER key member                           | 집합에 member가 존재하는지 확인                              |
| [SMOVE](http://redisgate.kr/redis/command/smove.php)         | SMOVE src_key dest_key member                  | 소스 집합의 member를 목적 집합으로 이동                      |
| [SPOP](http://redisgate.kr/redis/command/spop.php)           | SPOP key [count]                               | 집합에서 무작위로 member를 가져옴                            |
| [SRANDMEMBER](http://redisgate.kr/redis/command/srandmember.php) | SRANDMEMBER key [count]                        | 집합에서 무작위로 member를 조회                              |
| [SSCAN](http://redisgate.kr/redis/command/sscan.php)         | SSCAN key cursor [MATCH pattern] [COUNT count] | member를 일정 단위 갯수 만큼씩 조회                          |
| [SMISMEMBER](http://redisgate.kr/redis/command/smismember.php) | SMISMEMBER key member [member ...]             | 집합에 member가 존재하는지 확인 - 여러 개 가능               |
| [SLS  ](http://redisgate.kr/redis/command/sls.php)           | SLS key pattern                                | 주어진 패턴(pattern)과 일치하는 값(value)들을 조회하여 반환한다. |
| [SRM  ](http://redisgate.kr/redis/command/srm.php)           | SRM key pattern                                | 패턴(pattern)으로 값(value)을 삭제합니다.                    |
| [SLEN  ](http://redisgate.kr/redis/command/slen.php)         | SLEN key                                       | 키에 속한 멤버 개수를 반환                                   |
| [SADDS  ](http://redisgate.kr/redis/command/sadd_subquery.php) | SADDS key (subquery)                           | 서브쿼리로 member를 추가                                     |



# Sorted Set

(sorted set)은 각각의 멤버(member)에 대해 연관된 값을 가지는 정렬된 컬렉션입니다. 정렬된 집합의 멤버는 고유한 식별자(identifier)인 문자열로 지정됩니다. 이 식별자를 통해 정렬된 집합은 식별자에 대한 순서를 보장합니다.

정렬된 집합에서는 멤버의 값(value)에 대해 정렬을 수행합니다. Redis는 이 값을 double precision 부동소수점 숫자로 저장합니다.

Redis의 정렬된 집합은 다음과 같은 작업을 수행하는 데 최적화되어 있습니다.

- 값(value)에 따른 정렬된 집합(sorted set)에서 범위(range) 검색
- 값(value)에 따른 정렬된 집합(sorted set)에서 멤버(member) 수를 세는 것
- 값(value)에 따른 정렬된 집합(sorted set)에서 두 멤버(member)의 교집합(intersection) 또는 합집합(union)을 구하는 것

이러한 작업은 일반적으로 O(log(n)) 복잡도로 수행됩니다.

- 아이템들의 랭킹을 가지는데에 사용
  - 유저 랭킹 보드서버도 가능
- Double 형태, 특정 정수값을 사용할 수 없음
- Skiplist 자료구조
  - O(log n)의 검색속도
- set에 score라는 필드가 추가된 데이터 형 (score는 일종의 가중치)
- 일반적으로 set은 정렬이 되어있지않고 insert 한 순서대로 들어간다.
  그러나 Sorted Set은 Set의 특성을 그대로 가지며 추가적으로 저장된 member들의 순서도 관리한다. 
- 데이터가 저장될때부터 score 순으로 정렬되며 저장
- sorted set에서 데이터는 오름차순으로 내부 정렬
- value는 중복 불가능, score는 중복 가능
- 만약 score 값이 같으면 사전 순으로 정렬되어 저장 

## Sorted Sets 명령어

- **SET**: ZADD
- **GET**: ZRANGE, ZRANGEBYSCORE, ZRANGEBYLEX, ZREVRANGE, ZREVRANGEBYSCORE, ZREVRANGEBYLEX, ZRANK, ZREVRANK, ZSCORE, ZCARD, ZCOUNT, ZLEXCOUNT, ZSCAN
- **POP**: ZPOPMIN, ZPOPMAX
- **REM**: ZREM, ZREMRANGEBYRANK, ZREMRANGEBYSCORE, ZREMRANGEBYLEX
- **INCR**: ZINCRBY
- **집합연산**: ZUNIONSTORE, ZINTERSTORE
- **Enterprise**: ZISMEMBER, ZLS, ZRM, SLEN, SADDS (subquery)



| Commands                                                     | Syntax                                                       | Description                                  |
| ------------------------------------------------------------ | ------------------------------------------------------------ | -------------------------------------------- |
| [ZADD  ](http://redisgate.kr/redis/command/zadd.php)         | ZADD key score member [score member ...]                     | 집합에 score와 member를 추가                 |
| [ZCARD  ](http://redisgate.kr/redis/command/zcard.php)       | ZCARD key                                                    | 집합에 속한 member의 갯수를 조회             |
| [ZINCRBY  ](http://redisgate.kr/redis/command/zincrby.php)   | ZINCRBY key increment member                                 | 지정한 만큼 score 증가, 감소                 |
| [ZRANGE  ](http://redisgate.kr/redis/command/zrange.php)     | ZRANGE key start stop [withscores]                           | index로 범위를 지정해서 조회                 |
| [ZRANGEBYSCORE  ](http://redisgate.kr/redis/command/zrangebyscore.php) | ZRANGEBYSOCRE key min max [withscores] [limit offset count]  | score로 범위를 지정해서 조회                 |
| [ZREM  ](http://redisgate.kr/redis/command/zrem.php)         | ZREM key member [member ...]                                 | 집합에서 member를 삭제                       |
| [ZREMRANGEBYSCORE  ](http://redisgate.kr/redis/command/zremrangebyscore.php) | ZREMRANGEBYSCORE  min max                                    | score로 범위를 지정해서 member를 삭제        |
| [ZREVRANGE  ](http://redisgate.kr/redis/command/zrevrange.php) | ZREVRANGE key start stop [withscores]                        | index로 범위를 지정해서 큰 것부터 조회       |
| [ZSCORE  ](http://redisgate.kr/redis/command/zscore.php)     | ZSCORE key member                                            | member를 지정해서 score를 조회               |
| [ZCOUNT  ](http://redisgate.kr/redis/command/zcount.php)     | ZCOUNT key min max                                           | score로 범위를 지정해서 갯수 조회            |
| [ZRANK  ](http://redisgate.kr/redis/command/zrank.php)       | ZRANK key member                                             | member를 지정해서 rank(index)를 조회         |
| [ZREVRANK  ](http://redisgate.kr/redis/command/zrevrank.php) | ZREVRANK key member                                          | member를 지정해서 reverse rank(index)를 조회 |
| [ZREMRANGEBYRANK  ](http://redisgate.kr/redis/command/zremrangebyrank.php) | ZREMRANGEBYRANK key start stop                               | index로 범위를 지정해서 member를 삭제        |
| [ZUNIONSTORE  ](http://redisgate.kr/redis/command/zunionstore.php) | ZUNIONSTORE dest_key numkeys src_key [src_key ...] [WEIGHTS weight [weight ...]] [AGGREGATE SUM\|MIN\|MAX] | 합집합을 구해서 새로운 집합에 저장           |
| [ZINTERSTORE  ](http://redisgate.kr/redis/command/zinterstore.php) | ZINTERSTORE dest_key numkeys src_key [src_key ...] [WEIGHTS weight [weight ...]] [AGGREGATE SUM\|MIN\|MAX] | 교집합을 구해서 새로운 집합에 저장           |
| [ZREVRANGEBYSCORE  ](http://redisgate.kr/redis/command/zrevrangebyscore.php) | ZREVRANGEBYSCORE key max min [withscores] [limit offset count] | score로 범위를 지정해서 큰 것부터 조회       |
| [ZSCAN  ](http://redisgate.kr/redis/command/zscan.php)       | ZSCAN key cursor [MATCH pattern] [COUNT count]               | score, member를 일정 단위 갯수 만큼씩 조회   |
| [ZRANGEBYLEX  ](http://redisgate.kr/redis/command/zrangebylex.php) | ZRANGEBYLEX key min max [limit offset count]                 | member로 범위를 지정해서 조회                |
| [ZLEXCOUNT  ](http://redisgate.kr/redis/command/zlexcount.php) | ZLEXCOUNT key min max                                        | member로 범위를 지정해서 갯수 조회           |
| [ZREMRANGEBYLEX  ](http://redisgate.kr/redis/command/zremrangebylex.php) | ZREMRANGEBYLEX key min max                                   | member로 범위를 지정해서 member를 삭제       |
| [ZREVRANGEBYLEX  ](http://redisgate.kr/redis/command/zrevrangebylex.php) | ZREVRANGEBYLEX key max min [limit offset count]              | member로 범위를 지정해서 큰 것부터 조회      |
| [ZPOPMIN  ](http://redisgate.kr/redis/command/zpopmin.php)   | ZPOPMIN key                                                  | 작은 값부터 꺼내온다                         |
| [ZPOPMAX  ](http://redisgate.kr/redis/command/zpopmax.php)   | ZPOPMAX key                                                  | 큰 값부터 꺼내온다                           |
| [BZPOPMIN  ](http://redisgate.kr/redis/command/bzpopmin.php) | BZPOPMIN key                                                 | 데이터가 들어오면 작은 값부터 꺼내온다       |
| [BZPOPMAX  ](http://redisgate.kr/redis/command/bzpopmax.php) | BZPOPMAX key                                                 | 데이터가 들어오면 큰 값부터 꺼내온다         |
| [ZMSCORE  ](http://redisgate.kr/redis/command/zmscore.php)   | ZMSCORE member [member ...]                                  | member의 score를 리턴 - 여러 개 가능         |
| [ZRANDMEMBER  ](http://redisgate.kr/redis/command/zrandmember.php) | ZRANDMEMBER key                                              | 임의(random)의 멤버를 조회                   |
| [ZRANGESTORE  ](http://redisgate.kr/redis/command/zrangestore.php) | ZRANGESTORE dst src start stop                               | 조회해서 다른 키에 저장                      |
| [ZUNION  ](http://redisgate.kr/redis/command/zunion.php)     | ZUNION numkeys key [key ...]                                 | 합집합을 구함                                |
| [ZINTER  ](http://redisgate.kr/redis/command/zinter.php)     | ZINTER numkeys key [key ...]                                 | 교집합을 구함                                |
| [ZDIFF  ](http://redisgate.kr/redis/command/zdiff.php)       | ZDIFF numkeys key [key ...]                                  | 차집합을 구함                                |
| [ZDIFFSTORE  ](http://redisgate.kr/redis/command/zdiffstore.php) | ZDIFFSTORE destination numkeys key [key ...]                 | 차집합을 구해서 새로운 집합에 저장           |
| [ZISMEMBER  ](http://redisgate.kr/redis/command/zismember.php) | ZISMEMBER key member                                         | 집합에 member가 존재하는지 확인              |
| [ZLS  ](http://redisgate.kr/redis/command/zls.php)           | ZLS key pattern                                              | 패턴(pattern)으로 값(value) 조회             |
| [ZRM  ](http://redisgate.kr/redis/command/zrm.php)           | ZRM key pattern                                              | 패턴(pattern)으로 값(value) 삭제             |
| [ZLEN  ](http://redisgate.kr/redis/command/zlen.php)         | ZLEN key                                                     | 키에 속한 멤버 개수를 리턴                   |
| [ZADDS  ](http://redisgate.kr/redis/command/zadd_subquery.php) | ZADDS key (subquery)                                         | 서브쿼리로 데이터 추가                       |



# Hashes

Redis 해시는 어떤 언어에서든 매우 쉽게 사용할 수 있으며, 복잡한 쿼리를 작성하지 않고도 해시 내부의 필드를 쉽게 조작할 수 있습니다. 예를 들어, Redis 해시를 사용하여 사용자 프로필을 저장할 수 있으며, 이를 통해 다음과 같은 기능을 구현할 수 있습니다.

1. 새 사용자를 추가하거나 기존 사용자의 정보를 업데이트합니다.
2. 사용자가 마지막으로 로그인한 시간을 추적합니다.
3. 사용자가 수행한 작업 수를 추적합니다.

해시는 여러 개의 필드를 갖는다는 점에서 Redis 문자열과 다릅니다. 필드와 값은 모두 문자열입니다. Redis는 이를 처리하기 위한 몇 가지 명령어를 제공합니다.

- key-value로 구성 되어있는 전형적인 hash의 형태 (파이썬의 딕셔너리나 js객체 정도로 이해하면 된다)
- key 하위에 subkey를 이용해 추가적인 Hash Table을 제공하는 자료구조
- 메모리가 허용하는 한, 제한없이 field들을 넣을 수가 있다.
- 특정군의 data로 묶기
- key 하위에 subkey를 이용해 추가적인 Hash Table을 제공하는 자료구조



## Hashes 명령어 

- **SET**: HSET, HMSET, HSETNX
- **GET**: HGET, HMGET, HLEN, HKEYS, HVALS, HGETALL, HSTRLEN, HSCAN, HEXISTS
- **REM**: HDEL
- **INCR**: HINCRBY, HINCRBYFLOAT

| 명령어                                                       | 문법                                           | 설명                                                         |
| ------------------------------------------------------------ | ---------------------------------------------- | ------------------------------------------------------------ |
| [HSET](http://redisgate.kr/redis/command/hset.php)           | HSET key field value                           | 지정된 key에 field와 value를 저장                            |
| [HDEL](http://redisgate.kr/redis/command/hdel.php)           | HDEL key field [field ...]                     | key에서 지정된 field와 그에 해당하는 value 삭제              |
| [HGET](http://redisgate.kr/redis/command/hget.php)           | HGET key field                                 | key에서 지정된 field의 value 조회                            |
| [HLEN](http://redisgate.kr/redis/command/hlen.php)           | HLEN key                                       | key에 저장된 field의 개수 조회                               |
| [HMSET](http://redisgate.kr/redis/command/hmset.php)         | HMSET key field value [field value ...]        | 여러개의 field와 value를 한 번에 저장                        |
| [HMGET](http://redisgate.kr/redis/command/hmget.php)         | HMGET key field [field ...]                    | 여러개의 field에 해당하는 value를 조회                       |
| [HKEYS](http://redisgate.kr/redis/command/hkeys.php)         | HKEYS key                                      | key에 저장된 모든 field name 조회                            |
| [HVALS](http://redisgate.kr/redis/command/hvals.php)         | HVALS key                                      | key에 저장된 모든 value 조회                                 |
| [HGETALL](http://redisgate.kr/redis/command/hgetall.php)     | HGETALL key                                    | key에 저장된 모든 field와 value 조회                         |
| [HINCRBY](http://redisgate.kr/redis/command/hincrby.php)     | HINCRBY key field increment                    | key에서 지정된 field의 value를 increment만큼 증가 또는 감소  |
| [HEXISTS](http://redisgate.kr/redis/command/hexists.php)     | HEXISTS key field                              | key에서 지정된 field가 존재하는지 확인                       |
| [HSETNX](http://redisgate.kr/redis/command/hsetnx.php)       | HSETNX key field value                         | key에 지정된 field가 없을 때만 value 저장                    |
| [HINCRBYFLOAT](http://redisgate.kr/redis/command/hincrbyfloat.php) | HINCRBYFLOAT key field increment_float         | key에서 지정된 field의 value를 increment_float만큼 증가 또는 감소 |
| [HSCAN](http://redisgate.kr/redis/command/hscan.php)         | HSCAN key cursor [MATCH pattern] [COUNT count] | key에서 일정 단위의 field와 그에 해당하는 value 조회         |
| [HSTRLEN](http://redisgate.kr/redis/command/hstrlen.php)     | HSTRLEN key field                              | key에서 지정된 field의 value 길이(byte) 조회                 |



# Streams

Redis Streams는 메시지의 순서가 중요한 스트림 데이터 구조를 제공합니다. 스트림 데이터 구조는 다음과 같은 개념을 가지고 있습니다.

- **Stream**: 일련의 메시지를 저장하는 대용량, 연속적인 데이터 구조입니다.
- **Message**: 특정 순서를 가진 키-값 쌍의 일련의 데이터입니다.
- **Consumer Group**: 메시지를 처리하는 Consumer 그룹입니다.
- **Consumer**: Consumer 그룹 내에서 실제로 메시지를 처리하는 구성 요소입니다.

스트림은 FIFO(First In First Out) 데이터 구조이며, 새 메시지는 스트림의 끝에 추가됩니다. 각 메시지는 일련의 필드를 가질 수 있으며, 각 필드는 키-값 쌍으로 구성됩니다.

스트림은 특히 데이터가 발생하는 경우와 같이 대량의 이벤트를 처리하는 데 적합합니다. 예를 들어, 시스템 로그, 센서 데이터, 메시지 큐 등이 이에 해당합니다.

- Streams는 log를 저장하기 가장 좋은 자료 구조
- append-only 이며 중간에 데이터가 바뀌지 않는다.
- 읽어 올때 id값 기반으로 시간 범위로 검색
- tail -f 사용 하는 것 처럼 신규 추가 데이터 수신



## Streams 명령어

| 명령어                                                       | 구문                                                         | 설명                                                         |
| ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| [XADD](http://redisgate.kr/redis/command/xadd.php)           | XADD key ID field string [field string ...]                  | 스트림에 데이터를 추가합니다.                                |
| [XLEN](http://redisgate.kr/redis/command/xlen.php)           | XLEN key                                                     | 스트림의 길이를 반환합니다.                                  |
| [XRANGE](http://redisgate.kr/redis/command/xrange.php)       | XRANGE key start end [COUNT count]                           | 주어진 범위 내의 스트림 엔트리를 가져옵니다.                 |
| [XREVRANGE](http://redisgate.kr/redis/command/xrevrange.php) | XREVRANGE key end start [COUNT count]                        | 주어진 범위의 역순으로 스트림 엔트리를 가져옵니다.           |
| [XREAD](http://redisgate.kr/redis/command/xread.php)         | XREAD [COUNT count] [BLOCK milliseconds] STREAMS key [key ...] ID [ID ...] | 주어진 스트림 ID 뒤의 엔트리를 가져옵니다.                   |
| [XDEL](http://redisgate.kr/redis/command/xdel.php)           | XDEL key ID [ID ...]                                         | 주어진 스트림 ID의 엔트리를 삭제합니다.                      |
| [XTRIM](http://redisgate.kr/redis/command/xtrim.php)         | XTRIM key MAXLEN [~] count                                   | 주어진 길이 이상의 스트림 엔트리를 삭제합니다.               |
| [XGROUP](http://redisgate.kr/redis/command/xgroup.php)       | XGROUP [CREATE key group id-or-$] [DESTROY key group] [DELCONSUMER key group consumer] [SETID key group id-or-$] | 스트림 그룹을 관리합니다.                                    |
| [XREADGROUP](http://redisgate.kr/redis/command/xreadgroup.php) | XREADGROUP GROUP group consumer [COUNT count] [BLOCK milliseconds] STREAMS key [key ...] ID [ID ...] | 그룹 내의 소비자가 가져갈 엔트리를 가져옵니다.               |
| [XACK](http://redisgate.kr/redis/command/xack.php)           | XACK key group ID [ID ...]                                   | 주어진 스트림 ID에 대한 처리가 완료되었음을 그룹에 알립니다. |
| [XPENDING](http://redisgate.kr/redis/command/xpending.php)   | XPENDING key group [start end count] [consumer]              | 주어진 그룹 내에서 처리되지 않은 엔트리를 반환하거나 정보를 제공합니다. |
| [XCLAIM](http://redisgate.kr/redis/command/xclaim.php)       | XCLAIM key group consumer min-idle-time ID [ID ...] [IDLE ms] [TIME ms-unix-time] [RETRYCOUNT count] [FORCE] [JUSTID] | 소비자가 처리하지 않은 엔트리를 소비하거나 다른 소비자에게 재할당합니다. |
| [XAUTOCLAIM](http://redisgate.kr/redis/command/xautoclaim.php) | XAUTOCLAIM key group consumer min-idle-time start [COUNT count] [JUSTID] | 소비자가 idle 상태로 있는 동안 자동으로 pending 상태의 메시지를 소비하는 데 사용됩니다. `key`와 `group` 매개변수를 사용하여 대기열을 선택하고, `consumer`를 사용하여 작업자를 식별하며, `min-idle-time`은 대기열에서 대기하는 메시지가 있는지 확인할 때 대기할 최소 시간입니다. `start` 매개변수는 대기열에서 읽을 가장 오래된 ID를 지정하고, `COUNT` 매개변수는 처리할 메시지 수를 제한합니다. `JUSTID` 옵션은 소비자가 아직 처리하지 않은 메시지 ID 목록만 반환합니다. |
| [XINFO](http://redisgate.kr/redis/command/xinfo.php)         | XINFO [CONSUMERS key group] [GROUPS key] [STREAM key] [HELP] |                                                              |



# Redis Transaction

Redis Transaction은 Redis에서 제공하는 멀티 명령어 실행 기능으로, 여러 개의 Redis 명령어를 하나의 논리적인 단위로 묶어서 실행할 수 있도록 해줍니다. 이를 통해 데이터의 일관성을 보장하고, 다수의 클라이언트에서 발생할 수 있는 동시성 문제를 해결할 수 있습니다.

**Multi / Exec**

- 한번에 실행되는 것을 보장해주는 명령
- Multi : 명령을 모아서 대기
- Exec : Multi로 모인 명령을 다른 명령의 수행없이 순서대로 실행
- Redis는 싱글스레드여서 다른 명령어를 수행하지 않음
- Multi에 너무 많은 명령은 Redis전체 성능을 저하시킬 수 있음

Redis Transaction은 크게 MULTI, EXEC, DISCARD, WATCH 네 가지 명령어로 구성되어 있습니다.

- MULTI: Redis Transaction을 시작하는 명령어입니다. 이후 실행되는 모든 Redis 명령어는 하나의 Transaction으로 묶입니다.
- EXEC: Redis Transaction을 실행하는 명령어입니다. Transaction 내에 실행된 모든 Redis 명령어를 순서대로 실행합니다.
- DISCARD: Redis Transaction을 취소하는 명령어입니다. 이후 실행되는 모든 Redis 명령어는 취소됩니다.
- WATCH: Redis Transaction을 실행하기 전에, 지정한 키의 값을 모니터링합니다. 다른 클라이언트에서 해당 키의 값을 변경하면, Transaction 실행이 실패하게 됩니다.

Redis Transaction은 ACID (원자성, 일관성, 고립성, 지속성)의 원칙을 따릅니다. 따라서, Redis Transaction 내에서 실행되는 Redis 명령어는 전부 성공하거나 실패합니다. 실패하는 경우, 이전에 실행된 Redis 명령어도 모두 취소됩니다.





### 참조

* https://redis.io/docs/data-types/

* https://velog.io/@sileeee/Redis-vs-Memcached
* https://inpa.tistory.com/entry/REDIS-%F0%9F%93%9A-%EB%8D%B0%EC%9D%B4%ED%84%B0-%ED%83%80%EC%9E%85Collection-%EC%A2%85%EB%A5%98-%EC%A0%95%EB%A6%AC#thankYou
* https://velog.io/@6v6/Redis-%EA%B8%B0%EB%B3%B8-%EC%9E%90%EB%A3%8C%EA%B5%AC%EC%A1%B0
