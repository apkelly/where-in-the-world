package com.swizel.android.whereintheworld.model

enum class GameDifficulty(
    val value: Int,
    val description: String
) {
    EASY(1, "Easy"),
    MEDIUM(1, "Medium"),
    HARD(3, "Hard"),
    EXTREME(4, "Extreme"),
}
