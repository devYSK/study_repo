# Kotlin List Validation



코틀린 + Spring Boot 환경에서 Request 를 검증할 때,



String 타입의 List를 Null과 빈 값을 허용하지 않기 위한  `@NotBlank` 어노테이션으로는, List 타입의 필드를 검증할 수 없다.



자바에서는 다음처럼 사용할 수 있지만 코틀린에서는 사용할 수 없다. 

```java
public class Request {
  private List<@NotBlank String> inputs;
}
```



@field:NotBlack 와 @get:NotBlank 둘 다 사용할 수 없다.

* 필드나 getter에 걸고 싶기때문에 `field:`를 명시해줘야 하지만, 이 방법이 통하지 않는다. 



>  **해결하기 위해서 사용자 지정 `validation annotation`을  만들어야 한다.**

## @NotBlank List Elements Custom Validation Annotation



먼저 어노테이션을 선언한다

```kotlin
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [NotBlankElementListValidator::class])
annotation class NotBlankElementList(
    val message: String = "리스트 안에 빈 값이 존재합니다.",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<*>> = [],
)
```



Validator를 구현한다.

```java
class NotBlankElementListValidator : ConstraintValidator<NotBlankElementList, List<String>> {

    override fun isValid(lists: List<String>?, context: ConstraintValidatorContext?): Boolean {
        if (lists == null) {
            return false
        }
        return lists.none { StringUtils.isEmpty(it) }
    }
}
```

* 단순히 List 파라미터 자체가 null값인지, 또는 StringUtils를 이용하여 empty String인지 검사하는 로직이다



Request DTO에 적용한다

```kotlin
class Request(

    @field:NotNull
    val requestId: Long,

    @field:NotBlank
    val name: String,

    @NotBlankElementList
    val texts: List<String>

) 
```







> * https://stackoverflow.com/questions/51085138/kotlin-data-class-and-bean-validation-with-container-element-constraints
>
> * https://kotlinlang.org/docs/whatsnew14.html#type-annotations-in-the-jvm-bytecode
> * https://stackoverflow.com/questions/70215736/kotlin-spring-boot-bean-validation-not-working