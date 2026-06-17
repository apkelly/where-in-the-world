package com.swizel.android.whereintheworld.test

import androidx.activity.compose.LocalActivityResultRegistryOwner
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.ActivityResultRegistryOwner
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.core.app.ActivityOptionsCompat
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import app.cash.paparazzi.detectEnvironment
import com.android.resources.NightMode
import com.android.resources.ScreenOrientation
import com.google.testing.junit.testparameterinjector.TestParameter
import com.swizel.android.whereintheworld.theme.WhereInTheWorldTheme
import com.swizel.android.whereintheworld.utils.ConsoleLogger
import java.util.concurrent.atomic.AtomicInteger
import org.junit.Rule
import org.junit.rules.TestRule
import org.junit.rules.TestWatcher
import org.junit.runner.Description

open class BasePaparazziTest(
    @param:TestParameter val config: Config,
) {

    enum class Config(
        val deviceConfig: DeviceConfig,
    ) {
        PIXEL_6_LIGHT(deviceConfig = DeviceConfig.PIXEL_6.copy(orientation = ScreenOrientation.PORTRAIT, nightMode = NightMode.NOTNIGHT)),
        NEXUS_10_LIGHT(deviceConfig = DeviceConfig.NEXUS_10.copy(orientation = ScreenOrientation.LANDSCAPE, nightMode = NightMode.NOTNIGHT)),
        PIXEL_6_DARK(deviceConfig = DeviceConfig.PIXEL_6.copy(orientation = ScreenOrientation.PORTRAIT, nightMode = NightMode.NIGHT)),
        NEXUS_10_DARK(deviceConfig = DeviceConfig.NEXUS_10.copy(orientation = ScreenOrientation.LANDSCAPE, nightMode = NightMode.NIGHT)),
    }

    companion object {
        private val testNo = AtomicInteger(0)
    }

    @get:Rule
    val watcher: TestRule = object : TestWatcher() {
        override fun starting(
            description: Description,
        ) {
            ConsoleLogger.i("Start Test #${testNo.get()} - ${description.methodName}")
        }

        override fun finished(
            description: Description,
        ) {
            ConsoleLogger.i("End Test #${testNo.getAndIncrement()}\n\n")
        }
    }

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = config.deviceConfig,
        showSystemUi = false,
        theme = "android:Theme.Material.Light.NoActionBar",
        environment = detectEnvironment(),
    )

    fun paparazziSnapshot(
        content: @Composable () -> Unit,
    ) {
        paparazzi.snapshot {
            CompositionLocalProvider(
                LocalInspectionMode provides true,
                // Needed by screens that use `rememberLauncherForActivityResult`
                LocalActivityResultRegistryOwner provides PaparazziActivityResultRegistryOwner(),
            ) {
                WhereInTheWorldTheme {
                    content()
                }
            }
        }
    }

    /**
     * This class fixes issues with capturing screenshots of composables that use `rememberLauncherForActivityResult`
     */
    private class PaparazziActivityResultRegistryOwner : ActivityResultRegistryOwner {
        override val activityResultRegistry: ActivityResultRegistry
            get() = object : ActivityResultRegistry() {
                override fun <I, O> onLaunch(
                    requestCode: Int,
                    contract: ActivityResultContract<I, O>,
                    input: I,
                    options: ActivityOptionsCompat?,
                ) {
                }
            }
    }
}
