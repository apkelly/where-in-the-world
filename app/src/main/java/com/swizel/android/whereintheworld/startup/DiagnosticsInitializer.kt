package com.swizel.android.whereintheworld.startup

import android.content.Context
import com.github.apkelly.bolt.startup.BoltInitializer
import com.swizel.android.whereintheworld.BuildConfig
import com.swizel.android.whereintheworld.utils.ConsoleLogger
import com.swizel.android.whereintheworld.utils.Diagnostics
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class DiagnosticsInitializer :
    BoltInitializer,
    KoinComponent {
    override suspend fun create(
        context: Context,
    ) {
        withContext(Dispatchers.Default) {
            ConsoleLogger.d("DiagnosticsInitializer")

            val diagnostics: Diagnostics = get()

            diagnostics.setup(
                context,
                BuildConfig.VERSION_CODE > 1,
            )
        }
    }

    override fun dependencies(): List<Class<out BoltInitializer>> = listOf(KoinInitializer::class.java)
}
