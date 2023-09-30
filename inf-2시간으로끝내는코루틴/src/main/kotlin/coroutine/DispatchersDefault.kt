package coroutine

import kotlinx.coroutines.*

class DispatchersDefault {
}

fun main(): Unit = runBlocking {
    val job = launch(Dispatchers.Default) { // 하드코딩된 디스패처를 피하세요.
        var i = 1
        var nextPrintTime = System.currentTimeMillis()
        while (i <= 5) {
            if (nextPrintTime <= System.currentTimeMillis()) {
                printWithThread("${i++} 번째 출력!")
                nextPrintTime += 1_000L // 1초 후에 다시 출력되도록 한다.
            }

            if (!isActive) { // 코루틴 스스로 취소를 확인한다.
                throw CancellationException()
            }
        }
    }

    delay(100L)
    printWithThread("취소 시작")
    job.cancel()
}
