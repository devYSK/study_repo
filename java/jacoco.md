# JaCoCo

**JaCoCo**는 **자바 코드 커버리지를 체크하는 데에 사용되는 오픈소스 라이브러리** 이다.
테스트를 실행하고, 그 커버리지 결과를 html 파일이나 csv, xml 파일을 통해서 시각화 하여 볼 수 있다.
또한, 결과에 대한 기준치를 적용하여 기준을 만족시키지 못 하면 배포, 커밋  빌드 실패 등을 적용 할 수 있다.

특히 코드정적분석도구인 sonarqube와 연계하여 사용하는 경우가 많다.

Jacoco는 Java 바이트 코드를 기반으로 정적으로 커버리지를 측정해준다.

* http://www.semdesigns.com/Company/Publications/TestCoverage.pdf
  * TestCoverage 측정 방법에 대한 논문이다. 



> 공식 홈페이지 : https://www.jacoco.org/
>
> 릴리즈 버전 : https://www.eclemma.org/jacoco/
>
> docs : https://www.jacoco.org/jacoco/trunk/doc/
>
> JaCoCo의 버전은 [Maven Central Repository](https://search.maven.org/search?q=g:org.jacoco)를 통해 확인할 수 있다.



**코드 커버리지란**

> Coverage의 사전적 뜻 : 적용 범위
>
> 코드 커버리지란, 소프트웨어의 Test Case가 얼마나 충족되었는지, TestCode가 Production Code를 얼마나 실행했는지를 백분율(%)로 나타내는 지표 중 하나이다. Test를 진행하였을 때, '코드 자체가 얼마나 실행되었는지'를 나타내는것이고 이는 수치를 통해 확인할 수 있다.
>
> 즉 Test Code가 Production 코드를 얼마나 검증하고 있는지를 수치(혹은 %)로 나타내고 이 커버리지를 통해 현재 작성된 Test Code의 수가 충분한 것인지 논의할 수 있다.
>
> * [코드 커버리지란](https://0soo.tistory.com/174) 



Jacoco는 다른 도구보다 레퍼런스가 많으며, 사용하기가 쉬우므로 자주 사용되는 도구이다.

> 레퍼런스의 중요성
>
> 새로운 기술을 처음 도입하는 상황에서 **레퍼런스의 유무**는 정말 중요하다. 
>
> 기술을 도입하는 과정에서는 발생하는 여러 시행착오들을 레퍼런스가 많이 존재한다면 빠르게 해결할 수 있기 때문이다



JaCoCo가 가지는 **특징**으로는

- **Line, Branch Coverage를 제공**한다.
- **코드 커버리지 결과**를 보기 좋도록 **파일 형태로 저장**할 수 있다.
  - html, xml, csv 등으로 Report를 생성할 수 있다.
- 설정한 **커버리지 기준을 만족하는지 확인**하여 빌드 실패, 배포 실패 등을 할 수 있다.



Gradle 기준으로 Single Module과 MultiModule 두가지 방법을 정리하고자 한다.



# JaCoCo 설정 - Gradle 기준 (Single Module)

* [Jacoco User Guide - gradle](https://docs.gradle.org/current/userguide/jacoco_plugin.html#header)

```groovy
plugins {
    id 'java'
    id 'org.springframework.boot' version '2.7.6'
  	...
    id 'jacoco'
}

jacoco {
    toolVersion = '0.8.8'
}
```

`build.gradle` 파일에서 Jacoco를 플러그인으로 가져오고, 버전을 설정한다.

* `jacocoTestReport` 와 `jacocoTestCoverageVerification` task가 gradle verification항목에 추가된다

* 주의 해야 할 점은 생성된 두 개의 task는 `test` 가 먼저 실행된 다음에 실행이 되어야 한다.

플러그인이 프로젝트 적용되면 jacocoTestReposrt라는 new task가 생성되고 기본적으로 $buildDir/reports/jacoco/test 디렉토리 밑에 html report 파일이 생성된다. 



jacoco plugin이 성공적으로 적용되었다면 `jacoco` 로 이름이 붙은 `JacocoPluginExtension` 타입의 project extentions을 사용하여 빌드 파일에서 사용 될 기본적인 설정을 해줄수 있다.
`JacocoPluginExtension` 에서 설정해 줄 수 있는 값은 아래 두가지다.

1. toolVersion : Jacoco의 jar 버전
2. reportsDir : Jacoco report 결과물 디렉토리

reportsDir을 따로 지정해주고 싶다면 다음과 같은 설정을 추가한다

```groovy
jacoco {
    toolVersion = "0.8.8"
    reportsDirectory = layout.buildDirectory.dir('customJacocoReportDir')
}
```

* JaCoCo속성의 기본값은 $buildDir/reports/jacoco 이다. 



이후 gradle에서 jacocoTestReports설정과 jacocoTestCoverageVerification을 해줘야한다. 

- [jacocoTestReport](https://docs.gradle.org/current/dsl/org.gradle.testing.jacoco.tasks.JacocoReport.html): 바이너리 커버리지 결과를 사람이 읽기 좋은 형태의 리포트로 저장한다. html 파일로 생성해 사람이 쉽게 눈으로 확인할 수도 있고, SonarQube 등으로 연동하기 위해 xml, csv 같은 형태로도 리포트를 생성할 수 있다.
- [jacocoTestCoverageVerification](https://docs.gradle.org/current/dsl/org.gradle.testing.jacoco.tasks.JacocoCoverageVerification.html): 내가 원하는 커버리지 기준을 만족하는지 확인해 주는 task이다. 
  - 예를 들어, 브랜치 커버리지를 최소한 80% 이상으로 유지하고 싶다면, 이 task에 설정하면 된다. `test` task처럼 Gradle 빌드의 성공/실패로 결과를 보여준다.

## jacocoTestReports 설정 -  XML, CSV, HTML 파일로 보고서 생성

Jacoco는 위에서 생성한 바이너리 커버리지 (`test.exec`) 파일을 사람이 읽을 수 있는 XML, CSV, HTML 파일로도 생성하는 기능을 제공한다. `jacocoTestReport`  태스크는 바이너리 보고서를 사람이 읽기 좋은 형태로 출력해주는 역할을 한다. 

```groovy
jacocoTestReport {
    reports {
        xml.enabled true
        csv.enabled true
        html.enabled true

        xml.destination file("${buildDir}/jacoco/index.xml")
        csv.destination file("${buildDir}/jacoco/index.csv")
        html.destination file("${buildDir}/jacoco/index.html")
    }
}
```

>  `${buildDir}` 은 빌드 디렉토리 경로를 의미한다.

* enabled를 false로 하면 해당 파일은 제공하지 않는다

* csv를 추가적으로 제공해야 Sonar Qube에서 커버리지 측정을 확인할 수 있다.

* 추가 설정값 확인 : https://docs.gradle.org/current/dsl/org.gradle.testing.jacoco.tasks.JacocoReport.html



### 테스트가 끝난뒤 바로 jacocoTestReport 실행하기

test 태스크가 끝난 다음에 jacocoTestReport 를 실행하는 2번의 과정은 번거롭다. 

아래와 같이 추가적으로 설정해야 테스트가 끝나면 곧바로 jacocoTestReport가 실행되게 만들수 있다. 

```groovy
test {
    // ...
    finalizedBy 'jacocoTestReport'
}
```

* Gradle에서 `finalizedBy` 를 사용하면, 해당 태스크가 끝나고 성공 여부와 관계없이 명시한 Task를 이어 실행하도록 설정할 수 있다



## jacocoTestCoverageVerification - 빌드 성공 커버리지 기준 설정, 커버리지 검증 수준 정의

rule의 limit 규칙(rule) 설정에서 커버리지 % 지표로 빌드 성공, 실패 여부를 설정할 수 있다.

* task규칙에서  프로젝트의 코드 커버리지가 규칙을 통과하지 않으면 빌드가 실패한다

여러 수준의 정의를 `violationRules` 에서 다수의 `rule` 에 정의하여 사용할 수 있다.

```java
// in build.gradle 

jacocoTestCoverageVerification {
    violationRules {
        rule {
            enabled = true
            element = 'CLASS'
            // include = []
            limit {
                counter = 'LINE'
                value = 'COVEREDRATIO'
                minimum = 0.80
            }
          
            excludes = []
        }

        // ...

        rule { // 추가적인 규칙
            // 규칙을 여러개 추가할 수 있다.
        }
    }
}
```

각 설정값의 의미는 다음과 같다.

1. **enabled** : 해당 rule의 활성화 여부를 boolean으로 나타낸다.
   * 명시적으로 지정하지 않으면 `default로 true`.
2. **element** : 커버리지를 체크할 큰 단위를 나타낸다. - 기본값은 `BUNDLE`
   * `BUNDLE` : 패키지 번들 (전체 프로젝트)
   * `CLASS` : 클래스
   * `GROUP` : 논리적 번들 그룹
   * `METHOD` : 메소드
   * `PACKAGE` : 패키지
   * `SOURCEFILE` : 소스파일
3. **includes** : rule 적용 대상을 package 수준으로 정의.
   * 아무런 설정을 하지 않는다면 전체 적용.
4. limit : rule의 상세 설정을 나타내는 block .
   - [**counter**](https://www.eclemma.org/jacoco/trunk/doc/counters.html) : 코드 커버리지를 측정할 때 사용되는 지표 - 기본값은 `INSTRUCTION`
     * 이 때 측정은 java byte code가 실행된 것을 기준으로 counting
     * `LINE` : 빈 줄을 제외한 실제 코드의 라인 수 - 한 라인이라도 실행되었다면 측정, 소스 코드 포맷에 영향을 받는 측정방식
     * `BRANCH` : 조건문등의 분기 수 - `if`, `switch` 구문에 대한 커버리지 측정
     * `CLASS` : 클래스 수 - 클래스 내부 메소드가 한번이라도 실행된다면 실행된 것으로 간주
     * `METHOD` : 메소드 수 - 클래스와 마찬가지로 METHOD가 한번이라도 실행되면 실행된 것으로 간주
     * `INSTRUCTION` : 자바 바이트코드 명령 수 - jacoco의 가장 작은 측정 방식, LINE과 다르게 소스 코드 포맷에 영향을 받지 않는다
     * `COMPLEXITY` : 복잡도 [계산식]([Coverage Counters – JaCoCo docs](https://www.eclemma.org/jacoco/trunk/doc/counters.html) )
   - [value](https://docs.gradle.org/current/javadoc/org/gradle/testing/jacoco/tasks/rules/JacocoLimit.html#getValue--) : 측정한 counter의 정보를 어떠한 방식으로 보여줄지 정하고 얼마나 만족시킬지 결정
     * value의 종류 
     * TOTALCOUNT : 전체 개수
     * COVEREDCOUNT : 커버된 개수
     * MISSEDCOUNT : 커버되지 않은 개수
     * COVEREDRATIO 커버된 비율. 0부터 1 사이의 숫자로, 1이 100%
     * MISSEDRATIO : 커버되지 않은 비율. 0부터 1 사이의 숫자로, 1이 100%
     * COVEREDRATIO : 얼마나 우리의 코드가 테스트 되었는지 확인하면 되기 때문에 커버된 비율
   - minimum : count값을 value에 맞게 표현했을때 최소 값.
     - 이 값으로 jacoco coverage verification이 `성공할지 못할지 판단`.
     - 해당 값은 0.00 부터 1.00사이에 원하는 값 (0~100%)으로 설정해주면 된다.
5. excludes
   - verify 에서 `제외할 클래스`를 지정할 수 있다.
   - 패키지 레벨의 경로를 지정가능.
   - 경로에는 와일드 카드로 `*`와 `?`를 사용할 수 있다.
   - `querydsl QClass`도 제외 가능



## Report(보고서) 생성 후 커버리지 만족 검사 - 빌드 실패 유도

 `finalizedBy` 속성으로 `test` 태스크가 실행되고 `jacocoTestReport` 가 실행되도록 설정했다면, 

이어서 `jacocoTestReport` 태스크가 실행된 이후 `jacocoTestCoverageVerification` 가 바로 실행되어 테스트가 실행되고, 커버리지가 기준 미달이면 빌드가 실패되도록 흐름을 만들 수 있다.

```groovy
jacocoTestReport {
    // ...

    finalizedBy 'jacocoTestCoverageVerification'
}
```

간단히 위처럼 `jacocoTestReport` 에 추가해주면 끝이다.



### unit test와 integration test 등을 분리

JaCoCo 플러그인은 자동으로 모든 `Test` 타입의 task에 [JacocoTaskExtension](https://docs.gradle.org/current/dsl/org.gradle.testing.jacoco.plugins.JacocoTaskExtension.html)을 추가하고, `test` task에서 그 설정을 변경할 수 있다. ([JaCoCo specific task configuration](https://docs.gradle.org/current/userguide/jacoco_plugin.html#sec:jacoco_specific_task_configuration)) 그래서 아래 설정처럼 `test` task에서 extension을 설정할 수 있다.   
아래 설정은 커버리지 결과 데이터를 저장할 경로를 변경하는 것이고, unit test와 integration test 등을 분리할 때 사용하면 유용할 수 있다. [ [Kotlin DSL](https://gist.github.com/th-deng/fcc6e9d913f362b0008b56eb5bc82fef) ]

```groovy
test {
  jacoco {
    destinationFile = file("$buildDir/jacoco/jacoco.exec")
  }
}
```

아래 코드는 플러그인에서 `test` task에 default로 설정된 값들이다. 
이 값들은 위의 `destinationFile`처럼 오버라이드 할 수 있습니다. ([JacocoTaskExtension](https://docs.gradle.org/current/dsl/org.gradle.testing.jacoco.plugins.JacocoTaskExtension.html) 참고) [ [Kotlin DSL](https://gist.github.com/th-deng/d09f8ac100e7acce9e96dd7c31070aca) ]

```groovy
test {
  jacoco {
    enabled = true
    destinationFile = file("$buildDir/jacoco/$.exec")
    includes = []
    excludes = []
    excludeClassLoaders = []
    includeNoLocationClasses = false
    sessionId = "<auto-generated value>"
    dumpOnExit = true
    classDumpDir = null
    output = JacocoTaskExtension.Output.FILE
    address = "localhost"
    port = 6300
    jmx = false
  }
}
```



## JaCoCo 테스트에서 제외하기 (코드 커버리지 분석 대상 제외. 클래스, 파일)

예외 클래스, DTO 클래스, QueryDsl Q*.class, spring-batch 등의 배치 설정 파일 등 굳이 테스트를 하지 않아도 되는 클래스들이 있다.

 이런 클래스까지 포함하여 코드 커버리지를 계산하면 코드 커버리지가 낮게 나온다.

*  테스트를 안하니까. 

이런 클래스를 분석 대상에서 제외할 수 있다. 

* 제외할 때 신중해야 한다. 라이브러리나 프레임워크가 만들어주는건 테스트를 안한다 하지만, 우리가 만든 기능과 클래스는 더 믿기 힘들기 때문이다. 



> 참고로 
>
> 커버리지 측정 제외 대상(jacocoTestCoverageVerification)과
>  report 제외 대상(jacocoReport)이 1:1로 일치하지 않는다.
> 커버리지 측정 제외 대상은 **패키지+클래스**로 지정한 반면
> report 제외 대상은 **디렉토리** 기준으로 설정되어 있다.
>
> 둘다 제외 해야 의도한대로 동작하며 커버리지 비율을 낮추지 않는다. 



### jacocoReport에서 수집되지 않도록 제외하기 - 파일 경로

`jacocoTestReport` 태스크에서는 보고서에 표시되는 클래스 중 일부를 제외할 수 있다.

```java
jacocoTestReport {
    // ...
    afterEvaluate {
        classDirectories.setFrom(
                files(classDirectories.files.collect {
                    fileTree(dir: it, excludes: [
                            '**/*Application*',
                            '**/*Exception*',
                            '**/dto/**',
                            // ...
                    ])
                })
        )
    }
    // ...
}
```

제외 대상 `파일의 경로`를 [Ant 스타일](https://ant.apache.org/manual/dirtasks.html)로 작성한다



### jacocoTestCoverageVerification에서 수집되지 않도록 제외하기 - 디렉토리 경로

`jacocoTestCoverageVerification` 에서는 코드 커버리지를 만족하는지 확인할 대상 중 일부를 제외할 수 있다.  
 `jacocoTestReport` 에서 작성한 것과 다르게 파일의 경로가 아닌 패키지 + 클래스명을 적어주어야 한다.   
와일드 카드로 `*` (여러 글자) 와 `?` (한 글자) 를 사용할 수 있다.

```java
jacocoTestCoverageVerification {
    violationRules {
        rule {
            // ...

            excludes = [
                    '*.*Application',
                    '*.*Exception',
                    '*.dto.*',
                    // ...
            ]
        }
    }
}
```



### QueryDSL QClass 제외 예제

둘 다 설정하지 않으면, 프로젝트 커버리지 비율을 떨어뜨리므로 둘다 설정해야 한다. 

* jacocoTestReport 설정

```groovy
jacocoTestReport {

  		  // 이부분을 지정
        def Qdomains = []
        for(qPattern in "**/QA" .. "**/QZ"){
            Qdomains.add(qPattern+"*")
        }
  			// 이부분을 지정

        afterEvaluate {
            
            classDirectories.setFrom(files(classDirectories.files.collect {
                fileTree(dir: it,
                        exclude: [] + Qdomains) // 제외할 Qdomains 패턴 추가
            }))
        }

        finalizedBy 'jacocoTestCoverageVerification'
    }
```

`'*.QA*'` 부터 `'*.QZ*'` 까지의 모든 값을 만들어서 **`Qdomains` 리스트에 저장**



* jacocoTestCoverageVerification 설정

```groovy
jacocoTestCoverageVerification {
        // 이부분을 지정
        def Qdomains = []
        for (qPattern in "*.QA".."*.QZ") {  // qPattern = "*.QA","*.QB","*.QC", ... "*.QZ"
            Qdomains.add(qPattern + "*")
        } // 이부분을 지정

        violationRules {
            rule {
                enabled = true
                element = 'CLASS'

                limit {
                    counter = 'LINE'
                    value = 'COVEREDRATIO'
                    minimum = 0.60
                }

                excludes = [] + Qdomains // 제외할 Qdomains 패턴 추가

            }

        }
    }
```

* Qdomain의 경우 맨 앞글자가 Q 그다음 글자도 영문자 대문자로 시작하므로  for loop를 이용해서 해당 패턴의 Qdomain경로를 모아서 exclude에 추가해 주면된다. 



단순히 다음과 같이 `*.Q*` 라고 지정해준다면 한가지 문제가 발생한다.

```groovy
excludes = ["*.Q*"]
```

Entity를 비롯한 Q로 시작하는 모든 클래스를 검증 대상으로 삼지 않는다.



## Lombok과 DTO 제외하기

Lombok의 getter, Builder와 같은 메서드는 jacoco 테스트에서 문제가 될 수 있다.
getter와 builder가 테스트 되는지 알고 싶지 않을수 있고, 테스트 커버리지에 영향을 미치는것 또한 바라지 않을때 다음과 같이 설정한다.

프로젝트 루트/`lombok.config` 파일을 생성하여 다음 내용을 집어넣는다

```config
// 프로젝트 루트 밑 바로 아래 lombok.config파일

lombok.addLombokGeneratedAnnotation = true
```

`lombok.config`를 추가해준 다음 한 줄을 추가해주면 lombok에 의해 generated된 메서드는 테스트 검증에서 제외된다.



## jacoco 사용시 테스트에서 주의점

jacoco를 적용한뒤 test code를 작성할 때 주의해야할 점이 있다.

jacoco를 적용하면 test를 할 때 jacoco가 테스트 정보를 수집하기 위해
**Synthetic(인조) 필드와 메소드를 각각 1개씩 추가**한다.

> 관련 [jacoco issue](https://github.com/jacoco/jacoco/issues/168) 링크
>
> - Synthetic 필드와 메서드는 컴파일러에 의해 자동으로 생성된다.

만약 filed의 갯수나 reflection이 필요한 테스트 코드를 작성할 경우 위와 같은 부분을 주의하고
`field`의 `isSynthetic()` 메소드를 이용하여 필터링을 거친뒤에 사용해 주어야 한다.





# 멀티 모듈(Multi Module) 적용 - Gradle

api, auth, common 3 개의 모듈로 구성된 멀티모듈이라고 가정한다.

루트 프로젝트의 `build.gradle`

```groovy
plugins {
  ...
    
}

allprojects {
    group = 'com.ys
    version = '0.0.1-SNAPSHOT'
}

subprojects {
    apply plugin: 'java'
    apply plugin: 'org.springframework.boot'
    apply plugin: 'io.spring.dependency-management'
    apply plugin: 'org.asciidoctor.convert'
    apply plugin: 'jacoco'

    sourceCompatibility = '1.8'

    repositories {
        mavenCentral()
    }

    test {
        useJUnitPlatform()
        finalizedBy 'jacocoTestReport'
    }

    jacoco {
        toolVersion = '0.8.6'
    }

    jacocoTestReport {
        reports {
            html.enabled true
            csv.enabled true
            xml.enabled false
        }

        def Qdomains = []

        for (qPattern in '**/QA'..'**/QZ') { // qPattern = '*.QA', '*.QB', ... '*.QZ'
            Qdomains.add(qPattern + '*')
        }

        afterEvaluate {
            classDirectories.setFrom(
                    files(classDirectories.files.collect {
                        fileTree(dir: it, excludes: [] + Qdomains)
                    })
            )
        }

        finalizedBy 'jacocoTestCoverageVerification'
    }

    jacocoTestCoverageVerification {
        def Qdomains = []

        for (qPattern in '*.QA'..'*.QZ') { // qPattern = '*.QA', '*.QB', ... '*.QZ'
            Qdomains.add(qPattern + '*')
        }

        violationRules {
            rule {
                enabled = true
                element = 'CLASS'

                limit {
                    counter = 'LINE'
                    value = 'COVEREDRATIO'
                    minimum = 0.00
                }

                limit {
                    counter = 'BRANCH'
                    value = 'COVEREDRATIO'
                    minimum = 0.00
                }

                excludes = [] + Qdomains
            }
        }
    }
}

project(':api') {
    dependencies {
        compile project(':common')
    }
}

project(':auth') {
    dependencies {
        compile project(':common')
    }
}
```

모든 모듈의 테스트에 JaCoCo를 적용하려면  **JaCoCo 플러그인**을 `subprojects` 블록에 설정값으로 추가해야 한다

* 위 설정을 추가한 후, gradle 새로 고침을 실행하면 의존성이 추가되면서 서브 모듈의 `Tasks/verification` 에 JaCoCo의 Task가 추가된다.

나머지 설정은 위 싱글모듈 설정과 똑같다. 



Task 간의 순서는 `jacocoTestReport` ->  `jacocoTestCoverageVerification` 이여야 한다.



`./gradlew test` 명령에 `--continue` 옵션을 추가해줘야, 이전 Task의 실패 여부와 상관없이 모든 Task를 수행할 수 있다.

* 설정하지 않으면, 다른 모듈이 테스트에 실패하면 전체 테스트가 실패하게 된다.







### 참조

* https://velog.io/@lxxjn0/%EC%BD%94%EB%93%9C-%EB%B6%84%EC%84%9D-%EB%8F%84%EA%B5%AC-%EC%A0%81%EC%9A%A9%EA%B8%B0-2%ED%8E%B8-JaCoCo-%EC%A0%81%EC%9A%A9%ED%95%98%EA%B8%B0
* https://hudi.blog/dallog-jacoco/
* https://techblog.woowahan.com/2661/
* https://bottom-to-top.tistory.com/36#a024bd2fe21493fd7da0ebfcb368d163:295e29ad1877486a2f50d3a0ee124ac3ac28764cbee79dce848e409990ab67c6193911af6ce8724710687b2932466176c03e53a45b9ac6579a0efe6b2f24dbd7