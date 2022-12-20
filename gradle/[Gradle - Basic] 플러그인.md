## ◆ 플러그인의 기능

\1. 플러그인을 프로젝트에 적용하면 플러그인이 프로젝트의 기능을 확장해줍니다.

\- Gradle 모델 확장 (사용가능한 새로운 DSL 요소 추가)

\- 규칙에 따라 프로젝트를 구성 (새 작업 추가 또는 기본값 구성)

\- 특정 구성을 적용 (조직 저장소 추가 또는 표준 시행)

 

\2. 프로젝트 빌드 스크립트에 로직을 추가하는 대신 플러그인을 적용하면 여러가지 이점을 얻을 수 있습니다.

\- 재사용을 촉진하고 여러 프로젝트에서 유사한 논리를 유지하는 오버헤드를 줄입니다.

\- 높은 수준의 모듈화가 가능합니다.

\- 명령형 로직을 캡슐화하고 빌드스크립트를 가능한 한 선언적으로 작성할 수 있습니다.



- ﻿﻿플러그인의 기능
- ﻿﻿플러그인의 종류

* 플러그인 사용

* 스크립트 플러그인

- ﻿﻿이진 플러그인
- ﻿﻿특정 서브 프로젝트에만 플러그인 적용
- ﻿﻿buildSrc 디렉토리 에서 플러그인 적용
- ﻿﻿플러그인 관리
- ﻿﻿플러그인 저장소 사용

* 플러그인 버전 관리

* 플러그인 해결 전략 (resoluctionStrategy)

- ﻿﻿플러그인 마커 아티팩트
- ﻿﻿레거시 플러그인 애플리케이션
- ﻿﻿Gradle 플리그인 검색 사이트

 

## ◆ 플러그인의 종류

Gradle에는 스크립트 플러그인과 바이너리 플러그인 두 종류가 있습니다.

 

스크립트 플러그인은 빌드를 구성하고 일반적으로 빌드 조작에 대한 선언적 접근방식을 구현하는 빌드 스크립트입니다. 원격에서 외부화하고 액세스 할 수 있지만 일반적으로 빌드 내에서 사용됩니다.

 

이진 플러그인은 플러그인 인터페이스를 구현하고 빌드 조작을 위한 프로그래밍 방식을 채택하는 클래스입니다. 이진 플러그인은 빌드스크립트, 프로젝트 계층구조 또는 외부 플러그인 jar에 있을 수 있습니다.

 

플러그인은 작성하기가 쉽기 때문에, 스크립트 플러그인으로 시작하는 경우가 많습니다. 코드의 가치가 높아지면 여러 프로젝트 또는 조직간에 쉽게 테스트하고 공유할 수 있는 이진 플러그인으로 마이그레이션됩니다.

 

 

## ◆ 플러그인 사용

플러그인에 캡슐화 된 빌드 로직을 사용하려면 Gradle에서 두 단계를 수행해야합니다. 먼저 플러그인을 resolve 한 다음 플러그인을 대상에 apply 해야합니다.

 

플러그인을 resolve 한다는 것은 주어진 플러그인을 포함하는 jar의 버전을 찾고, 스크립트 클래스 경로에 추가하는 것을 의미합니다. 플러그인이 resolve 되면 빌드스크립트에서 해당 API를 사용할 수 있습니다. 스크립트 플러그인은 apply시 제공된 특정 파일경로 또는 URL에서 분석되므로 자체 resolve 됩니다. Gradle 배포의 일부로 제공된 핵심 바이너리 플러그인은 자동으로 resolve 됩니다.

 

플러그인을 apply 한다는 것은 프로젝트에서 플러그인의 Plugin.apply (T) 를 실제로 실행하는 것을 의미합니다. 플러그인 적용은 idempotent (여러번 적용해도 결과는 같다) 입니다. 즉, 부작용없이 여러 플러그인을 안전하게 여러번 적용 할 수 있습니다.

 

플러그인을 사용하는 가장 일반적인 사례는 플러그인을 resolve 하고 현재 프로젝트에 apply 하는 것입니다. 이것은 일반적인 사용 사례이므로 빌드 작성자는 플러그인 DSL을 사용하여 한번에 플러그인을 resolve 하고 apply 하는 것이 좋습니다.



 

## ◆ 스크립트 플러그인

\# Ex 1

스크립트 플러그인은 자동으로 resolve 되며 로컬 파일 시스템의 스크립트 또는 원격 위치에서 apply 할 수 있습니다. 파일 시스템 위치는 프로젝트 디렉토리에 상대적이고, 원격 스크립트 위치는 HTTP URL 로 지정됩니다. 주어진 대상에 여러 스크립트 플러그인을 적용할 수 있습니다.

 

<< groovy >>

```groovy
apply from: 'other.gradle'
```

 

 

## ◆ 이진 플러그인

플러그인의 전역 고유 식별자 또는 플러그인 id 로 플러그인을 적용합니다. 핵심 Gradle 플러그인은 'java'핵심 JavaPlugin 과 같은 짧은 이름을 제공한다는 점에서 특별합니다. 다른 모든 이진 플러그인은 완전한 형식의 플러그인 ID(com.github.foo.bar)를 사용해야 하지만 일부 레거시 플러그인은 여전히 짧은 정규화되지 않은 양식을 사용할 수 있습니다.

 

<< 이진 플러그인의 위치 >>

플러그인은 단순히 플러그인 인터페이스를 구현하는 모든 클래스입니다. Gradle은 JavaPlugin 배포의 일부로 핵심 플러그인을 제공하므로 자동으로 resolve 됩니다. 그러나 비 주류 바이너리 플러그인을 적용하려면 먼저 resolve 를 해야합니다.

\- 플러그인 포탈이나 플러그인 DSL을 사용하는 커스텀 저장소로부터 플러그인을 포함시킵니다.

\- 빌드 스크립트 종속성으로 정의 된 외부 jar 파일로부터 플러그인을 포함시킵니다.

\- 프로젝트의 buildSrc 폴더 아래에 있는 소스파일로서 플러그인을 정의합니다.

\- 빌드스크립트 안에서 한줄의 클래스로서 플러그인을 정의합니다.

 

<< 플러그인 DSL로 플러그인 적용하기 >>

\# Ex 1

핵심 플러그인 적용하기

 

<< groovy >>

```groovy
plugins {
    id 'java'
}
```

------

\# Ex 2

포탈에서 커뮤니티 플러그인을 적용 (완전한 플러그인 ID 사용)

 

<< groovy >>

```groovy
plugins {
    id 'com.jfrog.bintray' version '0.4.1'
}
```

 

 

## ◆ 특정 서브 프로젝트에만 플러그인 적용

\# Ex 1

settings.gradle - 프로젝트를 총 3개를 포함시킵니다.

build.gradle - hello, goodbye 플러그인을 비활성화 해두고 hello로 시작하는 프로젝트만 hello 플러그인을 apply 하도록 설정합니다.

따라서 helloA, helloB 프로젝트만 hello 플러그인이 실행됩니다.

 

<< settings.gradle >>

```groovy
include 'helloA'
include 'helloB'
include 'goodbyeC'
```

 

<< build.gradle >>

```groovy
plugins {
    id 'org.gradle.sample.hello' version '1.0.0' apply false
    id 'org.gradle.sample.goodbye' version '1.0.0' apply false
}

subprojects {
    if (name.startsWith('hello')) {
        apply plugin: 'org.gradle.sample.hello'
    }
}
```

 

<< goodbyeC/build.gradle >>

```groovy
plugins {
    id 'org.gradle.sample.goodbye'
}
```

 

<< command >>

```
> gradle hello
:helloA:hello
:helloB:hello
Hello!
Hello!

BUILD SUCCEEDED
```

 

 

## ◆ buildSrc 디렉토리 에서 플러그인 적용

\# Ex 1

my-plugin 이라는 ID를 가지는 플러그인의 구현 클래스 my.MyPlugin를 적용하는 방법입니다.

buildSrc/build.gradle - ID를 사용하여 buildSrc 플러그인 정의

build.gradle - 정의한 플러그인의 ID 를 매개로 적용

 

<< buildSrc/build.gradle >>

```groovy
plugins {
    id 'java'
    id 'java-gradle-plugin'
}

gradlePlugin {
    plugins {
        myPlugins {
            id = 'my-plugin'
            implementationClass = 'my.MyPlugin'
        }
    }
}

dependencies {
    compileOnly gradleApi()
}
```

 

<< build.gradle >>

```groovy
plugins {
    id 'my-plugin'
}
```

 

 

## ◆ 플러그인 관리

\# Ex 1

프로젝트의 플러그인 관리

 

<< settings.gradle >>

```groovy
pluginManagement {
    plugins {
    }
    resolutionStrategy {
    }
    repositories {
    }
}
```

------

\# Ex 2

프로젝트 전체의 플러그인 관리

 

<< init.gradle >>

```groovy
settingsEvaluated { settings ->
    settings.pluginManagement {
        plugins {
        }
        resolutionStrategy {
        }
        repositories {
        }
    }
}
```

 

 

## ◆ 플러그인 저장소 사용

Maven 사용시에는 mavenRepository 사이트를 활용하듯, Gradle도 플러그인 저장소를 활용할 수 있습니다. 이러한 저장소는 직접 만든 저장소 일 수도 있고, 자유롭게 이용 가능한 저장소 일 수도 있습니다.

 

플러그인을 resolve 할 때 Maven 저장소를 먼저 찾은 다음 플러그인이 Maven 저장소에없는 경우 Gradle 플러그인 포털을 확인하도록 Gradle에 지시합니다.

Gradle 플러그인 포털을 검색하지 않으려면 gradlePluginPortal() 부분을 지우면 됩니다.

마지막으로의 아이비 리포지토리 ../ivy-repo가 확인됩니다.



<< settings.gradle >>

```groovy
pluginManagement {
    repositories {
        maven {
            url '../maven-repo'
        }
        gradlePluginPortal()
        ivy {
            url '../ivy-repo'
        }
    }
}
```

 

 

## ◆ 플러그인 버전 관리

\# Ex 1

gradle.properties 부분에 버전 관련 정보를 입력합니다.

settings.gradle 에 플러그인의 아이디와 버전 정보를 등록해 둡니다.

build.gradle 에서 빌드하는 경우에 필요한 플러그인을 가져다가 사용합니다.

 

<< gradle.properties >>

```groovy
helloPluginVersion=1.0.0
```

 

<< settings.gradle >>

```groovy
pluginManagement {
  plugins {
        id 'org.gradle.sample.hello' version "${helloPluginVersion}"
    }
}
```

 

<< build.gradle >>

```groovy
plugins {
    id 'org.gradle.sample.hello'
}
```

 

 

## ◆ 플러그인 해결 전략 (resoluctionStrategy)

\# Ex 1

플러그인 요청에 해당하는 플러그인 요청 및 버전을 수정합니다.

 

<< settings.gradle >>

```groovy
pluginManagement {
    resolutionStrategy {
        eachPlugin {
            if (requested.id.namespace == 'org.gradle.sample') {
                useModule('org.gradle.sample:sample-plugins:1.0.0')
            }
        }
    }
    repositories {
        maven {
            url '../maven-repo'
        }
        gradlePluginPortal()
        ivy {
            url '../ivy-repo'
        }
    }
}
```

 

 

## ◆ 플러그인 마커 아티팩트

플러그인 마커 아티팩트란 플러그인을 조회하기위한 플래그를 의미합니다.

형식은 plugin.id : plugin.id.gradle.plugin : plugin.version 입니다.

이 마커는 실제 플러그인 구현에 종속되어야합니다.

이 마커는 java-gradle-plugin에 의해 자동화됩니다 .

 

<< build.gradle >>

```groovy
plugins {
    id 'java-gradle-plugin'
    id 'maven-publish'
    id 'ivy-publish'
}

group 'org.gradle.sample'
version '1.0.0'

gradlePlugin {
    plugins {
        hello {
            id = 'org.gradle.sample.hello'
            implementationClass = 'org.gradle.sample.hello.HelloPlugin'
        }
        goodbye {
            id = 'org.gradle.sample.goodbye'
            implementationClass = 'org.gradle.sample.goodbye.GoodbyePlugin'
        }
    }
}

publishing {
    repositories {
        maven {
            url '../../consuming/maven-repo'
        }
        ivy {
            url '../../consuming/ivy-repo'
        }
    }
}
```

 



![img](https://blog.kakaocdn.net/dn/bCYJdj/btqCrlgJqps/IjDVW8F8sHKXODXAPmZMik/img.png)



 

 

## ◆ 레거시 플러그인 애플리케이션

플러그인 DSL을 사용하면서 기존의 방식은 사용할 필요가 없어졌지만, 빌드 작성자가 현재 동작하는 방식이 레거시로 제한되는 경우에는 아래와 같은 방식을 사용할 수 있습니다.

------

\# Ex 1

아이디를 사용한 플러그인 적용

 

<< build.gradle >>

```groovy
apply plugin: 'java'
```

------

\# Ex 2

클래스 이름을 사용한 플러그인 적용

 

<< build.gradle >>

```groovy
apply plugin: JavaPlugin
```

------

\# Ex 3

빌드 스크립트 블록으로 플러그인 적용

 

<< build.gradle >>

```groovy
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'com.jfrog.bintray.gradle:gradle-bintray-plugin:0.4.1'
    }
}

apply plugin: 'com.jfrog.bintray'
```

 

 

## ◆ Gradle 플러그인 검색 사이트

https://plugins.gradle.org/

[
](https://plugins.gradle.org/)