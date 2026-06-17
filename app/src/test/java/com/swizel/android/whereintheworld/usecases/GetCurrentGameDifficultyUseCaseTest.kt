package com.swizel.android.whereintheworld.usecases

import com.swizel.android.whereintheworld.model.GameDifficulty
import com.swizel.android.whereintheworld.repositories.GameSessionRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetCurrentGameDifficultyUseCaseTest {

    @Test
    fun `returns repository difficulty`() = runTest {
        val repository = mockk<GameSessionRepository> {
            every { difficulty } returns GameDifficulty.EXTREME
        }
        val useCase = GetCurrentGameDifficultyUseCase(repository)

        assertEquals(GameDifficulty.EXTREME, useCase())
    }
}
