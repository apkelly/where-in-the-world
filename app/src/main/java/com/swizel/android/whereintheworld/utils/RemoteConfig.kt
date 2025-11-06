package com.swizel.android.whereintheworld.utils

interface RemoteConfig {
    suspend fun setup()
    fun getBooleanConfig(
        flag: RemoteConfigKey,
    ): Boolean
    fun getStringConfig(
        flag: RemoteConfigKey,
    ): String
}

enum class RemoteConfigKey(
    val configName: String,
) {
    EASY_CONFIG("easy_config"),
    MEDIUM_CONFIG("medium_config"),
    HARD_CONFIG("hard_config"),
    EXTREME_CONFIG("extreme_config"),
}
