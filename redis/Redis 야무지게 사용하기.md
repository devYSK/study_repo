# NHN FORWARD - redis 야무지게 사용하기



다룰 내용



1. Redis 캐시로 사용하기
2. Redis 데이터 타입 야무지게 활용하기
3. Redis에서 데이터를 영구 저장 하려면? (RDB vs AOF)
4. Redis 아키텍처 선택 노하우 (Replication VS Sentinel Vs Cluster)
5. Redis 운영 꿀팁 + 장애 포인트

# Redis 캐시로 사용하기



캐시 : 데이터의 원래 소스보다 더 빠르고 효율적으로 액세스할 수 있는 임시 데이터 저장소

캐시가 유용하게 사용되려면? 

* 캐시 접근이 원본 접근보다 쉽고 빨라야 한다
* 동일한 데이터에 반복 액세스할 시 캐시가 유용하다 

<img src="https://blog.kakaocdn.net/dn/3O2V3/btsdeIeVnKR/zzvloHntzBBRhkkqCRel40/img.png" width = 600 height = 400>

## Redis s a cache

Most poplar software caching solution

* 단순한 key-value 구조
* in-memory 데이터저장소 (RAM)
* 빠른 성능
  * 평균 작업속도 < 1 ms
  * 초당 수백만 건의 작업 가능



## 레디스 캐싱 전략 (Caching Strategies)

### 1. 읽기 전략

<img src="https://blog.kakaocdn.net/dn/bHwhNy/btsdemwmsQ6/DwYTUEu20X23AEoXELsvh0/img.png" width = 650 height = 350>

* Look-Aside (Lazy Loading) - 가장 일반적

* 애플리케이션에서 데이터를 읽는것이 잦을 때 쓰는 전략
* 애플리케이션은 캐시를 먼저 조회해서 캐시에 데이터가 있으면 반환, 없으면 DB에 접근해서 조회
* 주의할점 : 
  * 캐시에 커넥션이 많이 붙어있었다면, DB 조회시 커넥션이 모두 몰려가 부하가 생길 수 있다.  

* Cache Warming : 캐시에 데이터를 미리 적재하는 작업. 
  * 초기 캐시에 데이터가 없으면 DB에서 데이터를 조회해 캐시에 적재하는데, 이 때 많은 부하가 생길 수 있어 미리 적재



### 2. 쓰기 전략

<img src="https://blog.kakaocdn.net/dn/1uaXB/btsdemwmsOa/P03nSeWh9PPfXrPbJTzzKk/img.png" width = 650 height = 300>

* Write-Around : DB에만 데이터를 저장. 캐시 미스가 발생한 경우 캐시에 데이터를 적재.
  * 캐시의 데이터와 DB의 데이터가 다를 수 있다는 단점
* Write-Through : 캐시와 DB에 함께 저장하는 방법.
  * 캐시의 데이터와 DB의 데이터가 최신 정보를 가지고 있다는 장점이 있지만, 저장할 때마다 2단계 스텝을 거쳐 느릴 수 있다.

# Redis 데이터 타입 야무지게 활용하기

레디스는 많은 자료구조를 제공한다.

<img src="https://blog.kakaocdn.net/dn/kye7U/btsdeHtwMTs/nLEsgukxSwpgLoIOTlgiaK/img.png" width = 800 height = 480>



* Strings 
  * 단순 증감 연산에 좋다
  * INCR / INCRBY / INCRBYFLOAT / HINCRBY / HINCRBYFLOAT / ZINCRBY

* Bits
  * 데이터 저장공간 절약
  * 정수로 된 데이터만 카운팅 가능
  * 만약 금일 조회수를 알고싶다면, 금일 날짜로 된 정수타입을 만들고 천만명의 유저는 천만개의 bit로 표현할 수 있고 이는 1.2MB밖에 차지하지 않는다.
* HyperLogLogs
  * 대용량 데이터를카운팅할 때 적절
  * set과 비슷하지만 저장되는 용량은 매우 작음(12KB 고정)
  * 저장된 데이터는 다시 확인할 수 없음
* Lists - 메시징 큐 대안으로도 가능
  * Blocking 기능을 이용해 Event Queue로 사용 가능하다 
* Streams
  * 로그를 저장하기 가장 적절한 자료구조
  * append-only
  * 시간 범위로 검색 / 신규 추가 데이터 수신 / 소비자별 다른 데이터 수신(소비자 그룹)



# Redis에서 데이터를 영구 저장 하려면? (RDB vs AOF)

Redis는 In-memory 데이터 스토어

* 서버 재시작 시 모든 데이터 유실
* 복제 기능을 사용해도 사람의 실수 발생 시 데이터 복원 불가
* Redis를 캐시 이외의 용도로 사용한다면 적절한 데이터 백업이 필요

<img src="https://blog.kakaocdn.net/dn/dcq7by/btsdhyJQm1i/qtVduGzG9FKCjGZIqxkRQ1/img.png" width = 400 height = 300>

레디스에서 데이터를 영구저장하는 방법 2가지

1. AOF - Append Only File
   * 데이터 변경 커맨드까지 모두 저장한다.
   * 자동 저장시 : redis.conf 파일에서 auto-aof-rewrite-percentage 옵션으로 크기 기준으로 저장 가능
   * 수동 저장시: BGREWRITEAOF 커맨드를 이용해 CLI창에서 수동으로 AOF 파일 재작성이 가능하다 
2. RDB - snapshot
   * 저장 당시 메모리에 있던 데이터들만 그대로 저장 
   * 자동 저장시 : redis.conf 파일에서 SAVE 옵션으로 시간 기준으로 저장할 수 있다.
   * 수동 저장시 : BGSAVE 커맨드를 이용해 cli 창에서 수동으로 RDB파일을 저장할 수 있다.



### RDB VS AOF 선택 기준

- ﻿﻿백업은 필요하지만 어느 정도의 데이터 손실이 발생해도 괜찮은 경우

  - -> ﻿﻿RDB 단독 사용

  - ﻿﻿-> redis.conf 파일에서 SAVE 옵션을 적절히 사용 예) SAVE 900 1

- ﻿﻿장애 상황 직전까지의 모든 데이터가 보장되어야 할 경우

  - ->﻿﻿ AOF 사용(appendonly yes)

  - ﻿﻿-> APPENDFSYNC 옵션이 everysec인 경우 최대 1초 사이의 데이터 유실 가능(기본 설정)

- ﻿﻿제일 강력한 내구성이 필요한 경우
  - ﻿﻿RDB & AOF 동시 사용



# Redis 아키텍처 선택 노하우 (Replication VS Sentinel Vs Cluster)

<img src="https://blog.kakaocdn.net/dn/cc5kVN/btsdhx5dMBP/NcwH36gOcHYTMl3Y2D6L5K/img.png" width = 800 height= 500>

### Replication

단순한 복제 연결

* replicaof 커맨드를 이용해 간단하게 복제 연결
* 비동기식 복제
* HA 기능이 없으므로 장애 상황시 수동 복구
  * replicaof no one
  * 애플리케이션에서 연결 정보 변경

### Sentinel

자동 페일오버 가능한 HA 구성(High Availability)

* sentinel 노드가 다른 노드 감시
* 마스터가 비정상 상태일 때 자동으로 페일오버
* 연결 정보 변경 필요 없음
* sentinel노드는 항상 3대 이상의 홀수로 존재해야 함
  * 과반수 이상의 sentinel이 동의해야 페일 오버 진행

### Cluster

스케일 아웃과 HA 구성

* 키를 여러 노드에 자동으로 분할해서 저장(샤딩)
* 모든 노드가 서로를 감시하며 마스터 비정상 상태일 때 자동 페일오버
* 최소 3대의 마스터 노드가 필요 

<img src="https://blog.kakaocdn.net/dn/bqnzQr/btsdeZVgG7z/0KYkjM6f8xHkitSwGl7DP1/img.png" width = 800 height = 500>

# Redis 운영 꿀팁 + 장애 포인트

## 사용하면 안되는 커맨드

Redis는 single thread로 동작

* keys * -> scan으로 대체
* Hash나 Sorted Set등 자료 구조
  * 키 나누기 (최대 100만개)
  * hgetall -> hscan
  * del -> unlink(key를 백그라운드로 지워준다)

## 변경하면 장애를 막을 수 있는 기본 설정값

STOP-WRITES-ON-BGSAVE-ERROR = NO

- ﻿﻿yes(default)
- ﻿﻿RDB 파일 저장 실패 시 redis로의 모든 write 불가능

MAXMEMORY-POLICY = ALLKEYS-LRU

*  redis를 캐시로 사용할 때 Expire Time 설정 권장

*  메모리가 가득 찼을 때 MAXMEMORY-POLICY 정책에 의해 키 관리 

  * noeviction(default) : 삭제 안함

  - ﻿﻿volatile-tru

  - ﻿allkeys-Iru



## Cache stampede

캐시 데이터 만료 직후부터 데이터가 다시 캐싱될 때까지 수많은 조회 요청이 DB로 몰리는 현상

캐시가 만료되어 DB 조회를 하면서 발생하는 문제를 cache stampede라고 한다.



주로 TTL 값이 너무 작게 설정한 경우 발생한다.

<img src="https://blog.kakaocdn.net/dn/y6YZ6/btsdd0f22vO/KJ6GET0xWuPqOAOzKNhkd1/img.png" width = 800 height = 550>



## MaxMemory 값 설정

<img src="https://blog.kakaocdn.net/dn/IuQGL/btsdefKP1KH/pNCf96snjiumiDO3yVkEf0/img.png" width = 800 height = 500>

Persistence / 복제 사용시 MaxMemory 설정 주의

- ﻿﻿RDB 저장 & AOF rewrite 시 fork()
- ﻿﻿Copy-on-Write로 인해 서버의 메모리 사용량이 두배로 증가하는 문제가 발생 가능

*  Persistence / 복제 사용 시 MaxMemory는 실제 메모리의 절반으로 설정
  * 예 4GB -> 2048MB



## Memory 관리

<img src="https://blog.kakaocdn.net/dn/NX6V3/btsdeH1orzx/SIFVsaEIlZZndvWVCCiHFK/img.png">

물리적으로 사용되고 있는 메모리를 모니터링

- ﻿﻿used_memory: 논리적으로 Redis가 사용하는 메모리
- ﻿﻿used_memory_rss: 0S가 Redis에 할당하기 위해 사용한 물리적 메모리 양
- ﻿﻿**used_memort_rss를 주의깊게 봐야한다.**
- ﻿﻿삭제되는 키가 많으면 fragmentation 증가
  - ﻿﻿fragmentation : 실제 사용하는 메모리는 적은데 RSS가 크게 설정될 경우 -> fragmentation이 크다.
      -> 단편화가 많이 발생시 activedefrag 옵션을 활용하자.
  - ﻿﻿특정 시점에 피크를 찍고 다시 삭제되는 경우
  - ﻿﻿TTL로 인한 eviction이 많이 발생하는 경우
  
- ﻿﻿이때 activefrag 기능을 잠시 켜두면 도움이 된다.
  - ﻿﻿`CONFIG SET activedefrag yes`
  - ﻿﻿공식 문서에서도 fragmentation(단편화)가 많이 발생했을 때 켜두는 것을 권장하고 있다.








### 참조

* [nhn forward - Redis 야무지게 사용하기](https://www.youtube.com/watch?v=92NizoBL4uA)