package com.swizel.android.whereintheworld.utils.impl

import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.firebase.remoteconfig.remoteConfig
import com.swizel.android.whereintheworld.BuildConfig
import com.swizel.android.whereintheworld.R
import com.swizel.android.whereintheworld.utils.ConsoleLogger
import com.swizel.android.whereintheworld.utils.RemoteConfig
import com.swizel.android.whereintheworld.utils.RemoteConfigKey
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FirebaseRemoteConfig(
    private val workDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : RemoteConfig {
    private lateinit var firebaseRemoteConfig: FirebaseRemoteConfig

    override suspend fun setup() {
        withContext(workDispatcher) {
            firebaseRemoteConfig = Firebase.remoteConfig.apply {
                if (BuildConfig.DEBUG) {
                    setConfigSettingsAsync(
                        FirebaseRemoteConfigSettings.Builder()
                            .setMinimumFetchIntervalInSeconds(TimeUnit.MINUTES.toSeconds(10))
                            .build(),
                    )
                }
                setDefaultsAsync(R.xml.remote_config_defaults)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            ConsoleLogger.d("Defaults Succeeded : ${getString("easy_config")}")
                        } else {
                            ConsoleLogger.d("Defaults failed")
                        }
                    }
            }
            firebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        ConsoleLogger.d("Fetch Succeeded : ${firebaseRemoteConfig.getString("easy_config")}")
                    } else {
                        ConsoleLogger.d("Fetch failed")
                    }
                }
        }
    }

    override fun getBooleanConfig(
        flag: RemoteConfigKey,
    ): Boolean = firebaseRemoteConfig.getBoolean(flag.configName)

    override fun getStringConfig(
        flag: RemoteConfigKey,
    ): String = firebaseRemoteConfig.getString(flag.configName)
}
