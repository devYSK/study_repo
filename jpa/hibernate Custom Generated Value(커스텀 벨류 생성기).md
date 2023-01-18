

데이터를 저장할 때 특정 조건에 의해서 증가하는 값을 Hibernate에서는 어떻게 할 수 있는지 확인해보겠습니다.



아래와 같은 entity가 있을 때 `code`는 삭제된 데이터와 관계없이 `type` 별 최댓값으로 설정해야 한다고 가정하겠습니다. 그렇다면 `Article`을 생성하기 전에 생성하려는 `type`의 최대 `code`값을 조회하고, 1증가 시킨 후 등록해야 할 것입니다.

```
@Entity
@Table(name = "article")
class Article {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val articleNo: Int = 0
  var title: String = ""
  var type: String = ""
  var code: Int = 0
  @Type(type = "yes_no")
  var deleteYn: Boolean = false
}
```

요구 사항 대로 코드를 작성해보겠습니다. repository에 `type`별 최대 `code`값을 조회하는 메서드를 하나 만듭니다.

```
interface ArticleRepository: JpaRepository<Article, Int> {
  @Query("select max(a.code) from Article a where a.type = :type")
  fun getLastCode(@Param("type") type: String): Int?
}
```

위에 만든 `getLastCode()` 메서드를 이용해서 `code` 값을 1증가 시킨 후 저장하는 코드를 아래와 같이 만듭니다. 이대로 실행해 보면 이상 없이 잘 동작합니다. 하지만 몇 가지 개선하면 좋을 것 같습니다.

```
@Service
class ArticleService(private val articleRepository: ArticleRepository) {
  @Transactional
  fun create(article: Article) {
    (articleRepository.getLastCode(article.type) ?: 0).let {
      article.code = it + 1
      articleRepository.save(article)
    }
  }
}
```



```

@Transactional
fun create(create: ArticleCreate) {
  (articleRepository.getLastCode(create.type) ?: 0).let {
    articleRepository.save(Article.create(code = it + 1, create = create))
  }
}
```

그런데 `getLastCode()`를 호출하고 설정하는 부분을 코드를 읽을 때마다 매번 확인할 필요가 없고 변경될 가능성도 없기에 이 코드를 보기 싫을 수 있습니다. 그렇다면 service 영역에서 감추고 싶다면 어떻게 해야 할까요?





## @ValueGenerationType

이럴 경우에는 Hibernate의 `@ValueGenerationType`를 이용할 수 있습니다. 이 방식은 애노테이션으로 value 생성할 수 있게 해줍니다.

`ArticleMaxCode`이라는 애노테이션을 하나 정의하고 value 생성 역할은`ArticleMaxCodeGeneration`를 통해서 이루어지도록 설정합니다.

```
@ValueGenerationType(generatedBy = ArticleMaxCodeGeneration::class)
@Retention(AnnotationRetention.RUNTIME)
annotation class ArticleMaxCode
```

클래스 `ArticleMaxCodeGeneration`은 `AnnotationValueGeneration`을 구현합니다. INSERT 타이밍에 `getValueGenerator()`를 실행하는 코드입니다.

```
class ArticleMaxCodeGeneration : AnnotationValueGeneration<ArticleMaxCode> {
  override fun getValueGenerator(): ValueGenerator<*> {
    return ValueGenerator<Int> { session, owner ->
      when(owner) {
        is Article -> {
          (session.createQuery("select max(a.code) from Article a where a.type = :type")
              .setLockMode(LockModeType.PESSIMISTIC_WRITE)
              .setHibernateFlushMode(FlushMode.COMMIT)
              .setParameter("type", owner.type)
              .singleResult as Int? ?: 0) + 1   
        }
        else -> 0
      }
    }
  }

  override fun getGenerationTiming(): GenerationTiming {
    return GenerationTiming.INSERT
  }

  override fun initialize(annotation: ArticleMaxCode?, propertyType: Class<*>?) {
  }

  override fun referenceColumnInSql(): Boolean {
    return false
  }

  override fun getDatabaseGeneratedReferencedColumnValue(): String? {
    return null
  }
}
```

이제 `code`는 자동 생성하는 방식으로 변경 했으니 entity 생성자에서 빼고 `ArticleMaxCode` 애노테이션을 지정합니다.

> val로 정의 했기 때문에 setter가 없기에 Hibernatae에서 property가 아니라 field로 값을 주입하는 형태로 설정해야합니다.

```
@Entity
@Table(name = "article")
data class Article(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val articleNo: Int = 0
) {
  var title: String = ""
    private set

  var type: String = ""
    private set

  @field:ArticleMaxCode
  val code: Int = 0

  @Type(type = "yes_no")
  var deleteYn: Boolean = false
    private set

  companion object {
    fun create(create: ArticleCreate): Article {
      return Article().apply {
        title = create.title
        type = create.type
      }
    }
  }
}
```

이렇게 설정하고 나면 service 영역에서 코드가 훨씬 간결해집니다. 메서드에 코드가 복잡한 경우라면 이렇게 분리하는 것이 장점이 될 수도 있지만, 저장 시 `code` 값이 1씩 증가한다는 부분이 감춰져서 바로 알지 못할 수 있다는 것은 단점입니다.

```
@Transactional
fun create(create: ArticleCreate) {
  articleRepository.save(Article.create(create))
}
```





## 멀티스레드 환경

이런 식으로 최댓값을 조회하고 증가시키는 방식은 동시에 요청할 경우 문제가 있을 수 있습니다. 동시에 조회하고 그 값으로 `code`를 설정하게 되면 조회한 시점마다 값이 중복될 수 있기 때문에 의도와 다르게 값이 저장될 수 있습니다.

의도한 대로 정확한 `code` 값을 저장하기 위해서는 lock을 설정해야 합니다. 위 `ArticleMaxCodeGeneration` 클래스를 보면 이미 처리되어 있습니다. `LockModeType.PESSIMISTIC_WRITE` 을 설정한 것을 확인할 수 있습니다.

```
(session.createQuery("select max(a.code) from Article a where a.type = :type")
              .setLockMode(LockModeType.PESSIMISTIC_WRITE)
              .setHibernateFlushMode(FlushMode.COMMIT)
              .setParameter("type", owner.type)
              .singleResult as Int? ?: 0) + 1   
```

이 설정으로 실제 쿼리가 실행될 때 `SELECT FOR UPDATE` 형태로 실행됩니다. lock에 관련해서는 다음 기회에 좀 더 자세히 알아보겠습니다.

## 결론

`ValueGenerationType`를 이용하면 custom value 생성을 별도로 처리할 수 있습니다. Hibernate에서 날짜 생성을 위해서 제공하는 `@CreationTimestamp`와 `@UpdateTimestamp`도 동일한 방식으로 구현되어 있습니다. 그리고 어떤 조건 기준으로 값을 증가시키는 방식은 동시 요청에 대한 고민을 해야 한다는 점도 잊지 말아야 할 것입니다.

Id의 경우 custom value를 생성하는 방식이 조금 다릅니다. 자세한 내용은 [**Hibernate에서 Custom ID 생성하기**](https://kapentaz.github.io/jpa/Hibernate에서-Custom-ID-생성하기/)를 참고하세요.



---

# Hibernate @ValueGenerationType

- [Hibernate](https://kwonnam.pe.kr/wiki/java/hibernate)에서 필드의 값을 자동으로 생성하는 기법
- [ValueGenerationType (Hibernate JavaDocs)](https://docs.jboss.org/hibernate/orm/6.0/javadocs/org/hibernate/annotations/ValueGenerationType.html) 는 메타 어노테이션으로 다른 어노테이션을 값을 자동생성하는 어노테이션으로 지정해준다.
- [AnnotationValueGeneration](https://docs.jboss.org/hibernate/orm/6.0/javadocs/org/hibernate/tuple/AnnotationValueGeneration.html) 를 구현하여 실제로 값을 생성해주고, 이렇게 생성된 값이 필드의 값으로 자동으로 들어간다.
- 이를 기반으로 하여 [CreationTimestampGeneration](https://docs.jboss.org/hibernate/orm/6.0/javadocs/org/hibernate/tuple/CreationTimestampGeneration.html) [UpdateTimestampGeneration](https://docs.jboss.org/hibernate/orm/6.0/javadocs/org/hibernate/tuple/UpdateTimestampGeneration.html) 등이 만들어졌다.
- see [Hibernate @Generated](https://kwonnam.pe.kr/wiki/java/hibernate/generated)

## Example

```
@Entity(name = "Event")
public static class Event {
 
	@Id
	@GeneratedValue
	private Long id;
 
	@Column(name = "`timestamp`")
	@FunctionCreationTimestamp
	private Date timestamp;
 
	//Constructors, getters, and setters are omitted for brevity
}
 
@ValueGenerationType(generatedBy = FunctionCreationValueGeneration.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface FunctionCreationTimestamp {}
 
// DB를 통해 자동생성 지원
public static class FunctionCreationValueGeneration
		implements AnnotationValueGeneration<FunctionCreationTimestamp> {
 
	@Override
	public void initialize(FunctionCreationTimestamp annotation, Class<?> propertyType) {
	}
 
	/**
	 * Generate value on INSERT
	 * @return when to generate the value
	 */
	public GenerationTiming getGenerationTiming() {
		return GenerationTiming.INSERT;
	}
 
	/**
	 * Returns null because the value is generated by the database.
	 * @return null
	 */
	public ValueGenerator<?> getValueGenerator() {
		return null;
	}
 
	/**
	 * Returns true because the value is generated by the database.
	 * @return true
	 */
	public boolean referenceColumnInSql() {
		return true;
	}
 
	/**
	 * Returns the database-generated value
	 * @return database-generated value
	 */
	public String getDatabaseGeneratedReferencedColumnValue() {
		return "current_timestamp";
	}
}
```

쿼리가 아래처럼 실행된다.

```
INSERT INTO Event ("timestamp", id)
VALUES (CURRENT_TIMESTAMP, 1)
// In memory 상에서 자동생성
public static class FunctionCreationValueGeneration
		implements AnnotationValueGeneration<FunctionCreationTimestamp> {
 
	@Override
	public void initialize(FunctionCreationTimestamp annotation, Class<?> propertyType) {
	}
 
	/**
	 * Generate value on INSERT
	 * @return when to generate the value
	 */
	public GenerationTiming getGenerationTiming() {
		return GenerationTiming.INSERT;
	}
 
	/**
	 * Returns the in-memory generated value
	 * @return {@code true}
	 */
	public ValueGenerator<?> getValueGenerator() {
		return (session, owner) -> new Date( );
	}
 
	/**
	 * Returns false because the value is generated by the database.
	 * @return false
	 */
	public boolean referenceColumnInSql() {
		return false;
	}
 
	/**
	 * Returns null because the value is generated in-memory.
	 * @return null
	 */
	public String getDatabaseGeneratedReferencedColumnValue() {
		return null;
	}
}
```

쿼리가 다음과 같이 날라간다.

```
INSERT INTO Event ("timestamp", id)
VALUES ('Tue Mar 01 10:58:18 EET 2016', 1)
```







### 참조

* https://kapentaz.github.io/jpa/Hibernate%EC%97%90%EC%84%9C-Custom-Value-%EC%83%9D%EC%84%B1%ED%95%98%EA%B8%B0/#
* https://docs.jboss.org/hibernate/orm/4.3/topical/html/generated/GeneratedValues.html