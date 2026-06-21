package com.swizel.android.whereintheworld.repositories

import com.swizel.android.whereintheworld.model.GameDifficulty
import com.swizel.android.whereintheworld.model.GameState
import com.swizel.android.whereintheworld.model.Hint
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Test

class GameSessionRepositoryTest {

    @Test
    fun `requesting hints preserves reveal order while keeping strongest scoring hint`() {
        val repository = GameSessionRepository(GameState())
        repository.startNewGame(
            gameDifficulty = GameDifficulty.MEDIUM,
            config = JSONObject(
                """
                {
                  "num_rounds": 1,
                  "locations": [
                    {
                      "lat": 51.5007,
                      "lon": -0.1246,
                      "landmark": "Big Ben",
                      "country": "United Kingdom"
                    }
                  ]
                }
                """.trimIndent(),
            ),
        )

        assertEquals(Hint.LANDMARK, repository.requestHintForCurrentRound(Hint.LANDMARK))
        assertEquals(Hint.LANDMARK, repository.requestHintForCurrentRound(Hint.COUNTRY))
        assertEquals(listOf(Hint.LANDMARK, Hint.COUNTRY), repository.currentRevealedHints)
        assertEquals(Hint.LANDMARK, repository.currentHint)
    }
}
