# QueryDsl TIP

## QueryDsl 사용하는 이점

- IDE를 통한 자동완성 기능
- 컴파일 에러가 발생
- 조건문을 사용한 동적 쿼리문 작성이 간편
- 코드의 재사용성 증가

## QueryDsl 사용하는 단점

- 까다롭다

### 간단한 사용예시

> 사용 예시
> \1) query문을 작성하려면 JpaQuery 인스턴스가 필요합니다. 이를 위해 JpaQueryFactory를 통해 인스턴스를 생성해야 합니다. 먼저 JpaQueryFactory를 영속성 컨텍스트를 파라미터로 넘겨서 생성합니다.(Querydsl은 JPA API를 사용하며 JPA를 지원하는 모듈입니다.)
> `JpaQueryFactory queryFactory = new JpaQueryFactory(em);`
> \2) 사용하려는 QEntity를 생성합니다.
> `QItem qItem = QItem.item;` -> QItem은 정적 메소드로 만들어져 있는 인스턴스를 가져옵니다.
> \3) 쿼리문 작성
> `JpaQuery<Item> query = queryFacotory.selectFrom(qItem)......where....조건문;`
> \4) 쿼리 결과 반환
> `List<Item> items = query.fetch();`

출처 : 쿼리 메소드, JPQL, Querydsl 요약 - [https://velog.io/@simgyuhwan/%EC%BF%BC%EB%A6%AC-%EB%A9%94%EC%86%8C%EB%93%9C-JPQL-Querydsl-%EC%9A%94%EC%95%BD](https://velog.io/@simgyuhwan/쿼리-메소드-JPQL-Querydsl-요약)

## Get Strated

### 1. build.gradle에서 Querydsl 설정 방법

- https://data-make.tistory.com/728

### 2. Spring Boot Data Jpa 프로젝트에 Querydsl 적용하기

- https://jojoldu.tistory.com/372

# 예제를 통해 QueryDsl 공부

## Projection 사용하기

> **프로젝션(Projection)은 select 절에서 어떤 컬럼들을 조회할지 대상을 지정하는 것을 말한다.**

- [Querydsl] Projection & 결과 매핑
  - https://jaime-note.tistory.com/75
- 20201009 [jpa] querydsl 내, query projection 사용해보기
  - https://pasudo123.tistory.com/431
- [querydsl] querydsl에서 projection 다루기
  - https://devkingdom.tistory.com/253

## 서브쿼리 사용하기

- [Database]서브 쿼리(MySQL)
  - https://sskl660.tistory.com/69
- [Querydsl] 서브 쿼리(Subquery), Case(when, then), 상수(Constant), concat
  - https://jaime-note.tistory.com/74
- QueryDSL 서브 쿼리 사용법
  - https://icarus8050.tistory.com/6
- [Querydsl] 서브쿼리 사용하기
  - https://jojoldu.tistory.com/379

## Case When 사용하기

- [Querydsl] Case When 사용하기
  - https://jojoldu.tistory.com/401
- CASE 기초 문법 뽀개기
  - [https://z-hwan.tistory.com/entry/CASE-%EA%B8%B0%EC%B4%88-%EB%AC%B8%EB%B2%95-%EB%BD%80%EA%B0%9C%EA%B8%B0](https://z-hwan.tistory.com/entry/CASE-기초-문법-뽀개기)

## N+1 문제 해

- [JPA] 관심 카테고리 게시글 + 좋아요 Querydsl로 한방 쿼리 만들기 (N+1 문제 해결)
  - https://loosie.tistory.com/792

## 좋아요 기능 구현...

- 게시글 리스트에서 '좋아요' 버튼을 눌렀는지 판별
  - [https://hashcode.co.kr/questions/1697/%EA%B2%8C%EC%8B%9C%EA%B8%80-%EB%A6%AC%EC%8A%A4%ED%8A%B8%EC%97%90%EC%84%9C-%EC%A2%8B%EC%95%84%EC%9A%94-%EB%B2%84%ED%8A%BC%EC%9D%84-%EB%88%8C%EB%A0%80%EB%8A%94%EC%A7%80-%ED%8C%90%EB%B3%84](https://hashcode.co.kr/questions/1697/게시글-리스트에서-좋아요-버튼을-눌렀는지-판별)
- Springboot + JPA + Querydsl로 좋아요 기능 만들기 1 - 등록
  - https://coco-log.tistory.com/133

## Use Dynamic Query

- QueryDSL(2) - 쿼리 생성 방법, 기본 문법
  - https://ykh6242.tistory.com/107
- Querydsl 동적 쿼리
  - [https://velog.io/@aidenshin/Querydsl-%EB%8F%99%EC%A0%81-%EC%BF%BC%EB%A6%AC](https://velog.io/@aidenshin/Querydsl-동적-쿼리)
- Querydsl 동적쿼리
  - https://escapefromcoding.tistory.com/620
- [Querydsl] 다이나믹 쿼리 사용하기
  - https://jojoldu.tistory.com/394
- Spring querydsl 동적쿼리 Or 처리 할때
  - https://webstorage.tistory.com/2
- Querydsl - where절을 이용한 동적 쿼리와 성능 최적화
  - https://jddng.tistory.com/343

## Use Join

- querydsl join
  - https://hjhng125.github.io/querydsl/querydsl-join/

## 참고자료

- [QueryDSL] QueryDSL 시작하기
  - https://heekng.tistory.com/159
- [QueryDSL] QueryDSL 기본문법
  - https://heekng.tistory.com/160?category=1070077
- [Querydsl] 기본문법 학습하기
  - [https://velog.io/@shlee327/Querydsl-%EA%B8%B0%EB%B3%B8%EB%AC%B8%EB%B2%95-%ED%95%99%EC%8A%B5%ED%95%98%EA%B8%B0](https://velog.io/@shlee327/Querydsl-기본문법-학습하기)
- [SPRING] JPA의 영속성 컨텍스트
  - https://jaeho214.tistory.com/73
- Querydsl - 레퍼런스 문서
  - http://querydsl.com/static/querydsl/3.4.3/reference/ko-KR/html_single/#d0e1913





# ----------------------------



# Querydsl 소개

Querydsl은 HQL(Hibernate Query Language) 쿼리를 타입에 안전하게 생성 및 관리할 수 있게 해주는 프레임워크” 다. 공식 레퍼런스를 인용한 정의인데, 잘 와닿지 않는다면 “Querydsl은 자바 코드 기반으로 쿼리를 작성하게 해준다”라고 생각해도 좋을 것 같다.

- [참고 링크: Querydsl Reference Guide](https://querydsl.com/static/querydsl/4.4.0/reference/html_single/#intro)



# Querydsl은 왜 필요할까?

JPA를 사용한다고 가정해보자. 간단한 쿼리라면 인터페이스에 메서드 명세만 잘 정의해 주면 별다른 문제 없이 사용할 수 있을 것이다. 예를 들면 아래처럼 “제목에 특정 문자열이 포함된 기사를 조회”하는 메서드처럼 말이다.

```
Article findByTitleContains(String title);
```

조금 더 복잡한 쿼리가 필요한 경우에는 어떨까? 앞서 살펴본 것처럼 단순히 특정 문자열이 제목에 포함된 기사를 조회하는 것이 아니라, 기사를 작성한 사용자의 레벨을 기준으로 조회하는 것이다.

이런 경우에는 JPA 자체 제공 메서드만으로 해결하기 어렵기 때문에 네이티브 쿼리(Native Query)를 고려해볼 수 있다. 다음은 레벨이 특정 기준 이상인 사용자가 작성한 기사들을 조회하는 메서드다.



```
@Query(value = "SELECT id, title, user_id FROM article WHERE user_id IN (SELECT id FROM user WHERE level > :level)", nativeQuery = true)
List<Article> findByLevel(String level);
```

위에서 정의한 네이티브 쿼리를 다시 살펴보자. 가독성은 감안하더라도 문자열을 이어 붙여가며 직접 작성하기 때문에 오타가 발생하기 아주 좋다.

**그렇다면 이 코드를 Querydsl로 변경하면 어떻게 될까?** 아직 Querydsl을 사용하는 방법에 대해서 알아보지 않았지만 어떤 모습일지 먼저 살펴보자. 다음은 위에서 살펴본 네이티브 쿼리와 동일한 쿼리를 수행하는 Querydsl 예시다.

```
public List<Article> findByUserLevel(String level) {
    QArticle article = QArticle.article;
    QUser user = QUser.user;

    return queryFactory.selectFrom(article)
        .where(
            article.userId.in(
                JPAExpressions
                    .select(user.id)
                    .from(user)
                    .where(user.level.gt(level))
            )
        )
        .fetch();
}
```

앞서 살펴본 네이티브 쿼리보다 훨씬 가독성이 좋다. ~~물론 코드의 절대적인 양은 늘었지만…~~

또한 메서드 타입에 맞지 않는 파라미터를 넘기는 경우 친절하게 컴파일 오류를 발생시켜 잠재적인 버그를 방지해준다. 즉, 실행 시점 이전에 잘못된 쿼리 파라미터 타입까지 확인할 수 있는 장점이 있다.

그렇다면 지금부터 Querydsl의 사용법에 대해서 알아보자.



# Querydsl 관련 설정

> 예제는 2021년 7월 기준으로 다시 작성되었으며 전체 코드는 글 하단의 Github 저장소 링크를 참고해 주세요.

예제에서 사용된 프레임워크/라이브러리의 버전은 아래와 같다.

- Spring Boot: 2.5.0
- Gradle 7.1.1
- Querydsl: 4.4.0
- Lombok: 1.18.18

## gradle 설정

먼저 다음과 같이 `build.gradle` 파일에 선언한다. 프로젝트 구성에 필요한 일부 의존성은 생략하고 Querydsl 설정에 필요한 의존성만 나열하였다.

```
// ... 생략

dependencies {
    // ... 생략
    
    implementation "com.querydsl:querydsl-core:${queryDslVersion}"
    implementation "com.querydsl:querydsl-jpa:${queryDslVersion}"

    /*
     * `NoClassDefFoundError` 관련 대응으로 필요하다.
     * 참고로 javax -> jakarta 로 이름이 변경되었다.
     */
    annotationProcessor(
            "jakarta.persistence:jakarta.persistence-api",
            "jakarta.annotation:jakarta.annotation-api",
            "com.querydsl:querydsl-apt:${queryDslVersion}:jpa")
}

sourceSets {
    main {
        java {
            srcDirs = ["$projectDir/src/main/java", "$projectDir/build/generated"]
        }
    }
}

// ... 생략
```



## Querydsl Config 설정

다음으로 Querydsl을 사용하기 위한 Config 설정을 진행하면 된다. 여기서 등록한 `jpaQueryFactory` 빈을 `Repository`에서 사용하게 된다.

```
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.querydsl.jpa.impl.JPAQueryFactory;

@Configuration
public class QuerydslConfig {
	@PersistenceContext
	private EntityManager entityManager;

	@Bean
	public JPAQueryFactory jpaQueryFactory() {
		return new JPAQueryFactory(entityManager);
	}
}
```



# Querydsl 사용법

Querydsl을 사용하기 위한 설정은 끝났다. 이제 Querydsl을 어떻게 사용하는지 알아보자.

## Entity 클래스 정의

먼저 엔티티(Entity) 클래스를 정의한다. 각각 `article`과 `user` 테이블에 매핑되는 엔티티다.

```
@Getter
@Entity
@Table(name = "article")
public class Article {
	@Id
	private Integer id;
	@Column(name = "user_id")
	private Integer userId;
	private String title;
}
```



```
@Getter
@Entity
@Table(name = "user")
public class User {
	@Id
	private Integer id;
	private String name;
	private String level;
}
```



## Q클래스 생성

Querydsl은 컴파일 단계에서 엔티티를 기반으로 Q클래스 파일들을 생성한다. 이 클래스를 기반으로 쿼리를 작성하게 된다.

Q클래스를 생성하려면 Gradle 옵션을 통해서 소스 코드를 컴파일시키면 된다. 즉, `build` task의 `build` 옵션을 실행하거나 단순히 Q클래스만 만들 목적이라면 `other` 태스크의 `compileJava`만 실행시키면 된다.

실행 후에는 아래와 같이 빌드 결과물에 Q클래스들이 생긴다.

![q class](https://madplay.github.io/img/post/2021-01-01-introduction-to-querydsl-1.jpg)





## Repository 정의

다음으로 실제 쿼리를 작성하고 수행할 `Repository` 레이어들을 만든다. JPA 인터페이스 메서드와 Querydsl 기반으로 사용할 메서드를 모두 사용할 것이다.

먼저 구현할 Querydsl 메서드의 시그니처를 정의한다. `~RepositoryCustom` 이라는 네이밍을 갖는다.

```
/**
 * Querydsl로 작성할 쿼리는 이 곳에 시그니처를 선언하고 `~RepositoryImpl`에서 구현한다.
 */
public interface ArticleRepositoryCustom {
	List<Article> findByLevelUsingQuerydsl(String level);
}
```



다음으로 위에서 정의한 시그니처 기반으로 실제 동작을 정의할 구현체다. QuerydslConfig 클래스에서 등록한 `JPAQueryFactory`를 기반으로 쿼리를 작성하고 수행한다. 메서드 네이밍은 임의로 “~UsingQuerydsl”라는 접미사를 붙였지만, 다른 사람들과 진행하는 프로젝트라면 컨벤션에 맞게 정의하자.

```
/**
 * Querydsl를 이용한 쿼리를 작성한다.
 */
@Repository
@RequiredArgsConstructor
public class ArticleRepositoryImpl implements ArticleRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	public List<Article> findByLevelUsingQuerydsl(String level) {
		// Q클래스를 이용한다.
		QArticle article = QArticle.article;
		QUser user = QUser.user;

		return queryFactory.selectFrom(article)
			.where(
				article.userId.in(
					JPAExpressions
						.select(user.id)
						.from(user)
						.where(user.level.gt(level))
				)
			)
			.fetch();
	}
}
```

위와 같이 커스텀한 Repository은 네이밍 규약을 잘 지켜야 한다. 별도의 설정을 하지 않았다면, `~Impl` 접미사를 붙여야만 스프링이 찾을 수 있다. 관련해서는 `spring-data`에 포함된 `RepositoryConfigurationSourceSupport` 클래스와 `AnnotationRepositoryConfigurationSource` 클래스의 내부 코드를 보면 알 수 있다.

마지막으로 JPA 인터페이스 메서드도 같이 사용할 수 있도록 인터페이스를 정의한다. 아래 `findByLevel`은 Querydsl과 비교하기 위해 추가했다.

```
public interface ArticleRepository extends JpaRepository<Article, Integer>, ArticleRepositoryCustom {
	@Query(value = "SELECT * FROM article WHERE user_id IN (SELECT id FROM user WHERE level > :level)", nativeQuery = true)
	List<Article> findByLevel(String level);
}
```

## 테스트

이제 작성한 코드들을 실행하기 위해서 간단한 테스트 코드를 작성해보자.

```
@SpringBootTest
class ExampleApplicationTests {

	@Autowired
	private ArticleRepository articleRepository;

	@Test
	void testGetArticleList() {
		// Native Query
		List<Article> articleList = articleRepository.findByLevel("1");

		System.out.println("--------------------------------------------------");
		
		// Querydsl
		List<Article> articleListByQuerydsl = articleRepository.findByLevelUsingQuerydsl("1");

		Assertions.assertEquals(articleList.size(), articleListByQuerydsl.size());
	}
}
```

Querydsl의 쿼리가 잘 만들어지는지 확인하기 위해서 실행된 쿼리를 출력하는 설정도 추가한다.



```
spring.jpa.properties.hibernate.show_sql=true # 실행된 쿼리 출력
spring.jpa.properties.hibernate.format_sql=true # 쿼리를 예쁘게 출력
```

테스트 코드를 실행해보자. 아래와 출력되는 쿼리를 통해 두 개의 쿼리가 결과적으로 같음을 알 수 있다.

```
Hibernate: 
    SELECT
        id,
        title,
        user_id 
    FROM
        article 
    WHERE
        user_id IN (
            SELECT
                id 
            FROM
                user 
            WHERE
                level > ?
        )
--------------------------------------------------
Hibernate: 
    select
        article0_.id as id1_0_,
        article0_.title as title2_0_,
        article0_.user_id as user_id3_0_ 
    from
        article article0_ 
    where
        article0_.user_id in (
            select
                user1_.id 
            from
                user user1_ 
            where
                user1_.level>?
        )
```



## 동적 쿼리

Querydsl의 또 다른 장점으로 “동적 쿼리”를 뽑을 수 있다. 아래와 같이 코드 기반으로 메서드를 정의하여 조건식을 만들 수 있다. 전달되는 파라미터가 없어서 `where` 절에 `null`이 들어가는 경우 해당 조건은 생략된다.

```
public List<Article> searchArticle(String title, Integer userId) {
    return queryFactory.selectFrom(article)
        .where(titleContains(title), userIdEq(userId))
        .fetch();
}

private BooleanExpression titleContains(String title) {
    return StringUtils.isNotBlank(title) ? article.title.contains(title) : null;
}

private BooleanExpression userIdEq(Integer userId) {
    return userId != null ? article.userId.eq(userId) : null;
}
```



# 마치며

JPA를 사용하다 보면 기본 기능으로 해결되지 않는 경우에는 네이티브 쿼리를 사용하게 된다. 그런데 예제에서 살펴본 것처럼 네이티브 쿼리는 문자열을 이어 붙이기 떄문에 오타가 발생하기 쉽고 가독성이 떨어지는 단점이 있다.

Querydsl을 사용하면 자동 완성과 같은 IDE의 기능을 사용할 수 있고, 컴파일 시점에 타입이나 문법 오류를 확인할 수 있다. 또한 동적 쿼리도 쉽게 사용할 수 있어서 편리하다. 물론 경우에 따라서 적용 필요성이 다를 수 있기 때문에 프로젝트의 특성에 따라서 적절하게 선택하면 될 것 같다.



# 예제 소스 코드

이번 글에서 사용한 소스 코드는 모두 아래 저장소에 있습니다.

- github 코드 저장소: https://github.com/madplay/querydsl-example