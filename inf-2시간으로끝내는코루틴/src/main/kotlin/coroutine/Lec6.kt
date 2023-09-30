package coroutine

import kotlinx.coroutines.*
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class Lec6 {
}


suspend fun main() {
    val job = CoroutineScope(Dispatchers.Default).launch {
        delay(1_000L)
        printWithThread("Job 1")
    }
    job.join()

    // + 기호를 이용해 Element 합성
    var coroutineContext = CoroutineName("나만의 코루틴") + SupervisorJob()
    printWithThread(coroutineContext)

    // context에 element를 추가
    coroutineContext += CoroutineName("나만의 코루틴2")
    println(coroutineContext)
}

class AsyncLogic {
    private val scope = CoroutineScope(Dispatchers.Default)

    fun doSomething() {
        scope.launch {
        // 여기서 어떤 작업을 하고 있다.
        }
    }

    fun destroy() {
        scope.cancel()
    }
}