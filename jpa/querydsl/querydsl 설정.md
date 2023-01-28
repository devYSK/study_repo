# QueryDSL 설정방법



SpringBoot 2.x대와 SpringBoot 3.x대의 버전 설정 방법이 다르므로 둘에 나눠서 정리 하도록 한다.

# SpringBoot 2.x버전대 설정 방법

#### 1. Querydsl 버전을 plugins 위에 추가하고 plugins에 querydsl 플러그인을 추가한다.

```groovy
buildscript {
    ext {
        queryDslVersion = "5.0.0"
    }
}

plugins {
    id 'java'
    id 'org.springframework.boot' version '2.7.7'
    id 'io.spring.dependency-management' version '1.1.0'
    id "com.ewerk.gradle.plugins.querydsl" version "1.0.10" // querydsl
}
```

* buildscript는 project의 플러그인 의존성(라이브러리) 관리를 위한 설정이며 ext는 build.gradle 에서 사용하는 전역 변수를 설정하겠다는 의미이다.

 

#### 2 dependencies에 querydsl 관련 의존성 추가

```groovy
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    ...
    implementation 'mysql:mysql-connector-java'
		...    

    // querydsl 디펜던시 추가
    implementation "com.querydsl:querydsl-jpa:${queryDslVersion}"
    implementation "com.querydsl:querydsl-apt:${queryDslVersion}"
}
```

* 위에서 설정한 전역 변수를 통한 버전 을 명시한다. 

#### 3. querydsl 추가 설정

```groovy
// querydsl 사용할 경로를 지정한다. 지정한 부분은 .gitignore에 포함되므로 git에 올라가지 않는다. 
def querydslDir = "$buildDir/generated/'querydsl'"
 
// JPA 사용여부 및 사용 경로 설정
querydsl {
    jpa = true
    querydslSourcesDir = querydslDir
}
 
// build시 사용할 sourceSet을 추가하도록 설정
sourceSets {
    main.java.srcDir querydslDir
}
 
// querydsl 컴파일 시 사용할 옵션 설정
compileQuerydsl {
    options.annotationProcessorPath = configurations.querydsl
}
 
// querydsl이 compileClassPath를 상속하도록 설정
configurations {
    compileOnly {
        extendsFrom annotationProcessor
    }
    querydsl.extendsFrom compileClasspath
}
```

* SourceSets은 Java 소스와 리소스 파일의 논리적인 그룹
* 하나 이상의 Source 디렉터리를 Gradle에서 처리를 하기 위해서 SourceSets에 Source 디렉터리를 등록



최종 스크립트

```groovy
buildscript {
    ext {
        queryDslVersion = "5.0.0"
    }
}

plugins {
    id 'java'
    id 'org.springframework.boot' version '2.7.7'
    id 'io.spring.dependency-management' version '1.1.0'
    id "com.ewerk.gradle.plugins.querydsl" version "1.0.10" // querydsl
}

group = 'com.ys'
version = '0.0.1-SNAPSHOT'
sourceCompatibility = '11'

configurations {
    compileOnly {
        extendsFrom annotationProcessor
    }

}

repositories {
    mavenCentral()
}

ext {
    set('snippetsDir', file("build/generated-snippets"))
    queryDslVersion = "5.0.0"
}

dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-security'
    implementation 'org.springframework.boot:spring-boot-starter-validation'
    implementation 'org.springframework.boot:spring-boot-starter-web'

    // querydsl
    implementation "com.querydsl:querydsl-jpa:${queryDslVersion}"
    implementation "com.querydsl:querydsl-apt:${queryDslVersion}"
}

// querydsl 사용할 경로 지정. 현재 지정한 부분은 .gitignore 에 포함되므로 git 에 올라가지 않는다.
def querydslDir = "$buildDir/generated/'querydsl'"

querydsl { // JPA 사용여부 및 사용 경로 설정
    jpa = true
    querydslSourcesDir = querydslDir
}

sourceSets { // build시 사용할 sourceSet 추가 설정
    main.java.srcDir querydslDir
}

compileQuerydsl { // querydsl 컴파일 시 사용할 옵션 설정
    options.annotationProcessorPath = configurations.querydsl
}

// querydsl이 compileClassPath를 상속하도록 설정
configurations {
    compileOnly {
        extendsFrom annotationProcessor
    }
    querydsl.extendsFrom compileClasspath
}

compileQuerydsl.doFirst {
    if(file(querydslDir).exists() )
        delete(file(querydslDir))
}

compileQuerydsl {
    options.annotationProcessorPath = configurations.querydsl
}

// gradle clean 시에 QClass 디렉토리 삭제
clean {
    delete file(generated)
}

// ---- querydsl 설정 끝
```



IDE를 이용해서 gradle task 중 compileQuerydsl을 실행시키거나

```
./gradlew compileQuerydsl
```

명령어를 실행시켜 build해야 한다.



> `Entity`가 변경되었다면 이 과정을 다시 수행해야 한다.





# SpringBoot 3.x 버전대 설정



```groovy
plugins {
    id 'java'
    id 'org.springframework.boot' version '3.0.0'
    id 'io.spring.dependency-management' version '1.1.0'
}

group = 'com.ys'
version = '0.0.1-SNAPSHOT'
sourceCompatibility = '17'

configurations {
    compileOnly {
        extendsFrom annotationProcessor
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-web'

  	// querydsl 설정	
    implementation 'com.querydsl:querydsl-jpa:5.0.0:jakarta'
	  annotationProcessor "com.querydsl:querydsl-apt:${dependencyManagement.importedProperties['querydsl.version']}:jakarta"
	  annotationProcessor "jakarta.annotation:jakarta.annotation-api"
	  annotationProcessor "jakarta.persistence:jakarta.persistence-api"

}

tasks.named('test') {
    useJUnitPlatform()
}

// Querydsl 설정부
def querydslDir = 'src/main/generated'

// querydsl QClass 파일 생성 위치를 지정
tasks.withType(JavaCompile) {
    options.getGeneratedSourceOutputDirectory().set(file(generated))
}

// java source set 에 querydsl QClass 위치 추가
sourceSets {
    main.java.srcDirs += [ generated ]
}

// gradle clean 시에 QClass 디렉토리 삭제
clean {
    delete file(querydslDir)
}
```

스프링 부트 3.0 부터는 javax 패키지가 jakarta로 변경되었으므로 의존성을 jakarta로 가져와야 한다. 



src/main/generated에 QClass가 Git에 올라가지 않도록 .gitignore에 추가해야 한다

```
**/generated
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
> error: cannot find symbol
>
> Execution failed for task ':compileQuerydsl'.

  같은 에러 발생시에는 Preference -> Build.Execution, Deployment -> Build Tools -> gradle

* build and run using : Intellij IDEA
* Run tests using : Intellij IDEA

로 설정하고 다시 테스트하면 테스트가 통과한다. 

![image-20230118030906461](/Users/ysk/study/study_repo/jpa/querydsl/images//image-20230118030906461.png)
