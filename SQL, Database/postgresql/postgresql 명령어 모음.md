# Postgresql 명령어 모음



# 접속 명령어

```postgresql
psql -U postgres
// or
psql
```

* localhost의 5432 port로 Postgresql 접속

```postgresql
psql -h 10.52.0.1 -U posgres
```

* 10.52.0.1의 5432 port로 Postgresql 접속

```postgresql
psql -h 10.52.0.1 -U ysk -d testdb
```

* 10.52.0.1의 5432 port로 Postgresql 접속. 접속 User는 'ysk'이며 Target DB는 'testdb'

```postgresql
psql -h 10.52.0.1 -p 9000 -U ysk testdb
```

* 10.52.0.1의 9000 port로 Postgresql 접속. 접속 User는 'ysk'이며 Target DB는 'testdb'



```postgresql
PGPASSWORD=password psql -h 10.52.0.1 -p 9000 -U ysk
```

* 10.52.0.1의 9000 port로 ysk 유저로 password를 입력하여 접속 





# 명령어

- `psql -?` : 옵션 목록 및 도움말을 확인할 수 있습니다.
- `\l` or `\list` + `+`: 데이터베이스 목록을 보여줍니다. +를 붙여 자세한 내용을 확인할 수 있습니다.
- `\d` or `\dt`+ `+` : 테이블, 인덱스, 시퀀스, 뷰 목록을 보여줍니다. +를 붙여 자세한 내용을 확인할 수 있습니다.
- `\d [table]` : 해당 테이블의 정보(컬럼 목록)를 보여줍니다.
- `\di` : 인덱스 목록을 보여줍니다.
- `\ds` : 모든 시퀀스 정보를 보여줍니다.
- `\df` : 모든 함수 정보를 보여줍니다.
- `\dv` : 모든 뷰 테이블 정보를 보여줍니다.
- `\dg` or `\du` : 등록된 사용자 권한 정보 목록을 보여줍니다.
- `\dn` : 스키마 목록을 보여줍니다.
- `\dS` : 시스템 테이블 목록을 보여줍니다.
- `\h` : 간단한 구성의 SQL 사용법을 확인할 수 있습니다.
- `\e` : psql.edit 파일이 열려 query를 수정하고 실행할 수 있습니다.
- `\c` or `\connect` + `[database]` : 다른 데이터베이스에 접속합니다.
- `\c [database] [user]` : 다른 데이터베이스에 지정한 사용자로 접속합니다.
- `\q` : psql 종료

* `\dn` : 스키마 조회 (schema)

# DB 리스트 - 현재 PostgreSQL에 존재하는 모든 DB Instance 리스트를 조회

```postgresql
\l or \list
```



결과 ex)

```
                                     List of databases
        Name         |  Owner   | Encoding |  Collate   |   Ctype    |   Access privileges
---------------------+----------+----------+------------+------------+-----------------------
 blog                | postgres | UTF8     | en_US.utf8 | en_US.utf8 |
 book_store          | postgres | UTF8     | en_US.utf8 | en_US.utf8 |
 community_board     | postgres | UTF8     | en_US.utf8 | en_US.utf8 |
 function_example    | postgres | UTF8     | en_US.utf8 | en_US.utf8 |
 psql_test_db        | postgres | UTF8     | en_US.utf8 | en_US.utf8 |
 postgres            | postgres | UTF8     | en_US.utf8 | en_US.utf8 |
```



###   \- schema 조회

```postgresql
\dn
```



## dt로 다른 schema(스키마)의 테이블 조회

명령어.

```postgresql
\dt [schema_name].*
```

ex) cms 스키마 테이블 조회 

```postgresql
\dt cms.*
```

결과

```
                  List of relations
 Schema |           Name           | Type  |  Owner
--------+--------------------------+-------+----------
 cms    | black_keywords           | table | postgres
 cms    | category_templates       | table | postgres
 cms    | reservations             | table | postgres
 cms    | sellers                  | table | postgres
 cms    | user_manage_logs         | table | postgres
 cms    | users                    | table | postgres
...
```

### 사용할 DB Instance 와 연결

```postgresql
\c or \connect <INSTANCE_NAME> : 지정한 Instance의 이름을 통해서 연결을 수행한다.
```

ex)

```postgresql
\c blog
```



## DB Instance와 연결 후 해당 Instance에 대한 정보 확인

- \dt : 현재 Instance에 있는 모든 Table 정보 확인
- \ds : 현재 Instance에 있는 모든 Sequence 정보 확인
- \df : 현재 Instance에 있는 모든 Function 정보 확인
- \dv : 현재 Instance에 있는 모든 View 정보 확인
- \du : 현재 Instance에 등록된 사용자 목록 확인

##  특정 데이터베이스의 모든 테이블을 조회하려면 

```postgresql
SELECT table_name
FROM information_schema.tables
WHERE table_schema = 'public' -- 테이블이 속한 스키마명, 'public'은 기본 스키마
  AND table_catalog = 'test'; -- 데이터베이스명, 여기서는 'test' 데이터베이스
```

위의 쿼리에서 `public`은 테이블이 속한 스키마 이름입니다. 일반적으로 테이블이 `public` 스키마에 속하는 경우가 많습니다. 그러나 실제로 사용 중인 스키마 이름에 따라 이를 적절히 변경하십시오.

`table_catalog`에는 데이터베이스의 이름을 지정해야 합니다. 여기서는 `test` 데이터베이스에 있는 테이블을 조회하고자 하므로, 해당 데이터베이스의 이름인 `test`로 쿼리를 수정해야 합니다.

이 쿼리를 실행하면 `test` 데이터베이스에 속한 모든 테이블의 목록이 반환됩니다.

```postgresql
SELECT table_name
FROM information_schema.tables
WHERE table_schema = 'cms' 
  AND table_catalog = 'market_hunter_local'; 
```



## DATABASE

### 생성

```null
> postgres=# CREATE DATABASE <databaseName> [[WITH] [OWNER[=] <userName>]];
```

### 삭제

```null
> postgres=# DROP DATABASE <databaseName>;
```

## TABLE

대소문자를 구별하지 않고 큰따옴표를 사용해 대소문자 구분을 명시할 수 있습니다.
1줄 주석의 경우 `--`를 사용합니다.

### 생성

```null
create table 테이블이름 (
  컬럼이름 자료형 조건,
  ...
);
```

### 삭제

```null
> postgres=# DROP TABLE <tableName>;
```