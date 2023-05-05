# 매트, 토르의 MySQL 성능 최적화

10분 테코톡 - 매트, 토르의 MySQL 성능 최적화 영상을 보고 정리한 내용입니다.

---

목차

1. MySQL 8.0과 기본 스토리지 엔진인 InnoDB
2. 인덱스를 활용한 쿼리 최적화

<img src="https://blog.kakaocdn.net/dn/MrzKE/btsd0TtnN1a/KqOLUV7GVgkIiAK6NKQ5qK/img.png" width = 400 height =250>

* 사용할 테이블



## 인덱스를 왜 쓸까?

데이터베이스에서 성능 최적화는 디스크 I/O 와 관련이 많다.

**이 디스크 I/O를 줄이는 것이 조회 성능 개선의 핵심**

* 물리적으로 하드디스크는, 헤더가 물리적으로 움직여야 하므로 데이터의 입출력이 느리다.

* 실제로 하드디스크 I/O와 메모리 I/O의 속도 차이는 10만 ~ 15만 배 정도이다.

인덱스를 쓰면 **조회에 이득**을 얻고, **수정/삭제에서 손해**를 보는데 괜찮나요?

* 그럼에도 불구하고 사용하는 것이 좋다.
* 일반적으로 웹서비스 같은 경우에 CRUD에서 R과 CUD의 비율이 8:2에서 9:1 이라고 한다.

ORDER BY와 GROUP BY에서도 이득을 얻을 수 있다.

```sql
SELECT *
FROM crew
WHERE nickname >= "매트" AND nickname <= "토르"
ORDER BY nickname;
```

* 닉네임에 의해 정렬을 하고 있다.
* 인덱스가 없으면 데이터를 다 읽어와서 DB에서 정렬을 해야한다.
* 하지만 인덱스는 이미 정렬되어 있기 때문에 인덱스 순서대로 파일을 읽기만 하면 된다.

GROUP BY: 인덱스를 이용해 GROUP BY를 하는 경우

<img src="https://blog.kakaocdn.net/dn/bWZ4g6/btsd0Rvw5dY/4S8Dbkbkz4TqWnkK0uK5j1/img.png" width = 800 height = 400>

* 각 Track에서 nickname이 가장 빠른 사람들을 가져오는 쿼리를 날린다고 가정 

* 인덱스가 있는 경우는 프론트엔드 집단에서 순서상 가장 빠른 꼬재 만 읽고 나머지는 읽지 않고 백엔드 트랙으로 넘어가서 매트만 읽으면 된다. 

 

## 실행 계획 (explain)

실행계획 여러가지 중 가장 중요한 all, range, index 만 다룬다.

### ALL - TABLE FULL SCAN

<img src="https://blog.kakaocdn.net/dn/cNYOZ2/btsdY1TLQqK/LvzkqPI0MH8PrKKfIUyUf1/img.png" width = 800 height = 400>

전체 데이터를 하나하나 스캔하기 때문에 디스크 I/O 측면에서 본다면 성능상 좋지 않다는 것을 알 수 있다.

- all: 테이블 전체를 스캔할 때
  - 성능이 좋지 않다.

TABLE FULL SCAN이 일어나는 경우

- 인덱스가 없을 경우
- 데이터 전체의 개수가 적을 경우
- 읽고자하는 데이터가 인덱스가 있더라도, 전체 데이터가 많지 않거나 전체 데이터의 25% 이상일 경우

### Range Scan

<img src="https://blog.kakaocdn.net/dn/de6QDS/btsd4FHUTHB/1Ihwxe7Ttrd5CQjLBefBTk/img.png" width = 800 height =400>

인덱스를 이용하여 범위 검색을 할 때의 실행 계획이다. 이상적으로 인덱스를 잘 설정해두었을 때 발생하는 계획이다. 

필요한 부분만을 읽기 때문에 디스크 I/O를 줄일 수 있다.



### INDEX

<img src="https://blog.kakaocdn.net/dn/HGUXJ/btsdYZVYjQ5/vBYBETPOaYwWJTiYKAMjH0/img.png" width = 800 height = 400>

인덱스 전체를 스캔할 때의 실행 계획이다. 다른 말로는 **Index Full Scan**이라고 한다. 

인덱스는 데이터 파일보다 작기 때문에 Full Table Scan 보다는 성능이 좋지만 Index Range 스캔에 비해서 성능이 떨어진다.



# 인덱스 적용

## 인덱스 적용 사례 1

<img src="https://blog.kakaocdn.net/dn/ctOTDI/btsdYQY5iDk/uI2lOHFv5klOHHS9pArup1/img.png" width = 550 height = 300>

* 여기서는 track 컬럼의 카디널리티가 낮기 때문에 nickname에 인덱스를 적용했다.

어떤 컬럼에 인덱스를 걸어야 할까?

1. 서비스의 특성상 무엇에 대한 조회가 많이 일어나는지 파악
2. 카디널리티가 높은 칼럼에 대해 인덱스를 생성



## 인덱스 적용 사례 2 - 복합 인덱스

복합 인덱스란?

- 두개 이상의 컬럼을 합쳐서 인덱스를 만드는것
- 하나의 컬럼으로 인덱스를 만들었을 때 보다 더 적은 데이터 분포를 보여 탐색할 데이터 수가 줄어든다.
- 결합 인덱스, 다중 컬럼 인덱스, Composite Index라고도 불린다.

<img src="https://blog.kakaocdn.net/dn/PEb9t/btsdZOMUQxC/UfkZtKizMe9sykWqBMKwF0/img.png" width = 900 height = 400>

* 위 테이블은 나이순과 nickname순으로 정렬되어있는 상태

* 먼저 나이만을 조건으로 걸어둔 쿼리를 날렸을 경우에는 나이순으로 정렬되어있기 때문에 동키콩부터 데이터를 가지고 올 수 있어 탐색 범위가 줄어든다.

* 그 다음에는 나이와 nickname을 조건으로 걸어둔 쿼리를 날렸을 경우에는 나이순 그리고 nickname 순으로 정렬되어있어 토르부터 데이터를 가지고 오기 때문에 탐색 범위가 줄어드는 것을 알 수 있다.



닉네임을 기준으로 탐색할 경우에는?

* 닉네임에는 단독으로 인덱스가 없다.
* 동키콩보다 이후인 데이터를 가져오고자 했지만 티거, 파랑, 조시, 리버 등의 데이터를 가져오기 때문에 Full Table Scan이 발생한다. 
* 즉, 디스크 I/O를 줄일 수 없게 된다.
* 이런것들을 잘 고려해서 복합 인덱스를 사용하면 좋다. 

## 인덱스 적용 사례 3 - 커버링 인덱스

커버링 인덱스란? : 인덱스로 설정한 컬럼만을 읽어 쿼리를 모두 처리할 수 있는 인덱스. 

불필요한 디스크 I/O를 줄여 조회시간을 단축한다.



인덱스를 사용하여 처리하는 쿼리중 가장 큰 부하를 차지하는 부분은?

* 인덱스 검색에서 일치하는 키 값의 레코드를 읽는 것

<img src="https://blog.kakaocdn.net/dn/bl1p18/btsdZ3XmuaE/3e1UHXblzmzcfZIIkcaWDk/img.png" width = 900 height= 450>

* 인덱스 검색에서 일치하는 키 값의 데이터를 읽기 위해서 추가적인 디스크 I/O가 발생 

* N개의 인덱스를 검색할 때 최악의 경우에는 N번의 디스크 I/O가 발생할 수 있다.



쿼리 최적화의 가장 큰 목적은 **디스크 I/O를 줄이는것**

```sql
SELECT *
FROM crew
WHERE nickname BETWEEN 'a' AND 'd' AND track = 'BACKEND'
```

* 위 쿼리는 옵티마이저에 의해 TABLE FULL 스캔을 하게 된다.
  * `*(all, 애스터리스크)`으로 인해 전체 데이터의 20~25%이상을 조회하게 되는 경우이기 때문이다.

아래와 같이 튜닝할 수 있다.

```sql
SELECT nickname, track
FROM crew
WHERE nickname BETWEEN 'a' AND 'd' AND track = 'BACKEND'
```

* `*` 대신 `nickname`, `track`만 조회하도록 했다. 
* 이것을 활용하면, 데이터 파일을 읽지 않고 인덱스만 읽게 되므로 불필요한 디스크 I/O 시간을 단축할 수 있다. 

실행계획을 보게되면, 커버링 인덱스를 사용하기 때문에 type : range (Index Range Scan)과 Extra : Using Index 가 된다.

<img src="https://blog.kakaocdn.net/dn/2sYdy/btsd0n9lGsB/RL3KZU8PfBEdzK3Nvmn9QK/img.png" width = 900 height=350>

**커버링 인덱스의 비밀!!!!**

```sql
SELECT id, nickname, track
FROM crew
WHERE nickname BETWEEN 'a' AND 'd' AND track = 'BACKEND'
```

* 위 쿼리처럼, 추가적으로 복합 인덱스로 설정하지 않은 PK 컬럼인 id도 같이 조회하면 같은 결과(실행계획, 결과)가 나온다. 

그 이유는 InnoDB가 가진 세컨더리 인덱스의 특수한 구조 때문이다. 

즉, 리프 노드에는 실제 레코드의 주소가 아닌 클러스터링 인덱스가 걸린 PK를 주소로 가지기 때문에 id, nickname, track 모두 활용이 가능한 것이다.

<img src="https://blog.kakaocdn.net/dn/bIkksz/btsd3fbEB8X/PUsfQszTdDaFCLzdKIbfGK/img.png" width= 650 height = 350>

## 인덱스 적용사례 4 - 인덱스 컨디션 푸시다운



인덱스 컨디션 푸시다운(ICP, Index Condition Pushdown)이란, MySQL이 인덱스를 사용하여 테이블에서 행을 검색하는 경우의 최적화를 의미한다.

 ICP를 활성화하고 인덱스의 칼럼만 사용하여 WHERE 조건의 일부를 평가할 수 있는 경우 MySQL 엔진은 WHERE 조건 부분을 스토리지 엔진으로 푸시



<img src="https://blog.kakaocdn.net/dn/beBwAR/btsd6tHdnAd/bKhkdMpQsuSEgSOBAV3Ik0/img.png" width = 950 height = 260>

```sql
ALTER TABLE study_log ADD INDEX id_study_log_type(type);
```

* 학습 로그의 목적을 의미하는 type 컬럼을 빠르게 조회하기 위해 인덱스를 생성



예시 1.

```sql
SELECT *
FROM study_log
WHERE type = 'QUESTION'
AND created_at BETWEEN '2022-10-07 00:00' AND '2022-10-13 00:00';
```

* QUESTION 목적을 가진 2022-10-07~ 2022-10-13 사이 학습 로그 조회

실행계획

<img src="https://blog.kakaocdn.net/dn/bIj7YF/btsd4eXUX4v/w7Jb8K6ot3Fu9Rq9paymJk/img.png" width = 950 height = 260>

* type index를 사용하는것을 확인 가능
* 하지만 Extra 컬럼에 Using where라는 정보에 집중해야한다. 
* Extra 컬럼은 쿼리의 실행 계획에서 성능에 관련된 중요한 내용이 표시되고 내부적인 처리 알고리즘에 대해 조금 더 깊은 내용을 보여준다.
*  **Using where는 InnoDB 스토리지 엔진을 통해 테이블에서 행을 가져온 뒤 MySQL 엔진에서 추가적인 체크 조건을 활용하여 행의 범위를 축소한 것을 의미한다.** 
* 즉, InnoDB 스토리지 엔진은 불필요한 데이터를 디스크에서 읽어오게 된다.

<img src="https://blog.kakaocdn.net/dn/cunGnl/btsdZOGaZ6O/kwsH3i8pLwgppkYfCoiaH0/img.png" width = 700 height = 400>

이 문제를 복합 인덱스를 통해서 개선할 수 있다.

* 복합 인덱스를 적용한 상태에서 실행 계획을 보면 Extra 컬럼에 Using index condition이 표시되는 것을 확인할 수 있다.

type과 created_at 을 기반으로 복합 인덱스를 생성하면 된다.

<img src="https://blog.kakaocdn.net/dn/xD4N9/btsd02cCvZb/z9LT9d5QYA8FUGui9BNWG1/img.png" width = 950 height = 300>

* 다시 한번 실행계획을 살펴보면 index range scan이 발동한것을 볼 수 있다.
* Extra 칼럼의 Using Index Condition은 인덱스 컨디션 푸시다운으로 인해 표시

이 기능을 이용하면 인덱스의 컬럼만을 사용하여 WHERE 조건의 일부를 평가할 수 있는 경우 MySQL 엔진은 WHERE 조건 부분을 스토리지 엔진으로 푸시한다. (최신 버전의 InnoDB에서는 기본적으로 ICP가 활성화되어 있다.)

그 결과 불필요한 디스크 I/O를 줄여 조회시간을 단축하는 것을 알 수 있다.



## 추가적으로 학습을 하면 좋을 부분

인덱스 스킵 스캔

루스 인덱스 스캔

유니크 인덱스

전문 검색 인덱스(fulltext index)

옵티마이저



[매트, 토르의 MySQL 성능 최적화](https://www.youtube.com/watch?v=nvnl9YgnON8)