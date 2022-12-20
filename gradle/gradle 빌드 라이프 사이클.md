# gradle - Basic] 빌드 라이프 사이클

Gradle의 핵심은 의존성 기반 프로그래밍을위한 언어입니다. 이는 Gradle로 작업과 작업 간의 종속성을 정의 할 수 있음을 의미합니다. Gradle은 이러한 작업이 종속성 순서대로 실행되고 각 작업이 한 번만 실행되도록 보장합니다.

* 빌드 단계

* 싱글 프로젝트 빌드

*  다중 프로젝트 빌드

- ﻿﻿프로젝트 트리구조
- ﻿﻿빌드 라이프사이클 알림

 

## ◆ 빌드 단계

<< 초기화 >>

Gradle은 단일 및 다중 프로젝트 빌드를 지원합니다. 초기화 단계에서 Gradle은 빌드에 참여할 프로젝트를 결정하고 이러한 각 프로젝트에 대한 Project 인스턴스를 만듭니다 .

 

<< 구성 >>

이 과정에서 빌드과정내의 모든 프로젝트의 빌드스크립트가 실행되고, 프로젝트 객체가 구성됩니다.

 

<< 실행 >>

구성 단계에서 작성 및 구성된 태스크의 서브셋을 결정합니다. (서브셋은 Gradle 커맨드 및 현재 디렉토리에 전달 된 태스크 이름 인수에 의해 결정됩니다.) 그 후, 선택된 작업을 실행합니다.

 

 

## ◆ 싱글 프로젝트 빌드

\# Ex 1

println 부분이 build.gradle 의 첫번째, configured 블록 안에 두번째, testBoth 안에 세번째 동작을 정의하고 있습니다.

따라서 configured 블록의 작업은 3개의 출력이 나오게 됩니다.

특정 작업에만 해당하는 doLast / doFirst 블록은 해당 작업을 호출했을 경우에만 출력되게 됩니다.

 

<< settings.gradle >>

```groovy
println 'This is executed during the initialization phase.'
```

 

<< build.gradle >>

```groovy
println 'This is executed during the configuration phase.'

task configured {
    println 'This is also executed during the configuration phase.'
}

task test {
    doLast {
        println 'This is executed during the execution phase.'
    }
}

task testBoth {
	doFirst {
	  println 'This is executed first during the execution phase.'
	}
	doLast {
	  println 'This is executed last during the execution phase.'
	}
	println 'This is executed during the configuration phase as well.'
}
```

 

<< command >>

```
> gradle test testBoth
This is executed during the initialization phase.

> Configure project :
This is executed during the configuration phase.
This is also executed during the configuration phase.
This is executed during the configuration phase as well.

> Task :test
This is executed during the execution phase.

> Task :testBoth
This is executed first during the execution phase.
This is executed last during the execution phase.

BUILD SUCCESSFUL in 0s
2 actionable tasks: 2 executed
```

 

 

## ◆ 다중 프로젝트 빌드

다중 프로젝트 빌드는 Gradle을 한 번 실행하는 동안 둘 이상의 프로젝트를 빌드하는 것입니다. 둘 이상의 프로젝트를 빌드하기 위해서는 먼저 include 를 통해서 빌드할 프로젝트들을 선언해야합니다.

------

\# Ex 1

계층구조의 프로젝트 정의

아래와 같이 총 5개의 프로젝트가 생성됩니다.

\- project1

\- project2

\- project2:child

\- project3

\- project3:child1

 

<< settings.gradle >>

```groovy
include 'project1', 'project2:child', 'project3:child1'
```

------

\# Ex 2

평면구조의 프로젝트 정의

루트프로젝트의 하위폴더 형태로 존재하는 프로젝트입니다.

 

<< settings.gradle >>

```groovy
includeFlat 'project3', 'project4'
```

 

 

## ◆ 프로젝트 트리구조

\# Ex 1

트리의 요소 검색하기

 

<< settings.gradle >>

```groovy
println rootProject.name
println project(':projectA').name
```

 

\# Ex 2

트리의 요소 수정하기

 

<< settings.gradle >>

```groovy
rootProject.name = 'main'
project(':projectA').projectDir = new File(settingsDir, '../my-project-a')
project(':projectA').buildFileName = 'projectA.gradle'
```

 

 

## ◆ 빌드 라이프사이클 알림

\# Ex 1

프로젝트 평가 직전 및 직후에 알림을 받을 수 있습니다.

(특정 속성이 있는 프로젝트에 원하는 작업을 추가할 수 있습니다.)

빌드 스크립트의 모든 정의가 적용된 후 추가구성을 수행하고나 일부 사용자 정의 로깅 또는 프로파일링과 같은 작업을 수행하는데 사용할 수 있습니다.

 

<< build.gradle >>

```groovy
allprojects {
    afterEvaluate { project ->
        if (project.hasTests) {
            println "Adding test task to $project"
            project.task('test') {
                doLast {
                    println "Running tests for $project"
                }
            }
        }
    }
}
```

 

<< projectA.gradle >>

```groovy
hasTests = true
```

 

<< command >>

```
> gradle -q test
Adding test task to project ':projectA'
Running tests for project ':projectA'
```

------

\# Ex 2

project.state.failure 상태를 기준으로 빌드의 성공/실패에 따라서 알림을 받을 수 있습니다.

 

<< build.gradle >>

```groovy
gradle.afterProject { project ->
    if (project.state.failure) {
        println "Evaluation of $project FAILED"
    } else {
        println "Evaluation of $project succeeded"
    }
}
```

 

<< command >>

```
> gradle -q test
Evaluation of root project 'buildProjectEvaluateEvents' succeeded
Evaluation of project ':projectA' succeeded
Evaluation of project ':projectB' FAILED

FAILURE: Build failed with an exception.

* Where:
Build file '/home/user/gradle/samples/groovy/projectB.gradle' line: 1

* What went wrong:
A problem occurred evaluating project ':projectB'.
> broken

* Try:
Run with --stacktrace option to get the stack trace. Run with --info or --debug option to get more log output. Run with --scan to get full insights.

* Get more help at https://help.gradle.org

BUILD FAILED in 0s
```

------

\# Ex 3

작업이 프로젝트에 추가 된 직후 알림을 받을 수 있습니다. 빌드 파일에서 작업을 사용 가능하게 하기 전에 일부 기본값을 설정하거나 동작을 추가하는 데 사용할 수 있습니다.

 

아래의 스크립트는 srcDir 폴더를 각 작업이 생성될 때 설정합니다.

(모든 작업에 특정 속성을 설정)

 

<< build.gradle >>

```groovy
tasks.whenTaskAdded { task ->
    task.ext.srcDir = 'src/main/java'
}

task a

println "source dir is $a.srcDir"
```



<< command >>

```
> gradle -q a
source dir is src/main/java
```

------

\# Ex 4

작업의 실행 직전 및 직후에 알림을 받을 수 있습니다.

 

<< build.gradle >>

```groovy
task ok

task broken(dependsOn: ok) {
    doLast {
        throw new RuntimeException('broken')
    }
}

gradle.taskGraph.beforeTask { Task task ->
    println "executing $task ..."
}

gradle.taskGraph.afterTask { Task task, TaskState state ->
    if (state.failure) {
        println "FAILED"
    }
    else {
        println "done"
    }
}
```

 

<< command >>

```
> gradle -q broken
executing task ':ok' ...
done
executing task ':broken' ...
FAILED

FAILURE: Build failed with an exception.

* Where:
Build file '/home/user/gradle/samples/groovy/build.gradle' line: 5

* What went wrong:
Execution failed for task ':broken'.
> broken

* Try:
Run with --stacktrace option to get the stack trace. Run with --info or --debug option to get more log output. Run with --scan to get full insights.

* Get more help at https://help.gradle.org

BUILD FAILED in 0s
```