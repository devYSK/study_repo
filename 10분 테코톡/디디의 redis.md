# 10분 테코톡 - 디디의 Redis

목차

- Redis 개요
- Cache 개념
- Redis 자료구조
- Redis 주의사항



## **Redis 개요**

**Remote dictionary server**

[redis.io](https://redis.io/) 에는 다음과 같이 redis를 소개한다.

 Redis는 **인메모리** 데이터 구조 저장소로, 데이터베이스, **캐시**, 메시지 브로커로 사용한다고 말한다.

* Remote
* dictionary는 Key-Value 형식의 자료구조 DB
* server 

>  32bit CPU Int 최대값은? 21억~ 
>
> Key값이 21억을 넘어가서 쿠팡에서 오류가 발생했다 하여 int에서 long으로 데이터형식을 변환하여 문제를 해결했다.



## Cache 개념

나중 요청에 대한 결과를 미리 저장했다가 빠르게 사용하는것.

* 자주 사용하는 데이터나 값을 미리 복사해 놓는 임시 장소

![image-20230321131654971](/Users/ysk/study/study_repo/10분 테코톡/images//image-20230321131654971.png)

* 그림 : 메모리 계층 구조

위로갈수록 빠르고 비싸고 아래로 갈수록 느리고 저렴하다.

1. CPU Register : 매우 빠르고 비싸다
2. Cache
3. Main DRAM : 적당히 빠르고 적당히 비싸며 컴퓨터 껏다 키면 데이터가 휘발성됩니다
4. 하드디스크(SSD, HDD) : 비교적느리고 저렴하지만 저장공간이 크며 비휘발성입니다.



기본적으로 데이터는 컴퓨터가 꺼지더라도 저장되어야 하기 때문에 Database (HDD, SSD)에 저장한다.

기술의 발전과 하드웨어의 발달로 인해 메인 메모리에 저장해 빠르고 쉽게 데이터에 접근이 가능해졌고

`SSD 등에 저장하는 Database 보다 더 빠른 Memory에 더 자주 접근하고 덜 자주 바뀌는 데이터를 저장하기 위해`

Redis를 주로 사용한다. 



Redis는 기본적으로 **Single Thread**이며 

자료 구조는 **Atomic Critical Section**에 대한 동기화를 제공

서로 다른 Transacion Read/Write를 동기화를 함으로써 원치 않는 결과를 막아준다

 

Atomic Critical Section - 쉽게 말해... 여러 개의 프로세스가 동시에 접근하는 것을 방지

## Redis 자료구조

![image-20230321132824247](/Users/ysk/study/study_repo/10분 테코톡/images//image-20230321132824247.png)

>  레디스는 RDBMS의 VARCHAR, INT, DATETIME 등을 지원하지 않는다.
>
> 리스트, 배열 형식의 데이터 처리에 특화됨 ( 리스트 형 데이터의 입력과 삭제가 MySQL보다 10배 정도 빠르다. )

redis 는 아래 그림과 같이 key-value 형태로 데이터를 저장한다. 

여기서 value는 다양한 Type들을 사용할 수 있다. 

String, Bitmap, Hash, List, Set, Sorted Set, Geospatial Index, Hyperloglog, Stream

key 는 문자열이며, 최대 512MB 까지 가능하고 key 를 가독성있게 잘 설계하는 것이 중요



**레디스의 키**

RDBMS의 로우와 비슷하게 동작하지 않는다.

성능에 영향을 거의 주지 않으며 0(1)의 수행 시간을 가지고, 많은키(1,000,000,000,000개)건 단 1개의 키건 동일한 시간이 적용된다.



#### 장점1) Redis를 사용하면 Single Thread이므로 RaceCondition에 빠질 가능성이 낮다.

- Single Thread  : 프로세스에서 하나의 스레드 실행

  

- Race Condition

  - 두개 이상의 Thread가 실행될 때, **접근 순서(Context Switching)**에따라 그 **실행 결과**가 상황에 따라서 **달라지는 상황**

#### 장점2) Redis를 사용하면 atomic을 보장한다.

- atomic (원자성)
- Critical Section(여러개의 프로세스가 접근하면 안되는 영역)
- 서로 다른 Transaction Read/Write를 동기화한다.

#### 단점1) 오래걸리는 O(N)명령어 실행 시 다른 명령에 영향을 줄 수도 있다.

- O(N) 명령어는 사용 자제 : KEYS, FLUSHALL, FLUSHDB, DEL 등

### 어디에서 사용해야하나요?

- 여러 서버에서 같은 데이터를 공유할 경우



### Redis 사용 시 주의사항

- Single Thread 서버 이므로 시간복잡도를 고려해야한다.
  - O(N)같은 시간처리가 오래 걸리는 명령어의 사용은 자제한다.

* in-memory 특성상 메모리 파편화, 가상 메모리, 메모리 SWAP 등의 이해가 필요하다.

#### 메모리 SWAP 

메인 메모리(RAM)가 부족할 경우 메모리에 올린 프로세스들이 부족한 공간을 해결하기 위해 하드디스크에 SWAP 공간을 만들어 임시 저장하게 된다. 메모리 부족을 해결하기 위한 좋은 방법이 될 수 있다. 

하지만 SWAP을 하는동안 **레이턴시**가 발생하는 치명적이 단점이 있다.

보통 redis가 느려졌다면 RAM 용량이 부족해서 메모리 SWAP이 발생한 것이며, 한번 발생하면 계속해서 발생할 확률이 높다.

이를 해결하기 위해 프로세스를 재실행해야 한다.

#### 메모리 단편화 혹은 파편화

> RAM에서 메모리의 공간이 작은 조각으로 나뉘어져 사용가능한 메모리가 충분히 존재하지만 할당이 불가능한 상태를 **메모리 단편화**라고 한다.

redis는 jmalloc을 사용하여 메모리를 할당한다. 

예를들어 jmalloc이 메모리 페이지 사이즈를 **4096byte**로 잡으면 **1byte**만 저장하게 되어도 **4096byte**를 할당한다.

 때문에 사용하지 않는 메모리 공간이 발생하게 된다. 때문에 개발자는 정확한 메모리 사용량을 파악하기 어렵다.

메모리 단편화 문제는 다양한 크기의 데이터 사용을 줄이고 **유사한 크기의 데이터**를 사용하면 메모리 파편화를 줄일 수 있다.



### 참조

* https://www.youtube.com/watch?v=Gimv7hroM8A 
