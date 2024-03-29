package com.todo.common.helper

import java.util.concurrent.atomic.AtomicInteger
import kotlinx.coroutines.*

class RateLimiter(
    private val rateLimit: Int, // Number of actions allowed per time window
    private val timeWindow: Long, // Time window in milliseconds
    private val callback: suspend () -> Unit
) {
    private val tokenCount = AtomicInteger(rateLimit)
    private val scope = CoroutineScope(Dispatchers.IO) + Job()

    init {
        scope.launch {
            while (true) {
                delay(timeWindow /*/ rateLimit*/)
                refillToken()
            }
        }
    }

    private fun refillToken() {
        tokenCount.set(rateLimit)
    }

    suspend fun hitApi() {
        if (tokenCount.getAndDecrement() > 0) {
            callback.invoke()
        } else {
            // If no tokens are available, log or handle accordingly
            println("Rate limit exceeded. Unable to perform the action.")
        }
    }

    fun release() {
        scope.cancel()
    }
}

fun main() {
    runBlocking {
        // Example usage of com.example.todoapplication.common.helper.RateLimiter
        val rateLimiter = RateLimiter(rateLimit = 5, timeWindow = 10000L) {
            // Action to be performed within the rate limit
            println("Action performed at ${System.currentTimeMillis()}")
        }

        // Try to perform the action multiple times
        repeat(10) {
            rateLimiter.hitApi()
            delay(180)
        }

        // Release resources when done
        rateLimiter.release()
    }
}
