package coroutine

import kotlinx.coroutines.*

class LaunchException {
}

fun main(): Unit = runBlocking {
    val job = launch { // async로 변경해도 동일하다.
        throw IllegalArgumentException()
    }
    delay(1_000L)
}