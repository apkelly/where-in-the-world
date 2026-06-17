package com.swizel.android.whereintheworld.usecases

import com.swizel.android.whereintheworld.repositories.GameSessionRepository
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.test.runTest
import org.junit.Test

class RecordStreetViewTimeUseCaseTest {

    @Test
    fun `forwards duration to repository`() = runTest {
        val repository = mockk<GameSessionRepository> {
            every { recordTimeTakenForCurrentRound(42.seconds) } just runs
        }
        val useCase = RecordStreetViewTimeUseCase(repository)

        useCase(RecordStreetViewTimeUseCase.Params(timeTaken = 42.seconds))

        verify(exactly = 1) { repository.recordTimeTakenForCurrentRound(42.seconds) }
    }
}
