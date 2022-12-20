# gradle 로깅

- ﻿﻿로그 레벨
- ﻿﻿로그 레벨 지정하기
- ﻿﻿Stacktrace 커맨드라인 옵션

*  커스텀 로그 메시지

- ﻿﻿외부 도구 및 라이브러리에서의 로깅
- ﻿﻿Gradle 로그 변경

## ◆ 로그 레벨

| 레벨      | 설명             |
| --------- | ---------------- |
| ERROR     | 에러 메시지      |
| QUIET     | 중요 정보 메시지 |
| WARNING   | 경고 메시지      |
| LIFECYCLE | 진행정보 메시지  |
| INFO      | 정보 메시지      |
| DEBUG     | 디버그 메시지    |

 

 

## ◆ 로그 레벨 지정하기

명령어 옵션을 사용하여 각각 다른 로그레벨을 설정할 수 있습니다.

| 옵션               | 설명                       |
| ------------------ | -------------------------- |
| no logging options | LIFECYCLE 이상             |
| -q / --quiet       | QUIET 이상                 |
| -w / --warn        | WARN 이상                  |
| -i / --info        | INFO 이상                  |
| -d / --debug       | DEBUG 이상 (모든 로그레벨) |

 

 

## ◆ Stacktrace 커맨드라인 옵션

| 옵션                   | 설명                                                         |
| ---------------------- | ------------------------------------------------------------ |
| no stacktrace options  | 빌드 오류는 아무런 출력이 없음. 내부 오류가 발생한 경우에만 stacktrace 출력 |
| -s / --stacktrace      | 잘린 stacktrace 출력 (이 방법을 추천. 전체출력은 너무 길다.) |
| -S / --full-stacktrace | 전체 stacktrace 출력                                         |

 

 

## ◆ 커스텀 로그 메시지

\# Ex 1

stdout 을 사용한 로그 메시지 작성

 

<< build.gradle >>

```groovy
println 'A message which is logged at QUIET level'
```

------

\# Ex 2

Gradle에서 제공하는 빌드스크립트 속성 중에서 SLF4J 인터페이스를 사용하기

 

<< build.gradle >>

```groovy
logger.quiet('An info log message which is always logged.')
logger.error('An error log message.')
logger.warn('A warning log message.')
logger.lifecycle('A lifecycle info log message.')
logger.info('An info log message.')
logger.debug('A debug log message.')
logger.trace('A trace log message.')
```

------

\# Ex 3

특정 포맷으로 로그 출력하기

 

<< build.gradle >>

```
logger.info('A {} log message', 'info')
```

------

\# Ex 4

빌드에 사용된 다른 클래스 내에서 Gradle의 로깅 시스템에 연결하여 사용하기

 

<< build.gradle >>

```groovy
import org.slf4j.LoggerFactory

def slf4jLogger = LoggerFactory.getLogger('some-logger')
slf4jLogger.info('An info log message logged using SLF4j')
```

 

 

## ◆ 외부 도구 및 라이브러리에서의 로깅

내부적으로 Gradle은 Ant와 Ivy를 사용합니다. 둘 다 자체 로깅 시스템이 있습니다. Gradle은 로깅 출력을 Gradle 로깅 시스템으로 리디렉션합니다.

 

Ant / Ivy 로그 레벨에서 Gradle 로그 레벨로 맵핑되는 Ant / Ivy 로그 레벨을 제외하고는 Ant / Ivy TRACE 로그레벨에서 Gradle 로그 레벨로의 1:1 맵핑이 DEBUG 입니다.

이는 기본 Gradle 로그 수준에 오류 또는 경고가 아닌 한 Ant / Ivy 출력이 표시되지 않음을 의미합니다.

 

다시말해서 Ant / Ivy 의 TRACE 레벨의 로그가 Gradle 의 DEBUG 에 맵핑이 되어버리기 때문에, DEBUG 이상 레벨의 출력을 하지 않는 이상은 TRACE 레벨의 로그가 출력되지 않는다는 의미입니다.

------

\# Ex 1

로깅에는 여전히 표준 출력을 사용하는 많은 도구가 있습니다.

기본적으로 Gradle은 표준 출력을 QUIET 로그 레벨로, 표준 오류는 ERROR 로그 레벨로 리디렉션 합니다.

프로젝트 객체는 LoggingManager를 제공하여 빌드 스크립트를 평가할 때 표준 출력 또는 오류가 리디렉션 되는 로그 수준을 변경할 수 있습니다.

 

<< build.gradle >>

```groovy
logging.captureStandardOutput LogLevel.INFO
println 'A message which is logged at INFO level'
```

------

\# Ex 2

작업에 대한 표준 출력 캡처 구성

작업 실행 중 표준 출력 또는 오류에 대한 로그 수준을 변경하기 위해 작업 내에서 LoggingManager 도 제공됩니다.

 

<< build.gradle >>

```groovy
task logInfo {
    logging.captureStandardOutput LogLevel.INFO
    doFirst {
        println 'A task message which is logged at INFO level'
    }
}
```

 

 

## ◆ Gradle 로그 변경

Gradle의 로깅 UI 대부분을 자신의 로깅 UI로 바꿀 수 있습니다.

예를들면, 로그에 기록할 정보의 양을 조절하고 싶은 경우에 사용할 수 있습니다.

Gradle.useLogger 메소드를 사용하여 로깅을 바꿀 수 있습니다.

 

로거는 아래와 같은 리스너 인터페이스를 구현할 수 있습니다. 로거를 등록하면 해당 로거가 구현하는 인터페이스에 대한 로깅만 교체됩니다.

\- BuildListener
\- ProjectEvaluationListener
\- TaskExecutionGraphListener
\- TaskExecutionListener
\- TaskActionListener

------

\# Ex 1

Gradle 로그의 커스터마이징 방법

 

<< customLogger.init.gradle >>

```groovy
useLogger(new CustomEventLogger())

class CustomEventLogger extends BuildAdapter implements TaskExecutionListener {

    void beforeExecute(Task task) {
        println "[$task.name]"
    }

    void afterExecute(Task task, TaskState state) {
        println()
    }

    void buildFinished(BuildResult result) {
        println 'build completed'
        if (result.failure != null) {
            result.failure.printStackTrace()
        }
    }
}
```

 

<< command >>

```
$ gradle -I customLogger.init.gradle build

> Task :compile
[compile]
compiling source


> Task :testCompile
[testCompile]
compiling test source


> Task :test
[test]
running unit tests


> Task :build
[build]

build completed
3 actionable tasks: 3 executed
```