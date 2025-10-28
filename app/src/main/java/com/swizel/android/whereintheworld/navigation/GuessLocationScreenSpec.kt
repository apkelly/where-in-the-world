package com.swizel.android.whereintheworld.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import com.swizel.android.whereintheworld.screens.GuessLocationScreen
import com.swizel.android.whereintheworld.theme.LocalWindowSizeClass
import com.swizel.android.whereintheworld.theme.isExpandedWidth
import com.swizel.android.whereintheworld.viewmodels.GuessLocationViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Serializable
internal data object GuessLocationNavKey : NavKey

internal object GuessLocationScreenSpec : ScreenSpec<GuessLocationNavKey>() {

    @Composable
    override fun Content(
        arguments: GuessLocationNavKey,
        navigateTo: (NavKey) -> Unit,
    ) {
        val viewModel: GuessLocationViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        LaunchedEffect(Unit) {
            viewModel.fetchUiState()
        }

        GuessLocationScreen(
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
