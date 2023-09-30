package coroutine

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class CancelLec4 {
}

fun main(): Unit = runBlocking {
    val job = launch {
        var i = 1
        var nextPrintTime = System.currentTimeMillis()
        while (i <= 5) {
            if (nextPrintTime <= System.currentTimeMillis()) {
                printWithThread("${i++} 번째 출력!")
                nextPrintTime += 1_000L // 1초 후에 다시 출력되도록 한다.
            }
        }
    }

    delay(100L)

    job.cancel()
}