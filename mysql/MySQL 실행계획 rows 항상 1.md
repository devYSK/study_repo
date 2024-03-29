# MySQL 실행계획 rows, filtered가 제대로 안나오고 1이거나 100.00일때



MySQL에서 쿼리 튜닝에 대해 공부하다가, 쿼리와 실행계획을 확인할 때 rows와 filtered값이 제대로 확인되지 않았습니다.

* rows는 항상 1
* filtered는 항상 100.00

때문에 간단하게 발생한 상황과 정확한 값이 나오도록 해결한 방법에 대해 정리합니다.



## 상황

30만건의 데이터가 있는 사원 테이블이 존재합니다.

```
mysql> describe 사원;
+--------------+---------------+------+-----+---------+-------+
| Field        | Type          | Null | Key | Default | Extra |
+--------------+---------------+------+-----+---------+-------+
| 사원번호     | int           | NO   | PRI | NULL    |       |
| 생년월일     | date          | NO   |     | NULL    |       |
| 이름         | varchar(14)   | NO   |     | NULL    |       |
| 성           | varchar(16)   | NO   |     | NULL    |       |
| 성별         | enum('M','F') | NO   | MUL | NULL    |       |
| 입사일자     | date          | NO   | MUL | NULL    |       |
+--------------+---------------+------+-----+---------+-------+
```



이 테이블의 쿼리 실행게획을 보기 위해 explain 키워드를 사용했고, rows와 filtered를 확인하려고 하였습니다.

**쿼리** 

```sql
mysql> select count(*) from 사원 where 이름 LIKE 'K%';
+----------+
| count(*) |
+----------+
|    19221 |
+----------+
1 row in set (0.08 sec)
```

**실행계획**

```
mysql> explain select count(*) from 사원 where 이름 LIKE 'K%';
+----+-------------+--------+------------+------+---------------+------+---------+------+------+----------+-------------+
| id | select_type | table  | partitions | type | possible_keys | key  | key_len | ref  | rows | filtered | Extra       |
+----+-------------+--------+------------+------+---------------+------+---------+------+------+----------+-------------+
|  1 | SIMPLE      | 사원   | NULL       | ALL  | NULL          | NULL | NULL    | NULL |    1 |   100.00 | Using where |
+----+-------------+--------+------------+------+---------------+------+---------+------+------+----------+-------------+
1 row in set, 1 warning (0.00 sec)
```

* rows와 filtered가 1, 100.00으로 나오고 정확하게 나오질 않습니다.

해당 문제의 원인은 다음과 같은 문제들이 있을 수 있습니다.

1. 쿼리의 조건이 매우 구체적이거나 유니크한 경우, MySQL 옵티마이저는 매우 작은 결과 집합을 예상할 수 있다.
2. 인덱스가 잘 구성되어 있고, 쿼리가 인덱스를 효율적으로 사용하는 경우에도 이 현상이 발생할 수 있다.
3. 테이블에 매우 적은 데이터가 있는 경우, 실제 결과 집합이 작기 때문에 `rows` 값이 1로 나올 수 있다.
4. `MySQL 옵티마이저의 예상치가 정확하지 않은 경우, 잘못된 rows 값을 얻을 수도 있습니다. 이는 통계 데이터가 최신이 아니거나 정확하지 않은 경우에 발생할 수 있습니다.` -> 이때 **ANAYZE** 로 테이블의 정보를 분석할 수 있다.



저같은 경우에는 4번, 테이블 분석이 제대로 되지 않아서 생긴 문제였습니다.

## 해결방법

ANALYZE 키워드로 해당 테이블을 분석하고 업데이트 합니다.

* `ANALYZE` 명령어는 MySQL 데이터베이스에서 테이블의 키 분포 및 통계 정보를 분석하고 업데이트하는 데 사용
* MySQL 쿼리 옵티마이저가 더 효율적인 쿼리 실행 계획을 생성할 수 있도록 돕기 위해, 테이블과 관련된 통계 정보를 최신 상태로 유지하는 데 중요한 역할을 한다.
* ANALYZE로 MySQL은 테이블의 인덱스 및 키 분포에 대한 통계 정보를 수집하고 저장

`ANALYZE` 명령어는 데이터가 많이 변경된 경우, 즉 데이터가 추가되거나 삭제되어 테이블의 통계 정보가 최신 상태가 아닐 때 유용하게 사용할 수 있습니다. 

```sql
mysql> ANALYZE table 사원;
+---------------+---------+----------+----------+
| Table         | Op      | Msg_type | Msg_text |
+---------------+---------+----------+----------+
| tuning.사원   | analyze | status   | OK       |
+---------------+---------+----------+----------+
1 row in set (0.04 sec)
```



다시 기존 쿼리의 실행계획을 보니 정확한 rows와 filtered 값이 나오게 됩니다.

```sql
mysql> explain select count(*) from 사원 where 이름 LIKE 'K%';

+----+-------------+--------+------------+------+---------------+------+---------+------+--------+----------+-------------+
| id | select_type | table  | partitions | type | possible_keys | key  | key_len | ref  | rows   | filtered | Extra       |
+----+-------------+--------+------------+------+---------------+------+---------+------+--------+----------+-------------+
|  1 | SIMPLE      | 사원   | NULL       | ALL  | NULL          | NULL | NULL    | NULL | 299512 |    11.11 | Using where |
+----+-------------+--------+------------+------+---------------+------+---------+------+--------+----------+-------------+
```

* rows 299512, filtered 11.11 로 값이 나오는것을 확인할 수 있습니다







* 

