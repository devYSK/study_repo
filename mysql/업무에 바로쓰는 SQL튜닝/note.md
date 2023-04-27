

# 업무에 바로쓰는 SQL 튜닝

1장. MySQL과 MariaDB 개요

1.1 현황
1.2 상용 RDBMS와의 차이점
1.3 MySQL과 MariaDB 튜닝의 중요성
1.4 마치며



2장. SQL 튜닝 용어를 직관적으로 이해하기
2.1 물리 엔진과 오브젝트 용어
2.2 논리적인 SQL 개념 용어
2.3 개념적인 튜닝 용어
2.4 마치며



3장. SQL 튜닝의 실행 계획 파헤치기
3.1 실습 환경 구성하기
3.2 실행 계획 수행
3.3 프로파일링
3.4 마치며



4장. 악성 SQL 튜닝으로 초보자 탈출하기
4.1 SQL 튜닝 준비하기
4.2 SQL 문 단순 수정으로 착한 쿼리 만들기
4.3 테이블 조인 설정 변경으로 착한 쿼리 만들기
4.4 마치며



5장. 악성 SQL 튜닝으로 전문가 되기
5.1 SQL 문 재작성으로 착한 쿼리 만들
5.2 인덱스 조정으로 착한 쿼리 만들기
5.3 적절한 테이블 및 열 속성 설정으로 착한 쿼리 만들기
5.4 마치며



# 1장. MySQL과 MariaDB 개요

MySQL 버전 확인 쿼리

```sql
#
show variables like 'version';
# 또는
select @@version;
```

버전별 MySQL vs MariaDB 기능 차이

- MySQL: https://dev.mysq1.com/doc/refman/8.0/en/select.htmI
- ﻿﻿MariaDB: https://mariadb.com/kb/en/selecting-data/

## 1.1 현황

MySQL은 상용, 무료 버전으로 나뉜다. 무료버전의 라이센스는 GPL이다.

상용 버전은 오라클에서 다양한 보안 패치와 개선된 기능을 제공하는 밤년, 무료 버전은 제약된 기능과 서비스만 사용할 수 있다.

반면 MariaDB는 GPL v2를 따르는  완전한 오픈소스 소프트웨어이다

## 1.2 상용 RDBMS와의 차이점

### 1.2.1 구조적 차이

오라클과 MySQL을 실제 서비스에 도입할 때는 장애 예방 효과 또는 장애 발생 시 가용성을 기대하며 이중화 구조로 구축한다.

* 삼중화 이상의 다중화로도 가능하다.

오라클 DB는 통합된 스토리지 하나를 공유하는 방식

MySQL은 물리적인 DB서버마다 독립적으로 스토리지를 할당하여 구성한다.

| <img src="images//image-20230426232936675.png" width = 400 height = 500> | <img src="images//image-20230426233025361.png" width = 400 height =500> |
| ------------------------------------------------------------ | ------------------------------------------------------------ |



오라클은 공유 스토리지를 사용하므로 사용자가 어느 DB에 접속하여 SQL을 수행해도 같은 결과를 출력하거나 동일한 구문을 처리한다.

반면 MYSQL은 독립적인 스토리지 할당에 기반을 두는 만큼 이중와를 위한 클러스터(cluster)나 복제(replication) 구성으로 운영하더라도 **보통은 마스터 - 슬레이브 구조가 대부분이다** 

* 보통 마스터 노드는 쓰기/읽기 모두 처리 가능, 슬레이브 노드는 읽기만 처리 가능

즉, 물리적으로 여러 MySQL에 접속하더라도 동일한 구문이 처리되지 않을 수 있다.

쿼리문이 수행하는 서버의 위치를 파악하고 튜닝을 진행하면 물리적인 위치 특성이 내포된 쿼리 튜닝을 수행할 수 있다.

애플리케이션을 통해 쿼리 오프로딩(query offloading)이 적용되어 마스터랑 슬레이브에 각각 쓰기/ 읽기 작업만 수행한다

> 쿼리 오프로딩
>
> DB서버의 트랜잭션에서 쓰기(write) 작업과 읽기(read) 트랜잭션을 분리하여 DB 처리량을 증가시키는 성능 향상 기법

### 1.2.2 지원 기능 차이

MySQL vs Oracel 에선 조인 알고리즘 기능의 차이가 있다

* MySQL은 **대부분** 중첩 루프 조인(nested loop join) 방식으로 수행
* 오라클은 정렬 병합, 해시 조인 방식도 제공

또한 MySQL은 오라클 대비 메모리 사용률이 상대적으로 낮다.

### 1.2.3 oracle vs MySQL SQL 구문 차이

#### Null 대체

* Null이 포함될 때는 다른값으로 대체

```sql
# MySQL
IFNULL(컬럼명, '대체값')
```

오라클

```sql
NVL(컬럼명, '대체값')
```

#### 페이징 처리

MySQL은 LIMIT 오라클은 ROWNUM

**MySQL**

```sql
LIMIT 5;
```

**오라클**

```
ROWNUM <= 5
```

#### 현재 날짜

MySQL에서는 now() 함수를 사용하고 오라클에서는 SYSDATE키워드를 사용

**MySQL**

```sql
now()
```

**오라클**

```
SYSDATE
```

#### 조건문

MySQL은 IF - CASE WHEN~ THEN

오라클은 DECODE, IF CASE WHEN ~ THEN

**MySQL**

```sql
IF (조건식, '참값', '거짓값')
#
SELECT IF(col1 = 'A', 'apple', '-')
```

**오라클**

```sql
DECODE (컬럼명, '값', '참값', '거짓값')
#
SELECT DECODE(col1, 'A', 'apple', '-')
```

#### 날짜 형식

MySQL에서는 DATE_FORMAT()

오라클에서는 TO_CHAR ()

**MySQL**

```sql
DATE_FORMAT(날짜열, '형식')
#
SELECT DATE_FORMAT(now(), '%Y%m%d %H%i%s')
```

**오라클**

```sql
TO_CHAR(날짜열, '형식')
#
SELECT TO_CHAR(SYSDATE, 'YYYYMMDD HH24MISS')
```



#### 자동 증가값 - auto_increment, 시퀀스

MySQL에서는 auto_increment와 시퀀스를 이용할 수 있다. 

```sql
CREATE TABLE tab 
(seg INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
 title VARCHAR (20) NOT NULL
);
```

MardaDB 10.3 이상

```
CREATE SEQUENCE [시퀀스명]
INCREMENT BY [증감숫자]
START WITH [시작숫자]
NOMINVALUE OR MINVALUE [최솟값]
NOMAXVALUE OR MAXVALUE [최댓값]
CYCLE OR NOCYCLE
CACHE OR NOCACHE
```

사용법

```sql
CREATE SEQUENCE MARIA_SEQ_SAMPLE
INCREMENT BY 1
START WITH 1
MINVALUE 1
MAXVALUE 99999999999
CYCLE CACHE;
```

다음 값 채번

```sql
SELECT NEXTVAL(시퀀스 명);
```

```sql
SELECT NEXTVAL (MARIA_SEQ_SAMPLE);
```

  


마리아디비, 오라클에서는 sequence 오브젝트를 사용한다. 

* CREATE SEQUENCE 문으로 시퀀스 오브젝트를 생성한 뒤 해당 시퀀스 문으로 함수를 호출한다
* SELECT 시퀀스명.nextval FROM dual; 구문을 사용

생성 문법

```
CREATE SEQUENCE [시퀀스명]
INCREMENT BY [증감숫자]
START WITH [시작숫자]
NOMINVALUE OR MINVALUE [최솟값]
NOMAXVALUE OR MAXVALUE [최댓값]
CYCLE OR NOCYCLE
CACHE OR NOCACHE
```

예제

```sql
CREATE SEQUENCE ORACLE SEQ SAMPLE
INCREMENT BY 1
START WITH 1
MINVALUE 1
MAXVALUE 99999999999
CYCLE CACHE;
```

채번 문법

```sql
SELECT ORACLE SEO SAMPLE.NEXTVAL
FROM DUAL;
```

#### 문자 결합

MySQL에서는 CONCAT() 함수 사용

오라클에서는 || 나 CONCAT() 함수 사용



**MySQL**

```
CONCAT(열값 또는 문자열, 열값 또는 문자열)
```

예제

```sql
SELECT CONCAT ('A', 'B') TEXT;
```

**오라클**

```sql
SELECT 'A'!!'B' TEXT 2 FROM DUAL;
# 또는
SELECT CONCAT ('A', 'B') TEXT;
```



#### 문자 추출

MySQL 은 SUBSTRING()

오라클은 SUBSTR() 함수 사용



**MySQL**

```
SUBSTRING(열값 또는 문자열, 시작 위치, 추출하려는 문자 개수)
```

예제

```sql
# 두번째부터 시작해서 3개의 문자를 가져옴 
SELECT SUBSTRING('ABCDE',2,3) AS sub_string;
```



**오라클**

```
SUBSTR(열값 또는 문자열, 시작 위치, 추출하려는 문자 개수)
```

예제

```sql
SELECT SUBSTR('ABCDE', 2,3) AS sub_string
2 FROM DUAL;
```



### 1.3 MySQL과 MariaDB 튜닝의 중요성

안정적인 서비스 운영을 위해서 여러 아키텍처를 사용한다

한대의 기본 노드, 다수의 복제본 노드, 여러개의 마스터 노드 구조 등

![image-20230427000306300](/Users/ysk/study/study_repo/mysql/업무에 바로쓰는 SQL튜닝/images//image-20230427000306300.png)

MySQL은 무료, 경량화, 편리하지만 수행 가능한 알고리즘이 적어서 성능적으로 분리.



# 2장 SQL 튜닝 용어를 직관적으로 이해하기

![image-20230427000944255](/Users/ysk/study/study_repo/mysql/업무에 바로쓰는 SQL튜닝/images//image-20230427000944255.png)

MySQL 엔진에서  SQL이 실행되면 

1. 문법 에러를 먼저 검사하고, 
2. 대상 테이블이 존재하는지 확인한다 (파서 )
3. 이후 요청한 데이터를 빠르고 효율적으로 찾아가는 전략적 계획을 수립한다 (옵티마이저가)
4. 이 계획을 토대로 스토리지 엔진에 위치한 데이터까지 찾아간 뒤 해당 데이터를 MySQL 엔진으로 전달
5. MySQL 엔진은 전달된 데이터에서 불필요한 부분은 필터링(제거, 변경)하고 필요한 연산을 수행한 뒤 결과를 반환 



### 스토리지 엔진 (Storage Engine)

(InnoDB, MyISAM, Memory 등) 스토리지 엔진은 사용자가 요청한 SQL 문을 토대로

 DB 에 저장된 디스크나 메모리에서 필요한 데이터를 가져오는 역할을 수행한다.

이후 해당 데이터를 MySQL 엔진으로 보낸다. 

* 스토리지 엔진이 데이터를 저장하는 방식에 따라 각각의 스토리지 엔진을 선택해서 사용할 수 있다
* 필요하다면 외부에서 스토리지 엔진 설치 파일을 가져와 활성화하여 즉시 사용할 수 있다.

온라인상의 트랜잭션인 OLTP를 처리하면 InnoDB 엔진,

대량의 쓰기 트랜잭션이 발생하면 MyISAM 엔진

메모리 데이터를 빠르게 읽을때는 Memory 엔진을 사용한다.



**MySQL에서 엔진, 트랜잭션, 등을 조회하는법**

```sql
SELECT ENGINE, TRANSACTIONS, COMMENT FROM information_Schema.engines;
```

```
+--------------------+--------------+----------------------------------------------------------------+
| ENGINE             | TRANSACTIONS | COMMENT                                                        |
+--------------------+--------------+----------------------------------------------------------------+
| ndbcluster         | NULL         | Clustered, fault-tolerant tables                               |
| FEDERATED          | NULL         | Federated MySQL storage engine                                 |
| MEMORY             | NO           | Hash based, stored in memory, useful for temporary tables      |
| InnoDB             | YES          | Supports transactions, row-level locking, and foreign keys     |
| PERFORMANCE_SCHEMA | NO           | Performance Schema                                             |
| MyISAM             | NO           | MyISAM storage engine                                          |
| ndbinfo            | NULL         | MySQL Cluster system information storage engine                |
| MRG_MYISAM         | NO           | Collection of identical MyISAM tables                          |
| BLACKHOLE          | NO           | /dev/null storage engine (anything you write to it disappears) |
| CSV                | NO           | CSV storage engine                                             |
| ARCHIVE            | NO           | Archive storage engine                                         |
+--------------------+--------------+----------------------------------------------------------------+
```

### MySQL 엔진

MySOL 엔진의 역할

* 사용자가 요청한 SQL 문을 넘겨받은 뒤 SOL 문법 검사와 적절한 오브젝트 활용 검사를 하고, SOL. 문을 최소 단위로 분리하여 원하는 데이터를 빠르게 찾는 경로를 모 색하는 역할을 수행
* 이후 스토리지 엔진으로부터 전달받은 데이터 대상으로 불필요한 데이터는 제거하거나 가공 및 연산한다.
* 즉, SOL 문의 시작 및 마무리 단계에 MySOL 엔진이 관여하며, 스토리지 엔진으로부터 필요한 데이터만을 가져오는 핵심 역할을 담당한다



## 2.1.2 SQL 프로세스 용어

![image-20230427001837316](/Users/ysk/study/study_repo/mysql/업무에 바로쓰는 SQL튜닝/images//image-20230427001837316.png)

**파서(parser)**

* 사용자가 요청한 SQL문을 쪼개 최소 단위로 분리하고 트리를 만들면서 문법을 검사한다
  * 트리를 만드는 과정에서 문법 오류 검토. 
  * 트리의 최소 단위는 >, <, = 등의 기호나 SQL 키워드로 분리

**전처리기 (preprocessor)**

* 파서에서 생성한 트리를 토대로 SQL 문에 구조적인 문제가 없는지 파악 
* 테이블, 열, 함수, 뷰와 같은 오브젝트가 실질적으로 생성된 오브젝트인지, 접근 권한은 부여되어있는지 확인하는 역할 
  * 유효하지 않거나 권한이 없는 오브젝트를 호출하면 바로 에러를 발생하여 사용자에게 표시 

**옵티마이저(Optimizer)**

* 실행 계획을 수립하는 핵심 엔진 
* 전달된 파서 트리를 토대로 필요하지 않은 조건은 제거하거나 연산 과정 단순화
* 어떤 순서로 테이블에 접근할지, 인덱스를 사용할지, 사용한다면 어떤 인덱스를 사용할지, 정렬할 때 인덱스를 사용할 지 아니면 임시 테이블을 사용할지 실행 계획 수립  
  * 실행 계획으로 도출할 수 있는 경우의 수가 너무 많을 때는 최적의 실행 계획을 선택하기까지 시간이 오래 걸리는 만큼 모든 실행 계획을 판단하지는 않는다.
  * <u>즉 이말은 옵티마이저가 선택한 최적의 실행 계획이 최상의 실행 계획이 아닐 수도 있다는 뜻</u>

**엔진 실행기 (engine executor)**

* 옵티마이저에서 수립한 실행 계획을 참고하여 스토리지 엔진에서 데이터를 가져온다
* 이후 MySQL 엔진에서는 읽어온 데이터를 정렬하거나 조인하고 불필요한 데이터는 필터링 한다.
* 즉 MySQL 엔진의 부하를 줄이려면, 스토리지 엔진에서 가져오는 데이터 양을 줄이는게 중요하다 .

### 2.1.3 DB 오브젝트 용어



**인덱스(index)**

데이터 접근 속도를 높이고자 생성되는 키 기준으로 정렬된 오브젝트

기본키와 유니크(고유) 인덱스의 차이점 : 기본 키는 null 불가능, 유니크 인덱스는 가능



**뷰(view)**

가상 테이블. 제한된 정보만을 제공함으로써 보안적 측면으로 좋다. 

* 뷰를 사용하는 이유는 일부 데이터에 대해서만 데이터를 공개하고, 노출에 민감한 데이터에 대해서는 제약을 설정할 수 있는 보안성 때문. 
* 한편 여러 개의 테이블을 병합 join해서 활용할 때는 성능을 고려한 최적 화된 뷰를 생성함으로써 일관된 성능을 제공할 수 있다.



### 2.2.1 서브쿼리와 서브쿼리 위치에 따른 용어

스칼라 서브쿼리 :  SELECT 절에 사용되는 서브쿼리 

* 결괏값은 1행 1열의 구조로만 출력되어야 한다. 
* 스칼라 서브쿼리는 출력되는 건수가 1건이야 하므로 집계함수(max, min, avg, sum, count)가 자주 쓰임 

```sql
SELECT 이름,
	(SELECT COUNT (*)
		FROM 학생 AS 학생2
		WHERE 학생2. 이름 = 학생1. 이름 ) 카운트

FROM 학생 AS 학생1;
```



**인라인 뷰**   : FROM 절에 사용되는 서브쿼리 

* 내부에서 일시적으로 뷰를 생성하는 방식이여서 인라인 뷰
* 내부적으로 메모리 또는 디스크에 임시 테이블을 생성하여 활용

```sql
SELECT 학생2. 학번, 학생2. 이름
	FROM (SELECT *
				FROM 학생
WHERE 성별 = '남') 학생2;
```



 **중첩 서브쿼리** : WHERE 절에 사용되는 서브쿼리 

* 단순한 값 비교 대신 복잡한 값을 비교 연산하기 위해 사용
* 보통 비교 연산자 (=, <>,   >=, <>, 1=)를 비롯해 IN, EXISTS. NOT IN, NOT EXISTS 문을 많이 사용

```sql
SELECT *
	FROM 학생
WHERE 학번 = (SELECT MAX(학번) FROM 학생)
```

