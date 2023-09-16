# Postgresql 외래키 제약조건 없애고 테이블 데이터 초기화하하기



Postgresql을 사용할 때 마이그레이션, 리플리케이션 할 때나 

테스트시 테이블을 수정하거나 데이터를 초기화할 때 외래키 제약조건때문에 복잡한 경우가 있습니다.

MySQL에서는 `SET FOREIGN_KEY_CHECKS` 를 바꿈으로써 제약조건을 해제할 수 있는데요,

```mysql
-- foreign key 제약 체크(기본값) - 제약조건 체크함
SET FOREIGN_KEY_CHECKS = 1;

-- foreign key 제약 미체크 - 제약조건 관계없이 데이터 조작 가능
SET FOREIGN_KEY_CHECKS = 0;
```

Postgresql에서는 session_replication_role 명령어를 이용해서 제약조건을 해제할 수 있습니다. 



## session_replication_role

`session_replication_role`는 현재 세션에서 복제(replication)와 관련된 트리거와 규칙의 실행 여부를 제어하는 설정입니다.

설정은 다음과 같습니다. 

- **origin**: 모든 트리거와 규칙이 정상적으로 동작. PostgreSQL 세션 설정의 default 값
- **replica**: 이 모드가 활성화되면, 모든 트리거와 규칙의 실행이 일시적으로 비활성화됩니다.
  - replication 중인 데이터가 원본 데이터베이스에서 이미 실행된 트리거에 의해 변경되었을 가능성이 있기 때문에 사용합니다.
  - 이 모드를 사용하면 데이터가 변경되지 않고 복제될 수 있습니다.
- **local**: 로그에 기록된 데이터 변경은 적용되지 않으나, 로컬로 정의된 트리거나 규칙은 실행
- **always**: 모든 트리거와 규칙이 항상 실행



Postgresql에서 외래키 제약 조건은 트리거로 구현됩니다. 

* 트리거는 테이블에 대한 특정 이벤트 ( `INSERT`, `UPDATE`, `DELETE`)가 발생할 때 자동으로 실행되도록 정의된 함수

때문에 replica로 특정 세션의 role을 바꾸면 외래키 제약조건 트리거가 비활성화 되어서, 데이터 조작어 실행시 외래키 제약조건을 검사하지 않습니다. 
이렇게 하면 외래 키 제약 조건을 위반하는 레코드를 일시적으로 삽입하거나 수정할 수 있게 됩니다. 

>  이 설정의 원래 용도는 replication 시스템이 복제된 변경 사항을 적용할 때 이 설정을 복제하도록 설정하는 것입니다. 
>
> 데이터 복제나 로딩 시 트리거에 의한 부작용을 방지하기 위한 기능이에요.
>
> 예를들면 replication,  데이터 로딩 중 종종 데이터 무결성 문제나 불필요한 트리거 실행방지를 위해 사용됩니다.
>
> 그러니 아무렇지 않게 남발하면 안됩니다. 



### 외래키 제약조건 해제하고 테이블 데이터 초기화

```postgresql
-- 트리거 정지. 외래키 제약조건을 비활성 시키고 여러 테이블의 데이터 삭제
SET session_replication_role = 'replica'

-- 테이블 데이터 초기화
TRUNCATE table1, table2, table3 CASCADE; -- delete문도 대체가능

-- 데이터 초기화 후에 트리거 및 규칙 실행 재개
SET session_replication_role = 'origin';
```

`session_replication_role` 설정은 세션 별로 유효하며, 그 세션 내에서만 적용됩니다.

다른 세션에는 영향을 미치지 않으며, 세션이 종료되면 해당 설정도 초기 상태 (기본값은 `origin`)로 복원되므로, 초기화시에 사용하면 됩니다.

```postgresql
-- 다음과 같이 사용도 가능


-- 트리거 정지. 외래키 제약조건을 비활성 시키고 여러 테이블의 데이터 삭제
SET session_replication_role = 'replica'

-- 테이블 데이터 초기화 주어진 스키마명 내의 모든 테이블에 대해 DELETE문을 생성하고 이를 출력하므로 내용을 복사해서 지우면된다. 
SELECT 'delete from ' || tablename || ';' as de
FROM pg_catalog.pg_tables
WHERE schemaname = '스키마명';


-- 데이터 초기화 후에 트리거 및 규칙 실행 재개
SET session_replication_role = 'origin';
```



### 참조

* https://www.postgresql.org/docs/current/runtime-config-client.html#GUC-SESSION-REPLICATION-ROLE

* https://postgresqlco.nf/doc/en/param/session_replication_role/*

* https://learn.microsoft.com/ko-kr/azure/postgresql/flexible-server/how-to-bulk-load-data

* https://repost.aws/ko/knowledge-center/rds-postgresql-foreign-keys