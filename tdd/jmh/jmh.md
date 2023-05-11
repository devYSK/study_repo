# JMH

프로젝트와 코드를 테스트 하기 위해 알아보던 중 JMH를 알게 되었습니다. 

JMH는 JDK를 오픈소스로 제공하는 OpenJDK에서 만든 성능 측정용 라이브러리입니다. jit 컴파일러 개발자가 만들었습니다.

* https://github.com/openjdk/jmh

특정 코드에 대한 간단한 비교, 일부 로직에 대한 성능을 측정해야 할 경우에 사용할 수 있 실제 테스트하기전 워밍업 과정과 실제 측정 과정을 수행하는데 각 과정의 실행 수를 제어할 수 있고, 측정 후 결과로 나오는 시간의 단위를 지정하는 기능도 제공합니다.



JMH을 사용하여 benchmark 과정은 크게 다음과 같습니다.

1. **benchmarking project 생성**
2. **benchmark 클래스 및 소스코드 작성**
3. **benchmark 수행**

4. **benchmark 결과 확인**

# 1. 의존성 설정

2023년 5월 11일 기준.

1. https://github.com/openjdk/jmh를 참고하여 jmh plugin을 추가

```groovy
id "me.champeau.jmh" version "0.7.1"
```

2. 의존성을 추가.

```groovy
dependencies {
  
    // https://mvnrepository.com/artifact/org.openjdk.jmh/jmh-core
    jmh 'org.openjdk.jmh:jmh-core:1.36'
    // https://mvnrepository.com/artifact/org.openjdk.jmh/jmh-generator-annprocess
    jmh 'org.openjdk.jmh:jmh-generator-annprocess:1.36'
}
```

* https://mvnrepository.com/search?q=jmh 메이븐 레포지토리에서 최신 버전을 검색 가능하다.



3. jmh 옵션 추가

[jmh-gradle-plugin README.md](https://github.com/melix/jmh-gradle-plugin#configuration-options) 를 보고 원하는 설정을 추가할 수 있습니다.

저는 다음 옵션만 추가하였습니다.

```groovy
jmh {
    fork = 1
    iterations = 1
    warmupIterations = 1
}
```

* warmupIterations : 워밍업 수행 횟수
* iterations : 수행을 반복할 횟수
* fork: 단일 벤치마크를 포크할 횟수입니다. 포크를 모두 비활성화하려면 0을 입력합니다.



**최종 build.gradle**

```groovy
plugins {
    id 'java'
    id "me.champeau.jmh" version "0.7.1"
}

group 'org.ys'
version '1.0-SNAPSHOT'

repositories {
    mavenCentral()
}
jmh {
    fork = 1
    iterations = 1
    warmupIterations = 1
}
dependencies {
    testImplementation 'org.junit.jupiter:junit-jupiter-api:5.8.1'
    testRuntimeOnly 'org.junit.jupiter:junit-jupiter-engine:5.8.1'

    // https://mvnrepository.com/artifact/org.openjdk.jmh/jmh-core
    jmh 'org.openjdk.jmh:jmh-core:1.36'
    // https://mvnrepository.com/artifact/org.openjdk.jmh/jmh-generator-annprocess
    jmh 'org.openjdk.jmh:jmh-generator-annprocess:1.36'

}

test {
    useJUnitPlatform()
}
```

# 2. 디렉토리 구조 변경

https://github.com/melix/jmh-gradle-plugin#configuration 에 따르면 테스트할 클래스는

다음과 같은 디렉토리 구조를 가진 패키지에 들어있어야 합니다.

```
src/jmh
     |- java       : java sources for benchmarks
     |- resources  : resources for benchmarks
```

즉 기존에 다음과 같다면

```
root 프로젝트 폴더
    └─ src
        ├─ main
        │   ├─ java
        │   └─ resources
        └─ test
             ├─── java
             └─── resources
```

벤치마킹할 클래스들은 다음과 같이 src/jmh/java 하위에 넣어야합니다

```
root 프로젝트 폴더
    └─ src
        ├─ jmh // here
        │   ├─ java
        │   │    └─ packagename // here
        │   └─ resources
        ├─ main
        │   ├─ java
        │   └─ resources
        └─ test
             ├─── java
             └─── resources
```

* 주의해야 할 점은, `src/jmh/java` 바로 아래에 클래스 파일을 생성해서는 안되고, 해당 밑에 추가 패키지를 정의해주고 그 밑에 클래스 파일을 생성해야 합니다.
* 클래스 파일이 1개일때는 상관이 없지만, 2개 이상이 되는 순간 'Benchmark class should have package other than default.' 라는 에러가 발생하기 때문입니다.



# 3. 샘플 코드 작성

src/jmh/java/sample/Sample 클래스를 작성하였고 내용은 다음과 같습니다.

```java
package sample;

... 
  
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class Sample {

	private final int LIMIT_COUNT = 10000;
	private final List<Integer> array = new ArrayList<>();

	@Setup
	public void init() {
		// 성능 측정 전 사전에 필요한 작업
		for (int i = 0; i < LIMIT_COUNT; i++) {
			array.add(i);
		}
	}

	@Benchmark // 성능을 측정할 코드 작성
	public List<Integer> loopToAdd() {
		int size = array.size();
		List<Integer> list = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			list.add(i);
		}
		return list;
	}

	@Benchmark // 성능을 측정할 코드 작성
	public long loopToSum() {
		int result = 0;

		for (long i = 1L; i <= 100_000_000L; i++) {
			result += i;
		}

		return result;
	}

	@Benchmark // 성능을 측정할 코드 작성
	public void calculateTest() {
		for (int i = 0; i < 1_000_000; i++) {
			calculate(i);
		}
	}

	private void calculate(int i) {
		System.out.println(i + i);
	}

}
```

* 실제 벤치마킹할 메소드는 `loopToAdd()`,  `loopToSum()`, `calculateTest()` 이며 각 어노테이션들의 의미는 다음과 같습니다



https://ysjee141.github.io/blog/quality/java-benchmark/ 참조

### @State

JMH는 벤치마크에 사용되어지는 Argument의 상태를 지정할 수 있다. 벤치마크 테스트를 진행하다보면 상황에 따라 Argument가 항상 초기화 되어야 할 때도 있고, 값이 항상 유지되어야 할 경우도 있을텐데 이것을 가능하게 하는 것이 @State Annotation이며, 정의 가능한 Scope는 아래와 같다.

* **@State** 어노테이션으로 테스트 argument의 상태를 지정할 수 있다.

| Scope           | Description                                                  |
| --------------- | ------------------------------------------------------------ |
| Scope.Thread    | 각 Thread 별로 인스턴스를 생성하여 사용하는 모드             |
| Scope.Benchmark | 동일 테스트 내에서 모든 Thread에서 동일한 인스턴스를 공유하여 사용하는 모드로, Multi-Threading 테스트에 적합한 모드 |
| Scope.Group     | Thread 그룹마다 인스턴스를 생성하여 사용하는 모드            |

@Steate Annotation이 적용되는 클래스는 다음과 같은 규칙이 있습니다.

* 반드시 Public 클래스로 선언되어야 하며, 만약 중첩 클래스인 경우에는 static로 선언되어야 한다.
* Argument가 없는 기본 생성자가 반드시 있어야 한다. 
* 위 규칙을 준수하면 @State Annotation을 클래스에 적용하여 JMH가 인식하도록 할 수 있다.

 ### @BenchmarkMode

측정하려는 항목별로 다양한 모드를 지원한다. 

| Mode                | Description                                                  |
| ------------------- | ------------------------------------------------------------ |
| Mode.Throughput     | 초당 작업수를 측정하는 기본 모드                             |
| Mode.AverageTime    | 작업이 수행되는 평균 시간을 측정하는 모드                    |
| Mode.SampleTime     | 최대, 최소 시간 등 작업이 수행하는데 걸리는 시간을 측정하는 모드 |
| Mode.SingleShotTime | 단일 작업 수행 소요 시간을 측정하는 모드로, JVM 예열 없이 Cold Start에 적합한 모드 |
| Mode.All            | 위의 모든 시간을 측정하는 종합 모드                          |

* Mode.All로 지정할 경우 경우 모든 Mode가 순차적으로 수행된다.

### 설정 및 해제를 위한 @Setup / @TearDown

 `@Setup`은 메서드가 벤치마크 메서드에 전달되기 전에 객체나 상태를 set up 하기 위해 설정하는 어노테이션입니다. JUnit5의 @BeforeEach와 비슷하다고 보면 됩니다. 

 `@TearDown`은 벤치마크가 실행된 후 객체나 상태를 정리하기 위해 사용하는 어노테이션입니다. JUnit5의 @AfterEach랑 비슷하다고 보시면 됩니다.

* @Setup / @TearDown 의 실행시간은 벤치마크 측정 시간에 포함되지 않습니다.



@SetUp, @TearDown 어노테이션과 함께 Level Argument의 설정이 가능하며, 설정은 다음과 같습니다.

* 

| Level            | Description                                                  |
| ---------------- | ------------------------------------------------------------ |
| Level.Trial      | 벤치마크를 실행할 때마다 호출되며, 실행은 전체 Fork를 의미합니다. |
| Leve.Iteration   | 벤치마크를 반복할 때마다 한 번씩 호출됩니다.                 |
| Level.Invocation | 벤치마크 메소드를 호출할 때마다 호출됩니다.                  |



#### Dead Code

벤치마킹하는 메소드 내에서 특정 변수를 사용하지 않는다면 .jmh는 해당 코드를 인식하지 않는데 이를 Dead Code라 합니다.

때문에 다음과 같은 코드를 사용하는것을 주의하며 결과는 반드시  **Return** 하거나 **Black Hole 함수**를 사용해야 합니다

```java
public class MyBenchmark {

    @Benchmark
    public void testMethod() {
        int a = 1;
        int b = 2;
        int sum = a + b; // 사용 안하므로 dead Code
    }

}
```

 #### Black Hole 함수

jmh 벤치마크에서, 계산은 되었지만 사용하지 않는 값을 반환하여 측정이 될 수 있도록 할 수 있습니다. 

다음처럼 사용할 수 있습니다. 

```java
public class MyBenchmark {

    @Benchmark
   public void testMethod(Blackhole blackhole) {
        int a = 1;
        int b = 2;
        int sum = a + b;
        blackhole.consume(sum);
    }
}
```

* blackhole.consume(sum) 
* Method에 JMH가 제공하는 Blackhole 클래스를 Argument로  주입 받고 사용하면, Method 내에서 계산된 값을 넘겨주면 JVM에서는 계산된 값을 사용한 것으로 인식하여 정확한 측정이 가능하다




### @Fork

@Fork 어노테이션으로 벤치마크에 대한 기본 포크 매개변수를 설정할 수 있습니다. 이 어노테이션은 해당 메서드에만 영향을 미치기 위해 Benchmark 메서드에 배치하거나 클래스의 모든 Benchmark 메서드에 영향을 미치기 위해 둘러싸는 클래스 인스턴스에 배치할 수 있습니다. build.gradle에서 jmh {}을 이용해서 설정하더라도, @Fork를 이용해  런타임 옵션으로 재정의될 수 있습니다.

```java
public @interface Fork {

    int BLANK_FORKS = -1;

    String BLANK_ARGS = "blank_blank_blank_2014";

    /** @return number of times harness should fork, zero means "no fork" */
    int value() default BLANK_FORKS;

    /** @return number of times harness should fork and ignore the results */
    int warmups() default BLANK_FORKS;

    /** @return JVM executable to run with */
    String jvm() default BLANK_ARGS;

    /** @return JVM arguments to replace in the command line */
    String[] jvmArgs() default { BLANK_ARGS };

    /** @return JVM arguments to prepend in the command line */
    String[] jvmArgsPrepend() default { BLANK_ARGS };

    /** @return JVM arguments to append in the command line */
    String[] jvmArgsAppend() default { BLANK_ARGS };

}
```

다음과 같이 사용할 수 있습니다

```java
@Fork(value = 10, jvmArgs= {"-Xms4G", "-Xmx4G"})
```

* 외부 변수의 영향을 배제하거나 평균을 정확하게 측정하기 위해 10회 실시합니다.

* 힙 영역이 모자를 수 있으므로 gc 오버헤드(STW)를 줄이기 위해 힙 영역을 4G로 설정합니다. 

# 4. 벤치마크 실행

다음 명령어로 벤치마크를 실행할 수 있습니다

```shell
./gradlew jmh
```

* 다음의 테스크들도 지원한다

| Task Name                  | Description                                                  |
| -------------------------- | ------------------------------------------------------------ |
| jmhClasses                 | 원시 벤치마크 코드를 컴파일하는 태스크 <br />벤치마크 원시 코드를 컴파일 |
| jmhRunBytecodeGenerator    | 원시 벤치마크 코드에 바이트코드 생성기를 실행하고 실제 벤치마크를 생성하는 태스크 <br />벤치마크 원시 코드에 대해 바이트코드 생성기를 실행하고 실제 벤치마크를 생성 |
| jmhCompileGeneratedClasses | 생성된 벤치마크를 컴파일하는 태스크<br />생성된 벤치마크를 컴파일 |
| jmhJar                     | JMH 런타임과 컴파일된 벤치마크 클래스를 포함하는 JMH JAR 파일을 빌드하는 태스크 <br />JMH 런타임 및 컴파일된 벤치마크 클래스를 포함하는 JMH JAR 파일을 빌드 |
| jmh                        | 벤치마크를 실행하는 태스크                                   |



벤치마크가 끝나면 다음의 메시지가 출력됩니다.

```
NOTE: Current JVM experimentally supports Compiler Blackholes, and they are in use. Please exercise
extra caution when trusting the results, look into the generated code to check the benchmark still
works, and factor in a small probability of new VM bugs. Additionally, while comparisons between
different JVMs are already problematic, the performance difference caused by different Blackhole
modes can be very significant. Please make sure you use the consistent Blackhole mode for comparisons.

Benchmark             Mode  Cnt      Score   Error  Units
Sample.calculateTest  avgt       15558.388          ms/op
Sample.loopToAdd      avgt           0.035          ms/op
Sample.loopToSum      avgt          36.600          ms/op

Benchmark result is saved to ~~~/jmh/jmh-test/build/results/jmh/results.txt
```



# 5. 벤치마크 테스크 결과 확인

벤치마크 테스크 결과는 build/result/jmh 디렉토리 하위에 results.txt 라는 이름의 파일로 생성됩니다. 

```
Benchmark             Mode  Cnt      Score   Error  Units
Sample.calculateTest  avgt       15558.388          ms/op
Sample.loopToAdd      avgt           0.035          ms/op
Sample.loopToSum      avgt          36.600          ms/op
```

@OutputTimeUnit(TimeUnit.MILLISECONDS)이기 때문에 위 결과는 ms로 결과가 저장되었습니다. 

- `Benchmark`: 벤치마크의 이름입니다.
- `Mode`: 벤치마크 모드를 나타냅니다. 이 경우에는 `avgt`로, 평균 시간(AverageTime) 모드를 사용했습니다.
- `Cnt`: 벤치마크 실행 횟수를 나타냅니다.
- `Score`: 벤치마크의 결과 값으로, 낮을수록 더 좋은 성능을 나타냅니다. 단위는 `ms/op`(밀리초/작업)입니다.
- `Error`: 측정된 벤치마크 값의 오차 범위를 나타냅니다.
- `Units`: 결과 값의 단위를 나타냅니다.

해석을 해보면:

1. `Sample.calculateTest` 벤치마크는 평균 실행 시간이 15558.388 ms/op입니다. 이는 각 작업당 약 15.6초가 소요됨을 의미합니다.
2. `Sample.loopToAdd` 벤치마크는 평균 실행 시간이 0.035 ms/op입니다. 작업당 약 0.035 밀리초가 소요됨을 의미합니다.
3. `Sample.loopToSum` 벤치마크는 평균 실행 시간이 36.600 ms/op입니다. 작업당 약 36.6 밀리초가 소요됨을 의미합니다.

# Samples

JMH의 여러 샘플들입니다. 참고해서 작성해보면 좋습니다.

https://hg.openjdk.org/code-tools/jmh/file/39ed8b3c11ce/jmh-samples/src/main/java/org/openjdk/jmh/samples

# 결론

JMH는 OpenJDK Code Tools Project 중 하나로 운영되고 있으며 아직까지도 꾸준히 업데이트 되고 있습니다.
또한 Oracle의 JIT complier 개발자가 만들었기 때문에 타 benchmark framework보다 신뢰할 수 있다는 것이 장점입니다

그러나, 프로젝트를 새로 만들거나, 디렉토리를 만들어 추가로 benchmark 코드를 작성하여 테스트 해야 한다는 번거로움이 큰 단점입니다. 테스트 계획도 세워야 하는데 말이죠..

그리ㄴ고, 정확한 측정 결과를 위해서는 warmup 횟수와 메서드 수행 횟수를 늘려야 합니다.



### 참조

* https://github.com/openjdk/jmh

* https://www.baeldung.com/java-microbenchmark-harness

* https://github.com/melix/jmh-gradle-plugin

* https://jenkov.com/tutorials/java-performance/jmh.html

