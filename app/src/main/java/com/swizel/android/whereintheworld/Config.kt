package com.swizel.android.whereintheworld

import com.swizel.android.whereintheworld.model.GameDifficulty


object Config {

    const val MAP_LOCATION_H_ANCHOR = 0.5f

    const val MAP_LOCATION_V_ANCHOR = 0.875f

    const val MAP_GUESS_H_ANCHOR = 0.16f

    const val MAP_GUESS_V_ANCHOR = 0.81f

    const val MAP_INFO_H_ANCHOR = 0.5f

    const val MAP_INFO_V_ANCHOR = 0.14f

    const val MAPPING_DROPPED_PIN_ANIMATION_SPEED_MS = 750L

    fun getLeaderboardId(difficulty: GameDifficulty): String {
        return when (difficulty) {
            GameDifficulty.EASY -> "CgkI5_Hmq7kIEAIQDg"
            GameDifficulty.MEDIUM -> "CgkI5_Hmq7kIEAIQDw"
            GameDifficulty.HARD -> "CgkI5_Hmq7kIEAIQEA"
            GameDifficulty.EXTREME -> "CgkI5_Hmq7kIEAIQEQ"
        }
    }

    fun getMarcoPoloAchievement(): String {
        return "CgkI5_Hmq7kIEAIQAQ"
    }

    fun getCaptainJamesCookAchievement(): String {
        return "CgkI5_Hmq7kIEAIQAg"
    }

    fun getSirRichardBurtonAchievement(): String {
        return "CgkI5_Hmq7kIEAIQAw"
    }

    fun getPhileasFoggAchievement(): String {
        return "CgkI5_Hmq7kIEAIQBA"
    }

    fun getMichaelPalinAchievement(): String {
        return "CgkI5_Hmq7kIEAIQBQ"
    }

    fun getProclaimersAchievement(): String {
        return "CgkI5_Hmq7kIEAIQEg"
    }

}