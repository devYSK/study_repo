# gradle 멀티(다중) 프로젝트

- ﻿﻿프로젝트간 구성
- ﻿﻿일반적인 행동을 정의
- ﻿﻿서브 프로젝트 구성
- ﻿﻿프로젝트 필터링
- ﻿﻿다중 프로젝트 빌드에 대한 실행규칙

*  프로젝트와 작업 경로

- ﻿﻿의존성 제어
- ﻿﻿다른 프로젝트에서 생성한 작업의 아웃풋 에 따른 의존성

• 다중 프로젝트 구축 및 테스트





## 프로젝트간 구성

다중 프로젝트 빌드에 대한 강력한 지원은 Gradle의 고유한 포인트중 하나입니다.

Gradle의 다중 프로젝트 빌드는 하나의 루트 프로젝트와 하나 이상의 하위 프로젝트로 구성됩니다.

각 하위 프로젝트는 다른 하위 프로젝트를 완전히 분리하여 자체적으로 구성할 수 있지만, 하위 프로젝트는 공통된 특성을 공유하는 것이 일반적입니다.

일반적으로 프로젝트간에 구성을 공유하는 것이 바람직하므로, 동일한 구성이 여러 하위 프로젝트에 영향을 줍니다.

매우 간단한 다중 프로젝트 빌드부터 시작하겠습니다. Gradle은 기본적으로 범용 빌드 도구이므로 프로젝트가 Java 프로젝트일 필요는 없습니다.

첫 번째 예는 해양 생물에 관한 것입니다.

 

<< 구성 및 실행 >>

빌드 단계는 모든 Gradle 빌드 단계를 설명합니다. 다중 프로젝트 빌드의 구성 및 실행단계를 확대 해 보겠습니다.

여기서 구성이란 프로젝트 파일 build.gradle 또는 build.gradle.kts 를 실행하는 것을 의미합니다. 예를들어, apply plugin 또는 plugins 블록을 사용하여 선언된 모든 플러그인을 다운로드 하는 것을 의미합니다.

기본적으로 모든 프로젝트의 구성은 작업이 실행되기 전에 발생합니다. 즉, 단일 프로젝트에서 단일 작업을 요청하면 다중 프로젝트 빌드의 모든 프로젝트가 먼저 구성됩니다. 모든 프로젝트를 구성해야 하는 이유는 Gradle 프로젝트 모델의 모든 부분에 엑세스하고 변경할 수 있는 유연성을 지원하기 위함입니다.

 

<< 요청시 구성 >>

모든 프로젝트가 실행단계 전에 구성되어 있기 때문에 기능 및 전체 프로젝트 모델에 대한 엑세스가 가능합니다. 그러나 이 방법은 매우 큰 다중 프로젝트 빌드에서는 효율적이지 않을 수 있습니다. 확장 성은 Gradle의 중요한 요구사항입니다. 따라서 1.4 버전부터 필요한 경우에만 구성하는 모드가 도입되었습니다. (configuration on demand)

 

\- 루트 프로젝트는 항상 구성됩니다. 이런 방식으로 일반적인 공통 구성이 지원됩니다.

(모든 프로젝트 또는 하위 프로젝트 스크립트 블록)

 

\- 빌드가 실행되는 폴더의 프로젝트가 구성됩니다. (하지만 어떤 작업도 없이 Gradle이 실행되는 경우에만 가능합니다.) 이처럼, 빌드가 실행되는 폴더 안에 프로젝트가 필요하기 때문에 함께 구성됩니다.

 

\- 표준 프로젝트 종속성이 지원되며, 관련 프로젝트를 구성합니다. 프로젝트 A에 프로젝트 B에 대한 컴파일 종속성이 있는 경웅 A를 빌드하면 두 프로제게트가 모두 구성됩니다.

 

\- 작업 경로를 통해 선언된 작업 종속성이 지원되며 관련 프로젝트가 구성됩니다.

(예: someTask.dependsOn(":someOtherProject:someOtherTask"))

 

\- 커맨드라인에서 작업경로를 통해 요청된 작업은 관련 프로젝트를 구성합니다. 예를들어, 'projectA : projectB : someTask' 를 빌드하면 projectB가 구성됩니다.

 

 

## ◆ 일반적인 행동을 정의

\# Ex 1

루트 프로젝트는 water 라는 이름을 가지면서 bluewale 이라는 서브 프로젝트 포함하고 있는 경우

 

<< Project Structure >>

```
.
├── bluewhale/
├── build.gradle
└── settings.gradle
```

 

<< settings.gradle >>

```groovy
rootProject.name = 'water'
include 'bluewhale'
```

 

<< build.gradle >>

```groovy
Closure cl = { task -> println "I'm $task.project.name" }
task('hello').doLast(cl)
project(':bluewhale') {
    task('hello').doLast(cl)
}
```

 

<< Command >>

```
> gradle -q hello
I'm water
I'm bluewhale
```

------

\# Ex 2

루트 프로젝트는 water 라는 이름을 가지면서 bluewale, krill 이라는 서브 프로젝트 포함하고 있는 경우

 

<< Project Structure >>

```
.
├── bluewhale/
├── build.gradle
├── krill/
└── settings.gradle
```

 

<< settings.gradle >>

```groovy
rootProject.name = 'water'

include 'bluewhale', 'krill'
```

 

 

<< build.gradle >>

```groovy
allprojects {
    task hello {
        doLast { task ->
            println "I'm $task.project.name"
        }
    }
}
```

 

<< Command >>

```
> gradle -q hello
I'm water
I'm bluewhale
I'm krill
```

 

 

## ◆ 서브 프로젝트 구성

Project API는 하위 프로젝트에만 엑세스 하기 위한 속성도 제공합니다.

------

\# Ex 1

일반적인 행동의 정의 - 모든 프로젝트 및 하위 프로젝트의 공통 동작 정의

 

<< build.gradle >>

```groovy
allprojects {
    task hello {
        doLast { task ->
            println "I'm $task.project.name"
        }
    }
}

subprojects {
    hello {
        doLast {
            println "- I depend on water"
        }
    }
}
```

 

<< Command >>

```
> gradle -q hello
I'm water
I'm bluewhale
- I depend on water
I'm krill
- I depend on water
```

------

\# Ex 2

일반적인 동작 위에 특정 동작을 추가 할 수 있습니다.

 

<< build.gradle >>

```groovy
allprojects {
    task hello {
        doLast { task ->
            println "I'm $task.project.name"
        }
    }
}

subprojects {
    hello {
        doLast {
            println "- I depend on water"
        }
    }
}

project(':bluewhale').hello {
    doLast {
        println "- I'm the largest animal that has ever lived on this planet."
    }
}
```

 

<< Command >>

```
> gradle -q hello
I'm water
I'm bluewhale
- I depend on water
- I'm the largest animal that has ever lived on this planet.
I'm krill
- I depend on water
```

------

\# Ex 3

프로젝트 특정 작업을 정의

 

<< Project Structure >>

```
.
├── bluewhale
│   └── build.gradle
├── build.gradle
├── krill
│   └── build.gradle
└── settings.gradle
```

 

<< settings.gradle >>

```groovy
rootProject.name = 'water'
include 'bluewhale', 'krill'
```

 

<< bluewhale/build.gradle >>

```groovy
hello.doLast {
  println "- I'm the largest animal that has ever lived on this planet."
}
```

 

<< krill/build.gradle >>

```groovy
hello.doLast {
  println "- The weight of my species in summer is twice as heavy as all human beings."
}
```

 

<< build.gradle >>

```groovy
allprojects {
    task hello {
        doLast { task ->
            println "I'm $task.project.name"
        }
    }
}

subprojects {
    hello {
        doLast {
            println "- I depend on water"
        }
    }
}
```

 

<< Command >>

```
> gradle -q hello
I'm water
I'm bluewhale
- I depend on water
- I'm the largest animal that has ever lived on this planet.
I'm krill
- I depend on water
- The weight of my species in summer is twice as heavy as all human beings.
```

 

 

## ◆ 프로젝트 필터링

\# Ex 1

프로젝트 이름을 매개로 작업내용을 추가하는 방법입니다.

configure()메소드는 목록을 인수로 사용하여이 목록의 프로젝트에 구성을 적용합니다.

 

<< Project Structure >>

```
.
├── bluewhale/
│   └── build.gradle
├── build.gradle
├── krill/
│   └── build.gradle
├── settings.gradle
└── tropicalFish/
```

 

<< settings.gradle >>

```groovy
rootProject.name = 'water'
include 'bluewhale', 'krill', 'tropicalFish'
```

 

<< build.gradle >>

```groovy
allprojects {
    task hello {
        doLast { task ->
            println "I'm $task.project.name"
        }
    }
}

subprojects {
    hello {
        doLast {
            println "- I depend on water"
        }
    }
}

configure(subprojects.findAll {it.name != 'tropicalFish'}) {
    hello {
        doLast {
            println '- I love to spend time in the arctic waters.'
        }
    }
}
```

 

<< Command >>

```
> gradle -q hello
I'm water
I'm bluewhale
- I depend on water
- I love to spend time in the arctic waters.
- I'm the largest animal that has ever lived on this planet.
I'm krill
- I depend on water
- I love to spend time in the arctic waters.
- The weight of my species in summer is twice as heavy as all human beings.
I'm tropicalFish
- I depend on water
```

------

\# Ex 2

프로퍼티를 매개로 작업내용을 추가하는 방법입니다.

water 프로젝트의 빌드 파일에서 afterEvaluate 알림을 사용합니다 . 즉 , 하위 프로젝트의 빌드 스크립트 가 평가 된 후 전달하는 클로저 가 평가됩니다.

arctic 해당 빌드 스크립트에 속성 이 설정되었으므로이 방법으로 수행해야합니다.

 

<< Project Structure >>

```
.
├── bluewhale
│   └── build.gradle
├── build.gradle
├── krill
│   └── build.gradle
├── settings.gradle
└── tropicalFish
    └── build.gradle
```

 

<< settings.gradle >>

```groovy
rootProject.name = 'water'
include 'bluewhale', 'krill', 'tropicalFish'
```

 

<< bluewhale/build.gradle >>

```groovy
ext.arctic = true
hello.doLast {
  println "- I'm the largest animal that has ever lived on this planet."
}
```

 

<< krill/build.gradle >>

```groovy
ext.arctic = true
hello.doLast {
    println "- The weight of my species in summer is twice as heavy as all human beings."
}
```

 

<< troicalFish/build.gradle >>

```groovy
ext.arctic = false
```

 

<< build.gradle >>

```groovy
allprojects {
    task hello {
        doLast { task ->
            println "I'm $task.project.name"
        }
    }
}

subprojects {
    hello {
        doLast {println "- I depend on water"}
    }

    afterEvaluate { Project project ->
        if (project.arctic) {
            hello.configure {
                doLast {
                    println '- I love to spend time in the arctic waters.'
                }
            }
        }
    }
}
```

 

<< Command >>

```
> gradle -q hello
I'm water
I'm bluewhale
- I depend on water
- I'm the largest animal that has ever lived on this planet.
- I love to spend time in the arctic waters.
I'm krill
- I depend on water
- The weight of my species in summer is twice as heavy as all human beings.
- I love to spend time in the arctic waters.
I'm tropicalFish
- I depend on water
```

 

 

## ◆ 다중 프로젝트 빌드에 대한 실행규칙

```
> gradle -q hello
I'm bluewhale
- I depend on water
- I'm the largest animal that has ever lived on this planet.
- I love to spend time in the arctic waters.
```

 

Gradle 동작의 기본 규칙은 간단합니다. Gradle은 현재 dir로 시작 하여 이름이있는 작업에 대한 계층 구조를 내려다보고 hello실행합니다.

Gradle은 항상 다중 프로젝트 빌드의 모든 프로젝트를 평가 하고 모든 기존 작업 개체를 만듭니다. 그런 다음 작업 이름 인수와 현재 디렉토리에 따라 Gradle은 실행해야 할 작업을 필터링합니다. 

Gradle의 교차 프로젝트 구성으로 인해 작업을 실행 하기 전에 모든 프로젝트를 평가해야 합니다.

 

\# Ex 1

프로젝트 평가 및 실행

 

<< bluewhale/build.gradle >>

```groovy
ext.arctic = true
hello {
    doLast {
        println "- I'm the largest animal that has ever lived on this planet."
    }
}

task distanceToIceberg {
    doLast {
        println '20 nautical miles'
    }
}
```

 

<< krill/build.gradle >>

```groovy
ext.arctic = true
hello {
    doLast {
        println "- The weight of my species in summer is twice as heavy as all human beings."
    }
}

task distanceToIceberg {
    doLast {
        println '5 nautical miles'
    }
}
```

 

<< Command >>

```
> gradle -q distanceToIceberg
20 nautical miles
5 nautical miles

-----------------------------------------------------

> gradle distanceToIceberg

> Task :bluewhale:distanceToIceberg
20 nautical miles

> Task :krill:distanceToIceberg
5 nautical miles

BUILD SUCCESSFUL in 0s
2 actionable tasks: 2 executed
```

 

 

## ◆ 프로젝트와 작업 경로

프로젝트 경로는 다음과 같은 패턴을 가집니다. 루트 프로젝트를 나타내는 콜론으로 시작합니다. 루트 프로젝트는 경로에 이름으로 지정되지 않은 유일한 프로젝트입니다. 나머지 프로젝트 경로는 콜론으로 구분된 프로젝트 이름이며, 다음에 이어지는 프로젝트는 이전 프로젝트의 하위 프로젝트 입니다.

 

작업 경로는 단순히 프로젝트의 경로와 ":bluewhale:hello"와 같은 작업 이름 입니다. 프로젝트 내에서 이름으로만 동일한 프로젝트의 작업을 처리할 수 있습니다. 이것은 상대경로로 사용됩니다.

 

## ◆ 의존성 제어

\# Ex 1 

실행 종속성 - 의존성과 실행순서

아무것도 정해지지 않은 경우에 task 는 알파벳 순으로 실행되기 때문에 producer 가 consumer 보다 먼저 실행됩니다.

 

<< Project Structure >>

```
.
├── build.gradle
├── consumer
│   └── build.gradle
├── producer
│   └── build.gradle
└── settings.gradle
```

 

<< settings.gradle >>

```groovy
include 'consumer', 'producer'
```

 

<< build.gradle >>

```groovy
ext.producerMessage = null
```

 

<< consumer/build.gradle >>

```groovy
task action {
    doLast {
        println("Consuming message: ${rootProject.producerMessage}")
    }
}
```

 

<< producer/build.gradle >>

```groovy
task action {
    doLast {
        println "Producing message:"
        rootProject.producerMessage = 'Watch the order of execution.'
    }
}
```

 

<< Command >>

```
> gradle -q action
Consuming message: null
Producing message:
```

------

\# Ex 2

실제 사례

두개의 웹 애플리케이션을 빌드하는 경우

 

<< Project Structure >>

```
.
├── build.gradle
├── date
│   └── src
│       └── main
│           ├── java
│           │   └── org
│           │       └── gradle
│           │           └── sample
│           │               └── DateServlet.java
│           └── webapp
│               └── web.xml
├── hello
│   └── src
│       └── main
│           ├── java
│           │   └── org
│           │       └── gradle
│           │           └── sample
│           │               └── HelloServlet.java
│           └── webapp
│               └── web.xml
└── settings.gradle
```

 

<< settings.gradle >>

```groovy
rootProject.name = 'webDist'
include 'date', 'hello'
```

 

<< build.gradle >>

```groovy
allprojects {
    apply plugin: 'java'
    group = 'org.gradle.sample'
    version = '1.0'
}

subprojects {
    apply plugin: 'war'
    repositories {
        mavenCentral()
    }
    dependencies {
        implementation "javax.servlet:servlet-api:2.5"
    }
}

task explodedDist(type: Copy) {
    into "$buildDir/explodedDist"
    subprojects {
        from tasks.withType(War)
    }
}
```

------

\# Ex 3

프로젝트간의 종속성이 부분적인 멀티 프로젝트 빌드를 가능하게 합니다.

 

<< Project Structure >>

```
.
├── api
│   └── src
│       ├── main
│       │   └── java
│       │       └── org
│       │           └── gradle
│       │               └── sample
│       │                   ├── api
│       │                   │   └── Person.java
│       │                   └── apiImpl
│       │                       └── PersonImpl.java
│       └── test
│           └── java
│               └── org
│                   └── gradle
│                       └── PersonTest.java
├── build.gradle
├── services
│   └── personService
│       └── src
│           ├── main
│           │   └── java
│           │       └── org
│           │           └── gradle
│           │               └── sample
│           │                   └── services
│           │                       └── PersonService.java
│           └── test
│               └── java
│                   └── org
│                       └── gradle
│                           └── sample
│                               └── services
│                                   └── PersonServiceTest.java
├── settings.gradle
└── shared
    └── src
        └── main
            └── java
                └── org
                    └── gradle
                        └── sample
                            └── shared
                                └── Helper.java
```

 

<< settings.gradle >>

```groovy
include 'api', 'shared', 'services:personService'
```

 

<< build.gradle >>

```groovy
subprojects {
    apply plugin: 'java'
    group = 'org.gradle.sample'
    version = '1.0'
    repositories {
        mavenCentral()
    }
    dependencies {
        testImplementation "junit:junit:4.12"
    }
}

project(':api') {
    dependencies {
        implementation project(':shared')
    }
}

project(':services:personService') {
    dependencies {
        implementation project(':shared'), project(':api')
    }
}
```

------

\# Ex 4

종속성에 대한 세분화된 제어

메이븐을 사용하시던 분은 이정도면 충분히 만족하실텐데, Ivy 를 사용하시는 분들은 조금 더 명확한 것을 원하실 수 있습니다. 그렇기 때문에 아래와 같은 옵션을 제공합니다.

 

api 인터페이스만을 포함하는 라이브러리를 추가하고, 새로운 의존성을 구성합니다.

Person Service 를 위해서는 api 인터페이스에 대해서만 컴파일 되어야한다고 정의합니다.

하지만 테스트의 경우에는 api의 모든 클래스로 컴파일 되어야 한다고 정의합니다.

 

<< build.gradle >>

```groovy
subprojects {
    apply plugin: 'java-library'
    group = 'org.gradle.sample'
    version = '1.0'
}

project(':api') {
    configurations {
        spi
    }
    dependencies {
        implementation project(':shared')
    }
    task spiJar(type: Jar) {
        archiveBaseName = 'api-spi'
        from sourceSets.main.output
        include('org/gradle/sample/api/**')
    }
    artifacts {
        spi spiJar
    }
}

project(':services:personService') {
    dependencies {
        implementation project(':shared')
        implementation project(path: ':api', configuration: 'spi')
        testImplementation "junit:junit:4.12", project(':api')
    }
}
```

 

 

## ◆ 다른 프로젝트에서 생성한 작업의 아웃풋에 따른 의존성

\# Ex 1

빌드 정보가 포함된 특정 파일을 생성하는 작업

 

<< build.gradle >>

```groovy
task buildInfo(type: BuildInfo) {
    version = project.version
    outputFile = file("$buildDir/generated-resources/build-info.properties")
}

sourceSets {
    main {
        output.dir(buildInfo.outputFile.parentFile, builtBy: buildInfo)
    }
}
```

------

\# Ex 2

특성 파일을 생성하는 프로젝트에 대한 프로젝트 종속성 선언

소비하는 프로젝트는 런타임에 속성 파일을 읽을 수 있어야합니다. 프로덕션 프로젝트에서 프로젝트 종속성을 선언하면 특성을 미리 작성하고 런타임 클래스 경로에서 사용할 수있게됩니다.

 

<< build.gradle >>

```groovy
dependencies {
    runtimeOnly project(':producer')
}
```

 

 

## ◆ 다중 프로젝트 구축 및 테스트

\# Ex 1

싱글 프로젝트 빌드 및 테스트

 

<< Command >>

```
> gradle :api:build
> Task :shared:compileJava
> Task :shared:processResources
> Task :shared:classes
> Task :shared:jar
> Task :api:compileJava
> Task :api:processResources
> Task :api:classes
> Task :api:jar
> Task :api:assemble
> Task :api:compileTestJava
> Task :api:processTestResources
> Task :api:testClasses
> Task :api:test
> Task :api:check
> Task :api:build

BUILD SUCCESSFUL in 0s
9 actionable tasks: 9 executed
```

------

\# Ex 2

다중 프로젝트 빌드 및 테스트

 

<< Command >>

```
> gradle :api:buildNeeded
> Task :shared:compileJava
> Task :shared:processResources
> Task :shared:classes
> Task :shared:jar
> Task :api:compileJava
> Task :api:processResources
> Task :api:classes
> Task :api:jar
> Task :api:assemble
> Task :api:compileTestJava
> Task :api:processTestResources
> Task :api:testClasses
> Task :api:test
> Task :api:check
> Task :api:build
> Task :shared:assemble
> Task :shared:compileTestJava
> Task :shared:processTestResources
> Task :shared:testClasses
> Task :shared:test
> Task :shared:check
> Task :shared:build
> Task :shared:buildNeeded
> Task :api:buildNeeded

BUILD SUCCESSFUL in 0s
12 actionable tasks: 12 executed
```

------

\# Ex 3

의존성 프로젝트 빌드 및 테스트

 

<< Command >>

```
> gradle :api:buildDependents
> Task :shared:compileJava
> Task :shared:processResources
> Task :shared:classes
> Task :shared:jar
> Task :api:compileJava
> Task :api:processResources
> Task :api:classes
> Task :api:jar
> Task :api:assemble
> Task :api:compileTestJava
> Task :api:processTestResources
> Task :api:testClasses
> Task :api:test
> Task :api:check
> Task :api:build
> Task :services:personService:compileJava
> Task :services:personService:processResources
> Task :services:personService:classes
> Task :services:personService:jar
> Task :services:personService:assemble
> Task :services:personService:compileTestJava
> Task :services:personService:processTestResources
> Task :services:personService:testClasses
> Task :services:personService:test
> Task :services:personService:check
> Task :services:personService:build
> Task :services:personService:buildDependents
> Task :api:buildDependents

BUILD SUCCESSFUL in 0s
17 actionable tasks: 17 executed
```

 

 

마지막으로 모든 프로젝트에서 모든 것을 구축하고 테스트 할 수 있습니다. 루트 프로젝트 폴더에서 실행하는 모든 작업은 동일한 이름의 작업이 모든 자식에서 실행되도록합니다. 따라서“ gradle build” 만 실행하면 모든 프로젝트를 빌드하고 테스트 할 수 있습니다.