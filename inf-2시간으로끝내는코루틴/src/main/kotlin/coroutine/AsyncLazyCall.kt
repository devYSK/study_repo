package coroutine

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

class AsyncLazyCall {
}


fun main(): Unit = runBlocking {
    val time = measureTimeMillis {
        val job1 = async(start = CoroutineStart.LAZY) { apiCall1() }
        val job2 = async(start = CoroutineStart.LAZY) { apiCallV2() }

        job1.start()
        job2.start()

        printWithThread(job1.await() + job2.await())
    }
    printWithThread("소요 시간 : $time ms")
}

suspend fun apiCallV2(): Int {
    delay(1_000L)
    return 2
}