package com.swizel.android.whereintheworld.screens

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.tooling.preview.Preview
import com.swizel.android.whereintheworld.composables.BasicScaffold
import com.swizel.android.whereintheworld.composables.LoadingType
import com.swizel.android.whereintheworld.composables.UiState
import com.swizel.android.whereintheworld.viewmodels.GameOverViewModel


@Immutable
internal data class GameOverUiState(
    val dummy: String
)

@Composable
internal fun GameOverScreen(
    uiState: UiState<GameOverUiState>,
    isExpandedWidth: Boolean,
    onAction: (GameOverViewModel.Action) -> Unit,
) {
    BasicScaffold(
        uiState = uiState,
    ) { data ->
        Text("Game Over Screen")
    }
}

@Preview
@Composable
private fun GameOverScreenPreview() {
    MaterialTheme {
        GameOverScreen(
            uiState = UiState(
                isLoading = LoadingType.NOT_LOADING,
                data = GameOverUiState(dummy = "")
            ),
            isExpandedWidth = false,
            onAction = { }
        )
    }
}