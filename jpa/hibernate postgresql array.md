# Jpa ArrayType - Hibernate를 이용하여 PostgreSQL Array Type을 이용하는 방법 Hibernate Types

PostgreSQL을 포함한 몇몇 RDB에서는 Array Type의 컬럼 유형을 지원한다. 

그러나 hibernate에서 제공해주는 기본 Type들은 Array Type을 지원하지 않는다. 



개발자는 Hibernate Type을 Custom 구현해서 사용하거나, 

라이브러를 이용해서 PostgreSQL ArrayType을 Spring-data-jpa를 이용해서 hibernate로 매핑할 수 있다.



테스트에 사용된 도메인 엔티티는 다음과 같다. 

### 도메인 엔티티

```java
@Entity
public class User {
    @Id
    private Long id;
    private String name;

    private String[] roles;

    private List<String> hobbies;
  
}
```

## 1. Custom Hibernate Type

Hibernate는 다양한 기본 Type을 지정해 주므로 Custom 을 구현하기는 드물지만,  데이터를 저장, 가져오는 것에 대해 Custom Type을 지정할 수 있다. 

> org.hibernate.type.Type

인터페이스를  구현하거나

> org.hibernate.usertype.UserType

인터페이스를 구현하고 

타입을 적용할 컬럼에 `@Type(type = "패키지명.커스텀타입명")` 어노테이션을 적용해서 구현할 수 있다.



그 중 UserType을 이용해서 구현한 예제이다.

### 자바의 String[] 배열을 컬럼의 Array 타입으로 변환하는  Custom Type 

```java
public class CustomStringArrayType implements UserType {
    @Override
    public int[] sqlTypes() {
        return new int[]{Types.ARRAY};
    }

    @Override
    public Class returnedClass() {
        return String[].class;
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner)
      throws HibernateException, SQLException {
        Array array = rs.getArray(names[0]);
        return array != null ? array.getArray() : null;
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session)
      throws HibernateException, SQLException {
        if (value != null && st != null) {
            Array array = session.connection().createArrayOf("text", (String[])value);
            st.setArray(index, array);
        } else {
            st.setNull(index, sqlTypes()[0]);
        }
    }
    //implement equals, hashCode, and other methods 
}
```

* sqlTypes() 메소드 :  매핑된 컬럼에 대한 SQL Type을 반환해야 한다. 
  * SQL Type은 java.sql.Types에 정의되어 있다. 
* returnedClass() 메소드 : nullSafeGet() 메소드에 의해 반환될 클래스 타입을 지정해야 한다.
  * SQL Type과 같아야 한다
  * 사용자 지정 커스텀 클래스 (예를들면 Embedded 클래스)도 반환이 가능하다. 
* nullSafeGet() 메소드 : DB에서 값을 읽어온 후 자바 데이터 타입으로 처리한다. 
  * null을 반환할 수 있으므로 null 처리가 필요하다. 
* nullSafeSet() 메소드 :  DB에 값을 쓸 때, 자바 타입을 어떻게 쓸건지 처리한다. 
  * 여기서는 PostgreSQL text 타입의 배열을 지정한다.



그런 다음 CustomStringArrayType 클래스를 사용하여 문자열  배열을 PostgreSQL 텍스트 배열 에 매핑한다 .

```java
@Entity
public class User {
    //...

    @Column(columnDefinition = "text[]")
    @Type(type = "com.ys.arraymapping.CustomStringArrayType")
    private String[] roles;
  
}
```



## 2. hibernate-types 라이브러리로 매핑

깃허브 : https://github.com/vladmihalcea/hypersistence-utils

vladmihalcea의 오픈소스 라이브러리를 사용하면 쉽게 구현할 수 있다



여기서 다양한 데이터 타입들을 커스텀해서 지원해준다.

* 라이브러리를 추가하고 필요한것만 사용하면 된다.



### 의존성 추가

```kotlin
implementation("com.vladmihalcea:hibernate-types-52:2.21.1")
```

하이버네이트 버전에 따른 지원하는 버전이 다르므로, 깃허브 가서 보고 하이브네이트 버전에 맞는 라이브러리 버전을 선택하면 된다. 



`List<String>` 타입과 `String[]` 타입을 매핑하려면 다음과 같이 사용하면 된다.

```java
@TypeDefs({
    @TypeDef(
        name = "string-array",
        typeClass = StringArrayType.class
    ),
    @TypeDef(
    name = "list-array",
    typeClass = ListArrayType.class
		)
})
@Entity
public class User {
  
  @Id
  private Long id;
  
  private String name;
  @Type(type = "string-array")
  @Column(
        name = "roles",
        columnDefinition = "text[]"
  )
  private String[] roles;
		
  @Type(type = "list-array")
  @Column(
        name = "hobbies",
        columnDefinition = "text[]"
   )
  private List<String> hobbies;
  
}
```

Custom Type을 구현한것과 같이 CustomStringArrayType 과 유사하게,   
String  배열 에 대한 매퍼로서 `hibernate-types 라이브러리에서 제공하는 StringArrayType` 클래스를 사용할 수 있다.

또한, list-array 타입도 지원하므로 List에 대해서도 사용할 수 있다.

* 라이브러리에서 DateArrayType, EnumArrayType, DoubleArrayType 과 같은 몇 가지 편리한 매퍼도 추가적으로 제공한다. 





# hibernate-types 에서 지원하는 다양한 Array Types 예제

## 스키마와 도메인 모델 

event라는 Table이 있다. 이 Table의 스키마는 다음과 같다.

<img src="https://blog.kakaocdn.net/dn/8mo97/btrVjRV0sW8/onCXLLsdCGL7BNmGElYOFK/img.png" width = 450 height =300>

다음은 event table과 매핑되는 Entity Domain model이다.

<img src="https://blog.kakaocdn.net/dn/9I8Or/btrVjPjANXl/xFoaCkbzeEksqwrHhsrkw1/img.png" width = 450 height = 400>



다음과 같이 매핑할 수 있다.

```java
@Entity(name = "Event")
@Table(name = "event")
@TypeDef(
    name = "list-array",
    typeClass = ListArrayType.class
)
public class Event {
 
    @Id
    private Long id;
 
    @Type(type = "list-array")
    @Column(
        name = "sensor_ids",
        columnDefinition = "uuid[]"
    )
    private List<UUID> sensorIds;
 
    @Type(type = "list-array")
    @Column(
        name = "sensor_names",
        columnDefinition = "text[]"
    )
    private List<String> sensorNames;
 
    @Type(type = "list-array")
    @Column(
        name = "sensor_values",
        columnDefinition = "integer[]"
    )
    private List<Integer> sensorValues;
 
    @Type(type = "list-array")
    @Column(
        name = "sensor_long_values",
        columnDefinition = "bigint[]"
    )
    private List<Long> sensorLongValues;
 
    @Type(
        type = "io.hypersistence.utils.hibernate.type.array.ListArrayType",
        parameters = {
            @Parameter(
                name = ListArrayType.SQL_ARRAY_TYPE,
                value = "sensor_state"
            )
        }
    )
    @Column(
        name = "sensor_states",
        columnDefinition = "sensor_state[]"
    )
    private List<SensorState> sensorStates;
 
    @Type(type = "list-array")
    @Column(
        name = "date_values",
        columnDefinition = "date[]"
    )
    private List<Date> dateValues;
 
    @Type(type = "list-array")
    @Column(
        name = "timestamp_values",
        columnDefinition = "timestamp[]"
    )
    private List<Date> timestampValues;
 
    //Getters and setters omitted for brevity
}
```





https://vladmihalcea.com/postgresql-array-java-list/ 

이 사이트에 사용방법이 자주 업데이트 되므로 필요할때마다 가서 보면 된다.



### 참조

* https://vladmihalcea.com/postgresql-array-java-list/
* https://www.baeldung.com/java-hibernate-map-postgresql-array
* https://github.com/vladmihalcea/hypersistence-utils