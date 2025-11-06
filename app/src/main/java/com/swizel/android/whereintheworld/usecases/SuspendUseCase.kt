package com.swizel.android.whereintheworld.usecases

import com.swizel.android.whereintheworld.utils.ConsoleLogger
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
        try {
            ConsoleLogger.v("${this@SuspendUseCase.javaClass.simpleName} started")
            run(params)
        } finally {
            val endTime = System.currentTimeMillis()
            ConsoleLogger.v("${this@SuspendUseCase.javaClass.simpleName} completed in ${endTime - startTime}ms")
        }
    }
}
