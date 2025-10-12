package com.swizel.android.whereintheworld.startup

import android.content.Context
import com.github.apkelly.bolt.startup.BoltInitializer
import com.swizel.android.whereintheworld.utils.ConsoleLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoggingInitializer : BoltInitializer {

    override suspend fun create(
        context: Context,
    ) {
        withContext(Dispatchers.Default) {
            ConsoleLogger.init("WitW")
        }
    }

    override fun dependencies(): List<Class<out BoltInitializer>> {
        // No dependencies on other libraries.
        return emptyList()
    }
}
