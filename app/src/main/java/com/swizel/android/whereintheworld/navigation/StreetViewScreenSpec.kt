package com.swizel.android.whereintheworld.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import com.swizel.android.whereintheworld.R
import com.swizel.android.whereintheworld.screens.StreetViewScreen
import com.swizel.android.whereintheworld.theme.LocalWindowSizeClass
import com.swizel.android.whereintheworld.theme.isExpandedWidth
import com.swizel.android.whereintheworld.viewmodels.StreetViewViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Serializable
internal data object StreetViewNavKey : NavKey

internal object StreetViewScreenSpec : ScreenSpec<StreetViewNavKey> {

    @Composable
    override fun Content(
        arguments: StreetViewNavKey,
        navigateTo: (NavKey) -> Unit,
    ) {
        val viewModel: StreetViewViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        var showQuitDialog by remember { mutableStateOf(false) }

        LaunchedEffect(arguments) {
            viewModel.fetchUiState()
        }

        BackHandler {
            showQuitDialog = true
        }

        if (showQuitDialog) {
            AlertDialog(
                onDismissRequest = { showQuitDialog = false },
                title = { Text(text = stringResource(R.string.quit_game_title)) },
                text = { Text(text = stringResource(R.string.quit_game_message)) },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showQuitDialog = false
                            navigateTo(WelcomeNavKey)
                        },
                    ) {
                        Text(text = stringResource(R.string.quit_game_confirm))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showQuitDialog = false }) {
                        Text(text = stringResource(R.string.cancel))
                    }
                },
            )
        }

        StreetViewScreen(
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
