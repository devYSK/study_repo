package coroutine

import kotlinx.coroutines.*
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class Lec07 {
}


fun main() {

    val threadPool = Executors.newSingleThreadExecutor()

    CoroutineScope(threadPool.asCoroutineDispatcher())
        .launch {
            printWithThread("새로운 코루틴")
        }
}