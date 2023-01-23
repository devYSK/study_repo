# MySQL Spatial Data Types



* [docs](https://dev.mysql.com/doc/refman/8.0/en/spatial-types.html)

* Spatial data Types : 공간 데이터 타입

공간 데이터란 이 세상에 존재하는 위치 및 영역을 X, Y 좌표로 표현한 데이터



공간 데이터베이스란  X, Y 좌표로 구성된 공간 데이터 타입을 지원, 저장, 연산할 수 있는 공간 함수 기능을 제공해주는 데이터베이스이다.

일반적으로 사용하고 있는 RDBMS인 Oracle, MariaDB, MySQL, PostgreSQL 등에서 공간 데이터를 처리하기 위한 기능을 제공한다.

> 공간 데이터 타입과 함수는 Open Geospatial Consortium(OGC, [www.ogc.org](http://www.ogc.org/))에서 표준을 정의하고 있다.
>
> [표준 문서](http://portal.opengeospatial.org/files/?artifact_id=25354)

 

MySQL에서 제공하는 공간 데이터 타입 기준![image-20230119020535896](/Users/ysk/study/study_repo/mysql/images//image-20230119020535896.png)


|      | **공간데이터타입** | **정의**                                         | **SQL** **예**                                               |
| ---- | ------------------ | ------------------------------------------------ | ------------------------------------------------------------ |
| 1    | Point              | 좌표 공간에서 한 지점의 위치를 표시              | POINT (10 10)                                                |
| 2    | LineString         | 다수의 Point를 연결해주는 선분                   | LINESTRING (10 10, 20 25, 15 40)                             |
| 3    | **Polygon**        | 다수의 선분들이 연결되어 닫혀 있는 상태인 다각형 | POLYGON ((10 10, 10 20, 20 20, 20 10, 10 10))                |
| 4    | Multi-Point        | 다수 개의 Point 집합                             | MULTIPOINT (10 10, 30 20)                                    |
| 5    | Multi-LineString   | 다수 개의 LineString 집합                        | MULTILINESTRING ((10 10, 20 20), (20 15, 30 40))             |
| 6    | **Multi-Polygon**  | 다수 개의 Polygon 집합                           | MULTIPOLYGON ((( 10 10, 15 10, 20 15, 20 25, 15 20, 10 10 )) , (( 40 25, 50 40, 35 35, 25 10, 40 25 )) ) |
| 7    | GeomCollection     | 모든 공간 데이터들의 집합                        | GEOMETRYCOLLECTION ( POINT (10 10), LINESTRING (20 20, 30 40), POINT (30 15) ) |



## MySQL Spatial Data Type

MySQL 5.7부터 공간 데이터 타입을 지원한다. 이것을 활용해 위치 데이터를 인덱싱할 수 있다. 

- GEOMETRY
- POINT
- LINESTRING
- POLYGON

다른 데이터 유형

- MULTIPOINT
- MULTILINESTRING
- MULTIPOLYGON
- GEOMETRYCOLLECTION



**Geometry Type**

Geometry Type 지리적 특징(geographic)을 나타내는 단어이다.

다음과 같은 많은 속성이 있으며 모든 Geometry 하위 클래스에서 생성된 모든 기하학 값에 공통적이다.

| Name              | Description                                                  |
| :---------------- | :----------------------------------------------------------- |
| type              | Each geometry belongs to one of the instantiable classes in the hierarchy. |
| SRID              | SRID 는 Spatial Reference ID 의 줄임말로 공간 참조 식별자란 뜻이다. <br />특정 SRS 를 지칭하는 고유 번호를 의미하며, SRS-ID 와 SRID 는 동의어이다. <br />MySQL 서버에서는 SRS 고유 번호를 저장하는 컬럼의 이름으로 SRS_ID 로 사용하고 있으며, 함수의 인자나 식별자로 사용될 경우 SRID 로 사용한다. <br /> |
| coordinates(좌표) | 비어 있지 않은 모든 도형에는 최소한 한 쌍의 (X,Y) 좌표가 포함되며, 빈 도형에는 좌표가 없다. <br /> 좌표는 SRID와 관련이 있다. |
| MBR               | MBR(최소 경계 사각형, minimum bounding rectangle). 최소 및 최대(X,Y) 좌표로 구성된 경계 geometry이다.<br /> |
| dimension(차원)   | Geometry는 –1, 0, 1 또는 2의 차원을 가질 수 있다.<br/>비어 있는 Geometry의 경우 1 <br />길이와 면적이 없는 도형의 경우 0.<br/>길이가 0이 아니고 면적이 0인 도형의 경우 1.<br/>영역이 0이 아닌 형상의 경우 2. |



**[MBR](https://dev.mysql.com/doc/refman/5.7/en/spatial-relation-functions-mbr.html)**

- Minimum Bounding Rectangles의 약자로, 최소 경계 사각형이라는 뜻 .
- 지도 상의 임의의 사각형 구역
- 공간 관련 연산시 사용하는 용어.

**[POINT](https://dev.mysql.com/doc/refman/5.7/en/gis-class-point.html)**

- 지도 상의 경도(longitude), 위도(latitude) 값을 표현하는 객체.
- MySQL의 Spatial Data Type 중 하나.

**[LINESTRING](https://dev.mysql.com/doc/refman/5.7/en/gis-class-linestring.html)**

- 지도 상의 하나의 선(line을 의미하며, 일련의 Point들로 이루어진 객체.
- MySQL의 Spatial Data Type 중 하나.





## 공간 데이터 관련 용어

**OGC**

OGC 는 Open Geospatial Consortium (개방형 공간 정보 컨소시엄) 의 약자로 위치 기반 데이터에 대한 표준을 수립하는 단체 이다. 



**OepnGIS**

OGC 에서 제정한 지리 정보 시스템(GIS, Geographic Information System) 표준으로, WKT 나 WKB 같은 지리 정보 데이터를 표기하는 방법과 저장하는 방법, 그리고 SRID 와 같은 표준을 포함한다. OpenGIS 표준을 준수하는 응용 프로그램의 위치 기반 데이터는 `상호 변환 없이 교환 가능하도록 설계`되어 있다.



**SRS **

SRS 는 Spatial Reference System 약자로 한글로는 "공간 참조 시스템" 으로 번역이 되며, 좌표계 라고 이해하면된다. SRS 에는 크게 GCS(Geographic Coordinate System) 와 PCS(Projected Coordinate System) 으로 구분된다.

* MySQL 서버에서 지원하는 SRS 는 5000 여개가 넘으며, 지원하는 SRS 에 대한 정보는 information_schema 의 st_spatial_reference_systems 딕셔너리 테이블에서 확인 할 수 있다.

```sql
mysql> desc information_schema.st_spatial_reference_systems;
+--------------------------+---------------+------+-----+---------+-------+
| Field                    | Type          | Null | Key | Default | Extra |
+--------------------------+---------------+------+-----+---------+-------+
| SRS_NAME                 | varchar(80)   | NO   |     | NULL    |       |
| SRS_ID                   | int unsigned  | NO   |     | NULL    |       |
| ORGANIZATION             | varchar(256)  | YES  |     | NULL    |       |
| ORGANIZATION_COORDSYS_ID | int unsigned  | YES  |     | NULL    |       |
| DEFINITION               | varchar(4096) | NO   |     | NULL    |       |
| DESCRIPTION              | varchar(2048) | YES  |     | NULL    |       |
+--------------------------+---------------+------+-----+---------+-------+
```

* SRS_ID 컬럼 : 해당 좌표계를 지칭하는 고유 번호가 저장
* DEFINITION 컬럼 : 해당 좌표계가 어떤 좌표계인지에 대한 정의가 저장
  * DEFINITION 컬럼의 값은 **항상 GEOGCS 또는 PROJCS 로 시작**한다.
  * GEOGCS 는 지리 좌표계(GCS) 를 의미. 특정 지역을 위해서 만들어진 좌표계 - 대략 483개 지원
  * PROJCS 는 투영 좌표계(PCS) 를 의미. WGS 84 좌표계는 지구 전체를 구체 형태로 표현- 4668개 지원

SRS_ID 컬럼의 고유 번호는 컬럼에 데이터를 저장 하거나 검색할 때 필요하기 때문에 사용자는 저장하는 공간 데이터가 어느 좌표계를 사용하는지를 알고 있어야 한다.  
WGS 84 좌표계를 사용하는 위치 정보에서 특정 위치를 표시할 때에는 "**POINT(위도 경도)**" 와 같이 표현 해야 한다.

나열 된 순서대로 **첫번째는 X 축**, **두번째는 Y 축**에 해당 하며, **ST_X() 함수는 위도 값을 반환** 하며, **ST_Y() 함수는 경도 값을 반환**.

* **Lat** : 위도(latitude)  
* **Lon** : 경도(longitude)



**GCS**

GCS는 지구 구체상의 특정 위치나 공간을 표현하는 좌표계를 의미. 위도(Latitude) 와 경도(Longitude) 와 같이 각도(Angular unit) 단위의 숫자로 표시.

**PCS**

PCS 는 구체 형태의 지구를 종이 지도와 같은 평면으로 투영(프로젝션, Projection) 시킨 좌표계 의미하며, 보통 미터(meter) 와 같은 선형 적인 단위로 표시.



> GCS 는 지구와 같은 구체 표면에서 특정 위치를 정의 하는 반면에, PCS 는 GCS 위치 데이터를 2차원 평면인 종이에 어떻게 표현을 할지를 정의한다.
>
> 동일한 지점이라도 어떤 SRS(공간 좌표계)를 사용하느냐에 따라 표시 방법이 달라진다.


***\*SRID 와 SRS-ID\****

SRID 는 `Spatial Reference ID 의 줄임말`로 `특정 SRS 를 지칭하는 고유 번호`를 의미하며, SRS-ID 와 SRID 는 같은의미이다.
MySQL 서버에서는 SRS 고유 번호를 저장하는 컬럼의 이름으로 SRS_ID 로 사용하고 있으며, 함수의 인자나 식별자로 사용될 경우 SRID 로 사용된다.

<details>
  <summary> SRID 값에 따른 차이 - 접기/ 펼치기 </summary>
SRID 를 0으로 설정된 데이터에 대한 거리 계산은 실제 지구 구면체 상의 거리 계산을 하는 것이 아니다, SRID 이 0 인 공간 데이터는 단위를 가지지 않기 때문이다

아래 예제에서는 두 점 사이의 거리를 계산하는 내용으로 st_pointfromtext() 함수는 MySQL 서버의 공간 데이터(POINT 객체)를 생성하는 함수이다.

이 함수의 첫 번째 파라미터는 점의 위치이며 두 번재 파라미터는 첫 번째 파라미터에 명시된 점이 사용하는 공간 좌표계(SRS) 의 아이디(SRID) 값을 입력 한다.

```sql
-- 평면 좌표계(SRID=0) 를 사용하는 공간 데이터
mysql> select st_distance(st_pointfromtext('POINT(0 0)',0),
              st_pointfromtext('POINT(1 1)',0)) as distance;
+--------------------+
| distance           |
+--------------------+
| 1.4142135623730951 |
+--------------------+
-- 웹 기반 지도 좌표계(SRID=3857) 를 사용하는 공간 데이터
mysql> select st_distance(st_pointfromtext('POINT(0 0)',3857),
              st_pointfromtext('POINT(1 1)',3857)) as distance; 
+--------------------+
| distance           |
+--------------------+
| 1.4142135623730951 |
+--------------------+
-- WGS 84 지리 좌표계(SRID=4326) 를 사용하는 공간 데이터
mysql> select st_distance(st_pointfromtext('POINT(0 0)',4326),
              st_pointfromtext('POINT(1 1)',4326)) as distance; 
+--------------------+
| distance           |
+--------------------+
| 156897.79947260793 |
+--------------------+

```

위의 예제의 첫 번째는 **SRID 가 0 이며** 그렇기 때문에 st_distance 함수의 결과가 1.4142135623730951 라는 수치가 나왔으며 해당 값은 단위가 없으며 피타고라스의 정리에 의한 수식으로 계산된 거리 값이다. 즉 실생활에서 사용할 수 있는 수치가 아니다. 

두 번째 와 세 번째는 SRID 가 0 이 아닌 값으로 명시화 되어있기 때문에 st_distance 함수의 결과값이 단위를 가지는 결과 값이 되게 되며 단위는 미터(meter)이다.

**첫 번째와 두 번째**는 SRID 가 다르지만 둘다 지구 구체를 고려하지 않고 평면에서의 거리를 계산한 것이기 때문에 거리 계산 값이 동일한 것이며, **세 번째**는 지리 좌표계를 사용하였으며 그에 따라서 지구 구체 기반이기 때문에 거리 값이 첫 번째,두 번째와 완전히 다르게 계산 되어 출력 된 것.

MySQL 8.0 에서 SRID 를 별도로 명시하지 않거나 SRID 을 0으로 지정한 공간 데이터라고 해서 실제로 km 이나 meter 단위로 계산을 못하는 것은 아니다. 다만 MySQL 이 자동으로 필요한 값을 계산할 수 없다는 것을 의미한다.

MySQL8.0 에서 지원되는 공간 함수들이 모두 SRID를 지원하는 것은 아님으로 많은 공간 함수는 SRID 가 0 인 경우에만 작동할 수 있으며 일부 함수만 WGS 84 좌표계 데이터를 처리 할 수 있는 상태이다.

</details>

  



**WKT 와 WKB**

**WKT(Well-Known Text format)** 와 **WKB(Well-Known Binary format)** 은 OGC 에서 제정한 OpenGIS 에서 명시한 위치 좌표의 표현 방법이다. 

* **WKT**는 사람의 눈으로 쉽게 식별할 할 수 있는 텍스트 포맷

* **WKB** 는 컴퓨터에 저장할 수 있는 형태의 이진 포맷의 저장 표준

둘다 **OpenGIS 표준**에 명시된 공간 데이터 표기 방법이며, MySQL 서버가 내부적으로 공간 데이터를 처리하거나 **저장할 때는 WKT 나 WKB 포맷으로 저장하지는 않는다.**

MySQL 서버가 내부적으로 사용하는 이진 데이터 포맷은 **WKB 포맷과 흡사하지만 완전히 동일 하지는 않다.**


***\*MBR 과 R-Tree\****

MBR은 Minimum Bounding Rectangle 약자로 어떤 도형을 감싸는 최소의 사각 상자를 의미한다.

MBR 이 중요한 이유로 **공간 인덱스(Spatial Index)가 도형의 포함 관계를 이용하기 때문**이다.

이렇게 도형들의 포함 관계를 이용해서 만들어진 인덱스를 **R-Tree** 라고 한다.



## 공간 관계 함수 ( Spatial Relation Functions )

공간 데이터를 이용해 연산을 할 수 있는 공간 함수.

공간 관계 함수는 두 공간 객체 간의 관계를 일반 데이터 타입(Boolean 또는 숫자)으로 반환해주는 함수

![image-20230119025924500](/Users/ysk/study/study_repo/mysql/images//image-20230119025924500.png)

| **공간 관계 함수**                                   | **설명**                                                     |
| ---------------------------------------------------- | ------------------------------------------------------------ |
| ST_Equals (g1 Geometry, g2 Geometry)  : Boolean      | g1과 g2가 동일하면 True를 반환하고 상이하다면 False를 반환   |
| ST_Disjoint (g1 Geometry, g2 Geometry)    : Boolean  | g1과 g2가 겹치는 곳 없다면 True를 반환하고, 겹치는 곳이 있으면 False를 반환 |
| ST_Within (g1 Geometry, g2 Geometry)  : Boolean      | g1가 g2 영역 안에 포함된 경우 True를 반환하고 그렇지 않은 경우 False를 반환 |
| ST_Overlaps (g1 Geometry, g2 Geometry)   : Boolean   | g1과 g2 영역 중 교집합 영역이 존재하는 경우 True를 반환하고 존재하지 않는 경우 False를 반환 |
| ST_Intersects (g1 Geometry, g2 Geometry)   : Boolean | g1과 g2 영역 간에 교집합이 존재하는 경우 True를 반환하고 그렇지 않은 경우 False를 반환 |
| ST_Contains (g1 Geometry, g2 Geometry)  : Boolean    | g2가 g1 영역 안에 포함된 경우 True를 반환하고 그렇지 않은 경우 False를 반환 |
| ST_Touches (g1 Geometry, g2 Geometry)  : Boolean     | g1과 g2가 경계 영역에서만 겹치는 경우 결과 값으로 True를 반환하며 경계 영역 외에서 겹치거나 겹치는 곳이 없다면 False를 반환 |
| ST_Distance (g1 Geometry, g2 Geometry)  : Double     | g1과 g2간의 거리를 반환                                      |



* ST_GeomFromText(string) : Text 값을 Geometry Type으로 변환
  * ST_GeomFromText('POINT(1 1)')
  * ST_GeomFromText('LINESTRING(2 1, 6 6)')
  * ST_GeomFromText('POLYGON((0 5, 2 5, 2 7, 0 7, 0 5))')
* ST_AsText(geometry) : Gemetry Type을 text로 변환





## 공간 연산 함수 ( Spatial Operator Functions )

공간 연산 함수는 두 공간 객체의 연산 결과로 새로운 공간 객체를 반환해주는 함수이다.

![image-20230119025112886](/Users/ysk/study/study_repo/mysql/images//image-20230119025112886.png)

| **공간 연산 함수**                                     | **설명**                                  |
| ------------------------------------------------------ | ----------------------------------------- |
| ST_Intersection (g1 Geometry, g2 Geometry)  : Geometry | g1과 g2의 교집합인 공간 객체를 반환       |
| ST_Union (g1 Geometry, g2 Geometry)  : Geometry        | g1과 g2의 합집합인 공간 객체를 반환       |
| ST_Difference (g1 Geometry, g2 Geometry)  : Geometry   | g1과 g2의 차집합인 공간 객체를 반환       |
| ST_Buffer (g1 Geometry, d Double )  : Geometry         | g1에서 d 거리만큼 확장된 공간 객체를 반환 |
| ST_Envelope (g1 Geometry)  : Polygon                   | g1을 포함하는 최소 MBR인 Polygon을 반환   |
| ST_StartPoint (l1 LineString)  : Point                 | l1의 첫 번째 Point를 반환                 |
| ST_EndPoint (l1 LineString)  : Point                   | l1의 마지막 Point를 반환                  |
| ST_PointN (l1 LineString)  : Point                     | l1의 n 번째 Point를 반환                  |



## 예제 - MySQL

기준 좌표 x,y로 부터 n KM 떨어진 모든 범위의 location 데이터를 조회하는 쿼리

```mysql
# 기준 좌표 : x,y
# 기준 좌표의 북동쪽으로 nKM에 위치한 좌표 : x1, y1
# 기준 좌표의 남서쪽으로 nKM에 위치한 좌표 : x2, y2

SELECT *
FROM location as g
WHERE MBRCONTAINS(ST_LINESTRINGFROMTEXT('LINESTRING(x1 y1, x2 y2)'), g.location);
```

- MBRContains(g1, g2) : g1의 MBR에 g2의 MBR이 포함되는지 검사하는 함수
- ST_LINESTRINGFROMTEXT : WKT 표현식으로 표현된 문자열을 이용해 LINESTRING 객체를 만드는 함수

- 기준 위치로부터 nKM 떨어진 북동쪽 및 남서쪽 좌표를 이은 대각선(LINESTRING)의 MBR은 직사각형.
- 따라서 MBRContains(g1, g2)는 g1의 직사각형안에 g2 데이터가 포함되는지 찾는 연산.



## Practice In Spring Boot + JPA



의존성 추가

```groovy
// https://mvnrepository.com/artifact/org.hibernate/hibernate-spatial
implementation 'org.hibernate:hibernate-spatial'
```

 property 수정

```java
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/test
    username: root
    password: 
  jpa:
    database: mysql
    database-platform: org.hibernate.spatial.dialect.mysql.MySQL56InnoDBSpatialDialect
    hibernate:
      ddl-auto: update
    properties:
      hibernate.dialect : org.hibernate.spatial.dialect.mysql.MySQL8SpatialDialect
      ## 커스텀시. hibernate.dialect: com.ys.common.hibernate.MySQLDialectCustom
```



Entity, Repository 추가

Entity에서 Point 타입 선언시 반드시 `com.vividsolutions.jts.geom` 패키지에 존재하는 Point 클래스를 Import 

* 대체재로 org.locationtech.jts.geom도 있다. 

```groovy
// https://mvnrepository.com/artifact/org.locationtech.jts/jts-core
implementation 'org.locationtech.jts:jts-core:1.19.0'
```

```java
import com.vividsolutions.jts.geom.Point;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class User {
    
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @Column
  private String name;

  @Column // com.vividsolutions.jts.geom
  private Point location;
}
```



Point 데이터 DB에 저장하는 방법

```java
import com.vividsolutions.jts.io.WKTReader;

public void saveUser() {
    String name = "name"
    Double latitude = 36.543;
    Double longitude = 127.123;
    String pointWKT = String.format("POINT(%s %s)", longitude, latitude);

    // WKTReader를 통해 WKT를 실제 타입으로 변환.
    Point point = (Point) new WKTReader().read(pointWKT);
    User user = new User(name, point);
    userRepository.save(driverLocation);
}
```



Jpa AttributeConverter도 사용 가능

```java
import javax.persistence;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

@Converter 
public static class PointConverter implements AttributeConverter<Point, String> {

  private static WKTReader wktReader = new WKTReader();

  @Override
  public String convertToDatabaseColumn(Point attribute) {  
    return attribute.toText();  
  }

  @Override
  public Point convertToEntityAttribute(String dbData) {
    try {
      return (Point) wktReader.read(dbData);
    } catch (ParseException e) {
      throw new RuntimeException("convert fail", e); 
    } 
  }  
}

```

Converter Column에 적용

```java
import com.vividsolutions.jts.geom.Point;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverride(name = "location", column = @Column(columnDefinition = "point SRID 4326 NOT NULL, spatial index user_point_geo_index (location)")) // ddl 시 index 적용 
class User {
    
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @Column
  private String name;
	
  @ColumnTransformer(write = "ST_PointFromText(?, 4326)", read = "ST_AsText(location)")
  @Convert(converter = PointConverter.class)
  @Column(name = "location",
          columnDefinition = "point SRID 4326",
          nullable = false) // com.vividsolutions.jts.geom
  private Point location;
  
}
```



## JpaRepository 실습



### WithIn Example

```java
public interface UserRepository extends JpaRepository<User, Long> {
   @Query(value = "select id, ST_AsText(location) as location " +
            "from user " +
            " where ST_Within(ST_PointFromText(:#{#latLng.toPointText()}, 4326), user.location) ",
            nativeQuery = true)
    Optional<EmdArea> findByLatLngWithin(@Param("latLng") LatLng latLng);
  
}
```



### MbrContains Example

```java
public interface UserRepository extends JpaRepository<User, Long> {

  @Query(value = "select id, name, ST_AsText(location) as location 
            " from user" +
            " where MBRContains(location, ST_PointFromText(:#{#latLng.toPointText()}, 4326)) " +
            "and emd.area_name = :emdName",
            nativeQuery = true)
    Optional<EmdArea> findByLngLatMBRContains(@Param("latLng") LatLng latLng);
}

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class LatLng {

    private double latitude; // 위도
    private double longitude; // 경도

    private Point point;

    private String pointText;

    private String pointTextInSingleQuote;

    public static LatLng of(double latitude, double longitude) {
        return new LatLng(latitude, longitude);
    }

    public LatLng(double latitude, double longitude) {
        verifyLatitudeThrowIllegalArgument(latitude);
        this.latitude = latitude;
        this.longitude = longitude;
        initPointText();
        initPointTextInSingleQuote();

        try {
            this.point = (Point) new WKTReader().read(pointText);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void verifyLatitudeThrowIllegalArgument(double latitude) {
        if (latitude < -90 || latitude > 90) {
            throw new IllegalStateException(latitude + " which is out of range. It must be within [-90.000000, 90.000000].");
        }
    }

    private void initPointText() {
        this.pointText = String.format("POINT(%f %f)", latitude, longitude);
    }
    private void initPointTextInSingleQuote() {
        this.pointTextInSingleQuote = String.format("'POINT(%f %f)'", latitude, longitude);
    }

    // https://dba.stackexchange.com/questions/182136/does-mysql-store-point-datatypes-as-lat-lng-or-lng-lat
    // mysql은 x, y -> lat lng
    public String toPointText() {
        return point.toText(); // MYSQL은 Lat, Long임
    }

    public String toLatLngString() {
        return "LatLng{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
```



### 주어진 polygon의 중앙 좌표 쿼리

```java
public interface UserRepository extends JpaRepository<User, Long> {
  @Query(value = "select st_astext(st_srid(st_centroid(st_srid(user.polygon, 0)), 4326))" + 
         "from user "      
         nativeQuery = true)
  Optional<User> findCenterPoint();
}
```



### ST_Distance_Sphere - 특정 m(meter) 아래로 조회

* st_distance_sphere (좌표1, 좌표2) -> 두 좌표간 거리(단위: m)

```java
public interface UserRepository extends JpaRepository<User, Long> {
  @Query(value = "select u.id, u.name from user u " +
        "where ST_Distance_Sphere(ST_PointFromText(:#{#point.toPointText()}, 4326), p.location) <= :searchDistanceMeter "
        , nativeQuery = true)
    List<Long> findByPoint(@Param("point") LatLng point, 
        @Param("searchDistanceMeter") int searchDistanceMeter);
}
```







### 참조

* https://hoing.io/archives/5457
* https://www.w3resource.com/mysql/mysql-spatial-data-types.php
* https://sparkdia.tistory.com/24
* https://wooody92.github.io/project/JPA%EC%99%80-MySQL%EB%A1%9C-%EC%9C%84%EC%B9%98-%EB%8D%B0%EC%9D%B4%ED%84%B0-%EB%8B%A4%EB%A3%A8%EA%B8%B0/

* https://guswns1659.github.io/spring/Spring)-%EC%9C%84%EC%B9%98-%EB%8D%B0%EC%9D%B4%ED%84%B0-%EB%8B%A4%EB%A3%A8%EA%B8%B0/

