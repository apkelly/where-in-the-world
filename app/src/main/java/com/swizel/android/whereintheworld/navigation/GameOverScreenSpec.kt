package com.swizel.android.whereintheworld.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import com.swizel.android.whereintheworld.screens.GameOverScreen
import com.swizel.android.whereintheworld.theme.LocalWindowSizeClass
import com.swizel.android.whereintheworld.theme.isExpandedWidth
import com.swizel.android.whereintheworld.viewmodels.GameOverViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Serializable
internal data object GameOverNavKey : NavKey

internal object GameOverScreenSpec : ScreenSpec<GameOverNavKey>() {

    @Composable
    override fun Content(
        arguments: GameOverNavKey,
        navigateTo: (NavKey) -> Unit,
    ) {
        val viewModel: GameOverViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        LaunchedEffect(Unit) {
            viewModel.fetchUiState()
        }

        GameOverScreen(
            uiState = uiState,
            isExpandedWidth = LocalWindowSizeClass.current.isExpandedWidth(),
            onAction = { action ->
                viewModel.onAction(action) { route ->
                    navigateTo(route)
                }
            },
        )
    }
}