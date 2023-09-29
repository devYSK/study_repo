package coroutine

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class Launch {
}

fun main(): Unit = runBlocking {

    val launch = launch(start = CoroutineStart.LAZY) {
        printWithThread("hello launch")
    }


    delay(1_000L)
    launch.start()
}