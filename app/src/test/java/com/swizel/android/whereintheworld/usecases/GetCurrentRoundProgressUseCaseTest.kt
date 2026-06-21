package com.swizel.android.whereintheworld.usecases

import com.swizel.android.whereintheworld.model.GameDifficulty
import com.swizel.android.whereintheworld.model.Hint
import com.swizel.android.whereintheworld.repositories.GameSessionRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetCurrentRoundProgressUseCaseTest {

    @Test
    fun `returns repository progress`() = runTest {
        val repository = mockk<GameSessionRepository> {
            every { numRounds } returns 10
            every { currentRound } returns 4
            every { currentRoundData } returns sampleRound
            every { difficulty } returns GameDifficulty.HARD
            every { currentHint } returns Hint.COUNTRY
            every { currentRevealedHints } returns listOf(Hint.LANDMARK, Hint.COUNTRY)
        }
        val useCase = GetCurrentRoundProgressUseCase(repository)

        val result = useCase()

        assertEquals(10, result.numRounds)
        assertEquals(4, result.currentRound)
        assertEquals(sampleRound.landmark, result.landmark)
        assertEquals(sampleRound.country, result.country)
        assertEquals(GameDifficulty.HARD, result.gameDifficulty)
        assertEquals(Hint.COUNTRY, result.currentHint)
        assertEquals(listOf(Hint.LANDMARK, Hint.COUNTRY), result.revealedHints)
    }
}
