package com.swizel.android.whereintheworld.model

import kotlin.math.roundToLong
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GameStateTest {

    @Test
    fun `calculateRoundScore returns max points for perfect guess with no hint`() {
        assertEquals(5000L, GameState.calculateRoundScore(distanceMeters = 0.0, hint = Hint.NONE))
    }

    @Test
    fun `calculateRoundScore decreases as distance increases`() {
        val closeScore = GameState.calculateRoundScore(distanceMeters = 50_000.0, hint = Hint.NONE)
        val mediumScore = GameState.calculateRoundScore(distanceMeters = 500_000.0, hint = Hint.NONE)
        val farScore = GameState.calculateRoundScore(distanceMeters = 5_000_000.0, hint = Hint.NONE)

        assertTrue(closeScore > mediumScore)
        assertTrue(mediumScore > farScore)
        assertTrue(farScore > 0L)
    }

    @Test
    fun `calculateRoundScore applies gentler hint penalties`() {
        val noHintScore = GameState.calculateRoundScore(distanceMeters = 250_000.0, hint = Hint.NONE)
        val countryHintScore = GameState.calculateRoundScore(distanceMeters = 250_000.0, hint = Hint.COUNTRY)
        val landmarkHintScore = GameState.calculateRoundScore(distanceMeters = 250_000.0, hint = Hint.LANDMARK)

        assertTrue(kotlin.math.abs(countryHintScore - (noHintScore * 0.85).roundToLong()) <= 1L)
        assertTrue(kotlin.math.abs(landmarkHintScore - (noHintScore * 0.65).roundToLong()) <= 1L)
        assertTrue(noHintScore > countryHintScore)
        assertTrue(countryHintScore > landmarkHintScore)
    }
}
