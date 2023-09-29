package coroutine

import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class Async {
}

fun main(): Unit = runBlocking {
    val job = async {
        3  + 5
    }
}