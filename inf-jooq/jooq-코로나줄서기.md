# JOOQ 코로나 줄서기



* https://www.jooq.org/
* https://www.jooq.org/notes/?version=3.18
* https://github.com/etiennestuder/gradle-jooq-plugin
* 스프링 부트 2.7.5 예제 : https://github.com/etiennestuder/gradle-jooq-plugin/tree/main/example/configure_jooq_version_from_spring_boot

 

JOOQ



DB 테이블로 자바 클래스를 만들어준다.

DB schema -> Java class 생성 도구

*  ORM framework 가 아님
*  "jOOQ is not a replacement for JPA"
*  SQL 이 잘 어울리는 곳엔, Jooq 가 잘 맞아요
*  Object Persistence 가 잘 어울리는 곳엔, JPA 가 잘 맞아요
*  Jooq says: "Jooq + JPA
  * JPA 대체제가 아님



Jooq, 좋은 이유

*  Jooq 방식으로 사용한다면, 엔티티를 작성할 필요가 없다
*  로그가 보기 예쁨
*  query 로그 안에 binding parameter 가 함께 포함됨
*  결과 로그가 예쁨



## jOOQ의 불편한 점들

- **ORM 기술이 아닌 특징**:
  - Spring Data JPA와 결합이 잘 안 맞음.
  - JPA와는 정반대의 매커니즘을 가짐: jOOQ 클래스가 엔티티 클래스를 방해.
  - JPA 기술이 아니어서 오는 문제점들.

- **트랜잭션 연동의 어려움**:
  - Spring Data JPA 트랜잭션 연동이 힘듬: jOOQ는 자체 트랜잭션 기술을 가짐, 이로 인해 jOOQ 코드가 서비스에 노출됨.

- **하이버네이트와의 연동 제약**:
  - 하이버네이트의 auto-ddl 사용이 불가능.
  - 데이터베이스 스키마는 이미 준비되어 있어야 함.

- **스프링과의 연동 어려움**:
  - 스프링과의 직접적인 연동이 부족.
  - 스프링의 Pageable 정보로부터 jOOQ 쿼리를 조립하는 것이 까다로움.

- **Gradle 플러그인 사용시의 문제**:
  - DB 정보가 `build.gradle`에 침투.
    - DB 기본 정보, 로깅, 타입 변환(enum 등) 정보, 패키지 정보, DB dialect, JDBC 드라이버 정보 등이 포함됨.

- **문서화의 어려움**:
  - 매뉴얼이 꽤 어려움.
  - 참고할 수 있는 레퍼런스가 부족.