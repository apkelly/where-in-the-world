package com.swizel.android.whereintheworld.usecases

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
        }
        val useCase = GetCurrentRoundProgressUseCase(repository)

        val result = useCase()

        assertEquals(10, result.numRounds)
        assertEquals(4, result.currentRound)
    }
}
