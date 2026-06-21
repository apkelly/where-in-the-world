package com.swizel.android.whereintheworld.usecases

import com.swizel.android.whereintheworld.model.GameDifficulty
import com.swizel.android.whereintheworld.model.Hint
import com.swizel.android.whereintheworld.repositories.GameSessionRepository
import io.mockk.every
import io.mockk.mockk
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetCurrentRoundUseCaseTest {

    @Test
    fun `returns repository backed round details`() = runTest {
        val repository = mockk<GameSessionRepository> {
            every { numRounds } returns 5
            every { currentRound } returns 3
            every { difficulty } returns GameDifficulty.HARD
            every { currentHint } returns Hint.COUNTRY
            every { currentRevealedHints } returns listOf(Hint.LANDMARK, Hint.COUNTRY)
            every { currentRoundData } returns sampleRound
        }
        val useCase = GetCurrentRoundUseCase(repository)

        val result = useCase()

        assertEquals(5, result.numRounds)
        assertEquals(3, result.currentRound)
        assertEquals(50_000.milliseconds, result.timeAllowed)
        assertEquals(sampleRound.panoramaLatLng, result.panoramaLatLng)
        assertEquals(sampleRound.landmark, result.landmark)
        assertEquals(sampleRound.country, result.country)
        assertEquals(GameDifficulty.HARD, result.gameDifficulty)
        assertEquals(Hint.COUNTRY, result.currentHint)
        assertEquals(listOf(Hint.LANDMARK, Hint.COUNTRY), result.revealedHints)
    }
}
