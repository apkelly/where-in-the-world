package com.swizel.android.whereintheworld.utils.impl

import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import com.swizel.android.whereintheworld.R
import com.swizel.android.whereintheworld.utils.RemoteConfig
import com.swizel.android.whereintheworld.utils.RemoteConfigKey
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FirebaseRemoteConfig(
    private val workDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : RemoteConfig {
    private lateinit var firebaseRemoteConfig: FirebaseRemoteConfig

    override suspend fun setup() {
        withContext(workDispatcher) {
            firebaseRemoteConfig = Firebase.remoteConfig
            firebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
            firebaseRemoteConfig.fetchAndActivate()
        }
    }

    override fun getBooleanConfig(
        flag: RemoteConfigKey,
    ): Boolean = firebaseRemoteConfig.getBoolean(flag.configName)

    override fun getStringConfig(
        flag: RemoteConfigKey,
    ): String = firebaseRemoteConfig.getString(flag.configName)
}
