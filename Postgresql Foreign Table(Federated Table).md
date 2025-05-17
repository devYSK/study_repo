

# Postgresql Federated Table -fdw

* https://www.postgresql.org/docs/current/postgres-fdw.html

Federated Table은, 이는 서로 다른 데이터베이스 에 있는 테이블을 하나의 데이터베이스에 있는것처럼 사용하면서 데이터를 관리할 수 있게 해주는 기술입니다. 즉, Federated Table을 사용하면 하나의 데이터베이스에서 다른 데이터베이스의 테이블에 접근할 수 있습니다. 분산된 시스템에서 데이터를 통합 관리하거나, 서로 다른 데이터베이스 시스템 간의 데이터 연동이 필요할 때 유용합니다.

PostgreSQL에서 Federated Table 기능을 구현하기 위해선 주로 외부 데이터 래퍼(Foreign Data Wrapper, FDW)를 사용합니다. FDW는 PostgreSQL에 외부 데이터 소스를 연결하여 그 소스를 마치 로컬 테이블처럼 쿼리하여 사용합니다.  

> Federated Table은 데이터의 물리적인 복제본이 아니라, 원격 데이터베이스에 대한 참조(reference)입니다.

### PostgreSQL에서 Federated Table 설정하는 방법

1. **FDW 확장 설치하기**
   PostgreSQL에서 Federated Table을 사용하기 위해서는 적절한 FDW를 설치해야 합니다. 일반적으로 PostgreSQL에서 많이 사용되는 FDW로는 `postgres_fdw`가 있습니다. 이 확장을 설치하려면 아래와 같이 명령어를 실행합니다:

   ```sql
   CREATE EXTENSION IF NOT EXISTS postgres_fdw;
   ```

2. **Foreign Server 설정하기**
   외부 데이터베이스 서버를 설정해야 합니다. 이를 위해 `CREATE SERVER` 명령어를 사용합니다:

   ```sql
   CREATE SERVER foreign_server
   FOREIGN DATA WRAPPER postgres_fdw
   OPTIONS (host 'remote_host', dbname 'remote_db', port '5432');
   ```

   여기서 `remote_host`는 외부 데이터베이스의 주소, `remote_db`는 외부 데이터베이스의 이름, 그리고 `5432`는 포트 번호입니다.

3. **사용자 매핑하기**
   외부 서버에 접근하기 위한 사용자 매핑을 설정해야 합니다. 이를 위해 `CREATE USER MAPPING` 명령어를 사용합니다:

   ```sql
   CREATE USER MAPPING FOR local_user
   SERVER foreign_server
   OPTIONS (user 'remote_user', password 'remote_password');
   ```

   여기서 `local_user`는 로컬 PostgreSQL 사용자, `remote_user`는 외부 데이터베이스의 사용자, `remote_password`는 그 사용자의 비밀번호입니다.

4. **Foreign Table 생성하기**
   이제 외부 데이터베이스에 있는 테이블을 로컬에서 접근할 수 있도록 Foreign Table을 생성해야 합니다:

   ```sql
   CREATE FOREIGN TABLE foreign_table (
       id integer,
       name text,
       ...
   )
   SERVER foreign_server
   OPTIONS (schema_name 'remote_schema', table_name 'remote_table');
   ```

   여기서 `schema_name`은 외부 데이터베이스의 스키마 이름, `table_name`은 외부 테이블의 이름입니다.

5. **Foreign Table 쿼리하기**
   이제 로컬 PostgreSQL 데이터베이스에서 Foreign Table을 일반 테이블처럼 쿼리할 수 있습니다:

   ```sql
   SELECT * FROM foreign_table;
   ```

이 과정을 통해, 로컬 데이터베이스에 있는 테이블처럼 외부 데이터베이스의 테이블에 접근하고 조작할 수 있습니다. 이러한 Federated Table 방식은 특히 여러 데이터베이스에 분산된 데이터를 통합하여 조회하거나, 데이터를 결합해야 하는 상황에서 유용합니다.

# 예시

1. 같은 데이터베이스 서버의 A 데이터베이스와, B 데이터베이스에서, A데이터베이스의 public 스키마의 테이블 users와 options를 B데이터베이스에서 같은 테이블 users와 options를 공유하고자 함

아래는 같은 데이터베이스 서버의 두 개의 데이터베이스(A와 B)에서, A 데이터베이스의 `public` 스키마에 있는 `users`와 `options` 테이블을 B 데이터베이스에서 참조할 수 있도록 설정하는 예시입니다. 

### 예시 시나리오

- **A 데이터베이스**: 원본 데이터베이스. `users`와 `options` 테이블이 존재합니다.
- **B 데이터베이스**: 원격 테이블을 참조할 데이터베이스. `A` 데이터베이스의 `users`와 `options` 테이블을 `Foreign Table`로 참조합니다.

### 1. A 데이터베이스에서 테이블 생성

먼저, A 데이터베이스에서 `users`와 `options` 테이블을 생성합니다. 

```sql
-- A 데이터베이스에 접속하여 실행
CREATE TABLE public.users (
    id serial PRIMARY KEY,
    username varchar(255) NOT NULL,
    email varchar(255) NOT NULL,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE public.options (
    id serial PRIMARY KEY,
    user_id integer REFERENCES public.users(id),
    option_name varchar(255) NOT NULL,
    option_value text
);
```

### 2. B 데이터베이스에서 `postgres_fdw` 확장 설치

다음으로, B 데이터베이스에서 `postgres_fdw` 확장을 설치합니다.

```sql
-- B 데이터베이스에 접속하여 실행
CREATE EXTENSION IF NOT EXISTS postgres_fdw;
```

### 3. Foreign Server 생성

이제 B 데이터베이스에서 A 데이터베이스를 참조하는 Foreign Server를 생성합니다.

```sql
-- B 데이터베이스에 접속하여 실행
CREATE SERVER b_a_foreign_server
FOREIGN DATA WRAPPER postgres_fdw
OPTIONS (host 'localhost', dbname 'A', port '5432');
```

* **서버 이름(`b_a_foreign_server`)은 각 데이터베이스 내에서 고유해야 합니다**. 동일한 데이터베이스에서 동일한 이름의 `Foreign Server`를 두 번 생성할 수 없으므로, 각 `Foreign Server`의 이름은 고유해야 합니다.

예를 들어, 두 개 이상의 다른 데이터베이스에 연결해야 한다면 각각의 `Foreign Server`에 고유한 이름을 부여해야 합니다.

### 4. 사용자 매핑 생성

B 데이터베이스에서 A 데이터베이스에 접근할 수 있도록 사용자를 생성합니다.

```sql
-- B 데이터베이스에 접속하여 실행
CREATE USER MAPPING FOR b_user
SERVER a_server
OPTIONS (user 'a_user', password 'a_password');
```

* 여기서 `a_user`는 A 데이터베이스에 접근할 수 있는 사용자이고, `b_user`는 B 데이터베이스의 사용자입니다.

### 5. Foreign Table 생성

이제 B 데이터베이스에서 A 데이터베이스의 `users`와 `options` 테이블을 참조하는 Foreign Table을 생성합니다.

#### `users` 테이블에 대한 Foreign Table 생성

```sql
CREATE FOREIGN TABLE public.users (
    id serial,
    username varchar(255) NOT NULL,
    email varchar(255) NOT NULL,
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP
)
SERVER a_server
OPTIONS (schema_name 'public', table_name 'users');
```

**제약 조건**:

- **기본 키(Primary Key)**: 원본 테이블에 정의된 PK는 `Foreign Table`에서도 반영됩니다. 그러나, PostgreSQL은 `Foreign Table`에 직접적으로 제약 조건을 강제하지 않습니다. 즉, 기본 키 제약 조건을 선언할 수는 있지만, 이를 강제하는 것은 원본 데이터베이스에서 이루어집니다.
- **외래 키(Foreign Key)**: `Foreign Table`에서는 외래 키 제약 조건을 정의할 수 없습니다. 외래 키 제약 조건은 원본 테이블에서만 관리됩니다.
- **고유 제약(Unique Constraint)**: `Foreign Table`에 고유 제약 조건을 정의할 수 있지만, 실제로는 원본 테이블에서만 강제됩니다. `Foreign Table` 자체에서는 이 제약을 강제하지 않습니다.

**인덱스**:

- `Foreign Table`은 원본 테이블의 인덱스를 직접 사용할 수 없습니다. 그러나 원본 테이블의 인덱스는 원격 쿼리의 성능에 영향을 미칩니다. `Foreign Table` 자체에는 인덱스를 생성할 수 없습니다.

- `Foreign Table`을 생성할 때, 반드시 원본 테이블과 동일한 스키마를 사용할 필요는 없습니다. B 데이터베이스에서 A 데이터베이스의 테이블을 다른 스키마에 매핑할 수 있습니다.

- `Foreign Table`을 생성할 때, 원본 테이블의 데이터 타입과 동일하게 필드를 정의해야 합니다. 데이터 타입이 다르면 데이터 변환 중 문제가 발생할 수 있습니다.

- 필드 이름이 반드시 원본 테이블과 동일할 필요는 없지만, 다른 이름을 사용할 경우 쿼리 작성 시 혼동이 있을 수 있으므로 동일하게 유지하는 것이 일반적입니다.

- `Foreign Table`의 성능은 네트워크 상태와 원본 데이터베이스의 성능에 의존합니다. 필요한 경우, `use_remote_estimate` 옵션을 설정하여 원본 서버의 쿼리 실행 계획을 활용할 수 있습니다.

  

#### `options` 테이블에 대한 Foreign Table 생성

```sql
CREATE FOREIGN TABLE public.options (
    id serial,
    user_id integer,
    option_name varchar(255) NOT NULL,
    option_value text
)
SERVER a_server
OPTIONS (schema_name 'public', table_name 'options');
```

### 6. 권한 부여

A 데이터베이스의 테이블에 접근할 수 있도록 필요한 권한을 부여합니다.

```sql
-- A 데이터베이스에 접속하여 실행
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE public.users TO a_user;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE public.options TO a_user;
```

또한, B 데이터베이스의 사용자에게 필요한 권한을 부여합니다.

```sql
-- B 데이터베이스에 접속하여 실행
GRANT SELECT, INSERT, UPDATE, DELETE ON FOREIGN TABLE public.users TO b_user;
GRANT SELECT, INSERT, UPDATE, DELETE ON FOREIGN TABLE public.options TO b_user;
```

### 7. 사용 예시

이제 B 데이터베이스에서 `users`와 `options` 테이블을 로컬 테이블처럼 사용할 수 있습니다. 예를 들어, 데이터를 조회하거나 삽입하는 쿼리를 실행할 수 있습니다.

```sql
-- B 데이터베이스에 접속하여 실행
SELECT * FROM public.users;
INSERT INTO public.options (user_id, option_name, option_value) VALUES (1, 'theme', 'dark');
```

### 8. 원격 서버 쿼리 실행 계획 참조

```sql
ALTER SERVER b_a_foreign_server OPTIONS (ADD use_remote_estimate 'true');
```

* **`false` (기본값)**
* 원격 서버에서 제공하는 쿼리 실행 계획을 참조합니다. PostgreSQL은 원격 서버에 `EXPLAIN` 쿼리를 보내고, 그 결과를 기반으로 쿼리 실행 계획을 세웁니다. 이 방식은 원격 서버가 제공하는 정보에 기반하여 더 정확한 계획을 세울 수 있기 때문에 복잡한 쿼리나 대용량 데이터의 경우 성능을 향상시킬 수 있습니다.
* `'false'`로 설정하면, PostgreSQL은 원격 서버의 쿼리 실행 계획 추정치를 사용하지 않고, 로컬에서 자체적으로 추정하여 쿼리 계획을 세웁니다.

**장점**:

- **정확한 쿼리 계획**: 복잡한 쿼리나 대규모 데이터 집합에 대해, 원격 서버의 실행 계획을 참조함으로써 로컬 서버의 쿼리 실행 계획이 더 정확해집니다.
- **성능 최적화**: 특히, 조인이나 필터링이 많이 필요한 경우, 원격 서버에서 제공하는 통계와 정보를 사용하여 성능을 향상시킬 수 있습니다.

**단점**:

- **추가 네트워크 오버헤드**: 로컬 서버에서 원격 서버로 `EXPLAIN` 쿼리를 보내고 결과를 받아오는 과정에서 네트워크 트래픽이 증가할 수 있습니다. 이로 인해 쿼리의 초기 계획 시간이 더 길어질 수 있습니다.
- **복잡성 증가**: 단순한 쿼리에서는 불필요한 오버헤드가 발생할 수 있습니다. 원격 서버의 성능이 낮거나 네트워크 지연이 큰 경우, 오히려 성능 저하가 발생할 수 있습니다.

#### 

### 요약

- **A 데이터베이스**에서 `users`와 `options` 테이블을 생성합니다.
- **B 데이터베이스**에서 `postgres_fdw`를 사용하여 `Foreign Table`을 설정합니다.
- 사용자 권한을 적절히 부여하여, B 데이터베이스의 사용자들이 A 데이터베이스의 테이블에 접근할 수 있도록 설정합니다.

이 설정을 통해 B 데이터베이스에서 A 데이터베이스의 테이블을 마치 로컬 테이블처럼 사용하여 데이터 조회 및 조작이 가능합니다.



# **원본 테이블의 인덱스는 사용됩니다.**

### 구체적인 설명

- **Foreign Table의 동작**: `Foreign Table`을 통해 쿼리를 실행하면, PostgreSQL은 원본 데이터베이스(예: `sp_partners_cms`)에 쿼리를 전달합니다. 이 쿼리는 원본 데이터베이스에서 실행됩니다.
- **원본 테이블에서의 인덱스 사용**: 원본 데이터베이스에서 쿼리가 실행될 때, 해당 데이터베이스에 정의된 인덱스가 자동으로 사용됩니다. 따라서, 원본 테이블에 인덱스가 설정되어 있다면, 그 인덱스는 쿼리 성능을 최적화하는 데 사용됩니다.

예를 들어, `unit_partners_cms` 데이터베이스에서 `keywords` 테이블을 `Foreign Table`로 참조하여 `SELECT` 쿼리를 실행할 때, 실제 데이터는 `sp_partners_cms` 데이터베이스의 `keywords` 테이블에서 가져오며, 이 과정에서 `sp_partners_cms` 데이터베이스의 `keywords` 테이블에 설정된 인덱스가 사용됩니다.

### 요약

- `Foreign Table`을 통해 원본 데이터베이스에서 데이터를 쿼리할 때, **원본 테이블에 설정된 인덱스가 사용됩니다**.
- 인덱스는 원본 데이터베이스의 쿼리 성능을 최적화하는 데 중요한 역할을 합니다.

따라서, 원본 테이블에 적절한 인덱스를 설정해 두면, `Foreign Table`을 통해 접근할 때에도 성능이 향상됩니다.



# Foreign Server 설정  옵션들 

PostgreSQL에서 `FDW`를 사용하여 `Foreign Server`를 설정할 때, `use_remote_estimate` 외에도 여러 가지 설정 옵션이 있습니다. 이 옵션들은 `Foreign Server`와 `Foreign Table`의 동작을 세밀하게 조정할 수 있습니다.

### 1. `fetch_size`

**설명**: 한 번에 가져올 행(row)의 수를 설정합니다. 

- **기본값**: 100 (PostgreSQL 13 이후)
- 대용량 데이터를 가져올 때, 한 번에 가져오는 데이터 양을 조정하여 메모리 사용량을 최적화할 수 있습니다.
- **설정 예시**:

  ```sql
  ALTER SERVER a_server
  OPTIONS (ADD fetch_size '1000');
  ```

**장점**:
- 네트워크 왕복 횟수를 줄일 수 있어 성능이 향상될 수 있습니다.
- 메모리 관리에 유리하며, 대용량 데이터 처리 시 효율적입니다.

**단점**:
- 너무 큰 `fetch_size`는 메모리 사용량을 급격히 증가시킬 수 있습니다.

### 2. `async_capable`

**설명**: 비동기 쿼리 실행을 활성화할지 여부를 설정합니다. 

- **기본값**: `false`
- 비동기적으로 여러 원격 서버에 쿼리를 보내고 응답을 기다리지 않고 계속해서 다른 작업을 진행할 수 있도록 설정합니다.
- **설정 예시**:

  ```sql
  ALTER SERVER a_server
  OPTIONS (ADD async_capable 'true');
  ```

**장점**:
- 여러 원격 서버와의 통신에서 병렬 처리가 가능하여 성능이 향상될 수 있습니다.

**단점**:
- 비동기 처리의 복잡성이 증가할 수 있으며, 응답 시간에 따른 비동기 처리에 대한 추가 관리가 필요합니다.

### 3. `connect_timeout`

**설명**: 원격 서버에 연결하는 데 소요될 최대 시간을 초 단위로 설정합니다.

- **기본값**: `NULL` (시스템 기본값을 사용)
- 네트워크가 불안정하거나, 원격 서버의 응답이 느린 경우 타임아웃을 설정하여 쿼리가 무한정 기다리지 않도록 할 수 있습니다.
- **설정 예시**:

  ```sql
  ALTER SERVER a_server
  OPTIONS (ADD connect_timeout '10');
  ```

**장점**:
- 네트워크 문제로 인해 쿼리가 무한히 대기하는 것을 방지할 수 있습니다.

**단점**:
- 너무 짧게 설정하면, 일시적인 네트워크 지연에도 연결이 실패할 수 있습니다.

### 4. `keepalive`

**설명**: 서버 간 연결이 활성 상태를 유지하도록 주기적으로 "keepalive" 패킷을 보내는 기능을 설정합니다.

- **사용 예**: 장시간 연결이 유지되는 동안 네트워크의 연결 상태를 주기적으로 확인하고, 연결이 끊어지는 것을 방지할 수 있습니다.
- **설정 예시**:

  ```sql
  ALTER SERVER a_server
  OPTIONS (ADD keepalive '1');
  ```

**장점**:
- 네트워크가 불안정한 환경에서 유용합니다.

**단점**:
- 너무 자주 패킷을 보내면 네트워크 오버헤드가 증가할 수 있습니다.

### 5. `reconnect_attempts`

**설명**: 원격 서버에 대한 연결이 실패했을 때, 재시도할 횟수를 설정합니다.

- **사용 예**: 일시적인 네트워크 문제로 인해 연결이 실패하는 경우, 자동으로 재시도할 수 있습니다.
- **설정 예시**:

  ```sql
  ALTER SERVER a_server
  OPTIONS (ADD reconnect_attempts '3');
  ```

**장점**:
- 일시적인 네트워크 문제에 더 잘 대처할 수 있습니다.

**단점**:
- 재시도가 여러 번 발생할 경우, 쿼리 지연이 길어질 수 있습니다.

### 6. `use_remote_estimate`

**설명**: 원격 서버에서 제공하는 쿼리 실행 계획을 사용할지 여부를 설정합니다.

- **기본값**: `false`
- **사용 예**: 원격 서버의 실행 계획을 참고하여 로컬 서버에서의 쿼리 최적화를 돕습니다.
- **설정 예시**:

  ```sql
  ALTER SERVER a_server
  OPTIONS (SET use_remote_estimate 'true');
  ```

### 7. `updatable`

**설명**: `Foreign Table`이 업데이트 가능한지 여부를 설정합니다.

- **기본값**: `true`
- **사용 예**: `Foreign Table`을 읽기 전용으로 만들고 싶을 때 `false`로 설정할 수 있습니다.
- **설정 예시**:

  ```sql
  ALTER SERVER a_server
  OPTIONS (ADD updatable 'false');
  ```

**장점**:
- 읽기 전용으로 설정하여 데이터 보호.

**단점**:
- 데이터를 수정해야 하는 경우, 다시 설정을 변경해야 함.



# 삭제하고 재생성하기

PostgreSQL에서 외부 테이블(foreign table)을 확인하려면 외부 테이블이 연결된 서버를 확인하는 방법은 다음과 같습니다.

1. **테이블 목록 확인**: 먼저 외부 테이블이 어떤 외부 서버에 연결되어 있는지 확인하려면, `pg_foreign_table` 시스템 카탈로그를 조회해야 합니다.

   ```sql
   SELECT
       ft.relname AS foreign_table_name,
       srv.srvname AS foreign_server_name
   FROM
       pg_foreign_table ft
   JOIN
       pg_foreign_server srv
       ON ft.ftserver = srv.oid;
   ```

   이 쿼리는 현재 데이터베이스에 존재하는 외부 테이블 목록과 해당 테이블이 연결된 외부 서버의 이름을 출력합니다.

2. **외부 서버 정보 확인**: 만약 특정 서버에 대한 더 자세한 정보를 확인하고 싶다면 `pg_foreign_server` 테이블을 조회하면 됩니다.

   ```sql
   SELECT *
   FROM pg_foreign_server
   WHERE srvname = 'your_server_name';
   ```

   이 쿼리는 특정 서버에 대한 정보(서버 이름, 서버 유형, 서버 옵션 등)를 확인할 수 있습니다.

이러한 방법으로 외부 테이블이 어떤 서버와 연결되어 있는지 확인하고 해당 서버에 대한 자세한 정보를 얻을 수 있습니다.



서버이름을 알아냈으면, 

```
DROP FOREIGN TABLE IF EXISTS keywords;
```

 재생성해야합니다. 



# JPA와 Federated Table

JPA 자체는 데이터베이스에서 Federated Table을 직접적으로 지원하는 생성 및 관리하는 기능을 제공하지 않습니다. 

그러나 직접 데이터베이스에서  Federated Table을 설정한 후, JPA에서 이 Foreign Table을 로컬 테이블처럼 사용할 수 있습니다.

### 주의사항

- **성능**: Foreign Table은 네트워크를 통해 외부 데이터베이스와 상호작용하므로 로컬 테이블에 비해 퍼포먼스가 떨어질 수 있습니다. 특히 대량의 데이터를 처리할 때 주의가 필요합니다.
- **트랜잭션 관리**: JPA에서 관리하는 트랜잭션이 외부 데이터베이스의 트랜잭션과 동기화되지 않을 수 있습니다. 따라서 외부 데이터베이스와의 트랜잭션 일관성을 보장하기 위해서는 추가적인 트랜잭션 관리가 필요할 수 있습니다.
