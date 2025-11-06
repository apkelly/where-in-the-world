package com.swizel.android.whereintheworld.startup

import android.content.Context
import com.github.apkelly.bolt.startup.BoltInitializer
import com.swizel.android.whereintheworld.utils.ConsoleLogger
import com.swizel.android.whereintheworld.utils.RemoteConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class RemoteConfigInitializer :
    BoltInitializer,
    KoinComponent {
    override suspend fun create(
        context: Context,
    ) {
        withContext(Dispatchers.Default) {
            ConsoleLogger.d("RemoteConfigInitializer")

            val remoteConfig: RemoteConfig = get()

            remoteConfig.setup()
        }
    }

    override fun dependencies(): List<Class<out BoltInitializer>> = listOf(KoinInitializer::class.java)
}
