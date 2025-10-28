package com.swizel.android.whereintheworld.utils

interface FeatureFlags {
    suspend fun setup()
    fun getBooleanFlag(
        flag: FeatureFlag,
    ): Boolean
    fun getStringFlag(
        flag: FeatureFlag,
    ): String
}

enum class FeatureFlag(
    val configName: String,
) {
    EASY_CONFIG("easy_config"),
    MEDIUM_CONFIG("medium_config"),
    HARD_CONFIG("hard_config"),
    EXTREME_CONFIG("extreme_config"),
}
