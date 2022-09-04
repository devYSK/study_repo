

# 동시성 제어란

* 동시성 제어(Concurrency Control)란
  * 다중 사용자 환경을 지원하는 데이터베이스 시스템에서 동시에 실행되는 여러 트랜잭션간의 간섭으로 문제가 발생하지 않도록 트랜잭션의 실행순서를 제어하는 기법
  * 다중 사용자 환경을 지원하는 DB system의 경우 필수적으로 지원해야 하는 기능으로 병행제어라고도 한다.
  * **동시성 제어**(concurrency control)는 가능한 빠른 조회와 동시에 [병행](https://ko.wikipedia.org/wiki/병행_컴퓨팅)되는 동작의 정확한 결과가 발생하는 것을 보증한다.
  * 동시성 제어는 데이터베이스에서 발생하는 작업의 동시 실행을 제어하는 데 필요한 관리 절차.
  * 그러나 동시성 제어에 대해 알기 전에 동시 실행(Conccurent Execution에 대해 알아야 한다.



## Concurrent Execution in DBMS (데이터베이스 시스템에서의 동시 실행)

- 다중 사용자 시스템에서 여러 사용자가 한 번에 동일한 데이터베이스에 액세스하여 사용할 수 있으며 이를 데이터베이스 동시 실행이라고 한다.
- 다중 사용자 시스템에서 동일한 데이터베이스가 다른 사용자에 의해 `동시`에 실행됨을 의미.
- 데이터베이스 트랜잭션 작업을 하다보면 여러 사용자가 서로 다른 작업을 수행하기 위해 데이터베이스를 사용해야 하는 상황이 발생하는데, 이 경우 데이터베이스의 동시 실행이 이루어진다.
- 문제는 수행되는 동시 실행이 인터리브 방식으로 수행되어야 하며 어떤 작업도 실행 중인 다른 작업에 영향을 주지 않아 `데이터베이스의 일관성을 유지해야 한다는 것`입니다. 따라서 트랜잭션 작업을 동시에 수행할 때 해결해야 할 몇 가지 어려운 문제가 발생.

<br>

## 트랜잭션

**트랜잭션**

트랜잭션은 하나의 작업을 수행하기 위해 필요한 데이터베이스 연산을 모아둔 것을 의미하고 논리적인 작업의 단위가 된다. 트랜잭션은 일반적인 SQL 문의 명령문로 표현된 연산이라고 볼 수 있다.

 <br>

트랜잭션의 4가지 특성 - ACID

**1) 원자성 Atomicity**

트랜잭션의 원자성은 트랜잭션을 구성하는 연산들이 모두 완전히 실행되거나 모두 실행되지 않음을 의미한다. 트랜잭션 도중 장애가 발생하면, 초기 시점으로 데이터베이스를 회복하여 원자성을 보장하도록 한다.

 <br>

**2) 일관성 Consistency**

데이터베이스에 대한 작업 처리 결과가 트랜잭션의 내용에 일치하는 일관성을 가져야한다.

 <br>

**3) 격리성 Isolation**

현재 수행중인 트랜잭션이 완료될 때까지 트랜잭션이 생성한 중간 연산 결과를 다른 트랜잭션이 접근할 수 없다.

 <br>

**4) 영속성 Durability**

트랜잭션이 성공적으로 완료된 후 데이터베이스에 반영한 수행 결과는 어떠한 경우에도 손실되지 않고 영구적이어야한다.

**Commit과 Rollback 연산**

Commit: 트랜잭션이 성공적으로 수행되었음을 선언한다.

Rollback: 트랜잭션이 실패했음을 선언한다.

<br>

<br>

**트랜잭션 스케줄**

트랜잭션이 병행처리될 때, 여러 트랜잭션들의 액션들의 실행 순서를 스케줄이라고한다. 액션은 데이터베이스 접근 연산들의 목록이며 스케줄의 목적은 트랜잭션들을 병행 처리하지만 결과적으로 직렬 처리와 같은 효과를 얻는 연산 순서를 만드는 것이다.

 <br>

**스케줄의 표현**

* Serial Schedule 직렬 스케줄
  * 트랜잭션을 하나씩 순차적으로 처리한다. 가장 단순하고 병행처리의 오류가 없다.

* Concurrent Scehdule 병행 스케줄
  * 트랜잭션을 병행적으로 처리한다. 연산 결과의 일관성을 유지하기 어렵다.

* Serializable Schedule 직렬화 스케줄
  * 병행 처리를 기본으로 하지만 필요할 때 접근 제어를 하여 직렬성을 유지한다.

* Complete Schedule 완전 스케줄
  * 각 트랜잭션에 대한 철회와 완료를 포함하는 완전한 스케줄.

* Conflict Schedule 충돌 스케줄
  * 병행 처리중 제어가 필요한 스케줄로 직렬화 스케줄로 만들어야 한다.

 <br>

**충돌가능한 연산**

- Write-Read 충돌 : 한 트랜잭션이 자료를 갱신하는 도중에 다른 트랜잭션이 자료를 읽는다. 오류 읽기 dirty read와 잘못된 요약 incorrect summary이 예상된다.
- Read-Write 충돌: 한 트랜잭션이 자료를 읽는 도중에 다른 트랜잭션이 자료를 갱신한다. dirty read와 무결성 제약 조건에 문제가 있을 수 있다.
- Write-Write 충돌: 한 트랜잭션이 자료를 갱신하는 도중에 다른 트랜잭션도 자료를 갱신한다. 한 트랜잭션의 결과가 반영되지 않은 lost update가 발생할 수 있다.



<br>

## 동시성 제어를 안함으로써의 문제점

* 다중사용자 DBMS는 다중 프로그래밍의 개념을 이용하기 때문에 한 트랜잭션을 실행하는 중에 다른 트랜잭션이 끼어들어 실행할 수 있다. 
* 이때 동시에 실행되는 트랜잭션들이 서로 간에 간섭함으로써 나타나는 문제점들로는 
  * 갱신내용 손실(lost update) 
  * 연쇄 복귀(cascading rollback) 
  * 회복 불가능(Unrecoverability) 
  * 불일치 분석, 모순성(inconsistent analysis) 등과 같은 문제들이 발생할수 있다. 

<br>

* ### 갱신 내용 손실 (Lost Update)

  * 트랜잭션들이 `동일한 데이터`를 `동시에 갱신하는 경우`에 발생
  * 또는, 이전 트랜잭션이 데이터를 갱신한 후 트랜잭션이 종료하기 전에, 나중 트랜잭션이 동일한 데이터를 갱신하여 갱신된 값을 다시 덮어쓰는 경우에 발생하는 문제이다. 
  * 수정된 값을 부정확하게 만들어 데이터베이스를 일관성이 없게 만든다.

<br>

* ### 현황파악 오류

  * 트랜잭션의 중간 수행 결과를 다른 트랜잭션이 참조함으로써 생기는 오류
  * 읽기 작업을 하고있는 트랜잭션1 이 쓰기 작업을 하고 있는 트랜잭션2가 작업한 작업도중의 중간 데이터를 읽기 때문에 발생하는 문제
  *  작업중인 트랜잭션2 가 작업을 rollback한 경우 트랜잭션 1은 무효가 된 데이터를 읽게 되고 잘못된 결과를 도출한다. 

<br>

* ### 불일치 분석, 모순성 (inconsistent analysis)

  * 여러 개의 트랜잭션이 `동시에 실행할 때 끼어들기로 인해` 트랜잭션의 `일관성`이 유지되지 못하는 상황

  * `두 트랜잭션이 동시에 실행할 때` DB가 `일관성`이 없는 모순된 상태로 남는 오류이다. 

  * 다른 트랜잭션이 `해당 항목 값`을 갱신하는 동안 한 트랜잭션이 두 개의 항목 값 중, 어떤 것은 갱신되기 전의 값을 읽고 다른 것은 갱신된 후의 값을 읽게 되어 데이터의 불일치가 발생한다.

  * #### * 일관성 * : 트랜잭션이 실행되기 전과 후의 데이터베이스 내용이 일관되게 유지되는 특성 

<br>

* ### 연쇄 복귀 (Cascading Rollback)

  * 여러 트랜잭션이 동일한 데이터 내용을 접근할 때 발생

  * 여러개의 트랜잭션이 데이터를 공유할 때, 특정 트랜잭션이 이전 상태로 rollback(복귀) 할 경우  아무 문제 없는 다른 트랜잭션까지 연달아 복귀하게 되는 문제.

  * 이 때 한 `트랜잭션이 이미 완료된 상태`라면 트랜잭션의 `지속성` 조건에 따라 복귀 불가능

  * #### * 지속성 *: 트랜잭션이 성공적으로 완료되면 해당트랜잭션이 갱신한 데이터베이스의 내용은 영구적으로 저장되어야 하는 조건

<br>

### 동시성 제어는 이러한 문제들은 방지하고 데이터의 무결성 및 일관성을 보장하기 때문에 다중 사용자 DB 시스템에서 매우 중요하다.

* #### * 데이터 무결성 * : 데이터의 정확성과 일관성을 유지하고 보증하는 것을 가리키며, 데이터베이스 내의 데이터가 얼마나 정확한지를 의미

<br>

**동시성 제어의 목적**

- 프로세스와 디스크 활용을 최대화한다.
- 단위 시간당 트랜잭션 처리 건수를 증가시킨다.
- 사용자 서비스 제공에 대한 응답시간을 최소화한다.
- 데이터베이스 공유 정도의 최대화를 보장한다.
- 데이터베이스 일관성을 유지한다. 

<br>

## 동시성 제어 - 여러 트랜잭션들의 충돌문제 해결 방법

* 다중 프로그램 환경에서 여러 개의 트랜잭션을 병행수행한다는 것은  
  동시에 여러개의 명령을 실행 한다는 것이 아니라 
  `동시에 실행하는것처럼 빠르게` 일정한 시간 내에 각 트랜잭션에 있는 명령들이 번갈아 실행되는 것이다. 
* 병행 수행된 각각의 트랜잭션 결과는 각 트랜잭션을 독자적으로 수행시켰을 때의 결과와 같아야 하는데,
  이럴 직렬성(Seiraliazabilty) 또는 직렬화 가능성 이라고 한다.
* 트랜잭션들을 직렬 스케줄로 만들거나 직렬 가능한 스케줄로 만들어야 한다.
  * 직렬 처리를 실행한 결과를 낼 수 있도록 스케줄을 조정한다. 직렬 스케줄은 충돌 스케줄에 대해 직렬화하여 순차적으로 실행할 수 있게끔 만든다.
* 직렬 스케줄은 끼어들기를 허용하지 않고 순차적으로 실행하기 때문에 `다중 프로그래밍 환경에서 최적의 방법으로 볼 수 없다.`

다중사용자 DBMS에서는 동시성(병행 수행)을 최대한 보장하면서 직렬 스케줄과 동일한 결과를 얻을 수 있는
 **직렬 가능한 스케줄**로 만드는 것이 가장 좋은 방법이다.

<br>

***잠금(Locking)은 트랜잭션의 실행 순서를 강제로 제어하여 직렬 가능한 스케줄이 되도록 보장하는 방법이다.\***

<br>

## 동시성 제어 기법

### 1. Locking : 하나의 트랜잭션이 실행하는 동안 특정 데이터항목에 대해서 다른 트랜잭션이 동시에 접근하지 못하도록 하는 기능을 제공하는 기법

1. SharedLock (또는 Read Lock) : 읽기 잠금 이라고도한다. 읽기 락이 걸려있으면 데이터 항목에 대해 읽기만 가능하다.
   *  어떤 트랜잭션에서 데이터를 읽고자 할 때, 동시에 읽은수는 있지만, 변경은 불가능하다
   * SharedLock은 동시에 걸릴 수 있지만, 어떤 자원에 하나라도 걸려 있으면 Exclusive lock은 걸 수 없다.  -> 자원에 동시 읽기 잠금 가능
2. Exclusive Lock (또는 Write lock) : 쓰기 잠금이라고도 한다. 쓰기 락이 걸려 있으면 데이터 항목에 읽기와 쓰기 모두 불가능하다.
   * 어떤 트랜잭션에서 데이터를 변경하고자 할 대 해당 트랜잭션이 완료될 때가지 해당 테이블 혹은 로우를 다른 트랜잭션에서 읽거나 쓰지 못하게 하기 위해 락을 걸고 트랜잭션을 진행
   * exclusive look에 걸리면 shared lock을 걸 수 없다.
   * exclusive look에 걸린 테이블, 레코드등에 다른 트랜잭션이 exclusive look을 걸 수 없다. -> 자원에 동시에 배타 잠금 불가.

<br>

- 논리적 계수기(Logical Count) : 트랜잭션이 발생할 때마다 카운터를 하나씩 증가한다.
- 시스템 클락(System clock) : 트랜잭션이 시스템에 들어올때의 시스템 시각을 부여한다.

<br>

 Lock은 DBMS가 자동으로도 적용하기도 하고 수동으로도 줄 수 있다.
* Lock은 잠금 비용과 동시성비용을 고려해야한다.만약 lock을 걸어야할 페이지가 많다면, 그럴바에 테이블 전체에 lock을 걸어버리는 편이 한번에 처리하니까 잠금 비용에 낮아져 효율적이다.하지만 lock의 범위가 넓어질수록 동시에 접근할 수 없는 자원이 많아지므로 동시성 비용이 높아져 효율이 떨어진다.

<br>

### 2. TimeStamp Ordering, 타임스탬프

1. TimeStamp Ordering : DB 시스템에서 들어오는 트랜잭션 순서대로 (타임스탬프가 작은 트랜잭션) System Clock / Logical Counter 순서를 부여하여 동시성 제어의 기준으로 사용

* non-locking scheme
* 교착상태를 방지 할 수 있으나 RollBack 발생률이 높고 연쇄 복귀를 초래할 수 있다.

<br>

#### 동작 원리

* 트랜잭션이 읽어오거나 수정할 때마다 DBMS가 트랜잭션의 timstamp와 해당 튜플에 마지막으로 read나 write를 수행한 트랜잭션의 timestamp를 비교.
  * 튜플에는 마지막 readTimeStamp와 write timestamp가 기록 되어 있다.

<br>

* read_TS(X) : 항목 X의 읽기 타임스탬프. read(X) (항목 X 읽기) 연산을 성공적으로 수행한 트랜잭션들의 `타임 스탬프중 가장 큰것. `
  * read_TS(X) = TS(T) 이다.
  * T = 항목 X를 성공적으로 읽은 가장 최근 트랜잭션
* write_TS(X) : 항목 X의 쓰기 타임스탬프. write(x) 연산을 성공적으로 수행한 트랜잭션들의 타임스탬프 중 가장 큰 것
  * write_TS(X) = TS(T)
  * T = 항목 X를 성공적으로 쓴 가장 최근의 트랜잭션

<br>

#### 순서

* TS(T)는 새로 시작하려는 트랜잭션의 타임스탬프 값이다.

* 트랜잭션 T가 write_item(X) - 쓰기 연산을 수행하려고 할 경우
  1. read_TS(X) > TS(T) 또는 write_TS(X) > TS(T). 최근의 읽기 연산이나 쓰기 연산이 지금 트랜잭션 시간보다 나중에 일어났다.
     * T를 롤백 시키고( reject) -> 나중에 읽었다고 표시가 되어있으므로, 지금 T가 순서가 이미 지난것
     * ** `lost update, dirty read 발생 방지`**
  2. read_TS(X) > TS(T) 또는 write_TS(X) > TS(T) :  연산이 발생하지 않으면 write_TS(X)를 수행하고 wirte_TS(X)를 TS(T로 설정)
     * 쓰기 연산이 성공했으므로 가장 최근 쓰기 연산을 방금 수행한 트랜잭션으로 설정 
* 트랜잭션 T가 read_item(X)연산을 수행하려는 경우 
  1.  write_TS(X) > TS(T) : 마지막으로 읽기에 성공한 연산이 현재 수행하려는 읽기 연산 보다 나중이라면
     * T를 롤백하고 (reject) -> TS(T)가 읽을 시간이 이미 지나간 순서이므로. 나중에 읽으면 안된다.
     * **`dirty read 발생 방지`**
  2. write_TS(X) <= TS(T) : 마지막 쓰기 연산이 현재 트랜잭션 연산보다 이전이므로 수행 가능
     * read_item(X) 연산을 수행하고 read_TS(X)를 TS(T와) 현재의 read_TS(X) 값중 큰 값으로 설정한다
       * read_TS(X)는 가장 큰값이여야 하므로 
     * Casecade rollback이 발생할 수 있다.
       * 해당 순번 TS(T)까지 read_TS, write_TS가 올떄까지 대기. strict timestap)

<br>

* 타임스탬프 방식은 Optimistic 동시성 제어 schme이다 

> 여기서 **낙천적**이란, 트랜잭션간의 충돌이 발생하지 않을 것이라고 예상하는 것. 반대로, **비관적**이란 충돌을 걱정하여 락을 사용하여 순서를 강제하는 것이다.

- 락을 사용하지 않기 때문에, **데드락의** 걱정이 없다. 다만, starvation이 발생할 수 있다. TO가 상대적으로 큰 트랜잭션들은 계속해서 기다려야 할 수 있다.
- 트랜잭션 수행 **전**에 직렬성을 결정한다. 즉, 트랜잭션 수행 전에 미리 트랜잭션들의 serial한 순서를 만들어 두고, 이를 강제로 지키게 한다.

<br>

## 3. 낙관적 검증 (Optimistic Validation)

사용자들이 같은 데이터를 동시에 수정하지 않을것이라고 가정하는 낙관적 동시성 제어 기법에 기반함.

트랜잭션 수행 동안은 어떠한 검사도 하지 않고, 트랜잭션 종료 시에 일괄적으로 검사하는 기법.

- 낙관적 검증 기법의 특징

1. 트랜잭션 수행 동안 그 트랜잭션을 위해 유지되는 데이터 항목들의 지역 사본에 대해서만 갱신이 이루어진다.
2. 트랜잭션 종료 시에 동시성을 위한 트랜잭션 직렬화가 검증되면 일시에 DB로 반영한다.

<br>

* 낙관전 검증 기법의 구성도

<img src="https://blog.kakaocdn.net/dn/chjF6V/btrLmyp0IH9/gK9bCRcnWvvUqPkpacKvp0/img.png" width =850 height = 250>

 [처리단계] 판독 → 확인 → 실행(기록/철회)
\- 판독(Read phase, R) : 트랜잭션의 모든 갱신은 사본에 대해서만 수행, 실제 DB에 대해서 수행하지 않음
\- 확인(Validation phase, V) : 판독단계에서 사본에 반영된 트랜잭션의 실행결과를 DB 반영전 직렬 가능성의 위반여부를 확인
\- 기록(Write phase, W) : 확인 통과시 실행결과를 DB에 반영, 실패시 결과 취소후 트랜잭션 복귀

- 읽기 전용(Read Only) 트랜잭션이 대부분일 때 병행 제어를 하지 않아도 문제가 없는 점을 이용
- 검증과 확인하는 과정이 필요하여 검증 기법, **확인 기법**이라고도 부른다.
- 병행 수행 하고자 하는 대부분의 트랜잭션이 판독 전용 트랜잭션일 경우,
  트랜잭션 간의 충돌률이 매우 낮아서 병행 제어 기법을 사용하지 않고 실행되어도 이 중의 많은 트랜잭션은 시스템의 상태를 일관성 있게 유지한다는 점을 이용한 기법이다.



<br>

## 4. 2단계 잠금 - 2 Phase Locking 

모든 트랜잭션들이 lock과 unlock 연산을 확장 단계와 수축 단계로 구분하여 수행.

트랜잭션 도중에 락을 걸어서 동일한 데이터에 동시에 접근하려는 트랜잭션을 차단하여 직렬화를 보장하는 `DBMS`의 동시 제어 방법이다.

- 비관적인, pessimistic한 CC 스킴이다. lock을 잡는 growing phase 이후, lock을 해제하는 shrinking phase에 진입한다.
- 한번 락을 해제하면, 더 이상 락을 잡을 수 없다.
- 수행전 직렬성을 결정하는 타임스탬프 오더 방식과는 달리 **runtime**에 serializability(직렬성)를 결정한다.

- 이 잠금 프로토콜은 트랜잭션의 실행 단계를 세 단계로 나눈다.
  - 첫 번째 단계에서는 트랜잭션이 실행되기 시작할 때 필요한 락에 대한 권한을 요청한다.
  - 두 번째 부분은 트랜잭션이 모든 락 권한을 얻는 부분이다. 트랜잭션이 첫 번째 락을 해제할 때 세번째 단계가 실행된다.
  - 이 세 번째 단계에서는 트랜잭션이 새로운 락을 요청할 수 없으며, 대신 획득한 락을 해제할 수 만 있다.
- 따라서 `2PL`에서는 각 트랜잭션이 두 단계로 락 획득 또는 해제 요청을 할 수 있다.
  - **Growing Phase**: 이 단계에서는 오직 잠금을 획득할 수 있고, 해제할 수는 없다.
  - **Shrinking Phase**: 이 단계에서는 트랜잭션이 잠금을 해제할 수는 있지만 새로운 잠금을 획득하지는 못한다.
- `2PL` 프로토콜 방식은 직렬화는 보장하지만, 교착 상태가 발생하지 않도록 보장하지 못한다는 특징이 있다.

<br>

## 5. 다중버전 병행제어 기법 (MVCC)

트랜잭션이 한 데이터 아이템을 접근하려 할 때, 

그 트랜잭션의 타임스탬프와 접근하려는 데이터 아이템의 여러 버전의 타임스탬프를 비교하여, 

현재 실행하고 있는 스케줄의 직렬가능성이 보장되는 적절한 버전을 선택하여 접근하도록 하는 기법이다.

* **하나의논리적인** 대상에 대해서 **여러개의 물리적**인 버젼을 유지하고 있는 기법.
* 하나의 데이터 아이템에 대해 여러 버전의 값 유지한다.
* 하나의 데이터에 여러 버전의 데이터가 존재하게 되고. 사용자는 마지막 버전의 데이터를 읽게 된다. 

<br>

MVCC 모델에서 데이터에 접근하는 각 유저는 접근한 시간 데이터베이스의 **snapshot**를 읽는다. 

이 snapshot 데이터에 대한 변경이 완료 될 때까지 만들어진 변경사항은 다른 데이터베이스 사용자가 볼 수 없다(트랜잭션이 commit 될때까지).

이제 사용자가 데이터를 업데이트 하면, 이전의 데이터를 덮었는게 아니라 새로운 버전의 데이터를 만든다. 

대신 이전 버전의 데이터와 비교해서 변경된 내용을 기록한다. 

이렇게 해서 하나의 데이터에 대해서 여러 버전의 데이터가 존재하게 된다. 유저는 물론 제일 마지막 버전의 데이터를 읽게 된다.

<br>

MVCC 모델은 하나의 데이터에 대한 여러 버전의 데이터를 허용하기 때문에 데이터 버전의 충돌이 발생할 수 있다. 충돌은 애플리케이션 영역에서 해결해야 한다.



<br>

- 다중버전 병행 제어 기법의 특징

1. 판독 요청을 거절하지도, 대기하지도 않음
2. 기록보다 판독 연산이 주류를 이루는 데이터 베이스 시스템에 큰 이점
3. 데이터 아이템을 판독할 때마다 중복되는 디스크 접근
4. 트랜잭션간의 충돌문제는 대기가 아니라 복귀처리 함으로 연쇄 복귀초래 발생 가능성
5. **잠금을 필요로 하지 않기 때문에** 일반적인 RDBMS보다 매우 빠르게 작동
6. 사용하지 않는 데이터가 계속 쌓이게 되므로 데이터를 정리하는 시스템이 필요
7. 데이터 버전이 충돌하면 애플리케이션 영역에서 이러한 문제를 해결해야 함

<br>

- 다중 버전 병행 기법 제어 방법

사용자가 데이터를 업데이트하면 이전의 데이터를 덮어 씌우는게 아니라 새로운 버전의 데이터를 UNDO 영역에 생성한다. 

<br>

MVCC 원리

1. 이전 데이터 값 저장
2. 데이터 변경시 마다 Undo 영역에 저장

<br>

MVCC 버전 관리 방식

1. 타임스탬프 - 각 데이터 항목 버전 필드 값 보유

2. Undo 영역 - 지워진 데이터는 Undo segment 존재 

   

- MVTO: Timestamp 기법을 적용해서 Serialization order 정해주기
- MVOCC: 낙천적 동시성 제어 기법을 이용해서 order 결정
- MV2PL: 2 Phase Locking 기법을 이용해서 order 결정



<img src="https://blog.kakaocdn.net/dn/cSfBbx/btrLhgScIzV/vXEGmCFoGNHwgaslQeaXU0/img.png" width=850 height=400>

# 참조

https://www.javatpoint.com/dbms-concurrency-control

https://narakit.tistory.com/157

https://goodgid.github.io/Concurrency-Control/

https://jeong-pro.tistory.com/94

https://tino1999.tistory.com/20

https://goodgid.github.io/Concurrency-Control/

https://medium.com/myinterest/timestamp-ordering-%EA%B8%B0%EB%B2%95-c66b57bae978



https://dongwooklee96.github.io/post/2021/04/07/two-phase-lock2pl%EC%9D%B4%EB%9E%80-draft.html







----

---

---

---



# 멀티스레드 환경에서의 자바 동시성 제어 기법

멀티스레드 프로세스 환경에서의 자바 동시성 기어 제법 들어가기 전에 

프로세스와 쓰레드, 멀티쓰레드부터 뭔지 알아보자.



### 프로세스

* 실행중인 프로그램(program)이 메모리에 적재되어 실행되는것 

* 프로세스 내에는 코드 영역, 데이터 영역, 스택 영역, 힙 영역이 존재한다.



* Code 영역
  * 실행한 프로그램의 코드가 저장되는 메모리 영역 (프로그램 명령어,  소스 코드 자체)

* Data 영역
  * 프로그램의 전역 변수와 static 변수가 저장되는 메모리 영역( 전역변수, static 변수. 정적 )

* Heap 영역
  * 프로그래머가 직접 공간을 할당(malloc)/해제(free) 하는 메모리 영역(new() 등   동적)

* Stack 영역
  * 함수 호출 시 생성되는 지역 변수와 매개 변수가 저장되는 임시 메모리 영역 (지역변수, 매개변수, 함수, 리턴값 동적)

- Heap 영역에는 주로 긴 생명주기를 가지는 데이터들이 저장된다. (대부분의 오브젝트는 크기가 크고, 서로 다른 코드블럭에서 공유되는 경우가 많다)
- 애플리케이션의 모든 메모리 중 stack 에 있는 데이터를 제외한 부분이라고 보면 된다.
- 모든 Object 타입(Integer, String, ArrayList, ...)은 heap 영역에 생성된다.
- 몇개의 스레드가 존재하든 상관없이 단 하나의 heap 영역만 존재한다.
- Heap 영역에 있는 오브젝트들을 가리키는 레퍼런스 변수가 stack 에 올라가게 된다.



### 스레드 (쓰레드)

* 프로세스 내에서 실행되는 여러 흐름의 단위.

* 한 프로세스 내에 여러 스레드가 존재할 수 있다.

* 스레드는 Stack영역을 제외한 다른 영역을 공유한다
  * 같은 프로세스 안에 있는 여러 스레드들은 같은 힙 공간을 유지한다.



### 멀티 쓰레드 (멀티 스레드)

* 멀티 스레드란 한 프로세스(실행중인 하나의 프로그램) 를 여러개의 쓰레드로 구성하고 각 쓰레드로 하여금 한 작업(task)를 처리하도록 하는 것
* 한 프로세스에서 여러 작업을 병렬로 처리하는것
* 멀티 쓰레드에서는 한 프로세스 내에 여러개의 쓰레드가 있고, 각 쓰레드들은 Stack 영역을 제외한 Data, Code, Heap 영역을 공유한다.

<img src="https://blog.kakaocdn.net/dn/vp6Ax/btrI9lsWxkx/iSXHMM0MoEjBRJefidvuLK/img.png">



## 동시성 제어?

JVM에서는 한 프로세스 내에서 여러 스레드를 가질 수 있다.

스레드들은  Stack 영역을 제외한 Data, Code, Heap 영역을 공유하는데, 

Heap 영역에 참조 변수들이 load되어 서로 공유된다.

이 때 Heap 영역에 있는 변수들을 여러 스레드가 동시에 접근하게 되어 계산했을 시, 예상하는 결과와 다른 문제가 생길 수 있다.

이를 동시에 접근하여 문제가 생기는 것들을 동시성 문제(Concurrency ) 라고 하며,

동시성 문제를  제어하는것을 동시성 제어 (Concurrency Control) 이라고 한다.  



이 동시성 문제가 생길 수 있는 예제로 여러 사용자가 게시물을 조회했을 때, 조회수가 올바르지 않게 조회되는 것을 확인해보자



### 동시성 문제 예제, 조회수 

100명의 사용자가 1번씩 게시물을 조회했다고 하자.

1번 조회했을때 조회수는 1번씩 올라간다 하고, 단순 계산 했을 시에 조회수는 100이 되어야 한다. 

```java
public class ViewCounter {
    private int count = 0;

    public void view() {
        this.count += 1;
        System.out.println("currentThreadName : " + Thread.currentThread().getName()
                + ", view Count : " + this.getViewCount());
    }

    public int getViewCount() {
        return this.count;
    }

    public static void main(String[] args) {
        int threadCount = 100;

        ViewCounter viewCounter = new ViewCounter();

        for (int i = 0; i < threadCount; i++) {
            new Thread(viewCounter::view).start();
        }
        
        System.out.println(viewCounter.getViewCount());
    }

}
```



* 결과

<img src ="https://blog.kakaocdn.net/dn/zwQNk/btrLjinMTVn/KuKAqDoW6GycgGKUQ1vGW0/img.png" width=950, height=400>

* 결과로 100번이 아닌, 99번의 조회수가 나왔습니다.

왜 그러냐 하면,  조회수를 증가시킬 때, 조회수 변수인 count를 동시에 여러 스레드가 접근하여 

값이 바뀌기 전의 값을 증가시키기 때문입니다. 

```
// 쓰레드가 4개 있다고 가정.
// 모든 쓰레드는 동시에(완벽히 동시가 아닌, 아주 빠르게실행하면 동시에 접근한것처럼 보이므로 란 뜻)
1번 쓰레드 : count 0인 값을 가져와서 0 -> 1로 증가시킴
2번 쓰레드 : count 1인 값을 가져와서 1 -> 2로 증가시킴
3번 쓰레드 : count 1인 값을 가져와서 1 -> 2로 증가시킴 // 2번쓰레드와 겹침 이슈 발생 
4번 쓰레드 : count 2인 값을 가져와서 2 -> 3로 증가시킴 
```

예제로 적은 값과 쓰레드번호는, 상황에 따라 매번 다르게 변할 수 있다.

즉, 어떤 쓰레드가 접근했을 때 값을 이미 바꿨는데, 다른 쓰레드가 그 값을 바꾸기 직전을 참조해서 생기는 이슈 인것이다.



그렇다면 Java에서는 이 이슈를 어떻게 해결할까?

- 크게 3가지가 있다.



1. Synchronized 키워드
2. volataile 키워드
3. java.util.Concurrent  패키지 의 Atomic 클래스들과 Concurrent 컬렉션들(List, Map 등)



## 1. Synchonized 키워드

자바에서는 `synchronized` 키워드를 이용해 메서드, 내부(변수)에 lock을 걸어 동기화 할 수 있다.



lock을 자바에서는 2가지를 이용할 수 있습니다.

1. ReentrantLock 클래스 사용 - 명시적 동기화, 명시적 lock
   * 동기화된 메소드와 문장을 사용하여 액세스 할 수 있는 암시적인 모니터 잠금 기능과 같은 기본적인 동작과 의미를 가진 reentran
   * 상호 간의 상호 배제된 상호 배제 잠금 기능
   * 잠금의 시작점과 끝 점을 수동적으로 설정이 가능하다.
2. synchronized 키워드 사용 - 암시적 동기화, 암시적 lock
   * 메서드나, 메서드 내의 블럭 안에 syncronized 키워드를 사용하여 잠금 기능을 함
   * 변수에 lock을 걸기 위해선 해당 변수는 객체여야만 한다. int, long과 같은 기본형 타입에는 lock을 걸 수 없다.
   * 구간 전체를 lock을 걸기 때문에, 시작점과 끝점을 명시할 수 없다. 



### 1. ReentrantLock 클래스 사용 예제

```java
public class ViewCounter {
    private int count = 0;
    private Lock lock = new ReentrantLock();

    public int view() {
        return count++;
    }

    public Lock getLock() {
        return lock;
    }

    public static void main(String[] args) {
        ViewCounter viewCounter = new ViewCounter();
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                viewCounter.getLock().lock();
                System.out.println(viewCounter.view());
                viewCounter.getLock().unlock();
            }).start();
        }
    }
}
```

*  Lock의 범위를 메서드 내부에서 한정하기 어렵거나, 동시에 여러 Lock을 사용하고 싶을 때 사용. 
* 직접적으로 Lock 객체를 생성하여 사용. 
* lock() 메서드를 사용할 경우 다른 스레드가 해당 lock() 메서드 시작점에 접근하지 못하고 대기. 
  * Lock 객체의 unlock() 메서드를 실행해야 다른 메서드가 lock을 획득할 수 있다



### 2. synchronized 키워드 사용 예제

1. 메서드 전체 동기화 - 이 때 범위는 메서드 전체이고, 변수는 primitive 같은 기본형 타입이여도 된다.

```java
public class ViewCounter {

    private int count = 0;

    public synchronized void view() { // 메서드 Lock
        this.count += 1;
        System.out.println("currentThreadName : " + Thread.currentThread().getName()
                + ", view Count : " + this.count);
    }

}
```

2. 메서드 내 변수 동기화 - 이 때, 변수는 객체(오브젝트) 타입이여야 한다. 

```java
public class ViewCounter {

    private Integer count = 0; // 변수는 객체여야 한다.

    public void view2() { //  변수 Lock
        synchronized (this.count) { // 변수는 객체여야 한다.
            this.count += 1;
            System.out.println("currentThreadName : " + Thread.currentThread().getName()
                    + ", view Count : " + this.count;
        }
    }
}
```



- synchronized을 적용하게 되면 하나의 스레드가 해당 메서드를 실행하고 있을 때 다른 메서드가 해당 메서드를 실행하지 못하고 대기하게 된다.
  - 즉 한 번에 하나의 스레드만 접근할 수 있게 된다. 
    - 멀티 스레드가 동시에 접근해서 생긴 문제인데,  여러 스레드가 동시에 접근할 수 없게 만들어 동시성 이슈를 막을 수 있음
- 단점 : 한 번에 하나의 스레드만 메서드를 실행시킬 수 있으므로 병렬성은 매우 낮아짐
  - 성능 이슈가 생길 수 있다.
- 문제가 된 view 메서드에 synchronized 키워드를 붙이면 암시적 락이 걸린다.

lock은 메서드, 변수에 각각 걸 수 있다.

- 메서드에 lock을 걸 경우 해당 메서드에 진입하는 스레드는 단 하나만 가능
- 변수에 lock을 걸 경우 해당 변수를 단 하나의 스레드만 참조할 수 있다.



### 언제 synchoronized 식별자 사용하는것이 좋을까?

1. 하나의 객체에 여러개의 스레드가 접근해서 처리하고자 할때
2. static으로 선언한 객체에 여러 스레드가 동시에 사용할때

- 변수를 공유할땐 static 변수사용, 하지만 static변수는 객체를 생성할 수 없다. 
- Atomic은 기존변수에 동시성 제어를 하는것이 아니라 AtomicType객체를 생성해서 해당 AtomicType객체에 대한 동시성을 제어하는 것이다.
- 새롭게 생성할 수 없는 static변수나 기존 변수에 동시성을 제어하기 위해선 synchoronized 키워드를 붙여서 사용한다.



## 2. volatile 키워드

- Multi Thread환경에서 `Thread가 변수 값을 읽어올 때` 각각의 CPU Cache에 저장된 값이 다르기 때문에 `변수 값 불일치 문제`가 발생한다. 
- `volatile` 변수를 사용하고 있지 않는 MultiThread 어플리케이션에서는 Task를 수행하는 동안 `성능 향상`을 위해 Main Memory에서 읽은 변수 값을 CPU Cache에 저장한다. 
- `volatile` keyword는 Java 변수를 CPU cache가 아닌 Main Memory에 저장하겠다라는 것을 명시하는 것.
  - 즉, volatile 키워드는 CPU cache의 사용을 막는 것
  - 매번 변수의 값을 Read할 때마다 CPU cache에 저장된 값이 아닌 Main Memory에서 읽는 것.
  - 또한 변수의 값을 Write할 때마다 Main Memory에 까지 작성하는 것.
- 매 번 메모리에 접근해서 실제 값을 읽어오도록 설정해서 캐시 사용으로 인한 **데이터 불일치**를 막는다

* 다만, 성능을 위한 CPU 캐시를 비활성화하고 매번 메인 메모리에 접근하기 때문에 어느정도의 성능 저하가 필연적으로 발생한다.



### 언제 volatile을 사용하는 것이 적합할까?

* Multi Thread 환경에서 하나의 Thread만 read & write하고 나머지 Thread가 read하는 상황에서 `가장 최신의 값을 보장`

* 이 경우 read만 하는 스레드는 CPU 캐시를 사용하고 다른 스레드가 write한 값을 즉각적으로 확인하지 못한다.



```java
public class ViewCounter {
  private volatile int counter = 0;
}
```



## 3. Atomic 클래스

* Java 1.5 버전 이상 부터는 java.util.concurrent 라는 동시성 제어 유틸리티 패키지를 제공
  * java.util.concurrent : 동시성 제어 유틸 & 동시성 제어가 적용된 Collections 클래스들
  * java.util.concurrent.atomic : Boolean, Integer, Long 및 참조타입 의 동시성 제어 클래스들
  * java.util.concurrent.locks : 읽기, 쓰기관련 동시성 제어 유틸
* Atomic 클래스는 *CAS(compare-and-swap)를 이용하여 동시성을 보장
  * CAS(compare-and-swap) 
    * 특정 메모리위치의 값이 주어진 값과 동일하다면 해당 메모리 주소를 새로운 값으로 대체. 
    * 이 연산은 atomic이기 때문에 새로운 값이 최신의 정보임을 보장
    * 만약 값 비교 와중에 다른 스레드에서 그 값이 업데이트 되어 버리면 쓰기는 실패
* AtomicInteger는 synchronized 보다 적은 비용으로 동시성을 보장

```java
public class ViewCounter {

    private AtomicInteger count = new AtomicInteger();

    public int view() {
        return count.getAndIncrement(); // 값을 먼저 리턴하고 후에 증가. count++
//        return count.incrementAndGet(); // 값을 먼저 증가하고 후에 리턴. ++count
    }
    
}

```

* getAndIncrement(); // 값을 먼저 리턴하고 후에 증가. count++
* incrementAndGet(); // 값을 먼저 증가하고 후에 리턴. ++count



## 이외의 불변 객체

## 불변 객체 (Immutable Instance)

* 스레드 안전한 프로그래밍을 하는 방법중 효과적인 방법은 불변 객체(Immutable)를 만드는 것. 
* String 객체처럼 **한번 만들면 그 상태가 변하지 않는 객체**를 불변객체. 
  * 불변 객체는 락을 걸 필요가 없다. 

* 불변 객체는 생성자로 모든 상태 값을 생성할 때 세팅하고,  객체의 상태를 변화시킬 수 있는 부분을 모두 제거해야 합니다.
*  가장 간단한 방법은 세터(setter)를 만들지 않는 것
* 또한 내부 상태가 변하지 않도록 모든 변수를 final로 선언





## 참조 

https://devwithpug.github.io/java/java-thread-safe/

https://devkingdom.tistory.com/276

https://nesoy.github.io/articles/2018-06/Java-volatile

https://docs.oracle.com/javase/8/docs/api/java/util/Collections.html#synchronizedList-java.util.List-

https://jwkim96.tistory.com/202

https://deveric.tistory.com/104



---



# 스프링 동시성 제어 기법

https://velog.io/@mnsukoo/Spring-Boot-%EB%8F%99%EC%8B%9C%EC%84%B1-%EB%AC%B8%EC%A0%9C-ThreadLocal

# @Transactional 동시성 제어 기법

아마 Transactional 애노테이션은 기본적으로 달려있을거같은데요.

@Transactional 애노테이션에 isolate 설정을 좀 강하게 주면 해결할 수 있지 않을까 하네요.



https://ojava.tistory.com/207

# JPA 동시성 제어 기법

https://velog.io/@recordsbeat/JPA%EC%97%90%EC%84%9C-Write-Skew-%EB%B0%A9%EC%A7%80%ED%95%98%EA%B8%B0-locking-%EC%A0%84%EB%9E%B5







# JVM 동시성 제어기법

https://kotlinworld.com/38









































































1. 어플리케이션 레벨
2. 데이터베이스 Lock
3. Redis Distrubuted Lock 





## 작업환경 세팅
### docker 설치

brew install docker 

brew link docker

docker version

### mysql 설치 및 실행

docker pull mysqldocker run -d -p 3306:3306 -e MYSQL_ROOT_PASSWORD=1234 --name mysql mysql 

docker ps

---

docker: no matching manifest for linux/arm64/v8 in the manifest list entries. 오류가 발생하시는분은**docker pull --platform linux/x86_64 mysql**



### my sql 데이터베이스 생성

docker exec -it mysql bash

mysql -u root -p

create database stock_example;

use stock_example;




## MySQL VS Redis 

Mysql

이미 Mysql 을 사용하고 있다면 별도의 비용없이 사용가능하다.어느정도의 트래픽까지는 문제없이 활용이 가능하다.Redis 보다는 성능이 좋지않다.



Redis

활용중인 Redis 가 없다면 별도의 구축비용과 인프라 관리비용이 발생한다.

Mysql 보다 성능이 좋다.





















# 참조

https://www.javatpoint.com/dbms-concurrency-control

https://narakit.tistory.com/157

https://goodgid.github.io/Concurrency-Control/

https://jeong-pro.tistory.com/94

https://tino1999.tistory.com/20

https://goodgid.github.io/Concurrency-Control/

https://medium.com/myinterest/timestamp-ordering-%EA%B8%B0%EB%B2%95-c66b57bae978



https://dongwooklee96.github.io/post/2021/04/07/two-phase-lock2pl%EC%9D%B4%EB%9E%80-draft.html