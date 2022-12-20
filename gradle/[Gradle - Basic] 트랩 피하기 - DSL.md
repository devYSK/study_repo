 # 트랩 피하기



- ﻿﻿Groovy 스크립트 변수
- ﻿﻿Groovy 구성 및 실행

## ◆ Groovy 스크립트 변수

Groovy DSL 사용자는 Groovy가 스크립트 변수를 처리하는 방법을 이해하는 것이 중요합니다. Groovy에는 두 가지 유형의 스크립트 변수가 있습니다. 하나는 로컬 범위이고 다른 하나는 스크립트 범위입니다.

 

String / def 로 선언하지 않고 이름으로만 선언되어있는 변수가 스크립트 전체에서 사용되는 스코프입니다.

 

<< scope.groovy >>

```groovy
String localScope1 = 'localScope1'
def localScope2 = 'localScope2'
scriptScope = 'scriptScope'

println localScope1
println localScope2
println scriptScope

closure = {
    println localScope1
    println localScope2
    println scriptScope
}

def method() {
    try {
        localScope1
    } catch (MissingPropertyException e) {
        println 'localScope1NotAvailable'
    }
    try {
        localScope2
    } catch(MissingPropertyException e) {
        println 'localScope2NotAvailable'
    }
    println scriptScope
}

closure.call()
method()
```

 

<< Command >>

```
> groovy scope.groovy
localScope1
localScope2
scriptScope
localScope1
localScope2
scriptScope
localScope1NotAvailable
localScope2NotAvailable
scriptScope
```

 

 

## ◆ Groovy 구성 및 실행

Gradle에는 고유 한 구성 및 실행 단계가 있습니다

구성 단계 중에 디렉토리 작성이 발생하면 clean 태스크는 실행 단계 중에 디렉토리를 제거합니다.

 

<< build.gradle >>

```groovy
def classesDir = file('build/classes')
classesDir.mkdirs()
task clean(type: Delete) {
    delete 'build'
}
task compile {
    dependsOn 'clean'
    doLast {
        if (!classesDir.isDirectory()) {
            println 'The class directory does not exist. I can not operate'
            // do something
        }
        // do something
    }
}
```

 

<< Command >>

```
> gradle -q compile
The class directory does not exist. I can not operate
```