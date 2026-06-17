package com.swizel.android.whereintheworld.usecases

import com.swizel.android.whereintheworld.model.GameDifficulty
import com.swizel.android.whereintheworld.model.GameRoundResult
import com.swizel.android.whereintheworld.model.Guess
import com.swizel.android.whereintheworld.model.Hint
import com.swizel.android.whereintheworld.repositories.GameSessionRepository
import com.swizel.android.whereintheworld.utils.DiagnosticEvent
import io.mockk.every
import io.mockk.mockk
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class CompleteGameUseCaseTest {

    @Test
    fun `returns summary and tracks missed rounds and hint usage`() = runTest {
        val diagnostics = RecordingDiagnostics()
        val score = 8_450L
        val roundResults = listOf(
            GameRoundResult(
                round = sampleRound,
                guess = Guess(
                    guessedLatLng = sampleRound.panoramaLatLng,
                    guessTime = 12.seconds,
                    hint = Hint.LANDMARK,
                ),
            ),
            GameRoundResult(
                round = secondSampleRound,
                guess = null,
            ),
        )
        val repository = mockk<GameSessionRepository> {
            every { calculateScore() } returns score
            every { gameRoundResults } returns roundResults
            every { difficulty } returns GameDifficulty.MEDIUM
        }
        val useCase = CompleteGameUseCase(
            diagnostics = diagnostics,
            gameSessionRepository = repository,
        )

        val result = useCase()

        assertEquals(score, result.score)
        assertEquals(roundResults, result.roundResults)
        assertEquals(1, diagnostics.gameEndCount)
        assertEquals(listOf(score), diagnostics.trackedScores)

        val event = diagnostics.events.single()
        assertEquals(DiagnosticEvent.GAME_COMPLETED, event.event)
        assertEquals(GameDifficulty.MEDIUM.name, event.extras["difficulty"])
        assertEquals(score, event.extras["score"])
        assertEquals(2, event.extras["rounds"])
        assertEquals(1, event.extras["missed_rounds"])
        assertEquals(1, event.extras["hints_used"])
    }
}
