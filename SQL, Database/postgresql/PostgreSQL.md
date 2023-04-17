# PostgreSQL 이론



## PostgreSQL이란?

PostgreSQL은 오픈 소스 객체-관계형 데이터베이스 시스템(ORDBMS)으로, Enterprise급 DBMS의 기능과 차세대 DBMS에서나 볼 수 있을 법한 기능들을 제공한다.약 20여년의 오랜 역사를 갖는 PostgreSQL은 다른 관계형 데이터베이스 시스템과 달리 연산자, 복합 자료형, 집계 함수, 자료형 변환자, 확장 기능 등 다양한 데이터베이스 객체를 사용자가 임의로 만들 수 있는 기능을 제공함으로써 마치 새로운 하나의 프로그래밍 언어처럼 무한한 기능을 손쉽게 구현할 수 있다.



## PostgreSQL의 구조

PostgreSQL은 클라이언트/서버 모델을 사용한다. 서버는 데이터베이스 파일들을 관리하며, 클라이언트 애플리케이션으로부터 들어오는 연결을 수용하고, 클라이언트를 대신하여 데이터베이스 액션을 수행한다. 서버는 다중 클라이언트 연결을 처리할 수 있는데, 서버는 클라이언트의 연결 요청이 오면 각 커넥션에 대해 새로운 프로세스를 fork한다. 그리고 클라이언트는 기존 서버와의 간섭 없이 새로 생성된 서버 프로세스와 통신하게 된다.

![architecture](.\images\architecture.png)





## PostgreSQL의 기능

PostgreSQL은 관계형 DBMS의 기본적인 기능인 트랜잭션과 ACID(Atomicity, Consistency, Isolation, Durability)를 지원한다. ANSI:2008 구격을 상당 부분 만족시키고 있으며, 전부 지원하는 것을 목표로 계속 기능을 추가하고 있다. PostgreSQL은 기본적인 신뢰도와 안정성을 위한 기능 뿐만 아니라 진보적인 기능이나 학술적 연구를 위한 확장 기능도 많이 가지고 있는데, PostgreSQL의 주요 기능을 열거해보면 아래와 같다.

- Nested transactions (savepoints)
- Point in time recovery
- Online/hot backups, Parallel restore
- Rules system (query rewrite system)
- B-tree, R-tree, hash, GiST method indexes
- Multi-Version Concurrency Control (MVCC)
- Tablespaces
- Procedural Language
- Information Schema
- I18N, L10N
- Database & Column level collation
- Array, XML, UUID type
- Auto-increment (sequences),
- Asynchronous replication
- LIMIT/OFFSET
- Full text search
- SSL, IPv6
- Key/Value storage
- Table inheritance



이러한 PostgreSQL의 기본적인 제약 사항은 아래와 같다.

| 항목                                         | 제한 사항  |
| -------------------------------------------- | ---------- |
| 최대 DB 크기(Database Size)                  | 무제한     |
| 최대 테이블 크기(Table Size)                 | 32TB       |
| 최대 레코드 크기(Row Size)                   | 1.6TB      |
| 최대 컬럼 크기(Field Size)                   | 1 GB       |
| 테이블당 최대 레코드 개수(Rows per Table)    | 무제한     |
| 테이블당 최대 컬럼 개수(Columns per Table)   | 250~1600개 |
| 테이블당 최대 인덱스 개수(Indexes per Table) | 무제한     |



## PostgreSQL의 특징

* Portable
  * PostgreSQL은 ANSI C로 개발되었으며, 지원하는 플랫폼의 종류로는 Windows, Linux, MAC OS/X, Unix 등 다양한 플랫폼을 지원한다.
* Reliable
  * 트랜잭션 속성인 ACID에 대한 구현 및 MVCC
  * 로우 레벨 라킹 등이 구현
* Scalable
  * PostgreSQL의 멀티 버젼에 대해 사용이 가능
  * 대용량 데이터 처리를 위한 Table Partitioning과 Tables Space 기능 구현이 가능
* Secure
  * DB 보안은 데이터 암호화, 접근 제어 및 감시의 3가지로 구성됨
  * 호스트-기반의 접근 제어, Object-Level 권한, SSL 통신을 통한 클라이언트와 네트워크 구간의 전송 데이터를 암호화 등 지원
* Recovery & Availability
  * Streaming Replication을 기본으로, 동기식/비동기식 Hot Standbt 서버를 구축 가능
  * WAL Log 아카이빙 및 Hot Back up을 통해 Point in time recovery 가능
* Advanced
  * pg_upgrade를 통해 업그레이드를 할 수 있으며, 웹 또는 C/S 기반의 GUI 관리 도구를 제공하여 모니터링 및 관리는 물론 튜닝까지 가능
  * 사용자 정의 Procedural로 Perl, Java, Php 등의 스크립트 언어 지원이 가능





## Template 데이터베이스

PostgresQL에서 "Create Database"로 테이블을 생성할 때, 기본으로 생성되어 있는 Template1 Database를 복사하여 생성한다. 즉 Template Database는 표준 시스템 데이터베이스로 원본 데이터베이스에 해당하는데, 민액 template1에서 프로시저 언어 PL/Perl을 설치하는 경우 해당 데이터베이스를 생성할 때 추가적인 작업 없이 사용자 데이터베이스가 자동으로 사용가능하다.

PostgresQL에는 Template0라는 2차 표준 시스템 데이터베이스가 있는데, 이 데이터베이스에는 template1의 초기 내용과 동일한 데이터가 포함되어 있다. Template 0은 수정하지 않고 원본 그대로 유지하여 무수정 상태의 데이터베이스를 생성할 수 있으며, pg_dump 덤프를 복원할 때 유용하게 사용할 수 있다.

일반적으로 template1에는 인코딩이나 로케일 등과 같은 설정들을 해주고, 템플릿을 복사하여 데이터베이스를 생성한다. 그리고 template0을 통해서는 새로운 인코딩 및 로케일 설정을 지정할 수 있다.

template0`을 복사하여 데이터베이스를 생성하려면

```sql
CREATE DATABASE dbname TEMPLATE template0;
```

SQL 환경에서 다음을 사용해야 한다.

```sql
createdb -T template0 dbname
```





## Vacuum

Vacuum은 PostgreSQL에만 존재하는 고유 명령어로, 오래된 영역을 재사용 하거나 정리해주는 명령어이다. PostgreSQL에서는 MVCC(Multi-Version Concurrency Control, 다중 버전 동시성 제어) 기법을 활용하기 때문에 특정 Row를 추가 또는 업데이트 할 경우, 디스크 상의 해당 Row를 물리적으로 업데이트 하여 사용하지 않고, 새로운 영역을 할당해서 사용한다. 예를 들어 전체 테이블을 Update하는 경우에는 자료의 수만큼 자료 공간이 늘어나게 된다. 그러므로 Update, Delete, Insert가 자주 일어나는 Database의 경우는 물리적인 저장 공간이 삭제되지 않고 남아있게 되므로,  vacuum을 주기적으로 해주는 것이 좋다. Vacuum을 사용하면 어느 곳에서도 참조되지 않고, 안전하게 재사용할 수 있는 행을 찾아 FSM(Free Space Map)이라는 메모리 공간에 그 위치와 크기를 기록한다. 그리고 Insert 및 Update 등 새로운 행을 추가하는 경우, FSM에서 새로운 데이터를 저장할 수 있는 적당한 크기의 행을 찾아 사용한다.



### Vacuum Command

vacuumdb를 활용하여 주기적으로 정리할 수 있는데, 관련 옵션들은 아래와 같다. full 옵션 없이 vacuumdb를 실행할 경우는 단순히 사용가능한 공간만을 반환한다. 하지만 full옵션을 추가하는 경우에는 빈 영역에 tuple을 옮기는 등 디스크 최적화 작업을 하게 된다. 디스크 최적화를 위해 table에는 LOCK이 걸리게 되고, 시간이 오래 걸리게 되므로 사용 시 주의해야 한다. 

```
사용법:
 ``vacuumdb [옵션]... [DB이름]
```

 

```python
옵션들:
  -a, --all            				모든 데이터베이스 청소
  -d, --dbname=DBNAME       		DBNAME 데이터베이스 청소
  -e, --echo           				서버로 보내는 명령들을 보여줌
  -f, --full           				대청소
  -F, --freeze          			행 트랜잭션 정보 동결
  -q, --quiet           			어떠한 메시지도 보여주지 않음
  -t, --table='TABLE[(COLUMNS)]'	지정한 특정 테이블만 청소
  -v, --verbose          			작업내역의 자세한 출력
  -V, --version          			output version information, then exit
  -z, --analyze          			update optimizer statistics
  -Z, --analyze-only       			only update optimizer statistics
  -?, --help           				show this help, then exit
```

```python
연결 옵션들:
  -h, --host=HOSTNAME    			데이터베이스 서버 호스트 또는 소켓 디렉터리
  -p, --port=PORT      				데이터베이스 서버 포트
  -U, --username=USERNAME  			접속할 사용자이름
  -w, --no-password     			암호 프롬프트 표시 안 함
  -W, --password      				암호 프롬프트 표시함
  --maintenance-db=DBNAME  			alternate maintenance database
```



### autovacuum 활용

PostgreSQL 서버 실행 시에 참고하는 ``postgresql.conf`` 파일 안의 AUTOVACUUM PARAMETERS를 지정하여 활성화할 수 있다. 9.0 이상의 버전에서 부터는 해당 파라미터들이 주석처리(#)되어 있어도, default로 실행이 되게 되어 있다.





## 참고자료

* https://d2.naver.com/helloworld/227936
* [https://bitnine.tistory.com/entry/PostgreSQL%EC%9D%B4%EB%9E%80?category=553388](https://bitnine.tistory.com/entry/PostgreSQL이란?category=553388)
* https://velog.io/@ground4ekd/postgresql
* http://postgresql.kr/docs/11/manage-ag-templatedbs.html
* Vacuum
  * https://ktdsoss.tistory.com/256
  * http://postgresql.kr/blog/postgresql_table_bloating.html







# PostgreSQL 실습



## 외부접속 설정

http://www.gurubee.net/lecture/2917



## 사용자 생성

```sql
CREATE USER TEST_USER PASSWORD 'TEST_PASSWD' CREATEDB;
```



### 사용자 옵션 목록

|                     Option                     | Option                                                       |
| :--------------------------------------------: | :----------------------------------------------------------- |
|            SUPERUSER \| NOSUPERUSER            | 해당 USER를 SUPERUSER권한을 주는 것입니다. 따로 지정하지 않을 경우 DEFAULT값으로 NOSUPERUSER가 됩니다. |
|             CREATEDB \| NOCREATEDB             | DATABASE를 생성하는 권한을 정의합니다. CREATEDB를 선택할 경우 USER는 DATABASE를 생성할 권한이 부여됩니다. NOCREATEDB를 선택할 경우 USER는 DATABASE를 생성할 권한이 거부됩니다. 따로 정의 되어있지 않을 경우 NOCREATEDB값이 default값으로 설정 되어 있습니다. |
|           CREATEUSER \| NOCREATEUSER           | 스스로 새로운 유저를 생성하는 권한을 부여하는 것을 정의합니다. CREATEUSER를 선택할 경우 USER를 생성할 수 있는 권한이 부여됩니다. NOCREATEUSER를 선택할 경우 USER를 생성할 권한이 거부됩니다. |
|              INHERIT \| NOINHERIT              | DATABASE의 권한을 다른 구성원들에게 상속하는 역할을 합니다. 따로 정의 되어있지 않을 경우 INHERIT 값이 default값으로 설정 되어 있습니다. |
|                LOGIN \| NOLIGIN                | USER가 LOGIN을 하는 역할을 부여한다.                         |
|           CONNECTION LIMIT connlimit           | 로그인 할 때 동시연결을 지원 하는 기능으로 default값으로 -1(제한없음)로 설정 되어 있습니다. |
| [ENCRYPTED \| UNCRYPTED ] PASSWORD 'password'’ | ‘password’를 입력하고 인증이 필요 없는 경우 옵션을 생략이 가능합니다. |



## 데이터베이스 생성

```sql
CREATE DATABASE MY_DB OWNER TEST_USER
```



## 데이터베이스 삭제

```sql
DROPDB MY_DB
```



## 데이터베이스 접속

```SQL
PSQL MY_DB
```



## PostgreSQL이 지원하는 표준 SQL 타입

```SQL
int, smallint, real, double precision, char(N), varchar(N), date, time, timestamp, interval
```

real: single precision 부동 소수를 저장하기 위한 타입



## Docker로 PostgreSQL 실행하기

* 컨테이너 생성: docker run -d --rm -it --name test_psql -e POSTGRES_PASSWORD=test -p 5432:5432 postgres:latest
* 컨테이너 접속: PGPASSWORD=test psql -U postgres -d postgres -h 127.0.0.1 -p 5432
  * Database 목록 조회: \l or \list
  * 테이블 목록 조회: \dt
  * PSQL Shell 종료: \q



## 참고 자료

* http://www.gurubee.net/lecture/2939
* http://www.gurubee.net/lecture/2917