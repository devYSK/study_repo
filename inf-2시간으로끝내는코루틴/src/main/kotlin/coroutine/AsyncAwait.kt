package coroutine

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class AsyncAwait {
}

fun main(): Unit = runBlocking {
    val job = async {
        3 + 5
    }

    val eight = job.await()
}
