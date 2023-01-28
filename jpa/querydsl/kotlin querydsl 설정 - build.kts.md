# Kotlin Querydsl 설정



스프링부트 2.7.x 버전대 기준이다.

## build.gradle.kts 전체 파일

```kotlin
// build.gradle.kts
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinVersion = "1.6.21"

    id("org.springframework.boot") version "2.7.7"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    kotlin("plugin.jpa") version kotlinVersion

		// ✅ KAPT(Kotlin Annotation Processing Tool)를 설치
    kotlin("kapt") version kotlinVersion
		// ✅ Intellij에서 사용할 파일을 생성하는 플러그인
    idea
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
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
		annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    testImplementation("org.springframework.boot:spring-boot-starter-test")	
  
		// ✅ querydsl 의존성 추가
    implementation("com.querydsl:querydsl-jpa:5.0.0")
		// ✅ KAPT(Kotlin Annotation Processing Tool)를 설정
		//    kotlin 코드가 아니라면 kapt 대신 annotationProcessor를 사용
		//    JPAAnnotationProcessor를 사용하기 위해 마지막에 :jpa를 붙여야 한다.
    kapt("com.querydsl:querydsl-apt:5.0.0:jpa")
    
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

// ✅ QClass를 사용할 수 있도록 경로에 추가
idea {
    module {
        val kaptMain = file("build/generated/source/kapt/main")
        sourceDirs.add(kaptMain)
        generatedSourceDirs.add(kaptMain)
    }
}
```

kapt("com.querydsl:querydsl-apt:5.0.0:jpa") 는 JPAAnnotationProcessor를 적용하는 부분이다. 

* [공식문서](http://querydsl.com/static/querydsl/latest/reference/html/ch02.html#jpa_integration)에 JPAAnnotationProcessor 소개문.

>  The JPAAnnotationProcessor finds domain types annotated with the javax.persistence.Entity annotation and generates query types for them.

JPAAnnotationProcessor는 @Entity 어노테이션이 붙은 도메인을 찾아서 해당 도메인의 쿼리 타입을 생성한다.



## QClass 생성

```sh
./gradlew clean compileKotlin
```



## 에러 해결

```
오류: 기본 클래스 org.gradle.wrapper.GradleWrapperMain을(를) 찾거나 로드할 수 없습니다.
원인: java.lang.ClassNotFoundException: org.gradle.wrapper.GradleWrapperMain
```



라는 에러 발생시에는  .gradle/wrapper/ 아래에 두 파일이 존재하지 않아서 그렇다. 

- gradle-wrapper.jar

* gradle-wrapper.properties

 

gradle build 혹은 gradle wrap을 하지 않았으면 gradle-wrapper.jar이 누락되어 있으므로 

 `gradle wrap` 명령이 실행 후에 `./gradlew clean compileQuerydsl` 을 실행한다



### 테스트코드 에러 - Execution failed for task ':compileQuerydsl'.

>  Task :compileQuerydsl FAILED
>
>  error: cannot find symbol
>
>  Execution failed for task ':compileQuerydsl'.

  같은 에러 발생시에는 Preference -> Build.Execution, Deployment -> Build Tools -> gradle

* build and run using : Intellij IDEA
* Run tests using : Intellij IDEA

로 설정하고 다시 테스트하면 테스트가 통과한다. 

<img src="https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2F850Vf%2FbtrWx7V4cwx%2F9fKfCLAnKpqgSiNfUNAHgk%2Fimg.png">
