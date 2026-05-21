package com.swizel.android.whereintheworld.usecases

import com.swizel.android.whereintheworld.utils.ConsoleLogger
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class SuspendUseCase<in UseCaseParams, out UseCaseResult> {

    abstract suspend fun run(
        params: UseCaseParams,
    ): UseCaseResult

    suspend operator fun invoke(
        params: UseCaseParams,
    ): UseCaseResult = withContext(Dispatchers.Default) {
        val startTime = System.currentTimeMillis()
        var cancelled = false
        try {
            ConsoleLogger.v("${this@SuspendUseCase.javaClass.simpleName} started")
            run(params)
        } catch (e: CancellationException) {
            cancelled = true
            throw e // must re-throw to preserve structured concurrency
        } finally {
            val endTime = System.currentTimeMillis()
            ConsoleLogger.v("${this@SuspendUseCase.javaClass.simpleName} ${if (cancelled) "cancelled after" else "completed in"} ${endTime - startTime}ms")
        }
    }
}

suspend operator fun <R> SuspendUseCase<Unit, R>.invoke(): R = invoke(Unit)
