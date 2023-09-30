package coroutine

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull

class WithTimeout {
}

fun main(): Unit = runBlocking {

    val result = withTimeoutOrNull(100L) {
        delay(500L)
        10 + 20
    }

    printWithThread(result)
}