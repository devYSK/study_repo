# 인프런 - 코틀린 스프링 API 호출

* https://www.inflearn.com/course/%EC%BD%94%ED%8B%80%EB%A6%B0-%EC%8A%A4%ED%94%84%EB%A7%81-api%ED%98%B8%EC%B6%9C-%EC%9E%85%EB%AC%B8/dashboard





# build.gradle.kts 세팅

```kotlin
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.1.0"
    id("io.spring.dependency-management") version "1.1.0"
    kotlin("jvm") version "1.8.21"
    kotlin("plugin.spring") version "1.8.21"
    kotlin("plugin.jpa") version "1.8.21"
}

group = "com.ys"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

```



# JPA 사용하기 위해 allOpen, noArg 추가

- allopen 플러그인은 자동으로 모든 클래스(프로퍼티, 함수까지)를 open 시켜줍니다.
- 매번 클래스와 프로퍼티에 `open` 키워드를 넣는 반복작업을 해결 해줌
- Hibernate에서 엔터티 클래스는 final일 수 있지만 lazy loading을 위한 프록시 생성불가

* 코틀린의 모든 클래스는 final 이며, 상속을 위해서 명시적으로 open을 붙여야함

plugin.spring에서 Open 해주는 것 외에 추가로 Open 해줄 것 명시

```kotlin
allOpen {
	annotation("jakarta.persistence.Entity")
	annotation("jakarta.persistence.MappedSuperclass")
	annotation("jakarta.persistence.Embeddable")
}
```

noArg

* 매개변수가 없는 생성자를 자동으로 추가

```kotlin
noArg {
annotation("jakarta.persistence.Entity")
}
```



## NoArg 플러그인이 하는 일

- `@Entity`, `@Embeddable`, `@MappedSuperClass` 어노테이션이 붙은 클래스에 자동으로 기본 생성자를 만들어줌
- 덕분에 Hibernate의 Reflection을 사용 할 수 있게된다.

```kotlin
plugins {
 	kotlin("plugin.noarg") version kotlinVersion
 }
```

or

```kotlin
plugins {
  id "org.jetbrains.kotlin.plugin.noarg" version "1.5.30"
}
```



# kakao developers 써보기

* https://developers.kakao.com/docs/latest/ko/daum-search/dev-guide#search-blog

