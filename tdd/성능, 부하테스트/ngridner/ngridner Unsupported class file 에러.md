# Ngrinder Unsupported class file major version 61

로컬 환경의 기본 JDK 버전은 17이였습니다.

* m1 pro Ventura 13.0.1
* JDK 17 (amazon corretto)

nGrinder로 groovy 스크립트를 작성하고 Validate를 실행했을 때 다음과 같은 오류를 만났습니다.

```
2023-04-25 21:57:47,218 ERROR Script error - Error while initialize test runner
net.grinder.engine.common.EngineException: Error while initialize test runner
	at net.grinder.scriptengine.groovy.GroovyScriptEngine.<init>(GroovyScriptEngine.java:71)
	at net.grinder.scriptengine.groovy.GroovyScriptEngineService.createScriptEngine(GroovyScriptEngineService.java:87)
	at net.grinder.engine.process.ScriptEngineContainer.getScriptEngine(ScriptEngineContainer.java:105)
	at net.grinder.engine.process.GrinderProcess.run(GrinderProcess.java:345)
	at net.grinder.engine.process.WorkerProcessEntryPoint.run(WorkerProcessEntryPoint.java:87)
	at net.grinder.engine.process.WorkerProcessEntryPoint.main(WorkerProcessEntryPoint.java:60)
Caused by: org.codehaus.groovy.control.MultipleCompilationErrorsException: startup failed:
General error during conversion: Unsupported class file major version 61

java.lang.IllegalArgumentException: Unsupported class file major version 61
	at groovyjarjarasm.asm.ClassReader.<init>(ClassReader.java:196)
	at groovyjarjarasm.asm.ClassReader.<init>(ClassReader.java:177)
	at groovyjarjarasm.asm.ClassReader.<init>(ClassReader.java:163)
	at groovyjarjarasm.asm.ClassReader.<init>(ClassReader.java:284)
	at org.codehaus.groovy.ast.decompiled.AsmDecompiler.parseClass(AsmDecompiler.java:81)
	at org.codehaus.groovy.control.ClassNodeResolver.findDecompiled(ClassNodeResolver.java:251)
	at org.codehaus.groovy.control.ClassNodeResolver.tryAsLoaderClassOrScript(ClassNodeResolver.java:189)
	at org.codehaus.groovy.control.ClassNodeResolver.findClassNode(ClassNodeResolver.java:169)
	at org.codehaus.groovy.control.ClassNodeResolver.resolveName(ClassNodeResolver.java:125)
	at org.codehaus.groovy.ast.decompiled.AsmReferenceResolver.resolveClassNullable(AsmReferenceResolver.java:57)
	at org.codehaus.groovy.ast.decompiled.Annotations.createAnnotationNode(Annotations.java:40)
...
```

주요 에러 키워드를 보면 다음과 같습니다.

`Unsupported class file major version 61`

에러 코드를 검색해보니 해당 에러는 gradle과 jdk의 버전이 맞지 않아서 생기는 에러입니다

version 뒤의 숫자가 jdk버전에 따라 다르다고 하는데 61은 jdk 17과 호환되지 않는 문제란다.



먼저 발생했던 시나리오를 다시 되짚기 위해 실행부터 확인하였습니다.



### 발생 시나리오

로컬 자바 버전

```sh
ysk 🌈 > java -version

openjdk version "17.0.5" 2022-10-18 LTS
OpenJDK Runtime Environment Corretto-17.0.5.8.1 (build 17.0.5+8-LTS)
OpenJDK 64-Bit Server VM Corretto-17.0.5.8.1 (build 17.0.5+8-LTS, mixed mode, sharing)
```



실행시 다음과 같은 명령어로 실행하였습니다

```java
$ java -Djava.io.tmpdir=${NGRINDER_HOME}/lib -jar ngrinder-controller.war --port 7070
```

nGridner Web Admin 화면의 Script -> 기본 스크립트 - > Validate 진행

<img src="https://blog.kakaocdn.net/dn/o2x1s/btscCB9FcdG/KoNtI6SK2z7fZF9Q8PcKqk/img.png" width = 800 height =800 >

* `Unsupported class file major version 61` 에러 발생 확인

* 로컬은 자바17인데 ngrinder 기본 권장 사양은 자바 8 이였으므로 이것에 힌트를 얻어 고민하다 방법을 찾았습니다. 

 ## 해결 방법

jdk 1.8을 다운로드 하고, 실행시 지정해 주면 됩니다

* 실행 시 환경변수를 설정하는 것이기 때문에 전체 jdk가 변경되지는 않습니다! 

* 맥 m1 이기 때문에 azul을 다운로드 하였습니다.  

* [azul 다운로드 사이트](https://www.azul.com/downloads/?version=java-8-lts&os=macos&architecture=arm-64-bit&package=jdk#zulu)
  * Java 8 버전을 다운로드 하세요

자바 8버전을 설치하고 나면 다음과 같은 디렉토리에 저장이됩니다

* 디렉토리는 컴퓨터마다 다를 수 있어요

```sh
/Library/Java/JavaVirtualMachines/zulu-8.jdk/Contents/Home
```

**<u>다음 실행 명령어로 해결합니다</u>**

```
JAVA_HOME=/Library/Java/JavaVirtualMachines/zulu-8.jdk/Contents/Home Java -Djava.io.tmpdir=/Users/ysk/dev/ngrinder/lib -jar ngrinder-controller-3.5.8.war --port 7070
zsh: no such file or directory: -Djava.io.tmpdir=/Users/ysk/dev/ngrinder/lib
```

* 위 명령에서 `XXX` 부분은 설치된 JDK 8 버전에 따라 다를 수 있습니다. 이후 ngrinder를 실행하면 설정한 JDK 8이 사용됩니다.



### 간단 설명

**1. ngrinder 실행 명령어에 JDK 8 경로를 지정**

nGrinder를 실행할 때 다음 명령어랑 같이 실행해주면 됩니다.

```java
JAVA_HOME=/Library/Java/JavaVirtualMachines/zulu-8.jdk/Contents/Home
```

* 위 명령어는 JDK 8 환경 변수를 설정한것입니다. 



2. **ngridner 실행 명령어**

```
Java -Djava.io.tmpdir=/Users/ysk/dev/ngrinder/lib -jar ngrinder-controller-3.5.8.war --port 7070
zsh: no such file or directory: -Djava.io.tmpdir=/Users/ysk/dev/ngrinder/lib
```



3. 결과

<img src="https://blog.kakaocdn.net/dn/JaEjv/btscKF97umJ/KhQsI8Ec5rMuvsSannQAyK/img.png" width = 850 height =850>

**해결!** 



만약 agent를 이용한 테스트 스크립트 도 마찬가지로 발생한다면? 

agent 실행시에도 입력해주세요~ 

```java
JAVA_HOME=/Library/Java/JavaVirtualMachines/zulu-8.jdk/Contents/Home ./run_agent.sh
```

