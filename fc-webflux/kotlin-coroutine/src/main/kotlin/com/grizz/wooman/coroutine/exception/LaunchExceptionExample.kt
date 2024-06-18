package com.grizz.wooman.coroutine.exception

import com.grizz.wooman.coroutine.help.kLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

private val log = kLogger()
fun main() {
    runBlocking {
        val job = CoroutineScope(Dispatchers.IO).launch {
            launch {
                launch {
                    throw IllegalStateException("exception in launch")
                }
            }
        }

        job.join()
    }

    println("good")
}