package coroutine

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class Lec03 {
}

//fun main() {
//
//    runBlocking {
//        printWithThread("START")
//        launch {
//            delay(2_000L)
//            printWithThread("LAUNCH END")
//        }
//
//    }
//    printWithThread("END")
//}


//fun main(): Unit = runBlocking {
//    val job1 = launch {
//        delay(1_000)
//        printWithThread("Job 1")
//    }
//    job1.join()
//
//    val job2 = launch {
//        delay(1_000)
//        printWithThread("Job 2")
//    }
//
//}
