package com.swizel.android.whereintheworld.usecases

import com.swizel.android.whereintheworld.model.GameDifficulty
import com.swizel.android.whereintheworld.model.Hint
import com.swizel.android.whereintheworld.repositories.GameSessionRepository
import com.swizel.android.whereintheworld.utils.DiagnosticEvent
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class RequestHintUseCaseTest {

    @Test
    fun `returns scoring hint and tracks hint details`() = runTest {
        val diagnostics = RecordingDiagnostics()
        val repository = mockk<GameSessionRepository> {
            every { currentRound } returns 2
            every { difficulty } returns GameDifficulty.MEDIUM
            every { requestHintForCurrentRound(Hint.COUNTRY) } returns Hint.LANDMARK
        }
        val useCase = RequestHintUseCase(
            diagnostics = diagnostics,
            gameSessionRepository = repository,
        )

        val result = useCase(RequestHintUseCase.Params(Hint.COUNTRY))

        assertEquals(Hint.LANDMARK, result)
        val event = diagnostics.events.single()
        assertEquals(DiagnosticEvent.HINT_REQUESTED, event.event)
        assertEquals(2, event.extras["round"])
        assertEquals(GameDifficulty.MEDIUM.name, event.extras["difficulty"])
        assertEquals(Hint.COUNTRY.name, event.extras["requested_hint"])
        assertEquals(Hint.LANDMARK.name, event.extras["scoring_hint"])
        assertEquals(Hint.LANDMARK.multiplier, event.extras["score_multiplier"])
    }
}
