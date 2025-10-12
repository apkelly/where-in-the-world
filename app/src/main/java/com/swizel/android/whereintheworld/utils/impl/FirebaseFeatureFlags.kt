package com.swizel.android.whereintheworld.utils.impl

import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import com.swizel.android.whereintheworld.R
import com.swizel.android.whereintheworld.utils.FeatureFlag
import com.swizel.android.whereintheworld.utils.FeatureFlags
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FirebaseFeatureFlags(
    private val workDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : FeatureFlags {
    private lateinit var firebaseRemoteConfig: FirebaseRemoteConfig

    override suspend fun setup() {
        withContext(workDispatcher) {
            firebaseRemoteConfig = Firebase.remoteConfig
            firebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
            firebaseRemoteConfig.fetchAndActivate()
        }
    }

    override fun getBooleanFlag(
        flag: FeatureFlag,
    ): Boolean = firebaseRemoteConfig.getBoolean(flag.configName)

    override fun getStringFlag(
        flag: FeatureFlag,
    ): String = firebaseRemoteConfig.getString(flag.configName)

}