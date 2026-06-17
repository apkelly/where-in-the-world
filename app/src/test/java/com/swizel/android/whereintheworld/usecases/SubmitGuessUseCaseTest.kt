package com.swizel.android.whereintheworld.usecases

import com.google.android.gms.maps.model.LatLng
import com.swizel.android.whereintheworld.model.GameDifficulty
import com.swizel.android.whereintheworld.model.Hint
import com.swizel.android.whereintheworld.repositories.GameSessionRepository
import com.swizel.android.whereintheworld.utils.DiagnosticEvent
import io.mockk.every
import io.mockk.mockk
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SubmitGuessUseCaseTest {

    @Test
    fun `advances to next round and tracks round lifecycle`() = runTest {
        val diagnostics = RecordingDiagnostics()
        val guessedLocation = LatLng(10.0, 20.0)
        val repository = mockk<GameSessionRepository> {
            every { currentRound } returnsMany listOf(0, 1)
            every { currentRoundData } returns sampleRound
            every { currentTimeTaken } returns 15.seconds
            every { currentHint } returns Hint.COUNTRY
            every { difficulty } returns GameDifficulty.EXTREME
            every { submitGuessForCurrentRound(guessedLocation) } returns true
        }
        val useCase = SubmitGuessUseCase(
            diagnostics = diagnostics,
            gameSessionRepository = repository,
        )

        val result = useCase(SubmitGuessUseCase.Params(location = guessedLocation))

        assertEquals(SubmitGuessUseCase.Result.NextRound, result)

        val guessSubmitted = diagnostics.events.first { it.event == DiagnosticEvent.GUESS_SUBMITTED }
        assertEquals(0, guessSubmitted.extras["round"])
        assertEquals(GameDifficulty.EXTREME.name, guessSubmitted.extras["difficulty"])
        assertEquals(true, guessSubmitted.extras["has_guess"])
        assertEquals(15.seconds.inWholeMilliseconds, guessSubmitted.extras["time_taken_ms"])
        assertEquals(Hint.COUNTRY.name, guessSubmitted.extras["hint"])

        val roundCompleted = diagnostics.events.first { it.event == DiagnosticEvent.ROUND_COMPLETED }
        assertEquals(0, roundCompleted.extras["round"])
        assertEquals(GameDifficulty.EXTREME.name, roundCompleted.extras["difficulty"])
        assertEquals(sampleRound.landmark, roundCompleted.extras["landmark"])
        assertEquals(true, roundCompleted.extras["has_guess"])

        val nextRoundStarted = diagnostics.events.first { it.event == DiagnosticEvent.ROUND_STARTED }
        assertEquals(1, nextRoundStarted.extras["round"])
        assertEquals(GameDifficulty.EXTREME.name, nextRoundStarted.extras["difficulty"])
    }

    @Test
    fun `returns game complete on last round without starting another round`() = runTest {
        val diagnostics = RecordingDiagnostics()
        val repository = mockk<GameSessionRepository> {
            every { currentRound } returns 4
            every { currentRoundData } returns sampleRound
            every { currentTimeTaken } returns Duration.ZERO
            every { currentHint } returns Hint.NONE
            every { difficulty } returns GameDifficulty.EASY
            every { submitGuessForCurrentRound(null) } returns false
        }
        val useCase = SubmitGuessUseCase(
            diagnostics = diagnostics,
            gameSessionRepository = repository,
        )

        val result = useCase(SubmitGuessUseCase.Params(location = null))

        assertEquals(SubmitGuessUseCase.Result.GameComplete, result)
        assertTrue(diagnostics.events.any { it.event == DiagnosticEvent.GUESS_SUBMITTED })
        assertTrue(diagnostics.events.any { it.event == DiagnosticEvent.ROUND_COMPLETED })
        assertFalse(diagnostics.events.any { it.event == DiagnosticEvent.ROUND_STARTED })
    }
}
