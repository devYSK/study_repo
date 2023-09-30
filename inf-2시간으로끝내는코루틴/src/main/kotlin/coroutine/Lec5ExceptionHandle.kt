import coroutine.printWithThread
import kotlinx.coroutines.*

fun main(): Unit = runBlocking {
    val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        printWithThread("예외")
    }

    val job = CoroutineScope(Dispatchers.Default)
        .launch(exceptionHandler) {
            throw IllegalArgumentException()
        }

    delay(1_000L)
}