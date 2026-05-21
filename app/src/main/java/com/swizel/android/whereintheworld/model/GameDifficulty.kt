package com.swizel.android.whereintheworld.model

import com.swizel.android.whereintheworld.utils.RemoteConfigKey

enum class GameDifficulty(
    val value: Int,
    val description: String,
    val remoteConfigKey: RemoteConfigKey,
) {
    EASY(1, "Easy", RemoteConfigKey.EASY_CONFIG),
    MEDIUM(2, "Medium", RemoteConfigKey.MEDIUM_CONFIG),
    HARD(3, "Hard", RemoteConfigKey.HARD_CONFIG),
    EXTREME(4, "Extreme", RemoteConfigKey.EXTREME_CONFIG),
}
