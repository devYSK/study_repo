# 인프런 2시간으로 끝내는 코루틴

[toc]



# 1강. 루틴과 코루틴

co-routine : 협력하는 루틴, 함수

* routine 은 컴퓨터 공학에서 이야기하는루틴으로 간단히 ‘함수’라고 생각해도 좋다

```kotlin
fun main() {
	println("START")
	newRoutine()
	println("END")
} 

fun newRoutine() {
	val num1 = 1
	val num2 = 2
	println("${num1 + num2}")

}
```

1. ﻿﻿﻿main 루틴이 START 를 출력한 이후 new 루틴을 호출한다.
2. ﻿﻿﻿new 루틴은 1과 2를 계산해 3을 출력한다
3. ﻿﻿﻿그 이후, new 루틴은 종료되고 main 루틴으로 돌아온다.
4. ﻿﻿﻿main 루틴은 END 를 출력하고 종료된다.



이때 newRoutine()이 종료된 이후에는 해당 함수에서 사용된 num1과 num2는 다시 접근할수 없이 초기화 된다

- ﻿﻿루틴은 진입하는 곳이 한 곳이고
- ﻿﻿루틴이 종료되면 그 루틴에서 사용했던 정보가 초기화

## 코틀린 사용 의존성

```kotlin
dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.2")
    testImplementation(kotlin("test"))
}
```

```kotlin
fun main(): Unit = runBlocking {
    printWithThread("START")
    launch {
        newRoutine()
    }
    yield()
    printWithThread("END")
}

suspend fun newRoutine() {
    val num1 = 1
    val num2 = 2
    yield()
    printWithThread("${num1 + num2}")
}

fun printWithThread(str: Any?) {
    println("[${Thread.currentThread().name}] $str")
}
```

runBlocking 함수는 일반 루틴 세계와 코루 틴 세계를 연결하는 함수이다. 

이 함수 자체로 새로운 코루틴을 만들게 되고, runB Locking 에 넣어준 람다가 새로운 코루틴 안에 들어가게 된다.

 Launch 함수 :  새로운 코루틴을 만드는 함수. 주로 반환 값이 없는 코루틴을 만드는데 사용된다.

yield() 라는 함수는 지금 코루틴의 실행을 잠시 멈추고 다른 코루틴이 실행 되도록 양보한다.

* 이 코드에는 2개의 코루틴이 있다. runBlocking과 launch

suspend 라는 키워드가 붙으면 다른 suspend fun 을 호출할수 있다.

*  yield() 가 suspend fun 이기 때문에 함수 newRoutine 에 suspend 를 붙여 주었다.

실행결과

```
[main] START
[main] END
[main] 3
```

1. ﻿﻿﻿main 코루틴이 runBlocking 에 의해 시작되고 START 가 출력된다.
2. ﻿﻿﻿Launch 에 의해 새로운 코루틴이 생긴다. 하지만, newRoutine의 실행은 바로 일어나지 않는다.
3. ﻿﻿﻿main 코루틴 안에 있는 yield() 가 되면 main 코루틴은 newRoutine 코루틴에게 실행을 양보한다. 따라서 launch 가 만든 새로운 코루틴이 실행되고, newRoutine 함수가 실행된다.
4. ﻿﻿﻿newRoutine 함수는 다시 yield() 를 호출하고 main 코루틴으로 되돌아온다.
5. ﻿﻿﻿main 루틴은 END 를 출력하고 종료된다.
6. ﻿﻿﻿아직 newRoutine 함수가 끝나지 않았으니 newRoutine 함수로 되돌아가 3 이 출력되
    고 프로그램이 종료된다.

루틴과 코루틴의 가장 큰 차이는 중단과 재개이다. 

루틴은 한 번 시작되면 종료될 때 까지 멈추지 않지만, 코루틴은 상황에 따라 잠시 중단이 되었다가 다시 시작되기도 한다. 

`때문에 완전히 종료되기 전까지는 newRoutine 함수 안에 있는 num1 num2 변수가 메모리에서 제거되지도 않는다.`

<img src="./images//image-20230929232949381.png">

### 인텔리제이에서 코루틴 로그보기

InteliJ IDEA에서 vm option으로 ``-Dkotlinx.coroutines.debug` 를 주게 되면 어떤 코루 틴에서 출력이 일어났는지 확인할 수도 있다.

<img src="./images//image-20230929232608980.png">

# 2강. 스레드와 코루틴

프로세스(process)는 컴퓨터에서 실행되고 있는 프로그램을 의미하며
스레드는(thread)는 프로세스보다 작은 개념으로 프로세스에 소속되어 여러 코드를 동시에 실행할 수있도록 해준다. 

스레드는 코드를 실행하고 따라서 우리가 작성한 코드는 특정 스레드에서 실행된다.

그렇다면 코루틴은 무엇일까? `코루틴은 스레드보다 작은 개념이다`

* 코루틴은 특정 스레드에 종속되어있지 않다. 

가장 먼저 코루틴은 단지 우리가 작성한 루틴, 코드의 종류 중 하나이기 때문에 코루틴 코드가 실행되려면 스레드가 있어야만 한다. 

그런데 코루틴은 중단되었다가 재개될 수 있기 때문에(suspend), 코루틴 코드의 앞부분은 1번 스레드에 배정되고, 뒷부분은 2번 스레드에 배정될 수 있다.

## 스레드와 코루틴의 context switching 차이

- ﻿﻿프로세스
  - ﻿﻿프로세스는 각각독립된 메모리 영역을 갖고 있기 때문에 1번 프로세스에서 2번 프로세스로 실행이 변경되면 는 OS 수준에서 수행되며, CPU 레지스터, 프로그램 카운터, VM 상태, 스택, 힙 영역 등의 정보가 저장되고 모두 교체되어야 한다.
  - ﻿﻿때문에 프로세스 간의 context switching은 비용이 제일 크다.
- ﻿﻿스레드
  - ﻿﻿스레드는 독립된 스택 영역을 갖고 있지만, 힙 영역을 공유하고 있기 때문에 실행이 변경되면 스택 영역만 교체된다.
  - ﻿﻿따라서 프로세스보다는 context switching 비용이 적다.
  - 스레드는 병렬 실행이 가능하다. 멀티 코어 프로세서에서 여러 스레드는 동시에 실행될 수 있습니다.
  - 각 스레드는 고정된 크기의 스택 메모리를 가져서 많은 스레드를 생성하면 메모리 사용이 증가한다.
- ﻿﻿코루틴
  - ﻿﻿반면 코루틴은 1번 코루틴과 2번 코루틴이 같은 스레드에서 실행될 수 있다.
  - 때문에 동일한 스레드에서 코루틴이 실행되면, 메모리 전부를 공유하므로 스레드보다도 context switching 비용이 적다.
  - 기본적으로 코루틴은 동시성을 위해 설계되었지만, 병렬 실행을 위한 것은 아니다. 하지만 코루틴을 병렬로 실행하려면 특정 스레드 풀 또는 다른 백그라운드 메커니즘과 함께 사용할 수 있다.
  - 코루틴은 필요에 따라 동적으로 메모리를 할당합니다. 이로 인해 많은 코루틴을 생성하더라도 메모리 부담이 크게 늘어나지 않는다.

또한, 스레드는 동시성을 확보하기 위해 여러 개의 스레드가 필요하다. 

반면, 코루틴은 1번 코루틴과 2번 코루틴이 하나의 스레드에서 번갈아 실행될 수 있기 때문에 단 하나의 스레드 만으로도 동시성을 확보할 수 있다.

* 동시성은 한번에 한가지 일만 할수있지만 두 작업이 아주 빠르게 실행되어 동시에 보이는것. 

코루틴과 같은 방식을 '비선점형'이라 부르고 스레드와 같은 방식을 '선점형'이라 부른다.

# 3강. 코루틴 빌더와 Job

코루틴을 만드는 방법

1. runBlocking

2. launch
3. async()



## runBlocking

```kotlin
import kotlinx.coroutines.runBlocking

fun main() = runBlocking { 
    
}
```

새로운 코루틴을 만들고 기존 루틴과 코루틴을 이어주는 역할을 한다 

- 현재 스레드를 블로킹하여 주어진 람다 내의 코루틴 코드를 실행.
- 주로 테스트나 메인 함수에서 코루틴 코드를 동기적으로 실행할 때 사용

주의할점은, 이름에 blocking이 들어가있다는건 runBlocking으로 인해 만들어진 코루틴과 그 안에 있는 코루틴이 모두 완료될때까지 코드를 블락시킨다.

* 스레드가 블락되면 해당 스레드는 블락이 풀릴때까지 다른 코드를 실행시킬 수 없다

```kotlin
fun main() {

    runBlocking {
        printWithThread("START")
        launch {
            delay(2_000L)
            printWithThread("LAUNCH END")
        }

    }
    printWithThread("END")
}

```

main 함수에는 `runBlocking` 으로 만들어진 코루틴이 있고, `runBlocking` 으로 만들어진 코루 틴 안에는 `다시 한번 Launch 로 만들어진 코루틴`이 있다. 

* 여기서 사용된 delay() 함수는 코 루틴을 지정된 시간 동안 지연시키는 함수이다

이 코드에서 END 가 출력되기 위해서는 runBlocking 때문에 두 개의 코루틴이 모두 완전히

종료되어야 하고 따라서 출력 결과는 다음과 같다.

```kotlin
[main @coroutine#1] START
[main @coroutine#2] LAUNCH END
[main] END
```

* END가 출력되기 전에 runBlocking  블록이 끝나야 해서 2초를 기다려야하는 문제가 있다. 즉 스레드가 블락당한것이다

때문에 runBlocking함수를 사용하는것은 별로 좋지 않고, 메인 함수나 테스트 코드 작성시에만 사용하는것이 좋다



## launch

반환 값이 없는 코드를 실행할 때 사용한다.

`launch` 는 runBLocking 과는 다르게 만들어진 코루틴을 결과로 반환하고, 이 객체를 이용해 코루틴을 제어할 수 있다.
 이 객체의 타입은 `Job` 으로 코루틴을 나타낸다. 즉, Job 을 하나 받으면 코루틴을 하나 만든 것이다.

```kotlin
fun main() = runBlocking {
    val job = launch {
        printWithThread("Hello Launch")
    }
}
```

* job은 launch가 만들어낸 코루틴 자체를 제어할 수 있는 객체이다. 

Job 을 이용해 코루틴을 제어할 수 있다고 했는데, 제어한다는 의미는 무엇일까?!

* 시작시키거나, 취소시키거나, 종료시까지 대기하게 할 수 있다. 

예를 들어 우리가 코루틴을 만든 후, 시작 신호를 주어야 코루틴이 실행되도록 변경해 보자.

### launch-  job.start()

```kotlin
fun main(): Unit = runBlocking {

    val launch = launch(start = CoroutineStart.LAZY) {
        printWithThread("hello launch")
    }

    delay(1_000L)
    launch.start()
}

```

* launch 라는 코루틴 빌더를 사용할 때 coroutinestart. LAZY 옵션을주면 코루틴이 즉시 실행되지 않는다.

Job.start()를 직접 호출해야만 동작한다.

### launch - job.cancel()

canceL() 함수는 우리가 만든 코루틴을 취소하는 기능이다

```kotlin
fun main(): Unit = runBlocking {
    val job = launch {
        (1..5).forEach {
            printWithThread(it)
            delay(500)
        }
    }

    delay(1_000L)
    job.cancel()
}
```

원래대로라면 1~5까지 출력하지만, 1초뒤에  cancel() 시켜서 2까지만 출력한다

### launch - job.join()

Job 객체의 join() 기능을 사용하면 우리가 제어하고 있는 코루틴이 끝날 때까지 대기할 수도 있다

```kotlin
fun main(): Unit = runBlocking {
    val job1 = launch {
        delay(1_000)
        printWithThread("Job 1")
    }

    val job2 = launch {
        delay(1_000)
        printWithThread("Job 2")
    }

}
```

결과

```
[main] Job 1
[main] Job 2
```

위의 코드는 각각의 코루틴에서 delay가 1초씩 걸려 있지만, Job 1과 Job 2가 출력되는데

1.1초 정도면 충분하다! 그 이유는, j0b1에서 1초를 기다리는 동안 job2가 시작되어 함께 1 초를 기다리기 때문이다.
<img src = "./images//image-20230930011947242.png">

join() 기능을 사용하면 job1이 끝날떄까지 기다린다

```kotlin
fun main(): Unit = runBlocking {
    val job1 = launch {
        delay(1_000)
        printWithThread("Job 1")
    }
    job1.join()

    val job2 = launch {
        delay(1_000)
        printWithThread("Job 2")
    }

}
```

첫 번째 코루틴에 대해 join() 을 호출하며 첫 번째 코루틴이 끝날 때까지 완전히 기다렸기 때문에 시간이 더 걸린다

<img src="./images//image-20230930012046250.png">

## async()

async() 는 Launch() 와 거의 유사한데 딱 한 가지 다른 점이 있다. 
주어진 함수의 실행 결과를 반환할 수 없는 launch() 와 달리 async() 는 주어진 함수 실행 결과를 반환할 수 있다.

```kotlin
fun main(): Unit = runBlocking {
    val job = async {
        3  + 5
    }
}
```

async() 역시 Launch() 처럼 코루틴을 제어할 수 있는 객체를 반환하며 그 객체는 Deferred 이다.

* launch는 Job이다

Deferred 는 Job 의 하위 타입으로 Job 과 동일한 기능들이 있고, async() 에서 실행된 결 과를 가져오는 await() 함수가 추가적으로 존재한다.

<img src="./images//image-20230930012500581.png">

```kotlin
fun main(): Unit = runBlocking {
    val job = async {
        3 + 5
    }

    val eight = job.await()
}
```

이 async() 함수는 여러 외부 자원을 동시에 호출해야 하는 상황에서 유용하게 활용될 수 있다.

예를 들어, 두 API를 각각호출해 결과를 합해야 한다고 하자.

```kotlin
fun main(): Unit = runBlocking {
    val time = measureTimeMillis { // 소요시간 측정 
        val job1 = async { apiCall1() }
        val job2 = async { apiCall2() }
        printWithThread(job1.await() + job2.await())
    }
    
    printWithThread("소요 시간 : $time ms")
}

suspend fun apiCall1(): Int {
    delay(1_000L)
    return 1
}

suspend fun apiCall2(): Int {
    delay(1_000L)
    return 2
}
```

또한, 첫 번째 API의 결과가 두 번째 API에 필요한 경우에는 calback을 이용하지 않고도, 동기 방식으로 코드를 작성할 수 있게 해준다.

```kotlin
fun main(): Unit = runBlocking {
    val time = measureTimeMillis {
        val job1 = async { apiCall1() }
        val job2 = async { apiCallV2(job1.await()) }
        printWithThread(job2.await())
    }

    printWithThread("소요 시간 : $time ms")
}
```

async() 와 관련해 한 가지 주의할 점으로는, coroutinestart. LAZY 옵션을 사용해 코루틴을 지연 실행시킨다면, 

* 지연 코루틴이라고 한다 

await() 함수를 호출했을 때 계산 결과를 계속해서 기다린다는 것이다.

```kotlin

fun main(): Unit = runBlocking {
    val time = measureTimeMillis {
        val job1 = async(start = CoroutineStart.LAZY) { apiCall1() }
        val job2 = async(start = CoroutineStart.LAZY) { apiCallV2() }
        printWithThread(job1.await() + job2.await())
    }
    printWithThread("소요 시간 : $time ms") // 소요시간 2초 
}

suspend fun apiCallV2(): Int {
    delay(1_000L)
    return 2
}
```

만약 지연 코루틴 을 async()와 함께 사용하면서 동시에 호출하고 싶다면 start()를 먼저 써줘야 한다

```kotlin
fun main(): Unit = runBlocking {
    val time = measureTimeMillis {
        val job1 = async(start = CoroutineStart.LAZY) { apiCall1() }
        val job2 = async(start = CoroutineStart.LAZY) { apiCallV2() }

        job1.start() // 호출
        job2.start()
        
        printWithThread(job1.await() + job2.await())
    }
    printWithThread("소요 시간 : $time ms") // 결과 1초 
}
```





# 4강. 코루틴의 취소



# 5강. 코루틴의 예외 처리와 Job의 상태 변화



# 6강. Structued Concurrency



# 7강. CoroutineScope과 CoroutineContext



# 8강. suspending function



# 9강. 코루틴과 Continuation



# 10강. 코루틴의 활용과 마무리