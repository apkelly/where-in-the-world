package com.swizel.android.whereintheworld.startup

import android.content.Context
import com.github.apkelly.bolt.startup.BoltInitializer
import com.swizel.android.whereintheworld.utils.ConsoleLogger
import com.swizel.android.whereintheworld.utils.FeatureFlags
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class FeatureFlagsInitializer :
    BoltInitializer,
    KoinComponent {
    override suspend fun create(
        context: Context,
    ) {
        withContext(Dispatchers.Default) {
            ConsoleLogger.d("FeatureFlagsInitializer")

            val featureFlags: FeatureFlags = get()

            featureFlags.setup()
        }
    }

    override fun dependencies(): List<Class<out BoltInitializer>> = listOf(KoinInitializer::class.java)
}
