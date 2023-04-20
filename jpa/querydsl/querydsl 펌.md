

# 동적 쿼리 - BooleanExpression

QueryDSL은 SQL자체를 자바 코드로 작성하기 때문에 TypeSafe하고 컴파일 시점에 오류를 발견할 수 있다는 장점이 존재한다.

더불어서 QueryDSL의 가장 큰 장점 중 하나는 **"동적 쿼리 생성의 편리함"**이다

 

QueryDSL에서 where절에 동적 쿼리를 생성하기 위해서는 "BooleanExpression"을 활용하면 된다

 

## **BooleanExpression**

BooleanExpression을 return하는 "동적 쿼리 전용 메소드"를 만들면 굉장히 편리하게 특정 조건에 따른 동적 쿼리를 작성할 수 있다

```java
Integer ageUpper = 25;
String userNameContains = "nus1";
 
List<Member> fetch = query.selectFrom(member)
        .where(member.age.goe(ageUpper).and(member.username.contains(userNameContains)))
        .orderBy(member.age.asc())
        .fetch();
 
for (Member member : fetch) {
    System.out.println(member);
}
```

현재 이 쿼리는 where절에 그대로 각 값의 조건을 넣은 쿼리이다

여기서 만약에 age/username중 특정 조건만 넣고 싶다면 직접 where절을 변형시켜야 한다

 

하지만 BooleanExpression을 활용하면 직접 where절을 변형시키지 않아도 BooleanExpression의 동작 원리에 따라서 알아서 where절을 만들어준다

- BooleanExpression에서 "null"을 반환하게 된다면 where절에서 해당 조건은 제거된다

```java
// BooleanExpression
private BooleanExpression goeAge(Integer age) {
    if (age == null) {
        return null;
    }
 
    return member.age.goe(age);
}
 
private BooleanExpression containsUsername(String username) {
    if (username == null) {
        return null;
    }
 
    return member.username.contains(username);
}
 
// Query
Integer ageUpper = 25;
String userNameContains = "nus1";
 
List<Member> fetch = query.selectFrom(member)
        .where(
                goeAge(ageUpper),
                containsUsername(userNameContains)
        )
        .orderBy(member.age.asc())
        .fetch();
 
for (Member member : fetch) {
    System.out.println(member);
}
```



![img](https://blog.kakaocdn.net/dn/clGK1f/btrKHVmD1uS/N2XNDu1gyK6opZLz3OMMHk/img.png)



BooleanExpression를 사용하는 "where"절에서 콤마(,)의 의미는 각 조건을 연결하는 and()와 동일하다

 

여기서 만약 ageUpper를 null로 초기화한다면 어떤 쿼리가 날라갈지 살펴보자

```java
// BooleanExpression
private BooleanExpression goeAge(Integer age) {
    if (age == null) {
        return null;
    }
 
    return member.age.goe(age);
}
 
private BooleanExpression containsUsername(String username) {
    if (username == null) {
        return null;
    }
 
    return member.username.contains(username);
}
 
// Query
Integer ageUpper = null; // age null
String userNameContains = "nus1";
 
List<Member> fetch = query.selectFrom(member)
        .where(
                goeAge(ageUpper),
                containsUsername(userNameContains)
        )
        .orderBy(member.age.asc())
        .fetch();
 
for (Member member : fetch) {
    System.out.println(member);
}
```



![img](https://blog.kakaocdn.net/dn/P58KM/btrKMnoZrkR/e0BKEbKyifhAwkoY39Ik0k/img.png)



BooleanExpression에 따라서 동적으로 where절의 쿼리가 변형됨을 확인할 수 있다

 

> BooleanExpression은 특정 조건에 대해서 문장으로 나열하지 않고 **"의미있는 메소드"로 동적 조건을 결정**하기 때문에 가독성도 더 뛰어나고 편리하다
>
> 물론 BooleanBuilder를 통해서 BooleanBuilder 내부적으로 모든 조건을 판단하고 where절에 builder하나만 넣어도 되긴하지만 가독성이 떨어지고 어떤 쿼리가 나갈지 예상하기 힘들기 때문에 BooleanBuilder보다는 "BooleanExpression"을 사용하는 것을 권장한다





# **Join & SubQuery**

## **QueryDSL Join**

QueryDSL에서는 JPQL에서 지원하는 [InnerJoin, LeftOuterJoin]은 당연하게 사용할 수 있고 더해서 **[RightOuterJoin]**도 사용할 수 있다

 

추가적으로 on절과 더불어서 성능 최적화를 위한 FetchJoin도 활용할 수 있다

 

### **InnerJoin**

> *첫번째 파라미터(Root Entity에 대한 조인 대상)*
> *두번째 파라미터(조인 대상의 Alias)*

```
List<Member> fetch = query.selectFrom(member)
        .innerJoin(member.team, team)
        .where(member.age.goe(25).and(team.name.eq("Team-A")))
        .fetch();
        
for (Member member : fetch) {
    System.out.println("Member = " + member);
    System.out.println("\tTeam = " + member.getTeam()); // Team Proxy에 대한 초기화 과정 수행
}
```

현재 Member:Team은 1:N관계이고 위의 query를 통해서 [나이 ≥ 25 && 팀이름 = "Team-A"]인 멤버를 찾으려고 한다

그렇게 찾은 Member에 대해서 Member의 Team정보도 확인해보려고 하는데 여기서 예상할 수 있는 가장 대표적인 문제는 **N+1**이다



![img](https://blog.kakaocdn.net/dn/cbMNdh/btrKLab0iGv/SrSCyiHAkFkaaf9fBSvMu0/img.png)![img](https://blog.kakaocdn.net/dn/5Faua/btrKMFW5leO/TlVdvXBV1y0EQE3Qesq0a1/img.png)

왼쪽 = 원래 쿼리 / 오른쪽 = Team Proxy에 대해서 조회하기 위한 추가 쿼리



Member를 가져올때 Member의 Team은 Proxy로 가져왔기 때문에 당연히 Team에 대해서 접근할 때 프록시 초기화 과정이 이루어져서 Team에 대해서 실제로 DB에서 조회하게 된다

#### >> 이러한 N+1문제를 해결하기 위해서 **fetchJoin**을 활용해야 한다

```
List<Member> fetch = query.selectFrom(member)
        .innerJoin(member.team, team).fetchJoin()
        .where(member.age.goe(25).and(team.name.eq("Team-A")))
        .fetch();

for (Member member : fetch) {
    System.out.println("Member = " + member);
    System.out.println("\tTeam = " + member.getTeam());
}
```

QueryDSL에서 fetchJoin을 사용하기 위해서는 그냥 xxxJoin()에 fetchJoin()을 chaining시켜주면 된다



![img](https://blog.kakaocdn.net/dn/x2bOK/btrKHbiS6LY/qfHUJ7Pn1oKekR8RJt7Bak/img.png)



fetchJoin덕분에 Member와 LAZY로 매핑된 Team도 즉시 가져옴을 확인할 수 있다

 

### **ThetaJoin(CrossJoin)**

innerJoin()이나 left/right/fullJoin()없이 from절에 여러 엔티티를 작성하게 되면 해당 엔티티끼리 "카티션 곱"이 발생하는 **CrossJoin**을 수행하게 된다

```
List<Tuple> fetch = query.select(member, team)
        .from(member, team)
        .fetch();
```



![img](https://blog.kakaocdn.net/dn/GVLOU/btrKGoXk2W6/biuTgpvjp0IIvJQQPCpw5K/img.png)



멤버 13명 × 팀 5명의 모든 경우의 수인 65가지가 결과로 출력됨을 확인할 수 있다

 

### **leftJoin (Left Outer Join)**

```
List<Member> fetch = query.selectFrom(member)
        .leftJoin(member.team, team)
        .fetch();

for (Member member : fetch) {
    System.out.println("Member = " + member);
    System.out.println("\tTeam = " + member.getTeam()); // Team Proxy에 대한 초기화 과정 수행
}
```

**Team이 없는 Member도 조회**하기 위해서 Member - Team간에 Left Outer Join을 구현한 쿼리이다



![img](https://blog.kakaocdn.net/dn/ofVbA/btrKGQsOtXx/O5LYsTialy92Aagqka4dx1/img.png)![img](https://blog.kakaocdn.net/dn/tBLwe/btrKHYjlq1X/YK186lKlp370ja8eNDtiEk/img.png)



마찬가지로 Member를 가져올때 **Member와 연관된 Team은 LAZY Loading전략**을 따르기 때문에 **"Team의 프록시 객체"**로 가져오게 된다

그리고 실질적으로 **"member.getTeam()"에 의해서 Team에 직접적으로 접근할 때 "Team 프록시에 대한 초기화 과정"**이 이루어지면서 Team에 대한 Query가 추가적으로 나가게 된다

#### >> Left Outer Join에도 fetchJoin()을 적용해보자

```
List<Member> fetch = query.selectFrom(member)
        .leftJoin(member.team, team).fetchJoin()
        .fetch();

for (Member member : fetch) {
    System.out.println("Member = " + member);
    System.out.println("\tTeam = " + member.getTeam());
}
```



![img](https://blog.kakaocdn.net/dn/cKFTH2/btrKGQsOvUm/sTN7awV4bWbzNmkCqp7IAk/img.png)![img](https://blog.kakaocdn.net/dn/cURNyv/btrKH0akWOI/xbEHoAobE7Lrd8V0rkdK51/img.png)



 

### **rightJoin (Right Outer Join)**

QueryDSL에서는 JPQL에서는 지원하지 않는 **[Right Outer Join]**까지 지원해준다

> 이번에는 Team을 기준으로 Team에 소속된 멤버들을 구하는 Member - Team ==> Right Outer Join

```
List<Member> fetch = query.selectFrom(member)
        .rightJoin(member.team, team).fetchJoin()
        .orderBy(member.id.asc())
        .fetch();

for (Member member : fetch) {
    System.out.println("Member = " + member);
    System.out.println("\tTeam = " + member.getTeam());
}
```



![img](https://blog.kakaocdn.net/dn/nefue/btrKLfq20eh/3bD5Mmme9cgHKm6XlRm3Fk/img.png)![img](https://blog.kakaocdn.net/dn/LVGYM/btrKJ4pJQlS/8d2kQwDwayquPr6hOqZnyk/img.png)



- Team이 없는 [2, 10, 12]Member들은 쿼리에 포함되지 않음을 확인할 수 있다

 

## **QueryDSL SubQuery**

QueryDSL에서 SubQuery를 사용하는 방식은 2가지로 나눌 수 있다

#### 1. new JPASubQuery(~~~)로 서브쿼리를 생성

#### 2. JPAExpressions를 static import하고 편리하게 select(~~~)로 서브쿼리 생성



![img](https://blog.kakaocdn.net/dn/dwivoE/btrKLfxOA5i/q9mk5w0oqfpfOKJFQqd9Uk/img.png)



어차피 **JPAExpressions도 내부적으로 서브쿼리를 생성할 때 "new JPASubQuery"를 통해서 생성**하기 때문에 JPAExpressions를 static import하고 편리하게 쓰는것이 생산성에서 더 좋아보인다

 

### **1. Where절 서브쿼리 (age에 대한 equal SubQuery)**

> 최연소 멤버 & 최고령 멤버

```
// 최연소 멤버
List<Member> fetch = query.selectFrom(member)
        .innerJoin(member.team, team).fetchJoin()
        .where(member.age.eq(
                select(member.age.min()) // Member중에서 최연소 Member
                        .from(member)
        )).fetch();

for (Member member : fetch) {
    System.out.println("Member = " + member);
    System.out.println("\tTeam = " + member.getTeam());
}

// 최고령 멤버
List<Member> fetch = query.selectFrom(member)
        .innerJoin(member.team, team).fetchJoin()
        .where(member.age.eq(
                select(member.age.max()()) // Member중에서 최고령 Member
                        .from(member)
        )).fetch();

for (Member member : fetch) {
    System.out.println("Member = " + member);
    System.out.println("\tTeam = " + member.getTeam());
}
```



![img](https://blog.kakaocdn.net/dn/dzawtT/btrKOiAP5G3/buVnZgxK0MxLja5NktDvU0/img.png)![img](https://blog.kakaocdn.net/dn/2IcsO/btrKLaDj6TI/Xb35szkyW038ykKmpn8s1k/img.png)

![img](https://blog.kakaocdn.net/dn/bmlRGA/btrKHcvCU1A/wPZv1jVTKaEvIDfspMjQYK/img.png)

![img](https://blog.kakaocdn.net/dn/bbLuft/btrKMFXkuMH/PEKymCmEXLySaROTAUL9Z1/img.png)



 

### **2) Where절 SubQuery (age에 대한 IN SubQuery)**

> 평균 나이 이상인 멤버들

```
List<Member> fetch = query.selectFrom(member)
        .innerJoin(member.team, team).fetchJoin()
        .where(member.age.in(
                select(member.age)
                .from(member)
                .where(member.age.between(
                        select(member.age.avg().intValue()).from(member),
                        select(member.age.max()).from(member)
                        )
                )
        )).fetch();

Integer avgAge = query.select(member.age.avg().intValue())
        .from(member)
        .fetchOne();
System.out.println("평균 나이 = " + avgAge);

for (Member member : fetch) {
    System.out.println("Member = " + member);
    System.out.println("\tTeam = " + member.getTeam());
}
```



![img](https://blog.kakaocdn.net/dn/dJuRka/btrKGoQNW2E/K3W2do3Fh7SaXVYbCYMkx0/img.png)

![img](https://blog.kakaocdn.net/dn/lyfgB/btrKMFiJeVL/oXlQG2WY3EyNTmxB57SDa0/img.png)



 

> From절 서브쿼리는 JPA의 구현체인 Hibernate를 사용하면 여전히 활용할 수 없는 서브쿼리이다
> 당연히 QueryDSL도 From절 서브쿼리는 지원하지 않는다





# 정렬

## **정렬**

QueryDSL에서의 **정렬**은 **"orderBy()"메소드**를 통해서 내부적으로 정렬 기준의 필드에 대해서 **[asc(), desc()]**를 통해서 정렬하면 된다

```java
// 25살 이상 Member들에 대한 "나이 오름차순/이름 내림차순 정렬"
List<Member> fetch = query.selectFrom(member)
        .where(member.age.goe(25))
        .orderBy(member.age.asc(), member.username.desc())
        .fetch();
```



![img](https://blog.kakaocdn.net/dn/daFCYG/btrKHwNR5fy/NbPhLddFvbAKyyU00UqZv1/img.png)



 

## **페이징**

**페이징**은 **"offset() & limit()"**를 통해서 시작 지점/페이징 개수를 적절하게 조합해서 활용하면 된다

offset번째부터 limit개수의 페이징 처리를 한다는 의미이다

- QueryDSL에서 offset의 가장 처음 기준은 "0"이다

```java
Pageable pageable = PageRequest.of(2, 20);
 
List<Member> fetch1 = query.selectFrom(member)
        .where(member.age.goe(25))
        .offset(0) // 시작 지점 (offset)
        .limit(7) // 페이징 개수 (limit)
        .fetch();
 
List<Member> fetch2 = query.selectFrom(member)
        .where(member.age.goe(25))
        .offset((long) pageable.getPageNumber() * pageable.getPageSize()) // 시작 지점 (offset)
        .limit(pageable.getPageSize()) // 페이징 개수 (limit)
        .fetch();
```



![img](https://blog.kakaocdn.net/dn/blY7e1/btrKIxS2uS3/X9rzr2iQj24gKVeW0Ao7Wk/img.png)![img](https://blog.kakaocdn.net/dn/b5m83F/btrKGoXjISA/89c8VtWoyzkzYv3VMyfInK/img.png)

왼쪽 = 0번째부터 7개 (0 ~ 6) / 오른쪽 = 40번째부터 20개 (40 ~ 59)



 

실제로 페이징 처리를 하기 위해서는 **"전체 데이터 수"**를 반드시 파악해야 한다.

이때는 단순하게 fetch()로 페이징 리스트를 받는게 아니라 **"fetchResults()"**로 **페이징 리스트 + 전체 데이터 개수**까지 받아야 한다

> 하지만 QueryDSL 5.0부터 **fetchResults()/fetchCount()는 Deprecated**되었다
> 왜냐하면 QueryDSL을 쓴다고해도 모든 Dialect에서 QueryResults로 count query를 정확하게 날릴 수 없고 group by/having과 같은 절을 함께 사용한다면 count query가 제대로 나가지 않는다고 한다
>
> \>> 따라서 fetch()로 List를 끌고온 다음에 size()를 통해서 각 페이징 개수를 확인하거나, 아니면 전체 데이터 개수 조회용 count query를 따로 만들어서 사용해야 한다

```java
Pageable pageable = PageRequest.of(2, 20);
 
List<Member> fetch1 = query.selectFrom(member)
        .where(member.age.goe(25))
        .offset(0) // 시작 지점 (offset)
        .limit(7) // 페이징 개수 (limit)
        .fetch();
 
List<Member> fetch2 = query.selectFrom(member)
        .where(member.age.goe(25))
        .offset((long) pageable.getPageNumber() * pageable.getPageSize()) // 시작 지점 (offset)
        .limit(pageable.getPageSize()) // 페이징 개수 (limit)
        .fetch();
 
// 전체 데이터 count query
long memberCount = query.selectFrom(member)
        .where(member.age.goe(25))
        .stream().count();
 
// 각 페이징 개수 count
int fetch1Size = fetch1.size();
int fetch2Size = fetch2.size();
 
System.out.println("전체 데이터 개수 = " + memberCount);
System.out.println("fetch1 페이징 데이터 개수 = " + fetch1Size);
System.out.println("fetch2 페이징 데이터 개수 = " + fetch2Size);
```



![img](https://blog.kakaocdn.net/dn/ZUSrf/btrKHT258GB/sRHtuK71eJdkkNmEPIfGs0/img.png)별도의 전체 데이터 Count Query





# Querydsl 기본

## **JPAQueryFactory**

기본적으로 QueryDSL을 통해서 query를 생성하기 위해서는 "JPAQueryFactory"로부터 쿼리를 생성해야 한다.

하지만 JPAQueryFactory가 필요한 모든 곳에서 JPAQueryFactory를 가져오는 것은 큰 낭비이고 JPAQueryFactory를 사용하기 위해서는 "EntityManager"도 필요하기 때문에 불편하다

#### >> **JPAQueryFactory를 @Bean을 통해서 빈으로 등록**해버리면 프로젝트 전역에서 편리하게 JPAQueryFactory를 사용할 수 있게 된다

```
@Configuration
public class QueryDSLConfig {
    @PersistenceContext
    private EntityManager em;

    @Bean
    JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(em);
    }
}
```

 

## **쿼리 타입 (Q)**

QueryDSL을 사용할 때 각 빌드 방법(Gradle / IntelliJ IDEA)에 맞게 빌드를 하게되면 지정된 경로로 "쿼리 타입(Q)"가 각 Entity별로 생성된다



![img](https://blog.kakaocdn.net/dn/bsw9wI/btrKGRculSH/2zImTcOz9pZDe9uNPM93tk/img.png)![img](https://blog.kakaocdn.net/dn/qRkhL/btrKF08PfEi/eXfQqKIhhj0q1VucKKd8m0/img.png)

왼쪽 = Member / 오른쪽 = Member에 대한 쿼리 타입 QMember



왼쪽 Member Entity에 대해서 빌드를 하고난 후 쿼리 타입 QMember가 생성되었다

Member Entity의 모든 필드는 QMember에서 프로퍼티 접근을 통해서 접근할 수 있게 생성되었다

```
QMember member1 = QMember.member; // member1
QMember member2 = new QMember("m"); // m
```

기본적으로 QMember은 위와 같이 사용하는데 만약 **static으로 QMember에 대해서 member를 가져오게** 되면 해당 member에 대한 Alias는 **자동적으로 "member1"**이 된다

반면에 **new를 통해서 QMember를 생성**하고 **내부에 variable을 지정**하게 된다면 해당 variable이 Member에 대한 Alias가 된다

- 이 두가지 경우중에서 static으로 QMember에 대해서 member를 가져오면 import static을 활용해서 인스턴스를 더 편리하게 사용할 수 있다

```
import static JPA.QueryPractice.domain.QMember.member;
import static JPA.QueryPractice.domain.QOrder.order;
import static JPA.QueryPractice.domain.QProduct.product;
import static JPA.QueryPractice.domain.QTeam.team;
```

 

## **select - from**

```
// selectFrom
List<Member> fetch1 = query.selectFrom(member)
        .where(member.age.goe(25))
        .fetch();

// select(Entity) - from(Entity)
List<Member> fetch2 = query.select(member)
        .from(member)
        .where(member.age.goe(25))
        .fetch();
        
// select(필드) - from(Entity)
List<Tuple> fetch3 = query.select(member.id, member.username, member.age)
        .from(member)
        .where(member.age.goe(25))
        .fetch();
```

selectFrom은 From에서 조회하려는 엔티티 전체를 조회할 때 사용하는 메소드이다

반면 select - from은 From에서 조회하려는 엔티티 전체중에서 [전체 or 일부분]을 조회할 때 사용하는 메소드이다



![img](https://blog.kakaocdn.net/dn/c8ySje/btrKHbu627a/6qtag72MyK8fb1IoyRI8k0/img.png)![img](https://blog.kakaocdn.net/dn/u7kT3/btrKEp9Xt2n/9hUzDH4XPGVv1VKVyF1K9K/img.png)![img](https://blog.kakaocdn.net/dn/qqr98/btrKFeG6XDN/1k8cJiimhJ5DamcSx6b53K/img.png)

\1. selectFrom / 2. select(Entity) - from(Entity) / 3. select(필드) - from(Entity)



 

그러면 fetch3에서 조회한 [id, username, age]가 아닌 enabled라는 필드에 대해서 Tuple에서 꺼내서 조회하면 어떻게 될까?

```
List<Tuple> fetch3 = query.select(member.id, member.username, member.age)
        .from(member)
        .where(member.age.goe(25))
        .fetch();

for (Tuple tuple : fetch3) {
    System.out.println("Enabled = " + tuple.get(member.enabled));
}
```



![img](https://blog.kakaocdn.net/dn/bRcz1W/btrKFRdm4cE/0GEZm5oApdo1KZaFA3WTok/img.png)![img](https://blog.kakaocdn.net/dn/3ubE6/btrKFsSGVnN/rZlAx4DgxsraymEdHJ7Bzk/img.png)



결과로 알 수 있듯이 Member Table상에서 enabled의 값에는 전부 true/false로 들어가있지만 **query상에서 enabled를 조회하지 않았기 때문에 전부 null**로 들어가게 된다

 

## **where**

QueryDSL의 where절에는 and/or도 사용할 수 있고 "BooleanExpression"을 통해서 동적 쿼리도 굉장히 편리하게 작성할 수 있다

- 동적 쿼리와 관련된 BooleanExpression은 추후 포스팅

```
List<Member> fetch1 = query.selectFrom(member)
        .where(member.age.goe(25).and(member.username.contains("nus1")))
        .fetch();

List<Member> fetch2 = query.selectFrom(member)
        .where(member.age.lt(25).and(member.username.like("%nus1%")))
        .fetch();
```



![img](https://blog.kakaocdn.net/dn/9ekIx/btrKFQr4tqx/S4KIZzl2CpEWrUiU0KSeqK/img.png)![img](https://blog.kakaocdn.net/dn/QGcld/btrKFsZBatr/7A28kOE0bGd9UQGB1ZYkV0/img.png)



where절에는 **필드간 비교/매핑/체이닝**을 위한 메소드는 굉장히 많다

 

### **and(Predicate), or(Predicate)**

where절에 여러 조건들을 chaining해서 판별할 때 and()/or()을 사용한다

```
List<Member> fetch1 = query.selectFrom(member)
        .where(member.age.goe(25).and(member.username.contains("nus1")))
        .fetch();

List<Member> fetch2 = query.selectFrom(member)
        .where(member.age.goe(25).or(member.username.contains("nus1")))
        .fetch();
```



![img](https://blog.kakaocdn.net/dn/Jtuo3/btrKHvfWC1z/v80F9Hcn1P2U8GyeTXFZx1/img.png)![img](https://blog.kakaocdn.net/dn/ebwzAa/btrKGPMLPzV/fClEpxOVbQ7muCqiOC9Jq0/img.png)



 

### **andAnyOf(Predicate....), orAllOf(Predicate...)**

andAnyOf(..)는 **[앞의 조건 AND {Predicate1 OR Predicate2 OR Predicate3 OR ....}]**의 query가 나가게 된다

orAllOf(..)는 **[앞의 조건 OR {Predicate1 AND Predicate2 AND Predicate3 AND ...}]**의 query가 나가게 된다

```
List<Member> fetch1 = query.selectFrom(member)
        .where(member.username.contains("nus1").andAnyOf(member.age.goe(25), member.enabled.isTrue()))
        .fetch();

List<Member> fetch2 = query.selectFrom(member)
        .where(member.username.contains("nus1").orAllOf(member.age.lt(25), member.enabled.isFalse()))
        .fetch();
```



![img](https://blog.kakaocdn.net/dn/dZQd3T/btrKHuH9RcP/IVKCYrM0KznK2mFzZyn8T0/img.png)![img](https://blog.kakaocdn.net/dn/bCYvUe/btrKFFLcd7M/xs13rdJrOqVeOD7YJCdz3K/img.png)



 

### **isTrue(), isFalse()**

isTrue()/isFalse()는 Boolean 필드에 대해서 true/false를 찾을 때 사용한다

not()의 경우 **"앞의 필드 값이 아닌 것"**에 대해서 query를 날린다

```
List<Member> fetch1 = query.selectFrom(member)
        .where(member.enabled.isTrue())
        .fetch();

List<Member> fetch2 = query.selectFrom(member)
        .where(member.enabled.isFalse())
        .fetch();

List<Member> fetch3 = query.selectFrom(member)
        .where(member.age.eq(25).not()) // 멤버 나이가 25살이 "아닌"
        .fetch();
```



![img](https://blog.kakaocdn.net/dn/brnGuH/btrKFFElMTp/KrVkRhub283IKU8Nua2201/img.png)![img](https://blog.kakaocdn.net/dn/bnTcKU/btrKFz5dBza/j28p6sCzGknmWjwrFNsFsk/img.png)



 

### **eq(값), \**not()\****

eq()의 경우 **"앞의 필드와 인자의 값이 동일한 것"**에 대해서 query를 날린다

not()의 경우 **"앞의 필드 값이 아닌 것"**에 대해서 query를 날린다

```
List<Member> fetch1 = query.selectFrom(member)
        .where(member.age.eq(25)) // 멤버 나이가 25살
        .fetch();

List<Member> fetch2 = query.selectFrom(member)
        .where(member.age.eq(25).not()) // 멤버 나이가 25살이 "아닌"
        .fetch();
```



![img](https://blog.kakaocdn.net/dn/8k2AK/btrKHboyBbE/R9kirzXfTnNgqIt7SaicPk/img.png)![img](https://blog.kakaocdn.net/dn/9b7o4/btrKFFdll4i/y3H5nvIdooKmjHHh1T1WPk/img.png)



 

### **gt(값), \**lt(값),\** goe(값), loe(값)**

gt = 왼쪽 필드가 "값"보다 큰 것에 대한 query

lt = 왼쪽 필드가 "값"보다 작은 것에 대한 query

goe = 왼쪽 필드가 "값"보다 크거나 같은 것에 대한 query

loe = 왼쪽 필드가 "값"보다 작거나 같은 것에 대한 query

```
List<Member> fetch1 = query.selectFrom(member)
        .where(member.age.gt(25)) // age > 25
        .fetch();

List<Member> fetch2 = query.selectFrom(member)
        .where(member.age.lt(25)) // age < 25
        .fetch();

List<Member> fetch3 = query.selectFrom(member)
        .where(member.age.goe(25)) // age >= 25
        .fetch();

List<Member> fetch4 = query.selectFrom(member)
        .where(member.age.loe(25)) // age <= 25
        .fetch();
```



![img](https://blog.kakaocdn.net/dn/9X7OS/btrKFv3fZqe/f9m7VOURYr1GpidGZDGi70/img.png)![img](https://blog.kakaocdn.net/dn/pXOPP/btrKGSpj9U7/yHhTJH9kFY0KJrnSRFQHkK/img.png)

왼쪽 = gt / 오른쪽 = lt

![img](https://blog.kakaocdn.net/dn/bANCK4/btrKE6iniPz/CeJtsO2ENtzDun4hMB3Ax0/img.png)![img](https://blog.kakaocdn.net/dn/b770GV/btrKFQFPVhr/drxmyxrSAn16arnLJ7RSiK/img.png)

왼쪽 = goe / 오른쪽 = loe



 

### **between(from, to), notBetween(from, to)**

**[from, to]범위에 포함된 값/[from, to]범위에 포함되지 않은 값**에 대한 query를 날리게 된다

```
List<Order> fetch1 = query.selectFrom(order)
        .where(order.orderDate.between(
                LocalDateTime.of(2022, 8, 1, 0, 0, 0),
                LocalDateTime.of(2022, 8, 10, 0, 0, 0)
        )).fetch();

List<Order> fetch2 = query.selectFrom(order)
        .where(order.orderDate.notBetween(
                LocalDateTime.of(2022, 8, 1, 0, 0, 0),
                LocalDateTime.of(2022, 8, 10, 0, 0, 0)
        )).fetch();
```



![img](https://blog.kakaocdn.net/dn/c5kanu/btrKFRrkJvG/SLqn6rB54CIZQPSrFGqiu0/img.png)between

![img](https://blog.kakaocdn.net/dn/2E6Gb/btrKF7U0QwT/k6pKAyseKxaKBBeCdz6SA0/img.png)notBetween



 

### **like(문자열), notLike(문자열), startsWith(문자열), endsWith(문자열), contains(문자열)**

```
List<Member> fetch1 = query.selectFrom(member)
        .where(member.username.like("%nus1")) // username LIKE '%nus1%'
        .fetch();

List<Member> fetch2 = query.selectFrom(member)
        .where(member.username.notLike("%nus1")) // username NOT LIKE '%nus1%'
        .fetch();

List<Member> fetch3 = query.selectFrom(member)
        .where(member.username.startsWith("nus1")) // username LIKE 'nus1%'
        .fetch();

List<Member> fetch4 = query.selectFrom(member)
        .where(member.username.endsWith("nus1")) // username LIKE '%nus1'
        .fetch();

List<Member> fetch5 = query.selectFrom(member)
        .where(member.username.contains("nus1")) // username LIKE '%nus1%'
        .fetch();
```



![img](https://blog.kakaocdn.net/dn/c7Elaw/btrKFsS8JYM/o9OJ1smXdIYZ9rITnrirOK/img.png)![img](https://blog.kakaocdn.net/dn/rmX9M/btrKHwlVG8W/o6bgWxKG4FgXLJNwyv2ct0/img.png)

왼쪽 = like / 오른쪽 = notLike

![img](https://blog.kakaocdn.net/dn/kGaO8/btrKGxMFoZG/LI1YSKyAR6XA0jHhkXuqdK/img.png)![img](https://blog.kakaocdn.net/dn/pXhUl/btrKGRc31Xz/oPHlHPaSZjECeSMqEZ5lb1/img.png)![img](https://blog.kakaocdn.net/dn/nv9XH/btrKFrmpzjP/BojkkthkGmSrNjQlDLfuk0/img.png)

왼쪽 = startsWith / 가운데 = endsWith / 오른쪽 = contains



 

### **in(값...), notIn(값...)**

```
// List
List<Integer> list = new ArrayList<>();
list.add(20); list.add(25); list.add(30);

// Query
List<Member> fetch1 = query.selectFrom(member)
        .where(member.age.in(list))
        .fetch();

List<Member> fetch2 = query.selectFrom(member)
        .where(member.age.notIn(list))
        .fetch();
```



![img](https://blog.kakaocdn.net/dn/cGWYws/btrKF18md69/4Z7mYgIOftxT4Oyc4vieG1/img.png)![img](https://blog.kakaocdn.net/dn/dhuoLD/btrKHuaCVRf/EbK987NBZzTKUYPcNApOgk/img.png)



 